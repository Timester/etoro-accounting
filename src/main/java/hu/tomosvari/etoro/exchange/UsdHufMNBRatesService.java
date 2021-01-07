package hu.tomosvari.etoro.exchange;

import hu.tomosvari.etoro.exchange.mnb.MNBArfolyamServiceSoap;
import hu.tomosvari.etoro.exchange.mnb.MNBArfolyamServiceSoapGetExchangeRatesStringFaultFaultMessage;
import hu.tomosvari.etoro.exchange.mnb.MNBArfolyamServiceSoapImpl;
import lombok.extern.slf4j.Slf4j;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class UsdHufMNBRatesService implements ExchangeRatesService {

    public static final String USD_CURRENCY = "USD";
    private final MNBArfolyamServiceSoap mnbService;

    public UsdHufMNBRatesService() {
        MNBArfolyamServiceSoapImpl impl = new MNBArfolyamServiceSoapImpl();
        mnbService = impl.getCustomBindingMNBArfolyamServiceSoap();
    }

    @Override
    public Map<LocalDate, BigDecimal> getExchangeRates(LocalDate from, LocalDate to) {
        Map<LocalDate, BigDecimal> rates = new HashMap<>();

        try {
            log.debug("Calling MNB API from {} - to {}, for currency USD", from.toString(), to.toString());
            String usd = mnbService.getExchangeRates(from.toString(), to.toString(), USD_CURRENCY);

            Document exchangeRatesResponse = parseXmlResponse(usd);

            if (log.isDebugEnabled()) {
                printDocument(exchangeRatesResponse, System.out);
            }

            if (exchangeRatesResponse != null) {
                NodeList days = exchangeRatesResponse.getElementsByTagName("Day");
                for (int i = 0; i < days.getLength(); i++) {
                    Node day = days.item(i);
                    Node rate = day.getFirstChild();

                    rates.put(
                        LocalDate.parse(day.getAttributes().getNamedItem("date").getTextContent()),
                        new BigDecimal(rate.getTextContent().replace(',', '.'))
                    );
                }
            }
        } catch (MNBArfolyamServiceSoapGetExchangeRatesStringFaultFaultMessage e) {
            log.error("Error during getting exchange rates from MNB API, returning empty rates", e);
        }

        return rates;
    }

    private Document parseXmlResponse(String response) {
        try (ByteArrayInputStream input = new ByteArrayInputStream(response.getBytes(StandardCharsets.UTF_8))) {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();

            return builder.parse(input);
        } catch (IOException | ParserConfigurationException | SAXException e) {
            log.error("Error during parsing MNB exchange rate response.", e);
            return null;
        }
    }

    public static void printDocument(Document doc, OutputStream out) {
        try {
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
            transformer.setOutputProperty(OutputKeys.METHOD, "xml");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

            transformer.transform(new DOMSource(doc),
                new StreamResult(new OutputStreamWriter(out, StandardCharsets.UTF_8)));
        } catch (TransformerException e) {
            log.warn("Error during MNB XML response pretty print, skip printing.", e);
        }
    }
}
