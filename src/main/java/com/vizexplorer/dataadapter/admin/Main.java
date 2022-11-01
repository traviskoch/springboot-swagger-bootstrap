package com.vizexplorer.dataadapter.admin;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "DataAdapter Admin API", version = "1.0.0", description = "DataAdapter Admin Service"))
public class Main {

    private static final Logger LOG = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        LOG.info("DataAdapter admin server demo started");
        ConfigurableApplicationContext run = SpringApplication.run(Main.class, args);
        run.registerShutdownHook();
    }

}
