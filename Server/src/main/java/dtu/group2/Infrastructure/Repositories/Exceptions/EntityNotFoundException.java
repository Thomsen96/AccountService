package dtu.group2.Infrastructure.Repositories.Exceptions;

public class EntityNotFoundException extends Throwable {
    public EntityNotFoundException(String message) {
        super(message);
    }
}
