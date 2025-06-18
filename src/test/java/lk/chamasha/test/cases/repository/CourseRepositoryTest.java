package lk.chamasha.test.cases.repository;

import lk.chamasha.test.cases.model.Course;
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
public class CourseRepositoryTest {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    private Department savedDepartment;

    @BeforeEach
    public void setup() {
        courseRepository.deleteAll();
        departmentRepository.deleteAll();

        Department department = new Department();
        department.setDepartmentName("Engineering");

        savedDepartment = departmentRepository.save(department);
    }

    @Test
    public void testFindById_CourseExists() {
        // Arrange
        Course course = new Course();
        course.setCourseName("Computer Science");
        course.setDepartment(savedDepartment);

        Course savedCourse = courseRepository.save(course);

        // Act
        Optional<Course> foundCourse = courseRepository.findById(savedCourse.getId());

        // Assert
        Assertions.assertTrue(foundCourse.isPresent());
        Assertions.assertEquals(savedCourse.getId(), foundCourse.get().getId());
        Assertions.assertEquals("Computer Science", foundCourse.get().getCourseName());
        Assertions.assertEquals(savedDepartment.getId(), foundCourse.get().getDepartment().getId());
    }

    @Test
    public void testFindById_CourseDoesNotExist() {
        // Act
        Optional<Course> result = courseRepository.findById(999L);

        // Assert
        Assertions.assertFalse(result.isPresent());
    }
}
