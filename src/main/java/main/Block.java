package main;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kaspar on 16.03.18.
 */
public class Block {

    private List<Transaction> transactions;
    private String nonce;
    private String hash;

    public Block() {
        transactions = new ArrayList<>();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Block block = (Block) o;

        return hash.equals(block.getHash());
    }

    public String getNonce() {
        return nonce;
    }

    public void setNonce(String nonce) {
        this.nonce = nonce;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getHash() {
        return hash;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }
}
