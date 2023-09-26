package com.mindex.challenge.dao;

import com.mindex.challenge.data.ReportingStructure;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AggregateRepositoryTest {
    private static final String TOP_LEVEL_EMPLOYEE_ID = "16a596ae-edd3-4847-99fe-c4518e82c86f";
    private static final String SINGLE_LEVEL_REPORT_EMPLOYEE_ID = "c0c2293d-16bd-4603-8e08-638a9d18b22c";
    private static final String NESTED_EMPLOYEE_ID = "b7839309-3348-463b-a7e3-5de1c168beb3";
    @Autowired
    private AggregateRepository aggregateRepository;

    @Autowired
    private EmployeeRepository employeeRepo;
    @Test
    public void testGetReportingStructureRecursive() {
        int numberOfReports = aggregateRepository.getReportingStructure(TOP_LEVEL_EMPLOYEE_ID);
        assertEquals(5, numberOfReports);
    }
    @Test
    public void testGetReportingStructureSingleLayer() {
        int numberOfReports = aggregateRepository.getReportingStructure(SINGLE_LEVEL_REPORT_EMPLOYEE_ID);
        assertEquals(1, numberOfReports);
    }
    @Test
    public void testGetReportingStructureZeroReports() {
        int zeroReports = aggregateRepository.getReportingStructure(NESTED_EMPLOYEE_ID);
        assertEquals(0, zeroReports);
    }
}
