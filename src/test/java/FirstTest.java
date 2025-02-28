import org.testng.annotations.Test;

import static com.codeborne.selenide.Selenide.open;

public class FirstTest {
    @Test
    void test() {
        open("https://www.google.com");
    }
}
