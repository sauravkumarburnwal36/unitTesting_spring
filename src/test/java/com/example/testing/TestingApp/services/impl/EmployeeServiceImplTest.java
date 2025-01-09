package com.example.testing.TestingApp.services.impl;

import com.example.testing.TestingApp.TestContainerConfiguration;
import com.example.testing.TestingApp.dto.EmployeeDto;
import com.example.testing.TestingApp.entities.Employee;
import com.example.testing.TestingApp.exceptions.ResourceNotFoundException;
import com.example.testing.TestingApp.repositories.EmployeeRepository;
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

import static org.assertj.core.api.Assertions.*;
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
    void testGetEmployeeById_whenEmployeeIdIsNotPresent_thenThrowException(){
        //assign/given
          when(employeeRepository.findById(anyLong())).thenReturn(Optional.empty());

          //act & assert
        assertThatThrownBy(()->employeeService.getEmployeeById(1L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Employee not found with id:1");
        verify(employeeRepository).findById(1L);
    }

    @Test
    void testCreateNewEmployee_whenAttemptingCreatingNewEmployeeEmailAlreadyExists_thenThrowException(){
        //assign/given
    when(employeeRepository.findByEmail(anyString())).thenReturn(List.of(mockEmployee));
    //act & assert
    assertThatThrownBy(()->employeeService.createNewEmployee(mockEmployeeDto))
            .isInstanceOf(RuntimeException.class)
            .hasMessage("Employee already exists with email:"+mockEmployeeDto.getEmail());
    verify(employeeRepository).findByEmail(mockEmployeeDto.getEmail());
    verify(employeeRepository,never()).save(any());
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

    @Test
    void testUpdateEmployee_whenEmployeeDoesNotExist_thenThrowException(){
        //assign/given
        when(employeeRepository.findById(1L)).thenReturn(Optional.empty());

        //act & assert
        assertThatThrownBy(()->employeeService.updateEmployee(1l,mockEmployeeDto))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Employee not found with id:1");
        verify(employeeRepository).findById(1L);
        verify(employeeRepository,never()).save(any());
    }

    @Test
    void testUpdateEmployee_whenAttemptingUpdatingEmployeeForEmailExists_thenThrowException(){
        //assign/given
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(mockEmployee));
        mockEmployeeDto.setName("Random");
        mockEmployeeDto.setEmail("random@gmail.com");

        //act & assert
        assertThatThrownBy(()->
                employeeService.updateEmployee(
                        mockEmployeeDto.getId(),mockEmployeeDto))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("The email of the employee cannot be updated");
        verify(employeeRepository).findById(1L);
        verify(employeeRepository,never()).save(any());
    }

    @Test
    void testUpdateEmployee_whenEmployeeIsPresent_thenReturnEmployeeDto(){
        //assign/given
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(mockEmployee));
        mockEmployeeDto.setName("Raj");
        mockEmployeeDto.setSalary(BigDecimal.valueOf(120000.00));
        Employee newEmployee=modelMapper.map(mockEmployeeDto, Employee.class);
        when(employeeRepository.save(any(Employee.class))).thenReturn(newEmployee);

        //act
        EmployeeDto employeeDto=employeeService.updateEmployee(mockEmployeeDto.getId(),mockEmployeeDto);

        //assert/then
        assertThat(employeeDto).isNotNull();
        assertThat(employeeDto).isEqualTo(mockEmployeeDto);

        verify(employeeRepository).findById(1L);
        verify(employeeRepository).save(mockEmployee);
    }

    @Test
    void testDeleteEmployee_whenEmployeeDoesNotExist_thenThrowException(){
        //assign/given
        when(employeeRepository.existsById(1L)).
                thenReturn(false);

        //act &assert
        assertThatThrownBy(()->
                employeeService.deleteEmployee(1L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Employee not found with id:1");
        verify(employeeRepository,never()).deleteById(anyLong());
    }

    @Test
    void testDeleteEmployee_whenEmployeeExists_thenDeleteEmployee(){
        //assign/given
        when(employeeRepository.existsById(1L)).thenReturn(true);

        assertThatCode(()->employeeService.deleteEmployee(1L))
                .doesNotThrowAnyException();
        verify(employeeRepository).deleteById(1L);
    }
}