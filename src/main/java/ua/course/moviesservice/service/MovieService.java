package ua.course.moviesservice.service;

import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import ua.course.moviesservice.dto.*;
import ua.course.moviesservice.entity.Director;
import ua.course.moviesservice.entity.Movie;
import ua.course.moviesservice.repository.DirectorRepository;
import ua.course.moviesservice.repository.MovieRepository;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class MovieService {
    private final MovieRepository movieRepository;
    private final DirectorRepository directorRepository;

    public MovieService(MovieRepository movieRepository, DirectorRepository directorRepository) {
        this.movieRepository = movieRepository;
        this.directorRepository = directorRepository;
    }

    public MovieDetailsDto getByid(Long id){
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Movie with id " + id + " not found"));

        return mapToDetailsDto(movie);
    }

    public MovieDetailsDto create (MovieCreateUpdateDto dto) {
        Director director = directorRepository.findById(dto.getDirectorId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Director with id " + dto.getDirectorId() + " not found"));

        Movie movie = new Movie(dto.getTitle(), dto.getReleaseYear(), director);
        Movie saved = movieRepository.save(movie);
        return mapToDetailsDto(saved);
    }

    public MovieDetailsDto update (Long id, MovieCreateUpdateDto dto){
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Movie with id " + id + " not found"));

        Director director = directorRepository.findById(dto.getDirectorId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Director with id " + dto.getDirectorId() + " not found"));

        movie.setTitle(dto.getTitle());
        movie.setDirector(director);
        movie.setReleaseYear(dto.getReleaseYear());

        return mapToDetailsDto(movie);
    }

    public void delete(Long id) {
        if (!movieRepository.existsById(id)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Movie with id " + id + " not found"
            );
        }
        movieRepository.deleteById(id);
    }

    private MovieDetailsDto mapToDetailsDto(Movie movie) {
        Director director = movie.getDirector();
        DirectorDto directorDto = new DirectorDto(director.getId(), director.getName());

        return new MovieDetailsDto(movie.getId(), movie.getTitle(), movie.getReleaseYear(), directorDto);
    }


    public PagedResultDto<MovieListItemDto> getList(MovieListRequestDto request) {
        int page = (request.getPage() == null || request.getPage() < 0) ? 0 : request.getPage();
        int size = (request.getSize() == null || request.getSize() <= 0) ? 20 : request.getSize();

        Pageable pageable = PageRequest.of(page, size);

        Page<Movie> moviePage = movieRepository.searchMovies(
                emptyToNull(request.getTitleContains()),
                request.getDirectorId(),
                request.getYearFrom(),
                request.getYearTo(),
                pageable
        );

        var items = moviePage.getContent().stream()
                .map(m -> new MovieListItemDto(
                        m.getTitle(),
                        m.getReleaseYear(),
                        new DirectorDto(
                                m.getDirector().getId(),
                                m.getDirector().getName()
                        )
                ))
                .toList();

        return new PagedResultDto<>(items, moviePage.getTotalPages());
    }

    private String emptyToNull(String s) {
        return (s == null || s.isBlank()) ? null : s;
    }



    public byte[] generateReportCsv(MovieListRequestDto request) {
        String titleFilter = emptyToNull(request.getTitleContains());

        List<Movie> movies = movieRepository.searchMoviesNoPaging(
                titleFilter,
                request.getDirectorId(),
                request.getYearFrom(),
                request.getYearTo()
        );

        StringBuilder sb = new StringBuilder();
        sb.append("Id,Title,Release_year,DirectorId,DirectorName\n");

        for (Movie m : movies) {
            sb.append(m.getId()).append(",");
            String safeTitle = m.getTitle().replace("\"", "\"\"");
            sb.append("\"").append(safeTitle).append("\"").append(",");
            sb.append(m.getReleaseYear() != null ? m.getReleaseYear() : "").append(",");

            var director = m.getDirector();
            sb.append(director.getId()).append(",");
            String safeDirectorName = director.getName().replace("\"", "\"\"");
            sb.append("\"").append(safeDirectorName).append("\"").append("\n");
        }

        return sb.toString().getBytes(StandardCharsets.UTF_8);
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
