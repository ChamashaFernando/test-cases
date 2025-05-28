package lk.chamasha.test.cases.controller.request;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseRequest {
    private String courseName;
    private Long departmentId;
}
