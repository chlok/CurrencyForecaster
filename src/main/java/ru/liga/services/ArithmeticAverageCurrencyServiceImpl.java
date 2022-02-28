package ru.liga.services;

import org.apache.commons.math3.util.Precision;
import ru.liga.forms.DailyCurrency;
import ru.liga.repositories.CurrencyRepository;

import java.text.NumberFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import static ru.liga.constants.Constants.*;

/**
 * CurrencyService implementation which forecasts future currency values on the base
 * of previous week values
 */
public class ArithmeticAverageCurrencyServiceImpl implements CurrencyService {
    private final CurrencyRepository repository;

    public ArithmeticAverageCurrencyServiceImpl(CurrencyRepository repository) {
        this.repository = repository;
    }

    /**
     * @return new DaileCurrencyValue as a forecast for tomorrow
     */
    @Override
    public DailyCurrency getTomorrowCurrencyForecast() {
        LinkedList<DailyCurrency> currencyValues = getDailyCurrencyList(repository.getCurrencyStringsForPeriod(WEEK_PERIOD));
        return getNextDayCurrencyForecast(currencyValues);
    }

    /**
     * @return list of new DaileCurrencyValue objects as a forecast for the week
     */
    @Override
    public List<DailyCurrency> getWeekCurrencyForecast() {
        LinkedList<DailyCurrency> currencyValues = getDailyCurrencyList(repository.getCurrencyStringsForPeriod(WEEK_PERIOD));
        for (int i = 0; i < WEEK_PERIOD; i++) {
            DailyCurrency newDailyCurrency = getNextDayCurrencyForecast(currencyValues);
            currencyValues.offer(newDailyCurrency);
            currencyValues.remove();
        }
        return currencyValues;
    }

    /**
     * @param dailyCurrency prints dailyCurrency into the console
     */
    @Override
    public void printDailyCurrency(DailyCurrency dailyCurrency) {
        StringBuilder sb = new StringBuilder();
        sb.append(getRussianDayOfWeek(dailyCurrency.getDate()));
        sb.append(" ");
        sb.append(getStringFromDate(dailyCurrency.getDate()));
        sb.append(" ");
        sb.append(dailyCurrency.getValue());
        System.out.println(sb);
    }

    /**
     * @param currencyValues sorted by descending list of DailyCurrencyValue objects
     * @return next day after the latest day of the list
     */
    private DailyCurrency getNextDayCurrencyForecast(LinkedList<DailyCurrency> currencyValues) {
        double nextDayValue = roundToPlaces(calculateAverageValue(currencyValues));
        LocalDate nextDay = currencyValues.getLast().getDate().plusDays(ONE_DAY_PERIOD);
        return new DailyCurrency(nextDay, nextDayValue);
    }

    /**
     * @param currencyValues sorted by descending list of DailyCurrencyValue objects
     * @return average value of the currencyValues
     */
    private double calculateAverageValue(List<DailyCurrency> currencyValues) {
        return currencyValues.stream()
                .mapToDouble(DailyCurrency::getValue)
                .average()
                .orElseThrow(() -> new IllegalArgumentException(DATA_PROCESSING_EXCEPTION_MESSAGE));
    }

    /**
     * @param num
     * @return number rounded to PLACES
     */
    private Double roundToPlaces(Double num) {
        return Precision.round(num, PLACES);
    }

    private LinkedList<DailyCurrency> getDailyCurrencyList(List<List<String>> stringListsList) {
        LinkedList<DailyCurrency> dailyCurrencyList = new LinkedList<>();
        for (List<String> stringList : stringListsList) {
            try {
                dailyCurrencyList.add(mapIntoDailyCurrency(stringList));
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException(DATA_PROCESSING_EXCEPTION_MESSAGE);
            }
        }
        dailyCurrencyList.sort(Comparator.comparing(DailyCurrency::getDate));
        return dailyCurrencyList;
    }

    /**
     * @param dailyCurrencyStringList list of strings containing information about dailyCurrency
     * @return DailyCurrency object
     */
    private DailyCurrency mapIntoDailyCurrency(List<String> dailyCurrencyStringList) {
        LocalDate date = parseStringIntoLocalDate(dailyCurrencyStringList.get(0));
        double value = getDoubleValueFromStringWithComma(dailyCurrencyStringList.get(1));
        return new DailyCurrency(date, value);
    }

    /**
     * @param stringDate date information from the file in String-format
     * @return date information as a LocalDate object
     */
    private LocalDate parseStringIntoLocalDate(String stringDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        return LocalDate.parse(stringDate, formatter);
    }

    /**
     * @param string
     * @return double value
     */
    private double getDoubleValueFromStringWithComma(String string) {
        NumberFormat format = NumberFormat.getInstance(Locale.getDefault());
        Number number = null;
        try {
            number = format.parse(string);
        } catch (ParseException e) {
            throw new IllegalArgumentException("string is not parseable");
        }
        return number.doubleValue();
    }

    /**
     * @return reduced day of week name in Russian language
     */
    private String getRussianDayOfWeek(LocalDate date) {
        return date.getDayOfWeek().getDisplayName(TextStyle.SHORT, new Locale("ru", "RU"));
    }

    /**
     * @return date in String-format(for the Console Output)
     */
    private String getStringFromDate(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        return date.format(formatter);
    }
}
