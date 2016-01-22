package api;

import application.Application;
import com.eclipsesource.restfuse.*;
import com.eclipsesource.restfuse.annotation.Context;
import com.eclipsesource.restfuse.annotation.HttpTest;
import org.junit.*;
import org.junit.Assert;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import representation.service.Role;
import representation.service.Service;
import util.DbManager;

import static com.eclipsesource.restfuse.Assert.assertOk;
import static org.junit.Assert.*;

/**
 * Created by Timur on 1/22/2016.
 * Copyright Timur Tasci, ISW Universität Stuttgart
 */
@RunWith( HttpJUnitRunner.class )
public class ServiceControllerTest {

    public static Service service;
    public static String serviceId;
    public static final String serviceName = "TestService";
    public static final String roleName = "TestRole";

    @BeforeClass
    public static void init(){
        Application.main(new String[] {});
        service = new Service("1",serviceName, new Role(roleName));
        service = DbManager.registerService(service);
        serviceId = service.getServiceId();
    }

    @AfterClass
    public static void tearDown(){
        DbManager.unregisterServiceByID(service.getServiceId());
    }

    @Rule
    public Destination destination = new Destination(this,"http://localhost:8080");

    @Context
    private Response response;

    @HttpTest(method = Method.GET, path = "/api/services")
    public void testGetAllServices() throws Exception {
        assertOk(response);
    }

    @HttpTest(method = Method.GET, path = "/api/service/name/" + serviceName)
    public void testGetServicesByName() throws Exception {
        assertOk(response);
    }

    @HttpTest(method = Method.GET, path = "/api/service/id/{serviceId}")
    public void testGetServiceById() throws Exception {

    }

    @HttpTest(method = Method.GET, path = "/api/service/role/" + roleName)
    public void testGetServiceByRoleName() throws Exception {
    }

    @Test
    public void testRegisterService() throws Exception {

    }

    @Test
    public void testUnregisterService() throws Exception {

    }

}