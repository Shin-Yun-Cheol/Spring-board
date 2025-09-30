package mission.post.implement;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import mission.post.domain.Post;
import mission.post.repository.PostRepository;
import mission.post.dto.CreatePostRequest;
import mission.post.dto.PostCreateResponse;
import mission.post.dto.UpdatePostRequest;
import mission.post.business.PostService;
import mission.user.domain.User;
import mission.user.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Override
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

    @Override
    @Transactional
    public PostCreateResponse update(Long id, UpdatePostRequest updatePostRequest){
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
    @Override
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
