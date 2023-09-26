package com.mindex.challenge.data;

import java.util.Date;

public class Compensation {
    private double salary;
    private Date effectiveDate;
    public Compensation(double salary, Date effectiveDate) {
        this.salary = salary;
        this.effectiveDate = effectiveDate;
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
