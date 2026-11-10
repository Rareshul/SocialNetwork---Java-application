package Repository;
import Domain.User;
import validators.FriendshipValidator;
import validators.UserValidator;
import Domain.Friendship;

import java.util.Map;
import java.util.Properties;
import java.util.function.Supplier;

public class RepositoryFactory {
    private final Properties prop;

    public RepositoryFactory(Properties prop) {
        this.prop = prop;
    }

    public Repository<Long, User> createUtilizatorRepository(UserValidator validator) {
        Map<RepoTypes, Supplier<Repository<Long, User>>> suppliers = Map.of(
                RepoTypes.MEMORY, InMemoryRepo::new,
                RepoTypes.FILE, () -> new UtilizatorFileRepository(validator, required("file.utilizatori")),
                RepoTypes.DATABASE, () -> new UserDBRepository(
                        validator,
                        required("db.url"),
                        required("db.user"),
                        required("db.password"))
        );
        return suppliers.get(repoType()).get();
    }
    public Repository<Long, Friendship> createFriendshipRepository(FriendshipValidator validator, Repository<Long, User> userRepo) {
        Map<RepoTypes, Supplier<Repository<Long, Friendship>>> suppliers = Map.of(
                RepoTypes.MEMORY, InMemoryRepo::new,
                RepoTypes.FILE, () -> new FriendshipFileRepository(validator, required("file.prietenii"), userRepo),
                RepoTypes.DATABASE, () -> new FriendshipDBRepository(
                        validator,
                        required("db.url"),
                        required("db.user"),
                        required("db.password"))
        );
        return suppliers.get(repoType()).get();
    }

    public RepoTypes repoType() {
        final String type = required("repository.type");
        try {
            return RepoTypes.valueOf(type.trim());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("unknown repository.type: " + type, e);
        }
    }

    public String required(String key) {
        final String value = prop.getProperty(key);
        if (value == null) {
            throw new IllegalArgumentException("missing property: " + key);
        }
        return value;
    }
}