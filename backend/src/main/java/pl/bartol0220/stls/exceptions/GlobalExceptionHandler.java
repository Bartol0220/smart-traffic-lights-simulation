package pl.bartol0220.stls.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;
import pl.bartol0220.stls.dto.ErrorDto;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDto> handleGeneralException(Exception e) {
        ErrorDto error = new ErrorDto("Internal server error: " + e.getMessage());
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ErrorDto> handle404(NoHandlerFoundException e) {
        ErrorDto error = new ErrorDto("Adress not found: " + e.getRequestURL());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }
}
