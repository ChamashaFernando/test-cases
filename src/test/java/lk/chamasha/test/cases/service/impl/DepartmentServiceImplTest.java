//package lk.chamasha.test.cases.service.impl;
//
//import lk.chamasha.test.cases.controller.request.DepartmentRequest;
//import lk.chamasha.test.cases.controller.response.DepartmentResponse;
//import lk.chamasha.test.cases.model.Department;
//import lk.chamasha.test.cases.repository.DepartmentRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//
//import java.util.Arrays;
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//class DepartmentServiceImplTest {
//
//    @Mock
//    private DepartmentRepository departmentRepository;
//
//    @InjectMocks
//    private DepartmentServiceImpl departmentService;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    void createDepartment_Success() {
//        DepartmentRequest request = new DepartmentRequest();
//        request.setDepartmentName("IT");
//
//        Department savedDept = new Department();
//        savedDept.setId(1L);
//        savedDept.setDepartmentName("IT");
//
//        when(departmentRepository.save(any(Department.class))).thenReturn(savedDept);
//
//        DepartmentResponse response = departmentService.createDepartment(request);
//
//        assertEquals(1L, response.getId());
//        assertEquals("IT", response.getDepartmentName());
//
//        verify(departmentRepository, times(1)).save(any(Department.class));
//    }
//
//    @Test
//    void getAllDepartments_Success() {
//        Department dept1 = new Department();
//        dept1.setId(1L);
//        dept1.setDepartmentName("IT");
//
//        Department dept2 = new Department();
//        dept2.setId(2L);
//        dept2.setDepartmentName("HR");
//
//        when(departmentRepository.findAll()).thenReturn(Arrays.asList(dept1, dept2));
//
//        List<DepartmentResponse> responses = departmentService.getAllDepartments();
//
//        assertEquals(2, responses.size());
//        assertEquals("IT", responses.get(0).getDepartmentName());
//        assertEquals("HR", responses.get(1).getDepartmentName());
//
//        verify(departmentRepository, times(1)).findAll();
//    }
//
//    @Test
//    void getDepartmentById_Success() {
//        Department dept = new Department();
//        dept.setId(1L);
//        dept.setDepartmentName("IT");
//
//        when(departmentRepository.findById(1L)).thenReturn(Optional.of(dept));
//
//        DepartmentResponse response = departmentService.getDepartmentById(1L);
//
//        assertEquals(1L, response.getId());
//        assertEquals("IT", response.getDepartmentName());
//
//        verify(departmentRepository, times(1)).findById(1L);
//    }
//
//    @Test
//    void getDepartmentById_NotFound() {
//        when(departmentRepository.findById(1L)).thenReturn(Optional.empty());
//
//        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
//            departmentService.getDepartmentById(1L);
//        });
//
//        assertTrue(exception.getMessage().contains("Department not found with id: 1"));
//
//        verify(departmentRepository, times(1)).findById(1L);
//    }
//
//    @Test
//    void deleteDepartment_Success() {
//        when(departmentRepository.existsById(1L)).thenReturn(true);
//
//        doNothing().when(departmentRepository).deleteById(1L);
//
//        try {
//            departmentService.deleteDepartment(1L);
//        } catch (lk.chamasha.test.cases.exception.DepartmentNotFoundException e) {
//            throw new RuntimeException(e);
//        }
//
//        verify(departmentRepository, times(1)).existsById(1L);
//        verify(departmentRepository, times(1)).deleteById(1L);
//    }
//
//    @Test
//    void deleteDepartment_NotFound() {
//        when(departmentRepository.existsById(1L)).thenReturn(false);
//
//        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
//            departmentService.deleteDepartment(1L);
//        });
//
//        assertTrue(exception.getMessage().contains("Department not found with id: 1"));
//
//        verify(departmentRepository, times(1)).existsById(1L);
//        verify(departmentRepository, never()).deleteById(anyLong());
//    }
//}
