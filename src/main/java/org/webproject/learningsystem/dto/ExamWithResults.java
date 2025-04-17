package org.webproject.learningsystem.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.webproject.learningsystem.model.Exam;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ExamWithResults {
    private Exam exam;
    private List<StudentResult> studentResults;

    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class StudentResult {
        private Long studentId;
        private String studentName;
        private String studentEmail;
        private Integer score;
        private String completedAt;
        private String enrolledAt;
    }
}