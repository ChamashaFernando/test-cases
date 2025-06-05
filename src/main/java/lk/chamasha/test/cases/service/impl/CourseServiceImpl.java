package lk.chamasha.test.cases.service.impl;

import jakarta.transaction.Transactional;
import lk.chamasha.test.cases.controller.request.CourseRequest;
import lk.chamasha.test.cases.controller.response.CourseResponse;
import lk.chamasha.test.cases.exception.CourseNotCreatedException;
import lk.chamasha.test.cases.exception.CourseNotFoundException;
import lk.chamasha.test.cases.model.Course;
import lk.chamasha.test.cases.model.Department;
import lk.chamasha.test.cases.repository.CourseRepository;
import lk.chamasha.test.cases.repository.DepartmentRepository;
import lk.chamasha.test.cases.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    private final DepartmentRepository departmentRepository;

    @Override
    public CourseResponse createCourse(CourseRequest request) throws CourseNotCreatedException {
        Department dept = departmentRepository.findById(request.getDepartmentId())
                .orElseThrow(() -> new CourseNotCreatedException("Department not found with id: " + request.getDepartmentId()));

        Course course = new Course();
        course.setCourseName(request.getCourseName());
        course.setDepartment(dept);

        Course saved = courseRepository.save(course);

        return new CourseResponse(saved.getId(), saved.getCourseName(), dept.getDepartmentName());
    }

    @Override
    public List<CourseResponse> getAllCourses() {
        return courseRepository.findAll().stream()
                .map(c -> new CourseResponse(c.getId(), c.getCourseName(), c.getDepartment().getDepartmentName()))
                .collect(Collectors.toList());
    }

    @Override
    public CourseResponse getCourseById(Long id) throws CourseNotFoundException {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new CourseNotFoundException("Course not found with id: " + id));
        return new CourseResponse(course.getId(), course.getCourseName(), course.getDepartment().getDepartmentName());
    }

    @Override
    public void deleteCourse(Long id) throws CourseNotFoundException {
        if (!courseRepository.existsById(id)) {
            throw new CourseNotFoundException("Course not found with id: " + id);
        }
        courseRepository.deleteById(id);
    }
}
