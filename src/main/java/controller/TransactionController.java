package controller;

import enums.StatusCode;
import main.Block;
import main.Peers;
import main.Transaction;
import main.WebServer;
import util.Utils;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Created by kaspar on 18.03.18.
 */
public class TransactionController implements Controller {

    private static Block currentBlock = new Block();
    private static final int MAX_BLOCK_SIZE = 3;
    private static final String FORMAT = "{\"errcode\": \"%s\", \"errmsg\": \"%s\"}";

    @Override
    public void doGet(Request request, Response response) {
        PrintWriter out = response.getWriter();
        response.setCode(StatusCode.NOT_IMPLEMENTED);
        out.print(response.getVersion() + " ");
        out.println(response.getCode());

    }

    @Override
    public void doPost(Request request, Response response) {
        PrintWriter out = response.getWriter();
        response.setCode(StatusCode.OK);

        final String body = request.getBody();

        Optional<Transaction> tOpt = Utils.JSONStringToObject(body, Transaction.class);
        Transaction transaction = null;
        if (tOpt.isPresent()) transaction = tOpt.get();
        final String content = validateTransaction(transaction);

        if (content.equals("1")) {
            List<Transaction> transactions = currentBlock.getTransactions();
            transactions.add(transaction);
            if (transactions.size() == MAX_BLOCK_SIZE) {
                //TODO: add block to file (Done)!!
                Optional<Block[]> blOpt = Utils.JSONtoObject(WebServer.BLOCKS, Block[].class);
                if (blOpt.isPresent()) {
                    List<Block> blocks = new ArrayList<>(Arrays.asList(blOpt.get()));
                    System.out.println("[BLOCKS] - " + blocks);
                    blocks.add(currentBlock);
                    Utils.objectToJSON(WebServer.BLOCKS, blocks);
                }
                currentBlock = new Block();
            }
            Optional<Peers> peersOpt = Utils.JSONtoObject(WebServer.CONFIG, Peers.class);
            if (peersOpt.isPresent()) {
                List<String> nbs = new ArrayList<>();
                nbs.addAll(peersOpt.get().getDefaults());
                nbs.addAll(peersOpt.get().getPeers());
                nbs.forEach(c -> new Thread(new ForwardTransaction(c, body)).start());
            }
        }

        out.print(response.getVersion() + " ");
        out.println(response.getCode());
        out.println("Content-Type: application/json");
        out.println("Content-Length: " + content.length());
        out.println();

        out.println(content);
    }

    private String validateTransaction(Transaction transaction) {
        if (transaction == null) return String.format(FORMAT, "5", "structure of JSON is invalid");
        if (transaction.getHash().isEmpty()) return String.format(FORMAT, "4", "hash not specified");
        if (transaction.getTo().isEmpty()) return String.format(FORMAT, "3", "to is empty");
        if (transaction.getFrom().isEmpty()) return String.format(FORMAT, "2", "from is empty");
        if (currentBlock.getTransactions().contains(transaction)) return String.format(FORMAT, "0", "transaction already exists");
        Optional<Block[]> blocks = Utils.JSONtoObject(WebServer.BLOCKS, Block[].class);
        if (!blocks.isPresent()) return String.format(FORMAT, "1", "internal error");
        List<Block> blockList = Arrays.asList(blocks.get());
        boolean duplicate = blockList.stream().anyMatch(c -> c.getTransactions().contains(transaction));
        if (duplicate) return String.format(FORMAT, "0", "transaction already exists");
        return "1";
    }


    private class ForwardTransaction implements Runnable {

        private String peer;
        private String transaction;

        public ForwardTransaction(String peer, String transaction) {
            this.peer = peer;
            this.transaction = transaction;
        }

        @Override
        public void run() {
            forwardTransaction(peer, transaction);
        }

        private void forwardTransaction(String peer, String transaction) {
            int peerPort = Integer.parseInt(peer);
            if (WebServer.PORT == peerPort) return;
            HttpURLConnection con = null;
            int status = 404;
            try {
                con = (HttpURLConnection) new URL(WebServer.PEER_URL + peerPort +"/inv").openConnection();
                con.setDoOutput(true);
                con.setRequestMethod("POST");
                con.setRequestProperty("Content-Type", "application/json");
                con.setRequestProperty("Content-Length", String.valueOf(transaction.length()));

                OutputStream os = con.getOutputStream();
                os.write(transaction.getBytes("UTF-8"));
                os.flush();

                status = con.getResponseCode();
                con.disconnect();
            } catch (IOException e) {
                // TODO: INJECT MESSAGEHANDLER HERE!
            }
            if (status == 200) {
                System.out.println("[TRANSACTION] - forwarded to: " + peer);
            } else {
                System.out.println("[TRANSACTION] - was not able to forward to: " + peer);
            }
        }
    }
}
