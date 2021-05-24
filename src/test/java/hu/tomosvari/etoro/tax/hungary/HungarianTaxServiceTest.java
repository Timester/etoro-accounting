package hu.tomosvari.etoro.tax.hungary;

import hu.tomosvari.etoro.Transaction;
import hu.tomosvari.etoro.exchange.hungary.UsdHufMNBRatesService;
import hu.tomosvari.etoro.tax.TaxService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HungarianTaxServiceTest {

    @Mock
    private UsdHufMNBRatesService exchangeRatesService;

    private static Set<Transaction> transactions;

    @BeforeAll
    static void init() {
        transactions = new HashSet<>();

        transactions.add(new Transaction("1", LocalDateTime.now(), new BigDecimal(1), true));
        transactions.add(new Transaction("2", LocalDateTime.now(), new BigDecimal(2), true));
        transactions.add(new Transaction("3", LocalDateTime.now(), new BigDecimal(-2), true));
        transactions.add(new Transaction("4", LocalDateTime.now(), new BigDecimal(9), true));
    }

    @Test
    void calculateTax() {
        Map<LocalDate, BigDecimal> rates = new HashMap<>();
        rates.put(LocalDate.of(2020, 12, 31), new BigDecimal(1));

        when(exchangeRatesService.getExchangeRates(any(), any())).thenReturn(rates);

        TaxService taxService = new HungarianTaxService(exchangeRatesService);
        BigDecimal tax = taxService.calculateTax(transactions);

        assertEquals(1.5, tax.doubleValue());
    }
}