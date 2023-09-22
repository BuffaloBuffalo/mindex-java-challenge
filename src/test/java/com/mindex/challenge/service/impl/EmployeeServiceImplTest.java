package com.mindex.challenge.service.impl;

import com.mindex.challenge.data.CompensationRequest;
import com.mindex.challenge.data.CompensationResponseDto;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.EmployeeService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.GregorianCalendar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EmployeeServiceImplTest {
    private static final String TOP_LEVEL_EMPLOYEE_ID = "16a596ae-edd3-4847-99fe-c4518e82c86f";
    private String employeeUrl;
    private String employeeIdUrl;
    private String reportingStructureUrl;
    private String compensationUrl;

    @Autowired
    private EmployeeService employeeService;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Before
    public void setup() {
        employeeUrl = "http://localhost:" + port + "/employee";
        employeeIdUrl = "http://localhost:" + port + "/employee/{id}";
        reportingStructureUrl = "http://localhost:" + port + "/employee/{id}/reporting";
        compensationUrl =  "http://localhost:" + port + "/employee/{id}/compensation";
    }

    @Test
    public void testCreateReadUpdate() {
        Employee testEmployee = new Employee();
        testEmployee.setFirstName("John");
        testEmployee.setLastName("Doe");
        testEmployee.setDepartment("Engineering");
        testEmployee.setPosition("Developer");

        // Create checks
        Employee createdEmployee = restTemplate.postForEntity(employeeUrl, testEmployee, Employee.class).getBody();

        assertNotNull(createdEmployee.getEmployeeId());
        assertEmployeeEquivalence(testEmployee, createdEmployee);


        // Read checks
        Employee readEmployee = restTemplate.getForEntity(employeeIdUrl, Employee.class, createdEmployee.getEmployeeId()).getBody();
        assertEquals(createdEmployee.getEmployeeId(), readEmployee.getEmployeeId());
        assertEmployeeEquivalence(createdEmployee, readEmployee);


        // Update checks
        readEmployee.setPosition("Development Manager");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Employee updatedEmployee =
                restTemplate.exchange(employeeIdUrl,
                        HttpMethod.PUT,
                        new HttpEntity<Employee>(readEmployee, headers),
                        Employee.class,
                        readEmployee.getEmployeeId()).getBody();

        assertEmployeeEquivalence(readEmployee, updatedEmployee);
    }

    @Test
    public void testGetReportingStructure() {
        ReportingStructure structure = restTemplate.getForEntity(reportingStructureUrl, ReportingStructure.class, TOP_LEVEL_EMPLOYEE_ID).getBody();
        assertEquals(TOP_LEVEL_EMPLOYEE_ID, structure.getEmployee().getEmployeeId());
        assertEquals(5, structure.getNumberOfReports());
    }

    @Test
    public void testCreateReadUpdateCompensation(){
        // setup
        Employee testEmployee = new Employee();
        testEmployee.setFirstName("John");
        testEmployee.setLastName("Doe");
        testEmployee.setDepartment("Engineering");
        testEmployee.setPosition("Developer");

        // Create checks
        Employee createdEmployee = restTemplate.postForEntity(employeeUrl, testEmployee, Employee.class).getBody();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        Date d = new GregorianCalendar(2010, 01, 01).getTime();
        CompensationRequest request = new CompensationRequest(123, d);

        CompensationResponseDto comp =
                restTemplate.exchange(compensationUrl,
                        HttpMethod.PUT,
                        new HttpEntity<>(request, headers),
                        CompensationResponseDto.class,
                        createdEmployee.getEmployeeId()).getBody();

        assertEquals(123, comp.getSalary(), 0.001);
        assertEquals(d, comp.getEffectiveDate());
        assertEquals(createdEmployee.getEmployeeId(),comp.getEmployee().getEmployeeId());

        // Test Get
        comp = restTemplate.getForEntity(compensationUrl, CompensationResponseDto.class, createdEmployee.getEmployeeId()).getBody();
        assertEquals(123, comp.getSalary(), 0.001);
        assertEquals(d, comp.getEffectiveDate());
        assertEquals(createdEmployee.getEmployeeId(),comp.getEmployee().getEmployeeId());

        // update again and make sure the comp is reflected
        d = new GregorianCalendar(2020, 01, 01).getTime();
        request = new CompensationRequest(150, d);

        comp =
                restTemplate.exchange(compensationUrl,
                        HttpMethod.PUT,
                        new HttpEntity<>(request, headers),
                        CompensationResponseDto.class,
                        createdEmployee.getEmployeeId()).getBody();

        assertEquals(150, comp.getSalary(), 0.001);
        assertEquals(d, comp.getEffectiveDate());
        assertEquals(createdEmployee.getEmployeeId(),comp.getEmployee().getEmployeeId());

        // set future compensation change and verify it does not appear as their current compensation
        Date futureCompDate = new GregorianCalendar(2023, 11, 01).getTime();
        request = new CompensationRequest(200, d);

        comp =
                restTemplate.exchange(compensationUrl,
                        HttpMethod.PUT,
                        new HttpEntity<>(request, headers),
                        CompensationResponseDto.class,
                        createdEmployee.getEmployeeId()).getBody();

        // current comp, not future comp
        assertEquals(150, comp.getSalary(), 0.001);
        assertEquals(d, comp.getEffectiveDate());
        assertEquals(createdEmployee.getEmployeeId(),comp.getEmployee().getEmployeeId());
    }


    private static void assertEmployeeEquivalence(Employee expected, Employee actual) {
        assertEquals(expected.getFirstName(), actual.getFirstName());
        assertEquals(expected.getLastName(), actual.getLastName());
        assertEquals(expected.getDepartment(), actual.getDepartment());
        assertEquals(expected.getPosition(), actual.getPosition());
    }
}
