package Service;

import Domain.Community;
import Domain.Friendship;
import Domain.User;
import Exceptions.ServiceException;
import Repository.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

public class NetworkService {
    private Repository<Long, User> userRepo;
    private Repository<Long, Friendship> friendshipRepo;

    public NetworkService(Repository<Long, User> userRepo, Repository<Long, Friendship> friendshipRepo) {
        this.userRepo = userRepo;
        this.friendshipRepo = friendshipRepo;
    }

    private Map<User, List<User>> buildAdjacency() {
        Map<User, List<User>> adjacency = new HashMap<>();
        for (User user : userRepo.getAll()) {
            adjacency.put(user, new ArrayList<>());
        }
        for (Friendship friendship : friendshipRepo.getAll()) {
            adjacency.get(friendship.getA()).add(friendship.getB());
            adjacency.get(friendship.getB()).add(friendship.getA());
        }
        return adjacency;
    }

    public List<Community> getCommunities() {
        Map<User, List<User>> adjacency = buildAdjacency();
        List<Community> communities = new ArrayList<>();
        Set<User> visited = new HashSet<>();

        for (User start : adjacency.keySet()) {
            if (!visited.contains(start)) {
                Map<User, List<User>> part = new HashMap<>();
                Stack<User> stack = new Stack<>();
                stack.push(start);
                visited.add(start);
                while (!stack.isEmpty()) {
                    User current = stack.pop();
                    part.put(current, adjacency.get(current));
                    for (User neighbor : adjacency.get(current)) {
                        if (!visited.contains(neighbor)) {
                            visited.add(neighbor);
                            stack.push(neighbor);
                        }
                    }
                }
                communities.add(new Community(part));
            }
        }
        return communities;
    }

    public int numberOfCommunities() {
        return getCommunities().size();
    }

    public Community mostSociableCommunity() {
        List<Community> communities = getCommunities();
        if (communities.isEmpty()) {
            throw new ServiceException("There are no users in the network!");
        }
        Community best = null;
        int bestLength = -1;
        for (Community community : communities) {
            int length = community.longestPath();
            if (length > bestLength) {
                bestLength = length;
                best = community;
            }
        }
        return best;
    }
}
