package exception;

public class ConcurrencyException extends Exception {
    public ConcurrencyException() {
        super("Object changed by another user");
    }
}
