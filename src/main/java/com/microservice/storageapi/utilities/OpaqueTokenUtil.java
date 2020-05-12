package com.microservice.storageapi.utilities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

@Component
public class OpaqueTokenUtil {

    private static final Logger logger = LoggerFactory.getLogger(OpaqueTokenUtil.class);

    public String getOpaqueToken(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");

        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("token ")) {
            return headerAuth.substring(6, headerAuth.length());
        }

        return null;
    }

//    public boolean validateOpaqueToken(String token) {
//
//    }
}
