package com.grsu.teacherassistant.entities;

import com.grsu.teacherassistant.converters.db.LocalDateTimeAttributeConverter;

import javax.faces.bean.ManagedBean;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by zaychick-pavel on 2/9/17.
 */
@Entity
@ManagedBean(name = "newInstanceOfDiscipline")
public class Discipline implements AssistantEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
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
	@Column(name = "active")
	private Boolean active;

	@Basic
	@Convert(converter = LocalDateTimeAttributeConverter.class)
	@Column(name = "expiration_date")
	private LocalDateTime expirationDate;

	@OneToMany(mappedBy = "discipline", fetch = FetchType.EAGER)
	private List<Stream> streams;

	public Discipline() {
	}

	public Discipline(Discipline discipline) {
		this.name = discipline.name;
		this.description = discipline.description;
		this.createDate = discipline.createDate;
		this.active = discipline.active;
		this.expirationDate = discipline.expirationDate;
		this.streams = discipline.streams;
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

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Discipline that = (Discipline) o;

		if (id != null ? !id.equals(that.id) : that.id != null) return false;
		if (name != null ? !name.equals(that.name) : that.name != null) return false;
		if (description != null ? !description.equals(that.description) : that.description != null) return false;
		if (createDate != null ? !createDate.equals(that.createDate) : that.createDate != null) return false;
		if (active != null ? !active.equals(that.active) : that.active != null) return false;
		if (expirationDate != null ? !expirationDate.equals(that.expirationDate) : that.expirationDate != null)
			return false;

		return true;
	}

	@Override
	public int hashCode() {
		int result = id != null ? id.hashCode() : 0;
		result = 31 * result + (name != null ? name.hashCode() : 0);
		result = 31 * result + (description != null ? description.hashCode() : 0);
		result = 31 * result + (createDate != null ? createDate.hashCode() : 0);
		result = 31 * result + (active != null ? active.hashCode() : 0);
		result = 31 * result + (expirationDate != null ? expirationDate.hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return "Discipline{" +
				"id=" + id +
				", name='" + name + '\'' +
				", description='" + description + '\'' +
				", createDate='" + createDate + '\'' +
				", active=" + active +
				", expirationDate='" + expirationDate + '\'' +
				'}';
	}
}
