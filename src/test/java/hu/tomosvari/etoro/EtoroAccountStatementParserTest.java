package hu.tomosvari.etoro;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class EtoroAccountStatementParserTest {

    @Test
    void parseTransactions() {
        XSSFWorkbook workbook = readFile();

        Set<Transaction> transactions = EtoroAccountStatementParser.parseTransactions(workbook);

        assertEquals(5, transactions.size());

        List<Transaction> transactionList = transactions.stream().sorted(new TransactionValueComparator()).collect(Collectors.toList());

        assertEquals(3, transactionList.stream().mapToInt(x -> x.getProfit().intValue()).sum());
        assertEquals(-2, transactionList.get(0).getProfit().intValue());
        assertEquals(3, transactionList.get(4).getProfit().intValue());
        assertFalse(transactionList.get(4).isReal());
        assertTrue(transactionList.get(3).isReal());
    }


    private XSSFWorkbook readFile() {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("etoro2.xlsx").getFile());

        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            return new XSSFWorkbook(fileInputStream);
        } catch (Exception e) {
            fail();
        }

        return null;
    }
}