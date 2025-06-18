package lk.chamasha.test.cases.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lk.chamasha.test.cases.controller.request.DepartmentRequest;
import lk.chamasha.test.cases.controller.response.DepartmentResponse;
import lk.chamasha.test.cases.exception.DepartmentNotCreatedException;
import lk.chamasha.test.cases.service.DepartmentService;
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

@WebMvcTest(controllers = DepartmentController.class)
class DepartmentControllerUnitTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private DepartmentService departmentService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Create new department - Unit Test")
    void testCreateDepartment() throws Exception {
        DepartmentRequest request = new DepartmentRequest("IT");
        DepartmentResponse response = new DepartmentResponse(1L, "IT");

        Mockito.when(departmentService.create(Mockito.any(DepartmentRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/departments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header("X-Api-Version", "v1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.departmentName").value("IT"));
    }

    @Test
    @DisplayName("Get all departments - Unit Test")
    void testGetAllDepartments() throws Exception {
        DepartmentResponse response = new DepartmentResponse(1L, "Engineering");

        Mockito.when(departmentService.getAll()).thenReturn(Collections.singletonList(response));

        mockMvc.perform(get("/api/departments")
                        .header("X-Api-Version", "v1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].departmentName").value("Engineering"));
    }

    @Test
    @DisplayName("Get department by ID - Unit Test")
    void testGetDepartmentById() throws Exception {
        DepartmentResponse response = new DepartmentResponse(2L, "Science");

        Mockito.when(departmentService.getById(2L)).thenReturn(response);

        mockMvc.perform(get("/api/departments/2")
                        .header("X-Api-Version", "v1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.departmentName").value("Science"));
    }

    @Test
    @DisplayName("Delete department by ID - Unit Test")
    void testDeleteDepartment() throws Exception {
        Mockito.doNothing().when(departmentService).delete(3L);

        mockMvc.perform(delete("/api/departments/3")
                        .header("X-Api-Version", "v1"))
                .andExpect(status().isOk());
    }
}
