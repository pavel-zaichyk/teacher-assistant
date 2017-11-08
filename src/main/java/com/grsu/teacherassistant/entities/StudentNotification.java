package com.grsu.teacherassistant.entities;

import com.grsu.teacherassistant.converters.db.LocalDateTimeAttributeConverter;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @author Pavel Zaychick
 */
@Entity
@Table(name = "STUDENT_NOTIFICATION")
@Getter
@Setter
public class StudentNotification implements AssistantEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Boolean active;

    @Convert(converter = LocalDateTimeAttributeConverter.class)
    @Column(name = "create_date")
    private LocalDateTime createDate;

    private String description;

    @ManyToOne
    private Student student;

    @Override
    public String toString() {
        return "StudentNotification{" +
            "id=" + id +
            ", active=" + active +
            ", createDate=" + createDate +
            ", description='" + description + '\'' +
            '}';
    }
}
