package com.fresh.voc.model.voc;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonValue;

import lombok.Getter;

@Getter
public enum DueType {
	CLIENT("고객사"), SHIPPING("운송사");

	@JsonValue
	private final String name;

	DueType(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
			.append("name", name)
			.toString();
	}
}
