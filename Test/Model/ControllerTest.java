package Model;

import Exceptions.IDused;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class ControllerTest {

    public static Controller controller;

    public void stage1() {

        controller = new Controller();
    }

    public void stage2(){
        stage1();
        controller.addCountry("INSERT INTO countries(id, name, population, countryCode) VALUES ('6ec3e8ec-3dd0-11ed-b878-0242ac120002', 'Colombia', 50.2, '+57')");
        controller.addCities("INSERT INTO cities(id, name, countryID, population) VALUES ('e4aa04f6-3dd0-11ed-b878-0242ac120002', 'Cali', '6ec3e8ec-3dd0-11ed-b878-0242ac120002', 2.2)");
    }

    public void stage3(){
        stage1();
        controller.addCountry("INSERT INTO countries(id, name, population, countryCode) VALUES ('6ec3e8ec-3dd0-11ed-b878-0242ac120002', 'Colombia', 50.2, '+57')");
        controller.addCities("INSERT INTO cities(id, name, countryID, population) VALUES ('e4aa04f6-3dd0-11ed-b878-0242ac120002', 'Cali', '6ec3e8ec-3dd0-11ed-b878-0242ac120002', 2.2)");
        controller.addCities("INSERT INTO cities(id, name, countryID, population) VALUES ('e4bb24f6-3dd0-11ed-b878-0242ac120002', 'Medellin', '6ec3e8ec-3dd0-11ed-b878-0242ac120002', 2.2)");
        controller.addCities("INSERT INTO cities(id, name, countryID, population) VALUES ('e4aay156-3dd0-11ed-b878-0242ac120002', 'Bogota', '6ec3e8ec-3dd0-11ed-b878-0242ac120002', 2.2)");
        controller.addCities("INSERT INTO cities(id, name, countryID, population) VALUES ('e4aag156-3dd0-11ed-b878-0242ac120002', 'Buga', '6ec3e8ec-3dd0-11ed-b878-0242ac120002', 1)");
    }

    public void stage4(){
        stage1();
        stage3();
        controller.addCountry("INSERT INTO countries(id, name, population, countryCode) VALUES ('6ec3o9ec-3dd0-11ed-b878-0242ac120002', 'Afganistan', 50.2, '+57')");


    }

    @Test
    public void insertACountryTest1() {
        stage1();
        controller.addCountry("INSERT INTO countries(id, name, population, countryCode) VALUES ('6ec3e8ec-3dd0-11ed-b878-0242ac120002', 'Colombia', 50.2, '+57')");
        assertEquals(" 'Colombia'", controller.searchCountries(null, " 'Colombia'", null).getName());
    }

    @Test
    public void insertACityTest2() {
        stage1();
        controller.addCountry("INSERT INTO countries(id, name, population, countryCode) VALUES ('6ec3e8ec-3dd0-11ed-b878-0242ac120002', 'Colombia', 50.2, '+57')");
        controller.addCities("INSERT INTO cities(id, name, countryID, population) VALUES ('e4aa04f6-3dd0-11ed-b878-0242ac120002', 'Cali', '6ec3e8ec-3dd0-11ed-b878-0242ac120002', 2.2)");

        assertEquals(" 'Colombia'", controller.searchCountries(null, " 'Colombia'", null).getName());
        assertEquals(" 'Cali'", controller.searchCities(null, " 'Cali'").getName());

    }

    @Test
    public void insertTest3OfAddingACountryWithTheSameID() {
        stage2();

        boolean exceptionThrow = false;

        try {
            controller.addCountry("INSERT INTO countries(id, name, population, countryCode) VALUES ('6ec3e8ec-3dd0-11ed-b878-0242ac120002', 'Afganistan', 85, '+94')");
        } catch (IDused e) {
            e.printStackTrace();
            exceptionThrow = true;
        }

        assertTrue(exceptionThrow);
    }

    @Test
    public void insertTest4OfAddingAcountryWithTheSameID() {
        stage2();

        boolean exceptionThrow = false;

        try {
            controller.addCities("INSERT INTO cities(id, name, countryID, population) VALUES ('e4aa04f6-3dd0-11ed-b878-0242ac120002', 'Medellin', '6ec3e8ec-3dd0-11ed-b878-0242ac120002', 2.2)");

        } catch (IDused e) {
            e.printStackTrace();
            exceptionThrow = true;
        }

        assertTrue(exceptionThrow);
    }

    @Test
    public void selectTest1ToShowAllCitiesFromACountry(){
        stage3();

        assertEquals("""
                The Cities exist:
                ----------------
                Name:  'Buga'
                Population: 1.0
                --------------------------------
                Name:  'Bogota'
                Population: 2.2
                --------------------------------
                Name:  'Medellin'
                Population: 2.2
                --------------------------------
                Name:  'Cali'
                Population: 2.2
                ----------------""",controller.select("SELECT * FROM cities WHERE country = 'Colombia'"));

    }

    @Test
    public void selectTest2VerifyThatTheProgramCanOrganizeByName(){
        stage3();
        

        assertEquals("""
                ----------------
                Name:  'Bogota'
                Population: 2.2
                --------------------------------
                Name:  'Cali'
                Population: 2.2
                --------------------------------
                Name:  'Medellin'
                Population: 2.2
                ----------------""",controller.orderBy("SELECT * FROM cities WHERE population > 2 ORDER BY name"));


    }

    @Test
    public void selectTest3(){
        stage3();

        controller.addCountry("INSERT INTO countries(id, name, population, countryCode) VALUES ('6ec3e8ec-3dd0-11ed-b878-0242ac120007', 'Mexico', 50.2, '+57')");
        controller.addCountry("INSERT INTO countries(id, name, population, countryCode) VALUES ('6ec3e8ec-3dd0-11ed-b878-0242ac1200010', 'Argentina', 52.2, '+57')");
        assertEquals("----------------\n" +
                "Name:  'Colombia'\n" +
                "Population: 50.2\n" +
                "Country Code:  '+57'\n" +
                "--------------------------------\n" +
                "Name:  'Mexico'\n" +
                "Population: 50.2\n" +
                "Country Code:  '+57'\n" +
                "--------------------------------\n" +
                "Name:  'Argentina'\n" +
                "Population: 52.2\n" +
                "Country Code:  '+57'\n" +
                "----------------",controller.select("SELECT * FROM countries WHERE population > 2.0"));
    }


    @Test
    public void deleteTest1(){
        stage4();

        try {
            controller.delete("DELETE FROM countries WHERE id = '6ec3e8ec-3dd0-11ed-b878-0242ac120002'");
            assertEquals(null,controller.searchCountries("'6ec3e8ec-3dd0-11ed-b878-0242ac120002'",null,null));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Test
    public void deleteTest2(){
        stage4();

        assertEquals(" 'Colombia'", controller.searchCountries(null, " 'Colombia'", null).getName());
        assertEquals(" 'Cali'", controller.searchCities(null, " 'Cali'").getName());
        assertEquals(" 'Medellin'", controller.searchCities(null, " 'Medellin'").getName());
        assertEquals(" 'Bogota'", controller.searchCities(null, " 'Bogota'").getName());

        try {
            controller.delete("DELETE FROM cities WHERE country = 'Colombia'");
            assertNull(controller.searchCities(null, " 'Cali'"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}