import com.codeborne.selenide.Configuration;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.Test;

import static com.codeborne.selenide.Selenide.open;

public class FirstTest {
    @Test
    void test() {
        Configuration.headless = true;
        Configuration.browser = "chrome";
        Configuration.baseUrl = "https://www.google.com";
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--user-data-dir=/tmp/chrome-user-data-" + System.currentTimeMillis());
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");

        // Применяем дополнительные возможности браузера
        Configuration.browserCapabilities = options;
        open("/");
    }
}
