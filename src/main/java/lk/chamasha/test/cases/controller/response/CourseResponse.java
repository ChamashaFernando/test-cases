package lk.chamasha.test.cases.controller.response;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseResponse {
    private Long id;
    private String courseName;
    private String departmentName;
}
