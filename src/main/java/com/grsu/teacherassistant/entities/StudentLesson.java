package com.grsu.teacherassistant.entities;

import com.grsu.teacherassistant.converters.db.LocalTimeAttributeConverter;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalTime;
import java.util.List;

/**
 * @author Pavel Zaychick
 */
@Entity
@Table(name = "STUDENT_LESSON")
@Getter @Setter
public class StudentLesson implements AssistantEntity {
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

	@NotFound(action= NotFoundAction.IGNORE)
	@ManyToOne
	@JoinColumn(name = "lesson_id", referencedColumnName = "id")
	private Lesson lesson;

	@Basic
	@Column(name = "student_id", insertable = false, updatable = false)
	private Integer studentId;

	@Basic
	@Column(name = "lesson_id", insertable = false, updatable = false)
	private Integer lessonId;

	@OneToMany(fetch = FetchType.EAGER)
	@JoinColumn(name = "entity_id", referencedColumnName = "id")
	@Where(clause = "type = 'STUDENT_LESSON'")
	private List<Note> notes;

	public boolean isRegistered() {
		return Boolean.TRUE.equals(registered);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		StudentLesson that = (StudentLesson) o;

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
		return "StudentLesson{" +
				"id=" + id +
				", registered=" + registered +
				", registrationTime='" + registrationTime + '\'' +
				", registrationType='" + registrationType + '\'' +
				", mark='" + mark + '\'' +
				'}';
	}

}
