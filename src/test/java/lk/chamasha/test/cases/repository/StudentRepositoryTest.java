package lk.chamasha.test.cases.repository;

import lk.chamasha.test.cases.model.Course;
import lk.chamasha.test.cases.model.Department;
import lk.chamasha.test.cases.model.Student;
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
public class StudentRepositoryTest {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @BeforeEach
    public void clearDatabase() {
        studentRepository.deleteAll();
        courseRepository.deleteAll();
        departmentRepository.deleteAll();
    }

    @Test
    public void testFindById_StudentExists() {
        // Arrange - create Department -> Course -> Student
        Department department = new Department();
        department.setDepartmentName("Engineering");
        Department savedDept = departmentRepository.save(department);

        Course course = new Course();
        course.setCourseName("Computer Science");
        course.setDepartment(savedDept);
        Course savedCourse = courseRepository.save(course);

        Student student = new Student();
        student.setStudentName("Chamasha");
        student.setCourse(savedCourse);
        Student savedStudent = studentRepository.save(student);

        // Act
        Optional<Student> found = studentRepository.findById(savedStudent.getId());

        // Assert
        Assertions.assertTrue(found.isPresent());
        Assertions.assertEquals(savedStudent.getId(), found.get().getId());
        Assertions.assertEquals("Chamasha", found.get().getStudentName());
        Assertions.assertEquals("Computer Science", found.get().getCourse().getCourseName());
    }

    @Test
    public void testFindById_StudentDoesNotExist() {
        Optional<Student> result = studentRepository.findById(9999L);
        Assertions.assertFalse(result.isPresent());
    }
}
