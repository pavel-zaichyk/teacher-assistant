package com.grsu.teacherassistant.entities;

import com.grsu.teacherassistant.converters.db.LessonTypeAttributeConverter;
import com.grsu.teacherassistant.converters.db.LocalDateTimeAttributeConverter;
import com.grsu.teacherassistant.models.LessonType;
import com.grsu.teacherassistant.utils.EntityUtils;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.*;
import org.hibernate.annotations.CascadeType;

import javax.faces.bean.ManagedBean;
import javax.persistence.*;
import javax.persistence.Entity;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by zaychick-pavel on 2/9/17.
 */
@Entity
@ManagedBean(name = "newInstanceOfLesson")
@Getter
@Setter
public class Lesson implements AssistantEntity {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	@Basic
	@Column(name = "name")
	private String name;

	@Basic
	@Column(name = "description")
	private String description;

	@Basic
	@Convert(converter = LocalDateTimeAttributeConverter.class)
	@Column(name = "create_date")
	private LocalDateTime createDate;

	@Basic
	@Convert(converter = LocalDateTimeAttributeConverter.class)
	@Column(name = "date")
	private LocalDateTime date;

	@NotFound(action= NotFoundAction.IGNORE)
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "stream_id", referencedColumnName = "id")
	private Stream stream;

	@Convert(converter = LessonTypeAttributeConverter.class)
	@Column(name = "type_id")
	private LessonType type;

	@NotFound(action= NotFoundAction.IGNORE)
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "group_id", referencedColumnName = "id")
	private Group group;

	@Cascade(CascadeType.DELETE)
	@OneToMany(fetch = FetchType.EAGER)
	@JoinColumn(name = "entity_id", referencedColumnName = "id")
	@Where(clause = "type = 'LESSON'")
	private List<Note> notes;

	@ManyToOne
	@JoinColumn(name = "schedule_id", referencedColumnName = "id")
	private Schedule schedule;

	@MapKey(name = "studentId")
	@OneToMany(mappedBy = "lesson", fetch = FetchType.EAGER)
	@Cascade(org.hibernate.annotations.CascadeType.DELETE)
	private Map<Integer, StudentLesson> studentLessons;

	public Lesson() {
	}

	public Lesson(Lesson lesson) {
		this.id = lesson.id;
		this.name = lesson.name;
		this.description = lesson.description;
		this.createDate = lesson.createDate;
		this.date = lesson.date;
		this.stream = lesson.stream;
		this.type = lesson.type;
		this.group = lesson.group;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Lesson lesson = (Lesson) o;

		if (id != null ? !id.equals(lesson.id) : lesson.id != null) return false;
		if (name != null ? !name.equals(lesson.name) : lesson.name != null) return false;
		if (description != null ? !description.equals(lesson.description) : lesson.description != null) return false;
		if (createDate != null ? !createDate.equals(lesson.createDate) : lesson.createDate != null) return false;
		if (date != null ? !date.equals(lesson.date) : lesson.date != null) return false;
		if (!EntityUtils.compareEntity(stream, lesson.stream)) return false;
		if (!EntityUtils.compareEntity(group, lesson.group)) return false;

		return true;
	}

	@Override
	public int hashCode() {
		int result = id != null ? id.hashCode() : 0;
		result = 31 * result + (name != null ? name.hashCode() : 0);
		result = 31 * result + (description != null ? description.hashCode() : 0);
		result = 31 * result + (createDate != null ? createDate.hashCode() : 0);
		result = 31 * result + (date != null ? date.hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return "Lesson{" +
				"id=" + id +
				", name='" + name + '\'' +
				", description='" + description + '\'' +
				", createDate='" + createDate + '\'' +
				", date='" + date + '\'' +
				'}';
	}
}
