package com.google.automation.aws;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AllureReportUploader {
    private static final Logger log = LoggerFactory.getLogger(AllureReportUploader.class);
    private static final String ALLURE_REPORT_DIR = "target/allure-report";

    public static void main(String[] args) {
        log.info("Запуск загрузки отчета Allure в S3");

        // Создаем уникальный путь с временной меткой
        String timestamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());
        String s3Prefix = "allure-reports/" + timestamp + "/";

        S3Manager s3Manager = new S3Manager();
        s3Manager.uploadDirectory(ALLURE_REPORT_DIR, s3Prefix);

        // Выводим URL для доступа к отчету
        String reportUrl = s3Manager.getReportUrl(s3Prefix + "index.html");
        log.info("Отчет доступен по адресу: {}", reportUrl);
    }
}