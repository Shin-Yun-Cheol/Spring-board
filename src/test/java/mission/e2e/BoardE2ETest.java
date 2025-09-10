package mission.e2e;

import mission.comment.Comment;
import mission.comment.CommentRepository;
import mission.post.Post;
import mission.post.PostRepository;
import mission.user.User;
import mission.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * CommentController 매핑에 맞춘 통합 테스트:
 *  - 댓글 생성: POST /api/posts/{postId}/comments?userId={작성자ID}   (201 Created)
 *  - 댓글 수정: PUT  /api/comments/{id}?userId={작성자ID}
 *  - 댓글 삭제: DELETE /api/comments/{id}?userId={작성자ID}          (204 No Content)

 * 선행 조건:
 *  - 회원가입:   POST /api/users/signup   {email,password,username}
 *  - 게시글 생성: POST /api/posts         {email,password,title,content}

 * 주의: 현재 프로젝트는 댓글 인증을 userId(쿼리파라미터)로 처리하고,
 *      댓글 요청 바디에는 content만 담는 구조를 가정한다.
 */
@SpringBootTest
@AutoConfigureMockMvc
class CommentFlowE2ETest {

    @Autowired MockMvc mockMvc;
    @Autowired UserRepository userRepository;
    @Autowired PostRepository postRepository;
    @Autowired CommentRepository commentRepository;

    @BeforeEach
    void clean() {
        commentRepository.deleteAll();
        postRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("회원가입 → 게시글 생성 → 댓글 생성(201) → 댓글 수정 → 댓글 삭제(204)")
    void commentCrudFlow() throws Exception {
        // ---------- 1) 회원가입 ----------
        String email = "user1@ssu.ac.kr";
        String password = "pw1234";
        String username = "상혁";

        var signupJson = """
            {
              "email": "%s",
              "password": "%s",
              "username": "%s"
            }
            """.formatted(email, password, username);

        mockMvc.perform(post("/api/users/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(signupJson))
                .andExpect(status().is2xxSuccessful());

        User user = userRepository.findByEmail(email).orElseThrow();
        Long userId = user.getId();

        // ---------- 2) 게시글 생성 ----------
        var postCreateJson = """
            {
              "email": "%s",
              "password": "%s",
              "title": "첫 글",
              "content": "본문입니다."
            }
            """.formatted(email, password);

        mockMvc.perform(post("/api/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(postCreateJson))
                .andExpect(status().is2xxSuccessful());

        Post post = postRepository.findAll().stream().findFirst().orElseThrow();
        Long postId = post.getId();

        // ---------- 3) 댓글 생성 (회원만) ----------
        var commentCreateJson = """
            { "content": "첫 댓글입니다" }
            """;

        mockMvc.perform(post("/api/posts/{postId}/comments", postId)
                        .queryParam("userId", String.valueOf(userId))  // ✅ Controller 시그니처에 맞춤
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(commentCreateJson))
                .andExpect(status().isCreated());                      // ✅ 201 기대

        Comment created = commentRepository.findAll().stream().findFirst().orElseThrow();
        Long commentId = created.getId();
        assertThat(created.getContent()).isEqualTo("첫 댓글입니다");
        assertThat(created.getPost().getId()).isEqualTo(postId);
        assertThat(created.getAuthor().getId()).isEqualTo(userId);

        // ---------- 4) 댓글 수정 (작성자 본인) ----------
        var commentUpdateJson = """
            { "content": "수정된 댓글 내용" }
            """;

        mockMvc.perform(put("/api/comments/{id}", commentId)
                        .queryParam("userId", String.valueOf(userId))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(commentUpdateJson))
                .andExpect(status().is2xxSuccessful());

        Comment afterUpdate = commentRepository.findById(commentId).orElseThrow();
        assertThat(afterUpdate.getContent()).isEqualTo("수정된 댓글 내용");

        // ---------- 5) 댓글 삭제 (작성자 본인) ----------
        mockMvc.perform(delete("/api/comments/{id}", commentId)
                        .queryParam("userId", String.valueOf(userId)))
                .andExpect(status().isNoContent());

        assertThat(commentRepository.findById(commentId)).isEmpty();
    }
}
