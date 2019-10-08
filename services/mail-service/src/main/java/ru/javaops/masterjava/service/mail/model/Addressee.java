package ru.javaops.masterjava.service.mail.model;


import lombok.*;
import ru.javaops.masterjava.persist.model.BaseEntity;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class Addressee extends BaseEntity {

    private String email;
    private String name;

}
