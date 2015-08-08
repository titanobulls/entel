
package com.epcs.billing.bolsa.types;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for BolsaPPType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="BolsaPPType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="fechaExpiracion" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="saldo" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="instanciaBolsa" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="codBolsa" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="nombreBolsa" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="descBolsa" type="{http://www.epcs.com/billing/bolsa/types}ItemDescBolsaType"/>
 *         &lt;element name="tipoBolsa" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BolsaPPType", propOrder = {
    "fechaExpiracion",
    "saldo",
    "instanciaBolsa",
    "codBolsa",
    "nombreBolsa",
    "descBolsa",
    "tipoBolsa"
})
public class BolsaPPType {

    @XmlElement(required = true)
    protected String fechaExpiracion;
    @XmlElement(required = true)
    protected String saldo;
    @XmlElement(required = true)
    protected String instanciaBolsa;
    @XmlElement(required = true)
    protected String codBolsa;
    @XmlElement(required = true)
    protected String nombreBolsa;
    @XmlElement(required = true)
    protected ItemDescBolsaType descBolsa;
    @XmlElement(required = true)
    protected String tipoBolsa;

    /**
     * Gets the value of the fechaExpiracion property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFechaExpiracion() {
        return fechaExpiracion;
    }

    /**
     * Sets the value of the fechaExpiracion property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFechaExpiracion(String value) {
        this.fechaExpiracion = value;
    }

    /**
     * Gets the value of the saldo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSaldo() {
        return saldo;
    }

    /**
     * Sets the value of the saldo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSaldo(String value) {
        this.saldo = value;
    }

    /**
     * Gets the value of the instanciaBolsa property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInstanciaBolsa() {
        return instanciaBolsa;
    }

    /**
     * Sets the value of the instanciaBolsa property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInstanciaBolsa(String value) {
        this.instanciaBolsa = value;
    }

    /**
     * Gets the value of the codBolsa property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodBolsa() {
        return codBolsa;
    }

    /**
     * Sets the value of the codBolsa property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodBolsa(String value) {
        this.codBolsa = value;
    }

    /**
     * Gets the value of the nombreBolsa property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNombreBolsa() {
        return nombreBolsa;
    }

    /**
     * Sets the value of the nombreBolsa property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNombreBolsa(String value) {
        this.nombreBolsa = value;
    }

    /**
     * Gets the value of the descBolsa property.
     * 
     * @return
     *     possible object is
     *     {@link ItemDescBolsaType }
     *     
     */
    public ItemDescBolsaType getDescBolsa() {
        return descBolsa;
    }

    /**
     * Sets the value of the descBolsa property.
     * 
     * @param value
     *     allowed object is
     *     {@link ItemDescBolsaType }
     *     
     */
    public void setDescBolsa(ItemDescBolsaType value) {
        this.descBolsa = value;
    }

    /**
     * Gets the value of the tipoBolsa property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTipoBolsa() {
        return tipoBolsa;
    }

    /**
     * Sets the value of the tipoBolsa property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTipoBolsa(String value) {
        this.tipoBolsa = value;
    }

}
