package ru.liga.repositories;

import java.util.List;

public interface CurrencyRepository {

    List<List<String>> getCurrencyStringsForPeriod(int days);
}
