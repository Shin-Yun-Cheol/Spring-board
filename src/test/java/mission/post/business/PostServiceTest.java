package mission.post.business;

import mission.post.business.command.CreatePostCommand;
import mission.post.business.command.UpdatePostCommand;
import mission.post.business.result.PostResult;
import mission.post.domain.Post;
import mission.post.implement.PostReader;
import mission.post.implement.PostWriter;
import mission.user.domain.User;
import mission.user.implement.AuthVerifier;
import mission.user.implement.UserReader;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class) // Mockito 확장
class PostServiceTest {
/*
    @InjectMocks
    private PostService postService; // 테스트 대상

    @Mock private PostReader postReader;   // 게시글 조회 담당
    @Mock private PostWriter postWriter;   // 게시글 저장/삭제 담당
    @Mock private UserReader userReader;   // 유저 조회 담당 (이메일로 조회)
    @Mock private AuthVerifier authVerifier; // 이메일/비밀번호 검증 담당

    @Captor
    private ArgumentCaptor<Post> postCaptor; // save/delete 호출 시 넘긴 Post를 잡아 검증

    @Test
    @DisplayName("게시글 생성 성공 - 인증 성공 시 Post 저장")
    void create_success() {
        // given: 작성자 정보 및 인증 성공 시나리오
        CreatePostCommand cmd = new CreatePostCommand(
                "author@example.com",
                "plain_password",
                "hello",
                "content"
        );
        User author = User.builder()
                .email("author@example.com")
                .passwordHash("$2a$10$somebcrypt") // 실제 검증은 AuthVerifier가 함
                .username("author")
                .build();

        // 이메일로 유저 조회 성공
        given(userReader.getByEmail(cmd.email())).willReturn(author);
        // 인증 검증은 void 메서드: 예외만 던질 수 있음. 성공 시 아무 일도 일어나지 않음.
        // doNothing().when(authVerifier).verifyUserEmailAndPassword(...); // 생략 가능
        // 저장 시, 저장된 Post를 그대로 반환
        Post saved = Post.from(cmd, author);
        given(postWriter.save(any(Post.class))).willReturn(saved);

        // when
        PostResult result = postService.create(cmd);

        // then
        assertThat(result).isNotNull();
        assertThat(result.articleId()).isEqualTo(1L);
        assertThat(result.email()).isEqualTo(author.getEmail());
        assertThat(result.title()).isEqualTo("hello");
        assertThat(result.content()).isEqualTo("content");

        // 실제 저장에 넘어간 Post 내용 검증 (제목/내용/작성자)
        verify(postWriter).save(postCaptor.capture());
        Post toSave = postCaptor.getValue();
        assertThat(toSave.getTitle()).isEqualTo("hello");
        assertThat(toSave.getContent()).isEqualTo("content");
        assertThat(toSave.getAuthor()).isSameAs(author);

        // 유저조회/인증 호출 여부 검증
        verify(userReader).getByEmail("author@example.com");
        verify(authVerifier).verifyUserEmailAndPassword(author, "author@example.com", "plain_password");
    }

    @Test
    @DisplayName("게시글 생성 실패 - 이메일로 유저 조회 실패 시 예외")
    void create_fail_user_not_found() {
        // given
        CreatePostCommand cmd = new CreatePostCommand(
                "noone@example.com", "pw", "t", "c"
        );
        // 이메일 조회 실패 -> UserReader가 예외를 던지도록 스텁
        given(userReader.getByEmail(cmd.email()))
                .willThrow(new IllegalArgumentException("email not found"));

        // when & then
        assertThrows(IllegalArgumentException.class, () -> postService.create(cmd));

        // 저장은 시도되지 않음
        verify(postWriter, never()).save(any(Post.class));
        verify(authVerifier, never()).verifyUserEmailAndPassword(any(), any(), any());
    }

    @Test
    @DisplayName("게시글 수정 성공 - 인증 성공 시 제목/내용 갱신 후 저장")
    void update_success() {
        // given: 기존 게시글과 작성자
        UpdatePostCommand cmd = new UpdatePostCommand(
                10L, "author@example.com", "pw", "new title", "new content"
        );
        User author = User.builder()
                .email("author@example.com")
                .passwordHash("$2a$10$somebcrypt")
                .username("author")
                .build();
        Post existing = Post.builder()
                .id(10L)
                .title("old")
                .content("old")
                .author(author)
                .build();

        // 게시글 조회 성공
        given(postReader.getById(10L)).willReturn(existing);
        // (더티 체킹 전략이므로 save 스텁/검증은 불필요)

        // when
        PostResult result = postService.update(cmd);

        // then
        assertThat(result.title()).isEqualTo("new title");
        assertThat(result.content()).isEqualTo("new content");

        // 인증이 수행되었는지만 검증
        verify(authVerifier).verifyUserEmailAndPassword(author, "author@example.com", "pw");

        // 더티 체킹 전략: save 등 writer 상호작용이 없어야 정상
        verifyNoInteractions(postWriter);
    }

    @Test
    @DisplayName("게시글 수정 실패 - 인증 실패(비밀번호 불일치 등) 시 예외")
    void update_fail_auth() {
        // given
        UpdatePostCommand cmd = new UpdatePostCommand(
                10L, "author@example.com", "wrong", "new title", "new content"
        );
        User author = User.builder()
                .email("author@example.com")
                .passwordHash("$2a$10$somebcrypt")
                .username("author")
                .build();
        Post existing = Post.builder()
                .id(10L)
                .title("old")
                .content("old")
                .author(author)
                .build();

        given(postReader.getById(10L)).willReturn(existing);
        // 인증 실패 시 IllegalArgumentException 발생
        doThrow(new IllegalArgumentException("email or password does not match"))
                .when(authVerifier).verifyUserEmailAndPassword(author, "author@example.com", "wrong");

        // when & then
        assertThrows(IllegalArgumentException.class, () -> postService.update(cmd));

        // 저장(save)은 호출되지 않아야 함
        verify(postWriter, never()).save(any(Post.class));
    }



    @Test
    @DisplayName("게시글 삭제 성공 - 인증 성공 시 삭제")
    void delete_success() {
        // given
        Long id = 55L;
        String email = "author@example.com";
        String pw = "pw";
        User author = User.builder()
                .email(email)
                .passwordHash("$2a$10$somebcrypt")
                .username("author")
                .build();
        Post post = Post.builder()
                .id(id)
                .title("t")
                .content("c")
                .author(author)
                .build();

        given(postReader.getById(id)).willReturn(post);

        // when
        postService.delete(id, email, pw);

        // then: 인증이 호출되고, 삭제가 수행되어야 함
        verify(authVerifier).verifyUserEmailAndPassword(author, email, pw);
        verify(postWriter).delete(post);
    }

    @Test
    @DisplayName("게시글 삭제 실패 - 인증 실패 시 삭제 호출 없음")
    void delete_fail_auth() {
        // given
        Long id = 55L;
        String email = "author@example.com";
        String pw = "wrong";
        User author = User.builder()
                .email(email)
                .passwordHash("$2a$10$somebcrypt")
                .username("author")
                .build();
        Post post = Post.builder()
                .id(id)
                .title("t")
                .content("c")
                .author(author)
                .build();

        given(postReader.getById(id)).willReturn(post);
        doThrow(new IllegalArgumentException("email or password does not match"))
                .when(authVerifier).verifyUserEmailAndPassword(author, email, pw);

        // when & then
        assertThrows(IllegalArgumentException.class, () -> postService.delete(id, email, pw));

        // 인증 실패했으니 실제 삭제는 호출되면 안 됨
        verify(postWriter, never()).delete(any(Post.class));
    }*/
}
