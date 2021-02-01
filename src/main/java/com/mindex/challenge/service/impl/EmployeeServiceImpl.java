package com.mindex.challenge.service.impl;

import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private static final Logger LOG = LoggerFactory.getLogger(EmployeeServiceImpl.class);

    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public Employee create(Employee employee) {
        LOG.debug("Creating employee [{}]", employee);

        employee.setEmployeeId(UUID.randomUUID().toString());
        employeeRepository.insert(employee);


        return employee;
    }

    @Override
    public Employee read(String id) {
        LOG.debug("Creating employee with id [{}]", id);

        Employee employee = employeeRepository.findByEmployeeId(id);

        if (employee == null) {
            throw new RuntimeException("Invalid employeeId: " + id);
        }

        return employee;
    }

    @Override
    public Employee update(Employee employee) {
        LOG.debug("Updating employee [{}]", employee);

        return employeeRepository.save(employee);
    }

    @Override
    public ReportingStructure getReports(String id) {
        Employee queryEmployee = employeeRepository.findByEmployeeId(id);
        int numberOfReports = 0;
        Stack<String> stackOfEmployeeIDs = new Stack<>();
        stackOfEmployeeIDs.add(queryEmployee.getEmployeeId());

        while (!stackOfEmployeeIDs.isEmpty()){
            String emploeeID = stackOfEmployeeIDs.pop();
            List<Employee> directReports = read(emploeeID).getDirectReports();
            if (directReports!=null){
                numberOfReports+=directReports.size();
                for (Employee directReport : directReports){
                    stackOfEmployeeIDs.add(directReport.getEmployeeId());
                }
            }
        }
        return new ReportingStructure(queryEmployee,numberOfReports);
    }
}
