package com.mindex.challenge.data;

import java.util.Comparator;
import java.util.Date;

public class CompensationResponseDto {
    private Employee employee;
    private double salary;
    private Date effectiveDate;
    public CompensationResponseDto() {}
    public CompensationResponseDto(Employee e) {
        this.employee = e;
        if (e.getCompensation() != null && e.getCompensation().size() > 0) {
            // sort the compensations by effective date, latest first
            e.getCompensation().sort((c1, c2) -> c2.getEffectiveDate().compareTo(c1.getEffectiveDate()));
            // get the most recent compensation that is not in the future
            e.getCompensation().stream().filter(c -> c.getEffectiveDate().before(new Date())).findFirst().ifPresent(c -> {
                this.salary = c.getSalary();
                this.effectiveDate = c.getEffectiveDate();
            });
        }
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public Date getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(Date effectiveDate) {
        this.effectiveDate = effectiveDate;
    }
}
