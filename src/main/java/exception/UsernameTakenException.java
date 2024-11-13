package exception;

public class UsernameTakenException extends Exception {
    public UsernameTakenException() {
        super("Username taken");
    }
}
