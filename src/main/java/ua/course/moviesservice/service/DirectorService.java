package ua.course.moviesservice.service;

import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ua.course.moviesservice.dto.DirectorCreateUpdateDto;
import ua.course.moviesservice.dto.DirectorDto;
import ua.course.moviesservice.entity.Director;
import ua.course.moviesservice.repository.DirectorRepository;

import java.util.List;

@Service
@Transactional
public class DirectorService {
    private final DirectorRepository directorRepository;

    public DirectorService(DirectorRepository directorRepository) {
        this.directorRepository = directorRepository;
    }

    public List<DirectorDto> getAll() {
        return directorRepository.findAll()
                .stream()
                .map(d -> new DirectorDto(d.getId(), d.getName()))
                .toList();
    }

    public DirectorDto getById(Long id) {
        Director director = directorRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Director with id" + id + " not found"
                ));

        return new DirectorDto(director.getId(), director.getName());
    }

    public DirectorDto create(DirectorCreateUpdateDto dto){
        directorRepository.findByName(dto.getName())
                .ifPresent(existing -> {
                    throw new ResponseStatusException(HttpStatus.CONFLICT,
                            "Director with name: " + dto.getName() + " already exists");
                });

        Director director = new Director(dto.getName());

        Director saved = directorRepository.save(director);

        return new DirectorDto(saved.getId(), saved.getName());
    }

    public DirectorDto update(Long id, DirectorCreateUpdateDto dto) {
        Director director = directorRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Director with id" + id + " not found"));

        directorRepository.findByName(dto.getName())
                .ifPresent(existing -> {
                    if(!existing.getId().equals(id)){
                        throw new ResponseStatusException(HttpStatus.CONFLICT,
                                "Director with name '" + dto.getName() + "' already exists");
                    }
                });

        director.setName(dto.getName());
        return new DirectorDto(director.getId(), director.getName());
    }

    public void delete(Long id) {
        if(!directorRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Director with id" + id + " not found");
        }

        directorRepository.deleteById(id);
    }
}
