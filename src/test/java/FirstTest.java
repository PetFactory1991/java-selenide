import com.codeborne.selenide.Configuration;
import org.testng.annotations.Test;

import static com.codeborne.selenide.Selenide.open;

public class FirstTest {
    @Test
    void test() {
        Configuration.headless = true;
        Configuration.browser = "chrome";
        Configuration.baseUrl = "https://www.google.com";
        open("/");
    }
}
