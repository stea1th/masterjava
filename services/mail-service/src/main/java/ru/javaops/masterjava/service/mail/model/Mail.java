package ru.javaops.masterjava.service.mail.model;

import com.bertoncelj.jdbi.entitymapper.Column;
import lombok.*;
import ru.javaops.masterjava.persist.model.BaseEntity;
import ru.javaops.masterjava.service.mail.Addressee;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class Mail extends BaseEntity {

    @Column("to_list")
    private String toList;

    @Column("cc_list")
    private String ccList;

    private String subject;
    private String body;

    @Column("sent_date")
    private LocalDateTime sentDate;

    @Column(value="is_successful")
    @NonNull
    private Boolean isSuccessful = true;
}
