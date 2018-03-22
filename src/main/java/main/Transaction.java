package main;

/**
 * Created by kaspar on 18.03.18.
 */

public class Transaction {

    private String from;
    private String to;
    private String hash;

    public Transaction(String from, String to) {
        this.from = from;
        this.to = to;
    }

    public Transaction() {}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Transaction that = (Transaction) o;

        return hash != null ? hash.equals(that.hash) : that.hash == null;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }
}
