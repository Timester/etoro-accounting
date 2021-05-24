package hu.tomosvari.etoro.exchange.hungary;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

public interface ExchangeRatesService {
    /**
     * Return an exchange rate for each day in the period specified by the from to values
     * @param from from date, inclusive
     * @param to to date, inclusive
     * @return A map of date - exchangerate
     */
    Map<LocalDate, BigDecimal> getExchangeRates(LocalDate from, LocalDate to);
}
