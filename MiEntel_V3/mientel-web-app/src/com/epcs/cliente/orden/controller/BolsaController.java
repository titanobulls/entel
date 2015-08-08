/* Propiedad de Entel PCS. Todos los derechos reservados */
package com.epcs.cliente.orden.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.portlet.PortletPreferences;

import org.apache.log4j.Logger;

import com.bea.p13n.usermgmt.profile.ProfileWrapper;
import com.epcs.administracion.suscripciones.delegate.SuscripcionesDelegate;
import com.epcs.bean.BolsaBean;
import com.epcs.bean.BolsaPPBean;
import com.epcs.bean.BolsasActualesDisponiblesBean;
import com.epcs.bean.CodeDescBean;
import com.epcs.bean.GrupoBolsaBean;
import com.epcs.bean.OfertaBolsaBlindaje;
import com.epcs.bean.ResultadoContratarBolsaBean;
import com.epcs.bean.ResumenEquipoBean;
import com.epcs.bean.RutBean;
import com.epcs.bean.SimultaneidadBolsasBean;
import com.epcs.bean.TransaccionGTMBean;
import com.epcs.bean.ZonaPerfilBean;
import com.epcs.cliente.orden.delegate.BolsaDelegate;
import com.epcs.cliente.perfil.dao.BolsaBlindajeDAO;
import com.epcs.cliente.perfil.dao.EquipoDAO;
import com.epcs.cliente.perfil.dao.PlanDAO;
import com.epcs.cliente.perfil.delegate.EquipoDelegate;
import com.epcs.cliente.perfil.delegate.PlanDelegate;
import com.epcs.cliente.perfil.util.PlanHelper;
import com.epcs.inteligencianegocio.satisfaccioncliente.util.EstadisticasHelper;
import com.epcs.recursoti.configuracion.JsonHelper;
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
import com.epcs.vtasymktg.fidelizacion.delegate.BeneficiosDelegate;

/**
 * Esta clase se encarga de listar las bolsas disponibles, contratadas,
 * diponibles para regalar y regaladas, asi como la contracion y regalo de bolsas.
 * 
 * @author jmanzur (I2B) en nombre de Absalon Opazo (Atencion al Cliente,
 *         EntelPcs) 
 */
public class BolsaController implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final Logger LOGGER = Logger
            .getLogger(BolsaController.class);
//    private static final String SUSCRIPCION = MiEntelProperties
//            .getProperty("parametros.plan.suscripcion.sigla");
//    private static final String CONTROLADA = MiEntelProperties
//            .getProperty("parametros.plan.cuentacontrolada.sigla");

    private static final String BLACKBERRY = MiEntelProperties
            .getProperty("parametros.tipobolsa.blackberry.id");
    
    private static final String BOLSAS_BAM = MiEntelProperties
    .getProperty("parametros.bolsas.im.id");
    
    /* NUEVO TECNOVA 13/12/2013 */
    private static final String BOLSAS_ROA = MiEntelProperties
    .getProperty("parametros.tipobolsa.roa.id");
    
    private static final String OTRAS_BOLSAS = MiEntelProperties
    .getProperty("parametros.bolsas.otr.cod");
    
    
    private BolsaDelegate bolsaDelegate;
    private SuscripcionesDelegate suscripcionesDelegate;
    
    private boolean userAdmin = false;
    private boolean existenBolsasDisp = false;
    private boolean existenBolsasDispReg = false;
    private boolean existenBolsasRegaladas = false;
    private boolean existenBolsasContratadas = false;
    private boolean existeBolsaRecomendada = false;
    private boolean existeBolsaRecomendadaDisponible = false;
    private boolean existenbolsasActualesPP = false;
    private boolean mercadoSuscripcion = false;
    
    private int cantidad = 0;
    private BolsasActualesDisponiblesBean bolsasActualesDisponiblesBean;
    private OfertaBolsaBlindaje ofertaBolsaBlindaje;
    private BolsaBlindajeDAO bolsaBlindajeDAO;
    
    private List<BolsaBean> bolsasActuales;
    private List<BolsaPPBean> bolsasActualesPP;
    private List<BolsaBean> bolsasRegaladas;
    private List<BolsaBean> bolsasDispRegalo;
    private List<GrupoBolsaBean> grupoBolsasDisponibles;
    
    private String respuestaJson;
    private String mercado;
    private String codigoBolsaDisponible;
    private String codigoBolsaRecomendada;
    
    // Validacion prestaluka
    private BeneficiosDelegate beneficiosDelegate;
    private ZonaPerfilBean zonaPerfilBean;
    private static final String CODSBAD_PRESTALUKA = MiEntelProperties.getProperty("zonaEntel.prestaLukaService.codsbad");
    private String mensajeErrorPrestaLuka;
    private boolean validoPrestaLuka=true;
    
    
    public boolean isValidoPrestaLuka() {
		return validoPrestaLuka;
	}

	public String getMensajeErrorPrestaLuka() {
		return mensajeErrorPrestaLuka;
	}
	
	public OfertaBolsaBlindaje getBolsaBlindaje() {
		return ofertaBolsaBlindaje;
	}
	
	public void marcaOfertaBlindajeContratada(long codOferta ){
		bolsaBlindajeDAO.marcaOfertaBlindajeContratada(codOferta);
	}

    public BeneficiosDelegate getBeneficiosDelegate() {
		return beneficiosDelegate;
	}

	public void setBeneficiosDelegate(BeneficiosDelegate beneficiosDelegate) {
		this.beneficiosDelegate = beneficiosDelegate;
	}

	/**
     * @param bolsaDelegate
     *            the bolsaDelegate to set
     */
    public void setBolsaDelegate(BolsaDelegate bolsaDelegate) {
        this.bolsaDelegate = bolsaDelegate;
    }

     /**
     * Inicializa las listas de bolsas disponibles, contratadas, disp para regalo
     * y regaladas, estas ultimas si el usuario es SS
     * @param phase
     */
    public void init(final PhaseEvent phase) {
        try {

            if (phase.getPhaseId() == PhaseId.RENDER_RESPONSE) {

                final ProfileWrapper profileWrapper = ProfileWrapperHelper
                        .getProfile(JSFPortletHelper.getRequest());

                final String msisdn = ProfileWrapperHelper.getPropertyAsString(
                        profileWrapper, "numeroPcsSeleccionado");

                final String aaa = ProfileWrapperHelper.getPropertyAsString(
                        profileWrapper, "aaa");

                mercado = ProfileWrapperHelper.getPropertyAsString(
                        profileWrapper, "mercado");
                
                userAdmin = MiEntelBusinessHelper.isAAATitular(aaa);
                
                String flagBam = ProfileWrapperHelper.getPropertyAsString(profileWrapper,"flagBam");
                String rutSeleccionado = ProfileWrapperHelper.getPropertyAsString(profileWrapper,"rutUsuarioSeleccionado");
                
                PortletPreferences prefs = JSFPortletHelper.getPreferencesObject();
                String bolsasOcultas = JSFPortletHelper.getPreference(prefs, "bolsasOcultas", "");
                
                
                // Debe continuar la ejecucion asi no existan bolsas contradas
                // ni disponibles.
                try {
                	
                	//Buscar si tiene bolsa Recomendada blindaje
                	boolean tieneOferta = suscripcionesDelegate.movilTieneOferta(msisdn);
                	
                	if(tieneOferta)
                	//recuperar oferta
                	ofertaBolsaBlindaje = suscripcionesDelegate.obtenerBolsaBlindaje(msisdn);
                	
                    //Buscar bolsas disponibles
                    bolsasActualesDisponiblesBean = suscripcionesDelegate
                            .consultarBolsasActualesDisponibles(msisdn);

                    
                    bolsasActuales = bolsasActualesDisponiblesBean
                            .getBolsasActuales();

                    // Se eliminan las bolsas IM
                    bolsasActuales = filtroBolsasIM(bolsasActuales);

                    existenBolsasContratadas = (bolsasActuales != null && !bolsasActuales
                            .isEmpty());
                    
                    existeBolsaRecomendada = (ofertaBolsaBlindaje != null);
                    
                    try{
                    //Se comparan bolsas disponibles con bolsa recomendada blindaje
                    if(existeBolsaRecomendada && bolsasActuales != null){
                    	if(!bolsasActuales.isEmpty()){
                    		for(int i=0; i<bolsasActuales.size(); i++){
                    			codigoBolsaDisponible = bolsasActuales.get(i).getSnCodigo().toString().trim();
                    			codigoBolsaRecomendada = ofertaBolsaBlindaje.getCodigoBolsa().trim();
                    			if(codigoBolsaDisponible.compareTo(codigoBolsaRecomendada) == 0){
                            		existeBolsaRecomendadaDisponible = true;
                    			}
                    		}                    		
                    	}
                    }
                    }
                    catch(Exception ex){
                    	existeBolsaRecomendadaDisponible = true;
                        LOGGER.warn(
                                "Ocurrio un error al comparar la bolsa recomendada con las bolsas disponibles, por tanto se mostrara igualmente la bolsa recomendada :"
                                        + msisdn, ex);
                    }          
                    

                                        
                    String[] otrasBolsas = OTRAS_BOLSAS.split(",");
                    List otrasBolsasList = new ArrayList();
                    otrasBolsasList = Arrays.asList(otrasBolsas);
                    
                    // Agrupar Bolsas por tipo.
                    	bolsasActualesDisponiblesBean.setBolsasDisponibles(filtroBolsasROA(bolsasActualesDisponiblesBean.getBolsasDisponibles(), bolsasActuales, msisdn, mercado, aaa));
                    grupoBolsasDisponibles = buildGruposBolsa(bolsasActualesDisponiblesBean
                            .getBolsasDisponibles(), bolsasOcultas, otrasBolsasList);
                    
                    

                    existenBolsasDisp = (grupoBolsasDisponibles != null && !grupoBolsasDisponibles
                            .isEmpty());
                  
                } catch (Exception e) {
                	LOGGER.error(e);
                    LOGGER.warn(
                            "No existen bolsas contratadas ni disponibles para :"
                                    + msisdn, e);
                    if (MiEntelBusinessHelper.isMercadoSuscripcion(mercado)) {
                    	EstadisticasHelper.agregarMarcaEstadistica(EstadisticasHelper.TRANSACCION_NO_OK, EstadisticasHelper.GRUPO_BOLSAS, EstadisticasHelper.BOLSAS_PANTALLA_CONTRATAR_SS);
                    } else if (MiEntelBusinessHelper.isMercadoCuentaControlada(mercado)) {
                    	EstadisticasHelper.agregarMarcaEstadistica(EstadisticasHelper.TRANSACCION_NO_OK, EstadisticasHelper.GRUPO_BOLSAS, EstadisticasHelper.BOLSAS_PANTALLA_COMPRAR_CC);
                    }                    
                }
                
                // Solo para mercados PP (voz). Posiblemente mas adelante para CC
                /*
                if(MiEntelBusinessHelper.isMercadoCuentaControlada(mercado)){
                	
                   RutBean rutBean = new RutBean(rutSeleccionado);
                   zonaPerfilBean = beneficiosDelegate.getZonaPerfil(msisdn, rutBean.getNumero()+"0"+rutBean.getDigito());                   
                    
                   String[] codsBad = CODSBAD_PRESTALUKA.split(",");
            		for(String cod : codsBad){
            			if(zonaPerfilBean.getStatusRespuesta().equals(cod)){
            				validoPrestaLuka = false;
            				break;
            			}
            		}
                    
                    if(!isValidoPrestaLuka()){                 	                    
                		mensajeErrorPrestaLuka = MiEntelProperties.getServiceMessages().getErrorMessage("bolsasContratar.prestalukaNoValido");                  	              
                    }
                }
                */
                
                // Consultar Bolas Regaladas y dispon para regalo solo para SS
                if (MiEntelBusinessHelper.isMercadoSuscripcion(mercado)) {
                    
                    // Visualizar tabs para ss
                    mercadoSuscripcion = true;
                    // Consultar Bolsas Regaladas
                    bolsasRegaladas = consultarBolsasRegaladas(msisdn);
                    existenBolsasRegaladas = (bolsasRegaladas != null && !bolsasRegaladas.isEmpty());
                    // Consultar Bolsa disponibles para regalo.
                    bolsasDispRegalo = consultarBolsasDisponibleRegalo(msisdn,
                            mercado);
                    existenBolsasDispReg = (bolsasDispRegalo != null && !bolsasDispRegalo.isEmpty());

                } else if(MiEntelBusinessHelper.isMercadoCuentaControlada(mercado)) {
                    
                    bolsasActualesPP = bolsaDelegate.obtenerBolsasOneShot(msisdn);
                    existenbolsasActualesPP = (bolsasActualesPP != null && !bolsasActualesPP.isEmpty());
                }
                
                if (MiEntelBusinessHelper.isMercadoSuscripcion(mercado)) {
                	EstadisticasHelper.agregarMarcaEstadistica(EstadisticasHelper.TRANSACCION_OK, EstadisticasHelper.GRUPO_BOLSAS, EstadisticasHelper.BOLSAS_PANTALLA_CONTRATAR_SS);
                } else if (MiEntelBusinessHelper.isMercadoCuentaControlada(mercado)) {
                	EstadisticasHelper.agregarMarcaEstadistica(EstadisticasHelper.TRANSACCION_OK, EstadisticasHelper.GRUPO_BOLSAS, EstadisticasHelper.BOLSAS_PANTALLA_COMPRAR_CC);
                }
            }

        } catch (DAOException ex) {
            LOGGER.error("DAOException al consultar bolsas", ex);
            JSFMessagesHelper.addServiceErrorMessage("serviceDisabled");
            if (MiEntelBusinessHelper.isMercadoSuscripcion(mercado)) {
            	EstadisticasHelper.agregarMarcaEstadistica(EstadisticasHelper.TRANSACCION_NO_OK, EstadisticasHelper.GRUPO_BOLSAS, EstadisticasHelper.BOLSAS_PANTALLA_CONTRATAR_SS);
            } else if (MiEntelBusinessHelper.isMercadoCuentaControlada(mercado)) {
            	EstadisticasHelper.agregarMarcaEstadistica(EstadisticasHelper.TRANSACCION_NO_OK, EstadisticasHelper.GRUPO_BOLSAS, EstadisticasHelper.BOLSAS_PANTALLA_COMPRAR_CC);
            }          
        } catch (ServiceException e) {
            LOGGER.info("ServiceException codigo: " + e.getCodigoRespuesta()
                    + " - " + e.getDescripcionRespuesta());
            JSFMessagesHelper.addErrorCodeMessage("clienteOrden", e
                    .getCodigoRespuesta());
            if (MiEntelBusinessHelper.isMercadoSuscripcion(mercado)) {
            	EstadisticasHelper.agregarMarcaEstadistica(EstadisticasHelper.TRANSACCION_NO_OK, EstadisticasHelper.GRUPO_BOLSAS, EstadisticasHelper.BOLSAS_PANTALLA_CONTRATAR_SS);
            } else if (MiEntelBusinessHelper.isMercadoCuentaControlada(mercado)) {
            	EstadisticasHelper.agregarMarcaEstadistica(EstadisticasHelper.TRANSACCION_NO_OK, EstadisticasHelper.GRUPO_BOLSAS, EstadisticasHelper.BOLSAS_PANTALLA_COMPRAR_CC);
            }          
        } catch (Exception e) {
            LOGGER.error("Exception inesperado al consultar bolsas", e);
            JSFMessagesHelper.addServiceErrorMessage("serviceDisabled");
            if (MiEntelBusinessHelper.isMercadoSuscripcion(mercado)) {
            	EstadisticasHelper.agregarMarcaEstadistica(EstadisticasHelper.TRANSACCION_NO_OK, EstadisticasHelper.GRUPO_BOLSAS, EstadisticasHelper.BOLSAS_PANTALLA_CONTRATAR_SS);
            } else if (MiEntelBusinessHelper.isMercadoCuentaControlada(mercado)) {
            	EstadisticasHelper.agregarMarcaEstadistica(EstadisticasHelper.TRANSACCION_NO_OK, EstadisticasHelper.GRUPO_BOLSAS, EstadisticasHelper.BOLSAS_PANTALLA_COMPRAR_CC);
            }         
        }
    }
    
     /**
     * 
     * @param msisdn
     */
    private List<BolsaBean> consultarBolsasRegaladas(final String msisdn) {
        List<BolsaBean> bolsasRegaladasTemp = new ArrayList<BolsaBean>();
        try {

            bolsasRegaladasTemp = bolsaDelegate
                    .consultarBolsasRegaladasSS(msisdn);

        } catch (DAOException ex) {
            LOGGER.error("DAOException al consultar bolsas Regaladas", ex);
            JSFMessagesHelper.addServiceErrorMessage("serviceDisabled");
        } catch (ServiceException e) {
            LOGGER.info("ServiceException msisdn: "+msisdn+" - codigo: " + e.getCodigoRespuesta()
                    + " - " + e.getDescripcionRespuesta());
            JSFMessagesHelper.addErrorCodeMessage("clienteOrden", e
                    .getCodigoRespuesta());

        } catch (Exception e) {
            LOGGER.error("Exception al consultar bolsas Regaladas", e);
            JSFMessagesHelper.addServiceErrorMessage("serviceDisabled");
        }
        return bolsasRegaladasTemp;
    }

    /**
     * 
     * @param msisdn
     */
    private List<BolsaBean> consultarBolsasDisponibleRegalo(final String msisdn,
            final String mercado) {
        List<BolsaBean> bolsasDispoRegaladoTemp = new ArrayList<BolsaBean>();
        try {

            final String flagMercado = MiEntelProperties
                    .getProperty("parametros.flagmercado.".concat(mercado));
            LOGGER.info("flagMercado: " + flagMercado);
            bolsasDispoRegaladoTemp = suscripcionesDelegate
                    .consultarBolsasDisponiblesRegalo(msisdn, flagMercado);

        } catch (DAOException ex) {
            LOGGER.error("DAOException al consultar bolsas Regaladas", ex);
            JSFMessagesHelper.addServiceErrorMessage("serviceDisabled");
        } catch (ServiceException e) {
            LOGGER.info("ServiceException numero: "+msisdn+" - codigo: " + e.getCodigoRespuesta()
                    + " - " + e.getDescripcionRespuesta());
            JSFMessagesHelper.addErrorCodeMessage("clienteOrden", e
                    .getCodigoRespuesta());
        } catch (Exception e) {
            LOGGER.error("Exception al consultar bolsas Regaladas", e);
            JSFMessagesHelper.addServiceErrorMessage("serviceDisabled");
        }
        return bolsasDispoRegaladoTemp;
    }

    /**
     * Agrupa las bolsas por tipo.
     * 
     * @param listBolsas
     * @return List<GrupoBolsaBean>
     */
    private List<GrupoBolsaBean> buildGruposBolsa(List<BolsaBean> listBolsas, String bolsasOcultas, List otrasBolsasList) {
        List<GrupoBolsaBean> listGrupoBolsa = new ArrayList<GrupoBolsaBean>();
        // Tipos de Bolsa.
        List<CodeDescBean> tiposBolsas = ParametrosHelper.getListTiposBolsa();

        GrupoBolsaBean grupoBolsa;
        List<BolsaBean> listTemp;
        for (CodeDescBean codeDescBean : tiposBolsas) {
            grupoBolsa = new GrupoBolsaBean();
            grupoBolsa.setTipoBolsa(codeDescBean.getDescripcion());
            grupoBolsa.setTipoBolsaSinAcento(Utils.removerAcentos(codeDescBean.getDescripcion()));
            listTemp = new ArrayList<BolsaBean>();
            Iterator<BolsaBean> itetator = listBolsas.iterator();
            while (itetator.hasNext()) {
                BolsaBean bolsa = itetator.next();
                if (MiEntelBusinessHelper.isMercadoSuscripcion(mercado) || MiEntelBusinessHelper.isMercadoCuentaControlada(mercado)) {
                if (codeDescBean.getCodigo().contains(bolsa.getTipoBolsa())) {
                	if (!bolsasOcultas.contains(bolsa.getSnCodigo())) {
	                		if(!otrasBolsasList.contains(bolsa.getSnCodigo())){
                		listTemp.add(bolsa);
                    	itetator.remove();
                	}
                }
            }
	                if (codeDescBean.getCodigo().equals("OTR")) {
	                	if(otrasBolsasList.contains(bolsa.getSnCodigo())){
	                		listTemp.add(bolsa);
	                		itetator.remove();
	                	}
	                }
                }else{
                	if (codeDescBean.getCodigo().contains(bolsa.getTipoBolsa())) {
	                	if (!bolsasOcultas.contains(bolsa.getSnCodigo())) {
	                		listTemp.add(bolsa);
	                    	itetator.remove();	                		
	                	}
	                }
                }
	                
            }
            grupoBolsa.setBolsasDisponibles(listTemp);
            // Si el grupo tiene bolsas, se agregan para ser visualizadas.
            if (!listTemp.isEmpty()) {
                listGrupoBolsa.add(grupoBolsa);
            }
        }
        return listGrupoBolsa;
    }
    
    /**
     * Quita del listado las bolsas q son IM
     * @param listBolsas
     * @return
     */
    private List<BolsaBean> filtroBolsasIM(List<BolsaBean> listBolsas) {
        if(listBolsas != null){
        Iterator<BolsaBean> itetator = listBolsas.iterator();
            while (itetator.hasNext()) {
                BolsaBean bolsa = itetator.next();
                if (BOLSAS_BAM.contains(bolsa.getTipoBolsa())) {
                    itetator.remove();
                }
            }
        }
        return listBolsas;
    }
    
    /**
     * Punto de entrada para todas las peticiones ajax
     * @param phase
     */
    public void initProcesarAjax(final PhaseEvent phase){
        String accion = JsfUtil.getRequestParameter("accion");
        if(accion.equals("contratar")){
            this.contratarBolsaSS(); 
        }else if(accion.equals("contratarBlindaje")){
            this.contratarBolsaBlindajeSS(); 
        }else if(accion.equals("regalar")){
            this.regalarBolsaSS();       
        }else if(accion.equals("rechazarOferta")){
            this.rechazarOfertaBlindajeSS();            
    }
    }

    /**
     * Contratar Bolsas SS y CC
     * 
     * @param phase
     */
    private void contratarBolsaSS() {
        try {
            
            String codigoBolsa = JsfUtil.getRequestParameter("codigoBolsa");
            String valorBolsa = JsfUtil.getRequestParameter("valorBolsa");
            String tipoBolsa = JsfUtil.getRequestParameter("tipoBolsa");
            String nombreBolsa = JsfUtil.getRequestParameter("nombreBolsa");
        
            ResultadoContratarBolsaBean resulContratarBolsaBean; 
        
            final ProfileWrapper profileWrapper = ProfileWrapperHelper
                    .getProfile(JSFPortletHelper.getRequest());

            final String msisdn = ProfileWrapperHelper.getPropertyAsString(profileWrapper,
                    "numeroPcsSeleccionado");
            
             final String TIPO_ACTIVACION = MiEntelProperties
            .getProperty("parametros.bolsa.tipoactivacion");

            // Se valida la simultaneidad de bolsas.
            SimultaneidadBolsasBean simultaneidadBolsasBean = bolsaDelegate
                    .validarSimultaneidadBolsaCC(msisdn, codigoBolsa);
            
            if (!simultaneidadBolsasBean.isFlagDuplicidad()) {
            	String msj = "";
            	
                //Si es bby se usa un servicio diferente para contratar
                if (BLACKBERRY.contains(tipoBolsa)) {
                    bolsaDelegate.contratarBolsaBBerrySSCC(msisdn, codigoBolsa);
                    msj = MiEntelProperties.getServiceMessages().getRb().getString("success.clienteOrden.bolsacontratadabby");
                    respuestaJson = JsonHelper
                            .toJsonResponse(msj);
                }
                else {
                    resulContratarBolsaBean = bolsaDelegate.contratarBolsaSSCC(
                            msisdn, codigoBolsa,
                            Double.parseDouble(valorBolsa), TIPO_ACTIVACION);
                    msj = MiEntelProperties.getServiceMessages().getRb().getString("success.clienteOrden.bolsacontratada".concat(resulContratarBolsaBean.getTipoActivacion().toLowerCase()));
                }
                
                TransaccionGTMBean transGTM = new TransaccionGTMBean();
                transGTM.setIdTransaccion(msisdn.substring(msisdn.length() - 4));
                transGTM.setSkuID(codigoBolsa);
                transGTM.setNombre(nombreBolsa);
                transGTM.setNuevoValor(valorBolsa);
                transGTM.setCostoOperacional(0);
                transGTM.setNumeroPlanes(1);
                transGTM.setNumeroOperaciones(1);                 
                transGTM.setTipoProducto(tipoBolsa);
                transGTM.setValorTransaccion(Double.parseDouble(valorBolsa) + transGTM.getCostoOperacional());
                
                respuestaJson = JsonHelper.toJsonGTMResponse(transGTM, msj);
                
				EstadisticasHelper.agregarMarcaEstadistica(EstadisticasHelper.TRANSACCION_OK, EstadisticasHelper.GRUPO_BOLSAS, EstadisticasHelper.BOLSAS_CONFIRMAR_CONTRATAR_SSCC);
            }
            else {
                respuestaJson = JsonHelper
                        .toJsonServiceErrorMessage("bolsasduplicadas");
            }
        } catch (DAOException e) {
        	LOGGER.error("DAOException al obtener datos del plan", e);
            respuestaJson = JsonHelper
                    .toJsonServiceErrorMessage("nocontratabolsa");
            EstadisticasHelper.agregarMarcaEstadistica(EstadisticasHelper.TRANSACCION_NO_OK, EstadisticasHelper.GRUPO_BOLSAS, EstadisticasHelper.BOLSAS_CONFIRMAR_CONTRATAR_SSCC);
        } catch (ServiceException e) {
            LOGGER.info("ServiceException codigo: " + e.getCodigoRespuesta()
                    + " - " + e.getDescripcionRespuesta());
            respuestaJson = JsonHelper.toJsonServiceErrorMessage(
                    "clienteOrden", e.getCodigoRespuesta());
            EstadisticasHelper.agregarMarcaEstadistica(EstadisticasHelper.TRANSACCION_NO_OK, EstadisticasHelper.GRUPO_BOLSAS, EstadisticasHelper.BOLSAS_CONFIRMAR_CONTRATAR_SSCC);            
        } catch (Exception e) {
            LOGGER.error("Exception inesperado al obtener datos del plan", e);
            respuestaJson = JsonHelper
                    .toJsonServiceErrorMessage("nocontratabolsa");
            EstadisticasHelper.agregarMarcaEstadistica(EstadisticasHelper.TRANSACCION_NO_OK, EstadisticasHelper.GRUPO_BOLSAS, EstadisticasHelper.BOLSAS_CONFIRMAR_CONTRATAR_SSCC);            
        }
    }

    private void contratarBolsaBlindajeSS() {
        try {
            
            String codigoBolsa = JsfUtil.getRequestParameter("codigoBolsa");
            String valorBolsa = JsfUtil.getRequestParameter("valorBolsa");
            String tipoBolsa = JsfUtil.getRequestParameter("tipoBolsa");
            String codOferta = JsfUtil.getRequestParameter("codOferta");
        
            ResultadoContratarBolsaBean resulContratarBolsaBean; 
        
            final ProfileWrapper profileWrapper = ProfileWrapperHelper
                    .getProfile(JSFPortletHelper.getRequest());

            final String msisdn = ProfileWrapperHelper.getPropertyAsString(profileWrapper,
                    "numeroPcsSeleccionado");
            
             final String TIPO_ACTIVACION = MiEntelProperties
            .getProperty("parametros.bolsa.tipoactivacion");

            // Se valida la simultaneidad de bolsas.
            SimultaneidadBolsasBean simultaneidadBolsasBean = bolsaDelegate
                    .validarSimultaneidadBolsaCC(msisdn, codigoBolsa);
            
            if (!simultaneidadBolsasBean.isFlagDuplicidad()) {
                //Si es bby se usa un servicio diferente para contratar
                if (BLACKBERRY.contains(tipoBolsa)) {
                    bolsaDelegate.contratarBolsaBBerrySSCC(msisdn, codigoBolsa);
                    String msj = MiEntelProperties.getServiceMessages().getRb().getString("success.clienteOrden.bolsacontratadabby");
                    respuestaJson = JsonHelper
                            .toJsonResponse(msj);
                }
                else {
                    resulContratarBolsaBean = bolsaDelegate.contratarBolsaSSCC(
                            msisdn, codigoBolsa,
                            Double.parseDouble(valorBolsa), TIPO_ACTIVACION);
                    try
                    {
                    	cantidad = Integer.valueOf(resulContratarBolsaBean.getCantidad());               	
                    }
                    catch(Exception ex)
                    {}
                    if(cantidad==0)bolsaDelegate.marcaOfertaBlindajeContratada(Long.valueOf(codOferta));
                    
                    String msj = MiEntelProperties.getServiceMessages().getRb().getString("success.clienteOrden.bolsacontratada".concat(resulContratarBolsaBean.getTipoActivacion().toLowerCase()));
                    respuestaJson = JsonHelper.toJsonResponse(msj);
                }
				EstadisticasHelper.agregarMarcaEstadistica(EstadisticasHelper.TRANSACCION_OK, EstadisticasHelper.GRUPO_BOLSAS, EstadisticasHelper.BOLSAS_CONFIRMAR_CONTRATAR_SSCC);
            }
            else {
                respuestaJson = JsonHelper
                        .toJsonServiceErrorMessage("bolsasduplicadas");
            }

        } catch (DAOException e) {
        	LOGGER.error("DAOException al obtener datos del plan", e);
            respuestaJson = JsonHelper
                    .toJsonServiceErrorMessage("nocontratabolsa");
            EstadisticasHelper.agregarMarcaEstadistica(EstadisticasHelper.TRANSACCION_NO_OK, EstadisticasHelper.GRUPO_BOLSAS, EstadisticasHelper.BOLSAS_CONFIRMAR_CONTRATAR_SSCC);
        } catch (ServiceException e) {
            LOGGER.info("ServiceException codigo: " + e.getCodigoRespuesta()
                    + " - " + e.getDescripcionRespuesta());
            respuestaJson = JsonHelper.toJsonServiceErrorMessage(
                    "clienteOrden", e.getCodigoRespuesta());
            EstadisticasHelper.agregarMarcaEstadistica(EstadisticasHelper.TRANSACCION_NO_OK, EstadisticasHelper.GRUPO_BOLSAS, EstadisticasHelper.BOLSAS_CONFIRMAR_CONTRATAR_SSCC);            
        } catch (Exception e) {
            LOGGER.error("Exception inesperado al obtener datos del plan", e);
            respuestaJson = JsonHelper
                    .toJsonServiceErrorMessage("nocontratabolsa");
            EstadisticasHelper.agregarMarcaEstadistica(EstadisticasHelper.TRANSACCION_NO_OK, EstadisticasHelper.GRUPO_BOLSAS, EstadisticasHelper.BOLSAS_CONFIRMAR_CONTRATAR_SSCC);            
        }
    }
    
    private void rechazarOfertaBlindajeSS() {
    	
    	try{
        String codOferta = JsfUtil.getRequestParameter("codOferta");
    	
        bolsaDelegate.rechazaOfertaBlindaje(Long.valueOf(codOferta));
        
    	
    } catch (Exception e) {
        LOGGER.error("Exception inesperado al rechazar la oferta de contratacion de la bolsa", e);
        respuestaJson = JsonHelper
                .toJsonServiceErrorMessage("norechazabolsa");
        EstadisticasHelper.agregarMarcaEstadistica(EstadisticasHelper.TRANSACCION_NO_OK, EstadisticasHelper.GRUPO_BOLSAS, EstadisticasHelper.BOLSAS_CONFIRMAR_CONTRATAR_SSCC);            
    }
  }
    
    
    /**
     * Recibe un numero PP por request
     * Regalar Bolsa SS
     */
    private void regalarBolsaSS() {
        try {

            String msisdnTo = JsfUtil.getRequestParameter("numeroPrepago");
            String codigoBolsa = JsfUtil.getRequestParameter("codigoBolsa");
            String valorBolsa = JsfUtil.getRequestParameter("valorBolsa");
            
            final ProfileWrapper profileWrapper = ProfileWrapperHelper
                    .getProfile(JSFPortletHelper.getRequest());

            final String msisdnFrom = ProfileWrapperHelper.getPropertyAsString(profileWrapper,
                    "numeroPcsSeleccionado");
            
            final String mercado = ProfileWrapperHelper.getPropertyAsString(
                    profileWrapper, "mercado");
            
            String tipoMercado = MiEntelProperties
                    .getProperty("parametros.flagmercado.".concat(mercado));

            // En caso de no poder regalar la bolsa, se levanta una excepcion.
            bolsaDelegate.validarCompraBolsaBolsaRegaloSSCC(msisdnFrom,
                    msisdnTo, tipoMercado, codigoBolsa, Double
                            .parseDouble(valorBolsa));

            // Puede regalar la bolsa.
            bolsaDelegate.comprarBolsaRegaloSSCC(msisdnFrom, msisdnTo,
                    tipoMercado, codigoBolsa, Double.parseDouble(valorBolsa));

            respuestaJson = JsonHelper.toJsonResponse("Ok");

        } catch (DAOException e) {
        	LOGGER.error("DAOException caught", e);
            respuestaJson = JsonHelper
                    .toJsonServiceErrorMessage("noregalabolsa");
        } catch (ServiceException e) {
            LOGGER.info("Error de servicio codigo: " + e.getCodigoRespuesta()
                    + " - " + e.getDescripcionRespuesta());
            respuestaJson = JsonHelper.toJsonServiceErrorMessage(
                    "clienteOrden", e.getCodigoRespuesta());
        } catch (Exception e) {
        	LOGGER.error("Exception caught: "+e.getMessage(), e);
            respuestaJson = JsonHelper
                    .toJsonServiceErrorMessage("noregalabolsa");
        }
    }

    /* NUEVO TECNOVA 13/12/2013*/
    
    private List<BolsaBean> filtroBolsasROA(List<BolsaBean> listBolsasDisp,List<BolsaBean> listBolsasAct, String numeroPcsSeleccionado, String mercado,String aaa) throws DAOException, ServiceException {
    	
    	ResumenEquipoBean resumenEquipoBean;
    	EquipoDelegate equipoDelegate = new EquipoDelegate();
    	equipoDelegate.setEquipoDAO(new EquipoDAO());
    	//List<BolsaBean> listGrupoBolsa = new ArrayList<BolsaBean>();
    	
    	PlanDelegate planDelegate = new PlanDelegate();
    	planDelegate.setPlanDAO(new PlanDAO());
    	String plan = "";
    	//PlanHelper helper = new PlanHelper();
    	
            if(listBolsasDisp != null){
            Iterator<BolsaBean> itetator = listBolsasDisp.iterator();
                while (itetator.hasNext()) {
                    BolsaBean bolsa = itetator.next();
                    if (BOLSAS_ROA.contains(bolsa.getTipoBolsa())) {
                        //itetator.remove();
    					resumenEquipoBean = equipoDelegate.getResumenEquipo(numeroPcsSeleccionado);
    					if(("BlackBerry").equals(resumenEquipoBean.getMarca())){
    						//resumenProductosContratadosBean = producDelegate.getProductosContratados(mercado, numeroPcsSeleccionado);
    						plan = planDelegate.getPlanActualSSCC(numeroPcsSeleccionado, mercado, aaa).getCodbscs2();
    						if(PlanHelper.isPlanBberryMMTodoDestino(plan) || PlanHelper.isPlanBBMMCuentaControlada(plan) || hasBolsaIM(listBolsasAct)){
    						     
    							 
    							 if(!("1353").equals(bolsa.getSnCodigo()) && !("1354").equals(bolsa.getSnCodigo()) && !("1355").equals(bolsa.getSnCodigo())){
    								itetator.remove();
    							 }
    						}else if(PlanHelper.isPlanBBNoMM(plan)){
    						    if(!("885").equals(bolsa.getSnCodigo()) && !("585").equals(bolsa.getSnCodigo())){
    								itetator.remove();
    							 }
    						}else{
    							itetator.remove();
    						}
    					}else{
    							if(!("1412").equals(bolsa.getSnCodigo()) && !("1413").equals(bolsa.getSnCodigo()) && !("1414").equals(bolsa.getSnCodigo()) && !("1415").equals(bolsa.getSnCodigo())){
    								itetator.remove();
    							}
    					
    					}
                    }
                }
            }
            return listBolsasDisp;
        }
    	
    	
    	
    	
    	    private boolean hasBolsaIM(List<BolsaBean> listBolsas) {
            if(listBolsas != null){
            Iterator<BolsaBean> itetator = listBolsas.iterator();
                while (itetator.hasNext()) {
                    BolsaBean bolsa = itetator.next();
                    if (BOLSAS_BAM.contains(bolsa.getTipoBolsa())) {
                        return true;
                    }
                }
            }
            return false;
        }
    	    
    	/* FIN NUEVO TECNOVA 13/12/2013*/

    public String getRespuestaJson() {
        return respuestaJson;
    }

    /**
     * @return the bolsasActualesDisponiblesBean
     */
    public BolsasActualesDisponiblesBean getBolsasActualesDisponiblesBean() {
        return bolsasActualesDisponiblesBean;
    }

    /**
     * @return the userAdmin
     */
    public boolean isUserAdmin() {
        return userAdmin;
    }

    /**
     * @return the bolsasActuales
     */
    public List<BolsaBean> getBolsasActuales() {
        return bolsasActuales;
    }


    /**
     * @return the grupoBolsasDisponibles
     */
    public List<GrupoBolsaBean> getGrupoBolsasDisponibles() {
        return grupoBolsasDisponibles;
    }

    /**
     * @return the bolsasRegaladas
     */
    public List<BolsaBean> getBolsasRegaladas() {
        return bolsasRegaladas;
    }

    /**
     * @return the bolsasDispRegalo
     */
    public List<BolsaBean> getBolsasDispRegalo() {
        return bolsasDispRegalo;
    }

    /**
     * @return the existenBolsasDisp
     */
    public boolean isExistenBolsasDisp() {
        return existenBolsasDisp;
    }

    /**
     * @return the existenBolsasDispReg
     */
    public boolean isExistenBolsasDispReg() {
        return existenBolsasDispReg;
    }

    /**
     * @param suscripcionesDelegate
     *            the suscripcionesDelegate to set
     */
    public void setSuscripcionesDelegate(
            SuscripcionesDelegate suscripcionesDelegate) {
        this.suscripcionesDelegate = suscripcionesDelegate;
    }

    /**
     * @return the existenBolsasRegaladas
     */
    public boolean isExistenBolsasRegaladas() {
        return existenBolsasRegaladas;
    }

    /**
     * @return the mercadoSuscripcion
     */
    public boolean isMercadoSuscripcion() {
        return mercadoSuscripcion;
    }

    /**
     * @return the existenBolsasContratadas
     */
    public boolean isExistenBolsasContratadas() {
        return existenBolsasContratadas;
    }
    
    public boolean isExistenBolsasRecomendada() {
        return existeBolsaRecomendada;
    }
    
    public boolean isExistenBolsasRecomendadaDisponible() {
        return existeBolsaRecomendadaDisponible;
    }

    /**
     * @return the bolsasActualesPP
     */
    public List<BolsaPPBean> getBolsasActualesPP() {
        return bolsasActualesPP;
    }

    /**
     * @return the existenbolsasActualesPP
     */
    public boolean isExistenbolsasActualesPP() {
        return existenbolsasActualesPP;
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
}