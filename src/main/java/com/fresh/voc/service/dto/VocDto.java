package com.fresh.voc.service.dto;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fresh.voc.model.voc.Compensation;
import com.fresh.voc.model.voc.DueType;
import com.fresh.voc.model.voc.Penalty;
import com.fresh.voc.model.voc.Voc;

import lombok.Getter;

@Getter
public class VocDto {
	private final Long id;
	private final DueType dueType;
	private final String dueTargetName;
	private final String dueReason;
	private String penaltyContent;
	private Boolean confirmed;
	private Boolean objected;
	private CompensationDto compensation;

	public VocDto(Voc voc) {
		this(voc, voc.getPenalty(), voc.getCompensation());
	}

	public VocDto(Voc voc, Penalty penalty, Compensation compensation) {
		this.id = voc.getId();
		this.dueType = voc.getDueType();
		this.dueTargetName = voc.getDueTarget().getName();
		this.dueReason = voc.getDueReason();
		if (penalty != null) {
			this.penaltyContent = penalty.getContent();
			this.confirmed = penalty.getConfirmed();
			this.objected = penalty.getObjected();
		}
		if (compensation != null) {
			this.compensation = new CompensationDto(compensation);
		}
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
			.append("id", id)
			.append("dueType", dueType)
			.append("dueTargetName", dueTargetName)
			.append("dueReason", dueReason)
			.append("penaltyContent", penaltyContent)
			.append("confirmed", confirmed)
			.append("objected", objected)
			.append("compensation", compensation)
			.toString();
	}
}
