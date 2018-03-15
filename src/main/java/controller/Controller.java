package controller;

import java.io.Writer;

/**
 * Created by kaspar on 3.03.18.
 */
public interface Controller {

    public void doGet(Request request, Response respone);
    public void doPost(Request request, Response response);
}
