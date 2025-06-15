package com.diaa.movie_reservation.repository;

import com.diaa.movie_reservation.entity.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;


@Repository
public interface GenreRepository extends JpaRepository<Genre, Short> {
    long countByIdIn(Set<Short> ids);

    @Modifying
    @Query(value = "DELETE FROM movie_genres WHERE genre_id = :genreId", nativeQuery = true)
    void deleteGenreFromMovieGenres(short genreId);
}
