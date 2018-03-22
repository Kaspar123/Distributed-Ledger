package controller;

import message.MessageHandler;

import java.io.Writer;

/**
 * Created by kaspar on 3.03.18.
 */
public interface Controller {

    public void doGet(Request request, Response response);
    public void doPost(Request request, Response response);

}
