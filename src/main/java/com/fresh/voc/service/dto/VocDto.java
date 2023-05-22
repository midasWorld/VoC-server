package com.fresh.voc.service.dto;

import org.apache.commons.lang3.builder.ToStringBuilder;

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
	private final String penaltyContent;
	private final Boolean confirmed;
	private final Boolean objected;
	private final CompensationDto compensation;

	public VocDto(Voc voc) {
		this.id = voc.getId();
		this.dueType = voc.getDueType();
		this.dueTargetName = voc.getDueTarget().getName();
		this.dueReason = voc.getDueReason();

		Penalty panalty = voc.getPenalty();
		this.penaltyContent = panalty.getContent();
		this.confirmed = panalty.getConfirmed();
		this.objected = panalty.getObjected();

		this.compensation = new CompensationDto(voc.getCompensation());
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
