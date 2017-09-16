package com.grsu.teacherassistant.entities;

import com.grsu.teacherassistant.models.SkipInfo;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.*;
import org.hibernate.annotations.CascadeType;

import javax.faces.bean.ManagedBean;
import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.grsu.teacherassistant.constants.Constants.GROUPS_DELIMITER;
import static org.apache.commons.lang3.StringUtils.isEmpty;

/**
 * @author Pavel Zaychick
 */
@SqlResultSetMapping(
    name = "SkipInfoMapping",
    classes = {
        @ConstructorResult(
            targetClass = SkipInfo.class,
            columns = {
                @ColumnResult(name = "studentId", type = Integer.class),
                @ColumnResult(name = "lessonType", type = Integer.class),
                @ColumnResult(name = "count", type = int.class)
            }
        )
    }
)
@NamedNativeQueries({
    @NamedNativeQuery(
        name = "SkipInfoQuery",
        query = "select st.id as studentId, l.type_id as lessonType, count(*) as count\n" +
            "from STUDENT st\n" +
            "join STUDENT_LESSON sl on sl.student_id = st.id\n" +
            "join LESSON l on l.id = sl.lesson_id and l.type_id in (1, 2, 3)\n" +
            "join SCHEDULE sch on sch.id = l.schedule_id \n" +
            "join STREAM str on str.id = l.stream_id\n" +
            "where (sl.registered is null or sl.registered = 0) and str.id = :streamId " +
            "and ((date(l.date) < date('now', 'localtime')) or (date(l.date) = date('now', 'localtime') and time(sch.begin) <= time('now', 'localtime')) or l.id = :lessonId)\n" +
            "group by st.id, str.id, l.type_id",
        resultSetMapping = "SkipInfoMapping"),
    @NamedNativeQuery(
        name = "StudentSkipInfoQuery",
        query = "select st.id as studentId, l.type_id as lessonType, count(*) as count\n" +
            "from STUDENT st\n" +
            "join STUDENT_LESSON sl on sl.student_id = st.id\n" +
            "join LESSON l on l.id = sl.lesson_id and l.type_id in (1, 2, 3)\n" +
            "join SCHEDULE sch on sch.id = l.schedule_id " +
            "join STREAM str on str.id = l.stream_id\n" +
            "where st.id in (:studentId) and (sl.registered is null or sl.registered = 0) and str.id = :streamId " +
            "and ((date(l.date) < date('now', 'localtime')) or (date(l.date) = date('now', 'localtime') and time(sch.begin) <= time('now', 'localtime')) or l.id = :lessonId)\n" +
            "group by st.id, str.id, l.type_id",
        resultSetMapping = "SkipInfoMapping"),
    @NamedNativeQuery(
        name = "AdditionalStudents",
        query = "SELECT st.*\n" +
            "FROM STUDENT st\n" +
            "\tJOIN STUDENT_LESSON sl ON st.id = sl.student_id\n" +
            "\tJOIN LESSON l ON l.id = sl.lesson_id\n" +
            "WHERE l.id = :lessonId\n" +
            "\t\t\tAND ((l.group_id NOT NULL AND st.id NOT IN (\n" +
            "\tSELECT stg.student_id\n" +
            "\tFROM STUDENT_GROUP stg\n" +
            "\tWHERE stg.group_id = l.group_id\n" +
            "))\n" +
            "\t\t\t\t\t OR st.id NOT IN (\n" +
            "\tSELECT stg.student_id\n" +
            "\tFROM STUDENT_GROUP stg\n" +
            "\t\tJOIN STREAM_GROUP sg ON sg.group_id = stg.group_id\n" +
            "\tWHERE sg.stream_id = l.stream_id\n" +
            "));", resultClass = Student.class)
})
@Entity
@ManagedBean(name = "newInstanceOfStudent")
@Getter
@Setter
public class Student implements AssistantEntity, Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Basic
    @Column(name = "card_uid")
    private String cardUid;

    @Basic
    @Column(name = "card_id")
    private Integer cardId;

    @Basic
    @Column(name = "first_name")
    private String firstName;

    @Basic
    @Column(name = "last_name")
    private String lastName;

    @Basic
    @Column(name = "patronymic")
    private String patronymic;

    @Basic
    @Column(name = "phone")
    private String phone;

    @Basic
    @Column(name = "email")
    private String email;

    @ManyToMany
    @JoinTable(name = "STUDENT_GROUP",
        joinColumns = @JoinColumn(name = "student_id", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "group_id", referencedColumnName = "id"))
    private List<Group> groups;

    @Cascade(CascadeType.DELETE)
    @MapKey(name = "lessonId")
    @OneToMany(mappedBy = "student")
    private Map<Integer, StudentLesson> studentLessons;

    @Cascade(CascadeType.DELETE)
    @OneToMany
    @JoinColumn(name = "entity_id", referencedColumnName = "id")
    @Where(clause = "type = 'STUDENT'")
    private List<Note> notes;

    public String getFullName() {
        return String.join(" ", lastName, firstName);
    }

    public String getGroupNames() {
        if (groups != null) {
            return groups.stream().map(Group::getName).collect(Collectors.joining(GROUPS_DELIMITER));
        } else {
            return "";
        }
    }

    public void setCardUidFromCardId(int cardId) {
        cardUid = Integer.toHexString(cardId).toUpperCase();
    }

    //http://stackoverflow.com/a/7038867/7464024
    public void setCardIdFromCardUid(String cardUid) {
        try {
            cardId = (int) Long.parseLong(cardUid, 16);
        } catch (NumberFormatException ex) {
            this.cardId = 0;
        }
    }

    public void setCardUid(String cardUid) {
        if (isEmpty(cardUid)) {
            this.cardUid = null;
        } else {
            this.cardUid = cardUid.toUpperCase();
            setCardIdFromCardUid(this.cardUid);
        }
    }

    public void setCardId(Integer cardId) {
        this.cardId = cardId;
        if (this.cardId != 0) {
            setCardUidFromCardId(this.cardId);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Student student = (Student) o;

        if (id != null ? !id.equals(student.id) : student.id != null) return false;
        if (cardUid != null ? !cardUid.equals(student.cardUid) : student.cardUid != null) return false;
        if (cardId != null ? !cardId.equals(student.cardId) : student.cardId != null) return false;
        if (firstName != null ? !firstName.equals(student.firstName) : student.firstName != null) return false;
        if (lastName != null ? !lastName.equals(student.lastName) : student.lastName != null) return false;
        if (patronymic != null ? !patronymic.equals(student.patronymic) : student.patronymic != null) return false;
        if (phone != null ? !phone.equals(student.phone) : student.phone != null) return false;
        if (email != null ? !email.equals(student.email) : student.email != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (cardUid != null ? cardUid.hashCode() : 0);
        result = 31 * result + (cardId != null ? cardId.hashCode() : 0);
        result = 31 * result + (firstName != null ? firstName.hashCode() : 0);
        result = 31 * result + (lastName != null ? lastName.hashCode() : 0);
        result = 31 * result + (patronymic != null ? patronymic.hashCode() : 0);
        result = 31 * result + (phone != null ? phone.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Student{" +
            "id=" + id +
            ", cardUid='" + cardUid + '\'' +
            ", cardId='" + cardId + '\'' +
            ", firstName='" + firstName + '\'' +
            ", lastName='" + lastName + '\'' +
            ", patronymic='" + patronymic + '\'' +
            ", phone='" + phone + '\'' +
            ", email='" + email + '\'' +
            '}';
    }
}
