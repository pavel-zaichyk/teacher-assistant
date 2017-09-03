package com.grsu.teacherassistant.entities;

import com.grsu.teacherassistant.converters.db.LocalTimeAttributeConverter;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalTime;
import java.util.List;

/**
 * @author Pavel Zaychick
 */
@Entity
@Getter
@Setter
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

    @OneToMany(mappedBy = "schedule")
    private List<Lesson> lessons;

    @ManyToOne
    @JoinColumn(name = "version_id", referencedColumnName = "id")
    private ScheduleVersion version;

    public String getCaption() {
        return String.format("[%s] %s - %s", number, begin, end);
    }

    public String getTime() {
        return String.format("%s - %s", begin, end);
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
