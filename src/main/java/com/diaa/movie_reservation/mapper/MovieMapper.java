package com.diaa.movie_reservation.mapper;

import com.diaa.movie_reservation.dto.movie.MovieRequest;
import com.diaa.movie_reservation.dto.movie.MovieResponse;
import com.diaa.movie_reservation.entity.Genre;
import com.diaa.movie_reservation.entity.Movie;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring",uses = {GenreMapper.class})
public interface MovieMapper {
    @Mapping(target= "genres", source = "genreIds")
    Movie toEntity(MovieRequest movieRequest);

    @Mapping(target = "genres", source = "genres")
    MovieResponse toDTO(Movie movie);


    default Set<Genre> map(Set<Short> genreIds) {
        if (genreIds == null) {
            return Set.of();
        }
        return genreIds.stream()
                .map(id -> {
                    Genre genre = new Genre();
                    genre.setId(id);
                    return genre;
                })
                .collect(Collectors.toSet());
    }
}
