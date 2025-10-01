package mission.user.business.command;

public record SignUpCommand(
        String email,
        String password,
        String username
) {
    public SignUpCommand {
        if(email==null || email.isBlank()) throw new IllegalArgumentException("email is null or empty");
        if(password==null || password.isBlank()) throw new IllegalArgumentException("password is null or empty");
        if(username==null || username.isBlank()) throw new IllegalArgumentException("username is null or empty");
    }
}
