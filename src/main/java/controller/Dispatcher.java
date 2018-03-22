package controller;


/**
 * Created by kaspar on 3.03.18.
 */
public final class Dispatcher {

    public static Controller dispatch(Request request) {
        final String route = request.getPath();
        if (route == null) return new HelloController();
        if (route.equalsIgnoreCase("/first")) return new HelloController();
        if (route.equalsIgnoreCase("/addr")) return new PeerListController();
        if (route.equalsIgnoreCase("/getblocks")) return new BlockController(route);
        if (route.equalsIgnoreCase("/getdata")) return new BlockController(route);
        if (route.equalsIgnoreCase("/block")) return new BlockController(route);
        if (route.equalsIgnoreCase("/inv")) return new TransactionController();
        return new HelloController();
    }

}
