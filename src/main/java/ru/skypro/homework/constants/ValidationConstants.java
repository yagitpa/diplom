package ru.skypro.homework.constants;

public final class ValidationConstants {

    private ValidationConstants() {
    }

    // ------ Пользователь (авторизация и регистрация) ------
    public static final int USERNAME_MIN_SIZE = 10;
    public static final int USERNAME_MAX_SIZE = 254;

    public static final int PASSWORD_MIN_SIZE = 8;
    public static final int PASSWORD_MAX_SIZE = 16;

    public static final int FIRST_NAME_MIN_SIZE = 2;
    public static final int FIRST_NAME_MAX_SIZE = 50;
    public static final int LAST_NAME_MIN_SIZE = 2;
    public static final int LAST_NAME_MAX_SIZE = 50;

    // ----- Телефон -----
    public static final String PHONE_PATTERN = "\\+7\\s?\\(?\\d{3}\\)?\\s?\\d{3}-?\\d{2}-?\\d{2}";
    public static final String PHONE_EXAMPLE = "+7 (999) 999-99-99";

    // ----- Объявления -----
    public static final int AD_TITLE_MIN_SIZE = 4;
    public static final int AD_TITLE_MAX_SIZE = 100;

    public static final int AD_DESCRIPTION_MIN_SIZE = 8;
    public static final int AD_DESCRIPTION_MAX_SIZE = 512;

    public static final int AD_PRICE_MIN = 0;
    public static final int AD_PRICE_MAX = 10_000_000;

    // ----- Комментарии -----
    public static final int COMMENT_TEXT_MIN_SIZE = 8;
    public static final int COMMENT_TEXT_MAX_SIZE = 512;
}
