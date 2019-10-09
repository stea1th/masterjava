package ru.javaops.masterjava.service.mail.dao;

import com.bertoncelj.jdbi.entitymapper.EntityMapperFactory;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.GetGeneratedKeys;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapperFactory;
import ru.javaops.masterjava.persist.dao.AbstractDao;
import ru.javaops.masterjava.service.mail.model.Mail;

@RegisterMapperFactory(EntityMapperFactory.class)
public abstract class MailDao implements AbstractDao {

    @SqlUpdate("INSERT INTO mail (to_list, cc_list, subject, body, sent_date, is_successful) VALUES" +
            " (:toList, :ccList, :subject, :body, :sentDate, :isSuccessful) ")
    @GetGeneratedKeys
    public abstract int insertGeneratedId(@BindBean Mail mail);

    @Override
    @SqlUpdate("TRUNCATE mail CASCADE")
    public abstract void clean();
}
