package lk.chamasha.test.cases.service.impl;


import lk.chamasha.test.cases.controller.request.DepartmentRequest;
import lk.chamasha.test.cases.controller.response.DepartmentResponse;
import lk.chamasha.test.cases.exception.DepartmentNotCreatedException;
import lk.chamasha.test.cases.exception.DepartmentNotFoundException;
import lk.chamasha.test.cases.model.Department;
import lk.chamasha.test.cases.repository.DepartmentRepository;
import lk.chamasha.test.cases.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;

    @Override
    public DepartmentResponse create(DepartmentRequest request) throws DepartmentNotCreatedException {
        // Duplicate check
        Optional<Department> existingDept = departmentRepository.findByDepartmentName(request.getDepartmentName());
        if (existingDept.isPresent()) {
            throw new DepartmentNotCreatedException("Department is already registered with name: " + request.getDepartmentName());
        }

        Department dept = new Department();
        dept.setDepartmentName(request.getDepartmentName());

        Department saved = departmentRepository.save(dept);
        return new DepartmentResponse(saved.getId(), saved.getDepartmentName());
    }

    @Override
    public List<DepartmentResponse> getAll() {
        return departmentRepository.findAll()
                .stream()
                .map(d -> new DepartmentResponse(d.getId(), d.getDepartmentName()))
                .collect(Collectors.toList());
    }

    @Override
    public DepartmentResponse getById(Long id)throws DepartmentNotFoundException {
        Department dept = departmentRepository.findById(id)
                .orElseThrow(() -> new DepartmentNotFoundException("Department not found with id: " + id));
        return new DepartmentResponse(dept.getId(), dept.getDepartmentName());
    }

    @Override
    public void delete(Long id) throws DepartmentNotFoundException {
        if (!departmentRepository.existsById(id)) {
            throw new DepartmentNotFoundException("Department not found with id: " + id);
        }
        departmentRepository.deleteById(id);
    }
}
