
package com.epcs.billing.bolsa.types;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ResultadoContrataBolsaBBerrySSCCType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ResultadoContrataBolsaBBerrySSCCType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="respuesta" type="{http://www.epcs.com/billing/bolsa/types}RespuestaType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ResultadoContrataBolsaBBerrySSCCType", propOrder = {
    "respuesta"
})
public class ResultadoContrataBolsaBBerrySSCCType {

    @XmlElement(required = true)
    protected RespuestaType respuesta;

    /**
     * Gets the value of the respuesta property.
     * 
     * @return
     *     possible object is
     *     {@link RespuestaType }
     *     
     */
    public RespuestaType getRespuesta() {
        return respuesta;
    }

    /**
     * Sets the value of the respuesta property.
     * 
     * @param value
     *     allowed object is
     *     {@link RespuestaType }
     *     
     */
    public void setRespuesta(RespuestaType value) {
        this.respuesta = value;
    }

}
