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

import java.util.Locale;

import static com.codeborne.selenide.Selenide.$;
import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DeliveryCardNegativeCityTest {

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
    public void shouldNotCreateRequestForCardWithNotCity() {
        var validUser = DataGenerator.generateUser(new Locale("ru"));
        String planingDate = DataGenerator.generatePlaningDate();


        $("[data-test-id='date'] input").doubleClick().sendKeys(Keys.BACK_SPACE);
        $("[data-test-id='date'] input").setValue(planingDate).shouldBe(Condition.visible);
        $("[data-test-id='name'] input").shouldBe(Condition.visible).setValue(validUser.getName());
        $("[data-test-id='phone'] input").shouldBe(Condition.visible).setValue(validUser.getPhoneNumber());
        $("[data-test-id='agreement']").shouldBe(Condition.visible).click();
        $(Selectors.withText("Запланировать")).shouldBe(Condition.visible).click();

        var resultMessage = $("[data-test-id='city'] .input__sub").shouldBe(Condition.text("Поле обязательно для заполнения"));

        assertTrue(resultMessage.isDisplayed());
        assertEquals("Поле обязательно для заполнения", resultMessage.getText());

    }


    @Test
    public void shouldNotCreateRequestForCardWithCityInEnglich() {
        var validUser = DataGenerator.generateUser(new Locale("ru"));
        String planingDate = DataGenerator.generatePlaningDate();
        String city = DataGenerator.generateCity(Locale.ENGLISH);

        $("[data-test-id='city'] input").setValue(city);
        $("[data-test-id='date'] input").doubleClick().sendKeys(Keys.BACK_SPACE);
        $("[data-test-id='date'] input").setValue(planingDate).shouldBe(Condition.visible);
        $("[data-test-id='name'] input").shouldBe(Condition.visible).setValue(validUser.getName());
        $("[data-test-id='phone'] input").shouldBe(Condition.visible).setValue(validUser.getPhoneNumber());
        $("[data-test-id='agreement']").shouldBe(Condition.visible).click();
        $(Selectors.withText("Запланировать")).shouldBe(Condition.visible).click();

        var resultMessage = $("[data-test-id='city'] .input__sub").shouldBe(Condition.text("Доставка в выбранный город недоступна"));

        assertTrue(resultMessage.isDisplayed());
        assertEquals("Доставка в выбранный город недоступна", resultMessage.getText());

    }

    @Test
    public void shouldNotCreateRequestForCardWithCityAnotherCountry() {
        var validUser = DataGenerator.generateUser(new Locale("ru"));
        String planingDate = DataGenerator.generatePlaningDate();
        String city = DataGenerator.generateRandomCity(new Locale("en", "fr"));

        $("[data-test-id='city'] input").setValue(city);
        $("[data-test-id='date'] input").doubleClick().sendKeys(Keys.BACK_SPACE);
        $("[data-test-id='date'] input").setValue(planingDate).shouldBe(Condition.visible);
        $("[data-test-id='name'] input").shouldBe(Condition.visible).setValue(validUser.getName());
        $("[data-test-id='phone'] input").shouldBe(Condition.visible).setValue(validUser.getPhoneNumber());
        $("[data-test-id='agreement']").shouldBe(Condition.visible).click();
        $(Selectors.withText("Запланировать")).shouldBe(Condition.visible).click();

        var resultMessage = $("[data-test-id='city'] .input__sub").shouldBe(Condition.text("Доставка в выбранный город недоступна"));

        assertTrue(resultMessage.isDisplayed());
        assertEquals("Доставка в выбранный город недоступна", resultMessage.getText());

    }
}
