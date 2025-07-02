package kware.common.exception;

import cetus.Response;
import cetus.exception.CetusValidException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.*;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
@RequiredArgsConstructor
public class RestExceptionHandler {

    private final MessageSourceAccessor messageSource;

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({MethodArgumentNotValidException.class, BindException.class})
    public ResponseEntity requestBodyhandleValidException(MethodArgumentNotValidException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST.value())
                .body(getValidErrors(ex.getBindingResult()));
    }

    @ExceptionHandler(SimpleException.class)
    public ResponseEntity simpleException(SimpleException e) {
        return ResponseEntity
                .status(e.getStatus())
                .body(e.getMessage());
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity handleNoSuchElementException(NoSuchElementException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(messageSource.getMessage("http.error.empty"));
    }


    //bad
    private Map<String, String> getValidErrors(BindingResult bindingResult) {
        return bindingResult.getFieldErrors()
                .stream()
                .collect(Collectors.toMap(
                        FieldError::getField, // Key is the field name
                        FieldError::getDefaultMessage, // Value is the default error message
                        (existingMessage, newMessage) -> existingMessage + "<br>" + newMessage // Merge function for duplicate keys
                ));
    }
}
