package ua.course.moviesservice.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MovieListRequestDto {

    private String titleContains;
    private Long directorId;
    private Integer yearFrom;
    private Integer yearTo;
    private Integer page;
    private Integer size;

    public MovieListRequestDto() {
    }
}
