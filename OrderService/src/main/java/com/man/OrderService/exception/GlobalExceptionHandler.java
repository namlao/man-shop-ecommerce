package com.man.OrderService.exception;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
	@ExceptionHandler(OrderNotFoundException.class)
	public ResponseEntity<?> handlerProductNotFound(OrderNotFoundException exception){
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
				"error", 404,
				"message",exception.getMessage()
				));
	}
}

