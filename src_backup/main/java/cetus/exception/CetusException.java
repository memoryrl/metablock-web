package cetus.exception;

public class CetusException extends RuntimeException {
    /**
     *
     */
    private static final long serialVersionUID = 1767056807422793711L;

    public CetusException() {}

    public CetusException(String message) {
        super(message);
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}