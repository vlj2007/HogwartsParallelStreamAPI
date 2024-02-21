package ru.hogwarts.hogwartsparallelstreamapi.api;

import ru.hogwarts.hogwartsparallelstreamapi.model.Student;

import java.util.Collection;
import java.util.List;

public interface IStudent {
    Student createdStudent(Student student);

    Student editStudent(Student student);

    Student findStudent(Long id);

    void deleteStudent(long id);

    void deleteAllStudents(Student student);

    void deleteAllStudents();

    Collection<Student> getAllStudent();

    List<Student> findStudentByAge(int age);

    Collection<Student> findByAgeBetween(int min, int max);

    Collection<Student> findStudentByName(String name);

    Collection<Student> findStudentByNameIgnoreCase(String name);

    Collection<Student> findStudentByNameIgnoreCaseIsLike(String like);

    Collection<String> getStudentNamesStartingWithLetterInUpperCase(String startLetter);

    double getStudentAverageAge();
}