package ua.course.moviesservice.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import ua.course.moviesservice.dto.*;
import ua.course.moviesservice.service.ImportService;
import ua.course.moviesservice.service.MovieService;
import ua.course.moviesservice.service.ReportService;

import java.io.IOException;
import java.io.InputStream;

@RestController
@RequestMapping("/api/movies")
public class MovieController {
    private final MovieService movieService;
    private final ReportService reportService;
    private final ImportService importService;

    public MovieController(MovieService movieService, ReportService reportService, ImportService importService) {
        this.movieService = movieService;
        this.reportService = reportService;
        this.importService = importService;
    }

    @GetMapping("/{id}")
    public MovieDetailsDto getById(@PathVariable Long id) {
        return movieService.getByid(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MovieDetailsDto create(@Valid @RequestBody MovieCreateUpdateDto dto) {
        return movieService.create(dto);
    }

    @PutMapping("/{id}")
    public MovieDetailsDto update(@PathVariable Long id, @Valid @RequestBody MovieCreateUpdateDto dto) {
        return movieService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        movieService.delete(id);
    }

    @PostMapping("/_list")
    public PagedResultDto<MovieListItemDto> getList(@RequestBody MovieListRequestDto request) {
        return movieService.getList(request);
    }

    @PostMapping("/_report")
    public ResponseEntity<byte[]> getReport(@RequestBody MovieListRequestDto request) {
        byte[] csv = reportService.generateReportCsv(request);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_PLAIN);
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=movies-report.csv");

        return new ResponseEntity<>(csv, headers, HttpStatus.OK);
    }

    @PostMapping("/upload")
    public MovieImportResultDto upload(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "File is empty");
        }

        try (InputStream is = file.getInputStream()) {
            return importService.importMovies(is);
        } catch (IOException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Cannot read uploaded file",
                    e
            );
        }
    }
}
