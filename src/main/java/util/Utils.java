package util;

import java.io.*;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by kaspar on 4.03.18.
 */
public class Utils {

    private static final String CONFIG = "config.json";

    public static InputStream getFile(String fileName) {
        return Utils.class.getResourceAsStream("/" + fileName);
    }

    public static void createExternalFile(String filename) throws IOException {
        Files.copy(getFile(CONFIG), new File(filename).toPath(), StandardCopyOption.REPLACE_EXISTING);
    }

    public static String convert(File file) {
        StringBuilder builder = new StringBuilder();
        String line;
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            while ((line = bufferedReader.readLine()) !=  null) {
                builder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return builder.toString();
    }

    public static Map<String, String> splitQuery(String query) {
        Map<String, String> query_pairs = new HashMap<>();
        String[] pairs = query.split("&");
        for (String pair : pairs) {
            int idx = pair.indexOf("=");
            try {
                query_pairs.put(URLDecoder.decode(pair.substring(0, idx), "UTF-8"), URLDecoder.decode(pair.substring(idx + 1), "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                return query_pairs;
            }
        }
        return query_pairs;
    }
}
