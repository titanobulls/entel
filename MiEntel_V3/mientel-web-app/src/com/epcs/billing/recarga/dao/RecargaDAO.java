/* Propiedad de Entel PCS. Todos los derechos reservados */
package com.epcs.billing.recarga.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import com.epcs.bean.DetalleRecargasBean;
import com.epcs.bean.FactibilidadRecargaEntelTicketBean;
import com.epcs.bean.FactibilidadRecargaMultitiendaBean;
import com.epcs.bean.FactibilidadRecargaWebPayBean;
import com.epcs.bean.RecargaBean;
import com.epcs.bean.RecargaEntelTicketBean;
import com.epcs.bean.RecargaHistoricoBean;
import com.epcs.bean.RecargaMultitiendaBean;
import com.epcs.bean.RecargaWebPayBean;
import com.epcs.billing.recarga.BillingRecargaService;
import com.epcs.billing.recarga.BillingRecargaServicePortType;
import com.epcs.billing.recarga.dao.util.EntelTicketHelper;
import com.epcs.billing.recarga.dao.util.MultitiendaHelper;
import com.epcs.billing.recarga.dao.util.WebPayHelper;
import com.epcs.billing.recarga.types.ActualizarRegistroRecWebPayType;
import com.epcs.billing.recarga.types.ConsultarHistoricoRecargasType;
import com.epcs.billing.recarga.types.ConsultarRegistroRecWebPayType;
import com.epcs.billing.recarga.types.DetalleMontoType;
import com.epcs.billing.recarga.types.DetalleRecargasType;
import com.epcs.billing.recarga.types.FactibilidadRecargaMultitiendaType;
import com.epcs.billing.recarga.types.FactibilidadRecargaTicketType;
import com.epcs.billing.recarga.types.FactibilidadWebPayType;
import com.epcs.billing.recarga.types.HistoricoRecargasType;
import com.epcs.billing.recarga.types.InsertarRegistroRecWebPayType;
import com.epcs.billing.recarga.types.RecargaWebPayType;
import com.epcs.billing.recarga.types.ResConsultarRegistroRecWebPayType;
import com.epcs.billing.recarga.types.ResultadoActualizarRegistroRecWebPayType;
import com.epcs.billing.recarga.types.ResultadoConsultarHistoricoRecargasType;
import com.epcs.billing.recarga.types.ResultadoConsultarRegistroRecWebPayType;
import com.epcs.billing.recarga.types.ResultadoFactibilidadRecargaMultitiendaType;
import com.epcs.billing.recarga.types.ResultadoFactibilidadRecargaTicketType;
import com.epcs.billing.recarga.types.ResultadoFactibilidadWebPayType;
import com.epcs.billing.recarga.types.ResultadoInsertarRegistroRecWebPayType;
import com.epcs.billing.recarga.types.ResultadoRecWebPayType;
import com.epcs.billing.recarga.types.ResultadoRecargaMultitiendaType;
import com.epcs.billing.recarga.types.ResultadoRecargaTicketType;
import com.epcs.billing.recarga.types.ResultadoRecargaWebPayType;
import com.epcs.recursoti.configuracion.DateHelper;
import com.epcs.recursoti.configuracion.MiEntelProperties;
import com.epcs.recursoti.configuracion.Utils;
import com.epcs.recursoti.configuracion.WordNumberHelper;
import com.epcs.recursoti.configuracion.locator.WebServiceLocator;
import com.epcs.recursoti.configuracion.locator.WebServiceLocatorException;
import com.epcs.recursoti.excepcion.DAOException;
import com.epcs.recursoti.excepcion.ServiceException;

/**
 * DAO para los servicios de Recarga: multitiendas, tarjeta credito (webpay) y
 * entelticket
 * 
 * @author jlopez (I2B) en nombre de Absalon Opazo (Atencion al Cliente,
 *         EntelPcs)
 * 
 */
public class RecargaDAO {

    /**
     * Logger para RecargaDAO
     */
    private static final Logger LOGGER = Logger.getLogger(RecargaDAO.class);

    private static final String CODIGO_RESPUESTA_OK = MiEntelProperties
            .getProperty("servicios.codigoRespuesta.exito");

    private static final String TBK_COD_AUT_M001 = "TBK_COD_AUT_M001";

    // ------------------------------------------------------ WEBPAY

    /**
     * Ingresa una nueva recarga webpay, retornando el numero de orden de
     * compra.<br>
     * 
     * @param numeroPcs
     *            Numero al que se realiza la recarga
     * @param monto
     *            monto de la recarga
     * @param idp
     *            Identificador PCS asociado a la recarga
     * @return {@link RecargaWebPayBean} instancia de la recarga recien
     *         ingresada
     * @throws DAOException
     *             ante errores en la ejecucion del metodo
     * @throws ServiceException
     *             Si el Servcio retorna un mensaje de error
     */
    public RecargaWebPayBean ingresarRecargaWebPay(String numeroPcs,
            Double monto, String idp) throws DAOException, ServiceException {

        BillingRecargaServicePortType port = null;
        LOGGER.info("Instanciando el port "
                + BillingRecargaServicePortType.class);
        try {
            port = (BillingRecargaServicePortType) WebServiceLocator
                    .getInstance().getPort(BillingRecargaService.class,
                            BillingRecargaServicePortType.class);
        } catch (WebServiceLocatorException e) {
            LOGGER.error("Error al inicializar el Port "
                    + BillingRecargaServicePortType.class, e);
            LOGGER.error( new DAOException(e));
        }

        InsertarRegistroRecWebPayType request = new InsertarRegistroRecWebPayType();
        ResultadoInsertarRegistroRecWebPayType response = null;

        String ordenCompra = WebPayHelper.generarIdOrdenDeCompra();
        LOGGER.info("Orden de compra generado: " + ordenCompra);

        Date fechaTransaccion = new Date();

        try {
            // Constantes, obtenidas desde properties
            String tipoPago = MiEntelProperties
                    .getProperty("recarga.webpay.ingresar.tipoPago");
            request.setTipoPago(tipoPago);
            
            //El parametro4 es el numero en que se efectuara la recarga
            request.setParametro4(numeroPcs);

            // Valores generados
            request.setOrdCompra(ordenCompra);
            request.setFechaTransaccion(DateHelper.format(fechaTransaccion,
                    DateHelper.FORMAT_yyyyMMdd_HHmmss));
            // IDP es obtenido como parametro del metodo
            request.setIDP(idp);
            
            
            //Monto
            DetalleMontoType detalleMonto = new DetalleMontoType();
            detalleMonto.setOrdenCompra(ordenCompra);
            detalleMonto.setMonto(String.valueOf(monto.longValue()));
            
            String param = MiEntelProperties
            .getProperty("recarga.webpay.ingresar.parametroPersonalizable");
            detalleMonto.setParametroPersonalizable(param);
            
            request.getDetalleMonto().add(detalleMonto);
            
            LOGGER.info("Request -> getIdp: "+request.getIDP()+", monto: "+monto+" numeroPcs: "+numeroPcs+", param personalizable: "+param);
            LOGGER.info("## Invocando servicio InsertarRegistroRecWebPay ##");
            response = port.insertarRegistroRecWebPay(request);

            
            //Logger detallado para log de Recarga
            if(LOGGER.isDebugEnabled()) {
                LOGGER.debug(ordenCompra + " Registro recarga WebPay ingresado");
                LOGGER.debug(ordenCompra + "     numeroPcs : " + numeroPcs);
                LOGGER.debug(ordenCompra + "     monto : " + monto);
                LOGGER.debug(ordenCompra + "     idp : " + idp);
                LOGGER.debug(ordenCompra + "     fecha transaccion : " + fechaTransaccion);
                LOGGER.debug(ordenCompra + "     tipo pago: " + tipoPago);
                LOGGER.debug(ordenCompra + "     param. personalizable: " + param);
            }
            
        } catch (Exception e) {
            LOGGER.error("Exception caught on Service invocation: "
                    + "insertarRegistroRecWebPay", e);
            LOGGER.error( new DAOException(e));
        }

        String codigoRespuesta = response.getRespuesta().getCodigo();
        String descripcionRespuesta = response.getRespuesta().getDescripcion();

        LOGGER.info("codigoRespuesta " + codigoRespuesta);
        LOGGER.info("OrdenCompraRespuesta: "+response.getOrdenCompra());

        if (Utils.isEmptyString(codigoRespuesta)) {
            LOGGER.error( new DAOException("insertarRegistroRecWebPay(: Servicio no responde "
                    + "con codigoRespuesta"));
        }

        if (CODIGO_RESPUESTA_OK.equals(codigoRespuesta)) {

            
            try {
                RecargaWebPayBean recarga = new RecargaWebPayBean();

                
                String ordenCompraResponse = response.getOrdenCompra();
                if(LOGGER.isDebugEnabled()) {
                    LOGGER.debug(ordenCompra + " orden de compra obtenida: " + ordenCompra);
                }
                
                recarga.setOrdenCompra(ordenCompraResponse);
                recarga.setFechaSolicitud(fechaTransaccion);
                recarga.setMontoRecarga(monto);
                recarga.setNumeroPcs(numeroPcs);
                recarga.setIdp(idp);
                
                return recarga;
            } catch (Exception e) {
                LOGGER.error("Exception caught on Service response: "
                        + "insertarRegistroRecWebPay" , e);
                LOGGER.error( new DAOException(e));
            }

        }
        else {
            LOGGER
                    .error("insertarRegistroRecWebPay: Service error code received: "
                            + codigoRespuesta + " - " + descripcionRespuesta);
            LOGGER.error( new ServiceException(codigoRespuesta, descripcionRespuesta));
        }
        return new RecargaWebPayBean();
    }

    /**
     * Valida la factibilidad de recarga, de ser positivo, la solicitud es
     * ingresada a la cola de transacciones de recargas.<br>
     * Si es negativo, este metodo lanzara una ServiceException, con el codigo
     * de error y descripcion del motivo por el que no hay factibilidad
     * 
     * @param recarga
     *            {@link RecargaBean} Recarga solicitada
     * @throws DAOException
     *             ante errores en la ejecucion del metodo
     * @throws ServiceException
     *             Si no hay factibilidad de recarga, o el Servcio retorna un
     *             mensaje de error
     */
    public FactibilidadRecargaWebPayBean factibilidadRecargaWebPay(
            RecargaWebPayBean recarga, boolean eligeTuPromo, String codPromoF) throws DAOException, ServiceException {

        if (recarga == null) {
        	LOGGER.info("RecargaWebPayBean es nulo!");
            LOGGER.error( new DAOException("Recarga cannot be null"));
        }

        BillingRecargaServicePortType port = null;
        LOGGER.info("Instanciando el port "
                + BillingRecargaServicePortType.class);
        try {
            port = (BillingRecargaServicePortType) WebServiceLocator
                    .getInstance().getPort(BillingRecargaService.class,
                            BillingRecargaServicePortType.class);
        } catch (WebServiceLocatorException e) {
            LOGGER.error("Error al inicializar el Port "
                    + BillingRecargaServicePortType.class, e);
            LOGGER.error( new DAOException(e));
        }

        // request and response objects
        FactibilidadWebPayType request = RecargaTypeFactory
                .buildFactibilidadWebPayType(eligeTuPromo);
        ResultadoFactibilidadWebPayType response = null;

        // Codigo unico de transaccion
        String codUnicoTransaccion = WebPayHelper
                .generarCodigoUnicoTransaccion();
        
        LOGGER.info("ETPMovil: "+recarga.getNumeroPcs()+", Codigo unico transaccion: "+codUnicoTransaccion+", recarga.getOrdenCompra: "+recarga.getOrdenCompra());
        try {
            String fechaSolicitud = DateHelper.format(recarga
                    .getFechaSolicitud(), DateHelper.FORMAT_yyyyMMdd);
            request.setFechaSolicitud(fechaSolicitud);

            String montoRecarga = String.valueOf(recarga.getMontoRecarga().longValue());
            request.setMontoRecarga(montoRecarga);
            request.setMsisdnRecarga(recarga.getNumeroPcs());
            request.setCodigoUnicoTransaccion(codUnicoTransaccion);
            
            if (!codPromoF.trim().equals("")) {
            	
            	request.setCodLocal(codPromoF);
            	
            	LOGGER.info("ETPMovil: " + recarga.getNumeroPcs() + ", request.getCodLocal: " + codPromoF+", eligeTuPromo: "+eligeTuPromo);
            	      	            
            } else {
            	
            	LOGGER.info("ETPMovil: " + recarga.getNumeroPcs() + ", request.getCodLocal: " + codPromoF+", eligeTuPromo: "+eligeTuPromo);
            }

            response = port.factibilidadWebPay(request);
            
            //Logger detallado para log de Recarga
            if(LOGGER.isDebugEnabled()) {
                
                String ordenCompra = recarga.getOrdenCompra();
                
                LOGGER.debug(ordenCompra + " Factibilidad recarga WebPay consultada");
                LOGGER.debug(ordenCompra + "     numeroPcs : " + recarga.getNumeroPcs());
                LOGGER.debug(ordenCompra + "     monto : " + montoRecarga);
                LOGGER.debug(ordenCompra + "     cod. unico transaccion : " + codUnicoTransaccion);
                LOGGER.debug(ordenCompra + "     fecha solicitud : " + fechaSolicitud);
                LOGGER.debug(ordenCompra + "     Local : " + request.getCodLocal()); 
            }
            
        } catch (Exception e) {
            LOGGER.error("Exception caught on Service invocation: "
                    + "factibilidadWebPay", e);
            LOGGER.error( new DAOException(e));
        }

        String codigoRespuesta = response.getRespuesta().getCodigo();
        String descripcionRespuesta = response.getRespuesta().getDescripcion();

        LOGGER.info("codigoRespuesta " + codigoRespuesta);

        if (Utils.isEmptyString(codigoRespuesta)) {
            LOGGER.error( new DAOException("factibilidadWebPay: Servicio no responde "
                    + "con codigoRespuesta"));
        }

        if (CODIGO_RESPUESTA_OK.equals(codigoRespuesta)) {

            try {
                
            	LOGGER.info("Resp Factib, codComercio: "+response.getResultadoFactibilidadWebPay().getCodComercio());
            	LOGGER.info("Resp Factib, codIntegrador: "+response.getResultadoFactibilidadWebPay().getCodIntegrador());
            	LOGGER.info("Resp Factib, codRespuesta: "+response.getResultadoFactibilidadWebPay().getCodigoRespuesta());
            	LOGGER.info("Resp Factib, codDistribucion: "+response.getResultadoFactibilidadWebPay().getCodDistribuidor());
                
                FactibilidadRecargaWebPayBean factibilidad = new FactibilidadRecargaWebPayBean();
                factibilidad.setNumeroPcs(response.getResultadoFactibilidadWebPay().getMsisdnRecarga());
                factibilidad.setCodUnicoTransaccion(response.getResultadoFactibilidadWebPay().getCodigoUnicoTransaccion());

                return factibilidad;
            } catch (Exception e) {
                LOGGER.error("Exception caught on Service response: "
                        + "factibilidadWebPay", e);
                LOGGER.error( new DAOException(e));
            }
        }
        else {
        	LOGGER.info("Resp Factib, codComercio: "+response.getResultadoFactibilidadWebPay().getCodComercio());
        	LOGGER.info("Resp Factib, codIntegrador: "+response.getResultadoFactibilidadWebPay().getCodIntegrador());
        	LOGGER.info("Resp Factib, codRespuesta: "+response.getResultadoFactibilidadWebPay().getCodigoRespuesta());
        	LOGGER.info("Resp Factib, codDistribucion: "+response.getResultadoFactibilidadWebPay().getCodDistribuidor());
            
        	
            LOGGER.info("factibilidadWebPay: Service error code received: "
                    + codigoRespuesta + " - " + descripcionRespuesta);
            LOGGER.error( new ServiceException(codigoRespuesta, descripcionRespuesta));
        }
        return new FactibilidadRecargaWebPayBean();
    }

    public RecargaWebPayBean consultarRecargaWebPayBean(String ordenCompra)
            throws DAOException, ServiceException {

        BillingRecargaServicePortType port = null;
        LOGGER.info("Instanciando el port " + BillingRecargaServicePortType.class);
        try {
            port = (BillingRecargaServicePortType) WebServiceLocator.getInstance().getPort(
                    BillingRecargaService.class, BillingRecargaServicePortType.class);
        } catch (WebServiceLocatorException e) {
            LOGGER.error("Error al inicializar el Port " + BillingRecargaServicePortType.class, e);
            LOGGER.error( new DAOException(e));
        }

        ConsultarRegistroRecWebPayType request = new ConsultarRegistroRecWebPayType();
        ResultadoConsultarRegistroRecWebPayType response = null;
        
        try {
            request.setOrdenCompra(ordenCompra);

            response = port.consultarRegistroRecWebPay(request);
            
            //Logger detallado para log de Recarga
            if(LOGGER.isDebugEnabled()) {
                LOGGER.debug(ordenCompra + " consulta registro recarga WebPay");
            }
            
        } catch (Exception e) {
            LOGGER.error("Exception caught on Service invocation: consultarRegistroRecWebPay", e);
            LOGGER.error( new DAOException(e));
        }

        String codigoRespuesta = response.getRespuesta().getCodigo();
        String descripcionRespuesta = response.getRespuesta().getDescripcion();

        LOGGER.info("codigoRespuesta " + codigoRespuesta);
        LOGGER.info("response.getResultadoConsultarRegRecWebPay().getIDP(): "+response.getResultadoConsultarRegRecWebPay().getIDP());

        if (Utils.isEmptyString(codigoRespuesta)) {
            LOGGER.error( new DAOException("consultarRegistroRecWebPay: Servicio no respondio "
                    + "con codigoRespuesta"));
        }

        if (CODIGO_RESPUESTA_OK.equals(codigoRespuesta)) {

            try {
                ResConsultarRegistroRecWebPayType resultadoRegistroTransbank = response
                        .getResultadoConsultarRegRecWebPay();

                //Logger detallado para log de Recarga
                if (LOGGER.isDebugEnabled()) {

                    LOGGER.debug(ordenCompra
                            + " Resultado consulta registro recarga WebPay");
                    LOGGER.debug(ordenCompra + "     ordenCompra : "
                            + resultadoRegistroTransbank.getOrdenCompra());
                    LOGGER.debug(ordenCompra + "     idp : "
                            + resultadoRegistroTransbank.getIDP());
                    LOGGER.debug(ordenCompra + "     fechaTransaccion : "
                            + resultadoRegistroTransbank.getFechaTransaccion());
                    LOGGER.debug(ordenCompra + "     parametro4 : "
                            + resultadoRegistroTransbank.getParametro4());
                    LOGGER.debug(ordenCompra + "     parametro5 : "
                            + resultadoRegistroTransbank.getParametro5());
                    LOGGER.debug(ordenCompra + "     parametro6 : "
                            + resultadoRegistroTransbank.getParametro6());
                    LOGGER.debug(ordenCompra
                            + "     respuestaTransbank : "
                            + resultadoRegistroTransbank
                                    .getRespuestaTransbank());
                    LOGGER.debug(ordenCompra + "     tipoRecarga : "
                            + resultadoRegistroTransbank.getTipoRecarga());
                    LOGGER.debug(ordenCompra + "     detalleMonto : ");
                    Iterator<DetalleMontoType> it = resultadoRegistroTransbank
                            .getDetalleMonto().iterator();
                    while (it.hasNext()) {
                        LOGGER.debug(ordenCompra + "        monto:"
                                + it.next().getMonto());
                    }
                }                

                RecargaWebPayBean recarga = new RecargaWebPayBean();
                
                recarga.setOrdenCompra(resultadoRegistroTransbank.getOrdenCompra());
                recarga.setNumeroPcs(resultadoRegistroTransbank.getParametro4());
                
                String codUnicoAutorizacion = extraerCodUnicoAutorizacion(resultadoRegistroTransbank.getRespuestaTransbank());
                recarga.setCodigoUnicoAutorizacion(codUnicoAutorizacion);
                
                recarga.setRespuestaTransbank(resultadoRegistroTransbank.getRespuestaTransbank());
                
                Date fechaSolicitud = DateHelper.parseDate(resultadoRegistroTransbank.getFechaTransaccion(), DateHelper.FORMAT_yyyyMMdd_HHmmss);
                recarga.setFechaSolicitud(fechaSolicitud);
                
                Double montoRecarga = obtenerMontoRecarga(resultadoRegistroTransbank.getDetalleMonto());
                recarga.setMontoRecarga(montoRecarga);
                recarga.setIdp(resultadoRegistroTransbank.getIDP());
                
                return recarga;
            } catch (Exception e) {
                LOGGER.error("Exception caught on Service response: " +
                        "consultarRegistroRecWebPay", e);
                LOGGER.error( new DAOException(e));
            }
        }
        else {
            LOGGER.error("method: Service error code received: "
                    + codigoRespuesta + " - " + descripcionRespuesta);
            LOGGER.error( new ServiceException(codigoRespuesta, descripcionRespuesta));
        }
        return new RecargaWebPayBean();
    }
        
    /**
     * Extrae el valor del parametro TBK_COD_AUT_M001 desde el String de
     * respuesta recibido por Transbank.<br>
     * Si el parametro no se encuentra, o el string tiene un formato no valido,
     * este metodo retorna <code>null</code>
     * 
     * @param respuestaTransbank
     *            String original recibido por Transbank
     * @return String con valor de TBK_COD_AUT_M001. <code>null</code> en caso
     *         de no encontrarse, o formato invalido
     */
    private String extraerCodUnicoAutorizacion(String respuestaTransbank) {
        return WebPayHelper.extraerParametroTransbank(respuestaTransbank, TBK_COD_AUT_M001);
    }

    /**
     * Obtiene el monto de la recarga, a partir de la lista de detalles de monto
     * que retorna el registro de recarga.<br>
     * Actualmente, la logica de este metodo es obtener solo la primera
     * ocurrencia en la lista. Por tanto, este metodo retornara
     * <code>null</code> si la lista es null o <code>isEmpty()</code> retorna
     * <code>true</code><br>
     * Si la logica cambia en el futuro, debe modificarse este metodo.
     * 
     * @param detalleMonto
     *            List con detalles de montos
     * @return Double con monto de la recarga. <code>null</code> si se da la
     *         siguiente condicion:<br>
     * 
     *         <pre>
     * detalleMonto == null || detalleMonto.isEmpty()
     * </pre>
     * 
     */
    private Double obtenerMontoRecarga(List<DetalleMontoType> detalleMonto) {

        if(detalleMonto == null || detalleMonto.isEmpty()) {
            return null;
        }
        
        /*
         * Actualmente, la logica de obtencion de monto, extrae solo la 1era ocurrencia
         * de la lista.
         * Si la logica cambia en el futuro, modificar este metodo
         */
        Double monto = null;
        try {
            monto = Double.valueOf(detalleMonto.get(0).getMonto());
        } catch (Exception e) {
            return null;
        }
        
        return monto;
    }

    /**
     * Actualiza una recarga webpay con el resultado de Transbank
     * <code>recargaWebPay</code>
     * 
     * @param ordenCompra
     *            {@link RecargaWebPayBean} indica la recarga que debe
     *            realizarce.
     * @throws DAOException
     *             ante errores en la ejecucion del metodo
     * @throws ServiceException
     *             Si no hay factibilidad de recarga, o el Servcio retorna un
     *             mensaje de error
     */
    public void actualizarRecargaWebPay(String ordenCompra,
            String parametrosWebPay) throws DAOException,
            ServiceException {

        if (ordenCompra == null || parametrosWebPay == null) {
            LOGGER.error( new DAOException("Parametros de entrada no pueden ser null"));
        }

        BillingRecargaServicePortType port = null;
        LOGGER.info("Instanciando el port "
                + BillingRecargaServicePortType.class);
        try {
            port = (BillingRecargaServicePortType) WebServiceLocator
                    .getInstance().getPort(BillingRecargaService.class,
                            BillingRecargaServicePortType.class);
        } catch (WebServiceLocatorException e) {
            LOGGER.error("Error al inicializar el Port "
                    + BillingRecargaServicePortType.class, e);
            LOGGER.error( new DAOException(e));
        }

        // request y response del servicio
        ActualizarRegistroRecWebPayType request = new ActualizarRegistroRecWebPayType();
        ResultadoActualizarRegistroRecWebPayType response = null;

        try {
            request.setOrdenCompra(ordenCompra);
            request.setParametrosTransBank(parametrosWebPay);

            LOGGER.info("request-parametrosTransbank: " + parametrosWebPay);
            LOGGER.info("## Invocando ActualizarRegistroRecWebPay ##");
            response = port.actualizarRegistroRecWebPay(request);
            
            //Logger detallado para log de Recarga
            if(LOGGER.isDebugEnabled()) {
                
                LOGGER.debug(ordenCompra + " Actualizacion recarga WebPay");
                LOGGER.debug(ordenCompra + "     ordenCompra : " + ordenCompra);
                LOGGER.debug(ordenCompra + "     parametrosTransbank : " + parametrosWebPay);
            }
            
        } catch (Exception e) {
            LOGGER.error("Exception caught on Service " +
            		"invocation: actualizarRegistroRecWebPay", e);
            LOGGER.error( new DAOException(e));
        }

        String codigoRespuesta = response.getRespuesta().getCodigo();
        String descripcionRespuesta = response.getRespuesta().getDescripcion();

        LOGGER.info("codigoRespuesta " + codigoRespuesta);
        LOGGER.info("ordenCompra: " + response.getOrdenCompra());

        if (Utils.isEmptyString(codigoRespuesta)) {
            LOGGER.error( new DAOException(
                    "actualizarRegistroRecWebPay: Servicio no respondio "
                            + "con codigoRespuesta"));
        }

        if (CODIGO_RESPUESTA_OK.equals(codigoRespuesta)) {

            String ordenCompraOut = response.getOrdenCompra();

            // Validaciones de consistencia de retorno de servicio

            // Orden de compra vacia
            if (Utils.isEmptyString(ordenCompraOut)) {
                LOGGER.error( new DAOException(
                        "actualizarRegistroRecWebPay: Servicio no "
                                + "retorno orden compra esperada: "
                                + ordenCompraOut));
            }

            // Retorno de orden de compra no coincida con parametro de entrada
            if (!ordenCompraOut.equals(ordenCompra)) {
                LOGGER
                        .warn("Orden de compra obtenida no concuerda con valor entregado: "
                                + "IN: '"
                                + ordenCompra
                                + "' OUT: '" + ordenCompraOut + "'");
            }
            
            //Logger detallado para log de Recarga
            if(LOGGER.isDebugEnabled()) {
                
                LOGGER.debug(ordenCompra + " Resultado actualizacion recarga WebPay");
                LOGGER.debug(ordenCompra + "     ordenCompra : " + ordenCompraOut);
            }

        }
        else {
            LOGGER
                    .error("actualizarRegistroRecWebPay: Service error code received: "
                            + codigoRespuesta + " - " + descripcionRespuesta);
            LOGGER.error( new ServiceException(codigoRespuesta, descripcionRespuesta));
        }

    }

    /**
     * Hace efectiva una recarga webpay.<br>
     * La instancia de {@link RecargaWebPayBean} que retorna no es la misma que
     * se envia como parametro, ademas de contener campos de la respuesta al
     * servicio
     * 
     * @param recarga
     *            {@link RecargaWebPayBean} recarga a efectuarse
     * @return {@link RecargaWebPayBean} con los valores de la recarga efectuada,
     *         incluye informacion de Bonos, mensajes comerciales y nuevo saldo
     * @throws DAOException
     *             ante errores en la ejecucion del metodo
     * @throws ServiceException
     *             Si no hay factibilidad de recarga, o el Servcio retorna un
     *             mensaje de error
     */
    public RecargaWebPayBean efectuarRecargaWebPay(RecargaWebPayBean recarga, boolean eligeTuPromo)
            throws DAOException, ServiceException {

        BillingRecargaServicePortType port = null;
        LOGGER.info("Instanciando el port "
                + BillingRecargaServicePortType.class);
        try {
            port = (BillingRecargaServicePortType) WebServiceLocator
                    .getInstance().getPort(BillingRecargaService.class,
                            BillingRecargaServicePortType.class);
        } catch (WebServiceLocatorException e) {
            LOGGER.error("Error al inicializar el Port "
                    + BillingRecargaServicePortType.class, e);
            LOGGER.error( new DAOException(e));
        }

        // Service request and response objects
        RecargaWebPayType request;
        ResultadoRecargaWebPayType response = null;

        try {
            request = RecargaTypeFactory.buildRecargaWebPayType(eligeTuPromo);

            if (eligeTuPromo) {
				String[] idpPromo = recarga.getIdp().split("-");
				if (idpPromo.length == 2) {
					request.setCodLocal(idpPromo[1]);	
				}
			}
            
            request.setCodigoUnicoTransaccion(recarga.getOrdenCompra());
            request.setCodUnicoAutorizacion(recarga
                    .getCodigoUnicoAutorizacion());
            request.setFechaSolicitud(DateHelper.format(recarga
                    .getFechaSolicitud(), DateHelper.FORMAT_yyyyMMdd));
            
            String montoRecarga = String.valueOf(recarga.getMontoRecarga().longValue());
            request.setMontoRecarga(montoRecarga);
            
            request.setMsisdnRecarga(recarga.getNumeroPcs());
            
            LOGGER.info("request.getCodLocal: "+request.getCodLocal());
            LOGGER.info("## INVOCANDO RECARGA WEBPAY ##");
            response = port.recargaWebPay(request);
            
            //Logger detallado para log de Recarga
            if(LOGGER.isDebugEnabled()) {
                
                String ordenCompra = recarga.getOrdenCompra();
                
                LOGGER.debug(ordenCompra + " Efectuar recarga WebPay");
                LOGGER.debug(ordenCompra + "     codigoUnicoTransaccion : " + request.getCodigoUnicoTransaccion());
                LOGGER.debug(ordenCompra + "     codigoUnicoAutorizacion : " + request.getCodUnicoAutorizacion());
                LOGGER.debug(ordenCompra + "     fechaSolicitud : " + request.getFechaSolicitud());
                LOGGER.debug(ordenCompra + "     montoRecarga : " + request.getMontoRecarga());
                LOGGER.debug(ordenCompra + "     numeroPcs : " + request.getMsisdnRecarga());
            }
            
        } catch (Exception e) {
            LOGGER.error(
                    "Exception caught on Service invocation: recargaWebPay", e);
            LOGGER.error( new DAOException(e));
        }

        String codigoRespuesta = response.getRespuesta().getCodigo();
        String descripcionRespuesta = response.getRespuesta().getDescripcion();

        LOGGER.info("codigoRespuesta " + codigoRespuesta);
        LOGGER.info("ETPMovil: "+response.getResultadoRecargaWebPay().getMsisdnRecarga()+", CodComercio: "+response.getResultadoRecargaWebPay().getCodComercio());
        LOGGER.info("CodLocal: "+response.getResultadoRecargaWebPay().getCodLocal()+ ", MontoRecarga: "+response.getResultadoRecargaWebPay().getMontoRecarga());

        if (Utils.isEmptyString(codigoRespuesta)) {
            LOGGER.error( new DAOException("recargaWebPay: Servicio no respondio "
                    + "con codigoRespuesta"));
        }

        if (CODIGO_RESPUESTA_OK.equals(codigoRespuesta)) {

            try {
                ResultadoRecWebPayType resultado = response
                        .getResultadoRecargaWebPay();

                // Logger detallado para log de Recarga
                if (LOGGER.isDebugEnabled()) {

                    String ordenCompra = recarga.getOrdenCompra();

                    LOGGER.debug(ordenCompra
                            + " Resultado efectuar recarga WebPay");
                    LOGGER.debug(ordenCompra + "     codigoUnicoTransaccion : "
                            + resultado.getCodUnicoTransaccion());
                    LOGGER.debug(ordenCompra + "     fechaSolicitud : "
                            + resultado.getFechaSolicitud());
                    LOGGER.debug(ordenCompra + "     montoRecarga : "
                            + resultado.getMontoRecarga());
                    LOGGER.debug(ordenCompra + "     numeroPcs : "
                            + resultado.getMsisdnRecarga());
                    LOGGER.debug(ordenCompra + "     montoBono : "
                            + resultado.getMontoBono());
                    LOGGER.debug(ordenCompra + "     descuentoAcumulado : "
                            + resultado.getDescuentoAcumulado());
                    LOGGER.debug(ordenCompra + "     nuevoSaldo : "
                            + resultado.getNuevoSaldo());
                    LOGGER.debug(ordenCompra + "     validezRecarga : "
                            + resultado.getValidezRecarga());
                    LOGGER.debug(ordenCompra + "     mensajePublicidad : "
                            + resultado.getMensajePublicidad());
                }
                
                RecargaWebPayBean resultadoRecarga = new RecargaWebPayBean();

                resultadoRecarga.setCodigoUnicoAutorizacion(resultado
                        .getCodUnicoAutorizacion());

                resultadoRecarga.setFechaSolicitud(DateHelper.parseDate(
                        resultado.getFechaSolicitud(),
                        DateHelper.FORMAT_yyyyMMdd));

                resultadoRecarga.setFechaTransaccionTransbank(resultado.getFechaSolicitud());
                
                resultadoRecarga.setMontoRecarga(Double.valueOf(resultado
                        .getMontoRecarga()));

                resultadoRecarga.setNumeroPcs(resultado.getMsisdnRecarga());

                resultadoRecarga.setOrdenCompra(resultado
                        .getCodUnicoTransaccion());

                resultadoRecarga.setMontoBono(Double.valueOf(resultado
                        .getMontoBono()));

                resultadoRecarga.setDescuentoAcumulado(resultado
                        .getDescuentoAcumulado());

                resultadoRecarga.setNuevoSaldo(Double.valueOf(resultado
                        .getNuevoSaldo()));

                resultadoRecarga.setValidezRecarga(resultado
                        .getValidezRecarga());

                resultadoRecarga.setMensajePublicidad(resultado
                        .getMensajePublicidad());

                // Atributos desde RecargaWebPayBean de entrada
				resultadoRecarga.setIdp(recarga.getIdp());
				resultadoRecarga.setNumeroPcs(recarga.getNumeroPcs());
				resultadoRecarga.setRespuestaTransbank(recarga
						.getRespuestaTransbank());

                return resultadoRecarga;

            } catch (Exception e) {
                LOGGER.error("Exception caught on Service " +
                		"response: recargaWebPay", e);
                LOGGER.error( new DAOException(e));
            }
        }
        else {
            LOGGER.error("recargaWebPay: Service error code received: "
                    + codigoRespuesta + " - " + descripcionRespuesta);
            LOGGER.error( new ServiceException(codigoRespuesta, descripcionRespuesta));
        }
        return new RecargaWebPayBean();
    }

    // --------------------------------------------------- MULTITIENDA

    /**
     * Valida la factibilidad de recarga, de ser positivo, la solicitud es
     * ingresada a la cola de transacciones de recargas.<br>
     * Si es negativo, este metodo lanzara una ServiceException, con el codigo
     * de error y descripcion del motivo por el que no hay factibilidad
     * 
     * @param recarga
     *            {@link RecargaMultitiendaBean} Recarga solicitada
     * @throws DAOException
     *             ante errores en la ejecucion del metodo
     * @throws ServiceException
     *             Si no hay factibilidad de recarga, o el Servcio retorna un
     *             mensaje de error
     */
    public FactibilidadRecargaMultitiendaBean factibilidadRecargaMultitienda(
            RecargaMultitiendaBean recarga) throws DAOException,
            ServiceException {
        
        FactibilidadRecargaMultitiendaBean factibilidadMultitienda = new FactibilidadRecargaMultitiendaBean();
        
        BillingRecargaServicePortType port = null;
        LOGGER.info("Instanciando el port " + BillingRecargaServicePortType.class);
        try {
            port = (BillingRecargaServicePortType) WebServiceLocator.getInstance().getPort(
                    BillingRecargaService.class, BillingRecargaServicePortType.class);
        } catch (WebServiceLocatorException e) {
            LOGGER.error("Error al inicializar el Port " + BillingRecargaServicePortType.class, e);
            LOGGER.error( new DAOException(e));
        }
        
        FactibilidadRecargaMultitiendaType fact = new FactibilidadRecargaMultitiendaType();
        fact.setCodEmpresa(MiEntelProperties.getProperty("recarga.multitienda.factibilidad.codEmpresa"));                        
        fact.setCodTransMultitienda(MiEntelProperties.getProperty("recarga.multitienda.factibilidad.codTransMultimedia"));
        fact.setCodLocal(MiEntelProperties.getProperty("recarga.multitienda.factibilidad.codLocal"));
        fact.setCodigoUnicoAutorizacion(MiEntelProperties.getProperty("recarga.multitienda.factibilidad.codUnicoAutorizacion"));
        fact.setDiaPago(MiEntelProperties.getProperty("recarga.multitienda.factibilidad.diaPago"));        
        fact.setIdSistema(MiEntelProperties.getProperty("recarga.multitienda.factibilidad.idSistema"));
        fact.setMetodo(MiEntelProperties.getProperty("recarga.multitienda.factibilidad.metodo"));
        
        fact.setMontoRecarga(String.valueOf(recarga.getMontoRecarga()));
        fact.setMsisdnRecarga(recarga.getNumeroPcs());
        fact.setNumeroCuotas(String.valueOf(recarga.getCuotas()));
        fact.setClaveTarjeta(recarga.getClaveTarjeta());
        fact.setRut(String.valueOf(recarga.getNumeroTarjeta()));//Nro. tarjeta
        
        fact.setFechaSolicitud(DateHelper.format(new Date(), DateHelper.FORMAT_yyyyMMdd));        
        fact.setPlataforma(MiEntelProperties.getProperty("recarga.multitienda.factibilidad.plataforma"));        
        fact.setTipoOperacion(MiEntelProperties.getProperty("recarga.multitienda.factibilidad.tipoOperacion"));
        fact.setCodUnicoTransaccion(MultitiendaHelper.generarCodigoUnicoTransaccion());
        
        ResultadoFactibilidadRecargaMultitiendaType result = port.factibilidadRecargaMultitienda(fact);
        
        String codigoRespuesta = result.getRespuesta().getCodigo();
        String descripcionRespuesta = result.getRespuesta().getDescripcion();

        LOGGER.info("codigoRespuesta " + codigoRespuesta);
        
        if (CODIGO_RESPUESTA_OK.equals(codigoRespuesta)) {
            ResultadoRecargaMultitiendaType resMulti = result.getResultadoFactibilidadRecargaMultitienda(); 
            factibilidadMultitienda.setCodEmpresa(resMulti.getCodEmpresa());
            factibilidadMultitienda.setCodigoUnicoAutorizacion(resMulti.getCodigoUnicoAutorizacion());
            factibilidadMultitienda.setCodLocal(resMulti.getCodLocal());
            factibilidadMultitienda.setCodRespuestaTransA(resMulti.getCodRespuestaTransA());
            factibilidadMultitienda.setCodRespuestaTransB(resMulti.getCodRespuestaTransB());
            factibilidadMultitienda.setCodTransMultitienda(resMulti.getCodTransMultitienda());
            factibilidadMultitienda.setCodUnicoTransaccion(resMulti.getCodUnicoTransaccion());
            factibilidadMultitienda.setMontoBono(resMulti.getMontoBono());
            factibilidadMultitienda.setMontoRecarga(recarga.getMontoRecarga());
            factibilidadMultitienda.setNuevoSaldo(resMulti.getNuevoSaldo());
            factibilidadMultitienda.setNumeroPcs(resMulti.getMsisdnRecarga());
            factibilidadMultitienda.setPlataforma(resMulti.getPlataforma());
            factibilidadMultitienda.setTipoOperacion(resMulti.getTipoOperacion());
            factibilidadMultitienda.setValidezRecarga(resMulti.getValidezRecarga());
            return factibilidadMultitienda;
        }
        else {
            LOGGER.error("factibilidadMultitienda: Service error code received: "
                    + codigoRespuesta + " - " + descripcionRespuesta);
            LOGGER.error( new ServiceException(codigoRespuesta, descripcionRespuesta));
        }
        return new FactibilidadRecargaMultitiendaBean();                
    }

    /**
     * Hace efectiva una recarga hecha por medio de tarjeta de multitienda
     * 
     * @param recarga
     *            {@link RecargaMultitiendaBean} recarga a realizar
     * @throws DAOException
     *             ante errores en la ejecucion del metodo
     * @throws ServiceException
     *             Si no hay factibilidad de recarga, o el Servcio retorna un
     *             mensaje de error
     */
    public FactibilidadRecargaMultitiendaBean realizarRecargaMultitienda(RecargaMultitiendaBean recarga)
            throws DAOException, ServiceException {
        
        FactibilidadRecargaMultitiendaBean factibilidadMultitienda = new FactibilidadRecargaMultitiendaBean();
        
        BillingRecargaServicePortType port = null;
        LOGGER.info("Instanciando el port " + BillingRecargaServicePortType.class);
        try {
            port = (BillingRecargaServicePortType) WebServiceLocator.getInstance().getPort(
                    BillingRecargaService.class, BillingRecargaServicePortType.class);
        } catch (WebServiceLocatorException e) {
            LOGGER.error("Error al inicializar el Port " + BillingRecargaServicePortType.class, e);
            LOGGER.error( new DAOException(e));
        }
        
        Date fechaActual = new Date();        
        FactibilidadRecargaMultitiendaType fact = new FactibilidadRecargaMultitiendaType();
        fact.setCodEmpresa(MiEntelProperties.getProperty("recarga.multitienda.efectuar.codEmpresa"));                        
        fact.setCodTransMultitienda(MiEntelProperties.getProperty("recarga.multitienda.efectuar.codTransMultimedia"));
        fact.setCodLocal(MiEntelProperties.getProperty("recarga.multitienda.efectuar.codLocal"));
        fact.setCodigoUnicoAutorizacion(MiEntelProperties.getProperty("recarga.multitienda.efectuar.codUnicoAutorizacion"));
        fact.setDiaPago(MiEntelProperties.getProperty("recarga.multitienda.efectuar.diaPago"));        
        fact.setIdSistema(MiEntelProperties.getProperty("recarga.multitienda.efectuar.idSistema"));
        fact.setMetodo(MiEntelProperties.getProperty("recarga.multitienda.efectuar.metodo"));
        
        fact.setMontoRecarga(String.valueOf(recarga.getMontoRecarga()));
        fact.setMsisdnRecarga(recarga.getNumeroPcs());
        fact.setNumeroCuotas(String.valueOf(recarga.getCuotas()));
        fact.setClaveTarjeta(recarga.getClaveTarjeta());
        fact.setRut(String.valueOf(recarga.getNumeroTarjeta()));//Nro. tarjeta
        
        fact.setFechaSolicitud(DateHelper.format(fechaActual, DateHelper.FORMAT_yyyyMMdd));
        
        fact.setPlataforma(MiEntelProperties.getProperty("recarga.multitienda.efectuar.plaforma"));        
        fact.setTipoOperacion(MiEntelProperties.getProperty("recarga.multitienda.efectuar.tipoOperacion"));
        fact.setCodUnicoTransaccion(MultitiendaHelper.generarCodigoUnicoTransaccion());
        
        ResultadoFactibilidadRecargaMultitiendaType result = port.factibilidadRecargaMultitienda(fact);
        
        String codigoRespuesta = result.getRespuesta().getCodigo();
        String descripcionRespuesta = result.getRespuesta().getDescripcion();

        LOGGER.info("codigoRespuesta " + codigoRespuesta);
        
        if (CODIGO_RESPUESTA_OK.equals(codigoRespuesta)) {
            ResultadoRecargaMultitiendaType resMulti = result.getResultadoFactibilidadRecargaMultitienda(); 
            factibilidadMultitienda.setCodEmpresa(resMulti.getCodEmpresa());
            factibilidadMultitienda.setCodigoUnicoAutorizacion(resMulti.getCodigoUnicoAutorizacion());
            factibilidadMultitienda.setCodLocal(resMulti.getCodLocal());
            factibilidadMultitienda.setCodRespuestaTransA(resMulti.getCodRespuestaTransA());
            factibilidadMultitienda.setCodRespuestaTransB(resMulti.getCodRespuestaTransB());
            factibilidadMultitienda.setCodTransMultitienda(resMulti.getCodTransMultitienda());
            factibilidadMultitienda.setCodUnicoTransaccion(resMulti.getCodUnicoTransaccion());
            factibilidadMultitienda.setMontoBono(resMulti.getMontoBono());
            factibilidadMultitienda.setMontoRecarga(Integer.parseInt(resMulti.getMontoRecarga()));
            factibilidadMultitienda.setNuevoSaldo(resMulti.getNuevoSaldo());
            factibilidadMultitienda.setNumeroPcs(resMulti.getMsisdnRecarga());
            factibilidadMultitienda.setPlataforma(resMulti.getPlataforma());
            factibilidadMultitienda.setTipoOperacion(resMulti.getTipoOperacion());
            factibilidadMultitienda.setValidezRecarga(
                    MultitiendaHelper.getPeriodoValidezRecargaMultitienda(
                            fechaActual, 
                            Integer.parseInt(resMulti.getValidezRecarga())
                    )
            );            
        }
        else {
            LOGGER.error("recargaMultitienda: Service error code received: "
                    + codigoRespuesta + " - " + descripcionRespuesta);
            LOGGER.error( new ServiceException(codigoRespuesta, descripcionRespuesta));
        }
        
        return factibilidadMultitienda;
        
    }

    /**
     * Entrega la recarga de multitienda asociada a la orden de compra indicada
     * en <code>ordenCompra</code>
     * 
     * @param ordenCompra
     *            String orden de compra de la recarga solicitada
     * @return {@link RecargaMultitiendaBean} recarga solicitada
     * @throws DAOException
     *             ante errores en la ejecucion del metodo
     * @throws ServiceException
     *             Si no hay factibilidad de recarga, o el Servcio retorna un
     *             mensaje de error
     */
    public RecargaMultitiendaBean consultarRecargaMultitienda(String ordenCompra)
            throws DAOException, ServiceException {
        return null;
    }

    // ---------------------------------------------------- ENTEL TICKET

    /**
     * Valida la factibilidad de recarga, de ser positivo, la solicitud es
     * ingresada a la cola de transacciones de recargas.<br>
     * Si es negativo, este metodo lanzara una ServiceException, con el codigo
     * de error y descripcion del motivo por el que no hay factibilidad
     * 
     * @param recarga
     *            {@link FactibilidadRecargaEntelTicketBean} Recarga solicitada
     * @throws DAOException
     *             ante errores en la ejecucion del metodo
     * @throws ServiceException
     *             Si no hay factibilidad de recarga, o el Servcio retorna un
     *             mensaje de error
     */
    public FactibilidadRecargaEntelTicketBean factibilidadRecargaEntelTicket(
            RecargaEntelTicketBean recarga) throws DAOException,
            ServiceException {
        
        FactibilidadRecargaEntelTicketBean factibilidadRecarga = new FactibilidadRecargaEntelTicketBean();
        
        BillingRecargaServicePortType port = null;
        LOGGER.info("Instanciando el port " + BillingRecargaServicePortType.class);
        try {
            port = (BillingRecargaServicePortType) WebServiceLocator.getInstance().getPort(
                    BillingRecargaService.class, BillingRecargaServicePortType.class);
        } catch (WebServiceLocatorException e) {
            LOGGER.error("Error al inicializar el Port " + BillingRecargaServicePortType.class, e);
            LOGGER.error( new DAOException(e));
        }
        
        FactibilidadRecargaTicketType request;
        ResultadoFactibilidadRecargaTicketType result = null;
        
        try {

            LOGGER.info("Configurando datos de la peticion");
            request = RecargaTypeFactory.buildFactibilidadEntelTicketType();
            request.setNumeroEntelTicket(recarga.getNumeroSecretoEnteTicket());
            //request.setMsisdnRecarga(WordNumberHelper.getPrefijoAmpliacion() + recarga.getNumeroPcs());
            request.setMsisdnRecarga(recarga.getNumeroPcs());
            request.setCodUnicoTransaccion(EntelTicketHelper
                    .generarCodigoUnicoTransaccion());

            LOGGER.info("Invocando servicio");
            result = port.factibilidadRecargaTicket(request);
  
        } catch (Exception e) {
            LOGGER.error("Exception caught in service invocation " +
                    "factibilidadRecargaTicket", e);
            LOGGER.error( new DAOException(e));
        }


        String codigoRespuesta = result.getRespuesta().getCodigo();
        String descripcionRespuesta = result.getRespuesta().getDescripcion();

        LOGGER.info("codigoRespuesta " + codigoRespuesta);
        
        if (CODIGO_RESPUESTA_OK.equals(codigoRespuesta)) {

            try {

                ResultadoRecargaTicketType response = result
                        .getResultadoFactibilidadRecargaTicket();

                factibilidadRecarga.setAgenciaTicket(response
                        .getAgenciaTicket());

                factibilidadRecarga.setCodEstadoRespuesta(response
                        .getCodEstadoRespuesta());

                factibilidadRecarga.setCodRespuestaExitoAdicional(response
                        .getCodRespuestaExitoAdicional());

                factibilidadRecarga.setCodUnicoTransaccion(response
                        .getCodUnicoTransaccion());

                factibilidadRecarga.setFolioTicket(response.getFolioTicket());

                factibilidadRecarga.setMontoDispTicket(Double
                        .parseDouble(response.getMontoDispTicket()));

                factibilidadRecarga.setMontoInicialTicket(response
                        .getMontoInicialTicket());

                factibilidadRecarga.setNumeroEntelTicket(response
                        .getNumeroEntelTicket());

                factibilidadRecarga.setNumeroPcs(response.getMsisdnRecarga());

                factibilidadRecarga.setPlataforma(response.getPlataforma());

                factibilidadRecarga.setTipoOperacion(response
                        .getTipoOperacion());

            } catch (Exception e) {
                LOGGER.error("Exception caught in service response: " + 
                        "factibilidadRecargaTicket", e);
                LOGGER.error( new DAOException(e));
            }
        }
        else{
            LOGGER.error("recargaMultitienda: Service error code received: "
                    + codigoRespuesta + " - " + descripcionRespuesta);
            LOGGER.error( new ServiceException(codigoRespuesta, descripcionRespuesta));
        }

        return factibilidadRecarga;
    }

    /**
     * Hace efectiva una recarga de Entel ticket
     * 
     * @param recarga
     *            {@link RecargaEntelTicketBean} recarga a realizar
     * @throws DAOException
     *             ante errores en la ejecucion del metodo
     * @throws ServiceException
     *             Si no hay factibilidad de recarga, o el Servcio retorna un
     *             mensaje de error
     */
    public FactibilidadRecargaEntelTicketBean realizarRecargaEntelTicket(
            FactibilidadRecargaEntelTicketBean recarga) throws DAOException,
            ServiceException {

        FactibilidadRecargaEntelTicketBean factibilidadRecargaEntelTicket = new FactibilidadRecargaEntelTicketBean();

        BillingRecargaServicePortType port = null;
        LOGGER.info("Instanciando el port "
                + BillingRecargaServicePortType.class);
        try {
            port = (BillingRecargaServicePortType) WebServiceLocator
                    .getInstance().getPort(BillingRecargaService.class,
                            BillingRecargaServicePortType.class);
        } catch (WebServiceLocatorException e) {
            LOGGER.error("Error al inicializar el Port "
                    + BillingRecargaServicePortType.class, e);
            LOGGER.error( new DAOException(e));
        }

        FactibilidadRecargaTicketType request;
        ResultadoFactibilidadRecargaTicketType result = null;
        
        try {

            LOGGER.info("Configurando datos de la peticion");
            request = RecargaTypeFactory.buildRealizarEntelTicketType();
            request
                    .setMontoTicket(String
                            .valueOf(recarga.getMontoDispTicket()));
            
            request.setMontoRecarga(String
                    .valueOf(recarga.getMontoDispTicket()));
            
            request.setNumeroEntelTicket(recarga.getNumeroEntelTicket());
            //request.setMsisdnRecarga(WordNumberHelper.getPrefijoEntel() + recarga.getNumeroPcs());
            request.setMsisdnRecarga(recarga.getNumeroPcs());
            request.setCodUnicoTransaccion(EntelTicketHelper
                    .generarCodigoUnicoTransaccion());
            
            request.setAgenciaTicket(recarga.getAgenciaTicket());
            request.setFolioTicket(recarga.getFolioTicket());

            LOGGER.info("Invocando servicio");
            result = port.factibilidadRecargaTicket(request);

        } catch (Exception e) {
            LOGGER.error("Exception caught in service invocation ", e);
            LOGGER.error( new DAOException(e));
        }        
        
        String codigoRespuesta = result.getRespuesta().getCodigo();
        String descripcionRespuesta = result.getRespuesta().getDescripcion();

        LOGGER.info("codigoRespuesta " + codigoRespuesta);
        
        if (CODIGO_RESPUESTA_OK.equals(codigoRespuesta)) {
            
            try {

                ResultadoRecargaTicketType response = result
                        .getResultadoFactibilidadRecargaTicket();
                
                factibilidadRecargaEntelTicket.setAgenciaTicket(response
                        .getAgenciaTicket());
                
                factibilidadRecargaEntelTicket.setCodEstadoRespuesta(response
                        .getCodEstadoRespuesta());
                
                factibilidadRecargaEntelTicket
                        .setCodRespuestaExitoAdicional(response
                                .getCodRespuestaExitoAdicional());
                
                factibilidadRecargaEntelTicket.setCodUnicoTransaccion(response
                        .getCodUnicoTransaccion());
                
                factibilidadRecargaEntelTicket.setFolioTicket(response
                        .getFolioTicket());
                
                factibilidadRecargaEntelTicket.setMontoDispTicket(Double
                        .parseDouble(response.getMontoDispTicket()));
                
                factibilidadRecargaEntelTicket.setMontoInicialTicket(response
                        .getMontoInicialTicket());
                
                factibilidadRecargaEntelTicket.setNumeroEntelTicket(response
                        .getNumeroEntelTicket());
                
                factibilidadRecargaEntelTicket.setNumeroPcs(response
                        .getMsisdnRecarga());
                
                factibilidadRecargaEntelTicket.setPlataforma(response
                        .getPlataforma());
                
                factibilidadRecargaEntelTicket.setTipoOperacion(response
                        .getTipoOperacion());
                
                String validezRecarga = EntelTicketHelper
                        .getVigenciaMontoEntelTicket(response
                                .getMontoDispTicket());
                factibilidadRecargaEntelTicket
                        .setValidezRecarga(validezRecarga);

            } catch (Exception e) {
                LOGGER.error("Exception caught in service response ", e);
                LOGGER.error( new DAOException(e));
            }
        }
        else{
            LOGGER.error("factibilidadRecargaTicket: Service error code received: "
                    + codigoRespuesta + " - " + descripcionRespuesta);
            LOGGER.error( new ServiceException(codigoRespuesta, descripcionRespuesta));
        }   
        
        return factibilidadRecargaEntelTicket;
        
    }

    /**
     * Entrega la recarga de entelticket asociada a la orden de compra
     * <code>ordenCompra</code>
     * 
     * @param ordenCompra
     *            String orden de compra de la recarga
     * @return {@link RecargaEntelTicketBean} recarga solicitada
     * @throws DAOException
     *             ante errores en la ejecucion del metodo
     * @throws ServiceException
     *             Si no hay factibilidad de recarga, o el Servcio retorna un
     *             mensaje de error
     */
    public RecargaEntelTicketBean consultarRecargaEntelTicket(String ordenCompra)
            throws DAOException, ServiceException {
        return null;
    }
    
    /**
     * Entrega el detalle del Historial de Recargas
     * @param msisdn
     * @return
     * @throws DAOException
     * @throws ServiceException
     */
    public RecargaHistoricoBean getHistoricoRecargas(String msisdn) throws DAOException, ServiceException{
        
        String fechaInicial = DateHelper.format(DateHelper.fechaRestaMes(new java.util.Date(),-2),
                DateHelper.FORMAT_yyyyMMddhhmmss);

        String fechaFinal = DateHelper.format(new java.util.Date(),
                DateHelper.FORMAT_yyyyMMddhhmmss);
        
        return getHistoricoRecargasRango(msisdn,fechaInicial,fechaFinal);
        
    }
 
    
    /**
     * Entrega el detalle del Historial de Recargas segun rango de fechas
     * @param msisdn
     * @param fechaInicial
     * @param fechaFinal     
     * @return
     * @throws DAOException
     * @throws ServiceException
     */
    public RecargaHistoricoBean getHistoricoRecargasRango(String msisdn,String fechaInicial,String fechaFinal)
    		throws DAOException, ServiceException{
        
        RecargaHistoricoBean recargaHistoricoBean = new RecargaHistoricoBean();
        ArrayList<DetalleRecargasBean> listDetalleRecargas = new ArrayList<DetalleRecargasBean>();
        DetalleRecargasBean detalleRecargasBean;
        
        BillingRecargaServicePortType port = null;
        LOGGER.info("Instanciando el port");
        try {
            port = (BillingRecargaServicePortType)WebServiceLocator
            .getInstance().getPort(BillingRecargaService.class, BillingRecargaServicePortType.class);
            
        }catch(WebServiceLocatorException e){
            LOGGER.error("Error al inicializar el Port "
                    + BillingRecargaServicePortType.class, e);
            LOGGER.error( new DAOException(e));
            
        }

        ConsultarHistoricoRecargasType request = new ConsultarHistoricoRecargasType();
        request.setMsisdn(msisdn);
        request.setFechaInicial(fechaInicial);
        request.setFechaFinal(fechaFinal);
        
        LOGGER.info("Invocando servicio");
        
        ResultadoConsultarHistoricoRecargasType response = null;
        
        try{
            response = port.consultarHistoricoRecargas(request);

        } catch (Exception e) {
            LOGGER.error("Exception caught on Service invocation", e);
            LOGGER.error( new DAOException(e));
        }
        
        String codigoRespuesta = response.getRespuesta().getCodigo();
        String descripcionRespuesta = response.getRespuesta().getDescripcion();
    
        LOGGER.info("codigoRespuesta " + codigoRespuesta);
        
        if (CODIGO_RESPUESTA_OK.equals(response.getRespuesta()
                .getCodigo())) {
        
            HistoricoRecargasType historicoRecargas = 
                response.getResultadoConsultarHistoricoRecargas();
            
            recargaHistoricoBean.setFechaInicial(DateHelper.parseDate(historicoRecargas.getFechaInicial(), DateHelper.FORMAT_ddMMyyyy_HHmmss_HYPHEN));
            recargaHistoricoBean.setFechaFinal(DateHelper.parseDate(historicoRecargas.getFechaFinal(), DateHelper.FORMAT_ddMMyyyy_HHmmss_HYPHEN));
            recargaHistoricoBean.setMontoTotal(Double.parseDouble(historicoRecargas.getMontoTotal()));
            recargaHistoricoBean.setMsisdn(historicoRecargas.getMsisdn());
            recargaHistoricoBean.setNumeroConsultas(Integer.parseInt(historicoRecargas.getNumeroConsultas()));
            recargaHistoricoBean.setTiempo(historicoRecargas.getTiempo());
            
            
            List<DetalleRecargasType> detalleRecargas = historicoRecargas.getDetalleRecargas();
            
            String plataformaRecarga = "";
            String tipoRecarga = "";
            
            try {
            
            for(DetalleRecargasType detalleRecargasType : detalleRecargas){

                try{
                	plataformaRecarga = 
                		(detalleRecargasType.getPlataformaRecarga()).equals("") || 
                		(detalleRecargasType.getPlataformaRecarga()).equals("null") ? "" : 
                			MiEntelProperties.getProperty("recarga.historico.plataforma."+detalleRecargasType.getPlataformaRecarga());
                
                }catch(Exception e){
                	plataformaRecarga = MiEntelProperties.getProperty("recarga.historico.sindatos");
                	LOGGER.info("Exception al intentar obtener la plataforma de recarga : plataforma : "+detalleRecargasType.getPlataformaRecarga());
                   
                }
                
                try{
                	tipoRecarga = 
                    (detalleRecargasType.getTipoRecarga()).equals("") || 
                    (detalleRecargasType.getTipoRecarga()).equals("null") ? "" : 
                        " - "+MiEntelProperties.getProperty("recarga.historico.tipo."+detalleRecargasType.getTipoRecarga());
                }catch (Exception e) {
                	 LOGGER.warn("Exception al intentar obtener el tipo de recarga '"+detalleRecargasType.getTipoRecarga()+"'");
				}
                
                detalleRecargasBean = new DetalleRecargasBean();
                detalleRecargasBean.setCodigoPriRecarga(detalleRecargasType.getCodigoPriRecarga());
                detalleRecargasBean.setComercioRecarga(detalleRecargasType.getComercioRecarga());
                detalleRecargasBean.setComunaRecarga(detalleRecargasType.getComunaRecarga());
                detalleRecargasBean.setDistribuidorRecarga(detalleRecargasType.getDistribuidorRecarga());
                detalleRecargasBean.setFechaRecarga(DateHelper.parseDate(detalleRecargasType.getFechaRecarga(),DateHelper.FORMAT_yyyyMMdd_HHmmss_HYPHEN));
                detalleRecargasBean.setLocalRecarga(detalleRecargasType.getLocalRecarga());
                detalleRecargasBean.setMontoRecarga(Double.parseDouble(detalleRecargasType.getMontoRecarga()));
                detalleRecargasBean.setMsisdnRecarga(detalleRecargasType.getMsisdnRecarga());
                detalleRecargasBean.setPlataformaRecarga(plataformaRecarga);
                detalleRecargasBean.setRegionRecarga(detalleRecargasType.getRegionRecarga());
                detalleRecargasBean.setSubTipoRecarga(detalleRecargasType.getSubTipoRecarga());
                detalleRecargasBean.setTerminalRecarga(detalleRecargasType.getTerminalRecarga());
                detalleRecargasBean.setTipoRecarga(tipoRecarga);
                
                listDetalleRecargas.add(detalleRecargasBean);

            }
            
            } catch (Exception e) {
                LOGGER.error(
                        "Exception inesperada al recuperar historico de recargas", e);
                LOGGER.error( new DAOException(e));
            }
            
            recargaHistoricoBean.setDetalleRecargas(listDetalleRecargas);

            
        }else{
            LOGGER
            .error("Respuesta del Servicio :"+codigoRespuesta+ "Descripcion:"+descripcionRespuesta);
            LOGGER.error( new ServiceException(codigoRespuesta,descripcionRespuesta));
        }
        
        return recargaHistoricoBean;
        
    }

}