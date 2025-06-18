package lk.chamasha.test.cases.service;


import lk.chamasha.test.cases.controller.request.CourseRequest;
import lk.chamasha.test.cases.controller.response.CourseResponse;
import lk.chamasha.test.cases.exception.CourseNotCreatedException;
import lk.chamasha.test.cases.exception.CourseNotFoundException;

import java.util.List;

public interface CourseService {

    CourseResponse create(CourseRequest request)throws CourseNotCreatedException;

    List<CourseResponse> getAll();

    CourseResponse getById(Long id)throws CourseNotFoundException;

    void delete(Long id)throws CourseNotFoundException;
}
