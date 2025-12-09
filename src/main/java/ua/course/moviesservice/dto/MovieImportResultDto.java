package ua.course.moviesservice.dto;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class MovieImportResultDto {
    private int successCount;
    private int failedCount;
    private List<ErrorItem> errors = new ArrayList<>();

    public MovieImportResultDto() {
    }

    public MovieImportResultDto(int successCount, int failedCount, List<ErrorItem> errors) {
        this.successCount = successCount;
        this.failedCount = failedCount;
        this.errors = errors;
    }

    @Getter
    public static class ErrorItem {
        private final int index;
        private final String message;

        public ErrorItem(int index, String message) {
            this.index = index;
            this.message = message;
        }
    }
}
