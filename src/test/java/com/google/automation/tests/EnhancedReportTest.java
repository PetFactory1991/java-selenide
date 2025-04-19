// src/test/java/com/google/automation/tests/EnhancedReportTest.java
package com.google.automation.tests;

import com.google.automation.base.BaseTest;
import com.google.automation.config.EnvironmentConfig;
import com.google.automation.factory.PageFactory;
import com.google.automation.pages.GoogleSearchResultsPage;
import com.google.automation.utils.AllureLabels;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

public class EnhancedReportTest extends BaseTest {

    @Test
    @Feature("Расширенные отчеты")
    @Story("Визуализация в Allure")
    @Description("Демонстрация расширенных возможностей отчетов Allure")
    @Severity(SeverityLevel.NORMAL)
    public void testWithEnhancedReporting() {
        log.info("Запуск теста с расширенной отчетностью");

        // Добавляем стандартные метки
        AllureLabels.addStandardLabels("Chrome", "120.0", EnvironmentConfig.getProperty("env", "dev"));

        // Выполняем тест
        String query = "Allure reporting";

        // Фиксируем время начала
        long startTime = System.currentTimeMillis();

        // Выполняем поиск
        GoogleSearchResultsPage resultsPage = PageFactory.searchOnGoogle(query);

        // Фиксируем время окончания
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        // Добавляем метрики производительности
        Map<String, Double> metrics = new HashMap<>();
        metrics.put("search_duration_ms", (double) duration);
        metrics.put("results_count", (double) resultsPage.getResultsCount());

        AllureLabels.addPerformanceMetrics(metrics);

        // Добавляем таблицу данных
        Map<String, Object> tableData = new HashMap<>();
        tableData.put("Поисковый запрос", query);
        tableData.put("Количество результатов", resultsPage.getResultsCount());
        tableData.put("Время выполнения (мс)", duration);
        tableData.put("Заголовок первого результата", resultsPage.getFirstResultTitle());
        tableData.put("Окружение", EnvironmentConfig.getProperty("env", "dev"));

        AllureLabels.addDataTable("Результаты поиска", tableData);

        // Добавляем график времени загрузки
        Map<String, Double> chartData = new HashMap<>();
        chartData.put("DNS Lookup", 20.5);
        chartData.put("TCP Connection", 35.2);
        chartData.put("Request", 42.8);
        chartData.put("Response", 580.3);
        chartData.put("DOM Processing", 125.6);

        AllureLabels.addChart("Метрики загрузки страницы (мс)", chartData);

        // Проверки
        Assert.assertTrue(resultsPage.isPageLoaded(), "Страница результатов не загружена");
        Assert.assertTrue(resultsPage.getResultsCount() > 0, "Результаты поиска не найдены");
        Assert.assertEquals(resultsPage.getSearchQuery(), query, "Поисковый запрос не соответствует ожидаемому");
    }
}