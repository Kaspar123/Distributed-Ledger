package main;

import com.fasterxml.jackson.databind.ObjectMapper;
import message.MessageHandler;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

import static main.WebServer.CONFIG;
import static main.WebServer.PORT;

/**
 * Created by kaspar on 16.03.18.
 */
public class PeersUpdateTask extends TimerTask {

    private final MessageHandler handler;
    private final static String PEER_URL = "http://localhost:";

    public PeersUpdateTask(MessageHandler handler) {
        this.handler = handler;
    }

    private void update() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Peers p = mapper.readValue(new File(CONFIG), Peers.class);

        List<String> allPeers = new ArrayList<>();
        allPeers.addAll(p.getDefaults());
        allPeers.addAll(p.getPeers());


        for (String peer : allPeers) {
            int peerPort = Integer.parseInt(peer);
            if (PORT != peerPort) {
                HttpURLConnection con = (HttpURLConnection) new URL(PEER_URL + peerPort +"/addr?source=" + PORT).openConnection();
                con.setRequestMethod("GET");
                int status = 404;
                try {
                    status = con.getResponseCode();
                } catch (ConnectException e) {
                    handler.error("peer does not exist at " + peerPort);
                }
                if (status == 200) {
                    Peers nbPeers = mapper.readValue((InputStream) con.getContent(), Peers.class);
                    p.union(nbPeers);
                    handler.message("unioning peer list with one got from " + peerPort);
                }
                con.disconnect();
            }
        }

        mapper.writeValue(new File(CONFIG), p);
    }

    @Override
    public void run() {
        try {
            update();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
