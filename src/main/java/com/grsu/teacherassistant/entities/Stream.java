package com.grsu.teacherassistant.entities;

import com.grsu.teacherassistant.converters.db.LocalDateTimeAttributeConverter;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.persistence.Entity;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.grsu.teacherassistant.constants.Constants.GROUPS_DELIMITER;

/**
 * @author Pavel Zaychick
 */
@Entity
@Getter
@Setter
public class Stream implements AssistantEntity {
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
    @Column(name = "course")
    private Integer course;

    @Basic
    @Column(name = "active")
    private boolean active;

    @Basic
    @Convert(converter = LocalDateTimeAttributeConverter.class)
    @Column(name = "expiration_date")
    private LocalDateTime expirationDate;

    @ManyToMany
    @JoinTable(name = "STREAM_GROUP",
        joinColumns = @JoinColumn(name = "stream_id", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "group_id", referencedColumnName = "id"))
    private List<Group> groups;

    @OneToMany(mappedBy = "stream")
    private List<Lesson> lessons;

    @ManyToOne
    @JoinColumn(name = "discipline_id", referencedColumnName = "id")
    private Discipline discipline;

    @ManyToOne
    @JoinColumn(name = "department_id", referencedColumnName = "id")
    private Department department;

    @Column(name = "lecture_count")
    private Integer lectureCount;

    @Column(name = "practical_count")
    private Integer practicalCount;

    @Column(name = "lab_count")
    private Integer labCount;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Stream stream = (Stream) o;

        if (active != stream.active) return false;
        if (id != null ? !id.equals(stream.id) : stream.id != null) return false;
        if (name != null ? !name.equals(stream.name) : stream.name != null) return false;
        if (description != null ? !description.equals(stream.description) : stream.description != null) return false;
        if (createDate != null ? !createDate.equals(stream.createDate) : stream.createDate != null) return false;
        if (course != null ? !course.equals(stream.course) : stream.course != null) return false;
        return expirationDate != null ? expirationDate.equals(stream.expirationDate) : stream.expirationDate == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (createDate != null ? createDate.hashCode() : 0);
        result = 31 * result + (course != null ? course.hashCode() : 0);
        result = 31 * result + (active ? 1 : 0);
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
