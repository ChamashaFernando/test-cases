package lk.chamasha.test.cases.service;


import lk.chamasha.test.cases.controller.request.CourseRequest;
import lk.chamasha.test.cases.controller.response.CourseResponse;
import lk.chamasha.test.cases.exception.CourseNotCreatedException;
import lk.chamasha.test.cases.exception.CourseNotFoundException;

import java.util.List;

public interface CourseService {

    CourseResponse createCourse(CourseRequest request)throws CourseNotCreatedException;

    List<CourseResponse> getAllCourses();

    CourseResponse getCourseById(Long id)throws CourseNotFoundException;

    void deleteCourse(Long id)throws CourseNotFoundException;
}
