package com.grsu.teacherassistant.models;

/**
 * @author Pavel Zaychick
 */
public enum LessonType {
	LECTURE(1, "lecture"),
	PRACTICAL(2, "practical"),
	LAB(3, "lab"),
	ATTESTATION(4, "attestation"),
	EXAM(5, "exam");

	private final int code;
	private final String key;

	LessonType(int code, String key) {
		this.code = code;
		this.key = key;
	}

	public int getCode() {
		return code;
	}

	public String getKey() {
		return key;
	}

	public static LessonType getLessonTypeByCode(int code) {
		for (LessonType type : values()) {
			if (type.code == code) {
				return type;
			}
		}
		return null;
	}
}
