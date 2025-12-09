package ua.course.moviesservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class DirectorCreateUpdateDto {

    @NotBlank(message = "Name is required")
    @Size(max = 255,message = "Name must be at most 255 characters")
    private String name;

    public DirectorCreateUpdateDto() {}

    public DirectorCreateUpdateDto(String name) {
        this.name = name;
    }
}
