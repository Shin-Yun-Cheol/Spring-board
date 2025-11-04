package mission.post.business;

import mission.post.business.command.CreatePostCommand;
import mission.post.business.result.PostResult;
import mission.post.domain.Post;
import mission.post.implement.PostReader;
import mission.post.implement.PostWriter;
import mission.user.domain.User;
import mission.user.implement.AuthVerifier;
import mission.user.implement.UserReader;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {

    @Mock UserReader userReader;
    @Mock PostReader postReader;
    @Mock PostWriter postWriter;
    @Mock AuthVerifier authVerifier;
    @InjectMocks PostService postService;

    @Test
    void create_flow_ok() {
        CreatePostCommand cmd = CreatePostCommand.of("a@b.com", "pw", "title", "content");
        User author = new User("a@b.com", "$2a$10$hash", "nick");
        Post saved = new Post(1L, "title", "content", author);

        when(userReader.getByEmail("a@b.com")).thenReturn(author);
        when(postWriter.save(any(Post.class))).thenReturn(saved);

        PostResult result = postService.create(cmd);

        assertEquals(1L, result.articleId());
        assertEquals("a@b.com", result.email());
        verify(userReader).getByEmail("a@b.com");
        verify(postWriter).save(any(Post.class));
    }

    /*@Test
    void update_calls_reader_and_edit() {
        User author = new User("a@b.com", "$2a$10$hash", "nick");
        Post post = spy(new Post(1L, "old", "old", author));
        when(postReader.getById(1L)).thenReturn(post);

        UpdatePostCommand cmd = new UpdatePostCommand(1L, "new", "new", "title", "content");
        PostResult result = postService.update(cmd);

        assertEquals("new", result.title());
        verify(post).edit("new", "new");
    }*/

    @Test
    void delete_verifies_auth_and_deletes() {
        User author = new User("a@b.com", "$2a$10$hash", "nick");
        Post post = new Post(1L, "t", "c", author);
        when(postReader.getById(1L)).thenReturn(post);

        postService.delete(1L, "a@b.com", "pw");

        verify(authVerifier).verifyUserEmailAndPassword(author, "a@b.com", "pw");
        verify(postWriter).delete(post);
    }
}
