package controller;

import enums.StatusCode;
import main.WebServer;
import util.Utils;

import java.io.File;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.util.Map;

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

        System.out.println(params.get("source"));

        out.println(respone.getVersion() + " " + respone.getCode());
        out.println("Content-Type: application/json");
        out.println("Content-Length: " + content.length());
        out.println();
        out.println(content);

    }

    @Override
    public void doPost(Request request, Response response) {

    }
}
