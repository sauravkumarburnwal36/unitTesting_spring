package com.example.testing.TestingApp.services;

import com.example.testing.TestingApp.dto.EmployeeDto;

public interface EmployeeService {
    public EmployeeDto getEmployeeById(Long employeeId);

    public EmployeeDto createNewEmployee(EmployeeDto employeeDto);


    public EmployeeDto updateEmployee(Long employeeId, EmployeeDto employeeDto);

    public void deleteEmployee(Long employeeId);
}
