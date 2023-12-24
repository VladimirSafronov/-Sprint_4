import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import pages.CustomerInfo;
import pages.OrderInfo;
import pages.YandexSamokatMain;

@RunWith(Parameterized.class)
public class MakeOrderYandexSamokatTest extends BaseTest {

  private final String firstName;
  private final String lastName;
  private final String address;
  private final String undergroundStation;
  private final String phoneNumber;

  public MakeOrderYandexSamokatTest(String firstName, String lastName, String address,
      String undergroundStation, String phoneNumber) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.address = address;
    this.undergroundStation = undergroundStation;
    this.phoneNumber = phoneNumber;
  }

  @Parameters
  public static Object[][] getCredentials() {
    return new Object[][]{
        {"Иван", "Иванов", "проспект Свободы", "Алексеевская", "+79001234567"},
        {"Петр", "Денисов", "улица Братьев Кашириных", "Сокольники", "+79121234590"}
    };
  }

  @Test
  public void putCorrectOrderDataThenGetOrderInfo() {
    YandexSamokatMain yandexSamokatMain = new YandexSamokatMain(driver);
    yandexSamokatMain.clickAcceptCookies();
    yandexSamokatMain.clickUpOrderButton();
    CustomerInfo customerInfo = new CustomerInfo(driver);
    customerInfo.inputCustomerInfo(firstName, lastName, address, undergroundStation, phoneNumber);
    customerInfo.clickButtonNext();

    OrderInfo orderInfo = new OrderInfo(driver);
    orderInfo.inputOrderInfo();
    orderInfo.clickLowerOrderButton();
    orderInfo.clickConfirmOrder();
    String orderInfoText = orderInfo.getOrderInfo();
    Assert.assertTrue(
        "Всплывающее окно о подтверждении заказа не появилось. Текст заказа: " + orderInfoText,
        orderInfoText.startsWith("Заказ оформлен"));
  }
}
