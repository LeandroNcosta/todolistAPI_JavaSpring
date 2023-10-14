package br.com.leandro.todolist.errors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

// informar que essa classe / metodo sempre vai ser chamada quando houver a exception
@ControllerAdvice
public class ExceptionHandlerController {

  // informando o tipo da exception
  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<String> httpMessageNotReadableException(HttpMessageNotReadableException e) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMostSpecificCause().getMessage());
  }
}
