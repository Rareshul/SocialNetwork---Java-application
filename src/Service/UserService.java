package Service;

import Domain.Friendship;
import Domain.User;
import Exceptions.ServiceException;
import Repository.Repository;
import Validation.Validator;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class UserService implements Service<User> {
    private Repository<Long, User> repo;
    private Repository<Long, Friendship> friendshipRepo;
    private Validator<User> validator;

    public UserService(Repository<Long, User> repo, Repository<Long, Friendship> friendshipRepo, Validator<User> validator) {
        this.repo = repo;
        this.friendshipRepo = friendshipRepo;
        this.validator = validator;
    }

    @Override
    public Long generateId() {
        return repo.stream()
                .map(User::getIdentitykey)
                .max(Long::compare)
                .orElse(0L) + 1;
    }

    public User add(String firstName, String lastName) {
        User user = new User(firstName, lastName);
        validator.validate(user);
        user.setIdentitykey(generateId());
        repo.add(user);
        return user;
    }


    public Optional<User> findById(Long id) {
        return Optional.ofNullable(repo.findOne(id));
    }

    public List<User> findAll(Predicate<User> filter) {
        return repo.findAll(filter);
    }

    @Override
    public User remove(long id) {
        User user = findById(id)
                .orElseThrow(() -> new ServiceException("There is no user with ID " + id + "!"));
        friendshipRepo.findAll(friendship -> friendship.involves(id))
                .forEach(friendshipRepo::remove);
        repo.remove(user);
        return user;
    }

    public Iterable<User> getAll() {
        return repo.getAll();
    }
}
