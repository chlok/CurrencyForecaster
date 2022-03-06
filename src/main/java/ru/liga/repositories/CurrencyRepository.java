package ru.liga.repositories;

import java.util.List;

public interface CurrencyRepository {

    List<List<String>> getCurrenciesForPeriod(String fileName, int days);
}
