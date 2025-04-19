// src/main/java/com/google/automation/aws/AllureReportManager.java
package com.google.automation.aws;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import static com.google.automation.aws.S3Manager.BASE_PATH;

public class AllureReportManager {
    private static final Logger log = LoggerFactory.getLogger(AllureReportManager.class);
    private static final String ALLURE_REPORT_DIR = "target/allure-report";
    private static final S3Manager s3Manager = new S3Manager();

    public static void main(String[] args) {
        if (args.length > 0) {
            switch (args[0].toLowerCase()) {
                case "upload":
                    uploadReport();
                    break;
                case "list":
                    listReports();
                    break;
                case "open":
                    if (args.length > 1) {
                        openReport(Integer.parseInt(args[1]));
                    } else {
                        openReport(0); // Открыть последний отчет по умолчанию
                    }
                    break;
                case "compare":
                    if (args.length > 2) {
                        compareReports(Integer.parseInt(args[1]), Integer.parseInt(args[2]));
                    } else {
                        System.out.println("Укажите два индекса отчетов для сравнения: compare <index1> <index2>");
                    }
                    break;
                case "help":
                default:
                    printHelp();
                    break;
            }
        } else {
            interactiveMode();
        }
    }

    private static void interactiveMode() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n===== Менеджер отчетов Allure =====");
            System.out.println("1. Загрузить текущий отчет в S3");
            System.out.println("2. Список доступных отчетов");
            System.out.println("3. Открыть отчет");
            System.out.println("4. Сравнить два отчета");
            System.out.println("0. Выход");
            System.out.print("Выберите действие: ");

            int choice;
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Неверный ввод. Введите цифру от 0 до 4.");
                continue;
            }

            switch (choice) {
                case 0:
                    System.out.println("Выход из программы.");
                    scanner.close();
                    return;
                case 1:
                    uploadReport();
                    break;
                case 2:
                    listReports();
                    break;
                case 3:
                    System.out.print("Введите индекс отчета для открытия (0 - последний): ");
                    try {
                        int index = Integer.parseInt(scanner.nextLine());
                        openReport(index);
                    } catch (NumberFormatException e) {
                        System.out.println("Неверный ввод. Введите число.");
                    }
                    break;
                case 4:
                    System.out.print("Введите индекс первого отчета: ");
                    try {
                        int index1 = Integer.parseInt(scanner.nextLine());
                        System.out.print("Введите индекс второго отчета: ");
                        int index2 = Integer.parseInt(scanner.nextLine());
                        compareReports(index1, index2);
                    } catch (NumberFormatException e) {
                        System.out.println("Неверный ввод. Введите число.");
                    }
                    break;
                default:
                    System.out.println("Неверный выбор. Введите цифру от 0 до 4.");
                    break;
            }
        }
    }

    private static void uploadReport() {
        log.info("Запуск загрузки отчета Allure в S3");

        // Создаем уникальный путь с временной меткой
        String timestamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());
        String s3Prefix = "allure-reports/" + timestamp + "/";

        s3Manager.uploadDirectory(ALLURE_REPORT_DIR, s3Prefix);

        // Выводим URL для доступа к отчету
        String reportUrl = s3Manager.getReportUrl(BASE_PATH + s3Prefix + "index.html");
        log.info("Отчет доступен по адресу: {}", reportUrl);
        System.out.println("Отчет загружен и доступен по адресу: " + reportUrl);
    }

    private static void listReports() {
        List<Map<String, String>> reports = s3Manager.listAllureReports();

        if (reports.isEmpty()) {
            System.out.println("Отчеты не найдены.");
            return;
        }

        System.out.println("\n===== Доступные отчеты Allure =====");
        System.out.println("Индекс | Дата                | Имя");
        System.out.println("-------|--------------------|------------------");

        for (int i = 0; i < reports.size(); i++) {
            Map<String, String> report = reports.get(i);
            System.out.printf("%-6d | %-18s | %s%n",
                    i, report.get("date"), report.get("name"));
        }
    }

    private static void openReport(int index) {
        List<Map<String, String>> reports = s3Manager.listAllureReports();

        if (reports.isEmpty()) {
            System.out.println("Отчеты не найдены.");
            return;
        }

        if (index < 0 || index >= reports.size()) {
            System.out.println("Неверный индекс отчета. Доступны индексы от 0 до " + (reports.size() - 1));
            return;
        }

        String reportUrl = reports.get(index).get("url");
        System.out.println("Открываю отчет: " + reportUrl);

        try {
            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                Desktop.getDesktop().browse(new URI(reportUrl));
            } else {
                System.out.println("Не удалось открыть браузер автоматически. Скопируйте URL и откройте вручную.");
            }
        } catch (Exception e) {
            log.error("Ошибка при открытии отчета: {}", e.getMessage(), e);
            System.out.println("Ошибка при открытии отчета: " + e.getMessage());
        }
    }

    private static void compareReports(int index1, int index2) {
        List<Map<String, String>> reports = s3Manager.listAllureReports();

        if (reports.isEmpty()) {
            System.out.println("Отчеты не найдены.");
            return;
        }

        if (index1 < 0 || index1 >= reports.size() || index2 < 0 || index2 >= reports.size()) {
            System.out.println("Неверные индексы отчетов. Доступны индексы от 0 до " + (reports.size() - 1));
            return;
        }

        String reportUrl1 = reports.get(index1).get("url");
        String reportUrl2 = reports.get(index2).get("url");

        System.out.println("\n===== Сравнение отчетов =====");
        System.out.println("Отчет 1: " + reports.get(index1).get("date") + " - " + reports.get(index1).get("name"));
        System.out.println("Отчет 2: " + reports.get(index2).get("date") + " - " + reports.get(index2).get("name"));

        // В реальном приложении здесь могла бы быть более сложная логика сравнения
        // Например, загрузка JSON из отчетов и сравнение результатов

        System.out.println("\nОткрыть отчеты для сравнения? (y/n)");
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        try {
            String answer = reader.readLine();
            if (answer != null && answer.toLowerCase().startsWith("y")) {
                if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                    Desktop.getDesktop().browse(new URI(reportUrl1));
                    Thread.sleep(1000); // Небольшая задержка между открытием окон
                    Desktop.getDesktop().browse(new URI(reportUrl2));
                } else {
                    System.out.println("Не удалось открыть браузер автоматически. Скопируйте URLs и откройте вручную:");
                    System.out.println("Отчет 1: " + reportUrl1);
                    System.out.println("Отчет 2: " + reportUrl2);
                }
            }
        } catch (Exception e) {
            log.error("Ошибка при открытии отчетов: {}", e.getMessage(), e);
            System.out.println("Ошибка при открытии отчетов: " + e.getMessage());
        }
    }

    private static void printHelp() {
        System.out.println("\n===== Справка по командам =====");
        System.out.println("upload       - загрузить текущий отчет в S3");
        System.out.println("list         - список доступных отчетов");
        System.out.println("open [index] - открыть отчет (по умолчанию последний)");
        System.out.println("compare <index1> <index2> - сравнить два отчета");
        System.out.println("help         - показать эту справку");
    }
}