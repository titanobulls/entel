/* Propiedad de Entel PCS. Todos los derechos reservados */
package com.epcs.recursoti.configuracion;

import com.epcs.bean.RespuestaJsonBean;
import com.epcs.bean.TransaccionGTMBean;
import com.epcs.recursoti.configuracion.error.ServiceMessages;
import com.google.gson.Gson;

/**
 * Clase utilitaria para la conversion de Beans de dominio al lenguaje de descripcion Json.
 * @author jlopez (I2B) en nombre de Absalon Opazo (Atencion al Cliente, EntelPcs)
 *
 */
public class JsonHelper {

    /**
     * Instancia statica de Gson para la conversion de Objects a Json
     */
    private static final Gson GSON = new Gson();
    
    /**
     * Entega el Bean <code>obj</code> como codigo Json
     * @param obj Object a ser convertido en Json
     * @return String con la instancia de <code>obj</code> como String Json
     */
    public static String toJson(final Object obj) {
        return GSON.toJson(obj);
    }

    /**
     * Mensaje de error espec&iacute;fico de servicio encapsulado en el bean
     * <code>RespuestaJsonBean</code> en formato json
     * 
     * @param serviceName
     *            String nombre del servicio
     * @param serviceCode
     *            String c&oacute;digo de error del servicio
     *            
     * @return String mensaje de error
     */
    public static String toJsonServiceErrorMessage(String serviceName,
            String serviceCode) {
        ServiceMessages errorMessages = MiEntelProperties
                .getServiceMessages();
        RespuestaJsonBean respuestaJsonBean = new RespuestaJsonBean();
        respuestaJsonBean.setEstado("Error");
        respuestaJsonBean.setDetalle(errorMessages.getErrorCodeMessage(serviceCode,
                serviceName));
        return GSON.toJson(respuestaJsonBean);
    }
    
    /**
     * Entrega un mensaje de error general, asociado al nombre messageName.
     * preparado para una respuesta Gson.<br>
     * 
     * @param messageName String
     * 
     * @return String con mensaje de error
     */
    public static String toJsonServiceErrorMessage(String messageName) {
        ServiceMessages errorMessages = MiEntelProperties
                .getServiceMessages();
        RespuestaJsonBean respuestaJsonBean = new RespuestaJsonBean();
        respuestaJsonBean.setEstado("Error");
        if(null != messageName){
        respuestaJsonBean
                .setDetalle(errorMessages.getErrorMessage(messageName));
        }
        return GSON.toJson(respuestaJsonBean);
    }

    /**
     * Entrega un mensaje de exito general, asociado al nombre messageName.
     * preparado para una respuesta Gson.<br>
     * 
     * @param messageName String
     * 
     * @return String con mensaje de exito
     */
    public static String toJsonServiceSuccessMessage(String messageName) {
        ServiceMessages messages = MiEntelProperties
                .getServiceMessages();
        RespuestaJsonBean respuestaJsonBean = new RespuestaJsonBean();
        respuestaJsonBean.setEstado("Ok");
        respuestaJsonBean
                .setDetalle(messages.getSuccessMessage(messageName));
        return GSON.toJson(respuestaJsonBean);
    }
    
    /**
     * Mensaje de error encapsulado en el bean <code>RespuestaJsonBean</code> en
     * formato json
     * 
     * estado = "Error"
     * 
     * @param msg
     *            String mensaje de error
     */
    public static String toJsonErrorMessage(String msg) {
        RespuestaJsonBean respuestaJsonBean = new RespuestaJsonBean();
        respuestaJsonBean.setEstado("Error");
        respuestaJsonBean.setDetalle(msg);
        return GSON.toJson(respuestaJsonBean);
    }
    
    /**
     * Encapsula un <code>Onject</code>
     * en el bean <code>RespuestaJsonBean</code> 
     * en formato json
     * 
     * estado = "Ok"
     * 
     * @param Object
     *            Object Objeto  a encapsular en la respuesta.
    */
    public static String toJsonResponse(Object obj) {
        RespuestaJsonBean respuestaJsonBean = new RespuestaJsonBean();
        respuestaJsonBean.setEstado("Ok");
        respuestaJsonBean.setDetalle("");
        respuestaJsonBean.setRespuesta(obj);
        return GSON.toJson(respuestaJsonBean);
    }
    
    /**
     * Encapsula un <code>Object</code> en el bean
     * <code>RespuestaJsonBean</code> en formato json, junto con un mensaje
     * obtenido desde el properties de mensajes
     * 
     * estado = "Ok"
     * 
     * @param Object
     *            Object Objeto a encapsular en la respuesta.
     */
    public static String toJsonResponse(Object obj, String messageName) {
        ServiceMessages messages = MiEntelProperties.getServiceMessages();
        RespuestaJsonBean respuestaJsonBean = new RespuestaJsonBean();
        respuestaJsonBean.setEstado("Ok");
        respuestaJsonBean.setRespuesta(obj);
        respuestaJsonBean.setDetalle(messages.getSuccessMessage(messageName));
        return GSON.toJson(respuestaJsonBean);
    }

    /**
     * Encapsula un <code>Object</code> en el bean
     * <code>RespuestaJsonBean</code> en formato json, junto con un mensaje
     * obtenido desde el properties de mensajes
     * 
     * estado = "Ok"
     * 
     * @param Object
     *            Object Objeto a encapsular en la respuesta.
     */
    public static String toJsonResponse(Object obj, String messageName, String serviceName) {
        ServiceMessages messages = MiEntelProperties.getServiceMessages();
        RespuestaJsonBean respuestaJsonBean = new RespuestaJsonBean();
        respuestaJsonBean.setEstado("Ok");
        respuestaJsonBean.setRespuesta(obj);
        respuestaJsonBean.setDetalle(messages.getSuccessMessage(messageName, serviceName));
        return GSON.toJson(respuestaJsonBean);
    }

    /**
     * Encapsula un <code>Object</code> en el bean
     * <code>RespuestaJsonBean</code> en formato json, junto con un mensaje
     * obtenido desde el properties de mensajes
     * 
     * estado = "Ok"
     * 
     * @param Object
     *            Object Objeto a encapsular en la respuesta.
     */
    public static String toJsonResponse(Object obj, String messageName,
            String serviceName, String[] args) {
        ServiceMessages messages = MiEntelProperties.getServiceMessages();
        RespuestaJsonBean respuestaJsonBean = new RespuestaJsonBean();
        respuestaJsonBean.setEstado("Ok");
        respuestaJsonBean.setRespuesta(obj);
        respuestaJsonBean.setDetalle(messages.getSuccessMessage(messageName,
                serviceName, args));
        return GSON.toJson(respuestaJsonBean);
    }   
    
    
    /**
     * Mensaje de error espec&iacute;fico de servicio encapsulado
     * en el bean <code>RespuestaJsonBean</code> en formato json
     * 
     * @param serviceName
     *            String nombre del servicio
     * @param serviceCode
     *            String c&oacute;digo de error del servicio
     * @param args
     *            String array con los par&aacute;metros para el mensaje del servicio
     */
    public static String toJsonServiceErrorMessage(String serviceName,
            String serviceCode, String[] args) {
        ServiceMessages errorMessages = MiEntelProperties
                .getServiceMessages();
        RespuestaJsonBean respuestaJsonBean = new RespuestaJsonBean();
        respuestaJsonBean.setEstado("Error");
        respuestaJsonBean.setDetalle(errorMessages.getErrorCodeMessage(serviceCode, serviceName, args));
        return GSON.toJson(respuestaJsonBean);
    }
    
    /**
     * Encapsula un objeto <code>TransaccionGTMBean</code>
     * en el bean <code>RespuestaJsonBean</code> 
     * en formato json
     * 
     * estado = "Ok"
     * 
     * @param Object
     *            Object Objeto  a encapsular en la respuesta.
    */
    public static String toJsonGTMResponse(TransaccionGTMBean obj) {
    	return toJsonResponse(obj);
    }
    
    /**
     * Encapsula un objeto <code>TransaccionGTMBean</code>
     * en el bean <code>RespuestaJsonBean</code> 
     * en formato json, con un mensaje de detalle personalizado
     * 
     * estado = "Ok"
     * 
     * @param Object
     *            Object Objeto  a encapsular en la respuesta.
    */
    public static String toJsonGTMResponse(TransaccionGTMBean obj, String detalle) {
        RespuestaJsonBean respuestaJsonBean = new RespuestaJsonBean();
        respuestaJsonBean.setEstado("Ok");
        respuestaJsonBean.setDetalle(detalle);
        respuestaJsonBean.setRespuesta(obj);
        return GSON.toJson(respuestaJsonBean);
    }    
    
}
