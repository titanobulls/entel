/* Propiedad de Entel PCS. Todos los derechos reservados */
package com.epcs.billing.pago.dao;

import org.apache.log4j.Logger;

import com.epcs.bean.EstadoPagoAutomaticoBean;
import com.epcs.bean.EstadoPromoPagoAutomaticoBean;
import com.epcs.bean.PagoAutomaticoBean;
import com.epcs.billing.pago.BillingPagoService;
import com.epcs.billing.pago.BillingPagoService_Service;
import com.epcs.billing.pago.types.ConsultarEstadoPATType;
import com.epcs.billing.pago.types.ConsultarEstadoPromoPATPACType;
import com.epcs.billing.pago.types.InscribirPATType;
import com.epcs.billing.pago.types.ResultadoConsultarEstadoPATType;
import com.epcs.billing.pago.types.ResultadoConsultarEstadoPromoPATPACType;
import com.epcs.billing.pago.types.ResultadoInscribirPATType;
import com.epcs.recursoti.configuracion.DateHelper;
import com.epcs.recursoti.configuracion.MiEntelProperties;
import com.epcs.recursoti.configuracion.locator.WebServiceLocator;
import com.epcs.recursoti.configuracion.locator.WebServiceLocatorException;
import com.epcs.recursoti.excepcion.DAOException;
import com.epcs.recursoti.excepcion.ServiceException;

/**
 * @author jmanzur (I2B) en nombre de Absalon Opazo (Atencion al Cliente,
 *         EntelPcs)
 * 
 */
public class PagoAutomaticoDAO {

    private static final Logger LOGGER = Logger
            .getLogger(PagoAutomaticoDAO.class);
    
    public static final String CODIGO_RESPUESTA_OK = MiEntelProperties
            .getProperty("servicios.codigoRespuesta.exito");

    public static final String STD_PAT_PROCESO = MiEntelProperties
            .getProperty("pagoAutomatico.stdpatproceso");

    
    
    /**
     * Consultar el estado de pago automatico
     * 
     * @param msisdn
     * @param numeroCuenta
     * @return null si no esta inscrito
     * @throws DAOException
     * @throws ServiceException
     */
    public EstadoPagoAutomaticoBean consultarEstadoPAT(String msisdn,
            String numeroCuenta) throws DAOException, ServiceException {

        BillingPagoService port = null;
        EstadoPagoAutomaticoBean estadoPagoBean = null;

        LOGGER.info("Instanciando el port");
        try {
            port = (BillingPagoService) WebServiceLocator.getInstance()
                    .getPort(BillingPagoService_Service.class,
                            BillingPagoService.class);
        } catch (WebServiceLocatorException e) {
            LOGGER.error("Error al inicializar el Port "
                    + BillingPagoService.class, e);
            LOGGER.error( new DAOException(e));
        }


        ResultadoConsultarEstadoPATType response = null;
        ConsultarEstadoPATType request = new ConsultarEstadoPATType();

        try {

            LOGGER.info("Configurando Datos de la peticion");
            request.setMsisdn(msisdn);
            request.setNumeroCuenta(numeroCuenta);
            
            LOGGER.info("Invocando servicio");
            response = port.consultarEstadoPAT(request);

        } catch (Exception e) {
            LOGGER.error("Exception caught on Service invocation", e);
            LOGGER.error( new DAOException(e));
        }

        String codigoRespuesta = response.getRespuesta().getCodigo();
        String descripcionRespuesta = response.getRespuesta().getDescripcion();

        LOGGER.info("codigoRespuesta  " + codigoRespuesta);
        LOGGER.info("descripcionRespuesta " + descripcionRespuesta);

        if (CODIGO_RESPUESTA_OK.equals(codigoRespuesta)) {

            try {

                estadoPagoBean = new EstadoPagoAutomaticoBean();
                estadoPagoBean.setEstado(response.getResultadoConsultaEstadoPAT()
                        .getEstadoPAT());
                estadoPagoBean.setFechaSuscripcion(DateHelper.parseDate(response
                        .getResultadoConsultaEstadoPAT().getFechaSuscripcion(),
                        DateHelper.FORMAT_ddMMyyyy_SLASH));

            } catch (Exception e) {
                LOGGER.error("Exception caught on Service response", e);
                LOGGER.error( new DAOException(e));            
            }

        } else {

            LOGGER.error("Service error code received: " + codigoRespuesta
                    + " - " + descripcionRespuesta);
            LOGGER.error( new ServiceException(codigoRespuesta, descripcionRespuesta));

        }

        return estadoPagoBean;
    }

    /**
     * 
     * @param rut
     * @param numeroCuenta
     * @return null si no esta inscrito
     * @throws DAOException
     * @throws ServiceException
     */
    public EstadoPromoPagoAutomaticoBean consultarEstadoPromoPAT(String rut,
            String numeroCuenta) throws DAOException, ServiceException {

        BillingPagoService port = null;
        EstadoPromoPagoAutomaticoBean estadoPromo = null;

        LOGGER.info("Instanciando el port");
        try {
            port = (BillingPagoService) WebServiceLocator.getInstance()
                    .getPort(BillingPagoService_Service.class,
                            BillingPagoService.class);
        } catch (WebServiceLocatorException e) {
            LOGGER.error("Error al inicializar el Port "
                    + BillingPagoService.class, e);
            LOGGER.error( new DAOException(e));
        }

        ResultadoConsultarEstadoPromoPATPACType response = null;
        ConsultarEstadoPromoPATPACType request = new ConsultarEstadoPromoPATPACType();

        try {

            LOGGER.info("Configurando Datos de la peticion");
            request.setRut(rut);
            request.setNumeroCuenta(numeroCuenta);

            LOGGER.info("Invocando servicio");
            response = port.consultarEstadoPromoPATPAC(request);

        } catch (Exception e) {
            LOGGER.error("Exception caught on Service invocation", e);
            LOGGER.error( new DAOException(e));
        }

        String codigoRespuesta = response.getRespuesta().getCodigo();

        String descripcionRespuesta = response.getRespuesta().getDescripcion();

        LOGGER.info("codigoRespuesta  " + codigoRespuesta);
        LOGGER.info("descripcionRespuesta " + descripcionRespuesta);

        if (CODIGO_RESPUESTA_OK.equals(codigoRespuesta)) {

            try {

                estadoPromo = new EstadoPromoPagoAutomaticoBean();
                estadoPromo.setEstadoPromocion(response
                        .getResultadoConsultaEstadoPromoPATPAC()
                        .getEstadoPromocion());
                estadoPromo.setNumeroCuenta(response
                        .getResultadoConsultaEstadoPromoPATPAC().getNumeroCuenta());
                estadoPromo.setRutConGuion(response
                        .getResultadoConsultaEstadoPromoPATPAC().getRutConGuion());

            } catch (Exception e) {
                LOGGER.error("Exception caught on Service response", e);
                LOGGER.error( new DAOException(e));
            }

        } else {
            LOGGER.error("Service error code received: " + codigoRespuesta
                    + " - " + descripcionRespuesta);
            LOGGER.error( new ServiceException(codigoRespuesta, descripcionRespuesta));
        }

        return estadoPromo;
    }

    /**
     * Realiza el registro de la sol de pago automatico
     * @param pagoAutomaticoBean
     * @param msisdn
     * @param numeroCuenta
     * @throws DAOException
     * @throws ServiceException
     */
    public void inscribirPAT(PagoAutomaticoBean pagoAutomaticoBean,
            String msisdn, String numeroCuenta) throws DAOException, ServiceException {

        BillingPagoService port = null;

        LOGGER.info("Instanciando el port");
        try {
            port = (BillingPagoService) WebServiceLocator.getInstance()
                    .getPort(BillingPagoService_Service.class,
                            BillingPagoService.class);
        } catch (WebServiceLocatorException e) {
            LOGGER.error("Error al inicializar el Port "
                    + BillingPagoService.class, e);
            LOGGER.error( new DAOException(e));
        }

        ResultadoInscribirPATType response = null;
        InscribirPATType request = new InscribirPATType();

        try {

            LOGGER.info("Configurando Datos de la peticion");
            request.setMsisdn(msisdn);
            request.setNumeroCuenta(numeroCuenta);
            request.setEstadoPAT(STD_PAT_PROCESO);
            request.setRut(pagoAutomaticoBean.getRut());
            request.setEmail(pagoAutomaticoBean.getEmail());
            request.setTelefono(pagoAutomaticoBean.getTelefonoArea().concat(pagoAutomaticoBean.getTelefono()));
            request.setNombreTitularCompleto(pagoAutomaticoBean.getTitular());
            request.setNumeroTarjeta(pagoAutomaticoBean.getNumeroTarjeta());
            request.setTipoTarjeta(pagoAutomaticoBean.getTipoTarjeta());

            LOGGER.info("Invocando servicio");
            response = port.inscribirPAT(request);

        } catch (Exception e) {
            LOGGER.error("Exception caught on Service invocation", e);
            LOGGER.error( new DAOException(e));
        }

        String codigoRespuesta = response.getRespuesta().getCodigo();
        String descripcionRespuesta = response.getRespuesta().getDescripcion();

        LOGGER.info("codigoRespuesta  " + codigoRespuesta);
        LOGGER.info("descripcionRespuesta " + descripcionRespuesta);

        if (CODIGO_RESPUESTA_OK.equals(codigoRespuesta)) {

            LOGGER.info("Pat Inscrito ");

        } else {

            LOGGER.error("Service error code received: " + codigoRespuesta
                    + " - " + descripcionRespuesta);
            LOGGER.error( new ServiceException(codigoRespuesta, descripcionRespuesta));

        }

    }

}
