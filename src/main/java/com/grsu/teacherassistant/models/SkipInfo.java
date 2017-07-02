package com.grsu.teacherassistant.models;

/**
 * Created by zaychick-pavel on 2/22/17.
 */
public class SkipInfo {
	private Integer studentId;
	private LessonType lessonType;
	private int count;

	public SkipInfo(Integer studentId, Integer lessonType, int count) {
		this.studentId = studentId;
		this.lessonType = LessonType.getLessonTypeByCode(lessonType);
		this.count = count;
	}

	public Integer getStudentId() {
		return studentId;
	}

	public void setStudentId(Integer studentId) {
		this.studentId = studentId;
	}

	public LessonType getLessonType() {
		return lessonType;
	}

	public void setLessonType(LessonType lessonType) {
		this.lessonType = lessonType;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}
}
