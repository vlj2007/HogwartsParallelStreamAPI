package ru.hogwarts.hogwartsparallelstreamapi.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.hogwartsparallelstreamapi.model.Student;
import ru.hogwarts.hogwartsparallelstreamapi.service.StudentService;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("students")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping
    public ResponseEntity<Student> createStudent(@RequestBody Student student) {
        return ResponseEntity.ok(studentService.createdStudent(student));
    }

    @GetMapping("{id}")
    public ResponseEntity<Student> getStudentInfo(@PathVariable Long id) {
        Student student = studentService.findStudent(id);
        if (student == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(student);
    }

    @PutMapping
    public ResponseEntity<Student> editStudent(@RequestBody Student student) {
        Student foundStudent = studentService.editStudent(student);
        if (foundStudent == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok(foundStudent);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable long id) {
        studentService.deleteStudent(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping(path = "/find")
    public Collection<Student> getStudents() {
        return studentService.getAllStudent();
    }

    @DeleteMapping()
    public ResponseEntity<Void> deleteAllStudents(Student student) {
        studentService.deleteAllStudents(student);
        return ResponseEntity.ok().build();
    }

    @GetMapping(path = "/between")
    public Collection<Student> findByAgeBetween(int min, int max) {
        return studentService.findByAgeBetween(min, max);
    }

    @GetMapping(path = "/age")
    public ResponseEntity<List<Student>> findStudentByAge(@RequestParam Integer age) {

        if (studentService.findStudentByAge(age).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok((studentService.findStudentByAge(age)));
    }

    @GetMapping(path = "/find-student/{like}")
    public Collection<Student> findStudentByNameIgnoreCaseIsLike(@PathVariable String like) {
        return studentService.findStudentByNameIgnoreCaseIsLike(like);
    }

    @GetMapping(path = "/name/ignore")
    public ResponseEntity<Collection<Student>> findStudentByName(@RequestParam String name) {
        if (name != null) {
            return ResponseEntity.ok(studentService.findStudentByNameIgnoreCase(name));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }


    @GetMapping(path = "/name-with-started-a")
    public Collection<String> getStudentInfoWithLetterA(String startLetter) {
        return studentService.getStudentNamesStartingWithLetterInUpperCase(startLetter);
    }


    @GetMapping(path = "/get-student-average-age")
    public double getStudentAverageAge() {
        return studentService.getStudentAverageAge();
    }


}