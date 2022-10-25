package Model;
import com.google.gson.Gson;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;

public class Controller {

    private final String comandInsert = "INSERT INTO";
    private final String comandSelect = "SELECT * FROM";
    private Gson gson;

    public Controller(){
        gson = new Gson();
    }


    public boolean addCountry(String command){

        if(command.startsWith(comandInsert+" countries")){
           String[] array = command.split("VALUES");
           String[] arrayValues = array[1].replaceAll("\\(","").replaceAll("\\)","").split(",");
           Country[] country = {new Country(arrayValues[0],arrayValues[1],Double.parseDouble(arrayValues[2]) ,arrayValues[3])};
            String json = gson.toJson(country);
            try {
                FileOutputStream fos = new FileOutputStream(new File("array.elbicho"));
                fos.write(json.getBytes(StandardCharsets.ISO_8859_1));
                fos.close();
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return true;
    }

    public boolean addCities(String command){
        if (command.startsWith(comandInsert+ "cities")){
            String[] array = command.split("VALUES");
            String[] arrayValues = array[1].replaceAll("\\(","").replaceAll("\\)","").split(",");

            if(search(arrayValues[2])){
                City[] cities = {new City(arrayValues[0],arrayValues[1],arrayValues[2],Integer.parseInt(arrayValues[3]))};
                String json = gson.toJson(cities);
                try {
                    FileOutputStream fos = new FileOutputStream(new File("array.elbicho"));
                    fos.write(json.getBytes(StandardCharsets.ISO_8859_1));
                    fos.close();
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }else {
                return false;
            }

        }

        return false;
    }

    public boolean search(String id){
        Country[] countries = loadCountries();

        for (int i = 0; i < countries.length; i++) {
            if(countries[i].getCountryCode().equals(id)){
                return true;
            }
        }
        return false;
    }

    public City[] loadCities(){

    }

    public Country[] loadCountries(){
        File file = new File("array.elbicho");

        try {
            FileInputStream fis = new FileInputStream(file);
            BufferedReader reader = new BufferedReader(new InputStreamReader(fis));

            String json = "";
            String line;
            while ((line = reader.readLine()) != null){
                json += line;
            }
            System.out.println(json);
            fis.close();

            Country[] country = gson.fromJson(json,Country[].class);
            return country;

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }







}
