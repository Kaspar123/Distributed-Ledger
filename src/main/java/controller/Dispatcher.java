package controller;

import message.MessageHandler;


/**
 * Created by kaspar on 3.03.18.
 */
public final class Dispatcher {

    public static Controller dispatch(Request request) {
        final String route = request.getPath();
        if (route.equalsIgnoreCase("/first")) return new HelloController();
        if (route.equalsIgnoreCase("/addr")) return new PeerListController();
        if (route.equalsIgnoreCase("/getblocks")) return new BlockController(route);
        if (route.equalsIgnoreCase("/getdata")) return new BlockController(route);
        return new HelloController();
    }

}
