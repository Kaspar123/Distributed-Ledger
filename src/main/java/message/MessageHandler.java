package message;

/**
 * Created by kaspar on 11.02.18.
 */
public class MessageHandler {

    public synchronized void message(String message) {
        System.out.println("[OK] - " + message);
    }

    public synchronized void error(String error) {
        System.out.println("[ERROR] - " + error);
    }
}
