package controller;

import java.util.Map;

/**
 * Created by kaspar on 3.03.18.
 */
public interface Header {

    public String getMethod();
    public String getVersion();
    public Map<String, String> getValues();
}
