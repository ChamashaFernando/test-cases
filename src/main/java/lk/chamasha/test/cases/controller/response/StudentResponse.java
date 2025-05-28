package lk.chamasha.test.cases.controller.response;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentResponse {
    private Long id;
    private String studentName;
    private String courseName;
}
