/* Propiedad de Entel PCS. Todos los derechos reservados */
package com.epcs.cliente.perfil.dao;

import java.io.Serializable;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import com.epcs.bean.AccesorioEquipoBean;
import com.epcs.bean.BloqueoDesbloqueoEquipoBean;
import com.epcs.bean.BloqueoEquipoBean;
import com.epcs.bean.ConsultarBloqueoDesbloqueoBean;
import com.epcs.bean.ConsultarDatosBuicBean;
import com.epcs.bean.ContactoPorRenovacionBean;
import com.epcs.bean.DanoEquipoBean;
import com.epcs.bean.DatosEquipoRobadoBean;
import com.epcs.bean.DatosUsuarioBloqueoBean;
import com.epcs.bean.DefectoEquipoBean;
import com.epcs.bean.DiagnosticoBean;
import com.epcs.bean.DocumentoAperturaOTBean;
import com.epcs.bean.Equipo4GLteBean;
import com.epcs.bean.EquipoActualBean;
import com.epcs.bean.EquipoFullBean;
import com.epcs.bean.EquiposContratadosYArriendoMPTBean;
import com.epcs.bean.EstadoRenovacionEquipoBean;
import com.epcs.bean.GrupoClienteBean;
import com.epcs.bean.HistorialOrdenTrabajoBean;
import com.epcs.bean.InformeTecnicoOTBean;
import com.epcs.bean.OrdenTrabajoBean;
import com.epcs.bean.OrdenTrabajoMPTBean;
import com.epcs.bean.OrdenTrabajoVigenteBean;
import com.epcs.bean.PinPukBean;
import com.epcs.bean.PresupuestoBean;
import com.epcs.bean.ReparacionBean;
import com.epcs.bean.ReparacionEquipoBean;
import com.epcs.bean.ResumenEquipoBean;
import com.epcs.bean.ResumenLineaEquipoBean;
import com.epcs.bean.RutBean;
import com.epcs.bean.ServicioTecnicoMPTBean;
import com.epcs.bean.SintomaEquipoBean;
import com.epcs.bean.SituacionActualEquipoBean;
import com.epcs.bean.SolicitaIphoneBean;
import com.epcs.bean.TraficoEquiposBean;
import com.epcs.billing.balance.BillingBalanceService;
import com.epcs.billing.balance.BillingBalanceService_Service;
import com.epcs.billing.balance.types.ConsultarEstadoServicioPorFacturaType;
import com.epcs.billing.balance.types.ResultadoConsultarEstadoServicioPorFacturaType;
import com.epcs.cliente.contacto.BloqueoSubtelService;
import com.epcs.cliente.contacto.ClienteContactoService;
import com.epcs.cliente.contacto.ClienteContactoServicePortType;
import com.epcs.cliente.contacto.VerificacionBloqueoPortType;
import com.epcs.cliente.contacto.bloqueosubtel.types.ConsultarDatosBuicType;
import com.epcs.cliente.contacto.bloqueosubtel.types.ConsultarDatosEquipoPorImeiType;
import com.epcs.cliente.contacto.bloqueosubtel.types.DatosBuicType;
import com.epcs.cliente.contacto.bloqueosubtel.types.ResultadoConsultarDatosBuicType;
import com.epcs.cliente.contacto.bloqueosubtel.types.ResultadoConsultarDatosEquipoPorImeiType;
import com.epcs.cliente.contacto.bloqueosubtel.types.ResultadoValidarClaveDesbloqueoType;
import com.epcs.cliente.contacto.bloqueosubtel.types.ValidarClaveDesbloqueoType;
import com.epcs.cliente.contacto.types.ActualizarDatosBuicType;
import com.epcs.cliente.contacto.types.BloquearEquipoType;
import com.epcs.cliente.contacto.types.ConsultarBloqueoDesbloqueoType;
import com.epcs.cliente.contacto.types.ConsultarEquiposContratadosYArriendoMPTType;
import com.epcs.cliente.contacto.types.ConsultarImeiType;
import com.epcs.cliente.contacto.types.ConsultarPinPukType;
import com.epcs.cliente.contacto.types.DatosEquipoRobadoType;
import com.epcs.cliente.contacto.types.DatosUsuarioBloqueoType;
import com.epcs.cliente.contacto.types.EquipoActualType;
import com.epcs.cliente.contacto.types.EquiposContratadosYArriendoMPT;
import com.epcs.cliente.contacto.types.EstaEnGrupoHabilType;
import com.epcs.cliente.contacto.types.EstadoRenovacionEquipoType;
import com.epcs.cliente.contacto.types.IngresarBloqueoType;
import com.epcs.cliente.contacto.types.IngresarDesbloqueoType;
import com.epcs.cliente.contacto.types.ObtenerDatosUsuarioBloqueoType;
import com.epcs.cliente.contacto.types.ObtenerSituacionActualType;
import com.epcs.cliente.contacto.types.PinPukType;
import com.epcs.cliente.contacto.types.ResultadoActualizarDatosBuicType;
import com.epcs.cliente.contacto.types.ResultadoBloquearEquipoType;
import com.epcs.cliente.contacto.types.ResultadoConsultarBloqueoDesbloqueoType;
import com.epcs.cliente.contacto.types.ResultadoConsultarEquiposContratadosYArriendoMPTType;
import com.epcs.cliente.contacto.types.ResultadoConsultarImeiType;
import com.epcs.cliente.contacto.types.ResultadoConsultarPinPukType;
import com.epcs.cliente.contacto.types.ResultadoEstaEnGrupoHabilType;
import com.epcs.cliente.contacto.types.ResultadoIngresarBloqueoType;
import com.epcs.cliente.contacto.types.ResultadoIngresarDesbloqueoType;
import com.epcs.cliente.contacto.types.ResultadoObtenerDatosUsuarioBloqueoType;
import com.epcs.cliente.contacto.types.ResultadoObtenerSituacionActualType;
import com.epcs.cliente.contacto.types.ResultadoSolicitaIphoneType;
import com.epcs.cliente.contacto.types.ResultadoSolicitaLineaAdicionalType;
import com.epcs.cliente.contacto.types.ResultadoSolicitudRenovacionContactoType;
import com.epcs.cliente.contacto.types.ResultadoValidarBloqueoType;
import com.epcs.cliente.contacto.types.SituacionActualType;
import com.epcs.cliente.contacto.types.SolicitaIphoneType;
import com.epcs.cliente.contacto.types.SolicitaLineaAdicionalType;
import com.epcs.cliente.contacto.types.SolicitudRenovacionContactoType;
import com.epcs.cliente.contacto.types.ValidarBloqueoType;
import com.epcs.cliente.perfil.ClientePerfilService;
import com.epcs.cliente.perfil.ClientePerfilServicePortType;
import com.epcs.cliente.perfil.controller.util.EquipoControllerHelper;
import com.epcs.cliente.perfil.types.ConsultarDireccionPostalType;
import com.epcs.cliente.perfil.types.ConsultarEquipoFullType;
import com.epcs.cliente.perfil.types.ConsultarEquipoType;
import com.epcs.cliente.perfil.types.EquipoFullType;
import com.epcs.cliente.perfil.types.EquipoType;
import com.epcs.cliente.perfil.types.ResultadoConsultarDireccionPostalType;
import com.epcs.cliente.perfil.types.ResultadoConsultarEquipoFullType;
import com.epcs.cliente.perfil.types.ResultadoConsultarEquipoType;
import com.epcs.cliente.problema.ClienteProblemaService;
import com.epcs.cliente.problema.ClienteProblemaServicePortType;
import com.epcs.cliente.problema.types.AccesorioType;
import com.epcs.cliente.problema.types.AceptarRechazarPresupuestoType;
import com.epcs.cliente.problema.types.ConsultarOTVigentesType;
import com.epcs.cliente.problema.types.ConsultarServicioTecnicoMPTType;
import com.epcs.cliente.problema.types.DanosType;
import com.epcs.cliente.problema.types.DiagnosticoType;
import com.epcs.cliente.problema.types.DocumentoAperturaType;
import com.epcs.cliente.problema.types.HistorialOTType;
import com.epcs.cliente.problema.types.InfDefectosType;
import com.epcs.cliente.problema.types.InfReparacionType;
import com.epcs.cliente.problema.types.InformeTecnicoType;
import com.epcs.cliente.problema.types.OTVigenteType;
import com.epcs.cliente.problema.types.ObtenerDocumentoAperturaType;
import com.epcs.cliente.problema.types.ObtenerInformeTecnicoType;
import com.epcs.cliente.problema.types.OrdenesDeTrabajoMPTType;
import com.epcs.cliente.problema.types.PresupuestoType;
import com.epcs.cliente.problema.types.ReparacionesType;
import com.epcs.cliente.problema.types.ResultadoAceptarRechazarPresupuestoType;
import com.epcs.cliente.problema.types.ResultadoConsultarOTVigentesType;
import com.epcs.cliente.problema.types.ResultadoConsultarServicioTecnicoMPTType;
import com.epcs.cliente.problema.types.ResultadoObtenerDocumentoAperturaType;
import com.epcs.cliente.problema.types.ResultadoObtenerInformeTecnicoType;
import com.epcs.cliente.problema.types.SintomaType;
import com.epcs.erp.legal.consultaequiposrobados.ConsultaEquipoRobadoFaultMessage;
import com.epcs.erp.legal.consultaequiposrobados.ConsultaEquipoRobadoPortType;
import com.epcs.erp.legal.consultaequiposrobados.ConsultaEquipoRobadoServices;
import com.epcs.erp.legal.consultaequiposrobados.types.ConsultaEquipoRobadoRequestDocumentType;
import com.epcs.erp.legal.consultaequiposrobados.types.ConsultaEquipoRobadoResponseDocumentType;
import com.epcs.integracion.seguimientotrafservice.SeguimientoTrafService;
import com.epcs.integracion.seguimientotrafservice.SeguimientoTrafServiceService;
import com.epcs.recursoti.configuracion.MiEntelProperties;
import com.epcs.recursoti.configuracion.Utils;
import com.epcs.recursoti.configuracion.locator.WebServiceLocator;
import com.epcs.recursoti.configuracion.locator.WebServiceLocatorException;
import com.epcs.recursoti.excepcion.DAOException;
import com.epcs.recursoti.excepcion.ServiceException;
import com.epcs.recursoti.parametros.dao.ParametrosDAO;
import com.esa.clientes.perfilesclientes.resumenlineasequiposcliente.ClienteDTO;
import com.esa.clientes.perfilesclientes.resumenlineasequiposcliente.ResumenDTO;
import com.esa.clientes.perfilesclientes.resumenlineasequiposcliente.ResumenLineasEquiposPort;
import com.esa.clientes.perfilesclientes.resumenlineasequiposcliente.ResumenLineasEquiposService;

/**
 * @author jmanzur (I2B) en nombre de Absalon Opazo (Atencion al Cliente,
 *         EntelPcs)
 * 
 */
public class EquipoDAO  implements Serializable { 
	
	private static final long serialVersionUID = 1L;
	

    private static final Logger LOGGER = Logger.getLogger(EquipoDAO.class);

    public static final String CODIGO_RESPUESTA_OK = MiEntelProperties.getProperty("servicios.codigoRespuesta.exito");
    
    public static final String CODIGO_RESPUESTA_BLOQUEO_OPCION1 = MiEntelProperties.getProperty("servicios.codigoRespuesta.equipoBloqueadoOP1");
    
    public static final String CODIGO_RESPUESTA_BLOQUEO_OPCION2 = MiEntelProperties.getProperty("servicios.codigoRespuesta.equipoBloqueadoOP2");
    
    public static final String CODIGO_RESPUESTA_BLOQUEO_OPCION3 = MiEntelProperties.getProperty("servicios.codigoRespuesta.equipoBloqueadoOP3");
    
    public static final String CODIGO_RESPUESTA_BLOQUEO_OPCION4 = MiEntelProperties.getProperty("servicios.codigoRespuesta.equipoBloqueadoOP4");
    
    public static final String CODIGO_RESPUESTA_BLOQUEO_OPCION10 = MiEntelProperties.getProperty("servicios.codigoRespuesta.equipoBloqueadoOP10");
    
    public static final String CODIGO_RESPUESTA_PENDIENTE_BLOQUEO = MiEntelProperties.getProperty("servicios.codigoRespuesta.equipopPendienteBloqueo"); // Pendiente Bloqueo
    
    public static final String CODIGO_RESPUESTA_ERROR_DESBLOQUEO = MiEntelProperties.getProperty("servicios.codigoRespuesta.errorDesBloqueo"); // Error proceso DesBloqueo
    
    public static final String CODIGO_RESPUESTA_ERROR_BLOQUEO = MiEntelProperties.getProperty("servicios.codigoRespuesta.errorBloqueo"); // Error proceso Bloqueo
    
    public static final String CODIGO_RESPUESTA_PENDIENTE_DESBLOQUEO = MiEntelProperties.getProperty("servicios.codigoRespuesta.equipopPendienteDesBloqueo"); // Pendiente Des Bloqueo   
    
    public static final String CODIGO_RESPUESTA_NO_VALIDO = MiEntelProperties.getProperty("equipo.noValido");
    
    public static final String CODIGO_RESPUESTA_NO_RECONOCIDO = MiEntelProperties.getProperty("equipo.noReconocido");
    
    private static final String CODIGO_RESPUESTA_TIENE_BLOQUEO = MiEntelProperties.getProperty("equipo.bloqueo.tieneBloqueo");    
    
    private static final String BLOQUEO_POR_ROBO = MiEntelProperties.getProperty("equipo.bloqueo.motivo.robo");
    
    private static final String BLOQUEO_POR_EXTRAVIO = MiEntelProperties.getProperty("equipo.bloqueo.motivo.extravio");
    
	private static final String IVA = MiEntelProperties.getProperty("equipo.iva");
	
	private static final String EQUIPO_NO_RECONOCIDO = MiEntelProperties.getProperty("equipo.noReconocido.mensaje");
	
	private static final String PREFIJO_ENTEL = MiEntelProperties.getProperty("prefijo.entel");
	
	public static final String CODIGO_RESPUESTA_DATOS_INCOSINTENTES = MiEntelProperties.getProperty("servicios.codigoRespuesta.infoIncosistentes");
	
    public static final String EQUIPO_BLOQUEADO_FACTURA_IMPAGA = MiEntelProperties.getProperty("servicios.codigoRespuesta.equipoBloqueadoFacturaImpaga");
    
    public static final String SIM_ONLY_PLATAFORMA = MiEntelProperties.getProperty("equipo.simOnly.plataforma");
	
    /**
     * DAO de Parametros para obtener informacion de Region, ciudad y comuna de
     * usuarios.
     */
    private ParametrosDAO parametrosDao = new ParametrosDAO();
    
	
    /**
     * Obtener los datos del equipo del usuario desde el servicio
     * ClientePerfilService
     *  
     * @return bean con los datos del equipo del usuario en Sesion
     * @throws DAOException
     */
    public ResumenEquipoBean getResumenEquipo(String numeroPcs)
            throws DAOException, ServiceException {
        
        ClientePerfilServicePortType port;
        ResumenEquipoBean resumenEquipoBean = new ResumenEquipoBean();
        try {
            LOGGER.info("Instanciando el port");
            port = (ClientePerfilServicePortType) WebServiceLocator.getInstance()
                    .getPort(ClientePerfilService.class,
                            ClientePerfilServicePortType.class);
        
	        LOGGER.info("Configurando Datos de la peticion");
	        ConsultarEquipoType consultaEquipoTypeRequest = new ConsultarEquipoType();
	        consultaEquipoTypeRequest.setMsisdn(numeroPcs);
	
	        LOGGER.info("Invocando servicio");
	        ResultadoConsultarEquipoType consultaEquipoTypeResponse = null;
	        try{
	        	consultaEquipoTypeResponse = port.consultarEquipo(consultaEquipoTypeRequest);
	        }catch (Exception e) {
	        	LOGGER.error("Exception caught on Service invocation: "
	                    + "consultarEquipo", e);
	            LOGGER.error( new DAOException(e));
			}
	
	        String codigoRespuesta = consultaEquipoTypeResponse.getRespuesta()
	                .getCodigo();
	        String descripcionRespuesta = consultaEquipoTypeResponse
	                .getRespuesta().getDescripcion();
	        
	        LOGGER.info("Codigo de Respuesta :"+codigoRespuesta);
	        LOGGER.info("Descipcion de Respuesta :"+descripcionRespuesta);
	        
	        if (CODIGO_RESPUESTA_OK.equals(codigoRespuesta)) {
	
	            EquipoType equipoType = consultaEquipoTypeResponse.getEquipo();
	            
	            LOGGER.info("Configurando el Bean de Negocio");
	            try{
	            	resumenEquipoBean = new ResumenEquipoBean();
		            resumenEquipoBean.setModelo(equipoType.getModelo());
		            resumenEquipoBean.setUrlImagen(equipoType.getUrlImagen());
		            resumenEquipoBean.setMarca(equipoType.getMarca());     
	            }catch (Exception e) {
	            	LOGGER.error("Exception caught on Service response: "
		                    + "consultarEquipo", e);
		            LOGGER.error( new DAOException(e));
				}           
	            
	        }
	        else if( CODIGO_RESPUESTA_NO_VALIDO.equals(codigoRespuesta) || CODIGO_RESPUESTA_NO_RECONOCIDO.equals(codigoRespuesta) ){
	            LOGGER.info("Configurando el Bean de Negocio - Movil no reconocido");
	            resumenEquipoBean = new ResumenEquipoBean();
	            resumenEquipoBean.setModelo("");
	            resumenEquipoBean.setUrlImagen("");
	            resumenEquipoBean.setMarca(EQUIPO_NO_RECONOCIDO);
	        }
	        else {
	            LOGGER.info("Service error code received: " + codigoRespuesta
	                    + " - " + descripcionRespuesta);
	            LOGGER.error( new ServiceException(codigoRespuesta,
	                    descripcionRespuesta));
	        }
        
        } catch (WebServiceLocatorException e) {
            LOGGER.error("Error al inicializar el Port " 
            		+ ClientePerfilServicePortType.class, e);
            LOGGER.error( new DAOException(e));

        }
        LOGGER.info("END");
        return resumenEquipoBean;
    }           
    
    /**
     * Obtiene el imei correspondiente de un numeroPCS
     *  
     * @return String
     * @throws DAOException, ServiceException
     */  
   
   public String obtenerImei(String numeroPCS)throws DAOException, ServiceException {
    
    	ClienteContactoServicePortType port = null;    	    	    	
    	
    	try {
             
    		 port = (ClienteContactoServicePortType) WebServiceLocator.getInstance()
                     .getPort(ClienteContactoService.class,
                    		 ClienteContactoServicePortType.class);
    		 
         } catch (WebServiceLocatorException e) {
             LOGGER.error("Error al inicializar el Port "
                     + ClienteContactoService.class, e);
             LOGGER.error( new DAOException(e));
         }
         
         try{
        	 LOGGER.info("Configurando Datos de la peticion");
        	 ConsultarImeiType request = new ConsultarImeiType();
    		 request.setMsisdn(numeroPCS);
        	 
	         ResultadoConsultarImeiType response = null;
	         try{
	        	 
	    		 LOGGER.info("Invocando servicio");
	    		 response = port.consultarImei(request);
	    		 
	         }catch (Exception e) {
	        	 LOGGER.error("Exception caught on Service invocation: "
	                     + "consultarImei", e);
	        	 LOGGER.error( new DAOException(e));
	         } 
	    		 
			 String codigoRespuesta = response.getRespuesta().getCodigo();
	         String descripcionRespuesta = response.getRespuesta().getDescripcion();
	         
	         LOGGER.info("codigoRespuesta " + codigoRespuesta);   	        
	         LOGGER.info("descripcionRespuesta " + descripcionRespuesta);
			 
	         if( CODIGO_RESPUESTA_OK.equals(codigoRespuesta) ){   	        	   	        	 
	        	 return response.getImei();   	        	 
	         }   	         
	         else {
	            LOGGER.info("obtenerImei: Service error code received: "
	                    + codigoRespuesta + " - " + descripcionRespuesta);
	            LOGGER.error( new ServiceException(codigoRespuesta, descripcionRespuesta));
	         }
	    }catch (Exception e) {
	    	if(!(e instanceof ServiceException || e instanceof DAOException)){
		    	LOGGER.error("Exception inesperada obteniendo el imei "
		    			+ "del numero " + numeroPCS, e);
	    	}
	    }
         
        return null;
         
    }
    
    
    
    /*public String obtenerImei(String numeroPCS)throws DAOException, ServiceException {
    	
    	String imei = "";
    	String imsi = "";
    	
    	String imeiResult = null;
    	SeguimientoTrafService port;  	    	    	
    	try {
            port = (SeguimientoTrafService) WebServiceLocator
    		.getInstance().getPort(SeguimientoTrafServiceService.class, 
    				SeguimientoTrafService.class);
    		 
         } catch (WebServiceLocatorException e) {
             LOGGER.error("Error al inicializar el Port " + SeguimientoTrafService.class, e);
             LOGGER.error( new DAOException(e));
         }
         
         try{

        	 LOGGER.info("Configurando Datos de la peticion");
        	 numeroPCS = PREFIJO_ENTEL + numeroPCS;
        	 
        	 ConsultaActual request = new ConsultaActual();
    		 request.setMsisdn(numeroPCS);
	        	 
	    		 LOGGER.info("Invocando servicio");
	    		 String consultaActualResponse = port.consultaActual(request.getMsisdn(), imei, imsi);
	    		 
	    		 consultaActualResponse = consultaActualResponse.replace("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", "");
	         	
	             DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
	             DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
	             InputSource is = new InputSource();    
	             is.setCharacterStream(new StringReader(consultaActualResponse));
	             Document doc = docBuilder.parse(is);
	             doc.getDocumentElement ().normalize ();

	             NodeList ultimaTripleta = doc.getElementsByTagName("ultimaTripleta");
	             
	             for(int s=0; s<ultimaTripleta.getLength() ; s++){
	                 
	             	Node ultimaTripletaNode = ultimaTripleta.item(s);
	                 
	             	if(ultimaTripletaNode.getNodeType() == Node.ELEMENT_NODE){
	                     
	                 	 Element ultimaTripletaElement = (Element)ultimaTripletaNode;
	                 	 
	                     NodeList imeiName = ultimaTripletaElement.getElementsByTagName("imei");
	                     Element imeiNameElement = (Element)imeiName.item(0);
	                     NodeList textFNList = imeiNameElement.getChildNodes();
	                     imeiResult = ((Node)textFNList.item(0)).getNodeValue().trim();
	                 }
	             }
	    }catch (Exception e) {
	       	 LOGGER.error("Exception caught on Service invocation: " + "consultarImei", e);
	    	 LOGGER.error( new DAOException(e));
	    }
         
        return imeiResult;
    }*/
    
    /**
     * Obtiene apertura documento OT
     *  
     * @return InformeTecnicoOTBean
     * @throws DAOException, ServiceException
     */    
    public DocumentoAperturaOTBean obtenerAperturaDocumentoOT(String nroOrden)throws DAOException, ServiceException {
    	
    	ClienteProblemaServicePortType port = null;    	
    	DocumentoAperturaOTBean doc = new DocumentoAperturaOTBean();
    	
    	try {
             
    		 port = (ClienteProblemaServicePortType) WebServiceLocator.getInstance()
                     .getPort(ClienteProblemaService.class,
                    		 ClienteProblemaServicePortType.class);
    		 
         } catch (WebServiceLocatorException e) {
             LOGGER.error("Error al inicializar el Port "
                     + ClienteProblemaService.class, e);
             LOGGER.error( new DAOException(e));
         }
         
         try{
        	 
        	 LOGGER.info("Configurando Datos de la peticion");
        	 ObtenerDocumentoAperturaType request = new ObtenerDocumentoAperturaType();
    		 request.setNroOrden(nroOrden);
    		 
             ResultadoObtenerDocumentoAperturaType response = null;
             try{
            	 
            	 LOGGER.info("Invocando servicio");
        		 response = port.obtenerDocumentoApertura(request);
        		 
             }catch (Exception e) {
            	 LOGGER.error("Exception caught on Service invocation: "
                         + "obtenerDocumentoApertura", e);
            	 LOGGER.error( new DAOException(e));
             }
             
    		 String codigoRespuesta = response.getRespuesta().getCodigo();
             String descripcionRespuesta = response.getRespuesta().getDescripcion();
             
             LOGGER.info("codigoRespuesta " + codigoRespuesta);   	        
             LOGGER.info("descripcionRespuesta " + descripcionRespuesta);
    		 
             
             if( CODIGO_RESPUESTA_OK.equals(codigoRespuesta) ){
            	 
            	 try{
            		 DocumentoAperturaType dap = response.getDocumentoApertura();
                	 doc.setApellidoContacto1(dap.getApellidoContacto1());
                	 doc.setApellidoContacto2(dap.getApellidoContacto2());
                	 doc.setCiudadSucursal(dap.getCiudadSucursal());
                	 doc.setCodSucursal(dap.getCodSucursal());
                	 doc.setComunaCliente(dap.getComunaCliente());
                	 doc.setComunaSucursal(dap.getComunaSucursal());   	        	    	        
                	 doc.setDescSucural(dap.getDescSucural());
                	 doc.setDomicilioCliente(dap.getDomicilioCliente());
                	 doc.setFechaApertura(dap.getFechaApertura());
                	 doc.setFlagDespachoDomicilio(dap.getFlagDespachoDomicilio());
                	 doc.setFlagErt(dap.getFlagErt());
                	 doc.setFlagPresupuesto(dap.getFlagPresupuesto());
                	 doc.setFlagRetiroDomicilio(dap.getFlagRetiroDomicilio());
                	 doc.setMarca(dap.getMarca());
                	 doc.setModelo(dap.getModelo());
                	 doc.setNmContacto1(dap.getNmContacto1());
                	 doc.setNmContacto2(dap.getNmContacto2());
                	 doc.setNombreCliente(dap.getNombreCliente());
                	 doc.setNroOrden(dap.getNroOrden());
                	 doc.setNroSerie(dap.getNroSerie());
                	 doc.setParametro14(dap.getParametro14());
                	 doc.setRutCliente(dap.getRutCliente());   	        	 
                	 doc.setServiciosComplementarios(null);
                	 doc.setTipoDocApertura(dap.getTipoDocApertura());
                	    	        	 	        	   	        	 
                	 for( DanosType dan : dap.getDanos() ){
                		 DanoEquipoBean dano = new DanoEquipoBean();
                		 dano.setDescDano( dan.getDescDano() );
                		 dano.setTipoMoneda( dan.getTipoMoneda() );
                		 dano.setValor( parseDouble( dan.getValor() ) );
                		 doc.addDano(dano);
                	 }
                	 
                	 for( AccesorioType acc : dap.getAccesorios() ){
                		 AccesorioEquipoBean accesorio = new AccesorioEquipoBean();
                		 accesorio.setCodAccesorio(acc.getCodAccesorio());
                		 accesorio.setDescAccesorio(acc.getDescAccesorio());
                		 doc.addAccesorio(accesorio);
                	 }
                	 
                	 for( SintomaType sn : dap.getSintomas() ){
                		 SintomaEquipoBean sin = new SintomaEquipoBean();
                		 sin.setCodSintoma(sn.getCodSintoma());
                		 sin.setDescSintoma(sn.getDescSintoma());
                		 doc.addSintoma(sin);
                	 }
                	 
            	 }catch (Exception e) {
                	 LOGGER.error("Exception caught on Service response: "
                             + "obtenerDocumentoApertura", e);
                	 LOGGER.error( new DAOException(e));
                 }

             }
             else {
                LOGGER.info("obtenerDocumentoApertura: Service error code received: "
                        + codigoRespuesta + " - " + descripcionRespuesta);
                LOGGER.error( new ServiceException(codigoRespuesta, descripcionRespuesta));
             }
         }catch (Exception e) {
        	 if(!(e instanceof ServiceException || e instanceof DAOException)){
	        	 LOGGER.error("Exception inesperada al obtener apertura de documento OT "
	        			 + "para orden " + nroOrden, e);
        	 }
		 }

    	return doc;

    	
    }
    
    /**
     * Obtiene informe tecnico OT
     *  
     * @return InformeTecnicoOTBean
     * @throws DAOException, ServiceException
     */    
    public InformeTecnicoOTBean obtenerInformeTecnicoOT(String nroOrden)throws DAOException, ServiceException {
    	
    	ClienteProblemaServicePortType port = null;    	
    	InformeTecnicoOTBean inf = new InformeTecnicoOTBean();
    	
    	try {
             
    		 port = (ClienteProblemaServicePortType) WebServiceLocator.getInstance()
                     .getPort(ClienteProblemaService.class,
                    		 ClienteProblemaServicePortType.class);
    		 
         } catch (WebServiceLocatorException e) {
             LOGGER.error("Error al inicializar el Port "
                     + ClienteProblemaService.class, e);
             LOGGER.error( new DAOException(e));
         }
         
         try{
        	 
        	 LOGGER.info("Configurando Datos de la peticion");
        	 ObtenerInformeTecnicoType request = new ObtenerInformeTecnicoType();
    		 request.setNroOrden(nroOrden);
        	 
        	 ResultadoObtenerInformeTecnicoType response = null;
	         try{
	        	 
	        	 LOGGER.info("Invocando servicio");
	    		 response = port.obtenerInformeTecnico(request);
	    		 
	         }catch (Exception e) {
	        	 LOGGER.error("Exception caught on Service invocation: "
	                     + "obtenerInformeTecnico", e);
	        	 LOGGER.error( new DAOException(e));
	         }
	    		 
			 String codigoRespuesta = response.getRespuesta().getCodigo();
	         String descripcionRespuesta = response.getRespuesta().getDescripcion();
	         
	         LOGGER.info("codigoRespuesta " + codigoRespuesta);   	        
	         LOGGER.info("descripcionRespuesta " + descripcionRespuesta);
			 
	         
	         if( CODIGO_RESPUESTA_OK.equals(codigoRespuesta) ){
	        	 
	        	 try{
		        	 InformeTecnicoType inft = response.getInformeTecnicoOT();
		        	 inf.setCodProducto(inft.getCodProducto());
		        	 inf.setCodResolucion(inft.getCodResolucion());
		        	 inf.setCodSucursal(inft.getCodSucursal());   	        	    	        
		        	 inf.setDomicilioCliente(inft.getDomicilioCliente());
		        	 inf.setFecha(inft.getFecha());
		        	 inf.setFecha2(inft.getFecha2());
		        	 inf.setInformeTecnico(inft.getInformeTecnico());
		        	 inf.setMarca(inft.getMarca());
		        	 inf.setModelo(inft.getModelo());
		        	 inf.setNmCliente(inft.getNmCliente());
		        	 inf.setNmEquipo(inft.getNmEquipo());
		        	 inf.setNmSucursal(inft.getNmSucursal());
		        	 inf.setNroOrden(inft.getNroOrden());
		        	 inf.setNroserie(inft.getNroserie());
		        	 inf.setPiePagina1(inft.getPiePagina1());
		        	 inf.setPiePagina2(inft.getPiePagina2());
		        	 inf.setPiePagina3(inft.getPiePagina3());   	        	 
		        	 inf.setResolucionLaboratorio(inft.getResolucionLaboratorio());
		        	 inf.setRutCliente(inft.getRutCliente());
		        	 inf.setTpoInformeTecnico(inft.getTpoInformeTecnico());
		        	 inf.setGarantia(inft.getGarantia());
		        	 
		        	 for( InfDefectosType def : inft.getDefectos() ){
		        		DefectoEquipoBean df = new DefectoEquipoBean();
		        		df.setCodDefecto(def.getCodDefecto());
		        		df.setDescDefecto(def.getDescDefecto());
		        		inf.addDefecto(df);
		        	 }
		        	 
		        	 for( InfReparacionType in : inft.getReparaciones() ){
			        	ReparacionEquipoBean rp = new ReparacionEquipoBean();
			        	rp.setCodReparacion(in.getCodReparacion());
			        	rp.setDescReparacion(in.getDescReparacion());
			        	inf.addReparacion(rp);
		        	 }
	        	 
		         }catch (Exception e) {
		        	 LOGGER.error("Exception caught on Service response: "
		                     + "obtenerInformeTecnico", e);
		        	 LOGGER.error( new DAOException(e));
		         }
	        	 
	         }
	         else {
	        	 LOGGER.info("obtenerInformeTecnico: Service error code received: "
	        			 + codigoRespuesta + " - " + descripcionRespuesta);
	        	 LOGGER.error( new ServiceException(codigoRespuesta, descripcionRespuesta));
	         }
         }catch (Exception e) {
        	 if(!(e instanceof ServiceException || e instanceof DAOException)){
	        	 LOGGER.error("Exception inesperada al obtener informe tecnico OT "
	        			 + "para orden " + nroOrden, e);
        	 }
         }

    	return inf;
    	
    }
    
    /**
     * Obtiene las ordenes de trabajo activas
     *  
     * @return OrdenTrabajoVigenteBean
     * @throws DAOException, ServiceException
     */    
    public OrdenTrabajoVigenteBean obtenerOrdenesDeTrabajoVigente(String numeroPCS)throws DAOException, ServiceException {
    	
    	ClienteProblemaServicePortType port = null;    	    	
    	OrdenTrabajoVigenteBean ord = new OrdenTrabajoVigenteBean();
    	
    	try {
             
    		 port = (ClienteProblemaServicePortType) WebServiceLocator.getInstance()
                     .getPort(ClienteProblemaService.class,
                    		 ClienteProblemaServicePortType.class);
    		 
         } catch (WebServiceLocatorException e) {
             LOGGER.error("Error al inicializar el Port "
                     + ClienteProblemaService.class, e);
             LOGGER.error( new DAOException(e));
         }
         
         ResultadoConsultarOTVigentesType response = null;
         try{
        	 LOGGER.info("Configurando Datos de la peticion");
	    	 ConsultarOTVigentesType request = new ConsultarOTVigentesType();
			 request.setMsisdn(numeroPCS);
			 
			 LOGGER.info("Invocando servicio");
    		 response = port.consultarOTVigentes(request);
    		 
         }catch (Exception e) {
        	 LOGGER.error("Exception caught on Service invocation: "
                     + "consultarOTVigentes", e);
        	 LOGGER.error( new DAOException(e));
         }
    		 
    	 try{
			 String codigoRespuesta = response.getRespuesta().getCodigo();
	         String descripcionRespuesta = response.getRespuesta().getDescripcion();
	         
	         LOGGER.info("codigoRespuesta " + codigoRespuesta);   	        
	         LOGGER.info("descripcionRespuesta " + descripcionRespuesta);
			 
	         if( CODIGO_RESPUESTA_OK.equals(codigoRespuesta) ){
        	  
	        	 for( OTVigenteType ot : response.getOrdenesTrabajoVigentes() ){
	        		
	        		OrdenTrabajoBean otv = new OrdenTrabajoBean();
	        		otv.setDescripcion( ot.getDescripcion() );
	        		otv.setEstado(ot.getEstado());
	        		otv.setFechaIngreso(ot.getFechaIngreso());
	        		otv.setMarcaModelo(ot.getMarcaModelo());
	        		otv.setNroOrden(ot.getNroOrden());
	        		otv.setResLaboratorio(ot.getResLaboratorio());
	        		otv.setServContratados(ot.getServContratados());
	        		otv.setSolSucursal(ot.getSolSucursal());
	        		otv.setSucursalIngreso(ot.getSucursalIngreso());
	        		
	        		for( HistorialOTType historial : ot.getHistorial() ){
	        			HistorialOrdenTrabajoBean hist = new HistorialOrdenTrabajoBean();
	        			hist.setCodEstado(historial.getCodEstado());
	        			hist.setDescEstado(historial.getDescEstado());
	        			hist.setFechaEstado(historial.getFechaEstado());
	        			otv.addHistorial(hist);
	        		}
	        		
	        		PresupuestoType ps = ot.getPresupuesto();
	        		PresupuestoBean psb = new PresupuestoBean();
	        		
	        		if( ps != null && ps.getDiagnosticos() != null ){
	   	        		for( DiagnosticoType dt : ps.getDiagnosticos() ){
	   	        			DiagnosticoBean db = new DiagnosticoBean();
	   	        			db.setDiagnostico(dt.getDiagnostico());
	   	        			psb.addDiagnostico( db );	
	   	        		}
	        		}
	        		
	        		if( ps != null && ps.getReparaciones() != null ){
	   	        		for( ReparacionesType rp : ps.getReparaciones() ){
	   	        			ReparacionBean rpb = new ReparacionBean();
	   	        			rpb.setReparacion(rp.getReparacion());
	   	        			rpb.setValor(parseDouble(rp.getValor()));
	   	        			psb.addReparacion(rpb);	
	   	        		}
	   	        		psb.setValorTotal(parseDouble(ps.getValorTotal()));
	        		}   	        		   	        		
	        		
	        		otv.setPresupuesto(psb);   	        		
	        		ord.addOrden(otv);
	        		
	        	 }
        	 
	         }else {
	            LOGGER.info("consultarOTVigentes: Service error code received: "
	                    + codigoRespuesta + " - " + descripcionRespuesta);
	            LOGGER.error( new ServiceException(codigoRespuesta, descripcionRespuesta));
	         }
	     
    	 } catch (Exception e) {
        	 if(!(e instanceof ServiceException)){
	        	 LOGGER.error("Exception caught on Service response: "
	                     + "consultarOTVigentes", e);
	         }
         }
         
         return ord;
         
    }
    
    /**
     * Verifica que el usuario este en un grupo habil: 1, 6, 10, 24.
     * Si el usuario esta en alguno de estos grupos puede solicitar iphone
     * llenando el formulario de contacto.
     * */
    public boolean estaEnGrupoHabil(String rut)throws DAOException, ServiceException {
    	
    	ClienteContactoServicePortType port = null;    	
    	
    	try {
             
    		 port = (ClienteContactoServicePortType) WebServiceLocator.getInstance()
                     .getPort(ClienteContactoService.class,
                    		 ClienteContactoServicePortType.class);
    		 
         } catch (WebServiceLocatorException e) {
             LOGGER.error("Error al inicializar el Port "
                     + ClienteContactoService.class, e);
             LOGGER.error( new DAOException(e));
         }
        
         LOGGER.info("Configurando Datos de la peticion");
    	 EstaEnGrupoHabilType request = new EstaEnGrupoHabilType();
    	 request.setRut(rut);
		 
    	 ResultadoEstaEnGrupoHabilType response = null;
    	 try{
    		 LOGGER.info("Invocando servicio");
    		 response = port.estaEnGrupoHabil(request);
    	 }catch (Exception e) {
        	 LOGGER.error("Exception caught on Service invocation: "
                     + "estaEnGrupoHabil", e);
        	 LOGGER.error( new DAOException(e));
         }
    	 
    	 String codigoRespuesta = response.getRespuesta().getCodigo();
         String descripcionRespuesta = response.getRespuesta().getDescripcion();
         
         LOGGER.info("codigoRespuesta " + codigoRespuesta);   	        
         LOGGER.info("descripcionRespuesta " + descripcionRespuesta);
	 
         if( CODIGO_RESPUESTA_OK.equals(codigoRespuesta) ){        	
        	return Boolean.parseBoolean(response.getEstaEnGrupoHabil());
         }
         else {
            LOGGER.info("estaEnGrupoHabil: Service error code received: "
                 + codigoRespuesta + " - " + descripcionRespuesta);        
            LOGGER.error( new ServiceException(codigoRespuesta, descripcionRespuesta));
         }
		return false;
    	
    }
    
    /**
     * Verifica si el equipo tiene bloqueo existente
     *  
     * @return boolean
     * @throws DAOException, ServiceException
     */
    public boolean tieneBloqueoExistente(String numeroPCS, String mercado, String DVImei)throws DAOException, ServiceException {
    	
    	ClienteContactoServicePortType port = null;    	
    	LOGGER.info("Instanciando el port " + ClienteContactoServicePortType.class);
    	try {
             
    		 port = (ClienteContactoServicePortType) WebServiceLocator.getInstance()
                     .getPort(ClienteContactoService.class,
                    		 ClienteContactoServicePortType.class);
    		 
         } catch (WebServiceLocatorException e) {
             LOGGER.error("Error al inicializar el Port "
                     + ClienteContactoServicePortType.class, e);
             LOGGER.error( new DAOException(e));
         }
         
    	 LOGGER.info("Configurando Datos de la peticion");
    	 ValidarBloqueoType request = new ValidarBloqueoType();
		 
    	 request.setMsisdn(numeroPCS);
		 request.setMercado(mercado);
		 request.setDVImei(DVImei);
		 
		 ResultadoValidarBloqueoType response = null;
		 try{
        	 
    		 LOGGER.info("Invocando servicio");
    		 response = port.validarBloqueo(request);
    		 
         } catch (Exception e) {
        	 LOGGER.error("Exception caught on Service invocation: "
                     + "validarBloqueo", e);
             LOGGER.error( new DAOException(e));
         }
	         
	     try{
         
        	 String codigoRespuesta = response.getRespuesta().getCodigo();
   	         String descripcionRespuesta = response.getRespuesta().getDescripcion();
   	         
   	         LOGGER.info("codigoRespuesta " + codigoRespuesta);   	        
   	         LOGGER.info("descripcionRespuesta " + descripcionRespuesta);
    		 
   	         if( CODIGO_RESPUESTA_OK.equals(codigoRespuesta) ){
   	        	return false;
   	         }
   	         else if( CODIGO_RESPUESTA_TIENE_BLOQUEO.equals(codigoRespuesta) ){
   	        	 return true;
   	         }
   	         else {
   	            LOGGER.info("validarBloqueo: Service error code received: "
                        + codigoRespuesta + " - " + descripcionRespuesta);
   	            LOGGER.error( new ServiceException(codigoRespuesta, descripcionRespuesta));
   	         }
   	         
         }catch (Exception e) {
        	 if(!(e instanceof ServiceException)){
        		 LOGGER.error("Exception caught on Service response: "
        				 + "validarBloqueo", e);
        	 }
         }           
         
    	return false;
    	
    }
    
    /**
     * Bloquea equipo por robo 
     *  
     * @throws DAOException, ServiceException
     */    
    public void bloquearEquipoPorRobo(BloqueoEquipoBean bloq)throws DAOException, ServiceException {
    	bloquearEquipo(bloq, BLOQUEO_POR_ROBO);
    }
    
    /**
     * Bloquea equipo por extravio 
     *  
     * @throws DAOException, ServiceException
     */
    public void bloquearEquipoPorExtravio(BloqueoEquipoBean bloq)throws DAOException, ServiceException {
    	bloquearEquipo(bloq, BLOQUEO_POR_EXTRAVIO);
    }
    
    /**
     * Bloquea equipo por robo o extravio 
     *  
     * @throws DAOException, ServiceException
     */
    private void bloquearEquipo(BloqueoEquipoBean bloq, String motivo)throws DAOException, ServiceException {
    	
    	ClienteContactoServicePortType port = null;    	
    	
    	try {
             
    		 port = (ClienteContactoServicePortType) WebServiceLocator.getInstance()
                     .getPort(ClienteContactoService.class,
                    		 ClienteContactoServicePortType.class);
    		 
         } catch (WebServiceLocatorException e) {
             LOGGER.error("Error al inicializar el Port "
                     + ClienteContactoService.class, e);
             LOGGER.error( new DAOException(e));
         }
         
         try{
        	 
        	 LOGGER.info("Configurando Datos de la peticion");
        	 
        	 String mail = bloq.getUsuarioMail();
        	 
        	 if( mail != null && mail.contains("@") ){
        		String email[] = mail.split("@");
            	bloq.setUsuarioMail(email[0]);
            	bloq.setDominioMail(email[1]);
 	    	 }
        	 
        	 BloquearEquipoType request = new BloquearEquipoType();
        	 request.setAreaTelefonoFijo(bloq.getAreaTelefonoFijo());
        	 request.setClaveBloqueo(bloq.getClaveBloqueo());
        	 request.setDestinoBloqueo(bloq.getDestinoBloqueo());
        	 request.setDominioMail(bloq.getDominioMail());
        	 request.setDV(bloq.getDv());
        	 request.setFechaRobo(bloq.getFechaRobo());
        	 request.setIdSentidoBloqueo(bloq.getIdSentidoBloqueo());
        	 request.setMotivo(motivo);
        	 request.setMsisdn(bloq.getNumeroPCS());
        	 request.setMsisdnMotivoBloqueo_0020(bloq.getNumeroPCSMotivoBloqueo());
        	 request.setNombreCompletoUsuario(bloq.getNombreCompletoUsuario());
        	 request.setRutSinDV(bloq.getRutSinDV());
        	 request.setTelefonoFijo(bloq.getTelefonoFijo());
        	 request.setUsuarioMail(bloq.getUsuarioMail());
    		 
        	 ResultadoBloquearEquipoType response = null;
        	 try{
	        	 LOGGER.info("Invocando servicio");
	    		 response = port.bloquearEquipo(request);
	    		 
	         }catch (Exception e) {
	        	 LOGGER.error("Exception caught on Service invocation: "
	                     + "bloquearEquipo", e);
	        	 LOGGER.error( new DAOException(e));
	         }
    		 
			 String codigoRespuesta = response.getRespuesta().getCodigo();
	         String descripcionRespuesta = response.getRespuesta().getDescripcion();
	         
	         LOGGER.info("codigoRespuesta " + codigoRespuesta);   	        
	         LOGGER.info("descripcionRespuesta " + descripcionRespuesta);
			 
	         if( !CODIGO_RESPUESTA_OK.equals(codigoRespuesta) ){   	        	
	            LOGGER.info("bloquearEquipo: Service error code received: "
	                    + codigoRespuesta + " - " + descripcionRespuesta);        
	            LOGGER.error( new ServiceException(codigoRespuesta, descripcionRespuesta));
	         }
	         
         }catch (Exception e) {
        	 if(!(e instanceof ServiceException || e instanceof DAOException)){
	        	 LOGGER.error("Exception inesperada al bloquear equipo "
	                     + bloq.getNumeroPCS(), e);
        	 }
         }
         
    }
    
    /**
     * Actualiza datos de bloqueo de usuario
     *  
     * @throws DAOException, ServiceException
     */
    public void actualizarDatosUsuarioBloqueo(
    		DatosUsuarioBloqueoBean datosUsuarioBloqueo, String rutSinDV, 
    		String DV, String numeroPCS)
    throws DAOException, ServiceException {
    	
    	ClienteContactoServicePortType port = null;    	
    	
    	try {
             
    		 port = (ClienteContactoServicePortType) WebServiceLocator.getInstance()
                     .getPort(ClienteContactoService.class,
                    		 ClienteContactoServicePortType.class);
    		 
         } catch (WebServiceLocatorException e) {
             LOGGER.error("Error al inicializar el Port "
                     + ClienteContactoService.class, e);
             LOGGER.error( new DAOException(e));
         }
         
         try{
        	 
        	 LOGGER.info("Configurando Datos de la peticion");
        	 ActualizarDatosBuicType request = new ActualizarDatosBuicType();
        	 String email[] = datosUsuarioBloqueo.getEmail().split("@");
        	 request.setUsuarioMail(email[0]);
        	 request.setDominioMail(email[1]);
        	 request.setAreaTelefonoFijo(datosUsuarioBloqueo.getAreaTelefono());
        	 request.setTelefonoFijo(datosUsuarioBloqueo.getTelefono());
        	 request.setRutSinDV(rutSinDV);
        	 request.setDV(DV);
        	 request.setMsisdn(numeroPCS);
        	 
        	 ResultadoActualizarDatosBuicType response = null;
        	 try{
	        	 LOGGER.info("Invocando servicio");
	    		 response = port.actualizarDatosBuic(request);
	    		 
	         }catch (Exception e) {
	        	 LOGGER.error("Exception caught on Service invocation: "
	                     + "actualizarDatosBuic", e);
	        	 LOGGER.error( new DAOException(e));
	         }
    		 
			 String codigoRespuesta = response.getRespuesta().getCodigo();
	         String descripcionRespuesta = response.getRespuesta().getDescripcion();
	         
	         LOGGER.info("codigoRespuesta " + codigoRespuesta);   	        
	         LOGGER.info("descripcionRespuesta " + descripcionRespuesta);
			 
	         if( !CODIGO_RESPUESTA_OK.equals(codigoRespuesta) ){   	        	
	            LOGGER.info("actualizarDatosBuic: Service error code received: "
	                    + codigoRespuesta + " - " + descripcionRespuesta);        
	            LOGGER.error( new ServiceException(codigoRespuesta, descripcionRespuesta));
	         }
         }catch (Exception e) {
        	 if(!(e instanceof ServiceException || e instanceof DAOException)){
	        	 LOGGER.error("Exception inesperada al actualizar datos del usuario"
	        			 + rutSinDV + "-" + DV + "en BUIC", e);
        	 }
         }
         
    }
    
    /**
     * Obtener los datos de bloqueo de usuario como son
     * el email, dominio, telefono y area
     *  
     * @return bean con los datos de bloqueo del equipo
     * @throws DAOException, ServiceException
     */
    public DatosUsuarioBloqueoBean obtenerDatosUsuarioBloqueo(String rutSinDV, String DV)
    throws DAOException, ServiceException {
    	
    	ClienteContactoServicePortType port = null;
    	DatosUsuarioBloqueoBean bloq = new DatosUsuarioBloqueoBean();
    	
    	try {
             
    		 port = (ClienteContactoServicePortType) WebServiceLocator.getInstance()
                     .getPort(ClienteContactoService.class,
                    		 ClienteContactoServicePortType.class);
    		 
         } catch (WebServiceLocatorException e) {
             LOGGER.error("Error al inicializar el Port "
                     + ClienteContactoService.class, e);
             LOGGER.error( new DAOException(e));
         }
         
         try{
        	 
        	 LOGGER.info("Configurando Datos de la peticion");
        	 ObtenerDatosUsuarioBloqueoType request = new ObtenerDatosUsuarioBloqueoType();
        	 request.setRutSinDV(rutSinDV);
    		 request.setDV(DV);
        	 
    		 ResultadoObtenerDatosUsuarioBloqueoType response = null;
    		 try{
    	         
	    		 LOGGER.info("Invocando servicio");
	    		 response = port.obtenerDatosUsuarioBloqueo(request);
	    		 
	         }catch (Exception e) {
	        	 LOGGER.error("Exception caught on Service invocation: "
	                     + "obtenerDatosUsuarioBloqueo", e);
	        	 LOGGER.error( new DAOException(e));
	         }
         
         
			 String codigoRespuesta = response.getRespuesta().getCodigo();
	         String descripcionRespuesta = response.getRespuesta().getDescripcion();
	         
	         LOGGER.info("codigoRespuesta " + codigoRespuesta);   	        
	         LOGGER.info("descripcionRespuesta " + descripcionRespuesta);
		 
         	 if( CODIGO_RESPUESTA_OK.equals(codigoRespuesta) ){
         		 DatosUsuarioBloqueoType datos = response.getDatosUsuarioBloqueo();
            	 LOGGER.info(datos.getEmail()+ " - "+datos.getAreaTelefonoFijo()+" - "+datos.getTelefonoFijo());
            	 bloq.setEmail(datos.getEmail());
            	 bloq.setAreaTelefono(datos.getAreaTelefonoFijo());
            	 bloq.setTelefono(datos.getTelefonoFijo());
	         }else {
	             LOGGER.info("obtenerDatosUsuarioBloqueo: Service error code received: "
	                    + codigoRespuesta + " - " + descripcionRespuesta);
	             LOGGER.error( new ServiceException(codigoRespuesta, descripcionRespuesta));
	         }
         
         }catch(Exception e){
        	 if(!(e instanceof ServiceException || e instanceof DAOException)){
	        	 LOGGER.error("Exception inesperada al obtener los datos de bloqueo del usuario "
	        			 + rutSinDV + "-" + DV, e);
        	 }
    	 }
   	     
         return bloq;
         
    }
    
    /**
     *  
     * @return bean con los datos de PIN-PUK
     * @throws DAOException, ServiceException
     */
    public PinPukBean consultarPinPuk(String numeroPCS)throws DAOException, ServiceException {
    	
    	ClienteContactoServicePortType port = null;
    	PinPukBean pinPuk = new PinPukBean();
    	
    	try {
             
    		 port = (ClienteContactoServicePortType) WebServiceLocator.getInstance()
                     .getPort(ClienteContactoService.class,
                    		 ClienteContactoServicePortType.class);
    		 
         } catch (WebServiceLocatorException e) {
             LOGGER.error("Error al inicializar el Port "
                     + ClienteContactoService.class, e);
             LOGGER.error( new DAOException(e));
         }
         
         ResultadoConsultarPinPukType response = null;
         try{
        	 
        	 LOGGER.info("Configurando Datos de la peticion");
        	 ConsultarPinPukType request = new ConsultarPinPukType();
    		 request.setMsisdn(numeroPCS);    		 
    		 
    		 try{
	    		 LOGGER.info("Invocando servicio");
	    		 response = port.consultarPinPuk(request);
    		 
	         }catch (Exception e) {
	        	 LOGGER.error("Exception caught on Service invocation: "
	                     + "consultarPinPuk", e);
	        	 LOGGER.error( new DAOException(e));
	         }
    		 
			 String codigoRespuesta = response.getRespuesta().getCodigo();
	         String descripcionRespuesta = response.getRespuesta().getDescripcion();
	         
	         LOGGER.info("codigoRespuesta " + codigoRespuesta);   	        
	         LOGGER.info("descripcionRespuesta " + descripcionRespuesta);
			 
	         if( CODIGO_RESPUESTA_OK.equals(codigoRespuesta) ){
	        	 try{
					PinPukType pp = response.getConsultarPinPukSSCCPP();	    		
					pinPuk.setPin1(pp.getPin1());
					pinPuk.setPin2(pp.getPin2());
					pinPuk.setPuk1(pp.getPuk1());
					pinPuk.setPuk2(pp.getPuk2());
	        	 }catch(Exception e){
	            	 LOGGER.error("Exception caught on Service response: "
	                         + "consultarPinPuk", e);
	            	 LOGGER.error( new DAOException(e));
	        	 }
	         }else {
	            LOGGER.info("consultarPinPuk: Service error code received: "
	                    + codigoRespuesta + " - " + descripcionRespuesta);
	            LOGGER.error( new ServiceException(codigoRespuesta, descripcionRespuesta));
	         }
         
         }catch(Exception e){
        	 if(!(e instanceof ServiceException || e instanceof DAOException)){
        		 LOGGER.error("Exception inesperada al consultar pinPuk para " + numeroPCS, e);
        	 }
    	 }
         
         return pinPuk;
         
    }
    
    /**
     *  
     * @return bean con los datos de situacion actual equipo
     * @throws DAOException, ServiceException
     */
    public SituacionActualEquipoBean obtenerSituacionActualEquipo(
    		String numeroPCS, String rut, String nroCuenta,
    		String rutSinDV, String DV
    		)throws DAOException, ServiceException {
    	
    	ClienteContactoServicePortType port = null;
    	SituacionActualEquipoBean situacionActual = new SituacionActualEquipoBean();
    	
    	 try {
             
    		 port = (ClienteContactoServicePortType) WebServiceLocator.getInstance()
                     .getPort(ClienteContactoService.class,
                    		 ClienteContactoServicePortType.class);
    		 
         } catch (WebServiceLocatorException e) {
             LOGGER.error("Error al inicializar el Port "
                     + ClienteContactoService.class, e);
             LOGGER.error( new DAOException(e));
         }
         
         ResultadoObtenerSituacionActualType response = null;
         try{
        	 
        	 LOGGER.info("Configurando Datos de la peticion");        	 
        	 ObtenerSituacionActualType request = new ObtenerSituacionActualType();
    		 request.setMsisdn(numeroPCS);
    		 request.setNroCuenta(nroCuenta);
    		 request.setRut(rut);
    		 request.setRutSinDV(rutSinDV);
    		 request.setDV(DV);
        	 
    		 try{
    		 
	    		 LOGGER.info("Invocando servicio");
	    		 response = port.obtenerSituacionActual(request);
    		 
	         }catch (Exception e) {
	        	 LOGGER.error("Exception caught on Service invocation: "
	                     + "obtenerSituacionActual", e);
	        	 LOGGER.error( new DAOException(e));
	         }
    		 
		 String codigoRespuesta = response.getRespuesta().getCodigo();
         String descripcionRespuesta = response.getRespuesta().getDescripcion();
        
         LOGGER.info("codigoRespuesta " + codigoRespuesta);   	        
         LOGGER.info("descripcionRespuesta " + descripcionRespuesta);
		 
         if( CODIGO_RESPUESTA_OK.equals(codigoRespuesta) ){
        	 
        	 try{
        		 SituacionActualType situacion = response.getSituacionActual();  
            	 
        		 situacionActual.setCantidadLineasContratadas( parseInt(situacion.getCantidadLineasContratadas()) );
        		 situacionActual.setCuposDisponibles( parseInt(situacion.getCuposDisponibles()) );
        		 situacionActual.setEquiposContratoVigente( parseInt(situacion.getEquiposContratoVigente()) );
        		 
        		 for( EstadoRenovacionEquipoType equipo : response.getEstadoRenovacionEquipo() ){
        			 EstadoRenovacionEquipoBean equipoBean = new EstadoRenovacionEquipoBean();	    			 
        			 equipoBean.setCuotasPagadas( parseInt(equipo.getCuotasPagadas()) );
        			 equipoBean.setCuotasPendientes( parseInt(equipo.getCuotasPendientes()) );
        			 equipoBean.setCuotasTotales( parseInt(equipo.getCuotasTotales()) );
        			 equipoBean.setDescuentoCliente( Math.round( parseDouble(equipo.getDescuentoCliente())*parseDouble(IVA) ) );
        			 equipoBean.setEquipo(equipo.getEquipo());
        			 equipoBean.setNroCuenta(equipo.getNroCuenta());
        			 equipoBean.setParametro12(equipo.getParametro12());
        			 equipoBean.setParametro14(equipo.getParametro14());
        			 equipoBean.setParametro15(equipo.getParametro15());
        			 equipoBean.setParametro3(equipo.getParametro3());
        			 equipoBean.setPendientePagar( Math.round( parseDouble(equipo.getPendientePagar())*parseDouble(IVA)) );
        			 equipoBean.setSerie(equipo.getSerie());
        			 equipoBean.setTotalPagado(Math.round( parseDouble(equipo.getTotalPagado())*parseDouble(IVA)));
        			 equipoBean.setTotalPagar(Math.round( parseDouble(equipo.getTotalPagar())*parseDouble(IVA)));
        			 equipoBean.setValorCuota(Math.round( parseDouble(equipo.getValorCuota())*parseDouble(IVA)));	    			 
        			 situacionActual.addEstadoRenovacionEquipo(equipoBean);	    			 
        		 }
        	 }catch(Exception e){
            	 LOGGER.error("Exception caught on Service response: "
                         + "obtenerSituacionActual", e);
            	 LOGGER.error( new DAOException(e));
        	 }
    		 
         }else {
            LOGGER.info("obtenerSituacionActual: Service error code received: "
                    + codigoRespuesta + " - " + descripcionRespuesta);
            LOGGER.error( new ServiceException(codigoRespuesta, descripcionRespuesta));
         }
         
         }catch(Exception e){
        	 if(!(e instanceof ServiceException || e instanceof DAOException)){
        		 LOGGER.error("Exception inesperada al obtener situacion actual del equipo", e);
        	 }
    	 }
    	
    	return situacionActual;
    }
    
    /**
     *  
     * Metodo encargado de registrar formulario de solicita iphone.
     * @throws DAOException, ServiceException
     */
    public void solicitaIphone(SolicitaIphoneBean solicitaIphone)throws DAOException, ServiceException{
    	
    	ClienteContactoServicePortType port = null;
    	
    	
   	 try {
            
   		 port = (ClienteContactoServicePortType) WebServiceLocator.getInstance()
                    .getPort(ClienteContactoService.class,
                    		ClienteContactoServicePortType.class);
   		 
        } catch (WebServiceLocatorException e) {
            LOGGER.error("Error al inicializar el Port "
                    + ClienteContactoService.class, e);
            LOGGER.error( new DAOException(e));
        }
        
        try{
       	 
       	 LOGGER.info("Configurando Datos de la peticion");        	 
       	 SolicitaIphoneType request = new SolicitaIphoneType();
       	 request.setRut(solicitaIphone.getRut());
       	 request.setMsisdnContacto(solicitaIphone.getMsisdnContacto());
       	 request.setNombreCompleto(solicitaIphone.getNombreCompleto());
       	 request.setNombreFormulario(solicitaIphone.getNombreFormulario());
       	 request.setTelefonoAdicional(solicitaIphone.getTelefonoAdicional());
       	 
       	ResultadoSolicitaIphoneType response = null;
       	try{
       		LOGGER.info("Invocando servicio");
       		response = port.solicitaIphone(request); 
       	}catch (Exception e) {
			 LOGGER.error("Exception caught on Service invocation: "
			            + "solicitaIphone", e);
			 LOGGER.error( new DAOException(e));
		}
       	 
       	 String codigoRespuesta = response.getRespuesta().getCodigo();
  	         String descripcionRespuesta = response.getRespuesta().getDescripcion();
       	 
       	 LOGGER.info("codigoRespuesta " + codigoRespuesta);   	        
  	         LOGGER.info("descripcionRespuesta " + descripcionRespuesta);
   		 
  	         if( !CODIGO_RESPUESTA_OK.equals(codigoRespuesta) ){   	        	    	         
   	            LOGGER.info("solicitaIphone: Service error code received: "
                           + codigoRespuesta + " - " + descripcionRespuesta);        
      	            LOGGER.error( new ServiceException(codigoRespuesta, descripcionRespuesta));
      	     }
       	 
        }catch (Exception e) {
        	if(!(e instanceof ServiceException || e instanceof DAOException)){
        		LOGGER.error("Exception inesperada al solicitar Iphone", e);
        	}
        }    
    	
    }
    
    /**
     *  
     * Metodo encargado de registrar contacto por renovacion.
     * @throws DAOException, ServiceException
     */
    public void contactoPorRenovacion(ContactoPorRenovacionBean contacto)throws DAOException, ServiceException{
    	
    	ClienteContactoServicePortType port = null;
    	
    	
    	 try {
             
    		 port = (ClienteContactoServicePortType) WebServiceLocator.getInstance()
                     .getPort(ClienteContactoService.class,
                    		 ClienteContactoServicePortType.class);
    		 
         } catch (WebServiceLocatorException e) {
             LOGGER.error("Error al inicializar el Port "
                     + ClienteContactoService.class, e);
             LOGGER.error( new DAOException(e));
         }
         
         try{
        	 
        	 LOGGER.info("Configurando Datos de la peticion");        	 
        	 SolicitudRenovacionContactoType request = new SolicitudRenovacionContactoType();
        	 request.setRut(contacto.getRut());
        	 request.setMsisdnContacto(contacto.getMsisdnContacto());
        	 request.setNombreCompleto(contacto.getNombreCompleto());
        	 request.setNombreFormulario(contacto.getNombreFormulario());
        	 request.setTelefonoAdicional(contacto.getTelefonoAdicional());
        	 
        	 ResultadoSolicitudRenovacionContactoType response = null;
        	 try{
        		 LOGGER.info("Invocando servicio");
        		 response = port.solicitudRenovacionContacto(request);
        	 }catch (Exception e) {
            	 LOGGER.error("Exception caught on Service invocation: "
                         + "solicitudRenovacionContacto", e);
            	 LOGGER.error( new DAOException(e));
             }
        	 
        	 String codigoRespuesta = response.getRespuesta().getCodigo();
   	         String descripcionRespuesta = response.getRespuesta().getDescripcion();
        	 
        	 LOGGER.info("codigoRespuesta " + codigoRespuesta);   	        
   	         LOGGER.info("descripcionRespuesta " + descripcionRespuesta);
    		 
   	         if( !CODIGO_RESPUESTA_OK.equals(codigoRespuesta) ){   	        	    	         
    	            LOGGER.info("solicitudRenovacionContacto: Service error code received: "
                            + codigoRespuesta + " - " + descripcionRespuesta);        
       	            LOGGER.error( new ServiceException(codigoRespuesta, descripcionRespuesta));
       	     }
        	 
         }catch (Exception e) {
        	 if(!(e instanceof ServiceException || e instanceof DAOException)){
        		 LOGGER.error("Excepcion inesperada al solicitar contacto por renovacion", e);
        	 }
         }    
         
    }
        
    /**
     * Aceptar - Rechazar presupuesto de OT
     *  
     * @throws DAOException,  ServiceException
     */
    public void aceptarRechazarPresupuesto(String nroOrden, String accion)
            throws DAOException, ServiceException {

    	ClienteProblemaServicePortType port = null;
    	
    	try {
            
   		 port = (ClienteProblemaServicePortType) WebServiceLocator.getInstance()
                    .getPort(ClienteProblemaService.class,
                    		ClienteProblemaServicePortType.class);
   		 
        } catch (WebServiceLocatorException e) {
            LOGGER.error("Error al inicializar el Port "
                    + ClienteProblemaService.class, e);
            LOGGER.error( new DAOException(e));
        }
        
        try{
       	 
       	 LOGGER.info("Configurando Datos de la peticion");        	 
       	 AceptarRechazarPresupuestoType request = new AceptarRechazarPresupuestoType();
       	 request.setNroOrden(nroOrden);       	 
       	 request.setAccion(accion);
       	 
       	 ResultadoAceptarRechazarPresupuestoType response = null;
       	 try{
       		LOGGER.info("Invocando servicio");
       		response = port.aceptarRechazarPresupuesto(request);
       	 }catch (Exception e) {
	       	 LOGGER.error("Exception caught on Service invocation: "
	                    + "aceptarRechazarPresupuesto", e);
	       	 LOGGER.error( new DAOException(e));
         }
       	 
       	 String codigoRespuesta = response.getRespuesta().getCodigo();
  	         String descripcionRespuesta = response.getRespuesta().getDescripcion();
       	 
       	 LOGGER.info("codigoRespuesta " + codigoRespuesta);   	        
  	         LOGGER.info("descripcionRespuesta " + descripcionRespuesta);
   		 
  	         if( !CODIGO_RESPUESTA_OK.equals(codigoRespuesta) ){   	        	    	         
   	            LOGGER.info("aceptarRechazarPresupuesto: Service error code received: "
                           + codigoRespuesta + " - " + descripcionRespuesta);        
      	            LOGGER.error( new ServiceException(codigoRespuesta, descripcionRespuesta));
      	     }
       	 
        }catch (Exception e) {
        	if(!(e instanceof ServiceException || e instanceof DAOException)){
        		LOGGER.error("Exception inesperada al aceptar/rechazar presupuesto", e);
        	}
        }    

        
    }
    
    public void solicitaLineaAdicional(String msisdn, String nombre, String formulario, String rut, String telefonoAdicional) throws DAOException, ServiceException {
        ClienteContactoServicePortType port;
        LOGGER.info("Instanciando el port " + ClienteContactoServicePortType.class);
        try {
            port = (ClienteContactoServicePortType) WebServiceLocator.getInstance().getPort(
            		ClienteContactoService.class, ClienteContactoServicePortType.class);            

            SolicitaLineaAdicionalType request = new SolicitaLineaAdicionalType();
            ResultadoSolicitaLineaAdicionalType response = null;

            try {
                LOGGER.info("Configurando Datos de la peticion");
                
                request.setMsisdnContacto(msisdn);
                request.setNombreCompleto(nombre);
                request.setNombreFormulario(formulario);
                request.setRut(rut);
                request.setTelefonoAdicional(telefonoAdicional);
                
                LOGGER.info("Invocando servicio");
                response = port.solicitaLineaAdicional(request);
            } catch (Exception e) {
                LOGGER.error("Exception caught on Service invocation: "
                        + "solicitaLineaAdicional", e);
                LOGGER.error( new DAOException(e));
            }

            String codigoRespuesta = response.getRespuesta().getCodigo();
            String descripcionRespuesta = response.getRespuesta()
                    .getDescripcion();

            LOGGER.info("codigoRespuesta " + codigoRespuesta);
            LOGGER.info("descripcionRespuesta " + descripcionRespuesta);

            if (Utils.isEmptyString(codigoRespuesta)) {
                LOGGER.error( new DAOException("solicitaLineaAdicional: Servicio no responde "
                        + "con codigoRespuesta"));
            }

            if (CODIGO_RESPUESTA_OK.equals(codigoRespuesta)) {
                try {
                    return;
                } catch (Exception e) {
                    LOGGER.error("Exception caught on Service response: "
                            + "solicitaLineaAdicional", e);
                    LOGGER.error( new DAOException(e));
                }
            }
            else {
                LOGGER.info("solicitaLineaAdicional: Service error code received: "
                        + codigoRespuesta + " - " + descripcionRespuesta);
                LOGGER.error( new ServiceException(codigoRespuesta,
                        descripcionRespuesta));
            }
        } catch (WebServiceLocatorException e) {
            LOGGER.error("Error al inicializar el Port " + ClienteContactoServicePortType.class, e);
            LOGGER.error( new DAOException(e));
        }        
    }
    
    public EquiposContratadosYArriendoMPTBean consultarEquiposContratadosYArriendo(
            String msisdn, RutBean rut, String nroCuenta) throws DAOException, ServiceException {

        ClienteContactoServicePortType port;
        EquiposContratadosYArriendoMPTBean bean = new EquiposContratadosYArriendoMPTBean();
        LOGGER.info("Instanciando el port " + ClienteContactoServicePortType.class);
        try {
            port = (ClienteContactoServicePortType) WebServiceLocator.getInstance()
                    .getPort(ClienteContactoService.class,
                            ClienteContactoServicePortType.class);

            ConsultarEquiposContratadosYArriendoMPTType request = new ConsultarEquiposContratadosYArriendoMPTType();
            ResultadoConsultarEquiposContratadosYArriendoMPTType response = null;

            try {

                LOGGER.info("Configurando Datos de la peticion");

                request.setMsisdn(msisdn);
                request.setRut(rut.getStringValue());
                request.setNroCuenta(nroCuenta);
                request.setRutSinDV(String.valueOf(rut.getNumero()));
                request.setDV(String.valueOf(rut.getDigito()));

                LOGGER.info("Invocando servicio");
                response = port
                        .consultarEquiposContratadosYArriendoMPT(request);

            } catch (Exception e) {
                LOGGER.error("Exception caught on Service invocation: "
                        + "consultarEquiposContratadosYArriendoMPT", e);
                LOGGER.error( new DAOException(e));
            }

            String codigoRespuesta = response.getRespuesta().getCodigo();
            String descripcionRespuesta = response.getRespuesta()
                    .getDescripcion();

            LOGGER.info("codigoRespuesta " + codigoRespuesta);
            LOGGER.info("descripcionRespuesta " + descripcionRespuesta);

            if (Utils.isEmptyString(codigoRespuesta)) {
               LOGGER.error( new DAOException(
                        "consultarEquiposContratadosYArriendoMPT: Servicio no responde "
                                + "con codigoRespuesta"));
            }

            if (CODIGO_RESPUESTA_OK.equals(codigoRespuesta)) {
                try {
                    EquiposContratadosYArriendoMPT datosArriendo = response
                            .getEquiposContratadosYArriendoMPT();
                    bean.setCantidadLineasContratadas(Integer
                            .parseInt(datosArriendo
                                    .getCantidadLineasContratadas()));
                    bean
                            .setEquiposContratoVigente(Integer
                                    .parseInt(datosArriendo
                                            .getEquiposContratoVigente()));
                    return bean;
                } catch (Exception e) {
                    LOGGER.error("Exception caught on Service response: "
                            + "consultarEquiposContratadosYArriendoMPT", e);
                    LOGGER.error( new DAOException(e));
                }
            }
            else {
                LOGGER
                        .info("consultarEquiposContratadosYArriendoMPT: Service error code received: "
                                + codigoRespuesta
                                + " - "
                                + descripcionRespuesta);
                LOGGER.error( new ServiceException(codigoRespuesta,
                        descripcionRespuesta));
            }
        } catch (WebServiceLocatorException e) {
            LOGGER.error("Error al inicializar el Port "
                    + ClienteContactoServicePortType.class, e);
            LOGGER.error( new DAOException(e));
        }
        return new EquiposContratadosYArriendoMPTBean();
    }
    
    public ServicioTecnicoMPTBean consultarServicioTecnico(String msisdn)
            throws DAOException, ServiceException {
        ClienteProblemaServicePortType port;
        ServicioTecnicoMPTBean bean = new ServicioTecnicoMPTBean();
        LOGGER.info("Instanciando el port " + ClienteContactoServicePortType.class);
        try {
            port = (ClienteProblemaServicePortType) WebServiceLocator.getInstance()
                    .getPort(ClienteProblemaService.class,
                            ClienteProblemaServicePortType.class);

            ConsultarServicioTecnicoMPTType request = new ConsultarServicioTecnicoMPTType();
            ResultadoConsultarServicioTecnicoMPTType response = null;

            try {

                LOGGER.info("Configurando Datos de la peticion");

                request.setMsisdn(msisdn);

                LOGGER.info("Invocando servicio");
                response = port.consultarServicioTecnicoMPT(request);

            } catch (Exception e) {
                LOGGER.error("Exception caught on Service invocation: "
                        + "consultarServicioTecnicoMPT", e);
                LOGGER.error( new DAOException(e));
            }

            String codigoRespuesta = response.getRespuesta().getCodigo();
            String descripcionRespuesta = response.getRespuesta()
                    .getDescripcion();

            LOGGER.info("codigoRespuesta " + codigoRespuesta);
            LOGGER.info("descripcionRespuesta " + descripcionRespuesta);

            if (Utils.isEmptyString(codigoRespuesta)) {
                LOGGER.error( new DAOException(
                        "consultarServicioTecnicoMPT: Servicio no responde "
                                + "con codigoRespuesta"));
            }

            if (CODIGO_RESPUESTA_OK.equals(codigoRespuesta)) {
                try {
                    for (OrdenesDeTrabajoMPTType orden : response
                            .getServicioTecnicoMPT().getOrdenesDeTrabajo()) {
                        OrdenTrabajoMPTBean ordenBean = new OrdenTrabajoMPTBean();
                        ordenBean.setCodEstado(orden.getCodEstado());
                        ordenBean.setDescEstado(orden.getDescEstado());
                        bean.addOrdenBean(ordenBean);
                    }
                    return bean;
                } catch (Exception e) {
                    LOGGER.error("Exception caught on Service response: "
                            + "consultarServicioTecnicoMPT", e);
                    LOGGER.error( new DAOException(e));
                }
            }
            else {
                LOGGER.info("consultarServicioTecnicoMPT: Service error code received: "
                                + codigoRespuesta
                                + " - "
                                + descripcionRespuesta);
                LOGGER.error( new ServiceException(codigoRespuesta,
                        descripcionRespuesta));
            }
        } catch (WebServiceLocatorException e) {
            LOGGER.error("Error al inicializar el Port "
                    + ClienteContactoServicePortType.class, e);
            LOGGER.error( new DAOException(e));
        }
        return new ServicioTecnicoMPTBean();
    }    

	private double parseDouble(String value){
    	try{
    		return Double.parseDouble(value);
    	}catch (Exception e) {
    		LOGGER.error("Error parsing Double : "+value+" - parseDouble", e);
			return 0;
		}
    }
    
    private int parseInt(String value){
    	try{
    		return Integer.parseInt(value);
    	}catch (Exception e) {
    		LOGGER.error("Error parsing Integer : "+value+" - parseInt", e);
			return 0;
		}
    }
    
    
    /**
     * Obtener los datos del equipo del usuario desde el servicio
     * ClientePerfilService
     *  
     * @return bean con los datos del equipo del usuario en Sesion
     * @throws DAOException
     */
    public EquipoFullBean getEquipoCompatibilidad(String numeroPcs)
            throws DAOException, ServiceException {
        
        ClientePerfilServicePortType port;
        EquipoFullBean equipoFullBean = new EquipoFullBean();
        try {
            LOGGER.info("Instanciando el port");
            port = (ClientePerfilServicePortType) WebServiceLocator.getInstance()
                    .getPort(ClientePerfilService.class,
                            ClientePerfilServicePortType.class);
            
            LOGGER.info("Configurando Datos de la peticion");
            ConsultarEquipoFullType consultaEquipoFullTypeRequest = new ConsultarEquipoFullType();
            consultaEquipoFullTypeRequest.setMsisdn(numeroPcs);

            ResultadoConsultarEquipoFullType consultaEquipoFullTypeResponse = null;
            try{
            	LOGGER.info("Invocando servicio");
	            consultaEquipoFullTypeResponse = port
	            	.consultarEquipoFull(consultaEquipoFullTypeRequest);
            
            }catch (Exception e) {
	   	       	 LOGGER.error("Exception caught on Service invocation: "
	   	                    + "consultarEquipoFull", e);
	   	       	 LOGGER.error( new DAOException(e));
            }
            
            String codigoRespuesta = consultaEquipoFullTypeResponse.getRespuesta()
                    .getCodigo();
            String descripcionRespuesta = consultaEquipoFullTypeResponse
                    .getRespuesta().getDescripcion();
            
            LOGGER.info("Codigo de Respuesta :"+codigoRespuesta);
            LOGGER.info("Descipcion de Respuesta :"+descripcionRespuesta);
            
            if (CODIGO_RESPUESTA_OK.equals(codigoRespuesta)) {
            	
            	try{
            		EquipoFullType equipoFullType = consultaEquipoFullTypeResponse.getEquipo();
                    
                    LOGGER.info("Configurando el Bean de Negocio");
                    equipoFullBean = new EquipoFullBean();
                    equipoFullBean.setModelo(equipoFullType.getModeloEquipo());
                    equipoFullBean.setUrlImagen(equipoFullType.getUrlImagen());
                    equipoFullBean.setMarca(equipoFullType.getMarcaEquipo());  
                    equipoFullBean.setWappushEnabled(equipoFullType.getWapEnabled());
                    equipoFullBean.setBkpContSIM(equipoFullType.getBkpContSIM());
                    equipoFullBean.setBkpContSW(equipoFullType.getBkpContSW());
                    equipoFullBean.setBkpContSyncML(equipoFullType.getBkpContSyncML());
                    
                    MiEntelProperties.getProperty("servicios.codigoRespuesta.exito");
            	}catch (Exception e) {
	   	   	       	 LOGGER.error("Exception caught on Service response: "
		   	                    + "consultarEquipoFull", e);
		   	       	 LOGGER.error( new DAOException(e));
            	}

            }
            else if( CODIGO_RESPUESTA_NO_VALIDO.equals(codigoRespuesta) || CODIGO_RESPUESTA_NO_RECONOCIDO.equals(codigoRespuesta) ){
                LOGGER.info("Configurando el Bean de Negocio - Movil no reconocido");
                equipoFullBean = new EquipoFullBean();
                equipoFullBean.setModelo("");
                equipoFullBean.setUrlImagen("");
                equipoFullBean.setWappushEnabled("");
                equipoFullBean.setBkpContSIM("");
                equipoFullBean.setBkpContSW("");
                equipoFullBean.setBkpContSyncML("");
                equipoFullBean.setMarca(EQUIPO_NO_RECONOCIDO);
            }
            else {
                LOGGER.info("consultarEquipoFull: Service error code received: " 
                		+ codigoRespuesta + " - " + descripcionRespuesta);
                LOGGER.error( new ServiceException(codigoRespuesta,
                        descripcionRespuesta));
            }

        } catch (WebServiceLocatorException e) {
        	LOGGER.error("Error al inicializar el Port "
                    + ClientePerfilServicePortType.class, e);
            LOGGER.error( new DAOException(e));

        }
        LOGGER.info("END");
        return equipoFullBean;
    }  
    
    
    
    /**
     * Obtener los datos del equipo del usuario 
     * 
     * @return bean con los datos del equipo del usuario en Sesion y equipo bloqueado
     * @throws DAOException
     * @throws ConsultaEquipoRobadoFaultMessage 
     */
    public ConsultarBloqueoDesbloqueoBean getConsultarBloqueoDesbloqueo(String msisdn , String mercado , String imeiDV) throws DAOException, ServiceException {
        
        ClienteContactoServicePortType port = null;
        ConsultaEquipoRobadoPortType port2 = null;
        
        LOGGER.info("ENTRANDO A CONSULTAR CONSULTAR BLOQUEO DESBLOQUEO");
        
        ConsultarBloqueoDesbloqueoBean consultarBloqueoDesbloqueoBean;
        try {
            LOGGER.info("Instanciando el port");
            port = (ClienteContactoServicePortType) WebServiceLocator.getInstance().getPort(ClienteContactoService.class, ClienteContactoServicePortType.class);
        }catch (WebServiceLocatorException e) {
        	LOGGER.error("Error al inicializar el Port "
                    + ClienteContactoServicePortType.class, e);
            LOGGER.error( new DAOException(e));
        }
            
        ConsultarBloqueoDesbloqueoType consultarBloqueoDesbloqueoTypeRequest = new ConsultarBloqueoDesbloqueoType();
		
        LOGGER.info("Configurando Datos de la peticion");
		consultarBloqueoDesbloqueoTypeRequest.setMsisdn(msisdn);
		consultarBloqueoDesbloqueoTypeRequest.setMercado(mercado);
		consultarBloqueoDesbloqueoTypeRequest.setImeiDV(imeiDV);
		
		LOGGER.info("consultarBloqueoDesbloqueoTypeRequest.getMsisdn() ::::" + consultarBloqueoDesbloqueoTypeRequest.getMsisdn());
		LOGGER.info("consultarBloqueoDesbloqueoTypeRequest.getMercado():::"  + consultarBloqueoDesbloqueoTypeRequest.getMercado());
		LOGGER.info("consultarBloqueoDesbloqueoTypeRequest.getImeiDV() ::::" + consultarBloqueoDesbloqueoTypeRequest.getImeiDV());
		
		ResultadoConsultarBloqueoDesbloqueoType consultarBloqueoDesbloqueoTypeResponse = null;
		try{
			LOGGER.info("Invocando servicio");
			consultarBloqueoDesbloqueoTypeResponse = port.consultarBloqueoDesbloqueo(consultarBloqueoDesbloqueoTypeRequest);
		}catch (Exception e) {
			LOGGER.error("Exception caught on Service invocation: "
	                    + "consultarBloqueoDesbloqueo", e);
	       	 LOGGER.error( new DAOException(e));
		}		
		
		String codigoRespuesta = consultarBloqueoDesbloqueoTypeResponse.getRespuesta().getCodigo();
		String descripcionRespuesta = consultarBloqueoDesbloqueoTypeResponse.getRespuesta().getDescripcion();
		
		EquipoActualType equipoActualType = consultarBloqueoDesbloqueoTypeResponse.getEquipoActual();
		consultarBloqueoDesbloqueoBean = new ConsultarBloqueoDesbloqueoBean();
		
		LOGGER.info("CODIGO RESPUESTA PORT ::::: "+codigoRespuesta);
		
		codigoRespuesta= (codigoRespuesta==null || codigoRespuesta=="")?"0000":codigoRespuesta;		
		
		  if (codigoRespuesta.equals(CODIGO_RESPUESTA_OK) || (Integer.parseInt(codigoRespuesta)>=Integer.parseInt(CODIGO_RESPUESTA_BLOQUEO_OPCION1) && Integer.parseInt(codigoRespuesta)<=Integer.parseInt(CODIGO_RESPUESTA_BLOQUEO_OPCION10))) {
			
			LOGGER.info("Configurando el Bean de Negocio para EquipoActualType --> "+consultarBloqueoDesbloqueoTypeResponse.getEquipoActual());
			
			consultarBloqueoDesbloqueoBean.setEquipoActual(new EquipoActualBean());
			
			consultarBloqueoDesbloqueoBean.setCodigoRespuesta(codigoRespuesta);
			consultarBloqueoDesbloqueoBean.setDescripcionRespuesta(descripcionRespuesta);
			consultarBloqueoDesbloqueoBean.getEquipoActual().setImei(equipoActualType.getImei());
			consultarBloqueoDesbloqueoBean.getEquipoActual().setImsi(equipoActualType.getImsi());
			consultarBloqueoDesbloqueoBean.getEquipoActual().setIccid(equipoActualType.getIccid());
			consultarBloqueoDesbloqueoBean.getEquipoActual().setDescripcionEquipo(equipoActualType.getDescripcionEquipo());
			consultarBloqueoDesbloqueoBean.getEquipoActual().setMarca(equipoActualType.getMarca());
			consultarBloqueoDesbloqueoBean.getEquipoActual().setModelo(equipoActualType.getModelo());
			
			if ((Integer.parseInt(codigoRespuesta)>=5000 && Integer.parseInt(codigoRespuesta)<=5010) ) {
				
				LOGGER.info("EL MOVIL ESTA BLOQUEADO ENTRANDO A VERIFICAR SI ESTA BLOQUEADO EN WS:::: " + codigoRespuesta );

				DatosEquipoRobadoType datosEquipoRobadoType = consultarBloqueoDesbloqueoTypeResponse.getDatosEquipoRobado();
						
						
						if(datosEquipoRobadoType != null){
							
							LOGGER.info("Configurando el Bean de Negocio para DatosEquipoRobadoType ");
							
							consultarBloqueoDesbloqueoBean.setDatosEquipoRobado(new DatosEquipoRobadoBean());
							consultarBloqueoDesbloqueoBean.getDatosEquipoRobado().setCodOperacion(datosEquipoRobadoType.getCodOperacion());
							consultarBloqueoDesbloqueoBean.getDatosEquipoRobado().setCodCompEnvio(datosEquipoRobadoType.getCodCompEnvio());
							consultarBloqueoDesbloqueoBean.getDatosEquipoRobado().setCodImei(datosEquipoRobadoType.getCodImei());
							consultarBloqueoDesbloqueoBean.getDatosEquipoRobado().setFechaPerdida(datosEquipoRobadoType.getFechaPerdida());
							consultarBloqueoDesbloqueoBean.getDatosEquipoRobado().setFechaIoper(datosEquipoRobadoType.getFechaIoper());
							consultarBloqueoDesbloqueoBean.getDatosEquipoRobado().setFechaDenuncia(datosEquipoRobadoType.getFechaDenuncia());
							consultarBloqueoDesbloqueoBean.getDatosEquipoRobado().setDescMarcaEquipo(datosEquipoRobadoType.getDescMarcaEquipo());
							consultarBloqueoDesbloqueoBean.getDatosEquipoRobado().setDescModeloEquipo(datosEquipoRobadoType.getDescModeloEquipo());
							consultarBloqueoDesbloqueoBean.getDatosEquipoRobado().setDescTecnologia(datosEquipoRobadoType.getDescTecnologia());
							consultarBloqueoDesbloqueoBean.getDatosEquipoRobado().setIndcModalidad(datosEquipoRobadoType.getIndcModalidad());
							consultarBloqueoDesbloqueoBean.getDatosEquipoRobado().setNmroTelefono(datosEquipoRobadoType.getNmroTelefono());
							consultarBloqueoDesbloqueoBean.getDatosEquipoRobado().setRazonBloqueo(datosEquipoRobadoType.getRazonBloqueo());
							consultarBloqueoDesbloqueoBean.getDatosEquipoRobado().setErrorCode(datosEquipoRobadoType.getErrorCode());
							consultarBloqueoDesbloqueoBean.getDatosEquipoRobado().setDescError(datosEquipoRobadoType.getDescError());
							consultarBloqueoDesbloqueoBean.getDatosEquipoRobado().setFechaEjecucion(datosEquipoRobadoType.getFechaEjecucion());
							consultarBloqueoDesbloqueoBean.getDatosEquipoRobado().setCodImsi(datosEquipoRobadoType.getCodImsi());
							consultarBloqueoDesbloqueoBean.getDatosEquipoRobado().setCodTerminal(datosEquipoRobadoType.getCodTerminal());
						
						}else{
							
							LOGGER.info("ENTRANDO A CONSULTAR EL SERVICIO ConsultarEquipoRobadoService");
							
							try {
					            LOGGER.info("Instanciando el port");
					            port2 = (ConsultaEquipoRobadoPortType) WebServiceLocator.getInstance().getPort(ConsultaEquipoRobadoServices.class, ConsultaEquipoRobadoPortType.class);
					        }catch (WebServiceLocatorException e) {
					            LOGGER.error("ServiceLocatorException caught on Service port request",e);
					            LOGGER.error( new DAOException(e));
					        }
					        
					        LOGGER.info("Configurando Datos de la peticion");
					        ConsultaEquipoRobadoRequestDocumentType consultaEquipoRobadoRequestDocumentTypeRequest = new ConsultaEquipoRobadoRequestDocumentType();
					        
					        consultaEquipoRobadoRequestDocumentTypeRequest.setImei(consultarBloqueoDesbloqueoBean.getEquipoActual().getImei().concat(imeiDV));
					        
					        ConsultaEquipoRobadoResponseDocumentType consultaEquipoRobadoResponseDocumentType = null;
					        try{
					        	LOGGER.info("Invocando servicio");
					        	consultaEquipoRobadoResponseDocumentType = port2.consultaEquipoRobadoPortType(consultaEquipoRobadoRequestDocumentTypeRequest);
					        }catch (Exception e) {
								LOGGER.error("Exception caught on Service invocation: "
					                    + "consultaEquipoRobadoPortType", e);
								LOGGER.error( new DAOException(e));
					        }
					        
					        if(consultaEquipoRobadoResponseDocumentType != null){
					        	
								LOGGER.info("Configurando el Bean de Negocio para DatosEquipoRobadoType ");
								
								consultarBloqueoDesbloqueoBean.setDatosEquipoRobado(new DatosEquipoRobadoBean());
								consultarBloqueoDesbloqueoBean.getDatosEquipoRobado().setCodOperacion(consultaEquipoRobadoResponseDocumentType.getCodiOperacion());
								consultarBloqueoDesbloqueoBean.getDatosEquipoRobado().setCodCompEnvio(consultaEquipoRobadoResponseDocumentType.getCodiCompEnvio());
								consultarBloqueoDesbloqueoBean.getDatosEquipoRobado().setCodImei(consultaEquipoRobadoResponseDocumentType.getCodiImei());
								consultarBloqueoDesbloqueoBean.getDatosEquipoRobado().setFechaPerdida(consultaEquipoRobadoResponseDocumentType.getFechPerdida());
								consultarBloqueoDesbloqueoBean.getDatosEquipoRobado().setFechaIoper(consultaEquipoRobadoResponseDocumentType.getFechIoper());
								consultarBloqueoDesbloqueoBean.getDatosEquipoRobado().setFechaDenuncia(consultaEquipoRobadoResponseDocumentType.getFechDenuncia());
								consultarBloqueoDesbloqueoBean.getDatosEquipoRobado().setDescMarcaEquipo(consultaEquipoRobadoResponseDocumentType.getDescMarcaEqui());
								consultarBloqueoDesbloqueoBean.getDatosEquipoRobado().setDescModeloEquipo(consultaEquipoRobadoResponseDocumentType.getDescModeloEqui());
								consultarBloqueoDesbloqueoBean.getDatosEquipoRobado().setDescTecnologia(consultaEquipoRobadoResponseDocumentType.getDescTecnologia());
								consultarBloqueoDesbloqueoBean.getDatosEquipoRobado().setIndcModalidad(consultaEquipoRobadoResponseDocumentType.getIndcModalidad());
								consultarBloqueoDesbloqueoBean.getDatosEquipoRobado().setNmroTelefono(consultaEquipoRobadoResponseDocumentType.getNmroTelefono());
								consultarBloqueoDesbloqueoBean.getDatosEquipoRobado().setRazonBloqueo(consultaEquipoRobadoResponseDocumentType.getRazonBloqueo());
								consultarBloqueoDesbloqueoBean.getDatosEquipoRobado().setErrorCode(consultaEquipoRobadoResponseDocumentType.getErrorCode());
								consultarBloqueoDesbloqueoBean.getDatosEquipoRobado().setDescError(consultaEquipoRobadoResponseDocumentType.getDescError());
								consultarBloqueoDesbloqueoBean.getDatosEquipoRobado().setFechaEjecucion(consultaEquipoRobadoResponseDocumentType.getFechaEjecucion());
								consultarBloqueoDesbloqueoBean.getDatosEquipoRobado().setCodImsi(consultaEquipoRobadoResponseDocumentType.getCodiImsi());
								consultarBloqueoDesbloqueoBean.getDatosEquipoRobado().setCodTerminal(consultaEquipoRobadoResponseDocumentType.getCodiTerminal());

					        }
						}
			
			}
			
		}else if(CODIGO_RESPUESTA_DATOS_INCOSINTENTES.equals(codigoRespuesta)) {
			consultarBloqueoDesbloqueoBean.setCodigoRespuesta(codigoRespuesta);
			consultarBloqueoDesbloqueoBean.setDescripcionRespuesta(descripcionRespuesta);
		}else if(CODIGO_RESPUESTA_PENDIENTE_BLOQUEO.equals(codigoRespuesta)) {
			consultarBloqueoDesbloqueoBean.setCodigoRespuesta(codigoRespuesta);
			consultarBloqueoDesbloqueoBean.setDescripcionRespuesta(descripcionRespuesta);
		}else if(CODIGO_RESPUESTA_PENDIENTE_DESBLOQUEO.equals(codigoRespuesta)) {
			consultarBloqueoDesbloqueoBean.setCodigoRespuesta(codigoRespuesta);
			consultarBloqueoDesbloqueoBean.setDescripcionRespuesta(descripcionRespuesta);
		}else if(CODIGO_RESPUESTA_PENDIENTE_DESBLOQUEO.equals(codigoRespuesta)) {
			consultarBloqueoDesbloqueoBean.setCodigoRespuesta(codigoRespuesta);
			consultarBloqueoDesbloqueoBean.setDescripcionRespuesta(descripcionRespuesta);	
		}else if(CODIGO_RESPUESTA_ERROR_DESBLOQUEO.equals(codigoRespuesta)) {
			consultarBloqueoDesbloqueoBean.setCodigoRespuesta(codigoRespuesta);
			consultarBloqueoDesbloqueoBean.setDescripcionRespuesta(descripcionRespuesta);	
		}else if(CODIGO_RESPUESTA_ERROR_BLOQUEO.equals(codigoRespuesta)) {
			consultarBloqueoDesbloqueoBean.setCodigoRespuesta(codigoRespuesta);
			consultarBloqueoDesbloqueoBean.setDescripcionRespuesta(descripcionRespuesta);	
		}
		else{
			 LOGGER.error("consultarBloqueoDesbloqueo: Service error code received: " + codigoRespuesta+ " - " + descripcionRespuesta);
			 LOGGER.error( new ServiceException(codigoRespuesta,descripcionRespuesta));
		}
	
		codigoRespuesta=null;
        LOGGER.info("END");
        return consultarBloqueoDesbloqueoBean;
    }  
    
    /**
     * Obtener los datos BUIC
     * 
     * @return bean con los datos BUIC
     * @throws DAOException
     */
    public ConsultarDatosBuicBean getConsultarDatosBuic(String mercado , String  rutSinDV , String rutDV) throws DAOException, ServiceException {
    	
    	
    	VerificacionBloqueoPortType port;
        ConsultarDatosBuicBean consultarDatosBuicBean = new ConsultarDatosBuicBean();

        try {

            port = (VerificacionBloqueoPortType) WebServiceLocator
            		.getInstance().getPort(BloqueoSubtelService.class, 
            				VerificacionBloqueoPortType.class);
            
            LOGGER.info("Configurando Datos de la peticion");
            ConsultarDatosBuicType consultarDatosBuicTypeRequest = new ConsultarDatosBuicType();
            consultarDatosBuicTypeRequest.setMercado(mercado);
            consultarDatosBuicTypeRequest.setRut(rutSinDV);
            consultarDatosBuicTypeRequest.setDv(rutDV);
            
            ResultadoConsultarDatosBuicType resultadoConsultarDatosBuicTypeResponse = null;
            try{
            	LOGGER.info("Invocando servicio");
            	resultadoConsultarDatosBuicTypeResponse = port.consultarDatosBuicOperation(consultarDatosBuicTypeRequest);
            }catch (Exception e) {
            	LOGGER.error("Exception caught on Service invocation: "
	                    + "consultarDatosBuicOperation", e);
				LOGGER.error( new DAOException(e));
			}
            
            String codigoRespuesta = resultadoConsultarDatosBuicTypeResponse.getRespuesta().getCodigo();
            String descripcionRespuesta = resultadoConsultarDatosBuicTypeResponse.getRespuesta().getDescripcion();
            
            if (CODIGO_RESPUESTA_OK.equals(codigoRespuesta) ) {
            	
            	try{
	            	DatosBuicType datosBuicType = resultadoConsultarDatosBuicTypeResponse.getConsultarDatosBuic();
	            	
	            	consultarDatosBuicBean = new ConsultarDatosBuicBean();
	            	
	            	consultarDatosBuicBean.setEmailUsuario(datosBuicType.getEmailUsuario());
	            	consultarDatosBuicBean.setEmailDominio(datosBuicType.getEmailDominio());
	            	consultarDatosBuicBean.setAreaFono(datosBuicType.getAreaFono());
	            	consultarDatosBuicBean.setNumeroFono(datosBuicType.getNumeroFono());
	            	consultarDatosBuicBean.setDireccion(datosBuicType.getDireccion());
	            	consultarDatosBuicBean.setNumeroDomicilio(datosBuicType.getNumeroDomicilio());
	            	consultarDatosBuicBean.setDeptoOff(datosBuicType.getDeptoOff());
	            	consultarDatosBuicBean.setRegion(datosBuicType.getRegion());
	            	consultarDatosBuicBean.setCiudad(datosBuicType.getCiudad());
	            	consultarDatosBuicBean.setComuna(datosBuicType.getComuna());
            	}catch (Exception e) {
                    LOGGER.error("Exception caught on Service response: "
                            + "consultarDatosBuicOperation", e);
                    LOGGER.error( new DAOException(e));
                }
            	
            }else {
                LOGGER.info("consultarDatosBuicOperation: Service error code received: " 
                		+ codigoRespuesta+ " - " + descripcionRespuesta);
                LOGGER.error( new ServiceException(codigoRespuesta,descripcionRespuesta));
            }

        }catch (WebServiceLocatorException e) {
        	LOGGER.error("Error al inicializar el Port "
                    + VerificacionBloqueoPortType.class, e);
            LOGGER.error( new DAOException(e));
        }
        LOGGER.info("END");
        return consultarDatosBuicBean;
    }
    
    /**
     * Valida clave para desbloquear
     * 
     * @return String con el codigo de la respuesta
     * @throws DAOException
     */
    public String validarClaveDesbloqueo(String mercado , String  numeroPcs , String claveBloqueo) throws DAOException, ServiceException {
        
    	VerificacionBloqueoPortType port = null;
    	String respuestaValidarClaveDesbloqueo = null;
    	
    	LOGGER.info("EN EL DAO VALIDAR CLAVE :::" + mercado + ":::::" + numeroPcs + " ::::" + claveBloqueo );
    	
        try {
            port = (VerificacionBloqueoPortType) WebServiceLocator
            		.getInstance().getPort(BloqueoSubtelService.class, 
            				VerificacionBloqueoPortType.class);
        }catch (WebServiceLocatorException e) {
        	LOGGER.error("Error al inicializar el Port "
                    + VerificacionBloqueoPortType.class, e);
            LOGGER.error( new DAOException(e));
        }
            
        try{
        	LOGGER.info("Configurando Datos de la peticion");
        	ValidarClaveDesbloqueoType validarClaveDesbloqueoTypeRequest = new ValidarClaveDesbloqueoType();
            
            validarClaveDesbloqueoTypeRequest.setMercado(mercado);
            validarClaveDesbloqueoTypeRequest.setMsisdn(numeroPcs);
            validarClaveDesbloqueoTypeRequest.setClave(claveBloqueo);
            
            ResultadoValidarClaveDesbloqueoType resultadoValidarClaveDesbloqueoTypeResponse = null;
            try{
            	LOGGER.info("Invocando servicio");
            	resultadoValidarClaveDesbloqueoTypeResponse = port.validarClaveDesbloqueoOperation(validarClaveDesbloqueoTypeRequest);
            }catch (Exception e) {
            	LOGGER.error("Exception caught on Service invocation: "
                        + "validarClaveDesbloqueoOperation", e);
                LOGGER.error( new DAOException(e));
			}
            
            LOGGER.info("resultadoValidarClaveDesbloqueoTypeResponse.getRespuesta().getCodigo() :::: " + resultadoValidarClaveDesbloqueoTypeResponse.getRespuesta().getCodigo());
            
            if( resultadoValidarClaveDesbloqueoTypeResponse.getRespuesta().getCodigo() == null || ("").equals(resultadoValidarClaveDesbloqueoTypeResponse.getRespuesta().getCodigo())	){
            
            	LOGGER.info("ENTRANDO A CONSULTAR NUEVAMENTE EL SERVICIO DE VALIDAR CLAVE EN EL DAO....");
            	
            	ResultadoValidarClaveDesbloqueoType resultadoValidarClaveDesbloqueoTypeResponse1 = null;
            	try{
            		LOGGER.info("Invocando servicio");
            		resultadoValidarClaveDesbloqueoTypeResponse1 = port.validarClaveDesbloqueoOperation(validarClaveDesbloqueoTypeRequest);
            	}catch (Exception e) {
                	LOGGER.error("Exception caught on Service invocation: "
                            + "validarClaveDesbloqueoOperation", e);
                    LOGGER.error( new DAOException(e));
    			}
            	respuestaValidarClaveDesbloqueo = resultadoValidarClaveDesbloqueoTypeResponse1.getRespuesta().getCodigo();
            	
            }else{
            	respuestaValidarClaveDesbloqueo = resultadoValidarClaveDesbloqueoTypeResponse.getRespuesta().getCodigo();	
            }
            
            if(!resultadoValidarClaveDesbloqueoTypeResponse.getRespuesta().getCodigo().equals(CODIGO_RESPUESTA_OK)  ){
            	
                LOGGER.info("validarClaveDesbloqueoOperation: Service error code received: " 
                		+ resultadoValidarClaveDesbloqueoTypeResponse.getRespuesta().getCodigo() + " - " 
                        + resultadoValidarClaveDesbloqueoTypeResponse.getRespuesta().getDescripcion());
                LOGGER.error( new ServiceException(resultadoValidarClaveDesbloqueoTypeResponse.getRespuesta().getCodigo(), 
                		resultadoValidarClaveDesbloqueoTypeResponse.getRespuesta().getDescripcion()));
            }
            
            LOGGER.info("respuestaValidarClaveDesbloqueo :::: " + respuestaValidarClaveDesbloqueo);
            
        return respuestaValidarClaveDesbloqueo;
        
        }catch (Exception e) {
        	if(!(e instanceof ServiceException || e instanceof DAOException)){
        		LOGGER.error("Error al validar la clave para el desbloqueo", e);
        	}
        	LOGGER.error( new DAOException(e));
        }
        return "";
    }
    

    /**
     * consultar trafico equipos
     * 
     * @return List<ListaTraficoEquiposBean>
     * @throws DAOException
     */
    public  List<TraficoEquiposBean> consultarHistoricoEquipos(String numeroPcs , String imei , String imsi) throws DAOException, ServiceException {
        
        String msisdn = "";
        String cantidadDias = "40";

    	SeguimientoTrafService port;

    	List<TraficoEquiposBean> listaTraficoEquiposBean = new ArrayList<TraficoEquiposBean>();
    	
        try {
            
            port = (SeguimientoTrafService) WebServiceLocator
            		.getInstance().getPort(SeguimientoTrafServiceService.class, 
            				SeguimientoTrafService.class);
            

            
            msisdn = PREFIJO_ENTEL + numeroPcs;

            String consultaHistoricaResponse = port.consultaHistorica(msisdn, imei, imsi, cantidadDias);
            
        	consultaHistoricaResponse = consultaHistoricaResponse.replace("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", "");
        	
            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
            InputSource is = new InputSource();    
            is.setCharacterStream(new StringReader(consultaHistoricaResponse));
            Document doc = docBuilder.parse(is);
            doc.getDocumentElement ().normalize ();

            NodeList listTraficoEquipos = doc.getElementsByTagName("trafico");
            
            for(int s=0; s<listTraficoEquipos.getLength() ; s++){
            	
            	TraficoEquiposBean traficoEquiposBean = new TraficoEquiposBean();
                
            	Node traficoEquiposNode = listTraficoEquipos.item(s);
                
            	if(traficoEquiposNode.getNodeType() == Node.ELEMENT_NODE){
                    
                	Element traficoEquiposElement = (Element)traficoEquiposNode;

                    NodeList numeroTelefonoNameList = traficoEquiposElement.getElementsByTagName("numeroTelefono");
                    Element numeroTelefonoNameElement = (Element)numeroTelefonoNameList.item(0);
                    NodeList textFNList = numeroTelefonoNameElement.getChildNodes();
                    traficoEquiposBean.setNumeroTelefono( ((Node)textFNList.item(0)).getNodeValue().trim() );
                    
                    NodeList imsiNameList = traficoEquiposElement.getElementsByTagName("imsi");
                    Element imsiNameElement = (Element)imsiNameList.item(0);
                    NodeList textImsiList = imsiNameElement.getChildNodes();
                    traficoEquiposBean.setImsi(((Node)textImsiList.item(0)).getNodeValue().trim() );

                    NodeList imeiNameList = traficoEquiposElement.getElementsByTagName("imei");
                    Element imeiNameElement = (Element)imeiNameList.item(0);
                    NodeList textImeiList = imeiNameElement.getChildNodes();
                    traficoEquiposBean.setImei(((Node)textImeiList.item(0)).getNodeValue().trim() );

                    NodeList modalidadNameList = traficoEquiposElement.getElementsByTagName("modalidad");
                    Element modalidadNameElement = (Element)modalidadNameList.item(0);
                    NodeList textModalidadList = modalidadNameElement.getChildNodes();
                    traficoEquiposBean.setModalidad(((Node)textModalidadList.item(0)).getNodeValue().trim() );

                    NodeList fechaPrimerTraficoNameList = traficoEquiposElement.getElementsByTagName("fechaPrimerTrafico");
                    Element fechaPrimerTraficoNameElement = (Element)fechaPrimerTraficoNameList.item(0);
                    NodeList textFechaPrimerTraficoList = fechaPrimerTraficoNameElement.getChildNodes();
                    traficoEquiposBean.setFechaPrimerTrafico(((Node)textFechaPrimerTraficoList.item(0)).getNodeValue().trim() );

                    NodeList fechaUltimoTraficoNameList = traficoEquiposElement.getElementsByTagName("fechaUltimoTrafico");
                    Element fechaUltimoTraficoNameElement = (Element)fechaUltimoTraficoNameList.item(0);
                    NodeList textFechaUltimoTraficoList = fechaUltimoTraficoNameElement.getChildNodes();
                    traficoEquiposBean.setFechaUltimoTrafico(((Node)textFechaUltimoTraficoList.item(0)).getNodeValue().trim() );

                    NodeList celdaPrimerTraficoNameList = traficoEquiposElement.getElementsByTagName("celdaPrimerTrafico");
                    Element celdaPrimerTraficoNameElement = (Element)celdaPrimerTraficoNameList.item(0);
                    NodeList textCeldaPrimerTraficoList = celdaPrimerTraficoNameElement.getChildNodes();
                    traficoEquiposBean.setCeldaPrimerTrafico(((Node)textCeldaPrimerTraficoList.item(0)).getNodeValue().trim() );

                    NodeList nombreCeldaPrimerTraficoNameList = traficoEquiposElement.getElementsByTagName("nombreCeldaPrimerTrafico");
                    Element nombreCeldaPrimerTraficoNameElement = (Element)nombreCeldaPrimerTraficoNameList.item(0);
                    NodeList textNombreCeldaPrimerTraficoList = nombreCeldaPrimerTraficoNameElement.getChildNodes();
                    traficoEquiposBean.setNombreCeldaPrimerTrafico(((Node)textNombreCeldaPrimerTraficoList.item(0)).getNodeValue().trim() );

                    NodeList direccionCeldaPrimerTraficoNameList = traficoEquiposElement.getElementsByTagName("direccionCeldaPrimerTrafico");
                    Element direccionCeldaPrimerTraficoNameElement = (Element)direccionCeldaPrimerTraficoNameList.item(0);
                    NodeList textDireccionCeldaPrimerTraficoList = direccionCeldaPrimerTraficoNameElement.getChildNodes();
                    traficoEquiposBean.setDireccionCeldaPrimerTrafico(((Node)textDireccionCeldaPrimerTraficoList.item(0)).getNodeValue().trim() );
                    
                    NodeList comunaCeldaPrimerTraficoNameList = traficoEquiposElement.getElementsByTagName("comunaCeldaPrimerTrafico");
                    Element comunaCeldaPrimerTraficoNameElement = (Element)comunaCeldaPrimerTraficoNameList.item(0);
                    NodeList textComunaCeldaPrimerTraficoList = comunaCeldaPrimerTraficoNameElement.getChildNodes();
                    traficoEquiposBean.setComunaCeldaPrimerTrafico(((Node)textComunaCeldaPrimerTraficoList.item(0)).getNodeValue().trim() );

                    NodeList regionCeldaPrimerTraficoNameList = traficoEquiposElement.getElementsByTagName("regionCeldaPrimerTrafico");
                    Element regionCeldaPrimerTraficoNameElement = (Element)regionCeldaPrimerTraficoNameList.item(0);
                    NodeList textRegionCeldaPrimerTraficoList = regionCeldaPrimerTraficoNameElement.getChildNodes();
                    traficoEquiposBean.setRegionCeldaPrimerTrafico(((Node)textRegionCeldaPrimerTraficoList.item(0)).getNodeValue().trim() );
                    
                    NodeList celdaUltimoTraficoNameList = traficoEquiposElement.getElementsByTagName("celdaUltimoTrafico");
                    Element celdaUltimoTraficoNameElement = (Element)celdaUltimoTraficoNameList.item(0);
                    NodeList textCeldaUltimoTraficoList = celdaUltimoTraficoNameElement.getChildNodes();
                    traficoEquiposBean.setCeldaUltimoTrafico(((Node)textCeldaUltimoTraficoList.item(0)).getNodeValue().trim() );

                    NodeList nombreCeldaUltimoTraficoNameList = traficoEquiposElement.getElementsByTagName("nombreCeldaUltimoTrafico");
                    Element nombreCeldaUltimoTraficoNameElement = (Element)nombreCeldaUltimoTraficoNameList.item(0);
                    NodeList textNombreCeldaUltimoTraficoList = nombreCeldaUltimoTraficoNameElement.getChildNodes();
                    traficoEquiposBean.setNombreCeldaUltimoTrafico(((Node)textNombreCeldaUltimoTraficoList.item(0)).getNodeValue().trim() );

                    NodeList direccionCeldaUltimoTraficoNameList = traficoEquiposElement.getElementsByTagName("direccionCeldaUltimoTrafico");
                    Element direccionCeldaUltimoTraficoNameElement = (Element)direccionCeldaUltimoTraficoNameList.item(0);
                    NodeList textDireccionCeldaUltimoTraficoList = direccionCeldaUltimoTraficoNameElement.getChildNodes();
                    traficoEquiposBean.setDireccionCeldaUltimoTrafico(((Node)textDireccionCeldaUltimoTraficoList.item(0)).getNodeValue().trim() );
                    
                    NodeList comunaCeldaUltimoTraficoNameList = traficoEquiposElement.getElementsByTagName("comunaCeldaUltimoTrafico");
                    Element comunaCeldaUltimoTraficoNameElement = (Element)comunaCeldaUltimoTraficoNameList.item(0);
                    NodeList textComunaCeldaUltimoTraficoList = comunaCeldaUltimoTraficoNameElement.getChildNodes();
                    traficoEquiposBean.setComunaCeldaUltimoTrafico(((Node)textComunaCeldaUltimoTraficoList.item(0)).getNodeValue().trim() );

                    NodeList regionCeldaUltimoTraficoNameList = traficoEquiposElement.getElementsByTagName("regionCeldaUltimoTrafico");
                    Element regionCeldaUltimoTraficoNameElement = (Element)regionCeldaUltimoTraficoNameList.item(0);
                    NodeList textRegionCeldaUltimoTraficoList = regionCeldaUltimoTraficoNameElement.getChildNodes();
                    traficoEquiposBean.setRegionCeldaUltimoTrafico(((Node)textRegionCeldaUltimoTraficoList.item(0)).getNodeValue().trim() );

                    NodeList codigoEquipoNameList = traficoEquiposElement.getElementsByTagName("codigoEquipo");
                    Element codigoEquipoNameElement = (Element)codigoEquipoNameList.item(0);
                    NodeList textCodigoEquipoList = codigoEquipoNameElement.getChildNodes();
                    traficoEquiposBean.setCodigoEquipo(((Node)textCodigoEquipoList.item(0)).getNodeValue().trim() );
                    
                    NodeList descripcionEquipoNameList = traficoEquiposElement.getElementsByTagName("descripcionEquipo");
                    Element descripcionEquipoNameElement = (Element)descripcionEquipoNameList.item(0);
                    NodeList textDescripcionEquipoList = descripcionEquipoNameElement.getChildNodes();
                    traficoEquiposBean.setDescripcionEquipo(((Node)textDescripcionEquipoList.item(0)).getNodeValue().trim() );
                    
                    listaTraficoEquiposBean.add(traficoEquiposBean);
                }
            }
                
        }catch (WebServiceLocatorException e) {
        	LOGGER.error("Error al inicializar el Port "
                    + SeguimientoTrafService.class, e);
            LOGGER.error( new DAOException(e));
        }catch (SAXParseException err) {
        	LOGGER.error("** Parsing error" + ", line "+ err.getLineNumber () + ", uri " + err.getSystemId ());
        	LOGGER.error(" " + err.getMessage ());
        }catch (SAXException e) {
        	LOGGER.error("SAXException",e);
            Exception x = e.getException ();
            ((x == null) ? e : x).printStackTrace ();
         }catch (Throwable t) {
        	 LOGGER.error("Throwable",t);
           	 LOGGER.error(t);
         }
           
        LOGGER.info("END");
        return listaTraficoEquiposBean;
    }
    
    
    /**
     * consutar trafico equipos
     * 
     * @return String
     * @throws DAOException
     */
    public String consultaDescripcionMovilTraficado(String imei , String dvImei) throws DAOException, ServiceException {
        
    	VerificacionBloqueoPortType port = null;
    	String descripcionEquipo = null;

        try {
            port = (VerificacionBloqueoPortType) WebServiceLocator
            		.getInstance().getPort(BloqueoSubtelService.class, 
            				VerificacionBloqueoPortType.class);
        }catch (WebServiceLocatorException e) {
        	LOGGER.error("Error al inicializar el Port "
                    + VerificacionBloqueoPortType.class, e);
            LOGGER.error( new DAOException(e));
        }
            
        	LOGGER.info("Configurando Datos de la peticion");
        	ConsultarDatosEquipoPorImeiType consultarDatosEquipoPorImeiTypeRequest = new ConsultarDatosEquipoPorImeiType();
        	
        	String codImei = imei.concat(dvImei);
        	LOGGER.info("CONSULTANDO DESCRIPCION DEL MOVIL  :::" + codImei);
        	consultarDatosEquipoPorImeiTypeRequest.setImei(codImei);
            
        	ResultadoConsultarDatosEquipoPorImeiType resultadoConsultarDatosEquipoPorImeiTypeResponse = null;
        	try{
        		LOGGER.info("Invocando servicio");
        		resultadoConsultarDatosEquipoPorImeiTypeResponse = port.consultarDatosEquipoPorImeiOperation(consultarDatosEquipoPorImeiTypeRequest);
        	}catch (Exception e) {
            	LOGGER.error("Exception caught on Service invocation: "
                        + "consultarDatosEquipoPorImeiOperation", e);
                LOGGER.error( new DAOException(e));
			}
        	
            
        	String codigoRespuesta  = resultadoConsultarDatosEquipoPorImeiTypeResponse.getRespuesta().getCodigo();
        	
        	if (CODIGO_RESPUESTA_OK.equals(codigoRespuesta)) {
        		try{
        			descripcionEquipo = resultadoConsultarDatosEquipoPorImeiTypeResponse.getConsultarDatosEquipoPorImei().getRegistro().get(0).getMarcaModelo();
        		}catch (Exception e) {
        			LOGGER.error("Exception caught on Service response: "
                            + "consultarDatosEquipoPorImeiOperation", e);
                    LOGGER.error( new DAOException(e));
				}	
        	}else{
        		descripcionEquipo = "SIN DESCRIPCION DEFINIDA";
        	}
        	
        	LOGGER.info("descripcionEquipo ::::" + descripcionEquipo );
            
        return descripcionEquipo;
    }
    
    
    /**
     * Bloquea  el equipo
     *  
     * @throws DAOException, ServiceException
     */
    public void ingresarBloqueoEquipo(BloqueoDesbloqueoEquipoBean bloq) throws DAOException, ServiceException {

    	ClienteContactoServicePortType port = null;

        try {
            LOGGER.info("Instanciando el port en ingresarBloqueoEquipo");
            port = (ClienteContactoServicePortType) WebServiceLocator.getInstance().getPort(
            		ClienteContactoService.class, ClienteContactoServicePortType.class);

        }catch (Exception e) {
        	LOGGER.error("Error al inicializar el Port "
                    + ClienteContactoServicePortType.class, e);
        	 LOGGER.error( new DAOException(e));
         }  
        

            LOGGER.info("Configurando Datos de la peticion");
            
            String mail = bloq.getEmailUsuario();
            
          	 if( mail != null && mail.contains("@") ){
         		String email[] = mail.split("@");
             	bloq.setEmailUsuario(email[0]);
             	bloq.setEmailDominio(email[1]);
  	    	 }
         	 
            IngresarBloqueoType  ingresarBloqueoTypeRequest = new IngresarBloqueoType();
            
            ingresarBloqueoTypeRequest.setMsisdn(bloq.getMsisdn());
            ingresarBloqueoTypeRequest.setRut(bloq.getRut());
            ingresarBloqueoTypeRequest.setDv(bloq.getDv());
            ingresarBloqueoTypeRequest.setEmailUsuario(bloq.getEmailUsuario());
            ingresarBloqueoTypeRequest.setEmailDominio(bloq.getEmailDominio());
            ingresarBloqueoTypeRequest.setAreaFono(bloq.getAreaFono());
            ingresarBloqueoTypeRequest.setFonoContacto(bloq.getFonoContacto());
            ingresarBloqueoTypeRequest.setDireccion(bloq.getDireccion());
            ingresarBloqueoTypeRequest.setNumeroDomicilio(bloq.getNumeroDomicilio());
            ingresarBloqueoTypeRequest.setDeptoOff(bloq.getDeptoOff());
            ingresarBloqueoTypeRequest.setRegion(bloq.getRegion());
            ingresarBloqueoTypeRequest.setCiudad(bloq.getCiudad());
            ingresarBloqueoTypeRequest.setComuna(bloq.getComuna());
            ingresarBloqueoTypeRequest.setNombre(bloq.getNombre());
            ingresarBloqueoTypeRequest.setMercado(bloq.getMercado());
            ingresarBloqueoTypeRequest.setSentidoBloqueo(bloq.getSentidoBloqueo());
            ingresarBloqueoTypeRequest.setDestinoBloqueo(bloq.getDestinoBloqueo());
            ingresarBloqueoTypeRequest.setMotivoBloqueo(bloq.getMotivoBloqueo());
            ingresarBloqueoTypeRequest.setClaveBloqueo(bloq.getClaveBloqueo());
            ingresarBloqueoTypeRequest.setFechaRoboExtravio(bloq.getFechaRoboExtravio());
            ingresarBloqueoTypeRequest.setPlataforma(bloq.getPlataforma());
            ingresarBloqueoTypeRequest.setImei(bloq.getImei());
            ingresarBloqueoTypeRequest.setMarcaModelo(bloq.getMarcaModelo());
            ingresarBloqueoTypeRequest.setOperacionComercial(bloq.getOperacionComercial());
            ingresarBloqueoTypeRequest.setFechaOperacion(bloq.getFechaOperacion());
            ingresarBloqueoTypeRequest.setFechaConstancia(bloq.getFechaConstancia());
            ingresarBloqueoTypeRequest.setHoraConstancia(bloq.getHoraConstancia());
            ingresarBloqueoTypeRequest.setUnidad(bloq.getUnidad());
            ingresarBloqueoTypeRequest.setNumeroConstancia(bloq.getNumeroConstancia());
            ingresarBloqueoTypeRequest.setCodOperacion(bloq.getCodOperacion());
            ingresarBloqueoTypeRequest.setCodCompEnvio(bloq.getCodCompEnvio());
            ingresarBloqueoTypeRequest.setCodImei(bloq.getCodImei());
            ingresarBloqueoTypeRequest.setFechaPerdida(bloq.getFechaPerdida());
            ingresarBloqueoTypeRequest.setFechIoper(bloq.getFechIoper());
            ingresarBloqueoTypeRequest.setFechaDenuncia(bloq.getFechaDenuncia());
            ingresarBloqueoTypeRequest.setDescMarcaequi(bloq.getDescMarcaequi());
            ingresarBloqueoTypeRequest.setDescModeloequi(bloq.getDescModeloequi());
            ingresarBloqueoTypeRequest.setDescTecnologia(bloq.getDescTecnologia());
            ingresarBloqueoTypeRequest.setIndcModalidad(bloq.getIndcModalidad());
            ingresarBloqueoTypeRequest.setNmroTelefono(bloq.getNmroTelefono());
            ingresarBloqueoTypeRequest.setRazonBloqueo(bloq.getRazonBloqueo());
            ingresarBloqueoTypeRequest.setCodImsi(bloq.getCodImsi());
            ingresarBloqueoTypeRequest.setCodTerminal(bloq.getCodTerminal());
            
            
            LOGGER.info("bloq.getMsisdn() " +  bloq.getMsisdn());
            LOGGER.info("getRut " +  bloq.getRut());
            LOGGER.info("getDv " +  bloq.getDv());
            LOGGER.info("getEmailUsuario " +  bloq.getEmailUsuario());
            LOGGER.info("getEmailDominio " +  bloq.getEmailDominio());
            LOGGER.info("getAreaFono " +  bloq.getAreaFono());
            LOGGER.info("getFonoContacto " +  bloq.getFonoContacto());
            LOGGER.info("getDireccion " +  bloq.getDireccion());
            LOGGER.info("getNumeroDomicilio " +  bloq.getNumeroDomicilio());
            LOGGER.info("getDeptoOff " +  bloq.getDeptoOff());
            LOGGER.info("getRegion " +  bloq.getRegion());
            LOGGER.info("getCiudad " +  bloq.getCiudad());
            LOGGER.info("getComuna " +  bloq.getComuna());
            LOGGER.info("getNombre " +  bloq.getNombre());
            LOGGER.info("getMercado " +  bloq.getMercado());
            LOGGER.info("getSentidoBloqueo " +  bloq.getSentidoBloqueo());
            LOGGER.info("getDestinoBloqueo " +  bloq.getDestinoBloqueo());
            LOGGER.info("getMotivoBloqueo " +  bloq.getMotivoBloqueo());
            LOGGER.info("getClaveBloqueo " +  bloq.getClaveBloqueo());
            LOGGER.info("getFechaRoboExtravio " +  bloq.getFechaRoboExtravio());
            LOGGER.info("getPlataforma " +  bloq.getPlataforma());
            LOGGER.info("getImei " +  bloq.getImei());
            LOGGER.info("getMarcaModelo " +  bloq.getMarcaModelo());
            LOGGER.info("getOperacionComercial " +  bloq.getOperacionComercial());
            LOGGER.info("getFechaOperacion " +  bloq.getFechaOperacion());
            LOGGER.info("getFechaConstancia " +  bloq.getFechaConstancia());
            LOGGER.info("getHoraConstancia " +  bloq.getHoraConstancia());
            LOGGER.info("getUnidad " +  bloq.getUnidad());
            LOGGER.info("getNumeroConstancia " +  bloq.getNumeroConstancia());
            LOGGER.info("getCodOperacion " +  bloq.getCodOperacion());
            LOGGER.info("getCodCompEnvio " +  bloq.getCodCompEnvio());
            LOGGER.info("getCodImei " +  bloq.getCodImei());
            LOGGER.info("getFechaPerdida " +  bloq.getFechaPerdida());
            LOGGER.info("getFechIoper " +  bloq.getFechIoper());
            LOGGER.info("getFechaDenuncia " +  bloq.getFechaDenuncia());
            LOGGER.info("getDescMarcaequi " +  bloq.getDescMarcaequi());
            LOGGER.info("getDescModeloequi " +  bloq.getDescModeloequi());
            LOGGER.info("getDescTecnologia " +  bloq.getDescTecnologia());
            LOGGER.info("getIndcModalidad " +  bloq.getIndcModalidad());
            LOGGER.info("getNmroTelefono " +  bloq.getNmroTelefono());
            LOGGER.info("getRazonBloqueo " +  bloq.getRazonBloqueo());
            LOGGER.info("getCodImsi " +  bloq.getCodImsi());
            LOGGER.info("getCodTerminal " +  bloq.getCodTerminal());
            
            
            ResultadoIngresarBloqueoType resultadoIngresarBloqueoTypeResponse = null;
            try{
            	LOGGER.info("Invocando servicio");
                resultadoIngresarBloqueoTypeResponse = port.ingresarBloqueo(ingresarBloqueoTypeRequest);
            }catch (Exception e) {
            	LOGGER.error("Exception caught on Service invocation: "
                        + "ingresarBloqueo", e);
                LOGGER.error( new DAOException(e));
			}
            
            String codigoRespuesta = resultadoIngresarBloqueoTypeResponse.getRespuesta().getCodigo();
            String descripcionRespuesta = resultadoIngresarBloqueoTypeResponse.getRespuesta().getDescripcion();
            
            
  	         if( !CODIGO_RESPUESTA_OK.equals(codigoRespuesta) ){   	        	
    	            LOGGER.info("ingresarBloqueo: Service error code received: "+ codigoRespuesta + " - " + descripcionRespuesta);        
    	            LOGGER.error( new ServiceException(codigoRespuesta, descripcionRespuesta));
    	     }
        LOGGER.info("END");
    
    }
    
    /**
     * Desbloquea  el equipo
     *  
     * @throws DAOException, ServiceException
     */
    public void ingresarDesbloqueoEquipo(BloqueoDesbloqueoEquipoBean desbloq) throws DAOException, ServiceException {
    	
    	
    	ClienteContactoServicePortType port = null;

        try {
            LOGGER.info("Instanciando el port en ingresarDesbloqueoEquipo");
            port = (ClienteContactoServicePortType) WebServiceLocator.getInstance().getPort(
            		ClienteContactoService.class, ClienteContactoServicePortType.class);

        }catch (Exception e) {
        	LOGGER.error("Error al inicializar el Port "
                    + ClienteContactoServicePortType.class, e);
        	 LOGGER.error( new DAOException(e));
         }  

            LOGGER.info("Configurando Datos de la peticion");

            IngresarDesbloqueoType  ingresarDesbloqueoTypeRequest = new IngresarDesbloqueoType();
            
            ingresarDesbloqueoTypeRequest.setMsisdn(desbloq.getMsisdn());
            ingresarDesbloqueoTypeRequest.setRut(desbloq.getRut());
            ingresarDesbloqueoTypeRequest.setDv(desbloq.getDv());
            ingresarDesbloqueoTypeRequest.setMercado(desbloq.getMercado());
            ingresarDesbloqueoTypeRequest.setClaveBloqueo(desbloq.getClaveBloqueo());
            
            ingresarDesbloqueoTypeRequest.setSentidoBloqueo(desbloq.getSentidoBloqueo());
            ingresarDesbloqueoTypeRequest.setDestinoBloqueo(desbloq.getDestinoBloqueo() );
            ingresarDesbloqueoTypeRequest.setMotivoBloqueo(desbloq.getMotivoBloqueo());
            ingresarDesbloqueoTypeRequest.setIdTransaccion(desbloq.getIdTransaccion());
            ingresarDesbloqueoTypeRequest.setFechaConstancia(desbloq.getFechaConstancia());
            ingresarDesbloqueoTypeRequest.setHoraConstancia(desbloq.getHoraConstancia());
            ingresarDesbloqueoTypeRequest.setUnidad(desbloq.getUnidad());
            ingresarDesbloqueoTypeRequest.setNroConstancia(desbloq.getNroConstancia());
            ingresarDesbloqueoTypeRequest.setCodOperacion(desbloq.getCodOperacion());
            ingresarDesbloqueoTypeRequest.setCodCompEnvio(desbloq.getCodCompEnvio());
            ingresarDesbloqueoTypeRequest.setCodImei(desbloq.getCodImei());
            ingresarDesbloqueoTypeRequest.setFechaPerdida(desbloq.getFechaPerdida());
            ingresarDesbloqueoTypeRequest.setFechIoper(desbloq.getFechIoper());
            ingresarDesbloqueoTypeRequest.setFechaDenuncia(desbloq.getFechaDenuncia());
            ingresarDesbloqueoTypeRequest.setDescMarcaequi(desbloq.getDescMarcaequi());
            ingresarDesbloqueoTypeRequest.setDescModeloequi(desbloq.getDescModeloequi());
            ingresarDesbloqueoTypeRequest.setDescTecnologia(desbloq.getDescTecnologia());
            ingresarDesbloqueoTypeRequest.setIndcModalidad(desbloq.getIndcModalidad());
            ingresarDesbloqueoTypeRequest.setNmroTelefono(desbloq.getNmroTelefono());
            ingresarDesbloqueoTypeRequest.setRazonBloqueo(desbloq.getRazonBloqueo());
            ingresarDesbloqueoTypeRequest.setCodImsi(desbloq.getCodImsi());
            ingresarDesbloqueoTypeRequest.setCodTerminal(desbloq.getCodTerminal());
            
            ingresarDesbloqueoTypeRequest.setFechaPerdida(desbloq.getFechaPerdida());
            ingresarDesbloqueoTypeRequest.setFechIoper(desbloq.getFechIoper());
            ingresarDesbloqueoTypeRequest.setFechaDenuncia(desbloq.getFechaDenuncia());
            
            LOGGER.info("desbloq.getFechaPerdida() >>>>>>" + desbloq.getFechaPerdida());
            LOGGER.info("desbloq.getFechIoper() >>>>>>>" + desbloq.getFechIoper());
            LOGGER.info("desbloq.getFechaDenuncia() >>>>>> " + desbloq.getFechaDenuncia());
            
            ResultadoIngresarDesbloqueoType resultadoIngresarDesbloqueoTypeResponse = null;
            try{
            	LOGGER.info("Invocando servicio");
                resultadoIngresarDesbloqueoTypeResponse = port.ingresarDesbloqueo(ingresarDesbloqueoTypeRequest);
            }catch (Exception e) {
            	LOGGER.error("Exception caught on Service invocation: "
                        + "ingresarDesbloqueo", e);
                LOGGER.error( new DAOException(e));
			}
            
            String codigoRespuesta = resultadoIngresarDesbloqueoTypeResponse.getRespuesta().getCodigo();
            String descripcionRespuesta = resultadoIngresarDesbloqueoTypeResponse.getRespuesta().getDescripcion();
            
            LOGGER.info("Codigo de Respuesta :"+codigoRespuesta);
            LOGGER.info("Descipcion de Respuesta :"+descripcionRespuesta);
            
  	         if( !CODIGO_RESPUESTA_OK.equals(codigoRespuesta) ){   	        	
    	            LOGGER.info("desbloquearEquipo: Service error code received: "+ codigoRespuesta + " - " + descripcionRespuesta);        
    	            LOGGER.error( new ServiceException(codigoRespuesta, descripcionRespuesta));
    	     }
        LOGGER.info("END");
    
    }
    
   
    /**
     * Consultar si el medico tiene bloqueo por factura
     * 
     * @param msisdn   
     * @return
     * @throws DAOException
     * @throws ServiceException
     */
    public boolean tieneBloqueoPorFacturaImpaga(String msisdn) throws DAOException, ServiceException {

        BillingBalanceService port = null;
        LOGGER.info("Instanciando el port");
        boolean respuesta = false;
        try {
            port = (BillingBalanceService) WebServiceLocator.getInstance()
                    .getPort(BillingBalanceService_Service.class,
                            BillingBalanceService.class);
        } catch (WebServiceLocatorException e) {
            LOGGER.error("Error al inicializar el Port "
                    + BillingBalanceService.class, e);
            LOGGER.error( new DAOException(e));
        }
        
        ConsultarEstadoServicioPorFacturaType consultarEstadoFactura = new ConsultarEstadoServicioPorFacturaType(); 
        ResultadoConsultarEstadoServicioPorFacturaType  resultadoEstadoFactura = null;

        try {

            LOGGER.info("Configurando Datos de la peticion");        
            consultarEstadoFactura.setMsisdn(msisdn);            
            
            LOGGER.info("msisdn: " + consultarEstadoFactura.getMsisdn());

            LOGGER.info("Invocando servicio");          
            resultadoEstadoFactura = port.consultarEstadoServicioPorFactura(consultarEstadoFactura);
            
        } catch (Exception e) {
            LOGGER.error("Exception caught on Service invocation", e);
            LOGGER.error( new DAOException(e));
        }
        
        String codigoRespuesta      = resultadoEstadoFactura.getRespuesta().getCodigo();
        String descripcionRespuesta = resultadoEstadoFactura.getRespuesta().getDescripcion();

        LOGGER.info("codigoRespuesta  " + codigoRespuesta);
        LOGGER.info("descripcionRespuesta " + descripcionRespuesta);

        if (CODIGO_RESPUESTA_OK.equals(codigoRespuesta)) {
            LOGGER.info("Resultado EstadoServicioPorFactura :  " + resultadoEstadoFactura.getEstado());
            
            if(resultadoEstadoFactura.getEstado().equals(EQUIPO_BLOQUEADO_FACTURA_IMPAGA)){
            	respuesta = true;
            }
            
        }
        else {
            LOGGER.error("Service error code received: " + codigoRespuesta
                    + " - " + descripcionRespuesta);
            LOGGER.error( new ServiceException(codigoRespuesta, descripcionRespuesta));
     
        }
        
        return respuesta;

    }
    
	public GrupoClienteBean obtenerGrupoCliente(String rut, String numeroCuenta)
			throws DAOException, ServiceException {

		ClientePerfilServicePortType port = null;
		GrupoClienteBean grupoClienteBean = new GrupoClienteBean();

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

		String ID_SISTEMA = MiEntelProperties
				.getProperty("parametros.direccionpostal.idsistema");
		String PASSWORD_SISTEMA = MiEntelProperties
				.getProperty("parametros.direccionpostal.passwordsistema");

		request.setIdSistema(ID_SISTEMA);
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
			LOGGER.error( new DAOException(
					"consultarDireccionPostal: Servicio no respondio "
							+ "con codigoRespuesta"));
		}

		if (CODIGO_RESPUESTA_OK.equals(codigoRespuesta)) {
			try {
				grupoClienteBean.setCodigoGrupo(response
						.getDatosUsuarioDireccion().getCodigoGrupoCliente());
				grupoClienteBean.setNombreGrupo(response
						.getDatosUsuarioDireccion().getGrupoCliente());
			} catch (Exception e) {
				LOGGER.error("Exception caught on Service response: consultarDireccionPostal", e);
				LOGGER.error( new DAOException(e));
			}

		} else {
			LOGGER.info("consultarDireccionPostal: Service error code received: " + codigoRespuesta + " - " + descripcionRespuesta);
			LOGGER.error( new ServiceException(codigoRespuesta, descripcionRespuesta));
		}

		return grupoClienteBean;
	}    
    
	
    public Equipo4GLteBean compatibilidad(String numeroPcs)
    throws DAOException, ServiceException {

ClientePerfilServicePortType port;
Equipo4GLteBean equipo = new Equipo4GLteBean();
try {
    LOGGER.info("Instanciando el port");
    port = (ClientePerfilServicePortType) WebServiceLocator.getInstance()
            .getPort(ClientePerfilService.class,
                    ClientePerfilServicePortType.class);
    
    LOGGER.info("Configurando Datos de la peticion");
    ConsultarEquipoFullType consultaEquipoFullTypeRequest = new ConsultarEquipoFullType();
    consultaEquipoFullTypeRequest.setMsisdn(numeroPcs);

    ResultadoConsultarEquipoFullType consultaEquipoFullTypeResponse = null;
    try{
    	LOGGER.info("Invocando servicio");
        consultaEquipoFullTypeResponse = port
        	.consultarEquipoFull(consultaEquipoFullTypeRequest);
    
    }catch (Exception e) {
	       	 LOGGER.error("Exception caught on Service invocation: "
	                    + "consultarEquipoFull", e);
	       	 LOGGER.error( new DAOException(e));
    }
    
    String codigoRespuesta = consultaEquipoFullTypeResponse.getRespuesta()
            .getCodigo();
    String descripcionRespuesta = consultaEquipoFullTypeResponse
            .getRespuesta().getDescripcion();
    
    LOGGER.info("Codigo de Respuesta :"+codigoRespuesta);
    LOGGER.info("Descipcion de Respuesta :"+descripcionRespuesta);
    
    if (CODIGO_RESPUESTA_OK.equals(codigoRespuesta)) {
    	
    	try{
    		EquipoFullType equipoFullType = consultaEquipoFullTypeResponse.getEquipo();
            
            LOGGER.info("Configurando el Bean de Negocio");
            equipo = new Equipo4GLteBean();
            equipo.setModelo(equipoFullType.getModeloEquipo());
            equipo.setUrlImagen(equipoFullType.getUrlImagen());
            equipo.setMarca(equipoFullType.getMarcaEquipo());  
            equipo.setF4G2600(equipoFullType.getF4G2600());
            
            MiEntelProperties.getProperty("servicios.codigoRespuesta.exito");
    	}catch (Exception e) {
	   	       	 LOGGER.error("Exception caught on Service response: "
   	                    + "consultarEquipoFull", e);
   	       	 LOGGER.error( new DAOException(e));
    	}

    }
    else if( CODIGO_RESPUESTA_NO_VALIDO.equals(codigoRespuesta) || CODIGO_RESPUESTA_NO_RECONOCIDO.equals(codigoRespuesta) ){
        LOGGER.info("Configurando el Bean de Negocio - Movil no reconocido");
        equipo = new Equipo4GLteBean();
        equipo.setModelo("");
        equipo.setUrlImagen("");
        equipo.setF4G2600("");        
        equipo.setMarca(EQUIPO_NO_RECONOCIDO);
    }
    else {
        LOGGER.info("consultarEquipoLte: Service error code received: " 
        		+ codigoRespuesta + " - " + descripcionRespuesta);
        LOGGER.error( new ServiceException(codigoRespuesta,
                descripcionRespuesta));
    }

} catch (WebServiceLocatorException e) {
	LOGGER.error("Error al inicializar el Port "
            + ClientePerfilServicePortType.class, e);
    LOGGER.error( new DAOException(e));

}
LOGGER.info("END");
return equipo;
}
    
	/**
	 * 
	 * @param rut
	 * @param numeroCuenta
	 * @param grupoCliente
	 * @return
	 * @throws DAOException
	 * @throws ServiceException
	 */
    public ResumenLineaEquipoBean obtenerResumenLineaEquipos(String rut, String numeroCuenta, String grupoCliente)
			throws DAOException, ServiceException {

		ResumenLineasEquiposPort port = null;
		ResumenLineaEquipoBean resumenLineaEquipo = new ResumenLineaEquipoBean();

		LOGGER.info("Instanciando el port " + ResumenLineasEquiposPort.class);
		
		try {
			port = (ResumenLineasEquiposPort) WebServiceLocator
					.getInstance().getPort(ResumenLineasEquiposService.class,
							ResumenLineasEquiposPort.class);
		} catch (WebServiceLocatorException e) {
			LOGGER.error("Error al inicializar el Port " + ResumenLineasEquiposPort.class, e);
			LOGGER.error( new DAOException(e));
		}

		LOGGER.info("Configurando Datos de la peticion");
		ClienteDTO request = new ClienteDTO();

		request.setCtaResponsable(numeroCuenta);
		request.setGrupoCliente(grupoCliente);
		request.setPlataforma(SIM_ONLY_PLATAFORMA);
		request.setRut(rut);		

		LOGGER.info("Invocando servicio");
		ResumenDTO response = null;
		try {
			response = port.getResumen(request);
		} catch (Exception e) {
			LOGGER.error("Exception caught on Service invocation: obtenerResumenLineaEquipos", e);
			LOGGER.error( new DAOException(e));
		}

		int codigoRespuesta = response.getCodRespuesta();
		String descripcionRespuesta = response.getDescRespuesta();

		LOGGER.info("codigoRespuesta " + codigoRespuesta);
		LOGGER.info("descripcionRespuesta " + descripcionRespuesta);

		if (Utils.isEmptyString(String.valueOf(codigoRespuesta))) {
			throw new DAOException(
					"obtenerResumenLineaEquipos: Servicio no respondio " + "con codigoRespuesta");
		}

		if (codigoRespuesta == 0) {
			try {
				resumenLineaEquipo.setCantEquipoMultimedia(response.getCantEquipoMultim());
				resumenLineaEquipo.setCantEquipoNormal(response.getCantEquipoNormal());
				resumenLineaEquipo.setCantLineaMultimedia(response.getCantLineaMulti());
				resumenLineaEquipo.setCantLineaNormal(response.getCantLineaNormal());
				resumenLineaEquipo.setCantLineaSimOnly(response.getCantLineaSimOnly());
				resumenLineaEquipo.setCantMaxEquipoAcoc(response.getCantMaxEquipoAcoc());
				resumenLineaEquipo.setCantMaxEquipoFinan(response.getCantMaxEquipoFinan());
				resumenLineaEquipo.setCantMaxEquipoMulti(response.getCantMaxEquipoMulti());
				resumenLineaEquipo.setTotalEquiposAcoc(response.getTotalEquiposAcoc());
				resumenLineaEquipo.setTotalLineas(response.getTotalLineas());
				resumenLineaEquipo.setMsjCuposDisponibles(EquipoControllerHelper.obtenerMensajeCuposDisponibles(resumenLineaEquipo));
			} catch (Exception e) {
				LOGGER.error("Exception caught on Service response: obtenerResumenLineaEquipos", e);
				LOGGER.error( new DAOException(e));
			}

		} else {
			LOGGER.info("obtenerResumenLineaEquipos: Service error code received: "
				+ codigoRespuesta + " - " + descripcionRespuesta);
			LOGGER.error( new ServiceException(String.valueOf(codigoRespuesta), descripcionRespuesta));
		}

		return resumenLineaEquipo;
	}    

}