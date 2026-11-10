package Domain;

public class Friendship extends Entity<Long> {

    private User a;
    private User b;

    public Friendship(User a, User b) {
        this.a = a;
        this.b = b;
    }

    public User getA() {
        return a;
    }

    public User getB() {
        return b;
    }

    public boolean involves(Long userId) {
        return a.getIdentitykey().equals(userId) || b.getIdentitykey().equals(userId);
    }

    /**
     * Gives the other user of the friendship.
     *
     * @param userId the ID of one of the two users
     * @return the user that is not the one with the given ID
     */
    public User getOther(Long userId) {
        return a.getIdentitykey().equals(userId) ? b : a;
    }

    @Override
    public String toString() {
        return getIdentitykey() + ": " + a.getFirstName() + " " + a.getLastName()
                + " <-> " + b.getFirstName() + " " + b.getLastName();
    }
}
