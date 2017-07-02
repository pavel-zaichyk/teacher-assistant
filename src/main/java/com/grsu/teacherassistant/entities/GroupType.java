package com.grsu.teacherassistant.entities;

import javax.persistence.*;
import java.util.List;

/**
 * Created by zaychick-pavel on 2/9/17.
 */
@Entity
@Table(name = "GROUP_TYPE")
public class GroupType implements AssistantEntity {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	@Basic
	@Column(name = "name")
	private String name;

	@OneToMany(mappedBy = "type", fetch = FetchType.EAGER)
	private List<Group> groups;

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

	public List<Group> getGroups() {
		return groups;
	}

	public void setGroups(List<Group> groups) {
		this.groups = groups;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		GroupType groupType = (GroupType) o;

		if (id != null ? !id.equals(groupType.id) : groupType.id != null) return false;
		if (name != null ? !name.equals(groupType.name) : groupType.name != null) return false;

		return true;
	}

	@Override
	public int hashCode() {
		int result = id != null ? id.hashCode() : 0;
		result = 31 * result + (name != null ? name.hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return "GroupType{" +
				"id=" + id +
				", name='" + name + '\'' +
				'}';
	}
}
