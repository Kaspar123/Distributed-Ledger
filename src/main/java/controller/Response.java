package controller;

import java.io.PrintWriter;

/**
 * Created by kaspar on 3.03.18.
 */
public interface Response {

    public String getVersion();
    public String getCode();
    public PrintWriter getWriter();

    public void setCode(String code);
}
