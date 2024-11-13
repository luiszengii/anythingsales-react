package exception;

public class InvalidPasswordException extends Exception {
    public InvalidPasswordException() {
        super("Password incorrect");
    }
}
