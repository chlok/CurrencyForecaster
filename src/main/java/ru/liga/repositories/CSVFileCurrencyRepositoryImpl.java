package ru.liga.repositories;

import java.io.*;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * CurrencyRepository implementation which takes information from csv-file
 */
public class CSVFileCurrencyRepositoryImpl implements CurrencyRepository {
    private static final String CSV_SEPARATOR = ";";

    /**
     * to get currency values for the period
     * @param days number of the last days we take from the file
     * @return list of strings lists with dailyCurrency information
     */
    public List<List<String>> getCurrenciesForPeriod(String fileName, int days) {
        List<List<String>> currencies = new LinkedList<>();

        InputStream inputStream = getClass().getResourceAsStream(fileName);
        if (inputStream == null){
            return null;
        }
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream))) {
            bufferedReader.readLine();
            for (int i = 0; i < days; i++) {
                String[] fileStringArray = bufferedReader.readLine().split(CSV_SEPARATOR);

                currencies.add(Arrays.asList(fileStringArray));
            }
            return currencies;
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }
}

