package exception;

public class InsertException extends Exception {
    public InsertException() {
        super("Failed to create object");
    }
}
