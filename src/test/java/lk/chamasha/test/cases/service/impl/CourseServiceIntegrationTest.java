//package lk.chamasha.test.cases.service.impl;
//
//import lk.chamasha.test.cases.controller.request.CourseRequest;
//import lk.chamasha.test.cases.controller.response.CourseResponse;
//import lk.chamasha.test.cases.model.Department;
//import lk.chamasha.test.cases.repository.CourseRepository;
//import lk.chamasha.test.cases.repository.DepartmentRepository;
//import lk.chamasha.test.cases.service.CourseService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@SpringBootTest
//@Transactional
//class CourseServiceIntegrationTest {
//
//    @Autowired
//    private CourseService courseService;
//
//    @Autowired
//    private DepartmentRepository departmentRepository;
//
//    @Autowired
//    private CourseRepository courseRepository;
//
//    private Department savedDept;
//
//    @BeforeEach
//    void setUp() {
//        Department dept = new Department();
//        dept.setDepartmentName("Science");
//        savedDept = departmentRepository.save(dept);
//    }
//
//    // ✅ SUCCESS CASE: createCourse
//    @Test
//    void testCreateCourse_Success() {
//        CourseRequest request = new CourseRequest("Physics", savedDept.getId());
//
//        CourseResponse response = courseService.createCourse(request);
//
//        assertNotNull(response);
//        assertEquals("Physics", response.getCourseName());
//        assertEquals("Science", response.getDepartmentName());
//    }
//
//    // ❌ REJECT CASE: createCourse with invalid departmentId
//    @Test
//    void testCreateCourse_Reject_DepartmentNotFound() {
//        CourseRequest request = new CourseRequest("Chemistry", 999L); // invalid id
//
//        Exception exception = assertThrows(RuntimeException.class, () -> {
//            courseService.createCourse(request);
//        });
//
//        assertTrue(exception.getMessage().contains("Department not found"));
//    }
//
//    // ✅ SUCCESS CASE: getAllCourses
//    @Test
//    void testGetAllCourses_Success() {
//        courseService.createCourse(new CourseRequest("Math", savedDept.getId()));
//        courseService.createCourse(new CourseRequest("Biology", savedDept.getId()));
//
//        List<CourseResponse> all = courseService.getAllCourses();
//
//        assertTrue(all.size() >= 2);
//    }
//
//    // ✅ SUCCESS CASE: getCourseById
//    @Test
//    void testGetCourseById_Success() {
//        CourseResponse created = courseService.createCourse(new CourseRequest("IT", savedDept.getId()));
//
//        CourseResponse response = courseService.getCourseById(created.getId());
//
//        assertEquals("IT", response.getCourseName());
//    }
//
//    // ❌ REJECT CASE: getCourseById invalid id
//    @Test
//    void testGetCourseById_Reject_NotFound() {
//        Exception exception = assertThrows(RuntimeException.class, () -> {
//            courseService.getCourseById(123456L); // non-existent
//        });
//
//        assertTrue(exception.getMessage().contains("Course not found"));
//    }
//
//    // ✅ SUCCESS CASE: deleteCourse
//    @Test
//    void testDeleteCourse_Success() {
//        CourseResponse created = courseService.createCourse(new CourseRequest("Delete Me", savedDept.getId()));
//        Long id = created.getId();
//
//        courseService.deleteCourse(id);
//
//        assertFalse(courseRepository.existsById(id));
//    }
//
//    // ❌ REJECT CASE: deleteCourse with invalid id
//    @Test
//    void testDeleteCourse_Reject_NotFound() {
//        Exception exception = assertThrows(RuntimeException.class, () -> {
//            courseService.deleteCourse(8888L); // non-existent
//        });
//
//        assertTrue(exception.getMessage().contains("Course not found"));
//    }
//}
