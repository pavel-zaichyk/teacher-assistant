package com.grsu.teacherassistant.entities;

import javax.faces.bean.ManagedBean;
import javax.persistence.*;
import java.util.List;

/**
 * Created by zaychick-pavel on 2/9/17.
 */
@Entity
@ManagedBean(name = "newInstanceOfDepartment")
public class Department implements AssistantEntity {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	@Basic
	@Column(name = "name")
	private String name;

	@Basic
	@Column(name = "abbreviation")
	private String abbreviation;

	@OneToMany(mappedBy = "department", fetch = FetchType.EAGER)
	private List<Group> groups;

	@OneToMany(mappedBy = "department")
	private List<Stream> streams;

	public Department() {
	}

	public Department(Department department) {
		this.name = department.name;
		this.abbreviation = department.abbreviation;
		this.groups = department.groups;
		this.streams = department.streams;
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

	public String getAbbreviation() {
		return abbreviation;
	}

	public void setAbbreviation(String abbreviation) {
		this.abbreviation = abbreviation;
	}

	public List<Group> getGroups() {
		return groups;
	}

	public void setGroups(List<Group> groups) {
		this.groups = groups;
	}

	public List<Stream> getStreams() {
		return streams;
	}

	public void setStreams(List<Stream> streams) {
		this.streams = streams;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Department that = (Department) o;

		if (id != null ? !id.equals(that.id) : that.id != null) return false;
		if (name != null ? !name.equals(that.name) : that.name != null) return false;
		if (abbreviation != null ? !abbreviation.equals(that.abbreviation) : that.abbreviation != null) return false;

		return true;
	}

	@Override
	public int hashCode() {
		int result = id != null ? id.hashCode() : 0;
		result = 31 * result + (name != null ? name.hashCode() : 0);
		result = 31 * result + (abbreviation != null ? abbreviation.hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return "Department{" +
				"id=" + id +
				", name='" + name + '\'' +
				", abbreviation='" + abbreviation + '\'' +
				'}';
	}
}
