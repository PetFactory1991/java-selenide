// src/main/java/com/google/automation/utils/AllureLabels.java (исправленный)
package com.google.automation.utils;

import io.qameta.allure.Allure;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class AllureLabels {
    private static final Logger log = LoggerFactory.getLogger(AllureLabels.class);

    /**
     * Добавить стандартные метки к тесту
     *
     * @param browser        браузер
     * @param browserVersion версия браузера
     * @param environment    окружение
     */
    public static void addStandardLabels(String browser, String browserVersion, String environment) {
        log.info("Добавление стандартных меток: browser={}, browserVersion={}, environment={}",
                browser, browserVersion, environment);

        Allure.label("browser", browser);
        Allure.label("browser.version", browserVersion);
        Allure.label("environment", environment);
    }

    /**
     * Добавить метрики производительности
     *
     * @param metrics карта метрик
     */
    public static void addPerformanceMetrics(Map<String, Double> metrics) {
        log.info("Добавление метрик производительности: {}", metrics);

        metrics.forEach((key, value) -> {
            Allure.label("metric." + key, String.valueOf(value));
        });
    }

    /**
     * Добавить таблицу данных к отчету
     *
     * @param name имя таблицы
     * @param data данные таблицы
     */
    public static void addDataTable(String name, Map<String, Object> data) {
        log.info("Добавление таблицы данных: {}", name);

        StringBuilder htmlTable = new StringBuilder();
        htmlTable.append("<table border='1'>");
        htmlTable.append("<tr><th>Параметр</th><th>Значение</th></tr>");

        data.forEach((key, value) -> {
            htmlTable.append("<tr>");
            htmlTable.append("<td>").append(key).append("</td>");
            htmlTable.append("<td>").append(value).append("</td>");
            htmlTable.append("</tr>");
        });

        htmlTable.append("</table>");

        // Обратите внимание на порядок аргументов и .html расширение в имени
        Allure.addAttachment(name + ".html", "text/html", htmlTable.toString(), "html");
    }

    /**
     * Добавить график к отчету
     *
     * @param name имя графика
     * @param data данные графика
     */
    public static void addChart(String name, Map<String, Double> data) {
        log.info("Добавление графика: {}", name);

        // Создаем SVG-график
        StringBuilder svgChart = new StringBuilder();

        int width = 600;
        int height = 400;
        int padding = 50;

        svgChart.append("<svg width='").append(width).append("' height='").append(height).append("' xmlns='http://www.w3.org/2000/svg'>");

        // Заголовок
        svgChart.append("<text x='").append(width / 2).append("' y='20' text-anchor='middle' font-size='16'>")
                .append(name).append("</text>");

        // Находим максимальное значение для масштабирования
        double maxValue = data.values().stream().mapToDouble(Double::doubleValue).max().orElse(100.0);

        // Рисуем оси
        svgChart.append("<line x1='").append(padding).append("' y1='").append(height - padding)
                .append("' x2='").append(width - padding).append("' y2='").append(height - padding)
                .append("' stroke='black' stroke-width='2'></line>");

        svgChart.append("<line x1='").append(padding).append("' y1='").append(padding)
                .append("' x2='").append(padding).append("' y2='").append(height - padding)
                .append("' stroke='black' stroke-width='2'></line>");

        // Рисуем столбцы
        int barWidth = (width - 2 * padding) / data.size();
        int barPadding = 10;
        int barIndex = 0;

        for (Map.Entry<String, Double> entry : data.entrySet()) {
            String label = entry.getKey();
            double value = entry.getValue();

            int barHeight = (int) (value / maxValue * (height - 2 * padding));
            int barX = padding + barIndex * barWidth + barPadding;
            int barY = height - padding - barHeight;

            // Столбец
            svgChart.append("<rect x='").append(barX).append("' y='").append(barY)
                    .append("' width='").append(barWidth - 2 * barPadding).append("' height='").append(barHeight)
                    .append("' fill='steelblue'></rect>");

            // Метка
            svgChart.append("<text x='").append(barX + barWidth / 2 - barPadding).append("' y='")
                    .append(height - padding + 20).append("' text-anchor='middle' font-size='12'>")
                    .append(label).append("</text>");

            // Значение
            svgChart.append("<text x='").append(barX + barWidth / 2 - barPadding).append("' y='")
                    .append(barY - 5).append("' text-anchor='middle' font-size='12'>")
                    .append(value).append("</text>");

            barIndex++;
        }

        svgChart.append("</svg>");

        // Обратите внимание на порядок аргументов и .svg расширение в имени
        Allure.addAttachment(name + ".svg", "image/svg+xml", svgChart.toString(), "svg");
    }
}