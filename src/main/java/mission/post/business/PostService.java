package mission.post.business;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import mission.post.business.command.CreatePostCommand;
import mission.post.business.command.UpdatePostCommand;
import mission.post.business.result.PostResult;
import mission.post.domain.Post;
import mission.post.implement.PostReader;
import mission.post.implement.PostWriter;
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
    public PostResult create(CreatePostCommand createPostCommand) {
        User author = userReader.getByEmail(createPostCommand.email());
        authVerifier.verifyUserEmailAndPassword(author, createPostCommand.email(), createPostCommand.password());

        Post saved = postWriter.save(
                Post.builder()
                        .title(createPostCommand.title())
                        .content(createPostCommand.content())
                        .author(author)
                        .build()
        );

        return new PostResult(saved.getId(), saved.getAuthor().getEmail(), saved.getTitle(), saved.getContent());
    }

    @Transactional
    public PostResult update(UpdatePostCommand updatePostCommand) {
        Post post = postReader.getById(updatePostCommand.id());
        authVerifier.verifyUserEmailAndPassword(post.getAuthor(), updatePostCommand.email(), updatePostCommand.password());
        post.edit(updatePostCommand.title(), updatePostCommand.content());

        return new PostResult(
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
