package ru.javaops.masterjava.persist.dao;

import com.bertoncelj.jdbi.entitymapper.EntityMapperFactory;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapperFactory;
import ru.javaops.masterjava.persist.model.City;

@RegisterMapperFactory(EntityMapperFactory.class)
public abstract class CityDao implements AbstractDao  {

    @SqlUpdate("INSERT INTO city (id, name) VALUES (:id, :name) ON CONFLICT DO NOTHING ")
    public abstract int insert(@BindBean City city);

    @SqlUpdate("TRUNCATE city CASCADE")
    @Override
    public abstract void clean();

    @SqlQuery("SELECT c.* FROM city c LEFT JOIN users u ON c.id=u.city_id WHERE u.email = :email ")
    public abstract City getByUserEmail(@Bind("email") String email);

}
