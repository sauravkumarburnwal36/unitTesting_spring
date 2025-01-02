package com.example.testing.TestingApp.controllers;

import com.example.testing.TestingApp.TestContainerConfiguration;
import com.example.testing.TestingApp.dto.EmployeeDto;
import com.example.testing.TestingApp.entities.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.math.BigDecimal;

@AutoConfigureWebTestClient(timeout ="100000")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(TestContainerConfiguration.class)
public class AbstractIntegrationTest {
    @Autowired
    public WebTestClient webTestClient;

    Employee testEmployee=Employee.builder()
            .id(1L)
                .email("saurav@gmail.com")
                .name("Saurav")
                .salary(BigDecimal.valueOf(100000.00))
            .build();
    EmployeeDto testEmployeeDto=EmployeeDto.builder()
            .id(1L)
                .email("saurav@gmail.com")
                .name("Saurav")
                .salary(BigDecimal.valueOf(100000.00))
            .build();
}
