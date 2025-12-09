package ua.course.moviesservice.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="director")
@Getter
@Setter
public class Director {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    public Director(){}

    public Director(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Director(String name){
        this.name = name;
    }
}
