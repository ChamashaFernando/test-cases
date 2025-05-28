
package lk.chamasha.test.cases.service;



import lk.chamasha.test.cases.controller.request.DepartmentRequest;
import lk.chamasha.test.cases.controller.response.DepartmentResponse;

import java.util.List;

public interface DepartmentService {

    DepartmentResponse createDepartment(DepartmentRequest request);

    List<DepartmentResponse> getAllDepartments();

    DepartmentResponse getDepartmentById(Long id);

    void deleteDepartment(Long id);
}
