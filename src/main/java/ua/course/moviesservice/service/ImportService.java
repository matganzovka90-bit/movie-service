package ua.course.moviesservice.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ua.course.moviesservice.dto.MovieImportDto;
import ua.course.moviesservice.dto.MovieImportResultDto;
import ua.course.moviesservice.entity.Director;
import ua.course.moviesservice.entity.Movie;
import ua.course.moviesservice.repository.DirectorRepository;
import ua.course.moviesservice.repository.MovieRepository;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class ImportService {

    private final MovieRepository movieRepository;
    private final DirectorRepository directorRepository;

    public ImportService(MovieRepository movieRepository, DirectorRepository directorRepository) {
        this.movieRepository = movieRepository;
        this.directorRepository = directorRepository;
    }

    public MovieImportResultDto importMovies(InputStream jsonStream) {
        ObjectMapper mapper = new ObjectMapper();

        List<MovieImportDto> items;
        try {
            items = mapper.readValue(jsonStream, new TypeReference<List<MovieImportDto>>() {});
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Invalid JSON file format",
                    e
            );
        }

        int success = 0;
        int failed = 0;
        List<MovieImportResultDto.ErrorItem> errors = new ArrayList<>();

        for (int i = 0; i < items.size(); i++) {
            MovieImportDto dto = items.get(i);
            try {
                if (dto.getTitle() == null || dto.getTitle().isBlank()) {
                    throw new IllegalArgumentException("Title is empty");
                }
                if (dto.getReleaseYear() == null) {
                    throw new IllegalArgumentException("Release year is empty");
                }
                if (dto.getDirectorId() == null) {
                    throw new IllegalArgumentException("DirectorId is empty");
                }

                Director director = directorRepository.findById(dto.getDirectorId())
                        .orElseThrow(() -> new IllegalArgumentException(
                                "Director with id " + dto.getDirectorId() + " not found"
                        ));

                Movie movie = new Movie();
                movie.setTitle(dto.getTitle());
                movie.setReleaseYear(dto.getReleaseYear());
                movie.setDirector(director);

                movieRepository.save(movie);
                success++;
            } catch (Exception ex) {
                failed++;
                errors.add(new MovieImportResultDto.ErrorItem(i, ex.getMessage()));
            }
        }

        return new MovieImportResultDto(success, failed, errors);
    }
}
