import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.Random;

public class Utils {
    static int getRandomNumber(int max) {
        Random random = new Random();
        return random.nextInt(max);
    }

    static WebElement randomItem(List<WebElement> list) {
        return list.get(getRandomNumber(list.size()));
    }

}
