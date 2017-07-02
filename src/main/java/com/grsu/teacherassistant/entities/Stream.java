package com.grsu.teacherassistant.entities;

import com.grsu.teacherassistant.converters.db.LocalDateTimeAttributeConverter;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.faces.bean.ManagedBean;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.grsu.teacherassistant.constants.Constants.GROUPS_DELIMITER;

/**
 * Created by zaychick-pavel on 2/9/17.
 */
@Entity
@ManagedBean(name = "newInstanceOfStream")
public class Stream implements AssistantEntity {
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
	@Column(name = "course")
	private Integer course;

	@Basic
	@Column(name = "active")
	private Boolean active;

	@Basic
	@Convert(converter = LocalDateTimeAttributeConverter.class)
	@Column(name = "expiration_date")
	private LocalDateTime expirationDate;

	@ManyToMany
	@LazyCollection(LazyCollectionOption.FALSE)
	@JoinTable(name = "STREAM_GROUP",
			joinColumns = @JoinColumn(name = "stream_id", referencedColumnName = "id"),
			inverseJoinColumns = @JoinColumn(name = "group_id", referencedColumnName = "id"))
	private List<Group> groups;

	@OneToMany(mappedBy = "stream", fetch = FetchType.EAGER)
	private List<Lesson> lessons;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "discipline_id", referencedColumnName = "id")
	private Discipline discipline;

	@NotFound(action= NotFoundAction.IGNORE)
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "department_id", referencedColumnName = "id")
	private Department department;

	public Stream() {
	}

	public Stream(Stream stream) {
		this.name = stream.name;
		this.description = stream.description;
		this.createDate = stream.createDate;
		this.course = stream.course;
		this.active = stream.active;
		this.expirationDate = stream.expirationDate;
		this.groups = stream.groups;
		this.lessons = stream.lessons;
		this.discipline = stream.discipline;
		this.department = stream.department;
	}

	public String getGroupNames() {
		return groups.stream().map(Group::getName).collect(Collectors.joining(GROUPS_DELIMITER));
	}

	/* GETTERS & SETTERS */
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

	public Integer getCourse() {
		return course;
	}

	public void setCourse(Integer course) {
		this.course = course;
	}

	public Boolean isActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public LocalDateTime getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(LocalDateTime expirationDate) {
		this.expirationDate = expirationDate;
	}

	public List<Group> getGroups() {
		return groups;
	}

	public void setGroups(List<Group> groups) {
		this.groups = groups;
	}

	public List<Lesson> getLessons() {
		return lessons;
	}

	public void setLessons(List<Lesson> lessons) {
		this.lessons = lessons;
	}

	public Discipline getDiscipline() {
		return discipline;
	}

	public void setDiscipline(Discipline discipline) {
		this.discipline = discipline;
	}

	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Stream stream = (Stream) o;

		if (id != null ? !id.equals(stream.id) : stream.id != null) return false;
		if (name != null ? !name.equals(stream.name) : stream.name != null) return false;
		if (description != null ? !description.equals(stream.description) : stream.description != null) return false;
		if (createDate != null ? !createDate.equals(stream.createDate) : stream.createDate != null) return false;
		if (course != null ? !course.equals(stream.course) : stream.course != null) return false;
		if (active != null ? !active.equals(stream.active) : stream.active != null) return false;
		if (expirationDate != null ? !expirationDate.equals(stream.expirationDate) : stream.expirationDate != null)
			return false;

		return true;
	}

	@Override
	public int hashCode() {
		int result = id != null ? id.hashCode() : 0;
		result = 31 * result + (name != null ? name.hashCode() : 0);
		result = 31 * result + (description != null ? description.hashCode() : 0);
		result = 31 * result + (createDate != null ? createDate.hashCode() : 0);
		result = 31 * result + (course != null ? course.hashCode() : 0);
		result = 31 * result + (active != null ? active.hashCode() : 0);
		result = 31 * result + (expirationDate != null ? expirationDate.hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return "Stream{" +
				"id=" + id +
				", name='" + name + '\'' +
				", description='" + description + '\'' +
				", createDate='" + createDate + '\'' +
				", course=" + course +
				", active=" + active +
				", expirationDate='" + expirationDate + '\'' +
				'}';
	}
}
