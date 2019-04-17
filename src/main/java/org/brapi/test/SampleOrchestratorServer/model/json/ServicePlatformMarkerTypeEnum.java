package org.brapi.test.SampleOrchestratorServer.model.json;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum ServicePlatformMarkerTypeEnum {

	FIXED("FIXED"),

	DISCOVERABLE("DISCOVERABLE");

	private String value;

	ServicePlatformMarkerTypeEnum(String value) {
		this.value = value;
	}

	@Override
	@JsonValue
	public String toString() {
		return String.valueOf(value);
	}

	@JsonCreator
	public static ServicePlatformMarkerTypeEnum fromValue(String text) {
		for (ServicePlatformMarkerTypeEnum b : ServicePlatformMarkerTypeEnum.values()) {
			if (String.valueOf(b.value).equals(text)) {
				return b;
			}
		}
		return null;
	}
}
