package com.ai21.Assignment1.integration;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;

public class TestContainerInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
    public static final int MARIADB_PORT = 3306;

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
//        DockerComposeContainer container = new
    }
}
