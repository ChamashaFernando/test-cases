package lk.chamasha.test.cases.service.impl;

import lk.chamasha.test.cases.controller.request.DepartmentRequest;
import lk.chamasha.test.cases.controller.response.DepartmentResponse;
import lk.chamasha.test.cases.exception.DepartmentNotFoundException;
import lk.chamasha.test.cases.model.Department;
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

class DepartmentServiceImpSpringUnitTest {

    @Mock
    private DepartmentRepository departmentRepository;

    @InjectMocks
    private DepartmentServiceImpl departmentService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Create department successfully")
    void testCreateDepartment() {
        DepartmentRequest request = new DepartmentRequest();
        request.setDepartmentName("Computer Science");

        Department savedDept = new Department();
        savedDept.setId(1L);
        savedDept.setDepartmentName("Computer Science");

        when(departmentRepository.save(any(Department.class))).thenReturn(savedDept);

        DepartmentResponse response = departmentService.createDepartment(request);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("Computer Science", response.getDepartmentName());

        verify(departmentRepository).save(any(Department.class));
    }

    @Test
    @DisplayName("Get all departments")
    void testGetAllDepartments() {
        Department dept1 = new Department();
        dept1.setId(1L);
        dept1.setDepartmentName("Science");

        Department dept2 = new Department();
        dept2.setId(2L);
        dept2.setDepartmentName("Arts");

        when(departmentRepository.findAll()).thenReturn(List.of(dept1, dept2));

        List<DepartmentResponse> responses = departmentService.getAllDepartments();

        assertEquals(2, responses.size());
        assertTrue(responses.stream().anyMatch(d -> d.getDepartmentName().equals("Science")));
        assertTrue(responses.stream().anyMatch(d -> d.getDepartmentName().equals("Arts")));

        verify(departmentRepository).findAll();
    }

    @Test
    @DisplayName("Get department by id successfully")
    void testGetDepartmentByIdSuccess() throws DepartmentNotFoundException {
        Department dept = new Department();
        dept.setId(5L);
        dept.setDepartmentName("Mathematics");

        when(departmentRepository.findById(5L)).thenReturn(Optional.of(dept));

        DepartmentResponse response = departmentService.getDepartmentById(5L);

        assertNotNull(response);
        assertEquals(5L, response.getId());
        assertEquals("Mathematics", response.getDepartmentName());

        verify(departmentRepository).findById(5L);
    }

    @Test
    @DisplayName("Get department by id throws exception when not found")
    void testGetDepartmentByIdNotFound() {
        when(departmentRepository.findById(100L)).thenReturn(Optional.empty());

        assertThrows(DepartmentNotFoundException.class, () -> departmentService.getDepartmentById(100L));

        verify(departmentRepository).findById(100L);
    }

    @Test
    @DisplayName("Delete department successfully")
    void testDeleteDepartmentSuccess() throws DepartmentNotFoundException {
        when(departmentRepository.existsById(1L)).thenReturn(true);
        doNothing().when(departmentRepository).deleteById(1L);

        assertDoesNotThrow(() -> departmentService.deleteDepartment(1L));

        verify(departmentRepository).existsById(1L);
        verify(departmentRepository).deleteById(1L);
    }

    @Test
    @DisplayName("Delete department throws exception when not found")
    void testDeleteDepartmentNotFound() {
        when(departmentRepository.existsById(2L)).thenReturn(false);

        assertThrows(DepartmentNotFoundException.class, () -> departmentService.deleteDepartment(2L));

        verify(departmentRepository).existsById(2L);
        verify(departmentRepository, never()).deleteById(any());
    }
}
