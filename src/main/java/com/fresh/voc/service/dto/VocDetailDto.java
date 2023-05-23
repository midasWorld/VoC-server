package com.fresh.voc.service.dto;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fresh.voc.model.common.Person;
import com.fresh.voc.model.voc.DueType;
import com.fresh.voc.model.voc.Penalty;
import com.fresh.voc.model.voc.Voc;

import lombok.Getter;

@Getter
public class VocDetailDto {
	private final Long id;
	private final DueType dueType;
	private final String dueReason;
	private String dueTargetName;
	private String penaltyContent;
	private Boolean confirmed;
	private Boolean objected;

	public VocDetailDto(Voc voc) {
		this(voc, voc.getDueTarget(), voc.getPenalty());
	}

	public VocDetailDto(Voc voc, Person person, Penalty penalty) {
		this.id = voc.getId();
		this.dueType = voc.getDueType();
		this.dueReason = voc.getDueReason();
		if (person != null) {
			this.dueTargetName = person.getName();
		}
		if (penalty != null) {
			this.penaltyContent = penalty.getContent();
			this.confirmed = penalty.getConfirmed();
			this.objected = penalty.getObjected();
		}
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
			.append("id", id)
			.append("dueType", dueType)
			.append("dueReason", dueReason)
			.append("dueTargetName", dueTargetName)
			.append("penaltyContent", penaltyContent)
			.append("confirmed", confirmed)
			.append("objected", objected)
			.toString();
	}
}
