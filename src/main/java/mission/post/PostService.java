package mission.post;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import mission.post.dto.CreatePostRequest;
import mission.post.dto.PostCreateResponse;
import mission.post.dto.PostUpdateRequest;
import mission.user.User;
import mission.user.UserRepository;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Transactional
    public PostCreateResponse create(CreatePostRequest createPostRequest) {
        User author = userRepository.findByEmail(createPostRequest.email())
                .orElseThrow(()->new IllegalArgumentException("email or password not found"));
        if (!BCrypt.checkpw(createPostRequest.password(), author.getPasswordHash()))
            throw new IllegalArgumentException("email or password does not match");

        Post saved = postRepository.save(
                Post.builder()
                        .title(createPostRequest.title())
                        .content(createPostRequest.content())
                        .author(author)
                        .build()
        );

        return new PostCreateResponse(saved.getId(), author.getEmail(), saved.getTitle(), saved.getContent());
    }

    @Transactional
    public PostCreateResponse update(Long id, PostUpdateRequest updatePostRequest){
        Post post = postRepository.findById(id)
                .orElseThrow(()->new IllegalArgumentException("post not found"));

        if(!post.getAuthor().getEmail().equals(updatePostRequest.email()) || !BCrypt.checkpw(updatePostRequest.password(), post.getAuthor().getPasswordHash())){
            throw new IllegalArgumentException("email or password does not match");
        }

        post.edit(updatePostRequest.title(), updatePostRequest.content());

        return new PostCreateResponse(
                post.getId(),
                post.getAuthor().getEmail(),
                post.getTitle(),
                post.getContent()
        );
    }

    @Transactional
    public void delete(Long id, String email, String password){
        Post post = postRepository.findById(id)
                .orElseThrow(()->new IllegalArgumentException("post not found"));

        if(!post.getAuthor().getEmail().equals(email) || !BCrypt.checkpw(password, post.getAuthor().getPasswordHash())){
            throw new IllegalArgumentException("email or password does not match");
        }

        postRepository.delete(post);
    }
}
