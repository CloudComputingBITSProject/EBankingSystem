package com.example.loadbalancer.service;

import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.charset.Charset;

@Service
public class RedirectService {
    public ResponseEntity<?> route(HttpServletRequest request,String serviceName,String newPath) throws IOException {
        String body = IOUtils.toString(request.getInputStream(), Charset.forName(request.getCharacterEncoding()));
        RestTemplate restTemplate = new RestTemplate();
        try {
            return restTemplate.exchange(newPath,
                    HttpMethod.valueOf(request.getMethod()),
                    new HttpEntity<>(body),
                    Object.class,
                    request.getParameterMap());
        } catch (final HttpClientErrorException e) {
            return new ResponseEntity<>(e.getResponseBodyAsByteArray(), e.getResponseHeaders(), e.getStatusCode());
        }
    }
}
