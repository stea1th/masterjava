package ru.javaops.masterjava.persist.dao;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.javaops.masterjava.persist.CityTestData;
import ru.javaops.masterjava.persist.UserTestData;
import ru.javaops.masterjava.persist.model.City;

import static org.junit.Assert.assertEquals;

public class CityDaoTest extends AbstractDaoTest<CityDao> {

    public CityDaoTest() {
        super(CityDao.class);
    }

    @BeforeClass
    public static void init() {
        CityTestData.init();
        UserTestData.init();
    }

    @Before
    public void setUp() {
        CityTestData.setUp();
        UserTestData.setUp();
    }

    @Test
    public void getByUserEmailTest(){
        City city = dao.getByUserEmail("admin@javaops.ru");
        assertEquals("spb ", city.getId());
    }
}