package dtu.group2.Infrastructure.Repositories.Exceptions;

public class ArgumentNullException extends Throwable {
    public ArgumentNullException(String argument_id_cannot_be_null) {
        super(argument_id_cannot_be_null);
    }
}
