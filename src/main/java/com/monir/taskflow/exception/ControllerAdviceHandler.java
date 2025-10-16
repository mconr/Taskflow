package com.monir.taskflow.exception;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


import java.util.HashMap;
import java.util.Map;


@ControllerAdvice
public class ControllerAdviceHandler {
@ExceptionHandler(ResourceNotFoundException.class)
public ResponseEntity<?> handleNotFound(ResourceNotFoundException ex) {
Map<String, String> resp = new HashMap<>();
resp.put("error", ex.getMessage());
return ResponseEntity.status(HttpStatus.NOT_FOUND).body(resp);
}


@ExceptionHandler(MethodArgumentNotValidException.class)
public ResponseEntity<?> handleValidation(MethodArgumentNotValidException ex) {
Map<String, String> errors = new HashMap<>();
ex.getBindingResult().getFieldErrors().forEach(err -> errors.put(err.getField(), err.getDefaultMessage()));
return ResponseEntity.badRequest().body(errors);
}


@ExceptionHandler(Exception.class)
public ResponseEntity<?> handleOther(Exception ex) {
ex.printStackTrace();
Map<String, String> resp = new HashMap<>();
resp.put("error", "Internal server error");
return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(resp);
}
}