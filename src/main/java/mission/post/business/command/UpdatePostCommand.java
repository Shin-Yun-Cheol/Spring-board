package mission.post.business.command;

public record UpdatePostCommand(
        Long id,
        String email,
        String password,
        String title,
        String content
) {
    public UpdatePostCommand{
        if(id==null) throw new IllegalArgumentException("id is null");
        if(title==null || title.isBlank()) throw new IllegalArgumentException("title is null or empty");
        if(content==null || content.isBlank()) throw new IllegalArgumentException("content is null or empty");
    }
}
