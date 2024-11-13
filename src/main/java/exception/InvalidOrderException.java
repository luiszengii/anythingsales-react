package exception;

public class InvalidOrderException extends Exception {
    public InvalidOrderException() {
        super("Order value invalid");
    }
}
