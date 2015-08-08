/* Propiedad de Entel PCS. Todos los derechos reservados */
package com.epcs.inscripcion.controller;

import java.io.Serializable;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.bea.p13n.usermgmt.profile.ProfileWrapper;
import com.epcs.bean.CiudadBean;
import com.epcs.bean.ComunaBean;
import com.epcs.bean.DatosUsuarioTitularBean;
import com.epcs.bean.DireccionBean;
import com.epcs.bean.FacturacionElectronicaBean;
import com.epcs.bean.RegionBean;
import com.epcs.bean.RutBean;
import com.epcs.bean.UsuarioBean;
import com.epcs.bean.CuentaClienteBean;
import com.epcs.billing.prodfactura.delegate.FacturacionElectronicaDelegate;
import com.epcs.cliente.perfil.delegate.CuentaDelegate;
import com.epcs.erp.seguridad.delegate.SeguridadDelegate;
import com.epcs.inscripcion.delegate.InscripcionDelegate;
import com.epcs.inscripcion.util.InscripcionHelper;
import com.epcs.recursoti.configuracion.DateHelper;
import com.epcs.recursoti.configuracion.MiEntelBusinessHelper;
import com.epcs.recursoti.configuracion.MiEntelProperties;
import com.epcs.recursoti.configuracion.ParametrosHelper;
import com.epcs.recursoti.configuracion.Utils;
import com.epcs.recursoti.configuracion.jsf.utils.JSFMessagesHelper;
import com.epcs.recursoti.configuracion.jsf.utils.JSFPortletHelper;
import com.epcs.recursoti.configuracion.jsf.utils.JsfUtil;
import com.epcs.recursoti.configuracion.uup.ProfileWrapperHelper;
import com.epcs.recursoti.excepcion.DAOException;
import com.epcs.recursoti.excepcion.ServiceException;
import com.epcs.recursoti.parametros.dao.ParametrosDAO;
import com.epcs.recursoti.parametros.delegate.ParametrosDelegate;
import com.epcs.billing.balance.util.ArcFour;

/**
 * @author gcastro (I2B) en nombre de Absalon Opazo (Atencion al Cliente,
 *         EntelPcs)
 * 
 */

public class InscripcionController implements Serializable {

	private static final long serialVersionUID = 1L;

	private static final Logger LOGGER = Logger
			.getLogger(InscripcionController.class);

	private InscripcionDelegate inscripcionDelegate;

	private ParametrosDelegate parametrosDelegate;

	private FacturacionElectronicaDelegate facturacionElectronicaDelegate;
	
	private CuentaDelegate cuentaDelegate;

	private UsuarioBean usuarioBean;

	private String rutUsuario;

	private String numeroPcs;

	private String diaNacimiento;

	private String mesNacimiento;

	private String anoNacimiento;

	private SelectItem[] regionesList;
	
	private SelectItem[] areasList;

	private List<CiudadBean> itemsCiudades = null;

	private List<ComunaBean> itemsComunas = null;

	private static final String COD_INGRESO_FORMDATOS = MiEntelProperties
			.getProperty("inscripcion.formRegistro.codigo.ingresar");

	private static final String TIPO_INSCRIPCION_ATTTR_NAME = MiEntelProperties
			.getProperty("inscripcion.tipo.attr.name");
	
	private static final String CONTEXTO_ATTR_NAME = MiEntelProperties
			.getProperty("miEntel.login.contexto.attr");

	private String acceso = "";
	
	private String tipoInscripcion;

	/**
	 * DAO de Parametros para obtener informacion de Region, ciudad y comuna de
	 * usuarios.
	 */
	private ParametrosDAO parametrosDAO = new ParametrosDAO();

	private int maximoRutEnrolamiento;

	private int rutNumerico;

	private boolean suscripcionFactElectronica = true;
	
	private String mercado;
	
	private boolean mercadoPrepago = false;
	
	private boolean rutTitular = false;
	
	private CuentaClienteBean cuentaClienteBean = null;
	
	private boolean inscritoFacturacionElectronica = false;
	
	private String nmroCuentaTitular;

	private boolean redireccionarHomeEntel = false;
	/**
    * 
    */
	public void init(PhaseEvent event) {
		try {
			prepareData();
		} catch (Exception e) {
			LOGGER.error("Exception al intentar verificar el acceso y obtener los parametros de la URL.", e);
		}
	}

	/**
	 * 
	 */
	public InscripcionController() {
		usuarioBean = new UsuarioBean();

		DireccionBean direccionBean = new DireccionBean();
		direccionBean.setRegion(new RegionBean("", ""));
		direccionBean.setCiudad(new CiudadBean("", ""));
		direccionBean.setComuna(new ComunaBean("", ""));
		usuarioBean.setDireccionContacto(direccionBean);
	}
	

	/**
	 * @return the inscripcionDelegate
	 */
	public InscripcionDelegate getInscripcionDelegate() {
		return inscripcionDelegate;
	}

	/**
	 * @param inscripcionDelegate
	 *            the inscripcionDelegate to set
	 */
	public void setInscripcionDelegate(InscripcionDelegate inscripcionDelegate) {
		this.inscripcionDelegate = inscripcionDelegate;
	}

	/**
	 * @return the parametrosDelegate
	 */
	public ParametrosDelegate getParametrosDelegate() {
		return parametrosDelegate;
	}

	/**
	 * @param parametrosDelegate
	 *            the parametrosDelegate to set
	 */
	public void setParametrosDelegate(ParametrosDelegate parametrosDelegate) {
		this.parametrosDelegate = parametrosDelegate;
	}

	public FacturacionElectronicaDelegate getFacturacionElectronicaDelegate() {
		return facturacionElectronicaDelegate;
	}

	public void setFacturacionElectronicaDelegate(
			FacturacionElectronicaDelegate facturacionElectronicaDelegate) {
		this.facturacionElectronicaDelegate = facturacionElectronicaDelegate;
	}

	
	public CuentaDelegate getCuentaDelegate() {
		return cuentaDelegate;
	}

	public void setCuentaDelegate(CuentaDelegate cuentaDelegate) {
		this.cuentaDelegate = cuentaDelegate;
	}

	/**
	 * 
	 * @return
	 */
	public UsuarioBean getUsuarioBean() {
		return usuarioBean;
	}

	/**
	 * 
	 * @param usuarioBean
	 */
	public void setUsuarioBean(UsuarioBean usuarioBean) {
		this.usuarioBean = usuarioBean;
	}

	/**
	 * 
	 * @return
	 */
	public String getRutUsuario() {
		return rutUsuario;
	}

	/**
	 * 
	 * @param rutUsuario
	 */
	public void setRutUsuario(String rutUsuario) {
		this.rutUsuario = rutUsuario;
	}

	/**
	 * Metodo que se encarga de obtener y retornar el valor del
	 * numeroPcsSeleccionado.
	 * 
	 * @return
	 */
	public String getNumeroPcs() {
		return numeroPcs;
	}

	/**
	 * 
	 * @param numeroPcsSeleccionado
	 */
	public void setNumeroPcs(String numeroPcs) {
		this.numeroPcs = numeroPcs;
	}

	/**
	 * 
	 * @return
	 */
	public String getDiaNacimiento() {
		return diaNacimiento;
	}

	/**
	 * 
	 * @param diaNacimiento
	 */
	public void setDiaNacimiento(String diaNacimiento) {
		this.diaNacimiento = diaNacimiento;
	}

	/**
	 * 
	 * @return
	 */
	public String getMesNacimiento() {
		return mesNacimiento;
	}

	/**
	 * 
	 * @param mesNacimiento
	 */
	public void setMesNacimiento(String mesNacimiento) {
		this.mesNacimiento = mesNacimiento;
	}

	/**
	 * 
	 * @return
	 */
	public String getAnoNacimiento() {
		return anoNacimiento;
	}

	/**
	 * 
	 * @param anoNacimiento
	 */
	public void setAnoNacimiento(String anoNacimiento) {
		this.anoNacimiento = anoNacimiento;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getAcceso() {
		return acceso;
	}
	
	
	
	/**
	 * Metodo utilizado para obtener los parametros de acceso y perfil de usuario segun el caso
	 * de ingreso al flujo de inscripcion.
	 * 
	 */
	public void prepareData(){
		
		String numeroPCSByID = "";
		InscripcionHelper insc = new InscripcionHelper();
		
		// Parametro de acceso al formulario de inscripcion, Login 
		// Selector de Cuenta o Form Mis Datos.
		acceso = (Utils.isNotEmptyString((String) JSFPortletHelper.getRequest().getSession().getAttribute("acceso"))) ? 
				(String) JSFPortletHelper.getRequest().getSession().getAttribute("acceso") : 
					JSFPortletHelper.getRequest().getParameter("acceso");


		if (acceso != null) {
			
			 // LOGIN
			if (acceso.equals(insc.getAccesoLogin())) {
				
				//Obtenemos el contexto para redireccionar despues del registro exitoso
				String contexto = JSFPortletHelper.getRequest().getParameter(CONTEXTO_ATTR_NAME);
				
				if(Utils.isNotEmptyString(contexto))
					//Agregamos el contexto a la sesion para recuperarlo despues del registro exitoso de usuario
					JSFPortletHelper.getRequest().getSession().setAttribute(CONTEXTO_ATTR_NAME, contexto);
				
				if(Utils.isNotEmptyString(JSFPortletHelper.getRequest().getParameter("IDP"))){
					// Valida si se entregan todos los parametros
					if(InscripcionHelper.verificarParametros(JSFPortletHelper.getRequest().getParameter("IDP"),
							JSFPortletHelper.getRequest().getParameter("acceso"), 
							JSFPortletHelper.getRequest().getParameter("rut"), 
							JSFPortletHelper.getRequest().getParameter(TIPO_INSCRIPCION_ATTTR_NAME))){

						// Obtiene numeroPCS asociado a IDP pasado en el request
						numeroPCSByID = InscripcionHelper.deletePrefijoEntel((InscripcionHelper.obtenerMsisdnByIDP(JSFPortletHelper.getRequest().getParameter("IDP"))));								
						
						// Valida que el numeroPcs e IDP pasado en el request sean correspondientes
						if(numeroPCSByID.equals(JSFPortletHelper.getRequest().getParameter("numeroPcs"))) {

					      	JSFPortletHelper.getRequest().getSession()
					      			.setAttribute("numeroPcs",numeroPCSByID);

							JSFPortletHelper.getRequest().getSession()
									.setAttribute("rutUsuario",JSFPortletHelper.getRequest().getParameter("rut"));
		
							JSFPortletHelper.getRequest().getSession()
									.setAttribute(TIPO_INSCRIPCION_ATTTR_NAME,JSFPortletHelper.getRequest().getParameter(
															TIPO_INSCRIPCION_ATTTR_NAME));
											   
						} else {
							LOGGER.info("Redireccionando desde el Formulario de Registro a la pagina de LOGIN por inconsistencia de IDP vs numeroPCS.");
							this.redireccionarHomeEntel = true;
						}
					
					}else{
						LOGGER.info("Redireccionando desde el Formulario de Registro a la pagina de LOGIN por falta de parametros.");
						this.redireccionarHomeEntel = true;
					}
				// Modificacion ID, basado en EPCSEncriptedLoginModule
				} else if (Utils.isNotEmptyString(JSFPortletHelper.getRequest().getParameter("ID"))) { 
					String idEncript=JSFPortletHelper.getRequest().getParameter("ID");
					final String clave = "ClaveIPBAMPP_2013";
			        final Date date=new Date();
			    	long actual=date.getTime();
			    	
			        ArcFour a4=new ArcFour();  
			        final String desencriptado = a4.desencriptar(idEncript, clave);
			        final String[] splitted = desencriptado.split(":");
			        if (splitted.length != 4) {
			        	LOGGER.info("Redireccionando desde el Formulario de Registro a la pagina de LOGIN por ID invalido.");	
			        	this.redireccionarHomeEntel = true;		
					}

			        final String msisdn = splitted[0];
			        final String ip = splitted[1];
			        final String schar = splitted[2];
			        final String timestamp = splitted[3];

					long stamp=0;
					
					try{
						stamp=Long.parseLong(timestamp);
					}catch (NumberFormatException e) {
						LOGGER.info("Redireccionando desde el Formulario de Registro a la pagina de LOGIN por timestamp del ID invalido.");
						this.redireccionarHomeEntel = true;		
					}finally{
						stamp+=60000;//Le damos 1 minuto 
					}
					
					if (stamp<actual) {
						LOGGER.info("Redireccionando desde el Formulario de Registro a la pagina de LOGIN por ID vencido.");
						this.redireccionarHomeEntel = true;		
					}
					
					numeroPCSByID = InscripcionHelper.deletePrefijoEntel(msisdn);								
					
					// Valida que el numeroPcs e IDP pasado en el request sean correspondientes
					if(numeroPCSByID.equals(JSFPortletHelper.getRequest().getParameter("numeroPcs"))) {

				      	JSFPortletHelper.getRequest().getSession()
				      			.setAttribute("numeroPcs",numeroPCSByID);

						JSFPortletHelper.getRequest().getSession()
								.setAttribute("rutUsuario",JSFPortletHelper.getRequest().getParameter("rut"));
	
						JSFPortletHelper.getRequest().getSession()
								.setAttribute(TIPO_INSCRIPCION_ATTTR_NAME,JSFPortletHelper.getRequest().getParameter(
														TIPO_INSCRIPCION_ATTTR_NAME));
										   
					} else {
						LOGGER.info("Redireccionando desde el Formulario de Registro a la pagina de LOGIN por inconsistencia de ID vs numeroPCS.");
						this.redireccionarHomeEntel = true;
					}
				} else {
					LOGGER.info("Redireccionando desde el Formulario de Registro a la pagina de LOGIN por IDP e ID en blanco.");
					this.redireccionarHomeEntel = true;					
				}				
				// Modificacion ID
				
				this.numeroPcs = (Utils.isEmptyString(numeroPCSByID)) ? ((String) JSFPortletHelper
						.getRequest().getSession().getAttribute("numeroPcs") == null ? (String) JSFPortletHelper
						.getRequest().getParameter("numeroPcs")
						: (String) JSFPortletHelper.getRequest().getSession().getAttribute("numeroPcs")) : numeroPCSByID;

				this.rutUsuario = ((Utils.isEmptyString(JSFPortletHelper.getRequest().getParameter("rut"))) ? (String) JSFPortletHelper.getRequest().getSession().getAttribute("rutUsuario") : JSFPortletHelper.getRequest().getParameter("rut"));

				
				LOGGER.info("RUT NUMERICO = "+this.rutUsuario.substring(0, this.rutUsuario.length() - 1));
				this.rutNumerico = Integer.parseInt(this.rutUsuario.substring(0, this.rutUsuario.length() - 1));

				this.tipoInscripcion = ((Utils
						.isEmptyString(JSFPortletHelper.getRequest()
								.getParameter(TIPO_INSCRIPCION_ATTTR_NAME))) ? (String) JSFPortletHelper
						.getRequest().getSession().getAttribute(
								TIPO_INSCRIPCION_ATTTR_NAME)
						: JSFPortletHelper.getRequest().getParameter(
								TIPO_INSCRIPCION_ATTTR_NAME));
				
				try{
					this.mercado = inscripcionDelegate.obtenerMercado(this.numeroPcs).getMercado();
					
					if (!mercado.equalsIgnoreCase("PP")){		
						
						DatosUsuarioTitularBean datosUsuarioTitular = obtenerDatosTitular(this.numeroPcs);
						String rutUsuarioTitular = datosUsuarioTitular.getRutTitular();
						this.nmroCuentaTitular = datosUsuarioTitular.getNumeroCuenta();
						
						int max1 = rutUsuario.length();
						int max2 = rutUsuarioTitular.length();
						
						if (rutUsuarioTitular!=null && rutUsuarioTitular.replaceFirst("^0*", "").equals(rutUsuario.replaceFirst("^0*", ""))){
							this.rutTitular = true;
							this.rutUsuario = rutUsuarioTitular;
							
							List<CuentaClienteBean> cuentasClienteBean = cuentaDelegate.obtenerCuentaCliente(rutUsuario);    		
				    		
				    		if (cuentasClienteBean !=null){
				    			
				    			for (CuentaClienteBean cuentaCliente: cuentasClienteBean){
					    			if (cuentaCliente.getNroCuenta().equals(nmroCuentaTitular)){
					    				this.cuentaClienteBean = cuentaCliente;
					    				break;
					    			}
				    			}
				    		}				    		
				    		
				    		//Verifica si el usuario se encuentra inscrito al servicio de facturacion electronica
				    		FacturacionElectronicaBean facturacionElectronica = facturacionElectronicaDelegate.getFacturacionElectronica(rutUsuarioTitular, nmroCuentaTitular);
				    		if (facturacionElectronica!=null){
				    			this.inscritoFacturacionElectronica = true;
				    		}
						}						
					}
				}
				catch (Exception exc){
					LOGGER.error("Error al obtener el mercado del usuario. ", exc);
				}

				try {
					this.maximoRutEnrolamiento = Integer
							.parseInt(JSFPortletHelper.getPreference(
									JSFPortletHelper.getPreferencesObject(),
									"RutEnrolamiento", null));
					LOGGER.info("Maximo rut enrolamiento obtenido. Valor="
							+ this.maximoRutEnrolamiento);
				}catch(NumberFormatException ex){
					LOGGER.error("Error parseando rut ",ex);
				}catch(Exception ex){
					LOGGER.error("Error obteniendo preference ",ex);
				}

				insc.getMessageByParametroTipoInscripcion();
			
		    // SELECTOR CUENTA	
			} else if ( acceso.equals(insc.getAccesoSelectorCuenta())) {
				
				ProfileWrapper profile = ProfileWrapperHelper.getProfile(JSFPortletHelper.getRequest());

				try {
					this.numeroPcs = ProfileWrapperHelper.getPropertyAsString(
							profile, "numeroPcsSeleccionado");
				} catch (Exception e) {
					LOGGER.error("Exception al intentar obtener el parametro numeroPCS del perfil.");
				}
				
				this.tipoInscripcion = acceso;
				insc.getMessageByParametroAcceso();
			
		    // FORM MIS DATOS
			}else if( acceso.equals(insc.getAccesoFormMisDatos())){
				
				if ((Utils.isNotEmptyString(JSFPortletHelper.getRequest().getParameter("acceso")))) {
					
					JSFPortletHelper.getRequest().getSession()
							.setAttribute("numeroPcs",JSFPortletHelper.getRequest().getParameter("numeroPcs"));
				}
				
				this.numeroPcs = (String) JSFPortletHelper.getRequest().getSession().getAttribute("numeroPcs");
				this.tipoInscripcion = acceso;
				insc.getMessageByParametroAcceso();
			}
			
			JSFPortletHelper.getRequest().getSession().setAttribute("acceso", acceso);
		}
	}

	/**
	 * Metodo utilitario para la obtencion del rut titular asociado a un
	 * numeroPcs.
	 * 
	 */
	public DatosUsuarioTitularBean obtenerDatosTitular(String msisdn) {
		DatosUsuarioTitularBean datosUsuarioTitular = null;

		try {
			InscripcionDelegate inscripcionDelegate = new InscripcionDelegate();
			datosUsuarioTitular = inscripcionDelegate.obtenerDatosTitular(msisdn);

		} catch (DAOException e) {
			LOGGER.error("DAOException caught: " + e);
			JSFMessagesHelper.addServiceErrorMessage("noDisponible");

		} catch (ServiceException e) {
			LOGGER.info("ServiceException caught: " + e.getCodigoRespuesta()
					+ " - " + e.getDescripcionRespuesta());
			JSFMessagesHelper.addErrorCodeMessage("gestionDePerfiles", e
					.getCodigoRespuesta());

		} catch (Exception e) {
			LOGGER.error("Exception caught: " + e);
			JSFMessagesHelper.addServiceErrorMessage("noDisponible");
		}

		return datosUsuarioTitular;
	}
	
	/**
	 * Metodo utilitario para la obtencion del rut titular asociado a un
	 * numeroPcs.
	 * 
	 */
	public String obtenerRutTitular(String msisdn) {
		String rutTitular = "";

		try {
			InscripcionDelegate inscripcionDelegate = new InscripcionDelegate();
			rutTitular = inscripcionDelegate.obtenerRutTitular(msisdn);

		} catch (DAOException e) {
			LOGGER.error("DAOException caught: " + e);
			JSFMessagesHelper.addServiceErrorMessage("noDisponible");

		} catch (ServiceException e) {
			LOGGER.info("ServiceException caught: " + e.getCodigoRespuesta()
					+ " - " + e.getDescripcionRespuesta());
			JSFMessagesHelper.addErrorCodeMessage("gestionDePerfiles", e
					.getCodigoRespuesta());

		} catch (Exception e) {
			LOGGER.error("Exception caught: " + e);
			JSFMessagesHelper.addServiceErrorMessage("noDisponible");
		}

		return rutTitular;

	}

	/**
	 * Metodo que valida movil en el Buic y verifica la regla de restriccion por numero de dias,
	 * si se cumple con las condiciones es direccionado al formulario de inscripcion.
	 * 
	 * @return
	 */
	public String accederFormInscripcion() {
		boolean validarMovilBuic = false;
		boolean verificarRestriccionDias = false;

		try {
			validarMovilBuic = validarMovilBuic();
			if (validarMovilBuic) {

				InscripcionHelper.redirectInscripcionByIngresoRut(
						this.getNumeroPcs(), this.getRutUsuario(), 
						getAcceso());
			}else{
				
				verificarRestriccionDias = InscripcionHelper
				.verificarRestriccionDias(this.getNumeroPcs(), 
						this.getRutUsuario());
				
				   if(verificarRestriccionDias){
					   InscripcionHelper.redirectInscripcionByIngresoRut(this.getNumeroPcs(),this.getRutUsuario(),
							   getAcceso());
				   }
			}

		} catch (Exception e) {
			LOGGER.error("No fue posible ingresar al Formulario de Inscripcion "
					+ "desde el formulario de ingreso RUT. Exception caught: "
							+ e.getMessage());
		}

		return "";

	}

	/**
	 * Metodo que valida el movil en el Buic.
	 * 
	 * @return
	 */
	public boolean validarMovilBuic() {
		boolean outcome = false;
		try {

			inscripcionDelegate.validarMovilBuic(this.getNumeroPcs());

		} catch (DAOException e) {
			LOGGER.error("DAOException caught: " + e.getMessage());
			JSFMessagesHelper.addServiceErrorMessage("validarMovilBuic",
					new String[] { getNumeroPcs() });
		} catch (ServiceException e) {

			if (e.getCodigoRespuesta().equals(COD_INGRESO_FORMDATOS)) {
				outcome = true;
			} else {
				LOGGER.info("ServiceException caught: "
						+ e.getCodigoRespuesta() + " - "
						+ e.getDescripcionRespuesta());
				JSFMessagesHelper.addErrorCodeMessage("gestionDePerfiles", e
						.getCodigoRespuesta());
			}

		} catch (Exception e) {
			LOGGER.error("Exception al validar Movil BUIC: " + e.getMessage());
			JSFMessagesHelper.addServiceErrorMessage("validarMovilBuic",
					new String[] { getNumeroPcs() });
		}

		return outcome;
	}
	
	

	/**
	 * Devuelve las Regiones
	 * 
	 * @return SelectItem[]
	 * @throws DAOException
	 */
	public SelectItem[] getRegionesList() throws DAOException {
		if (this.regionesList == null) {
			try {
				this.regionesList = JsfUtil.getSelectItemsCodeDesc(
						this.parametrosDelegate.getRegionesList(), false);
			} catch (Exception e) {
				LOGGER.error("Error al cargar Regiones", e);
				regionesList = new SelectItem[0];
			}
		}
		return this.regionesList;
	}

	/**
	 * 
	 * @return
	 * @throws DAOException
	 */
	public List<CiudadBean> getCiudadesList() {

		if (RegionBean.isEmptyBean(this.usuarioBean.getDireccionContacto()
				.getRegion())) {

			itemsCiudades = Collections.emptyList();

		} else {

			if (itemsCiudades == null) {
				try {
					itemsCiudades = this.parametrosDelegate
							.getCiudadesList(this.usuarioBean
									.getDireccionContacto().getRegion());

				} catch (Exception e) {
					LOGGER.error("Error al cargar Ciudades", e);
					itemsCiudades = Collections.emptyList();
				}
			}

		}
		return itemsCiudades;
	}

	/**
	 * 
	 * @return
	 * @throws DAOException
	 */
	public List<ComunaBean> getComunasList() {

		if (RegionBean.isEmptyBean(this.usuarioBean.getDireccionContacto()
				.getRegion())
				&& CiudadBean.isEmptyBean(this.usuarioBean
						.getDireccionContacto().getCiudad())) {

			itemsComunas = Collections.emptyList();
		} else {
			if (itemsComunas == null) {
				try {
					itemsComunas = this.parametrosDelegate
							.getComunasList(this.usuarioBean
									.getDireccionContacto().getRegion(),
									this.usuarioBean.getDireccionContacto()
											.getCiudad());
				} catch (Exception e) {
					LOGGER.error("Error al cargar Ciudades", e);
					itemsComunas = Collections.emptyList();
				}
			}
		}

		return itemsComunas;
	}

	/**
	 * 
	 * @return
	 * @throws DAOException
	 */
//	public SelectItem[] getNivelEstudiosList() throws DAOException {
//		return JsfUtil.getSelectItemsCodeDesc(ParametrosHelper
//				.getNivelEstudiosList(), false);
//	}

	/**
	 * 
	 * @return
	 * @throws DAOException
	 */
//	public SelectItem[] getAreaLaboralList() throws DAOException {
//		return JsfUtil.getSelectItemsCodeDesc(ParametrosHelper
//				.getAreaLaboralList(), false);
//	}

	/**
	 * 
	 * @return
	 * @throws DAOException
	 */
	public SelectItem[] getSexoList() throws DAOException {
		return JsfUtil.getSelectItemsCodeDesc(ParametrosHelper.getSexoList(),
				false);
	}
	
	public SelectItem[] getAreasList() {
		return JsfUtil.getSelectItemsCodeDesc(ParametrosHelper.getAreaTelefonicaList(), 
				false);
	}

	/**
	 * 
	 * @return
	 * @throws DAOException
	 */
//	public SelectItem[] getActividadLaboralList() throws DAOException {
//		return JsfUtil.getSelectItemsCodeDesc(ParametrosHelper
//				.getActividadLaboralList(), false);
//	}

	/**
	 * Metodo Utilitario que valida y devuelve un dato booleano si el rut
	 * titular es igual al rut del usuario seleccionado
	 * 
	 * @return
	 */
	public boolean isExisteIgualdadEntreRuts() {

		boolean existeIgualdadEntreRuts = false;		
				
		try {	
			if(inscripcionDelegate.obtenerMercado(this.numeroPcs).getMercado().equals(MiEntelBusinessHelper.getSiglaPrepago())){
				return true;
			}
			else{
				RutBean rutTitular = new RutBean(obtenerRutTitular(this.getNumeroPcs()));
				RutBean rutUsuario = new RutBean(this.getRutUsuario());
				existeIgualdadEntreRuts = (rutTitular.equals(rutUsuario));
			}			
		} catch (Exception e) {
			LOGGER.error("Exception al intentar validar la similitud entre el rut titular y seleccionado.");
		}

		return existeIgualdadEntreRuts;
	}

	/**
	 * 
	 * @return
	 * @throws DAOException
	 */
	public SelectItem[] getRelacionesTitularList() throws DAOException {
		return JsfUtil.getSelectItemsCodeDesc(ParametrosHelper
				.getRelacionesTitularList(), false);
	}

	/**
	 * 
	 * @return
	 * @throws DAOException
	 */
//	public SelectItem[] getEstadosCivilList() throws DAOException {
//		return JsfUtil.getSelectItemsCodeDesc(ParametrosHelper
//				.getEstadosCivilList(), false);
//	}

	/**
	 * 
	 * @return
	 * @throws DAOException
	 */
//	public SelectItem[] getHijosList() throws DAOException {
//		return JsfUtil.getSelectItemsCodeDesc(ParametrosHelper.getHijosList(),
//				false);
//	}

	/**
	 * Metodo que devuelve la lista de los prefijos para red fija
	 * 
	 * @return
	 */
//	public List<SelectItem> getPrefijosTelefono() {
//		return ParametrosHelper.getPrefijoTelefonoParametrosList();
//	}

	/**
	 * Metodo utilitario que direcciona al paso de confirmacion del formulario
	 * de inscripcion de usuario
	 * 
	 * @return
	 */
	public String confirmarRegistro() {

		String postback = null;
		try {
			usuarioBean.setRut(RutBean.parseRut(this.getRutUsuario()));
			usuarioBean.setNumeroPCS(this.getNumeroPcs());

			if(this.getDiaNacimiento()!=null){
				String fechaCompuesta = (this.getDiaNacimiento()).concat(
						this.getMesNacimiento()).concat(this.getAnoNacimiento());
	
				Date fechaNacimiento = DateHelper.parseDate(fechaCompuesta,
						DateHelper.FORMAT_ddMMyyyy);
				usuarioBean.setFechaNacimiento(fechaNacimiento);
			}

			DireccionBean dirContactoBean = new DireccionBean();

			// REGION
			if(usuarioBean.getDireccionContacto()!=null){
				String regionId = usuarioBean.getDireccionContacto()
						.getRegion().getCodigo();
				RegionBean regionBean = RegionBean.emptyBean();
				regionBean = this.findRegionById(regionId);
	
				dirContactoBean.setRegion(regionBean);
	
				dirContactoBean.setCiudad(new CiudadBean(usuarioBean
								.getDireccionContacto().getCiudad().getCodigo(),
								usuarioBean.getDireccionContacto().getCiudad()
										.getCodigo()));
				
				dirContactoBean.setComuna(new ComunaBean(usuarioBean
								.getDireccionContacto().getComuna().getCodigo(),
								usuarioBean.getDireccionContacto().getComuna()
										.getCodigo()));
				
				dirContactoBean.setCalle(usuarioBean.getDireccionContacto().getCalle());
				dirContactoBean.setNumero(usuarioBean.getDireccionContacto().getNumero());
				dirContactoBean.setDepartamento(usuarioBean.getDireccionContacto().getDepartamento());
				usuarioBean.setDireccionContacto(dirContactoBean);
			}
			LOGGER.info("Usuario BEAN Confirmacion: "+this.usuarioBean.getEmail()+", "+this.usuarioBean.getPrefijoTelefonoAdicional()+"-"+this.usuarioBean.getTelefonoAdicional());
			this.configurarDescripciones(usuarioBean);

			postback = "formularioConfirmar";
			
		} catch (Exception e) {
			LOGGER.error(
					"No se pudo confirmar la informacion ingresada para luego "
					+ "ser direccionado al formulario de confirmacion, rutUsuario : "
					+ usuarioBean.getRut(), e);
		}
		return postback;
	}

	/**
	 * Metodo utilitario para obtencion de la region a partir del id.<br>
	 * Este metodo se apoya en {@link ParametrosDAO}.<br>
	 * 
	 * @see ParametrosDAO#findRegionById(String)
	 * @param regionId
	 * @return
	 * @throws DAOException
	 * @throws ServiceException
	 */
	private RegionBean findRegionById(String regionId) throws DAOException,
			ServiceException {
		return parametrosDAO.findRegionById(regionId);
	}

	/**
	 * Metodo que registra la informacion del usuario
	 * 
	 * @return
	 */
	public String registrarUsuario() {

		try {
			usuarioBean.setRut(RutBean.parseRut(this.getRutUsuario()));

			// Suscribir a facturacion electronica
			if (suscripcionFactElectronica) {
				try {
					facturacionElectronicaDelegate
							.inscribirServicioFacturacionElectronica(
									usuarioBean.getRut().getStringValue(), this.nmroCuentaTitular,
									usuarioBean.getEmail(), usuarioBean
											.getNumeroPCS());
				} catch (DAOException e) {
					LOGGER
							.error(
									"DAOException Error al registrar facturacion electronica del usuario",
									e);
					JSFMessagesHelper.addServiceErrorMessage(
							"registrarUsuario", new String[] { this.usuarioBean
									.getNumeroPCS() });
				} catch (ServiceException e) {
					LOGGER
							.info("ServiceException caught al registrar facturacion electronica del usuario");
					JSFMessagesHelper.addErrorCodeMessage("gestionDePerfiles",
							e.getCodigoRespuesta(), new String[] { "NumeroPcs "
									+ this.usuarioBean.getNumeroPCS() });
				} catch (Exception e) {
					LOGGER.error(
							"Exception no esperada al registrar facturacion electronica del usuario"
									+ this.usuarioBean.getNumeroPCS(), e);
					JSFMessagesHelper.addServiceErrorMessage(
							"registrarUsuario", new String[] { this.usuarioBean
									.getNumeroPCS() });
				}
			}

			inscripcionDelegate.actualizarDatos(this.usuarioBean);

			//Obtenemos contexto desde la sesion
			String contexto = (String)JSFPortletHelper.getRequest().getSession()
						.getAttribute(CONTEXTO_ATTR_NAME);

			if (Utils.isEmptyString(contexto)) {
				LOGGER.info("Usuario NO ingreso por login de contexto");
				this.direccionarDashBoard(this.usuarioBean.getNumeroPCS());
			} else {
				LOGGER.info("Usuario ingreso por login de contexto");
				this.direccionarContexto(this.usuarioBean.getNumeroPCS(),contexto);
			}

		} catch (DAOException e) {
			LOGGER.error("DAOException Error al registrar datos de Usuario", e);
			JSFMessagesHelper.addServiceErrorMessage("registrarUsuario",
					new String[] { this.usuarioBean.getNumeroPCS() });
		} catch (ServiceException e) {
			LOGGER.info("ServiceException caught al registrar datos de Usuario");
			JSFMessagesHelper.addErrorCodeMessage("gestionDePerfiles", e
					.getCodigoRespuesta(), new String[] { "NumeroPcs "
					+ this.usuarioBean.getNumeroPCS() });
		} catch (Exception e) {

			LOGGER.error("Exception no esperada al registrar datos del usuario"
					+ this.usuarioBean.getNumeroPCS(), e);
			JSFMessagesHelper.addServiceErrorMessage("registrarUsuario",
					new String[] { this.usuarioBean.getNumeroPCS() });

		}

		return "formularioConfirmar";
	}

	/**
	 * Actualiza las descripciones de los combos y radios del formulario
	 * 
	 * @param usuarioBean
	 */
	private void configurarDescripciones(UsuarioBean usuarioBean) {
		try {
			usuarioBean.setSexoDesc(ParametrosHelper.getCodeDescBeanByTypeId(
					"sexo", usuarioBean.getSexo()).getDescripcion());

//			usuarioBean.setEstadoCivilDesc(ParametrosHelper
//					.getCodeDescBeanByTypeId("estadoCivil",
//							usuarioBean.getEstadoCivil()).getDescripcion());

//			usuarioBean.setHijosDesc(ParametrosHelper.getCodeDescBeanByTypeId(
//					"hijo", usuarioBean.getHijos()).getDescripcion());

//			usuarioBean.setEstudioDesc(ParametrosHelper
//					.getCodeDescBeanByTypeId("nivelEstudios",
//							usuarioBean.getEstudio()).getDescripcion());

//			usuarioBean
//					.setActividadLaboralDesc(ParametrosHelper
//							.getCodeDescBeanByTypeId("actividad",
//									usuarioBean.getActividadLaboral())
//							.getDescripcion());

//			usuarioBean.setAreaLaboralDesc(ParametrosHelper
//					.getCodeDescBeanByTypeId("areaLaboral",
//							usuarioBean.getAreaLaboral()).getDescripcion());

			usuarioBean.setRelacionTitularDesc(ParametrosHelper
					.getCodeDescBeanByTypeId("relacionTitular",
							usuarioBean.getRelacionTitular()).getDescripcion());

		} catch (Exception e) {
			LOGGER.error("No se pudo configurar las descripciones de las propiedades :"
					+ usuarioBean.getRut(), e);
		}
	}

	/**
	 * 
	 * @return
	 */
	public String getTipoInscripcion() {
		return tipoInscripcion;
	}

	/**
	 * 
	 * @param tipoInscripcion
	 */
	public void setTipoInscripcion(String tipoInscripcion) {
		this.tipoInscripcion = tipoInscripcion;
	}

	/**
	 * Metodo utilitario que redirecciona al dashboard correspondiente al
	 * mercado del usuario.
	 * 
	 * @param numeroPCS
	 */
	private void direccionarDashBoard(String numeroPCS) {

		try {
			HttpServletRequest servletRequest = JSFPortletHelper
					.getRequest(FacesContext.getCurrentInstance());

			HttpServletResponse servletResponse = JSFPortletHelper
					.getResponse();

			// Obtener el IDP del Usuario
			SeguridadDelegate seguridadDelegate = new SeguridadDelegate();
			String id_session = seguridadDelegate.consultarIDP(numeroPCS);

			InscripcionHelper.redirectApp(servletRequest, servletResponse, this
					.getTipoInscripcion(), id_session);

		} catch (DAOException e) {
			LOGGER.error("Exception al intentar obtener el IDP correspondiente al movil "
					+ numeroPCS);
			JSFMessagesHelper.addServiceErrorMessage("noDisponible");
		} catch (ServiceException e) {
			LOGGER.error("Error de servicio al intentar obtener el IDP correspondiente al movil "
					+ numeroPCS);
			JSFMessagesHelper.addServiceErrorMessage(e.getCodigoRespuesta());
		} catch (Exception e) {
			LOGGER.error("Exception al intentar obtener el IDP correspondiente al movil "
					+ numeroPCS);
			JSFMessagesHelper.addServiceErrorMessage("noDisponible");
		}

	}
	
	/**
	 * Metodo utilitario que redirecciona al dashboard correspondiente al
	 * mercado del usuario.
	 * 
	 * @param numeroPCS
	 */
	private void direccionarContexto(String numeroPCS, String contexto) {

		try {
			HttpServletRequest servletRequest = JSFPortletHelper
					.getRequest(FacesContext.getCurrentInstance());

			HttpServletResponse servletResponse = JSFPortletHelper
					.getResponse();

			// Obtener el IDP del Usuario
			SeguridadDelegate seguridadDelegate = new SeguridadDelegate();
			String id_session = seguridadDelegate.consultarIDP(numeroPCS);

			InscripcionHelper.redirectAppContext(servletRequest, servletResponse, this
					.getTipoInscripcion(), id_session, contexto);

		} catch (DAOException e) {
			LOGGER.error("Exception al intentar obtener el IDP correspondiente al movil "
					+ numeroPCS);
			JSFMessagesHelper.addServiceErrorMessage("noDisponible");
		} catch (ServiceException e) {
			LOGGER.error("Error de servicio al intentar obtener el IDP correspondiente al movil "
					+ numeroPCS);
			JSFMessagesHelper.addServiceErrorMessage(e.getCodigoRespuesta());
		} catch (Exception e) {
			LOGGER.error("Exception al intentar obtener el IDP correspondiente al movil "
					+ numeroPCS);
			JSFMessagesHelper.addServiceErrorMessage("noDisponible");
		}

	}

	/**
	 * Metodo utilitario que retorna un valor booleano, es utilizado para
	 * verificar si existe en la url el parametro de inscripcion
	 * 
	 * @return true o false
	 */
	public boolean isParametroUrlInscripcion() {
		return InscripcionHelper.getParametroUrlInscripcion();
	}

	/**
	 * 
	 * @return
	 */
	public boolean isRedireccionarHomeEntel() {
		return redireccionarHomeEntel;
	}

	/**
	 * 
	 * @param redireccionarHomeEntel
	 */
	public void setRedireccionarHomeEntel(boolean redireccionarHomeEntel) {
		this.redireccionarHomeEntel = redireccionarHomeEntel;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getUrlHomeEntel(){
        return InscripcionHelper.getUrlHomeEntel();
    }
	public int getMaximoRutEnrolamiento() {
		return maximoRutEnrolamiento;
	}
	
	public void setMaximoRutEnrolamiento(int maximoRutEnrolamiento) {
		this.maximoRutEnrolamiento = maximoRutEnrolamiento;
	}
	
	public int getRutNumerico() {
		return rutNumerico;
	}
	
	public void setRutNumerico(int rutNumerico) {
		this.rutNumerico = rutNumerico;
	}
	
	public boolean isSuscripcionFactElectronica() {
		return suscripcionFactElectronica;
	}

	public void setSuscripcionFactElectronica(boolean suscripcionFactElectronica) {
		this.suscripcionFactElectronica = suscripcionFactElectronica;
	}

	public String getMercado() {
		return mercado;
	}

	public void setMercado(String mercado) {
		this.mercado = mercado;
	}
	
	public boolean isMercadoPrepago(){		
		mercadoPrepago = false;		
		if (Utils.isNotEmptyString(this.mercado)) {
			if (this.mercado.equalsIgnoreCase("PP")){
				mercadoPrepago = true;
			}
		}
		return mercadoPrepago;
	}


	public CuentaClienteBean getCuentaClienteBean() {
		return cuentaClienteBean;
	}

	public void setCuentaClienteBean(CuentaClienteBean cuentaClienteBean) {
		this.cuentaClienteBean = cuentaClienteBean;
	}
	
	public boolean isRutTitular(){
		return rutTitular;
	}

	public boolean isInscritoFacturacionElectronica() {
		return inscritoFacturacionElectronica;
	}

	public void setInscritoFacturacionElectronica(
			boolean inscritoFacturacionElectronica) {
		this.inscritoFacturacionElectronica = inscritoFacturacionElectronica;
	}
	
	public String getMsjTerminosRegistro() {
		try {
			return JSFPortletHelper.getNodePropertyBinaryValueAsString("idContenido", "msjTerminosRegistro", "html");
		} catch (Exception e) {
			return "";
		}
	}
}
