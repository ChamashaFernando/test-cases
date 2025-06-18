package lk.chamasha.test.cases.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lk.chamasha.test.cases.controller.request.StudentRequest;
import lk.chamasha.test.cases.model.Course;
import lk.chamasha.test.cases.model.Student;
import lk.chamasha.test.cases.repository.CourseRepository;
import lk.chamasha.test.cases.repository.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
class StudentControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Long courseId;

    @BeforeEach
    void setUp() {
        studentRepository.deleteAll();
        courseRepository.deleteAll();

        Course course = new Course();
        course.setCourseName("Artificial Intelligence");
        courseId = courseRepository.save(course).getId();
    }

    @Test
    @DisplayName("Create student - Integration Test")
    void testCreateStudent() throws Exception {
        StudentRequest request = new StudentRequest("Nimali", courseId);

        mockMvc.perform(post("/api/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header("X-Api-Version", "v1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.studentName").value("Nimali"))
                .andExpect(jsonPath("$.courseName").value("Artificial Intelligence"));
    }

    @Test
    @DisplayName("Get all students - Integration Test")
    void testGetAllStudents() throws Exception {
        Student student = new Student();
        student.setStudentName("Saman");
        student.setCourse(courseRepository.findById(courseId).orElseThrow());
        studentRepository.save(student);

        mockMvc.perform(get("/api/students")
                        .header("X-Api-Version", "v1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].studentName").value("Saman"));
    }

    @Test
    @DisplayName("Get student by ID - Integration Test")
    void testGetStudentById() throws Exception {
        Student student = new Student();
        student.setStudentName("Kamali");
        student.setCourse(courseRepository.findById(courseId).orElseThrow());
        Long studentId = studentRepository.save(student).getId();

        mockMvc.perform(get("/api/students/" + studentId)
                        .header("X-Api-Version", "v1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.studentName").value("Kamali"))
                .andExpect(jsonPath("$.courseName").value("Artificial Intelligence"));
    }

    @Test
    @DisplayName("Delete student by ID - Integration Test")
    void testDeleteStudentById() throws Exception {
        Student student = new Student();
        student.setStudentName("Tharindu");
        student.setCourse(courseRepository.findById(courseId).orElseThrow());
        Long studentId = studentRepository.save(student).getId();

        mockMvc.perform(delete("/api/students/" + studentId)
                        .header("X-Api-Version", "v1"))
                .andExpect(status().isOk());
    }
}
