package controller;

import enums.StatusCode;

import java.io.PrintWriter;

/**
 * Created by kaspar on 3.03.18.
 */
public class HelloController implements Controller {

    public static final String HELLO = "<html><body><h1>Hello Controller!</h1></body></html>";

    public void doGet(Request request, Response response) {
        PrintWriter out = response.getWriter();
        response.setCode(StatusCode.OK);

        out.print(response.getVersion() + " ");
        out.println(response.getCode());
        out.println("Content-Type: text/html");
        out.println("Content-Length: " + HELLO.length());
        out.println();
        out.println(HELLO);
    }

    public void doPost(Request request, Response response) {
        doGet(request, response);
    }
}
