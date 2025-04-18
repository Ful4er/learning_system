package org.webproject.learningsystem.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;

import java.util.Objects;

@Getter
@Setter
@Embeddable
public class ExamStudentId implements java.io.Serializable {
    private static final long serialVersionUID = -7368348421767221796L;

    @Column(name = "exam_id", nullable = false)
    private Long examId;

    @Column(name = "student_id", nullable = false)
    private Long studentId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ExamStudentId entity = (ExamStudentId) o;
        return Objects.equals(this.studentId, entity.studentId) &&
                Objects.equals(this.examId, entity.examId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(studentId, examId);
    }
}