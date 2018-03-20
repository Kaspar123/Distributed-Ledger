package main;

/**
 * Created by kaspar on 18.03.18.
 */
public class Transaction {

    private String data;

    public Transaction(String data) {
        this.data = data;
    }

    public Transaction() { }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
