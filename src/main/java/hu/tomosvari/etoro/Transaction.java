package hu.tomosvari.etoro;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class Transaction {
    private final String positionId;
    private final LocalDateTime closedAt;
    private final BigDecimal profit;
    private final boolean isReal;
}
