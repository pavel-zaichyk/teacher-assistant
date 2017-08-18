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

/**
 * @author Pavel Zaychick
 */
@Entity
@Table(name = "[GROUP]")
public class Group implements AssistantEntity {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	@Basic
	@Column(name = "name")
	private String name;

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
			joinColumns = @JoinColumn(name = "group_id", referencedColumnName = "id"),
			inverseJoinColumns = @JoinColumn(name = "stream_id", referencedColumnName = "id"))
	private List<Stream> streams;

	@ManyToMany
	@LazyCollection(LazyCollectionOption.FALSE)
	@JoinTable(name = "STUDENT_GROUP",
			joinColumns = @JoinColumn(name = "group_id", referencedColumnName = "id"),
			inverseJoinColumns = @JoinColumn(name = "student_id", referencedColumnName = "id"))
	private List<Student> students;

	@NotFound(action= NotFoundAction.IGNORE)
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "department_id", referencedColumnName = "id")
	private Department department;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "type_id", referencedColumnName = "id")
	private GroupType type;

	@OneToMany(mappedBy = "group", fetch = FetchType.EAGER)
	private List<Lesson> lessons;

	public Group() {
	}

	public Group(Group group){
		this.name = group.name;
		this.active = group.active;
		this.expirationDate = group.expirationDate;
		this.streams = group.streams;
		this.students = group.students;
		this.department = group.department;
		this.type = group.type;
		this.lessons = group.lessons;
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

	public List<Stream> getStreams() {
		return streams;
	}

	public void setStreams(List<Stream> streams) {
		this.streams = streams;
	}

	public List<Student> getStudents() {
		return students;
	}

	public void setStudents(List<Student> students) {
		this.students = students;
	}

	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

	public GroupType getType() {
		return type;
	}

	public void setType(GroupType type) {
		this.type = type;
	}

	public List<Lesson> getLessons() {
		return lessons;
	}

	public void setLessons(List<Lesson> lessons) {
		this.lessons = lessons;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Group group = (Group) o;

		if (id != null ? !id.equals(group.id) : group.id != null) return false;
		if (name != null ? !name.equals(group.name) : group.name != null) return false;
		if (active != null ? !active.equals(group.active) : group.active != null) return false;
		if (expirationDate != null ? !expirationDate.equals(group.expirationDate) : group.expirationDate != null)
			return false;

		return true;
	}

	@Override
	public int hashCode() {
		int result = id != null ? id.hashCode() : 0;
		result = 31 * result + (name != null ? name.hashCode() : 0);
		result = 31 * result + (active != null ? active.hashCode() : 0);
		result = 31 * result + (expirationDate != null ? expirationDate.hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return "Group{" +
				"id=" + id +
				", name='" + name + '\'' +
				", active=" + active +
				", expirationDate='" + expirationDate + '\'' +
				'}';
	}
}
