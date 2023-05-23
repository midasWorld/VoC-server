package com.fresh.voc.service.dto;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fresh.voc.model.voc.DueType;
import com.fresh.voc.model.voc.Voc;

import lombok.Getter;

@Getter
public class VocDto {
	private final Long id;
	private final DueType dueType;
	private final String dueReason;

	public VocDto(Voc voc) {
		this.id = voc.getId();
		this.dueType = voc.getDueType();
		this.dueReason = voc.getDueReason();
	}

	public Voc toEntity() {
		return new Voc(id, dueType, dueReason);
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
			.append("id", id)
			.append("dueType", dueType)
			.append("dueReason", dueReason)
			.toString();
	}
}
