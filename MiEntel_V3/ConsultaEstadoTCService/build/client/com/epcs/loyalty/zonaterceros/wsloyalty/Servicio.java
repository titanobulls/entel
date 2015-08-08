
package com.epcs.loyalty.zonaterceros.wsloyalty;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;


/**
 * This class was generated by the JAX-WS RI.
 * Oracle JAX-WS 2.1.4
 * Generated source version: 2.1
 * 
 */
@WebService(name = "servicio", targetNamespace = "http://www.epcs.com/Loyalty/zonaTerceros/WSLoyalty")
@XmlSeeAlso({
    ObjectFactory.class
})
public interface Servicio {


    /**
     * 
     * @param rut
     * @return
     *     returns com.epcs.loyalty.zonaterceros.wsloyalty.Estadotarjetadto
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "consultaEstadoTCRut", targetNamespace = "http://www.epcs.com/Loyalty/zonaTerceros/WSLoyalty", className = "com.epcs.loyalty.zonaterceros.wsloyalty.ConsultaEstadoTCRut")
    @ResponseWrapper(localName = "consultaEstadoTCRutResponse", targetNamespace = "http://www.epcs.com/Loyalty/zonaTerceros/WSLoyalty", className = "com.epcs.loyalty.zonaterceros.wsloyalty.ConsultaEstadoTCRutResponse")
    public Estadotarjetadto consultaEstadoTCRut(
        @WebParam(name = "Rut", targetNamespace = "")
        String rut);

    /**
     * 
     * @param movil
     * @return
     *     returns com.epcs.loyalty.zonaterceros.wsloyalty.Estadotarjetadto
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "consultaEstadoTCMovil", targetNamespace = "http://www.epcs.com/Loyalty/zonaTerceros/WSLoyalty", className = "com.epcs.loyalty.zonaterceros.wsloyalty.ConsultaEstadoTCMovil")
    @ResponseWrapper(localName = "consultaEstadoTCMovilResponse", targetNamespace = "http://www.epcs.com/Loyalty/zonaTerceros/WSLoyalty", className = "com.epcs.loyalty.zonaterceros.wsloyalty.ConsultaEstadoTCMovilResponse")
    public Estadotarjetadto consultaEstadoTCMovil(
        @WebParam(name = "Movil", targetNamespace = "")
        int movil);

    /**
     * 
     * @param rut
     * @param movil
     * @return
     *     returns com.epcs.loyalty.zonaterceros.wsloyalty.Estadotarjetadto
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "consultaEstadoTCRutMovil", targetNamespace = "http://www.epcs.com/Loyalty/zonaTerceros/WSLoyalty", className = "com.epcs.loyalty.zonaterceros.wsloyalty.ConsultaEstadoTCRutMovil")
    @ResponseWrapper(localName = "consultaEstadoTCRutMovilResponse", targetNamespace = "http://www.epcs.com/Loyalty/zonaTerceros/WSLoyalty", className = "com.epcs.loyalty.zonaterceros.wsloyalty.ConsultaEstadoTCRutMovilResponse")
    public Estadotarjetadto consultaEstadoTCRutMovil(
        @WebParam(name = "Rut", targetNamespace = "")
        String rut,
        @WebParam(name = "Movil", targetNamespace = "")
        int movil);

    /**
     * 
     * @param rut
     * @return
     *     returns com.epcs.loyalty.zonaterceros.wsloyalty.Datosnocliente
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "consultaDatosNoCliente", targetNamespace = "http://www.epcs.com/Loyalty/zonaTerceros/WSLoyalty", className = "com.epcs.loyalty.zonaterceros.wsloyalty.ConsultaDatosNoCliente")
    @ResponseWrapper(localName = "consultaDatosNoClienteResponse", targetNamespace = "http://www.epcs.com/Loyalty/zonaTerceros/WSLoyalty", className = "com.epcs.loyalty.zonaterceros.wsloyalty.ConsultaDatosNoClienteResponse")
    public Datosnocliente consultaDatosNoCliente(
        @WebParam(name = "Rut", targetNamespace = "")
        String rut);

}
