package ua.course.moviesservice.dto;

import lombok.Getter;

@Getter
public class MovieListItemDto {
    private String title;
    private Integer releaseYear;
    private DirectorDto director;

    public MovieListItemDto() {}

    public MovieListItemDto(String title, Integer releaseYear, DirectorDto director) {
        this.title = title;
        this.releaseYear = releaseYear;
        this.director = director;
    }
}
