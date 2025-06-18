package lk.chamasha.test.cases.service.impl;

import lk.chamasha.test.cases.controller.request.DepartmentRequest;
import lk.chamasha.test.cases.controller.response.DepartmentResponse;
import lk.chamasha.test.cases.exception.DepartmentNotCreatedException;
import lk.chamasha.test.cases.exception.DepartmentNotFoundException;
import lk.chamasha.test.cases.model.Department;
import lk.chamasha.test.cases.repository.DepartmentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class DepartmentServiceImpSpringUnitTest {


    @InjectMocks
    private DepartmentServiceImpl departmentService; // service under test

    @Mock
    private DepartmentRepository departmentRepository; // mocked dependency

    private Department department;

    @BeforeEach
    void setUp() {
        department = new Department();
        department.setId(1L);
        department.setDepartmentName("Engineering");
    }

    @Test
    void createDepartment_Success() throws DepartmentNotCreatedException {
        DepartmentRequest request = new DepartmentRequest();
        request.setDepartmentName("Engineering");

        when(departmentRepository.findByDepartmentName("Engineering")).thenReturn(Optional.empty());

        Department saved = new Department();
        saved.setId(1L);
        saved.setDepartmentName("Engineering");

        when(departmentRepository.save(any(Department.class))).thenReturn(saved);

        DepartmentResponse response = departmentService.create(request);

        assertNotNull(response);
        assertEquals(saved.getId(), response.getId());
        assertEquals("Engineering", response.getDepartmentName());

        verify(departmentRepository, times(1)).findByDepartmentName("Engineering");
        verify(departmentRepository, times(1)).save(any(Department.class));
    }

    @Test
    void createDepartment_AlreadyExists() {
        DepartmentRequest request = new DepartmentRequest();
        request.setDepartmentName("Engineering");

        when(departmentRepository.findByDepartmentName("Engineering"))
                .thenReturn(Optional.of(department));

        DepartmentNotCreatedException ex = assertThrows(DepartmentNotCreatedException.class, () ->
                departmentService.create(request));

        assertEquals("Department is already registered with name: Engineering", ex.getMessage());
        verify(departmentRepository, times(1)).findByDepartmentName("Engineering");
        verify(departmentRepository, never()).save(any());
    }

    @Test
    void getAllDepartments_ReturnsList() {
        Department dept1 = new Department();
        dept1.setId(1L);
        dept1.setDepartmentName("Science");

        Department dept2 = new Department();
        dept2.setId(2L);
        dept2.setDepartmentName("Arts");

        when(departmentRepository.findAll()).thenReturn(Arrays.asList(dept1, dept2));

        List<DepartmentResponse> result = departmentService.getAll();

        assertEquals(2, result.size());
        assertEquals("Science", result.get(0).getDepartmentName());
        assertEquals("Arts", result.get(1).getDepartmentName());

        verify(departmentRepository, times(1)).findAll();
    }

    @Test
    void getAllDepartments_ReturnsEmptyList() {
        when(departmentRepository.findAll()).thenReturn(Collections.emptyList());

        List<DepartmentResponse> result = departmentService.getAll();

        assertTrue(result.isEmpty());
        verify(departmentRepository, times(1)).findAll();
    }

    @Test
    void getById_Success() throws DepartmentNotFoundException {
        when(departmentRepository.findById(1L)).thenReturn(Optional.of(department));

        DepartmentResponse response = departmentService.getById(1L);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("Engineering", response.getDepartmentName());

        verify(departmentRepository, times(1)).findById(1L);
    }

    @Test
    void getById_NotFound() {
        when(departmentRepository.findById(99L)).thenReturn(Optional.empty());

        DepartmentNotFoundException ex = assertThrows(DepartmentNotFoundException.class, () ->
                departmentService.getById(99L));

        assertEquals("Department not found with id: 99", ex.getMessage());
        verify(departmentRepository, times(1)).findById(99L);
    }

    @Test
    void deleteDepartment_Success() throws DepartmentNotFoundException {
        when(departmentRepository.existsById(1L)).thenReturn(true);
        doNothing().when(departmentRepository).deleteById(1L);

        assertDoesNotThrow(() -> departmentService.delete(1L));

        verify(departmentRepository, times(1)).existsById(1L);
        verify(departmentRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteDepartment_NotFound() {
        when(departmentRepository.existsById(88L)).thenReturn(false);

        DepartmentNotFoundException ex = assertThrows(DepartmentNotFoundException.class, () ->
                departmentService.delete(88L));

        assertEquals("Department not found with id: 88", ex.getMessage());
        verify(departmentRepository, times(1)).existsById(88L);
        verify(departmentRepository, never()).deleteById(anyLong());
    }
}