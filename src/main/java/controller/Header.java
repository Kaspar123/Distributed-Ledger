package controller;

/**
 * Created by kaspar on 3.03.18.
 */
public interface Header {

    public String getMethod();
    public String getVersion();
    public String getHost();
    public String getAccept();
    public String getUserAgent();
}
