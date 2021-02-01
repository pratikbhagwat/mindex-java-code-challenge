package com.mindex.challenge.service.impl;

import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.service.CompensationService;
import com.mindex.challenge.service.EmployeeService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CompensationServiceImplTest {

    private String compensationUrl;
    private String compensationIdUrl;

    @Autowired
    private CompensationService compensationService;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Before
    public void setup() {
        compensationUrl = "http://localhost:" + port + "/compensation";
        compensationIdUrl = "http://localhost:" + port + "/compensation/{id}";
    }

    @Test
    public void testCreateRead(){

        Employee employee = new Employee();
        employee.setEmployeeId("b7839309-3348-463b-a7e3-5de1c168beb3");
        employee.setFirstName("Paul");
        employee.setLastName("McCartney");
        employee.setDepartment("Engineering");
        employee.setPosition("Developer I");


        Compensation expectedCompensation = new Compensation(employee,"120000USD",new Date());

        // create check
        Compensation createdCompensation = restTemplate.postForEntity(compensationUrl,expectedCompensation,Compensation.class).getBody();
        assert createdCompensation != null;
        assertCompensationEquivalence(expectedCompensation,createdCompensation);

        // read check
        Compensation readCompensation = restTemplate.getForEntity(compensationIdUrl,Compensation.class,createdCompensation.getEmployee().getEmployeeId()).getBody();
        assert readCompensation != null;
        assertCompensationEquivalence(expectedCompensation,readCompensation);

    }

    private void assertCompensationEquivalence(Compensation expectedCompensation, Compensation actualCompensation) {
        assertEmployeeEquivalence(expectedCompensation.getEmployee(),actualCompensation.getEmployee());
        assertEquals(expectedCompensation.getCompensation(),actualCompensation.getCompensation());
        assertEquals(expectedCompensation.getEffectiveDate(),actualCompensation.getEffectiveDate());

    }

    private static void assertEmployeeEquivalence(Employee expected, Employee actual) {
        assertEquals(expected.getFirstName(), actual.getFirstName());
        assertEquals(expected.getLastName(), actual.getLastName());
        assertEquals(expected.getDepartment(), actual.getDepartment());
        assertEquals(expected.getPosition(), actual.getPosition());
    }
}
