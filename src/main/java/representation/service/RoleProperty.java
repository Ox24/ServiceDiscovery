package representation.service;

/**
 * Created by Timur on 1/14/2016.
 */
public class RoleProperty {
    protected String Name;
    protected RolePropertyType Type;

    public RoleProperty() {

    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public RolePropertyType getType() {
        return Type;
    }

    public void setType(RolePropertyType type) {
        Type = type;
    }
}
