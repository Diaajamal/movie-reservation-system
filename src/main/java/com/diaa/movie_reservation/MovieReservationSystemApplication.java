package com.diaa.movie_reservation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class MovieReservationSystemApplication {

    public static void main(String[] args) {
        // Start the Spring Boot application
        SpringApplication.run(MovieReservationSystemApplication.class, args);
    }

}
