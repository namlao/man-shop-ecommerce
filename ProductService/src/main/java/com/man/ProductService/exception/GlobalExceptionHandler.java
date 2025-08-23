package com.man.ProductService.exception;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler(ProductNotFoundException.class)
	public ResponseEntity<?> handlerProductNotFound(ProductNotFoundException exception){
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
				"error", 404,
				"message",exception.getMessage()
				));
	}
}
