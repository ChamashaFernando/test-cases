package lk.chamasha.test.cases.service.impl;

import lk.chamasha.test.cases.controller.request.StudentRequest;
import lk.chamasha.test.cases.controller.response.StudentResponse;
import lk.chamasha.test.cases.model.Course;
import lk.chamasha.test.cases.repository.CourseRepository;
import lk.chamasha.test.cases.repository.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class StudentServiceImplSpringIntegrationTest {

    @Autowired
    private StudentServiceImpl studentService;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private StudentRepository studentRepository;

    private Course savedCourse;

    @BeforeEach
    public void setup() {
        // Course එකක් create කරමු tests වලට
        Course course = new Course();
        course.setCourseName("Maths");
        savedCourse = courseRepository.save(course);
    }

    @Test
    public void testCreateStudentSuccess() {
        StudentRequest request = new StudentRequest();
        request.setStudentName("Chamasha");
        request.setCourseId(savedCourse.getId());

        StudentResponse response = studentService.createStudent(request);

        assertNotNull(response);
        assertEquals("Chamasha", response.getStudentName());
        assertEquals(savedCourse.getCourseName(), response.getCourseName());

        assertTrue(studentRepository.existsById(response.getId()));
    }

    @Test
    public void testCreateStudentCourseNotFound() {
        StudentRequest request = new StudentRequest();
        request.setStudentName("Nethmi");
        request.setCourseId(999L);  // නොමැති course id එකක්

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            studentService.createStudent(request);
        });

        assertTrue(exception.getMessage().contains("Course not found"));
    }

    @Test
    public void testGetAllStudents() {
        // Create 2 students
        StudentRequest req1 = new StudentRequest();
        req1.setStudentName("Dilshan");
        req1.setCourseId(savedCourse.getId());
        studentService.createStudent(req1);

        StudentRequest req2 = new StudentRequest();
        req2.setStudentName("Kasun");
        req2.setCourseId(savedCourse.getId());
        studentService.createStudent(req2);

        List<StudentResponse> students = studentService.getAllStudents();

        assertNotNull(students);
        assertTrue(students.size() >= 2);
    }

    @Test
    public void testGetStudentByIdSuccess() {
        StudentRequest request = new StudentRequest();
        request.setStudentName("Roshan");
        request.setCourseId(savedCourse.getId());
        StudentResponse created = studentService.createStudent(request);

        StudentResponse fetched = studentService.getStudentById(created.getId());

        assertNotNull(fetched);
        assertEquals(created.getStudentName(), fetched.getStudentName());
        assertEquals(created.getCourseName(), fetched.getCourseName());
    }

    @Test
    public void testGetStudentByIdNotFound() {
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            studentService.getStudentById(999L);
        });

        assertTrue(exception.getMessage().contains("Student not found"));
    }

    @Test
    public void testDeleteStudentSuccess() {
        StudentRequest request = new StudentRequest();
        request.setStudentName("Saman");
        request.setCourseId(savedCourse.getId());
        StudentResponse created = studentService.createStudent(request);

        assertTrue(studentRepository.existsById(created.getId()));

        studentService.deleteStudent(created.getId());

        assertFalse(studentRepository.existsById(created.getId()));
    }

    @Test
    public void testDeleteStudentNotFound() {
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            studentService.deleteStudent(999L);
        });

        assertTrue(exception.getMessage().contains("Student not found"));
    }
}
