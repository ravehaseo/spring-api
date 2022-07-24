package com.maybank.sample.constants;

public enum EmploymentStatus {

	PRESENT(1, "Present"), LEFT(2, "Left");

	private final int id;
	private final String value;

	EmploymentStatus(final int id, final String value) {
		this.id = id;
		this.value = value;
	}

	public int getEmploymentStatusId() {
		return id;
	}

	public String getEmploymentStatusIdInStringFormat() {
		return Integer.toString(id);
	}

	public String getValue() {
		return value;
	}

	public static EmploymentStatus getStatus(final String value) {
		for (EmploymentStatus a : values()) {
			if (a.value.equalsIgnoreCase(value)) {
				return a;
			}
		}
		return null;
	}

	public static EmploymentStatus getStatusById(final int id) {
		for (EmploymentStatus a : values()) {
			if (a.id == id) {
				return a;
			}
		}
		return null;
	}

}
