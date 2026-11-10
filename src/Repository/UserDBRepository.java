package Repository;

import Domain.User;
import validators.UserValidator;

public class UserDBRepository implements Repository<Long, User> {
    private final UserValidator validator;
    private final String dbUrl;
    private final String dbUser;
    private final String dbPassword;

    public UserDBRepository(UserValidator validator, String dbUrl, String dbUser, String dbPassword) {
        this.validator = validator;
        this.dbUrl = dbUrl;
        this.dbUser = dbUser;
        this.dbPassword = dbPassword;
    }

    @Override
    public void add(User a) {
        throw new UnsupportedOperationException("Database persistence is not implemented yet");
    }

    @Override
    public void remove(User a) {
        throw new UnsupportedOperationException("Database persistence is not implemented yet");
    }

    @Override
    public User findOne(Long aLong) {
        throw new UnsupportedOperationException("Database persistence is not implemented yet");
    }

    @Override
    public Iterable<User> getAll() {
        throw new UnsupportedOperationException("Database persistence is not implemented yet");
    }
}
