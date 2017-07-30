package com.grsu.teacherassistant.models;

import com.grsu.teacherassistant.constants.Constants;
import com.grsu.teacherassistant.entities.Student;
import lombok.Data;

import java.time.LocalTime;

/**
 * Created by pavel on 3/4/17.
 */
@Data
public class LessonStudentModel {
	private Integer id;
	private String name;
	private Integer totalSkip;
	private Integer lectureSkip;
	private Integer practicalSkip;
	private Integer labSkip;
	private LocalTime registrationTime;
	private Student student;
	private Double averageAttestation;
	private boolean additional;
	private Integer examMark;
	private Integer totalMark;

	public LessonStudentModel(Student student) {
		this.student = student;
		this.id = student.getId();
		this.name = student.getFullName();
	}

	public String getSkips() {
		String skips = "";
		if (lectureSkip != null || practicalSkip != null || labSkip != null) {
			return (lectureSkip == null ? "0" : lectureSkip) + "/" +
					(practicalSkip == null ? "0" : practicalSkip) + "/" +
					(labSkip == null ? "0" : labSkip);
		}
		return skips;
	}

	public void updateTotal() {
		if (examMark == null || averageAttestation == null) {
			if (examMark == null) {
				totalMark = null;
			} else {
				totalMark = examMark;
			}
		} else {
			totalMark = (int) Math.round(averageAttestation * Constants.MARK_ATTESTATION_WEIGHT + examMark * Constants.MARK_EXAM_WEIGHT);
		}
	}

	public void updateExam() {
		if (totalMark == null) {
			examMark = null;
		} else {
			if (averageAttestation == null) {
				examMark = totalMark;
			} else {
				examMark = (int) Math.round((totalMark - averageAttestation * Constants.MARK_ATTESTATION_WEIGHT) / Constants.MARK_EXAM_WEIGHT);
				if (examMark < 0) {
					examMark = 0;
					updateTotal();
				}
				if (examMark > 10) {
					examMark = 10;
					updateTotal();
				}
			}
		}
	}

}
