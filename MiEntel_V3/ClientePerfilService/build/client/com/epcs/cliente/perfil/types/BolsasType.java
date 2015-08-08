
package com.epcs.cliente.perfil.types;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for BolsasType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="BolsasType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="bolsa" type="{http://www.epcs.com/cliente/perfil/types}BolsaType" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BolsasType", propOrder = {
    "bolsa"
})
public class BolsasType {

    @XmlElement(nillable = true)
    protected List<BolsaType> bolsa;

    /**
     * Gets the value of the bolsa property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the bolsa property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getBolsa().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link BolsaType }
     * 
     * 
     */
    public List<BolsaType> getBolsa() {
        if (bolsa == null) {
            bolsa = new ArrayList<BolsaType>();
        }
        return this.bolsa;
    }

}
