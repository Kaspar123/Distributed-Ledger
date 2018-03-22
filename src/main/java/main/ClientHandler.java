package main;

import controller.*;
import enums.Method;
import enums.StatusCode;
import message.MessageHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by kaspar on 10.02.18.
 */
public class ClientHandler implements Runnable {

    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;
    private MessageHandler messageHandler;

    public ClientHandler(Socket socket, MessageHandler messageHandler) {
        clientSocket = socket;
        this.messageHandler = messageHandler;
    }

    public void run() {
        try {
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            messageHandler.message(clientSocket.getInetAddress() + " connected");
        } catch (IOException e) {
            e.printStackTrace();
            messageHandler.error(clientSocket.getLocalAddress() + " failed to connect");
            return;
        }
        process();
        try {
            in.close();
            out.close();
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void process() {
        Request request = new RequestImpl(in, messageHandler);
        Response response = new ResponseImpl(request.getHeader().getVersion(), StatusCode.NOT_FOUND, out);

        if (!request.isRequestOK()) return;
        Controller controller = Dispatcher.dispatch(request);
        if (request.getHeader().getMethod().equals(Method.GET)) {
            controller.doGet(request, response);
        } else if (request.getHeader().getMethod().equals(Method.POST)) {
            controller.doPost(request, response);
        }
    }
}
