package ru.javaops.masterjava.persist.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor

public class City {

    private @NonNull String id;
    private @NonNull String name;

    public City(String id, String name) {
        if(id.length() > 4) {
            throw new IllegalArgumentException("Not longer than 4");
        }
        this.id = id;
        this.name = name;
    }
}
