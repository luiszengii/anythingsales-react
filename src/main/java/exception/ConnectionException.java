package exception;

public class ConnectionException extends Exception {
    public ConnectionException() {
        super("Failed to complete transaction");
    }
}
