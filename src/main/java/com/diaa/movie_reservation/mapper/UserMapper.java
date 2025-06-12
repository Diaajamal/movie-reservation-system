package com.diaa.movie_reservation.mapper;

import com.diaa.movie_reservation.dto.user.UserResponse;
import com.diaa.movie_reservation.dto.auth.RegisterRequest;
import com.diaa.movie_reservation.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserResponse toDTO(User user);

    User toEntity(RegisterRequest registerRequest);
}
