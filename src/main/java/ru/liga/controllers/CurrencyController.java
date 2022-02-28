package ru.liga.controllers;

import ru.liga.forms.DailyCurrency;
import ru.liga.repositories.CSVFileCurrencyRepositoryImpl;
import ru.liga.repositories.CurrencyRepository;
import ru.liga.services.ArithmeticAverageCurrencyServiceImpl;
import ru.liga.services.CurrencyService;

import java.util.List;
import java.util.Scanner;

import static ru.liga.constants.Constants.*;

public class CurrencyController {

    private static CurrencyService currencyService;
    private static String currency;
    private static String period;

    /**
     * starts App
     */
    public static void start() {
        while (true) {
            System.out.println("enter your query or exit");
            String consoleLine = getConsoleInput();
            if (consoleLine.equals("exit")) {
                break;
            }
            if (!inputIsCorrect(consoleLine)) {
                System.out.println("Your input is not correct");
                continue;
            }
            currencyService = getCurrencyService(currency);
            if (currencyService == null) {
                continue;
            }
            try {
                displayResult(period, currencyService);
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    /**
     * displays the result of the query
     *
     * @param period          chosen period of forecast(tomorrow or week)
     * @param currencyService used CurrencyService Implementation
     */
    private static void displayResult(String period, CurrencyService currencyService) {
        switch (period) {
            case TOMORROW:
                currencyService.printDailyCurrency(currencyService.getTomorrowCurrencyForecast());
                break;
            case WEEK:
                printList(currencyService.getWeekCurrencyForecast());
                break;
            default:
                System.out.println("entered period is not available in App");
        }
    }

    /**
     * gets a query from user
     *
     * @return query in String-format
     */
    private static String getConsoleInput() {
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }


    /**
     * @param line user line from console
     * @return boolean about correctness of input and invokes currency and period setting
     */
    private static boolean inputIsCorrect(String line) {
        String[] inputStringArray = line.split(" ");
        if (inputStringArray.length == 3 && inputStringArray[0].equals(RATE_COMMAND)) {
            setCurrencyAndPeriod(inputStringArray);
            return true;
        }
        return false;
    }


    /**
     * @param inputStringArray gets cuurency and period from strings array
     */
    private static void setCurrencyAndPeriod(String[] inputStringArray) {
        currency = inputStringArray[1];
        period = inputStringArray[2];
    }

    /**
     * @param currency chosen currency
     * @return currency sevice depending on the chosen currency
     */
    private static CurrencyService getCurrencyService(String currency) {
        CurrencyRepository repository = null;
        switch (currency) {
            case TRY:
                repository = new CSVFileCurrencyRepositoryImpl(TRY_FILEPATH);
                break;
            case EUR:
                repository = new CSVFileCurrencyRepositoryImpl(EUR_FILEPATH);
                break;
            case USD:
                repository = new CSVFileCurrencyRepositoryImpl(USD_FILEPATH);
                break;
            default:
                System.out.println("Chosen currency is absent in the App or doesn't exist!");
                return null;
        }
        return new ArithmeticAverageCurrencyServiceImpl(repository);
    }

    /**
     * prints list of DailyCurrency objects
     *
     * @param dailyCurrencies list
     */
    private static void printList(List<DailyCurrency> dailyCurrencies) {
        for (DailyCurrency dailyCurrency : dailyCurrencies) {
            currencyService.printDailyCurrency(dailyCurrency);
        }
    }
}
