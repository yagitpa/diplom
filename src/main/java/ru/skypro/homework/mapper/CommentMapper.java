package ru.skypro.homework.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;
import ru.skypro.homework.config.MapStructConfig;
import ru.skypro.homework.dto.comment.CommentDto;
import ru.skypro.homework.dto.comment.CreateOrUpdateComment;
import ru.skypro.homework.model.Comment;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Mapper(config = MapStructConfig.class)
public interface CommentMapper {

    @Mapping(source = "author.id", target = "author")
    @Mapping(source = "author.image", target = "authorImage")
    @Mapping(source = "author.firstName", target = "authorFirstName")
    @Mapping(source = "createdAt", target = "createdAt", qualifiedByName = "localDateTimeToEpochMillis")
    CommentDto toCommentDto(Comment entity);

    @Mapping(target = "pk", ignore = true)
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "author", ignore = true)
    @Mapping(target = "ad", ignore = true)
    Comment toCommentEntity(CreateOrUpdateComment dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "pk", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "author", ignore = true)
    @Mapping(target = "ad", ignore = true)
    void updateCommentFromDto(CreateOrUpdateComment dto, @MappingTarget Comment entity);

    // ========== Вспомогательный метод: LocalDateTime → Long (epoch millis) ==========
    @Named("localDateTimeToEpochMillis")
    default Long localDateTimeToEpochMillis(LocalDateTime dateTime) {
        if (dateTime == null) return null;
        return dateTime.toInstant(ZoneOffset.UTC).toEpochMilli();
    }

    default LocalDateTime epochMillisToLocalDateTime(Long epoch) {
        if (epoch == null) return null;
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(epoch), ZoneOffset.UTC);
    }
}