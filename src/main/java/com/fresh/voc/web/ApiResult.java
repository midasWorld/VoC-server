package com.fresh.voc.web;

import java.time.LocalDateTime;

import org.apache.commons.lang3.builder.ToStringBuilder;

import lombok.Getter;

@Getter
public class ApiResult<T> {

	private final boolean success;
	private final T response;
	private final ApiError error;

	public ApiResult(boolean success, T response, ApiError error) {
		this.success = success;
		this.response = response;
		this.error = error;
	}

	public static <T> ApiResult<T> ok(T response) {
		return new ApiResult<>(true, response, null);
	}

	public static ApiResult<?> error(LocalDateTime timestamp, Throwable throwable) {
		return new ApiResult<>(false, null, new ApiError(timestamp, throwable));
	}

	public static ApiResult<?> error(LocalDateTime timestamp, String message) {
		return new ApiResult<>(false, null, new ApiError(timestamp, message));
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
			.append("success", success)
			.append("response", response)
			.append("error", error)
			.toString();
	}
}
