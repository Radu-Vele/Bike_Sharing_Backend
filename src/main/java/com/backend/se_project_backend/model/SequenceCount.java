package com.backend.se_project_backend.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;

@Document
@Getter
@Setter
public class SequenceCount {
    @Id
    private String id;

    private long sequence;
}
