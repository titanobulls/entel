/* Propiedad de Entel PCS. Todos los derechos reservados */
package com.epcs.vtasymktg.fidelizacion.dao;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.ws.Holder;

import org.apache.log4j.Logger;
import org.example.zonaverano.ZonaVerano;
import org.example.zonaverano.ZonaVerano_Service;

import cl.src.entel.consultacartolapuntosanual.ConsultaCartolaPuntosAnual;
import cl.src.entel.consultacartolapuntosanual.ConsultaCartolaPuntosAnualResponse;
import cl.src.entel.consultacartolapuntosanual.IConsultaCartolaPuntosAnual;
import cl.src.entel.consultacartolapuntosanual.TransaccionDetalle;
import cl.src.entel.consultacartolapuntosanual.TransaccionPagina;
import cl.src.entel.consultapuntosporvencer.ConsultaPuntosPorVencer;
import cl.src.entel.consultapuntosporvencer.ConsultaPuntosPorVencerResponse;
import cl.src.entel.consultapuntosporvencer.IConsultaPuntosPorVencer;
import cl.src.entel.consultapuntosporvencer.VencimientoDetalle;

import com.epcs.bean.CanjeDePuntosBean;
import com.epcs.bean.ExpiracionProximaPuntosBean;
import com.epcs.bean.HistorialDetalleBean;
import com.epcs.bean.ItemDetalleCanjeBean;
import com.epcs.bean.ItemMatrizCanjeBean;
import com.epcs.bean.ItemMercadoCanjeBean;
import com.epcs.bean.ItemProductoCanjeBean;
import com.epcs.bean.MatrizCatalogoCanjeBean;
import com.epcs.bean.PaginaHistorialCanjeBean;
import com.epcs.bean.PuntosBean;
import com.epcs.bean.ResultadoCanjeDePuntosBean;
import com.epcs.bean.ResultadoConsultarPuntosBean;
import com.epcs.bean.ResultadoObtenerHistorialBean;
import com.epcs.bean.ResultadoRegalarPuntosBean;
import com.epcs.loyalty.zona.wsadminpuntos.DetalleResponse;
import com.epcs.loyalty.zona.wsadminpuntos.IPuntos;
import com.epcs.loyalty.zona.wsadminpuntos.ItemMatrizResponse;
import com.epcs.loyalty.zona.wsadminpuntos.MatrizResponse;
import com.epcs.loyalty.zona.wsadminpuntos.MercadoResponse;
import com.epcs.loyalty.zona.wsadminpuntos.ProductoResponse;
import com.epcs.loyalty.zona.wsadminpuntos.WSAdminPuntos;
import com.epcs.recursoti.configuracion.DateHelper;
import com.epcs.recursoti.configuracion.MiEntelBusinessHelper;
import com.epcs.recursoti.configuracion.MiEntelProperties;
import com.epcs.recursoti.configuracion.Utils;
import com.epcs.recursoti.configuracion.locator.WebServiceLocator;
import com.epcs.recursoti.configuracion.locator.WebServiceLocatorException;
import com.epcs.recursoti.excepcion.DAOException;
import com.epcs.recursoti.excepcion.ServiceException;
import com.epcs.vtasymktg.fidelizacion.VtasYMktgFidelizacionService;
import com.epcs.vtasymktg.fidelizacion.VtasYMktgFidelizacionServicePortType;
import com.epcs.vtasymktg.fidelizacion.types.CanjeDePuntosSSCCPPType;
import com.epcs.vtasymktg.fidelizacion.types.CanjearPuntosSSCCPPType;
import com.epcs.vtasymktg.fidelizacion.types.ConsultarPuntosSSCCPPType;
import com.epcs.vtasymktg.fidelizacion.types.PuntosSSCCPPType;
import com.epcs.vtasymktg.fidelizacion.types.RegPuntosSSCCType;
import com.epcs.vtasymktg.fidelizacion.types.RegalarPuntosSSCCType;
import com.epcs.vtasymktg.fidelizacion.types.ResultadoCanjeDePuntosSSCCPPType;
import com.epcs.vtasymktg.fidelizacion.types.ResultadoConsultarPuntosSSCCPPType;
import com.epcs.vtasymktg.fidelizacion.types.ResultadoRegalarPuntosSSCCType;
import com.epcs.vtasymktg.fidelizacion.types.ResultadoValidacionRutMovilType;
import com.epcs.vtasymktg.fidelizacion.types.ValidacionRutMovilType;
import com.esa.ventas.programasfidelizacion.consultabonos.ConsultaBonos;
import com.esa.ventas.programasfidelizacion.consultabonos.ConsultaPuntosCampanaResponse;
import com.esa.ventas.programasfidelizacion.consultabonos.ConsultarBonoPromocionService;
import com.esa.ventas.programasfidelizacion.consultabonos.ConsultarPuntosPromocionalesRequest;
import com.esa.ventas.programasfidelizacion.consultabonos.ConsultarPuntosPromocionalesResponse;


/**
 * @author jivasquez (I2B) en nombre de Absalon Opazo (Atencion al Cliente, EntelPcs)
 *
 */
public class VtasYMktgFidelizacionDAO {
    

    /**
     * Logger para VtasYMktgFidelizacionDAO
     */
    private static final Logger LOGGER = Logger.getLogger(VtasYMktgFidelizacionDAO.class);
    public static final String CODIGO_RESPUESTA_OK = MiEntelProperties
            .getProperty("servicios.codigoRespuesta.exito");
    
    public static final String PLATAFORMA_SS = MiEntelProperties
    .getProperty("zonaEntel.regalarBolsas.plataforma.suscripcion");

    public static final String PLATAFORMA_CC = MiEntelProperties
    .getProperty("zonaEntel.regalarBolsas.plataforma.cuentacontrolada");
    
    public static final String ZONA_VERANO_CODIGO_RESPUESTA_OK = MiEntelProperties
    .getProperty("servicios.codigoRespuesta.zonaVerano.exito");
    
    public static final String ZONA_VERANO_CODIGO_RESPUESTA_OBSERVACION = MiEntelProperties
    .getProperty("servicios.codigoRespuesta.zonaVerano.observacio");
    
    public static final String ZONA_VERANO_CODIGO_RESPUESTA_CLAVE_INVALIDA = MiEntelProperties
    .getProperty("servicios.codigoRespuesta.zonaVerano.claveInvalida");
    
    
    // Constantes para catalogo
    public static final String BOLSA_OTROS = MiEntelProperties.getProperty("zonaEntel.catalogo.operacion.bolsaOtros");
	public static final String BOLSA_SIMISMO = MiEntelProperties.getProperty("zonaEntel.catalogo.operacion.bolsaSiMismo");
	public static final String RECARGA_OTROS = MiEntelProperties.getProperty("zonaEntel.catalogo.operacion.recargaOtros");
	public static final String RECARGA_SIMISMO = MiEntelProperties.getProperty("zonaEntel.catalogo.operacion.recargaSiMismo");
	
	public static final String BOLSA_VOZ = MiEntelProperties.getProperty("zonaEntel.canjeDePuntos.bolsaVoz");
	public static final String BOLSA_VOZ_R = MiEntelProperties.getProperty("zonaEntel.canjeDePuntos.bolsaVozRegalar");
	public static final String BOLSA_SMS = MiEntelProperties.getProperty("zonaEntel.canjeDePuntos.bolsaSMS");
	public static final String BOLSA_SMS_R = MiEntelProperties.getProperty("zonaEntel.canjeDePuntos.bolsaSMSRegalar");
	public static final String BOLSA_BAM = MiEntelProperties.getProperty("zonaEntel.canjeDePuntos.bolsaBAM");
	public static final String BOLSA_BAM_R = MiEntelProperties.getProperty("zonaEntel.canjeDePuntos.bolsaBAMRegalar");
	public static final String BOLSA_MIXTA = MiEntelProperties.getProperty("zonaEntel.canjeDePuntos.bolsaMixta");
	public static final String BOLSA_MIXTA_R = MiEntelProperties.getProperty("zonaEntel.canjeDePuntos.bolsaMixtaRegalar");
	public static final String BOLSA_IM = MiEntelProperties.getProperty("zonaEntel.canjeDePuntos.bolsaIM");
	public static final String BOLSA_IM_R = MiEntelProperties.getProperty("zonaEntel.canjeDePuntos.bolsaIMRegalar");
	
	public static final String BOLSA_BAMCC = MiEntelProperties.getProperty("zonaEntel.canjeDePuntos.bolsaBAMCC");
	public static final String BOLSA_BAMCC_R = MiEntelProperties.getProperty("zonaEntel.canjeDePuntos.bolsaBAMCC_R");
	public static final String BOLSA_BAMPP = MiEntelProperties.getProperty("zonaEntel.canjeDePuntos.bolsaBAMPP");
	public static final String BOLSA_BAMPP_R = MiEntelProperties.getProperty("zonaEntel.canjeDePuntos.bolsaBAMPP_R");	

	public static final String VIGENTE = "1";

    
    
    /**
     * Obtiene Informacion de los puntos de un usuario
     * 
     * @param rutSinDV rut sin digito validador
     * @return {@link ResultadoConsultarPuntosBean} con el detalle de los puntos
     * @throws ServiceException
     * @throws DAOException
     */
    public ResultadoConsultarPuntosBean consultarPuntos (
            String rutSinDV) throws DAOException, ServiceException {
        
        VtasYMktgFidelizacionServicePortType port = null;
        LOGGER.info("Instanciando el port " + VtasYMktgFidelizacionServicePortType.class);
        try {
            port = (VtasYMktgFidelizacionServicePortType) WebServiceLocator.getInstance().getPort(
                    VtasYMktgFidelizacionService.class, VtasYMktgFidelizacionServicePortType.class);
        } catch (WebServiceLocatorException e) {
            LOGGER.error("Error al inicializar el Port " + VtasYMktgFidelizacionServicePortType.class, e);
            LOGGER.error( new DAOException(e));
        }
        
        ConsultarPuntosSSCCPPType request = new ConsultarPuntosSSCCPPType();
        ResultadoConsultarPuntosSSCCPPType response = null;

        try {

            LOGGER.info("Configurando Datos de la peticion");
            request.setRutSinDV(rutSinDV);
            String identificacion = DateHelper.format(new Date(), "MMdd");
            String transaccion = DateHelper.format(new Date(), "yyyyMMddHHmmss");
            request.setIdentificacion(identificacion);
            request.setTransaccion(transaccion);

            LOGGER.info("Invocando servicio");
            response = port.consultarPuntosSSCCPP(request);

        } catch (Exception e) {
            LOGGER.error("Exception caught on Service invocation: consultarPuntosSSCCPP",
                    e);
            LOGGER.error( new DAOException(e));
        }

        String codigoRespuesta = response.getRespuesta().getCodigo();
        String descripcionRespuesta = response.getRespuesta().getDescripcion();

        LOGGER.info("codigoRespuesta " + codigoRespuesta);
        LOGGER.info("descripcionRespuesta " + descripcionRespuesta);

        if (Utils.isEmptyString(codigoRespuesta)) {
            LOGGER.error( new DAOException("consultarPuntosSSCCPP: Servicio no responde "
                    + "con codigoRespuesta"));
        }

        //Se inicializan los objetos para la respuesta.
        ResultadoConsultarPuntosBean resultado = new ResultadoConsultarPuntosBean();
        PuntosBean puntos = new PuntosBean();
        
        if (CODIGO_RESPUESTA_OK.equals(codigoRespuesta)) {

            try {
                PuntosSSCCPPType puntosType = response.getPuntos();
                if(puntosType == null){
                    LOGGER.info("el rut "+rutSinDV+" no tiene puntos asociados");
                }
                else{
                    //Configuramos objeto de respuesta
                    puntos.setFechaActPuntos(DateHelper.parseDate(
                            this.editarStringFecha(puntosType.getFechaActPuntos()),
                            DateHelper.FORMAT_yyyyMMdd_HYPHEN));
                    puntos.setFechaVencPuntos(DateHelper.parseDate(
                            this.editarStringFecha(puntosType.getFechaVencPuntos()),
                            DateHelper.FORMAT_yyyyMMdd_HYPHEN));
                    puntos.setPuntosVencidos((Integer)Utils.parseNumber(puntosType.getPuntosVencidos(),1));
                    puntos.setSaldoPuntos((Integer)Utils.parseNumber(puntosType.getSaldoPuntos(),1));
                }
                
            } catch (Exception e) {
                LOGGER.error("Exception caught on Service response: "
                        + "consultarPuntosSSCCPP", e);
                LOGGER.error( new DAOException(e));
            }
            
            resultado.setPuntos(puntos);

        }
        else {
            LOGGER.error("consultarPuntosSSCCPP: Service error code received: "
                    + codigoRespuesta + " - " + descripcionRespuesta);
            LOGGER.error( new ServiceException(codigoRespuesta, descripcionRespuesta));
        }
        
        return resultado;
    }
    
    /**
     * Obtiene el historial de caje de puntos de un usuario
     * 
     * @param rutSinDV
     * @param periodo
     * @return {@link ResultadoObtenerHistorialBean} con el historial de canje puntos de un usuario
     * @throws ServiceException
     * @throws DAOException
     */
    public ResultadoObtenerHistorialBean obtenerHistorial(String rutSinDV, Integer periodo) 
            throws DAOException, ServiceException{
        
        ResultadoObtenerHistorialBean resultado = new ResultadoObtenerHistorialBean();
        /*
        VtasYMktgFidelizacionServicePortType port;
        LOGGER.info("Instanciando el port " + VtasYMktgFidelizacionServicePortType.class);
        try {
            port = (VtasYMktgFidelizacionServicePortType) WebServiceLocator.getInstance().getPort(
                    VtasYMktgFidelizacionService.class, VtasYMktgFidelizacionServicePortType.class);
        } catch (WebServiceLocatorException e) {
            LOGGER.error("Error al inicializar el Port " + VtasYMktgFidelizacionServicePortType.class, e);
            LOGGER.error( new DAOException(e));
        }
        
        
        ObtenerHistorialSSCCPPType request = new ObtenerHistorialSSCCPPType();
        ResultadoObtenerHistorialSSCCPPType response;

        try {

            LOGGER.info("Configurando Datos de la peticion");
            request.setRutSinDv(rutSinDV);
            request.setPeriodo(Integer.toString(periodo));
            

            LOGGER.info("Invocando servicio");
            response = port.obtenerHistorialSSCCPP(request);

        } catch (Exception e) {
            LOGGER.error("Exception caught on Service invocation: obtenerHistorialSSCCPP",
                    e);
            LOGGER.error( new DAOException(e));
        }

        String codigoRespuesta = response.getRespuesta().getCodigo();
        String descripcionRespuesta = response.getRespuesta().getDescripcion();

        LOGGER.info("codigoRespuesta " + codigoRespuesta);
        LOGGER.info("descripcionRespuesta " + descripcionRespuesta);

        if (Utils.isEmptyString(codigoRespuesta)) {
            throw new DAOException("obtenerHistorialSSCCPP: Servicio no responde "
                    + "con codigoRespuesta");
        }

        if (CODIGO_RESPUESTA_OK.equals(codigoRespuesta)) {

            try {

                //Extraemos informacion de la respuesta del servicio
                DetalleSSCCPPType detalleType = response.getDetalle();
                List<HistorialDetalleSSCCPPType> historialType = response.getHistorial();
                
                if(historialType == null || historialType.isEmpty()){
                    LOGGER.info("No hay informacion historica");
                }
                
                //Inicializamos objetos para armar la respuesta del metodo
                DetalleBean detalle = new DetalleBean();
                List<HistorialDetalleBean> historial = new LinkedList<HistorialDetalleBean>();
                
                detalle.setFechaActual(DateHelper.parseDate(
                        this.editarStringFecha(detalleType.getFechaActual()), 
                        DateHelper.FORMAT_yyyyMMdd_HYPHEN));
                detalle.setFechaVencimiento(DateHelper.parseDate(
                        this.editarStringFecha(detalleType.getFechaVencimiento()),
                        DateHelper.FORMAT_yyyyMMdd_HYPHEN));
                detalle.setTotalPuntos((Integer)Utils.parseNumber(detalleType.getTotalPuntos(),1));
                detalle.setVencimientoPuntos((Integer)Utils.parseNumber(detalleType.getVencimientoPuntos(),1));
                
                for(HistorialDetalleSSCCPPType reg : historialType){
                    HistorialDetalleBean regHistorial = new HistorialDetalleBean();
                    regHistorial.setAbono((Double)Utils.parseNumber(reg.getAbono(),3));
                    regHistorial.setCanje(reg.getCanje());
                    regHistorial.setDescripcion(reg.getDescripcion());
                    regHistorial.setFechaTransaccion(DateHelper.parseDate(
                            this.editarStringFecha(reg.getFechaTransaccion()), 
                            DateHelper.FORMAT_yyyyMMdd_HYPHEN));
                    regHistorial.setNuevoSaldo((Double)Utils.parseNumber(reg.getNuevoSaldo(),3));
                    
                    historial.add(regHistorial);
                }
                
                resultado.setDetalle(detalle);
                resultado.setHistorial(historial);

            } catch (Exception e) {
                LOGGER.error("Exception caught on Service response: "
                        + "obtenerHistorialSSCCPP", e);
                LOGGER.error( new DAOException(e));
            }

        }
        else {
            LOGGER.error("obtenerHistorialSSCCPP: Service error code received: "
                    + codigoRespuesta + " - " + descripcionRespuesta);
            LOGGER.error( new ServiceException(codigoRespuesta, descripcionRespuesta));
        }
        */
        return resultado;
    }
    
    /**
     * Realiza la operacion de canje de puntos de un usuario
     * @param msisdn numeroPcs al que se va hacer la recarga o al que se van adjudicar las bolsas
     * @param rut rut del usuario en sesion
     * @param monto monto de la recarga
     * @return {@link ResultadoCanjeDePuntosBean} con el detalle de la operacion
     * @throws DAOException
     * @throws ServiceException
     */
    public ResultadoCanjeDePuntosBean canjearPuntos (
            String msisdn, String rut, String monto) throws DAOException, ServiceException {
        String transaccion = null;
        String idTransaccion = null;
        
        VtasYMktgFidelizacionServicePortType port = null;
        LOGGER.info("Instanciando el port " + VtasYMktgFidelizacionServicePortType.class);
        try {
            port = (VtasYMktgFidelizacionServicePortType) WebServiceLocator.getInstance().getPort(
                    VtasYMktgFidelizacionService.class, VtasYMktgFidelizacionServicePortType.class);
        } catch (WebServiceLocatorException e) {
            LOGGER.error("Error al inicializar el Port " + VtasYMktgFidelizacionServicePortType.class, e);
            LOGGER.error( new DAOException(e));
        }
        
        CanjearPuntosSSCCPPType request = new CanjearPuntosSSCCPPType();
        ResultadoCanjeDePuntosSSCCPPType response = null;

        try {

            LOGGER.info("Configurando Datos de la peticion");
            transaccion = DateHelper.format(new Date(), "MMdd");
            idTransaccion = DateHelper.format(new Date(), "yyyyMMddHHmmss");
            
            request.setIdTransaccion(idTransaccion);
            request.setMonto(monto);
            request.setMsisdn(msisdn);
            request.setRut(rut);
            request.setTransaccion(transaccion);
            
            LOGGER.info("Invocando servicio");
            response = port.canjearPuntosSSCCPP(request);
            LOGGER.info("Servicio respondio");

        } catch (Exception e) {
            LOGGER.error("Exception caught on Service invocation: canjearPuntosSSCCPP",
                    e);
            LOGGER.error( new DAOException(e));
        }

        String codigoRespuesta = response.getRespuesta().getCodigo();
        String descripcionRespuesta = response.getRespuesta().getDescripcion();

        LOGGER.info("codigoRespuesta " + codigoRespuesta);
        LOGGER.info("descripcionRespuesta " + descripcionRespuesta);

        if (Utils.isEmptyString(codigoRespuesta)) {
            LOGGER.error( new DAOException("canjearPuntosSSCCPP: Servicio no responde "
                    + "con codigoRespuesta"));
        }

        //Se inicializa el objeto a devolver por este metodo.
        ResultadoCanjeDePuntosBean resultado = new ResultadoCanjeDePuntosBean();
        
        if (CODIGO_RESPUESTA_OK.equals(codigoRespuesta)) {

            try {
                CanjeDePuntosSSCCPPType canjeType = response.getCanje();
                CanjeDePuntosBean canje = new CanjeDePuntosBean();
                
                //Extrayendo datos de la respuesta
                if(Utils.isEmptyString(canjeType.getCodAutorizacion()) || 
                		canjeType.getCodAutorizacion().trim().equals("0")){
                    LOGGER.error("El servicio no devolvio codigo de autorizacion: "
                            + "canjearPuntosSSCCPP - " 
                            + "\n Codigo de Auto :"+canjeType.getCodAutorizacion() 
                            + "\n msisdn :"+msisdn+" - " 
                            + "\n rut :"+rut + " - "
                            + "\n monto :"+monto + " - "
                            + "\n Transaccion :"+transaccion+" - "
                            + "\n idTransaccion :"+idTransaccion+" - ");
                    
                    LOGGER.error( new DAOException("El servicio no devolvio codigo de autorizacion"));
                }
                else {
                    canje.setCodAutorizacion(canjeType.getCodAutorizacion());
                    canje.setDiasDeValidez((Integer)Utils.parseNumber(canjeType.getDiasDeValidez(),1));
                    canje.setSaldoPuntos((Double)Utils.parseNumber(canjeType.getSaldoPuntos(),3));
                    canje.setStatusRespuestaComercio(canjeType.getStatusRespuestaComercio());
                    canje.setFechaCanje(new Date());
                    resultado.setCanje(canje);
                }
                
                
            } catch (Exception e) {
                LOGGER.error("Exception caught on Service response: "
                        + "canjearPuntosSSCCPP", e);
                LOGGER.error( new DAOException(e));
            }

        }
        else {
            LOGGER.error("canjearPuntosSSCCPP: Service error code received: "
                    + codigoRespuesta + " - " + descripcionRespuesta);
            LOGGER.error( new ServiceException(codigoRespuesta, descripcionRespuesta));
        }
        
        return resultado;
    }
    
    /**
     * Realiza la operacion de regalo de puntos de un usuario
     * @param msisdn numeroPcs al que se va hacer la recarga o al que se van adjudicar las bolsas
     * @param rut rut del usuario en sesion
     * @param monto monto de la recarga
     * @return {@link ResultadoCanjeDePuntosBean} con el detalle de la operacion
     * @throws DAOException
     * @throws ServiceException
     */
    public ResultadoRegalarPuntosBean regalarPuntos (
            String msisdnSponsor, String msisdnRecibe, String rutSinDV, String monto, String mercado) throws DAOException, ServiceException {
        String transaccion = null;
        String idTransaccion = null;
        String plataforma;
        
        VtasYMktgFidelizacionServicePortType port = null;
        LOGGER.info("Instanciando el port " + VtasYMktgFidelizacionServicePortType.class);
        try {
            port = (VtasYMktgFidelizacionServicePortType) WebServiceLocator.getInstance().getPort(
                    VtasYMktgFidelizacionService.class, VtasYMktgFidelizacionServicePortType.class);
        } catch (WebServiceLocatorException e) {
            LOGGER.error("Error al inicializar el Port " + VtasYMktgFidelizacionServicePortType.class, e);
            LOGGER.error( new DAOException(e));
        }
        
        RegalarPuntosSSCCType request = new RegalarPuntosSSCCType();
        ResultadoRegalarPuntosSSCCType response = null;        

        try {

            LOGGER.info("Configurando Datos de la peticion");
            transaccion = DateHelper.format(new Date(), "MMdd");
            idTransaccion = DateHelper.format(new Date(), "yyyyMMddHHmmss");
            plataforma = MiEntelBusinessHelper.getSiglaSuscripcion().equals(mercado) ? PLATAFORMA_SS : PLATAFORMA_CC;
            
            request.setIdTransaccion(idTransaccion);
            request.setMonto(monto);
            request.setMsisdnSponsor(msisdnSponsor);
            request.setMsisdnRecibe(msisdnRecibe);
            request.setRut(rutSinDV);
            request.setTransaccion(transaccion);
            request.setPlataforma(plataforma);
            
            LOGGER.info("Invocando servicio");
            response = port.regalarPuntosSSCC(request);

        } catch (Exception e) {
            LOGGER.error("Exception caught on Service invocation: regalarPuntos",
                    e);
            LOGGER.error( new DAOException(e));
        }

        String codigoRespuesta = response.getRespuesta().getCodigo();
        String descripcionRespuesta = response.getRespuesta().getDescripcion();

        LOGGER.info("codigoRespuesta " + codigoRespuesta);
        LOGGER.info("descripcionRespuesta " + descripcionRespuesta);

        if (Utils.isEmptyString(codigoRespuesta)) {
            LOGGER.error( new DAOException("regalarPuntos: Servicio no responde "
                    + "con codigoRespuesta"));
        }

        //Se inicializa el objeto a devolver por este metodo.
        ResultadoRegalarPuntosBean regalar = null;
        
        if (CODIGO_RESPUESTA_OK.equals(codigoRespuesta)) {

            try {
                RegPuntosSSCCType canjeType = response.getRegPuntos();                
                
                //Extrayendo datos de la respuesta
                if(Utils.isEmptyString(canjeType.getCodAutorizacion()) || 
                		canjeType.getCodAutorizacion().trim().equals("0")){
                    LOGGER.error("El servicio no devolvio codigo de autorizacion (REGALAR): "
                            + "regalarPuntosSSCCPP - " 
                            + "\n Codigo de Auto :"+canjeType.getCodAutorizacion() 
                            + "\n msisdnSponsor :"+msisdnSponsor+" - "
                            + "\n msisdnRecibe :"+msisdnRecibe+" - "
                            + "\n rut :"+rutSinDV + " - "
                            + "\n monto :"+monto + " - "
                            + "\n Transaccion :"+transaccion+" - "
                            + "\n idTransaccion :"+idTransaccion+" - ");
                    
                    LOGGER.error( new DAOException("El servicio no devolvio codigo de autorizacion"));
                }
                else {
                	regalar = new ResultadoRegalarPuntosBean();
                    regalar.setCodAutorizacion(canjeType.getCodAutorizacion());
                    regalar.setDiasDeValidez((Integer)Utils.parseNumber(canjeType.getDiasDeValidez(),1));
                    regalar.setSaldoPuntos((Double)Utils.parseNumber(canjeType.getSaldoPuntos(),3));
                    regalar.setStatusRespuestaComercio(canjeType.getStatusRespuestaComercio());
                    Date fechaRecarga = new Date();
                    regalar.setFechaRecarga(DateHelper.format(fechaRecarga,"dd/MM/yyyy hh.mm 'hrs'"));
                    Date validez = DateHelper.addDays(fechaRecarga, Integer.parseInt(canjeType.getDiasDeValidez()));
                    regalar.setFechaValidez(DateHelper.format(validez,"dd/MM/yyyy hh.mm 'hrs'"));
                }
                
                
            } catch (Exception e) {
                LOGGER.error("Exception caught on Service response: "
                        + "regalarPuntos", e);
                LOGGER.error( new DAOException(e));
            }

        }
        else {
            LOGGER.error("regalarPuntos: Service error code received: "
                    + codigoRespuesta + " - " + descripcionRespuesta);
            LOGGER.error( new ServiceException(codigoRespuesta, descripcionRespuesta));
        }
        
        return regalar;
    }
    
    public String validarAsociacionRutMovil(String msisdn, String rutSinDV) throws ServiceException, DAOException{
    	
    	String asociacionMovilRut = "";
    	
    	VtasYMktgFidelizacionServicePortType port = null;
        LOGGER.info("Instanciando el port " + VtasYMktgFidelizacionServicePortType.class);
        try {
            port = (VtasYMktgFidelizacionServicePortType) WebServiceLocator.getInstance().getPort(
                    VtasYMktgFidelizacionService.class, VtasYMktgFidelizacionServicePortType.class);
        } catch (WebServiceLocatorException e) {
            LOGGER.error("Error al inicializar el Port " + VtasYMktgFidelizacionServicePortType.class, e);
            LOGGER.error( new DAOException(e));
        }
        
        ValidacionRutMovilType request = new ValidacionRutMovilType();
        ResultadoValidacionRutMovilType response = null;

        try {

            LOGGER.info("Configurando Datos de la peticion");
            request.setMsisdn(msisdn);
            request.setRut(rutSinDV);
            
            LOGGER.info("Invocando servicio");
            response = port.validacionRutMovil(request);

        } catch (Exception e) {
            LOGGER.error("Exception caught on Service invocation: validacionRutMovil",
                    e);
            LOGGER.error( new DAOException(e));
        }

        String codigoRespuesta = response.getRespuesta().getCodigo();
        String descripcionRespuesta = response.getRespuesta().getDescripcion();

        LOGGER.info("codigoRespuesta " + codigoRespuesta);
        LOGGER.info("descripcionRespuesta " + descripcionRespuesta);

        if (Utils.isEmptyString(codigoRespuesta)) {
            LOGGER.error( new DAOException("validacionRutMovil: Servicio no responde "
                    + "con codigoRespuesta"));
        }
        
        if (CODIGO_RESPUESTA_OK.equals(codigoRespuesta)) {

            try {
                asociacionMovilRut = response.getAsociacionMovilRut();                
                
                if(Utils.isEmptyString(asociacionMovilRut)){
                    LOGGER.error("respuesta Asociacion Rut Movil invalida: "
                            + "validacionRutMovil - " 
                            + "\n msisdnRecibe :"+msisdn+" - "
                            + "\n rut :"+rutSinDV);
                    
                    LOGGER.error( new DAOException("respuesta Asociacion Rut Movil invalida"));
                }
                
            } catch (Exception e) {
                LOGGER.error("Exception caught on Service response: "
                        + "validacionRutMovil", e);
                LOGGER.error( new DAOException(e));
            }

        }
        else {
            LOGGER.error("validacionRutMovil: Service error code received: "
                    + codigoRespuesta + " - " + descripcionRespuesta);
            LOGGER.error( new ServiceException(codigoRespuesta, descripcionRespuesta));
        }
        
        return asociacionMovilRut;
    }
    
    /**
     * 
     * @param canal
     * @param mercado  
     * @throws DAOException
     * @throws ServiceException
     */
    public MatrizCatalogoCanjeBean getMatrizCatalogo(int canal, int mercado) throws ServiceException, DAOException{    	    
    	
    	IPuntos port = null;
        LOGGER.info("Instanciando el port " + IPuntos.class);
        try {
            port = (IPuntos) WebServiceLocator.getInstance().getPort(
            		WSAdminPuntos.class, IPuntos.class);
        } catch (WebServiceLocatorException e) {
            LOGGER.error("Error al inicializar el Port " + IPuntos.class, e);
            LOGGER.error( new DAOException(e));
        }
                
        MatrizResponse response;        
        MatrizCatalogoCanjeBean matrizBean = new MatrizCatalogoCanjeBean();
        
        try {

            LOGGER.info("Configurando Datos de la peticion");                     
            LOGGER.info("Invocando servicio");
            response = port.catalogoCanje(canal, mercado);                          
            
            for(ItemMatrizResponse item : response.getItemMatriz()){
            	
            	ItemMatrizCanjeBean imc = new ItemMatrizCanjeBean();
            	imc.setTipo(item.getTipo());
            	imc.setListDetalles(new ArrayList<ItemDetalleCanjeBean>());
            	
            	for(DetalleResponse item2 : item.getDetalle()){
            		
            		ItemDetalleCanjeBean idc = new ItemDetalleCanjeBean();            		
            		idc.setHashMercados(new HashMap<String, ItemMercadoCanjeBean>());
            		
            		for(MercadoResponse item3 : item2.getMercado()){
            			
            			ItemMercadoCanjeBean imer = new ItemMercadoCanjeBean();
            			imer.setId(item3.getId());
            			imer.setHashProductos(new HashMap<String, ItemProductoCanjeBean>());
            			
            			for(ProductoResponse item4 : item3.getProducto()){            				            				
            				
            				ItemProductoCanjeBean ipc = new ItemProductoCanjeBean();
            				ipc.setCodBolsa(item4.getCodiBolsa());
            				ipc.setCodProducto(item4.getCodiProducto());
            				ipc.setDescProducto(item4.getDescProducto());
            				ipc.setLetraChica(item4.getDescDetalleProd());
            				ipc.setMonto(item4.getCantPesos());
            				ipc.setNombreUsuario(item4.getNombUsuario());
            				ipc.setPuntos(item4.getCantPuntosCanje());
            				ipc.setTipoProducto(item4.getTipoProducto());
            				ipc.setVigente(VIGENTE.equals(item4.getIndcVigencia()+""));            				
            				/*
            				ipc.setFechaFinVigencia(fechaFinVigencia);
            				ipc.setFechaIniVigencia(fechaIniVigencia);
            				*/
            				
            				imer.getHashProductos().put(ipc.getMonto()+"",ipc);
            				
            				if(imc.getTipo().equals(BOLSA_OTROS) || imc.getTipo().equals(BOLSA_SIMISMO)){            		            
                        		if(ipc.getTipoProducto().equals(BOLSA_VOZ) || ipc.getTipoProducto().equals(BOLSA_VOZ_R)){
                        			matrizBean.getBolsasVoz().add(ipc);
                        		}else if(ipc.getTipoProducto().equals(BOLSA_SMS) || ipc.getTipoProducto().equals(BOLSA_SMS_R)){
                        			matrizBean.getBolsasSMS().add(ipc);
                        		}else if(ipc.getTipoProducto().equals(BOLSA_BAM) || ipc.getTipoProducto().equals(BOLSA_BAM_R)){
                        			matrizBean.getBolsasBAM().add(ipc);
                        		}else if(ipc.getTipoProducto().equals(BOLSA_MIXTA) || ipc.getTipoProducto().equals(BOLSA_MIXTA_R)){
                        			matrizBean.getBolsasMixtas().add(ipc);
                        		}else if(ipc.getTipoProducto().equals(BOLSA_IM) || ipc.getTipoProducto().equals(BOLSA_IM_R)){
                        			matrizBean.getBolsasInternetMovil().add(ipc);
                        		}else if(ipc.getTipoProducto().equals(BOLSA_BAMCC) || ipc.getTipoProducto().equals(BOLSA_BAMCC_R) || 
                        				 ipc.getTipoProducto().equals(BOLSA_BAMPP) || ipc.getTipoProducto().equals(BOLSA_BAMPP_R)){
                        			matrizBean.getBolsasCanjeBAM().add(ipc);
                        		}
            				}else if(imc.getTipo().equals(RECARGA_OTROS) || imc.getTipo().equals(RECARGA_SIMISMO)){
            					matrizBean.getRecargas().add(ipc);
            				}
            				
            			}  
            			
            			idc.getHashMercados().put(imer.getId(), imer);
            		}            		
            		imc.getListDetalles().add(idc);            		
            	}
            	            	
            	matrizBean.getMatrizCatalogo().put(imc.getTipo(), imc);            	
				            	            	
            }
            
            LOGGER.info("Finalizo obtener matriz");

        } catch (Exception e) {
            LOGGER.error("Error obteniendo matriz de catalogo.", e);
            LOGGER.error( new DAOException(e));
        }

        
        return matrizBean;
    }
    
    /**
     * 
     * @param rut     
     * @throws DAOException
     * @throws ServiceException
     */
   
    public List<ExpiracionProximaPuntosBean> getExpiracionProximaPuntos(int rut) throws ServiceException, DAOException{    	        
    	    	
    	List<ExpiracionProximaPuntosBean> listaExpiracion = new ArrayList<ExpiracionProximaPuntosBean>();     	    	  	    
    	
    	IConsultaPuntosPorVencer port = null;
        LOGGER.info("Instanciando el port " + IConsultaPuntosPorVencer.class);
        try {
            port = (IConsultaPuntosPorVencer) WebServiceLocator.getInstance().getPort(
            		ConsultaPuntosPorVencer.class, IConsultaPuntosPorVencer.class);
        } catch (WebServiceLocatorException e) {
            LOGGER.error("Error al inicializar el Port " + IConsultaPuntosPorVencer.class, e);
            LOGGER.error( new DAOException(e));
        }
                
        ConsultaPuntosPorVencerResponse response = null;                
        
        try {

            LOGGER.info("Configurando Datos de la peticion");                     
            LOGGER.info("Invocando servicio");
            
            response = port.consulta(rut);  
        
            LOGGER.info("Finalizo obtener puntos por vencer");
        } catch (Exception e) {
            LOGGER.error("Error obteniendo puntos por vencer.", e);
            LOGGER.error( new DAOException(e));
        }    
        
        if(response !=null && (response.getCodRespuesta()+"").equals("0")){
        	try{
            	List<VencimientoDetalle> listaVencimientos = response.getVencimiento().getVencimientoDetalle();            
                                
                for(VencimientoDetalle vd : listaVencimientos){            	            	
                	
                	ExpiracionProximaPuntosBean ep = new ExpiracionProximaPuntosBean();
                	ep.setPeriodoVencimiento(vd.getPeriodoVencimiento());
                	ep.setPuntos(vd.getPuntosPorVencer().intValueExact());
                	ep.setPuntosStringFormat(Utils.formatPuntos(vd.getPuntosPorVencer().intValueExact(), Locale.GERMAN));
                	
                	listaExpiracion.add(ep);                                
                }
            } catch (Exception e) {
                LOGGER.error("Error obteniendo puntos por vencer.", e);
                LOGGER.error( new DAOException(e));
            }   
        }
        else{
        	LOGGER.error( new ServiceException(response.getCodRespuesta()+"", "Error de servicio al consultar expiracion de puntos"));
        }
    	
    	return listaExpiracion;
    	
    }

    
    /**
     * 
     * @param rut     
     * @throws DAOException
     * @throws ServiceException
     */
    public List<PaginaHistorialCanjeBean> getHistorialCartolaPuntos(int rut, int periodo, int tipo, int porPagina, String descAbonoPromocional) throws ServiceException, DAOException{    	        
    	    	
    	List<PaginaHistorialCanjeBean> listaPaginas = new ArrayList<PaginaHistorialCanjeBean>();     	    	  	       	    	
    	
    	IConsultaCartolaPuntosAnual port = null;
        LOGGER.info("Instanciando el port " + IConsultaCartolaPuntosAnual.class);
        try {
            port = (IConsultaCartolaPuntosAnual) WebServiceLocator.getInstance().getPort(
            		ConsultaCartolaPuntosAnual.class, IConsultaCartolaPuntosAnual.class);
        } catch (WebServiceLocatorException e) {
            LOGGER.error("Error al inicializar el Port " + IConsultaCartolaPuntosAnual.class, e);
            LOGGER.error( new DAOException(e));
        }
                
        ConsultaCartolaPuntosAnualResponse response = null;                
        
        try {

            LOGGER.info("Configurando Datos de la peticion");                     
            LOGGER.info("Invocando servicio");
            
            response = port.cartola(rut, periodo, tipo, porPagina); 
            LOGGER.info("Finalizo obtener historial cartola puntos");

        } catch (Exception e) {
            LOGGER.error("Error obteniendo historial cartola puntos.", e);
            LOGGER.error( new DAOException(e));
        }
        
        if(response !=null && (response.getCodRespuesta()+"").equals("0")){
        	try{
	        	if(response.getPaginas() != null){
	
	                List<TransaccionPagina> listaPaginasServicio = response.getPaginas().getTransaccionPagina();                 	                
	                	                
	                for(TransaccionPagina tp : listaPaginasServicio){ 
	                	PaginaHistorialCanjeBean pagina = new PaginaHistorialCanjeBean();
	                	List<HistorialDetalleBean> listaDetalle = new ArrayList<HistorialDetalleBean>();
	                	int totalPagina = 0;
	                	for(TransaccionDetalle td : tp.getTransaccion().getTransaccionDetalle()){
	                		
	                		HistorialDetalleBean detalleBean = new HistorialDetalleBean();
	                		detalleBean.setTipoTransaccion(getDescripcionTipoCanje(td.getTipoTransaccion()+""));
	                		
							if (!descAbonoPromocional.equals("")
									&& td.getDescripcionTransaccion().contains(descAbonoPromocional)) {
								detalleBean.setTransaccionAbonoPromocional(true);
							}
	                		
	                		detalleBean.setDescripcionTransaccion(td.getDescripcionTransaccion());
	                		detalleBean.setPuntosTransaccion(td.getPuntosTransaccion().intValue());
	                		detalleBean.setPuntosTransaccionStringFormat(Utils.formatPuntos(td.getPuntosTransaccion().intValue(), Locale.GERMAN));
	                		detalleBean.setSaldoTransaccion(td.getSaldoTransaccion().doubleValue());
	                		detalleBean.setSaldoTransaccionStringFormat(Utils.formatPuntos(td.getSaldoTransaccion().doubleValue(), Locale.GERMAN));
	                		detalleBean.setFechaTransaccion(td.getFechaTransaccion().toGregorianCalendar().getTime());	 
	                		detalleBean.setFechaTransaccionStringFormat(DateHelper.format(td.getFechaTransaccion().toGregorianCalendar().getTime(), 
	                										DateHelper.FORMAT_ddMMyyyy_HHmm_SLASH));	                		
	                		
	                		listaDetalle.add(detalleBean);
	                		totalPagina += Math.abs(td.getPuntosTransaccion().intValue());	                		
	                	}
	                	pagina.setListaDetalle(listaDetalle);
	                	pagina.setTotalPuntos(Utils.formatPuntos(totalPagina, Locale.GERMAN));
	                	
	                	listaPaginas.add(pagina);
	                }
	            }
	        } catch (Exception e) {
	            LOGGER.error("Error obteniendo historial cartola puntos.", e);
	            LOGGER.error( new DAOException(e));
	        }
        }
        else{            	
        	LOGGER.error( new ServiceException(response.getCodRespuesta()+"", "Error de servicio al consultar historial de puntos"));
        }
            	
    	return listaPaginas;
    	
    }
    
    
    /**
     * Obtenemos una subcadena con formato yyyy-MM-dd a partir del String recibido,
     * el cual tiene el siguiente formato 2011-09-01T00:00:00.
     * @param <code>fecha</code> la fecha a formatear
     * @return Devuelve una subcadena a partir de la cadena <code>fecha</code>
     */
    private String editarStringFecha(String fecha){
        int indexT = fecha.indexOf("T");
        return fecha.substring(0, indexT);
    }
    
    
    private String getDescripcionTipoCanje(String codigo){
    	try{
    		return MiEntelProperties.getProperty("parametros.zonaEntel.historialCanje.listado.tipo."+codigo+".desc");
    	}
    	catch(Exception e){
    		LOGGER.warn("Tipo de canje no encontrado en el archivo de properties. Codigo: "+codigo);
    		return "";
    	}    	
    }
    
  
   
  
    /**
     * 
     * @param canal
     * @param mercado  
     * @throws DAOException
     * @throws ServiceException
     */
    public String canjearPuntosZonaVerano(String rutCliente,String movil,String codProducto,String codStand,String claveStand,String puntosCanje) throws ServiceException, DAOException{    	    
    	ZonaVerano port = null;
        LOGGER.info("Instanciando el port " + ZonaVerano.class);
        try {
            port = (ZonaVerano) WebServiceLocator.getInstance().getPort(
            		ZonaVerano_Service.class, ZonaVerano.class);
        } catch (WebServiceLocatorException e) {
            LOGGER.error("Error al inicializar el Port " + ZonaVerano.class, e);
            LOGGER.error( new DAOException(e));
        }
            
        String codigoRespuesta = "";
        String descripcionRespuesta = ""; 
        
        try {
        	
        	Holder<Integer> cod = new Holder<Integer>();
        	Holder<String> mensaje = new Holder<String>();
        	
            LOGGER.info("Configurando Datos de la peticion");                     
            LOGGER.info("Invocando servicio");
           
            port.canjeZonaVerano(rutCliente, movil, codProducto, codStand, claveStand, puntosCanje, cod, mensaje);
            
            codigoRespuesta = cod.value+"";
            descripcionRespuesta = mensaje.value+"";
            
            LOGGER.info("Finalizo registro canje zona verano");

        } catch (Exception e) {
            LOGGER.error("Error registrando canje zona verano.", e);
            LOGGER.error( new DAOException(e));
        }
           
        
        if(!codigoRespuesta.equals(ZONA_VERANO_CODIGO_RESPUESTA_OK) && !codigoRespuesta.equals(ZONA_VERANO_CODIGO_RESPUESTA_OBSERVACION)){       	
        	LOGGER.info("Error registrando canje zona verano: Service error code received: " 
            		+ codigoRespuesta + " - " 
                    + descripcionRespuesta);
        	
        	 LOGGER.error( new ServiceException(codigoRespuesta, 
        			 descripcionRespuesta));       
       }

        
        return codigoRespuesta;
    }  
    
    /**
     * 
     * @param clave     *  
     * @throws DAOException
     * @throws ServiceException
     */
    public boolean validarClaveZonaVerano(String claveStand,String codigoStand) throws ServiceException, DAOException{    	    
    	boolean estado =true; 
    	ZonaVerano port = null;
		    LOGGER.info("Instanciando el port " + ZonaVerano.class);
		    try {
		        port = (ZonaVerano) WebServiceLocator.getInstance().getPort(
		        		ZonaVerano_Service.class, ZonaVerano.class);
		    } catch (WebServiceLocatorException e) {
		        LOGGER.error("Error al inicializar el Port " + ZonaVerano.class, e);
		        LOGGER.error( new DAOException(e));
		    }
   
        	String codigoRespuesta = "";
            String descripcionRespuesta = "";   
            try {
            	
            	Holder<Integer> cod = new Holder<Integer>();
            	Holder<String> mensaje = new Holder<String>();
            	
                LOGGER.info("Configurando Datos de la peticion");                     
                LOGGER.info("Invocando servicio");
            	
            	port.canjeZonaVerano("", "", "", codigoStand, claveStand, "", cod, mensaje);

                codigoRespuesta = cod.value+"";
                descripcionRespuesta = mensaje.value+"";
                LOGGER.info("Finalizo validacion de clave");

            } catch (Exception e) {
                LOGGER.error("Error validar clave zona verano.", e);
                LOGGER.error( new DAOException(e));
            }
            

            if(codigoRespuesta.equals(ZONA_VERANO_CODIGO_RESPUESTA_CLAVE_INVALIDA)){
            	estado = false;
            	LOGGER.info("Error validar clave zona verano: Service error code received: " 
                		+ codigoRespuesta + " - " 
                        + descripcionRespuesta);
            	
            	 LOGGER.error( new ServiceException(codigoRespuesta, 
            			 descripcionRespuesta));            
            }
        
        return estado;
    }   
    
    /**
     * 
     * @param  fecha     *  
     * @throws DAOException
     * @throws ServiceException
     */
    public String convertirFechaProducto(XMLGregorianCalendar fecha) throws ServiceException, DAOException{    	    
    	   String fechaProducto="";        
	       try {               
	    	   fecha.getDay();	    	   
	    	   fechaProducto = fecha.getDay()+" de "+  DateHelper.nombreMes(fecha.getMonth()) ;	           
	          } catch (Exception e) {
	            LOGGER.error("Error al convertir fecha del producto ", e);
	            LOGGER.error( new DAOException(e));
	       }     	
        return fechaProducto;
    }
    
    /**
     * 
     * @param  codigosZona
     * @param  codigoZona  
     * @throws DAOException
     * @throws ServiceException
     */
    public boolean filtoZonaPuntos(String codigosZona, String codigoZona) throws ServiceException, DAOException{ 
    	   String listaVector[];
    	   boolean sw=false;
    	   codigoZona  = codigoZona==null?"":codigoZona;
    	   codigosZona = codigosZona==null?"":codigosZona;
	       try {               
	    	   if(!codigosZona.equals("")){	
	 		      listaVector = codigosZona.split(","); 
	 	    	   for (int i=0; i< listaVector.length;i++ ) {
	 	    		  
	 	    		   if(codigoZona.equals(listaVector[i])){
	 	    			  sw=true;
	 	    		  } 
	 	    	   }
	    	   }   
	          } catch (Exception e) {
	            LOGGER.error("Error al filtrar zona verano", e);
	            LOGGER.error( new DAOException(e));
	       }     	
        return sw;
    }
   
    /**
     * 
     * @param canal
     * @param mercado  
     * @throws DAOException
     * @throws ServiceException
     */
    public List<ItemProductoCanjeBean> getMatrizCatalogoZonaVerano(int canal, int mercado,String codigosZona) throws ServiceException, DAOException{    	    
    	
    	IPuntos port = null;
    	List<ItemProductoCanjeBean> listaProductos = new ArrayList<ItemProductoCanjeBean>();
    	
        LOGGER.info("Instanciando el port " + IPuntos.class);
        try {
            port = (IPuntos) WebServiceLocator.getInstance().getPort(
            		WSAdminPuntos.class, IPuntos.class);
        } catch (WebServiceLocatorException e) {
            LOGGER.error("Error al inicializar el Port " + IPuntos.class, e);
            LOGGER.error( new DAOException(e));
        }
                
        MatrizResponse response;        
        MatrizCatalogoCanjeBean matrizBean = new MatrizCatalogoCanjeBean();
        
        try {

            LOGGER.info("Configurando Datos de la peticion");                     
            LOGGER.info("Invocando servicio");
            response = port.catalogoCanje(canal, mercado);                          
            
            for(ItemMatrizResponse item : response.getItemMatriz()){
            	
              if(filtoZonaPuntos(codigosZona,item.getTipo())){
            		
            	ItemMatrizCanjeBean imc = new ItemMatrizCanjeBean();
            	imc.setTipo(item.getTipo());
            	imc.setListDetalles(new ArrayList<ItemDetalleCanjeBean>());
            	
            	
            	for(DetalleResponse item2 : item.getDetalle()){
            		
            		ItemDetalleCanjeBean idc = new ItemDetalleCanjeBean();            		
            		idc.setHashMercados(new HashMap<String, ItemMercadoCanjeBean>());
            		
            		for(MercadoResponse item3 : item2.getMercado()){
            			
            			ItemMercadoCanjeBean imer = new ItemMercadoCanjeBean();
            			imer.setId(item3.getId());
            			imer.setHashProductos(new HashMap<String, ItemProductoCanjeBean>());
            			
            			for(ProductoResponse item4 : item3.getProducto()){            				            				
            				
            				ItemProductoCanjeBean ipc = new ItemProductoCanjeBean();
            				ipc.setCodBolsa(item4.getCodiBolsa());
            				ipc.setCodProducto(item4.getCodiProducto());
            				ipc.setDescProducto(item4.getDescProducto());
            				ipc.setLetraChica(item4.getDescDetalleProd());
            				ipc.setMonto(item4.getCantPesos());
            				ipc.setNombreUsuario(item4.getNombUsuario());
            				ipc.setPuntos(item4.getCantPuntosCanje());
            				ipc.setTipoProducto(item4.getTipoProducto());
            				ipc.setVigente(VIGENTE.equals(item4.getIndcVigencia()+""));            				
            				
            				imer.getHashProductos().put(ipc.getMonto()+"",ipc); 
            				
            				       try{  
	            				    	ipc.setFechaFinVigencia(convertirFechaProducto(item4.getFechFinVigencia()));
	                    				ipc.setFechaIniVigencia(convertirFechaProducto(item4.getFechIniVigencia()));  
	                    				ipc.setAnoVigencia(item4.getFechFinVigencia().getYear());
	                    				ipc.setCodZona(imc.getTipo());
                		            }catch(Exception e){
                		                   LOGGER.warn("Error Conviertiendo las fechas de vigencia zona verano");
                		            } 
                		            
                		            listaProductos.add(ipc); 
                		            //matrizBean.getBolsasZonaVerano().add(ipc);
            				
            			}  
            			
            			//idc.getHashMercados().put(imer.getId(), imer);
            		}            		
            		//imc.getListDetalles().add(idc);            		
            	}
            	//matrizBean.getMatrizCatalogo().put(imc.getTipo(), imc);  
              }            	            	
            }
            
            LOGGER.info("Finalizo obtener productos zona verano");

        } catch (Exception e) {
            LOGGER.error("Error obteniendo matriz de catalogo.", e);
            LOGGER.error( new DAOException(e));
        }

        
        return listaProductos;
    }  
    
    /**
     * Consulta los puntos promocionales de un usuario
     * @param codTrx
     * @param canal
     * @param rut
     * @return
     * @throws DAOException
     * @throws ServiceException
     */
    public PuntosBean consultarPuntosPromocion(Integer codTrx, Integer canal, String rut) throws DAOException, ServiceException {        
        ConsultaBonos port = null;    	
        LOGGER.info("Instanciando el port " + ConsultaBonos.class);
        
        try {
            port = (ConsultaBonos) WebServiceLocator.getInstance().getPort(
                    ConsultarBonoPromocionService.class, ConsultaBonos.class);
        } catch (WebServiceLocatorException e) {
            LOGGER.error("Error al inicializar el Port " + ConsultaBonos.class, e);
            LOGGER.error( new DAOException(e));
        }
        
        ConsultarPuntosPromocionalesRequest request = new ConsultarPuntosPromocionalesRequest();
        ConsultarPuntosPromocionalesResponse response = null;

        try {
            LOGGER.info("Configurando Datos de la peticion");
            request.setCodTrx(codTrx);
            request.setCanal(canal);
            request.setRut(Integer.parseInt(rut));

            LOGGER.info("Invocando servicio");
            response = port.consultarPuntosPromocionales(request);
        } catch (Exception e) {
            LOGGER.error("Exception caught on Service invocation: consultarPuntosPromocionales", e);
            LOGGER.error( new DAOException(e));
        }
        
        int codigoRespuesta = response.getReturn().getCodRespuesta();
        String descripcionRespuesta = response.getReturn().getDescRespuesta();

        LOGGER.info("codigoRespuesta " + codigoRespuesta);
        LOGGER.info("descripcionRespuesta " + descripcionRespuesta);        
        
        PuntosBean puntos = null;
        if (codigoRespuesta == 0) {
        	// Se inicializa el objeto para la respuesta.
            puntos = new PuntosBean();
            try {
				ConsultaPuntosCampanaResponse puntosCampana = response.getReturn();
				if (puntosCampana == null) {
					puntos.setEstadoPromocion("NO");
					LOGGER.info("el rut " + rut + " no tiene puntos asociados");
				} else {
					// Configuramos objeto de respuesta
					puntos.setEstadoPromocion("OK");
					puntos.setPuntosPromocion(puntosCampana.getValorBono());					
					puntos.setPuntosPromocionFormated(Utils.formatMoneyPuntos(new Double(puntosCampana.getValorBono())));

					int anoVenc = puntosCampana.getFVencimientoAno();
					int mesVenc = puntosCampana.getFVencimientoMes() > 0 ? puntosCampana.getFVencimientoMes() - 1 : puntosCampana.getFVencimientoMes();
					int diaVenc = puntosCampana.getFVencimientoDia();
					
					Calendar c = Calendar.getInstance();
					c.set(anoVenc, mesVenc, diaVenc);

					puntos.setFechaVencPromocion(c.getTime());
					puntos.setFechaVencPromoFormated(DateHelper.format(c
							.getTime(), DateHelper.FORMAT_ddMMyyyy_SLASH));
				}                
            } catch (Exception e) {
                LOGGER.error("Exception caught on Service response: "
                        + "consultarPuntosPromocionales", e);
                LOGGER.error( new DAOException(e));
            }
        } else {
            LOGGER.error("consultarPuntosPromocionales: Service error code received: "
                    + codigoRespuesta + " - " + descripcionRespuesta);
            LOGGER.error( new ServiceException(String.valueOf(codigoRespuesta), descripcionRespuesta));
        }
        
        return puntos;
    }
    
}
