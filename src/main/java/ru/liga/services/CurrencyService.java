package ru.liga.services;

import ru.liga.forms.DailyCurrency;

import java.util.List;
import java.util.Queue;

public interface CurrencyService {

    DailyCurrency getTomorrowCurrencyForecast();

    List<DailyCurrency> getWeekCurrencyForecast();

    void printDailyCurrency(DailyCurrency dailyCurrency);
}
