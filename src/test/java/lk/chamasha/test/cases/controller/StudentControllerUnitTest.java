package lk.chamasha.test.cases.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lk.chamasha.test.cases.controller.request.StudentRequest;
import lk.chamasha.test.cases.controller.response.StudentResponse;
import lk.chamasha.test.cases.service.StudentService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = StudentController.class)
class StudentControllerUnitTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private StudentService studentService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Create new student - Unit Test")
    void testCreateStudent() throws Exception {
        StudentRequest request = new StudentRequest("Nimal", 1L);
        StudentResponse response = new StudentResponse(1L, "Nimal", "200012345678");

        Mockito.when(studentService.create(Mockito.any(StudentRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header("X-Api-Version", "v1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.studentName").value("Nimal"))
                .andExpect(jsonPath("$.courseName").value("200012345678"));
    }

    @Test
    @DisplayName("Get all students - Unit Test")
    void testGetAllStudents() throws Exception {
        StudentResponse response = new StudentResponse(1L, "Sunil", "123456789V");

        Mockito.when(studentService.getAll()).thenReturn(Collections.singletonList(response));

        mockMvc.perform(get("/api/students")
                        .header("X-Api-Version", "v1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].studentName").value("Sunil"))
                .andExpect(jsonPath("$[0].courseName").value("123456789V"));
    }

    @Test
    @DisplayName("Get student by ID - Unit Test")
    void testGetStudentById() throws Exception {
        StudentResponse response = new StudentResponse(2L, "Kamal", "987654321V");

        Mockito.when(studentService.getById(2L)).thenReturn(response);

        mockMvc.perform(get("/api/students/2")
                        .header("X-Api-Version", "v1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2L))
                .andExpect(jsonPath("$.studentName").value("Kamal"))
                .andExpect(jsonPath("$.courseName").value("987654321V"));
    }

    @Test
    @DisplayName("Delete student by ID - Unit Test")
    void testDeleteStudent() throws Exception {
        Mockito.doNothing().when(studentService).delete(3L);

        mockMvc.perform(delete("/api/students/3")
                        .header("X-Api-Version", "v1"))
                .andExpect(status().isOk());
    }
}
