package ua.course.moviesservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.course.moviesservice.entity.Director;

import java.util.Optional;

public interface DirectorRepository extends JpaRepository<Director, Long> {
    Optional<Director> findByName(String name);
}
