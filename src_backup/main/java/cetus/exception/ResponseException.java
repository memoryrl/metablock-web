package cetus.exception;

import lombok.Getter;
import cetus.Response;

@Getter
public class ResponseException extends CetusException {

    /**
     *
     */
    private static final long serialVersionUID = -165159758143852797L;

    public static final ResponseException UN_AUTHORIZED = new ResponseException("권한이 없습니다.");

    private Response response;

    public ResponseException(String message) {
        super(message);
        response = Response.error(message);
    }

    public ResponseException(Response r) {
        super(r.getMessage());
        this.response = r;
    }

    public ResponseException(int code) {
        response = Response.error(code);
    }

    public ResponseException() {
        this(-1);
    }

}