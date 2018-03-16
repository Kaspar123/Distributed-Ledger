package controller;

import enums.StatusCode;
import main.WebServer;
import util.Utils;

import java.io.File;
import java.io.PrintWriter;

/**
 * Created by kaspar on 16.03.18.
 */
public class BlockController implements Controller {

    @Override
    public void doGet(Request request, Response response) {
        PrintWriter out = response.getWriter();
        response.setCode(StatusCode.OK);

        String content = Utils.convert(new File(WebServer.BLOCKS));

        out.print(response.getVersion() + " ");
        out.println(response.getCode());
        out.println("Content-Type: application/json");
        out.println("Content-Length: " + content.length());
        out.println();

        out.println(content);

    }

    @Override
    public void doPost(Request request, Response response) {

    }
}
