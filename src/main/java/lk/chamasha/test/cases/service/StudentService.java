package lk.chamasha.test.cases.service;



import lk.chamasha.test.cases.controller.request.StudentRequest;
import lk.chamasha.test.cases.controller.response.StudentResponse;

import java.util.List;

public interface StudentService {

    StudentResponse createStudent(StudentRequest request);

    List<StudentResponse> getAllStudents();

    StudentResponse getStudentById(Long id);

    void deleteStudent(Long id);
}
