import java.util.concurrent.TimeUnit;
import org.junit.After;
import org.junit.Before;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

/**
 * Базовый класс тестов для настройки драйвера, и его закрытия перед и после каждого теста
 */
public class BaseTest {

  protected WebDriver driver;

  @Before
  public void before() {
    System.setProperty("webdriver.chrome.driver", System.getenv("CHROME_DRIVER"));
    driver = new ChromeDriver();
//    System.setProperty("webdriver.gecko.driver", System.getenv("FIREFOX_DRIVER"));
//    driver = new FirefoxDriver();

    driver.manage().window().maximize();
    driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
    driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
    driver.manage().timeouts().setScriptTimeout(30, TimeUnit.SECONDS);

    driver.get("https://qa-scooter.praktikum-services.ru/");
  }

  @After
  public void after() {
    driver.quit();
  }
}
