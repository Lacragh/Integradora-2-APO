package UI;

import Exceptions.FormatIncorrect;
import Exceptions.IDused;
import Exceptions.NotFoundCountryID;
import Model.Controller;
import Model.Country;

import java.io.FileNotFoundException;
import java.io.IOException;
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
                    insertCommand();
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
        try {
            controller.loadCountries();
            controller.loadCities();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void insertCommand() {

        System.out.println("Please insert the command\n");
        String command = reader.nextLine();
        command = reader.nextLine();
        if (command.startsWith("INSERT INTO countries(id, name, population, countryCode) VALUES")) {

            try {
                controller.addCountry(command);
                System.out.println(controller.showCountries());
            } catch (FormatIncorrect | IDused e) {
                e.printStackTrace();
            }

        } else if (command.startsWith("INSERT INTO cities(id, name, countryID, population) VALUES")) {

            try {
                controller.addCities(command);
            } catch (NotFoundCountryID | FormatIncorrect e) {
                throw new RuntimeException(e);
            }

        } else if (command.contains("ORDER BY")) {
            System.out.println(controller.orderBy(command));

        } else if (command.startsWith("SELECT * FROM countries")) {

            try {
                if (!controller.select(command).equals("")) {
                    System.out.println(controller.select(command));
                } else {
                    System.out.println("There are no countries with those specifications");
                }
            } catch (FormatIncorrect e) {
                throw new RuntimeException(e);
            }

        } else if (command.startsWith("SELECT * FROM cities")) {

            try {
                if (!controller.select(command).equals("")) {
                    System.out.println(controller.select(command));
                } else {
                    System.out.println("There are no cities with those specifications");
                }
            } catch (FormatIncorrect e) {
                throw new RuntimeException(e);
            }

        } else if (command.startsWith("DELETE FROM countries")) {

            try {
                String info = controller.delete(command);
                if(!info.equals("")){
                    System.out.println(info);
                }else{
                    System.out.println("There are no countries with those specifications");
                }

            } catch (FormatIncorrect | IOException e) {
                throw new RuntimeException(e);
            }
        }else if (command.startsWith("DELETE FROM cities")) {

            try {
                String info = controller.delete(command);
                if(!info.equals("")){
                    System.out.println(info);
                }else{
                    System.out.println("There are no cities with those specifications");
                }

            } catch (FormatIncorrect | IOException e) {
                throw new RuntimeException(e);
            }
        }else{

            System.out.println("The command that you inserted doesn't exist!");

        }

    }

}
