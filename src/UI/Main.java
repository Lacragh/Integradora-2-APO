package UI;

import Exceptions.FormatIncorrect;
import Exceptions.IDused;
import Exceptions.NotFoundCountryID;
import Model.Controller;

import java.io.IOException;
import java.util.Scanner;

public class Main {

    public static Controller controller;
    public static Scanner reader;


    public static void main(String[] args) throws IOException {

        menu();
        int exit = 0;

        while (exit == 0) {
            System.out.println("1. Insert Comand\n2. Import data from file .SQL\n3. Exit");
            int menu = reader.nextInt();

            switch (menu) {

                case 1:
                    System.out.println("Please insert the command\n");
                    String command = reader.nextLine();
                    command = reader.nextLine();

                    insertCommand(command);
                    break;
                case 2:
                    System.out.println("Please type the name of archive SQL like this.(Ejem.SQL");
                    System.out.println("Datos1.SQL");
                    String sql = reader.next();
                    controller.loadSQL(sql);

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
        String menu = ("""
                                / |  / | -------------------------------
                                | |__| |                               |
                               /   O O\\__  Welcome to the geographic   |
                              /          \\   information system        |
                             /      \\     \\                            |
                            /   _    \\     \\ ---------------------------
                           /    |\\____\\     \\      ||
                          /     | | | |\\____/      ||
                         /       \\| | | |/ |     __||
                        /  /  \\   -------  |_____| ||
                       /   |   |           |       --|
                       |   |   |           |_____  --|
                       |  |_|_|_|          |     \\----
                       /\\                  |
                      / /\\        |        /
                     / /  |       |       |
                 ___/ /   |       |       |
                |____/    c_c_c_C/ \\C_c_c_c""");
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

    public static void insertCommand(String command) {

        if (command.startsWith("INSERT INTO countries(id, name, population, countryCode) VALUES")) {

            if (!proveUUID(command)) {
                System.out.println("The uuid format is incorrect!\n");
            } else {
                try {
                    controller.addCountry(command);
                } catch (FormatIncorrect | IDused e) {
                    e.printStackTrace();
                }
            }

        } else if (command.startsWith("INSERT INTO cities(id, name, countryID, population) VALUES")) {
            try{
                if (!proveUUID(command)) {
                    System.out.println("The uuid format is incorrect!\n");
                } else {
                    try {
                        controller.addCities(command);
                    } catch (NotFoundCountryID | FormatIncorrect e) {
                        e.printStackTrace();
                    }
                }
            }catch (FormatIncorrect e){
                e.printStackTrace();
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
                e.printStackTrace();
            }

        } else if (command.startsWith("SELECT * FROM cities")) {

            try {
                if (!controller.select(command).equals("")) {
                    System.out.println(controller.select(command));
                } else {
                    System.out.println("There are no cities with those specifications");
                }
            } catch (FormatIncorrect e) {
                e.printStackTrace();
            }

        } else if (command.startsWith("DELETE FROM countries")) {

            try {
                String info = controller.delete(command);
                if (!info.equals("")) {
                    System.out.println(info);
                } else {
                    System.out.println("There are no countries with those specifications");
                }

            } catch (FormatIncorrect | IOException e) {
                e.printStackTrace();
            }
        } else if (command.startsWith("DELETE FROM cities")) {

            try {
                String info = controller.delete(command);
                if (!info.equals("")) {
                    System.out.println(info);
                } else {
                    System.out.println("There are no cities with those specifications");
                }

            } catch (FormatIncorrect | IOException e) {
                e.printStackTrace();
            }
        } else {

            System.out.println("The command that you inserted doesn't exist!");

        }

    }

    public static boolean proveUUID(String command) throws FormatIncorrect {
        try {
            int count = 0;
            String[] commandSplit = command.split("VALUES");
            String[] commandSplit2 = commandSplit[1].split(",");
            char[] uuidCommand = commandSplit2[0].replaceAll("\\(", "").replaceAll(" ", "").toCharArray();
            String a = commandSplit2[0].replaceAll("\\(", "").replaceAll(" ", "");
            String[] commandSplit3 = a.split("'");
            String commandSplit4 = commandSplit3[1].replaceAll("-", "");

            for (int i = 0; i < uuidCommand.length; i++) {
                if (uuidCommand[i] == '-') {
                    count++;
                }
            }
            if (count != 4 || commandSplit4.length() != 32) {
                return false;
            }

            return true;

        } catch (ArrayIndexOutOfBoundsException e) {
            throw new FormatIncorrect();
        }

    }

}
