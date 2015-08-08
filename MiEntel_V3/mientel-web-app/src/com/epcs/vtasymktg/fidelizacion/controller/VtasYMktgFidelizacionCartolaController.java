package com.epcs.vtasymktg.fidelizacion.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.faces.event.PhaseEvent;
import javax.faces.model.SelectItem;
import javax.portlet.PortletPreferences;

import org.apache.log4j.Logger;

import com.bea.p13n.usermgmt.profile.ProfileWrapper;
import com.epcs.bean.CodeDescBean;
import com.epcs.bean.DetalleBean;
import com.epcs.bean.ExpiracionProximaPuntosBean;
import com.epcs.bean.PaginaHistorialCanjeBean;
import com.epcs.bean.PuntosBean;
import com.epcs.bean.ResultadoConsultarPuntosBean;
import com.epcs.bean.RutBean;
import com.epcs.recursoti.configuracion.JsonHelper;
import com.epcs.recursoti.configuracion.MiEntelProperties;
import com.epcs.recursoti.configuracion.ParametrosHelper;
import com.epcs.recursoti.configuracion.Utils;
import com.epcs.recursoti.configuracion.jsf.utils.JSFMessagesHelper;
import com.epcs.recursoti.configuracion.jsf.utils.JSFPortletHelper;
import com.epcs.recursoti.configuracion.jsf.utils.JsfUtil;
import com.epcs.recursoti.configuracion.uup.ProfileWrapperHelper;
import com.epcs.recursoti.excepcion.DAOException;
import com.epcs.recursoti.excepcion.ServiceException;
import com.epcs.vtasymktg.fidelizacion.delegate.VtasYMktgFidelizacionDelegate;

public class VtasYMktgFidelizacionCartolaController {
	
	/**
     * Logger para VtasYMktgFidelizacionController
     */
    private static final Logger LOGGER = Logger
            .getLogger(VtasYMktgFidelizacionController.class);
    
    private static final long serialVersionUID = 1L;
    private VtasYMktgFidelizacionDelegate vtasYMktgFidelizacionDelegate;
    private String categoriaClientePuntos;
    private Integer periodoHistorialSelector;
    private boolean existenRegistrosHist = false;
    private DetalleBean detalleHistorial;
    private PuntosBean detallePuntos;
    private String historialJSON;
    
    private List<ExpiracionProximaPuntosBean> listaExpiracion;  
    private SelectItem[] tiposHistorialList;
    private List<SelectItem> aniosHistorialList;               
    
    
	/**
	 * @return the historialJSON
	 */
	public String getHistorialJSON() {
		return historialJSON;
	}

	/**
	 * @param historialJSON the historialJSON to set
	 */
	public void setHistorialJSON(String historialJSON) {
		this.historialJSON = historialJSON;
	}

	/**
	 * @return the listaExpiracion
	 */
	public List<ExpiracionProximaPuntosBean> getListaExpiracion() {
		return listaExpiracion;
	}

	/**
	 * @param listaExpiracion the listaExpiracion to set
	 */
	public void setListaExpiracion(List<ExpiracionProximaPuntosBean> listaExpiracion) {
		this.listaExpiracion = listaExpiracion;
	}

	/**
	 * @return the tiposHistorialList
	 */
	public SelectItem[] getTiposHistorialList() {
		return tiposHistorialList;
	}

	/**
	 * @param tiposHistorialList the tiposHistorialList to set
	 */
	public void setTiposHistorialList(SelectItem[] tiposHistorialList) {
		this.tiposHistorialList = tiposHistorialList;
	}

	/**
	 * @return the aniosHistorialList
	 */
	public List<SelectItem> getAniosHistorialList() {
		return aniosHistorialList;
	}

	/**
	 * @param aniosHistorialList the aniosHistorialList to set
	 */
	public void setAniosHistorialList(List<SelectItem> aniosHistorialList) {
		this.aniosHistorialList = aniosHistorialList;
	}

	/**
	 * @return the vtasYMktgFidelizacionDelegate
	 */
	public VtasYMktgFidelizacionDelegate getVtasYMktgFidelizacionDelegate() {
		return vtasYMktgFidelizacionDelegate;
	}

	/**
	 * @param vtasYMktgFidelizacionDelegate the vtasYMktgFidelizacionDelegate to set
	 */
	public void setVtasYMktgFidelizacionDelegate(
			VtasYMktgFidelizacionDelegate vtasYMktgFidelizacionDelegate) {
		this.vtasYMktgFidelizacionDelegate = vtasYMktgFidelizacionDelegate;
	}

	/**
	 * @return the periodoHistorialSelector
	 */
	public Integer getPeriodoHistorialSelector() {
		return periodoHistorialSelector;
	}

	/**
	 * @param periodoHistorialSelector the periodoHistorialSelector to set
	 */
	public void setPeriodoHistorialSelector(Integer periodoHistorialSelector) {
		this.periodoHistorialSelector = periodoHistorialSelector;
	}

	/**
	 * @return the existenRegistrosHist
	 */
	public boolean isExistenRegistrosHist() {
		return existenRegistrosHist;
	}

	/**
	 * @param existenRegistrosHist the existenRegistrosHist to set
	 */
	public void setExistenRegistrosHist(boolean existenRegistrosHist) {
		this.existenRegistrosHist = existenRegistrosHist;
	}

	/**
	 * @return the detalleHistorial
	 */
	public DetalleBean getDetalleHistorial() {
		return detalleHistorial;
	}

	/**
	 * @param detalleHistorial the detalleHistorial to set
	 */
	public void setDetalleHistorial(DetalleBean detalleHistorial) {
		this.detalleHistorial = detalleHistorial;
	}

	/**
	 * @return the detallePuntos
	 */
	public PuntosBean getDetallePuntos() {
		return detallePuntos;
	}

	/**
	 * @param detallePuntos the detallePuntos to set
	 */
	public void setDetallePuntos(PuntosBean detallePuntos) {
		this.detallePuntos = detallePuntos;
	}

	/**
	 * @return the categoriaClientePuntos
	 */
	public String getCategoriaClientePuntos() {
		return categoriaClientePuntos;
	}

	/**
	 * @param categoriaClientePuntos the categoriaClientePuntos to set
	 */
	public void setCategoriaClientePuntos(String categoriaClientePuntos) {
		this.categoriaClientePuntos = categoriaClientePuntos;
	}

	/**
     * Invoca los action method que inicializan la tabla de historial y el listado 
     * de periodos para parametrizar la cantidad de datos a ver en dicha tabla.
     * Este metodo llena la tabla teniendo en cuenta el periodo por defecto
     * @param phase
     * @see obtenerPeriodosHistorialList() , obtenerHistorial(String rutSinDV, Integer periodo)
     */
    public void initCargarHistorialCanje(PhaseEvent phase) {
    	String msisdn = "";
        try {
            LOGGER.info("phase " + phase.getPhaseId());
            
            //Obtenemos datos necesarios para consulta
            ProfileWrapper profile = ProfileWrapperHelper.getProfile(JSFPortletHelper.getRequest());
            String rut = ProfileWrapperHelper.getPropertyAsString(profile, "rutUsuario");
            msisdn = ProfileWrapperHelper.getPropertyAsString(profile, "numeroPcs");                       
            
            //Invocamos metodos de accion que inicializan las bolsas y recargas
            String rutSinDV = Long.toString(RutBean.parseRut(rut).getNumero());
            
            // Consultar info de puntos
            consultarInfoPuntosCliente(rutSinDV, msisdn);
            
            // Obtener expiracion de puntos en los proximos 6 meses
            obtenerExpiracionProxima(rutSinDV);
        
            //Inicializamos el listado de tipos para visializar el historial
            if(tiposHistorialList == null){
              obtenerTiposHistorialList();
            }
            
            // Inicializamos el listado de anios para visializar el historial
            if(aniosHistorialList == null){
            	obtenerAniosHistorialList();
            }            

            //FacesContext.getCurrentInstance().responseComplete();
        } catch (Exception e) {
            LOGGER.error("Error inesperado al cargar historial de canje.", e);            
        }

    }
        
    
    /**
     * Action method para obtener los puntos a expirar en los proximos 6 meses
     * @param 
     * @param 
     */
    private void obtenerExpiracionProxima(String rut){
            	
        String msisdn = "";
        
        try{
        	ProfileWrapper profile = ProfileWrapperHelper.getProfile(JSFPortletHelper.getRequest());    		
        	msisdn = ProfileWrapperHelper.getPropertyAsString(profile, "numeroPcs");
        	listaExpiracion = new ArrayList<ExpiracionProximaPuntosBean>();
        	
        	listaExpiracion = vtasYMktgFidelizacionDelegate.expiracionPuntos(Integer.parseInt(rut));        
        	if(listaExpiracion.size()>6){
        		listaExpiracion = listaExpiracion.subList(0, 6);
        	}
        	
        } catch (DAOException e) {
            LOGGER.error("DAOException al consultar expiracion de puntos para el msisdn:"+msisdn, e);            
        } catch (ServiceException e) {
        	LOGGER.info("ServiceException al consultar expiracion de puntos: " + msisdn + " - "
						+ e.getCodigoRespuesta() + " - " + e.getDescripcionRespuesta());            
        } catch (Exception e) {
        	LOGGER.error("Exception al consultar expiracion de puntos para el msisdn:"+msisdn, e);            
        }

    }
    
    public int getSizeExpiracion(){    	
    	return listaExpiracion.size();
    }
    
    /**
     * Action method para obtener historial de canje de un usuario, dependiendo del periodo seleccionado
     * 
     */
    public void obtenerHistorialCanje(PhaseEvent phase){
    	
    	List<PaginaHistorialCanjeBean> resultado = new ArrayList<PaginaHistorialCanjeBean>();
    	String rut = "";
    	String rutSinDV = "";
        String numeroPcs = "";
        int anio=0;        
        int tipo;
        
        try{
        	
        	String numPaginasProperty = "";
        	
        	try{
        		numPaginasProperty = MiEntelProperties.getProperty("parametros.zonaEntel.historialCanje.numeroPorPagina");
        		if(Utils.isEmptyString(numPaginasProperty)){
        			LOGGER.error( new Exception());
        		}
        	}
        	catch(Exception e){
        		numPaginasProperty = "12";
        		LOGGER.info("No fue encontrado property de numero de pagina para historial de canje. Valor asignado por defecto: 25");        		
        	}        	
        	
        	ProfileWrapper profile = ProfileWrapperHelper.getProfile(JSFPortletHelper.getRequest());   
        	rut = ProfileWrapperHelper.getPropertyAsString(profile, "rutUsuario");
            rutSinDV = Long.toString(RutBean.parseRut(rut).getNumero());
        	numeroPcs = ProfileWrapperHelper.getPropertyAsString(profile, "numeroPcsSeleccionado");
        	
        	
        	// Argumentos obtenidos de la vista
        	anio = Integer.parseInt(JsfUtil.getRequestParameter("filtroAnio"));
        	tipo = Integer.parseInt(JsfUtil.getRequestParameter("filtroTipo"));
        	String descAbonoPromocional = JsfUtil.getRequestParameter("descAbonoPromocional");
        	
            resultado = vtasYMktgFidelizacionDelegate.getHistorialCartolaPuntos(Integer.parseInt(rutSinDV), anio, 
            																tipo, Integer.parseInt(numPaginasProperty), descAbonoPromocional);  
            if(resultado.size() > 1000){
            	resultado = resultado.subList(0, 1000);
            }
            historialJSON = JsonHelper.toJsonResponse(resultado);
            
            existenRegistrosHist = (resultado != null && !resultado.isEmpty());
            
        } catch (DAOException e) {
            LOGGER.error("DAOException al consultar historial para los siguientes rutSinDV y periodo: " + rutSinDV + " " + anio, e);
            historialJSON = JsonHelper.toJsonServiceErrorMessage("historialCanjeVacio");            
        } catch (ServiceException e) {
        	historialJSON = JsonHelper.toJsonServiceErrorMessage("historialCanjeVacio");
        	LOGGER.info("ServiceException al consultar historial: " + numeroPcs + " - "
						+ e.getCodigoRespuesta() + " - " + e.getDescripcionRespuesta());            
        } catch (Exception e) {
        	historialJSON = JsonHelper.toJsonServiceErrorMessage("historialCanjeVacio");
            LOGGER.error("Exception al consultar historial para los siguientes rutSinDV y periodo: " + rutSinDV + " " + anio, e);            
        }

    }
    
    /**
     * Action method para consultar informacion de puntos para el rut del usuario en sesion
     * @param <code>rutSinDV</code> rut sin digito verificador
     */
    private void consultarInfoPuntosCliente(String rutSinDV, String msisdn){
        ResultadoConsultarPuntosBean infoPuntos = new ResultadoConsultarPuntosBean();
        
        try{        	        	
            infoPuntos = vtasYMktgFidelizacionDelegate.consultarInfoPuntosCliente(rutSinDV);
            infoPuntos = vtasYMktgFidelizacionDelegate.consultarCategoriaCliente(infoPuntos, msisdn);
            detallePuntos = infoPuntos.getPuntos();
            categoriaClientePuntos = infoPuntos.getCategoriaCliente();
            
        } catch (DAOException e) {
            LOGGER.error("DAOException al consultar puntos para el rut: " + rutSinDV, e);            
        } catch (ServiceException e) {
        	LOGGER.info("ServiceException en la consulta de puntos: " + msisdn + " - "
						+ e.getCodigoRespuesta() + " - " + e.getDescripcionRespuesta());             
        } catch (Exception e) {
            LOGGER.error("Exception al conusltar puntos para el rut: " + rutSinDV, e);            
        }
        
    }    
    
    /**
     * Metodo utilitario que devuelve un listado de periodos para ver el historial de canje <br>
     * haciendo uso de la clase {@link JsfUtil}
     */
    private void obtenerTiposHistorialList() {
        try{        
        	tiposHistorialList = new SelectItem[0];
            List<CodeDescBean> tiposList = ParametrosHelper.obtenerTiposHistorialList();
            boolean opcionVacia = tiposList.isEmpty() ? true : false;            
            tiposHistorialList = JsfUtil.getSelectItemsCodeDesc(tiposList, opcionVacia);
        } catch(Exception e){
            LOGGER.error("Exception caught in <obtenerTiposHistorialList>: ", e);
        }
    }
    
    /**
     * Metodo utilitario que devuelve un listado con el anio actual y los dos anteriores <br>
     */
    private void obtenerAniosHistorialList() {
        try{
        	aniosHistorialList = new ArrayList<SelectItem>();
        	
        	Calendar c = Calendar.getInstance();
        	aniosHistorialList.add(new SelectItem(c.get(Calendar.YEAR)+"",c.get(Calendar.YEAR)+""));
        	
        	c.roll(Calendar.YEAR, -1);
        	aniosHistorialList.add(new SelectItem(c.get(Calendar.YEAR)+"",c.get(Calendar.YEAR)+""));
        	
        	c.roll(Calendar.YEAR, -1);
        	aniosHistorialList.add(new SelectItem(c.get(Calendar.YEAR)+"",c.get(Calendar.YEAR)+""));        	
        	
        } catch(Exception e){
            LOGGER.error("Exception caught in <obtenerAniosHistorialList>: ", e);
        }
   } 
   
    
}
