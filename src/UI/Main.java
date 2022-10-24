package UI;

import Model.Controller;
import Model.Country;

import java.util.Scanner;

public class Main {

    public static Controller controller;
    public static Scanner reader;

    public static void main(String[] args) {




    }

    public static void  menu(){
        String menu = ("                / |  / | --------------------------\n" +
                "                | |__| |                          |\n" +
                "               /   O O\\__  I have a horny little  |\n" +
                "              /          \\   operating system     |\n" +
                "             /      \\     \\                       |\n" +
                "            /   _    \\     \\ ----------------------\n" +
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
    }
}
