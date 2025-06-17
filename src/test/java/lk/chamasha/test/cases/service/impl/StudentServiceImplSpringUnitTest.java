package lk.chamasha.test.cases.service.impl;

import lk.chamasha.test.cases.controller.request.StudentRequest;
import lk.chamasha.test.cases.controller.response.StudentResponse;
import lk.chamasha.test.cases.model.Course;
import lk.chamasha.test.cases.model.Student;
import lk.chamasha.test.cases.repository.CourseRepository;
import lk.chamasha.test.cases.repository.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StudentServiceImplSpringUnitTest {

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private CourseRepository courseRepository;

    @InjectMocks
    private StudentServiceImpl studentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Create student successfully")
    void testCreateStudentSuccess() {
        StudentRequest request = new StudentRequest();
        request.setStudentName("Chamasha");
        request.setCourseId(1L);

        Course course = new Course();
        course.setId(1L);
        course.setCourseName("Computer Science");

        Student savedStudent = new Student();
        savedStudent.setId(1L);
        savedStudent.setStudentName("Chamasha");
        savedStudent.setCourse(course);

        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(studentRepository.save(any(Student.class))).thenReturn(savedStudent);

        StudentResponse response = studentService.createStudent(request);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("Chamasha", response.getStudentName());
        assertEquals("Computer Science", response.getCourseName());

        verify(courseRepository).findById(1L);
        verify(studentRepository).save(any(Student.class));
    }

    @Test
    @DisplayName("Create student fails when course not found")
    void testCreateStudentCourseNotFound() {
        StudentRequest request = new StudentRequest();
        request.setStudentName("Chamasha");
        request.setCourseId(99L);

        when(courseRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> studentService.createStudent(request));

        assertEquals("Course not found with id: 99", exception.getMessage());

        verify(courseRepository).findById(99L);
        verify(studentRepository, never()).save(any());
    }

    @Test
    @DisplayName("Get all students successfully")
    void testGetAllStudents() {
        Course course = new Course();
        course.setCourseName("Maths");

        Student student1 = new Student();
        student1.setId(1L);
        student1.setStudentName("Nimal");
        student1.setCourse(course);

        Student student2 = new Student();
        student2.setId(2L);
        student2.setStudentName("Kamal");
        student2.setCourse(course);

        when(studentRepository.findAll()).thenReturn(List.of(student1, student2));

        List<StudentResponse> responses = studentService.getAllStudents();

        assertEquals(2, responses.size());
        assertTrue(responses.stream().anyMatch(s -> s.getStudentName().equals("Nimal")));
        assertTrue(responses.stream().anyMatch(s -> s.getStudentName().equals("Kamal")));

        verify(studentRepository).findAll();
    }

    @Test
    @DisplayName("Get student by id successfully")
    void testGetStudentByIdSuccess() {
        Course course = new Course();
        course.setCourseName("Physics");

        Student student = new Student();
        student.setId(5L);
        student.setStudentName("Saman");
        student.setCourse(course);

        when(studentRepository.findById(5L)).thenReturn(Optional.of(student));

        StudentResponse response = studentService.getStudentById(5L);

        assertNotNull(response);
        assertEquals(5L, response.getId());
        assertEquals("Saman", response.getStudentName());
        assertEquals("Physics", response.getCourseName());

        verify(studentRepository).findById(5L);
    }

    @Test
    @DisplayName("Get student by id throws exception when not found")
    void testGetStudentByIdNotFound() {
        when(studentRepository.findById(100L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> studentService.getStudentById(100L));

        assertEquals("Student not found with id: 100", exception.getMessage());

        verify(studentRepository).findById(100L);
    }

    @Test
    @DisplayName("Delete student successfully")
    void testDeleteStudentSuccess() {
        when(studentRepository.existsById(1L)).thenReturn(true);
        doNothing().when(studentRepository).deleteById(1L);

        assertDoesNotThrow(() -> studentService.deleteStudent(1L));

        verify(studentRepository).existsById(1L);
        verify(studentRepository).deleteById(1L);
    }

    @Test
    @DisplayName("Delete student throws exception when not found")
    void testDeleteStudentNotFound() {
        when(studentRepository.existsById(999L)).thenReturn(false);

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> studentService.deleteStudent(999L));

        assertEquals("Student not found with id: 999", exception.getMessage());

        verify(studentRepository).existsById(999L);
        verify(studentRepository, never()).deleteById(any());
    }
}
