package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.skypro.homework.dto.auth.Role;
import ru.skypro.homework.dto.comment.CommentDto;
import ru.skypro.homework.dto.comment.CommentsDto;
import ru.skypro.homework.dto.comment.CreateOrUpdateCommentDto;
import ru.skypro.homework.exception.AdNotFoundException;
import ru.skypro.homework.exception.CommentNotFoundException;
import ru.skypro.homework.exception.UnauthorizedAccessException;
import ru.skypro.homework.mapper.CommentMapper;
import ru.skypro.homework.model.AdsDao;
import ru.skypro.homework.model.CommentsDao;
import ru.skypro.homework.model.UsersDao;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.repository.CommentRepository;
import ru.skypro.homework.service.CommentService;
import ru.skypro.homework.service.CurrentUserService;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final AdRepository adRepository;
    private final CommentMapper commentMapper;

    private final CurrentUserService currentUserService;

    @Override
    @Transactional(readOnly = true)
    public CommentsDto getComments(Integer adId) {
        if (!adRepository.existsById(adId)) {
            throw new AdNotFoundException("Ad not found with id: " + adId);
        }
        List<CommentsDao> comments = commentRepository.findByAdPkOrderByCreatedAtDesc(adId);
        List<CommentDto> commentDtos = comments.stream()
                                               .map(commentMapper::toCommentDto)
                                               .collect(Collectors.toList());
        CommentsDto result = new CommentsDto();
        result.setCount(commentDtos.size());
        result.setResults(commentDtos);
        return result;
    }

    @Override
    public CommentDto addComment(Integer adId, String email, CreateOrUpdateCommentDto createComment) {
        UsersDao author = currentUserService.getUserByEmail(email);
        AdsDao ad = getAdById(adId);

        CommentsDao comment = commentMapper.toCommentEntity(createComment);
        comment.setAuthor(author);
        comment.setAd(ad);
        CommentsDao savedComment = commentRepository.save(comment);

        log.info("Comment added with id: {} to ad: {} by user: {}", savedComment.getPk(), adId, email);
        return commentMapper.toCommentDto(savedComment);
    }

    @Override
    public void deleteComment(Integer adId, Integer commentId, String email) {
        CommentsDao comment = getCommentByIdAndAdId(commentId, adId);
        checkPermissions(comment, email);
        commentRepository.delete(comment);
        log.info("Comment deleted with id: {} from ad: {} by user: {}", commentId, adId, email);
    }

    @Override
    public CommentDto updateComment(Integer adId, Integer commentId, String email, CreateOrUpdateCommentDto updateComment) {
        CommentsDao comment = getCommentByIdAndAdId(commentId, adId);
        checkPermissions(comment, email);
        commentMapper.updateCommentFromDto(updateComment, comment);
        CommentsDao updatedComment = commentRepository.save(comment);
        log.info("Comment updated with id: {} in ad: {} by user: {}", commentId, adId, email);
        return commentMapper.toCommentDto(updatedComment);
    }

    private AdsDao getAdById(Integer adId) {
        return adRepository.findById(adId)
                           .orElseThrow(() -> new AdNotFoundException("Ad not found with id: " + adId));
    }

    private CommentsDao getCommentByIdAndAdId(Integer commentId, Integer adId) {
        return commentRepository.findByPkAndAdPk(commentId, adId)
                                .orElseThrow(() -> new CommentNotFoundException(
                                        String.format("Comment with id %d not found for ad id %d", commentId, adId)));
    }

    private void checkPermissions(CommentsDao comment, String email) {
        UsersDao user = currentUserService.getUserByEmail(email);
        boolean isAuthor = comment.getAuthor().getId().equals(user.getId());
        boolean isAdmin = user.getRole() == Role.ADMIN;
        if (!isAuthor && !isAdmin) {
            throw new UnauthorizedAccessException("User does not have permission to modify this comment");
        }
    }
}