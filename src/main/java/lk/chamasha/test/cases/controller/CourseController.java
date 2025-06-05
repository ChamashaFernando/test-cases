package lk.chamasha.test.cases.controller;

import lk.chamasha.test.cases.controller.request.CourseRequest;
import lk.chamasha.test.cases.controller.response.CourseResponse;
import lk.chamasha.test.cases.exception.CourseNotCreatedException;
import lk.chamasha.test.cases.exception.CourseNotFoundException;
import lk.chamasha.test.cases.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;

    @PostMapping
    public ResponseEntity<CourseResponse> createCourse(@RequestBody CourseRequest request)throws CourseNotCreatedException {
        try {
            return ResponseEntity.ok(courseService.createCourse(request));
        } catch (lk.chamasha.test.cases.exception.CourseNotCreatedException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping
    public ResponseEntity<List<CourseResponse>> getAllCourses() {
        return ResponseEntity.ok(courseService.getAllCourses());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourseResponse> getCourseById(@PathVariable Long id)throws CourseNotFoundException {
        try {
            return ResponseEntity.ok(courseService.getCourseById(id));
        } catch (lk.chamasha.test.cases.exception.CourseNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable Long id)throws CourseNotFoundException {
        try {
            courseService.deleteCourse(id);
        } catch (lk.chamasha.test.cases.exception.CourseNotFoundException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.noContent().build();
    }
}
