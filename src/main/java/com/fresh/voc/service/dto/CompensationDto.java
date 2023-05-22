package com.fresh.voc.service.dto;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fresh.voc.model.voc.Compensation;

import lombok.Getter;

@Getter
public class CompensationDto {
	private final Long amount;

	public CompensationDto(Compensation compensation) {
		this.amount = compensation.getAmount();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
			.append("amount", amount)
			.toString();
	}
}
