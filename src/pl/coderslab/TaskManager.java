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
    private static final String fileName = "tasks.csv";
    private static final Path path = Paths.get(fileName);
    private static String[][] tasks = getTasksFromFile();

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
            String[] linia = list.get(i).split(",");
            fileTasks[i] = linia;
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

    public static void main(String[] args) {
        while (true) {
            showMainMenu();
            Scanner scanner = new Scanner(System.in);
            String opcja = scanner.nextLine();
            switch (opcja) {
                case "add": {
                    addTask(scanner);
                    break;
                }
                case "remove": {
                    removeTask(scanner);
                    break;
                }
                case "list": {
                    listTasks();
                }
            }
            if (opcja.equalsIgnoreCase("exit")) {
                zapiszDoPliku(tasks);
                System.out.println(ConsoleColors.RED + "Bye, bye.");
                break;
            }
        }
    }

    private static void zapiszDoPliku(String[][] tasks) {
        String[] linia = new String[tasks.length];
        for (int i = 0; i < tasks.length; i++) {
            linia[i] = String.join(",", tasks[i]);
        }

        try {
            Files.write(path, Arrays.asList(linia));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private static void listTasks() {
        for (int i = 0; i < tasks.length; i++) {
            System.out.print(i + ": ");
            for (int j = 0; j < tasks[i].length; j++) {
                System.out.print(tasks[i][j]);
                if (j != tasks[i].length - 1) {
                    System.out.print(" ");
                } else {
                    System.out.println("");
                }
            }
        }
        System.out.println("");
    }

    private static void removeTask(Scanner scanner) {
        System.out.println("Please select number to remove.");
        int index = pobierzLiczbeOdUzytkownika(scanner);
        while (index < 0 || index > tasks.length - 1) {
            System.out.println("Index out of bound, enter valid index");
            index = pobierzLiczbeOdUzytkownika(scanner);
        }
        tasks = ArrayUtils.remove(tasks, index);
    }

    private static int pobierzLiczbeOdUzytkownika(Scanner scanner) {
        while (!scanner.hasNextInt()) {
            scanner.next();
            System.out.println("Not a number, please enter a number");
        }
        return scanner.nextInt();
    }

    private static void addTask(Scanner scanner) {
        System.out.println("Please add task description:");
        String description = scanner.nextLine();
        System.out.println("Please add task due date");
        String date = pobierzDateOdUzytkownika(scanner);
        System.out.println("Is your task important: true/false");
        String important = scanner.nextLine();
        int nowaDlugosc = tasks.length + 1;
        tasks = Arrays.copyOf(tasks, nowaDlugosc);
        String[] linia = {description, date, important};
        tasks[nowaDlugosc - 1] = linia;
    }

    private static String pobierzDateOdUzytkownika(Scanner scanner) {
        System.out.println("Use dd-MM-yyyy format");
        String sdate = scanner.nextLine();
        while (!isValidDate(sdate)) {
            System.out.println("Please use correct date and format!");
            sdate = scanner.nextLine();
        }
//        while (!isInTheFuture(sdate)){
//            System.out.println("Date can't be in the past!");
//            sdate = scanner.nextLine();
//            while (!isValidDate(sdate)){
//                System.out.println("Please use correct date and format!");
//                sdate = scanner.nextLine();
//            }
//        }
        return sdate;
    }

//    private static boolean isInTheFuture(String sdate) {
//        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
//        Date taskDate = null;
//        try {
//            taskDate = dateFormat.parse(sdate);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        Date current = dateFormat.parse();
//        System.out.println(current);
//        System.out.println(current.after(taskDate));
//        return current.after(taskDate);
//    }

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

    private static void showMainMenu() {
        List<String> menuOptions = new ArrayList<>();
        menuOptions.add(ConsoleColors.BLUE + "Please select an option:");
        menuOptions.add(ConsoleColors.RESET + "add");
        menuOptions.add("remove");
        menuOptions.add("list");
        menuOptions.add("exit");
        for (int i = 0; i < menuOptions.size(); i++) {
            System.out.println(menuOptions.get(i));
        }

    }
}
