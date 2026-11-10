package main;

import Domain.Community;
import Domain.User;
import Domain.UserCriteria;
import Exceptions.ServiceException;
import Exceptions.ValidationException;
import Service.FriendshipService;
import Service.NetworkService;
import Service.UserService;

import java.util.Scanner;

public class Ui {
    private UserService userService;
    private FriendshipService friendshipService;
    private NetworkService networkService;
    private Scanner scanner;

    public Ui(UserService userService, FriendshipService friendshipService, NetworkService networkService) {
        this.userService = userService;
        this.friendshipService = friendshipService;
        this.networkService = networkService;
        this.scanner = new Scanner(System.in);
    }

    public void printMenu() {
        System.out.println();
        System.out.println("1. Add user");
        System.out.println("2. Remove user");
        System.out.println("3. Add friendship");
        System.out.println("4. Remove friendship");
        System.out.println("5. Number of communities");
        System.out.println("6. Most sociable community");
        System.out.println("7. Show users");
        System.out.println("8. Show friendships");
        System.out.println("9. Search users by last name");
        System.out.println("10. Show friends of a user");
        System.out.println("0. Exit");
        System.out.print("Choose an option: ");
    }

    public void run() {
        boolean running = true;
        while (running) {
            printMenu();
            String option = scanner.nextLine().trim();
            switch (option) {
                case "1":
                    addUser();
                    break;
                case "2":
                    removeUser();
                    break;
                case "3":
                    addFriendship();
                    break;
                case "4":
                    removeFriendship();
                    break;
                case "5":
                    showNumberOfCommunities();
                    break;
                case "6":
                    showMostSociable();
                    break;
                case "7":
                    showUsers();
                    break;
                case "8":
                    showFriendships();
                    break;
                case "9":
                    searchUsersByLastName();
                    break;
                case "10":
                    showFriendsOfUser();
                    break;
                case "0":
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option!");
            }
        }
    }

    public void addUser() {
        System.out.print("Please enter user's first name: ");
        String firstName = scanner.nextLine();
        System.out.print("Please enter user's last name: ");
        String lastName = scanner.nextLine();
        try {
            userService.add(firstName, lastName);
            System.out.println("User was added successfully!");
        } catch (ValidationException | ServiceException e) {
            System.out.println(e.getMessage());
        }
    }

    public void removeUser() {
        System.out.print("Please enter user's ID: ");
        try {
            Long id = Long.parseLong(scanner.nextLine().trim());
            userService.remove(id);
            System.out.println("User was deleted successfully!");
        } catch (NumberFormatException e) {
            System.out.println("The ID must be a number!");
        } catch (ValidationException | ServiceException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addFriendship() {
        try {
            System.out.print("Please enter first user's ID: ");
            Long id1 = Long.parseLong(scanner.nextLine().trim());
            System.out.print("Please enter second user's ID: ");
            Long id2 = Long.parseLong(scanner.nextLine().trim());
            friendshipService.add(id1, id2);
            System.out.println("Friendship was added successfully!");
        } catch (NumberFormatException e) {
            System.out.println("The ID must be a number!");
        } catch (ValidationException | ServiceException e) {
            System.out.println(e.getMessage());
        }
    }

    public void removeFriendship() {
        System.out.print("Please enter friendship's ID: ");
        try {
            Long id = Long.parseLong(scanner.nextLine().trim());
            friendshipService.remove(id);
            System.out.println("Friendship was deleted successfully!");
        } catch (NumberFormatException e) {
            System.out.println("The ID must be a number!");
        } catch (ValidationException | ServiceException e) {
            System.out.println(e.getMessage());
        }
    }

    public void showNumberOfCommunities() {
        System.out.println("Number of communities: " + networkService.numberOfCommunities());
    }

    public void showMostSociable() {
        try {
            Community community = networkService.mostSociableCommunity();
            System.out.println("Longest path: " + community.longestPath() + " edges");
            System.out.println("Members:");
            community.getMembers().stream()
                    .map(this::describe)
                    .forEach(System.out::println);
        } catch (ServiceException e) {
            System.out.println(e.getMessage());
        }
    }

    public void showUsers() {
        userService.getAll().forEach(user -> System.out.println(describe(user)));
    }

    public void showFriendships() {
        friendshipService.getAll().forEach(System.out::println);
    }

    public void searchUsersByLastName() {
        System.out.print("Please enter the last name: ");
        String lastName = scanner.nextLine().trim();
        userService.findAll(UserCriteria.byLastName(lastName)).stream()
                .map(this::describe)
                .forEach(System.out::println);
    }

    public void showFriendsOfUser() {
        System.out.print("Please enter user's ID: ");
        try {
            Long id = Long.parseLong(scanner.nextLine().trim());
            friendshipService.friendsOf(id).stream()
                    .map(this::describe)
                    .forEach(System.out::println);
        } catch (NumberFormatException e) {
            System.out.println("The ID must be a number!");
        } catch (ServiceException e) {
            System.out.println(e.getMessage());
        }
    }

    private String describe(User user) {
        return user.getIdentitykey() + " " + user.getFirstName() + " " + user.getLastName();
    }
}
