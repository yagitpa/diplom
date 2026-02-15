package ru.skypro.homework.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import ru.skypro.homework.config.MapStructConfig;
import ru.skypro.homework.dto.auth.RegisterDto;
import ru.skypro.homework.dto.user.UpdateUserDto;
import ru.skypro.homework.dto.user.UserDto;
import ru.skypro.homework.model.UsersDao;

@Mapper(config = MapStructConfig.class)
public interface UserMapper {

    UserDto toUserDto(UsersDao entity);

    @Mapping(source = "username", target = "email")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "image", ignore = true)
    @Mapping(target = "adsDaos", ignore = true)
    @Mapping(target = "commentsDaos", ignore = true)
    UsersDao toUserEntity(RegisterDto registerDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateUserFromDto(UpdateUserDto dto, @MappingTarget UsersDao entity);
}