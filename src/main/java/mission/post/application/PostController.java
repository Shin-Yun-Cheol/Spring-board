package mission.post.application;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mission.post.business.PostService;
import mission.post.application.dto.CreatePostRequest;
import mission.post.application.dto.PostCreateResponse;
import mission.post.application.dto.UpdatePostRequest;
import mission.post.business.result.PostResult;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @PostMapping
    public PostCreateResponse create(@RequestBody @Valid CreatePostRequest req) {
        PostResult result = postService.create(req.toCommand());
        return PostCreateResponse.of(result);
    }

    @PutMapping("/{id}")
    public PostCreateResponse update(@PathVariable Long id, @RequestBody @Valid UpdatePostRequest req) {
        PostResult result = postService.update(req.toCommand(id));
        return PostCreateResponse.of(result);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id, @RequestBody Map<String, String> body) {
        String email = body.get("email");
        String password = body.get("password");
        postService.delete(id, email, password);
    }
}
