package com.grsu.teacherassistant.entities;

import com.grsu.teacherassistant.converters.db.LocalTimeAttributeConverter;

import javax.persistence.*;
import java.time.LocalTime;
import java.util.List;

/**
 * Created by zaychick-pavel on 2/9/17.
 */
@Entity
public class Schedule implements AssistantEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	@Basic
	@Convert(converter = LocalTimeAttributeConverter.class)
	@Column(name = "begin")
	private LocalTime begin;

	@Basic
	@Convert(converter = LocalTimeAttributeConverter.class)
	@Column(name = "end")
	private LocalTime end;

	@Basic
	@Column(name = "number")
	private Integer number;

	@OneToMany(mappedBy = "schedule", fetch = FetchType.EAGER)
	private List<Class> classes;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "version_id", referencedColumnName = "id")
	private ScheduleVersion version;

	public String getCaption() {
		return String.format("[%s] %s - %s", number, begin, end);
	}

	public String getTime() {
		return String.format("%s - %s", begin, end);
	}

	/* GETTERS & SETTERS */
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public LocalTime getBegin() {
		return begin;
	}

	public void setBegin(LocalTime begin) {
		this.begin = begin;
	}

	public LocalTime getEnd() {
		return end;
	}

	public void setEnd(LocalTime end) {
		this.end = end;
	}

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

	public List<Class> getClasses() {
		return classes;
	}

	public void setClasses(List<Class> classes) {
		this.classes = classes;
	}

	public ScheduleVersion getVersion() {
		return version;
	}

	public void setVersion(ScheduleVersion version) {
		this.version = version;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Schedule schedule = (Schedule) o;

		if (id != null ? !id.equals(schedule.id) : schedule.id != null) return false;
		if (begin != null ? !begin.equals(schedule.begin) : schedule.begin != null) return false;
		if (end != null ? !end.equals(schedule.end) : schedule.end != null) return false;
		if (number != null ? !number.equals(schedule.number) : schedule.number != null) return false;

		return true;
	}

	@Override
	public int hashCode() {
		int result = id != null ? id.hashCode() : 0;
		result = 31 * result + (begin != null ? begin.hashCode() : 0);
		result = 31 * result + (end != null ? end.hashCode() : 0);
		result = 31 * result + (number != null ? number.hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return "Schedule{" +
				"id=" + id +
				", begin='" + begin + '\'' +
				", end='" + end + '\'' +
				", number=" + number +
				'}';
	}
}
