package hu.tomosvari.etoro;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
@Builder
public class Transaction {
    private final String positionId;
    private final LocalDateTime closedAt;
    private final BigDecimal profit;
    private final boolean isReal;
}
