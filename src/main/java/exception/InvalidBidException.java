package exception;

public class InvalidBidException extends Exception {
    public InvalidBidException() {
        super("Bid value invalid");
    }
}
