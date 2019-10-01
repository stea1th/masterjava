package ru.javaops.masterjava.persist.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@EqualsAndHashCode
public class City {

    private @NonNull String id;
    private @NonNull String name;

    public City(String id, String name) {
        this.id = id;
        this.name = name;
    }
}
