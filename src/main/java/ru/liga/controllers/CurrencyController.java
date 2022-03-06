package ru.liga.controllers;

import ru.liga.forms.DailyCurrency;
import ru.liga.repositories.CSVFileCurrencyRepositoryImpl;
import ru.liga.services.ArithmeticAverageCurrencyServiceImpl;
import ru.liga.services.CurrencyService;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

import static ru.liga.constants.Constants.*;

public class CurrencyController {

    private final CurrencyService currencyService;
    private String currency;
    private String period;
    private final Scanner scanner;

    public CurrencyController() {
        this.currencyService = new ArithmeticAverageCurrencyServiceImpl(new CSVFileCurrencyRepositoryImpl());
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        while (true) {
            displayMenu();
            String consoleLine = scanner.nextLine();
            if (consoleLine.equals("exit")) {
                break;
            }
            if (!inputIsCorrect(consoleLine)) {
                System.out.println("Your input is not correct");
                continue;
            }
            try {
                displayResult(period, currency);
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    /**
     * prints dailyCurrency into the console
     *
     * @param dailyCurrency - currency for one day
     */
    public void printDailyCurrency(DailyCurrency dailyCurrency) {
        StringBuilder sb = new StringBuilder();
        sb.append(getStringFromDate(dailyCurrency.getDate()));
        sb.append(" ");
        sb.append(dailyCurrency.getValue());
        System.out.println(sb);
    }

    private void displayMenu() {
        System.out.println("enter your query like:");
        System.out.println("rate {currency} {period}");
        System.out.println("options for currency:");
        System.out.println(USD + " " + EUR + " " + TRY);
        System.out.println("options for period");
        System.out.println(TOMORROW + " " + WEEK);
    }


    private synchronized void displayResult(String period, String currency) {
        switch (period) {
            case TOMORROW:
                printList(currencyService.getForecast(currency, ONE_DAY_PERIOD));
                break;
            case WEEK:
                printList(currencyService.getForecast(currency, WEEK_PERIOD));
                break;
            default:
                System.out.println("entered period is not available in App");
        }
        System.out.println();
    }

    private boolean inputIsCorrect(String line) {
        String[] inputStringArray = line.split(" ");
        if (inputStringArray.length == 3 && inputStringArray[0].equals(RATE_COMMAND)) {
            setCurrencyAndPeriod(inputStringArray);
            return true;
        }
        return false;
    }

    private void setCurrencyAndPeriod(String[] inputStringArray) {
        currency = inputStringArray[1];
        period = inputStringArray[2];
    }


    private void printList(List<DailyCurrency> dailyCurrencies) {
        if (dailyCurrencies == null) {
            return;
        }
        for (DailyCurrency dailyCurrency : dailyCurrencies) {
            printDailyCurrency(dailyCurrency);
        }
    }

    private String getStringFromDate(LocalDate date) {
        return date.format(formatter);
    }
}
