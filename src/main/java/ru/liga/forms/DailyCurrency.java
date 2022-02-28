package ru.liga.forms;

import java.time.LocalDate;


/**
 * class that describes daily currency value with value and date as fields
 */
public class DailyCurrency {
    private final LocalDate date;
    private final double value;

    public DailyCurrency(LocalDate date, double value) {
        this.date = date;
        this.value = value;
    }

    public LocalDate getDate() {
        return date;
    }

    public double getValue() {
        return value;
    }
}
