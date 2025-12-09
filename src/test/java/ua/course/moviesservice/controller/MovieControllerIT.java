package ua.course.moviesservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import ua.course.moviesservice.dto.MovieListRequestDto;
import ua.course.moviesservice.entity.Director;
import ua.course.moviesservice.entity.Movie;
import ua.course.moviesservice.repository.DirectorRepository;
import ua.course.moviesservice.repository.MovieRepository;

import java.nio.charset.StandardCharsets;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class MovieControllerIT {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private DirectorRepository directorRepository;

    @Autowired
    private MovieRepository movieRepository;

    private Director director;
    private Movie existingMovie;

    @BeforeEach
    void setUp() {
        movieRepository.deleteAll();
        directorRepository.deleteAll();

        director = directorRepository.save(new Director(null, "Christopher Nolan"));
        existingMovie = movieRepository.save(new Movie("Inception", 2010, director));
    }


    @Test
    void createMovie_shouldReturnCreated() throws Exception {
        String body = """
                {
                  "title": "Interstellar",
                  "releaseYear": 2014,
                  "directorId": %d
                }
                """.formatted(director.getId());

        mockMvc.perform(post("/api/movies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated());
    }


    @Test
    void getMovieById_shouldReturnOk() throws Exception {
        mockMvc.perform(get("/api/movies/{id}", existingMovie.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Inception"));
    }


    @Test
    void updateMovie_shouldReturnOk() throws Exception {
        String body = """
                {
                  "title": "Inception Updated",
                  "releaseYear": 2010,
                  "directorId": %d
                }
                """.formatted(director.getId());

        mockMvc.perform(put("/api/movies/{id}", existingMovie.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Inception Updated"));
    }


    @Test
    void deleteMovie_shouldReturnNoContent() throws Exception {
        mockMvc.perform(delete("/api/movies/{id}", existingMovie.getId()))
                .andExpect(status().isNoContent());
    }


    @Test
    void listMovies_shouldReturnOk() throws Exception {
        MovieListRequestDto request = new MovieListRequestDto();
        request.setTitleContains("");
        request.setDirectorId(null);
        request.setYearFrom(null);
        request.setYearTo(null);
        request.setPage(0);
        request.setSize(10);

        String json = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/api/movies/_list")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.list").isArray());
    }


    @Test
    void reportMovies_shouldReturnOk() throws Exception {
        MovieListRequestDto request = new MovieListRequestDto();
        request.setTitleContains("");
        request.setDirectorId(null);
        request.setYearFrom(null);
        request.setYearTo(null);

        String json = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/api/movies/_report")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());
    }


    @Test
    void uploadMovies_shouldReturnOk() throws Exception {
        String importJson = """
                [
                  {
                    "title": "Test Movie 1",
                    "releaseYear": 2000,
                    "directorId": %d
                  },
                  {
                    "title": "Test Movie 2",
                    "releaseYear": 2010,
                    "directorId": %d
                  }
                ]
                """.formatted(director.getId(), director.getId());

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "movies-import.json",
                "application/json",
                importJson.getBytes(StandardCharsets.UTF_8)
        );

        mockMvc.perform(multipart("/api/movies/upload")
                        .file(file))
                .andExpect(status().isOk());
    }
}
