package com.backend.se_project_backend.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.*;

@Document
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Bike {
    @Id
    private String id;

    private boolean available;

    private boolean usable;

    private Double rating;
}
