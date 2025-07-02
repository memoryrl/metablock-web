package cetus.exception;

import cetus.Response;

public class MoveToBackException extends ResponseException {

    private static final long serialVersionUID = 6256014704022891439L;

    public MoveToBackException() {
    }

    public MoveToBackException(Response r) {
        super(r);
    }

}