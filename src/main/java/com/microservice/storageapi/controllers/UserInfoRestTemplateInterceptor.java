package com.microservice.storageapi.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class UserInfoRestTemplateInterceptor implements ClientHttpRequestInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(UserInfoRestTemplateInterceptor.class);

    @Value("${sso.github.token}")
    String token;

    @Override
    public ClientHttpResponse intercept(
            HttpRequest request,
            byte[] body,
            ClientHttpRequestExecution execution) throws IOException {
        request.getHeaders().set("Authorization", "token " + token);
        request.getHeaders().set("accept", "application/json");
        ClientHttpResponse response = execution.execute(request, body);
        logger.warn("user info request: {}", request.getURI());
        logger.warn("uesr info response: {}", response.toString());
        return response;
    }
}
