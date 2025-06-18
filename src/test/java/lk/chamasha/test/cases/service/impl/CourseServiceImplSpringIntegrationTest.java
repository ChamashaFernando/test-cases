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
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class CourseServiceImplSpringIntegrationTest {
    @Autowired
    private CourseServiceImpl courseService;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    private Department department;

    @BeforeEach
    void setup() {
        courseRepository.deleteAll();
        departmentRepository.deleteAll();

        department = new Department();
        department.setDepartmentName("IT");
        department = departmentRepository.save(department);
    }

    @Test
    void testCreateCourse_Success() throws Exception {
        CourseRequest request = new CourseRequest();
        request.setCourseName("Data Structures");
        request.setDepartmentId(department.getId());

        CourseResponse response = courseService.create(request);

        assertNotNull(response);
        assertEquals("Data Structures", response.getCourseName());
        assertEquals("IT", response.getDepartmentName());
    }

    @Test
    void testCreateCourse_DepartmentNotFound() {
        final Long departmentId = 999L;
        CourseRequest request = new CourseRequest();
        request.setCourseName("Algorithms");
        request.setDepartmentId(departmentId);

        assertThrows(CourseNotCreatedException.class, () -> courseService.create(request));
    }

    @Test
    void testGetAllCourses() {
        Course course1 = new Course();
        course1.setCourseName("OOP");
        course1.setDepartment(department);

        Course course2 = new Course();
        course2.setCourseName("DBMS");
        course2.setDepartment(department);

        courseRepository.saveAll(Arrays.asList(course1, course2));

        List<CourseResponse> courses = courseService.getAll();
        assertEquals(2, courses.size());
    }

    @Test
    void testGetAllCourses_Empty() {
        List<CourseResponse> courses = courseService.getAll();
        assertTrue(courses.isEmpty());
    }

    @Test
    void testGetCourseById_Success() throws Exception {
        Course course = new Course();
        course.setCourseName("Networking");
        course.setDepartment(department);
        course = courseRepository.save(course);

        CourseResponse response = courseService.getById(course.getId());
        assertNotNull(response);
        assertEquals("Networking", response.getCourseName());
    }

    @Test
    void testGetCourseById_NotFound() {
        assertThrows(CourseNotFoundException.class, () -> courseService.getById(999L));
    }

    @Test
    void testDeleteCourse_Success() throws Exception {
        Course course = new Course();
        course.setCourseName("Artificial Intelligence");
        course.setDepartment(department);
        course = courseRepository.save(course);

        Long courseId = course.getId();

        assertDoesNotThrow(() -> courseService.delete(courseId));

        assertFalse(courseRepository.existsById(courseId));
    }

    @Test
    void testDeleteCourse_NotFound() {
        assertThrows(CourseNotFoundException.class, () -> courseService.delete(999L));
    }
}