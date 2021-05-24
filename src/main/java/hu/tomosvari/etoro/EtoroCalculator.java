package hu.tomosvari.etoro;

import hu.tomosvari.etoro.exchange.hungary.UsdHufMNBRatesService;
import hu.tomosvari.etoro.tax.hungary.HungarianTaxService;
import hu.tomosvari.etoro.tax.TaxService;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
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

        Set<Transaction> transactions = EtoroAccountStatementParser.parseTransactions(workbook);
        printStatistics(transactions);

        TaxService taxService = new HungarianTaxService(new UsdHufMNBRatesService());
        taxService.calculateTax(transactions);
    }

    private XSSFWorkbook readWorkbook(String fileLocation) throws IOException {
        try (FileInputStream file = new FileInputStream(fileLocation)) {
            return new XSSFWorkbook(file);
        }
    }
}
