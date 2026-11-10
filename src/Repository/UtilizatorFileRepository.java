package Repository;
import validators.UserValidator;

import Domain.User;

public class UtilizatorFileRepository extends AbstractFileRepo<Long, User> {

    public UtilizatorFileRepository(Validation.Validator<User> val, String filePath) {
        super(filePath, val);
        loadFromFile();
    }
    @Override
    public User extractEntity(String line)
    {
        String[] stripedline = line.split(";");
        User user = new User(stripedline[1], stripedline[2]);
        Long id = Long.valueOf(stripedline[0]);
        user.setIdentitykey(id);
        return user;
    }
    @Override
    public String createEntityToString(User user) {
        return user.getIdentitykey() + ";" + user.getFirstName() + ";" + user.getLastName();
    }
}
