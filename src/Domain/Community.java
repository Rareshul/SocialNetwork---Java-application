package Domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Community {
    private Map<User, List<User>> adjacency;
    private Integer longestPath;

    public Community(Map<User, List<User>> adjacency) {
        this.adjacency = adjacency;
    }

    public Set<User> getMembers() {
        return adjacency.keySet();
    }

    public int size() {
        return adjacency.size();
    }

    /**
     * Computes the length (in edges) of the longest simple path in the community.
     * The result is computed once and then remembered.
     *
     * @return the number of edges of the longest path
     */
    public int longestPath() {
        if (longestPath == null) {
            longestPath = adjacency.keySet().stream()
                    .mapToInt(start -> longestFrom(start, List.of(start)))
                    .max()
                    .orElse(0);
        }
        return longestPath;
    }

    /**
     * Longest path that starts in {@code current} and does not go through the users already in {@code path}.
     *
     * @param current the user where the path continues
     * @param path    the users already visited, including {@code current}
     * @return the number of edges that can still be added
     */
    private int longestFrom(User current, List<User> path) {
        return adjacency.get(current).stream()
                .filter(neighbor -> !path.contains(neighbor))
                .mapToInt(neighbor -> {
                    List<User> longer = new ArrayList<>(path);
                    longer.add(neighbor);
                    return 1 + longestFrom(neighbor, longer);
                })
                .max()
                .orElse(0);
    }
}
