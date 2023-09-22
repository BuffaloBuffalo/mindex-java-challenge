package com.mindex.challenge.controller;

import com.mindex.challenge.data.CompensationRequest;
import com.mindex.challenge.data.CompensationResponseDto;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class EmployeeController {
    private static final Logger LOG = LoggerFactory.getLogger(EmployeeController.class);

    @Autowired
    private EmployeeService employeeService;

    @PostMapping("/employee")
    public Employee create(@RequestBody Employee employee) {
        LOG.debug("Received employee create request for [{}]", employee);

        return employeeService.create(employee);
    }

    @GetMapping("/employee/{id}")
    public Employee read(@PathVariable String id) {
        LOG.debug("Received employee get request for id [{}]", id);

        return employeeService.read(id);
    }

    @GetMapping("/employee/{id}/reporting")
    public ReportingStructure getReportingStructure(@PathVariable String id){
        LOG.debug("Received employee create request for id [{}]", id);

        return employeeService.getReportingStructure(id);
    }

    @PutMapping("/employee/{id}")
    public Employee update(@PathVariable String id, @RequestBody Employee employee) {
        LOG.debug("Received employee update request for id [{}] and employee [{}]", id, employee);

        employee.setEmployeeId(id);
        return employeeService.update(employee);
    }

    @PutMapping("/employee/{id}/compensation")
    public CompensationResponseDto addCompensation(@PathVariable String id, @RequestBody CompensationRequest comp){
        Employee e = employeeService.addCompensation(id, comp);
        return new CompensationResponseDto(e);
    }
    @GetMapping("/employee/{id}/compensation")
    public CompensationResponseDto getCompensation(@PathVariable String id){
        Employee e = employeeService.read(id);
        return new CompensationResponseDto(e);
    }
}
