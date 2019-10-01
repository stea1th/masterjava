package ru.javaops.masterjava.persist;

import com.google.common.collect.ImmutableList;
import ru.javaops.masterjava.persist.dao.CityDao;
import ru.javaops.masterjava.persist.model.City;

import java.util.List;

public class CityTestData {

    public static City SPB;
    public static City KIV;
    public static City MOV;
    public static City MNSK;
    public static List<City> CITIES;
    public static CityDao DAO;

    public static void init() {
        DAO = DBIProvider.getDao(CityDao.class);
        SPB = new City("spb", "Санкт-Петербург");
        KIV = new City("kiv", "Киев");
        MOV = new City("mov", "Москва");
        MNSK = new City("mnsk", "Минск");
        CITIES = ImmutableList.of(SPB, KIV, MOV, MNSK);
    }

    public static void setUp() {

        DAO.clean();
        DBIProvider.getDBI().useTransaction((conn, status) -> {
            CITIES.forEach(DAO::insert);
        });
    }
}
