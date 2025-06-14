package com.diaa.movie_reservation.repository;

import com.diaa.movie_reservation.entity.Seat;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SeatRepository extends JpaRepository<Seat, Long> {
    Page<Seat> findAllByTheater_Id(Short theaterId, Pageable pageable);

    Page<Seat> findByTheater_IdAndIdNotIn(Short theaterId, List<Long> excludedSeatIds,Pageable pageable);

    boolean existsByTheater_IdAndRowLabelAndNumber(Short theaterId, String rowLabel, short number);
}
