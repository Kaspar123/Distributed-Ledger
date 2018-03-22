package controller;

import message.MessageHandler;

import java.util.Map;

/**
 * Created by kaspar on 3.03.18.
 */
public interface Request {

    public Header getHeader();
    public String getPath();
    public String getQueryString();
    public String getBody();
    public boolean isRequestOK();
}
