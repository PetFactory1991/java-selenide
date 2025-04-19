// src/main/java/com/google/automation/aws/S3Manager.java
package com.google.automation.aws;

import com.google.automation.config.EnvironmentConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;

public class S3Manager {
    private static final Logger log = LoggerFactory.getLogger(S3Manager.class);
    static final String BASE_PATH = "java-selenide/";

    private final String bucketName;
    private final S3Client s3;
    private final Region region;

    public S3Manager() {
        this.bucketName = EnvironmentConfig.getBucketName();
        this.region = Region.of(EnvironmentConfig.getAWSRegion());
        this.s3 = S3Client.builder()
                .region(region)
                .build();

        log.info("Инициализирован S3Manager для бакета {} в регионе {}",
                bucketName, region);
    }

    /**
     * Загрузить файл в S3
     *
     * @param localFilePath локальный путь к файлу
     * @param s3Key         ключ в S3
     */
    public void uploadFile(String localFilePath, String s3Key) {
        log.info("Загрузка файла {} в S3: {}", localFilePath, s3Key);
        try {
            File file = new File(localFilePath);
            PutObjectRequest request = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(BASE_PATH + s3Key)
                    .build();

            s3.putObject(request, RequestBody.fromFile(file));
            log.info("Файл успешно загружен в S3");
        } catch (Exception e) {
            log.error("Ошибка при загрузке файла в S3: {}", e.getMessage(), e);
        }
    }

    /**
     * Загрузить директорию в S3
     *
     * @param localDirPath локальный путь к директории
     * @param s3Prefix     префикс в S3
     */
    public void uploadDirectory(String localDirPath, String s3Prefix) {
        log.info("Загрузка директории {} в S3: {}", localDirPath, s3Prefix);
        try {
            File directory = new File(localDirPath);
            if (!directory.isDirectory()) {
                throw new IllegalArgumentException(localDirPath + " не является директорией");
            }

            uploadDirectoryInternal(directory, s3Prefix, directory.getPath());
            log.info("Директория успешно загружена в S3");
        } catch (Exception e) {
            log.error("Ошибка при загрузке директории в S3: {}", e.getMessage(), e);
        }
    }

    private void uploadDirectoryInternal(File currentDir, String s3Prefix, String basePath) {
        File[] files = currentDir.listFiles();
        if (files == null) {
            return;
        }

        for (File file : files) {
            if (file.isDirectory()) {
                uploadDirectoryInternal(file, s3Prefix, basePath);
            } else {
                String relativePath = file.getPath().substring(basePath.length());
                if (relativePath.startsWith(File.separator)) {
                    relativePath = relativePath.substring(1);
                }

                String s3Key = s3Prefix + relativePath.replace(File.separator, "/");
                uploadFile(file.getPath(), s3Key);
            }
        }
    }

    /**
     * Получить список всех отчетов Allure в S3
     *
     * @return список отчетов с датами
     */
    public List<Map<String, String>> listAllureReports() {
        log.info("Получение списка отчетов Allure из S3");
        try {
            ListObjectsV2Request request = ListObjectsV2Request.builder()
                    .bucket(bucketName)
                    .prefix(BASE_PATH + "allure-reports/")
                    .delimiter("/")
                    .build();

            ListObjectsV2Response response = s3.listObjectsV2(request);

            List<Map<String, String>> reports = new ArrayList<>();

            for (CommonPrefix prefix : response.commonPrefixes()) {
                String reportPath = prefix.prefix();
                String reportName = reportPath.substring(reportPath.lastIndexOf('/') + 1);

                // Проверяем наличие index.html
                HeadObjectRequest headRequest = HeadObjectRequest.builder()
                        .bucket(bucketName)
                        .key(reportPath + "index.html")
                        .build();

                try {
                    s3.headObject(headRequest);

                    // Пробуем извлечь дату из имени отчета
                    Date reportDate = null;
                    try {
                        if (reportName.matches("\\d{4}-\\d{2}-\\d{2}_\\d{2}-\\d{2}-\\d{2}")) {
                            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
                            reportDate = format.parse(reportName);
                        }
                    } catch (Exception e) {
                        log.warn("Не удалось распарсить дату из имени отчета: {}", reportName);
                    }

                    Map<String, String> reportInfo = new HashMap<>();
                    reportInfo.put("path", reportPath);
                    reportInfo.put("name", reportName);
                    reportInfo.put("url", getReportUrl(reportPath + "index.html"));
                    reportInfo.put("date", reportDate != null ?
                            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(reportDate) : "Unknown");

                    reports.add(reportInfo);
                } catch (NoSuchKeyException e) {
                    // Пропускаем директории без index.html
                    log.warn("Директория без index.html: {}", reportPath);
                }
            }

            // Сортируем по дате, если возможно
            reports.sort((r1, r2) -> {
                try {
                    if (r1.get("date").equals("Unknown") || r2.get("date").equals("Unknown")) {
                        return r1.get("name").compareTo(r2.get("name"));
                    }

                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date d1 = format.parse(r1.get("date"));
                    Date d2 = format.parse(r2.get("date"));

                    // Сортировка по убыванию даты (новые отчеты сверху)
                    return d2.compareTo(d1);
                } catch (Exception e) {
                    return r1.get("name").compareTo(r2.get("name"));
                }
            });

            log.info("Найдено {} отчетов", reports.size());
            return reports;
        } catch (Exception e) {
            log.error("Ошибка при получении списка отчетов: {}", e.getMessage(), e);
            return List.of();
        }
    }

    /**
     * Создать URL для доступа к отчету
     *
     * @param reportKey ключ отчета
     * @return URL
     */
    public String getReportUrl(String reportKey) {
        return String.format("https://%s.s3.%s.amazonaws.com/%s",
                bucketName, region.toString(), reportKey);
    }
}