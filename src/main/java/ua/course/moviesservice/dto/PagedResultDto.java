package ua.course.moviesservice.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class PagedResultDto<T> {
    private final List<T> list;
    private final int totalPages;

    public PagedResultDto(List<T> list, int totalPages) {
        this.list = list;
        this.totalPages = totalPages;
    }
}
