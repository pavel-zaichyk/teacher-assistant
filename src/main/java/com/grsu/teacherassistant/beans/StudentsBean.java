package com.grsu.teacherassistant.beans;

import com.grsu.teacherassistant.dao.EntityDAO;
import com.grsu.teacherassistant.dao.StudentDAO;
import com.grsu.teacherassistant.entities.Student;
import lombok.Data;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;
import java.util.List;

/**
 * @author Pavel Zaychick
 */
@ManagedBean(name = "studentsBean")
@ViewScoped
@Data
public class StudentsBean implements Serializable {

    private List<Student> students;
    private List<Student> filteredStudents;
    private Student selectedStudent;

    private boolean showClosed;

    public void removeStudent(Student student) {
        EntityDAO.delete(student);
        students.remove(student);
    }

    public List<Student> getStudents() {
        if (students == null) {
            students = StudentDAO.getAll();
        }
        return students;
    }

}
