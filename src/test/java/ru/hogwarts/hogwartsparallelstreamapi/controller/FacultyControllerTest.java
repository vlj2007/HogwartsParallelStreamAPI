package ru.hogwarts.hogwartsparallelstreamapi.controller;

import org.junit.jupiter.api.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import ru.hogwarts.hogwartsparallelstreamapi.model.Faculty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import ru.hogwarts.hogwartsparallelstreamapi.model.Student;
import ru.hogwarts.hogwartsparallelstreamapi.repository.FacultyRepository;
import ru.hogwarts.hogwartsparallelstreamapi.repository.StudentRepository;
import ru.hogwarts.hogwartsparallelstreamapi.service.FacultyService;
import ru.hogwarts.hogwartsparallelstreamapi.service.StudentService;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class FacultyControllerTest {

    @LocalServerPort
    private int port;
    @MockBean
    private FacultyRepository facultyRepository;

    @MockBean
    private StudentRepository studentRepository;

    @SpyBean
    private StudentService studentService;

    @SpyBean
    private FacultyService facultyService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TestRestTemplate restTemplate;

    private Faculty faculty;
    private Student student;
    Long id = 2L;
    String name = "newFaculty";
    String color = "pin";
    int age = 25;
    private List<Student> students;
    private List<Faculty> faculties;


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
        faculties = new ArrayList<>();
        faculties.add(faculty);
    }

    @Autowired
    private FacultyController facultyController;

    @Test
    public void contextLoads() throws Exception {
        assertThat(facultyController).isNotNull();
    }

    @Test
    public void testGetFacultyId() throws Exception {
        assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/faculty/1", String.class))
                .contains("1");
    }

    @Test
    public void testGetFacultyColor() throws Exception {
        assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/faculty/green",
                String.class))
                .contains("green");
    }

    @Test
    public void getFacultyInfoTest() throws Exception {
        assertThat(this.restTemplate.<Collection>getForObject("http://localhost:" + port + "/faculty/find",
                Collection.class)).isNotNull();
    }

    @Test
    public void testGetFacultyByNameOrColor() throws Exception{
        assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/faculty?color=1", String.class)).isNotNull();
        assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/faculty?name=1", String.class)).isNotNull();
    }


    @Test
    public void testFacultyPostMethodIsNotNull() throws Exception {
        assertThat(this.restTemplate.postForObject("http://localhost:" + port + "/faculty/", faculty, String.class))
                .isNotNull();
    }


}