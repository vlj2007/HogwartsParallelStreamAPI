package ru.hogwarts.hogwartsparallelstreamapi.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import ru.hogwarts.hogwartsparallelstreamapi.api.IFaculty;
import ru.hogwarts.hogwartsparallelstreamapi.model.Faculty;
import ru.hogwarts.hogwartsparallelstreamapi.model.Student;
import ru.hogwarts.hogwartsparallelstreamapi.repository.FacultyRepository;
import ru.hogwarts.hogwartsparallelstreamapi.exception.BadRequestException;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class FacultyService implements IFaculty {

    Logger logger = LoggerFactory.getLogger(FacultyService.class);

    @Autowired
    private final FacultyRepository facultyRepository;

    public FacultyService(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }

    public Faculty createFaculty(Faculty faculty) {
        logger.info("Was invoked method for create faculty");
        return facultyRepository.save(faculty);
    }

    public Faculty editFaculty(Faculty faculty) {
        logger.info("Was invoked method for edit faculty");
        return facultyRepository.save(faculty);
    }

    public Faculty findFaculty(Long id) {
        logger.error("There is not faculty with id = " + id);
        return facultyRepository.findById(id).orElseThrow(() -> new BadRequestException("Отсутствует id"));
    }

    public void deleteFacultyById(long id) {
        logger.error("There is not faculty with id = " + id);
        Faculty faculty = facultyRepository.findById(id).orElseThrow(() -> new BadRequestException("Отсутствует id"));
        Set<Student> students = faculty.getStudents();
        for (Student student : students) {
            student.setFaculty(null);
        }
        facultyRepository.deleteById(id);
    }

    public void deleteAllFaculty(Faculty faculty) {
        logger.info("Was invoked method for delete all faculty");
        facultyRepository.deleteAll();
    }

    public List<Faculty> showAllFaculty() {
        logger.info("Was invoked method for show all faculty");
        return facultyRepository.findAll();
    }

    public List<Faculty> findFacultyByName(String name) {
        logger.info("Was invoked method for find faculty by name");
        return facultyRepository.findFacultyByName(name);
    }

    public Collection<Faculty> findFacultyByNameIgnoreCase(String name) {
        logger.info("Was invoked method for find faculty by name ignore case");
        return facultyRepository.findFacultyByNameIgnoreCase(name);
    }

    public Collection<Faculty> findFacultyByColorIgnoreCase(String color) {
        logger.info("Was invoked method for find faculty by color ignore case");
        return facultyRepository.findFacultyByColorIgnoreCase(color);
    }


    public String longestFacultyName() {
        logger.info("Was invoked method for find longest faculty name");
        return facultyRepository.findAll().stream()
                .max(Comparator.comparingInt(f -> f.getName().length()))
                .get().getName();
    }


}