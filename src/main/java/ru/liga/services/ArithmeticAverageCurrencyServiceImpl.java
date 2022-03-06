package ru.liga.services;

import org.apache.commons.math3.util.Precision;
import ru.liga.forms.DailyCurrency;
import ru.liga.repositories.CurrencyRepository;

import java.text.NumberFormat;
import java.text.ParseException;
import java.time.LocalDate;
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
     * get forecast for the period
     *
     * @param currency - chosen currency
     * @param period   - period
     * @return list of new DaileCurrencyValue objects as a forecast for the week
     */
    @Override
    public List<DailyCurrency> getForecast(String currency, int period) {
        String fileName = getFileName(currency);
        if (fileName == null) {
            System.out.println("this currency is absent in our forecaster");
            return null;
        }
        LinkedList<DailyCurrency> currencyValues = getCurrencyList(repository.getCurrenciesForPeriod(fileName, WEEK_PERIOD));
        for (int i = 0; i < period; i++) {
            DailyCurrency newDailyCurrency = getDailyForecast(currencyValues);
            currencyValues.offer(newDailyCurrency);
            currencyValues.remove();
        }
        return currencyValues;
    }

    private DailyCurrency getDailyForecast(LinkedList<DailyCurrency> currencyValues) {
        double nextDayValue = roundToPlaces(calculateAverageValue(currencyValues));
        LocalDate nextDay = currencyValues.getLast().getDate().plusDays(ONE_DAY_PERIOD);
        return new DailyCurrency(nextDay, nextDayValue);
    }

    private double calculateAverageValue(List<DailyCurrency> currencyValues) {
        return currencyValues.stream()
                .mapToDouble(DailyCurrency::getValue)
                .average()
                .orElseThrow(() -> new IllegalArgumentException(DATA_PROCESSING_EXCEPTION_MESSAGE));
    }

    private Double roundToPlaces(Double num) {
        return Precision.round(num, PLACES);
    }

    private LinkedList<DailyCurrency> getCurrencyList(List<List<String>> stringListsList) {
        LinkedList<DailyCurrency> dailyCurrencyList = new LinkedList<>();
        for (List<String> stringList : stringListsList) {
            try {
                dailyCurrencyList.add(mapToCurrency(stringList));
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException(DATA_PROCESSING_EXCEPTION_MESSAGE);
            }
        }
        dailyCurrencyList.sort(Comparator.comparing(DailyCurrency::getDate));
        return dailyCurrencyList;
    }

    private DailyCurrency mapToCurrency(List<String> dailyCurrencyStringList) {
        LocalDate date = parseString(dailyCurrencyStringList.get(0));
        double value = getDoubleFromStringWithComma(dailyCurrencyStringList.get(1));
        return new DailyCurrency(date, value);
    }

    private LocalDate parseString(String stringDate) {
        return LocalDate.parse(stringDate, formatter);
    }

    private double getDoubleFromStringWithComma(String string) {
        NumberFormat format = NumberFormat.getInstance(Locale.FRANCE);
        Number number = null;
        try {
            number = format.parse(string);
        } catch (ParseException e) {
            throw new IllegalArgumentException("string is not parseable");
        }
        return number.doubleValue();
    }

    private String getFileName(String currency) {
        switch (currency) {
            case USD:
                return USD_FILEPATH;
            case EUR:
                return EUR_FILEPATH;
            case TRY:
                return TRY_FILEPATH;
            default:
                return null;
        }
    }
}


















