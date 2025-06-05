//package lk.chamasha.test.cases.service.impl;
//
//import lk.chamasha.test.cases.controller.request.DepartmentRequest;
//import lk.chamasha.test.cases.controller.response.DepartmentResponse;
//import lk.chamasha.test.cases.repository.DepartmentRepository;
//import org.junit.jupiter.api.*;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.transaction.annotation.Transactional;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//import java.util.List;
//
//@SpringBootTest
//@Transactional
//class DepartmentServiceIntegrationTest {
//
//    @Autowired
//    private DepartmentServiceImpl departmentService;
//
//    @Autowired
//    private DepartmentRepository departmentRepository;
//
//    @Test
//    void createDepartment_Success() {
//        DepartmentRequest request = new DepartmentRequest();
//        request.setDepartmentName("IT");
//
//        DepartmentResponse response = departmentService.createDepartment(request);
//
//        assertNotNull(response.getId());
//        assertEquals("IT", response.getDepartmentName());
//        assertTrue(departmentRepository.existsById(response.getId()));
//    }
//
//    @Test
//    void createDepartment_Failure() {
//        DepartmentRequest request = new DepartmentRequest();
//        request.setDepartmentName(null); // assuming NOT NULL constraint in DB on departmentName
//
//        assertThrows(Exception.class, () -> {
//            departmentService.createDepartment(request);
//        });
//    }
//
//    @Test
//    void getAllDepartments_Success() {
//        DepartmentRequest request1 = new DepartmentRequest();
//        request1.setDepartmentName("IT");
//        departmentService.createDepartment(request1);
//
//        DepartmentRequest request2 = new DepartmentRequest();
//        request2.setDepartmentName("HR");
//        departmentService.createDepartment(request2);
//
//        List<DepartmentResponse> departments = departmentService.getAllDepartments();
//
//        assertTrue(departments.size() >= 2);
//    }
//
//    @Test
//    void getDepartmentById_Success() {
//        DepartmentRequest request = new DepartmentRequest();
//        request.setDepartmentName("IT");
//        DepartmentResponse created = departmentService.createDepartment(request);
//
//        DepartmentResponse response = departmentService.getDepartmentById(created.getId());
//
//        assertEquals(created.getId(), response.getId());
//        assertEquals("IT", response.getDepartmentName());
//    }
//
//    @Test
//    void getDepartmentById_NotFound() {
//        Long fakeId = 99999L;
//        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
//            departmentService.getDepartmentById(fakeId);
//        });
//
//        assertTrue(exception.getMessage().contains("Department not found"));
//    }
//
//    @Test
//    void deleteDepartment_Success() {
//        DepartmentRequest request = new DepartmentRequest();
//        request.setDepartmentName("IT");
//        DepartmentResponse created = departmentService.createDepartment(request);
//
//        try {
//            departmentService.deleteDepartment(created.getId());
//        } catch (lk.chamasha.test.cases.exception.DepartmentNotFoundException e) {
//            throw new RuntimeException(e);
//        }
//
//        assertFalse(departmentRepository.existsById(created.getId()));
//    }
//
//    @Test
//    void deleteDepartment_NotFound() {
//        Long fakeId = 99999L;
//        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
//            departmentService.deleteDepartment(fakeId);
//        });
//
//        assertTrue(exception.getMessage().contains("Department not found"));
//    }
//}
