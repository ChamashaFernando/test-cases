package lk.chamasha.test.cases.repository;


import lk.chamasha.test.cases.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {
    Optional<Student> findById(Long id);
}
