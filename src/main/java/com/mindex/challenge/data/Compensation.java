package com.mindex.challenge.data;

import java.util.Date;

public class Compensation {
    private Employee employee;
    private String compensation;
    private Date effectiveDate;

    public Compensation(Employee employee,String compensation,Date effectiveDate) {
        this.employee=employee;
        this.compensation=compensation;
        this.effectiveDate=effectiveDate;
    }

    public Employee getEmployee() {
        return employee;
    }

    public Date getEffectiveDate() {
        return effectiveDate;
    }

    public String getCompensation() {
        return compensation;
    }
}
