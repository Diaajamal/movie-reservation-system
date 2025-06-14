package com.diaa.movie_reservation.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "theaters")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString(exclude = "shows")
public class Theater {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Short id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(name = "total_seats", nullable = false)
    private Integer totalSeats;

    @Column(nullable = false)
    private String location;

    @OneToMany(
            mappedBy = "theater",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private Set<Show> shows = new HashSet<>();

    @Override
    public int hashCode() {
        return Short.hashCode(id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Theater theater = (Theater) o;
        return id != null && id.equals(theater.id);
    }
}
