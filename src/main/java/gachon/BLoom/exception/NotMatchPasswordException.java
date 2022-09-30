package gachon.BLoom.exception;

public class NotMatchPasswordException extends RuntimeException {
    public NotMatchPasswordException(String message) {
        super(message);
    }
}
