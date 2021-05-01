package hu.tomosvari.etoro.exchange.mnb;

import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElementRef;
import jakarta.xml.bind.annotation.XmlType;

/**
 * <p>Java class for GetCurrentExchangeRatesResponseBody complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="GetCurrentExchangeRatesResponseBody">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="GetCurrentExchangeRatesResult" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GetCurrentExchangeRatesResponseBody", propOrder = {
    "getCurrentExchangeRatesResult"
})
public class GetCurrentExchangeRatesResponseBody {

    @XmlElementRef(name = "GetCurrentExchangeRatesResult", namespace = "http://www.mnb.hu/webservices/", type = JAXBElement.class, required = false)
    protected JAXBElement<String> getCurrentExchangeRatesResult;

    /**
     * Gets the value of the getCurrentExchangeRatesResult property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getGetCurrentExchangeRatesResult() {
        return getCurrentExchangeRatesResult;
    }

    /**
     * Sets the value of the getCurrentExchangeRatesResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setGetCurrentExchangeRatesResult(JAXBElement<String> value) {
        this.getCurrentExchangeRatesResult = value;
    }

}
