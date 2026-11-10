package Repository;

import Domain.Friendship;
import Domain.User;
import Domain.UserCriteria;
import validators.FriendshipValidator;

public class FriendshipFileRepository extends AbstractFileRepo<Long, Friendship> {
    private final Repository<Long, User> userRepo;

    /**
     * @param val      the validator of the friendships
     * @param filePath the file with the friendships
     * @param userRepo the repository where the users of each friendship are searched (by name)
     * @throws Exceptions.RepositoryException if the file is missing or has a corrupt line
     */
    public FriendshipFileRepository(FriendshipValidator val, String filePath, Repository<Long, User> userRepo) {
        super(filePath, val);
        this.userRepo = userRepo;
        loadFromFile();
    }

    @Override
    public Friendship extractEntity(String line) {
        String[] stripedline = line.split(";");
        if (stripedline.length != 5)
            throw new IllegalArgumentException("Se asteptau sa se citeasca din fisier 5 argumente, linie corupta");
        User a = findUser(stripedline[1], stripedline[2]);
        User b = findUser(stripedline[3], stripedline[4]);
        Friendship f = new Friendship(a, b);
        Long id = Long.valueOf(stripedline[0]);
        f.setIdentitykey(id);
        return f;
    }

    /**
     * Finds the real user (the one with an ID) that has the given name.
     *
     * @param firstName the first name written in the file
     * @param lastName  the last name written in the file
     * @return the first user with that name
     * @throws IllegalArgumentException if no such user exists
     */
    private User findUser(String firstName, String lastName) {
        return userRepo.findAll(UserCriteria.byFirstName(firstName).and(UserCriteria.byLastName(lastName)))
                .stream()
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "Nu exista utilizatorul " + firstName + " " + lastName));
    }

    @Override
    public String createEntityToString(Friendship entity) {
        Long id = entity.getIdentitykey();
        String c = id.toString();
        String line = c + ';' + entity.getA().getFirstName() + ';' + entity.getA().getLastName() + ';' + entity.getB().getFirstName() + ';' + entity.getB().getLastName();
        return line;
    }
}