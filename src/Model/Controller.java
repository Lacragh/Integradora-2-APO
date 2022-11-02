package Model;

import Exceptions.FormatIncorrect;
import Exceptions.IDused;
import Exceptions.NotFoundCountryID;
import UI.Main;
import com.google.gson.Gson;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;

public class Controller {

    private final String comandInsert = "INSERT INTO";
    private final String comandSelect = "SELECT * FROM";
    private final String comandDelete = "DELETE FROM";
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
                throw new FormatIncorrect();
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

                if (searchCountries(arrayValues[2], null, null) != null) {
                    try {

                        int count = 0;
                        for (int i = 0; i < cities.size(); i++) {
                            if (arrayValues[0].equals(cities.get(i).getId())) {
                                count += 1;
                            }
                        }

                        if(count == 0){
                            cities.add(new City(arrayValues[0], arrayValues[1], arrayValues[2], Double.parseDouble(arrayValues[3])));

                            FileOutputStream fos = new FileOutputStream("Cities.SQL");
                            Gson gson = new Gson();
                            String json = gson.toJson(cities);
                            fos.write(json.getBytes(StandardCharsets.UTF_8));
                            fos.close();
                        }else{
                            throw new IDused();
                        }

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

    public Country searchCountries(String id, String name, String countryCode) {

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
        if (countryCode != null) {
            for (Country country : countries) {
                if (country.getCountryCode().equals(countryCode)) {
                    return country;
                }
            }
        }

        return null;
    }

    public City searchCities(String id, String name) {

        if (name != null) {
            for (City city : cities) {
                if (city.getName().equals(name)) {
                    return city;
                }
            }

        }
        if (id != null) {
            for (City city : cities) {
                if (city.getId().equals(id)) {
                    return city;
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

    public void loadCountries() throws IOException {
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

    public void saveCities() throws IOException {
        FileOutputStream fos = new FileOutputStream("Cities.SQL");
        Gson gson = new Gson();
        String json = gson.toJson(cities);
        fos.write(json.getBytes(StandardCharsets.UTF_8));
        fos.close();
    }

    public void saveCountries() throws IOException {
        FileOutputStream fos = new FileOutputStream("Countries.SQL");
        Gson gson = new Gson();
        String json = gson.toJson(countries);
        fos.write(json.getBytes(StandardCharsets.UTF_8));
        fos.close();
    }

    public void loadSQL(String text) throws IOException {

        FileInputStream fis = new FileInputStream(text);
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(fis)
        );

        String line = "";
        while ((line = reader.readLine()) != null){
           Main.insertCommand(line);
        }
    }

    public String orderBy(String command) {
        String info = "";
        if (command.contains("<") && command.contains("countries") && command.contains("name")) {
            String[] orderby = command.split("<");
            if (orderby[1].contains("ORDER BY name")) {
                String[] orderby2 = orderby[1].split(" ");
                ArrayList<Country> countryOrder = new ArrayList<>();
                for (int i = 0; i < countries.size(); i++) {
                    if (countries.get(i).getPopulation() < Integer.parseInt(orderby2[1])) {
                        countryOrder.add(countries.get(i));
                    }
                }
                Collections.sort(countryOrder, (a, b) -> {
                    return a.getName().compareTo(b.getName());
                });
                for (int i = 0; i < countryOrder.size(); i++) {
                    info += countryOrder.get(i).toString();
                }
                return info;

            }


        }

        if (command.contains(">") && command.contains("countries")&& command.contains("name")) {
            String[] orderby = command.split(">");
            if (orderby[1].contains("ORDER BY name")) {
                String[] orderby2 = orderby[1].split(" ");
                ArrayList<Country> countryOrder = new ArrayList<>();
                for (int i = 0; i < countries.size(); i++) {
                    if (countries.get(i).getPopulation() > Integer.parseInt(orderby2[1])) {
                        countryOrder.add(countries.get(i));
                    }
                }
                Collections.sort(countryOrder, (a, b) -> {
                    return a.getName().compareTo(b.getName());
                });
                for (int i = 0; i < countryOrder.size(); i++) {
                    info += countryOrder.get(i).toString();
                }
                return info;

            }
        }

        if (command.contains("<") && command.contains("cities")&& command.contains("name")) {
            String[] orderby = command.split("<");
            if (orderby[1].contains("ORDER BY name")) {
                String[] orderby2 = orderby[1].split(" ");
                ArrayList<City> cityOrder = new ArrayList<>();
                for (int i = 0; i < cities.size(); i++) {
                    if (cities.get(i).getPopulation() < Integer.parseInt(orderby2[1])) {
                        cityOrder.add(cities.get(i));
                    }
                }
                Collections.sort(cityOrder, (a, b) -> {
                    return a.getName().compareTo(b.getName());
                });
                for (int i = 0; i < cityOrder.size(); i++) {
                    info += cityOrder.get(i).toString();
                }
                return info;

            }


        }

        if (command.contains(">") && command.contains("cities")&& command.contains("name")) {
            String[] orderby = command.split(">");
            if (orderby[1].contains("ORDER BY name")) {
                String[] orderby2 = orderby[1].split(" ");
                ArrayList<City> cityOrder = new ArrayList<>();
                for (int i = 0; i < cities.size(); i++) {
                    if (cities.get(i).getPopulation() > Integer.parseInt(orderby2[1])) {
                        cityOrder.add(cities.get(i));
                    }
                }
                Collections.sort(cityOrder, (a, b) -> {
                    return a.getName().compareTo(b.getName());
                });
                for (int i = 0; i < cities.size(); i++) {
                    info += cities.get(i).toString();
                }
                return info;

            }
        }

        //PAISES Y CIUDADES POR BY NAME

        if (command.contains("<") && command.contains("countries") && command.contains("population")) {
            String[] orderby = command.split("<");
            if (orderby[1].contains("ORDER BY name")) {
                String[] orderby2 = orderby[1].split(" ");
                ArrayList<Country> countryOrder = new ArrayList<>();
                for (int i = 0; i < countries.size(); i++) {
                    if (countries.get(i).getPopulation() < Integer.parseInt(orderby2[1])) {
                        countryOrder.add(countries.get(i));
                    }
                }
                Collections.sort(countryOrder, (a, b) -> {
                    if (a.getPopulation() < b.getPopulation()) {
                        return 1;
                    } else {
                        return -1;
                    }
                });
                for (int i = 0; i < countryOrder.size(); i++) {
                    info += countryOrder.get(i).toString();
                }
                return info;

            }


        }

        if (command.contains(">") && command.contains("countries")&& command.contains("population")) {
            String[] orderby = command.split(">");
            if (orderby[1].contains("ORDER BY name")) {
                String[] orderby2 = orderby[1].split(" ");
                ArrayList<Country> countryOrder = new ArrayList<>();
                for (int i = 0; i < countries.size(); i++) {
                    if (countries.get(i).getPopulation() > Integer.parseInt(orderby2[1])) {
                        countryOrder.add(countries.get(i));
                    }
                }
                Collections.sort(countryOrder, (a, b) -> {
                    if (a.getPopulation() < b.getPopulation()) {
                        return 1;
                    } else {
                        return -1;
                    }
                });
                for (int i = 0; i < countryOrder.size(); i++) {
                    info += countryOrder.get(i).toString();
                }
                return info;

            }
        }

        if (command.contains("<") && command.contains("cities")&& command.contains("population")) {
            String[] orderby = command.split("<");
            if (orderby[1].contains("ORDER BY name")) {
                String[] orderby2 = orderby[1].split(" ");
                ArrayList<City> cityOrder = new ArrayList<>();
                for (int i = 0; i < cities.size(); i++) {
                    if (cities.get(i).getPopulation() < Integer.parseInt(orderby2[1])) {
                        cityOrder.add(cities.get(i));
                    }
                }
                Collections.sort(cityOrder, (a, b) -> {
                    if (a.getPopulation() < b.getPopulation()) {
                        return 1;
                    } else {
                        return -1;
                    }
                });
                for (int i = 0; i < cityOrder.size(); i++) {
                    info += cityOrder.get(i).toString();
                }
                return info;

            }


        }

        if (command.contains(">") && command.contains("cities")&& command.contains("population")) {
            String[] orderby = command.split(">");
            if (orderby[1].contains("ORDER BY name")) {
                String[] orderby2 = orderby[1].split(" ");
                ArrayList<City> cityOrder = new ArrayList<>();
                for (int i = 0; i < cities.size(); i++) {
                    if (cities.get(i).getPopulation() > Integer.parseInt(orderby2[1])) {
                        cityOrder.add(cities.get(i));
                    }
                }
                Collections.sort(cityOrder, (a, b) -> {
                    if (a.getPopulation() < b.getPopulation()) {
                        return 1;
                    } else {
                        return -1;
                    }
                });
                for (int i = 0; i < cities.size(); i++) {
                    info += cities.get(i).toString();
                }
                return info;

            }
        }

        if (command.contains("=") && command.contains("name")) {
            String[] orderby = command.split("=");
            String[] orderby2 = orderby[1].split(" ");
            ArrayList<City> populationSort = new ArrayList<>();
            if (orderby[1].contains("ORDER BY population")) {
                String filter = orderby2[1].replaceAll(" ", "");
                for (int i = 0; i < cities.size(); i++) {
                    if (filter.equals(cities.get(i).getName().replaceAll(" ", ""))) {
                        populationSort.add(cities.get(i));
                    }
                }
                Collections.sort(populationSort, (a, b) -> {
                    return a.getName().compareTo(b.getName());
                });
                info += ("\nOrden de las ciudades por población:\n");
                for (int i = 0; i < populationSort.size(); i++) {
                    info += (populationSort.get(i).toString());
                }
                return info;
            }

        }

        if (command.contains("=") && command.contains("population")) {
            String[] orderby = command.split("=");
            String[] orderby2 = orderby[1].split(" ");
            ArrayList<City> populationSort = new ArrayList<>();
            if (orderby[1].contains("ORDER BY population")) {
                String filter = orderby2[1].replaceAll(" ", "");
                for (int i = 0; i < cities.size(); i++) {
                    if (filter.equals(cities.get(i).getName().replaceAll(" ", ""))) {
                        populationSort.add(cities.get(i));
                    }
                }
                Collections.sort(populationSort, (a, b) -> {
                    if (a.getPopulation() < b.getPopulation()) {
                        return 1;
                    } else {
                        return -1;
                    }
                });
                info += ("\nOrden de las ciudades por población:\n");
                for (int i = 0; i < populationSort.size(); i++) {
                    info += (populationSort.get(i).toString());
                }
                return info;
            }

        }

        return "";
    }


    public String select(String command) throws FormatIncorrect {

        //
        if (command.equals(comandSelect + " countries")) {
            return showCountries();
        } else if (command.equals(comandSelect + " cities")) {
            return showCities();
        }
        //

        if (command.startsWith(comandSelect + " countries WHERE population")) {
            if (command.startsWith(comandSelect + " countries WHERE population < ")) {
                String[] countries1 = command.split("<");
                double pop = Double.parseDouble(countries1[1]);
                return filter(pop, "2");

            } else if (command.startsWith(comandSelect + " countries WHERE population > ")) {
                String[] countries1 = command.split(">");
                double pop = Double.parseDouble(countries1[1]);
                return filter(pop, "1");

            } else if (command.startsWith(comandSelect + " countries WHERE population = ")) {
                String[] countries1 = command.split("=");

                double pop = Double.parseDouble(countries1[1]);
                return filter(pop, "=");

            } else {
                throw new FormatIncorrect();
            }
        } else if (command.startsWith(comandSelect + " cities WHERE population")) {
            if (command.startsWith(comandSelect + " cities WHERE population < ")) {
                String[] cities1 = command.split("<");
                double pop = Double.parseDouble(cities1[1]);
                return filter2(pop, "2");

            } else if (command.startsWith(comandSelect + " cities WHERE population > ")) {
                String[] cities1 = command.split(">");
                double pop = Double.parseDouble(cities1[1]);
                return filter2(pop, "1");

            } else if (command.startsWith(comandSelect + " cities WHERE population = ")) {
                String[] cities1 = command.split("=");

                double pop = Double.parseDouble(cities1[1]);
                return filter2(pop, "=");

            } else {
                throw new FormatIncorrect();
            }
        }

        if (command.startsWith(comandSelect + " countries WHERE name =")) {
            String[] countries1 = command.split("=");

            if ((searchCountries(null, countries1[1], null)) != null) {
                String info = "";

                for (int i = 0; i < countries.size(); i++) {
                    if (countries1[1].equals(countries.get(i).getName())) {
                        info += countries.get(i).toString();
                    }
                }
                return "The country exists:\n" + info;
            }
        } else if (command.startsWith(comandSelect + " cities WHERE name =")) {
            String[] cities1 = command.split("=");

            if ((searchCities(null, cities1[1])) != null) {
                String info = "";

                for (int i = 0; i < cities.size(); i++) {
                    if (cities1[1].equals(cities.get(i).getName())) {
                        info += cities.get(i).toString();
                    }
                }
                return "The city exists:\n" + info;
            }
        }

        if (command.startsWith(comandSelect + " countries WHERE countryCode =")) {
            String[] countries1 = command.split("=");

            if ((searchCountries(null, null, countries1[1])) != null) {
                String info = "";

                for (int i = 0; i < countries.size(); i++) {
                    if (countries1[1].equals(countries.get(i).getCountryCode())) {
                        info += countries.get(i).toString();
                    }
                }
                return "The country exists:\n" + info;
            }
        }

        if (command.startsWith(comandSelect + " countries WHERE id =")) {
            String[] countries1 = command.split("=");

            if ((searchCountries(null, null, countries1[1])) != null) {
                String info = "";

                for (int i = 0; i < countries.size(); i++) {
                    if (countries1[1].equals(countries.get(i).getId())) {
                        info = countries.get(i).toString();
                    }
                }
                return "The Country exists:\n" + info;
            }
        } else if (command.startsWith(comandSelect + " cities WHERE id =")) {
            String[] cities1 = command.split("=");

            if ((searchCities(cities1[1], null)) != null) {
                String info = "";

                for (int i = 0; i < cities.size(); i++) {
                    if (cities1[1].equals(cities.get(i).getId())) {
                        info = cities.get(i).toString();
                    }
                }
                return "The City exists:\n" + info;
            }
        }

        if (command.startsWith(comandSelect + " cities WHERE country =")) {
            String[] citiesOfCountries = command.split("=");
            String info = "";

            for (int i = cities.size() - 1; i >= 0; i--) {
                if (searchCountries(null, citiesOfCountries[1], null).getId().equals(cities.get(i).getCountryID())) {
                    info += cities.get(i).toString();
                }

            }

            return "The Cities exist:\n" + info;
        }

        return "";
    }

    public String filter(double population, String minMax) {

        String info = "";
        if (minMax.equals("1")) {
            //Mayor
            for (int i = 0; i < countries.size(); i++) {
                if (countries.get(i).getPopulation() > population) {
                    info += countries.get(i).toString();
                }
            }
        } else if (minMax.equals("2")) {
            //Menor
            for (int i = 0; i < countries.size(); i++) {
                if (countries.get(i).getPopulation() < population) {
                    info += countries.get(i).toString();
                }
            }
        } else if (minMax.equals("=")) {
            //Igual
            for (int i = 0; i < countries.size(); i++) {
                if (countries.get(i).getPopulation() == population) {
                    info += countries.get(i).toString();
                }
            }
        }

        return info;
    }

    public String filter2(double population, String minMax) {

        String info = "";
        if (minMax.equals("1")) {
            //Mayor
            for (int i = 0; i < cities.size(); i++) {
                if (cities.get(i).getPopulation() > population) {
                    info += cities.get(i).toString();
                }
            }
        } else if (minMax.equals("2")) {
            //Menor
            for (int i = 0; i < cities.size(); i++) {
                if (cities.get(i).getPopulation() < population) {
                    info += cities.get(i).toString();
                }
            }
        } else if (minMax.equals("=")) {
            //Igual
            for (int i = 0; i < cities.size(); i++) {
                if (cities.get(i).getPopulation() == population) {
                    info += cities.get(i).toString();
                }
            }
        }

        return info;
    }

    public String delete(String command) throws FormatIncorrect, IOException {

        //
        if (command.equals(comandDelete + " countries")) {
            String info = deleteCountries();
            deleteCities();

            saveCountries();
            saveCities();

            return info;
        } else if (command.equals(comandDelete + " cities")) {
            String info = deleteCities();

            saveCities();

            return info;
        }
        //

        if (command.startsWith(comandDelete + " countries WHERE population")) {
            if (command.startsWith(comandDelete + " countries WHERE population < ")) {
                String[] countries1 = command.split("<");
                double pop = Double.parseDouble(countries1[1]);
                String info = findDelete(pop, "2");

                saveCountries();
                saveCities();

                return info;

            } else if (command.startsWith(comandDelete + " countries WHERE population > ")) {
                String[] countries1 = command.split(">");
                double pop = Double.parseDouble(countries1[1]);
                String info = findDelete(pop, "1");

                for (int i = 0; i < countries.size(); i++) {
                    System.out.println(countries.get(i).toString());
                }

                saveCountries();
                saveCities();

                return info;

            } else if (command.startsWith(comandDelete + " countries WHERE population = ")) {
                String[] countries1 = command.split("=");

                double pop = Double.parseDouble(countries1[1]);
                String info = findDelete(pop, "=");

                saveCountries();
                saveCities();

                return info;

            } else {
                throw new FormatIncorrect();
            }
        } else if (command.startsWith(comandDelete + " cities WHERE population")) {
            if (command.startsWith(comandDelete + " cities WHERE population < ")) {
                String[] cities1 = command.split("<");
                double pop = Double.parseDouble(cities1[1]);
                String info = findDelete2(pop, "2");

                saveCities();

                return info;

            } else if (command.startsWith(comandDelete + " cities WHERE population > ")) {
                String[] cities1 = command.split(">");
                double pop = Double.parseDouble(cities1[1]);
                String info = findDelete2(pop, "1");

                saveCities();

                return info;

            } else if (command.startsWith(comandDelete + " cities WHERE population = ")) {
                String[] cities1 = command.split("=");

                double pop = Double.parseDouble(cities1[1]);
                String info = findDelete2(pop, "=");

                saveCities();

                return info;

            } else {
                throw new FormatIncorrect();
            }
        }

        if (command.startsWith(comandDelete + " countries WHERE name =")) {
            String[] countries1 = command.split("=");

            if ((searchCountries(null, countries1[1], null)) != null){

                deleteCitiesNotCountries(searchCountries(null, countries1[1], null));

                for (int i = 0; i < countries.size(); i++) {
                    if (countries1[1].equals(countries.get(i).getName())) {
                        countries.remove(i);
                    }
                }

                saveCountries();
                saveCities();

                return "Countries eliminated";
            }
        } else if (command.startsWith(comandDelete + " cities WHERE name =")) {
            String[] cities1 = command.split("=");

            if ((searchCities(null, cities1[1])) != null) {

                for (int i = 0; i < cities.size(); i++) {
                    if (cities1[1].equals(cities.get(i).getName())) {
                        cities.remove(i);
                    }
                }

                saveCities();

                return "Cities eliminated";
            }
        }

        if (command.startsWith(comandDelete + " countries WHERE id =")) {
            String[] countries1 = command.split("=");

            if ((searchCountries(null, null, countries1[1])) != null) {

                deleteCitiesNotCountries(searchCountries(null, null, countries1[1]));

                for (int i = 0; i < countries.size(); i++) {
                    if (countries1[1].equals(countries.get(i).getId())) {
                        countries.remove(i);
                    }
                }

                saveCountries();
                saveCities();

                return "Country eliminated";
            }
        } else if (command.startsWith(comandDelete + " cities WHERE id =")) {
            String[] cities1 = command.split("=");

            if ((searchCities(cities1[1], null)) != null) {

                for (int i = 0; i < cities.size(); i++) {
                    if (cities1[1].equals(cities.get(i).getId())) {
                        cities.remove(i);
                    }
                }

                saveCities();

                return "City eliminated";
            }
        }


        if (command.startsWith(comandDelete + " cities WHERE country =")) {
            String[] citiesOfCountries = command.split("=");

            for (int i = cities.size() - 1; i >= 0; i--) {
                if (searchCountries(null, citiesOfCountries[1], null).getId().equals(cities.get(i).getCountryID())) {
                    cities.remove(i);
                }

            }

            saveCities();

            return "Cities from " + citiesOfCountries[1].replaceAll(" ", "") + " eliminated";

        }

        return "";
    }

    public String deleteCountries() {

        String info = "";
        for (int i = countries.size() - 1; i >= 0; i--) {
            countries.remove(i);
            info = "Countries eliminated";
        }

        return info;
    }

    public String deleteCities() {

        String info = "";
        for (int i = cities.size() - 1; i >= 0; i--) {
            cities.remove(i);
            info = "Cities eliminated";
        }

        return info;
    }

    public String findDelete(double population, String minMax){

        String info = "";
        if (minMax.equals("1")) {
            //Mayor
            for (int i = countries.size() - 1; i >= 0; i--){
                if (countries.get(i).getPopulation() > population){
                    deleteCitiesNotCountries(countries.get(i));
                    countries.remove(i);
                    info = "Countries eliminated";
                }
            }
        } else if (minMax.equals("2")) {
            //Menor
            for (int i = countries.size() - 1; i >= 0; i--) {
                if (countries.get(i).getPopulation() < population){
                    deleteCitiesNotCountries(countries.get(i));
                    countries.remove(i);
                    info = "Countries eliminated";
                }
            }
        } else if (minMax.equals("=")) {
            //Igual
            for (int i = countries.size() - 1; i >= 0; i--) {
                if (countries.get(i).getPopulation() == population){
                    deleteCitiesNotCountries(countries.get(i));
                    countries.remove(i);
                    info = "Countries eliminated";
                }
            }
        }

        return info;
    }

    public String findDelete2(double population, String minMax){

        String info = "";
        if (minMax.equals("1")) {
            //Mayor
            for (int i = cities.size() - 1; i >= 0; i--) {
                if (cities.get(i).getPopulation() > population){
                    cities.remove(i);
                    info = "Cities eliminated";
                }
            }
        } else if (minMax.equals("2")) {
            //Menor
            for (int i = cities.size() - 1; i >= 0; i--){
                if (cities.get(i).getPopulation() < population){
                    cities.remove(i);
                    info = "Cities eliminated";
                }
            }
        } else if (minMax.equals("=")){
            //Igual
            for (int i = cities.size() - 1; i >= 0; i--){
                if (cities.get(i).getPopulation() == population){
                    cities.remove(i);
                    info = "Cities eliminated";
                }
            }
        }

        return info;
    }

    public void deleteCitiesNotCountries(Country country) {

        for (int i = cities.size() - 1; i >= 0; i--) {

            if (cities.get(i).getCountryID().equals(country.getId())) {
                cities.remove(i);
            }

        }
    }




}
