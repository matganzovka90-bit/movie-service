package ua.course.moviesservice.dto;

import lombok.Getter;

@Getter
public class DirectorDto {
    private Long id;
    private String name;

    public DirectorDto(){}

    public DirectorDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
