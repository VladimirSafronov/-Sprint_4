package pages;

import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Page Object данных заказа
 */
public class OrderInfo {

  private static final String WHEN_BRING_ORDER_FIELD_XPATH = ".//input[@placeholder='* Когда привезти самокат']";
  private static final String ORDER_DAYS_COUNT_FIELD_XPATH = ".//div[@class='Dropdown-placeholder']";
  private static final String ORDER_BUTTONS_XPATH = ".//button[contains(text(), 'Заказать')]";
  private static final String CONFIRM_ORDER_BUTTON_XPATH = ".//button[contains(text(), 'Да')]";
  private static final String ORDER_INFO_XPATH = ".//div[contains(text(), 'Заказ оформлен')]";

  //поле Когда привезти самокат
  private final WebElement whenBringOrderField;
  //выпадающий список Срок аренды
  private final WebElement orderDaysCountField;
  //верхняя кнопка Заказать
  private final WebElement upperOrderButton;
  //нижняя кнопка Заказать
  private final WebElement lowerOrderButton;

  private final WebDriver driver;
  private final WebDriverWait wait;

  public OrderInfo(WebDriver driver) {
    this.driver = driver;
    this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));

    wait.until(ExpectedConditions.elementToBeClickable(By.xpath(WHEN_BRING_ORDER_FIELD_XPATH)));
    this.whenBringOrderField = driver.findElement(By.xpath(WHEN_BRING_ORDER_FIELD_XPATH));
    this.orderDaysCountField = driver.findElement(By.xpath(ORDER_DAYS_COUNT_FIELD_XPATH));
    this.upperOrderButton = driver.findElements(By.xpath(ORDER_BUTTONS_XPATH)).get(0);
    this.lowerOrderButton = driver.findElements(By.xpath(ORDER_BUTTONS_XPATH)).get(1);
  }

  /**
   * Метод получает строчное представление данных о сделанном заказе
   */
  public String getOrderInfo() {
    WebElement orderInfo = driver.findElement(By.xpath(ORDER_INFO_XPATH));
    wait.until(ExpectedConditions.visibilityOf(orderInfo));
    return orderInfo.getText();
  }

  /**
   * Метод кликает по кнопке Да, подтверждая заказ
   */
  public void clickConfirmOrder() {
    wait.until(ExpectedConditions.elementToBeClickable(By.xpath(CONFIRM_ORDER_BUTTON_XPATH)))
        .click();
  }

  /**
   * Метод заполняет данные о заказе
   */
  public void inputOrderInfo(String startRent, String rentDays) {
    whenBringOrderField.sendKeys(startRent, Keys.ENTER);
    orderDaysCountField.click();
    wait.until(ExpectedConditions.elementToBeClickable(By.xpath(
        String.format(".//div[@class='Dropdown-option' and text()='%s']", rentDays)))).click();
  }

  /**
   * Метод кликает по нижней кнопке Заказать
   */
  public void clickLowerOrderButton() {
    lowerOrderButton.click();
  }
}
