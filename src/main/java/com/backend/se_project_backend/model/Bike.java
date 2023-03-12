package com.backend.se_project_backend.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.*;
import java.util.Objects;

@Document(collection = "bikes")
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Bike bike = (Bike) o;
        return available == bike.available && usable == bike.usable && id.equals(bike.id) && Objects.equals(rating, bike.rating);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, available, usable, rating);
    }
}
