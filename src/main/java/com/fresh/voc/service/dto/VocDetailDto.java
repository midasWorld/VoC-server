package com.fresh.voc.service.dto;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fresh.voc.model.common.Person;
import com.fresh.voc.model.voc.DueType;
import com.fresh.voc.model.voc.Voc;

import lombok.Getter;

@Getter
public class VocDetailDto {
	private final Long id;
	private final DueType dueType;
	private final String dueReason;
	private String dueTargetName;

	public VocDetailDto(Voc voc) {
		this(voc, voc.getDueTarget());
	}

	public VocDetailDto(Voc voc, Person person) {
		this.id = voc.getId();
		this.dueType = voc.getDueType();
		this.dueReason = voc.getDueReason();
		if (person != null) {
			this.dueTargetName = person.getName();
		}
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
			.append("id", id)
			.append("dueType", dueType)
			.append("dueReason", dueReason)
			.append("dueTargetName", dueTargetName)
			.toString();
	}
}
