package Domain;

public class User extends Entity<Long> {
    private String FirstName;
    private String LastName;

    public User(String a, String b) {
        this.FirstName = a;
        this.LastName = b;
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }
}
