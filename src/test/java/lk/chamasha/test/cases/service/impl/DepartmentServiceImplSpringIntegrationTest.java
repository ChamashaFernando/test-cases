package lk.chamasha.test.cases.service.impl;

import lk.chamasha.test.cases.controller.request.DepartmentRequest;
import lk.chamasha.test.cases.exception.DepartmentNotFoundException;
import lk.chamasha.test.cases.model.Department;
import lk.chamasha.test.cases.repository.DepartmentRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")  // ensure test profile or in-memory db
class DepartmentServiceImplSpringIntegrationTest {

    @Autowired
    private DepartmentServiceImpl departmentService;

    @Autowired
    private DepartmentRepository departmentRepository;

    @BeforeEach
    void setUp() {
        departmentRepository.deleteAll();  // clear db before each test
    }

    @Test
    @DisplayName("Test creating a department")
    void testCreateDepartment() {
        DepartmentRequest request = new DepartmentRequest();
        request.setDepartmentName("Engineering");

        var response = departmentService.createDepartment(request);

        assertNotNull(response);
        assertNotNull(response.getId());
        assertEquals("Engineering", response.getDepartmentName());
    }

    @Test
    @DisplayName("Test retrieving department by ID")
    void testGetDepartmentById() throws DepartmentNotFoundException {
        DepartmentRequest request = new DepartmentRequest();
        request.setDepartmentName("Science");

        var created = departmentService.createDepartment(request);
        var response = departmentService.getDepartmentById(created.getId());

        assertNotNull(response);
        assertEquals("Science", response.getDepartmentName());
    }

    @Test
    @DisplayName("Test retrieving department by invalid ID")
    void testGetDepartmentByInvalidId() {
        Long invalidId = 999L;
        assertThrows(DepartmentNotFoundException.class, () -> departmentService.getDepartmentById(invalidId));
    }

    @Test
    @DisplayName("Test getting all departments")
    void testGetAllDepartments() {
        departmentService.createDepartment(new DepartmentRequest("IT"));
        departmentService.createDepartment(new DepartmentRequest("HR"));

        List<?> departments = departmentService.getAllDepartments();

        assertEquals(2, departments.size());
    }

    @Test
    @DisplayName("Test deleting a department")
    void testDeleteDepartment() throws DepartmentNotFoundException {
        var request = new DepartmentRequest("Marketing");
        var response = departmentService.createDepartment(request);

        departmentService.deleteDepartment(response.getId());

        assertThrows(DepartmentNotFoundException.class, () -> departmentService.getDepartmentById(response.getId()));
    }

    @Test
    @DisplayName("Test deleting with invalid department ID")
    void testDeleteDepartmentInvalidId() {
        Long invalidId = 888L;
        assertThrows(DepartmentNotFoundException.class, () -> departmentService.deleteDepartment(invalidId));
    }
}
