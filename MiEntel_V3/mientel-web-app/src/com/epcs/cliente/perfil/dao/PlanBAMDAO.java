/* Propiedad de Entel PCS. Todos los derechos reservados */
package com.epcs.cliente.perfil.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.epcs.bean.PlanBAMBean;
import com.epcs.bean.PlanResumenBAMPPBean;
import com.epcs.cliente.perfil.ClientePerfilService;
import com.epcs.cliente.perfil.ClientePerfilServicePortType;
import com.epcs.cliente.perfil.types.CambioPlanSSCCType;
import com.epcs.cliente.perfil.types.ConsultarPlanFullActualBAMSSCCType;
import com.epcs.cliente.perfil.types.ConsultarPlanResumenPPType;
import com.epcs.cliente.perfil.types.ConsultarPlanesDisponiblesBAMType;
import com.epcs.cliente.perfil.types.PlanBAMType;
import com.epcs.cliente.perfil.types.PlanFullActualBAMSSCCType;
import com.epcs.cliente.perfil.types.ResPlanResumenPPType;
import com.epcs.cliente.perfil.types.ResultadoCambioPlanSSCCType;
import com.epcs.cliente.perfil.types.ResultadoConsultarPlanFullActualBAMSSCCType;
import com.epcs.cliente.perfil.types.ResultadoConsultarPlanResumenPPType;
import com.epcs.cliente.perfil.types.ResultadoConsultarPlanesDisponiblesBAMType;
import com.epcs.cliente.perfil.types.ResultadoValidacionBloqueoTemporalType;
import com.epcs.cliente.perfil.types.ResultadoValidacionComercialType;
import com.epcs.cliente.perfil.types.ValidacionBloqueoTemporalType;
import com.epcs.cliente.perfil.types.ValidacionComercialType;
import com.epcs.cliente.perfil.util.PlanBAMHelper;
import com.epcs.recursoti.configuracion.DateHelper;
import com.epcs.recursoti.configuracion.MiEntelProperties;
import com.epcs.recursoti.configuracion.Utils;
import com.epcs.recursoti.configuracion.locator.WebServiceLocator;
import com.epcs.recursoti.configuracion.locator.WebServiceLocatorException;
import com.epcs.recursoti.excepcion.DAOException;
import com.epcs.recursoti.excepcion.ServiceException;

/**
 * @author jmanzur (I2B) en nombre de Absalon Opazo (Atencion al Cliente,
 *         EntelPcs)
 * 
 */
public class PlanBAMDAO {

    private static final Logger LOGGER = Logger.getLogger(PlanBAMDAO.class);
    
    private static final Logger CAJA_TRAFICO_LOGGER = Logger.getLogger("cajaTraficoLog");

    public static final String CODIGO_RESPUESTA_OK = MiEntelProperties.getProperty("servicios.codigoRespuesta.exito");

    private List<PlanBAMBean> planesList;
      
    
    /**
     * Realiza el cambio de plan BAM SSCC
     * 
     * @param numeroPcsSeleccionado
     * @param codigoNuevoPlan
     * @throws DAOException
     * @throws ServiceException
     */
    public void cambiarPlanBAMSSCC(String numeroPcsSeleccionado, String codigoNuevoPlan) throws DAOException,
    ServiceException {
    	
		ClientePerfilServicePortType port = null;
		
		LOGGER.info("Instanciando el Port.");
		try {
		    port = (ClientePerfilServicePortType) WebServiceLocator
		            .getInstance().getPort(ClientePerfilService.class,
		                    ClientePerfilServicePortType.class);
		} catch (WebServiceLocatorException e) {
		    LOGGER.error("Error al inicializar el Port "+ ClientePerfilService.class);
		    LOGGER.error( new DAOException(e));
		}
		
		LOGGER.info("Configurando parametros de la peticion");
		CambioPlanSSCCType cambioPlanSSCCRequest = new CambioPlanSSCCType();
		cambioPlanSSCCRequest.setMsisdn(numeroPcsSeleccionado);
		cambioPlanSSCCRequest.setCodigobscs2(codigoNuevoPlan);
		
		LOGGER.info("Invocando servicio");
		ResultadoCambioPlanSSCCType cambioPlanSSCCResponse = null;
		
		try{
			cambioPlanSSCCResponse = port.cambioPlanSSCC(cambioPlanSSCCRequest);
            
        } catch (Exception e) {
            LOGGER.error("Exception caught on Service invocation: "
            		+ "cambioPlanSSCC.", e);
            LOGGER.error( new DAOException(e));
        }
		
		String codigoRespuesta = cambioPlanSSCCResponse.getRespuesta().getCodigo();
		String descripcionRespuesta = cambioPlanSSCCResponse.getRespuesta().getDescripcion();
		
	    LOGGER.info("codigoRespuesta " + codigoRespuesta);
	    LOGGER.info("descripcionRespuesta " + descripcionRespuesta);
	        
	    if (CODIGO_RESPUESTA_OK.equals(codigoRespuesta)) {
		    LOGGER.info("Respuesta del Servicio : " + codigoRespuesta+ " - " + descripcionRespuesta);
		}else{
			LOGGER.info("Respuesta del Servicio : " + codigoRespuesta+ " - " + descripcionRespuesta);
			LOGGER.error( new ServiceException(codigoRespuesta, descripcionRespuesta));
		}
    }
     
     /**
      * Obtiene la informacion acerca del plan actual del usuario SS y CC.
      * 
      * @param numeroPcs
      * @param mercado
      * @param atributoAAA
      * @return
      * @throws DAOException
      * @throws ServiceException
      */
     public PlanBAMBean obtenerPlanActualBAMSSCC(String numeroPcs, String mercado, String atributoAAA, boolean logCajaTrafico) throws DAOException,
     ServiceException {
     	
    	PlanBAMBean planBAMBean = null;
 		ClientePerfilServicePortType port = null;
 		ClientePerfilService service = null;
 		
 		if(logCajaTrafico){
        	CAJA_TRAFICO_LOGGER.info("****** Clase: com.epcs.billing.registrouso.dao.TraficoBamDAO ******");
            CAJA_TRAFICO_LOGGER.info("Instanciando el port");
        }else{
        	LOGGER.info("Instanciando el port");
        }
 	    try {
 		    port = (ClientePerfilServicePortType) WebServiceLocator
 		            .getInstance().getPort(ClientePerfilService.class,
 		                    ClientePerfilServicePortType.class);
 		    service = (ClientePerfilService) WebServiceLocator
					.getInstance().getService(ClientePerfilService.class, false);
 		} catch (WebServiceLocatorException e) {
 		    LOGGER.error("Error al inicializar el Port "+ ClientePerfilService.class);
 		   if(logCajaTrafico){
           	CAJA_TRAFICO_LOGGER.error("Error al inicializar el Port "
                       + ClientePerfilService.class, e);
           }else{
           	LOGGER.error("Error al inicializar el Port "
                       + ClientePerfilService.class, e);
           }
 		    LOGGER.error( new DAOException(e));
 		}
 		
 		if(logCajaTrafico){
        	CAJA_TRAFICO_LOGGER.info("Configurando Datos de la peticion");
        	CAJA_TRAFICO_LOGGER.info("****** INICIO INPUT ******");
        	CAJA_TRAFICO_LOGGER.info("IdSistema: ");
        	CAJA_TRAFICO_LOGGER.info("IdUsuario: ");
        	CAJA_TRAFICO_LOGGER.info("Msisdn: " + numeroPcs);
            CAJA_TRAFICO_LOGGER.info("Aaa: " + atributoAAA);
            CAJA_TRAFICO_LOGGER.info("Mercado: " + mercado);
            CAJA_TRAFICO_LOGGER.info("****** FIN INPUT ******");
        }else{
        	LOGGER.info("Configurando Datos de la peticion");
        }
 		ConsultarPlanFullActualBAMSSCCType request = new ConsultarPlanFullActualBAMSSCCType();
 		request.setIdSistema("");
 		request.setIdUsuario("");
 		request.setMsisdn(numeroPcs);
 		request.setMercado(mercado);
 		request.setAaa(atributoAAA);
 		
 		if(logCajaTrafico){
        	CAJA_TRAFICO_LOGGER.info("Invocando servicio");
            CAJA_TRAFICO_LOGGER.info("Service name:           " + service.getServiceName());
    	    CAJA_TRAFICO_LOGGER.info("WSDL Document Location: " + service.getWSDLDocumentLocation());
            CAJA_TRAFICO_LOGGER.info("Operacion:              ConsultarTraficoSSBAM");
        }else{
        	LOGGER.info("Invocando servicio");
        }
 		ResultadoConsultarPlanFullActualBAMSSCCType response = null;
 
 		try{
 		   response = port.consultarPlanFullActualBAMSSCC(request);
 			
         } catch (Exception e) {
             if(logCajaTrafico){
         		CAJA_TRAFICO_LOGGER.error("Exception caught on Service invocation: "
                  		 + "consultarPlanFullActualBAMSSCC.", e);
         	 }else{
         		LOGGER.error("Exception caught on Service invocation: "
               		 + "consultarPlanFullActualBAMSSCC.", e);
         	 }
             LOGGER.error( new DAOException(e));
         }
 		
 		String codigoRespuesta = response.getRespuesta().getCodigo();
 		String descripcionRespuesta = response.getRespuesta().getDescripcion();
 		
 		if (Utils.isEmptyString(codigoRespuesta)) {
        	CAJA_TRAFICO_LOGGER.error("Servicio ConsultarPlanFullActualBAMSSCC no respondio "
                    + "con codigoRespuesta");
            LOGGER.error( new DAOException("Servicio ConsultarPlanFullActualBAMSSCC no respondio "
                    + "con codigoRespuesta"));
        }
 		
 		if(logCajaTrafico){
        	CAJA_TRAFICO_LOGGER.info("****** INICIO OUTPUT ******");
            CAJA_TRAFICO_LOGGER.info("codigoRespuesta:      " + codigoRespuesta);
            CAJA_TRAFICO_LOGGER.info("descripcionRespuesta: " + descripcionRespuesta);
        }else{
	        LOGGER.info("codigoRespuesta " + codigoRespuesta);
	        LOGGER.info("descripcionRespuesta " + descripcionRespuesta);
        }

 		if (CODIGO_RESPUESTA_OK.equals(codigoRespuesta)) {
 			try{
 			    PlanFullActualBAMSSCCType planActual =  response.getPlanFullActualBAMSSCC();
 			    planBAMBean = new PlanBAMBean();
 			    
 			    CAJA_TRAFICO_LOGGER.info("****** Incio Plan Full Actual BAM SSCC ******");
                CAJA_TRAFICO_LOGGER.info("planActual.getTipoPlan() " + planActual.getTipoPlan());
                CAJA_TRAFICO_LOGGER.info("planActual.getCodbscs1() " + planActual.getCodbscs1());
                CAJA_TRAFICO_LOGGER.info("planActual.getCodbscs2() " + planActual.getCodbscs2());
                CAJA_TRAFICO_LOGGER.info("planActual.getNombrePlan() " + planActual.getNombrePlan());
                CAJA_TRAFICO_LOGGER.info("planActual.getCargoFijo() " + planActual.getCargoFijo());
                CAJA_TRAFICO_LOGGER.info("planActual.getUmbralFairUseMB() " + planActual.getUmbralFairUseMB());
                CAJA_TRAFICO_LOGGER.info("planActual.getUmbralFairUseGB() " + planActual.getUmbralFairUseGB());
                CAJA_TRAFICO_LOGGER.info("planActual.getVelocidadMaxPlan() " + planActual.getVelocidadMaxPlan());
                CAJA_TRAFICO_LOGGER.info("planActual.getValorAdicionalMB() " + planActual.getValorAdicionalMB());
                CAJA_TRAFICO_LOGGER.info("planActual.getVelocidadFairUse() " + planActual.getVelocidadFairUse());
                CAJA_TRAFICO_LOGGER.info("****** Fin Plan Full Actual BAM SSCC ******");
                
 			    planBAMBean.setTipoPlan(planActual.getTipoPlan());
 			    planBAMBean.setCodbscs1(planActual.getCodbscs1());
 			    planBAMBean.setCodbscs2(planActual.getCodbscs2());
 			    planBAMBean.setNombrePlan(PlanBAMHelper.extraerNombrePlan(planActual.getNombrePlan()));
 			    planBAMBean.setCargoFijoPlan(Double.parseDouble(planActual.getCargoFijo()));
 			    if(MiEntelProperties.getProperty("parametros.planesbam.ilimitado")
 			    		.equalsIgnoreCase(planActual.getUmbralFairUseMB())){
 			    	planBAMBean.setUmbralFairUseMb(MiEntelProperties.getProperty("parametros.planesbam.ilimitadoLabel"));
                }else{
                	planBAMBean.setUmbralFairUseMb(planActual.getUmbralFairUseMB());
                }
 			    if(MiEntelProperties.getProperty("parametros.planesbam.ilimitado")
			    		.equalsIgnoreCase(planActual.getUmbralFairUseGB())){
			    	planBAMBean.setUmbralFairUseGb(MiEntelProperties.getProperty("parametros.planesbam.ilimitadoLabel"));
                }else{
                	planBAMBean.setUmbralFairUseGb(planActual.getUmbralFairUseGB());
                }
 			    planBAMBean.setVelocidadMaxPlan(planActual.getVelocidadMaxPlan());
 			    planBAMBean.setValorAdicionalMB(planActual.getValorAdicionalMB());
 			    planBAMBean.setVelocidadFairUse( Utils.isNotEmptyString(planActual.getVelocidadFairUse()) ? planActual.getVelocidadFairUse() : "" );
 			    
 			   CAJA_TRAFICO_LOGGER.info("****** FIN OUTPUT ******");
 			    
 			} catch (Exception e) {
                 if(logCajaTrafico)
                     CAJA_TRAFICO_LOGGER.error("Exception caught on Service response: "
                    		+ "consultarPlanFullActualBAMSSCC.", e);
             	else
             		LOGGER.error("Exception caught on Service response: "
                		+ "consultarPlanFullActualBAMSSCC.", e);
                 LOGGER.error( new DAOException(e));
             }
 
 		}else {
 		   if(logCajaTrafico)
 			   CAJA_TRAFICO_LOGGER.error("Service error code received: " + codigoRespuesta
	                       + " - " + descripcionRespuesta);
	       else
	    	   LOGGER.error("Service error code received: " + codigoRespuesta
		                    + " - " + descripcionRespuesta);
 		    LOGGER.error( new ServiceException(codigoRespuesta, descripcionRespuesta));
 		}

 		return planBAMBean;
     }
     
     /**
      * Obtiene la informacion acerca del plan contratado del usuario PP
      * 
      * @param numeroPcs
      * @return
      * @throws DAOException
      * @throws ServiceException
      */
     public PlanResumenBAMPPBean obtenerPlanResumenBAMPP(String numeroPcs) throws DAOException,ServiceException {
     	
    	PlanResumenBAMPPBean planResumenBAMPPBean = null;
 		ClientePerfilServicePortType port = null;
 		
 		LOGGER.info("Instanciando el Port.");
 	    try {
 		    port = (ClientePerfilServicePortType) WebServiceLocator
 		            .getInstance().getPort(ClientePerfilService.class,
 		                    ClientePerfilServicePortType.class);
 		} catch (WebServiceLocatorException e) {
 		    LOGGER.error("Error al inicializar el Port "+ ClientePerfilService.class);
 		    LOGGER.error( new DAOException(e));
 		}
 		
 		LOGGER.info("Configurando parametros de la peticion");
 		ConsultarPlanResumenPPType  request = new ConsultarPlanResumenPPType();
 		request.setMsisdn(numeroPcs);
 		
 		LOGGER.info("Invocando servicio");
 		ResultadoConsultarPlanResumenPPType response = null;
 
 		try{
 		   response = port.consultarPlanResumenPP(request);
 			
         } catch (Exception e) {
             LOGGER.error("Exception caught on Service invocation: "
            		 + "consultarPlanResumenPP.", e);
             LOGGER.error( new DAOException(e));
         }
 		
 		String codigoRespuesta = response.getRespuesta().getCodigo();
 		String descripcionRespuesta = response.getRespuesta().getDescripcion();
 		
 	    LOGGER.info("codigoRespuesta " + codigoRespuesta);
 	    LOGGER.info("descripcionRespuesta " + descripcionRespuesta);

 		if (CODIGO_RESPUESTA_OK.equals(codigoRespuesta)) {
 			try{

 				ResPlanResumenPPType planResumen = response.getResPlanResumenPP();
 				planResumenBAMPPBean = new PlanResumenBAMPPBean();
 				planResumenBAMPPBean.setSaldo(planResumen.getSaldo());
 				planResumenBAMPPBean.setFechaExpiracion(DateHelper.parseDate(planResumen.getFechaExpiracion(),DateHelper.FORMAT_yyyyMMddhhmmss));
 				
 			} catch (Exception e) {
                 LOGGER.error("Exception caught on Service response: "
                 		+ "consultarPlanResumenPP.", e);
                 LOGGER.error( new DAOException(e));
             }
 
 		}else {
 		    LOGGER.info("Respuesta del Servicio : " +codigoRespuesta+ " - " + descripcionRespuesta);
 		    LOGGER.error( new ServiceException(codigoRespuesta, descripcionRespuesta));
 		}

 		return planResumenBAMPPBean;
     }

     /**
      * Obtiene los Planes Disponibles para un usuario SS o CC
      * 
      * @param numeroPcs
      * @param mercado
      * @return
      * @throws DAOException
      * @throws ServiceException
      */
     public List<PlanBAMBean> obtenerPlanesDisponiblesBAM(String msisdn) throws DAOException,
     ServiceException {

 		ClientePerfilServicePortType port = null; 		
 		List<PlanBAMType> planesListCC = null;
 		List<PlanBAMType> planesListSS = null;
 	    this.planesList = new ArrayList<PlanBAMBean>();
 	    
 		LOGGER.info("Instanciando el Port.");
 		try {
 		    port = (ClientePerfilServicePortType) WebServiceLocator
 		            .getInstance().getPort(ClientePerfilService.class,
 		                    ClientePerfilServicePortType.class);
 		
 		} catch (WebServiceLocatorException e) {
 		    LOGGER.error("Error al inicializar el Port "+ ClientePerfilService.class);
 		    LOGGER.error( new DAOException(e));
 		}
 		
 		LOGGER.info("Configurando parametros de la peticion");
 		ConsultarPlanesDisponiblesBAMType request = new ConsultarPlanesDisponiblesBAMType();
 		request.setMsisdn(msisdn);
 		LOGGER.info("Invocando servicio");
 		ResultadoConsultarPlanesDisponiblesBAMType response = null;
 		
 		try{
 			
 		   response = port.consultarPlanesDisponiblesBAM(request);
         
 		} catch (Exception e) {
             LOGGER.error("Exception caught on Service invocation: "
            		 + "consultarPlanesDisponiblesBAM.", e);
             LOGGER.error( new DAOException(e));
         }
     
 		String codigoRespuesta = response.getRespuesta().getCodigo();
 		String descripcionRespuesta = response.getRespuesta().getDescripcion();
 		
 	    LOGGER.info("codigoRespuesta " + codigoRespuesta);
 	    LOGGER.info("descripcionRespuesta " + descripcionRespuesta);
 	  
 		if (CODIGO_RESPUESTA_OK.equals(codigoRespuesta)) {
 			try{

 	 			planesListSS = response.getPlanesDisponiblesSS();
 	 			planesListCC = response.getPlanesDisponiblesCC();

 	 			addPlanes(planesListSS, "SS");
 	 			addPlanes(planesListCC, "CC"); 			 			
 				
 				
 			} catch (Exception e) {
                 LOGGER.error("Exception caught on Service response: "
                  		+ "consultarPlanesDisponiblesBAM.", e);
                 LOGGER.error( new DAOException(e));
            }

 		}else {
 		    LOGGER.info("Respuesta del Servicio : " + codigoRespuesta+ " - " + descripcionRespuesta);
 		    LOGGER.error( new ServiceException(codigoRespuesta, descripcionRespuesta));
 		}
 		return planesList;
     }
     
     /**
      * Realiza la validacion de Bloqueo Temporal mercado SS o CC
      * 
      * @param msisdn
      * @return
      * @throws DAOException
      * @throws ServiceException
      */
     public void validacionBloqueoTemporal(String msisdn) throws DAOException,
     ServiceException {

        ClientePerfilServicePortType port = null;      
           
        LOGGER.info("Instanciando el Port.");
        try {
            port = (ClientePerfilServicePortType) WebServiceLocator
                    .getInstance().getPort(ClientePerfilService.class,
                            ClientePerfilServicePortType.class);
        
        } catch (WebServiceLocatorException e) {
            LOGGER.error("Error al inicializar el Port "+ ClientePerfilService.class);
            LOGGER.error( new DAOException(e));
        }
        
        LOGGER.info("Configurando parametros de la peticion");
        ValidacionBloqueoTemporalType request = new ValidacionBloqueoTemporalType();
        request.setMsisdn(msisdn);
        LOGGER.info("Invocando servicio");
        ResultadoValidacionBloqueoTemporalType response = null;
        
        try{
            
           response = port.validacionBloqueoTemporal(request);
         
        } catch (Exception e) {
             LOGGER.error("Exception caught on Service invocation: "
            		 + "validacionBloqueoTemporal.", e);
             LOGGER.error( new DAOException(e));
         }
     
        String codigoRespuesta = response.getRespuesta().getCodigo();
        String descripcionRespuesta = response.getRespuesta().getDescripcion();
        
        LOGGER.info("codigoRespuesta " + codigoRespuesta);
        LOGGER.info("descripcionRespuesta " + descripcionRespuesta);
      
        if (CODIGO_RESPUESTA_OK.equals(codigoRespuesta)) {
            try{
                
                LOGGER.info("Validacion Bloqueo Temporal Correcta para :"+msisdn);
                
            } catch (Exception e) {
                 LOGGER.error("Exception inesperada al realizar "
                		 + "la validacion de Bloqueo Temporal.", e);
                 LOGGER.error( new DAOException(e));
            }

        }else {
            LOGGER.info("Respuesta del Servicio : " + codigoRespuesta+ " - " + descripcionRespuesta);
            LOGGER.error( new ServiceException(codigoRespuesta, descripcionRespuesta));
        }
        
     }
     
     
     /**
      * Realiza la validacion Comercial mercado SS o CC
      * 
      * @param msisdn
      * @param codbscs2
      * @return
      * @throws DAOException
      * @throws ServiceException
      */
     public void validacionComercial(String msisdn, String codbscs2) throws DAOException,
     ServiceException {

        ClientePerfilServicePortType port = null;      
           
        LOGGER.info("Instanciando el Port.");
        try {
            port = (ClientePerfilServicePortType) WebServiceLocator
                    .getInstance().getPort(ClientePerfilService.class,
                            ClientePerfilServicePortType.class);
        
        } catch (WebServiceLocatorException e) {
            LOGGER.error("Error al inicializar el Port "+ ClientePerfilService.class);
            LOGGER.error( new DAOException(e));
        }
        
        LOGGER.info("Configurando parametros de la peticion");
        ValidacionComercialType request = new ValidacionComercialType();
        request.setMsisdn(msisdn);
        request.setCodigobscs2(codbscs2);
        LOGGER.info("Invocando servicio");
        ResultadoValidacionComercialType response = null;
        
        try{
            
           response = port.validacionComercial(request);
         
        } catch (Exception e) {
             LOGGER.error("Exception caught on Service invocation: "
            		 + "validacionComercial.", e);
             LOGGER.error( new DAOException(e));
         }
     
        String codigoRespuesta = response.getRespuesta().getCodigo();
        String descripcionRespuesta = response.getRespuesta().getDescripcion();
        
        LOGGER.info("codigoRespuesta " + codigoRespuesta);
        LOGGER.info("descripcionRespuesta " + descripcionRespuesta);
      
        if (CODIGO_RESPUESTA_OK.equals(codigoRespuesta)) {
            try{
                
                LOGGER.info("Validacion Comercia Correcta para :"+msisdn+ "Bolsa :"+codbscs2);
                
            } catch (Exception e) {
                 LOGGER.error("Exception inesperada al realizar "
                		 + "la validacion comercial.", e);
                 LOGGER.error( new DAOException(e));
            }

        }else {
            LOGGER.info("Respuesta del Servicio : " + codigoRespuesta+ " - " + descripcionRespuesta);
            LOGGER.error( new ServiceException(codigoRespuesta, descripcionRespuesta));
        }
        
     }
     

     
     /**
      * Agrega a una lista los planes disponibles para BAM SS y CC.
      * 
      * @param planesListSSCC
      * @param tipoMercado
      */
     private void addPlanes(List<PlanBAMType> planesListSSCC, String tipoMercado){
    	 
    	 for( PlanBAMType types : planesListSSCC ){
    		 
    		 try{
				PlanBAMBean planBean = new PlanBAMBean();
				planBean.setTipoPlan(types.getTipoPlan());	
				planBean.setCodbscs1(types.getCodbscs1());
				planBean.setCodbscs2(types.getCodbscs2());
				planBean.setNombrePlan(PlanBAMHelper.extraerNombrePlan(types.getNombrePlan()));
				planBean.setCargoFijoPlan(Double.parseDouble(types.getCargoFijo()));
				planBean.setUmbralFairUseMb(PlanBAMHelper.obtenerTasacionPlanBAM(types.getDetallePlan(), "umbralFairUseMB"));
				planBean.setVelocidadMaxPlan(PlanBAMHelper.obtenerTasacionPlanBAM(types.getDetallePlan(), "velocidadMaxPlan"));
				
				planBean.setTipoMercado(tipoMercado);
				
								
				planesList.add(planBean);

    		 } catch (Exception e) {
                 LOGGER.warn("Exception al agregar planes disponibles a la lista.", e);
             }
		}
     } 
}
