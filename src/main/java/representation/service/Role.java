package representation.service;

import java.util.List;

/**
 * Created by Timur on 1/14/2016.
 */
public class Role {
    protected String name;
    protected List<MessageInterface> MessageInterfaces;
    protected List<RoleProperty> RoleProps;
    protected String Model;

    public Role() {
    }

    public Role(String name, List<MessageInterface> anInterface, List<RoleProperty> roleProps, String model) {
        this.name = name;
        MessageInterfaces = anInterface;
        RoleProps = roleProps;
        Model = model;
    }

    public Role(String name, List<MessageInterface> anInterface, List<RoleProperty> roleProps) {
        this.name = name;
        MessageInterfaces = anInterface;
        RoleProps = roleProps;
    }

    public Role(String name) {
        this.name = name;
    }

    public Role(String name, List<MessageInterface> anInterface) {
        this.name = name;
        MessageInterfaces = anInterface;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public List<MessageInterface> getMessageInterfaces() {
        return MessageInterfaces;
    }

    public void setMessageInterfaces(List<MessageInterface> anInterface) {
        MessageInterfaces = anInterface;
    }
}
