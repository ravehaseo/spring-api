package com.maybank.sample.constants;

public enum ResponseStatus {

	SUCCESS("Success"), FAILED("Failed");

	private final String value;

	ResponseStatus(final String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public static ResponseStatus getStatus(final String value) {
		for (ResponseStatus a : values()) {
			if (a.value.equalsIgnoreCase(value)) {
				return a;
			}
		}
		return null;
	}

}
