package ru.hogwarts.hogwartsparallelstreamapi.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.hogwartsparallelstreamapi.model.Faculty;
import ru.hogwarts.hogwartsparallelstreamapi.service.FacultyService;

import java.util.Collection;
import java.util.Collections;

@RestController
@RequestMapping("faculty")
public class FacultyController {
    private final FacultyService facultyService;

    public FacultyController(FacultyService facultyService) {
        this.facultyService = facultyService;
    }

    @PostMapping
    public Faculty createFaculty(@RequestBody Faculty faculty) {
        return facultyService.createFaculty(faculty);
    }

    @GetMapping("{id}")
    public ResponseEntity<Faculty> getFacultyInfo(@PathVariable long id) {
        Faculty faculty = facultyService.findFaculty(id);
        if (faculty == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(faculty);
    }

    @PutMapping
    public Faculty editFaculty(Faculty faculty) {
        return facultyService.editFaculty(faculty);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Faculty> deleteFacultyById(@PathVariable long id) {
        facultyService.deleteFacultyById(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping()
    public ResponseEntity<Void> deleteAllFaculty(Faculty faculty) {
        facultyService.deleteAllFaculty(faculty);
        return ResponseEntity.ok().build();
    }

    @GetMapping(path = "/find")
    public Collection<Faculty> getFaculty() {
        return facultyService.showAllFaculty();
    }

    @GetMapping(path = "/color")
    public ResponseEntity<Collection<Faculty>> findFacultyByColor(@RequestParam(required = false) String color) {
        if (color != null && !color.isBlank()) {
            return ResponseEntity.ok(facultyService.findFacultyByColorIgnoreCase(color));
        }
        return ResponseEntity.ok(Collections.emptyList());
    }

    @GetMapping(path = "/nameorcolor")
    public ResponseEntity<Collection<Faculty>> findFacultyByNameOrColor(@RequestParam(required = false) String name, @RequestParam(required = false) String color) {
        if (name != null && !name.isBlank()) {
            ResponseEntity<Collection<Faculty>> responseEntity = ResponseEntity.ok(facultyService.findFacultyByNameIgnoreCase(name));
            return responseEntity;
        }

        if (color != null && !color.isBlank()) {
            return ResponseEntity.ok(facultyService.findFacultyByColorIgnoreCase(color));
        }

        return ResponseEntity.ok(facultyService.showAllFaculty());

    }
}