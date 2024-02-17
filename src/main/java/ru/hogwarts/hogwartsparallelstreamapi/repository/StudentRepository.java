package ru.hogwarts.hogwartsparallelstreamapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.hogwarts.hogwartsparallelstreamapi.model.Student;

import java.util.Collection;
import java.util.List;


public interface StudentRepository extends JpaRepository<Student, Long> {

    List<Student> findStudentByAge(int age);

    List<Student> findStudentByName(String name);

    Collection<Student> findByAgeBetween(int min, int max);

    Collection<Student> findStudentByNameIgnoreCase(String name);

    Collection<Student> findStudentByNameIgnoreCaseIsLike(String like);

    @Query(value = "SELECT COUNT(*) FROM student", nativeQuery = true)
    Integer countStudents();

    @Query(value = "SELECT AVG(age) FROM student", nativeQuery = true)
    Integer middleAgeByStudents();

    @Query(value = "SELECT * FROM student ORDER BY id DESC LIMIT 5", nativeQuery = true)
    List<Student> getLastFiveStudentsById();


}
