package com.microservice.storageapi.configurations.filters;


import com.microservice.storageapi.utilities.OpaqueTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class GitHubOAuthFilter extends OncePerRequestFilter {

    @Autowired
    OpaqueTokenUtil tokenParser;

    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String token = tokenParser.getOpaqueToken(request);
            if(token != null && )
        } catch (Exception e) {

        }
        filterChain.doFilter(request, response);
    }

}
