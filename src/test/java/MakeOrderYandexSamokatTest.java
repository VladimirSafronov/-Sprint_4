import helpers.Constants;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import pages.CustomerInfo;
import pages.OrderInfo;
import pages.YandexSamokatMain;

import static helpers.Constants.CUSTOMER_INFO_PAGE_TITLE;
import static helpers.Constants.FIVE_DAYS_RENT;
import static helpers.Constants.ONE_DAY_RENT;
import static helpers.Constants.SEVEN_DAYS_RENT;
import static helpers.Constants.YANDEX_SAMOKAT_URL;

@RunWith(Parameterized.class)
public class MakeOrderYandexSamokatTest extends BaseTest {

  private final String firstName;
  private final String lastName;
  private final String address;
  private final String undergroundStation;
  private final String phoneNumber;
  private final String startRent;
  private final String rentDays;

  public MakeOrderYandexSamokatTest(String firstName, String lastName, String address,
      String undergroundStation, String phoneNumber, String startRent, String rentDays) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.address = address;
    this.undergroundStation = undergroundStation;
    this.phoneNumber = phoneNumber;
    this.startRent = startRent;
    this.rentDays = rentDays;
  }

  @Parameters
  public static Object[][] getCredentials() {
    return new Object[][]{
        {"Иван", "Иванов", "проспект Свободы", "Алексеевская", "+79001234567", "11.12.2024",
            SEVEN_DAYS_RENT},
        {"Петр", "Денисов", "улица Братьев Кашириных", "Сокольники", "+79121234590", "21.03.2024",
            ONE_DAY_RENT}
    };
  }

  @Test
  public void putCorrectOrderDataThenGetOrderInfo() {
    driver.get(YANDEX_SAMOKAT_URL);
    YandexSamokatMain yandexSamokatMain = new YandexSamokatMain(driver);
    yandexSamokatMain.clickAcceptCookies();
    yandexSamokatMain.clickUpperOrderButton();
    CustomerInfo customerInfo = new CustomerInfo(driver);
    customerInfo.inputCustomerInfo(firstName, lastName, address, undergroundStation, phoneNumber);
    customerInfo.clickButtonNext();

    OrderInfo orderInfo = new OrderInfo(driver);
    orderInfo.inputOrderInfo(startRent, rentDays);
    orderInfo.clickLowerOrderButton();
    orderInfo.clickConfirmOrder();
    String orderInfoText = orderInfo.getOrderInfo();
    Assert.assertTrue(
        "Всплывающее окно о подтверждении заказа не появилось. Текст заказа: " + orderInfoText,
        orderInfoText.startsWith("Заказ оформлен"));
  }

  @Test
  public void clickLowerOrderButtonThenOpenOrderWindow() {
    driver.get(YANDEX_SAMOKAT_URL);
    YandexSamokatMain yandexSamokatMain = new YandexSamokatMain(driver);
    yandexSamokatMain.clickAcceptCookies();
    yandexSamokatMain.clickLowerOrderButton();
    CustomerInfo customerInfo = new CustomerInfo(driver);
    String actualTitle = customerInfo.getTitleText();
    Assert.assertEquals("Ожидалось открытие окна с вводом данных для заказа, содержащих заголовок: "
            + CUSTOMER_INFO_PAGE_TITLE + ". Открылось окно с заголовком: " + actualTitle,
        CUSTOMER_INFO_PAGE_TITLE, actualTitle);
  }
}
