package controller;

import message.MessageHandler;


/**
 * Created by kaspar on 3.03.18.
 */
public final class Dispatcher {

    public static Controller dispatch(Request request) {
        if (request.getPath().equalsIgnoreCase("/first")) return new HelloController();
        if (request.getPath().equalsIgnoreCase("/addr")) return new PeerListController();
        if (request.getPath().equalsIgnoreCase("/getblocks")) return new BlockController();
        return new HelloController();
    }

}
