package main;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by kaspar on 9.03.18.
 */
public class Peers {

    private List<String> defaults;
    private List<String> peers;

    public Peers() {
        defaults = new ArrayList<>();
        peers = new ArrayList<>();
    }

    public void addPeer(String peer) {
        peers.add(peer);
    }

    public List<String> getDefaults() {
        return defaults;
    }

    public List<String> getPeers() {
        return peers;
    }

    public void union(Peers otherPeers) {
        Set<String> result = new HashSet<>();
        result.addAll(peers);
        result.addAll(otherPeers.getPeers());
        peers = new ArrayList<>(result);
    }
}
