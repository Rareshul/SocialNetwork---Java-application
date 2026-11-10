package validators;

import Domain.User;
import Exceptions.ValidationException;

public class UserValidator implements Validation.Validator<User> {
    @Override
    public void validate(User user) {
        String errors = "";
        if (user.getFirstName() == null || user.getFirstName().trim().isEmpty()) {
            errors += "First name cannot be empty!\n";
        }
        if (user.getLastName() == null || user.getLastName().trim().isEmpty()) {
            errors += "Last name cannot be empty!\n";
        }
        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
    }
}
