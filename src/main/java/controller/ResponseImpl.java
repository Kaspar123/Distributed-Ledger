package controller;

import java.io.PrintWriter;

/**
 * Created by kaspar on 3.03.18.
 */
public class ResponseImpl implements Response {
    private String version;
    private String code;
    private PrintWriter writer;

    public ResponseImpl(String version, String code, PrintWriter writer) {
        this.version = version;
        this.code = code;
        this.writer = writer;
    }

    public String getVersion() {
        return version;
    }

    public String getCode() {
        return code;
    }

    public PrintWriter getWriter() {
        return writer;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
