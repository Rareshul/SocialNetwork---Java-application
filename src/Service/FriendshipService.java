package Service;

import Domain.Friendship;
import Domain.User;
import Exceptions.ServiceException;
import Repository.Repository;
import Validation.Validator;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class FriendshipService implements Service<Friendship> {
    private Repository<Long, Friendship> repo;
    private Repository<Long, User> userRepo;
    private Validator<Friendship> validator;

    public FriendshipService(Repository<Long, Friendship> repo, Repository<Long, User> userRepo, Validator<Friendship> validator) {
        this.repo = repo;
        this.userRepo = userRepo;
        this.validator = validator;
    }

    @Override
    public Long generateId() {
        return repo.stream()
                .map(Friendship::getIdentitykey)
                .max(Long::compare)
                .orElse(0L) + 1;
    }

    public Friendship add(Long userId1, Long userId2) {
        User a = findUser(userId1);
        User b = findUser(userId2);
        Friendship friendship = new Friendship(a, b);
        validator.validate(friendship);
        boolean alreadyFriends = repo.stream().anyMatch(existing -> connects(existing, a, b));
        if (alreadyFriends) {
            throw new ServiceException("These users are already friends!");
        }
        friendship.setIdentitykey(generateId());
        repo.add(friendship);
        return friendship;
    }

    @Override
    public Friendship remove(long id) {
        Friendship friendship = Optional.ofNullable(repo.findOne(id))
                .orElseThrow(() -> new ServiceException("There is no friendship with ID " + id + "!"));
        repo.remove(friendship);
        return friendship;
    }

    /**
     * Gives the friends of a user.
     *
     * @param userId the ID of the user
     * @return the list of friends (empty if the user has none)
     * @throws ServiceException if there is no user with that ID
     */
    public List<User> friendsOf(Long userId) {
        findUser(userId);
        return repo.stream()
                .filter(friendship -> friendship.involves(userId))
                .map(friendship -> friendship.getOther(userId))
                .collect(Collectors.toList());
    }

    public Iterable<Friendship> getAll() {
        return repo.getAll();
    }

    /**
     * @param id the ID of the user
     * @return the user
     * @throws ServiceException if there is no user with that ID
     */
    private User findUser(Long id) {
        return Optional.ofNullable(userRepo.findOne(id))
                .orElseThrow(() -> new ServiceException("There is no user with ID " + id + "!"));
    }

    /**
     * @return true if the friendship is between a and b, in any order
     */
    private boolean connects(Friendship friendship, User a, User b) {
        boolean sameOrder = friendship.getA().equals(a) && friendship.getB().equals(b);
        boolean reverseOrder = friendship.getA().equals(b) && friendship.getB().equals(a);
        return sameOrder || reverseOrder;
    }
}
