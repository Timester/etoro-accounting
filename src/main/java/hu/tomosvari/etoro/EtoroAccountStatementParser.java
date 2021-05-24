package hu.tomosvari.etoro;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

@Slf4j
public class EtoroAccountStatementParser {
    public static Set<Transaction> parseTransactions(XSSFWorkbook workbook) {
        Sheet sheet = workbook.getSheetAt(1);
        Iterator<Row> rowIterator = sheet.rowIterator();

        Set<Transaction> transactions = new HashSet<>();

        if (rowIterator.hasNext()) {
            rowIterator.next();
            log.debug("Skipping first row (header)");
        }

        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();

            try {
                Transaction transaction = Transaction.builder()
                        .positionId(row.getCell(0).getStringCellValue())
                        .profit(new BigDecimal(row.getCell(8).getStringCellValue()))
                        .closedAt(LocalDateTime.parse(row.getCell(10).getStringCellValue(), DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")))
                        .isReal("Real".equals(row.getCell(14).getStringCellValue()))
                        .build();

                transactions.add(transaction);
            } catch (Exception e) {
                log.warn("Error during parsing a transaction, skipping", e);
            }
        }

        return transactions;
    }
}
