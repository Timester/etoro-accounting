package hu.tomosvari.etoro;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class TransactionStatisticsServiceTest {

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
    void sumProfit() {
        BigDecimal sum = TransactionStatisticsService.sumProfit(transactions);

        assertEquals(10, sum.intValue());
    }

    @Test
    void calculateAverage() {
        BigDecimal avg = TransactionStatisticsService.calculateAverage(transactions);

        assertEquals(2.5, avg.doubleValue());
    }

    @Test
    void countGainingTransactions() {
        long count = TransactionStatisticsService.countGainingTransactions(transactions);

        assertEquals(3, count);
    }

    @Test
    void countLosingTransactions() {
        long count = TransactionStatisticsService.countLosingTransactions(transactions);

        assertEquals(1, count);
    }

    @Test
    void getBestTransaction() {
        Transaction bestTransaction = TransactionStatisticsService.getBestTransaction(transactions);

        assertEquals("4", bestTransaction.getPositionId());
    }

    @Test
    void getWorstTransaction() {
        Transaction worstTransaction = TransactionStatisticsService.getWorstTransaction(transactions);

        assertEquals("3", worstTransaction.getPositionId());
    }
}