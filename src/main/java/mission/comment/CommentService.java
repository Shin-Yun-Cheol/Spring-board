package mission.comment;

import mission.comment.dto.*;
import mission.post.Post;
import mission.post.PostRepository;
import lombok.RequiredArgsConstructor;
import mission.user.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Transactional
    public CommentResponse create(Long postId, Long userId, CommentCreateRequest req) {
        User author = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));

        Comment c = Comment.builder()
                .post(post)
                .author(author)
                .content(req.content())
                .build();
        return CommentResponse.from(commentRepository.save(c));
    }

    @Transactional
    public CommentResponse update(Long commentId, Long userId, CommentUpdateRequest req) {
        Comment c = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 댓글입니다."));
        if (!c.getAuthor().getId().equals(userId)) {
            throw new IllegalArgumentException("본인 댓글만 수정할 수 있습니다.");
        }
        c.changeContent(req.content());
        return CommentResponse.from(c);
    }

    @Transactional
    public void delete(Long commentId, Long userId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 댓글입니다."));

        if (!comment.getAuthor().getId().equals(userId)) {
            throw new IllegalArgumentException("본인 댓글만 삭제할 수 있습니다.");
        }

        commentRepository.delete(comment);
    }

}