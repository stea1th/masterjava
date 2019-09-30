package ru.javaops.masterjava.persist.dao;

import com.bertoncelj.jdbi.entitymapper.EntityMapperFactory;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapperFactory;
import ru.javaops.masterjava.persist.model.City;

@RegisterMapperFactory(EntityMapperFactory.class)
public abstract class CityDao implements AbstractDao  {

    @SqlUpdate("INSERT INTO city (id, name) VALUES (:id, :name) ")
    abstract int insertGeneratedId(@BindBean City city);

}
