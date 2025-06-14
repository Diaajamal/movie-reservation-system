package com.diaa.movie_reservation.repository;

import com.diaa.movie_reservation.entity.Show;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface ShowRepository extends JpaRepository<Show, Long> {
    Page<Show> findAllByShowTimeAfter(LocalDateTime showTimeAfter, Pageable pageable);
}
