package lk.chamasha.test.cases.model;

import jakarta.persistence.*;
import lk.chamasha.test.cases.model.Department;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "department")
@EqualsAndHashCode(exclude = "department")
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String courseName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    private Department department;
}
