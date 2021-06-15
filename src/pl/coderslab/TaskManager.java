package pl.coderslab;

import org.apache.commons.lang3.ArrayUtils;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class TaskManager {

    private static final List<String> menu = Arrays.asList(
            ConsoleColors.BLUE + "Please select an option:",
            ConsoleColors.RESET + "1. add",
            "2. remove",
            "3. list",
            "4. exit");
    private static final String fileName = "tasks.csv";
    private static final Path path = Paths.get(fileName);
    private static String[][] tasks = getTasksFromFile();

    public static void main(String[] args) {
        if (tasks.length == 0){
            System.out.println(ConsoleColors.BLUE_BOLD + "Hello to the TaskManager! This application will allow you to manage your tasks in a simple way.");
        }
        boolean loop = true;
        while (loop) {
            showMainMenu();
            Scanner scanner = new Scanner(System.in);
            String input = scanner.nextLine();
            switch (input) {
                case "1":
                case "add": {
                    addTask(scanner);
                    break;
                }
                case "2":
                case "remove": {
                    removeTask(scanner);
                    break;
                }
                case "3":
                case "list": {
                    listTasks();
                    break;
                }
                case "4":
                case "exit": {
                    System.out.println(ConsoleColors.RED + "Bye, bye.");
                    loop = false;
                    break;
                }
            }
        }
    }

    private static void showMainMenu() {
        for (int i = 0; i < menu.size(); i++) {
            System.out.println(menu.get(i));
        }
    }

    private static void addTask(Scanner scanner) {
        boolean loop = true;
        String description = "";
        while (loop){
            System.out.println("Please add task description:");
            description = scanner.nextLine();
            if (description.contains(""+(char)168)){
                description = "";
                System.out.println("Description contains an illegal character, please try again.");
            }else{
                loop = false;
            }
        }
        System.out.println("Please add task due date");
        String date = getValidDateFromUser(scanner);
        String important = "";
        loop = true;
        while (loop){
            System.out.println("Is your task important: true/false");
            important = scanner.nextLine();
            if (important.contains("true") || important.contains("false")){
                loop = false;
            }
        }
        int newLength = tasks.length + 1;
        tasks = Arrays.copyOf(tasks, newLength);
        String[] newLine = {description, date, important};
        tasks[newLength - 1] = newLine;
        saveToFile(tasks);
    }

    private static String[][] getTasksFromFile() {
        createFile();
        String[][] fileTasks = new String[0][0];
        List<String> list = null;
        try {
            list = Files.readAllLines(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < list.size(); i++) {
            fileTasks = Arrays.copyOf(fileTasks, fileTasks.length + 1);
            String[] line = list.get(i).split(Character.toString((char)168));
            fileTasks[i] = line;
        }
        return fileTasks;
    }

    private static void createFile() {
        if (!Files.exists(path)) {
            try {
                Files.createFile(path);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void saveToFile(String[][] tasks) {
        String[] line = new String[tasks.length];
        for (int i = 0; i < tasks.length; i++) {
            line[i] = String.join(Character.toString((char)168), tasks[i]);
        }

        try {
            Files.write(path, Arrays.asList(line));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private static void listTasks() {
        if (tasks.length == 0){
            System.out.println("You don't have any tasks added.");
        }else{
            for (int i = 0; i < tasks.length; i++) {
                System.out.print(i + ": ");
                for (int j = 0; j < tasks[i].length; j++) {
                    System.out.print(tasks[i][j]);
                    if (j != tasks[i].length - 1) {
                        System.out.print(" | ");
                    } else {
                        System.out.println("");
                    }
                }
            }
        }
    }

    private static void removeTask(Scanner scanner) {
        if (tasks.length == 0){
            System.out.println("You don't have any tasks to remove.");
        }else{
            System.out.println("Please select number to remove.");
            int index = getValidNumberFromUser(scanner);
            while (index < 0 || index > tasks.length - 1) {
                System.out.println("Index out of bound, enter valid index");
                index = getValidNumberFromUser(scanner);
            }
            tasks = ArrayUtils.remove(tasks, index);
            saveToFile(tasks);
        }
    }

    private static int getValidNumberFromUser(Scanner scanner) {
        while (!scanner.hasNextInt()) {
            scanner.next();
            System.out.println("Not a number, please enter a number!");
        }
        return scanner.nextInt();
    }

    private static String getValidDateFromUser (Scanner scanner) {
        System.out.println("Use dd-MM-yyyy format");
        String stringDate = scanner.nextLine();
        while (!isValidDate(stringDate)) {
            System.out.println("Please use correct date and format!");
            stringDate = scanner.nextLine();
        }
        return stringDate;
    }

    public static boolean isValidDate(String date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        dateFormat.setLenient(false);
        try {
            dateFormat.parse(date.trim());
        } catch (ParseException pe) {
            return false;
        }
        return true;
    }
}
