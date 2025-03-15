package ru.netology;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selectors;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import org.openqa.selenium.Keys;
import ru.netology.data.DataGenerator;

import java.time.Duration;
import java.util.Locale;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DeliveryCardTest {

    // .\gradlew allureserve - команда для запуска аллюре
    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }


    @BeforeEach
    public void setup() {
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
    public void shouldChangeMeetingDate() {
        String planingDate = DataGenerator.generatePlaningDate();
        String newPlaningDate = DataGenerator.generatePlaningDate();

        var validUser = DataGenerator.generateUser(new Locale("ru"));

        $("[data-test-id='city'] input").setValue(validUser.getCity());
        $("[data-test-id='date'] input").doubleClick().sendKeys(Keys.BACK_SPACE);
        $("[data-test-id='date'] input").setValue(planingDate).shouldBe(Condition.visible);
        $("[data-test-id='name'] input").shouldBe(Condition.visible).setValue(validUser.getName());
        $("[data-test-id='phone'] input").shouldBe(Condition.visible).setValue(validUser.getPhoneNumber());
        $("[data-test-id='agreement']").shouldBe(Condition.visible).click();
        $(Selectors.withText("Запланировать")).shouldBe(Condition.visible).click();
        $(".notification__content").shouldBe(Condition.text("Встреча успешно запланирована на " + planingDate), Duration.ofSeconds(15));
        $("[data-test-id='date'] input").doubleClick().sendKeys(Keys.BACK_SPACE);
        $("[data-test-id='date'] input").setValue(newPlaningDate).shouldBe(Condition.visible);
        $(Selectors.withText("Запланировать")).shouldBe(Condition.visible).click();
        $("[data-test-id='replan-notification'] .notification__content").shouldBe(Condition.text("У вас уже запланирована встреча на другую дату. Перепланировать?"),
                Duration.ofSeconds(15));
        $("[data-test-id='replan-notification'] button.button").shouldBe(Condition.visible).click();

        var result = $(".notification__content");
        var resultMessage = $(".notification__content").shouldBe(Condition.text("Встреча успешно запланирована на " + newPlaningDate));

        assertTrue(result.isDisplayed());
        assertEquals("Встреча успешно запланирована на " + newPlaningDate, resultMessage.getText());


    }


}
