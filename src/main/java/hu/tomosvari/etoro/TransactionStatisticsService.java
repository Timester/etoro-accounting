package hu.tomosvari.etoro;

import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Set;

@Slf4j
public class TransactionStatisticsService {
    public static BigDecimal sumProfit(Set<Transaction> transactions) {
        return transactions.stream().map(Transaction::getProfit).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public static BigDecimal calculateAverage(Set<Transaction> transactions) {
        BigDecimal sum = sumProfit(transactions);
        return sum.divide(new BigDecimal(transactions.size()), RoundingMode.FLOOR);
    }

    public static long countGainingTransactions(Set<Transaction> transactions) {
        return transactions.stream().filter(x -> x.getProfit().compareTo(BigDecimal.ZERO) >= 0).count();
    }

    public static long countLosingTransactions(Set<Transaction> transactions) {
        return transactions.stream().filter(x -> x.getProfit().compareTo(BigDecimal.ZERO) < 0).count();
    }

    public static void printStatistics(Set<Transaction> transactions) {
        log.info("STATS - SUM: {}, COUNT: {}, GAINING: {}, LOSING: {}, AVG: {}, MIN: {}, MAX: {}",
            sumProfit(transactions),
            transactions.size(),
            countGainingTransactions(transactions),
            countLosingTransactions(transactions),
            calculateAverage(transactions),
            2,3);
    }
}
