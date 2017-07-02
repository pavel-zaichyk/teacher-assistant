package com.grsu.teacherassistant.entities;

import com.grsu.teacherassistant.converters.db.LocalTimeAttributeConverter;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalTime;
import java.util.List;

/**
 * Created by pavel on 2/10/17.
 */
@Entity
@Table(name = "STUDENT_CLASS")
public class StudentClass implements AssistantEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	@Basic
	@Column(name = "registered")
	private Boolean registered;

	@Basic
	@Convert(converter = LocalTimeAttributeConverter.class)
	@Column(name = "registration_time")
	private LocalTime registrationTime;

	@Basic
	@Column(name = "registration_type")
	private String registrationType;

	@Basic
	@Column(name = "mark")
	private String mark;

	@NotFound(action= NotFoundAction.IGNORE)
	@ManyToOne
	@JoinColumn(name = "student_id", referencedColumnName = "id")
	private Student student;

	@ManyToOne
	@JoinColumn(name = "class_id", referencedColumnName = "id")
	private Class clazz;


	@Basic
	@Column(name = "student_id", insertable = false, updatable = false)
	private Integer studentId;

	@Basic
	@Column(name = "class_id", insertable = false, updatable = false)
	private Integer classId;

	@OneToMany(fetch = FetchType.EAGER)
	@JoinColumn(name = "entity_id", referencedColumnName = "id")
	@Where(clause = "type = 'STUDENT_CLASS'")
	private List<Note> notes;

	/* GETTERS & SETTERS */
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Boolean getRegistered() {
		return Boolean.TRUE.equals(registered);
	}

	public void setRegistered(Boolean registered) {
		this.registered = registered;
	}

	public LocalTime getRegistrationTime() {
		return registrationTime;
	}

	public void setRegistrationTime(LocalTime registrationTime) {
		this.registrationTime = registrationTime;
	}

	public String getRegistrationType() {
		return registrationType;
	}

	public void setRegistrationType(String registrationType) {
		this.registrationType = registrationType;
	}

	public String getMark() {
		return mark;
	}

	public void setMark(String mark) {
		this.mark = mark;
	}

	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}

	public Class getClazz() {
		return clazz;
	}

	public void setClazz(Class clazz) {
		this.clazz = clazz;
	}


	public Integer getStudentId() {
		return studentId;
	}

	public void setStudentId(Integer studentId) {
		this.studentId = studentId;
	}


	public Integer getClassId() {
		return classId;
	}

	public void setClassId(Integer classId) {
		this.classId = classId;
	}

	public List<Note> getNotes() {
		return notes;
	}

	public void setNotes(List<Note> notes) {
		this.notes = notes;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		StudentClass that = (StudentClass) o;

		if (id != null ? !id.equals(that.id) : that.id != null) return false;
		if (registered != null ? !registered.equals(that.registered) : that.registered != null) return false;
		if (registrationTime != null ? !registrationTime.equals(that.registrationTime) : that.registrationTime != null)
			return false;
		if (registrationType != null ? !registrationType.equals(that.registrationType) : that.registrationType != null)
			return false;
		if (mark != null ? !mark.equals(that.mark) : that.mark != null) return false;

		return true;
	}

	@Override
	public int hashCode() {
		int result = id != null ? id.hashCode() : 0;
		result = 31 * result + (registered != null ? registered.hashCode() : 0);
		result = 31 * result + (registrationTime != null ? registrationTime.hashCode() : 0);
		result = 31 * result + (registrationType != null ? registrationType.hashCode() : 0);
		result = 31 * result + (mark != null ? mark.hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return "StudentClass{" +
				"id=" + id +
				", registered=" + registered +
				", registrationTime='" + registrationTime + '\'' +
				", registrationType='" + registrationType + '\'' +
				", mark='" + mark + '\'' +
				'}';
	}

}
