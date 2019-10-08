package ru.javaops.masterjava.service.mail.dao;

import com.bertoncelj.jdbi.entitymapper.EntityMapperFactory;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlBatch;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapperFactory;
import ru.javaops.masterjava.service.mail.model.MailAddressee;

import java.util.List;

@RegisterMapperFactory(EntityMapperFactory.class)
public abstract class MailAddresseeDao {

    @SqlBatch("INSERT INTO mail_addressee (mail_id, addressee_id) VALUES (:mailId, :addresseeId) ")
    public abstract void insertBatch(@BindBean List<MailAddressee> mailAddressees);
}
