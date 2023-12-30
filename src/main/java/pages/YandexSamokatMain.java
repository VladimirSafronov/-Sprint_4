package pages;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Page Object главной страницы Яндекс Самокат
 */
public class YandexSamokatMain {

  private static final String IMPORTANT_QUESTIONS_BLOCK_XPATH = ".//div[@class='accordion']";
  private static final String QUESTION_BUTTON_XPATH = ".//div[@class='accordion__button']";
  private static final String ORDER_BUTTONS_XPATH = ".//button[text()='Заказать']";
  private static final String CONFIRM_COOKIES_BUTTON_XPATH = ".//button[@id='rcc-confirm-button']";

  private final WebDriver driver;
  private final WebDriverWait wait;

  //блок с частыми вопросами
  private final WebElement importantQuestionsBlock;
  //Список кнопок с вопросами
  private final List<WebElement> questionButtons;
  //список частых вопросов-ответов
  private final Map<String, String> questionsAnswers;
  //верхняя кнопка Заказать
  private final WebElement upperOrderButton;
  //нижняя кнопка Заказать
  private final WebElement lowerOrderButton;
  //кнопка Принятия cookies
  private final WebElement confirmCookiesButton;

  public YandexSamokatMain(WebDriver driver) {
    this.driver = driver;
    this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));

    wait.until(ExpectedConditions.elementToBeClickable(By.xpath(IMPORTANT_QUESTIONS_BLOCK_XPATH)));
    this.importantQuestionsBlock = driver.findElement(By.xpath(IMPORTANT_QUESTIONS_BLOCK_XPATH));

    this.questionButtons = driver.findElements(By.xpath(QUESTION_BUTTON_XPATH));
    questionsAnswers = new HashMap<>();

    wait.until(ExpectedConditions.elementToBeClickable(By.xpath(ORDER_BUTTONS_XPATH)));
    this.upperOrderButton = driver.findElements(By.xpath(ORDER_BUTTONS_XPATH)).get(0);
    this.lowerOrderButton = driver.findElements(By.xpath(ORDER_BUTTONS_XPATH)).get(1);

    wait.until(ExpectedConditions.elementToBeClickable(By.xpath(CONFIRM_COOKIES_BUTTON_XPATH)));
    this.confirmCookiesButton = driver.findElement(By.xpath(CONFIRM_COOKIES_BUTTON_XPATH));
  }

  /**
   * Метод принимает cookies нажимая на кнопку Все привыкли
   */
  public void clickAcceptCookies() {
    confirmCookiesButton.click();
  }

  /**
   * Производит скроллинг мышью до выбранного элемета
   */
  public void scrollToElement(WebElement element) {
    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView();", element);
  }

  /**
   * Кликает по верхней кнопке Зказать
   */
  public void clickUpperOrderButton() {
    upperOrderButton.click();
  }

  /**
   * Кликает по нижней кнопке Зказать
   */
  public void clickLowerOrderButton() {
    lowerOrderButton.click();
  }

  /**
   * Метод загружает все вопросы и ответы на них в хранилище
   */
  public void loadQuestionsAnswers() {
    for (int i = 0; i < questionButtons.size(); i++) {
      wait.until(ExpectedConditions.elementToBeClickable(questionButtons.get(i)));
      questionButtons.get(i).click();
      String question = getQuestionOrAnswerText(true, i);
      String answer = getQuestionOrAnswerText(false, i);
      System.out.println("Question: " + question + " Answer: " + answer);
      questionsAnswers.put(question, answer);
    }
  }

  /**
   * Метод возвращает ответ на конкретный вопрос в блоке Вопросы о важном. Если такого вопроса нет,
   * вернет пустую строку.
   */
  public String getAnswer(String question) {
    String answer = "";
    for (int i = 0; i < questionButtons.size(); i++) {
      if (questionButtons.get(i).getText().equals(question)) {
        wait.until(ExpectedConditions.elementToBeClickable(questionButtons.get(i))).click();
        wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath(
            ".//div[@id='accordion__panel-" + i + "']"))));
        answer = driver.findElement(By.xpath(
            ".//div[@id='accordion__panel-" + i + "']")).getText();
        break;
      }
    }
    return answer;
  }

  /**
   * Метод находит текст из блока Вопросы о важном
   *
   * @param isQuestion - производится ли поиск вопроса
   * @param indexList  - порядковый номер искомого элемента в списке
   */
  private String getQuestionOrAnswerText(boolean isQuestion, int indexList) {
    return isQuestion ? driver.findElement(By.xpath(
        ".//div[@id='accordion__heading-" + indexList + "']")).getText() :
        driver.findElement(By.xpath(
            ".//div[@id='accordion__panel-" + indexList + "']")).getText();
  }

  public Map<String, String> getQuestionsAnswers() {
    return questionsAnswers;
  }

  public WebElement getImportantQuestionsBlock() {
    return importantQuestionsBlock;
  }
}
