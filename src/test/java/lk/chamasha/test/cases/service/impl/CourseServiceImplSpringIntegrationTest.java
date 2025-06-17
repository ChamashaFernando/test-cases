package lk.chamasha.test.cases.service.impl;

import lk.chamasha.test.cases.controller.request.CourseRequest;
import lk.chamasha.test.cases.controller.response.CourseResponse;
import lk.chamasha.test.cases.exception.CourseNotCreatedException;
import lk.chamasha.test.cases.exception.CourseNotFoundException;
import lk.chamasha.test.cases.model.Department;
import lk.chamasha.test.cases.repository.CourseRepository;
import lk.chamasha.test.cases.repository.DepartmentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

@SpringBootTest
@Transactional
public class CourseServiceImplSpringIntegrationTest {

    @Autowired
    private CourseServiceImpl courseService;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private CourseRepository courseRepository;

    private Department savedDepartment;

    @BeforeEach
    public void setup() {
        // Department එක create කරමු test cases වලට
        Department dept = new Department();
        dept.setDepartmentName("Computer Science");
        savedDepartment = departmentRepository.save(dept);
    }

    @Test
    public void testCreateCourseSuccess() throws CourseNotCreatedException {
        CourseRequest request = new CourseRequest();
        request.setCourseName("Data Structures");
        request.setDepartmentId(savedDepartment.getId());

        CourseResponse response = courseService.createCourse(request);

        assertNotNull(response);
        assertEquals("Data Structures", response.getCourseName());
        assertEquals("Computer Science", response.getDepartmentName());

        assertTrue(courseRepository.existsById(response.getId()));
    }

    @Test
    public void testCreateCourseDepartmentNotFound() {
        CourseRequest request = new CourseRequest();
        request.setCourseName("Algorithms");
        request.setDepartmentId(999L);  // නොමැති departmentId එකක්

        Exception exception = assertThrows(CourseNotCreatedException.class, () -> {
            courseService.createCourse(request);
        });

        assertTrue(exception.getMessage().contains("Department not found"));
    }

    @Test
    public void testGetAllCourses() throws CourseNotCreatedException {
        // Create 2 courses
        CourseRequest req1 = new CourseRequest();
        req1.setCourseName("Operating Systems");
        req1.setDepartmentId(savedDepartment.getId());
        courseService.createCourse(req1);

        CourseRequest req2 = new CourseRequest();
        req2.setCourseName("Networks");
        req2.setDepartmentId(savedDepartment.getId());
        courseService.createCourse(req2);

        List<CourseResponse> courses = courseService.getAllCourses();
        assertNotNull(courses);
        assertTrue(courses.size() >= 2);
    }

    @Test
    public void testGetCourseByIdSuccess() throws CourseNotFoundException, CourseNotCreatedException {
        CourseRequest request = new CourseRequest();
        request.setCourseName("Database Systems");
        request.setDepartmentId(savedDepartment.getId());
        CourseResponse created = courseService.createCourse(request);

        CourseResponse fetched = courseService.getCourseById(created.getId());

        assertNotNull(fetched);
        assertEquals(created.getCourseName(), fetched.getCourseName());
        assertEquals(created.getDepartmentName(), fetched.getDepartmentName());
    }

    @Test
    public void testGetCourseByIdNotFound() {
        Exception exception = assertThrows(CourseNotFoundException.class, () -> {
            courseService.getCourseById(999L);  // නොමැති id
        });

        assertTrue(exception.getMessage().contains("Course not found"));
    }

    @Test
    public void testDeleteCourseSuccess() throws CourseNotFoundException, CourseNotCreatedException {
        CourseRequest request = new CourseRequest();
        request.setCourseName("Software Engineering");
        request.setDepartmentId(savedDepartment.getId());
        CourseResponse created = courseService.createCourse(request);

        assertTrue(courseRepository.existsById(created.getId()));

        courseService.deleteCourse(created.getId());

        assertFalse(courseRepository.existsById(created.getId()));
    }

    @Test
    public void testDeleteCourseNotFound() {
        Exception exception = assertThrows(CourseNotFoundException.class, () -> {
            courseService.deleteCourse(999L);
        });

        assertTrue(exception.getMessage().contains("Course not found"));
    }
}
