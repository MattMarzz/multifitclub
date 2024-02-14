package it.uniroma2.dicii.ispw.view.cli;

import it.uniroma2.dicii.ispw.bean.UtenteBean;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public abstract class TemplateView {
    protected UtenteBean usrBean;

    public abstract int userChoice();
    public abstract void control();

    public TemplateView() {
    }

    public TemplateView(UtenteBean usrBean) {
        this.usrBean = usrBean;
    }

    protected void printHeader(String headerText) {
        int width = 50;
        String border = new String(new char[width]).replace("\0", "-");

        System.out.println();
        System.out.println("\033[36m" + border);
        int leftPadding = (width - headerText.length()) / 2;
        for (int i = 0; i < leftPadding; i++) {
            System.out.print(" ");
        }
        System.out.println(headerText);
        System.out.println(border + "\033[0m");
    }

    protected int operationMenu(String title, List<String> options) {
        Scanner scanner = new Scanner(System.in);
        int choice = 0;

        System.out.println();
        System.out.println("\033[34m--- " + title + " ---\033[0m");
        for (int i = 0; i < options.size(); i++) {
            System.out.println((i + 1) + ") " + options.get(i));
        }

        do {
            System.out.println();
            System.out.print("Scegli un'opzione (1-" + options.size() + "): ");

            if (scanner.hasNextInt()) {
                choice = scanner.nextInt();
                if (choice < 1 || choice > options.size()) {
                    System.out.println("Per favore inserisci un numero tra 1 e " + options.size());
                    choice = 0;
                }
            } else {
                System.out.println("Input non valido. Per favore inserisci un numero.");
                scanner.next();
            }
        } while (choice == 0);
        return choice;
    }

    public String getDesiredIn(String title, String inMsg) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        printHeader(title);

        System.out.print(inMsg);
        return reader.readLine();
    }

    public <T> void printTable(List<T> list) {
        if (list == null || list.isEmpty()) {
            System.out.println("La lista è vuota.");
            return;
        }

        Method[] methods = list.get(0).getClass().getDeclaredMethods();

        List<Method> getters = filterGetters(methods);

        List<String> headers = new ArrayList<>();
        List<Integer> columnWidths = new ArrayList<>();

        // header and column weight
        for (Method getter : getters) {
            String header = getter.getName().substring(3);
            headers.add(header);
            int maxWidth = header.length();

            for (T item : list) {
                try {
                    String valueString = String.valueOf(getter.invoke(item));
                    maxWidth = Math.max(maxWidth, valueString.length());
                } catch (Exception e) {
                    throw new RuntimeException();
                }
            }
            columnWidths.add(maxWidth);
        }

        // print table header
        for (int i = 0; i < headers.size(); i++) {
            System.out.printf("\033[35m%-" + columnWidths.get(i) + "s ", headers.get(i));
        }
        System.out.println();

        for (int width : columnWidths) {
            System.out.print("-".repeat(width) + " ");
        }
        System.out.println("\033[0m");

        // rows
        for (T item : list) {
            for (int i = 0; i < getters.size(); i++) {
                try {
                    String valueString = String.valueOf(getters.get(i).invoke(item));
                    System.out.printf("%-" + columnWidths.get(i) + "s ", valueString);
                } catch (Exception e) {
                    System.out.print("Errore" + " ".repeat(columnWidths.get(i) - 6));
                }
            }
            System.out.println();
        }
    }

    private static List<Method> filterGetters(Method[] methods) {
        return Arrays.stream(methods)
                .filter(m -> m.getName().startsWith("get") && m.getParameterCount() == 0)
                .collect(Collectors.toList());
    }

}
