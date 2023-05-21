package com.fresh.voc.web;

import java.time.LocalDateTime;

import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MultipartException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
public class GeneralExceptionHandler {

	private ResponseEntity<ApiResult<?>> newResponse(Throwable throwable, HttpStatus status) {
		return new ResponseEntity<>(ApiResult.error(LocalDateTime.now(), throwable), status);
	}

	@ExceptionHandler({
		IllegalStateException.class, IllegalArgumentException.class,
		TypeMismatchException.class, HttpMessageNotReadableException.class,
		MissingServletRequestParameterException.class, MultipartException.class,
	})
	public ResponseEntity<?> handleBadRequestException(Exception e) {
		log.debug("Bad request exception occurred: {}", e.getMessage(), e);
		return newResponse(e, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(HttpMediaTypeException.class)
	public ResponseEntity<?> handleHttpMediaTypeException(Exception e) {
		return newResponse(e, HttpStatus.UNSUPPORTED_MEDIA_TYPE);
	}

	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	public ResponseEntity<?> handleMethodNotAllowedException(Exception e) {
		return newResponse(e, HttpStatus.METHOD_NOT_ALLOWED);
	}

	@ExceptionHandler({Exception.class, RuntimeException.class})
	public final ResponseEntity<ApiResult<?>> handleAllException(Exception e) {
		log.error("Unexpected exception occurred: ${}", e.getMessage(), e);
		return newResponse(e, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
