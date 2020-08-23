package hu.tomosvari.etoro;

import java.util.Comparator;

public class TransactionDateComparator implements Comparator<Transaction> {
    @Override
    public int compare(Transaction o1, Transaction o2) {
        return o1.getClosedAt().compareTo(o2.getClosedAt());
    }
}
