package lk.chamasha.test.cases.service.impl;

import jakarta.transaction.Transactional;
import lk.chamasha.test.cases.controller.request.DepartmentRequest;
import lk.chamasha.test.cases.controller.response.DepartmentResponse;
import lk.chamasha.test.cases.exception.DepartmentNotCreatedException;
import lk.chamasha.test.cases.exception.DepartmentNotFoundException;
import lk.chamasha.test.cases.model.Department;
import lk.chamasha.test.cases.repository.DepartmentRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@ActiveProfiles("test")
@Transactional
class DepartmentServiceImplSpringIntegrationTest {
    @Autowired
    private DepartmentServiceImpl departmentService;

    @Autowired
    private DepartmentRepository departmentRepository;

    @BeforeEach
    void setUp() {
        departmentRepository.deleteAll();
    }

    // ===== CREATE =====

    @Test
    void testCreateDepartment_Success() throws Exception {
        final DepartmentRequest request = new DepartmentRequest();
        request.setDepartmentName("Engineering");

        DepartmentResponse response = departmentService.create(request);

        assertNotNull(response);
        assertEquals("Engineering", response.getDepartmentName());
        assertTrue(departmentRepository.existsById(response.getId()));
    }

    @Test
    void testCreateDepartment_AlreadyExists() {
        Department dept = new Department();
        dept.setDepartmentName("Business");
        departmentRepository.save(dept);

        final DepartmentRequest request = new DepartmentRequest();
        request.setDepartmentName("Business");

        assertThrows(DepartmentNotCreatedException.class, () -> departmentService.create(request));
    }

    // ===== GET ALL =====

    @Test
    void testGetAllDepartments() {
        Department dept1 = new Department();
        dept1.setDepartmentName("Science");

        Department dept2 = new Department();
        dept2.setDepartmentName("Arts");

        departmentRepository.saveAll(Arrays.asList(dept1, dept2));

        List<DepartmentResponse> list = departmentService.getAll();

        assertEquals(2, list.size());
    }

    @Test
    void testGetAllDepartments_Empty() {
        List<DepartmentResponse> list = departmentService.getAll();
        assertTrue(list.isEmpty());
    }

    // ===== GET BY ID =====

    @Test
    void testGetById_Success() throws Exception {
        Department dept = new Department();
        dept.setDepartmentName("Math");
        dept = departmentRepository.save(dept);

        DepartmentResponse response = departmentService.getById(dept.getId());

        assertNotNull(response);
        assertEquals("Math", response.getDepartmentName());
    }

    @Test
    void testGetById_NotFound() {
        assertThrows(DepartmentNotFoundException.class, () -> departmentService.getById(1234L));
    }

    // ===== DELETE =====

    @Test
    void testDeleteDepartment_Success() throws Exception {
        // Arrange: Create and save a Department
        Department department = new Department();
        department.setDepartmentName("Physics");
        department = departmentRepository.save(department);

        final Long departmentId = department.getId();

        // Act & Assert: Call the service method and verify deletion
        assertDoesNotThrow(() -> departmentService.delete(departmentId));
        assertFalse(departmentRepository.existsById(departmentId));
    }

    @Test
    void testDelete_NotFound() {
        assertThrows(DepartmentNotFoundException.class, () -> departmentService.delete(9999L));
    }
}