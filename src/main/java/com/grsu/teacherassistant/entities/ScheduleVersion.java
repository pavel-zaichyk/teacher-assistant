package com.grsu.teacherassistant.entities;

import com.grsu.teacherassistant.converters.db.LocalDateTimeAttributeConverter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by zaychick-pavel on 2/9/17.
 */
@Entity
@Table(name = "SCHEDULE_VERSION")
public class ScheduleVersion implements AssistantEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	@Basic
	@Convert(converter = LocalDateTimeAttributeConverter.class)
	@Column(name = "start_date")
	private LocalDateTime startDate;

	@Basic
	@Convert(converter = LocalDateTimeAttributeConverter.class)
	@Column(name = "end_date")
	private LocalDateTime endDate;

	@OneToMany(mappedBy = "version", fetch = FetchType.EAGER)
	private List<Schedule> schedules;

	/* GETTERS & SETTERS */
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public LocalDateTime getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDateTime startDate) {
		this.startDate = startDate;
	}

	public LocalDateTime getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDateTime endDate) {
		this.endDate = endDate;
	}

	public List<Schedule> getSchedules() {
		return schedules;
	}

	public void setSchedules(List<Schedule> schedules) {
		this.schedules = schedules;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		ScheduleVersion that = (ScheduleVersion) o;

		if (id != null ? !id.equals(that.id) : that.id != null) return false;
		if (startDate != null ? !startDate.equals(that.startDate) : that.startDate != null) return false;
		if (endDate != null ? !endDate.equals(that.endDate) : that.endDate != null) return false;

		return true;
	}

	@Override
	public int hashCode() {
		int result = id != null ? id.hashCode() : 0;
		result = 31 * result + (startDate != null ? startDate.hashCode() : 0);
		result = 31 * result + (endDate != null ? endDate.hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return "ScheduleVersion{" +
				"id=" + id +
				", startDate='" + startDate + '\'' +
				", endDate='" + endDate + '\'' +
				'}';
	}
}
