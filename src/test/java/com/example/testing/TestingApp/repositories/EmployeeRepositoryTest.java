package com.example.testing.TestingApp.repositories;

import com.example.testing.TestingApp.TestContainerConfiguration;
import com.example.testing.TestingApp.entities.Employee;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

//@SpringBootTest
@Import(TestContainerConfiguration.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace =AutoConfigureTestDatabase.Replace.ANY)
class EmployeeRepositoryTest {

    @Autowired
    private EmployeeRepository employeeRepository;

    private Employee employee;

    @BeforeEach
    void setup(){
        employee=Employee.builder().id(1L)
                .name("Saurav")
                .email("saurav@gmail.com")
                .salary(BigDecimal.valueOf(100000.00))
                .build();
    }

    @Test
    void testFindByEmail_whenEmailIsPresent_thenReturnEmployeeList() {
        //Arrange/Given
        employeeRepository.save(employee);
        //Act/when
        List<Employee> employeeList=employeeRepository.findByEmail(employee.getEmail());
        //Assert/then
        assertThat(employeeList).isNotNull();
        assertThat(employeeList).isNotEmpty();
        assertThat(employeeList.get(0).getEmail()).isEqualTo(employee.getEmail());
    }

    @Test
    void testFindByEmail_whenEmailNotFound_thenReturnEmptyEmployee(){
        //Arrange/given
        String email="noPresent123@gmail.com";

        //Act/When
        List<Employee> employeeList=employeeRepository.findByEmail(email);

        //Assert/Then
        assertThat(employeeList).isNotNull();
        assertThat(employeeList).isEmpty();
    }
}