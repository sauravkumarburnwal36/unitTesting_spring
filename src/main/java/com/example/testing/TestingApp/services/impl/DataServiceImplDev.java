package com.example.testing.TestingApp.services.impl;

import com.example.testing.TestingApp.services.DataService;
import org.springframework.stereotype.Service;

@Service
public class DataServiceImplDev implements DataService {
    @Override
    public String getData() {
        return "Dev Data";
    }
}
