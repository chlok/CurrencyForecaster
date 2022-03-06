package ru.liga.services;

import ru.liga.forms.DailyCurrency;

import java.util.List;

public interface CurrencyService {

    List<DailyCurrency> getForecast(String currency, int period);
}
