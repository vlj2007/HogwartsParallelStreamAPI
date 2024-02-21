package ru.hogwarts.hogwartsparallelstreamapi.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.hogwarts.hogwartsparallelstreamapi.api.IStudent;
import ru.hogwarts.hogwartsparallelstreamapi.model.Student;
import ru.hogwarts.hogwartsparallelstreamapi.repository.AvatarRepository;
import ru.hogwarts.hogwartsparallelstreamapi.repository.StudentRepository;
import ru.hogwarts.hogwartsparallelstreamapi.exception.BadRequestException;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudentService implements IStudent {

    Logger logger = LoggerFactory.getLogger(StudentService.class);

    private final StudentRepository studentRepository;
    private final AvatarRepository avatarRepository;

    public StudentService(StudentRepository studentRepository, AvatarRepository avatarRepository) {
        this.studentRepository = studentRepository;
        this.avatarRepository = avatarRepository;

    }

    public Student createdStudent(Student student) {
        logger.info("Was invoked method for create student");
        student.setId(null);
        return studentRepository.save(student);
    }

    public Student editStudent(Student student) {
        logger.info("Was invoked method for edit student");
        return studentRepository.save(student);
    }

    public Student findStudent(Long id) {
        logger.error("There is not student with id = " + id);
        return studentRepository.findById(id).orElseThrow(() -> new BadRequestException("Отсутствует id"));
    }

    public void deleteStudent(long id) {
        logger.info("Was invoked method for delete student by id");
        studentRepository.deleteById(id);
    }

    public void deleteAllStudents(Student student) {
        logger.info("Was invoked method for delete all student");
        studentRepository.deleteAll();
    }

    public void deleteAllStudents() {
        logger.info("Was invoked method for delete all student");
        studentRepository.deleteAll();
    }

    public Collection<Student> getAllStudent() {
        logger.info("Was invoked method for find all student");
        return studentRepository.findAll();
    }

    public List<Student> findStudentByAge(int age) {
        logger.info("Was invoked method for find student by age");
        return studentRepository.findStudentByAge(age);
    }

    public Collection<Student> findByAgeBetween(int min, int max) {
        logger.info("Was invoked method for find student by age between min and max");
        return studentRepository.findByAgeBetween(min, max);
    }

    public Collection<Student> findStudentByName(String name) {
        logger.info("Was invoked method for find student by name");
        return studentRepository.findStudentByName(name);
    }

    public Collection<Student> findStudentByNameIgnoreCase(String name) {
        logger.info("Was invoked method for find student by name ignore case");
        return studentRepository.findStudentByNameIgnoreCase(name);
    }

    public Collection<Student> findStudentByNameIgnoreCaseIsLike(String like) {
        logger.info("Was invoked method for find student by name ignore case is like");
        return studentRepository.findStudentByNameIgnoreCaseIsLike(like);
    }

    public Collection<String> getStudentNamesStartingWithLetterInUpperCase(String startLetter) {
        logger.info("Was invoked method for find all student with letter A");
        return studentRepository.findAll().stream()
                .map(Student::getName)
                .map(String::toUpperCase)
                .filter(name -> name.startsWith(startLetter))
                .sorted()
                .collect(Collectors.toList());
    }

    public double getStudentAverageAge() {
        logger.info("Was invoked method for find all student with Average Age");
        return studentRepository.findAll().stream()
                .map(Student::getAge)
                .mapToInt(o->o)
                .average()
                .orElse(0.0);
    }


    public List<Student> findAll() {
        logger.info("Was invoked method for findAll student");
        return studentRepository.findAll();
    }


    public void printName(int i){
        logger.info("Was invoked method for print name student");
        System.out.println(findAll().get(i).getName());
    }


    //Разные потоки выводятся параллельно

    public void printNameParallel(){
        logger.info("Was invoked method for print name student parallel");
        printName(0);//Lucius Malfoy

        new Thread(()->{
            printName(2); // Aberforth Dumbledore
            printName(3); // Fred Weasley
        }).start();

        new Thread(()->{
            printName(4);//Albus Dumbledore
            printName(5); //George Weasley
        }).start();

        printName(1);
    }

    //Разные потоки выводятся по очередно
    public synchronized void printNameSync(int i){
        logger.info("Was invoked method for print name sync student");
        System.out.println(findAll().get(i).getName());
    }

    public void printNameParallelSync(){
        logger.info("Was invoked method for print name sync student parallel");
        printNameSync(0); //Lucius Malfoy

        new Thread(()->{
            printNameSync(2);
            printNameSync(3);
        }).start();

        new Thread(()->{
            printNameSync(4);
            printNameSync(5);
        }).start();

        printNameSync(1);

    }

}



