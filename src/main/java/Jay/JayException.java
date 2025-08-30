package Jay;

public class JayException extends Exception {
    public JayException(String message) {
        super(message);
    }

    @Override
    public String toString() {
        return getMessage();
    }
}