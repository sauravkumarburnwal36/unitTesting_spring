package com.example.testing.TestingApp.services.impl;

import com.example.testing.TestingApp.TestContainerConfiguration;
import com.example.testing.TestingApp.dto.EmployeeDto;
import com.example.testing.TestingApp.entities.Employee;
import com.example.testing.TestingApp.repositories.EmployeeRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@Import(TestContainerConfiguration.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ExtendWith(MockitoExtension.class)
class EmployeeServiceImplTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Spy
    private ModelMapper modelMapper;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    private Employee mockEmployee;

    private EmployeeDto mockEmployeeDto;

    @BeforeEach
    void setup(){
        mockEmployee=Employee.builder()
                .id(1L)
                .email("saurav@gmail.com")
                .name("Saurav")
                .salary(BigDecimal.valueOf(100000.00))
                .build();
        mockEmployeeDto=modelMapper.map(mockEmployee,EmployeeDto.class);
    }

    @Test
    void testGetEmployeeById_whenEmployeeIdIsPresent_thenReturnEmployeeDto(){
        //assign/given
        Long id=mockEmployee.getId();

        when(employeeRepository.findById(id)).thenReturn(Optional.of(mockEmployee));
        //act/when
        EmployeeDto employeeDto=employeeService.getEmployeeById(id);
        //assert/then
        assertThat(employeeDto).isNotNull();
        assertThat(employeeDto.getId()).isEqualTo(id);
        assertThat(employeeDto.getEmail()).isEqualTo(mockEmployee.getEmail());
        verify(employeeRepository).findById(id);
        verify(employeeRepository,times(1)).findById(id);
        verify(employeeRepository,never()).save(mockEmployee);
    }

    @Test
    void testCreateNewEmployee_whenValidEmployee_thenCreateNewEmployee(){
        when(employeeRepository.findByEmail(anyString())).thenReturn(List.of());
        when(employeeRepository.save(any(Employee.class))).thenReturn(mockEmployee);

        //act/when
        EmployeeDto employeeDto=employeeService.createNewEmployee(mockEmployeeDto);

        //assert/then
        assertThat(employeeDto).isNotNull();
        assertThat(employeeDto.getEmail()).isEqualTo(mockEmployeeDto.getEmail());
        //verify(employeeRepository,times(1)).save(mockEmployee);

        ArgumentCaptor<Employee> employeeArgumentCaptor=ArgumentCaptor.forClass(Employee.class);

        verify(employeeRepository).save(employeeArgumentCaptor.capture());

        Employee capturedEmployee=employeeArgumentCaptor.getValue();
        assertThat(capturedEmployee.getEmail()).isEqualTo(mockEmployee.getEmail());

    }
}