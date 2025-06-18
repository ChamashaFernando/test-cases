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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CourseServiceImplSpringUnitTest {

    @InjectMocks
    private CourseServiceImpl courseService;

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private DepartmentRepository departmentRepository;

    private Department department;

    @BeforeEach
    void setUp() {
        department = new Department();
        department.setId(1L);
        department.setDepartmentName("IT");
    }

    // ===== create() tests =====

    @Test
    void createCourse_Success() throws CourseNotCreatedException {
        CourseRequest request = new CourseRequest();
        request.setCourseName("Data Structures");
        request.setDepartmentId(department.getId());

        Mockito.when(departmentRepository.findById(department.getId())).thenReturn(Optional.of(department));

        Course savedCourse = new Course();
        savedCourse.setId(10L);
        savedCourse.setCourseName(request.getCourseName());
        savedCourse.setDepartment(department);

        Mockito.when(courseRepository.save(Mockito.any(Course.class))).thenReturn(savedCourse);

        CourseResponse response = courseService.create(request);

        assertNotNull(response);
        assertEquals(savedCourse.getId(), response.getId());
        assertEquals("Data Structures", response.getCourseName());
        assertEquals("IT", response.getDepartmentName());

        Mockito.verify(departmentRepository, Mockito.times(1)).findById(department.getId());
        Mockito.verify(courseRepository, Mockito.times(1)).save(Mockito.any(Course.class));
    }

    @Test
    void createCourse_DepartmentNotFound() {
        Long invalidDeptId = 99L;

        CourseRequest request = new CourseRequest();
        request.setCourseName("Algorithms");
        request.setDepartmentId(invalidDeptId);

        Mockito.when(departmentRepository.findById(invalidDeptId)).thenReturn(Optional.empty());

        CourseNotCreatedException thrown = assertThrows(CourseNotCreatedException.class, () -> {
            courseService.create(request);
        });

        assertEquals("Department not found with id: " + invalidDeptId, thrown.getMessage());
        Mockito.verify(departmentRepository, Mockito.times(1)).findById(invalidDeptId);
        Mockito.verify(courseRepository, Mockito.never()).save(Mockito.any());
    }

    // ===== getAll() tests =====

    @Test
    void getAllCourses_ReturnsList() {
        Course c1 = new Course();
        c1.setId(1L);
        c1.setCourseName("OOP");
        c1.setDepartment(department);

        Course c2 = new Course();
        c2.setId(2L);
        c2.setCourseName("DBMS");
        c2.setDepartment(department);

        Mockito.when(courseRepository.findAll()).thenReturn(Arrays.asList(c1, c2));

        List<CourseResponse> result = courseService.getAll();

        assertEquals(2, result.size());
        assertEquals("OOP", result.get(0).getCourseName());
        assertEquals("DBMS", result.get(1).getCourseName());

        Mockito.verify(courseRepository, Mockito.times(1)).findAll();
    }

    @Test
    void getAllCourses_ReturnsEmptyList() {
        Mockito.when(courseRepository.findAll()).thenReturn(Arrays.asList());

        List<CourseResponse> result = courseService.getAll();

        assertTrue(result.isEmpty());

        Mockito.verify(courseRepository, Mockito.times(1)).findAll();
    }

    // ===== getById() tests =====

    @Test
    void getById_Success() throws CourseNotFoundException {
        Long courseId = 1L;

        Course course = new Course();
        course.setId(courseId);
        course.setCourseName("Networking");
        course.setDepartment(department);

        Mockito.when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));

        CourseResponse response = courseService.getById(courseId);

        assertNotNull(response);
        assertEquals(courseId, response.getId());
        assertEquals("Networking", response.getCourseName());
        assertEquals("IT", response.getDepartmentName());

        Mockito.verify(courseRepository, Mockito.times(1)).findById(courseId);
    }

    @Test
    void getById_NotFound() {
        Long invalidId = 99L;

        Mockito.when(courseRepository.findById(invalidId)).thenReturn(Optional.empty());

        CourseNotFoundException thrown = assertThrows(CourseNotFoundException.class, () -> {
            courseService.getById(invalidId);
        });

        assertEquals("Course not found with id: " + invalidId, thrown.getMessage());
        Mockito.verify(courseRepository, Mockito.times(1)).findById(invalidId);
    }

    // ===== delete() tests =====

    @Test
    void delete_Success() throws Exception {
        Long courseId = 1L;

        Mockito.when(courseRepository.existsById(courseId)).thenReturn(true);
        Mockito.doNothing().when(courseRepository).deleteById(courseId);

        assertDoesNotThrow(() -> courseService.delete(courseId));

        Mockito.verify(courseRepository, Mockito.times(1)).existsById(courseId);
        Mockito.verify(courseRepository, Mockito.times(1)).deleteById(courseId);
    }

    @Test
    void delete_NotFound() {
        Long invalidId = 99L;

        Mockito.when(courseRepository.existsById(invalidId)).thenReturn(false);

        CourseNotFoundException thrown = assertThrows(CourseNotFoundException.class, () -> {
            courseService.delete(invalidId);
        });

        assertEquals("Course not found with id: " + invalidId, thrown.getMessage());
        Mockito.verify(courseRepository, Mockito.times(1)).existsById(invalidId);
        Mockito.verify(courseRepository, Mockito.never()).deleteById(Mockito.anyLong());
    }
}