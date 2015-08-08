/* Propiedad de Entel PCS. Todos los derechos reservados */
package com.epcs.administracion.suscripciones.controller;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.bea.content.Node;
import com.bea.p13n.usermgmt.profile.ProfileWrapper;
import com.epcs.administracion.suscripciones.delegate.PlanesSeguridadBAMDelegate;
import com.epcs.bean.CredencialesPlanSeguridadBean;
import com.epcs.bean.PlanSeguridadBAMBean;
import com.epcs.bean.TransaccionGTMBean;
import com.epcs.bean.UsuarioBean;
import com.epcs.cliente.perfil.delegate.CuentaDelegate;
import com.epcs.recursoti.configuracion.DateHelper;
import com.epcs.recursoti.configuracion.JsonHelper;
import com.epcs.recursoti.configuracion.MiEntelBusinessHelper;
import com.epcs.recursoti.configuracion.MiEntelProperties;
import com.epcs.recursoti.configuracion.Utils;
import com.epcs.recursoti.configuracion.jsf.utils.JSFMessagesHelper;
import com.epcs.recursoti.configuracion.jsf.utils.JSFPortletHelper;
import com.epcs.recursoti.configuracion.jsf.utils.JsfUtil;
import com.epcs.recursoti.configuracion.uup.ProfileWrapperHelper;
import com.epcs.recursoti.excepcion.DAOException;
import com.epcs.recursoti.excepcion.ServiceException;

/**
 * @author jivasquez (I2B) en nombre de Absalon Opazo (Atencion al Cliente, EntelPcs)
 *
 */
public class PlanesSeguridadBAMCCSSController {
	
	private static final Logger LOGGER = Logger.getLogger(PlanesSeguridadBAMCCSSController.class);
    private static final String CODIGO_RESPUESTA_NO_INFO = MiEntelProperties
    		.getProperty("servicios.codigoRespuesta.noInfo");
    
	private static final String PAGE_LABEL_PLANES_SEGURIDAD_CC = MiEntelProperties
			.getProperty("parametros.planesSeguridadBAM.pageLabel.cc");
	private static final String PAGE_LABEL_PLANES_SEGURIDAD_SS = MiEntelProperties
			.getProperty("parametros.planesSeguridadBAM.pageLabel.ss");
	private static final String CLICK_BANNER_ATTTR_NAME = MiEntelProperties
			.getProperty("parametros.bannerSeguridad.click.attr.name");
	private static final String PLAN_PROMO_ATTTR_NAME = MiEntelProperties
			.getProperty("parametros.planesSeguridadBAM.planPromo.attr.name");
	
	private PlanesSeguridadBAMDelegate planesSeguridadBAMDelegate;
	private CuentaDelegate cuentaDelegate;
	private PlanSeguridadBAMBean planActual;
	private List<PlanSeguridadBAMBean> planesDisponiblesSS;
	private PlanSeguridadBAMBean planPromo;
	private String operacion;
	private String mailUsuario;
	private String respuestaComprarPlanJson;
	private String respuestaActualizarEmailJson;
	private String urlDescarga;
	private String pageLabelPlanes;
	private boolean hayPlanesDisponibles;
	private boolean tienePlanActual;
	private boolean planActualActivo;
	private boolean cuentaSuspendida;
	private boolean cambioPlan;
	private boolean mostrarPromoBanner;
	private boolean mostrarPromoSeguridad;
	private boolean aplicaPromo;
	
	/**
	 * @return the planesSeguridadBAMDelegate
	 */
	public PlanesSeguridadBAMDelegate getPlanesSeguridadBAMDelegate() {
		return planesSeguridadBAMDelegate;
	}
	/**
	 * @param planesSeguridadBAMDelegate the planesSeguridadBAMDelegate to set
	 */
	public void setPlanesSeguridadBAMDelegate(
			PlanesSeguridadBAMDelegate planesSeguridadBAMDelegate) {
		this.planesSeguridadBAMDelegate = planesSeguridadBAMDelegate;
	}
	/**
	 * @return the cuentaDelegate
	 */
	public CuentaDelegate getCuentaDelegate() {
		return cuentaDelegate;
	}
	/**
	 * @param cuentaDelegate the cuentaDelegate to set
	 */
	public void setCuentaDelegate(CuentaDelegate cuentaDelegate) {
		this.cuentaDelegate = cuentaDelegate;
	}
	/**
	 * @return the planActual
	 */
	public PlanSeguridadBAMBean getPlanActual() {
		return planActual;
	}
	/**
	 * @param planActual the planActual to set
	 */
	public void setPlanActual(PlanSeguridadBAMBean planActual) {
		this.planActual = planActual;
	}
	/**
	 * @return the planesDisponiblesSS
	 */
	public List<PlanSeguridadBAMBean> getPlanesDisponiblesSS() {
		return planesDisponiblesSS;
	}
	/**
	 * @param planesDisponiblesSS the planesDisponiblesSS to set
	 */
	public void setPlanesDisponiblesSS(
			List<PlanSeguridadBAMBean> planesDisponiblesSS) {
		this.planesDisponiblesSS = planesDisponiblesSS;
	}
	/**
	 * @return the operacion
	 */
	public String getOperacion() {
		return operacion;
	}
	/**
	 * @param operacion the operacion to set
	 */
	public void setOperacion(String operacion) {
		this.operacion = operacion;
	}
	/**
	 * @return the mailUsuario
	 */
	public String getMailUsuario() {
		return mailUsuario;
	}
	/**
	 * @param mailUsuario the mailUsuario to set
	 */
	public void setMailUsuario(String mailUsuario) {
		this.mailUsuario = mailUsuario;
	}
	/**
	 * @return the respuestaComprarPlanJson
	 */
	public String getRespuestaComprarPlanJson() {
		return respuestaComprarPlanJson;
	}
	/**
	 * @param respuestaComprarPlanJson the respuestaComprarPlanJson to set
	 */
	public void setRespuestaComprarPlanJson(String respuestaComprarPlanJson) {
		this.respuestaComprarPlanJson = respuestaComprarPlanJson;
	}
	/**
	 * @return the respuestaActualizarEmailJson
	 */
	public String getRespuestaActualizarEmailJson() {
		return respuestaActualizarEmailJson;
	}
	/**
	 * @param respuestaActualizarEmailJson the respuestaActualizarEmailJson to set
	 */
	public void setRespuestaActualizarEmailJson(String respuestaActualizarEmailJson) {
		this.respuestaActualizarEmailJson = respuestaActualizarEmailJson;
	}
	/**
	 * @return the urlDescarga
	 */
	public String getUrlDescarga() {
		if(Utils.isEmptyString(urlDescarga))
			return "";
		else
			return urlDescarga
				.replace("{mail}", planActual != null ? planActual.getMail() : "")
				.replace("{password}", planActual != null ? planActual.getPassword() : "")
				.replace("{imsi}", planActual != null ? planActual.getImsi() : "");
	}
	/**
	 * @param urlDescarga the urlDescarga to set
	 */
	public void setUrlDescarga(String urlDescarga) {
		this.urlDescarga = urlDescarga;
	}
	/**
	 * @return the pageLabelPlanes
	 */
	public String getPageLabelPlanes() {
		return pageLabelPlanes;
	}
	/**
	 * @param pageLabelPlanes the pageLabelPlanes to set
	 */
	public void setPageLabelPlanes(String pageLabelPlanes) {
		this.pageLabelPlanes = pageLabelPlanes;
	}
	/**
	 * @return the planPromo
	 */
	public PlanSeguridadBAMBean getPlanPromo() {
		return planPromo;
	}
	/**
	 * @param planPromo the planPromo to set
	 */
	public void setPlanPromo(PlanSeguridadBAMBean planPromo) {
		this.planPromo = planPromo;
	}
	/**
	 * @return the hayPlanesDisponibles
	 */
	public boolean isHayPlanesDisponibles() {
		return hayPlanesDisponibles;
	}
	/**
	 * @param hayPlanesDisponibles the hayPlanesDisponibles to set
	 */
	public void setHayPlanesDisponibles(boolean hayPlanesDisponibles) {
		this.hayPlanesDisponibles = hayPlanesDisponibles;
	}
	/**
	 * @return the tienePlanActual
	 */
	public boolean isTienePlanActual() {
		return tienePlanActual;
	}
	/**
	 * @param tienePlanActual the tienePlanActual to set
	 */
	public void setTienePlanActual(boolean tienePlanActual) {
		this.tienePlanActual = tienePlanActual;
	}
	/**
	 * @return the planActualActivo
	 */
	public boolean isPlanActualActivo() {
		return planActualActivo;
	}
	/**
	 * @param planActualActivo the planActualActivo to set
	 */
	public void setPlanActualActivo(boolean planActualActivo) {
		this.planActualActivo = planActualActivo;
	}
	
	/**
	 * @return the cuentaSuspendida
	 */
	public boolean isCuentaSuspendida() {
		return cuentaSuspendida;
	}
	/**
	 * @param cuentaSuspendida the cuentaSuspendida to set
	 */
	public void setCuentaSuspendida(boolean cuentaSuspendida) {
		this.cuentaSuspendida = cuentaSuspendida;
	}
	/**
	 * @return the cambioPlan
	 */
	public boolean isCambioPlan() {
		return cambioPlan;
	}
	/**
	 * @param cambioPlan the cambioPlan to set
	 */
	public void setCambioPlan(boolean cambioPlan) {
		this.cambioPlan = cambioPlan;
	}
	/**
	 * @return the mostrarPromo
	 */
	public boolean isMostrarPromoBanner() {
		return mostrarPromoBanner;
	}
	/**
	 * @param mostrarPromoBanner the mostrarPromoBanner to set
	 */
	public void setMostrarPromoBanner(boolean mostrarPromoBanner) {
		this.mostrarPromoBanner = mostrarPromoBanner;
	}
	/**
	 * @return the mostrarPromoSeguridad
	 */
	public boolean isMostrarPromoSeguridad() {
		return mostrarPromoSeguridad;
	}
	/**
	 * @param mostrarPromoSeguridad the mostrarPromoSeguridad to set
	 */
	public void setMostrarPromoSeguridad(boolean mostrarPromoSeguridad) {
		this.mostrarPromoSeguridad = mostrarPromoSeguridad;
	}
	/**
	 * @return the aplicaPromo
	 */
	public boolean isAplicaPromo() {
		return aplicaPromo;
	}
	/**
	 * @param aplicaPromo the aplicaPromo to set
	 */
	public void setAplicaPromo(boolean aplicaPromo) {
		this.aplicaPromo = aplicaPromo;
	}
	
	/**
     * Invoca los action methods que obtienen la informacion de planes de seguridad .
     * @param phase
     */
    public void initCargarPlanesSeguridadBAM(PhaseEvent phase) {
        try {
            LOGGER.info("phase " + phase.getPhaseId());
            //Obtenemos datos necesarios para consulta
            ProfileWrapper profile = ProfileWrapperHelper.getProfile(JSFPortletHelper.getRequest());
            String msisdn = ProfileWrapperHelper.getPropertyAsString(profile, "numeroPcsSeleccionado");
            String rut = ProfileWrapperHelper.getPropertyAsString(profile, "rutUsuarioSeleccionado");
            String aaa = ProfileWrapperHelper.getPropertyAsString(profile, "aaa");
            
            /*
             * Validamos que el usuario tenga suficientes permisos para 
             * contratar o cambiar un plan de seguridad BAM
             */
            if(Integer.parseInt(aaa) >= Integer.parseInt(
            		MiEntelProperties.getProperty("aaa.controlTotal.code"))){
            	/*
                 * Preguntamos si la peticion viene del banner; para ello verificamos
                 * si en el request viene el flag que nos indica esto.
                 */
                mostrarPromoBanner = isParametroUrlBanner();
                mostrarPromoSeguridad = isParametroIdPlanPromo();
                
                //Validacion tomada de MiEntel v2, pero modificada para tomar 
                //el estado de la cuenta desde la session
                HttpServletRequest request = JSFPortletHelper
                		.getRequest(FacesContext.getCurrentInstance());
	             
	            HttpSession session = request.getSession();
	            String flagEstadoMsisdn = (String)session.getAttribute("flagEstadoMsisdn");
	            
                if(Utils.isNotEmptyString(flagEstadoMsisdn) && 
                		flagEstadoMsisdn.equalsIgnoreCase("S")){
                	cuentaSuspendida = true;
                }else if(mostrarPromoBanner){
            		String idContenido = MiEntelProperties.getProperty("parametros.bannerSeguridad.infoPlan.idContenido");
                	Node contenido = JSFPortletHelper.getContentNode("idContenido", idContenido);
                	/*
                	 * La propiedad 'html' del contenido recuperado, contiene una cadena con el siguiente formato:
                	 * <CODIGO_PLAN>|<NOMBRE_PLAN>|<PRECIO_PLAN>. Por esta razon hacemos un 'split' de la cadena
                	 * obtenida e inicializamos un objeto de tipo 'PlanSeguridadBAMBean' que representa al plan 
                	 * asociado a la promocion de seguridad BAM.
                	 */
                	String [] infoPlan = contenido.getProperty("html").getValue().getStringValue().split("\\|");
                	planPromo = new PlanSeguridadBAMBean();
                	planPromo.setCodigoPlan(infoPlan[0]);//<CODIGO_PLAN>
                	planPromo.setNombrePlan(infoPlan[1]);//<NOMBRE_PLAN>
                	planPromo.setValorPlan(infoPlan[2]);//<PRECIO_PLAN>
                	        	
    		        
                	//Consultamos plan actual de seguridad
                    consultarPlanActualSeguridad(msisdn);
                    if(!this.planActualActivo){
                    	String email = obtenerMailUsuario(msisdn, rut);
                    	
                    	//Validamos que se pueda contratar el plan
                        boolean validarPlan = validarPlanSeguridad(
                        		msisdn, email, infoPlan[0], "0");
                        if(validarPlan){
                        	aplicaPromo = true;
                        }
                    }
                	
                	/*
                	 * Dependiendo del mercardo, se obtiene el pageLabel de la pagina de planes de seguridad BAM.
                	 * Esto, en caso que el usuario haya tenido un problema de validacion y no pueda acceder a la 
                	 * promocion o sencillameente quiera ir a ver los demas planes de seguridad.
                	 */
                	String mercado = ProfileWrapperHelper.getPropertyAsString(profile, "mercado");
                	if(MiEntelBusinessHelper.isMercadoCuentaControlada(mercado)){
                		pageLabelPlanes = JSFPortletHelper.getPreference(
            					JSFPortletHelper.getPreferencesObject(), PAGE_LABEL_PLANES_SEGURIDAD_CC, null);
                	}else if(MiEntelBusinessHelper.isMercadoSuscripcion(mercado)){
                		pageLabelPlanes = JSFPortletHelper.getPreference(
            					JSFPortletHelper.getPreferencesObject(), PAGE_LABEL_PLANES_SEGURIDAD_SS, null);
                	}
                	 
            	}else if(mostrarPromoSeguridad){
            		String idContenido = MiEntelProperties
            			.getProperty("parametros.planesSeguridadBAM.planPromo.infoPlan.idContenido");
            		String idPlan = getValorParametroPlanPromo();
            		/*
            		 * El contenido recuperado debe tener su propiedad idContenido definida con 
            		 * siguiente formato: <idContenidoBase><idPlan>, sin espacios.
            		 */
                	Node contenido = JSFPortletHelper.getContentNode("idContenido", idContenido + idPlan);
                	/*
                	 * La propiedad 'html' del contenido recuperado, contiene una cadena con el siguiente formato:
                	 * <CODIGO_PLAN>|<NOMBRE_PLAN>|<PRECIO_PLAN>. Por esta razon hacemos un 'split' de la cadena
                	 * obtenida e inicializamos un objeto de tipo 'PlanSeguridadBAMBean' que representa al plan 
                	 * asociado a la promocion de seguridad BAM.
                	 */
                	String [] infoPlan = contenido.getProperty("html").getValue().getStringValue().split("\\|");
                	planPromo = new PlanSeguridadBAMBean();
                	planPromo.setCodigoPlan(infoPlan[0]);//<CODIGO_PLAN>
                	planPromo.setNombrePlan(infoPlan[1]);//<NOMBRE_PLAN>
                	planPromo.setValorPlan(infoPlan[2]);//<PRECIO_PLAN>
                	
                	/*
                	 * Dependiendo del mercardo, se obtiene el pageLabel de la pagina de planes de seguridad BAM.
                	 * Esto, en caso que el usuario haya tenido un problema de validacion y no pueda acceder a la 
                	 * promocion o sencillameente quiera ir a ver los demas planes de seguridad.
                	 */
                	String mercado = ProfileWrapperHelper.getPropertyAsString(profile, "mercado");
                	if(MiEntelBusinessHelper.isMercadoCuentaControlada(mercado)){
                		pageLabelPlanes = JSFPortletHelper.getPreference(
            					JSFPortletHelper.getPreferencesObject(), PAGE_LABEL_PLANES_SEGURIDAD_CC, null);
                	}else if(MiEntelBusinessHelper.isMercadoSuscripcion(mercado)){
                		pageLabelPlanes = JSFPortletHelper.getPreference(
            					JSFPortletHelper.getPreferencesObject(), PAGE_LABEL_PLANES_SEGURIDAD_SS, null);
                	}
            		
            	}else{
                	//Consultamos plan actual de seguridad
                    consultarPlanActualSeguridad(msisdn);
                    this.urlDescarga = "";
                    
                    //Si tiene plan actual activo, consultamos las credenciales del plan
                    if(planActualActivo){
                    	consultarCredencialesPlanSeguridad(msisdn);
                    	//Id del contenido con la URL para descargar el software de McAfee comprado por el usuario
                    	String idContenido = MiEntelProperties.getProperty("parametros.planesSeguridadBAM.urlDescarga.idContenido");
                    	Node contenido = JSFPortletHelper.getContentNode("idContenido", idContenido);
                    	this.urlDescarga = contenido.getProperty("html").getValue().getStringValue();
                    }
                    
                    //Consultamos plan actual los planes disponibles
                    this.obtenerPlanesSeguridadDisponiblesCCSS(msisdn);
                    if(hayPlanesDisponibles){
                    	for (PlanSeguridadBAMBean plan : planesDisponiblesSS) {
                            setTextoBoton(plan);
                        }
                    }
                }
                
                /* Mail a mostrar en el casilla verificacion del email */
            	mailUsuario = obtenerMailUsuario(msisdn, rut);
                
                operacion = !tienePlanActual 
    	    		? MiEntelProperties.getProperty("planSeguridadBAM.compra.packNormal.sscc") 
    	    		: MiEntelProperties.getProperty("planSeguridadBAM.cambioPlan");	
            }else{
            	LOGGER.info("No es " + MiEntelProperties.getProperty("aaa.controlTotal.desc")
            			+ " ni" + MiEntelProperties.getProperty("aaa.titular.desc"));
                JSFMessagesHelper.addServiceErrorMessage("aaa");
            }
            
        } catch (Exception e) {
            LOGGER.error("Error inesperado al cargar informacion de planes de seguridad", e);
            JSFMessagesHelper.addServiceErrorMessage("initCargarPlanesSeguridadBAM");
        }

    }
    
    /**
     * Action method para obtener el plan actual de seguridad de un usuario PP 
     * @param event evento disparado al confirmar el canje de una bolsa o recarga
     */
    public void consultarPlanActualSeguridad(String msisdn){
        try{
            //Invocamos el metodo de accion que consulta el plan actual de seguridad
            planActual = this.planesSeguridadBAMDelegate.consultarPlanActualSeguridad(msisdn);
            tienePlanActual = planActual != null && !planActual.getEstadoPlan().equalsIgnoreCase("INACTIVO");
            planActualActivo = planActual != null && planActual.getEstadoPlan().equalsIgnoreCase("ACTIVO");
            
        } catch (DAOException e) {
            LOGGER.error("DAOException al consultar plan actual de seguridad para el movil: " + msisdn,e);
        } catch (ServiceException e) {
        	if(e.getCodigoRespuesta().equals(CODIGO_RESPUESTA_NO_INFO)){
        		LOGGER.info("El movil" + msisdn + " no tiene plan actual de seguridad BAM.");
        	}else{
        		LOGGER.info("ServiceException al consultar plan actual de seguridad para el movil: " + msisdn);
        	}
        } catch (Exception e) {
            LOGGER.error("Exception al consultar plan actual de seguridad para el movil: " + msisdn, e);
        }
        
    }
    
    /**
     * Action method para obtener los planes de seguridad disponibles para PP 
     * @param event evento disparado al confirmar el canje de una bolsa o recarga
     */
    public void obtenerPlanesSeguridadDisponiblesCCSS(String msisdn){
        try{
        	planesDisponiblesSS = new LinkedList<PlanSeguridadBAMBean>(); 
            //Invocamos el metodo de accion que obtiene los planes de seguiridad disponibles para PP
        	List<PlanSeguridadBAMBean> planes = this.planesSeguridadBAMDelegate.
        		obtenerPlanesSeguridadDispobiblesCCSS(msisdn);
        	/*
        	 * Incluimos en planesDisponibleSS solo aquellos con codigo diferente al vetado.
        	 * Esta validacion fue tomada de mientel V2.
        	 */
        	for (PlanSeguridadBAMBean plan : planes) {
        		if(!isPlanVetado(plan)){
        			planesDisponiblesSS.add(plan);
        		}
        		
        	}
            //Variable usada en la vista para saber si hay planes disponibles.
            hayPlanesDisponibles = planesDisponiblesSS != null;
            
        } catch (DAOException e) {
            LOGGER.error("DAOException consultando planes de seguridad disponibles para SS y CC. Numero: "+msisdn, e);
            JSFMessagesHelper.addServiceErrorMessage("obtenerPlanesSeguridadDisponiblesCCSS");
        } catch (ServiceException e) {
            LOGGER.info("ServiceException consultando planes de seguridad disponibles para SS y CC. Numero: "+msisdn);
            JSFMessagesHelper.addErrorCodeMessage("planesDeSeguridad.mcAfee",e.getCodigoRespuesta());
        } catch (Exception e) {
            LOGGER.error("Exception consultando planes de seguridad disponibles para SS y CC: ", e);
            JSFMessagesHelper.addServiceErrorMessage("obtenerPlanesSeguridadDisponiblesCCSS");
        }
        
    }
    
    /**
     * Metodo para determinar si se agrega un plan de seguridad a la lista de 
     * planes disponibles para usuarios SS 
     * @param plan
     */
    private boolean isPlanVetado(PlanSeguridadBAMBean plan){
    	boolean resultado = false;
    	/*
    	 * Primero recuperamos los codigos de planes que no se van mostrar a usuarios SS. 
    	 */
    	String planesVetados = MiEntelProperties.getProperty("planSeguridadBAM.planesVetados.ss");
    	String [] array_vetados = planesVetados.split("[,]");
    	for(int i=0; i<array_vetados.length; i++){
    		if(plan.getCodigoPlan().equals(array_vetados[i])){
    			resultado = true;
    			break;
    		}
    	}
    	return resultado;
    }
    
    /**
     * Action method para consultar las credenciales de un plan de seguridad 
     * @param event evento disparado al confirmar el canje de una bolsa o recarga
     */
    public void consultarCredencialesPlanSeguridad(String msisdn){
        try{
            //Invocamos el metodo de accion que consulta las credenciales del plan actual
        	CredencialesPlanSeguridadBean credenciales = 
        		this.planesSeguridadBAMDelegate.consultarCredencialesPlanSeguridad(msisdn);
        	if(credenciales == null){
        		planActual.setImsi("");
        		planActual.setMail("");
        		planActual.setPassword("");
        	}else{
        		planActual.setImsi(credenciales.getImsi() != null ? credenciales.getImsi() : "");
                planActual.setMail(credenciales.getEmail() != null ? credenciales.getEmail() : "");
                planActual.setPassword(credenciales.getPassword() != null ? credenciales.getPassword() : "");
        	}
            
        } catch (DAOException e) {
            LOGGER.error("DAOException al consultar credenciales plan de seguridad para el movil: " + msisdn, e);
            JSFMessagesHelper.addServiceErrorMessage("consultarCredencialesPlanSeguridad");
        } catch (ServiceException e) {
            LOGGER.info("ServiceException al consultar credenciales plan de seguridad para el movil: " + msisdn);
            JSFMessagesHelper.addErrorCodeMessage("planesDeSeguridad.mcAfee",e.getCodigoRespuesta());
        } catch (Exception e) {
            LOGGER.error("Exception al consultar credenciales plan de seguridad para el movil: " + msisdn, e);
            JSFMessagesHelper.addServiceErrorMessage("consultarCredencialesPlan");
        }
        
    }
    
    /**
     * Action method para consultar el mail de un usuario 
     * @param event evento disparado al confirmar el canje de una bolsa o recarga
     */
    public String obtenerMailUsuario(String msisdn, String rut){
    	
    	String mailUsuario = null;
    	
        try{
            //Obtenemos los datos del usuario para luego retornar el mail
        	UsuarioBean usuario = this.cuentaDelegate.obtenerUsuario(msisdn, rut);
        	mailUsuario = usuario.getEmail();
            
        } catch (DAOException e) {
            LOGGER.error("DAOException al consultar mail del usuario: " + msisdn);
            JSFMessagesHelper.addServiceErrorMessage("obtenerMailUsuario");
        } catch (ServiceException e) {
            LOGGER.info("ServiceException al consultar mail del usuario: "+msisdn);
            JSFMessagesHelper.addErrorCodeMessage("gestionDePerfiles",e.getCodigoRespuesta());
        } catch (Exception e) {
            LOGGER.error("Exception al consultar mail del usuario: " + msisdn, e);
            JSFMessagesHelper.addServiceErrorMessage("obtenerMailUsuario");
        }
        
        return mailUsuario;
    }

    
    /**
     * Action method para validar el estado de un plan de seguridad antes de activarlo
     * @param msisdn
     * @param mailUsuario
     * @param codigoPlan
     * @param valorPlan
     * @return
     */
    public boolean validarPlanSeguridad(
    	String msisdn, String mailUsuario, String codigoPlan, String valorPlan)
    		throws Exception{
    	
    	boolean respuesta = false;
        try{
            respuesta = planesSeguridadBAMDelegate.validarPlanSeguridad(
            		msisdn, mailUsuario, codigoPlan, valorPlan);
            
        } catch (DAOException e) {
            LOGGER.error("DAOException al validar plan de seguridad para el usuario: " + msisdn);
        } catch (ServiceException e) {
            LOGGER.info("ServiceException al validar plan de seguridad para el usuario: " + msisdn);
            JSFMessagesHelper.addErrorCodeMessage("planesDeSeguridad.mcAfee",e.getCodigoRespuesta());
        } catch (Exception e) {
            LOGGER.error("Exception al validar plan de seguridad para el usuario: " + msisdn + "\n", e);
        }
        
        return respuesta;
    }
    
    /**
     * Action method para activar plan de seguridad
     * @param msisdn
     * @param mailUsuario
     * @param codigoPlan
     * @param valorPlan
     * @return
     */
    public boolean activarPlanSeguridad(
    	String msisdn, String tipoMovil, String mailUsuario, String codigoPlan)
    		throws Exception{
    	
    	boolean respuesta = false;
        try{
        	respuesta = planesSeguridadBAMDelegate.activarPlanSeguridad(
        			msisdn, tipoMovil, mailUsuario, codigoPlan);
            
        } catch (DAOException e) {
            LOGGER.error("DAOException al activar plan de seguridad para el usuario: " + msisdn);
        } catch (ServiceException e) {
            LOGGER.info("ServiceException al activar plan de seguridad para el usuario: " + msisdn);
        } catch (Exception e) {
            LOGGER.error("Exception al activar plan de seguridad para el usuario: " + msisdn + "\n", e);
        }
        
        return respuesta;
    }
    
    /**
     * Action method para activar plan de seguridad
     * @param msisdn
     * @param mailUsuario
     * @param codigoPlan
     * @param valorPlan
     * @return
     */
    public boolean desactivarPlanSeguridad(
    	String msisdn, String tipoMovil, String mailUsuario, String codigoPlan)
    		throws Exception{
    	
    	boolean respuesta = false;
        try{
        	respuesta = planesSeguridadBAMDelegate.desactivarPlanSeguridad(
        			msisdn, tipoMovil, mailUsuario, codigoPlan);
            
        } catch (DAOException e) {
            LOGGER.error("DAOException al desactivar plan de seguridad para el usuario: " + msisdn);
        } catch (ServiceException e) {
            LOGGER.info("ServiceException al desactivar plan de seguridad para el usuario: " + msisdn);
        } catch (Exception e) {
            LOGGER.error("Exception al desactivar plan de seguridad para el usuario: " + msisdn);
        }
        
        return respuesta;
    }
    
    /**
     * Action method para actualizar mail de usuario BAM
     * @param event
     */
    public void actualizarMailUsuario(PhaseEvent event){
    	
    	String msisdn = null;
    	String rut = null;
    	boolean respuesta = false;
    		
        try{
        	//Obtenemos datos necesarios para consulta
            ProfileWrapper profile = ProfileWrapperHelper.getProfile(JSFPortletHelper.getRequest());
            rut = ProfileWrapperHelper.getPropertyAsString(profile, "rutUsuarioSeleccionado");
            String email = JsfUtil.getRequestParameter("email");
            msisdn = ProfileWrapperHelper.getPropertyAsString(profile, "numeroPcs");
            
            String [] array_email = email.split("[@]");
            
        	respuesta = cuentaDelegate.actualizarCorreoBuzon(msisdn, rut, array_email[0], array_email[1]);
        	
        	if(respuesta){
        		respuestaActualizarEmailJson = JsonHelper.toJsonResponse(null,"email","gestionDePerfiles");
        	}else{
        		LOGGER.error(new Exception());
        	}
        	
        	
        }catch (DAOException e) {
            LOGGER.error("DAOException al actualizar mail del usuario: " + msisdn + "\n", e);
            respuestaActualizarEmailJson = JsonHelper.toJsonServiceErrorMessage("actualizarMail");
        } catch (ServiceException e) {
            LOGGER.info("ServiceException al actualizar mail del usuario [Movil: " + msisdn 
            		+ "][Rut: " + rut +"]" + "\n" + e.getDescripcionRespuesta());
            JSFMessagesHelper.addErrorCodeMessage("gestionDePerfiles",e.getCodigoRespuesta());
            respuestaActualizarEmailJson = JsonHelper.toJsonServiceErrorMessage("actualizarMail");
        } catch (Exception e) {
            LOGGER.error("Exception al actualizar mail del usuario: " + msisdn + "\n", e);
            respuestaActualizarEmailJson = JsonHelper.toJsonServiceErrorMessage("actualizarMail");
        }
    	
    }
    
    /**
     * Action method para confirmar la compra de un plan de seguridad. Dependiendo el mercado del usuario,
     * se redirige el flujo a los metodos confirmarCompraPlanSS o confirmarCompraPlanCC
     * @param event evento disparado al confirmar la compra de un plan de seguridad.
     */
    public void confirmarCompraPlanSeguridad(PhaseEvent event){
    	boolean respuesta = false;
        boolean puedeActivar = true;
        this.cambioPlan = false;
        
        try{
            //Obtenemos datos necesarios para consulta
            ProfileWrapper profile = ProfileWrapperHelper.getProfile(JSFPortletHelper.getRequest());
            String msisdn = ProfileWrapperHelper.getPropertyAsString(profile, "numeroPcsSeleccionado");
            String mercado = ProfileWrapperHelper.getPropertyAsString(profile, "mercado");
            String codigoPlan = JsfUtil.getRequestParameter("codigoPlan");
            String valorPlan = JsfUtil.getRequestParameter("valorPlan");
            String email = JsfUtil.getRequestParameter("email");
            String planACambiar = JsfUtil.getRequestParameter("planActual");
            String operacion = JsfUtil.getRequestParameter("operacion");
            String tipoMovil = mercado.equalsIgnoreCase("SS") 
            		? MiEntelProperties.getProperty("parametros.mcafee.tipoMovil.suscripcion") 
            		: mercado.equalsIgnoreCase("PP") ? MiEntelProperties.getProperty("parametros.mcafee.tipoMovil.prepago")
            		: MiEntelProperties.getProperty("parametros.mcafee.tipoMovil.cuentacontrolada");
            
			if (operacion.equalsIgnoreCase(MiEntelProperties.getProperty("planSeguridadBAM.cambioPlan"))) {
            	consultarPlanActualSeguridad(msisdn);
            	if(planActual != null){
            		if(planActual.getEstadoPlan() == null || planActual.getEstadoPlan().equalsIgnoreCase("INACTIVO")){
            			respuestaComprarPlanJson = JsonHelper.toJsonServiceErrorMessage("noDisponible");
            			puedeActivar = false;
            		}else{
            			this.cambioPlan = true;
            			respuesta = desactivarPlanSeguridad(
        						msisdn, tipoMovil, email, planACambiar);
            			if(!respuesta){
            				respuestaComprarPlanJson = JsonHelper.toJsonServiceErrorMessage("noDisponible");
            				puedeActivar = false;
            			}
            		}
            	}else{
            		respuestaComprarPlanJson = JsonHelper.toJsonServiceErrorMessage("noDisponible");
            		puedeActivar = false;
    			}
            }
            
            //Invocamos el metodo de accion que valida el plan a contratar
            respuesta = validarPlanSeguridad(
                		msisdn, email, codigoPlan, valorPlan);
            
	        if(!respuesta){
	            respuestaComprarPlanJson = JsonHelper.toJsonServiceErrorMessage("noDisponible");
	            puedeActivar = false;
	        }if(puedeActivar){
	        	respuesta = activarPlanSeguridad(
	        			msisdn, tipoMovil, email, codigoPlan);
	        	
	        	if(!respuesta){
	        		respuestaComprarPlanJson = JsonHelper.toJsonServiceErrorMessage("validarPlan");
	        	}else{
	        		//Pausamos el flujo para que a la cola de plan actual pueda identificar el nuevo plan
	                try {
	                    Thread.sleep(6000);
	                } catch (Exception e) {
	                }
	                //Consultamos la informacion del nuevo plan activado
	                consultarPlanActualSeguridad(msisdn);
	                this.urlDescarga = "";
	                if(planActual != null){
	                	if(planActual.getEstadoPlan() == null || planActual.getEstadoPlan().equalsIgnoreCase("INACTIVO")){
	                		respuestaComprarPlanJson = JsonHelper.toJsonServiceErrorMessage("noDisponible");
	                	}else{
	                		/*
		                	 * Actualizamos la UrlDescarga para que el usuario pueda obtener el producto
		                	 * contratado y activarlo ingresando el numero de licencia que se ha 
		                	 * enviado a tu casilla de email.
		                	 */
	                		consultarCredencialesPlanSeguridad(msisdn);
	                    	//Id del contenido con la URL para descargar el software de McAfee comprado por el usuario
	                    	String idContenido = MiEntelProperties.getProperty("parametros.planesSeguridadBAM.urlDescarga.idContenido");
	                    	Node contenido = JSFPortletHelper.getContentNode("idContenido", idContenido);
	                    	this.urlDescarga = contenido.getProperty("html").getValue().getStringValue();
	                    	this.planActual.setUrlDescarga(this.getUrlDescarga());
	                    	
	                		try{
	                			String fechaActFormatted = DateHelper.format(
	                					planActual.getFechaActivacionPlan(), "d 'de' MMMM 'de' yyyy");
	                			String fechaVenFormatted = DateHelper.format(
	                					planActual.getFechaVencimientoPlan(), "d 'de' MMMM 'de' yyyy");
		                		planActual.setFechaActivacionPlanFormatted(fechaActFormatted);
		                		planActual.setFechaVencimientoPlanFormatted(fechaVenFormatted);
	                		}catch(Exception e){
	                			/*
	                			 * En caso que las fechas devueltas por el servicio tengan un 
	                			 * formato invalido, se setean fechas por defecto.
	                			 */
	                			LOGGER.info("Fechas de activacion y vencimiento de plan invalidas", e);
	                			Date fecha = new Date();
	                			String fechaActFormatted = DateHelper.format(
	                					fecha, "d 'de' MMMM 'de' yyyy");
	                			/*
	                			 * El primer cargo por uso del servicio se 
	                			 * realiza despues de los primeros 30 dias
	                			 */
	                			String fechaVenFormatted = DateHelper.format(
	                					DateHelper.addDays(fecha, 29), "d 'de' MMMM 'de' yyyy");
		                		planActual.setFechaActivacionPlanFormatted(fechaActFormatted);
		                		planActual.setFechaVencimientoPlanFormatted(fechaVenFormatted);
	                			
	                		}
	                		
	                        //Carga de datos para el marcado GTM
	                        TransaccionGTMBean transGTM = new TransaccionGTMBean();
							transGTM.setIdTransaccion(msisdn.substring(msisdn.length() - 4));
	                        transGTM.setSkuID(codigoPlan);
	                        transGTM.setNombre(planActual.getNombrePlan());
	                        transGTM.setNuevoValor(valorPlan);
	                        transGTM.setCostoOperacional(0);
	                        transGTM.setNumeroPlanes(1);
	                        transGTM.setNumeroOperaciones(1);
	                        transGTM.setValorTransaccion(Double.parseDouble(valorPlan) + transGTM.getCostoOperacional());
		                	planActual.setTransaccionGTM(transGTM);
		                	
	                        respuestaComprarPlanJson = JsonHelper.toJsonResponse(planActual);
	                	}
	                }else{
	                	respuestaComprarPlanJson = JsonHelper.toJsonServiceErrorMessage("noDisponible");
	                }
	        	}
	        }
			
        }catch(Exception e){
        	LOGGER.error("Error durante la compra del plan de seguridad BAM", e);
        	if(Utils.isEmptyString(respuestaComprarPlanJson))
        		respuestaComprarPlanJson = JsonHelper.toJsonServiceErrorMessage("falloCompra");
        }
            
    }
    
    /**
     * Metodo utilitario que resuelve el texto que lleva el boton del plan
     * @param plan
     */
    private void setTextoBoton(PlanSeguridadBAMBean plan){
    	if(plan.getValorPlan().equalsIgnoreCase("0")){
			plan.setTextoBoton(MiEntelProperties
				.getProperty("planSeguridadBAM.compra.packPromo"));
		}else if(planActual != null && !planActual.getEstadoPlan()
					.equalsIgnoreCase("INACTIVO")) {
            plan.setTextoBoton(MiEntelProperties
                .getProperty("planSeguridadBAM.cambioPlan"));
        }else{
            plan.setTextoBoton(MiEntelProperties
                .getProperty("planSeguridadBAM.compra.packNormal.sscc"));
        }
    }
	
	
	/**
	 * Metodo utilitario que retorna un valor booleano. Valida si en la url se
	 * encuentra el parametro indica si se esta accediendo a los planes de seguridad
	 * desde el banner de seguridad.
	 * 
	 * @return true o false
	 */
	public boolean isParametroUrlBanner() {
		boolean resp = false;
		
		try {
			resp = (JSFPortletHelper.getRequest().getParameter(
					CLICK_BANNER_ATTTR_NAME) != null) ? true : false;
		} catch (Exception e) {
			LOGGER.error("Exception al intentar obtener el parametro banner promo de seguridad",e);
		}

		return resp;
	}
	
	/**
	 * Metodo utilitario que retorna un valor booleano. Valida si en la url se
	 * encuentra el parametro indica el codigo del plan promo a mostrar.
	 * 
	 * @return true o false
	 */
	public boolean isParametroIdPlanPromo() {
		boolean resp = false;
		
		try {
			resp = (JSFPortletHelper.getRequest().getParameter(
					PLAN_PROMO_ATTTR_NAME) != null) ? true : false;
		} catch (Exception e) {
			LOGGER.error("Exception al intentar obtener el parametro planPromo de seguridad",e);
		}

		return resp;
	}
	
	/**
	 * Metodo utilitario que retorna el codigo del plan promo a mostrar.
	 * 
	 * @return true o false
	 */
	public String getValorParametroPlanPromo() {
		String resp = "";
		
		try {
			resp = JSFPortletHelper.getRequest().getParameter(PLAN_PROMO_ATTTR_NAME);
		} catch (Exception e) {
			LOGGER.error("Exception al intentar obtener el parametro planPromo de seguridad",e);
		}

		return resp;
	}
	
}
