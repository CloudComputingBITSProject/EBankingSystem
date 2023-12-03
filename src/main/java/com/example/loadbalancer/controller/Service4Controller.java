package com.example.loadbalancer.controller;

import com.example.loadbalancer.service.RedirectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RestController
@RequestMapping("/service3")
public class Service3Controller {
    @Autowired
    private RedirectService redirectService;
//    private final DockerAgent dockerAgent;
//    ServiceController(DockerAgent dockerAgent){
//        this.dockerAgent = dockerAgent;
//    }
//    @GetMapping("/service1/**")
//    public String handleHttpServletRequest(HttpServletRequest request) {
//        // Extracting path from the request
//        String fullPath = request.getRequestURI();
//        String dynamicPath = fullPath.replaceFirst("/service1/", "");
//
//
//        // Your logic using the extracted path
//        return "Dynamic Path: " + dynamicPath;
//    }
@RequestMapping(value = "/**")
public ResponseEntity<?> route(HttpServletRequest request) throws IOException {
    String firstUrl = "http://localhost:8080";
    String fullPath = request.getRequestURI();
//        String serviceNo = fullPath.split("/")[2];
    String dynamicPath = fullPath.replaceFirst("/service3/", "/");
    String newPath = firstUrl + dynamicPath;
    return redirectService.route(request,"service-3",newPath);
}
    @GetMapping("/ping")
    public ResponseEntity<?> hello(){
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
