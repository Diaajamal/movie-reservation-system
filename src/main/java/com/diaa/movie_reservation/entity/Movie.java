package com.diaa.movie_reservation.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "movies")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"genres", "shows"})
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(length = 500)
    private String description;

    @Column(nullable = false)
    private String director;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "movie_genres", joinColumns = @JoinColumn(name = "movie_id"), inverseJoinColumns = @JoinColumn(name = "genre_id"))
    private Set<Genre> genres = new HashSet<>();

    @Column(nullable = false)
    private Integer duration;

    @Column(nullable = false, name = "release_date")
    private LocalDate releaseDate;

    @Column(name = "poster_url")
    private String posterUrl;

    @OneToMany(
            mappedBy = "movie",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private Set<Show> shows = new HashSet<>();

    @Override
    public int hashCode() {
        return Long.hashCode(id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Movie movie = (Movie) o;
        return id != 0 && id == movie.id;
    }
}
