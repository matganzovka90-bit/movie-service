package ua.course.moviesservice.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.course.moviesservice.dto.MovieListRequestDto;
import ua.course.moviesservice.entity.Movie;
import ua.course.moviesservice.repository.MovieRepository;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
@Transactional
public class ReportService {
    private final MovieRepository movieRepository;

    public ReportService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
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
}
