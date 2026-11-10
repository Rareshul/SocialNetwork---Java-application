package main;

import Domain.Friendship;
import Domain.User;
import Exceptions.ValidationException;
import Repository.Repository;
import Repository.RepositoryFactory;
import Service.FriendshipService;
import Service.NetworkService;
import Service.UserService;
import Validation.Validator;
import validators.FriendshipValidator;
import validators.UserValidator;

import java.io.IOException;
import java.util.Properties;

public class Main {
    public static void main(String[] args) {
        Properties porp = loadProperties();
        RepositoryFactory repofac = new RepositoryFactory(porp);
        Repository<Long,User> userRepo = repofac.createUtilizatorRepository(new UserValidator());
        Repository<Long,Friendship> friendshipRepo = repofac.createFriendshipRepository(new FriendshipValidator(), userRepo);

        // new rule written as a lambda and chained to the existing validator
        Validator<User> userValidator = new UserValidator().andThen(user -> {
            if (user.getFirstName().length() > 30 || user.getLastName().length() > 30) {
                throw new ValidationException("Names must have at most 30 characters!");
            }
        });

        UserService userService = new UserService(userRepo, friendshipRepo, userValidator);
        FriendshipService friendshipService = new FriendshipService(friendshipRepo, userRepo, new FriendshipValidator());
        NetworkService networkService = new NetworkService(userRepo, friendshipRepo);

        Ui ui = new Ui(userService, friendshipService, networkService);
        ui.run();
    }
    public static Properties loadProperties()
    {
        final var props = new Properties();
        try(final var io = Main.class.getResourceAsStream("/config.properties")){
            if (io == null) throw new RuntimeException("Could not find config properties");
            props.load(io);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return props;
    }
}