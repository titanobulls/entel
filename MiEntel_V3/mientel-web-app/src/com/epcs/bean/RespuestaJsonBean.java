/* Propiedad de Entel PCS. Todos los derechos reservados */
package com.epcs.bean;

import java.io.Serializable;

/**
 * @author jmanzur (I2B) en nombre de Absalon Opazo (Atencion al Cliente, EntelPcs)
 *
 */
public class RespuestaJsonBean implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    private String estado;
    private String detalle = "";
    private Object respuesta;

    public String getDetalle() {
     return detalle;
    }
    public void setDetalle(String detalle) {
     this.detalle = detalle;
    }
    public String getEstado() {
     return estado;
    }
    public void setEstado(String estado) {
     this.estado = estado;
    }
    public Object getRespuesta() {
     return respuesta;
    }
    public void setRespuesta(Object respuesta) {
     this.respuesta = respuesta;
    }


}
