package com.fresh.voc.service.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fresh.voc.model.voc.DueType;

import lombok.Getter;

@Getter
public class VocCreateRequest {

	@NotNull
	private final DueType dueType;

	@NotNull
	private final Long dueTargetId;

	@NotBlank
	private final String dueReason;

	public VocCreateRequest(DueType dueType, Long dueTargetId, String dueReason) {
		this.dueType = dueType;
		this.dueTargetId = dueTargetId;
		this.dueReason = dueReason;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
			.append("dueType", dueType)
			.append("dueTargetId", dueTargetId)
			.append("dueReason", dueReason)
			.toString();
	}
}
