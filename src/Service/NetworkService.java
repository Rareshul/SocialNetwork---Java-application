package Service;

import Domain.Community;
import Domain.Friendship;
import Domain.User;
import Exceptions.ServiceException;
import Repository.Repository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class NetworkService {
    private Repository<Long, User> userRepo;
    private Repository<Long, Friendship> friendshipRepo;

    public NetworkService(Repository<Long, User> userRepo, Repository<Long, Friendship> friendshipRepo) {
        this.userRepo = userRepo;
        this.friendshipRepo = friendshipRepo;
    }

    /**
     * @return for every user, the list of his friends
     */
    private Map<User, List<User>> buildAdjacency() {
        List<Friendship> friendships = friendshipRepo.stream().collect(Collectors.toList());
        return userRepo.stream()
                .collect(Collectors.toMap(Function.identity(), user -> neighborsOf(user, friendships)));
    }

    private List<User> neighborsOf(User user, List<Friendship> friendships) {
        return friendships.stream()
                .filter(friendship -> friendship.involves(user.getIdentitykey()))
                .map(friendship -> friendship.getOther(user.getIdentitykey()))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * Finds everybody that can be reached from the users in {@code found}.
     * At every step the friends of all the known users are added; when nobody new appears, the search is over.
     *
     * @param found     the users known so far
     * @param adjacency the friends of every user
     * @return the set of users connected to the ones in {@code found}
     */
    private Set<User> explore(Set<User> found, Map<User, List<User>> adjacency) {
        Set<User> expanded = found.stream()
                .flatMap(user -> adjacency.get(user).stream())
                .collect(Collectors.toCollection(HashSet::new));
        expanded.addAll(found);
        return expanded.size() == found.size() ? found : explore(expanded, adjacency);
    }

    private Community toCommunity(Set<User> members, Map<User, List<User>> adjacency) {
        return new Community(members.stream()
                .collect(Collectors.toMap(Function.identity(), adjacency::get)));
    }

    /**
     * @return the connected components of the network, one Community for each
     */
    public List<Community> getCommunities() {
        Map<User, List<User>> adjacency = buildAdjacency();
        return adjacency.keySet().stream()
                .map(user -> explore(Set.of(user), adjacency))
                .distinct()
                .map(members -> toCommunity(members, adjacency))
                .collect(Collectors.toList());
    }

    public int numberOfCommunities() {
        return getCommunities().size();
    }

    /**
     * @return the community that contains the longest path
     * @throws ServiceException if there are no users in the network
     */
    public Community mostSociableCommunity() {
        return getCommunities().stream()
                .max(Comparator.comparingInt(Community::longestPath))
                .orElseThrow(() -> new ServiceException("There are no users in the network!"));
    }
}
