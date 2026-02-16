package com.example.UserMs.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.file.AccessDeniedException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptions {

    //Example: AccessDeniedException (403) happens inside the Spring Security filter chain, before the request reaches your controller.
    //wont work -> refer: public class CustomAccessDeniedHandler implements AccessDeniedHandler {
   /* @ExceptionHandler(org.springframework.security.access.AccessDeniedException.class)
    public ResponseEntity<Map<String,Object>> accessDeniedException(AccessDeniedException exception) {
        Map<String, Object> responseError = new HashMap<>();

        Map<String,Object> errorMap = new HashMap<>();
        errorMap.put("Status Code", 403);
        errorMap.put("Error", "Access Failing");

        responseError.put("Access Denied!", errorMap);

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(responseError);
    }*/

    //we can modify the Response Entity for global exceptions, using minimal approach in this method
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<String> authentication(AuthenticationException ex) {
        return ResponseEntity.status(401).body("Login required");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String,Object>> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, Object> response = new HashMap<>();

        Map<String, Object> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.toMap(
                        fieldError -> fieldError.getField(), fieldError -> fieldError.getDefaultMessage()
                ));

        response.put("status", 400);
        response.put("message", "Validation failed");
        response.put("errors", errors);  // example: "email": "must not be blank"

        return ResponseEntity.badRequest().body(response);  //400 Bad Request
    }



}
