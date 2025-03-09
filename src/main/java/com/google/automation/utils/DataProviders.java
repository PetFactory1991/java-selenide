package com.google.automation.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.DataProvider;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DataProviders {

    protected static final Logger log = LoggerFactory.getLogger(DataProviders.class);

    @DataProvider(name = "searchQueries")
    public static Iterator<Object[]> searchQueriesProvider() {
        List<Object[]> data = new ArrayList<>();

        try (InputStream is = DataProviders.class.getClassLoader()
                .getResourceAsStream("testdata/search_queries.csv")) {
            assert is != null;
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {

                // Пропускаем заголовок
                String line = reader.readLine();

                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length >= 2) {
                        data.add(new Object[]{parts[0], parts[1]});
                    }
                }

            }
        } catch (IOException e) {
            log.error("Ошибка чтения тестовых данных", e);
        }

        return data.iterator();
    }

    @DataProvider(name = "simpleSearchQueries")
    public static Object[][] simpleSearchQueriesProvider() {
        return new Object[][]{
                {"Automation Testing", "Automation"},
                {"Selenium vs Selenide", "Selenide"},
                {"AWS CodeBuild CI/CD", "CodeBuild"}
        };
    }
}