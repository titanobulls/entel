/* Propiedad de Entel PCS. Todos los derechos reservados */
package com.epcs.cliente.perfil.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.faces.event.PhaseEvent;
import javax.portlet.PortletPreferences;

import org.apache.log4j.Logger;

import com.bea.p13n.usermgmt.profile.ProfileWrapper;
import com.epcs.bean.ResumenProductosContratadosBean;
import com.epcs.cliente.perfil.delegate.ProductoDelegate;
import com.epcs.recursoti.configuracion.JsonHelper;
import com.epcs.recursoti.configuracion.MiEntelProperties;
import com.epcs.recursoti.configuracion.Utils;
import com.epcs.recursoti.configuracion.jsf.utils.JSFMessagesHelper;
import com.epcs.recursoti.configuracion.jsf.utils.JSFPortletHelper;
import com.epcs.recursoti.configuracion.uup.ProfileWrapperHelper;
import com.epcs.recursoti.excepcion.DAOException;
import com.epcs.recursoti.excepcion.ServiceException;

/**
 * @author jmanzur (I2B) en nombre de Absalon Opazo (Atencion al Cliente,
 *         EntelPcs)
 * 
 */
public class ProductoController implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private static final Logger LOGGER = Logger
            .getLogger(ProductoController.class);
    private static final Logger PRODUCTOS_LOGGER = Logger.getLogger("cajaProdContratadosLog");

    private ProductoDelegate productoDelegate;

    private ResumenProductosContratadosBean resumenProductosContratadosBean;

    private String productosContratadosJson;

    private List<String> productosContratados;
    
    private static final String SUBMERCADO_CCPP = MiEntelProperties.getProperty("miEntel.subMercadoCCPP");

    public ProductoController() {
        super();
    }

    /**
     * @return the productoDelegate
     */
    public ProductoDelegate getProductoDelegate() {
        return productoDelegate;
    }

    /**
     * @param productoDelegate
     *            the productoDelegate to set
     */
    public void setProductoDelegate(ProductoDelegate productoDelegate) {
        this.productoDelegate = productoDelegate;
    }

    /**
     * Metodo que inicializa la Caja Productos Contratados del Dashboard
     * Este metodo REGISTRA en el log configurado para dicha caja y no en el log por defecto para la clase.
     * @param event
     */
    public void init(PhaseEvent event) {
        try {
            
        	PRODUCTOS_LOGGER.info("INICIO REGISTRO LOG CAJA PRODUCTOS CONTRATADOS");
        	PRODUCTOS_LOGGER.info("****** Clase: com.epcs.cliente.perfil.controller.ProductoController ******");
            loadData();
            PRODUCTOS_LOGGER.info("FIN REGISTRO LOG CAJA PRODUCTOS CONTRATADOS");
  
        } catch (DAOException e) {
        	PRODUCTOS_LOGGER.error("DAOException al obtener productos contratados", e);
        	PRODUCTOS_LOGGER.info("FIN REGISTRO LOG CAJA PRODUCTOS CONTRATADOS");
            productosContratadosJson = JsonHelper.toJsonErrorMessage("Ha ocurrido un error inesperado. " 
                    + " Disculpe las molestias");
            
        } catch (ServiceException e) {
        	PRODUCTOS_LOGGER.info("ServiceException de servicio codigo: " + e.getCodigoRespuesta()
                    + " - " + e.getDescripcionRespuesta());
            PRODUCTOS_LOGGER.info("FIN REGISTRO LOG CAJA PRODUCTOS CONTRATADOS");
            productosContratadosJson = JsonHelper.toJsonServiceErrorMessage(e.getCodigoRespuesta());
           
        } catch (Exception e) {
        	PRODUCTOS_LOGGER.error("Exception inesperado al obtener productos contratados", e);
            PRODUCTOS_LOGGER.info("FIN REGISTRO LOG CAJA PRODUCTOS CONTRATADOS");
            productosContratadosJson = JsonHelper.toJsonErrorMessage("Ha ocurrido un error inesperado. " 
                    + " Disculpe las molestias");
        }

    }

    /**
     * Metodo que carga la informacion a mostrar en la Caja de Productos Contratados del Dashboard.
     * Este metodo REGISTRA en el log configurado para dicha caja y no en el log por defecto para la clase.
     */
    private void loadData() throws Exception {

        ProfileWrapper profileWrapper = ProfileWrapperHelper
                .getProfile(JSFPortletHelper.getRequest());
        String numeroPcsSeleccionado = ProfileWrapperHelper.getPropertyAsString(
                profileWrapper, "numeroPcsSeleccionado");
        String rutUsuarioSeleccionado = ProfileWrapperHelper.getPropertyAsString(
                profileWrapper, "rutUsuarioSeleccionado");
        String mercado = ProfileWrapperHelper.getPropertyAsString(
                profileWrapper, "mercado");
        String subMercado = ProfileWrapperHelper.getPropertyAsString(
                profileWrapper, "subMercado");
        String paramMercado = subMercado.equals(SUBMERCADO_CCPP) ? subMercado : mercado;

        if (resumenProductosContratadosBean == null) {
        	
        	PRODUCTOS_LOGGER.info("Movil: " + numeroPcsSeleccionado + " - Rut: " + rutUsuarioSeleccionado);
        	this.resumenProductosContratadosBean = this.productoDelegate
                    .getProductosContratados(mercado, numeroPcsSeleccionado);
            
            // Se carga lista de String para presentaci&oacute;n de productos
            loadProductosList();
           
        }
        
    }
    
    private void loadProductosList()throws Exception {
        
        productosContratados = new ArrayList<String>();
        if (!Utils.isEmptyString(resumenProductosContratadosBean
                .getNombrePlanVoz())) {
            productosContratados.add(resumenProductosContratadosBean
                    .getNombrePlanVoz());
        }
        if (!Utils.isEmptyString(resumenProductosContratadosBean
                .getNombrePlanInternet())) {
            productosContratados.add(resumenProductosContratadosBean
                    .getNombrePlanInternet());
        }
        if (resumenProductosContratadosBean.getNombreBolsas() != null
                && !resumenProductosContratadosBean.getNombreBolsas().isEmpty())
        productosContratados.addAll(resumenProductosContratadosBean
                .getNombreBolsas());
        
        productosContratadosJson = JsonHelper.toJsonResponse(productosContratados);
        
    }

    /**
     * @return the resumenProductosContratadosBean
     */
    public ResumenProductosContratadosBean getResumenProductosContratadosBean() {
        return resumenProductosContratadosBean;
    }

    /**
     * @param resumenProductosContratadosBean
     *            the resumenProductosContratadosBean to set
     */
    public void setResumenProductosContratadosBean(
            ResumenProductosContratadosBean resumenProductosContratadosBean) {
        this.resumenProductosContratadosBean = resumenProductosContratadosBean;
    }

    /**
     * @return the productosContratados
     */
    public List<String> getProductosContratados() {

        if (productosContratados == null) {
            if (resumenProductosContratadosBean == null) {

                try {
                    loadData();
                } catch (Exception e) {
                	PRODUCTOS_LOGGER.error("no se obtuvieron prouctosContratados", e);
                  //TODO cambiar a mensaje en properties
                    JSFMessagesHelper.addErrorMessage("El servicio no esta disponible en este momento");
                    return Collections.emptyList();
                }
            }

            // Json string
            productosContratadosJson = JsonHelper.toJsonResponse(productosContratados);
        }

        return productosContratados;
    }

    /**
     * @param productosContratados
     *            the productosContratados to set
     */
    public void setProductosContratados(List<String> productosContratados) {
        this.productosContratados = productosContratados;
    }

    /**
     * @return the productosContratadosJson
     */
    public String getProductosContratadosJson() {
        return productosContratadosJson;
    }

    /**
     * @param productosContratadosJson
     *            the productosContratadosJson to set
     */
    public void setProductosContratadosJson(String productosContratadosJson) {
        this.productosContratadosJson = productosContratadosJson;
    }
    
    /**
     * Retorna el valor de la preferencia pagePlanes
     * @return
     */
    public String getPageLabelPlanes() {
        try {
            PortletPreferences prefs = JSFPortletHelper.getPreferencesObject();
            return JSFPortletHelper.getPreference(prefs, "pagePlanes", "");
        } catch (Exception e) {
        	PRODUCTOS_LOGGER.error("No se ha podido obtener el pageLabel de planes" + e.getMessage());
            return "";
        }        
    }
    
    /**
     * Retorna el valor de la preferencia pageBolsas
     * @return
     */    
    public String getPageLabelBolsas() {
        try {
            PortletPreferences prefs = JSFPortletHelper.getPreferencesObject();
            return JSFPortletHelper.getPreference(prefs, "pageBolsas", "");
        } catch (Exception e) {
        	PRODUCTOS_LOGGER.error("No se ha podido obtener el pageLabel de bolsas" + e.getMessage(), e);
            return "";
        }
    }
    
    /**
     * Retorna el valor de la preferencia pageInternet
     * @return
     */    
    public String getPageLabelInternet() {
        try {
            PortletPreferences prefs = JSFPortletHelper.getPreferencesObject();
            return JSFPortletHelper.getPreference(prefs, "pageInternet", "");
        } catch (Exception e) {
        	PRODUCTOS_LOGGER.error("No se ha podido obtener el pageLabel de internet movil" + e.getMessage(), e);
            return "";
        }
    }      

}
