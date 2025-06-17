package com.diaa.movie_reservation.service;

import com.diaa.movie_reservation.dto.ticket.TicketRequest;
import com.diaa.movie_reservation.dto.ticket.TicketResponse;
import com.diaa.movie_reservation.entity.*;
import com.diaa.movie_reservation.exception.seat.SeatAlreadyBookedException;
import com.diaa.movie_reservation.mapper.TicketMapper;
import com.diaa.movie_reservation.repository.TicketRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TicketServiceTest {
    private TicketRepository ticketRepository;
    private UserService userService;
    private ShowService showService;
    private SeatService seatService;
    private TicketMapper ticketMapper;
    private DistributedLockService distributedLockService;
    private TicketService ticketService;

    @BeforeEach
    void setUp() {
        ticketRepository = mock(TicketRepository.class);
        userService = mock(UserService.class);
        showService = mock(ShowService.class);
        seatService = mock(SeatService.class);
        ticketMapper = mock(TicketMapper.class);
        distributedLockService = mock(DistributedLockService.class);
        ticketService = new TicketService(ticketRepository, userService, showService, seatService, ticketMapper, distributedLockService);
    }

    @Test
    void bookTicketShouldAcquireAndReleaseLock() {
        TicketRequest request = new TicketRequest(1L, 2L, 3L, BigDecimal.TEN);
        User user = User.builder().id(1L).email("test@example.com").password("pwd").build();
        Theater theater = Theater.builder().id((short) 1).name("T").totalSeats(10).location("loc").build();
        Seat seat = Seat.builder().id(3L).theater(theater).rowLabel("A").number((short) 1).build();
        Show show = Show.builder().id(2L).theater(theater).movie(new Movie()).showTime(LocalDateTime.now().plusHours(1)).price(BigDecimal.ONE).build();
        Ticket ticket = new Ticket();
        Ticket savedTicket = new Ticket();
        TicketResponse response = new TicketResponse(1L, null, null, Status.BOOKED.name(), BigDecimal.TEN, null, null);

        when(userService.getUser(1L)).thenReturn(user);
        when(showService.getShow(2L)).thenReturn(show);
        when(seatService.getSeatForUpdate(3L)).thenReturn(seat);
        when(distributedLockService.acquireLock("2:3", 5, TimeUnit.SECONDS)).thenReturn(true);
        when(ticketRepository.existsByShowIdAndSeatIdAndStatus(2L, 3L, Status.BOOKED)).thenReturn(false);
        when(ticketMapper.toEntity(request)).thenReturn(ticket);
        when(ticketRepository.save(ticket)).thenReturn(savedTicket);
        when(ticketMapper.toDTO(savedTicket)).thenReturn(response);

        TicketResponse result = ticketService.bookTicket(request);

        assertEquals(response, result);
        verify(distributedLockService).acquireLock("2:3", 5, TimeUnit.SECONDS);
        verify(distributedLockService).releaseLock("2:3");
    }

    @Test
    void bookTicketShouldThrowIfLockNotAcquired() {
        TicketRequest request = new TicketRequest(1L, 2L, 3L, BigDecimal.TEN);
        Theater theater = Theater.builder().id((short) 1).name("T").totalSeats(10).location("loc").build();
        Seat seat = Seat.builder().id(3L).theater(theater).rowLabel("A").number((short) 1).build();
        Show show = Show.builder().id(2L).theater(theater).movie(new Movie()).showTime(LocalDateTime.now().plusHours(1)).price(BigDecimal.ONE).build();
        User user = User.builder().id(1L).email("test@example.com").password("pwd").build();

        when(userService.getUser(1L)).thenReturn(user);
        when(showService.getShow(2L)).thenReturn(show);
        when(seatService.getSeatForUpdate(3L)).thenReturn(seat);
        when(distributedLockService.acquireLock("2:3", 5, TimeUnit.SECONDS)).thenReturn(false);

        assertThrows(SeatAlreadyBookedException.class, () -> ticketService.bookTicket(request));
        verify(distributedLockService, never()).releaseLock(any());
        verify(ticketRepository, never()).save(any());
    }

    @Test
    void simulateBookingRaceCondition() throws InterruptedException {
        TicketRequest request1 = new TicketRequest(1L, 2L, 3L, BigDecimal.TEN);
        TicketRequest request2 = new TicketRequest(2L, 2L, 3L, BigDecimal.TEN);

        Theater theater = Theater.builder().id((short) 1).name("T").totalSeats(10).location("loc").build();
        Seat seat = Seat.builder().id(3L).theater(theater).rowLabel("A").number((short) 1).build();
        Show show = Show.builder().id(2L).theater(theater).movie(new Movie()).showTime(LocalDateTime.now().plusHours(1)).price(BigDecimal.ONE).build();
        User user1 = User.builder().id(1L).email("test1@example.com").password("pwd").build();
        User user2 = User.builder().id(2L).email("test2@example.com").password("pwd").build();

        when(showService.getShow(2L)).thenReturn(show);
        when(seatService.getSeatForUpdate(3L)).thenReturn(seat);
        when(distributedLockService.acquireLock("2:3", 5, TimeUnit.SECONDS)).thenReturn(true);
        when(ticketRepository.existsByShowIdAndSeatIdAndStatus(2L, 3L, Status.BOOKED))
                .thenReturn(false) // first call: seat not booked
                .thenReturn(true); // second call: seat booked

        when(userService.getUser(1L)).thenReturn(user1);
        when(userService.getUser(2L)).thenReturn(user2);

        Ticket ticket = new Ticket();
        Ticket savedTicket = new Ticket();
        TicketResponse response = new TicketResponse(1L, null, null, Status.BOOKED.name(), BigDecimal.TEN, null, null);

        when(ticketMapper.toEntity(any())).thenReturn(ticket);
        when(ticketRepository.save(ticket)).thenReturn(savedTicket);
        when(ticketMapper.toDTO(savedTicket)).thenReturn(response);

        List<Exception> exceptions = Collections.synchronizedList(new ArrayList<>());
        CountDownLatch latch = new CountDownLatch(1);

        Runnable bookingTask1 = () -> {
            try {
                latch.await();
                ticketService.bookTicket(request1);
            } catch (Exception e) {
                exceptions.add(e);
            }
        };
        Runnable bookingTask2 = () -> {
            try {
                latch.await();
                ticketService.bookTicket(request2);
            } catch (Exception e) {
                exceptions.add(e);
            }
        };

        Thread t1 = new Thread(bookingTask1);
        Thread t2 = new Thread(bookingTask2);

        t1.start();
        t2.start();
        latch.countDown();

        t1.join();
        t2.join();

        verify(ticketRepository, atMost(1)).save(any());
        long seatAlreadyBookedCount = exceptions.stream()
                .filter(SeatAlreadyBookedException.class::isInstance)
                .count();
        assertEquals(1, seatAlreadyBookedCount, "Exactly one booking should fail with SeatAlreadyBookedException");
    }
}