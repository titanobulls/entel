/* Propiedad de Entel PCS. Todos los derechos reservados */
package com.epcs.cliente.perfil.controller;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;

import org.apache.log4j.Logger;

import com.bea.content.Node;
import com.bea.p13n.usermgmt.profile.ProfileWrapper;
import com.epcs.bean.BloqueoEquipoBean;
import com.epcs.bean.ContactoPorRenovacionBean;
import com.epcs.bean.DatosUsuarioBloqueoBean;
import com.epcs.bean.DocumentoAperturaOTBean;
import com.epcs.bean.GrupoClienteBean;
import com.epcs.bean.InformeTecnicoOTBean;
import com.epcs.bean.OrdenTrabajoVigenteBean;
import com.epcs.bean.PinPukBean;
import com.epcs.bean.ResumenEquipoBean;
import com.epcs.bean.ResumenLineaEquipoBean;
import com.epcs.bean.RutBean;
import com.epcs.bean.SituacionActualEquipoBean;
import com.epcs.cliente.perfil.delegate.EquipoDelegate;
import com.epcs.erp.seguridad.delegate.SeguridadDelegate;
import com.epcs.recursoti.configuracion.DateHelper;
import com.epcs.recursoti.configuracion.JsonHelper;
import com.epcs.recursoti.configuracion.MiEntelBusinessHelper;
import com.epcs.recursoti.configuracion.MiEntelProperties;
import com.epcs.recursoti.configuracion.PDFCreatorHelper;
import com.epcs.recursoti.configuracion.Utils;
import com.epcs.recursoti.configuracion.jsf.utils.JSFMessagesHelper;
import com.epcs.recursoti.configuracion.jsf.utils.JSFPortletHelper;
import com.epcs.recursoti.configuracion.jsf.utils.JsfUtil;
import com.epcs.recursoti.configuracion.uup.ProfileWrapperHelper;
import com.epcs.recursoti.excepcion.DAOException;
import com.epcs.recursoti.excepcion.ServiceException;

/**
 * @author jmanzur (I2B) en nombre de Absalon Opazo (Atencion al Cliente,
 *         EntelPcs)
 * 
 */
public class EquipoBAMController implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final Logger LOGGER = Logger
            .getLogger(EquipoBAMController.class);

    private EquipoDelegate equipoDelegate;

    private ResumenEquipoBean resumenEquipoBean;
    
    private String numeroPcs = "";
    
    private SituacionActualEquipoBean situacionActualEquipo;
    
    private ContactoPorRenovacionBean contactoPorRenovacion;
    
    private PinPukBean pinPuk;
    
    private DatosUsuarioBloqueoBean datosUsuarioBloqueo;
    
    private OrdenTrabajoVigenteBean ordenesTrabajoVigentes;        
    
    private int ordenesLength;
    
    private String respuestaJson;
    
    private String imei;
    
    private String fechaActual;
    
    private String atributoAceptacionPrespuesto = MiEntelProperties.getProperty("equipo.presupuesto.accion.aceptar");
    
    private String atributoRechazoPrespuesto = MiEntelProperties.getProperty("equipo.presupuesto.accion.rechazar");
    
    private InformeTecnicoOTBean informeTecnicoOT;
    
    private DocumentoAperturaOTBean documentoAperturaOT;
    
    private boolean bloqueoExistente;
    
    private boolean enGrupoHabil;
    
    private String mensajeEquipoBeta;
    
    private SeguridadDelegate seguridadDelegate;
    
    private ResumenLineaEquipoBean resumenLineaEquipoBean;
    
    
    /**
	 * @return the seguridadDelegate
	 */
	public SeguridadDelegate getSeguridadDelegate() {
		return seguridadDelegate;
	}

	/**
	 * @param seguridadDelegate the seguridadDelegate to set
	 */
	public void setSeguridadDelegate(SeguridadDelegate seguridadDelegate) {
		this.seguridadDelegate = seguridadDelegate;
	}

	/**
	 * @return the resumenLineaEquipoBean
	 */
	public ResumenLineaEquipoBean getResumenLineaEquipoBean() {
		return resumenLineaEquipoBean;
	}

	/**
	 * @param resumenLineaEquipoBean the resumenLineaEquipoBean to set
	 */
	public void setResumenLineaEquipoBean(
			ResumenLineaEquipoBean resumenLineaEquipoBean) {
		this.resumenLineaEquipoBean = resumenLineaEquipoBean;
	}

	/**
     * @return the mensajeEquipoBeta
     */
    public String getMensajeEquipoBeta() {
        String mensaje = "";
        try {
            String contexto = MiEntelProperties
                    .getProperty("contexto.v2.bloqueo");
            if (mensajeEquipoBeta != null) {
                mensaje = mensajeEquipoBeta.replace("{url}", this.getURLMiEntelV2()+ contexto);
            }
        } catch (Exception e) {
            LOGGER.error("No se pudo obtener mensajeEquipoBeta", e);
        }
        return mensaje;
    }

	/**
	 * @param mensajeEquipoBeta the mensajeEquipoBeta to set
	 */
	public void setMensajeEquipoBeta(String mensajeEquipoBeta) {
		this.mensajeEquipoBeta = mensajeEquipoBeta;
	}

	/**
     * @return the equipoDelegate
     */
    public EquipoDelegate getEquipoDelegate() {
        return equipoDelegate;
    }

    /**
     * @param equipoDelegate
     *            the equipoDelegate to set
     */
    public void setEquipoDelegate(EquipoDelegate equipoDelegate) {
        this.equipoDelegate = equipoDelegate;
    }

    /**
     * Aceptar - rechazar presupuesto.
     * 
     * @param event
     */
    public void aceptarRechazarPresupuestoAjax(PhaseEvent event) {
    	try{    		
	    	String nroOrden = JsfUtil.getRequestParameter("nroOrden");
	    	String accion = JsfUtil.getRequestParameter("accion");    		
	    	equipoDelegate.aceptarRechazarPresupuesto(nroOrden, accion);
	    	respuestaJson = JsonHelper.toJsonResponse("Ok");	    		    	
    	} catch (DAOException e) {    		
            LOGGER.error("Error al aceptar/rechazar presupuesto", e);
            respuestaJson = JsonHelper.toJsonServiceErrorMessage("equipo");
        } catch (ServiceException e) {
            LOGGER.info("Error al aceptar/rechazar presupuesto");
            respuestaJson = JsonHelper.toJsonServiceErrorMessage("equipo", e.getCodigoRespuesta());
        } catch (Exception e) {
            LOGGER.error("Error al aceptar/rechazar presupuesto", e);
            respuestaJson = JsonHelper.toJsonServiceErrorMessage("equipo");
        }
    }
    
    /**
     * Bloquear Equipo
     * 
     * @param event
     */
    public void bloquearEquipoAjax(PhaseEvent event) {
    	String numeroPcsSeleccionado = "";
    	try{    		
    		
    		ProfileWrapper profileWrapper = ProfileWrapperHelper
            .getProfile(JSFPortletHelper.getRequest());
    		
    		numeroPcsSeleccionado = ProfileWrapperHelper.getPropertyAsString(
            profileWrapper, "numeroPcsSeleccionado");
    		
    		String rutTitular = ProfileWrapperHelper.getPropertyAsString(profileWrapper, "rutTitular");
    		rutTitular = rutTitular == null ? ProfileWrapperHelper.getPropertyAsString(profileWrapper,"rutUsuarioSeleccionado") : rutTitular;
    		
    		RutBean rt = new RutBean(rutTitular);
            String rutSinDV = rutTitular != null ? rutTitular.substring(0, rutTitular.length()-1) : "";
            String DV = String.valueOf(rt.getDigito());
    		
	    	String fecha = JsfUtil.getRequestParameter("fecha");
	    	String hora = JsfUtil.getRequestParameter("hora");
	    	String clave = JsfUtil.getRequestParameter("clave");
	    	String mail = JsfUtil.getRequestParameter("mail");
	    	String tel = JsfUtil.getRequestParameter("tel");
	    	String bloqueo = JsfUtil.getRequestParameter("bloqueo");
	    	String nombre = JsfUtil.getRequestParameter("nombre");
	    	String sentido = JsfUtil.getRequestParameter("sentido");
	    	String area = JsfUtil.getRequestParameter("area");
	    	String telefono = JsfUtil.getRequestParameter("telefono");
	    	
	    	String fechaRobo = fecha != null ? fecha.replace("/", "").concat(hora).concat("0000") : ""; 
	    	
	    	BloqueoEquipoBean bloq = new BloqueoEquipoBean();
	    	
	    	bloq.setClaveBloqueo(clave);
		    bloq.setDominioMail(mail);
		    bloq.setUsuarioMail(mail);
		    
		    bloq.setDv(DV);
	    	bloq.setFechaRobo(fechaRobo);
	    	bloq.setIdSentidoBloqueo(sentido);
	    	
	    	bloq.setMotivo(bloqueo);
	    	bloq.setAreaTelefonoFijo(area);
	    	bloq.setDestinoBloqueo(telefono);	    	

	    	bloq.setNombreCompletoUsuario(nombre);
	    	bloq.setNumeroPCS(numeroPcsSeleccionado);
	    	bloq.setNumeroPCSMotivoBloqueo(numeroPcsSeleccionado);
	    	bloq.setRutSinDV(rutSinDV);
	    	bloq.setTelefonoFijo(tel);
	    	
	    	DatosUsuarioBloqueoBean buic = new DatosUsuarioBloqueoBean();
	    	buic.setAreaTelefono(area);
	    	buic.setEmail(mail);
	    	buic.setTelefono(telefono);
	    	
	    	equipoDelegate.actualizarDatosUsuarioBloqueo(buic, rutSinDV, DV, numeroPcsSeleccionado);	    	
	    	equipoDelegate.bloquearEquipoPorRobo(bloq);	    	
	    	
	    	respuestaJson = JsonHelper.toJsonResponse("Ok");
	    	
    	} catch (DAOException e) {    		
            LOGGER.error("DAOException al bloquear equipo", e);
            respuestaJson = JsonHelper.toJsonServiceErrorMessage("equipo");
        } catch (ServiceException e) {
            LOGGER.info("ServiceException al bloquear equipo. msisdn: "+numeroPcsSeleccionado);
            respuestaJson = JsonHelper.toJsonServiceErrorMessage("equipo", e.getCodigoRespuesta());
        } catch (Exception e) {
            LOGGER.error("Exception al bloquear equipo", e);
            respuestaJson = JsonHelper.toJsonServiceErrorMessage("equipo");
        }
    }
    
    /**
     * Obtener documento de apertur de la OT
     * 
     * @param event
     */
    public String obtenerAperturaDocumentoOT(){
    	
    	String postback = "success";
    	String nroOrden = "";
    	try{    		    		
    		
	    	nroOrden = JsfUtil.getRequestParameter("nroOrden");	    	
	    	this.documentoAperturaOT = equipoDelegate.obtenerAperturaDocumentoOT(nroOrden);
	    	
			Map<String, Object> propiedades = new HashMap<String, Object>();
			propiedades.put("documento", documentoAperturaOT);
			propiedades.put("hoy", DateHelper.format(new Date(), DateHelper.FORMAT_ddMMyyyy_HYPHEN));
	    	
			String templatePath = MiEntelProperties.getProperty("equipo.documentoApertura.plantilla.xml");			
	    	
            PDFCreatorHelper.createAndDownloadPDF(propiedades, templatePath,
                    "DocuentoAperturaOT.pdf", JSFPortletHelper.getResponse(),
                    JSFPortletHelper.getResponse().getOutputStream());

			FacesContext.getCurrentInstance().responseComplete();
			
    	} catch (DAOException e) {    		
            LOGGER.error("DAOException al obtener apertura de documento presupuesto", e);
            JSFMessagesHelper.addServiceErrorMessage("noinfoequipo");
        } catch (ServiceException e) {
            LOGGER.info("ServiceException al obtener apertura de documento. nroOrden: "+nroOrden);
            JSFMessagesHelper.addServiceErrorMessage("noinfoequipo");
        } catch (Exception e) {
            LOGGER.error("Exception al obtener apertura de documento", e);
            JSFMessagesHelper.addServiceErrorMessage("noinfoequipo");
        }
        
        return postback;
        
    }
    
    /**
     * Obtener informe tecnico
     * 
     * @param event
     */
    public void obtenerInformeTecnico(PhaseEvent event){
    	String nroOrden = "";
    	try{    		    		
	    	nroOrden = JsfUtil.getRequestParameter("nOrden");
	    	this.informeTecnicoOT = equipoDelegate.obtenerInformeTecnicoOT(nroOrden);
	    	fechaActual = DateHelper.format(new Date(), DateHelper.FORMAT_ddMMyyyy_HYPHEN);
    	} catch (DAOException e) {   
    		informeTecnicoOT = null;
            LOGGER.error("DAOException al obtener informe tecnico", e);
            respuestaJson = JsonHelper.toJsonServiceErrorMessage("equipo");
        } catch (ServiceException e) {
        	informeTecnicoOT = null;
            LOGGER.info("ServiceException al obtener informe tecnico. nroOrden: "+nroOrden);
            respuestaJson = JsonHelper.toJsonServiceErrorMessage("equipo", e.getCodigoRespuesta());
        } catch (Exception e) {
        	informeTecnicoOT = null;
            LOGGER.error("Exception al obtener informe tecnico", e);
            respuestaJson = JsonHelper.toJsonServiceErrorMessage("equipo");
        }
    }
    
    /**
     * Enviar contacto por renovacion.
     * 
     * @param event
     */
    public void contactoRenovacionAjax(PhaseEvent event) {
    	String numeroPcs = "";
    	try{
    	
	    	ProfileWrapper profileWrapper = ProfileWrapperHelper
	        .getProfile(JSFPortletHelper.getRequest());
	    	
	    	String nombre1 = JsfUtil.getRequestParameter("n1");
	    	String nombre2 = JsfUtil.getRequestParameter("n2");
	    	String apellido1 = JsfUtil.getRequestParameter("ap1");
	    	String apellido2 = JsfUtil.getRequestParameter("ap2");
	    	String tel = JsfUtil.getRequestParameter("tel");
	    	
	    	numeroPcs = ProfileWrapperHelper.getPropertyAsString(
	                    profileWrapper, "numeroPcs");
	    	
	    	contactoPorRenovacion = new ContactoPorRenovacionBean();	    	
	    	contactoPorRenovacion.setMsisdnContacto(numeroPcs);
	    	contactoPorRenovacion.setTelefonoAdicional(tel);
	    	contactoPorRenovacion.setNombreCompleto(nombre1+" "+nombre2+" "+apellido1+" "+apellido2);
	    	contactoPorRenovacion.setRut(ProfileWrapperHelper.getPropertyAsString(profileWrapper,"rutTitular"));
	    	contactoPorRenovacion.setNombreFormulario("");
	    	
	    	contactoPorRenovacion();
	    	
	    	respuestaJson = JsonHelper.toJsonResponse("Ok");
	    	
    	} catch (DAOException e) {    		
            LOGGER.error("DAOException al buscar informacion de equipo.", e);
            respuestaJson = JsonHelper.toJsonServiceErrorMessage("equipo");
        } catch (ServiceException e) {
            LOGGER.info("ServiceException de servicio al buscar informacion de equipo. msisdn: "+numeroPcs);
            respuestaJson = JsonHelper.toJsonServiceErrorMessage("equipo", e.getCodigoRespuesta());
        } catch (Exception e) {
            LOGGER.error("Exception al buscar informacion de equipo.", e);
            respuestaJson = JsonHelper.toJsonServiceErrorMessage("equipo");
        }
    	
    }
    
    /**
     * Inicializa los datos de Equipo (Renovacion - Bloqueo - Servicio Tecnico - Configuracion)
     * 
     * @param event
     */
    public void initEquipos(PhaseEvent event) {
    	String numeroPcsSeleccionado = "";
    	try {
    		
            ProfileWrapper profileWrapper = ProfileWrapperHelper
                    .getProfile(JSFPortletHelper.getRequest());
            numeroPcsSeleccionado = ProfileWrapperHelper.getPropertyAsString(
                    profileWrapper, "numeroPcsSeleccionado");
            
            String mercado = ProfileWrapperHelper.getPropertyAsString(profileWrapper, "mercado");

            //loadDataEquipo(numeroPcsSeleccionado);
            
            this.setNumeroPcs(numeroPcsSeleccionado);
            
            String rutTitular = ProfileWrapperHelper.getPropertyAsString(profileWrapper,"rutTitular");
            String rutUsuario = ProfileWrapperHelper.getPropertyAsString(profileWrapper,"rutUsuarioSeleccionado");
            String nroCuenta = ProfileWrapperHelper.getPropertyAsString(profileWrapper,"numeroCuenta");
            
            rutTitular = rutTitular == null ? ProfileWrapperHelper.getPropertyAsString(profileWrapper,"rutUsuarioSeleccionado") : rutTitular;
            
            RutBean rt = new RutBean(rutTitular);
            String rutSinDV = rutTitular != null ? rutTitular.substring(0, rutTitular.length()-1) : "";
            String DV = String.valueOf(rt.getDigito());
            
            try {
                enGrupoHabil = equipoDelegate.estaEnGrupoHabil(rutTitular);

                String idContenido = MiEntelProperties.getProperty("parametros.equipo.mensajeEquipoBeta.idContenido");
            	Node mensajeServicio = JSFPortletHelper.getContentNode("idContenido", idContenido);
            	mensajeEquipoBeta = mensajeServicio.getProperty("html").getValue().getStringValue();

            } catch (Exception e) {
                LOGGER.error("Error al verificar grupo habil.", e);
            }            
            
            //loadDataImei(numeroPcsSeleccionado);
            loadDataPinPuk(numeroPcsSeleccionado);
            loadDataOrdenesTrabajo(numeroPcsSeleccionado);
            loadDataSituacionActualEquipo(rutTitular, nroCuenta, rutSinDV, DV);
            //loadDataDatosUsuarioBloqueo(rutSinDV, DV);
            
            if(MiEntelBusinessHelper.isMercadoSuscripcion(mercado) || MiEntelBusinessHelper.isMercadoCuentaControlada(mercado)){
            	
            	RutBean rutBean = new RutBean(rutUsuario);
	            String rutUsuarioSinDV = rutUsuario != null ? rutUsuario.substring(0, rutUsuario.length()-1) : "";
	            String dvRutUsuario = String.valueOf(rutBean.getDigito());
	            
	            /* Formateamos rut para consultar servicio que obtiene el grupo del cliente. 
				 * Convertimos dvRutUsuario a mayusculas y concatenamos con rutUsuarioSinDV */
		        
				String rutFormated = rutUsuarioSinDV.concat(dvRutUsuario.toUpperCase());
	            
	            GrupoClienteBean grupoClienteBean = obtenerGrupoCliente(rutFormated, nroCuenta);
	            String grupoCliente = Utils.isEmptyString(grupoClienteBean.getCodigoGrupo()) ? "" : grupoClienteBean.getCodigoGrupo();
	            
	            loadEquiposDisponiblesACOC(rutUsuario, nroCuenta, grupoCliente);
            }
            
        }  catch (DAOException e) {
            LOGGER.error("DAOException al buscar informacion de equipo.", e);
            JSFMessagesHelper.addServiceErrorMessage("noinfoequipo");
        } catch (ServiceException e) {
            LOGGER.info("ServiceException de servicio al buscar informacion de equipo. msisdn: "+numeroPcsSeleccionado);
            JSFMessagesHelper.addErrorCodeMessage("gestionDePerfiles", e.getCodigoRespuesta(), new String[]{numeroPcsSeleccionado});
        } catch (Exception e) {
            LOGGER.error("Exception al buscar informacion de equipo.", e);
            JSFMessagesHelper.addServiceErrorMessage("noinfoequipo");
        }
    }
    
    /**
     * Inicializa los datos de Equipo
     * 
     * @param event
     */
    private void loadDataEquipo(String numeroPcsSeleccionado){
    	try{
    		ProfileWrapper profileWrapper = ProfileWrapperHelper
            .getProfile(JSFPortletHelper.getRequest());
    		String mercado = ProfileWrapperHelper.getPropertyAsString(profileWrapper, "mercado");
    		String msisdn = numeroPcsSeleccionado = ProfileWrapperHelper.getPropertyAsString(profileWrapper, "numeroPcsSeleccionado");
    		
    		this.resumenEquipoBean = this.equipoDelegate.getResumenEquipo(numeroPcsSeleccionado);
    		
    		int DVImei = loadImeiDV(msisdn);
    		
    		bloqueoExistente = equipoDelegate.tieneBloqueoExistente(numeroPcsSeleccionado,mercado,String.valueOf(DVImei));
    		
    		
        }   catch (DAOException e) {
            LOGGER.error("DAOException al buscar informacion de equipo.", e);
            JSFMessagesHelper.addServiceErrorMessage("noinfoequipo");
        } catch (ServiceException e) {
            LOGGER.info("ServiceException de servicio al buscar informacion de equipo. msisdn: "+numeroPcsSeleccionado);
            JSFMessagesHelper.addErrorCodeMessage("gestionDePerfiles", e.getCodigoRespuesta(), new String[]{numeroPcsSeleccionado});
        } catch (Exception e) {
            LOGGER.error("Exception al buscar informacion de equipo.", e);
            JSFMessagesHelper.addServiceErrorMessage("noinfoequipo");
        } 	
    }
    
    /**
     * Inicializa los datos de Imei Equipo
     * 
     * @param event
     */
    private void loadDataImei(String numeroPcsSeleccionado){
    	try{
        	imei = equipoDelegate.obtenerImei(numeroPcsSeleccionado);
        } catch (DAOException e) {  	
            LOGGER.error("DAOException al buscar informacion de Pin Puk equipo.", e);
            JSFMessagesHelper.addServiceErrorMessage("noinfoequipo");
        } catch (ServiceException e) {
            LOGGER.info("DAOException al buscar informacion de Pin Puk equipo. msisdn: "+numeroPcsSeleccionado);
            JSFMessagesHelper.addServiceErrorMessage(e.getCodigoRespuesta());
        } catch (Exception e) {
            LOGGER.error("Exception al buscar informacion de Pin Puk equipo.", e);
            JSFMessagesHelper.addServiceErrorMessage("noinfoequipo");
        }
    }
    
    /**
     * Inicializa los datos de Pin Puk Equipo
     * 
     * @param event
     */
    private void loadDataPinPuk(String numeroPcsSeleccionado){
    	try{
        	pinPuk = equipoDelegate.consultarPinPuk(numeroPcsSeleccionado);
        } catch (DAOException e) {
        	pinPuk = null;            	
            LOGGER.error("DAOException al buscar informacion de Pin Puk equipo.", e);
            JSFMessagesHelper.addServiceErrorMessage("noinfoequipo");
        } catch (ServiceException e) {
        	pinPuk = null;
            LOGGER.info("ServiceException al buscar informacion de Pin Puk equipo. msisdn: "+numeroPcsSeleccionado);
            JSFMessagesHelper.addServiceErrorMessage(e.getCodigoRespuesta());
        } catch (Exception e) {
        	pinPuk = null;
            LOGGER.error("Exception al buscar informacion de Pin Puk equipo.", e);
            JSFMessagesHelper.addServiceErrorMessage("noinfoequipo");
        }
    }
    
    /**
     * Inicializa los datos de Ordenes de trabajo Equipo
     * 
     * @param event
     */
    private void loadDataOrdenesTrabajo(String numeroPcsSeleccionado){
    	try{
        	ordenesTrabajoVigentes = equipoDelegate.obtenerOrdenesDeTrabajoVigente(numeroPcsSeleccionado);
        	ordenesLength = ordenesTrabajoVigentes != null ? ordenesTrabajoVigentes.getOrdenes() != null ? ordenesTrabajoVigentes.getOrdenes().size() : 0 : 0;
        }catch (DAOException e) {
        	ordenesTrabajoVigentes = null;    	
            LOGGER.error("DAOException al buscar informacion de ordenes de trabajo equipo.", e);
            JSFMessagesHelper.addServiceErrorMessage("noinfoequipo");
        } catch (ServiceException e) {
        	ordenesTrabajoVigentes = null;
            LOGGER.info("ServiceException al buscar informacion de ordenes de trabajo equipo. msisdn: "+numeroPcsSeleccionado);
            JSFMessagesHelper.addServiceErrorMessage(e.getCodigoRespuesta());
        } catch (Exception e) {
        	ordenesTrabajoVigentes = null;
            LOGGER.error("Exception al buscar informacion de ordenes de trabajo equipo.", e);
            JSFMessagesHelper.addServiceErrorMessage("noinfoequipo");
        }
    }
    
    /**
     * Inicializa los datos de usuaio bloqueo Equipo
     * 
     * @param event
     */
    private void loadDataDatosUsuarioBloqueo(String rutSinDV, String DV){
    	try{
        	datosUsuarioBloqueo = equipoDelegate.obtenerDatosUsuarioBloqueo(rutSinDV, DV);        	
        }catch (DAOException e) {
        	datosUsuarioBloqueo = null;
            LOGGER.error("DAOException al buscar informacion de datos usuario bloqueo equipo.", e);
            JSFMessagesHelper.addServiceErrorMessage("noinfoequipo");
        } catch (ServiceException e) {
        	datosUsuarioBloqueo = null;
            LOGGER.info("ServiceException al buscar informacion de datos usuario bloqueo equipo. rutSinDv: "+rutSinDV);
            JSFMessagesHelper.addServiceErrorMessage(e.getCodigoRespuesta());
        } catch (Exception e) {
        	datosUsuarioBloqueo = null;
            LOGGER.error("Exception al buscar informacion de datos usuario bloqueo equipo.", e);
            JSFMessagesHelper.addServiceErrorMessage("noinfoequipo");
        }
    }
    
    /**
     * Inicializa los datos de situacion actual Equipo
     * 
     * @param event
     */
    private void loadDataSituacionActualEquipo(String rutTitular, String nroCuenta, String rutSinDV, String DV){
    	try{
    	    if (isEnGrupoHabil()) {
                this.situacionActualEquipo = equipoDelegate
                        .obtenerSituacionActualEquipo(numeroPcs, rutTitular,
                                nroCuenta, rutSinDV, DV);
    	    } else {
    	        this.situacionActualEquipo = new SituacionActualEquipoBean();
    	    }
        } catch (DAOException e) {
        	situacionActualEquipo = null;            	
            LOGGER.error("DAOException al buscar informacion de situacion actual equipo.", e);
            JSFMessagesHelper.addServiceErrorMessage("noinfoequipo");
        } catch (ServiceException e) {
        	situacionActualEquipo = null;   
            LOGGER.info("ServiceException al buscar informacion de situacion actual equipo. msisdn: "+numeroPcs);
            JSFMessagesHelper.addServiceErrorMessage(e.getCodigoRespuesta());
        } catch (Exception e) {
        	situacionActualEquipo = null;   
            LOGGER.error("Exception al buscar informacion de situacion actual equipo.", e);
            JSFMessagesHelper.addServiceErrorMessage("noinfoequipo");
        }
    }
    
    /**
     * 
     * @param rutUsuario
     * @param nroCuenta
     */
    private GrupoClienteBean obtenerGrupoCliente(String rutUsuario, String nroCuenta){
    	
    	GrupoClienteBean grupoClienteBean = new GrupoClienteBean();
    	try{
    		grupoClienteBean = equipoDelegate.obtenerGrupoCliente(rutUsuario, nroCuenta);
            	
        } catch (DAOException e) {
            LOGGER.error("DAOException al obtener grupo del cliente.", e);
        } catch (ServiceException e) {
            LOGGER.info("ServiceException de servicio al obtener grupo del cliente. msisdn: "+numeroPcs);
        } catch (Exception e) {
            LOGGER.error("Exception al buscar informacion de situacion actual equipo.", e);
        }
        return grupoClienteBean;
    }
    
    /**
     * Inicializa la cantidad de equipos disponibles con ACOC al cual el cliente puede acceder, incorporando los mensajes respectivos de acuerdo a los tipos de planes contratados.
     * @param rutUsuario
     * @param nroCuenta
     * @param grupoCliente
     */
    private void loadEquiposDisponiblesACOC(String rutUsuario, String nroCuenta, String grupoCliente){
    	
    	try{
            resumenLineaEquipoBean = equipoDelegate.obtenerResumenLineaEquipos(rutUsuario, nroCuenta, grupoCliente);
        } catch (DAOException e) {
        	LOGGER.error("DAOException al obtener el resumen de lineas. Msisdn: "+numeroPcs, e);
        } catch (ServiceException e) {
        	LOGGER.info("ServiceException de servicio al obtener el resumen de lineas. Msisdn: "+numeroPcs, e);
        } catch (Exception e) {
        	LOGGER.error("DAOException al obtener el resumen de lineas. Msisdn: "+numeroPcs, e);
        }
    }
    
    /**
     * Inicializa los datos de Equipo
     * 
     * @param event
     */
    public void init(PhaseEvent event) {
    	String numeroPcsSeleccionado = "";
    	try {

            ProfileWrapper profileWrapper = ProfileWrapperHelper
                    .getProfile(JSFPortletHelper.getRequest());
            
            numeroPcsSeleccionado = ProfileWrapperHelper.getPropertyAsString(
                    profileWrapper, "numeroPcsSeleccionado");
            this.resumenEquipoBean = this.equipoDelegate
                    .getResumenEquipo(numeroPcsSeleccionado);
            this.setNumeroPcs(numeroPcsSeleccionado);
            
        }  catch (DAOException e) {
            LOGGER.error("DAOException al buscar informacion de equipo.", e);
            JSFMessagesHelper.addServiceErrorMessage("noinfoequipo");
        } catch (ServiceException e) {
            LOGGER.info("ServiceException al buscar informacion de equipo. msisdn: "+numeroPcsSeleccionado);
            JSFMessagesHelper.addErrorCodeMessage("gestionDePerfiles", e.getCodigoRespuesta(), new String[]{numeroPcs});
        } catch (Exception e) {
            LOGGER.error("Exception al buscar informacion de equipo.", e);
            JSFMessagesHelper.addServiceErrorMessage("noinfoequipo");
        }
    }       
    
    public void contactoPorRenovacion(){
    	try{
    		equipoDelegate.contactoPorRenovacion(contactoPorRenovacion);
    	}  catch (DAOException e) {
            LOGGER.error("DAOException al buscar informacion de equipo.", e);
            JSFMessagesHelper.addServiceErrorMessage("equipo");
        } catch (ServiceException e) {
            LOGGER.info("ServiceException al buscar informacion de equipo.");
            JSFMessagesHelper.addServiceErrorMessage(e.getCodigoRespuesta());
        } catch (Exception e) {
            LOGGER.error("Exception al buscar informacion de equipo.", e);
            JSFMessagesHelper.addServiceErrorMessage("equipo");
        }
    }
        
    /**
	 * @return bloqueoExistente
	 */
	public boolean isBloqueoExistente() {
		return bloqueoExistente;
	}

	/**
	 * @param bloqueoExistente
	 */
	public void setBloqueoExistente(boolean bloqueoExistente) {
		this.bloqueoExistente = bloqueoExistente;
	}

	/**
     * Datos del equipo del usuario en sesion
     * 
     * @return ResumenEquipoBean
     * @throws Exception
     */
    public ResumenEquipoBean getResumenEquipo() {
        return this.resumenEquipoBean;
    }

    /**
     * @return numeroPcs
     */
    public String getNumeroPcs() {
        return numeroPcs;
    }

    /**
     * @param numeroPcs numeroPcs to set
     */
    public void setNumeroPcs(String numeroPcs) {
        this.numeroPcs = numeroPcs;
    }

	/**
	 * @return situacionActualEquipo
	 */
	public SituacionActualEquipoBean getSituacionActualEquipo() {
		return situacionActualEquipo;
	}

	/**
	 * @param situacionActualEquipo
	 */
	public void setSituacionActualEquipo(
			SituacionActualEquipoBean situacionActualEquipo) {
		this.situacionActualEquipo = situacionActualEquipo;
	}
	
	/**
	 * @return pinPuk
	 */
	public PinPukBean getPinPuk() {
		return pinPuk;
	}

	/**
	 * @param pinPuk the pinPuk to set
	 */
	public void setPinPuk(PinPukBean pinPuk) {
		this.pinPuk = pinPuk;
	}

	/**
	 * @return contactoPorRenovacion
	 */
	public ContactoPorRenovacionBean getContactoPorRenovacion() {
		return contactoPorRenovacion;
	}

	/**
	 * @param contactoPorRenovacion
	 */
	public void setContactoPorRenovacion(
			ContactoPorRenovacionBean contactoPorRenovacion) {
		this.contactoPorRenovacion = contactoPorRenovacion;
	}

	/**
	 * @return respuestaJson
	 */
	public String getRespuestaJson() {
		return respuestaJson;
	}

	/**
	 * @param respuestaJson
	 */
	public void setRespuestaJson(String respuestaJson) {
		this.respuestaJson = respuestaJson;
	}

	/**
	 * @return datosUsuario
	 */
	public DatosUsuarioBloqueoBean getDatosUsuarioBloqueo() {
		return datosUsuarioBloqueo;
	}

	/**
	 * @param datosUsuario
	 */
	public void setDatosUsuarioBloqueo(DatosUsuarioBloqueoBean datosUsuarioBloqueo) {
		this.datosUsuarioBloqueo = datosUsuarioBloqueo;
	}

	/**
	 * @return ordenesTrabajoVigentes
	 */
	public OrdenTrabajoVigenteBean getOrdenesTrabajoVigentes() {
		return ordenesTrabajoVigentes;
	}

	/**
	 * @param ordenesTrabajoVigentes
	 */
	public void setOrdenesTrabajoVigentes(
			OrdenTrabajoVigenteBean ordenesTrabajoVigentes) {
		this.ordenesTrabajoVigentes = ordenesTrabajoVigentes;
	}

	/**
	 * @return ordenesLength
	 */
	public int getOrdenesLength() {
		return ordenesLength;
	}

	/**
	 * @param ordenesLength
	 */
	public void setOrdenesLength(int ordenesLength) {
		this.ordenesLength = ordenesLength;
	}
	
	 /**
	 * @return imei
	 */
	public String getImei() {
		return imei;
	}

	/**
	 * @param imei
	 */
	public void setImei(String imei) {
		this.imei = imei;
	}

	/**
     * Metodo que se encarga de obtener y retornar el valor de la preferencia del portlet en solicitud
     * dependiendo del mercado como clave asociada al valor.
     * @return
     */
    public String getPageLabelByMercado(){
    	try{
    	  ProfileWrapper profileWrapper = ProfileWrapperHelper
            .getProfile(JSFPortletHelper.getRequest());
    		
    	  return JSFPortletHelper.getPreference(JSFPortletHelper.getPreferencesObject(), ProfileWrapperHelper.getPropertyAsString(
                  profileWrapper, "mercado"), null);
		 
        }catch(Exception e){
        	LOGGER.error("No se ha podido obtener el pageLabel "+ e.getMessage(), e);
        	return "";
        }
    
    }

	/**
	 * @return atributoAceptacionPrespuesto
	 */
	public String getAtributoAceptacionPrespuesto() {
		return atributoAceptacionPrespuesto;
	}

	/**
	 * @param atributoAceptacionPrespuesto
	 */
	public void setAtributoAceptacionPrespuesto(String atributoAceptacionPrespuesto) {
		this.atributoAceptacionPrespuesto = atributoAceptacionPrespuesto;
	}

	/**
	 * @return atributoRechazoPrespuesto
	 */
	public String getAtributoRechazoPrespuesto() {
		return atributoRechazoPrespuesto;
	}

	/**
	 * @param atributoRechazoPrespuesto
	 */
	public void setAtributoRechazoPrespuesto(String atributoRechazoPrespuesto) {
		this.atributoRechazoPrespuesto = atributoRechazoPrespuesto;
	}

	/**
	 * @return informeTecnicoOT
	 */
	public InformeTecnicoOTBean getInformeTecnicoOT() {
		return informeTecnicoOT;
	}

	/**
	 * @param informeTecnicoOT
	 */
	public void setInformeTecnicoOT(InformeTecnicoOTBean informeTecnicoOT) {
		this.informeTecnicoOT = informeTecnicoOT;
	}

	/**
	 * @return documentoAperturaOT
	 */
	public DocumentoAperturaOTBean getDocumentoAperturaOT() {
		return documentoAperturaOT;
	}

	/**
	 * @param documentoAperturaOT
	 */
	public void setDocumentoAperturaOT(DocumentoAperturaOTBean documentoAperturaOT) {
		this.documentoAperturaOT = documentoAperturaOT;
	}

	/**
	 * @return fechaActual
	 */
	public String getFechaActual() {
		return fechaActual;
	}

	/**
	 * @param fechaActual
	 */
	public void setFechaActual(String fechaActual) {
		this.fechaActual = fechaActual;
	}
	
    public boolean isEnGrupoHabil() {
        return enGrupoHabil;
    }
    
	 /**
     * Obtiene el digito de verificacion para el imei
     * 
     * @param event
     */
    
    private int loadImeiDV( String msisdn ){
    	int imeiDV = 0;
    	try{
    		LOGGER.info("loadImeiDV");
    		String imeiEquipo = equipoDelegate.obtenerImei(msisdn);
    		imeiDV = calcularDigitoIMEI( imeiEquipo );
    		
    	}   catch (DAOException e) {
            LOGGER.error("DAOException al buscar informacion del imei.", e);
            JSFMessagesHelper.addServiceErrorMessage("noinfoequipo");
        } catch (ServiceException e) {
            LOGGER.info("ServiceException de servicio al buscar informacion de imei. msisdn: "+msisdn);
            JSFMessagesHelper.addErrorCodeMessage("general", e.getCodigoRespuesta());
        } catch (Exception e) {
            LOGGER.error("Exception al buscar informacion de imei.", e);
            JSFMessagesHelper.addServiceErrorMessage("noinfoequipo");
        }
		return imeiDV;     	
    }
    
    /**
     * Calcula el DV para el imei
     * 
     * @param event
     */    
    public int calcularDigitoIMEI(String imei) {

        int sum = 0;
  
        for (int i = 0; i < imei.length(); i++) {
            sum += Integer.parseInt(imei.substring(i, i + 1));
        }

        int delta[] = {0, 1, 2, 3, 4, -4, -3, -2, -1, 0};
        for (int i = imei.length() - 1; i >= 0; i -= 2) {
            int deltaIndex = Integer.parseInt(imei.substring(i, i + 1));
            int deltaValue = delta[deltaIndex];
            sum += deltaValue;
        }

        int mod10 = sum % 10;
        mod10 = 10 - mod10;
        if (mod10 == 10) {
            mod10 = 0;
        }

        return mod10;
    }

    /**
     * Metodo que se encarga de obtener y retornar el valor de la preferencia del portlet en solicitud
     * @return
     */
    public String getURLMiEntelV2() {
        String idp = "";
        String url = "";
        this.seguridadDelegate = new SeguridadDelegate();
        try {
            ProfileWrapper profileWrapper = ProfileWrapperHelper.getProfile(JSFPortletHelper.getRequest());
            idp = this.seguridadDelegate.consultarIDP(ProfileWrapperHelper.getPropertyAsString(profileWrapper, "numeroPcs"));
            url = MiEntelProperties.getProperty("miEntel.contextoV2");
            return (url.concat(idp).concat("&contexto="));
        } catch (DAOException e) {
            LOGGER.error("DAOException al obtener la URL de MiEntelV2", e);
            return "";
        } catch (ServiceException e) {
            LOGGER.info("ServiceException al obtener la URL de MiEntelV2 codigo: " + e.getCodigoRespuesta()
                    + " - " + e.getDescripcionRespuesta());
            return "";
        } catch (Exception e) {
            LOGGER.error("Exception inesperado al obtener la URL de MiEntelV2", e);
            return "";
        }
    }
    
}
