package com.diaa.movie_reservation.repository;

import com.diaa.movie_reservation.entity.Theater;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TheaterRepository extends JpaRepository<Theater, Short> {
}
