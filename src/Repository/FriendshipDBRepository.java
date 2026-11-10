package Repository;
import Domain.Friendship;
import validators.FriendshipValidator;

public class FriendshipDBRepository implements Repository<Long, Friendship> {
    private final FriendshipValidator validator;
    private final String dbUrl;
    private final String dbUser;
    private final String dbPassword;

    public FriendshipDBRepository(FriendshipValidator validator, String dbUrl, String dbUser, String dbPassword) {
        this.validator = validator;
        this.dbUrl = dbUrl;
        this.dbUser = dbUser;
        this.dbPassword = dbPassword;
    }

    @Override
    public void add(Friendship a) {
        throw new UnsupportedOperationException("Database persistence is not implemented yet");
    }

    @Override
    public void remove(Friendship a) {
        throw new UnsupportedOperationException("Database persistence is not implemented yet");
    }

    @Override
    public Friendship findOne(Long aLong) {
        throw new UnsupportedOperationException("Database persistence is not implemented yet");
    }

    @Override
    public Iterable<Friendship> getAll() {
        throw new UnsupportedOperationException("Database persistence is not implemented yet");
    }
}
