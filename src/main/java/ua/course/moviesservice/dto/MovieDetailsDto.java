package ua.course.moviesservice.dto;

import lombok.Getter;

@Getter
public class MovieDetailsDto {
    private Long id;
    private String title;
    private Integer release_year;
    private DirectorDto director;

    public MovieDetailsDto() {}

    public MovieDetailsDto(Long id, String title, Integer release_year, DirectorDto director) {
        this.id = id;
        this.title = title;
        this.release_year = release_year;
        this.director = director;
    }
}
