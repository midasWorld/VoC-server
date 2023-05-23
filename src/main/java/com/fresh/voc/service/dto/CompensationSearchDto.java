package com.fresh.voc.service.dto;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fresh.voc.model.voc.Compensation;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class CompensationSearchDto {

	private final Long amouont;
	private final VocDetailDto voc;

	public CompensationSearchDto(Compensation compensation) {
		this(compensation, new VocDetailDto(compensation.getVoc()));
	}

	public CompensationSearchDto(Compensation compensation, VocDetailDto voc) {
		this.amouont = compensation.getAmount();
		this.voc = voc;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
			.append("amouont", amouont)
			.append("voc", voc)
			.toString();
	}
}
