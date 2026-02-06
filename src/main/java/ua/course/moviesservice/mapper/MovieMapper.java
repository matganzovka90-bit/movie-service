package ua.course.moviesservice.mapper;

import org.springframework.stereotype.Component;
import ua.course.moviesservice.dto.DirectorDto;
import ua.course.moviesservice.dto.MovieDetailsDto;
import ua.course.moviesservice.dto.MovieListItemDto;
import ua.course.moviesservice.entity.Movie;

@Component
public class MovieMapper {
    public MovieDetailsDto toDetailsDto(Movie movie){
        return new MovieDetailsDto(
                movie.getId(),
                movie.getTitle(),
                movie.getReleaseYear(),
                new DirectorDto(
                        movie.getDirector().getId(),
                        movie.getDirector().getName()
                )
        );
    }

    public MovieListItemDto toListItemDto(Movie m){
        return new MovieListItemDto(
                m.getTitle(),
                m.getReleaseYear(),
                new DirectorDto(
                        m.getDirector().getId(),
                        m.getDirector().getName()
                )
        );
    }
}
