
package com.epcs.loyalty.zonaterceros.wsloyalty;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for consultaEstadoTCRutMovilResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="consultaEstadoTCRutMovilResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="return" type="{http://www.epcs.com/Loyalty/zonaTerceros/WSLoyalty}estadotarjetadto" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "consultaEstadoTCRutMovilResponse", propOrder = {
    "_return"
})
public class ConsultaEstadoTCRutMovilResponse {

    @XmlElement(name = "return")
    protected Estadotarjetadto _return;

    /**
     * Gets the value of the return property.
     * 
     * @return
     *     possible object is
     *     {@link Estadotarjetadto }
     *     
     */
    public Estadotarjetadto getReturn() {
        return _return;
    }

    /**
     * Sets the value of the return property.
     * 
     * @param value
     *     allowed object is
     *     {@link Estadotarjetadto }
     *     
     */
    public void setReturn(Estadotarjetadto value) {
        this._return = value;
    }

}
