package main;

import com.fasterxml.jackson.databind.ObjectMapper;
import message.MessageHandler;
import util.Utils;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by kaspar on 10.02.18.
 */
public class WebServer {

    public static int PORT;
    public static String CONFIG;
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
        CONFIG = "config_" + PORT + ".json";
        Utils.createExternalFile(CONFIG);

        Timer t = new Timer();
        TimerTask task = new PeersUpdateTask(messageHandler);
        t.scheduleAtFixedRate(task, 0, PEER_UPDATE_PERIOD);

        main.WebServer server = new main.WebServer();
        server.start(PORT);
    }

}
