package hu.tomosvari.etoro;

import hu.tomosvari.etoro.tax.HungarianTaxService;
import hu.tomosvari.etoro.tax.TaxService;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import static hu.tomosvari.etoro.TransactionStatisticsService.printStatistics;

@Slf4j
public class EtoroCalculator {
    public static void main(String[] args) {
        EtoroCalculator p = new EtoroCalculator();
        p.start(args[0]);
    }

    private void start(String fileLocation) {
        XSSFWorkbook workbook = null;
        try {
            workbook = readWorkbook(fileLocation);
        } catch (IOException e) {
            log.error("Could not read workbook.", e);
            System.exit(14);
        }

        Set<Transaction> transactions = parseTransactions(workbook);
        printStatistics(transactions);

        TaxService taxService = new HungarianTaxService();
        taxService.calculateTax(transactions);
    }

    private XSSFWorkbook readWorkbook(String fileLocation) throws IOException {
        try (FileInputStream file = new FileInputStream(new File(fileLocation))) {
            return new XSSFWorkbook(file);
        }
    }

    private Set<Transaction> parseTransactions(XSSFWorkbook workbook) {
        Sheet sheet = workbook.getSheetAt(1);
        Iterator<Row> rowIterator = sheet.rowIterator();

        Set<Transaction> transactions = new HashSet<>();

        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();

            if (row.getRowNum() == 0) {
                log.debug("Skipping first row");
                continue;
            }

            try {
                Transaction transaction = Transaction.builder()
                    .positionId(row.getCell(0).getStringCellValue())
                    .profit(new BigDecimal(row.getCell(8).getStringCellValue()))
                    .closedAt(LocalDateTime.parse(row.getCell(10).getStringCellValue(), DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")))
                    .isReal("Real".equals(row.getCell(14).getStringCellValue()))
                    .build();

                transactions.add(transaction);

                log.debug(transaction.toString());
            } catch (Exception e) {
                log.warn("Error during parsing a transaction, skipping", e);
            }
        }

        return transactions;
    }
}
