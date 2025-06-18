package lk.chamasha.test.cases.controller;

import jakarta.transaction.Transactional;
import lk.chamasha.test.cases.controller.request.DepartmentRequest;
import lk.chamasha.test.cases.controller.response.DepartmentResponse;
import lk.chamasha.test.cases.exception.DepartmentNotCreatedException;
import lk.chamasha.test.cases.exception.DepartmentNotFoundException;
import lk.chamasha.test.cases.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Transactional
public class DepartmentController {

    private final DepartmentService departmentService;


    @PostMapping(value = "/api/departments", headers = "X-Api-Version=v1")
    public DepartmentResponse create(@RequestBody DepartmentRequest request) throws DepartmentNotCreatedException {
        return departmentService.create(request);
    }

    @GetMapping(value = "/api/departments", headers = "X-Api-Version=v1")
    public List<DepartmentResponse> getAll() {
        return departmentService.getAll();
    }

    @GetMapping(value = "/api/departments/{department-id}", headers = "X-Api-Version=v1")
    public DepartmentResponse getById(@PathVariable("department-id") Long id) throws DepartmentNotFoundException {
        return departmentService.getById(id);
    }

    @DeleteMapping(value = "/api/departments/{department-id}", headers = "X-Api-Version=v1")
    public void delete(@PathVariable("department-id") Long id) throws DepartmentNotFoundException {
        departmentService.delete(id);
    }
}
