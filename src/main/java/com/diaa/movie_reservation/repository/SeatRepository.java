package com.diaa.movie_reservation.repository;

import com.diaa.movie_reservation.entity.Seat;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SeatRepository extends JpaRepository<Seat, Long> {
    Page<Seat> findAllByTheater_Id(Short theaterId, Pageable pageable);

    Page<Seat> findByTheater_IdAndIdNotIn(Short theaterId, List<Long> excludedSeatIds,Pageable pageable);

    boolean existsByTheater_IdAndRowLabelAndNumber(Short theaterId, String rowLabel, short number);


    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT s FROM Seat s WHERE s.id = :id")
    Optional<Seat> findByIdForUpdate(@Param("id") Long id);
}
