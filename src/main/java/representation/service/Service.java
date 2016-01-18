package representation.service;

/**
 * Created by Timur on 1/14/2016.
 */

public class Service {

    protected String ServiceId;
    protected String ServiceName;
    protected Role role;

    public Service(){}

    public Service(String ID, String ServiceName, Role role) {
        this.ServiceId = ID;
        this.ServiceName = ServiceName;
        this.role = role;
    }

    public String getServiceId() {
        return ServiceId;
    }

    public void setServiceId(String serviceId) {
        ServiceId = serviceId;
    }

    public String getServiceName() {
        return ServiceName;
    }

    public void setServiceName(String serviceName) {
        ServiceName = serviceName;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
