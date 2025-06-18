package lk.chamasha.test.cases.repository;

import lk.chamasha.test.cases.model.Department;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class DepartmentRepositoryTest {

    @Autowired
    private DepartmentRepository departmentRepository;

    @BeforeEach
    void setUp() {
        departmentRepository.deleteAll();
    }

    @Test
    void testFindByDepartmentName_DepartmentExists() {
        // Arrange
        Department department = new Department();
        department.setDepartmentName("Engineering");

        Department saved = departmentRepository.save(department);

        // Act
        Optional<Department> found = departmentRepository.findByDepartmentName("Engineering");

        // Assert
        Assertions.assertTrue(found.isPresent());
        Assertions.assertEquals(saved.getId(), found.get().getId());
        Assertions.assertEquals("Engineering", found.get().getDepartmentName());
    }

    @Test
    void testFindByDepartmentName_DepartmentDoesNotExist() {
        // Act
        Optional<Department> result = departmentRepository.findByDepartmentName("NonExistentDept");

        // Assert
        Assertions.assertFalse(result.isPresent());
    }

    @Test
    void testFindById_DepartmentExists() {
        // Arrange
        Department department = new Department();
        department.setDepartmentName("Science");

        Department saved = departmentRepository.save(department);

        // Act
        Optional<Department> found = departmentRepository.findById(saved.getId());

        // Assert
        Assertions.assertTrue(found.isPresent());
        Assertions.assertEquals("Science", found.get().getDepartmentName());
    }

    @Test
    void testFindById_DepartmentDoesNotExist() {
        // Act
        Optional<Department> result = departmentRepository.findById(999L);

        // Assert
        Assertions.assertFalse(result.isPresent());
    }
}