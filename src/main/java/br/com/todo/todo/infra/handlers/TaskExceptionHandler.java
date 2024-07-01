package br.com.todo.todo.infra.handlers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import br.com.todo.todo.dto.ErrorMessageDTO;
import br.com.todo.todo.exceptions.NotFoudException;

@ControllerAdvice
public class TaskExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(NotFoudException.class)
    public ResponseEntity<ErrorMessageDTO> notFoudExecption(NotFoudException exception) {
        ErrorMessageDTO errorDTO = new ErrorMessageDTO(HttpStatus.NOT_FOUND, exception.getMessage(),
                exception.getDetails());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDTO);

    }
}