package com.mindex.challenge.service.impl;

import com.mindex.challenge.dao.AggregateRepository;
import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.data.CompensationRequest;
import org.bson.Document;
import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.exception.ResourceNotFoundException;
import com.mindex.challenge.service.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.UUID;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private static final Logger LOG = LoggerFactory.getLogger(EmployeeServiceImpl.class);

    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private AggregateRepository aggregateRepository;

    @Override
    public Employee create(Employee employee) {
        LOG.debug("Creating employee [{}]", employee);

        employee.setEmployeeId(UUID.randomUUID().toString());
        employeeRepository.insert(employee);

        return employee;
    }

    @Override
    public Employee read(String id) {
        LOG.debug("Fetching employee with id [{}]", id);

        Employee employee = employeeRepository.findByEmployeeId(id);

        if (employee == null) {
            throw new RuntimeException("Invalid employeeId: " + id);
        }

        return employee;
    }

    @Override
    public ReportingStructure getReportingStructure(String employeeId) {
        LOG.debug("Fetching reporting structure for employee with id [{}]", employeeId);

        Employee employee = employeeRepository.findByEmployeeId(employeeId);

        if (employee == null) {
            throw new ResourceNotFoundException("Invalid employeeId: " + employeeId);
        }

        int numberOfReports = 0;
        // if there are no direct reports then there's no need to try and query the total number of reports
        if(employee.getDirectReports() != null && employee.getDirectReports().size() > 0) {
            numberOfReports = aggregateRepository.getReportingStructure(employeeId);
        }
        return new ReportingStructure(employee, numberOfReports);
    }

    @Override
    public Employee addCompensation(String employeeId, CompensationRequest compRequest) {
        LOG.debug("Updating employee employee compensation [{}]", employeeId);
        Employee employee = employeeRepository.findByEmployeeId(employeeId);

        if (employee == null) {
            throw new ResourceNotFoundException("Invalid employeeId: " + employeeId);
        }
        Compensation compensation = new Compensation(compRequest.getSalary(), compRequest.getEffectiveDate());
        if(employee.getCompensation() == null) {
            employee.setCompensation(Arrays.asList(compensation));
        } else{
            employee.getCompensation().add(compensation);
        }

        return employeeRepository.save(employee);
    }

    @Override
    public Employee update(Employee employee) {
        LOG.debug("Updating employee [{}]", employee);

        return employeeRepository.save(employee);
    }
}
