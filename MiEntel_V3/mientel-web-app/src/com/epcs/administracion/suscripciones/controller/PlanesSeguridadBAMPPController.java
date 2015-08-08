/* Propiedad de Entel PCS. Todos los derechos reservados */
package com.epcs.administracion.suscripciones.controller;

import java.text.NumberFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.faces.event.PhaseEvent;

import org.apache.log4j.Logger;

import com.bea.content.Node;
import com.bea.p13n.usermgmt.profile.ProfileWrapper;
import com.epcs.administracion.suscripciones.delegate.PlanesSeguridadBAMDelegate;
import com.epcs.bean.CredencialesPlanSeguridadBean;
import com.epcs.bean.PlanContratadoBean;
import com.epcs.bean.PlanSeguridadBAMBean;
import com.epcs.bean.TransaccionGTMBean;
import com.epcs.bean.UsuarioBean;
import com.epcs.cliente.perfil.delegate.CuentaDelegate;
import com.epcs.recursoti.configuracion.DateHelper;
import com.epcs.recursoti.configuracion.JsonHelper;
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
public class PlanesSeguridadBAMPPController {
	
	private static final Logger LOGGER = Logger.getLogger(PlanesSeguridadBAMPPController.class);
	private static final String CODIGO_RESPUESTA_NO_INFO = MiEntelProperties
			.getProperty("servicios.codigoRespuesta.noInfo");
	
	private static final String PAGE_LABEL_PLANES_SEGURIDAD = MiEntelProperties
			.getProperty("parametros.planesSeguridadBAM.pageLabel.pp");
	private PlanesSeguridadBAMDelegate planesSeguridadBAMDelegate;
	private CuentaDelegate cuentaDelegate;
	private PlanSeguridadBAMBean planActual;
	private List<PlanSeguridadBAMBean> planesDisponiblesPP;
	private String mailUsuario;
	private String respuestaComprarPlanJson;
	private String respuestaActualizarEmailJson;
	private String panelGroup;
	private String urlDescarga;
	private String pageLabelPlanes;
	private int saldoUsuario;
	private Date vencimientoSaldo;
	private boolean hayPlanesDisponibles;
	private boolean tienePlanActual;
	private boolean planActualActivo;
	
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
	 * @return the planesDisponiblesPP
	 */
	public List<PlanSeguridadBAMBean> getPlanesDisponiblesPP() {
		return planesDisponiblesPP;
	}
	/**
	 * @param planesDisponiblesPP the planesDisponiblesPP to set
	 */
	public void setPlanesDisponiblesPP(
			List<PlanSeguridadBAMBean> planesDisponiblesPP) {
		this.planesDisponiblesPP = planesDisponiblesPP;
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
	 * @return the panelGroup
	 */
	public String getPanelGroup() {
		return panelGroup;
	}
	/**
	 * @param panelGroup the panelGroup to set
	 */
	public void setPanelGroup(String panelGroup) {
		this.panelGroup = panelGroup;
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
	 * @return the saldoUsuario
	 */
	public int getSaldoUsuario() {
		return saldoUsuario;
	}
	/**
	 * @param saldoUsuario the saldoUsuario to set
	 */
	public void setSaldoUsuario(int saldoUsuario) {
		this.saldoUsuario = saldoUsuario;
	}
	/**
	 * @return the vencimientoSaldo
	 */
	public Date getVencimientoSaldo() {
		return vencimientoSaldo;
	}
	/**
	 * @param vencimientoSaldo the vencimientoSaldo to set
	 */
	public void setVencimientoSaldo(Date vencimientoSaldo) {
		this.vencimientoSaldo = vencimientoSaldo;
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
     * Invoca los action methods que obtienen la informacion de planes de seguridad .
     * @param phase
     */
    public void initCargarPlanesSeguridadBAM(PhaseEvent phase) {
        try {
            LOGGER.info("phase " + phase.getPhaseId());
            //Obtenemos datos necesarios para consulta
            ProfileWrapper profile = ProfileWrapperHelper.getProfile(JSFPortletHelper.getRequest());
            String msisdn = ProfileWrapperHelper.getPropertyAsString(profile, "numeroPcs");
            String rut = ProfileWrapperHelper.getPropertyAsString(profile, "rutUsuarioSeleccionado");
            
            //Consultamos plan actual de seguridad
            this.consultarPlanActualSeguridad(msisdn);
            this.urlDescarga = "";
            
            //Si tiene plan actual activo, consultamos las credenciales del plan
            if(planActualActivo){
            	consultarCredencialesPlanSeguridad(msisdn);
            	String idContenido = MiEntelProperties.getProperty("parametros.planesSeguridadBAM.urlDescarga.idContenido");
            	Node contenido = JSFPortletHelper.getContentNode("idContenido", idContenido);
            	this.urlDescarga = contenido.getProperty("html").getValue().getStringValue();
            }
            
            mailUsuario = obtenerMailUsuario(msisdn, rut);
            
            //Consultamos plan actual los planes disponibles
            this.obtenerPlanesSeguridadDisponiblesPP();
            if(hayPlanesDisponibles){
            	for (PlanSeguridadBAMBean plan : planesDisponiblesPP) {
                    if (plan.getValorPlan().equalsIgnoreCase("0")) {
                        plan.setTextoBoton(MiEntelProperties
                            .getProperty("planSeguridadBAM.compra.packPromo"));
                    } else {
                        plan.setTextoBoton(MiEntelProperties
                            .getProperty("planSeguridadBAM.compra.packNormal.pp"));
                    }
                }
            }
            
            verificarSaldoSuficiente();
            
            pageLabelPlanes = JSFPortletHelper.getPreference(
					JSFPortletHelper.getPreferencesObject(), PAGE_LABEL_PLANES_SEGURIDAD, null);

        } catch (Exception e) {
            LOGGER.error("Error inesperado al cargar informacion de planes de seguridad", e);
            JSFMessagesHelper.addServiceErrorMessage("initCargarPlanesSeguridadBAM");
        }

    }
    
    /**
     * Action method para obtener el plan actual de seguridad de un usuario PP
     * @param msisdn
     */
    public void consultarPlanActualSeguridad(String msisdn){
        try{
            //Invocamos el metodo de accion que consulta el plan actual de seguridad
            planActual = this.planesSeguridadBAMDelegate.consultarPlanActualSeguridad(msisdn);
            tienePlanActual = planActual != null && !planActual.getEstadoPlan().equalsIgnoreCase("D");
            planActualActivo = planActual != null && planActual.getEstadoPlan().equalsIgnoreCase("ACTIVO");
            
        } catch (DAOException e) {
            LOGGER.error("DAOException al consultar plan actual de seguridad para el movil: " + msisdn, e);
        } catch (ServiceException e) {
        	if(e.getCodigoRespuesta().equals(CODIGO_RESPUESTA_NO_INFO)){
        		LOGGER.info("El movil" + msisdn + " no tiene plan actual de seguridad BAM. ");
        	}else{
        		LOGGER.info("ServiceException al consultar plan actual de seguridad para el movil: " + msisdn + "\n" + e.getDescripcionRespuesta());
        	}
        } catch (Exception e) {
            LOGGER.error("Exception al consultar plan actual de seguridad para el movil: " + msisdn, e);
        }
        
    }
    
    /**
     * Action method para obtener los planes de seguridad disponibles para PP
     */
    public void obtenerPlanesSeguridadDisponiblesPP(){
        try{
            //Invocamos el metodo de accion que obtiene los planes de seguiridad disponibles para PP
            planesDisponiblesPP = this.planesSeguridadBAMDelegate.obtenerPlanesSeguridadDispobiblesPP();
            hayPlanesDisponibles = planesDisponiblesPP != null;
            
        } catch (DAOException e) {
            LOGGER.error("DAOException consultando planes de seguridad disponibles para PP: ", e);
            JSFMessagesHelper.addServiceErrorMessage("obtenerPlanesSeguridadDisponiblesPP");
        } catch (ServiceException e) {
            LOGGER.info("ServiceException consultando planes de seguridad disponibles para PP.");
            JSFMessagesHelper.addErrorCodeMessage("planesDeSeguridad.mcAfee",e.getCodigoRespuesta());
        } catch (Exception e) {
            LOGGER.error("Exception consultando planes de seguridad disponibles para PP: ", e);
            JSFMessagesHelper.addServiceErrorMessage("obtenerPlanesSeguridadDisponiblesPP");
        }
        
    }
    
    /**
     * Action method para consultar las credenciales de un plan de seguridad
     * @param msisdn
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
     * @param msisdn
     * @param rut
     * @return
     */
    public String obtenerMailUsuario(String msisdn, String rut){
    	
    	UsuarioBean usuario = null;
    	
        try{
            //Obtenemos los datos del usuario para luego retornar el mail
        	usuario = this.cuentaDelegate.obtenerUsuario(msisdn, rut);
            
        } catch (DAOException e) {
            LOGGER.error("DAOException al consultar mail del usuario: " + msisdn, e);
            JSFMessagesHelper.addServiceErrorMessage("obtenerMailUsuario");
        } catch (ServiceException e) {
            LOGGER.info("ServiceException al consultar mail del usuario: "+ msisdn);
            JSFMessagesHelper.addErrorCodeMessage("gestionDePerfiles",e.getCodigoRespuesta());
        } catch (Exception e) {
            LOGGER.error("Exception al consultar mail del usuario: " + msisdn, e);
            JSFMessagesHelper.addServiceErrorMessage("obtenerMailUsuario");
        }
        
        return usuario.getEmail();
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
            LOGGER.error("DAOException al validar plan de seguridad para el usuario: " + msisdn + "\n", e);
            JSFMessagesHelper.addServiceErrorMessage("validarPlan");
            LOGGER.error( new Exception(e.getMessage()));
        } catch (ServiceException e) {
            LOGGER.info("ServiceException al validar plan de seguridad para el usuario: " + msisdn + "\n" + e.getDescripcionRespuesta());
            JSFMessagesHelper.addErrorCodeMessage("planesDeSeguridad.mcAfee",e.getCodigoRespuesta());
             LOGGER.error( new Exception(e.getMessage()));
        } catch (Exception e) {
            LOGGER.error("Exception al validar plan de seguridad para el usuario: " + msisdn + "\n", e);
            JSFMessagesHelper.addServiceErrorMessage("validarPlan");
             LOGGER.error( new Exception(e.getMessage()));
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
            LOGGER.error("DAOException al activar plan de seguridad para el usuario: " + msisdn + "\n", e);
            JSFMessagesHelper.addServiceErrorMessage("activarPlanPPCC");
           LOGGER.error( new Exception(e.getMessage()));
        } catch (ServiceException e) {
            LOGGER.info("ServiceException al activar plan de seguridad para el usuario: " + msisdn + "\n" + e.getDescripcionRespuesta());
            JSFMessagesHelper.addErrorCodeMessage("planesDeSeguridad.mcAfee",e.getCodigoRespuesta());
            LOGGER.error( new Exception(e.getMessage()));
        } catch (Exception e) {
            LOGGER.error("Exception al activar plan de seguridad para el usuario: " + msisdn + "\n", e);
            JSFMessagesHelper.addServiceErrorMessage("activarPlanPPCC");
            LOGGER.error( new Exception(e.getMessage()));
        }
        
        return respuesta;
    }
    
    /**
     * Action method para consultar saldo y vegencia de saldo
     */
    private void verificarSaldoSuficiente(){
    	
    	PlanContratadoBean plan = null;
    	String msisdn = null;
    	
        try{
        	//Obtenemos datos necesarios para consulta
            ProfileWrapper profile = ProfileWrapperHelper.getProfile(JSFPortletHelper.getRequest());
            msisdn = ProfileWrapperHelper.getPropertyAsString(profile, "numeroPcs");
            
            /* Recuperamos el saldo del usuario */
        	plan = this.cuentaDelegate.consultarPlanContratadoPP(msisdn);
        	saldoUsuario = Integer.parseInt(plan.getSaldo());
        	vencimientoSaldo = plan.getFechaExpiracion();
        	
        	for(PlanSeguridadBAMBean planSeguridad : planesDisponiblesPP){
        		planSeguridad.setSaldoSuficiente(
        			saldoUsuario > Integer.parseInt(planSeguridad.getValorPlan()));
        	}
            
        } catch (DAOException e) {
            LOGGER.error("DAOException al consultar saldo y vigencia de saldo para el usuario: " + msisdn, e);
            JSFMessagesHelper.addServiceErrorMessage("consultarSaldoYVigenciaPP");
        } catch (ServiceException e) {
            LOGGER.info("ServiceException al consultar saldo y vigencia de saldo para el usuario: " 
            		+ msisdn + "\n" + e.getDescripcionRespuesta());
            JSFMessagesHelper.addServiceErrorMessage(e.getCodigoRespuesta());
        } catch (Exception e) {
            LOGGER.error("Exception al consultar saldo y vigencia de saldo para el usuario: " + msisdn, e);
            JSFMessagesHelper.addServiceErrorMessage("consultarSaldoYVigenciaPP");
        }
        
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
        		LOGGER.error( new Exception());
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
     * Action method para confirmar la compra de un plan de seguridad 
     * @param event evento disparado al confirmar la compra de un plan de seguridad
     */
    public void confirmarCompraPlanSeguridad(PhaseEvent event){
        boolean validarPlan = false;
        boolean activarPlan = false;
        
        try{
            //Obtenemos datos necesarios para consulta
            ProfileWrapper profile = ProfileWrapperHelper.getProfile(JSFPortletHelper.getRequest());
            String msisdn = ProfileWrapperHelper.getPropertyAsString(profile, "numeroPcsSeleccionado");
            String mercado = ProfileWrapperHelper.getPropertyAsString(profile, "mercado");
            String codigoPlan = JsfUtil.getRequestParameter("codigoPlan");
            String valorPlan = JsfUtil.getRequestParameter("valorPlan");
            String email = JsfUtil.getRequestParameter("email");
            String tipoMovil = mercado.equalsIgnoreCase("SS") 
		    		? MiEntelProperties.getProperty("parametros.mcafee.tipoMovil.suscripcion") 
		    		: mercado.equalsIgnoreCase("PP") ? MiEntelProperties.getProperty("parametros.mcafee.tipoMovil.prepago")
		    		: MiEntelProperties.getProperty("parametros.mcafee.tipoMovil.cuentacontrolada");
            
            //Invocamos el metodo de accion que realiza el canje de puntos
            validarPlan = validarPlanSeguridad(
            		msisdn, email, codigoPlan, valorPlan);
	        
	        //Armamos respuesta de la recarga en formato JSON
	        if(!validarPlan){
	            respuestaComprarPlanJson = JsonHelper.toJsonServiceErrorMessage("validarPlan");
	        }else{
	        	activarPlan = activarPlanSeguridad(
	        			msisdn, tipoMovil, email, codigoPlan);
	        	
	        	if(!activarPlan){
	        		respuestaComprarPlanJson = JsonHelper.toJsonServiceErrorMessage("noDisponible");
	        	}else{
	        		//Pausamos el flujo para que a la cola de plan actual pueda identificar el nuevo plan
	                try {
	                    Thread.sleep(6000);
	                } catch (Exception e) {
	                }
	                //Consultamos la informacion del nuevo plan activado
	                consultarPlanActualSeguridad(msisdn);
	                if(planActual != null){
	                	if(planActual == null || !planActual.getEstadoPlan().equalsIgnoreCase("ACTIVO")){
		                	respuestaComprarPlanJson = 
		                		JsonHelper.toJsonServiceErrorMessage("noDisponible");
		                }else{
		                	/*
		                	 * Actualizamos la UrlDescarga para que el usuario pueda obtener el producto
		                	 * contratado o pueda volver a activarlo ingresando el nuevo numero de licencia
		                	 * que se ha enviado a tu casilla de email.
		                	 */
		                	consultarCredencialesPlanSeguridad(msisdn);
	                    	//Id del contenido con la URL para descargar el software de McAfee comprado por el usuario
	                    	String idContenido = MiEntelProperties.getProperty("parametros.planesSeguridadBAM.urlDescarga.idContenido");
	                    	Node contenido = JSFPortletHelper.getContentNode("idContenido", idContenido);
	                    	this.urlDescarga = contenido.getProperty("html").getValue().getStringValue();
	                    	this.planActual.setUrlDescarga(this.getUrlDescarga());
		                	verificarSaldoSuficiente();
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
	                			String fechaVenFormatted = DateHelper.format(
	                					DateHelper.addDays(fecha, 29), "d 'de' MMMM 'de' yyyy");
		                		planActual.setFechaActivacionPlanFormatted(fechaActFormatted);
		                		planActual.setFechaVencimientoPlanFormatted(fechaVenFormatted);
	                			
	                		}
	                		try{
	                			/*
	                			 * Damos formato numerico (con separador de miles) al valor 
	                			 * del nuevo saldo y lo devolvemos en el objeto de respuesta,
	                			 * para ser mostrado al usuario en la vista de exito.
	                			 */
	                			String language = MiEntelProperties.getProperty("locale.language");
	                			NumberFormat nf = NumberFormat.getNumberInstance(new Locale(language));
	                			String saldoFormatted = nf.format(saldoUsuario); 
	                			/*
	                			 * Usamos el campo detallePlan, para almacenar el valor
	                			 * del nuevo saldo de usuario, despues de la activacion 
	                			 * del plan de seguridad.
	                			 */
	                			planActual.setDetallePlan(saldoFormatted);
	                		}catch(Exception e){
	                			planActual.setDetallePlan(String.valueOf(saldoUsuario));
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
            
        } catch(Exception e){
            LOGGER.error("Exception durante la compra del plan de seguridad BAM", e);
            if(Utils.isEmptyString(respuestaComprarPlanJson))
            	respuestaComprarPlanJson = JsonHelper.toJsonServiceErrorMessage("falloCompra");
        }
        
    }
	
}
