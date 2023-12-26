package pages;

import exceptions.NotFoundStationException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Page Object данных заказчика
 */
public class CustomerInfo {

  private static final String FIELD_FIRST_NAME_XPATH = ".//input[@placeholder='* Имя']";
  private static final String FIELD_SECOND_NAME_XPATH = ".//input[@placeholder='* Фамилия']";
  private static final String FIELD_ADDRESS_XPATH = ".//input[@placeholder='* Адрес: куда привезти заказ']";
  private static final String UNDERGROUND_STATION_XPATH = ".//input[@placeholder='* Станция метро']";
  private static final String FIELD_PHONE_NUMBER_XPATH = ".//input[@placeholder='* Телефон: на него позвонит курьер']";
  private static final String BUTTON_NEXT_XPATH = ".//button[contains(text(), 'Далее')]";
  private static final String UNDERGROUND_STATION_LIST_XPATH = ".//li[@class='select-search__row']";
  private static final String TITLE_PAGE_XPATH = ".//div[contains(text(), 'Для кого самокат')]";

  //Заголовок окна
  private final WebElement title;
  //поле Имя
  private final WebElement fieldFirstName;
  //поле Фамилия
  private final WebElement fieldSecondName;
  //поле Адрес
  private final WebElement fieldAddress;
  //выпадающий список Станция метро
  private final WebElement selectorUndergroundStation;
  //список всех станций метро
  private List<WebElement> undergroundStations;
  //полее Телефон
  private final WebElement fieldPhoneNumber;
  //кнопка Далее
  private final WebElement buttonNext;

  private final WebDriver driver;
  private final WebDriverWait wait;

  public CustomerInfo(WebDriver driver) {
    this.driver = driver;
    this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));

    wait.until(ExpectedConditions.elementToBeClickable(By.xpath(BUTTON_NEXT_XPATH)));
    this.fieldFirstName = driver.findElement(By.xpath(FIELD_FIRST_NAME_XPATH));
    this.fieldSecondName = driver.findElement(By.xpath(FIELD_SECOND_NAME_XPATH));
    this.fieldAddress = driver.findElement(By.xpath(FIELD_ADDRESS_XPATH));
    this.selectorUndergroundStation = driver.findElement(By.xpath(UNDERGROUND_STATION_XPATH));
    this.fieldPhoneNumber = driver.findElement(By.xpath(FIELD_PHONE_NUMBER_XPATH));
    this.buttonNext = driver.findElement(By.xpath(BUTTON_NEXT_XPATH));
    this.title = driver.findElement(By.xpath(TITLE_PAGE_XPATH));
    this.undergroundStations = new ArrayList<>();
  }

  /**
   * Метод заполняет поля для заказа
   *
   * @param firstName          - поле Имя
   * @param secondName         - поле Фамилия
   * @param address            - поле Адрес
   * @param undergroundStation - выпадающий список станций метро
   * @param phoneNumber        - поле телефон
   */
  public void inputCustomerInfo(String firstName, String secondName, String address,
      String undergroundStation, String phoneNumber) {
    fieldFirstName.sendKeys(firstName);
    fieldSecondName.sendKeys(secondName);
    fieldAddress.sendKeys(address);
    loadUndergroundStations();
    clickChoosenStation(selectUndergroundStation(undergroundStation));
    fieldPhoneNumber.sendKeys(phoneNumber);
  }

  /**
   * Метод кликает по кнопке Далее
   */
  public void clickButtonNext() {
    buttonNext.click();
  }

  /**
   * Метод возвращает текст заголовка страницы
   */
  public String getTitleText() {
    return title.getText();
  }

  /**
   * Метод загружает все станции метро
   */
  private void loadUndergroundStations() {
    selectorUndergroundStation.click();
    undergroundStations = driver.findElements(By.xpath(UNDERGROUND_STATION_LIST_XPATH));
  }

  /**
   * Метод находит необходимую станцию из списка
   */
  private WebElement selectUndergroundStation(String undergroundStationName) {
    WebElement station = null;
    for (WebElement undergroundStation : undergroundStations) {
      if (undergroundStation.getText().equals(undergroundStationName)) {
        station = undergroundStation;
        System.out.println("Найдена станция: " + station.getText());
        break;
      }
    }
    if (station == null) {
      throw new NotFoundStationException(
          "Станции по имени " + undergroundStationName + " не найдено");
    }
    return station;
  }

  /**
   * Метод скроллит мышью до выбранной станции и кликает по ней
   */
  private void clickChoosenStation(WebElement station) {
    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView();", station);
    station.click();
  }
}
