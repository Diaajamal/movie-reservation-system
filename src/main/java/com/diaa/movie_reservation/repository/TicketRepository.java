package com.diaa.movie_reservation.repository;

import com.diaa.movie_reservation.entity.Status;
import com.diaa.movie_reservation.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
   boolean existsByShowIdAndSeatIdAndStatus(Long showId, Long seatId, Status status);
}
