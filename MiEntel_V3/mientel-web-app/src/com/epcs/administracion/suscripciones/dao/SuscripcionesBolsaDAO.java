/* Propiedad de Entel PCS. Todos los derechos reservados */
package com.epcs.administracion.suscripciones.dao;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import org.apache.log4j.Logger;

import weblogic.apache.xpath.operations.And;

import com.epcs.administracion.suscripciones.AdminSuscripcionService;
import com.epcs.administracion.suscripciones.AdminSuscripcionService_Service;
import com.epcs.administracion.suscripciones.types.BolsaActivaVigenteType;
import com.epcs.administracion.suscripciones.types.BolsaActualType;
import com.epcs.administracion.suscripciones.types.BolsaCompradaBAMFullType;
import com.epcs.administracion.suscripciones.types.BolsaDisponibleCompraType;
import com.epcs.administracion.suscripciones.types.BolsaType;
import com.epcs.administracion.suscripciones.types.BolsasDisponiblesOneShootType;
import com.epcs.administracion.suscripciones.types.ConsultaBolsasDisponiblesOneShootType;
import com.epcs.administracion.suscripciones.types.ConsultarBolsasActBBerryCCType;
import com.epcs.administracion.suscripciones.types.ConsultarBolsasActualYDisponibleFullSSCCType;
import com.epcs.administracion.suscripciones.types.ConsultarBolsasCompradasBAMFullType;
import com.epcs.administracion.suscripciones.types.ConsultarBolsasCompradasBAMType;
import com.epcs.administracion.suscripciones.types.ConsultarBolsasPlanesMPTType;
import com.epcs.administracion.suscripciones.types.ConsultarInternetMovilContratadoMPTType;
import com.epcs.administracion.suscripciones.types.ConsultarSaldoBolsaVigenteBAMType;
import com.epcs.administracion.suscripciones.types.ConsultarSaldoYBolsaDisponiblesCompraBAMType;
import com.epcs.administracion.suscripciones.types.InfoBolsasCompradasFullType;
import com.epcs.administracion.suscripciones.types.ResultadoConsultaBolsasDisponiblesOneShootType;
import com.epcs.administracion.suscripciones.types.ResultadoConsultarBolsasActBBerryCCType;
import com.epcs.administracion.suscripciones.types.ResultadoConsultarBolsasActualYDisponibleFullSSCCType;
import com.epcs.administracion.suscripciones.types.ResultadoConsultarBolsasCompradasBAMFullType;
import com.epcs.administracion.suscripciones.types.ResultadoConsultarBolsasCompradasBAMType;
import com.epcs.administracion.suscripciones.types.ResultadoConsultarBolsasPlanesMPTType;
import com.epcs.administracion.suscripciones.types.ResultadoConsultarInternetMovilContratadoMPTType;
import com.epcs.administracion.suscripciones.types.ResultadoConsultarSaldoBolsaVigenteBAMType;
import com.epcs.administracion.suscripciones.types.ResultadoConsultarSaldoYBolsaDisponiblesCompraBAMType;
import com.epcs.bean.BolsaBean;
import com.epcs.bean.BolsaPPBAMBean;
import com.epcs.bean.BolsaPPBean;
import com.epcs.bean.BolsasActualesDisponiblesBean;
import com.epcs.bean.ResumenPlanPPBAM;
import com.epcs.bean.SaldoBolsaPPBAMBean;
import com.epcs.bean.SaldoYBolsaDisponiblesCompraBAMBean;
import com.epcs.cliente.orden.dao.BolsaDAO;
import com.epcs.cliente.orden.delegate.BolsaDelegate;
import com.epcs.provision.suscripcion.bolsaspp.types.ListarBolsasResponseType;
import com.epcs.provision.suscripcion.bolsaspp.ListarBolsasServiceFaultMessage;
import com.epcs.provision.suscripcion.bolsaspp.SCOBPortType;
import com.epcs.provision.suscripcion.bolsaspp.SCOBPortTypeSOAPBindingQSService;
import com.epcs.provision.suscripcion.bolsaspp.types.RequestType;
import com.epcs.provision.suscripcion.bolsaspp.types.ListarBolsasResponseType.Mensaje.ListadoCartas.DetalleCartaServicio;
import com.epcs.provision.suscripcion.bolsaspp.types.ServiceFaultType;
import com.epcs.recursoti.configuracion.DateHelper;
import com.epcs.recursoti.configuracion.MiEntelProperties;
import com.epcs.recursoti.configuracion.Utils;
import com.epcs.recursoti.configuracion.locator.WebServiceLocator;
import com.epcs.recursoti.configuracion.locator.WebServiceLocatorException;
import com.epcs.recursoti.excepcion.DAOException;
import com.epcs.recursoti.excepcion.ServiceException;

/**
 * @author jmanzur (I2B) en nombre de Absalon Opazo (Atencion al Cliente, EntelPcs)
 *
 */
public class SuscripcionesBolsaDAO {
    
    private static final Logger LOGGER = Logger.getLogger(SuscripcionesBolsaDAO.class);

    public static final String CODIGO_RESPUESTA_OK = MiEntelProperties
            .getProperty("servicios.codigoRespuesta.exito");
    public static final String CODIGO_RESPUESTA_NO_INFO = MiEntelProperties
            .getProperty("servicios.codigoRespuesta.noInfo");
    public static final String TIME_ZONE_BOLSAS_BAM = MiEntelProperties
    .getProperty("parametros.bolsasbam.timezone");      
        
    public static final String TZ_BOLSAS_BAM_SANTIAGO = "America/Santiago";
    
    public static final String CANAL_SCOB = MiEntelProperties
    .getProperty("parametros.bolsasSCOBPP.canal");
    
    public static final String CANAL_SCOB_BAM = MiEntelProperties
    .getProperty("parametros.bolsasBAMCCPP.canal");
    
    public static final String PREFIJO = MiEntelProperties
    .getProperty("prefijo.entel");   
        
    
    /**
     * Consultar las bolsas actuales y disponibles de un usuario ss
     * 
     * @param msisdn
     * @return BolsasActualesDisponiblesBean
     * @throws DAOException
     * @throws ServiceException
     */
    public BolsasActualesDisponiblesBean consultarBolsasActualesDisponibles(
            String msisdn) throws DAOException, ServiceException {

        AdminSuscripcionService port = null;
        BolsasActualesDisponiblesBean bolsasActualesDisponiblesBean = new BolsasActualesDisponiblesBean();
        List<BolsaBean> listBolsasActualesBean = new ArrayList<BolsaBean>();
        List<BolsaBean> listBolsasDisponiblesBean = new ArrayList<BolsaBean>();

        LOGGER.info("Instanciando el port");
        try {
            port = (AdminSuscripcionService) WebServiceLocator.getInstance()
                    .getPort(AdminSuscripcionService_Service.class,
                            AdminSuscripcionService.class);
        } catch (WebServiceLocatorException e) {
            LOGGER.error("Error al inicializar el Port "
                    + AdminSuscripcionService.class, e);
            LOGGER.error( new DAOException(e));
        }

        LOGGER.info("Configurando Datos de la peticion");
        ConsultarBolsasActualYDisponibleFullSSCCType request = new ConsultarBolsasActualYDisponibleFullSSCCType();
        request.setMsisdn(msisdn);

        ResultadoConsultarBolsasActualYDisponibleFullSSCCType response = null;

        LOGGER.info("Invocando servicio");
        try {
            response = port.consultarBolsasActualYDisponibleFullSSCC(request);

        } catch (Exception e) {
            LOGGER.error("Exception caught on Service invocation", e);
            LOGGER.error( new DAOException(e));
        }

        String codigoRespuesta = response.getRespuesta().getCodigo();
        String descripcionRespuesta = response.getRespuesta().getDescripcion();

        LOGGER.info("codigoRespuesta  " + codigoRespuesta);
        LOGGER.info("descripcionRespuesta " + descripcionRespuesta);

        if (CODIGO_RESPUESTA_OK.equals(codigoRespuesta)) {

            List<BolsaActualType> listBolsasActuales = response.getBolsasActuales().getItemBolsaActual();
            List<BolsaType> listBolsasDisponibles = response
                    .getBolsasDisponibles().getItemBolsa();

            // Construir Lista de Bolsas Actuales
            for (BolsaActualType bolsaType : listBolsasActuales) {
                listBolsasActualesBean.add(buildBolsaActual(bolsaType));
            }

            // Construir Lista de Bolsas Disponibles
            for (BolsaType bolsaType : listBolsasDisponibles) {
                listBolsasDisponiblesBean.add(buildBolsa(bolsaType));
            }
            
            bolsasActualesDisponiblesBean.setBolsasActuales(listBolsasActualesBean);
            bolsasActualesDisponiblesBean
                    .setBolsasDisponibles(listBolsasDisponiblesBean);

        }
        else {

            LOGGER.error("Service error code received: " + codigoRespuesta
                    + " - " + descripcionRespuesta);
            LOGGER.error( new ServiceException(codigoRespuesta, descripcionRespuesta));

        }

        return bolsasActualesDisponiblesBean;
    }
    
    /**
     * Consultar las bolsas actuales y disponibles de un usuario ss
     * 
     * @param msisdn
     * @return BolsasActualesDisponiblesBean
     * @throws DAOException
     * @throws ServiceException
     */
    public BolsasActualesDisponiblesBean consultarBolsasActualesDisponiblesSSCC(
            String msisdn) throws DAOException, ServiceException {

        AdminSuscripcionService port = null;
        BolsasActualesDisponiblesBean bolsasActualesDisponiblesBean = new BolsasActualesDisponiblesBean();
        List<BolsaBean> listBolsasActualesBean = new ArrayList<BolsaBean>();
        List<BolsaBean> listBolsasDisponiblesBean = new ArrayList<BolsaBean>();

        LOGGER.info("Instanciando el port");
        try {
            port = (AdminSuscripcionService) WebServiceLocator.getInstance()
                    .getPort(AdminSuscripcionService_Service.class,
                            AdminSuscripcionService.class);
        } catch (WebServiceLocatorException e) {
            LOGGER.error("Error al inicializar el Port "
                    + AdminSuscripcionService.class, e);
            LOGGER.error( new DAOException(e));
        }

        LOGGER.info("Configurando Datos de la peticion");
        ConsultarBolsasActualYDisponibleFullSSCCType request = new ConsultarBolsasActualYDisponibleFullSSCCType();
        request.setMsisdn(msisdn);

        ResultadoConsultarBolsasActualYDisponibleFullSSCCType response = null;

        LOGGER.info("Invocando servicio");
        try {
            response = port.consultarBolsasActualYDisponibleFullSSCC(request);

        } catch (Exception e) {
            LOGGER.error("Exception caught on Service invocation", e);
            LOGGER.error( new DAOException(e));
        }

        String codigoRespuesta = response.getRespuesta().getCodigo();
        String descripcionRespuesta = response.getRespuesta().getDescripcion();

        LOGGER.info("codigoRespuesta  " + codigoRespuesta);
        LOGGER.info("descripcionRespuesta " + descripcionRespuesta);

        if (CODIGO_RESPUESTA_OK.equals(codigoRespuesta)) {
            List<BolsaActualType> listBolsasActuales = response.getBolsasActuales().getItemBolsaActual();
            List<BolsaType> listBolsasDisponibles = response
                    .getBolsasDisponibles().getItemBolsa();

            // Construir Lista de Bolsas Actuales
            for (BolsaActualType bolsaType : listBolsasActuales) {
                listBolsasActualesBean.add(buildBolsaActual(bolsaType));
            }

            // Construir Lista de Bolsas Disponibles
            for (BolsaType bolsaType : listBolsasDisponibles) {
                listBolsasDisponiblesBean.add(buildBolsa(bolsaType));
            }
            
            bolsasActualesDisponiblesBean.setBolsasActuales(listBolsasActualesBean);
            bolsasActualesDisponiblesBean
                    .setBolsasDisponibles(listBolsasDisponiblesBean);

        } else if (CODIGO_RESPUESTA_NO_INFO.equals(codigoRespuesta)) {
            return null;
        } else {
            LOGGER.error("Service error code received: " + codigoRespuesta
                    + " - " + descripcionRespuesta);
            LOGGER.error( new ServiceException(codigoRespuesta, descripcionRespuesta));
        }

        return bolsasActualesDisponiblesBean;
    }    
    
    
    /**
     * Listado de Bolsas BB Actuales de un usuario
     * 
     * @param msisdn
     * @return
     * @throws DAOException
     * @throws ServiceException
     */
    public List<BolsaBean> consultarBolsasActualesBBerryCC(String msisdn)
            throws DAOException, ServiceException {

        AdminSuscripcionService port = null;
        List<BolsaBean> listBolsasActualesBBBean = new ArrayList<BolsaBean>();

        LOGGER.info("Instanciando el port");
        try {
            port = (AdminSuscripcionService) WebServiceLocator.getInstance()
                    .getPort(AdminSuscripcionService_Service.class,
                            AdminSuscripcionService.class);
        } catch (WebServiceLocatorException e) {
            LOGGER.error("Error al inicializar el Port "
                    + AdminSuscripcionService.class, e);
            LOGGER.error( new DAOException(e));
        }

        LOGGER.info("Configurando Datos de la peticion");
        ConsultarBolsasActBBerryCCType request = new ConsultarBolsasActBBerryCCType();
        ResultadoConsultarBolsasActBBerryCCType response = null;

        LOGGER.info("Invocando servicio");
        try {

            request.setMsisdn(msisdn);
            response = port.consultarBolsasActBBerry(request);

        } catch (Exception e) {
            LOGGER.error("Exception caught on Service invocation", e);
            LOGGER.error( new DAOException(e));
        }

        String codigoRespuesta = response.getRespuesta().getCodigo();
        String descripcionRespuesta = response.getRespuesta().getDescripcion();

        LOGGER.info("codigoRespuesta  " + codigoRespuesta);
        LOGGER.info("descripcionRespuesta " + descripcionRespuesta);

        if (CODIGO_RESPUESTA_OK.equals(codigoRespuesta)) {

            List<BolsaType> listBolsasActuales = response.getBolsasActuales()
                    .getItemBolsa();

            // Construir Lista de Bolsas Actuales
            for (BolsaType bolsaType : listBolsasActuales) {
                listBolsasActualesBBBean.add(buildBolsa(bolsaType));
            }
        }
        else {

            LOGGER.error("Service error code received: " + codigoRespuesta
                    + " - " + descripcionRespuesta);
            LOGGER.error( new ServiceException(codigoRespuesta, descripcionRespuesta));

        }

        return listBolsasActualesBBBean;
    }
    
    /**
     * Listado de bolsas Disponibles para regalo
     * 
     * @param msisdn
     * @return
     * @throws DAOException
     * @throws ServiceException
     */
    public List<BolsaBean> consultarBolsasDisponiblesRegalo(String msisdn,String mercado)
            throws DAOException, ServiceException {

        AdminSuscripcionService port = null;
        List<BolsaBean> listBolsasParaRegaloBean = new ArrayList<BolsaBean>();

        LOGGER.info("Instanciando el port");
        try {
            port = (AdminSuscripcionService) WebServiceLocator.getInstance()
                    .getPort(AdminSuscripcionService_Service.class,
                            AdminSuscripcionService.class);
        } catch (WebServiceLocatorException e) {
            LOGGER.error("Error al inicializar el Port "
                    + AdminSuscripcionService.class, e);
            LOGGER.error( new DAOException(e));
        }

        LOGGER.info("Configurando Datos de la peticion");
        ConsultaBolsasDisponiblesOneShootType request = new ConsultaBolsasDisponiblesOneShootType();
        request.setMsisdn(msisdn);
        request.setFlagMercado(mercado);

        ResultadoConsultaBolsasDisponiblesOneShootType response = null;

        LOGGER.info("Invocando servicio");
        try {
            response = port.consultaBolsasDisponiblesOneShoot(request);

        } catch (Exception e) {
            LOGGER.error("Exception caught on Service invocation", e);
            LOGGER.error( new DAOException(e));
        }

        String codigoRespuesta = response.getRespuesta().getCodigo();
        String descripcionRespuesta = response.getRespuesta().getDescripcion();

        LOGGER.info("codigoRespuesta  " + codigoRespuesta);
        LOGGER.info("descripcionRespuesta " + descripcionRespuesta);

        if (CODIGO_RESPUESTA_OK.equals(codigoRespuesta)) {

            List<BolsasDisponiblesOneShootType> listBolsasRegalo = response
                    .getBolsasDisponiblesOneShoot()
                    .getBolsasDisponiblesOneShootType();
            BolsaBean bolsaBean;
            // Construir Lista de Bolsas Actuales
            for (BolsasDisponiblesOneShootType bolsaType : listBolsasRegalo) {
                bolsaBean = new BolsaBean();
                bolsaBean.setNombreBolsa(bolsaType.getNombreBolsa());
                bolsaBean.setSnCodigo(bolsaType.getCodBolsa());
                bolsaBean.setObservacion(bolsaType.getCartaServicio());
                bolsaBean.setDescBolsa(bolsaType.getDescBolsa()
                        .getItemDescBolsa());
                bolsaBean.setTipoBolsa(bolsaType.getTipoBolsa());

                try {
                    bolsaBean.setCantidad(Double.parseDouble(bolsaType
                            .getCantidad()));
                } catch (NumberFormatException nfe) {
                    LOGGER
                            .warn("Cantidad de Bolsa Para Regalo no valido. bolsa:"+bolsaType.getNombreBolsa()+ " codigo :"+bolsaType.getCodBolsa() + "Cantida +'"+nfe.getMessage()+"'");
                }
                
                try {
                    bolsaBean
                    .setCosto(Double.parseDouble(bolsaType.getCosto()));
                } catch (NumberFormatException nfe) {
                    LOGGER.error("Valor Bolsa Para Regalo no valido."
                            + nfe.getMessage());
                    LOGGER.error( new DAOException("Valor Bolsa Para Regalo no valido."
                            + nfe.getMessage()));
                }
                listBolsasParaRegaloBean.add(bolsaBean);
            }
        }
        else {

            LOGGER.error("Service error code received: " + codigoRespuesta
                    + " - " + descripcionRespuesta);
            LOGGER.error( new ServiceException(codigoRespuesta, descripcionRespuesta));

        }

        return listBolsasParaRegaloBean;
    }
    
    public boolean consultarBolsasPlanes(String mercado, String msisdn)
            throws DAOException, ServiceException {

        boolean tieneBolsas = false;
        AdminSuscripcionService port;
        LOGGER.info("Instanciando el port " + AdminSuscripcionService.class);
        try {
            port = (AdminSuscripcionService) WebServiceLocator.getInstance()
                    .getPort(AdminSuscripcionService_Service.class,
                            AdminSuscripcionService.class);

            ConsultarBolsasPlanesMPTType request = new ConsultarBolsasPlanesMPTType();
            ResultadoConsultarBolsasPlanesMPTType response = null;

            try {

                LOGGER.info("Configurando Datos de la peticion");

                request.setMercado(mercado);
                request.setMsisdn(msisdn);

                LOGGER.info("Invocando servicio");
                response = port.consultarBolsasPlanesMPT(request);

            } catch (Exception e) {
                LOGGER.error("Exception caught on Service invocation: "
                        + "consultarBolsasPlanesMPT", e);
                LOGGER.error( new DAOException(e));
            }

            String codigoRespuesta = response.getRespuesta().getCodigo();
            String descripcionRespuesta = response.getRespuesta()
                    .getDescripcion();

            LOGGER.info("codigoRespuesta " + codigoRespuesta);
            LOGGER.info("descripcionRespuesta " + descripcionRespuesta);

            if (Utils.isEmptyString(codigoRespuesta)) {
                LOGGER.error( new DAOException(
                        "consultarBolsasPlanesMPT: Servicio no responde "
                                + "con codigoRespuesta"));
            }

            if (CODIGO_RESPUESTA_OK.equals(codigoRespuesta)) {

                try {
                    if (response.getTieneBolsasPlanesMPT().equals("1")) {
                        tieneBolsas = true;
                    }
                    return tieneBolsas;
                } catch (Exception e) {
                    LOGGER.error("Exception caught on Service response: "
                            + "consultarBolsasPlanesMPT", e);
                    LOGGER.error( new DAOException(e));
                }

            }
            else {
                LOGGER
                        .error("consultarBolsasPlanesMPT: Service error code received: "
                                + codigoRespuesta
                                + " - "
                                + descripcionRespuesta);
                LOGGER.error( new ServiceException(codigoRespuesta,
                        descripcionRespuesta));
            }
        } catch (WebServiceLocatorException e) {
            LOGGER.error("Error al inicializar el Port "
                    + AdminSuscripcionService.class, e);
            LOGGER.error( new DAOException(e));
        }
        return false;
    }
    
    public boolean consultarInternetMovilContratado(String msisdn)
            throws DAOException, ServiceException {

        boolean tieneIM = false;
        AdminSuscripcionService port;
        LOGGER.info("Instanciando el port " + AdminSuscripcionService.class);
        try {
            port = (AdminSuscripcionService) WebServiceLocator.getInstance()
                    .getPort(AdminSuscripcionService_Service.class,
                            AdminSuscripcionService.class);

            ConsultarInternetMovilContratadoMPTType request = new ConsultarInternetMovilContratadoMPTType();
            ResultadoConsultarInternetMovilContratadoMPTType response = null;

            try {

                LOGGER.info("Configurando Datos de la peticion");

                request.setMsisdn(msisdn);

                LOGGER.info("Invocando servicio");
                response = port.consultarInternetMovilContratadoMPT(request);

            } catch (Exception e) {
                LOGGER.error("Exception caught on Service invocation: "
                        + "consultarInternetMovilContratadoMPT", e);
                LOGGER.error( new DAOException(e));
            }

            String codigoRespuesta = response.getRespuesta().getCodigo();
            String descripcionRespuesta = response.getRespuesta()
                    .getDescripcion();

            LOGGER.info("codigoRespuesta " + codigoRespuesta);
            LOGGER.info("descripcionRespuesta " + descripcionRespuesta);

            if (Utils.isEmptyString(codigoRespuesta)) {
                LOGGER.error( new DAOException(
                        "consultarInternetMovilContratadoMPT: Servicio no responde "
                                + "con codigoRespuesta"));
            }

            if (CODIGO_RESPUESTA_OK.equals(codigoRespuesta)) {
                try {
                    if (response.getInternetMovilContratadoMPT().getTieneIM().equals("1")) {
                        tieneIM = true;
                    }
                    return tieneIM;
                } catch (Exception e) {
                    LOGGER.error("Exception caught on Service response: "
                            + "consultarInternetMovilContratadoMPT", e);
                    LOGGER.error( new DAOException(e));
                }

            }
            else {
                LOGGER
                        .error("consultarInternetMovilContratadoMPT: Service error code received: "
                                + codigoRespuesta
                                + " - "
                                + descripcionRespuesta);
                LOGGER.error( new ServiceException(codigoRespuesta,
                        descripcionRespuesta));
            }
        } catch (WebServiceLocatorException e) {
            LOGGER.error("Error al inicializar el Port "
                    + AdminSuscripcionService.class, e);
            LOGGER.error( new DAOException(e));
        }
        return false;
    }    
    
    
    /**
     * 
     * @param bolsaType
     * @return BolsaBean
     */
    private BolsaBean buildBolsa(BolsaType bolsaType) {
        BolsaBean bolsaBean = new BolsaBean();
        
            bolsaBean.setCantidad(Double.parseDouble(bolsaType.getCantidad()));
            bolsaBean.setCosto(Double.parseDouble(bolsaType.getCosto()));
            bolsaBean.setDescBolsa(bolsaType.getDescBolsa().getItemDescBolsa());
            bolsaBean.setFlagPromocion(bolsaType.getFlagPromocion());
            bolsaBean.setNombreBolsa(bolsaType.getNombreBolsa());
            bolsaBean.setObservacion(bolsaType.getObservacion());
            bolsaBean.setSpCodigo(bolsaType.getSpCodigo());
            bolsaBean.setSnCodigo(bolsaType.getSnCodigo());
            bolsaBean.setTipoBolsa(bolsaType.getTipoBolsa());
            bolsaBean.setTipoVigencia(bolsaBean.getTipoVigencia());
            bolsaBean.setVigencia(bolsaType.getVigencia());
        
        return bolsaBean;
    }
    
    
    /**
     * 
     * @param bolsaType
     * @return BolsaBean
     */
    private BolsaBean buildBolsaActual(BolsaActualType bolsaType) {
        BolsaBean bolsaBean = new BolsaBean();
        try {
            try{
                bolsaBean.setCosto(Double.parseDouble(bolsaType.getCosto()));
               }catch(NumberFormatException nfe){
                   LOGGER.warn("Costo de bolsa no valida:".concat(bolsaType.getNombreBolsa()).concat(",Cantidad").concat(bolsaType.getCosto()), nfe.getCause());
               }
            try{   
            bolsaBean.setCantidad(Double.parseDouble(bolsaType.getCantidad()));
            }catch(NumberFormatException nfe){
                   LOGGER.warn("Cantidad de bolsa no valida:".concat(bolsaType.getNombreBolsa()).concat(",Cantidad").concat(bolsaType.getCantidad()), nfe.getCause());
            }
            bolsaBean.setDescBolsa(bolsaType.getDescBolsa().getItemDescBolsa());
            bolsaBean.setFlagPromocion(bolsaType.getFlagPromocion());
            bolsaBean.setNombreBolsa(bolsaType.getNombreBolsa());
            bolsaBean.setObservacion(bolsaType.getObservacion());
            bolsaBean.setSpCodigo(bolsaType.getSpCodigo());
            bolsaBean.setSnCodigo(bolsaType.getSnCodigo());
            bolsaBean.setTipoBolsa(bolsaType.getTipoBolsa());
            bolsaBean.setTipoVigencia(bolsaBean.getTipoVigencia());
            bolsaBean.setVigencia(bolsaType.getVigencia());
            bolsaBean.setEstado(bolsaType.getEstado());
            bolsaBean.setFechaPendiente(DateHelper.parseDate(bolsaType.getFechaPendiente(),DateHelper.FORMAT_yyyyMMdd_HYPHEN));
        } catch (Exception ex) {
            LOGGER.warn("Excepcion a construir bolsa.".concat(bolsaType.getNombreBolsa()), ex);
        }
        return bolsaBean;
    }

    /**
     * Listado de bolsas compradas de un usuario pp BAM
     * 
     * @param msisdn
     * @return
     * @throws DAOException
     * @throws ServiceException
     */
    public List<BolsaPPBAMBean> consultarBolsasCompradasPPBAM(String msisdn)
            throws DAOException, ServiceException {

        AdminSuscripcionService port = null;
        List<BolsaPPBAMBean> listBolsasCompradas = new ArrayList<BolsaPPBAMBean>();

        LOGGER.info("Instanciando el port");
        try {
            port = (AdminSuscripcionService) WebServiceLocator.getInstance()
                    .getPort(AdminSuscripcionService_Service.class,
                            AdminSuscripcionService.class);
        } catch (WebServiceLocatorException e) {
            LOGGER.error("Error al inicializar el Port "
                    + AdminSuscripcionService.class, e);
            LOGGER.error( new DAOException(e));
        }

        LOGGER.info("Configurando Datos de la peticion");
        ConsultarBolsasCompradasBAMFullType request = new ConsultarBolsasCompradasBAMFullType();
        request.setMsisdn(msisdn);

        ResultadoConsultarBolsasCompradasBAMFullType response = null;

        LOGGER.info("Invocando servicio");
        try {
            response = port.consultarBolsasCompradasBAMFull(request);

        } catch (Exception e) {
            LOGGER.error("Exception caught on Service invocation", e);
            LOGGER.error( new DAOException(e));
        }

        String codigoRespuesta = response.getRespuesta().getCodigo();
        String descripcionRespuesta = response.getRespuesta().getDescripcion();

        LOGGER.info("codigoRespuesta  " + codigoRespuesta);
        LOGGER.info("descripcionRespuesta " + descripcionRespuesta);

        if (CODIGO_RESPUESTA_OK.equals(codigoRespuesta)) {
              BolsaPPBAMBean bolsaPPBAMBean;
              for(BolsaCompradaBAMFullType bolsaComprada : response.getConsultaBolsasCompradasFull().getBolsasCompradas()){
                  bolsaPPBAMBean = new BolsaPPBAMBean();
                  bolsaPPBAMBean.setCantidad(Integer.parseInt(bolsaComprada.getCantidad()));
                  bolsaPPBAMBean.setFechaFin(DateHelper.parseDate(bolsaComprada.getFechaFin(), ""));
                  bolsaPPBAMBean.setFechaInicio(DateHelper.parseDate(bolsaComprada.getFechaInicio(), ""));
                  bolsaPPBAMBean.setIdBolsa(bolsaComprada.getIdBolsa());
                  bolsaPPBAMBean.setNombreBolsa(bolsaComprada.getNombreBolsa());
                  bolsaPPBAMBean.setSaldoTiempo(bolsaComprada.getSaldo());
                  listBolsasCompradas.add(bolsaPPBAMBean);
              }
        }
        else {

            LOGGER.error("Service error code received: " + codigoRespuesta
                    + " - " + descripcionRespuesta);
            LOGGER.error( new ServiceException(codigoRespuesta, descripcionRespuesta));

        }

        return listBolsasCompradas;
    }
    
    
    /**
     * Devuelve el saldo de la bolsa usuario pp BAM
     * 
     * @param msisdn
     * @return
     * @throws DAOException
     * @throws ServiceException
     */
    public SaldoBolsaPPBAMBean consultarSaldoBolsaVigentePPBAM(String msisdn)
            throws DAOException, ServiceException {

        AdminSuscripcionService port = null;
        SaldoBolsaPPBAMBean saldoBolsaPPBAM = null;
        LOGGER.info("Instanciando el port");
        try {
            port = (AdminSuscripcionService) WebServiceLocator.getInstance()
                    .getPort(AdminSuscripcionService_Service.class,
                            AdminSuscripcionService.class);
        } catch (WebServiceLocatorException e) {
            LOGGER.error("Error al inicializar el Port "
                    + AdminSuscripcionService.class, e);
            LOGGER.error( new DAOException(e));
        }

        LOGGER.info("Configurando Datos de la peticion");
        ConsultarSaldoBolsaVigenteBAMType request = new ConsultarSaldoBolsaVigenteBAMType();
        request.setMsisdn(msisdn);
        
        TimeZone tz = Calendar.getInstance().getTimeZone();        
        if(tz.getID().equals(TZ_BOLSAS_BAM_SANTIAGO)){
        	request.setZonaHoraria(TIME_ZONE_BOLSAS_BAM);
        }
        else{
        	request.setZonaHoraria("");
        }
        

        ResultadoConsultarSaldoBolsaVigenteBAMType response = null;

        LOGGER.info("Invocando servicio");
        try {
            response = port.consultarSaldoBolsaVigenteBAM(request);

        } catch (Exception e) {
            LOGGER.error("Exception caught on Service invocation", e);
            LOGGER.error( new DAOException(e));
        }

        String codigoRespuesta = response.getRespuesta().getCodigo();
        String descripcionRespuesta = response.getRespuesta().getDescripcion();

        LOGGER.info("codigoRespuesta  " + codigoRespuesta);
        LOGGER.info("descripcionRespuesta " + descripcionRespuesta);

        if (CODIGO_RESPUESTA_OK.equals(codigoRespuesta)) {
            saldoBolsaPPBAM = new SaldoBolsaPPBAMBean();
            BolsaActivaVigenteType bolsaActivaVigenteType = response.getBolsaActivaVigente();
            if(bolsaActivaVigenteType != null){
                String saldo[] = bolsaActivaVigenteType.getSaldoTiempo().split(" ");
                saldoBolsaPPBAM.setDias(saldo[0].replaceAll("[a-zA-Z]", ""));
                saldoBolsaPPBAM.setHoras(saldo[1].replaceAll("[a-zA-Z]", ""));
                saldoBolsaPPBAM.setMinutos(saldo[2].replaceAll("[a-zA-Z]", ""));
                saldoBolsaPPBAM.setTieneSaldo(true);
            }
        }
        else {

            LOGGER.error("Service error code received: " + codigoRespuesta
                    + " - " + descripcionRespuesta);
            LOGGER.error( new ServiceException(codigoRespuesta, descripcionRespuesta));

        }

        return saldoBolsaPPBAM;
    }
    
    
    /**
     * Devuelve el saldo de la bolsa usuario pp y el listado de bolsas dipsonibles BAM
     * 
     * @param msisdn
     * @return
     * @throws DAOException
     * @throws ServiceException
     */
    public SaldoYBolsaDisponiblesCompraBAMBean consultarSaldoYBolsaDisponiblesCompraPPBAM(String msisdn, String fdt)
            throws DAOException, ServiceException {

		SaldoYBolsaDisponiblesCompraBAMBean bolsaDisponiblesCompraBAMBean = new SaldoYBolsaDisponiblesCompraBAMBean();
        List<BolsaPPBean> listBolsaDisp = new ArrayList<BolsaPPBean>() ;
		List<BolsaPPBean> bolsasActivas = new ArrayList<BolsaPPBean>();
		Date vigencia=new Date();
		Double saldo=0.0;
		
        try {
        	listBolsaDisp=obtenerBolsasDisponiblesBAMScob(msisdn);
        	BolsaDelegate bolsaDelegate=new BolsaDelegate();
			bolsaDelegate.setBolsaDAO(new BolsaDAO());            	
        	bolsasActivas = bolsaDelegate.obtenerBolsasActivasBAMScob(msisdn);

        	List<BolsaPPBean> tempActivas=new ArrayList<BolsaPPBean>();
        
        	saldo=bolsasActivas.get(0).getSaldo();
        	vigencia=bolsasActivas.get(0).getFechaExpiracion();
        	
			for (int x=1;x<bolsasActivas.size();x++){
				if (bolsasActivas.get(x).getTipoBolsa().length()>4 && bolsasActivas.get(x).getTipoBolsa().substring(0,5).equalsIgnoreCase("PPBAM")){
            			tempActivas.add(bolsasActivas.get(x));            			
        }
        }
			bolsasActivas=tempActivas;

            bolsaDisponiblesCompraBAMBean = new SaldoYBolsaDisponiblesCompraBAMBean();
            
	        bolsaDisponiblesCompraBAMBean.setSaldoRecargas(saldo);	        
	        bolsaDisponiblesCompraBAMBean.setVigenciaSaldo(vigencia);
            bolsaDisponiblesCompraBAMBean.setBolsasDisponibles(listBolsaDisp);
	        bolsaDisponiblesCompraBAMBean.setBolsasActivas(bolsasActivas);

        }catch (DAOException de){
        	LOGGER.error("Service error code received: ");
        }

        return bolsaDisponiblesCompraBAMBean;
    }
    
    
    /**
     * Devuelve el saldo de la bolsa usuario pp y el listado de bolsas dipsonibles BAM
     * 
     * @param msisdn
     * @return
     * @throws DAOException
     * @throws ServiceException
     */
    public ResumenPlanPPBAM consultarResumenPlanPPBAM(String msisdn)
            throws DAOException, ServiceException {

        AdminSuscripcionService port = null;
        ResumenPlanPPBAM resumenPlanPPBAM = null;
        
        LOGGER.info("Instanciando el port");
        try {
            port = (AdminSuscripcionService) WebServiceLocator.getInstance()
                    .getPort(AdminSuscripcionService_Service.class,
                            AdminSuscripcionService.class);
        } catch (WebServiceLocatorException e) {
            LOGGER.error("Error al inicializar el Port "
                    + AdminSuscripcionService.class, e);
            LOGGER.error( new DAOException(e));
        }

        LOGGER.info("Configurando Datos de la peticion");
        ConsultarBolsasCompradasBAMType request = new ConsultarBolsasCompradasBAMType();
        request.setMsisdn(msisdn);

        ResultadoConsultarBolsasCompradasBAMType response = null;

        LOGGER.info("Invocando servicio");
        try {
            
            response = port.consultarBolsasCompradasBAM(request);

        } catch (Exception e) {
            LOGGER.error("Exception caught on Service invocation", e);
            LOGGER.error( new DAOException(e));
        }

        String codigoRespuesta = response.getRespuesta().getCodigo();
        String descripcionRespuesta = response.getRespuesta().getDescripcion();

        LOGGER.info("codigoRespuesta  " + codigoRespuesta);
        LOGGER.info("descripcionRespuesta " + descripcionRespuesta);

        if (CODIGO_RESPUESTA_OK.equals(codigoRespuesta)) {
            resumenPlanPPBAM = new ResumenPlanPPBAM();
            resumenPlanPPBAM.setEstado(response.getConsultaBolsasCompradas().getState());
            resumenPlanPPBAM.setFechaActivacion(DateHelper.parseDate(response.getConsultaBolsasCompradas().getActivationDate(), DateHelper.FORMAT_yyyyMMddhhmmss));
            resumenPlanPPBAM.setIccid(response.getConsultaBolsasCompradas().getIccid());
            resumenPlanPPBAM.setImsi(response.getConsultaBolsasCompradas().getImsi());
            resumenPlanPPBAM.setMsisdn(response.getConsultaBolsasCompradas().getMsisdn());
            resumenPlanPPBAM.setPin(response.getConsultaBolsasCompradas().getPin());
            resumenPlanPPBAM.setPin2(response.getConsultaBolsasCompradas().getPin2());
            resumenPlanPPBAM.setPuk(response.getConsultaBolsasCompradas().getPuk());
            resumenPlanPPBAM.setPuk2(response.getConsultaBolsasCompradas().getPuk2());
            resumenPlanPPBAM.setSaldoRecargas(Double.parseDouble(response.getConsultaBolsasCompradas().getDebit()));
            resumenPlanPPBAM.setServicePrivider(response.getConsultaBolsasCompradas().getServiceProviderId());
        }
        else {

            LOGGER.error("Service error code received: " + codigoRespuesta
                    + " - " + descripcionRespuesta);
            LOGGER.error( new ServiceException(codigoRespuesta, descripcionRespuesta));

        }

        return resumenPlanPPBAM;
    }
    
    /**
     * 
     * @param msisdn
     * @return
     * @throws DAOException
     * @throws ServiceException
     */
    public boolean consultarBolsasPlanesBAM(String msisdn)
    throws DAOException, ServiceException {

            boolean tieneBolsas = false;
            AdminSuscripcionService port;
            //MensajesParaTiPortType port;
            LOGGER.info("Instanciando el port " + AdminSuscripcionService.class);
            try {
                port = (AdminSuscripcionService) WebServiceLocator.getInstance().getPort(
                        AdminSuscripcionService_Service.class, AdminSuscripcionService.class);
                
                
                ConsultarBolsasCompradasBAMFullType request = new ConsultarBolsasCompradasBAMFullType();       
                ResultadoConsultarBolsasCompradasBAMFullType response = null;
            
                try {
            
                    LOGGER.info("Configurando Datos de la peticion");
                    request.setMsisdn(msisdn);                
            
                    LOGGER.info("Invocando servicio");
                    response = port.consultarBolsasCompradasBAMFull(request);
            
                } catch (Exception e) {
                    LOGGER.error("Exception caught on Service invocation: "
                            + "ConsultarBolsasCompradasBAMFull", e);
                    LOGGER.error( new DAOException(e));
                }
            
                String codigoRespuesta = response.getRespuesta().getCodigo();
                String descripcionRespuesta = response.getRespuesta()
                        .getDescripcion();
            
                LOGGER.info("codigoRespuesta " + codigoRespuesta);
                LOGGER.info("descripcionRespuesta " + descripcionRespuesta);
            
                if (Utils.isEmptyString(codigoRespuesta)) {
                     LOGGER.error( new DAOException("ConsultarBolsasCompradasBAMFull: Servicio no responde "
                            + "con codigoRespuesta"));
                }
            
                if (CODIGO_RESPUESTA_OK.equals(codigoRespuesta)) {
            
                    try {
                          InfoBolsasCompradasFullType info =response.getConsultaBolsasCompradasFull();
         
                          if ( (info.getBolsasCompradas().size()) >0) {  
                              tieneBolsas = true;
                          }
                             return tieneBolsas;
                     
                    } catch (Exception e) {
                        LOGGER.error("Exception caught on Service response: "
                                + "ConsultarBolsasCompradasBAMFull", e);
                        LOGGER.error( new DAOException(e));
                    }
            
                }
                else {
                    LOGGER.error("ConsultarBolsasCompradasBAMFull: Service error code received: "
                            + codigoRespuesta + " - " + descripcionRespuesta);
                    LOGGER.error( new ServiceException(codigoRespuesta,
                            descripcionRespuesta));
                }
            } catch (WebServiceLocatorException e) {
                LOGGER.error("Error al inicializar el Port " + AdminSuscripcionService.class, e);
                LOGGER.error( new DAOException(e));
            }
            return false;
        }

    /**
     * Obtiene bolsas disponibles servicio scob
     * 
     * @param msisdn
     * @return List<BolsaBean>
     * @throws DAOException
     * @throws ServiceException
     */
    public List<BolsaPPBean> obtenerBolsasDisponiblesScob(String msisdn)
            throws DAOException, ServiceException {

    	SCOBPortType port = null;
        List<BolsaPPBean> listBolsasCompradasBean = new ArrayList<BolsaPPBean>();

        LOGGER.info("Instanciando el port");
        try {
            port = (SCOBPortType) WebServiceLocator.getInstance()
                    .getPort(SCOBPortTypeSOAPBindingQSService .class,
                    		SCOBPortType.class);
        } catch (WebServiceLocatorException e) {
            LOGGER.error("Error al inicializar el Port "
                    + SCOBPortType.class, e);
            LOGGER.error( new DAOException(e));
        }       
        
        LOGGER.info("Configurando Datos de la peticion");
        RequestType request = new RequestType();
        ListarBolsasResponseType.Mensaje response = null;

        LOGGER.info("Invocando servicio");
        try {
            
            request.setMsisdn(PREFIJO+msisdn);
            request.setCanal(CANAL_SCOB);  

            response = port.listarBolsasRequestDocument(request);

        } catch (ListarBolsasServiceFaultMessage e) {
            ServiceFaultType mensaje = e.getFaultInfo().getMensaje();
            String codigoRespuesta = mensaje.getCodigo();
            String descripcionRespuesta = mensaje.getDescripcion();
            
            LOGGER.error("Service error code received: " + codigoRespuesta
                    + " - " + descripcionRespuesta);
            LOGGER.error( new ServiceException(codigoRespuesta, descripcionRespuesta));                
        } catch (Exception e) {
            LOGGER.error("Exception caught on Service invocation: "
                    + "listarBolsasRequestDocument", e);
            LOGGER.error( new DAOException(e));
        }
      
        try {

            List<DetalleCartaServicio> listGrupoBolsas = response.getListadoCartas().getDetalleCartaServicio();
            // Construir Lista de Bolsas Compradas
            for (DetalleCartaServicio bolsaType : listGrupoBolsas) {
               
                   BolsaPPBean bolsaBean = new BolsaPPBean();
                   bolsaBean = new BolsaPPBean();
                   bolsaBean.setCodBolsa(bolsaType.getCodigo());                  
                   bolsaBean.setNombreBolsa(bolsaType.getNombre());                  
                   bolsaBean.setCaracteristicas(bolsaType.getDescripcion());    
                   bolsaBean.setDescComercial(bolsaType.getDescripcionComercial());                  
                   bolsaBean.setTipoBolsa(bolsaType.getTipoOferta());
                   bolsaBean.setFechaExpiracion(DateHelper.parseDate(bolsaType.getFinExposicion(), DateHelper.FORMAT_yyyyMMddhhmmss));
                   bolsaBean.setUnidad(bolsaType.getUnidad());   
                   bolsaBean.setOrden(bolsaType.getOrden()); 
                   
                   try {
                       bolsaBean.setValorBolsa(Double.parseDouble(bolsaType.getPrecio()));
                   } catch (NumberFormatException nfe) {
                       LOGGER.warn("Valor de Bolsa Comprada no valido :"+bolsaType.getCodigo()
                               + nfe.getMessage());
                   }                  
                  
                    listBolsasCompradasBean.add(bolsaBean);
                
            }
        }catch (Exception e) {
                LOGGER.error("Exception caught on Service response: "
                        + "listarBolsasRequestDocument", e);
                LOGGER.error( new DAOException(e));
         }
        return listBolsasCompradasBean;
    }    

    /**
     * Obtiene bolsas BAM disponibles servicio scob
     * 
     * @param msisdn
     * @return List<BolsaBean>
     * @throws DAOException
     * @throws ServiceException
     */
    public List<BolsaPPBean> obtenerBolsasDisponiblesBAMScob(String msisdn)
            throws DAOException, ServiceException {

    	SCOBPortType port = null;
        List<BolsaPPBean> listBolsasCompradasBean = new ArrayList<BolsaPPBean>();

        LOGGER.info("Instanciando el port");
        try {
            port = (SCOBPortType) WebServiceLocator.getInstance()
                    .getPort(SCOBPortTypeSOAPBindingQSService .class,
                    		SCOBPortType.class);
        } catch (WebServiceLocatorException e) {
            LOGGER.error("Error al inicializar el Port "
                    + SCOBPortType.class, e);
            LOGGER.error( new DAOException(e));
        }       
        
        LOGGER.info("Configurando Datos de la peticion");
        RequestType request = new RequestType();
        ListarBolsasResponseType.Mensaje response = null;

        LOGGER.info("Invocando servicio");
        try {
            
            request.setMsisdn(PREFIJO+msisdn);
            request.setCanal(CANAL_SCOB_BAM);  

            response = port.listarBolsasRequestDocument(request);

        } catch (ListarBolsasServiceFaultMessage e) {
            ServiceFaultType mensaje = e.getFaultInfo().getMensaje();
            String codigoRespuesta = mensaje.getCodigo();
            String descripcionRespuesta = mensaje.getDescripcion();
            
            LOGGER.error("Service error code received: " + codigoRespuesta
                    + " - " + descripcionRespuesta);
            LOGGER.error( new ServiceException(codigoRespuesta, descripcionRespuesta));                
        } catch (Exception e) {
            LOGGER.error("Exception caught on Service invocation: "
                    + "listarBolsasRequestDocument", e);
            LOGGER.error( new DAOException(e));
        }
      
        try {

            List<DetalleCartaServicio> listGrupoBolsas = response.getListadoCartas().getDetalleCartaServicio();
            // Construir Lista de Bolsas 
            for (DetalleCartaServicio bolsaType : listGrupoBolsas) {
               
            	   BolsaPPBean bolsaBean = new BolsaPPBean();
                   bolsaBean = new BolsaPPBean();
                   bolsaBean.setCodBolsa(bolsaType.getCodigo());                  
                   bolsaBean.setNombreBolsa(bolsaType.getNombre());                  
                   
                   if (bolsaType.getDescripcionTecnica().compareToIgnoreCase("ilimitada")==0){
                	   bolsaBean.setDescSaldo("Sin restricci&oacute;n");
                	   bolsaBean.setUnidad("TIEMPO");
                   }else{      
                	   String resultado="";
                	   try{
							Double datos;
							datos=Double.parseDouble(bolsaType.getDescripcionTecnica());
							DecimalFormat formateador = new DecimalFormat("###.#");
							DecimalFormatSymbols dfs=new DecimalFormatSymbols(Locale.FRENCH);    	
							formateador.setDecimalFormatSymbols(dfs);
							if (datos<1024)
								resultado=formateador.format(datos)+" KB";
							datos=datos/1024;
							if (datos>=1 &&datos<1024)
								resultado=formateador.format(datos)+" MB";
							datos=datos/1024;
							if (datos>=1 &&datos<1024)
								resultado=formateador.format(datos)+" GB";
							   bolsaBean.setDescSaldo(resultado);
							   bolsaBean.setUnidad("CUOTA");
                	   }catch (Exception e) {
						// TODO: handle exception
                	   }
                	   }
                   
                   bolsaBean.setTipoBolsa(bolsaType.getDestino());
                   bolsaBean.setDescComercial(bolsaType.getDescripcionComercial());                  
                   bolsaBean.setFechaExpiracion(DateHelper.parseDate(bolsaType.getFinExposicion(), DateHelper.FORMAT_yyyyMMddhhmmss));
                   bolsaBean.setOrden(bolsaType.getOrden()); 
                   
                   try {
                       bolsaBean.setValorBolsa(Double.parseDouble(bolsaType.getPrecio()));
                   } catch (NumberFormatException nfe) {
                       LOGGER.warn("Valor de Bolsa Comprada no valido :"+bolsaType.getCodigo()
                               + nfe.getMessage());
                   }                  
                  
                    listBolsasCompradasBean.add(bolsaBean);
                
            }
        }catch (Exception e) {
                LOGGER.error("Exception caught on Service response: "
                        + "listarBolsasRequestDocument", e);
                LOGGER.error( new DAOException(e));
         }
        return listBolsasCompradasBean;
    }   
}
