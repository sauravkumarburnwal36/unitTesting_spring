package com.example.testing.TestingApp.services.impl;

import com.example.testing.TestingApp.dto.EmployeeDto;
import com.example.testing.TestingApp.entities.Employee;
import com.example.testing.TestingApp.exceptions.ResourceNotFoundException;
import com.example.testing.TestingApp.repositories.EmployeeRepository;
import com.example.testing.TestingApp.services.EmployeeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@Slf4j
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final ModelMapper modelMapper;
    @Override
    public EmployeeDto getEmployeeById(Long employeeId) {
        log.info("Fetching employee with id:{}",employeeId);
        Employee employee=employeeRepository.findById(employeeId).orElseThrow(()-> {
            log.error("Employee not found with id:{}", employeeId);
            return new ResourceNotFoundException("Employee not found with id:" +employeeId);
        });
        log.info("Successfully Fetched Employee with id:{}",employeeId);
        return modelMapper.map(employee,EmployeeDto.class);
    }

    @Override
    public EmployeeDto createNewEmployee(EmployeeDto employeeDto) {
        log.info("Creating New Employee with email:{}",employeeDto.getEmail());
        List<Employee> employees=employeeRepository.findByEmail(employeeDto.getEmail());
        if(!employees.isEmpty())
        {
            log.error("Employee already exists with email:{}",employeeDto.getEmail());
            throw new RuntimeException("Employee already exists with email:"+employeeDto.getEmail());
        }
        Employee employee=modelMapper.map(employeeDto,Employee.class);
        Employee savedEmployee=employeeRepository.save(employee);
        log.info("Successfully created new employee:{}",savedEmployee);
        return modelMapper.map(savedEmployee,EmployeeDto.class);
    }

    @Override
    public EmployeeDto updateEmployee(Long employeeId, EmployeeDto employeeDto) {
        log.info("Updating employee with id:{} and employee:{}",employeeId,employeeDto);
        Employee employee=employeeRepository.findById(employeeId).orElseThrow(()->{
                log.error("Employee not found with id:{}",employeeId);
                throw new ResourceNotFoundException("Employee not found with id:"+employeeId);
    });
        if (!employee.getEmail().equals(employeeDto.getEmail())) {
            log.error("Attempted to update email for employee with id: {}", employeeId);
            throw new RuntimeException("The email of the employee cannot be updated");
        }

        modelMapper.map(employeeDto, employee);
        employee.setId(employeeId);
        Employee savedEmployee=employeeRepository.save(employee);
        log.info("Employee details updated:{]",savedEmployee);
        return modelMapper.map(savedEmployee,EmployeeDto.class);
    }

    @Override
    public void deleteEmployee(Long employeeId) {
        log.info("Trying to delete employee with id:{}",employeeId);
        boolean exists= employeeRepository.existsById(employeeId);
        if(!exists){
            log.error("Employee not found with id:{}",employeeId);
            throw new ResourceNotFoundException("Employee not found with id:"+employeeId);
        }
        log.info("Deleted employee with id:{}",employeeId);
        employeeRepository.deleteById(employeeId);

    }
}
