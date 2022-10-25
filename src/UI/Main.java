package UI;

import Model.Controller;
import Model.Country;

import java.util.Scanner;

public class Main {

    public static Controller controller;
    public static Scanner reader;

    public static void main(String[] args) {
        menu();

        System.out.println("1. Insert Comand\n2. Import data from file .SQL\n3. Exit");
        int menu = reader.nextInt();

        controller.addCountry("INSERT INTO countries(id, name, population, countryCode) VALUES ('6ec3e8ec-3dd0-11ed-b878-0242ac120002', 'Colombia', 50.2, '+57')");
        controller.load();


    }

    public static void  menu(){
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
