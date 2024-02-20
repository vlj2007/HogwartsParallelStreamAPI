package ru.hogwarts.hogwartsparallelstreamapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.AbstractBooleanAssert;
import org.assertj.core.api.Assertions;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.MvcResult;
import ru.hogwarts.hogwartsparallelstreamapi.model.Faculty;
import ru.hogwarts.hogwartsparallelstreamapi.model.Student;
import ru.hogwarts.hogwartsparallelstreamapi.repository.AvatarRepository;
import ru.hogwarts.hogwartsparallelstreamapi.repository.FacultyRepository;
import ru.hogwarts.hogwartsparallelstreamapi.repository.StudentRepository;
import ru.hogwarts.hogwartsparallelstreamapi.service.AvatarService;
import ru.hogwarts.hogwartsparallelstreamapi.service.FacultyService;
import ru.hogwarts.hogwartsparallelstreamapi.service.StudentService;

import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class StudentControllerMVCTest {

    @LocalServerPort
    private int port;

    @Autowired
    private MockMvc mockMvc;

    @SpyBean
    private StudentService studentService;

    @MockBean
    private StudentRepository studentRepository;
    @MockBean
    private FacultyRepository facultyRepository;

    @MockBean
    private AvatarRepository avatarRepository;

    @SpyBean
    private AvatarService avatarService;

    @InjectMocks
    private StudentController studentController;

    @Autowired
    private TestRestTemplate restTemplate;

    Long id = 1L;
    String name = "newStudent";
    String color = "newColor";
    int age = 22;

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

    @Test
    void testStudentsIsNotNull() throws Exception {
        Assertions.assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/students", String.class))
                .isNotNull();
    }

    @Test
    public void testGetStudentsId() throws Exception {
        Assertions.assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/students/1", String.class))
                .contains("1");
    }

    @Test
    public void saveStudentTest() throws Exception {
        JSONObject studentObject = new JSONObject();
        when(studentRepository.save(any(Student.class))).thenReturn(student);
        when(studentRepository.findById(any(Long.class))).thenReturn(Optional.of(student));
        ResultActions resultActions;
        resultActions = mockMvc.perform(MockMvcRequestBuilders
                        .post("/students")
                        .content(studentObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.age").value(age));
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/students/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.age").value(age));
    }

    @Test
    public void testGetStudentAge() throws Exception {
        Assertions
                .assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/students/age",
                        String.class)).isNotNull();
    }

    @Test
    public void testDeletedStudentById() throws Exception {
        Student student = new Student(1L, name, age);
        Long id = this.restTemplate.postForObject("http://localhost:" + port + "/students/", students, Student.class).getId();
        restTemplate.delete("http://localhost:" + port + "/students/" + id);
        Assertions
                .assertThat((this.restTemplate.getForObject("http://localhost:" + port +
                                "/students/" + id,
                        String.class)))
                .toString().contains("500");
    }

    @Test
    void findAllShouldReturnAllStudent() throws Exception {
        Mockito.when(this.studentService.getAllStudent()).thenReturn(students);
        mockMvc.perform(get("/students/find"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    public void deleteStudentsById() throws Exception {
        when(studentRepository.findById(any(Long.class))).thenReturn( Optional.of(student));
        doNothing().when(studentRepository).deleteById(any(Long.class));
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/students/"+ student.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }


    @Test
    public void findStudentByAge() throws Exception {
        when(studentRepository.findStudentByAge(any(Integer.class))).thenReturn(students);
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/students/age?age=" + student.getAge() )
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1));
    }


    @Test
    void getStudentByAgeBetweenTest() throws Exception {
        List<Student> studentList = new ArrayList<>(List.of
                (new Student(5L, "newStudent5", 17),
                        new Student(6L, "newStudent6", 26)));
        when(studentRepository.findByAgeBetween(17, 26))
                .thenReturn(studentList);
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/students/between?min=17&max=26")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2));
    }

    @Test
    public void getStudentByIdTest() throws Exception{
        when(studentRepository.save(any(Student.class))).thenReturn(student);
        when(studentRepository.findById(any(Long.class))).thenReturn(Optional.of(student));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/students/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.age").value(age));
    }


}
