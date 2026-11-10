package Domain;

import java.util.List;
import java.util.function.Predicate;

/**
 * Search criteria for users. Every method returns a {@link Predicate} that can be
 * given to {@code Repository.findAll} and combined with and, or, negate, Predicate.not.
 */
public class UserCriteria {

    /**
     * @param lastName the last name to look for (case is ignored)
     * @return a predicate that accepts the users with that last name
     */
    public static Predicate<User> byLastName(String lastName) {
        return user -> user.getLastName().equalsIgnoreCase(lastName);
    }

    /**
     * @param firstName the first name to look for (case is ignored)
     * @return a predicate that accepts the users with that first name
     */
    public static Predicate<User> byFirstName(String firstName) {
        return user -> user.getFirstName().equalsIgnoreCase(firstName);
    }

    /**
     * @param prefix the beginning of the first name (case is ignored)
     * @return a predicate that accepts the users whose first name starts with the prefix
     */
    public static Predicate<User> firstNameStartsWith(String prefix) {
        return user -> user.getFirstName().toLowerCase().startsWith(prefix.toLowerCase());
    }

    /**
     * @param friendships all the friendships of the network
     * @return a predicate that accepts the users that appear in none of the friendships
     */
    public static Predicate<User> withoutFriends(List<Friendship> friendships) {
        return user -> friendships.stream().noneMatch(f -> f.involves(user.getIdentitykey()));
    }
}
