package main;

import java.util.Date;

/**
 * Created by kaspar on 16.03.18.
 */
public class Block {

    private String hash;
    private String previousHash;
    private String data;
    private long timeStamp;

    public Block(String data) {
        this.data = data;
        timeStamp = new Date().getTime();
    }

    public String getHash() {
        hash = String.valueOf(Math.random());
        return hash;
    }

    public String getPreviousHash() {
        previousHash = String.valueOf(Math.random());
        return previousHash;
    }

    public String getData() {
        return data;
    }

    public long getTimeStamp() {
        return timeStamp;
    }
}
