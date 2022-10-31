package Model;

import Exceptions.FormatIncorrect;
import Exceptions.IDused;
import Exceptions.NotFoundCountryID;
import com.google.gson.Gson;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;

public class Controller {

    private final String comandInsert = "INSERT INTO";
    private final String comandSelect = "SELECT * FROM";
    private static ArrayList<Country> countries;
    private static ArrayList<City> cities;

    public Controller() {
        countries = new ArrayList<>();
        cities = new ArrayList<>();
    }

    public boolean addCountry(String command) throws FormatIncorrect, IDused {

        if (command.startsWith(comandInsert + " countries(id, name, population, countryCode) VALUES")) {
            String[] array = command.split("VALUES");
            String[] arrayValues = array[1].replaceAll("\\(", "").replaceAll("\\)", "").split(",");

            if (arrayValues[0].startsWith(" '") && arrayValues[0].endsWith("'") &&
                    arrayValues[1].startsWith(" '") && arrayValues[1].endsWith("'") &&
                    arrayValues[3].startsWith(" '") && arrayValues[3].endsWith("'")) {

                try {
                    int count = 0;
                    for (int i = 0; i < countriesSize(); i++) {
                        if (arrayValues[0].equals(countries.get(i).getId())) {
                            count += 1;
                        }
                    }

                    if (count == 0) {
                        countries.add(new Country(
                                arrayValues[0], arrayValues[1], Double.parseDouble(arrayValues[2]), arrayValues[3]
                        ));

                        FileOutputStream fos = new FileOutputStream("Countries.SQL");
                        Gson gson = new Gson();
                        String json = gson.toJson(countries);
                        fos.write(json.getBytes(StandardCharsets.UTF_8));
                        fos.close();
                    } else {
                        throw new IDused();
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else {
                throw new  FormatIncorrect();
            }
        } else {
            throw new FormatIncorrect();
        }
        return true;
    }


    public boolean addCities(String command) throws NotFoundCountryID, FormatIncorrect {

        if (command.startsWith(comandInsert + " cities(id, name, countryID, population) VALUES")) {
            String[] array = command.split("VALUES");
            String[] arrayValues = array[1].replaceAll("\\(", "").replaceAll("\\)", "").split(",");

            if (arrayValues[0].startsWith(" '") && arrayValues[0].endsWith("'") &&
                    arrayValues[1].startsWith(" '") && arrayValues[1].endsWith("'") &&
                    arrayValues[2].startsWith(" '") && arrayValues[2].endsWith("'")
            ) {

                if (search(arrayValues[2], null) != null) {
                    try {
                        cities.add(new City(arrayValues[0], arrayValues[1], arrayValues[2], Double.parseDouble(arrayValues[3])));
                        FileOutputStream fos = new FileOutputStream("Cities.SQL");
                        Gson gson = new Gson();
                        String json = gson.toJson(cities);
                        fos.write(json.getBytes(StandardCharsets.UTF_8));
                        fos.close();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    throw new NotFoundCountryID();
                }


            } else {
                throw new FormatIncorrect();
            }


        } else {
            throw new FormatIncorrect();
        }

        return false;
    }

    public Country search(String id, String name) {

        if (name != null) {
            for (Country country : countries) {
                if (country.getName().equals(name)) {
                    return country;
                }
            }

        }
        if (id != null) {
            for (Country country : countries) {
                if (country.getId().equals(id)) {
                    return country;
                }
            }
        }

        return null;
    }

    public void loadCities() throws IOException {
        FileInputStream fis = new FileInputStream("Cities.SQL");
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(fis)
        );

        String json = "";
        String line = "";
        while ((line = reader.readLine()) != null) {
            json += line;
        }

        Gson gson = new Gson();
        City[] data = gson.fromJson(json, City[].class);
        if (data != null) {
            for (int i = 0; i < data.length; i++) {
                cities.add(data[i]);
            }
        }
    }

    public void loadCountries()  throws IOException{
        FileInputStream fis = new FileInputStream("Countries.SQL");
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(fis)
        );

        String json = "";
        String line = "";
        while ((line = reader.readLine()) != null) {
            json += line;
        }

        Gson gson = new Gson();
        Country[] data = gson.fromJson(json, Country[].class);
        if (data != null) {
            for (int i = 0; i < data.length; i++) {
                countries.add(data[i]);
            }
        }
    }

    public int countriesSize() {
        return countries.size();
    }

    public String showCountries() {
        StringBuilder info = new StringBuilder();
        for (Country country : countries) {
            info.append(country.toString());
        }

        return info.toString();
    }

    public String showCities() {
        StringBuilder info = new StringBuilder();
        for (City city : cities) {
            info.append(city.toString());
        }

        return info.toString();
    }


    /*
    private  boolean isNumeric(String cadena){
        try {
            cadena.trim();
            System.out.println(cadena);
            Integer.parseInt(cadena);
            return true;
        } catch (NumberFormatException nfe){
            return false;
        }
    }

     */

    public String filter(double population,String minMax){

        String info = "";
        if (minMax.equals("1")){
            //Mayor
            for (int i = 0; i < countries.size(); i++) {
                if(countries.get(i).getPopulation() > population){
                    info += countries.get(i).toString();
                }
            }
        }else if (minMax.equals("2")){
            //Menor
            for (int i = 0; i < countries.size(); i++) {
                if(countries.get(i).getPopulation() < population){
                    info += countries.get(i).toString();
                }
            }
        }else if (minMax.equals("=")) {
            //Igual
            for (int i = 0; i < countries.size(); i++) {
                if(countries.get(i).getPopulation() == population){
                    info += countries.get(i).toString();
                }
            }
        }

        return info;
    }

}
