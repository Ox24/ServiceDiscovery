package representation.service;

/**
 * Created by Timur on 1/14/2016.
 */
public class Service {

    Role role;
    int ID;

    public Service(int ID, Role role) {
        this.role = role;
        this.ID = ID;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
