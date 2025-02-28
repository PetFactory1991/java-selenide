import com.codeborne.selenide.Configuration;
import org.testng.annotations.Test;

import static com.codeborne.selenide.Selenide.open;

public class FirstTest {
    @Test
    void test() {
        Configuration.baseUrl = "https://www.google.com";
        open("/");
    }
}
