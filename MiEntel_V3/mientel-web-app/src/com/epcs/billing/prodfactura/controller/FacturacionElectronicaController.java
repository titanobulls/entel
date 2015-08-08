/* Propiedad de Entel PCS. Todos los derechos reservados */
package com.epcs.billing.prodfactura.controller;

import java.util.Date;

import javax.faces.event.PhaseEvent;
import org.apache.log4j.Logger;
import com.bea.content.Node;
import com.bea.p13n.usermgmt.profile.ProfileWrapper;
import com.epcs.bean.ComunaBean;
import com.epcs.bean.DireccionBean;
import com.epcs.bean.FacturacionElectronicaBean;
import com.epcs.bean.InformacionTitularBean;
import com.epcs.bean.RutBean;
import com.epcs.bean.UsuarioBean;
import com.epcs.billing.prodfactura.delegate.FacturacionElectronicaDelegate;
import com.epcs.billing.prodfactura.util.FacturaElectronicaHelper;
import com.epcs.cliente.perfil.controller.CuentaController;
import com.epcs.cliente.perfil.delegate.CuentaDelegate;
import com.epcs.recursoti.configuracion.DateHelper;
import com.epcs.recursoti.configuracion.JsonHelper;
import com.epcs.recursoti.configuracion.MiEntelBusinessHelper;
import com.epcs.recursoti.configuracion.MiEntelProperties;
import com.epcs.recursoti.configuracion.jsf.converter.RutConverter;
import com.epcs.recursoti.configuracion.jsf.utils.JSFMessagesHelper;
import com.epcs.recursoti.configuracion.jsf.utils.JSFPortletHelper;
import com.epcs.recursoti.configuracion.uup.ProfileWrapperHelper;
import com.epcs.recursoti.excepcion.DAOException;
import com.epcs.recursoti.excepcion.ServiceException;

/**
 * @author rmesino (I2B) en nombre de Absalon Opazo (Atencion al Cliente, EntelPcs)
 *
 */
public class FacturacionElectronicaController {
    
    private static final Logger LOGGER = Logger
    .getLogger(FacturacionElectronicaController.class);

    private FacturacionElectronicaBean facturacionElectronicaBean;
    
    private InformacionTitularBean infoTitularBean;
    
    private FacturacionElectronicaDelegate facturacionElectronicaDelegate;
    
    private String correoFE;
    
    private boolean estadoInscrito;
    
    private String fechaActual;
    
    private String aaa;
    
    private String autorizacionVoluntariaContent; 
    
    private String confirmarDesinscribir;
    
    private CuentaDelegate cuentaDelegate;
    
    private String contratoFENoInscritoJSON;
    
    private String motivoDesinscripcion;
    
    private UsuarioBean usuarioBean;
    
    private boolean nuevoInscritoFE = false;
    
//    private String mensajeUsuarioNoAutorizado;

	public FacturacionElectronicaController(){
    	cuentaDelegate = new CuentaDelegate();
    }
    
    public String getContratoFENoInscritoJSON() {
		return contratoFENoInscritoJSON;
	}

	public void setContratoFENoInscritoJSON(String contratoFENoInscritoJSON) {
		this.contratoFENoInscritoJSON = contratoFENoInscritoJSON;
	}

	public void init(PhaseEvent event) {               
        //if( event.getPhaseId() == PhaseId.RENDER_RESPONSE ){
            loadData();
        //}
    }
    
    private void loadData(){ 
    	
    	String numeroPcs = "";    
        String rutTitular = "";
        String nroCuenta = "";
        String nombreUsuario = "";
        String rutUsuario = "";
        String rutTitularFormatted = "";
        String rutUsuarioFormatted = "";
        
    	try{   
    		
    		ProfileWrapper profileWrapper = ProfileWrapperHelper.getProfile(JSFPortletHelper.getRequest());
    		
    		//Seteamos variables de sesion
    		numeroPcs = ProfileWrapperHelper.getPropertyAsString(profileWrapper,"numeroPcsSeleccionado");
    		rutTitular = ProfileWrapperHelper.getPropertyAsString(profileWrapper,"rutTitular");
    		nroCuenta = ProfileWrapperHelper.getPropertyAsString(profileWrapper,"numeroCuenta");
    		nombreUsuario = ProfileWrapperHelper.getPropertyAsString(profileWrapper,"nombreUsuario");
    		rutUsuario = ProfileWrapperHelper.getPropertyAsString(profileWrapper,"rutTitular");
    		aaa = ProfileWrapperHelper.getPropertyAsString(profileWrapper,"aaa");
    		
            /*
        	 * Aplicamos el formato '#.###.###-#' al rutTitular y rutUsuario.
        	 * Ejemplo: 1.000.502-7
        	 */
        	RutConverter converter = new RutConverter();
        	rutTitularFormatted = converter.getAsString(null, null, RutBean.parseRut(rutTitular));        	
        	rutUsuarioFormatted = converter.getAsString(null, null, RutBean.parseRut(rutUsuario));
        	
        	String idContenido = MiEntelProperties.getProperty("parametros.facturacionElectronica.autorizacionVoluntaria.idContenido"); 
            Node mensajeServicio = JSFPortletHelper.getContentNode("idContenido", idContenido); 
            autorizacionVoluntariaContent = mensajeServicio.getProperty("html").getValue().getStringValue();
            
            idContenido = MiEntelProperties.getProperty("parametros.facturacionElectronica.confirmarDesinscribir.idContenido");
        	mensajeServicio = JSFPortletHelper.getContentNode("idContenido", idContenido);
        	confirmarDesinscribir = mensajeServicio.getProperty("html").getValue().getStringValue().replace("{cuenta}", nroCuenta);
        	
        	//Invocamos al servicio ObtenerRut
            infoTitularBean = cuentaDelegate.obtenerInformacionTitular(numeroPcs);
            	
        		autorizacionVoluntariaContent = autorizacionVoluntariaContent
        			.replace("{nombreTitularCompleto}", infoTitularBean.getNombreCompletoTitular())
        			.replace("{rutTitular}", rutTitularFormatted)
        			.replace("{domicilioTitular}", infoTitularBean.getDireccionTitular())
        			.replace("{comunaTitular}", infoTitularBean.getComunaTitular())
        			.replace("{cuenta}", nroCuenta)
        			.replace("{fechaActual}", getFechaActual())
        			.replace("{nombreUsuarioCompleto}", nombreUsuario)
        			.replace("{rutUsuario}", rutUsuarioFormatted);
     		
        	try {

				facturacionElectronicaBean = facturacionElectronicaDelegate.getFacturacionElectronica(rutTitular, nroCuenta);
				
	            autorizacionVoluntariaContent = autorizacionVoluntariaContent.replace("{email}", facturacionElectronicaBean.getCorreoFactura());

	            estadoInscrito = ( FacturaElectronicaHelper.isInscrita(facturacionElectronicaBean.getEstadoFE()) || 
	                    FacturaElectronicaHelper.isInscritaModificada(facturacionElectronicaBean.getEstadoFE()));		

	            LOGGER.info("Estado inscrito del cliente: "+estadoInscrito);

			} catch (DAOException e) {
				LOGGER.error("DAOException al cargar informacion", e);
				facturacionElectronicaBean = null;
				JSFMessagesHelper.addErrorCodeMessage("general", "0001");
			} catch (ServiceException e) {
				LOGGER.error("ServiceException al cargar informacion. msisdn: " + numeroPcs);
				facturacionElectronicaBean = null;
				autorizacionVoluntariaContent = autorizacionVoluntariaContent .replace("{email}", "<span id='espacio-correofe-contrato'></span>");
				JSFMessagesHelper.addErrorCodeMessage("factelect", e.getCodigoRespuesta());
			}

			
		try{
            //Cargar informacion de usuario            
			this.usuarioBean = this.cuentaDelegate.obtenerUsuario(numeroPcs, rutTitular);
            
			if (MiEntelBusinessHelper.isAAATitular(this.usuarioBean.getAaa())){
				
				DireccionBean direccionFactura = null;

				direccionFactura = this.cuentaDelegate.obtenerDireccionFactura(rutTitular, nroCuenta);
				
				if(direccionFactura == null){
					
					direccionFactura = new DireccionBean();
					direccionFactura.setComuna(new ComunaBean("", ""));
				}
				
				this.usuarioBean.setDireccionFactura(direccionFactura);
			
			} else {
				
				DireccionBean direccionFactura = new DireccionBean();
				direccionFactura.setComuna(new ComunaBean("", ""));
				this.usuarioBean.setDireccionFactura(direccionFactura);
			
			}
        } catch (ServiceException e) {
			
        	LOGGER.error("ServiceException al cargar informacion. msisdn: " + numeroPcs);
        	JSFMessagesHelper.addErrorCodeMessage("general", "0001");
        	
        }catch (Exception e) {
        	LOGGER.error("Exception inesperada al cargar informacion", e);
            JSFMessagesHelper.addErrorCodeMessage("general", "0001");
        }
         	
    	}catch (Exception e) {
    			// TODO: handle exception
    		
            	LOGGER.error("El type ObtenerRutTitular del servicio ClienteContactoService ha fallado... ",e);
            	     
            	infoTitularBean = null;
            	facturacionElectronicaBean = null;
    		}
    }
        
    /**
     * 
     * @throws DAOException
     * @throws ServiceException
     * @throws Exception
     */
    public String actualizarServicioFacturacionElectronica() {
    	String numeroPcsSeleccionado ="";
        try{
            
            ProfileWrapper profileWrapper = ProfileWrapperHelper.getProfile(JSFPortletHelper.getRequest());
            numeroPcsSeleccionado = ProfileWrapperHelper.getPropertyAsString(profileWrapper,"numeroPcsSeleccionado");
            String rutTitular = ProfileWrapperHelper.getPropertyAsString(profileWrapper,"rutTitular");
            String nroCuenta = ProfileWrapperHelper.getPropertyAsString(profileWrapper,"numeroCuenta");
            
            facturacionElectronicaDelegate.actualizarServicioFacturacionElectronica(
                        rutTitular, nroCuenta, correoFE, numeroPcsSeleccionado);
            
            // Actualizacion de email de usuario en la BUIC
            try {
            	String[] email = correoFE.split("@");            	
            	cuentaDelegate.actualizarCorreoBuzon(numeroPcsSeleccionado, rutTitular, email[0], email[1]);
				
				/**
				 * Actualizacion de email de usuario en la instancia de
				 * CuentaController que esta en la sesion
				 */
            	CuentaController cuentaController = (CuentaController) JSFPortletHelper
						.getFacesBeanInstanceFromSession("cuentaController");
				cuentaController.getUsuario().setEmail(correoFE);
				JSFPortletHelper.setFacesBeanInstanceToSession("cuentaController", cuentaController);
            } catch (DAOException e) {
            	LOGGER.error("DAOException al intentar actualizar email del usuario en la BUIC", e);
            } catch (ServiceException e) {            	
            	LOGGER.error("ServiceException al intentar actualizar email del usuario en la BUIC", e);
            } catch (Exception e) {            	
            	LOGGER.error("Exception al intentar actualizar email del usuario en la BUIC", e);
            }
            
            nuevoInscritoFE = false;
        }catch (DAOException e) {  
        	LOGGER.error("DAOException al intentar actualizar factura electronica", e);
            facturacionElectronicaBean = null;
            JSFMessagesHelper.addErrorCodeMessage("general", "0001");
        }
        catch (ServiceException e) {
        	LOGGER.info("ServiceException al cargar informacion. msisdn: " + numeroPcsSeleccionado);
            facturacionElectronicaBean = null;
            JSFMessagesHelper.addErrorCodeMessage("factelect", e.getCodigoRespuesta());
        }
        catch (Exception e) {
        	LOGGER.error("Exception inesperada al intentar actualizar factura electronica", e);
            facturacionElectronicaBean = null;
            JSFMessagesHelper.addErrorCodeMessage("general", "0001");
        }
        
        return "success";
        
    }
    
    public String autorizacionVoluntaria(){
    	return "autorizacionVoluntaria";
    }
    
    public String autorizacionVoluntariaNoInscrito(){    	
    	return "autorizacionVoluntaria";
    }
    
    
    /**
     * 
     * @throws DAOException
     * @throws ServiceException
     * @throws Exception
     */
    
    public String inscribirServicioFacturacionElectronica(){
    	String numeroPcsSeleccionado = "";        
        try{
            
            ProfileWrapper profileWrapper = ProfileWrapperHelper.getProfile(JSFPortletHelper.getRequest());
            numeroPcsSeleccionado = ProfileWrapperHelper.getPropertyAsString(profileWrapper,"numeroPcsSeleccionado");
            String rutTitular = ProfileWrapperHelper.getPropertyAsString(profileWrapper,"rutTitular");
            String nroCuenta = ProfileWrapperHelper.getPropertyAsString(profileWrapper,"numeroCuenta");
            
            
        	facturacionElectronicaDelegate.inscribirServicioFacturacionElectronica(
                    rutTitular, nroCuenta, correoFE, numeroPcsSeleccionado);      
            
        	nuevoInscritoFE = true;

            /**
             * Actualizacion de email de usuario en la instancia de
             * CuentaController que esta en la sesion
             */
            CuentaController cuentaController = (CuentaController) JSFPortletHelper
                    .getFacesBeanInstanceFromSession("cuentaController");
            cuentaController.getUsuario().setEmail(correoFE);
            JSFPortletHelper.setFacesBeanInstanceToSession("cuentaController", cuentaController);
        }catch (DAOException e) {
        	LOGGER.error("DAOException al intentar inscribir factura electronica", e);
            facturacionElectronicaBean = null;
            JSFMessagesHelper.addErrorCodeMessage("general", "0001");
        }
        catch (ServiceException e) {
        	LOGGER.info("ServiceException al intentar inscribir factura electronica. msisdn: "+numeroPcsSeleccionado);
            facturacionElectronicaBean = null;
            JSFMessagesHelper.addErrorCodeMessage("factelect", e.getCodigoRespuesta());
        }
        catch (Exception e) {
        	LOGGER.error("Exception inesperada al intentar inscribir factura electronica", e);
            facturacionElectronicaBean = null;
            JSFMessagesHelper.addErrorCodeMessage("general", "0001");
        }
        
        return "success";
        
    }
    
    public String cancelarServicioFacturacionElectronica(){
    	String rutTitular = "";
    	String respuestaJSON = "";
    	
        try{
            ProfileWrapper profileWrapper = ProfileWrapperHelper.getProfile(JSFPortletHelper.getRequest());
            rutTitular = ProfileWrapperHelper.getPropertyAsString(profileWrapper,"rutTitular");
            String nroCuenta = ProfileWrapperHelper.getPropertyAsString(profileWrapper,"numeroCuenta");
            facturacionElectronicaDelegate.cancelarServicioFacturacionElectronica(rutTitular, nroCuenta, (motivoDesinscripcion!=null && !motivoDesinscripcion.equals("")) ? motivoDesinscripcion : "1");
            respuestaJSON = JsonHelper.toJsonResponse("Factura electronica cancelada");
        }catch (DAOException e) {
        	LOGGER.error("DAOException al intentar cancelar factura electronica", e);
            facturacionElectronicaBean = null;
            JSFMessagesHelper.addErrorCodeMessage("general", "0001");
            respuestaJSON = JsonHelper.toJsonErrorMessage("Error al intentar cancelar factura electronica");
        }
        catch (ServiceException e) {
        	LOGGER.info("ServiceException al intentar cancelar factura electronica. rutTitular: "+rutTitular);
            facturacionElectronicaBean = null;
            JSFMessagesHelper.addErrorCodeMessage("factelect", e.getCodigoRespuesta());
            respuestaJSON = JsonHelper.toJsonErrorMessage("Error al intentar cancelar factura electronica");
        }
        catch (Exception e) {
        	LOGGER.error("Exception inesperada al intentar cancelar factura electronica", e);
            facturacionElectronicaBean = null;
            JSFMessagesHelper.addErrorCodeMessage("general", "0001");
            respuestaJSON = JsonHelper.toJsonErrorMessage("Error al intentar cancelar factura electronica");
        }
        
        return respuestaJSON;
        
    }
        
    public String getCancelarFE() {
    	return cancelarServicioFacturacionElectronica();
    }    
    
    /**
     * @return the facturacionElectronicaDelegate
     */
    public FacturacionElectronicaDelegate getFacturacionElectronicaDelegate() {
        return facturacionElectronicaDelegate;
    }

    /**
     * @param facturacionElectronicaDelegate the facturacionElectronicaDelegate to set
     */
    public void setFacturacionElectronicaDelegate(
            FacturacionElectronicaDelegate facturacionElectronicaDelegate) {
        this.facturacionElectronicaDelegate = facturacionElectronicaDelegate;
    }
    
    
    //SC 05/08 Luis
    public InformacionTitularBean getInfoTitularBean() {
		return infoTitularBean;
	}

	public void setInfoTitularBean(InformacionTitularBean infoTitularBean) {
		this.infoTitularBean = infoTitularBean;
	}
	//FIN SC

	/**
     * @return the facturacionElectronicaBean
     */
    public FacturacionElectronicaBean getFacturacionElectronicaBean() {
        return facturacionElectronicaBean;
    }

    /**
     * @param facturacionElectronicaBean the facturacionElectronicaBean to set
     */
    public void setFacturacionElectronicaBean(
            FacturacionElectronicaBean facturacionElectronicaBean) {
        this.facturacionElectronicaBean = facturacionElectronicaBean;
    }

    /**
     * @return the estadoInscrito
     */
    public boolean isEstadoInscrito() {
        return estadoInscrito;
    }

    /**
     * @param estadoInscrito the estadoInscrito to set
     */
    public void setEstadoInscrito(boolean estadoInscrito) {
        this.estadoInscrito = estadoInscrito;
    }

    /**
     * @return the correoFE
     */
    public String getCorreoFE() {
        return correoFE;
    }

    /**
     * @param correoFE the correoFE to set
     */
    public void setCorreoFE(String correoFE) {
        this.correoFE = correoFE;
    }

    /**
     * @return the fechaActual
     */
    public String getFechaActual() {
        if( fechaActual == null ){
            fechaActual = DateHelper.format(new Date(), "dd MMMM yyyy");
        }
        return fechaActual;
    }

    /**
     * @param fechaActual the fechaActual to set
     */
    public void setFechaActual(String fechaActual) {
        this.fechaActual = fechaActual;
    }

    /**
     * @return the aAA
     */
    public String getAAA() {
        return aaa;
    }

    /**
     * @param aAA the aAA to set
     */
    public void setAAA(String aAA) {
        aaa = aAA;
    }

    public String getAutorizacionVoluntariaContent() {    	
		return autorizacionVoluntariaContent;
	}

	public void setAutorizacionVoluntariaContent(String autorizacionVoluntariaContent) {
		this.autorizacionVoluntariaContent = autorizacionVoluntariaContent;
	}

	public String getConfirmarDesinscribir() {
		return confirmarDesinscribir;
	}

	public void setConfirmarDesinscribir(String confirmarDesinscribir) {
		this.confirmarDesinscribir = confirmarDesinscribir;
	}

	/**
     * @return the mensajeUsuarioNoAutorizado
     */
    public String getMensajeUsuarioNoAutorizado() {
        String mensaje = "";
        try{
         mensaje = MiEntelProperties
        .getProperty("solo.usuario.titular");
        }catch (Exception e) {
            LOGGER.error("Erro al obtener propiedad [solo.usuario.tituar]",e);
        }
        return mensaje;        
    }
    
    public String getMotivoDesinscripcion() {
		return motivoDesinscripcion;
	}
    
    public void setMotivoDesinscripcion(String motivoDesinscripcion) {
		this.motivoDesinscripcion = motivoDesinscripcion;
	}

//    /**
//     * @param mensajeUsuarioNoAutorizado the mensajeUsuarioNoAutorizado to set
//     */
//    public void setMensajeUsuarioNoAutorizado(String mensajeUsuarioNoAutorizado) {
//        this.mensajeUsuarioNoAutorizado = mensajeUsuarioNoAutorizado;
//    }  
    
	public String getPageLabelMisDatos() {
		try {
			ProfileWrapper profileWrapper = ProfileWrapperHelper
					.getProfile(JSFPortletHelper.getRequest());
			String mercado = ProfileWrapperHelper.getPropertyAsString(
					profileWrapper, "mercado").toLowerCase();
			String flagBam = ProfileWrapperHelper.getPropertyAsString(
					profileWrapper, "flagBam");

			String pageLabel = mercado + "_misdatos_page";
			if (MiEntelBusinessHelper.isUserBam(flagBam)) {
				return "bam" + pageLabel;
			} else {
				return pageLabel;
			}
		} catch (Exception e) {
			return "";
		}
	}

	public UsuarioBean getUsuarioBean() {
		return usuarioBean;
	}

	public void setUsuarioBean(UsuarioBean usuarioBean) {
		this.usuarioBean = usuarioBean;
	}

	public boolean isNuevoInscritoFE() {
		return nuevoInscritoFE;
	}

	public void setNuevoInscritoFE(boolean nuevoInscritoFE) {
		this.nuevoInscritoFE = nuevoInscritoFE;
	}
}
