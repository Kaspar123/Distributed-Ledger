package controller;

import enums.StatusCode;
import main.Peers;
import main.WebServer;
import util.Utils;

import java.io.File;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.Optional;

/**
 * Created by kaspar on 8.03.18.
 */
public class PeerListController implements Controller {

    @Override
    public void doGet(Request request, Response respone) {
        PrintWriter out = respone.getWriter();
        respone.setCode(StatusCode.OK);

        String content = Utils.convert(new File(WebServer.CONFIG));
        Map<String, String> params = Utils.splitQuery(request.getQueryString());
        String requesterAddress = params.get("source");

        out.println(respone.getVersion() + " " + respone.getCode());
        out.println("Content-Type: application/json");
        out.println("Content-Length: " + content.length());
        out.println();
        out.println(content);

        Optional<Peers> pOpt = Utils.JSONtoObject(WebServer.CONFIG, Peers.class);
        if (!pOpt.isPresent() || pOpt.get().hasPeer(requesterAddress)) return;
        Peers p = pOpt.get();
        p.addPeer(requesterAddress);
        Utils.objectToJSON(WebServer.CONFIG, p);

    }

    @Override
    public void doPost(Request request, Response response) {

    }
}
