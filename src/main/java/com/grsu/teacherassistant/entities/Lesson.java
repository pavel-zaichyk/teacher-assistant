package com.grsu.teacherassistant.entities;

import com.grsu.teacherassistant.converters.db.LessonTypeAttributeConverter;
import com.grsu.teacherassistant.converters.db.LocalDateTimeAttributeConverter;
import com.grsu.teacherassistant.models.LessonType;
import com.grsu.teacherassistant.utils.EntityUtils;
import org.hibernate.annotations.*;
import org.hibernate.annotations.CascadeType;

import javax.faces.bean.ManagedBean;
import javax.persistence.*;
import javax.persistence.Entity;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

/**
 * Created by zaychick-pavel on 2/9/17.
 */
@Entity
@ManagedBean(name = "newInstanceOfLesson")
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

	@OneToMany(mappedBy = "lesson", fetch = FetchType.EAGER)
	@Cascade(CascadeType.DELETE)
	private Set<Class> classes;

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

	public Lesson() {
	}

	public Lesson(Lesson lesson) {
		this.id = lesson.id;
		this.name = lesson.name;
		this.description = lesson.description;
		this.createDate = lesson.createDate;
		this.classes = lesson.classes;
		this.stream = lesson.stream;
		this.type = lesson.type;
		this.group = lesson.group;
	}

	/* GETTERS & SETTERS */
	public Class getClazz() {
		if (classes != null && classes.size() > 0) {
			return (Class) classes.toArray()[0];
		}
		return null;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public LocalDateTime getCreateDate() {
		return createDate;
	}

	public void setCreateDate(LocalDateTime createDate) {
		this.createDate = createDate;
	}

	public Set<Class> getClasses() {
		return classes;
	}

	public void setClasses(Set<Class> classes) {
		this.classes = classes;
	}

	public Stream getStream() {
		return stream;
	}

	public void setStream(Stream stream) {
		this.stream = stream;
	}

	public LessonType getType() {
		return type;
	}

	public void setType(LessonType type) {
		this.type = type;
	}

	public Group getGroup() {
		return group;
	}

	public void setGroup(Group group) {
		this.group = group;
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

		Lesson lesson = (Lesson) o;

		if (id != null ? !id.equals(lesson.id) : lesson.id != null) return false;
		if (name != null ? !name.equals(lesson.name) : lesson.name != null) return false;
		if (description != null ? !description.equals(lesson.description) : lesson.description != null) return false;
		if (createDate != null ? !createDate.equals(lesson.createDate) : lesson.createDate != null) return false;
		if (!EntityUtils.compareEntityLists(classes, lesson.classes)) return false;
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
		return result;
	}

	@Override
	public String toString() {
		return "Lesson{" +
				"id=" + id +
				", name='" + name + '\'' +
				", description='" + description + '\'' +
				", createDate='" + createDate + '\'' +
				'}';
	}
}
