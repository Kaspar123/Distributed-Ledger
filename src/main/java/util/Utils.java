package util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import main.Block;
import main.Transaction;

import java.io.*;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Created by kaspar on 4.03.18.
 */
public class Utils {

    public static final String CONFIG = "config.json";
    public static final String BLOCKS = "blocks.json";

    public static InputStream getFile(String fileName) {
        return Utils.class.getResourceAsStream("/" + fileName);
    }

    public static void createExternalFile(String internalFileName, String filename) throws IOException {
        Files.copy(getFile(internalFileName), new File(filename).toPath(), StandardCopyOption.REPLACE_EXISTING);
    }

    public static <T> Optional<T> JSONtoObject(String externalFileName, Class<T> c) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return Optional.ofNullable(mapper.readValue(new File(externalFileName), c));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public static String hash256(String data) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            return bytesToHex(md.digest(data.getBytes()));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return data;
    }

    public static String bytesToHex(byte[] bytes) {
        StringBuilder builder = new StringBuilder();
        for (byte byt : bytes) builder.append(Integer.toString((byt & 0xff) + 0x100, 16).substring(1));
        return builder.toString();
    }


    public static void addBlock(List<Block> blocks, String data) {
        Block newBlock = new Block();
        if (blocks.isEmpty()) {
            newBlock.setHash("");
        } else {
            //Block prev = blocks.get(blocks.size() - 1);
            newBlock.getTransactions().add(new Transaction(data, data));
            newBlock.setHash(hash256(data));
            newBlock.setNonce(hash256(data).substring(10, 16));
        }
        blocks.add(newBlock);
    }

    public static <T> Optional<T> JSONtoObject(InputStream in, Class<T> c) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return Optional.ofNullable(mapper.readValue(in, c));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public static void objectToJSON(String externalFileName, Object o) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(new File(externalFileName), o);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static String objectToJSONString(Object o, boolean beautify) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return beautify ? mapper.writerWithDefaultPrettyPrinter().writeValueAsString(o) : mapper.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static <T> Optional<T> JSONStringToObject(String json, Class<T> c) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return Optional.ofNullable(mapper.readValue(json, c));
        } catch (IOException e) {
            Optional.empty();
        }
        return Optional.empty();
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
            if (idx == -1) continue;
            try {
                query_pairs.put(URLDecoder.decode(pair.substring(0, idx), "UTF-8"), URLDecoder.decode(pair.substring(idx + 1), "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                return query_pairs;
            }
        }
        return query_pairs;
    }
}
