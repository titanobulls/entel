///* Propiedad de Entel PCS. Todos los derechos reservados */
//package com.esa.ponline.portalbolsas.ws.recarga.dao.imp;
//
//import java.io.Serializable;
//import java.util.Map;
//
////import javax.faces.context.FacesContext;
////import javax.faces.event.PhaseEvent;
//
//
//
//
//import javax.servlet.http.HttpServletRequest;
//
//import org.apache.log4j.Logger;
//
//import com.esa.ponline.portalbolsas.bean.RecargaWebPayBean;
//import com.esa.ponline.portalbolsas.ws.recarga.dao.delegate.RecargaDelegate;
//
////import com.bea.p13n.usermgmt.profile.ProfileWrapper;
////import com.epcs.bean.MontoRecargaBean;
////import com.epcs.bean.RecargaWebPayBean;
////import com.epcs.billing.recarga.dao.util.WebPayHelper;
////import com.epcs.billing.recarga.delegate.RecargaDelegate;
////import com.epcs.recursoti.configuracion.MiEntelProperties;
////import com.epcs.recursoti.configuracion.ParametrosHelper;
////import com.epcs.recursoti.configuracion.Utils;
////import com.epcs.recursoti.configuracion.jsf.utils.JSFMessagesHelper;
////import com.epcs.recursoti.configuracion.jsf.utils.JSFPortletHelper;
////import com.epcs.recursoti.configuracion.jsf.utils.JsfUtil;
////import com.epcs.recursoti.configuracion.uup.ProfileWrapperHelper;
////import com.epcs.recursoti.excepcion.DAOException;
////import com.epcs.recursoti.excepcion.ServiceException;
//
///**
// * @author ccastro (MZZO) en nombre de Absalon Opazo (Atencion al Cliente,
// *         EntelPcs)
// * 
// */
//public class CierreRecargaWebpayController implements Serializable {
//
//    /**
//     * 
//     */
//    private static final long serialVersionUID = 1L;
//
//    /**
//     * Logger para CierreRecargaWebpayController
//     */
//    private static final Logger LOGGER = Logger.getLogger(CierreRecargaWebpayController.class);
//
//    private static final String TBK_ID_TRANSACCION = "TBK_ID_TRANSACCION";
//
//    private static final String TBK_ID_SESION = "TBK_ID_SESION";
//    
//    private RecargaDelegate recargaDelegate;
//
//    private RecargaWebPayBean recarga;
//
//    private String resultado;
//
//    private String ordenCompra;
//
//    private String idTransaccion;
//
//    private String idSesion;
//
//    private String nroTarjeta;
//
//    private String nroCuotas;
//
//    private String nombreUsuario;
//
//    /**
//     * @param recargaDelegate
//     *            the recargaDelegate to set
//     */
//    public void setRecargaDelegate(RecargaDelegate recargaDelegate) {
//        this.recargaDelegate = recargaDelegate;
//    }
//
//    /**
//     * @return the recargaDelegate
//     */
//    public RecargaDelegate getRecargaDelegate() {
//        return recargaDelegate;
//    }
//
//    /**
//     * @return the recarga
//     */
//    public RecargaWebPayBean getRecarga() {
//        return recarga;
//    }
//
//    /**
//     * @param recarga
//     *            the recarga to set
//     */
//    public void setRecarga(RecargaWebPayBean recarga) {
//        this.recarga = recarga;
//    }
//
//    /**
//     * @return the resultado
//     */
//    public String getResultado() {
//        return resultado;
//    }
//
//    /**
//     * @param resultado
//     *            the resultado to set
//     */
//    public void setResultado(String resultado) {
//        this.resultado = resultado;
//    }
//
//    /**
//     * @return the ordenCompra
//     */
//    public String getOrdenCompra() {
//        return ordenCompra;
//    }
//
//    /**
//     * @param ordenCompra
//     *            the ordenCompra to set
//     */
//    public void setOrdenCompra(String ordenCompra) {
//        this.ordenCompra = ordenCompra;
//    }
//
//    /**
//     * @return the idTransaccion
//     */
//    public String getIdTransaccion() {
//        return idTransaccion;
//    }
//
//    /**
//     * @param idTransaccion
//     *            the idTransaccion to set
//     */
//    public void setIdTransaccion(String idTransaccion) {
//        this.idTransaccion = idTransaccion;
//    }
//
//    /**
//     * @return the idSesion
//     */
//    public String getIdSesion() {
//        return idSesion;
//    }
//
//    /**
//     * @param idSesion
//     *            the idSesion to set
//     */
//    public void setIdSesion(String idSesion) {
//        this.idSesion = idSesion;
//    }
//
////    /**
////     * Init method para la accion de cierre de recarga.<br>
////     * Esta accion corresponde a un request desde Transbank indicando la fase de
////     * cierre del pago mediante Webpay.<br>
////     * Este metodo recoge los parametros de transbank y los asigna a atributos
////     * de este Bean
////     * 
////     * @param event
////     */
////    public void initCierreRecargaWebpay(PhaseEvent event) {
////
////        /*
////         * Obtencion parametros y log
////         */
////        resultado = JsfUtil.getRequestParameter("RESULTADO");
////        ordenCompra = JsfUtil.getRequestParameter("ordenCompra");
////        idTransaccion = JsfUtil.getRequestParameter(TBK_ID_TRANSACCION);
////        idSesion = JsfUtil.getRequestParameter(TBK_ID_SESION);
////
////        LOGGER.info("Cierre de recarga Webpay. Parametros recibidos: ");
////        LOGGER.info("'RESULTADO' : " + resultado);
////        LOGGER.info("'ordenCompra' : " + ordenCompra);
////        LOGGER.info("'" + TBK_ID_TRANSACCION + "' : " + idTransaccion);
////        LOGGER.info("'" + TBK_ID_SESION + "' : " + idSesion);
////        
////    }
//    
//    /**
//     * Init method para la accion de cierre de recarga.<br>
//     * Esta accion corresponde a un request desde Transbank indicando la fase de
//     * cierre del pago mediante Webpay.<br>
//     * Este metodo recoge los parametros de transbank y los asigna a atributos
//     * de este Bean
//     * 
//     * @param event
//     */
//    public void initCierreRecargaWebpay(HttpServletRequest req) {
//
//        /*
//         * Obtencion parametros y log
//         */
//        resultado = req.getParameter("RESULTADO");
//        ordenCompra = req.getParameter("ordenCompra");
//        idTransaccion = req.getParameter(TBK_ID_TRANSACCION);
//        idSesion = req.getParameter(TBK_ID_SESION);
//
//        LOGGER.info("Cierre de recarga Webpay. Parametros recibidos: ");
//        LOGGER.info("'RESULTADO' : " + resultado);
//        LOGGER.info("'ordenCompra' : " + ordenCompra);
//        LOGGER.info("'" + TBK_ID_TRANSACCION + "' : " + idTransaccion);
//        LOGGER.info("'" + TBK_ID_SESION + "' : " + idSesion);
//        
//    }
//    
//    
//
//
//    // --------------------------------------- propiedades transbank
//
//    /**
//     * @return the nroTarjeta
//     */
//    public String getNroTarjeta() {
//        return nroTarjeta;
//    }
//
//    /**
//     * @param nroTarjeta
//     *            the nroTarjeta to set
//     */
//    public void setNroTarjeta(String nroTarjeta) {
//        this.nroTarjeta = nroTarjeta;
//    }
//
//    /**
//     * @return the nroCuotas
//     */
//    public String getNroCuotas() {
//        return nroCuotas;
//    }
//
//    /**
//     * @param nroCuotas
//     *            the nroCuotas to set
//     */
//    public void setNroCuotas(String nroCuotas) {
//        this.nroCuotas = nroCuotas;
//    }
//
//    /**
//     * @return the nombreUsuario
//     */
//    public String getNombreUsuario() {
//        return nombreUsuario;
//    }
//
//    /**
//     * @param nombreUsuario
//     *            the nombreUsuario to set
//     */
//    public void setNombreUsuario(String nombreUsuario) {
//        this.nombreUsuario = nombreUsuario;
//    }
//
//    public String finalizarRecargaTarjetaCredito() {
//        
//        String postback = "success";
//        
//        Map<String, String> parameterMap = FacesContext.getCurrentInstance()
//                .getExternalContext().getRequestParameterMap();
//        
//        /*
//         * Este action method es invocado por un formulario via javascript, por tanto,
//         * los campos estan en campos hidden de tipo HTML, no JSF.
//         * Por esta razon los valores son obtenidos desde el Map de parametros 
//         * presente en el request.
//         */
//        ordenCompra = parameterMap.get("ordenCompra");
//        resultado = parameterMap.get("RESULTADO");
//        idTransaccion = parameterMap.get(TBK_ID_TRANSACCION);
//        idSesion = parameterMap.get(TBK_ID_SESION);
//        
//
//        /*
//         * log de parmetros
//         */
//        LOGGER.info("Finalizacion recarga Webpay. Parametros recibidos: ");
//        LOGGER.info("'RESULTADO' : " + resultado);
//        LOGGER.info("'ordenCompra' : " + ordenCompra);
//        LOGGER.info("'" + TBK_ID_TRANSACCION + "' : " + idTransaccion);
//        LOGGER.info("'" + TBK_ID_SESION + "' : " + idSesion);
//
//        /*
//         * Validacion de parametros
//         */
//        if(!checkParameters()) {
//            return postback;
//        }
//
//        /**
//         * Posibles valores de retorno de Transbank
//         */
//        String EXITO = MiEntelProperties.getProperty("recarga.webpay.resultado.exito");
//        String FRACASO = MiEntelProperties.getProperty("recarga.webpay.resultado.fracaso");
//        
//        if(FRACASO.equals(resultado)) {
//            LOGGER.error(ordenCompra + " Recarga ha retornado de Transbank con estado de FRACASO");
//            JSFMessagesHelper.addServiceErrorMessage("fracasoRecargaWebpay");
//            return postback;
//        }
//        else if(EXITO.equals(resultado)) {
//            LOGGER.error(ordenCompra + " Recarga ha retornado de Transbank con estado de EXITO");
//            
//            /*
//             * Obtencion recarga asociada
//             */
//            try {
//                recarga = recargaDelegate.consultarRecargaWebPayBean(ordenCompra);
//            } catch (DAOException e) {
//                LOGGER.error("DAOException caught: " + e.getMessage(), e);
//                JSFMessagesHelper.addServiceErrorMessage("consultarRecarga",
//                        new String[] { ordenCompra });
//            } catch (ServiceException e) {
//            	LOGGER.info("ServiceException caught: "+ idTransaccion + " - " + e.getCodigoRespuesta() 
//            				+ " - " + e.getDescripcionRespuesta());
//                JSFMessagesHelper.addErrorCodeMessage("recarga", e.getCodigoRespuesta());
//            } catch (Exception e) {
//                LOGGER.error("Exception caught: " + e.getMessage(), e);
//                JSFMessagesHelper.addServiceErrorMessage("consultarRecarga",
//                        new String[] { ordenCompra });
//            }
//
//            //Si recarga es null, es decir, no fue recuperada, no se busca el resto de los valores
//            if(recarga == null) {
//                return postback;
//            }
//            
//            try {
//
//                /*
//                 * Valores de Transbank a mostrar
//                 */
//                nroTarjeta = WebPayHelper.extraerParametroTransbank(recarga
//                        .getRespuestaTransbank(), "TBK_FINAL_NUMERO_TARJETA");
//                nroCuotas = WebPayHelper.extraerParametroTransbank(recarga
//                        .getRespuestaTransbank(), "TBK_NUMERO_CUOTAS_M001");
//
//                /*
//                 * Nombre del usuario
//                 */
//                ProfileWrapper profile = ProfileWrapperHelper
//                        .getProfile(JSFPortletHelper.getRequest());
//                nombreUsuario = ProfileWrapperHelper.getPropertyAsString(
//                        profile, "nombreUsuario");
//
//                /*
//                 * Vigencia de la recarga segun el monto
//                 */
//                recarga.setValidezRecarga(obtenerVigenciaMonto(recarga
//                        .getMontoRecarga()));
//
//            } catch (Exception e) {
//                LOGGER.error("Exception caught: " + e.getMessage());
//                JSFMessagesHelper.addServiceErrorMessage("recuperarRecarga",
//                        new String[] { ordenCompra });
//            }
//
//        }
//        //Caso de borde: Transbank retorna como resultado un valor distinto a EXITO y FRACASO
//        else {
//            LOGGER.error(ordenCompra + " Resultado '" + resultado
//                    + "' no es un valor reconocido");
//            JSFMessagesHelper.addServiceErrorMessage("recuperarRecarga",
//                    new String[] { ordenCompra });
//        }
//        
//        return postback;
//    }
//
//    private String obtenerVigenciaMonto(Double monto) {
//        String montoId = String.valueOf(monto.longValue());
//        MontoRecargaBean montoBean = ParametrosHelper.getMontoRecargaTarjetaCredito(montoId);
//        if(montoBean == null) {
//            LOGGER.warn("No se obtuvo informacion del monto de recarga de tarjeta credito " + montoId);
//        }
//        return montoBean.getVigencia();
//        
//    }
//    
//    /**
//     * Metodo utilitario para el chequeo de los parametros externos que se
//     * reciben al finalizar la recarga Webpay. Si este metodo detecta algun
//     * error, agrega los mensajes respectivos y hace las tareas de logging
//     * respectivas.
//     * 
//     * @return boolean true si los parametros tienen los valores esperados,
//     *         false en caso contrario.
//     */
//    private boolean checkParameters() {
//
//        boolean ret = true;
//        
//        if (Utils.isEmptyString(resultado)) {
//            LOGGER.error("parametro 'RESULTADO' no presente");
//            JSFMessagesHelper.addServiceErrorMessage("faltaInformacion");
//            ret = false;
//        }
//
//        if (Utils.isEmptyString(ordenCompra)) {
//            LOGGER.error("parametro 'ordenCompra' no presente");
//            JSFMessagesHelper.addServiceErrorMessage("faltaInformacion");
//            ret = false;
//        }
//
//        return ret;
//        
//    }
//    
//	/**
//	 * Retorna el valor de la cookie que contiene la descripcion de la
//	 * promocion de recarga seleccionada
//	 * @return
//	 */
//    public String getCookieDescPromoRecarga() {
//		String cookieValue = Utils.getCookieValue(JSFPortletHelper.getRequest(), 
//				"descPromoRecargaWPDesktopPrivado");
//		if (!cookieValue.trim().equals("")) {
//			return "_" + cookieValue;
//		} else {
//			return cookieValue;
//		}
//	}
//    
//	
//	/**
//	 * Realiza el redireccionamiento hacia la seccion de bolsas
//	 * @throws Exception
//	 */    
//    public String getPageLabelBolsas() {
//        try {
//            ProfileWrapper profileWrapper = ProfileWrapperHelper
//                    .getProfile(JSFPortletHelper.getRequest());
//            return JSFPortletHelper.getPreference(JSFPortletHelper
//                    .getPreferencesObject(), ProfileWrapperHelper
//                    .getPropertyAsString(profileWrapper, "mercado"), null);
//        } catch (Exception e) {
//            LOGGER.error("No se ha podido obtener el pageLabel"
//                    + e.getMessage(), e);
//            return "";
//        }
//    }
//}
