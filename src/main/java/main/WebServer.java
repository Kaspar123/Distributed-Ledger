package main;

import com.fasterxml.jackson.databind.ObjectMapper;
import message.MessageHandler;
import util.Utils;

import java.io.*;
import java.net.*;
import java.util.*;

/**
 * Created by kaspar on 10.02.18.
 */
public class WebServer {

    public static int PORT;
    public static String CONFIG;
    public static String BLOCKS;
    private ServerSocket serverSocket;
    private final static long PEER_UPDATE_PERIOD = 60000;
    private boolean running = true;

    private static final MessageHandler messageHandler = new MessageHandler();

    private void start(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        messageHandler.message("server started on port " + port);
        while (running) {
            new Thread(new ClientHandler(serverSocket.accept(), messageHandler)).start();
        }
        serverSocket.close();
        messageHandler.message("server shut down");
    }

    public static void main(String[] args) throws IOException, URISyntaxException {
        if (args.length < 1) {
            messageHandler.error("port is not defined");
            return;
        }

        PORT = Integer.parseInt(args[0]);

        String[] names = {"A", "B", "C", "D", "E", "F", "G", "H", "I"};
        List<Block> blocks = new ArrayList<>();


        CONFIG = "config_" + PORT + ".json";
        BLOCKS = "blocks_" + PORT + ".json";
        Utils.createExternalFile(Utils.CONFIG, CONFIG);
        Utils.createExternalFile(Utils.BLOCKS, BLOCKS);

        if (PORT == 8080) {
            Random rand = new Random();
            for (int i = 0; i < 10; i++) {
                String a = names[rand.nextInt(names.length)];
                String b = names[rand.nextInt(names.length)];
                String data = a + " sends " + b + " " + Math.random() * 100 + " coins.";
                Utils.addBlock(blocks, data);
            }
            Utils.objectToJSON(BLOCKS, blocks);
        }

        Timer t = new Timer();
        TimerTask task = new PeersUpdateTask(messageHandler);
        t.scheduleAtFixedRate(task, 0, PEER_UPDATE_PERIOD);

        main.WebServer server = new main.WebServer();
        server.start(PORT);
    }

}
