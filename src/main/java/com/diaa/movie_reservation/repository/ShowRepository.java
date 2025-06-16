package com.diaa.movie_reservation.repository;

import com.diaa.movie_reservation.dto.seat.ShowSeatResponse;
import com.diaa.movie_reservation.entity.Show;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;



@Repository
public interface ShowRepository extends JpaRepository<Show, Long> {
    Page<Show> findAllByShowTimeAfter(LocalDateTime showTimeAfter, Pageable pageable);

    Page<Show> findAllByShowTimeBetween(LocalDateTime showTimeAfter, LocalDateTime showTimeBefore, Pageable pageable);

    @Query("""
    SELECT new com.diaa.movie_reservation.dto.seat.ShowSeatResponse(
      s.id,
      s.rowLabel,
      s.number,
      COALESCE(t.status)
    )
    FROM Seat s
    LEFT JOIN Ticket t
      ON t.seat   = s
     AND t.show.id = :showId
     AND t.status  = com.diaa.movie_reservation.entity.Status.BOOKED
""")
    Page<ShowSeatResponse> findShowSeats(
            @Param("showId") Long showId,
            Pageable pageable
    );
}
