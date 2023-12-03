package com.example.loadbalancer.controller;

import com.example.loadbalancer.service.AutoScaler;
import com.example.loadbalancer.service.LoadBalancer;
import com.example.loadbalancer.service.RedirectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;


import org.springframework.web.bind.annotation.GetMapping;
        import org.springframework.web.bind.annotation.RequestMapping;
        import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/settings")
public class SettingsController {
    @Autowired
    LoadBalancer loadBalancer;
    @Autowired
    AutoScaler autoScaler;
    @Autowired
    private RedirectService redirectService;
    @PostMapping("/autoscaler/{strategy,user_id}")
    public ResponseEntity<?> autoScalerController(@PathVariable String strategy,String user_id){
        //TODO
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @PostMapping("/loadbalancer/{strategy,user_id}")
    public ResponseEntity<?> loadBalancerController(@PathVariable String strategy){
        //TODO
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @PostMapping("/start/{container_id,user_id}")
    public ResponseEntity<?> startServiceController(@PathVariable int container_id){
        //TODO
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/end/{container_id,user_id}")
    public ResponseEntity<?> endServiceController(@PathVariable int container_id){
        //TODO
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @GetMapping("/ping")
    public ResponseEntity<?> hello(){
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
