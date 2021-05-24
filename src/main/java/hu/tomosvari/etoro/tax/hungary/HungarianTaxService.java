package hu.tomosvari.etoro.tax.hungary;

import hu.tomosvari.etoro.Transaction;
import hu.tomosvari.etoro.TransactionDateComparator;
import hu.tomosvari.etoro.exchange.hungary.ExchangeRatesService;
import hu.tomosvari.etoro.exchange.hungary.UsdHufMNBRatesService;
import hu.tomosvari.etoro.tax.TaxService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

import static hu.tomosvari.etoro.TransactionStatisticsService.sumProfit;

@Slf4j
@RequiredArgsConstructor
public class HungarianTaxService implements TaxService {

    public static final String TAX_RATE = "0.15";

    private final ExchangeRatesService ratesService;

    @Override
    public BigDecimal calculateTax(Set<Transaction> transactions) {
        BigDecimal taxRate = new BigDecimal(TAX_RATE);
        List<Transaction> orderedTransactions = new ArrayList<>(transactions);
        orderedTransactions.sort(new TransactionDateComparator());

        Map<LocalDate, BigDecimal> exchangeRates = ratesService.getExchangeRates(
            orderedTransactions.get(0).getClosedAt().toLocalDate(),
            orderedTransactions.get(orderedTransactions.size() - 1).getClosedAt().toLocalDate());

        BigDecimal transactionsSumInHUF = BigDecimal.ZERO;

        for (Transaction transaction : orderedTransactions) {
            BigDecimal exchangeRate = getLatestExchangeRate(exchangeRates, transaction.getClosedAt().toLocalDate());
            BigDecimal profitInHuf = transaction.getProfit().multiply(exchangeRate);

            log.debug("Transaction {} closed at {} with profit ${} in HUF is {} using rate {}", transaction.getPositionId(), transaction.getClosedAt(), transaction.getProfit(), profitInHuf, exchangeRate);

            transactionsSumInHUF = transactionsSumInHUF.add(profitInHuf);
        }

        log.info("Profit in USD: ${}", sumProfit(transactions));
        log.info("Profit in HUF: {} HUF - exchanged each transaction on transaction close date MNB mid exchange rate.", transactionsSumInHUF);

        BigDecimal taxToPayInHUF = transactionsSumInHUF.multiply(taxRate);
        log.info("Tax to pay in HUF: {}, with tax rate of {}%", taxToPayInHUF, taxRate.multiply(new BigDecimal("100")));

        return taxToPayInHUF;
    }

    private BigDecimal getLatestExchangeRate(Map<LocalDate, BigDecimal> exchangeRates, LocalDate actualDate) {
        BigDecimal rate = exchangeRates.get(actualDate);

        return Objects.requireNonNullElseGet(rate, () -> getLatestExchangeRate(exchangeRates, actualDate.minusDays(1)));
    }
}
