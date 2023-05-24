package com.fresh.voc.service.dto.request;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;

import org.apache.commons.lang3.builder.ToStringBuilder;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class PenaltyCreateRequest {

	@NotBlank
	private final String content;

	@DecimalMin("0")
	private final Long amount;

	public PenaltyCreateRequest(String content, Long amount) {
		this.content = content;
		this.amount = amount;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
			.append("content", content)
			.append("amount", amount)
			.toString();
	}
}
