package controller;

import enums.Method;
import enums.Version;
import message.MessageHandler;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * Created by kaspar on 10.02.18.
 */
public class RequestImpl implements Request {

    private String method;
    private String path;
    private String version;
    private String host; // must if version is 1.1
    private String accept;
    private String userAgent;

    private String query;
    private boolean blankLine = false;
    private MessageHandler messageHandler;

    public RequestImpl(BufferedReader bufferedReader, MessageHandler messageHandler) {
        this.messageHandler = messageHandler;
        try {
            parse(bufferedReader);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getPath() {
        return path;
    }

    @Override
    public String getQueryString() {
        return query;
    }

    @Override
    public Header getHeader() {
        return new Header() {
            public String getMethod() {
                return method;
            }

            public String getVersion() {
                return version;
            }

            public String getHost() {
                return host;
            }

            public String getAccept() {
                return accept;
            }

            public String getUserAgent() {
                return userAgent;
            }
        };
    }

    private boolean parse(BufferedReader header) throws IOException {
        String line = header.readLine();
        messageHandler.message(line);
        String[] firstLine = line.split(" ");
        if (firstLine.length != 3) return false;
        if (!parseMethod(firstLine[0])) return false;
        path = firstLine[1];

        query = "";
        if (path.contains("?")) {
            int index = path.indexOf("?");
            query = path.substring(index + 1);
            path = path.substring(0, index);
        }

        if (method.equals(Method.GET)) {
            messageHandler.message("GET request at path: " + path + ", query string: " + query);
        } else if (method.equals(Method.POST)) {
            messageHandler.message("POST request at path: " + path);
        }

        if (!parseVersion(firstLine[2])) return false;

        while ((line = header.readLine()) != null) {
            if (line.length() == 0) {
                blankLine = true;
                break;
            }
            parseKeyValue(line);
        }

        return blankLine;
    }

    private boolean parseMethod(String met) {
        if ("GET".equals(met)) method = Method.GET;
        else if ("POST".equals(met)) method = Method.POST;
        else if ("HEAD".equals(met)) method = Method.HEAD;
        else if ("DELETE".equals(met)) method = Method.DELETE;
        else if ("PUT".equals(met)) method = Method.PUT;
        else return false;
        return true;
    }

    private boolean parseVersion(String ver) {
        if ("HTTP/1.0".equals(ver)) version = Version.HTTP_1_0;
        else if ("HTTP/1.1".equals(ver)) version  = Version.HTTP_1_1;
        else if ("HTTP/2.0".equals(ver)) version = Version.HTTP_2_0;
        else return false;
        return true;
    }

    private boolean parseKeyValue(String line) {
        String[] splitted = line.split(": ");
        if (splitted.length != 2) return false;
        if ("Host".equals(splitted[0])) host = splitted[1];
        else if ("From".equals(splitted[0])) host = splitted[1];
        else if ("User-Agent".equals(splitted[0])) userAgent = splitted[1];
        else if ("Accept".equals(splitted[0])) accept = splitted[1];
        return false;
    }


}
