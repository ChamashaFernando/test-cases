package lk.chamasha.test.cases.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lk.chamasha.test.cases.controller.request.DepartmentRequest;
import lk.chamasha.test.cases.model.Department;
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

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
public class DepartmentControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Long departmentId;

    @BeforeEach
    void setUp() {
        departmentRepository.deleteAll();

        Department dept = new Department();
        dept.setDepartmentName("Engineering");
        departmentId = departmentRepository.save(dept).getId();
    }

    @Test
    @DisplayName("Create new department - Integration Test")
    void testCreateDepartment() throws Exception {
        DepartmentRequest request = new DepartmentRequest("Business");

        mockMvc.perform(post("/api/departments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header("X-Api-Version", "v1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.departmentName").value("Business"));
    }

    @Test
    @DisplayName("Get all departments - Integration Test")
    void testGetAllDepartments() throws Exception {
        mockMvc.perform(get("/api/departments")
                        .header("X-Api-Version", "v1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", greaterThanOrEqualTo(1)))
                .andExpect(jsonPath("$[0].departmentName", not(emptyOrNullString())));
    }

    @Test
    @DisplayName("Get department by ID - Integration Test")
    void testGetDepartmentById() throws Exception {
        mockMvc.perform(get("/api/departments/" + departmentId)
                        .header("X-Api-Version", "v1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(departmentId))
                .andExpect(jsonPath("$.departmentName").value("Engineering"));
    }

    @Test
    @DisplayName("Delete department by ID - Integration Test")
    void testDeleteDepartmentById() throws Exception {
        mockMvc.perform(delete("/api/departments/" + departmentId)
                        .header("X-Api-Version", "v1"))
                .andExpect(status().isOk());
    }
}
