package mission.post;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import mission.comment.Comment;
import mission.user.User;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "post")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String title;

    @Lob
    @NotBlank
    @Column(nullable = false)
    private String content;

    // 글 작성자
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "author")
    private User author;

    // 이 글의 댓글들 ( 글 삭제 시 댓글도 함꼐 삭제)
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Comment> comments = new ArrayList<>();

    public void edit(String title, String content){
        this.title = title;
        this.content = content;
    }
}
