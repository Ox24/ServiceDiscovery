package representation.service;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * Created by Timur on 1/14/2016.
 */
@JsonPropertyOrder({ "ServiceId", "ServiceName", "role" })
public class Service {

    int ServiceId;
    String ServiceName;
    Role role;

    public Service(){}

    public Service(int ID, String ServiceName, Role role) {
        this.ServiceId = ID;
        this.ServiceName = ServiceName;
        this.role = role;
    }

    public int getServiceId() {
        return ServiceId;
    }

    public void setServiceId(int serviceId) {
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
