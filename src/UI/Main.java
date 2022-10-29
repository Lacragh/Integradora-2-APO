package UI;

import Exceptions.FormatIncorrect;
import Exceptions.NotFoundCountryID;
import Model.Controller;
import Model.Country;

import javax.swing.*;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.UUID;

public class Main {

    public static Controller controller;
    public static Scanner reader;
    public static int countCountry;


    public static void main(String[] args) {
        menu();
        int exit = 0;

        while (exit == 0) {
            System.out.println("1. Insert Comand\n2. Import data from file .SQL\n3. Exit");
            int menu = reader.nextInt();

            switch (menu) {

                case 1:
                    System.out.println("What do you want to do?");
                    System.out.println("1. Add country/city");
                    System.out.println("2. Search country/city");
                    System.out.println("3. Filter country/city");
                    System.out.println("4. Delete country/city");
                    int choice = reader.nextInt();

                    switch (choice) {

                        case 1:

                            System.out.println("What do you want to add?");
                            System.out.println("1. Country");
                            System.out.println("2. City");
                            int choice2 = reader.nextInt();

                            switch (choice2) {

                                case 1:
                                    System.out.println("Please add the data of the country with the following " +
                                            "format\n-> INSERT INTO countries(id, name, population, countryCode) VALUES ('6ec3e8ec-3dd0-11ed-b878-0242ac120002', 'Colombia', 50.2, '+57')");
                                    String addCountry = reader.nextLine();
                                    addCountry = reader.nextLine();

                                    try {
                                        controller.addCountry(addCountry);
                                    } catch (FormatIncorrect e) {
                                        e.printStackTrace();
                                    }
                                    break;
                                case 2:
                                    if (controller.loadCountries().size() == 0) {
                                        System.out.println("There are no countries added yet!");
                                    } else {

                                        System.out.println("Please add the data of the city with the following" +
                                                " format\n-> INSERT INTO cities(id, name, countryID, population) VALUES ('e4aa04f6-3dd0-11ed-b878-0242ac120002', 'Cali', '6ec3e8ec-3dd0-11ed-b878-0242ac120002', 2.2)");
                                        String addCity = reader.nextLine();
                                        addCity = reader.nextLine();

                                        try {
                                            controller.addCities(addCity);
                                        } catch (NotFoundCountryID | FormatIncorrect e) {
                                            e.printStackTrace();
                                        }
                                    }

                                    break;

                            }
                            break;
                        case 2:

                            break;
                        case 3:

                            break;
                        case 4:

                            break;
                        case 5:

                            break;

                    }
                    break;
                case 2:

                    break;
                case 3:
                    System.out.println("Thanks for using this program🗺️");
                    exit++;
                    break;
                default:
                    System.out.println("Please choose a valid option!");
            }
        }

    }



    public static void menu() {
        String menu = ("                / |  / | -------------------------------\n" +
                "                | |__| |                               |\n" +
                "               /   O O\\__  Welcome to the geographic   |\n" +
                "              /          \\   information system        |\n" +
                "             /      \\     \\                            |\n" +
                "            /   _    \\     \\ ---------------------------\n" +
                "           /    |\\____\\     \\      ||\n" +
                "          /     | | | |\\____/      ||\n" +
                "         /       \\| | | |/ |     __||\n" +
                "        /  /  \\   -------  |_____| ||\n" +
                "       /   |   |           |       --|\n" +
                "       |   |   |           |_____  --|\n" +
                "       |  |_|_|_|          |     \\----\n" +
                "       /\\                  |\n" +
                "      / /\\        |        /\n" +
                "     / /  |       |       |\n" +
                " ___/ /   |       |       |\n" +
                "|____/    c_c_c_C/ \\C_c_c_c" +
                "");
        System.out.println(menu);
        reader = new Scanner(System.in);
        controller = new Controller();
    }
}
