package ru.hogwarts.hogwartsparallelstreamapi.controller;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.hogwarts.hogwartsparallelstreamapi.model.Faculty;
import ru.hogwarts.hogwartsparallelstreamapi.model.Student;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import ru.hogwarts.hogwartsparallelstreamapi.repository.StudentRepository;

import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class StudentControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TestRestTemplate restTemplate;
    Long id = 3L;
    String name = "newStudent";
    int age = 44;
    String color = "newColor";

    private Faculty faculty;
    private Student student;
    private List<Student> students;

    @BeforeEach
    void setUp() {

        faculty = new Faculty();
        faculty.setId(id);
        faculty.setName(name);
        faculty.setColor(color);

        student = new Student();
        student.setId(id);
        student.setName(name);
        student.setAge(age);
        student.setFaculty(faculty);

        students = new ArrayList<>();
        students.add(student);

        Set<Student> studentSet = new HashSet<>();
        studentSet.add(student);
        faculty.setStudents(studentSet);

    }

    @Autowired
    private StudentController studentController;

    @Autowired
    private StudentRepository studentRepository;

    @Test
    public void contextLoads() throws Exception {
        Assertions.assertThat(studentController).isNotNull();
    }

    @Test
    public void testGetStudents() throws Exception {
        Assertions.assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/students/1", String.class))
                .contains("1");
    }

    @Test
    public void testPutStudent() {
        Long id = this.restTemplate.postForObject("http://localhost:" + port + "/students", student, Student.class).getId();
        restTemplate.put("http://localhost:" + port + "/students", student);
        Assertions
                .assertThat((this.restTemplate.getForObject("http://localhost:" + port +
                                "/student/" + id,
                        String.class))).toString().contains("newstudent");
    }

    @Test
    public void testDeletedStudentById() throws Exception {
        Long id = this.restTemplate.postForObject("http://localhost:" + port + "/students", student, Student.class).getId();
        restTemplate.delete("http://localhost:" + port + "/students/" + id);
        Assertions
                .assertThat((this.restTemplate.getForObject("http://localhost:" + port +
                                "/students/" + id,
                        String.class))).toString().contains("500");
    }


}