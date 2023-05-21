package com.fresh.voc.web;

import java.time.LocalDateTime;

import org.apache.commons.lang3.builder.ToStringBuilder;

import lombok.Getter;

@Getter
public class ApiError {

	private final LocalDateTime timestamp;
	private final String message;

	public ApiError(LocalDateTime timestamp, Throwable throwable) {
		this(timestamp, throwable.getMessage());
	}

	public ApiError(LocalDateTime timestamp, String message) {
		this.timestamp = timestamp;
		this.message = message;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
			.append("timestamp", timestamp)
			.append("message", message)
			.toString();
	}
}
