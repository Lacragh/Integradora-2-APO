package Model;
import Exceptions.FormatIncorrect;
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
    private Gson gson;

    public Controller(){
        gson = new Gson();
        countries = new ArrayList<>();
    }


    public boolean addCountry(String command) throws FormatIncorrect{

        if(command.startsWith(comandInsert+" countries(id, name, population, countryCode) VALUES")){
           String[] array = command.split("VALUES");
           String[] arrayValues = array[1].replaceAll("\\(","").replaceAll("\\)","").split(",");

            if (arrayValues[0].startsWith(" '") && arrayValues[0].endsWith("'") &&
                    arrayValues[1].startsWith(" '") && arrayValues[1].endsWith("'") &&
                    arrayValues[3].startsWith(" '") && arrayValues[3].endsWith("'")
            ){
                try {
                    loadCountries();
                    countries.add(new Country(
                            arrayValues[0],arrayValues[1],Double.parseDouble(arrayValues[2]) ,arrayValues[3]
                            ));

                    FileOutputStream fos = new FileOutputStream("Countries.SQL");
                    Gson gson = new Gson();
                    String json = gson.toJson(countries);
                    fos.write(json.getBytes(StandardCharsets.UTF_8));
                    fos.close();

                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }else{
                throw new FormatIncorrect();
            }



        }else{
            throw new FormatIncorrect();
        }
        return true;
    }


    public boolean addCities(String command) throws NotFoundCountryID, FormatIncorrect {

        if (command.startsWith(comandInsert+ " cities(id, name, countryID, population) VALUES")){
            String[] array = command.split("VALUES");
            String[] arrayValues = array[1].replaceAll("\\(","").replaceAll("\\)","").split(",");

            if(arrayValues[0].startsWith(" '") && arrayValues[0].endsWith("'") &&
            arrayValues[1].startsWith(" '") && arrayValues[1].endsWith("'") &&
            arrayValues[2].startsWith(" '") && arrayValues[2].endsWith("'")
            ){

                if(search(arrayValues[2])){
                    loadCountries();
                   // countries.add(new City(arrayValues[0],arrayValues[1],arrayValues[2],Double.parseDouble(arrayValues[3]));
                   // String json = gson.toJson(cities);
                    try {
                        FileOutputStream fos = new FileOutputStream(new File("Cities.SQL"));
                       // fos.write(json.getBytes(StandardCharsets.ISO_8859_1));
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else {
                    throw new NotFoundCountryID();
                }


            }else{
                throw new FormatIncorrect();
            }


        }else{
            throw new FormatIncorrect();
        }

        return false;
    }

    public boolean search(String id){


        for (int i = 0; i < countries.size(); i++) {
            if(countries.get(i).getId().equals(id)){
                return true;
            }
        }
        return false;
    }

    public City[] loadCities(){
        File file = new File("Cities.SQL");

        try {
            FileInputStream fis = new FileInputStream(file);
            BufferedReader reader = new BufferedReader(new InputStreamReader(fis));

            String json = "";
            String line;
            while ((line = reader.readLine()) != null){
                json += line;
            }
            fis.close();

            City[] cities = gson.fromJson(json,City[].class);

            return cities;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public ArrayList<Country> loadCountries(){
        File file = new File("Countries.SQL");

        try {
            FileInputStream fis = new FileInputStream(file);
            BufferedReader reader = new BufferedReader(new InputStreamReader(fis));

            String json = "";
            String line;
            while ((line = reader.readLine()) != null){
                json += line;
            }

            fis.close();

            Country[] country = gson.fromJson(json,Country[].class);
            for (int i = 0; i < country.length; i++) {
            countries.add(country[i]);
            }
            return countries;

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void loadMemory() throws IOException{
        FileInputStream fis = new FileInputStream("program.json");
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(fis)
        );

        String json = "";
        String line = "";
        while ((line = reader.readLine()) != null){
            json += line;
        }

        Gson gson = new Gson();
        Country[] data = gson.fromJson(json, Country[].class);
        for (int i=0 ; i<data.length ; i++){
            countries.add(data[i]);
        }
    }



}
