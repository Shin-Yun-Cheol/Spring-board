package mission.comment;

import mission.comment.dto.*;
import mission.post.Post;
import mission.post.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    @Transactional
    public CommentResponse create(Long postId, CommentCreateRequest req) {
        validateContent(req.content());

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));

        Comment saved = commentRepository.save(
                Comment.builder()
                        .post(post)
                        .email(req.email())
                        .password(req.password())
                        .content(req.content().trim())
                        .build()
        );

        return new CommentResponse(saved.getId(), saved.getEmail(), saved.getContent());
    }

    @Transactional
    public CommentResponse update(Long commentId, CommentUpdateRequest req) {
        validateContent(req.content());

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 댓글입니다."));

        verifyOwnership(comment, req.email(), req.password());

        comment.changeContent(req.content().trim());

        return new CommentResponse(comment.getId(), comment.getEmail(), comment.getContent());
    }

    @Transactional
    public void delete(Long commentId, CommentDeleteRequest req) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 댓글입니다."));

        verifyOwnership(comment, req.email(), req.password());

        commentRepository.delete(comment);
    }

    private void validateContent(String content) {
        if (!StringUtils.hasText(content)) {
            throw new IllegalArgumentException("content는 공백/빈문자열/NULL을 허용하지 않습니다.");
        }
    }

    private void verifyOwnership(Comment comment, String email, String password) {
        if (!comment.getEmail().equals(email) || !comment.getPassword().equals(password)) {
            throw new SecurityException("본인 댓글만 수정/삭제할 수 있습니다.");
        }
    }
}