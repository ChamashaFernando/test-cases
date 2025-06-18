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
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class StudentServiceImplSpringUnitTest {

    @InjectMocks
    private StudentServiceImpl studentService;

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private CourseRepository courseRepository;

    private Course course;
    private Student student;

    @BeforeEach
    void setUp() {
        course = new Course();
        course.setId(1L);
        course.setCourseName("Computer Science");

        student = new Student();
        student.setId(1L);
        student.setStudentName("Chamasha");
        student.setCourse(course);
    }

    @Test
    void createStudent_Success() {
        StudentRequest request = new StudentRequest();
        request.setStudentName("Chamasha");
        request.setCourseId(1L);

        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));

        Student savedStudent = new Student();
        savedStudent.setId(1L);
        savedStudent.setStudentName("Chamasha");
        savedStudent.setCourse(course);

        when(studentRepository.save(any(Student.class))).thenReturn(savedStudent);

        StudentResponse response = studentService.create(request);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("Chamasha", response.getStudentName());
        assertEquals("Computer Science", response.getCourseName());

        verify(courseRepository, times(1)).findById(1L);
        verify(studentRepository, times(1)).save(any(Student.class));
    }

    @Test
    void createStudent_CourseNotFound() {
        StudentRequest request = new StudentRequest();
        request.setStudentName("Chamasha");
        request.setCourseId(99L);

        when(courseRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> studentService.create(request));
        assertEquals("Course not found with id: 99", ex.getMessage());

        verify(courseRepository, times(1)).findById(99L);
        verify(studentRepository, never()).save(any());
    }

    @Test
    void getAllStudents_ReturnsList() {
        Student student1 = new Student();
        student1.setId(1L);
        student1.setStudentName("Alice");
        student1.setCourse(course);

        Student student2 = new Student();
        student2.setId(2L);
        student2.setStudentName("Bob");
        student2.setCourse(null);

        when(studentRepository.findAll()).thenReturn(Arrays.asList(student1, student2));

        List<StudentResponse> responseList = studentService.getAll();

        assertEquals(2, responseList.size());

        assertEquals("Alice", responseList.get(0).getStudentName());
        assertEquals("Computer Science", responseList.get(0).getCourseName());

        assertEquals("Bob", responseList.get(1).getStudentName());
        assertNull(responseList.get(1).getCourseName());

        verify(studentRepository, times(1)).findAll();
    }

    @Test
    void getAllStudents_ReturnsEmptyList() {
        when(studentRepository.findAll()).thenReturn(Collections.emptyList());

        List<StudentResponse> responseList = studentService.getAll();

        assertTrue(responseList.isEmpty());
        verify(studentRepository, times(1)).findAll();
    }

    @Test
    void getById_Success() {
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));

        StudentResponse response = studentService.getById(1L);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("Chamasha", response.getStudentName());
        assertEquals("Computer Science", response.getCourseName());

        verify(studentRepository, times(1)).findById(1L);
    }

    @Test
    void getById_NotFound() {
        when(studentRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> studentService.getById(99L));

        assertEquals("Student not found with id: 99", ex.getMessage());
        verify(studentRepository, times(1)).findById(99L);
    }

    @Test
    void deleteStudent_Success() {
        when(studentRepository.existsById(1L)).thenReturn(true);
        doNothing().when(studentRepository).deleteById(1L);

        assertDoesNotThrow(() -> studentService.delete(1L));

        verify(studentRepository, times(1)).existsById(1L);
        verify(studentRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteStudent_NotFound() {
        when(studentRepository.existsById(88L)).thenReturn(false);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> studentService.delete(88L));

        assertEquals("Student not found with id: 88", ex.getMessage());

        verify(studentRepository, times(1)).existsById(88L);
        verify(studentRepository, never()).deleteById(anyLong());
    }
}