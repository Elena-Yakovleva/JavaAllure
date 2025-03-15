package ru.netology.data;

import com.github.javafaker.Faker;
import lombok.Value;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Random;

import static java.lang.Math.random;

public class DataGenerator {


    private DataGenerator() {

    }

    public static String generateCurrentDate() {
        return LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    }

    public static String generatePlaningDate() {
        Random random = new Random();
        int days = random.nextInt(89) + 4;
        return LocalDate.now().plusDays(days).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    }

    private static String[] citiesRussianRu = { "Майкоп", "Горно-Алтайск", "Уфа", "Улан-Удэ", "Махачкала", "Магас", "Нальчик", "Петразоводск",
                "Сыктывкар", "Йошкар-Ола", "Саранск", "Якутск", "Владикавказ", "Казань", "Кызыл", "Ижевск", "Абакан", "Грозный",
                "Чебоксары", "Барнаул", "Чита", "Петропавловск-Камчатский", "Краснодар", "Красноярск", "Пермь", "Ставрополь",
                "Хабаровск", "Благовещенск", "Архангельск", "Астрахань", "Белгород", "Брянск", "Владимир", "Волгоград", "Вологда",
                "Воронеж", "Иваново", "Иркутск", "Калининград", "Калуга", "Кемерово", "Киров", "Кострома", "Курган", "Курск",
                "Санкт-Петербург", "Липецк", "Магадан", "Москва", "Мурманск", "Нижний Новгород", "Великий Новгород",
                "Новосибирск", "Омск", "Оренбург", "Орел", "Пенза", "Псков", "Ростов-на-Дону", "Рязань", "Самара", "Саратов",
                "Южно-Сахалинск", "Екатеринбург", "Смоленск", "Тамбов", "Тверь", "Томск", "Тула", "Тюмень", "Ульяновск", "Челябинск",
                "Ярославль", "Нарьян-Мар", "Ханты-Мансийск", "Анадырь", "Салехард", "Биробиджан"
    };

    private static String[] citiesRussianEn = {
            "Maykop", "Gorno-Altaisk", "Ufa", "Ulan-Ude", "Makhachkala", "Magas", "Nalchik", "Petrozavodsk",
            "Syktyvkar", "Yoshkar-Ola", "Saransk", "Yakutsk", "Vladikavkaz", "Kazan", "Kyzyl", "Izhevsk", "Abakan", "Grozny",
            "Cheboksary", "Barnaul", "Chita", "Petropavlovsk-Kamchatsky", "Krasnodar", "Krasnoyarsk", "Perm", "Stavropol",
            "Khabarovsk", "Blagoveshchensk", "Arkhangelsk", "Astrakhan", "Belgorod", "Bryansk", "Vladimir", "Volgograd", "Vologda",
            "Voronezh", "Ivanovo", "Irkutsk", "Kaliningrad", "Kaluga", "Kemerovo", "Kirov", "Kostroma", "Kurgan", "Kursk",
            "Saint Petersburg", "Lipetsk", "Magadan", "Moscow", "Murmansk", "Nizhny Novgorod", "Veliky Novgorod",
            "Novosibirsk", "Omsk", "Orenburg", "Orel", "Penza", "Pskov", "Rostov-on-Don", "Ryazan", "Samara", "Saratov",
            "Yuzhno-Sakhalinsk", "Yekaterinburg", "Smolensk", "Tambov", "Tver", "Tomsk", "Tula", "Tyumen", "Ulyanovsk", "Chelyabinsk",
            "Yaroslavl", "Naryan-Mar", "Khanty-Mansiysk", "Anadyr", "Salekhard", "Birobidzhan"
    };


    public static String generateCity(Locale locale) {
        if (locale.equals(Locale.ENGLISH)) {
            return citiesRussianEn[new Random().nextInt(citiesRussianEn.length)];
        } else {
            return citiesRussianRu[new Random().nextInt(citiesRussianRu.length)];
        }
    }

    public static String generateRandomCity(Locale locale) {
        Faker faker = new Faker(locale);
        return faker.address().city();

    }

    public static String generateName(Locale locale) {
        Faker faker = new Faker(locale);

        return faker.name().lastName().replace("ё", "е") +  " " + faker.name().firstName().replace("ё", "е");
        //return faker.name().fullName(); вывод полного имени
    }

    public static String generatePhone(Locale locale) {

        return new Faker(locale).phoneNumber().phoneNumber();
    }


    @Value
     public static class UserInfo {
        String city;
        String name;
        String phoneNumber;
    }

    public static UserInfo generateUser(Locale locale) {

        return new UserInfo(generateCity(locale), generateName(locale), generatePhone(locale));
    }

}
