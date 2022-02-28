package ru.liga.repositories;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * CurrencyRepository implementation which takes information from csv-file
 */
public class CSVFileCurrencyRepositoryImpl implements CurrencyRepository {
    private final String fileName;

    public CSVFileCurrencyRepositoryImpl(String fileName) {
        this.fileName = fileName;
    }

    /**
     * @param days number of the last days we take from the file
     * @return list of strings lists with dailyCurrency information
     */
    public List<List<String>> getCurrencyStringsForPeriod(int days) {
        List<List<String>> currencies = new LinkedList<>();
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName))) {
            bufferedReader.readLine();
            for (int i = 0; i < days; i++) {
                String[] fileStringArray = bufferedReader.readLine().split("[; ]");
                currencies.add(Arrays.asList(fileStringArray));
            }
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
        return currencies;
    }
}

