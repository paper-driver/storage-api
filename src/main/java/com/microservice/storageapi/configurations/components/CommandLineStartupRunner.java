package com.microservice.storageapi.configurations.components;

import com.microservice.storageapi.services.MediaManagerStorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class CommandLineStartupRunner implements CommandLineRunner {
    private static final Logger logger = LoggerFactory.getLogger(CommandLineStartupRunner.class);

    @Resource
    MediaManagerStorageService mediaManagerStorageService;

    @Override
    public void run(String... arg) throws Exception {
        mediaManagerStorageService.init();
        logger.info("Initialized storage");
    }
}
