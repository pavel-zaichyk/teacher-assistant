package com.grsu.teacherassistant.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

/**
 * @author Pavel Zaychick
 */
@Entity
@Table(name = "GROUP_TYPE")
@Getter
@Setter
public class GroupType implements AssistantEntity {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	@Basic
	@Column(name = "name")
	private String name;

	@OneToMany(mappedBy = "type")
	private List<Group> groups;

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
