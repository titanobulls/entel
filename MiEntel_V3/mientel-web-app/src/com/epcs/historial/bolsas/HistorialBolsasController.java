package com.epcs.historial.bolsas;

import javax.faces.event.PhaseEvent;


import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.security.Principal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.faces.event.PhaseEvent;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import org.apache.log4j.Logger;

import com.bea.p13n.usermgmt.profile.ProfileWrapper;
import com.epcs.bean.DetalleRecargasBean;
import com.epcs.bean.PerfilUsuarioBean;
import com.epcs.bean.RecargaHistoricoBean;
import com.epcs.bean.TraficoEnLineaBean;
import com.epcs.bean.TraficoEnLineaPPCCBean;
import com.epcs.billing.recarga.controller.RecargaHistoricoController;
import com.epcs.billing.recarga.delegate.RecargaDelegate;
import com.epcs.billing.registrouso.controller.TraficoEnLineaController;
import com.epcs.billing.registrouso.delegate.TraficoDelegate;
import com.epcs.cliente.perfil.delegate.CuentaDelegate;
import com.epcs.historial.bolsas.delegate.HistorialDelegate;
import com.epcs.recursoti.configuracion.DateHelper;
import com.epcs.recursoti.configuracion.JsonHelper;
import com.epcs.recursoti.configuracion.MiEntelProperties;
import com.epcs.recursoti.configuracion.Utils;
import com.epcs.recursoti.configuracion.error.ServiceMessages;
import com.epcs.recursoti.configuracion.jsf.utils.JSFMessagesHelper;
import com.epcs.recursoti.configuracion.jsf.utils.JSFPortletHelper;
import com.epcs.recursoti.configuracion.jsf.utils.JsfUtil;
import com.epcs.recursoti.configuracion.uup.ProfileWrapperHelper;
import com.epcs.recursoti.excepcion.DAOException;
import com.epcs.recursoti.excepcion.ServiceException;
import com.esa.billing.recargas.registrobolsas.types.BolsaSMSType;


/**
 * @author jlopez (Tecnova)
 * 
 */



public class HistorialBolsasController implements Serializable {
	
private static final long serialVersionUID = 1L;
private static final Logger LOGGER = Logger
            .getLogger(HistorialBolsasController.class);

private int paginaActual;

public String numPagina;

private TraficoDelegate traficoDelegate;

private HistorialDelegate historialDelegate;

private RecargaDelegate recargaDelegate;

private RecargaHistoricoBean recargaHistoricoBean;

private List<TraficoEnLineaPPCCBean> traficoEnLineaPPCC;

private List<TraficoEnLineaPPCCBean> detalleTraficoEnLineaPPCC;

private List<BolsaSMSType> listaBolsasPP;

private List<DetalleRecargasBean> detalleRecargasPP;

private boolean registros;

private boolean sesionPag;

public boolean isSesionPag() {
	return sesionPag;
}


public void setSesionPag(boolean sesionPag) {
	this.sesionPag = sesionPag;
}


public boolean isRegistros() {
	return registros;
}


public void setRegistros(boolean registros) {
	this.registros = registros;
}


public List<BolsaSMSType> getListaBolsasPP() {
	return listaBolsasPP;
}


public void setListaBolsasPP(List<BolsaSMSType> listaBolsasPP) {
	this.listaBolsasPP = listaBolsasPP;
}




public List<DetalleRecargasBean> getDetalleRecargasPP() {
	return detalleRecargasPP;
}


public void setDetalleRecargasPP(List<DetalleRecargasBean> detalleRecargasPP) {
	this.detalleRecargasPP = detalleRecargasPP;
}


private boolean existeHistoricoRecargas;

private String traficoEnLineaJson;

private String paginaTotal;

private String paginaTotal2;

public String getPaginaTotal() {
	return paginaTotal;
}


public void setPaginaTotal(String paginaTotal) {
	this.paginaTotal = paginaTotal;
}


public String getPaginaTotal2() {
	return paginaTotal2;
}


public void setPaginaTotal2(String paginaTotal2) {
	this.paginaTotal2 = paginaTotal2;
}


public boolean isExisteHistoricoRecargas() {
	return existeHistoricoRecargas;
}


public void setExisteHistoricoRecargas(boolean existeHistoricoRecargas) {
	this.existeHistoricoRecargas = existeHistoricoRecargas;
}


public List<TraficoEnLineaPPCCBean> getTraficoEnLineaPPCC() {
	return traficoEnLineaPPCC;
}


public void setTraficoEnLineaPPCC(
		List<TraficoEnLineaPPCCBean> traficoEnLineaPPCC) {
	this.traficoEnLineaPPCC = traficoEnLineaPPCC;
}


public String getTraficoEnLineaPPCCJson() {
	return traficoEnLineaPPCCJson;
}


public void setTraficoEnLineaPPCCJson(String traficoEnLineaPPCCJson) {
	this.traficoEnLineaPPCCJson = traficoEnLineaPPCCJson;
}


private String traficoEnLineaPPCCJson;
	
private CuentaDelegate cuentaDelegate;


public CuentaDelegate getCuentaDelegate() {
	return cuentaDelegate;
}


public void setCuentaDelegate(CuentaDelegate cuentaDelegate) {
	this.cuentaDelegate = cuentaDelegate;
}


public void initHistorialBolsas(PhaseEvent event){
	
	try {
        
        numPagina = JsfUtil.getRequestParameter("numPagina");
		loadHistorialBolsas2(numPagina);
		loadHistorialBolsas3(numPagina);
		
		Principal userPrincipal = event.getFacesContext().getCurrentInstance().getExternalContext().getUserPrincipal();
        if(userPrincipal==null) { 
           
            this.setSesionPag(false); 
            return;
        }else{
        	this.setSesionPag(true);
        }
        
    }catch (DAOException e) {
    	LOGGER.error("DAOException caught: "+e.getMessage(), e);
        traficoEnLineaPPCCJson = JsonHelper
        .toJsonErrorMessage("El servicio no est&aacute; disponible de momento."
                + " Por favor intente m&aacute;s tarde");

	} catch (ServiceException e) {
	    LOGGER.info("ServiceException caught codigo: " + e.getCodigoRespuesta()
	            + " - " + e.getDescripcionRespuesta());
	    traficoEnLineaPPCCJson = JsonHelper.toJsonServiceErrorMessage(
	            "gestionRegistroUso", e.getCodigoRespuesta());
	
	} catch (Exception e) {
	    LOGGER.error("Exception inesperado al obtener datos del plan", e);
	    traficoEnLineaPPCCJson = JsonHelper
	            .toJsonErrorMessage("Ha ocurrido un error inesperado. "
	                    + " Disculpe las molestias");
	}

}



private void loadHistorialBolsas2(String numPagina) throws Exception {
	
	detalleTraficoEnLineaPPCC = new LinkedList<TraficoEnLineaPPCCBean>();
	
    //System.out.println(numPagina);
    
    if(numPagina==null){
    	numPagina="1";
    }
    
    //System.out.println(numPagina);
	
    ProfileWrapper profile = ProfileWrapperHelper.getProfile(JSFPortletHelper.getRequest());
    String numeroPcs = ProfileWrapperHelper.getPropertyAsString(profile,"numeroPcsSeleccionado");
    String mercado = "";    
    int flagBam;
    
    if(numeroPcs.equals(ProfileWrapperHelper.getPropertyAsString(profile, "numeroPcsSeleccionado"))){
    	mercado = ProfileWrapperHelper.getPropertyAsString(profile, "mercado");    	
    	flagBam = ProfileWrapperHelper.getPropertyAsInt(profile, "flagBam");
    	if(mercado.equals("PP") && flagBam == 1){
    		mercado = "PPBAM";
    	}
    }
    else{
    	PerfilUsuarioBean pub = cuentaDelegate.obtenerPerfilUsuario(numeroPcs);
    	mercado = pub.getMercado();    	
    	flagBam = pub.getFlagBam();
    	if(mercado.equals("PP") && flagBam == 1){
    		mercado = "PPBAM";
    	}
    }
    
    //System.out.println("numero =  "+numeroPcs+"  ");
        
    List<BolsaSMSType> listaBolsas = new ArrayList<BolsaSMSType>();
    
    listaBolsas = historialDelegate.getHistorialBolsas(numeroPcs,mercado);
      
    //System.out.println("largo listaDeBolsas =  "+listaBolsas.size());
    
    int i =0;
        
    //# paginas
    double registros = listaBolsas.size();
    
    if(listaBolsas.size()!=0){
    	
	    String registrosPaginaAux = null;
	    try{
	    	 registrosPaginaAux = MiEntelProperties.getProperty("parametro.registrosPagina");
	    }catch(NullPointerException  e){
	    	registrosPaginaAux="25";
	    }
	    if(registrosPaginaAux=="0"){
	    	registrosPaginaAux="25";
	    }
	    
	    int registrosPagina = Integer.valueOf(registrosPaginaAux);
	    
	    //System.out.println("registros por pagina : "+registrosPagina);
	    
	    this.paginaTotal = String.valueOf( (int) Math.ceil(registros/registrosPagina));
	    
	    //System.out.println("# paginas :"+this.paginaTotal);
	    
	    
	    listaBolsas = getPageArray(Integer.parseInt(numPagina), listaBolsas, registrosPagina);
	    
	    listaBolsas = mapeoCanales(listaBolsas);
	    
	    this.setRegistros(true);
     
    }else{
    	
    	this.setRegistros(false);
    	
    	this.paginaTotal = "-1";
    	
    	
    	
    	//System.out.println("fin else "+this.registros);
    	
    }
    
    //System.out.println("registros = "+ this.registros);
    
    listaBolsasPP = listaBolsas;   
 
    this.setListaBolsasPP(listaBolsas);

    //System.out.println("success");
}

private void loadHistorialBolsas3(String numPagina) throws Exception {
	
	detalleTraficoEnLineaPPCC = new LinkedList<TraficoEnLineaPPCCBean>();
	
    //System.out.println(numPagina);
    
    if(numPagina==null){
    	numPagina="1";
    }
    
    
    ProfileWrapper profile = ProfileWrapperHelper
	    .getProfile(JSFPortletHelper.getRequest());
	String msisdn = ProfileWrapperHelper.getPropertyAsString(profile,
	    "numeroPcsSeleccionado");
		
	recargaHistoricoBean = recargaDelegate.getHistoricoRecargas(msisdn);
    
	ArrayList<DetalleRecargasBean> detalleRecargasAux = new ArrayList<DetalleRecargasBean>();
	List<DetalleRecargasBean> detalleRecargas = new ArrayList<DetalleRecargasBean>();
     
        
    detalleRecargasAux = recargaHistoricoBean.getDetalleRecargas();
      
    
    
    int i =0;
        
    //# paginas
    double registros = detalleRecargasAux.size();
    
    if(detalleRecargasAux.size()!=0){
    	
	    String registrosPaginaAux = null;
	    try{
	    	 registrosPaginaAux = MiEntelProperties.getProperty("parametro.registrosPagina");
	    }catch(NullPointerException  e){
	    	registrosPaginaAux="25";
	    }
	    if(registrosPaginaAux=="0"){
	    	registrosPaginaAux="25";
	    }
	    
	    int registrosPagina = Integer.valueOf(registrosPaginaAux);
	    
	    //System.out.println("registros por pagina : "+registrosPagina);
	    
	    this.paginaTotal2 = String.valueOf( (int) Math.ceil(registros/registrosPagina));
	    
	    //System.out.println("# paginas :"+this.paginaTotal);
	    
	    
	    detalleRecargas = getPageArrayRec(Integer.parseInt(numPagina), detalleRecargasAux, registrosPagina);
	    
	    this.setExisteHistoricoRecargas(true);
     
    }else{
    	
    	this.setExisteHistoricoRecargas(false);
    	
    	this.paginaTotal2 = "-1";
    	
    	
    	
    	//System.out.println("fin else "+this.registros);
    	
    }
    
    //System.out.println("registros = "+ this.registros);
    
    detalleRecargasPP = detalleRecargas;   
 
    this.setDetalleRecargasPP(detalleRecargas);

    //System.out.println("success");
}




public List<BolsaSMSType> mapeoCanales( List<BolsaSMSType> data ){
	
	String canalOld = "";
	
	String OLD_FORMAT = "yyyy/MM/dd HH:mm:ss";
	
	String NEW_FORMAT = "dd/MM/yyyy HH:mm:ss";
	
	
	for(int i=0; i<data.size(); i++){
		
		String fechaContratacion = data.get(i).getFechaContratacion();
		
		String fechaEnvio = data.get(i).getFechaEnvioSMS();
		
		fechaContratacion = fechaContratacion.replace("-", "/");
		
		fechaEnvio = fechaEnvio.replace("-", "/");
		
		try {
			
			SimpleDateFormat sdf = new SimpleDateFormat(OLD_FORMAT);			
			
			Date d = sdf.parse(fechaContratacion);		
			
			Date d2 = sdf.parse(fechaEnvio);
			
			sdf.applyPattern(NEW_FORMAT);
			
			data.get(i).setFechaContratacion(sdf.format(d));
			
			data.get(i).setFechaEnvioSMS(sdf.format(d2));
			
			
		} catch (ParseException e) {
			System.out.println("error tiempo");
			LOGGER.error(e);
		}
		
		
		
		
		data.get(i).setPrecioOPuntosZonaEntel( "$"+data.get(i).getPrecioOPuntosZonaEntel()   );
		
		canalOld = data.get(i).getCanalContratacion();
		
		if(canalOld.compareToIgnoreCase("MiEntel")==0){
			
			data.get(i).setCanalContratacion("Portal Mi Entel");
			
		}
		if(canalOld.compareToIgnoreCase("SGA")==0){
			
			data.get(i).setCanalContratacion("Atenci\u00F3n Ejecutivo");
			
		}
		if(canalOld.compareToIgnoreCase("PID")==0){
			
			data.get(i).setCanalContratacion("Portal Tarifa Diaria");
			
		}
		if(canalOld.compareToIgnoreCase("PIM")==0){
			
			data.get(i).setCanalContratacion("Portal Wap");
			
		}
		if(canalOld.compareToIgnoreCase("PMOVIL")==0){
			
			data.get(i).setCanalContratacion("Portal Mi Entel m\u00F3vil");
			
		}
		
        if(canalOld.compareToIgnoreCase("IVR")==0){
        	
        	data.get(i).setCanalContratacion("Autoatenci\u00F3n telef\u00F3nica");
			
		}
        if(canalOld.compareToIgnoreCase("Call Center")==0){
			
		}
        if(canalOld.compareToIgnoreCase("Sucursal")==0){
        	
        	data.get(i).setCanalContratacion("Tienda");
			
		}
        if(canalOld.compareToIgnoreCase("USSD")==0){
        	
        	data.get(i).setCanalContratacion("C\u00F3digo *119");
			
		}
        if(canalOld.compareToIgnoreCase("Portal WAP")==0){
        	
        	data.get(i).setCanalContratacion("Portal Wap");
			
		}
        if(canalOld.compareToIgnoreCase("Portal m\u00F3vil")==0){
        	
        	data.get(i).setCanalContratacion("Portal Mi Entel m\u00F3vil");
			
		}
        if(canalOld.compareToIgnoreCase("SMS")==0){
        	
        	data.get(i).setCanalContratacion("SMS al 301");
			
		}
		
	}
	
	return data;
	
}

public List<BolsaSMSType> getPageArray(int pageNumber, List<BolsaSMSType> data, int pageSize)
{

	int toIndex = -1;

	int fromIndex = -1;

	fromIndex = ((pageNumber - 1) * pageSize);

	toIndex = data.size() - fromIndex < pageSize ? (fromIndex + (data.size() - fromIndex)) : (pageNumber * pageSize);

	List<BolsaSMSType> pageData = data.subList(fromIndex, toIndex);

	/*Object[] page = new Object[Math.abs(fromIndex - toIndex)];

	int index = 0;

	for (Iterator iterator = pageData.iterator(); iterator.hasNext();)
	{
		Object item = iterator.next();
		page[index] = item;
		index++;
	}
	/**/
	
	//System.out.println("fromIndex =  "+fromIndex+" toIndex = "+toIndex);

	return pageData;
}
	
public List<DetalleRecargasBean> getPageArrayRec(int pageNumber, ArrayList<DetalleRecargasBean> data, int pageSize)
{

	int toIndex = -1;

	int fromIndex = -1;

	fromIndex = ((pageNumber - 1) * pageSize);

	toIndex = data.size() - fromIndex < pageSize ? (fromIndex + (data.size() - fromIndex)) : (pageNumber * pageSize);

	List<DetalleRecargasBean> pageData = data.subList(fromIndex, toIndex);
	
	/*Object[] page = new Object[Math.abs(fromIndex - toIndex)];

	int index = 0;

	for (Iterator iterator = pageData.iterator(); iterator.hasNext();)
	{
		Object item = iterator.next();
		page[index] = item;
		index++;
	}
	/**/
	
	//System.out.println("fromIndex =  "+fromIndex+" toIndex = "+toIndex);

	return pageData;
}	

	public HistorialDelegate getHistorialDelegate() {
	return historialDelegate;
}


public void setHistorialDelegate(HistorialDelegate historialDelegate) {
	this.historialDelegate = historialDelegate;
}


	public RecargaHistoricoBean getRecargaHistoricoBean() {
	return recargaHistoricoBean;
}


public void setRecargaHistoricoBean(RecargaHistoricoBean recargaHistoricoBean) {
	this.recargaHistoricoBean = recargaHistoricoBean;
}



public RecargaDelegate getRecargaDelegate() {
	return recargaDelegate;
}


public void setRecargaDelegate(RecargaDelegate recargaDelegate) {
	this.recargaDelegate = recargaDelegate;
}


	/**
     * 
     * @return 
     * @throws Exception
     */
    public void initDetalleTraficoEnLineaPPCC(PhaseEvent event){
    	
    	try {
    		
    		numPagina = JsfUtil.getRequestParameter("numPagina");
    		loadDataDetalleTraficoEnLineaPPCC(numPagina);
	            
        }catch (DAOException e) {
        	LOGGER.error("DAOException caught: "+e.getMessage(), e);
            traficoEnLineaPPCCJson = JsonHelper
            .toJsonErrorMessage("El servicio no est&aacute; disponible de momento."
                    + " Por favor intente m&aacute;s tarde");

		} catch (ServiceException e) {
		    LOGGER.info("ServiceException caught codigo: " + e.getCodigoRespuesta()
		            + " - " + e.getDescripcionRespuesta());
		    traficoEnLineaPPCCJson = JsonHelper.toJsonServiceErrorMessage(
		            "gestionRegistroUso", e.getCodigoRespuesta());
		
		} catch (Exception e) {
		    LOGGER.error("Exception inesperado al obtener datos del plan", e);
		    traficoEnLineaPPCCJson = JsonHelper
		            .toJsonErrorMessage("Ha ocurrido un error inesperado. "
		                    + " Disculpe las molestias");
		}

    }
    
    
private void loadDataDetalleTraficoEnLineaPPCC(String numPagina) throws Exception {
		
    	detalleTraficoEnLineaPPCC = new LinkedList<TraficoEnLineaPPCCBean>();
    	
       
        ProfileWrapper profile = ProfileWrapperHelper.getProfile(JSFPortletHelper.getRequest());
        String numeroPcs = ProfileWrapperHelper.getPropertyAsString(profile,"numeroPcsSeleccionado");

        traficoEnLineaPPCC = traficoDelegate.getTraficoEnLineaPPCC(numeroPcs , numPagina);
        
        Collections.<TraficoEnLineaPPCCBean> sort(traficoEnLineaPPCC);

	    for (int i = 0; i < traficoEnLineaPPCC.size() && i <= 25; i++) {
	        detalleTraficoEnLineaPPCC.add(traficoEnLineaPPCC.get(i));
	    }
	    
	    System.out.println("largo =  "+traficoEnLineaPPCC.size());
        
        this.setDetalleTraficoEnLineaPPCC(detalleTraficoEnLineaPPCC);
        this.traficoEnLineaPPCCJson = JsonHelper.toJsonResponse(detalleTraficoEnLineaPPCC);
    }


//////////////////////////////////
//GETTERS SETTERS
//////////////////////////////////

public List<TraficoEnLineaPPCCBean> getDetalleTraficoEnLineaPPCC() {
	return detalleTraficoEnLineaPPCC;
}

public void setDetalleTraficoEnLineaPPCC(
		List<TraficoEnLineaPPCCBean> detalleTraficoEnLineaPPCC) {
	this.detalleTraficoEnLineaPPCC = detalleTraficoEnLineaPPCC;
}


public TraficoDelegate getTraficoDelegate() {
	return traficoDelegate;
}


public String getTraficoEnLineaJson() {
	return traficoEnLineaJson;
}


public void setTraficoEnLineaJson(String traficoEnLineaJson) {
	this.traficoEnLineaJson = traficoEnLineaJson;
}


public void setTraficoDelegate(TraficoDelegate traficoDelegate) {
	this.traficoDelegate = traficoDelegate;
}
	

}
