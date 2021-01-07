package hu.tomosvari.etoro;

import java.util.Comparator;

public class TransactionValueComparator implements Comparator<Transaction> {
    @Override
    public int compare(Transaction o1, Transaction o2) {
        return o1.getProfit().compareTo(o2.getProfit());
    }
}
