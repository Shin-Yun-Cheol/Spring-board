package mission.post.business;

import mission.post.dto.CreatePostRequest;
import mission.post.dto.PostCreateResponse;
import mission.post.dto.UpdatePostRequest;

public interface PostService {
    PostCreateResponse create(CreatePostRequest request);
    PostCreateResponse update(Long id, UpdatePostRequest request);
    void delete(Long id, String email, String password);
}
