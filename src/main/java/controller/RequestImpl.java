package controller;

import enums.Method;
import enums.Version;
import message.MessageHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by kaspar on 10.02.18.
 */
public class RequestImpl implements Request {

    private String method;
    private String path;
    private String version;
    private String body;
    private Map<String, String> values = new HashMap<>();

    private boolean requestOK = false;

    private String query;
    private boolean blankLine = false;
    private MessageHandler messageHandler;

    public RequestImpl(BufferedReader bufferedReader, MessageHandler messageHandler) {
        this.messageHandler = messageHandler;
        try {
            requestOK = parse(bufferedReader);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean isRequestOK() {
        return requestOK;
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
    public String getBody() {
        return body;
    }

    @Override
    public Header getHeader() {
        return new Header() {
            @Override
            public String getMethod() {
                return method;
            }

            @Override
            public String getVersion() {
                return version;
            }

            @Override
            public Map<String, String> getValues() {
                return values;
            }
        };
    }

    private boolean parse(BufferedReader header) throws IOException {
        String line = header.readLine();
        if (line == null) return false;
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

        if (!parseVersion(firstLine[2])) return false;

        while ((line = header.readLine()) != null) {
            if (line.length() == 0) {
                blankLine = true;
                break;
            }
            parseKeyValue(line);
        }

        if (method.equals(Method.GET)) {
            messageHandler.message("GET request at path: " + path + ", query string: " + query);
        } else if (method.equals(Method.POST)) {
            int length = Integer.valueOf(values.getOrDefault("Content-Length", "0"));
            body = "";
            if (length > 0) {
                char[] buffer = new char[length];
                int capacity = header.read(buffer, 0, length);
                StringBuilder data = new StringBuilder(capacity);
                data.append(buffer, 0, capacity);
                body = data.toString();
            }
            messageHandler.message("POST request at path: " + path + ", body: " + body);
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
        if (splitted[0].isEmpty() || splitted[1].isEmpty()) return false;
        values.put(splitted[0], splitted[1]);
        return true;
    }


}
