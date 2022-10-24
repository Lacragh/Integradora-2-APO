package Model;

import java.util.UUID;

public class Country {
    private String id;
    private String name;
    private String population;

    public Country(String name, String population, String countryCode) {
        this.id = String.valueOf(UUID.randomUUID());
        this.name = name;
        this.population = population;
        this.countryCode = countryCode;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPopulation() {
        return population;
    }

    public void setPopulation(String population) {
        this.population = population;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    private String countryCode;
}
