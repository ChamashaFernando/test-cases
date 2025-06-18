package lk.chamasha.test.cases.service.impl;

import lk.chamasha.test.cases.controller.request.StudentRequest;
import lk.chamasha.test.cases.controller.response.StudentResponse;
import lk.chamasha.test.cases.model.Course;
import lk.chamasha.test.cases.model.Student;
import lk.chamasha.test.cases.repository.CourseRepository;
import lk.chamasha.test.cases.repository.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class StudentServiceImplSpringIntegrationTest {
    @Autowired
    private StudentServiceImpl studentService;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private CourseRepository courseRepository;

    private Course course;

    @BeforeEach
    void setUp() {
        studentRepository.deleteAll();
        courseRepository.deleteAll();

        course = new Course();
        course.setCourseName("Software Engineering");
        course = courseRepository.save(course);
    }

    // ===== CREATE =====

    @Test
    void testCreateStudent_Success() {
        StudentRequest request = new StudentRequest();
        request.setStudentName("Chamasha");
        request.setCourseId(course.getId());

        StudentResponse response = studentService.create(request);

        assertNotNull(response);
        assertEquals("Chamasha", response.getStudentName());
        assertEquals("Software Engineering", response.getCourseName());
        assertTrue(studentRepository.existsById(response.getId()));
    }

    @Test
    void testCreateStudent_CourseNotFound() {
        StudentRequest request = new StudentRequest();
        request.setStudentName("Kasun");
        request.setCourseId(9999L); // Invalid course ID

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> studentService.create(request));
        assertEquals("Course not found with id: 9999", thrown.getMessage());
    }

    // ===== GET ALL =====

    @Test
    void testGetAllStudents_ReturnsList() {
        Student s1 = new Student();
        s1.setStudentName("Nimal");
        s1.setCourse(course);

        Student s2 = new Student();
        s2.setStudentName("Sunil");
        s2.setCourse(course);

        studentRepository.saveAll(Arrays.asList(s1, s2));

        List<StudentResponse> students = studentService.getAll();

        assertEquals(2, students.size());
        assertEquals("Nimal", students.get(0).getStudentName());
        assertEquals("Sunil", students.get(1).getStudentName());
    }

    @Test
    void testGetAllStudents_ReturnsEmptyList() {
        List<StudentResponse> students = studentService.getAll();
        assertTrue(students.isEmpty());
    }

    // ===== GET BY ID =====

    @Test
    void testGetById_Success() {
        Student student = new Student();
        student.setStudentName("Amal");
        student.setCourse(course);
        student = studentRepository.save(student);

        StudentResponse response = studentService.getById(student.getId());

        assertNotNull(response);
        assertEquals("Amal", response.getStudentName());
        assertEquals("Software Engineering", response.getCourseName());
    }

    @Test
    void testGetById_NotFound() {
        RuntimeException thrown = assertThrows(RuntimeException.class, () -> studentService.getById(8888L));
        assertEquals("Student not found with id: 8888", thrown.getMessage());
    }

    // ===== DELETE =====

    @Test
    void testDeleteStudent_Success() {
        // Arrange: Save a student with a course
        Course savedCourse = new Course();
        savedCourse.setCourseName("Computer Science");
        savedCourse = courseRepository.save(savedCourse);

        Student student = new Student();
        student.setStudentName("Kamal");
        student.setCourse(savedCourse);
        student = studentRepository.save(student);

        Long studentId = student.getId();

        // Act & Assert
        assertDoesNotThrow(() -> studentService.delete(studentId));
        assertFalse(studentRepository.existsById(studentId));
    }

    @Test
    void testDeleteStudent_NotFound() {
        RuntimeException thrown = assertThrows(RuntimeException.class, () -> studentService.delete(7777L));
        assertEquals("Student not found with id: 7777", thrown.getMessage());
    }
}