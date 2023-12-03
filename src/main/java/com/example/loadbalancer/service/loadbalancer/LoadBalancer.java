package com.example.loadbalancer.service;

import org.springframework.stereotype.Service;

@Service
public interface LoadBalancer {


    public int getNextPort();

}
