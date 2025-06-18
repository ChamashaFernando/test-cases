package lk.chamasha.test.cases.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lk.chamasha.test.cases.controller.request.CourseRequest;
import lk.chamasha.test.cases.model.Course;
import lk.chamasha.test.cases.model.Department;
import lk.chamasha.test.cases.repository.CourseRepository;
import lk.chamasha.test.cases.repository.DepartmentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
public class CourseControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Long departmentId;

    @BeforeEach
    void setUp() {
        courseRepository.deleteAll();
        departmentRepository.deleteAll();

        // Create and save a Department for course requests
        Department department = new Department();
        department.setDepartmentName("Computer Science");
        departmentId = departmentRepository.save(department).getId();
    }

    @Test
    @DisplayName("Create a new course - Integration Test")
    void testCreateCourse() throws Exception {
        CourseRequest request = new CourseRequest("Software Engineering", departmentId);

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/courses")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                                .header("X-Api-Version", "v1")
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$.courseName").value("Software Engineering"))
                .andExpect(jsonPath("$.departmentName").value("Computer Science"));
    }

    @Test
    @DisplayName("Get all courses - Integration Test")
    void testGetAllCourses() throws Exception {
        Course course = new Course();
        course.setCourseName("Database Systems");
        course.setDepartment(departmentRepository.findById(departmentId).orElseThrow());
        courseRepository.save(course);

        mockMvc.perform(
                        MockMvcRequestBuilders.get("/api/courses")
                                .header("X-Api-Version", "v1")
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].courseName").value("Database Systems"))
                .andExpect(jsonPath("$[0].departmentName").value("Computer Science"));
    }

    @Test
    @DisplayName("Get course by ID - Integration Test")
    void testGetCourseById() throws Exception {
        Course course = new Course();
        course.setCourseName("AI Fundamentals");
        course.setDepartment(departmentRepository.findById(departmentId).orElseThrow());
        Long courseId = courseRepository.save(course).getId();

        mockMvc.perform(
                        MockMvcRequestBuilders.get("/api/courses/" + courseId)
                                .header("X-Api-Version", "v1")
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$.courseName").value("AI Fundamentals"))
                .andExpect(jsonPath("$.departmentName").value("Computer Science"));
    }

    @Test
    @DisplayName("Delete course by ID - Integration Test")
    void testDeleteCourseById() throws Exception {
        Course course = new Course();
        course.setCourseName("Networking");
        course.setDepartment(departmentRepository.findById(departmentId).orElseThrow());
        Long courseId = courseRepository.save(course).getId();

        mockMvc.perform(
                MockMvcRequestBuilders.delete("/api/courses/" + courseId)
                        .header("X-Api-Version", "v1")
        ).andExpect(status().isOk());
    }
}