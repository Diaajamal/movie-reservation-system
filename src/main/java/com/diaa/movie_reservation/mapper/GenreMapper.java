package com.diaa.movie_reservation.mapper;

import com.diaa.movie_reservation.dto.genre.GenreRequest;
import com.diaa.movie_reservation.dto.genre.GenreResponse;
import com.diaa.movie_reservation.entity.Genre;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface GenreMapper {
    Genre toEntity(GenreRequest genreRequest);

    GenreResponse toDTO(Genre genre);

}
