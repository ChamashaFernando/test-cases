package lk.chamasha.test.cases.service.impl;

import lk.chamasha.test.cases.controller.request.CourseRequest;
import lk.chamasha.test.cases.controller.response.CourseResponse;
import lk.chamasha.test.cases.exception.CourseNotCreatedException;
import lk.chamasha.test.cases.exception.CourseNotFoundException;
import lk.chamasha.test.cases.model.Course;
import lk.chamasha.test.cases.model.Department;
import lk.chamasha.test.cases.repository.CourseRepository;
import lk.chamasha.test.cases.repository.DepartmentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CourseServiceImplSpringUnitTest {

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private DepartmentRepository departmentRepository;

    @InjectMocks
    private CourseServiceImpl courseService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Create course successfully")
    void testCreateCourseSuccess() throws CourseNotCreatedException {
        Department dept = new Department();
        dept.setId(1L);
        dept.setDepartmentName("Engineering");

        CourseRequest request = new CourseRequest();
        request.setCourseName("Computer Science");
        request.setDepartmentId(1L);

        when(departmentRepository.findById(1L)).thenReturn(Optional.of(dept));

        Course savedCourse = new Course();
        savedCourse.setId(10L);
        savedCourse.setCourseName("Computer Science");
        savedCourse.setDepartment(dept);

        when(courseRepository.save(any(Course.class))).thenReturn(savedCourse);

        CourseResponse response = courseService.createCourse(request);

        assertNotNull(response);
        assertEquals(10L, response.getId());
        assertEquals("Computer Science", response.getCourseName());
        assertEquals("Engineering", response.getDepartmentName());

        verify(departmentRepository).findById(1L);
        verify(courseRepository).save(any(Course.class));
    }

    @Test
    @DisplayName("Create course throws exception when department not found")
    void testCreateCourseDepartmentNotFound() {
        CourseRequest request = new CourseRequest();
        request.setCourseName("Math");
        request.setDepartmentId(99L);

        when(departmentRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(CourseNotCreatedException.class, () -> courseService.createCourse(request));

        verify(departmentRepository).findById(99L);
        verify(courseRepository, never()).save(any());
    }

    @Test
    @DisplayName("Get course by id successfully")
    void testGetCourseByIdSuccess() throws CourseNotFoundException {
        Department dept = new Department();
        dept.setDepartmentName("Science");

        Course course = new Course();
        course.setId(5L);
        course.setCourseName("Physics");
        course.setDepartment(dept);

        when(courseRepository.findById(5L)).thenReturn(Optional.of(course));

        CourseResponse response = courseService.getCourseById(5L);

        assertNotNull(response);
        assertEquals(5L, response.getId());
        assertEquals("Physics", response.getCourseName());
        assertEquals("Science", response.getDepartmentName());

        verify(courseRepository).findById(5L);
    }

    @Test
    @DisplayName("Get course by id throws exception when not found")
    void testGetCourseByIdNotFound() {
        when(courseRepository.findById(100L)).thenReturn(Optional.empty());

        assertThrows(CourseNotFoundException.class, () -> courseService.getCourseById(100L));

        verify(courseRepository).findById(100L);
    }

    @Test
    @DisplayName("Delete course successfully")
    void testDeleteCourseSuccess() throws CourseNotFoundException {
        when(courseRepository.existsById(1L)).thenReturn(true);
        doNothing().when(courseRepository).deleteById(1L);

        assertDoesNotThrow(() -> courseService.deleteCourse(1L));

        verify(courseRepository).existsById(1L);
        verify(courseRepository).deleteById(1L);
    }

    @Test
    @DisplayName("Delete course throws exception when not found")
    void testDeleteCourseNotFound() {
        when(courseRepository.existsById(2L)).thenReturn(false);

        assertThrows(CourseNotFoundException.class, () -> courseService.deleteCourse(2L));

        verify(courseRepository).existsById(2L);
        verify(courseRepository, never()).deleteById(any());
    }

    @Test
    @DisplayName("Get all courses")
    void testGetAllCourses() {
        Department dept1 = new Department();
        dept1.setDepartmentName("Science");
        Department dept2 = new Department();
        dept2.setDepartmentName("Arts");

        Course course1 = new Course();
        course1.setId(1L);
        course1.setCourseName("Physics");
        course1.setDepartment(dept1);

        Course course2 = new Course();
        course2.setId(2L);
        course2.setCourseName("History");
        course2.setDepartment(dept2);

        when(courseRepository.findAll()).thenReturn(List.of(course1, course2));

        List<CourseResponse> responses = courseService.getAllCourses();

        assertEquals(2, responses.size());
        assertTrue(responses.stream().anyMatch(r -> r.getCourseName().equals("Physics") && r.getDepartmentName().equals("Science")));
        assertTrue(responses.stream().anyMatch(r -> r.getCourseName().equals("History") && r.getDepartmentName().equals("Arts")));

        verify(courseRepository).findAll();
    }
}
