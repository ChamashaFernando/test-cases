
package lk.chamasha.test.cases.service;



import lk.chamasha.test.cases.controller.request.DepartmentRequest;
import lk.chamasha.test.cases.controller.response.DepartmentResponse;
import lk.chamasha.test.cases.exception.DepartmentNotCreatedException;
import lk.chamasha.test.cases.exception.DepartmentNotFoundException;

import java.util.List;

public interface DepartmentService {

    DepartmentResponse create(DepartmentRequest request)throws DepartmentNotCreatedException;

    List<DepartmentResponse> getAll();

    DepartmentResponse getById(Long id)throws DepartmentNotFoundException;

    void delete(Long id)throws DepartmentNotFoundException;
}
