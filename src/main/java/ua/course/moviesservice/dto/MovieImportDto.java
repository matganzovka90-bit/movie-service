package ua.course.moviesservice.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MovieImportDto {
    private String title;
    private Integer releaseYear;
    private Long directorId;

    public MovieImportDto() {
    }
}
