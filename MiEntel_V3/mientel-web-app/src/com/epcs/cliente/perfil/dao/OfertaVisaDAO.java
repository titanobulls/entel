/* Propiedad de Entel PCS. Todos los derechos reservados */
package com.epcs.cliente.perfil.dao;

import java.io.Serializable;
import java.sql.SQLException;

import org.apache.log4j.Logger;


import com.epcs.cliente.perfil.ClientePerfilService;
import com.epcs.cliente.perfil.ClientePerfilServicePortType;
import com.epcs.cliente.perfil.bean.Oferta;
import com.epcs.cliente.perfil.types.ConsultarDireccionPostalType;
import com.epcs.cliente.perfil.types.ResultadoConsultarDireccionPostalType;
import com.epcs.loyalty.zonaterceros.wsloyalty.ConsultaEstadoTCRutMovil;
import com.epcs.loyalty.zonaterceros.wsloyalty.ConsultaEstadoTCRutMovilResponse;
import com.epcs.loyalty.zonaterceros.wsloyalty.ConsultaEstadoTCService;
import com.epcs.loyalty.zonaterceros.wsloyalty.Servicio;
import com.epcs.recursoti.configuracion.MiEntelProperties;
import com.epcs.recursoti.configuracion.ParametrosHelper;
import com.epcs.recursoti.configuracion.Utils;
import com.epcs.recursoti.configuracion.locator.WebServiceLocator;
import com.epcs.recursoti.configuracion.locator.WebServiceLocatorException;
import com.epcs.recursoti.excepcion.DAOException;
import com.epcs.recursoti.excepcion.ServiceException;


/**
 * @author wbrochero (I2B) en nombre de Absalon Opazo (Atencion al Cliente,
 *         EntelPcs)
 * 
 */
public class OfertaVisaDAO implements Serializable {
	 
	private static final long serialVersionUID = 1L;

    private OfertaVisaEntelDAO  ofertaVisaEntelDAO;  
    private static final Logger LOGGER = Logger.getLogger(OfertaVisaDAO.class);
    public static final String CODIGO_RESPUESTA_OK = MiEntelProperties
    .getProperty("servicios.codigoRespuesta.exito");
    
    public OfertaVisaDAO() {
    	ofertaVisaEntelDAO = new OfertaVisaEntelDAO();
    }

    /**
     * @return the ofertaVisaEntelDAO
     */
    public OfertaVisaEntelDAO getOfertaVisaEntelDAO() {
        return ofertaVisaEntelDAO;
    }

    /**
     * @param OfertaVisaEntelDAO ofertaVisaEntelDAOto set
     */
    public void setOfertaVisaEntelDAO(OfertaVisaEntelDAO ofertaVisaEntelDAO) {
        this.ofertaVisaEntelDAO = ofertaVisaEntelDAO;
    }
    
    /**
     * Marca la oferta entel visa, para no volver a mostrarla al cliente
     * @param codigo
     * @param rechazado     
     * @throws SQLException
     */
    public void marcarOfertaVisa(long codigo, boolean rechazado) throws Exception {
    	ofertaVisaEntelDAO.marcarOfertaVisa(codigo,  rechazado);        
    }
    
    /**
     * Marca la oferta entel visa con la fecha en la que fue visitada
     * @param codigo        
     * @throws SQLException
     */
    public void actualizarVisitaVisaEntel(long codigo) throws Exception {
    	ofertaVisaEntelDAO.actualizarVisitaVisaEntel(codigo);
    }
    
    /**
     * Consultar la oferta visa para cargar los datos de esta en un objeto con atributos de la oferta.
     * @param movil    
     * @param rut 
     * @param estado     
     * @throws SQLException
     */
    public Oferta getVisaEntelByMovilRutEstado(String movil, String rut, int estado) throws Exception {    	
    	return ofertaVisaEntelDAO.getVisaEntelByMovilRutEstado(movil, rut, estado);    	
    }
    

    /**
     * Insertar una oferta visa.
     * @param movil    
     * @param rut 
     * @param estado     
     * @throws SQLException
     */
    public void insertarVisitaVisaEntel(String movil, String rut, int estado) throws Exception {
    	ofertaVisaEntelDAO.insertarVisitaVisaEntel(movil, rut, estado);
    }
    
    
    public int consultarAlternancia(String rut, String movil, String ofertas) throws Exception {
    	return ofertaVisaEntelDAO.consultarAlternancia(rut, movil, ofertas);
    }    
    
    
    /**
     * Obtiene estado oferta visa
     * 
     * @param msisdn 
     * @param rut
     * @return
     * @throws DAOException
     * @throws ServiceException
     */
    public String obtenerEstadoVisa(int msisdn, String rut) throws DAOException,
    ServiceException {    	
   	
    	String respuesta="";
    	Servicio port = null;	    
		LOGGER.info("Instanciando el Port.");
		
		try 
		{
		   port = (Servicio) WebServiceLocator
		            .getInstance().getPort(ConsultaEstadoTCService.class,
		            		Servicio.class);
		} 
		catch (WebServiceLocatorException e) 
		{
		    LOGGER.error("Error al inicializar el Port "+ Servicio.class);
		    LOGGER.error( new DAOException(e));
		}
		
		LOGGER.info("Configurando parametros de la peticion"); 			   
		ConsultaEstadoTCRutMovil request = new ConsultaEstadoTCRutMovil();
		 		
		request.setRut(rut.toUpperCase());
		request.setMovil(msisdn);
		
		LOGGER.info("Invocando servicio");
		ConsultaEstadoTCRutMovilResponse response = new ConsultaEstadoTCRutMovilResponse();
		
		try
		{
			response.setReturn(port.consultaEstadoTCRutMovil(rut, msisdn));
			
        } 
	    catch (Exception e) {
             LOGGER.error("Exception caught on Service invocation: "
                     + "consultaEstadoTCRutMovil", e);
             LOGGER.error( new DAOException(e));
        }	
		
		if(response.getReturn().getEstado()!=null)
		{				
	       LOGGER.info("codigoRespuesta " + response.getReturn().getEstado()); 
		
			try
			{
				respuesta = response.getReturn().getEstado();
			   
			 
			}	
			catch (Exception e) 
			{
                LOGGER.error("Exception inesperada en el Consultar Solicitudes Activas", e);
                LOGGER.error( new DAOException(e));
            } 			 			
		}
		return respuesta ;
    }   
    
    
    /**
     * Metodo de tipo boolean que determina si un cliente es preferencial.
     * 
     * @throws DAOException
     * @throws ServiceException
     * 
     */
    public boolean isClientePreferencial(String rut, String numeroCuenta) throws DAOException,
            ServiceException {

        ClientePerfilServicePortType port = null;
        boolean respuesta= false;

        LOGGER.info("Instanciando el port "
                + ClientePerfilServicePortType.class);
        try {
            port = (ClientePerfilServicePortType) WebServiceLocator
                    .getInstance().getPort(ClientePerfilService.class,
                            ClientePerfilServicePortType.class);
        } catch (WebServiceLocatorException e) {
            LOGGER.error("Error al inicializar el Port "
                    + ClientePerfilServicePortType.class, e);
            LOGGER.error( new DAOException(e));
        }

        LOGGER.info("Configurando Datos de la peticion");
        ConsultarDireccionPostalType request = new ConsultarDireccionPostalType();

        
        String IDS_ISTEMA = MiEntelProperties
                .getProperty("parametros.direccionpostal.idsistema");

        String PASSWORD_SISTEMA = MiEntelProperties
                .getProperty("parametros.direccionpostal.passwordsistema");
        
        request.setIdSistema(IDS_ISTEMA);
        request.setPasswordSistema(PASSWORD_SISTEMA);
        request.setRut(rut);
        request.setNumeroCuenta(numeroCuenta);

        LOGGER.info("Invocando servicio");
        ResultadoConsultarDireccionPostalType response = null;
        try {
            response = port.consultarDireccionPostal(request);
        } catch (Exception e) {
            LOGGER.error("Exception caught on Service invocation: consultarDireccionPostal", e);
            LOGGER.error( new DAOException(e));
        }

        String codigoRespuesta = response.getRespuesta().getCodigo();
        String descripcionRespuesta = response.getRespuesta().getDescripcion();

        LOGGER.info("codigoRespuesta " + codigoRespuesta);
        LOGGER.info("descripcionRespuesta " + descripcionRespuesta);

        if (Utils.isEmptyString(codigoRespuesta)) {
            LOGGER.error( new DAOException("consultarDireccionPostal: Servicio no respondio "
                    + "con codigoRespuesta"));
        }

        if (CODIGO_RESPUESTA_OK.equals(codigoRespuesta)) {

        	respuesta = ParametrosHelper.getExisteParametro(
					"ofertas.grupoUsuarioPreferencial", response.getDatosUsuarioDireccion().getCodigoGrupoCliente());       
            
        }
        else {
            LOGGER.info("consultarDireccionPostal: Service error code received: "
                    + codigoRespuesta + " - " + descripcionRespuesta);
            LOGGER.error( new ServiceException(codigoRespuesta, descripcionRespuesta));
        }

        return respuesta;
    }  
    
    
    	
    	
}
