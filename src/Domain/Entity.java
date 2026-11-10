package Domain;

public class Entity<ID> {
    private ID identitykey;

    public ID getIdentitykey() {
        return identitykey;
    }

    public void setIdentitykey(ID identitykey) {
        this.identitykey = identitykey;
    }
/*    public boolean equals(Entity<ID> a)
    {
        if(this.equals(a))
        {
            return true;
        }
        else if(a == null || a.getClass() != this.getClass())
        {
            return false;
        }
        else
        {

        }
    }

 */

}
