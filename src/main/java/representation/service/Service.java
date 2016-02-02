package representation.service;

/**
 * Created by Timur on 1/14/2016.
 */

public class Service {

    protected String ServiceId;
    protected String ServiceName;
    protected Role role;
    protected Long version;
    protected String status;

    public Service(){}

    public Service(String ServiceName, Role role) {
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

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
