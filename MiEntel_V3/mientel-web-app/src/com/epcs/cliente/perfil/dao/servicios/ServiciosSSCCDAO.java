/* Propiedad de Entel Pcs. Todos los derechos reservados */
package com.epcs.cliente.perfil.dao.servicios;

import java.util.Date;

import org.apache.log4j.Logger;

import com.epcs.bean.ResultadoServicioBean;
import com.epcs.bean.ResultadoServiciosSSCCBean;
import com.epcs.cliente.orden.ClienteOrdenService;
import com.epcs.cliente.orden.ClienteOrdenService_Service;
import com.epcs.cliente.orden.types.AdministrarServicioDatosSSCCType;
import com.epcs.cliente.orden.types.AdministrarServiciosOrqSSCCType;
import com.epcs.cliente.orden.types.AdministrarServiciosOrqType;
import com.epcs.cliente.orden.types.ResultadoAdministrarServicioDatosSSCCType;
import com.epcs.cliente.orden.types.ResultadoAdministrarServiciosOrqSSCCType;
import com.epcs.cliente.perfil.dao.AdministracionServiciosDAO;
import com.epcs.cliente.perfil.dao.util.ServiciosPropertiesHelper;
import com.epcs.recursoti.configuracion.DateHelper;
import com.epcs.recursoti.configuracion.MiEntelProperties;
import com.epcs.recursoti.configuracion.Utils;
import com.epcs.recursoti.configuracion.locator.WebServiceLocator;
import com.epcs.recursoti.configuracion.locator.WebServiceLocatorException;
import com.epcs.recursoti.excepcion.DAOException;
import com.epcs.recursoti.excepcion.ServiceException;

/**
 * Clase con metodos de administracion de servicios comunes para mercados SS y
 * CC.<br>
 * Los servicios que maneja esta clase son:<br>
 * 
 * <ul>
 * <li>Numero Interarea</li>
 * <li>Numero Roaming</li>
 * <li>Larga Distancia Internacional</li>
 * <li>Numero 700</li>
 * <li>Numero 300</li>
 * <li>Numero 606</li>
 * <li>Numero 609</li>
 * <li>WAP</li>
 * </ul>
 * Esta clase no esta pensada para ser usada directamente, sino que como
 * Composicion de {@link AdministracionServiciosDAO}
 * 
 * @author jlopez (I2B) en nombre de Absalon Opazo (Atencion al Cliente,
 *         EntelPcs)
 * 
 */
public class ServiciosSSCCDAO {

    
    private static final Logger LOGGER = Logger
            .getLogger(ServiciosSSCCDAO.class);

    /**
     * Valor de exito para la respuesta a un servicio
     */
    public static final String CODIGO_RESPUESTA_OK = MiEntelProperties
            .getProperty("servicios.codigoRespuesta.exito");
    
    /**
     * 
     */
    public ServiciosSSCCDAO() {
    }

    

    /**
     * Metodo 'common' para la activacion de servicios
     * 
     * @param numeroPcs
     * @param servicio
     * @return
     * @throws DAOException
     * @throws ServiceException
     */
    public ResultadoServiciosSSCCBean activarServicio(String numeroPcs,
            String servicio) throws DAOException, ServiceException {

        String idTipoServicio = ServiciosPropertiesHelper
                .getIdTipoServicio(servicio);
        String idServicio = ServiciosPropertiesHelper.getIdServicio(servicio);
        String descripcionServicio = ServiciosPropertiesHelper
                .getDescripcionServicio(servicio);
        String accion = ServiciosPropertiesHelper.getActivar(servicio);
        String accionTransferencia = ServiciosPropertiesHelper
                .getActivarTransferencia(servicio);

        Date fechaActual = new Date();

        return this.administrarServicios(numeroPcs, idTipoServicio, idServicio,
                descripcionServicio, accion, accionTransferencia, fechaActual);
    }

    /**
     * Metodo 'common' para la desactivacion de servicios
     * 
     * @param numeroPcs
     * @param servicio
     * @return
     * @throws DAOException
     * @throws ServiceException
     */
    public ResultadoServiciosSSCCBean desactivarServicio(String numeroPcs,
            String servicio) throws DAOException, ServiceException {

        String idTipoServicio = ServiciosPropertiesHelper
                .getIdTipoServicio(servicio);
        String idServicio = ServiciosPropertiesHelper.getIdServicio(servicio);
        String descripcionServicio = ServiciosPropertiesHelper
                .getDescripcionServicio(servicio);
        String accion = ServiciosPropertiesHelper.getDesactivar(servicio);
        String accionTransferencia = ServiciosPropertiesHelper
                .getDesactivarTransferencia(servicio);

        Date fechaActual = new Date();

        return this.administrarServicios(numeroPcs, idTipoServicio, idServicio,
                descripcionServicio, accion, accionTransferencia, fechaActual);
    }
    
    /**
     * Metodo 'common' para la activacion de servicios de datos
     * 
     * @param numeroPcs
     * @param servicio
     * @return
     * @throws DAOException
     * @throws ServiceException
     */
    public ResultadoServiciosSSCCBean activarServicioDatos(String numeroPcs,
            String servicio) throws DAOException, ServiceException {

        String idServicio = ServiciosPropertiesHelper.getIdServicio(servicio);
        String accion = ServiciosPropertiesHelper.getActivar(servicio);
        
        return this.administrarServiciosDatos(numeroPcs, idServicio, accion);
    }

    /**
     * Metodo 'common' para la desactivacion de servicios de datos
     * 
     * @param numeroPcs
     * @param servicio
     * @return
     * @throws DAOException
     * @throws ServiceException
     */
    public ResultadoServiciosSSCCBean desactivarServicioDatos(String numeroPcs,
            String servicio) throws DAOException, ServiceException {

        String idServicio = ServiciosPropertiesHelper.getIdServicio(servicio);
        String accion = ServiciosPropertiesHelper.getDesactivar(servicio);

        return this.administrarServiciosDatos(numeroPcs, idServicio, accion);
    }

    /**
     * Metodo para activacion/desactivacion de los servicios:<br>
     * <ul>
     * <li>Numero Interarea</li>
     * <li>Numero Roaming</li>
     * <li>Larga Distancia Internacional</li>
     * <li>Numero 700</li>
     * <li>Numero 300</li>
     * <li>Numero 606</li>
     * <li>Numero 609</li>
     * <li>WAP</li>
     * </ul>
     * <br>
     * Este metodo realiza la activacion/desactivacion del servicio junto con su
     * correspondiente accion de Transferencia, por esta razon, recibe ambas
     * acciones.<br>
     * No se recomienda usar este metodo directamente, mas bien, emplear los
     * metodos de activar/desactivar especificos para cada servicio, puesto
     * estos resuelven los valores para los parametros <code>accion</code>,
     * <code>accionTransferencia</code>, <code>idTipoServicio</code> e
     * <code>idServicio</code>
     * 
     * @see #activarServicio(String, String)
     * @see #desactivarServicio(String, String)
     * 
     * @param numeroPcs
     *            String msisdn para quien se realiza la accion
     * @param idTipoServicio
     *            String identificador de tipo de servicio
     * @param idServicio
     *            String identificador del servicio dentro de su tipo
     * @param descripcionServicio
     *            String nombre descripcion servicio (transferencia)
     * @param accion
     *            String con nombre de la accion (varia segun cada servicio)
     * @param accionTransferencia
     *            nombre de la accion para transferencia
     * @param fechaActual
     *            {@link Date} fecha con que debe registrarse la accion sobre el
     *            servicio
     * @return {@link ResultadoServicioBean} Con el correlativo de
     *         transferencia, flag de cambio pendiente y msisdn a quien se le
     *         realizo la accio
     * 
     * @throws DAOException
     * @throws ServiceException
     */
    public ResultadoServiciosSSCCBean administrarServicios(String numeroPcs,
            String idTipoServicio, String idServicio,
            String descripcionServicio, String accion,
            String accionTransferencia, Date fechaActual) throws DAOException,
            ServiceException {

        ClienteOrdenService port = null;
        LOGGER.info("Instanciando el port " + ClienteOrdenService.class);
        try {
            port = (ClienteOrdenService) WebServiceLocator.getInstance()
                    .getPort(ClienteOrdenService_Service.class,
                            ClienteOrdenService.class);
        } catch (WebServiceLocatorException e) {
            LOGGER.error("Error al inicializar el Port "
                    + ClienteOrdenService.class, e);
            LOGGER.error( new DAOException(e));
        }

        AdministrarServiciosOrqSSCCType request = new AdministrarServiciosOrqSSCCType();
        ResultadoAdministrarServiciosOrqSSCCType response = null;

        try {

            LOGGER.info("Configurando Datos de la peticion");
            request.setAccion(accion);
            request.setAccionTransferencia(accionTransferencia);
            request.setDescripcionServicio(descripcionServicio);
            String fechaActualFormatted = DateHelper.format(fechaActual,
                    DateHelper.FORMAT_yyyyMMdd_HYPHEN);
            request.setFechaActual(fechaActualFormatted);
            request.setIdServicio(idServicio);
            request.setIdTipoServicio(idTipoServicio);
            request.setMsisdn(numeroPcs);

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Solicitud de administracion de servicio para "
                        + numeroPcs);
                LOGGER.debug("    msisdn : " + numeroPcs);
                LOGGER.debug("    idTipoServicio : " + idTipoServicio);
                LOGGER.debug("    idServicio : " + idServicio);
                LOGGER
                        .debug("    descripcionServicio : "
                                + descripcionServicio);
                LOGGER.debug("    accion : " + accion);
                LOGGER
                        .debug("    accionTransferencia : "
                                + accionTransferencia);
                LOGGER.debug("    fechaActual : " + fechaActualFormatted);
            }
            LOGGER.info("Invocando servicio");
            response = port.administrarServiciosOrqSSCC(request);

        } catch (Exception e) {
            LOGGER.error("Exception caught on Service invocation: "
                    + "administrarServiciosOrqSSCC", e);
            LOGGER.error( new DAOException(e));
        }

        String codigoRespuesta = response.getRespuesta().getCodigo();
        String descripcionRespuesta = response.getRespuesta().getDescripcion();

        LOGGER.info("codigoRespuesta " + codigoRespuesta);
        LOGGER.info("descripcionRespuesta " + descripcionRespuesta);

        if (Utils.isEmptyString(codigoRespuesta)) {
           LOGGER.error( new DAOException(
                    "administrarServiciosOrqSSCC: Servicio no responde "
                            + "con codigoRespuesta"));
        }

        if (CODIGO_RESPUESTA_OK.equals(codigoRespuesta)) {

            try {

                ResultadoServiciosSSCCBean resultadoServicioBean = new ResultadoServiciosSSCCBean();
                AdministrarServiciosOrqType resultado = response
                        .getAdministrarServiciosSSCC();

                resultadoServicioBean.setCambioPendiente(Boolean
                        .valueOf(resultado.getFlagCambioPendiente()));

                resultadoServicioBean.setCorrelativoTransferencia(resultado
                        .getCorrelativoTransferencia());

                resultadoServicioBean.setNumeroPcs(resultado.getMsisdn());

                if (LOGGER.isDebugEnabled()) {

                    LOGGER.debug("Administracion servicio realizada para "
                            + resultadoServicioBean.getNumeroPcs());
                    LOGGER.debug("    correlativo : "
                            + resultadoServicioBean
                                    .getCorrelativoTransferencia());
                    LOGGER.debug("    cambio pendiente : "
                            + resultadoServicioBean.isCambioPendiente());
                }

                return resultadoServicioBean;

            } catch (Exception e) {
                LOGGER.error("Exception caught on Service response: "
                        + "administrarServiciosOrqSSCC", e);
                LOGGER.error( new DAOException(e));
            }

        }
        else {
            LOGGER
                    .info("administrarServiciosOrqSSCC: Service error code received: "
                            + codigoRespuesta + " - " + descripcionRespuesta);
            LOGGER.error( new ServiceException(codigoRespuesta, descripcionRespuesta));
        }
        return new ResultadoServiciosSSCCBean();
    }
    
    /**
     * Metodo para activacion/desactivacion de los servicios:<br>
     * <ul>
     * <li>Banda Ancha Movil</li>
     * <li>Internet Movil</li>
     * <li>Internet Movil / Banda Ancha Movil</li>
     * </ul>
     * <br>
     * Este metodo realiza la activacion/desactivacion del servicio indicado.<br>
     * No se recomienda usar este metodo directamente, mas bien, emplear los
     * metodos de activar/desactivar especificos para cada servicio, puesto que
     * estos resuelven los valores para los parametros <code>accion</code> y
     * <code>idServicio</code>
     * 
     * @see #activarServicioDatos(String, String)
     * @see #activarServicioDatos(String, String)
     * 
     * @param numeroPcs
     *            String msisdn para quien se realiza la accion
     * @param idServicio
     *            String identificador del servicio
     * @param accion
     *            String con nombre de la accion (varia segun cada servicio)
     * @return {@link ResultadoServicioBean} Con el msisdn a quien se le
     *         realizo la accion
     * 
     * @throws DAOException
     * @throws ServiceException
     */
    public ResultadoServiciosSSCCBean administrarServiciosDatos(String numeroPcs,
            String idServicio, String accion) throws DAOException,
            ServiceException {

        ClienteOrdenService port = null;
        LOGGER.info("Instanciando el port " + ClienteOrdenService.class);
        try {
            port = (ClienteOrdenService) WebServiceLocator.getInstance()
                    .getPort(ClienteOrdenService_Service.class,
                            ClienteOrdenService.class);
        } catch (WebServiceLocatorException e) {
            LOGGER.error("Error al inicializar el Port "
                    + ClienteOrdenService.class, e);
            LOGGER.error( new DAOException(e));
        }

        AdministrarServicioDatosSSCCType request = new AdministrarServicioDatosSSCCType();
        ResultadoAdministrarServicioDatosSSCCType response = null;

        try {

            LOGGER.info("Configurando Datos de la peticion");
            request.setAccion(accion);
            request.setIdServicio(idServicio);
            request.setMsisdn(numeroPcs);

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Solicitud de administracion de servicio para "
                        + numeroPcs);
                LOGGER.debug("    msisdn : " + numeroPcs);
                LOGGER.debug("    idServicio : " + idServicio);
                LOGGER.debug("    accion : " + accion);
            }
            LOGGER.info("Invocando servicio");
            response = port.administrarServicioDatosSSCC(request);

        } catch (Exception e) {
            LOGGER.error("Exception caught on Service invocation: "
                    + "administrarServicioDatosSSCC", e);
            LOGGER.error( new DAOException(e));
        }

        String codigoRespuesta = response.getRespuesta().getCodigo();
        String descripcionRespuesta = response.getRespuesta().getDescripcion();

        LOGGER.info("codigoRespuesta " + codigoRespuesta);
        LOGGER.info("descripcionRespuesta " + descripcionRespuesta);

        if (Utils.isEmptyString(codigoRespuesta)) {
            LOGGER.error( new DAOException(
                    "administrarServicioDatosSSCC: Servicio no responde "
                            + "con codigoRespuesta"));
        }

        if (CODIGO_RESPUESTA_OK.equals(codigoRespuesta)) {

            try {

            	ResultadoServiciosSSCCBean resultadoServicioBean = new ResultadoServiciosSSCCBean();
                resultadoServicioBean.setNumeroPcs(numeroPcs);

                if (LOGGER.isDebugEnabled()) {

                    LOGGER.debug("Administracion servicio realizada para "
                            + resultadoServicioBean.getNumeroPcs());
                }

                return resultadoServicioBean;

            } catch (Exception e) {
                LOGGER.error("Exception caught on Service response: "
                        + "administrarServicioDatosSSCC", e);
                LOGGER.error( new DAOException(e));
            }

        }
        else {
            LOGGER.info("administrarServicioDatosSSCC: Service error code received: "
                            + codigoRespuesta + " - " + descripcionRespuesta);
            LOGGER.error( new ServiceException(codigoRespuesta, descripcionRespuesta));
        }
        return new ResultadoServiciosSSCCBean();
    }

    
}
