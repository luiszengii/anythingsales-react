package exception;

public class NotFoundException extends Exception {
    public NotFoundException() {
        super("Object not found");
    }
}
