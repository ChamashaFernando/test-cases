package lk.chamasha.test.cases.service.impl;

import lk.chamasha.test.cases.controller.request.StudentRequest;
import lk.chamasha.test.cases.controller.response.StudentResponse;
import lk.chamasha.test.cases.model.Course;
import lk.chamasha.test.cases.model.Student;
import lk.chamasha.test.cases.repository.CourseRepository;
import lk.chamasha.test.cases.repository.StudentRepository;
import lk.chamasha.test.cases.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;

    @Override
    public StudentResponse createStudent(StudentRequest request) {
        Course course = courseRepository.findById(request.getCourseId())
                .orElseThrow(() -> new RuntimeException("Course not found with id: " + request.getCourseId()));

        Student student = new Student();
        student.setStudentName(request.getStudentName());
        student.setCourse(course);

        Student saved = studentRepository.save(student);

        return new StudentResponse(saved.getId(), saved.getStudentName(), course.getCourseName());
    }

    @Override
    public List<StudentResponse> getAllStudents() {
        return studentRepository.findAll().stream()
                .map(s -> new StudentResponse(s.getId(), s.getStudentName(),
                        s.getCourse() != null ? s.getCourse().getCourseName() : null))
                .collect(Collectors.toList());
    }

    @Override
    public StudentResponse getStudentById(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + id));
        return new StudentResponse(student.getId(), student.getStudentName(),
                student.getCourse() != null ? student.getCourse().getCourseName() : null);
    }

    @Override
    public void deleteStudent(Long id) {
        if (!studentRepository.existsById(id)) {
            throw new RuntimeException("Student not found with id: " + id);
        }
        studentRepository.deleteById(id);
    }
}
