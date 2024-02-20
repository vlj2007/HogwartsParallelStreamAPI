package ru.hogwarts.hogwartsparallelstreamapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.hogwarts.hogwartsparallelstreamapi.exception.BadRequestException;
import ru.hogwarts.hogwartsparallelstreamapi.model.Faculty;
import ru.hogwarts.hogwartsparallelstreamapi.model.Student;
import ru.hogwarts.hogwartsparallelstreamapi.repository.AvatarRepository;
import ru.hogwarts.hogwartsparallelstreamapi.repository.FacultyRepository;
import ru.hogwarts.hogwartsparallelstreamapi.repository.StudentRepository;
import ru.hogwarts.hogwartsparallelstreamapi.service.AvatarService;
import ru.hogwarts.hogwartsparallelstreamapi.service.FacultyService;
import ru.hogwarts.hogwartsparallelstreamapi.service.StudentService;

import static org.mockito.Mockito.*;

import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class FacultyControllerMVCTest {

    @LocalServerPort
    private int port;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private StudentRepository studentRepository;

    @MockBean
    private FacultyRepository facultyRepository;

    @MockBean
    private AvatarRepository avatarRepository;

    @SpyBean
    private StudentService studentService;

    @SpyBean
    private FacultyService facultyService;

    @SpyBean
    private AvatarService avatarService;

    @InjectMocks
    private FacultyController facultyController;

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

    @Test
    void contextLoads() throws Exception {
        Assertions.assertThat(facultyController).isNotNull();
    }

    @Test
    public void testGetFacultyId() throws Exception {
        Assertions.assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/faculty/1",
                        String.class))
                .contains("1");
    }

    @Test
    void testFacultyIsNotNull() throws Exception {
        Assertions.assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/faculty",
                String.class)).isNotNull();
    }

    @Test
    public void saveFacultyTest() throws Exception {
        JSONObject facultyObject = new JSONObject();
        when(facultyRepository.save(any(Faculty.class))).thenReturn(faculty);
        when(facultyRepository.findById(any(Long.class))).thenReturn(Optional.of(faculty));
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/faculty")
                        .content(facultyObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.color").value(color));
        mockMvc.perform(get("/faculty/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.color").value(color));
    }

    @Test
    public void givenId_whenGetNotExistingFaculty_thenStatus404anExceptionThrown() throws Exception {
        ResultActions resultActions = mockMvc.perform(
                        get("/faculty/11"))
                .andExpect(status().isNotFound())
                .andExpect(mvcResult -> mvcResult.getResolvedException().getClass().equals(BadRequestException.class));
    }

    @Test
    void findAllShouldReturnAllFaculty() throws Exception {
        Mockito.when(this.facultyService.showAllFaculty()).thenReturn(faculties);
        mockMvc.perform(get("/faculty/find"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }


    @Test
    public void givenFaculty_whenDeleteFaculty_thenStatus200() throws Exception {
        when(facultyRepository.findById(any())).thenReturn(Optional.of(faculty));
        ResultActions resultActions = mockMvc.perform(
                        delete("/faculty/{id}", faculty.getId()))
                .andExpect(status().isOk());
    }


    @Test
    public void getFacultyByIdTest() throws Exception {
        when(facultyRepository.findById(any(Long.class)))
                .thenReturn(Optional.of(faculty));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.color").value(color));
    }

    @Test
    public void getDeleteFacultyById() throws Exception {
        when(facultyRepository.findById(any(Long.class))).thenReturn(Optional.of(faculty));
        doNothing().when(facultyRepository).deleteById(any(Long.class));
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/faculty/" + faculty.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetFacultyByColor() throws Exception {
        when(facultyRepository.save(any(Faculty.class))).thenReturn(faculty);
        when(facultyRepository.findFacultyByColor(any(String.class))).thenReturn(faculties);
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty/color?color=" + faculty.getColor()))
                .andExpect(status().isOk());
    }


}