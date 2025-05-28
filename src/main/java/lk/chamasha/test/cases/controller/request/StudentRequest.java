package lk.chamasha.test.cases.controller.request;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentRequest {
    private String studentName;
    private Long courseId;
}
