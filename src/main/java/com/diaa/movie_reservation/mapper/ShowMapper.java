package com.diaa.movie_reservation.mapper;

import com.diaa.movie_reservation.dto.show.ShowRequest;
import com.diaa.movie_reservation.dto.show.ShowResponse;
import com.diaa.movie_reservation.dto.show.ShowResponseExtended;
import com.diaa.movie_reservation.entity.Show;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring",uses = {MovieMapper.class, TheaterMapper.class})
public interface ShowMapper {
    Show toEntity(ShowRequest showRequest);

    ShowResponseExtended toExtendedDTO(Show show);

    @Mapping( target = "movieID", source = "movie.id")
    @Mapping( target = "theaterID", source = "theater.id")
    ShowResponse toDTO(Show show);

}
