package com.diaa.movie_reservation.repository;

import com.diaa.movie_reservation.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {
}
