package ru.skypro.homework.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import ru.skypro.homework.config.MapStructConfig;
import ru.skypro.homework.dto.auth.Register;
import ru.skypro.homework.dto.user.UpdateUser;
import ru.skypro.homework.dto.user.UserDto;
import ru.skypro.homework.model.User;

@Mapper(config = MapStructConfig.class)
public interface UserMapper {

    UserDto toUserDto(User entity);

    @Mapping(source = "username", target = "email")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "image", ignore = true)
    @Mapping(target = "ads", ignore = true)
    @Mapping(target = "comments", ignore = true)
    User toUserEntity(Register registerDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateUserFromDto(UpdateUser dto, @MappingTarget User entity);
}