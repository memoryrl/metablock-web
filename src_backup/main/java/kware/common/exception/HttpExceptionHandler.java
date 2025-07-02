package kware.common.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class HttpExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ModelAndView handleNotFoundException(ResourceNotFoundException ex) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("error/404");
        return modelAndView;
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public Object handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex, HttpServletRequest request) {
        String accept = request.getHeader("Accept");

        if(accept != null && accept.contains("application/json")) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        } else {
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.setViewName("error/404");
            return modelAndView;
        }
    }
}
