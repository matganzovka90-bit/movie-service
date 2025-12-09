package ua.course.moviesservice.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class MovieCreateUpdateDto {
    @NotBlank(message = "Title is required")
    @Size(max = 255, message = "Title must be at most 255 characters")
    private String title;

    @NotNull(message = "Year is required")
    @Min(value = 1888, message = "Release year must be >= 1888")
    private Integer releaseYear;

    @NotNull(message = "Director id is required")
    private Long directorId;

    public MovieCreateUpdateDto() {}

    public MovieCreateUpdateDto(String title, Integer releaseYear, Long directorId) {
        this.title = title;
        this.releaseYear = releaseYear;
        this.directorId = directorId;
    }
}
