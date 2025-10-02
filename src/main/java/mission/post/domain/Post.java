package mission.post.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import mission.post.business.command.CreatePostCommand;
import mission.user.domain.User;

@Entity
@Table(name = "post")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
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

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "author")
    private User author;

    @Builder
    public Post(String title, String content, User author) {
        this.title = title;
        this.content = content;
        this.author = author;
    }

    public static Post of(CreatePostCommand command, User author) {
        return Post.builder()
                .title(command.title())
                .content(command.content())
                .author(author)
                .build();
    }

    public void edit(String title, String content){
        this.title = title;
        this.content = content;
    }
}
