package com.example.testing.TestingApp.controllers;

import com.example.testing.TestingApp.TestContainerConfiguration;
import com.example.testing.TestingApp.dto.EmployeeDto;
import com.example.testing.TestingApp.entities.Employee;
import com.example.testing.TestingApp.repositories.EmployeeRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class EmployeeControllerTestIT extends AbstractIntegrationTest{


    @Autowired
    private EmployeeRepository employeeRepository;



    @BeforeEach
    void setup(){
        employeeRepository.deleteAll();
    }

    @Test
    void testGetEmployeeById_success(){
        Employee savedEmployee=employeeRepository.save(testEmployee);
        webTestClient.get()
                .uri("/employees/{employeeId}",savedEmployee.getId())
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isEqualTo(savedEmployee.getId())
                .jsonPath("$.email").isEqualTo(savedEmployee.getEmail());
//                .value(employeeDto->{
//                    assertThat(employeeDto.getEmail()).isEqualTo(savedEmployee.getEmail());
//                    assertThat(employeeDto.getId()).isEqualTo(savedEmployee.getId());
//                });
    }

    @Test
    void testGetEmployeeById_failure(){
        webTestClient.get()
                .uri("/employees/1")
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void testCreateNewEmployee_whenEmployeeAlreadyExist_thenThrowException(){
        Employee savedEmployee=employeeRepository.save(testEmployee);
        webTestClient.post().uri("/employees")
                .bodyValue(testEmployeeDto)
                .exchange()
                .expectStatus().is5xxServerError();
    }

    @Test
    void testCreateNewEmployee_whenEmployeeDoesNotExist_thenCreateEmployee(){
        webTestClient.post().uri("/employees")
                .bodyValue(testEmployeeDto)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.email").isEqualTo(testEmployeeDto.getEmail())
                .jsonPath("$.name").isEqualTo((testEmployeeDto.getName()));
    }

    @Test
    void testUpdateEmployee_whenEmployeeDoesNotExist_thenThrowException(){
        webTestClient.put().uri("/employees/999")
                .bodyValue(testEmployeeDto)
                .exchange()
                .expectStatus().isNotFound();
    }


    @Test
    void testUpdateEmployee_whenAttemptingToUpdateEmail_thenThrowException(){
        Employee savedEmployee=employeeRepository.save(testEmployee);
        testEmployeeDto.setName("Random Name");
        testEmployeeDto.setEmail("random@gmail.com");
        webTestClient.put().uri("/employees/{employeeId}",savedEmployee.getId())
                .bodyValue(testEmployeeDto)
                .exchange()
                .expectStatus().is5xxServerError();
    }

    @Test
    void testUpdateEmployee_whenEmployeeIsValid_thenUpdateEmployee(){
        Employee savedEmployee=employeeRepository.save(testEmployee);
        testEmployeeDto.setName("Random Name");
        testEmployeeDto.setSalary(BigDecimal.valueOf(50000.00));
        webTestClient.put().uri("/employees/{employeeId}",savedEmployee.getId())
                .bodyValue(testEmployeeDto)
                .exchange()
                .expectStatus().isOk()
                .expectBody(EmployeeDto.class)
                .isEqualTo(testEmployeeDto);
    }

    @Test
    void testDeleteEmployee_whenEmployeeDoesNotExist_thenThrowException(){
        webTestClient.delete().uri("/employees/1")
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void testDeleteEmployee_whenEmployeeExist_thenDeleteEmployee(){
        Employee savedEmployee=employeeRepository.save(testEmployee);
        webTestClient.delete().uri("/employees/{employeeId}",savedEmployee.getId())
                .exchange()
                .expectStatus().isNoContent()
                .expectBody(Void.class);
        webTestClient.delete().uri("/employees/{employeeId}",savedEmployee.getId())
                .exchange()
                .expectStatus().isNotFound();
    }

}