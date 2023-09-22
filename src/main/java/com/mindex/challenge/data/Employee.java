package com.mindex.challenge.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.annotation.Id;

import java.util.List;

public class Employee {
    // Without this Spring Data will create duplicate records when we just try to update the existing ones
    // if we add @Id to employeeId, the underlying collection schema changes (employeeId becomes _id in the schema)
    @Id
    @JsonIgnore
    private String id;
    private String employeeId;
    private String firstName;
    private String lastName;
    private String position;
    private String department;
    private List<Employee> directReports;

    @JsonIgnore
    private List<Compensation> compensation;

    public Employee() {
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public List<Employee> getDirectReports() {
        return directReports;
    }

    public void setDirectReports(List<Employee> directReports) {
        this.directReports = directReports;
    }

    public List<Compensation> getCompensation() {
        return compensation;
    }

    public void setCompensation(List<Compensation> compensation) {
        this.compensation = compensation;
    }
}
