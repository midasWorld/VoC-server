package com.fresh.voc.web;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MultipartException;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
public class GeneralExceptionHandler {

	private ResponseEntity<ApiResult<?>> newResponse(Throwable throwable, HttpStatus status) {
		return new ResponseEntity<>(ApiResult.error(LocalDateTime.now(), throwable), status);
	}

	private ResponseEntity<ApiResult<?>> newResponse(String message, HttpStatus status) {
		return new ResponseEntity<>(ApiResult.error(LocalDateTime.now(), message), status);
	}

	@ExceptionHandler({
		IllegalStateException.class, IllegalArgumentException.class,
		TypeMismatchException.class, HttpMessageNotReadableException.class,
		MissingServletRequestParameterException.class, MultipartException.class,
		MethodArgumentNotValidException.class
	})
	public ResponseEntity<?> handleBadRequestException(Exception e) {
		if (e instanceof HttpMessageNotReadableException
				&& e.getCause() instanceof InvalidFormatException
				&& ((InvalidFormatException)e.getCause()).getTargetType().isEnum())
		{
			Optional<String> message = getErrorMessage((InvalidFormatException)e.getCause());
			if (message.isPresent()) {
				return newResponse(message.get(), HttpStatus.BAD_REQUEST);
			}
		}
		if (e instanceof MethodArgumentNotValidException) {
			Optional<String> message = getErrorMessage((MethodArgumentNotValidException)e);
			if (message.isPresent()) {
				return newResponse(message.get(), HttpStatus.BAD_REQUEST);
			}
		}

		log.debug("Bad request exception occurred: {}", e.getMessage(), e);
		return newResponse(e, HttpStatus.BAD_REQUEST);
	}

	private Optional<String> getErrorMessage(InvalidFormatException e) {
		if (e == null) {
			return Optional.empty();
		}
	
		String field = e.getPath().get(0).getFieldName();
		String wrongValue = e.getValue().toString();

		String result = new StringBuilder()
			.append("Invalid ").append(field).append(" : ")
			.append(wrongValue).toString();

		return Optional.of(result);
	}

	private Optional<String> getErrorMessage(MethodArgumentNotValidException e) {
		BindingResult bindingResult = e.getBindingResult();
		FieldError fieldError = bindingResult.getFieldError();
		if (fieldError == null) {
			return Optional.empty();
		}

		StringBuilder builder = new StringBuilder();
		builder.append(fieldError.getField()).append(" : ");
		builder.append(fieldError.getDefaultMessage());

		return Optional.of(builder.toString());
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
