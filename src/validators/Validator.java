package Validation;

@FunctionalInterface
public interface Validator<T> {
    void validate(T entity);

    default Validator<T> andThen(Validator<T> other) {
        return entity -> {
            validate(entity);
            other.validate(entity);
        };
    }
}
