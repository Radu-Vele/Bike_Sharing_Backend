package com.backend.se_project_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;

@SpringBootApplication
public class SeProjectBackendApplication {

    @RequestMapping
    public static void main(String[] args) {
        //RecommenderService recommenderService = new RecommenderServiceImpl();
        //System.out.print(recommenderService.dijkstra(1, 12));
        SpringApplication.run(SeProjectBackendApplication.class, args);
    }

}
