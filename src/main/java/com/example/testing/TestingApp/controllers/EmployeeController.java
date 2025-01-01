package com.example.testing.TestingApp.controllers;

import com.example.testing.TestingApp.dto.EmployeeDto;
import com.example.testing.TestingApp.services.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/employees")
public class EmployeeController {
    private final EmployeeService employeeService;

    @GetMapping("/{employeeId}")
    public ResponseEntity<EmployeeDto> getEmployeeById(@PathVariable Long employeeId){
        return ResponseEntity.ok(employeeService.getEmployeeById(employeeId));
    }

    @PostMapping
    public ResponseEntity<EmployeeDto> createNewEmployee(@RequestBody EmployeeDto employeeDto){
        EmployeeDto employeeDto1=employeeService.createNewEmployee(employeeDto);
        return new ResponseEntity<>(employeeDto1, HttpStatus.CREATED);
    }
    @PutMapping("/{employeeId}")
    public ResponseEntity<EmployeeDto> updateEmployee(@PathVariable Long employeeId,
                                                      @RequestBody EmployeeDto employeeDto)
    {
        EmployeeDto employeeDto1=employeeService.updateEmployee(employeeId,employeeDto);
        return ResponseEntity.ok(employeeDto1);
    }
    @DeleteMapping("/{employeeId}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long employeeId){
        employeeService.deleteEmployee(employeeId);
        return ResponseEntity.noContent().build();
    }
}
