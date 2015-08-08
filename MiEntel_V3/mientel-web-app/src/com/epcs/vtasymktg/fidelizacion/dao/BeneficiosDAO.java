/* Propiedad de Entel PCS. Todos los derechos reservados */
package com.epcs.vtasymktg.fidelizacion.dao;

import java.util.Date;

import org.apache.log4j.Logger;

import com.epcs.bean.ZonaPerfilBean;
import com.epcs.recursoti.configuracion.DateHelper;
import com.epcs.recursoti.configuracion.MiEntelProperties;
import com.epcs.recursoti.configuracion.jsf.utils.JSFMessagesHelper;
import com.epcs.recursoti.configuracion.locator.WebServiceLocator;
import com.epcs.recursoti.configuracion.locator.WebServiceLocatorException;
import com.epcs.recursoti.excepcion.DAOException;
import com.epcs.recursoti.excepcion.ServiceException;
import com.epcs.ventas.fidelizacion.ZonaBamService;
import com.epcs.ventas.fidelizacion.ZonaBamServiceFaultMessage;
import com.epcs.ventas.fidelizacion.ZonaBamServicePortType;
import com.epcs.ventas.fidelizacion.types.ZonaPerfilRegaloRequestType;
import com.epcs.ventas.fidelizacion.types.ZonaPerfilRegaloResponseType;
import com.epcs.ventas.fidelizacion.types.ZonaPerfilRequestType;
import com.epcs.ventas.fidelizacion.types.ZonaPerfilResponseType;

/**
 * @author jlopez (I2B) en nombre de Absalon Opazo (Atencion al Cliente, EntelPcs)
 *
 */
public class BeneficiosDAO {

private static final Logger LOGGER = Logger.getLogger(BeneficiosDAO.class);	
	
	private static final String PLATAFORMA = MiEntelProperties.getProperty("zonaEntel.prestaLukaService.plataforma");
	private static final String ACCION_PERFILZONA = "PERFILZONA";
	private static final String EMPRESA = MiEntelProperties.getProperty("zonaEntel.prestaLukaService.empresa");
	private static final String LOCAL = MiEntelProperties.getProperty("zonaEntel.prestaLukaService.local");
	private static final String TERMINAL = MiEntelProperties.getProperty("zonaEntel.prestaLukaService.terminal");
	private static final String FILLER1 = "NONE";
	private static final String FILLER2 = "NONE";
	private static final String FILLER3 = "NONE";			
	
	/**
     * 
     * @param numeroPcs
     * @param rut
     * @return
     * @throws DAOException
     * @throws ServiceException
     */
    public ZonaPerfilBean getZonaPerfil(String numeroPcs, String rut) throws DAOException, ServiceException {
    	
    	ZonaPerfilBean zonaPerfilBean = new ZonaPerfilBean();
    	/*
    	zonaPerfilBean.setNumeroPcs(numeroPcs);
		zonaPerfilBean.setRut(rut);
		zonaPerfilBean.setStatusRespuesta("00");
		*/
    	
        ZonaBamServicePortType port = null;
        
        LOGGER.info("Instanciando el port");
        try {
            port = (ZonaBamServicePortType) WebServiceLocator
                    .getInstance().getPort(ZonaBamService.class,
                    		ZonaBamServicePortType.class);
        } catch (WebServiceLocatorException e) {
            LOGGER.error("Error al inicializar el Port "+ ZonaBamServicePortType.class, e);
            LOGGER.error( new DAOException(e));
        }                
        
        LOGGER.info("Configurando Datos de la peticion");
        ZonaPerfilRequestType request = new ZonaPerfilRequestType();
        request.setPlataforma(PLATAFORMA);
        request.setAccion(ACCION_PERFILZONA);
        request.setEmpresa(EMPRESA);
        request.setLocal(LOCAL);
        request.setTerminal(TERMINAL);             
        request.setCorrelativo(DateHelper.format(new Date(), "MMdd"));
        request.setIdTransaccion(EMPRESA + LOCAL + DateHelper.format(new Date(), DateHelper.FORMAT_yyyyMMddhhmmss));
        request.setMovil(numeroPcs);
        request.setRut(rut);
        request.setFiller1(FILLER1);
        request.setFiller2(FILLER2);
        request.setFiller3(FILLER3);
                
        LOGGER.info("Invocando servicio");
        ZonaPerfilResponseType response;

        /*
        LOGGER.info(request.getPlataforma());
        LOGGER.info(request.getAccion());
        LOGGER.info(request.getEmpresa());
        LOGGER.info(request.getLocal());
        LOGGER.info(request.getTerminal());
        LOGGER.info(request.getCorrelativo());
        LOGGER.info(request.getIdTransaccion());
        LOGGER.info(request.getMovil());
        LOGGER.info(request.getRut());
        LOGGER.info(request.getFiller1());
        LOGGER.info(request.getFiller2());
        LOGGER.info(request.getFiller3());
        */
        
        try {
        	
			response = port.zonaPerfil(request);			
			zonaPerfilBean.setNumeroPcs(response.getMovil());
			zonaPerfilBean.setRut(response.getRut());
			zonaPerfilBean.setStatusRespuesta(response.getStatusRespuesta());
						
			LOGGER.info("ZonaPerfil - statusRespuesta ---> "+zonaPerfilBean.getStatusRespuesta());						
					
        }         
        catch (ZonaBamServiceFaultMessage e) {
			LOGGER.error("Exception caught on Service invocation", e);
			
			String codigoRespuesta = e.getFaultInfo().getCodigo();
	        String descripcionRespuesta = e.getFaultInfo().getMensaje();
	        
	        LOGGER.error("Respuesta del Servicio : " +codigoRespuesta+ " - " + descripcionRespuesta);
	        
			LOGGER.error( new ServiceException(codigoRespuesta, descripcionRespuesta));		
		}  
        catch(Exception e){
        	LOGGER.error( new DAOException("Error inesperado al obtener zonaPerfil : "+e.getMessage() ));
        }		
    	
        return zonaPerfilBean;
    }
    
    
    /**
     * 
     * @param msisdnSponsor
     * @param msisdnRecibe
     * @param rut
     * @return
     * @throws DAOException
     * @throws ServiceException
     */
    public ZonaPerfilBean getZonaPerfilRegalo(String msisdnSponsor, String msisdnRecibe, String rut) throws DAOException, ServiceException {
    	
    	ZonaPerfilBean zonaPerfilBean = new ZonaPerfilBean();
    	/*
    	zonaPerfilBean.setNumeroPcs(numeroPcs);
		zonaPerfilBean.setRut(rut);
		zonaPerfilBean.setStatusRespuesta("00");
		*/
    	
        ZonaBamServicePortType port = null;
        
        LOGGER.info("Instanciando el port");
        try {
            port = (ZonaBamServicePortType) WebServiceLocator
                    .getInstance().getPort(ZonaBamService.class,
                    		ZonaBamServicePortType.class);
        } catch (WebServiceLocatorException e) {
            LOGGER.error("Error al inicializar el Port "+ ZonaBamServicePortType.class, e);
            LOGGER.error( new DAOException(e));
        }                
        
        LOGGER.info("Configurando Datos de la peticion");
        ZonaPerfilRegaloRequestType request = new ZonaPerfilRegaloRequestType();
        request.setPlataforma(PLATAFORMA);
        request.setAccion(ACCION_PERFILZONA);
        request.setEmpresa(EMPRESA);
        request.setLocal(LOCAL);
        request.setTerminal(TERMINAL);             
        request.setCorrelativo(DateHelper.format(new Date(), "MMdd"));
        request.setIdTransaccion(EMPRESA + LOCAL + DateHelper.format(new Date(), DateHelper.FORMAT_yyyyMMddhhmmss));
        request.setMovil(msisdnRecibe);
        request.setRut(rut);
        request.setFiller1(msisdnSponsor);
        request.setFiller2(FILLER2);
        request.setFiller3(FILLER3);
                
        LOGGER.info("Invocando servicio");
        ZonaPerfilRegaloResponseType response;

        /*
        LOGGER.info(request.getPlataforma());
        LOGGER.info(request.getAccion());
        LOGGER.info(request.getEmpresa());
        LOGGER.info(request.getLocal());
        LOGGER.info(request.getTerminal());
        LOGGER.info(request.getCorrelativo());
        LOGGER.info(request.getIdTransaccion());
        LOGGER.info(request.getMovil());
        LOGGER.info(request.getRut());
        LOGGER.info(request.getFiller1());
        LOGGER.info(request.getFiller2());
        LOGGER.info(request.getFiller3());
        */
        
        try {
        	
			response = port.zonaPerfilRegalo(request);			
			zonaPerfilBean.setNumeroPcs(response.getMovil());
			zonaPerfilBean.setRut(response.getRut());
			zonaPerfilBean.setStatusRespuesta(response.getStatusRespuesta());
						
			LOGGER.info("ZonaPerfilRegalo - statusRespuesta ---> "+zonaPerfilBean.getStatusRespuesta());						
					
        }         
        catch (ZonaBamServiceFaultMessage e) {
			LOGGER.error("Exception caught on Service invocation", e);
			
			String codigoRespuesta = e.getFaultInfo().getCodigo();
	        String descripcionRespuesta = e.getFaultInfo().getMensaje();
	        
	        LOGGER.error("Respuesta del Servicio : " +codigoRespuesta+ " - " + descripcionRespuesta);
	        
			LOGGER.error( new ServiceException(codigoRespuesta, descripcionRespuesta));		
		}  
        catch(Exception e){
        	LOGGER.error( new DAOException("Error inesperado al obtener zonaPerfil : "+e.getMessage() ));
        }		
    	
        return zonaPerfilBean;
    }
    
}
