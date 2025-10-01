package mission.post.implement;

import lombok.RequiredArgsConstructor;
import mission.post.domain.Post;
import mission.post.repository.PostRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PostWriter {
    private final PostRepository postRepository;

    public Post save(Post post){return postRepository.save(post);}
    public void delete(Post post){postRepository.delete(post);}

}
