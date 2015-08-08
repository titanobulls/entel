/* Propiedad de Entel PCS. Todos los derechos reservados */
package com.epcs.provision.suscripcion.datoskpibam.dao;


import java.net.URL;
import java.util.ResourceBundle;
import javax.xml.namespace.QName;
import org.apache.log4j.Logger;


import com.epcs.cliente.perfil.ClientePerfilService;
import com.epcs.cliente.perfil.ClientePerfilServicePortType;
import com.epcs.cliente.perfil.types.ConsultarDetallePlanesBAMType;
import com.epcs.cliente.perfil.types.ResultadoConsultarDetallePlanesBAMType;
import com.epcs.recursoti.configuracion.MiEntelProperties;
import com.epcs.recursoti.configuracion.ResourceBundleControl;
import com.epcs.recursoti.configuracion.locator.WebServiceLocator;
import com.epcs.recursoti.configuracion.locator.WebServiceLocatorException;
import com.epcs.recursoti.excepcion.DAOException;
import com.epcs.recursoti.excepcion.ServiceException;
import com.esa.provision.suscripcion.datoskpibam.IngresarDatosKpiBAMPortType;
import com.esa.provision.suscripcion.datoskpibam.IngresarDatosKpiBAMService;
import com.esa.provision.suscripcion.datoskpibam.types.IngresarDatosKpiBAMRequestType;
import com.esa.provision.suscripcion.datoskpibam.types.IngresarDatosKpiBAMResponseType;
import com.esa.provision.suscripcion.datoskpibam.types.RequestType;
import com.esa.provision.suscripcion.datoskpibam.types.HeaderInType;


public class KIPBAMDAO {

    private static final Logger LOGGER = Logger.getLogger(KIPBAMDAO.class);   
    private static final String PATH = MiEntelProperties.getProperty("miEntel.webservices.properties.path");    
    
    /**
     * Ingresar marca KPI
     * 
     * @return String
     * @throws DAOException
     * @throws ServiceException
     */    

    public IngresarDatosKpiBAMResponseType insertarKPIBAM(String codArea, String nombreUsuario, String codIndicador, String fecha, String valor) throws DAOException, ServiceException  {
    	 LOGGER.info("Instanciando el port del WS marcas KPI");
    	 IngresarDatosKpiBAMPortType port;
    	 IngresarDatosKpiBAMService servicio;
    	 IngresarDatosKpiBAMRequestType parametros = new IngresarDatosKpiBAMRequestType();
    	 IngresarDatosKpiBAMResponseType respuesta;
    	 RequestType kpi = new RequestType();
    	 HeaderInType cabecera = new HeaderInType();
    	 
    	 
		 try {
			 String name = "com.esa.provision.suscripcion.DatosKpiBAMService.properties";
			 ResourceBundle bundle = ResourceBundle.getBundle(PATH + name,ResourceBundleControl.getControl());			 
			 URL url = new URL(bundle.getString("wsdl"));
			 servicio = new IngresarDatosKpiBAMService(url,new QName("http://www.esa.com/Provision/Suscripcion/DatosKpiBAM", "ingresarDatosKpiBAMService"));			 
			 port = servicio.getIngresarDatosKpiBAMSoapPort();
			 
			 cabecera.setAppName("MIENTELBAM");
			 cabecera.setUserName("MIENTELBAM");
			 kpi.setHeaderIn(cabecera);			 
			 kpi.setCodiArea(codArea);
			 kpi.setNombreUsuario(nombreUsuario);
			 kpi.setCodiIndicador(codIndicador);
			 kpi.setFecha(fecha);
			 kpi.setValorDatos(valor);			
			 
			 String contenidoKIP = "[Cod. Area="+ kpi.getCodiArea() +"]";
			 contenidoKIP += "[Nombre usuario="+ kpi.getNombreUsuario() +"]";
			 contenidoKIP += "[Cod. indicador="+ kpi.getCodiIndicador() +"]";
			 contenidoKIP += "[Fecha="+ kpi.getFecha() +"]";
			 contenidoKIP += "[Valor="+ kpi.getValorDatos() +"]";			 
			 
			 parametros.setRequest(kpi);	
			 
			 LOGGER.info("Insertando KPI: " + contenidoKIP);
			 respuesta = port.ingresaDatoKpiBam(parametros);	
			 
			 return respuesta;
	
		 } catch (Exception e) {
		     LOGGER.error("Error al inicializar el Port " + IngresarDatosKpiBAMService.class, e);
		     LOGGER.error( new DAOException(e));
		 }	 
		return new IngresarDatosKpiBAMResponseType();
    }
    
    
    public String esMovilSAPC(String movil) throws DAOException, ServiceException{
		ClientePerfilServicePortType port;
		ClientePerfilService service;
		ConsultarDetallePlanesBAMType consulta = new ConsultarDetallePlanesBAMType();
		ResultadoConsultarDetallePlanesBAMType response;
		String respuesta = "";
		
		LOGGER.info("Consultado esMovilSAPC: " + movil);
		
		
		try {
		    port = (ClientePerfilServicePortType) WebServiceLocator.getInstance().getPort(ClientePerfilService.class,ClientePerfilServicePortType.class);
		    service = (ClientePerfilService) WebServiceLocator.getInstance().getService(ClientePerfilService.class, false);
		    
		    consulta.setMsisdn(movil);
		    consulta.setMercado("CC");
		    
		    response = port.consultarDetallePlanesBAM(consulta);
		    LOGGER.info("Origen datos:" + response.getDetallePlanesBAM().getOrigenDatos());
		    if("SAPC".equalsIgnoreCase(response.getDetallePlanesBAM().getOrigenDatos())){
		    	respuesta = "SAPC";
		    }
		    
		    return respuesta;
		    
		} catch (Exception e) {
			LOGGER.error("Error al consultar tipo plan BAM CC ", e);
		    return "";
		}

    	
    }

}
