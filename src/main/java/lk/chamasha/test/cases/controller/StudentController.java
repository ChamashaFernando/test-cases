package lk.chamasha.test.cases.controller;

import lk.chamasha.test.cases.controller.request.StudentRequest;
import lk.chamasha.test.cases.controller.response.StudentResponse;
import lk.chamasha.test.cases.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import jakarta.transaction.Transactional;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Transactional
public class StudentController {

    private final StudentService studentService;

    @PostMapping(value = "/api/students", headers = "X-Api-Version=v1")
    public StudentResponse create(@RequestBody StudentRequest request) {
        return studentService.create(request);
    }

    @GetMapping(value = "/api/students", headers = "X-Api-Version=v1")
    public List<StudentResponse> getAll() {
        return studentService.getAll();
    }

    @GetMapping(value = "/api/students/{student-id}", headers = "X-Api-Version=v1")
    public StudentResponse getById(@PathVariable("student-id") Long id) {
        return studentService.getById(id);
    }

    @DeleteMapping(value = "/api/students/{student-id}", headers = "X-Api-Version=v1")
    public void delete(@PathVariable("student-id") Long id) {
        studentService.delete(id);
    }
}
