package validators;

import Domain.Friendship;
import Exceptions.ValidationException;

public class FriendshipValidator implements Validation.Validator<Friendship> {
    @Override
    public void validate(Friendship friendship) {
        if (friendship.getA() == null || friendship.getB() == null) {
            throw new ValidationException("A friendship needs two users!");
        }
        if (friendship.getA().equals(friendship.getB())) {
            throw new ValidationException("A user cannot be friend with himself!");
        }
    }
}
