package ua.course.moviesservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ua.course.moviesservice.entity.Director;
import ua.course.moviesservice.repository.DirectorRepository;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class DirectorControllerSimpleIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private DirectorRepository directorRepository;

    private Director existingDirector;

    @BeforeEach
    void setUp() {
        directorRepository.deleteAll();
        existingDirector = directorRepository.save(new Director(null, "Christopher Nolan"));
    }

    @Test
    void createDirector_shouldReturn2xx() throws Exception {
        String body = """
                {
                  "name": "Quentin Tarantino"
                }
                """;

        mockMvc.perform(post("/api/directors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void getDirector_shouldReturn2xx() throws Exception {
        mockMvc.perform(get("/api/directors/{id}", existingDirector.getId()))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void updateDirector_shouldReturn2xx() throws Exception {
        String body = """
                {
                  "name": "Christopher Nolan Updated"
                }
                """;

        mockMvc.perform(put("/api/directors/{id}", existingDirector.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void deleteDirector_shouldReturn2xxOr204() throws Exception {
        mockMvc.perform(delete("/api/directors/{id}", existingDirector.getId()))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void listDirectors_shouldReturn2xx() throws Exception {
        mockMvc.perform(get("/api/directors"))
                .andExpect(status().is2xxSuccessful());
    }
}
