package representation.service;

import java.util.List;

/**
 * Created by Timur on 1/14/2016.
 */
public class Role {
    String ID;
    List<MessageInterface> MessageInterfaces;
    List<RoleProperty> RoleProps;
    String Model;

    public Role(String ID, List<MessageInterface> anInterface, List<RoleProperty> roleProps, String model) {
        this.ID = ID;
        MessageInterfaces = anInterface;
        RoleProps = roleProps;
        Model = model;
    }

    public Role(String ID, List<MessageInterface> anInterface, List<RoleProperty> roleProps) {
        this.ID = ID;
        MessageInterfaces = anInterface;
        RoleProps = roleProps;
    }

    public Role(String ID) {
        this.ID = ID;
    }

    public Role(String ID, List<MessageInterface> anInterface) {
        this.ID = ID;
        MessageInterfaces = anInterface;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getModel() {
        return Model;
    }

    public void setModel(String model) {
        Model = model;
    }

    public List<RoleProperty> getRoleProps() {
        return RoleProps;
    }

    public void setRoleProps(List<RoleProperty> roleProps) {
        RoleProps = roleProps;
    }

    public List<MessageInterface> getInterface() {
        return MessageInterfaces;
    }

    public void setInterface(List<MessageInterface> anInterface) {
        MessageInterfaces = anInterface;
    }
}
