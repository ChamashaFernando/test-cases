package lk.chamasha.test.cases.controller;

import lk.chamasha.test.cases.controller.request.CourseRequest;
import lk.chamasha.test.cases.controller.response.CourseResponse;
import lk.chamasha.test.cases.exception.CourseNotCreatedException;
import lk.chamasha.test.cases.exception.CourseNotFoundException;
import lk.chamasha.test.cases.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import jakarta.transaction.Transactional;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Transactional
public class CourseController {

    private final CourseService courseService;

    @PostMapping(value = "/api/courses", headers = "X-Api-Version=v1")
    public CourseResponse create(@RequestBody CourseRequest request) throws CourseNotCreatedException {
        return courseService.create(request);
    }

    @GetMapping(value = "/api/courses", headers = "X-Api-Version=v1")
    public List<CourseResponse> getAll() {
        return courseService.getAll();
    }

    @GetMapping(value = "/api/courses/{course-id}", headers = "X-Api-Version=v1")
    public CourseResponse getById(@PathVariable("course-id") Long id) throws CourseNotFoundException {
        return courseService.getById(id);
    }

    @DeleteMapping(value = "/api/courses/{course-id}", headers = "X-Api-Version=v1")
    public void delete(@PathVariable("course-id") Long id) throws CourseNotFoundException {
        courseService.delete(id);
    }
}
