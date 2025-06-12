package com.diaa.movie_reservation.mapper;

import com.diaa.movie_reservation.dto.movie.MovieRequest;
import com.diaa.movie_reservation.dto.movie.MovieResponse;
import com.diaa.movie_reservation.entity.Movie;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring",uses = {GenreMapper.class})
public interface MovieMapper {
    Movie toEntity(MovieRequest movieRequest);

    @Mapping(target = "genres", source = "genres")
    MovieResponse toDTO(Movie movie);
}
