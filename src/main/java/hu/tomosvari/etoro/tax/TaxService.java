package hu.tomosvari.etoro.tax;

import hu.tomosvari.etoro.Transaction;

import java.math.BigDecimal;
import java.util.Set;

public interface TaxService {
    BigDecimal calculateTax(Set<Transaction> transactions);
}
