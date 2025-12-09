package ua.course.moviesservice.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ua.course.moviesservice.dto.DirectorCreateUpdateDto;
import ua.course.moviesservice.dto.DirectorDto;
import ua.course.moviesservice.service.DirectorService;

import java.util.List;

@RestController
@RequestMapping("/api/directors")
public class DirectorController {
    private final DirectorService directorService;

    public DirectorController(DirectorService directorService) {
        this.directorService = directorService;
    }

    @GetMapping
    public List<DirectorDto> getAll(){
        return directorService.getAll();
    }

    @GetMapping("/{id}")
    public DirectorDto getById (@PathVariable Long id) {
        return directorService.getById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DirectorDto create(@Valid @RequestBody DirectorCreateUpdateDto dto) {
        return directorService.create(dto);
    }

    @PutMapping("/{id}")
    public DirectorDto update(@PathVariable Long id,
                              @Valid @RequestBody DirectorCreateUpdateDto dto) {
        return directorService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        directorService.delete(id);
    }
}
