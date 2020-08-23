package hu.tomosvari.etoro.tax;

import hu.tomosvari.etoro.Transaction;
import hu.tomosvari.etoro.TransactionDateComparator;
import hu.tomosvari.etoro.exchange.ExchangeRatesService;
import hu.tomosvari.etoro.exchange.UsdHufMNBRatesService;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

import static hu.tomosvari.etoro.TransactionStatisticsService.sumProfit;

@Slf4j
public class HungarianTaxService implements TaxService {


    public static final String TAX_RATE = "0.15";

    @Override
    public BigDecimal calculateTax(Set<Transaction> transactions) {
        BigDecimal taxRate = new BigDecimal(TAX_RATE);
        List<Transaction> orderedTransactions = new ArrayList<>(transactions);
        orderedTransactions.sort(new TransactionDateComparator());

        ExchangeRatesService ratesService = new UsdHufMNBRatesService();
        Map<LocalDate, BigDecimal> exchangeRates = ratesService.getExchangeRates(
            orderedTransactions.get(0).getClosedAt().toLocalDate(),
            orderedTransactions.get(orderedTransactions.size() - 1).getClosedAt().toLocalDate());

        BigDecimal transactionsSumInHUF = BigDecimal.ZERO;

        for (Transaction transaction : orderedTransactions) {
            BigDecimal exchangeRate = getLatestExchangeRate(exchangeRates, transaction.getClosedAt().toLocalDate());
            BigDecimal profitInHuf = transaction.getProfit().multiply(exchangeRate);

            log.debug("Transaction {} with profit ${} in HUF is {} using rate {}", transaction.getPositionId(), transaction.getProfit(), profitInHuf, exchangeRate);

            transactionsSumInHUF = transactionsSumInHUF.add(profitInHuf);
        }

        HungarianTaxService.log.info("Profit in USD: ${}", sumProfit(transactions));
        HungarianTaxService.log.info("Profit in HUF: {} HUF - exchanged each transaction on transaction close date MNB mid exchange rate.", transactionsSumInHUF);

        BigDecimal taxToPayInHUF = transactionsSumInHUF.multiply(taxRate);
        HungarianTaxService.log.info("Tax to pay in HUF: {}, with tax rate of {}%", taxToPayInHUF, taxRate.multiply(new BigDecimal("100")));

        return taxToPayInHUF;
    }

    private BigDecimal getLatestExchangeRate(Map<LocalDate, BigDecimal> exchangeRates, LocalDate actualDate) {
        BigDecimal rate = exchangeRates.get(actualDate);

        return Objects.requireNonNullElseGet(rate, () -> getLatestExchangeRate(exchangeRates, actualDate.minusDays(1)));
    }
}
