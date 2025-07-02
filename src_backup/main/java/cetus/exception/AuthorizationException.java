package cetus.exception;

import org.springframework.security.core.AuthenticationException;

public class AuthorizationException extends AuthenticationException  {
    public AuthorizationException(String msg) {
        super(msg);
    }

    /**
     * Constructs a <code>BadCredentialsException</code> with the specified message and
     * root cause.
     * @param msg the detail message
     * @param cause root cause
     */
    public AuthorizationException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
