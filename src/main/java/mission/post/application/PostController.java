package mission.post.application;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mission.post.business.PostService;
import mission.post.dto.CreatePostRequest;
import mission.post.dto.PostCreateResponse;
import mission.post.dto.UpdatePostRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @PostMapping
    public PostCreateResponse create(@RequestBody @Valid CreatePostRequest createPostRequest) {
        return postService.create(createPostRequest);
    }

    @PutMapping("/{id}")
    public PostCreateResponse update(@PathVariable Long id, @RequestBody @Valid UpdatePostRequest updatePostRequest) {
        return postService.update(id, updatePostRequest);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id, @RequestBody Map<String, String> body) {
        String email = body.get("email");
        String password = body.get("password");
        postService.delete(id, email, password);
    }
}
