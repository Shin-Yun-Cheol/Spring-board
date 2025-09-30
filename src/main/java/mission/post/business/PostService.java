package mission.post.business;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import mission.post.domain.Post;
import mission.post.implement.PostReader;
import mission.post.implement.PostWriter;
import mission.post.dto.CreatePostRequest;
import mission.post.dto.PostCreateResponse;
import mission.post.dto.UpdatePostRequest;
import mission.user.domain.User;
import mission.user.implement.AuthVerifier;
import mission.user.implement.UserReader;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostReader postReader;
    private final PostWriter postWriter;
    private final UserReader userReader;
    private final AuthVerifier authVerifier;

    @Transactional
    public PostCreateResponse create(CreatePostRequest createPostRequest) {
        User author = userReader.getByEmail(createPostRequest.email());
        authVerifier.verifyUserEmailAndPassword(author, createPostRequest.email(), createPostRequest.password());

        Post saved = postWriter.save(
                Post.builder()
                        .title(createPostRequest.title())
                        .content(createPostRequest.content())
                        .author(author)
                        .build()
        );

        return new PostCreateResponse(saved.getId(), author.getEmail(), saved.getTitle(), saved.getContent());
    }

    @Transactional
    public PostCreateResponse update(Long id, UpdatePostRequest updatePostRequest){
        Post post = postReader.getById(id);

        authVerifier.verifyUserEmailAndPassword(post.getAuthor(), updatePostRequest.email(), updatePostRequest.password());

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
        Post post = postReader.getById(id);

        authVerifier.verifyUserEmailAndPassword(post.getAuthor(), email, password);

        postWriter.delete(post);
    }
}
