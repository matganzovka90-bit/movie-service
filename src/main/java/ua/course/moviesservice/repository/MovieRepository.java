package ua.course.moviesservice.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ua.course.moviesservice.entity.Movie;

import java.util.List;

public interface MovieRepository extends JpaRepository<Movie, Long> {

    @Query("""
       SELECT m FROM Movie m
       WHERE (:title IS NULL 
              OR LOWER(m.title) LIKE CONCAT('%', LOWER(CAST(:title AS string)), '%'))
         AND (:directorId IS NULL OR m.director.id = :directorId)
         AND (:yearFrom IS NULL OR m.releaseYear >= :yearFrom)
         AND (:yearTo IS NULL OR m.releaseYear <= :yearTo)
       """)
    Page<Movie> searchMovies(
            @Param("title") String title,
            @Param("directorId") Long directorId,
            @Param("yearFrom") Integer yearFrom,
            @Param("yearTo") Integer yearTo,
            Pageable pageable
    );

}
