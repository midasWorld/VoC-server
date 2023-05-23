package com.fresh.voc.service.dto;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fresh.voc.model.voc.Penalty;

import lombok.Getter;

@Getter
public class PenaltyDto {

	private final Long id;
	private final String content;
	private final Long amount;
	private final Boolean confirmed;
	private final Boolean objected;

	public PenaltyDto(Penalty penalty) {
		this.id = penalty.getId();
		this.content = penalty.getContent();
		this.amount = penalty.getAmount();
		this.confirmed = penalty.getConfirmed();
		this.objected = penalty.getObjected();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
			.append("id", id)
			.append("content", content)
			.append("amount", amount)
			.append("confirmed", confirmed)
			.append("objected", objected)
			.toString();
	}
}
