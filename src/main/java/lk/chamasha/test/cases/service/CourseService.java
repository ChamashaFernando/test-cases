package lk.chamasha.test.cases.service;


import lk.chamasha.test.cases.controller.request.CourseRequest;
import lk.chamasha.test.cases.controller.response.CourseResponse;

import java.util.List;

public interface CourseService {

    CourseResponse createCourse(CourseRequest request);

    List<CourseResponse> getAllCourses();

    CourseResponse getCourseById(Long id);

    void deleteCourse(Long id);
}
