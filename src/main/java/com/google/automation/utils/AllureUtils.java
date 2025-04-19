// src/main/java/com/google/automation/utils/AllureUtils.java
package com.google.automation.utils;

import io.qameta.allure.Allure;
import io.qameta.allure.model.Status;
import io.qameta.allure.model.StepResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

public class AllureUtils {
    private static final Logger log = LoggerFactory.getLogger(AllureUtils.class);

    /**
     * Прикрепить файл к текущему тесту в Allure
     *
     * @param name     имя вложения
     * @param filePath путь к файлу
     * @param type     тип содержимого (например, "application/json")
     */
    public static void attachFile(String name, String filePath, String type) {
        log.info("Прикрепление файла к отчету Allure: {}", filePath);
        try (InputStream is = Files.newInputStream(Paths.get(filePath))) {
            Allure.addAttachment(name, type, is, null);
        } catch (IOException e) {
            log.error("Ошибка при прикреплении файла: {}", e.getMessage(), e);
        }
    }

    /**
     * Прикрепить текстовое содержимое к текущему тесту в Allure
     *
     * @param name    имя вложения
     * @param content текстовое содержимое
     */
    public static void attachText(String name, String content) {
        log.info("Прикрепление текста к отчету Allure: {}", name);
        Allure.addAttachment(name, "text/plain", content);
    }

    /**
     * Прикрепить JSON-содержимое к текущему тесту в Allure
     *
     * @param name    имя вложения
     * @param content JSON-содержимое
     */
    public static void attachJson(String name, String content) {
        log.info("Прикрепление JSON к отчету Allure: {}", name);
        Allure.addAttachment(name, "application/json", content);
    }

    /**
     * Создать пользовательский шаг в Allure
     *
     * @param name     имя шага
     * @param runnable действия в шаге
     */
    public static void step(String name, Runnable runnable) {
        log.info("Создание пользовательского шага: {}", name);
        String uuid = UUID.randomUUID().toString();

        Allure.getLifecycle().startStep(uuid, new StepResult().setName(name));
        try {
            runnable.run();
            Allure.getLifecycle().updateStep(uuid, s -> s.setStatus(Status.PASSED));
        } catch (Throwable e) {
            Allure.getLifecycle().updateStep(uuid, s -> s.setStatus(Status.FAILED));
            throw e;
        } finally {
            Allure.getLifecycle().stopStep(uuid);
        }
    }
}