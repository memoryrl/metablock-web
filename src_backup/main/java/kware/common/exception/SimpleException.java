package kware.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class SimpleException extends RuntimeException {

    private final HttpStatus status;

    public SimpleException() {
        this("내부 서버에 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public SimpleException(String message) {
        this(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public SimpleException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public SimpleException(HttpStatus status) {
        this.status = status;
    }
}
