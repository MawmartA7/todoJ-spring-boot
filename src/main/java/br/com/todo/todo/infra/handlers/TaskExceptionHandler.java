package br.com.todo.todo.infra.handlers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import br.com.todo.todo.dto.errors.DefaultErrorMessageDTO;
import br.com.todo.todo.dto.errors.ValidationErrorMessageDTO;
import br.com.todo.todo.exceptions.NotFoundException;

@ControllerAdvice
public class TaskExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<DefaultErrorMessageDTO> handleNotFoundException(NotFoundException exception) {
        DefaultErrorMessageDTO errorDTO = new DefaultErrorMessageDTO(HttpStatus.NOT_FOUND.value(),
                exception.getMessage(),
                exception.getDetails());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDTO);

    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<DefaultErrorMessageDTO> handleIllegalArgumentException(IllegalArgumentException exception) {
        DefaultErrorMessageDTO errorDTO = new DefaultErrorMessageDTO(HttpStatus.BAD_REQUEST.value(),
                exception.getMessage(),
                "To carry out a partial task update, at least one task field must be supplied");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDTO);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(@NonNull MethodArgumentNotValidException ex,
            @NonNull HttpHeaders headers, @NonNull HttpStatusCode status, @NonNull WebRequest request) {

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {

            String fieldName = ((FieldError) error).getField();
            String message = error.getDefaultMessage();
            errors.put(fieldName, message);
        });
        ValidationErrorMessageDTO errorMessage = new ValidationErrorMessageDTO(HttpStatus.BAD_REQUEST.value(),
                "Validation errors", errors);

        return new ResponseEntity<Object>(errorMessage, HttpStatus.BAD_REQUEST);
    }
}