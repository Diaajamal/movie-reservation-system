package com.diaa.movie_reservation.repository;

import com.diaa.movie_reservation.dto.reservation.ReservationResponse;
import com.diaa.movie_reservation.entity.Status;
import com.diaa.movie_reservation.entity.Ticket;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    boolean existsByShowIdAndSeatIdAndStatus(Long showId, Long seatId, Status status);

    List<Ticket> findByUser_IdAndShow_ShowTimeAfterAndStatus(Long userId, LocalDateTime showShowTimeAfter, Status status);

    @Query("""
                SELECT new com.diaa.movie_reservation.dto.reservation.ReservationResponse(
                  t.id,
                  m.title,
                  CONCAT(se.rowLabel, se.number),
                  t.status,
                  t.pricePaid,
                  t.createdAt,
                  t.updatedAt
                )
                FROM Ticket t
                JOIN t.show sh
                JOIN t.seat se
                JOIN sh.movie m
                WHERE t.show.id = :showId
                  AND (:status IS NULL OR t.status = :status)
                ORDER BY t.createdAt DESC
            """)
    Page<ReservationResponse> findReservationsByShowAndStatus(@Param("showId") Long showId, @Param("status") Status status, Pageable pageable);
}
