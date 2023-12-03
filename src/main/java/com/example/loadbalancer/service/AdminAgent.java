package com.example.loadbalancer.service;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@Component
public class AdminAgent {
    Map<String,DockerAgent> userMapping;
    public AdminAgent(){
        this.userMapping = new HashMap<>();
    }
}
