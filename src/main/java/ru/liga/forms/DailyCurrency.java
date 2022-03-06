package ru.liga.forms;

import com.opencsv.bean.CsvBindByName;

import java.time.LocalDate;
import java.util.Objects;


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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DailyCurrency that = (DailyCurrency) o;
        return Double.compare(that.value, value) == 0 && Objects.equals(date, that.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date, value);
    }

    @Override
    public String toString() {
        return "DailyCurrency{" +
                "date=" + date +
                ", value=" + value +
                '}';
    }
}
