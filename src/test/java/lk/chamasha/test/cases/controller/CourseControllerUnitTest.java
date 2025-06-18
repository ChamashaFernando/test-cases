package lk.chamasha.test.cases.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lk.chamasha.test.cases.controller.request.CourseRequest;
import lk.chamasha.test.cases.controller.response.CourseResponse;
import lk.chamasha.test.cases.service.CourseService;
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

@WebMvcTest(controllers = CourseController.class)
class CourseControllerUnitTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CourseService courseService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Create new course - Unit Test")
    void testCreateCourse() throws Exception {
        CourseRequest request = new CourseRequest("Mathematics", 1L);
        CourseResponse response = new CourseResponse(1L, "Mathematics", "Science");

        Mockito.when(courseService.create(Mockito.any(CourseRequest.class))).thenReturn(response);

        mockMvc.perform(
                        post("/api/courses")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                                .header("X-Api-Version", "v1")
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.courseName").value("Mathematics"))
                .andExpect(jsonPath("$.departmentName").value("Science"));
    }

    @Test
    @DisplayName("Get all courses - Unit Test")
    void testGetAllCourses() throws Exception {
        CourseResponse response = new CourseResponse(1L, "Physics", "Science");

        Mockito.when(courseService.getAll()).thenReturn(Collections.singletonList(response));

        mockMvc.perform(
                        get("/api/courses")
                                .header("X-Api-Version", "v1")
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].courseName").value("Physics"));
    }

    @Test
    @DisplayName("Get course by ID - Unit Test")
    void testGetCourseById() throws Exception {
        CourseResponse response = new CourseResponse(2L, "Chemistry", "Science");

        Mockito.when(courseService.getById(2L)).thenReturn(response);

        mockMvc.perform(
                        get("/api/courses/2")
                                .header("X-Api-Version", "v1")
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$.courseName").value("Chemistry"))
                .andExpect(jsonPath("$.departmentName").value("Science"));
    }

    @Test
    @DisplayName("Delete course by ID - Unit Test")
    void testDeleteCourse() throws Exception {
        Mockito.doNothing().when(courseService).delete(3L);

        mockMvc.perform(
                delete("/api/courses/3")
                        .header("X-Api-Version", "v1")
        ).andExpect(status().isOk());
    }
}