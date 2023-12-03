package com.example.loadbalancer.controller;

import com.example.loadbalancer.service.AdminAgent;
import com.example.loadbalancer.service.RedirectService;
import com.example.loadbalancer.service.SQLAgent;
import com.example.loadbalancer.service.User;
import com.github.dockerjava.api.model.Container;
import com.github.dockerjava.api.model.Image;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/user")
public class Authentication {
    @PostMapping("/signup")
    public ResponseEntity<?> autoScalerController(@RequestParam String strategy, @RequestParam String service, String username){

        User currentUser = adminAgent.addAndGetAgent(username);
        currentUser.setAutoScalerStrategy(strategy,service);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @PostMapping("/login")
    public ResponseEntity<?> loadBalancerController(@RequestParam String strategy,@RequestParam String service, String username,@RequestParam Optional<List<Integer>> weights){
        User currentUser = adminAgent.addAndGetAgent(username);
        currentUser.setLoadBalancerStrategy(strategy,service, weights.orElse(null));
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @GetMapping("/ping")
    public ResponseEntity<?> hello(){
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
