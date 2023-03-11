package com.backend.se_project_backend;

import com.backend.se_project_backend.repository.BikeRepository;
import com.backend.se_project_backend.service.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;

@SpringBootApplication(exclude={DataSourceAutoConfiguration.class})
public class SeProjectBackendApplication {

    @RequestMapping
    public static void main(String[] args) {

        SpringApplication.run(SeProjectBackendApplication.class, args);
    }

}
