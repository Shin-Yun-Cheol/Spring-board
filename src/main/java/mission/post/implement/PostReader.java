package mission.post.implement;


import lombok.RequiredArgsConstructor;
import mission.post.domain.Post;
import mission.post.repository.PostRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PostReader {
    private final PostRepository postRepository;

    public Post getById(Long id) {
        return postRepository.findById(id)
                .orElseThrow(()->new IllegalArgumentException("post not found"));
    }
}
