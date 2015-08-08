/* Propiedad de Entel PCS. Todos los derechos reservados */
package com.epcs.bean;

import java.io.Serializable;

/**
 * @author rmesino (I2B) en nombre de Absalon Opazo (Atencion al Cliente, EntelPcs)
 *
 */
public class FacturacionElectronicaBean implements Serializable{

    private static final long serialVersionUID = 1786112807000980557L;

    protected String rut;
    
    protected String nroCuenta;
    
    protected String codigoGrupoCliente;
    
    protected String grupoCliente;
    
    protected String correoFactura;
    
    protected String parametro6;
    
    protected String parametro7;
    
    protected String estadoFE;
    
    protected String parametro9;
    
    protected String parametro10;
    
    protected String fechaHoraInscripcion;
    
    protected String msisdn;
    
    protected String parametro13;

    public FacturacionElectronicaBean(){
        
    }
    
    /**
     * @return the rut
     */
    public String getRut() {
        return rut;
    }

    /**
     * @param rut the rut to set
     */
    public void setRut(String rut) {
        this.rut = rut;
    }

    /**
     * @return the nroCuenta
     */
    public String getNroCuenta() {
        return nroCuenta;
    }

    /**
     * @param nroCuenta the nroCuenta to set
     */
    public void setNroCuenta(String nroCuenta) {
        this.nroCuenta = nroCuenta;
    }

    /**
     * @return the codigoGrupoCliente
     */
    public String getCodigoGrupoCliente() {
        return codigoGrupoCliente;
    }

    /**
     * @param codigoGrupoCliente the codigoGrupoCliente to set
     */
    public void setCodigoGrupoCliente(String codigoGrupoCliente) {
        this.codigoGrupoCliente = codigoGrupoCliente;
    }

    /**
     * @return the grupoCliente
     */
    public String getGrupoCliente() {
        return grupoCliente;
    }

    /**
     * @param grupoCliente the grupoCliente to set
     */
    public void setGrupoCliente(String grupoCliente) {
        this.grupoCliente = grupoCliente;
    }

    /**
     * @return the correoFactura
     */
    public String getCorreoFactura() {
        if(null != correoFactura){
        	return correoFactura;
        }
    	return "";
    }

    /**
     * @param correoFactura the correoFactura to set
     */
    public void setCorreoFactura(String correoFactura) {
        this.correoFactura = correoFactura;
    }

    /**
     * @return the parametro6
     */
    public String getParametro6() {
        return parametro6;
    }

    /**
     * @param parametro6 the parametro6 to set
     */
    public void setParametro6(String parametro6) {
        this.parametro6 = parametro6;
    }

    /**
     * @return the parametro7
     */
    public String getParametro7() {
        return parametro7;
    }

    /**
     * @param parametro7 the parametro7 to set
     */
    public void setParametro7(String parametro7) {
        this.parametro7 = parametro7;
    }

    /**
     * @return the estadoFE
     */
    public String getEstadoFE() {
        return estadoFE;
    }

    /**
     * @param estadoFE the estadoFE to set
     */
    public void setEstadoFE(String estadoFE) {
        this.estadoFE = estadoFE;
    }

    /**
     * @return the parametro9
     */
    public String getParametro9() {
        return parametro9;
    }

    /**
     * @param parametro9 the parametro9 to set
     */
    public void setParametro9(String parametro9) {
        this.parametro9 = parametro9;
    }

    /**
     * @return the parametro10
     */
    public String getParametro10() {
        return parametro10;
    }

    /**
     * @param parametro10 the parametro10 to set
     */
    public void setParametro10(String parametro10) {
        this.parametro10 = parametro10;
    }

    /**
     * @return the fechaHoraInscripcion
     */
    public String getFechaHoraInscripcion() {
        return fechaHoraInscripcion;
    }

    /**
     * @param fechaHoraInscripcion the fechaHoraInscripcion to set
     */
    public void setFechaHoraInscripcion(String fechaHoraInscripcion) {
        this.fechaHoraInscripcion = fechaHoraInscripcion;
    }

    /**
     * @return the msisdn
     */
    public String getMsisdn() {
        return msisdn;
    }

    /**
     * @param msisdn the msisdn to set
     */
    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    /**
     * @return the parametro13
     */
    public String getParametro13() {
        return parametro13;
    }

    /**
     * @param parametro13 the parametro13 to set
     */
    public void setParametro13(String parametro13) {
        this.parametro13 = parametro13;
    }
    
}
