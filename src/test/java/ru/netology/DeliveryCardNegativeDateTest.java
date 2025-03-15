package ru.netology;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selectors;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;
import ru.netology.data.DataGenerator;

import java.time.Duration;
import java.util.Locale;

import static com.codeborne.selenide.Selenide.$;
import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DeliveryCardNegativeDateTest {

    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }

    @BeforeEach
    public  void setup() {
        given()
                .baseUri("http://localhost:9999")
                .contentType("text/html; charset=UTF-8")
                .when()
                .get("/")
                .then()
                .statusCode(200);

        Selenide.open("http://localhost:9999");
        $("h2").shouldBe(Condition.visible, Condition.text("Карта с доставкой!"));

    }

    @Test
    public void shouldNotCreateRequestForCardWithoutDate() {
        var validUser = DataGenerator.generateUser(new Locale("ru"));

        $("[data-test-id='city'] input").setValue(validUser.getCity());
        $("[data-test-id='date'] input").doubleClick().sendKeys(Keys.BACK_SPACE);

        $("[data-test-id='name'] input").shouldBe(Condition.visible).setValue(validUser.getName());
        $("[data-test-id='phone'] input").shouldBe(Condition.visible).setValue(validUser.getPhoneNumber());
        $("[data-test-id='agreement']").shouldBe(Condition.visible).click();
        $(Selectors.withText("Запланировать")).shouldBe(Condition.visible).click();

        var resultMessage = $("[data-test-id='date'] .input__sub").shouldBe(Condition.text("Неверно введена дата"));

        assertTrue(resultMessage.isDisplayed());
        assertEquals("Неверно введена дата", resultMessage.getText());

    }

    @Test
    public void shouldNotCreateRequestForCardWithCurrentDate() {
        var validUser = DataGenerator.generateUser(new Locale("ru"));
        String planingDate = DataGenerator.generateCurrentDate();


        $("[data-test-id='city'] input").setValue(validUser.getCity());
        $("[data-test-id='date'] input").doubleClick().sendKeys(Keys.BACK_SPACE);
        $("[data-test-id='date'] input").setValue(planingDate).shouldBe(Condition.visible);
        $("[data-test-id='name'] input").shouldBe(Condition.visible).setValue(validUser.getName());
        $("[data-test-id='phone'] input").shouldBe(Condition.visible).setValue(validUser.getPhoneNumber());
        $("[data-test-id='agreement']").shouldBe(Condition.visible).click();
        $(Selectors.withText("Запланировать")).shouldBe(Condition.visible).click();

        var resultMessage = $("[data-test-id='date'] .input__sub").shouldBe(Condition.text("Заказ на выбранную дату невозможен"));

        assertTrue(resultMessage.isDisplayed());
        assertEquals("Заказ на выбранную дату невозможен", resultMessage.getText());

    }

    @Test
    public void shouldNotChangeMeetingDateWithoutDate() {
        String planingDate = DataGenerator.generatePlaningDate();

        var validUser = DataGenerator.generateUser(new Locale("ru"));

        $("[data-test-id='city'] input").setValue(validUser.getCity());
        $("[data-test-id='date'] input").doubleClick().sendKeys(Keys.BACK_SPACE);
        $("[data-test-id='date'] input").setValue(planingDate).shouldBe(Condition.visible);
        $("[data-test-id='name'] input").shouldBe(Condition.visible).setValue(validUser.getName());
        $("[data-test-id='phone'] input").shouldBe(Condition.visible).setValue(validUser.getPhoneNumber());
        $("[data-test-id='agreement']").shouldBe(Condition.visible).click();
        $(Selectors.withText("Запланировать")).shouldBe(Condition.visible).click();
        $(".notification__content").shouldBe(Condition.text("Встреча успешно запланирована на " + planingDate), Duration.ofSeconds(15));
        $("span.icon.icon_size_s.icon_name_close.icon_theme_alfa-on-color").click();
        $("[data-test-id='date'] input").doubleClick().sendKeys(Keys.BACK_SPACE);

        $(Selectors.withText("Запланировать")).shouldBe(Condition.visible).click();

        var resultMessage = $("[data-test-id='date'] .input__sub").shouldBe(Condition.text("Неверно введена дата"));

        assertTrue(resultMessage.isDisplayed());
        assertEquals("Неверно введена дата", resultMessage.getText());

    }

    @Test
    public void shouldNotChangeMeetingDateWithCurrentDate() {
        String planingDate = DataGenerator.generatePlaningDate();
        String newPlaningDate = DataGenerator.generateCurrentDate();

        var validUser = DataGenerator.generateUser(new Locale("ru"));

        $("[data-test-id='city'] input").setValue(validUser.getCity());
        $("[data-test-id='date'] input").doubleClick().sendKeys(Keys.BACK_SPACE);
        $("[data-test-id='date'] input").setValue(planingDate).shouldBe(Condition.visible);
        $("[data-test-id='name'] input").shouldBe(Condition.visible).setValue(validUser.getName());
        $("[data-test-id='phone'] input").shouldBe(Condition.visible).setValue(validUser.getPhoneNumber());
        $("[data-test-id='agreement']").shouldBe(Condition.visible).click();
        $(Selectors.withText("Запланировать")).shouldBe(Condition.visible).click();
        $(".notification__content").shouldBe(Condition.text("Встреча успешно запланирована на " + planingDate), Duration.ofSeconds(15));
        $("span.icon.icon_size_s.icon_name_close.icon_theme_alfa-on-color").click();
        $("[data-test-id='date'] input").doubleClick().sendKeys(Keys.BACK_SPACE);
        $("[data-test-id='date'] input").setValue(newPlaningDate).shouldBe(Condition.visible);
        $(Selectors.withText("Запланировать")).shouldBe(Condition.visible).click();

        var resultMessage = $("[data-test-id='date'] .input__sub").shouldBe(Condition.text("Заказ на выбранную дату невозможен"));

        assertTrue(resultMessage.isDisplayed());
        assertEquals("Заказ на выбранную дату невозможен", resultMessage.getText());

    }
}
