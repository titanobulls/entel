package com.epcs.bean;

import java.io.Serializable;

import com.epcs.recursoti.configuracion.MiEntelBusinessHelper;
import com.epcs.recursoti.configuracion.MiEntelProperties;

/**
 * Bean empleado como wrapper para {@link MiEntelBusinessHelper} y exponer sus
 * metodos para uso en Expression Language de JSF en JSPs de portlets
 * 
 * @author jlopez (I2B) en nombre de Absalon Opazo (Atencion al Cliente,
 *         EntelPcs)
 * 
 */
public class MiEntelBean implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * @return String sigla mercado suscripcion
     */
    public String getSiglaSuscripcion() {
        return MiEntelBusinessHelper.getSiglaSuscripcion();
    }
    
    /**
     * 
     * @param mercado
     *            sigla a comparar
     * 
     * @return true si <code>mercado</code> corresponde a sigla mercado
     *         suscripcion. false en caso contrario
     */
    public boolean isMercadoSuscripcion(String mercado) {
        return MiEntelBusinessHelper.isMercadoSuscripcion(mercado);
    }

    /**
     * @return String sigla mercado cuenta controlada
     */
    public String getSiglaCuentaControlada() {
        return MiEntelBusinessHelper.getSiglaCuentaControlada();
    }

    /**
     * 
     * @param mercado
     *            sigla a comparar
     * 
     * @return true si <code>mercado</code> corresponde a sigla mercado cuenta
     *         controlada. false en caso contrario
     */
    public boolean isMercadoCuentaControlada(String mercado) {
        return MiEntelBusinessHelper.isMercadoCuentaControlada(mercado);
    }

    /**
     * @return String sigla mercado prepago
     */
    public String getSiglaPrepago() {
        return MiEntelBusinessHelper.getSiglaPrepago();
    }

    /**
     * 
     * @param mercado
     *            sigla a comparar
     * 
     * @return true si <code>mercado</code> corresponde a sigla mercado prepago.
     *         false en caso contrario
     */
    public boolean isMercadoPrepago(String mercado) {
        return MiEntelBusinessHelper.isMercadoPrepago(mercado);
    }

    /**
     * @return array de String con todas las siglas de mercados
     */
    public String[] getMercados() {

        return MiEntelBusinessHelper.getMercados();
    }

    /**
     * 
     * @return array de String con todos valores de Atributos de Auto atencion
     */
    public String[] getAAAs() {
        return MiEntelBusinessHelper.getAAAs();
    }

    /**
     * 
     * @return valor AAA titular
     */
    public String getAAATitular() {
        return MiEntelBusinessHelper.getAAATitular();
    }

    /**
     * 
     * @return valor AAA Control total
     */
    public String getAAAControlTotal() {
        return MiEntelBusinessHelper.getAAAControlTotal();
    }

    /**
     * 
     * @return valor AAA control parcial
     */
    public String getAAAControlParcial() {
        return MiEntelBusinessHelper.getAAAControlParcial();
    }

    /**
     * 
     * @return valor AAA consultar
     */
    public String getAAAConsultar() {
        return MiEntelBusinessHelper.getAAAConsultar();
    }

    /**
     * 
     * @param aaa
     *            valor de Atributo Auto atencion a comparar
     * @return true si <code>aaa</code> corresponde al valor de titular
     */
    public boolean isAAATitular(String aaa) {
        return MiEntelBusinessHelper.isAAATitular(aaa);
    }

    /**
     * 
     * @param aaa
     *            valor de Atributo Auto atencion a comparar
     * @return true si <code>aaa</code> corresponde al valor de titular
     */
    public boolean isAAATitular(int aaa) {
        return MiEntelBusinessHelper.isAAATitular(aaa);
    }

    /**
     * 
     * @param aaa
     *            valor de Atributo Auto atencion a comparar
     * @return true si <code>aaa</code> corresponde al valor de control total
     */
    public boolean isAAAControlTotal(String aaa) {
        return MiEntelBusinessHelper.isAAAControlTotal(aaa);
    }

    /**
     * 
     * @param aaa
     *            valor de Atributo Auto atencion a comparar
     * @return true si <code>aaa</code> corresponde al valor de control total
     */
    public boolean isAAAControlTotal(int aaa) {
        return MiEntelBusinessHelper.isAAAControlTotal(aaa);
    }

    /**
     * 
     * @param aaa
     *            valor de Atributo Auto atencion a comparar
     * @return true si <code>aaa</code> corresponde al valor de control parcial
     */
    public boolean isAAAControlParcial(String aaa) {
        return MiEntelBusinessHelper.isAAAControlParcial(aaa);
    }

    /**
     * 
     * @param aaa
     *            valor de Atributo Auto atencion a comparar
     * @return true si <code>aaa</code> corresponde al valor de control parcial
     */
    public boolean isAAAControlParcial(int aaa) {
        return MiEntelBusinessHelper.isAAAControlParcial(aaa);
    }

    /**
     * 
     * @param aaa
     *            valor de Atributo Auto atencion a comparar
     * @return true si <code>aaa</code> corresponde al valor de consulta
     */
    public boolean isAAAConsulta(String aaa) {
        return MiEntelBusinessHelper.isAAAConsulta(aaa);
    }

    /**
     * 
     * @param aaa
     *            valor de Atributo Auto atencion a comparar
     * @return true si <code>aaa</code> corresponde al valor de consulta
     */
    public boolean isAAAConsulta(int aaa) {
        return MiEntelBusinessHelper.isAAAConsulta(aaa);
    }
    

    /**
     * @return String sigla categoria prepago plus (alto valor).
     */
    public String getSiglaPrepagoPlus() {
        return MiEntelBusinessHelper.getSiglaPrepagoPlus();
    }

    
    /*
     * AAA Security
     */
    
    /**
     * Entrega el mensaje default para Acceso denegado
     */
    public String getAccessDeniedDefaultMessage() {
        return MiEntelProperties.getProperty("miEntel.aaa.security.defaultMessage");
    }
    
    
    /**
     * Entrega el mensaje default para Acceso denegado
     */
    public String getSiglaUsuarioBAM() {
        return MiEntelProperties.getProperty("flagBam.usuarioBAM");
    }
}
