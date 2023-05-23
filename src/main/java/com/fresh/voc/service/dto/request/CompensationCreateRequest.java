package com.fresh.voc.service.dto.request;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.builder.ToStringBuilder;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class CompensationCreateRequest {

	@DecimalMin("0")
	private final Long amount;

	@NotNull
	private final Long vocId;

	public CompensationCreateRequest(Long amount, Long vocId) {
		this.amount = amount;
		this.vocId = vocId;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
			.append("amount", amount)
			.append("vocId", vocId)
			.toString();
	}
}
