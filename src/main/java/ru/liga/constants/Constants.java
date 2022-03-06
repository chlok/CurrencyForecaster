package ru.liga.constants;

import java.time.format.DateTimeFormatter;

public class Constants {
    public static final String EUR_FILEPATH = "/EUR_F01_02_2002_T01_02_2022.csv";
    public static final String TRY_FILEPATH = "/TRY_F01_02_2002_T01_02_2022.csv";
    public static final String USD_FILEPATH = "/USD_F01_02_2002_T01_02_2022.csv";
    public static final String TRY = "TRY";
    public static final String USD = "USD";
    public static final String EUR = "EUR";
    public static final String TOMORROW = "tomorrow";
    public static final String WEEK = "week";
    public static final int PLACES = 4;
    public static final int WEEK_PERIOD = 7;
    public static final int ONE_DAY_PERIOD = 1;
    public static final String RATE_COMMAND = "rate";
    public static final String DATA_PROCESSING_EXCEPTION_MESSAGE = "resources information can't be processed";
    public static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
}
