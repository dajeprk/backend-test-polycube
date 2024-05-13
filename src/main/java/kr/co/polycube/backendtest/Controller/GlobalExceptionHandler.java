package kr.co.polycube.backendtest.Controller;

import kr.co.polycube.backendtest.Exceptions.SpecialCharacterException;
import kr.co.polycube.backendtest.Exceptions.UserAlreadyExistsException;
import kr.co.polycube.backendtest.Exceptions.UserDoesNotExistException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(UserDoesNotExistException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleUserDoesNotExistException (
            UserDoesNotExistException exception
    ) {
        return Map.of("reason", exception.getMessage());
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Map<String, String> handleUserAlreadyExistsException (
            UserAlreadyExistsException exception
    ) {
        // 절대로 일어날 수 없지만 일단 넣어놉니다
        return Map.of("reason", "UUID Conflict. Please try again.");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleMethodArgumentNotValidException (
            MethodArgumentNotValidException exception
    ) {
        Map<String, String> errors = new HashMap<>();
        // 실제사유는 error의 필드와 message로 간주합니다
        exception.getBindingResult().getAllErrors().forEach(
                error -> {
                    String msg = error.getDefaultMessage();
                    errors.put("reason", msg);
                }
        );
        return errors;
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleHttpMessageNotReadableException (
            HttpMessageNotReadableException exception
    ) {
        return Map.of("reason", "Invalid request body: " + exception.getMessage());
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleResourceNotFoundException (
            NoHandlerFoundException exception
    ) {
        return Map.of(
                "reason",
                "The requested URL " + exception.getRequestURL() + " was not found"
        );
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public Map<String, String> handleHttpRequestMethodNotSupportedException (
            HttpRequestMethodNotSupportedException exception
    ) {
        return Map.of(
                "reason",
                "The requested HTTP method " + exception.getMethod() + " is not supported"
        );
    }

    // Catch가 안되고 Internal Server 500를 던지고 있습니다
    // FilterConfig 또는 DispatcherServlet을 더 공부해야될것 같습니다.
    /*@ExceptionHandler(SpecialCharacterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleSpecialCharacterException (
            SpecialCharacterException exception
    ) {
        return Map.of("reason", exception.getMessage());
    }*/
}
