/* Propiedad de Entel PCS. Todos los derechos reservados */
package com.epcs.cliente.perfil.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import com.epcs.bean.HistorialComunik2Bean;
import com.epcs.bean.NumeroFrecuenteBean;
import com.epcs.bean.Plan4GLteBean;
import com.epcs.bean.PlanBean;
import com.epcs.bean.PlanDisponibleBean;
import com.epcs.bean.PlanPPBean;
import com.epcs.bean.PlanesFullBean;
import com.epcs.bean.PlanesMultimediaBean;
import com.epcs.bean.ResumenPlan;
import com.epcs.bean.SlotBean;
import com.epcs.bean.SolicitudComunik2Bean;
import com.epcs.cliente.perfil.ClientePerfilService;
import com.epcs.cliente.perfil.ClientePerfilServicePortType;
import com.epcs.cliente.perfil.types.AdministrarSolicitudPlanComunik2PPType;
import com.epcs.cliente.perfil.types.AgregarModificarNumeroFrecuentePPType;
import com.epcs.cliente.perfil.types.CambioPlanComunik2PPType;
import com.epcs.cliente.perfil.types.CambioPlanPPType;
import com.epcs.cliente.perfil.types.CambioPlanSSCCType;
import com.epcs.cliente.perfil.types.ConsultarHistorialPlanComunik2PPType;
import com.epcs.cliente.perfil.types.ConsultarPlanActualPPType;
import com.epcs.cliente.perfil.types.ConsultarPlanActualType;
import com.epcs.cliente.perfil.types.ConsultarPlanFullActualSSCCType;
import com.epcs.cliente.perfil.types.ConsultarPlanResumenCCType;
import com.epcs.cliente.perfil.types.ConsultarPlanResumenPPType;
import com.epcs.cliente.perfil.types.ConsultarPlanesDisponiblesPPType;
import com.epcs.cliente.perfil.types.ConsultarPlanesDisponiblesSSCCType;
import com.epcs.cliente.perfil.types.ConsultarSolicitudesActivasPPType;
import com.epcs.cliente.perfil.types.ConsultarUsuarioBuicType;
import com.epcs.cliente.perfil.types.ConsultarXmlPlanesFullType;
import com.epcs.cliente.perfil.types.DetallePlanActualType;
import com.epcs.cliente.perfil.types.EliminarNumeroFrecuentePPType;
import com.epcs.cliente.perfil.types.HistorialPlanComunik2ItemType;
import com.epcs.cliente.perfil.types.NumerosFrecuentesPPType;
import com.epcs.cliente.perfil.types.PlanActualPPType;
import com.epcs.cliente.perfil.types.PlanesDisponiblesPPType;
import com.epcs.cliente.perfil.types.PlanesDisponiblesSSCCType;
import com.epcs.cliente.perfil.types.PlanesPPType;
import com.epcs.cliente.perfil.types.ResPlanResumenCCType;
import com.epcs.cliente.perfil.types.ResPlanResumenPPType;
import com.epcs.cliente.perfil.types.ResultadoAdministrarSolicitudPlanComunik2PPType;
import com.epcs.cliente.perfil.types.ResultadoAgregarModificarNumeroFrecuentePPType;
import com.epcs.cliente.perfil.types.ResultadoCambioPlanComunik2PPType;
import com.epcs.cliente.perfil.types.ResultadoCambioPlanPPType;
import com.epcs.cliente.perfil.types.ResultadoCambioPlanSSCCType;
import com.epcs.cliente.perfil.types.ResultadoConsultaXmlplanesfullType;
import com.epcs.cliente.perfil.types.ResultadoConsultarHistorialPlanComunik2PPType;
import com.epcs.cliente.perfil.types.ResultadoConsultarPlanActualPPType;
import com.epcs.cliente.perfil.types.ResultadoConsultarPlanFullActualSSCCType;
import com.epcs.cliente.perfil.types.ResultadoConsultarPlanResumenCCType;
import com.epcs.cliente.perfil.types.ResultadoConsultarPlanResumenPPType;
import com.epcs.cliente.perfil.types.ResultadoConsultarPlanesDisponiblesPPType;
import com.epcs.cliente.perfil.types.ResultadoConsultarPlanesDisponiblesSSCCType;
import com.epcs.cliente.perfil.types.ResultadoConsultarSolicitudesActivasPPType;
import com.epcs.cliente.perfil.types.ResultadoConsultarUsuarioBuicType;
import com.epcs.cliente.perfil.types.ResultadoEliminarNumeroFrecuentePPType;
import com.epcs.cliente.perfil.types.ResultadoValidacionBloqueoTemporalType;
import com.epcs.cliente.perfil.types.ResultadoValidacionComercialType;
import com.epcs.cliente.perfil.types.SlotsType;
import com.epcs.cliente.perfil.types.UsuarioBuicType;
import com.epcs.cliente.perfil.types.ValidacionBloqueoTemporalType;
import com.epcs.cliente.perfil.types.ValidacionComercialType;
import com.epcs.cliente.perfil.util.PlanHelper;
import com.epcs.provisionyentrega.suscripcion.datosexcedidos.DatosExcedidosPortType;
import com.epcs.provisionyentrega.suscripcion.datosexcedidos.DatosExcedidosService;
import com.epcs.provisionyentrega.suscripcion.datosexcedidos.entity.ConsultaPlanesMultimediaType;
import com.epcs.provisionyentrega.suscripcion.datosexcedidos.entity.DatosExcedidosType;
import com.epcs.provisionyentrega.suscripcion.datosexcedidos.entity.HeaderInType;
import com.epcs.provisionyentrega.suscripcion.datosexcedidos.response.ConsultaPlanesMultimediaResponseType;
import com.epcs.provisionyentrega.suscripcion.datosexcedidos.response.ResponseConsultaPlanType;
import com.epcs.recursoti.configuracion.DateHelper;
import com.epcs.recursoti.configuracion.MiEntelProperties;
import com.epcs.recursoti.configuracion.ParametrosHelper;
import com.epcs.recursoti.configuracion.Utils;
import com.epcs.recursoti.configuracion.locator.WebServiceLocator;
import com.epcs.recursoti.configuracion.locator.WebServiceLocatorException;
import com.epcs.recursoti.excepcion.DAOException;
import com.epcs.recursoti.excepcion.ServiceException;
import com.esa.clientes.perfilesclientes.serviciolte.ConsultaPlanesLtePortType;
import com.esa.clientes.perfilesclientes.serviciolte.WsConsultaPlanesLteService;
import com.esa.clientes.perfilesclientes.serviciolte.types.ConsultaPlanesLteRequestType;
import com.esa.clientes.perfilesclientes.serviciolte.types.ConsultaPlanesLteResponseType;
import com.esa.clientes.perfilesclientes.serviciolte.types.RequestType;
import com.esa.clientes.perfilesclientes.serviciolte.types.ResponseType;

/**
 * @author jmanzur (I2B) en nombre de Absalon Opazo (Atencion al Cliente,
 *         EntelPcs)
 * 
 */
public class PlanDAO {

    private static final Logger LOGGER = Logger.getLogger(PlanDAO.class);

    public static final String CODIGO_RESPUESTA_OK = MiEntelProperties.getProperty("servicios.codigoRespuesta.exito");   
    public static final String CLASIFICACION_PLAN_EXCEDIDO = MiEntelProperties.getProperty("servicios.codigoRespuesta.clasificacionPlan");
    public static final String CODIGO_PREFIJO_ENTEL = MiEntelProperties.getProperty("prefijo.entel");
    public static final String CODIGO_APPNAME_PLANES_MM_EXCEDIDOS = MiEntelProperties.getProperty("parametro.servicio.planesMMExcedidos.appName");
    public static final String CODIGO_USERNAME_PLANES_MM_EXCEDIDOS = MiEntelProperties.getProperty("parametro.servicio.planesMMExcedidos.userName");
    public static final String MBADI = MiEntelProperties.getProperty("parametro.planesMMExcedidos.MBADI");
    private List<PlanBean> planesList;

	private static final String CODIGO_RESPUESTA_DISPONIBILIDAD4GLTE_OK = MiEntelProperties
			.getProperty("servicios.codigoRespuesta.disponibilidad");
	private static final String CODIGO_APPNAME_DISPONIBIIDAD_4GLTE = MiEntelProperties
			.getProperty("parametro.servicio.disponibilidadPlan4GLte.appName");
	private static final String CODIGO_USERNAME_DISPONIBILIDAD_4GLTE = MiEntelProperties
			.getProperty("parametro.servicio.disponibilidadPlan4GLte.userName");
    
    
    /**
     * Resumen del Plan del Usuario CC
     * 
     * @param cta
     * @param atributoAA
     * @return ResumenPlan
     * @throws DAOException
     * @throws ServiceException
     */
    public ResumenPlan getPlanResumenCC(String cta, String atributoAA)
            throws DAOException, ServiceException {

        ClientePerfilServicePortType port = null;
        ResumenPlan resumenPlan = null;

        LOGGER.info("Instanciando el Port.");
        try {
            port = (ClientePerfilServicePortType) WebServiceLocator
                    .getInstance().getPort(ClientePerfilService.class,
                            ClientePerfilServicePortType.class);

        } catch (WebServiceLocatorException e) {
            LOGGER.error("Error al inicializar el Port "
                    + ClientePerfilService.class);
            LOGGER.error( new DAOException(e));
        }

        LOGGER.info("Configurando parametros de la peticion");
        ConsultarPlanResumenCCType planResumenCCRequest = new ConsultarPlanResumenCCType();
        planResumenCCRequest.setMsisdn(cta);
        planResumenCCRequest.setAaa(atributoAA);

        ResultadoConsultarPlanResumenCCType planResumenCCResponse = null;
        LOGGER.info("Invocando servicio");
        
        try{
        	
	        planResumenCCResponse = port.consultarPlanResumenCC(planResumenCCRequest);
	        
        }catch (Exception e) {
            LOGGER.error("Exception caught on Service invocation: "
           		 + "consultarPlanResumenCC.", e);
            LOGGER.error( new DAOException(e));
        }
        
        ResPlanResumenCCType planResumen = planResumenCCResponse.getResPlanResumenCC();
        
        String codigoRespuesta = planResumenCCResponse.getRespuesta().getCodigo();
        String descripcionRespuesta = planResumenCCResponse.getRespuesta().getDescripcion();

        LOGGER.info("codigoRespuesta " + codigoRespuesta);
        LOGGER.info("descripcionRespuesta " + descripcionRespuesta);

        if (CODIGO_RESPUESTA_OK.equals(codigoRespuesta)) {
            resumenPlan = new ResumenPlan();
            resumenPlan.setNombrePlan(planResumen.getNombrePlan());

            String saldo = planResumen.getSaldo();
            String fechaExp = planResumen.getFechaExpiracion();
            resumenPlan.setSaldo(new Double(saldo));

            Date fechaExpiracion = DateHelper.parseDate(fechaExp, DateHelper.FORMAT_yyyyMMddhhmmss);

            if (fechaExpiracion == null) {
                LOGGER.error( new DAOException(
                        "fechaEmision no contiene formato esperado: '"
                                + DateHelper.FORMAT_yyyyMMddhhmmss + "' - "
                                + planResumen.getFechaExpiracion()));
            }

            resumenPlan.setFechaExpiracion(fechaExpiracion);
        }
        else {
            LOGGER.info("Service error code received: " + codigoRespuesta+ " - " + descripcionRespuesta);
            LOGGER.error( new ServiceException(codigoRespuesta, descripcionRespuesta));
        }
        return resumenPlan;

    }

    /**
     * Obtiene Resumen del Plan de un Uusario PP
     * 
     * @param cta
     * @return ResumenPlan
     * @throws DAOException
     * @throws ServiceException
     */
    public ResumenPlan getPlanResumenPP(String cta) throws DAOException,
            ServiceException {

        ClientePerfilServicePortType port = null;
        ResumenPlan resumenPlan = null;

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
        ConsultarPlanResumenPPType planResumenPPRequest = new ConsultarPlanResumenPPType();
        planResumenPPRequest.setMsisdn(cta);

        ResultadoConsultarPlanResumenPPType planResumenPPResponse = null;
        LOGGER.info("Invocando servicio");
        
        try{
	        planResumenPPResponse = port.consultarPlanResumenPP(planResumenPPRequest);
        
        }catch (Exception e) {
            LOGGER.error("Exception caught on Service invocation: "
           		 + "consultarPlanResumenPP.", e);
            LOGGER.error( new DAOException(e));
        }
                
        ResPlanResumenPPType planResumen = planResumenPPResponse.getResPlanResumenPP();

        String codigoRespuesta = planResumenPPResponse.getRespuesta().getCodigo();
        String descripcionRespuesta = planResumenPPResponse.getRespuesta().getDescripcion();

        LOGGER.info("codigoRespuesta " + codigoRespuesta);
        LOGGER.info("descripcionRespuesta " + descripcionRespuesta);

        if (CODIGO_RESPUESTA_OK.equals(codigoRespuesta)) {
            resumenPlan = new ResumenPlan();

            resumenPlan.setNombrePlan(planResumen.getNombrePlan());
            String saldo 	= planResumen.getSaldo();
            String fechaExp = planResumen.getFechaExpiracion();                        
            resumenPlan.setSaldo(new Double(saldo));                           
            
            LOGGER.info("Fecha Exp:"+fechaExp);
            Date fechaExpiracion = DateHelper.parseDate(fechaExp, DateHelper.FORMAT_yyyyMMddhhmmss);

            if (fechaExpiracion == null) {
                LOGGER.error( new DAOException(
                        "fechaEmision no contiene formato esperado: '"
                                + DateHelper.FORMAT_yyyyMMddhhmmss + "' - "
                                + planResumen.getFechaExpiracion()));
            }
            resumenPlan.setFechaExpiracion(fechaExpiracion);
                        
            String saldoReservado = planResumen.getSaldoReservado().isEmpty() ? "0"  : planResumen.getSaldoReservado();
            String fechaExpReservado = planResumen.getFechaExpiracionSaldoReservado();
            resumenPlan.setSaldoReservado(new Double(saldoReservado));
            LOGGER.info("Fecha Expiracion Saldo Reservado:"+fechaExpReservado);
            Date fechaExpiracionReservado = DateHelper.parseDate(fechaExpReservado, DateHelper.FORMAT_yyyyMMdd_HHmmss_HYPHEN);

            if (fechaExpiracionReservado == null && !fechaExpReservado.isEmpty()) {
                LOGGER.error( new DAOException(
                        "fechaExpiracionReservado no contiene formato esperado: '"
                                + DateHelper.FORMAT_yyyyMMddhhmmss + "' - "
                                + planResumen.getFechaExpiracionSaldoReservado()));
            }
            resumenPlan.setFechaExpiracionSaldoReservado(fechaExpiracionReservado);
            
        }
        else {
            LOGGER.info("Service error code received: " + codigoRespuesta+ " - " + descripcionRespuesta);
            LOGGER.error( new ServiceException(codigoRespuesta, descripcionRespuesta));
        }
        return resumenPlan;
    }
    
    
    /**
     * Obtiene el Plan Actual de un usuario PP
     * 
     * @param msisdn
     * @return
     * @throws DAOException
     * @throws ServiceException
     */
    public PlanPPBean obtenerPlanActualPP(String msisdn) throws DAOException,
    ServiceException {
    	
    	PlanPPBean planActualPPBean = null;
		ClientePerfilServicePortType port = null;
		NumeroFrecuenteBean numerosFrecuentesBean;
		
		ArrayList<NumeroFrecuenteBean> listNumerosFrecuentes= new ArrayList<NumeroFrecuenteBean>();
	    
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
		ConsultarPlanActualPPType planActualPPRequest = new ConsultarPlanActualPPType();
		planActualPPRequest.setMsisdn(msisdn);
		
		LOGGER.info("Invocando servicio");
		ResultadoConsultarPlanActualPPType planActualPPResponse = null;
		
		try{
			planActualPPResponse = port.consultarPlanActualPP(planActualPPRequest);
        } catch (Exception e) {
            LOGGER.error("Exception caught on Service invocation: "
            		+ "consultarPlanActualPP", e);
            LOGGER.error( new DAOException(e));
        }
		
		String codigoRespuesta = planActualPPResponse.getRespuesta().getCodigo();
		String descripcionRespuesta = planActualPPResponse.getRespuesta().getDescripcion();
		
	    LOGGER.info("codigoRespuesta " + codigoRespuesta);
	    LOGGER.info("descripcionRespuesta " + descripcionRespuesta);
	        
		PlanActualPPType planActual = planActualPPResponse.getPlanActualPP();

		
		if (CODIGO_RESPUESTA_OK.equals(codigoRespuesta)) {
			planActualPPBean = new PlanPPBean();
			
			try{
				planActualPPBean.setIdTarifa(Integer.parseInt(planActual.getIdTarifa()));
				planActualPPBean.setTipoPlan(planActual.getTipoPlan());
				planActualPPBean.setFlagVisible(Boolean.parseBoolean(planActual.getFlagVisible()));
				planActualPPBean.setNombrePlan(planActual.getNombrePlan());
				planActualPPBean.setDescripcionPlan(planActual.getDescripcionPlan());
				planActualPPBean.setBreveDescripcion( (PlanHelper.construirGlosaPlan(planActual.getDescripcionPlan() )).get(0)  );
				planActualPPBean.setGlosaFormated( PlanHelper.construirGlosaPlan(planActual.getDescripcionPlan()) );
				
				List<NumerosFrecuentesPPType> numerosFrecuentes = planActual.getNumerosFrecuentes();
			
				for(NumerosFrecuentesPPType numerosFrecuentesPPType : numerosFrecuentes){
					
					numerosFrecuentesBean = new NumeroFrecuenteBean();
					numerosFrecuentesBean.setIdSlot(Integer.parseInt(numerosFrecuentesPPType.getIdSlot()));
					numerosFrecuentesBean.setNombreSlot(numerosFrecuentesPPType.getNombreSlot());
					numerosFrecuentesBean.setUsoSlot(numerosFrecuentesPPType.getUsoSlot());
					numerosFrecuentesBean.setTipo(numerosFrecuentesPPType.getTipo());
					numerosFrecuentesBean.setCostoAgregar(Double.parseDouble(numerosFrecuentesPPType.getCostoAgregar()));
					numerosFrecuentesBean.setCostoEditar(Double.parseDouble(numerosFrecuentesPPType.getCostoEditar()));
					numerosFrecuentesBean.setCostoBorrar(Double.parseDouble(numerosFrecuentesPPType.getCostoBorrar()));
					numerosFrecuentesBean.setUrl(numerosFrecuentesPPType.getUrl());
					numerosFrecuentesBean.setNumeroFrecuente(numerosFrecuentesPPType.getNumeroFrecuente());
					
					listNumerosFrecuentes.add(numerosFrecuentesBean);
				}

			} catch (Exception e) {
                LOGGER.error("Exception caught on Service response: "
            		+ "consultarPlanActualPP", e);
                LOGGER.error( new DAOException(e));
            }
			
			planActualPPBean.setNumerosFrecuentes(listNumerosFrecuentes);
		
		}else {
		    LOGGER.info("Respuesta del Servicio : " + codigoRespuesta+ " - " + descripcionRespuesta);
		    LOGGER.error( new ServiceException(codigoRespuesta, descripcionRespuesta));
		}
		
		return planActualPPBean;
    }
    
    /**
     * Obtiene los planes disponibles para usuarios PP
     * 
     * @param msisdn
     * @return
     * @throws DAOException
     * @throws ServiceException
     */
    public PlanDisponibleBean obtenerPlanesDisponiblesPP(String numeroPcsSeleccionado) throws DAOException,
    ServiceException {

    	PlanDisponibleBean planDisponibleBean = null;
		ClientePerfilServicePortType port = null;
		
		ArrayList<PlanPPBean> listPlanes= new ArrayList<PlanPPBean>();
		ArrayList<SlotBean> listSlots= new ArrayList<SlotBean>();
	    
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
		ConsultarPlanesDisponiblesPPType planesDisponiblesPPRequest = new ConsultarPlanesDisponiblesPPType();
		planesDisponiblesPPRequest.setMsisdn(numeroPcsSeleccionado);
		
		LOGGER.info("Invocando servicio");
		ResultadoConsultarPlanesDisponiblesPPType planesDisponiblesPPResponse = null;
		
		try{
			
			planesDisponiblesPPResponse = port.consultarPlanesDisponiblesPP(planesDisponiblesPPRequest);
		
        } catch (Exception e) {
            LOGGER.error("Exception caught on Service invocation: "
            		+ "consultarPlanesDisponiblesPP.", e);
            LOGGER.error( new DAOException(e));
        }
    
		String codigoRespuesta = planesDisponiblesPPResponse.getRespuesta().getCodigo();
		String descripcionRespuesta = planesDisponiblesPPResponse.getRespuesta().getDescripcion();
		
	    LOGGER.info("codigoRespuesta " + codigoRespuesta);
	    LOGGER.info("descripcionRespuesta " + descripcionRespuesta);
	        
		PlanesDisponiblesPPType planesDisponibles = planesDisponiblesPPResponse.getPlanesDisponiblesPP();

		if (CODIGO_RESPUESTA_OK.equals(codigoRespuesta)) {
			planDisponibleBean = new PlanDisponibleBean();
			List<PlanesPPType> planesList = planesDisponibles.getPlan();
			
			try{
				for(PlanesPPType planesPPType : planesList){
					
					PlanPPBean planPPBean = new PlanPPBean();

					planPPBean.setIdTarifa(Integer.parseInt(planesPPType.getIdTarifa()));
					planPPBean.setEstado(planesPPType.getEstado());
					planPPBean.setCostoCambioPlan(Double.parseDouble(planesPPType.getCostoCambioPlan()));
					planPPBean.setFlagVisible(Boolean.parseBoolean(planesPPType.getFlagVisible()));
					planPPBean.setFlagFrecuente(Boolean.parseBoolean(planesPPType.getFlagFrecuente()));
					planPPBean.setNombrePlan(planesPPType.getNombrePlan());
					planPPBean.setDescripcionPlan(planesPPType.getDescripcionPlan());
					planPPBean.setBreveDescripcion( (PlanHelper.construirGlosaPlan(planesPPType.getDescripcionPlan() )).get(0)  );
					planPPBean.setGlosaFormated( PlanHelper.construirGlosaPlan(planesPPType.getDescripcionPlan()) );
					
					
					List<SlotsType> slotsList = planesPPType.getSlots();
					 for(SlotsType slotsType : slotsList){
						  
						  SlotBean slotBean = new SlotBean();
						  
						  slotBean.setIdSlot(Integer.parseInt(slotsType.getIdSlot()));
						  slotBean.setUsoSlot(slotsType.getUsoSlot());
						  slotBean.setUrl(slotsType.getUrl());
						  slotBean.setCostoAgregar(Double.parseDouble(slotsType.getCostoAgregar()));
						  slotBean.setCostoEditar(Double.parseDouble(slotsType.getCostoEditar()));
						  slotBean.setCostoBorrar(Double.parseDouble(slotsType.getCostoBorrar()));
						  
						  listSlots.add(slotBean);
					  }
					 
					  planPPBean.setSlotBean(listSlots);
					
					  listPlanes.add(planPPBean);
				}
			} catch (Exception e) {
                LOGGER.error("Exception caught on Service response: "
                		+ "consultarPlanesDisponiblesPP.", e);
                LOGGER.error( new DAOException(e));
            }
			
			planDisponibleBean.setPlanesDisponiblesPP(listPlanes);
		
		}else {
		    LOGGER.info("Respuesta del Servicio : " + codigoRespuesta+ " - " + descripcionRespuesta);
		    LOGGER.error( new ServiceException(codigoRespuesta, descripcionRespuesta));
		}
		return planDisponibleBean;
    }
    
    /**
     * Realiza el cambio de plan SSCC
     * 
     * @param numeroPcsSeleccionado
     * @param codigoNuevoPlan
     * @throws DAOException
     * @throws ServiceException
     */
    public void cambiarPlanSSCC(String numeroPcsSeleccionado, String codigoNuevoPlan) throws DAOException,
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
		
        LOGGER.info("Respuesta del Servicio : " + codigoRespuesta + " - "
                + descripcionRespuesta);
	        
	    if (CODIGO_RESPUESTA_OK.equals(codigoRespuesta)) {
            LOGGER.info("Cambio de Plan Correcto para :"
                    + numeroPcsSeleccionado + " -  Codigo del nuevo plan:"
                    + codigoNuevoPlan);
		}else{
			LOGGER.info("Respuesta del Servicio : " + codigoRespuesta+ " - " + descripcionRespuesta);
			LOGGER.error( new ServiceException(codigoRespuesta, descripcionRespuesta));
		}
    }
    

   /**
    * Realiza el cambio de plan PP
    * 
    * @param numeroPcsSeleccionado
    * @param codigoNuevoPlan
    * @throws DAOException
    * @throws ServiceException
    */
    public void cambiarPlanPP(String numeroPcsSeleccionado, String codigoNuevoPlan) throws DAOException,
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
		CambioPlanPPType cambioPlanPPRequest = new CambioPlanPPType();
		cambioPlanPPRequest.setMsisdn(numeroPcsSeleccionado);
		cambioPlanPPRequest.setCodNuevoPlan(codigoNuevoPlan);
		
		LOGGER.info("Invocando servicio");
		ResultadoCambioPlanPPType cambioPlanPPResponse = null;
		
		try{
			cambioPlanPPResponse = port.cambioPlanPP(cambioPlanPPRequest);
            
        } catch (Exception e) {
            LOGGER.error("Exception caught on Service invocation: "
            		+ "cambioPlanPP.", e);
            LOGGER.error( new DAOException(e));
        }
		
		String codigoRespuesta = cambioPlanPPResponse.getRespuesta().getCodigo();
		String descripcionRespuesta = cambioPlanPPResponse.getRespuesta().getDescripcion();
		
	    LOGGER.info("codigoRespuesta " + codigoRespuesta);
	    LOGGER.info("descripcionRespuesta " + descripcionRespuesta);
	        
		if (CODIGO_RESPUESTA_OK.equals(codigoRespuesta)) {
			LOGGER.info("Cambio de Plan Correcto para :"+numeroPcsSeleccionado+" -  Codigo del nuevo plan:"+codigoNuevoPlan);
		}else{
			LOGGER.info("Respuesta del Servicio : " + codigoRespuesta+ " - " + descripcionRespuesta);
			LOGGER.error( new ServiceException(codigoRespuesta, descripcionRespuesta));
		}
    }
    
    

    /**
     * Agrega o modifica un numero frecuente
     * 
     * @param numeroPcsSeleccionado
     * @param idSlot
     * @param numeroRecibeSolicitud
     * @throws DAOException
     * @throws ServiceException
     */
    public void administrarNumeroFrecuentePP(String numeroPcsSeleccionado, String idSlot, String numeroRecibeSolicitud, int codAccion) throws DAOException,
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
		AgregarModificarNumeroFrecuentePPType agregarmodificarNumeroFrecuentePPRequest = new AgregarModificarNumeroFrecuentePPType();
		agregarmodificarNumeroFrecuentePPRequest.setMsisdn(numeroPcsSeleccionado);
		agregarmodificarNumeroFrecuentePPRequest.setIdSlot(idSlot);
		agregarmodificarNumeroFrecuentePPRequest.setMsisdnRecibeSolicitud(numeroRecibeSolicitud);
		agregarmodificarNumeroFrecuentePPRequest.setAccion(Integer.toString(codAccion));
		
		LOGGER.info("Invocando servicio");
		ResultadoAgregarModificarNumeroFrecuentePPType agregarmodificarNumeroFrecuentePPResponse = null;
		try{
			agregarmodificarNumeroFrecuentePPResponse = port.agregarModificarNumeroFrecuentePP(agregarmodificarNumeroFrecuentePPRequest);
        } catch (Exception e) {
            LOGGER.error("Exception caught on Service invocation: "
            		+ "agregarModificarNumeroFrecuentePP.", e);
            LOGGER.error( new DAOException(e));
        }
		
		String codigoRespuesta = agregarmodificarNumeroFrecuentePPResponse.getRespuesta().getCodigo();
		String descripcionRespuesta = agregarmodificarNumeroFrecuentePPResponse.getRespuesta().getDescripcion();
		
	    LOGGER.info("codigoRespuesta " + codigoRespuesta);
	    LOGGER.info("descripcionRespuesta " + descripcionRespuesta);
	        
	    if (CODIGO_RESPUESTA_OK.equals(codigoRespuesta)) {
	    	LOGGER.info("Administracion Numero Frecuente Correcto para :"+numeroPcsSeleccionado+" -  idSlot:"+idSlot+" - Numero que recibe solicitud:"+numeroRecibeSolicitud);
		}else{
			LOGGER.info("Respuesta del Servicio : " + codigoRespuesta+ " - " + descripcionRespuesta);
			LOGGER.error( new ServiceException(codigoRespuesta, descripcionRespuesta));
		}
	
    }
    


    /**
     * Elimina un numero frecuente
     * 
     * @param numeroPcsSeleccionado
     * @param idSlot
     * @throws DAOException
     * @throws ServiceException
     */
    public void eliminarNumeroFrecuentePP(String numeroPcsSeleccionado, String idSlot) throws DAOException,
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
		EliminarNumeroFrecuentePPType eliminarNumeroFrecuentePPRequest = new EliminarNumeroFrecuentePPType();
		eliminarNumeroFrecuentePPRequest.setMsisdn(numeroPcsSeleccionado);
		eliminarNumeroFrecuentePPRequest.setIdSlot(idSlot);

		LOGGER.info("Invocando servicio");
		ResultadoEliminarNumeroFrecuentePPType eliminarNumeroFrecuentePPResponse = null;
		
		try{
			eliminarNumeroFrecuentePPResponse = port.eliminarNumeroFrecuentePP(eliminarNumeroFrecuentePPRequest);
        } catch (Exception e) {
            LOGGER.error("Exception caught on Service invocation: "
            		+ "eliminarNumeroFrecuentePP.", e);
            LOGGER.error( new DAOException(e));
        }
		
		String codigoRespuesta = eliminarNumeroFrecuentePPResponse.getRespuesta().getCodigo();
		String descripcionRespuesta = eliminarNumeroFrecuentePPResponse.getRespuesta().getDescripcion();
		
	    LOGGER.info("codigoRespuesta " + codigoRespuesta);
	    LOGGER.info("descripcionRespuesta " + descripcionRespuesta);
	        
	    if (CODIGO_RESPUESTA_OK.equals(codigoRespuesta)) {
	    	LOGGER.info("Eliminacion Numero Frecuente Correcto para :"+numeroPcsSeleccionado+" -  idSlot:"+idSlot);
		}else{
			LOGGER.info("Respuesta del Servicio : " + codigoRespuesta+ " - " + descripcionRespuesta);
			LOGGER.error( new ServiceException(codigoRespuesta, descripcionRespuesta));
		}

    }
    

	/**
	 * Cambiar al Plan Comunik2
	 * 
	 * @param numeroPcsSeleccionado
	 * @param numeroRecibeSolicitud
	 * @throws DAOException
	 * @throws ServiceException
	 */
     public void cambiarPlanComunik2PP(String numeroPcsSeleccionado, String numeroRecibeSolicitud) throws DAOException,
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
 		CambioPlanComunik2PPType cambioPlanComunikPPRequest = new CambioPlanComunik2PPType();
 		cambioPlanComunikPPRequest.setMsisdn(numeroPcsSeleccionado);
 		cambioPlanComunikPPRequest.setMsisdnRecibeSolicitud(numeroRecibeSolicitud);

 		LOGGER.info("Invocando servicio");
 		ResultadoCambioPlanComunik2PPType cambioPlanComunikPPResponse = null;
 		
 		try{
 			cambioPlanComunikPPResponse = port.cambioPlanComunik2PP(cambioPlanComunikPPRequest);
         } catch (Exception e) {
             LOGGER.error("Exception caught on Service invocation: "
            		 + "cambioPlanComunik2PP.", e);
             LOGGER.error( new DAOException(e));
         }
 		
 		String codigoRespuesta = cambioPlanComunikPPResponse.getRespuesta().getCodigo();
 		String descripcionRespuesta = cambioPlanComunikPPResponse.getRespuesta().getDescripcion();
 		
 	    LOGGER.info("codigoRespuesta " + codigoRespuesta);
 	    LOGGER.info("descripcionRespuesta " + descripcionRespuesta);
 	        
 	    if (CODIGO_RESPUESTA_OK.equals(codigoRespuesta)) {
 	    	LOGGER.info("Cambio Plan Comunik2 Correcto para :"+numeroPcsSeleccionado+" -  Numero que recibe solicitud:"+numeroRecibeSolicitud);
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
     public PlanBean obtenerPlanActualSSCC(String numeroPcs, String mercado, String atributoAAA) throws DAOException,
     ServiceException {
     	
    	PlanBean planActualSSCCBean = null;
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
 		ConsultarPlanFullActualSSCCType planActualSSCCRequest = new ConsultarPlanFullActualSSCCType();
 		planActualSSCCRequest.setMsisdn(numeroPcs);
 		planActualSSCCRequest.setMercado(mercado);
 		planActualSSCCRequest.setAaa(atributoAAA);
 		
 		LOGGER.info("Invocando servicio");
 		ResultadoConsultarPlanFullActualSSCCType planActualSSCCResponse = null;
 
 		try{
 			planActualSSCCResponse = port.consultarPlanFullActualSSCC(planActualSSCCRequest);
 			
         } catch (Exception e) {
             LOGGER.error("Exception caught on Service invocation: "
            		 + "consultarPlanFullActualSSCC", e);
             LOGGER.error( new DAOException(e));
         }
 		
 		String codigoRespuesta = planActualSSCCResponse.getRespuesta().getCodigo();
 		String descripcionRespuesta = planActualSSCCResponse.getRespuesta().getDescripcion();
 		
 	    LOGGER.info("codigoRespuesta " + codigoRespuesta);
 	    LOGGER.info("descripcionRespuesta " + descripcionRespuesta);
 	       
 	    ConsultarPlanActualType planActual =  planActualSSCCResponse.getConsultarPlanActual();
 	    

 		if (CODIGO_RESPUESTA_OK.equals(codigoRespuesta)) {
 			try{
 				planActualSSCCBean = new PlanBean();
 				planActualSSCCBean.setTipoPlan(planActual.getTipoPlan());
 				planActualSSCCBean.setFlagTipoDetalle(!Utils.isEmptyString(planActual.getFlagTipoDetalle()) ? planActual.getFlagTipoDetalle() : "");
 				planActualSSCCBean.setCodbscs1(planActual.getCodbscs1());
 				planActualSSCCBean.setCodbscs2(planActual.getCodbscs2());
 				planActualSSCCBean.setNombrePlan(PlanHelper.extraerNombrePlan(planActual.getNombrePlan()));
 				planActualSSCCBean.setTipoTasacion(planActual.getTipoTasacion());
 				planActualSSCCBean.setTipoMercado(mercado);
 				planActualSSCCBean.setTotalMinutos(PlanHelper.convertirSegundosaMinutos(planActual.getTotalMinutos()));
 				planActualSSCCBean.setTotalMinutosNumerico(Long.parseLong(PlanHelper.convertirSegundosaMinutos(planActual.getTotalMinutos())));
 				planActualSSCCBean.setTotalMinutosAdicional(PlanHelper.convertirSegundosaMinutos(planActual.getTotalMinutosAdicional()));
 				planActualSSCCBean.setCargoFijoPlan(Double.parseDouble(planActual.getCargoFijoPlan()));
 				planActualSSCCBean.setDescIMovil(!Utils.isEmptyString(planActual.getDescIMovil()) ? planActual.getDescIMovil() : "");
				planActualSSCCBean.setLimiteIMovil(!Utils.isEmptyString(planActual.getLimiteIMovil()) ? planActual.getLimiteIMovil() : "");
 				planActualSSCCBean.setListaTasaciones(PlanHelper.construirDetalleTasacionesPlanSSCC(planActual.getDetallePlanActual(),planActual.getTipoPlan(),planActual.getTipoTasacion()));

			} catch (Exception e) {
				LOGGER.error("Exception caught on Service response: "
			            		 + "consultarPlanFullActualSSCC", e);
				LOGGER.error( new DAOException(e));
			}
 
 		}else {
 		    LOGGER.info("Respuesta del Servicio : " +codigoRespuesta+ " - " + descripcionRespuesta);
 		    LOGGER.error( new ServiceException(codigoRespuesta, descripcionRespuesta));
 		}

 		return planActualSSCCBean;
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
     public PlanDisponibleBean obtenerPlanesDisponibles(String numeroPcs) throws DAOException,
     ServiceException {

     	PlanDisponibleBean planDisponibleBean = null;
 		ClientePerfilServicePortType port = null; 		
 		List<PlanesDisponiblesSSCCType> planesListCC = null;
 		List<PlanesDisponiblesSSCCType> planesListSS = null;
 	    this.planesList = new ArrayList<PlanBean>();
 	    
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
 		ConsultarPlanesDisponiblesSSCCType planesDisponiblesSSCCRequest = new ConsultarPlanesDisponiblesSSCCType();
 		planesDisponiblesSSCCRequest.setMsisdn(numeroPcs);
 	
 		
 		LOGGER.info("Invocando servicio");
 		ResultadoConsultarPlanesDisponiblesSSCCType planesDisponiblesSSCCResponse = null;
 		
 		try{
 			
 			planesDisponiblesSSCCResponse = port.consultarPlanesDisponiblesSSCC(planesDisponiblesSSCCRequest);
         
 		} catch (Exception e) {
             LOGGER.error("Exception caught on Service invocation: "
            		 + "consultarPlanesDisponiblesSSCC", e);
             LOGGER.error( new DAOException(e));
        }
     
 		String codigoRespuesta = planesDisponiblesSSCCResponse.getRespuesta().getCodigo();
 		String descripcionRespuesta = planesDisponiblesSSCCResponse.getRespuesta().getDescripcion();
 		
 	    LOGGER.info("codigoRespuesta " + codigoRespuesta);
 	    LOGGER.info("descripcionRespuesta " + descripcionRespuesta);
 	  
 		if (CODIGO_RESPUESTA_OK.equals(codigoRespuesta)) {
 			try{

 	 			planDisponibleBean = new PlanDisponibleBean();
 	 			
 	 			planesListSS = planesDisponiblesSSCCResponse.getPlanesDisponiblesSS();
 	 			planesListCC = planesDisponiblesSSCCResponse.getPlanesDisponiblesCC();

 	 			addPlanes(planesListSS, "SS");
 	 			addPlanes(planesListCC, "CC"); 			 			
 				
 				planDisponibleBean.setPlanesDisponibles(planesList);
 				
 			} catch (Exception e) {
                 LOGGER.error("Exception caught on Service response: "
                		 + "consultarPlanesDisponiblesSSCC", e);
                 LOGGER.error( new DAOException(e));
            }

 		}else {
 		    LOGGER.info("Respuesta del Servicio : " + codigoRespuesta+ " - " + descripcionRespuesta);
 		    LOGGER.error( new ServiceException(codigoRespuesta, descripcionRespuesta));
 		}
 		return planDisponibleBean;
     }

     
     /**
      * Agrega a una lista los planes disponibles para SS y CC.
      * 
      * @param planesListSSCC
      * @param tipoMercado
      */
     private void addPlanes(List<PlanesDisponiblesSSCCType> planesListSSCC, String tipoMercado){
    	 
    	 for( PlanesDisponiblesSSCCType types : planesListSSCC ){
    		 
    		 try{
				PlanBean planBean = new PlanBean();
				planBean.setTipoPlan(types.getTipoPlan());	
				planBean.setFlagTipoDetalle(!Utils.isEmptyString(types.getFlagTipoDetalle()) ? types.getFlagTipoDetalle() : "");			
				planBean.setCodbscs1(types.getCodbscs1());
				planBean.setCodbscs2(types.getCodbscs2());
				planBean.setNombrePlan(PlanHelper.extraerNombrePlan(types.getNombrePlan()));
				planBean.setCodigoNombrePlan(PlanHelper.extraerCodigoNombrePlan(types.getNombrePlan()));
				planBean.setTipoMercado(tipoMercado);
				planBean.setTipoTasacion(types.getTipoTasacion());
				planBean.setTotalMinutos(PlanHelper.convertirSegundosaMinutos(types.getTotalMinutos()));
				planBean.setTotalMinutosAdicional(PlanHelper.convertirSegundosaMinutos(types.getTotalMinutosAdicional()));
				planBean.setCargoFijoPlan(Double.parseDouble(types.getCargoFijoPlan()));
				planBean.setDescIMovil(!Utils.isEmptyString(types.getDescIMovil()) ? types.getDescIMovil() : "");
				planBean.setLimiteIMovil(!Utils.isEmptyString(types.getLimiteIMovil()) ? types.getLimiteIMovil() : "");
				planBean.setListaTasaciones(PlanHelper.construirDetalleTasacionesPlanSSCC(types.getDetallePlanActual(), types.getTipoPlan(), types.getTipoTasacion()));
				
				planesList.add(planBean);

    		 } catch (Exception e) {
                 LOGGER.warn("Exception al agregar planes disponibles a la lista.", e);
             }
		}
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
    
        String codigoRespuesta = response.getRespuesta()
         .getCodigo();
   
        String descripcionRespuesta = response.getRespuesta()
         .getDescripcion();
       
        LOGGER.info("codigoRespuesta " + codigoRespuesta);
        LOGGER.info("descripcionRespuesta " + descripcionRespuesta);
     
        if (CODIGO_RESPUESTA_OK.equals(codigoRespuesta)) {
            try{
               
                LOGGER.info("Validacion Bloqueo Temporal Correcta para :"+msisdn);
               
            } catch (Exception e) {
                 LOGGER.error("Exception inesperada al realizar "
                		 + "la validacion de Bloqueo Temporal", e);
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
               
                LOGGER.info("Validacion Comercial Correcta para :"+msisdn+ "Bolsa :"+codbscs2);
               
            } catch (Exception e) {
                 LOGGER.error("Exception inesperada al realizar la validacion Comercial.", e);
                 LOGGER.error( new DAOException(e));
            }

        }else {
            LOGGER.info("Respuesta del Servicio : " + codigoRespuesta+ " - " + descripcionRespuesta);
            LOGGER.error( new ServiceException(codigoRespuesta, descripcionRespuesta));
        }
       
     }
     
   
     /**
      * Obtiene el historial del plan comunik2 
      * 
      * @param msisdn
      * @return
      * @throws DAOException
      * @throws ServiceException
      */
     public List<HistorialComunik2Bean> obtenerHistorialComunik2(String msisdn) throws DAOException,
     ServiceException {
     	
    	HistorialComunik2Bean comunik2Bean;
 		ClientePerfilServicePortType port = null; 		
 		
 	   List <HistorialComunik2Bean> listcomunik2Bean= new LinkedList<HistorialComunik2Bean>();
 	    
 		LOGGER.info("Instanciando el Port.");
 		try 
 		{
 		   port = (ClientePerfilServicePortType) WebServiceLocator
 		            .getInstance().getPort(ClientePerfilService.class,
 		                    ClientePerfilServicePortType.class);
 		} 
 		catch (WebServiceLocatorException e) 
 		{
 		    LOGGER.error("Error al inicializar el Port "+ ClientePerfilService.class);
 		    LOGGER.error( new DAOException(e));
 		}
 		
 		LOGGER.info("Configurando parametros de la peticion");
 		ConsultarHistorialPlanComunik2PPType request = new ConsultarHistorialPlanComunik2PPType();
 		
 		request.setMsisdn(msisdn);
 		
 		LOGGER.info("Invocando servicio");
 		ResultadoConsultarHistorialPlanComunik2PPType response = null;
 		
 		try
 		{
 			response = port.consultarHistorialPlanComunik2PP(request);
         } 
 		    catch (Exception e) {
              LOGGER.error("Exception caught on Service invocation: "
             		 + "consultarHistorialPlanComunik2PP.", e);
              LOGGER.error( new DAOException(e));
         }
 		
 		String codigoRespuesta = response.getRespuesta().getCodigo();
 		String descripcionRespuesta = response.getRespuesta().getDescripcion();
 		
 	    LOGGER.info("codigoRespuesta " + codigoRespuesta);
 	    LOGGER.info("descripcionRespuesta " + descripcionRespuesta);
 		
 		if (CODIGO_RESPUESTA_OK.equals(codigoRespuesta)) 
 		{ 		
 			try
 			{
 			  	List<HistorialPlanComunik2ItemType> historialType =  response.getHistorialPlanComunik2Item();
 				
 				for(HistorialPlanComunik2ItemType itemComunik2Type : historialType)
 				{
 					comunik2Bean = new HistorialComunik2Bean();
 					comunik2Bean.setNumeroPcs(itemComunik2Type.getNumero().substring(3, itemComunik2Type.getNumero().length()));
 					comunik2Bean.setTipoInvitacion(ParametrosHelper.getRespuestaServicio("comunik2.respuesta",itemComunik2Type.getAccion()));
 					comunik2Bean.setFecha(DateHelper.parseDate(itemComunik2Type.getFecha(), DateHelper.FORMAT_yyyyMMddhhmmss));
 					comunik2Bean.setRespuesta(itemComunik2Type.getResultado()); 					
 				    listcomunik2Bean.add(comunik2Bean); 		           
 				}

 			} 
 			catch (Exception e) 
 			{
                 LOGGER.error("Exception caught on Service response: "
                 		 + "consultarHistorialPlanComunik2PP.", e);
                 LOGGER.error( new DAOException(e));
             } 			 			
 		}
 		else 
 		{
 		    LOGGER.info("Respuesta del Servicio : " + codigoRespuesta+ " - " + descripcionRespuesta);
 		    LOGGER.error( new ServiceException(codigoRespuesta, descripcionRespuesta));
 		} 		
 		return listcomunik2Bean;
     }   
     
     
     /**
      * Obtiene la solicitud activa del cliente para el plan comunik2
      * 
      * @param msisdn
      * @return
      * @throws DAOException
      * @throws ServiceException
      */
     public SolicitudComunik2Bean obtenerSolicitudComunik2(String msisdn) throws DAOException,
     ServiceException {
     	
    	 SolicitudComunik2Bean solicitudComunik2Bean = null;
 		 ClientePerfilServicePortType port = null; 		
 		
 	  
 	    
 		LOGGER.info("Instanciando el Port.");
 		try 
 		{
 		   port = (ClientePerfilServicePortType) WebServiceLocator
 		            .getInstance().getPort(ClientePerfilService.class,
 		                    ClientePerfilServicePortType.class);
 		} 
 		  catch (WebServiceLocatorException e) 
 		{
 		    LOGGER.error("Error al inicializar el Port "+ ClientePerfilService.class);
 		    LOGGER.error( new DAOException(e));
 		}
 		
 		LOGGER.info("Configurando parametros de la peticion"); 			   
 		ConsultarSolicitudesActivasPPType request = new ConsultarSolicitudesActivasPPType();
 		 		
 		request.setMsisdn(msisdn);
 		
 		LOGGER.info("Invocando servicio");
 		ResultadoConsultarSolicitudesActivasPPType response = null;
 		
 		try
 		{
 			response = port.consultarSolicitudesActivasPP(request);
 			
         } 
 		    catch (Exception e) {
              LOGGER.error("Exception caught on Service invocation: "
              		 + "consultarSolicitudesActivasPP.", e);
              LOGGER.error( new DAOException(e));
         }
 		
 		String codigoRespuesta = response.getRespuesta().getCodigo();

 		String descripcionRespuesta = response.getRespuesta()
         .getDescripcion();
 		
 	    LOGGER.info("codigoRespuesta " + codigoRespuesta);
 	    LOGGER.info("descripcionRespuesta " + descripcionRespuesta);
 		
 		if (CODIGO_RESPUESTA_OK.equals(codigoRespuesta)) 
 		{ 		
 			try
 			{
 				 
 			   solicitudComunik2Bean = new SolicitudComunik2Bean();
 			 
 			   solicitudComunik2Bean.setMsisdn(response.getSolicitudesAbiertasPP().getMsisdn());
 			   solicitudComunik2Bean.setDescripcion(response.getSolicitudesAbiertasPP().getDescripcionPlan());
 			   solicitudComunik2Bean.setFechaSolicitud(DateHelper.parseDate(response.getSolicitudesAbiertasPP().getFechaSolicitud(), DateHelper.FORMAT_yyyyMMddhhmmss));
 			   solicitudComunik2Bean.setMsisdnEnvia(response.getSolicitudesAbiertasPP().getMsisdnEnviaSolicitud());
 			   solicitudComunik2Bean.setMsisdnRecibe(response.getSolicitudesAbiertasPP().getMsisdnRecibeSolicitud());
 			   
 			   if( solicitudComunik2Bean.getMsisdn().equals( solicitudComunik2Bean.getMsisdnRecibe() ) ){
 				   solicitudComunik2Bean.setSolicitudRecibida(true); 			    
 			   }
 			   
 			   solicitudComunik2Bean.setMsisdn(solicitudComunik2Bean.getMsisdn().substring(3, solicitudComunik2Bean.getMsisdn().length()));
 			   solicitudComunik2Bean.setMsisdnEnvia(solicitudComunik2Bean.getMsisdnEnvia().substring(3, solicitudComunik2Bean.getMsisdnEnvia().length()));
 			   solicitudComunik2Bean.setMsisdnRecibe(solicitudComunik2Bean.getMsisdnRecibe().substring(3, solicitudComunik2Bean.getMsisdnRecibe().length()));
 			   
 			   
 			 
 			}	
 			  catch (Exception e) 
 			{
                 LOGGER.error("Exception caught on Service response: "
                  		 + "consultarSolicitudesActivasPP.", e);
                 LOGGER.error( new DAOException(e));
             } 			 			
 		}
 		  else 
 		{
 		    LOGGER.info("Respuesta del Servicio : " + codigoRespuesta+ " - " + descripcionRespuesta);
 		    LOGGER.error( new ServiceException(codigoRespuesta, descripcionRespuesta));
 		} 		
 		return solicitudComunik2Bean ;
     }   
     
     
     /**
      * Metodo para administrar las solicitudes plan comunik2 , aceptar , rechazar
      * 
      * @param msisdn
      * @param accion
      * @return
      * @throws DAOException
      * @throws ServiceException
      */
	public void administrarSolicitudComunik2(String msisdn, String accion)
			throws DAOException, ServiceException {

		ClientePerfilServicePortType port = null;

		LOGGER.info("Instanciando el Port.");
		try {
			port = (ClientePerfilServicePortType) WebServiceLocator
					.getInstance().getPort(ClientePerfilService.class,
							ClientePerfilServicePortType.class);
		} catch (WebServiceLocatorException e) {
			LOGGER.error("Error al inicializar el Port "
					+ ClientePerfilService.class);
			LOGGER.error( new DAOException(e));
		}

		LOGGER.info("Configurando parametros de la peticion");

		AdministrarSolicitudPlanComunik2PPType request = new AdministrarSolicitudPlanComunik2PPType();

		request.setMsisdn(msisdn);
		request.setAceptar(accion);

		LOGGER.info("Invocando servicio");
		ResultadoAdministrarSolicitudPlanComunik2PPType response = null;

		try {
			response = port.administrarSolicitudPlanComunik2PP(request);

		} catch (Exception e) {
			LOGGER.error("Exception caught on Service invocation: "
             		 + "administrarSolicitudPlanComunik2PP.", e);
			LOGGER.error( new DAOException(e));
		}

		String codigoRespuesta = response.getRespuesta().getCodigo();
		String descripcionRespuesta = response.getRespuesta().getDescripcion();

		LOGGER.info("codigoRespuesta " + codigoRespuesta);
		LOGGER.info("descripcionRespuesta " + descripcionRespuesta);

		if (CODIGO_RESPUESTA_OK.equals(codigoRespuesta)) {
			try {

			} catch (Exception e) {
				LOGGER.error("Exception inesperada en el Consultar Solicitudes Activas", e);
				LOGGER.error( new DAOException(e));
			}
		} else {
			LOGGER.info("Respuesta del Servicio : " + codigoRespuesta + " - "
					+ descripcionRespuesta);
			LOGGER.error( new ServiceException(codigoRespuesta, descripcionRespuesta));
		}

	}
    
    /**
     * Metodo que retorna la informacion de un plan excedidos dado un numero movil
     * @author Wilson Brochero Munoz
     * @param  movil    
     * @return PlanesMultimediaBean
     * @throws DAOException
     * @throws ServiceException     
     * */   
	public PlanesMultimediaBean consultarPlanMultimediaExcedido(String movil)
            throws DAOException, ServiceException {

        LOGGER.info("Instanciando el Port.");        
        DatosExcedidosPortType port = null;
		try {
			LOGGER
					.info("DatosExcedidosService: "
							+ DatosExcedidosService.class);
			LOGGER.info("DatosExcedidosPortType: "
					+ DatosExcedidosPortType.class);
			port = (DatosExcedidosPortType) WebServiceLocator.getInstance()
					.getPort(DatosExcedidosService.class,
							DatosExcedidosPortType.class);

		} catch (WebServiceLocatorException e) {
			LOGGER.error("Error al inicializar el Port "
					+ DatosExcedidosService.class);
			LOGGER.error( new DAOException(e));
		}

        ConsultaPlanesMultimediaType requestType = new ConsultaPlanesMultimediaType();
        
        ConsultaPlanesMultimediaResponseType responseType = null;
        ResponseConsultaPlanType responseConsultaPlanType;	
        HeaderInType headerInType = new HeaderInType();
		try {
			   LOGGER.info("Configurando parametros de la peticion");			 
			    headerInType.setAppName(CODIGO_APPNAME_PLANES_MM_EXCEDIDOS);
			    headerInType.setUserName(CODIGO_USERNAME_PLANES_MM_EXCEDIDOS);
			    requestType.setHeaderIn(headerInType);
			    requestType.setMovil(CODIGO_PREFIJO_ENTEL+movil);
			LOGGER.info("Invocando servicio");
			responseType = port.consultaPlanesMultimedia(requestType);
		} catch (Exception e) {
			LOGGER
					.error(
							"Exception caught on Service invocation: consultaPlanesMultimedia",
							e);
			LOGGER.error( new DAOException(e));
		}
        responseConsultaPlanType = responseType.getReturn();
        
        String codigoRespuesta = responseConsultaPlanType.getCodigo();
        String descripcionRespuesta = responseConsultaPlanType.getDescCodigo();
        
        LOGGER.info("codigoRespuesta " + codigoRespuesta);
	    LOGGER.info("descripcionRespuesta " + descripcionRespuesta);        
        
        PlanesMultimediaBean planesMultimedia = new PlanesMultimediaBean();        
        
    	if(responseConsultaPlanType.getCodigo().equals(CODIGO_RESPUESTA_OK)){
			DatosExcedidosType datosExcedidosType = new DatosExcedidosType();
			datosExcedidosType = responseConsultaPlanType.getDatosExcedidos();
			
			if(datosExcedidosType.getClasificacionPlan().equals(CLASIFICACION_PLAN_EXCEDIDO)){
				
				    planesMultimedia.setFechaDiaMesFormat(datosExcedidosType.getFechaReferencia());				    
					planesMultimedia.setVelocidadPlan(datosExcedidosType.getVelocidadPlan());
					planesMultimedia.setCuotaTraficoUtil(datosExcedidosType.getCuotaTraficoUtil());			
					planesMultimedia.setValorTotalTrafico(datosExcedidosType.getValorTotalTrafico());			
					planesMultimedia.setTraficoExcedido(datosExcedidosType.getTraficoExcedido());
					planesMultimedia.setValorMBExcedido(datosExcedidosType.getValorMBExcedido());		
					planesMultimedia.setTotalTrafico(datosExcedidosType.getTotalTrafico());
					planesMultimedia.setClasificacionPlan(datosExcedidosType.getClasificacionPlan());
					planesMultimedia.setCodigoPlanBSCS(datosExcedidosType.getCodigoPlanBSCS());
					planesMultimedia.setNombrePlanBSCS(datosExcedidosType.getNombrePlanBSCS());
					planesMultimedia.setDescPlanBSCS(datosExcedidosType.getDescPlanBSCS());				
					planesMultimedia.setPromConsumo("");
					planesMultimedia.setPromConsumoSinDec("");
				
				return planesMultimedia;
			}else{
				return null;
			}			
		}else{
			LOGGER.info("Respuesta del Servicio : " + codigoRespuesta+ " - " + descripcionRespuesta);
	           LOGGER.error( new ServiceException(codigoRespuesta, descripcionRespuesta));					
		}
         return new PlanesMultimediaBean();
    } 
	
	
	 /**
     * Metodo que retorna la informacion full de un plan 
     * @author Wilson Brochero Munoz
     * @param  codbscs2
     * @param  tipoPlan
     * @return PlanesFullBean
     * @throws DAOException
     * @throws ServiceException     
     * */   
	 public PlanesFullBean consultaXmlPlanesFull(String codbscs2, String  tipoPlan) throws DAOException, ServiceException {
	    	
	    ClientePerfilServicePortType port = null;
	    PlanesFullBean planesFullBean = null;
	      try {
	            port = (ClientePerfilServicePortType) WebServiceLocator
	                    .getInstance().getPort(ClientePerfilService.class,
	                    		ClientePerfilServicePortType.class);
	        } catch (WebServiceLocatorException e) {
	            LOGGER.error("Error al inicializar el Port "
	                + DatosExcedidosService.class);
	        LOGGER.error( new DAOException(e));
	     }
	    
		    ResultadoConsultaXmlplanesfullType responseType = null;
		    ConsultarXmlPlanesFullType consultarXmlPlanesFullType =new ConsultarXmlPlanesFullType();
		    
	        LOGGER.info("Configurando Datos de la peticion");
	        consultarXmlPlanesFullType.setCodbscs2(codbscs2);
	        consultarXmlPlanesFullType.setTipoPlan(tipoPlan);
	        
	        LOGGER.info("codbscs2 :" + codbscs2);  
	        LOGGER.info("tipoPlan :" + tipoPlan); 
            LOGGER.info("Invocando servicio");
            
		try {
			responseType = port
					.consultaXmlPlanesFull(consultarXmlPlanesFullType);
		} catch (Exception e) {
			LOGGER
					.error(
							"Exception caught on Service invocation: consultaXmlPlanesFull",
							e);
			LOGGER.error( new DAOException(e));
		}
	 
	    
		String codigoRespuesta = responseType.getRespuesta().getCodigo();
		String descripcionRespuesta = responseType.getRespuesta()
				.getDescripcion();

		LOGGER.info("codigoRespuesta " + codigoRespuesta);
		LOGGER.info("descripcionRespuesta " + descripcionRespuesta);

		if (responseType.getRespuesta().getCodigo().equals(CODIGO_RESPUESTA_OK)) {

			planesFullBean = new PlanesFullBean();
			planesFullBean
					.setCodbscs2(responseType.getPlanFull().getCodbscs2());
			planesFullBean.setCondProrrateo(responseType.getPlanFull()
					.getCondProrrateo());
			planesFullBean.setDescIMovil(responseType.getPlanFull()
					.getDescIMovil());
			planesFullBean.setFlagTipoDetalle(responseType.getPlanFull()
					.getFlagTipoDetalle());
			planesFullBean.setPlanExcedido(responseType.getPlanFull()
					.getPlanExcedido());
			planesFullBean
					.setTipoPlan(responseType.getPlanFull().getTipoPlan());
			planesFullBean.setTotalMinutos(responseType.getPlanFull()
					.getTotalMinutos());
			planesFullBean.setTotalMinutosAdicional(responseType.getPlanFull()
					.getTotalMinutosAdicional());			
			try {	
				List<DetallePlanActualType> listDetalle;
				listDetalle = responseType.getPlanFull().getDetallePlaFull();
				
				for(DetallePlanActualType detalle : listDetalle){
					if(detalle!=null){
						if(detalle.getNombreTasacion()!=null){
							if(detalle.getNombreTasacion().equals(MBADI)){
								planesFullBean.setValorMBExcedido(PlanHelper.convertirValorTasacion(detalle.getValorTasacion()));
							}
						}											
					}		
					
				}				
				
			} catch (Exception e) {
				LOGGER
				.error("Error al contruir la Tasaciones en (consultaXmlPlanesFull)");				
			}


		} else {
			LOGGER.info("Respuesta del Servicio : " + codigoRespuesta + " - "
					+ descripcionRespuesta);
			LOGGER.error( new ServiceException(codigoRespuesta, descripcionRespuesta));
		}
	    
	    return planesFullBean;
	    
	}

    /**
	 * Metodo el cual retorna la informacion acerca de la disponibilidad de un
	 * Plan para acceder a 4G LTE.
	 * 
	 * @author Anthony David Cajamarca Acuna
	 * @param codigoPlan
	 * @return Plane4GLteBean
	 * @throws DAOException
	 * @throws ServiceException
	 * */
	public Plan4GLteBean consultarDisponibilidadPlan4GLte(String codigoPlan)
			throws DAOException, ServiceException {

		LOGGER.info("Instanciando el Port.");
		ConsultaPlanesLtePortType port = null;

		try {
			port = (ConsultaPlanesLtePortType) WebServiceLocator.getInstance()
					.getPort(WsConsultaPlanesLteService.class,
							ConsultaPlanesLtePortType.class);
		} catch (WebServiceLocatorException e) {
			LOGGER.error("Error al inicializar el Port "
					+ ClientePerfilService.class);
			LOGGER.error( new DAOException(e));
		}

		RequestType requestType = new RequestType();
		ConsultaPlanesLteRequestType consultaPlanesRequestType = new ConsultaPlanesLteRequestType();

		ResponseType responseType = new ResponseType();
		ConsultaPlanesLteResponseType consultaPlanesResponseType = new ConsultaPlanesLteResponseType();

		com.esa.clientes.perfilesclientes.serviciolte.types.HeaderInType headerIn = new com.esa.clientes.perfilesclientes.serviciolte.types.HeaderInType();

		try {
			LOGGER.info("Configurando los parametros de la peticion.");
			headerIn.setAppName(CODIGO_APPNAME_DISPONIBIIDAD_4GLTE);
			headerIn.setUserName(CODIGO_USERNAME_DISPONIBILIDAD_4GLTE);
			requestType.setHeaderIn(headerIn);
			requestType.setCodPlan(codigoPlan);
			LOGGER.info("Invocando servicio");

			consultaPlanesRequestType.setRequest(requestType);
			consultaPlanesResponseType = port
					.consultaDisponibilidadPlanLte(consultaPlanesRequestType);
		} catch (Exception e) {
			LOGGER
					.error(
							"Exception caught on Service invocation: consultaPlanesMultimedia",
							e);
			LOGGER.error( new DAOException(e));
		}
		
		responseType = consultaPlanesResponseType.getResponse();
				
		LOGGER.info("Disponibilidad4GLte: " + responseType.getDisponibilidadLte());
		LOGGER.info("NombrePlanBSCS: " + responseType.getNombrePlanBSCS());
		
		Plan4GLteBean plan4GLte = new Plan4GLteBean();
		
		String codigoRespuesta = responseType.getHeaderOut().getCodigo();
		String descripcionRespuesta = responseType.getHeaderOut().getDescripcion();

		LOGGER.info("codigoRespuesta: " + codigoRespuesta);
		LOGGER.info("descripcionRespuesta: " + descripcionRespuesta);
		
		if(codigoRespuesta.equals(CODIGO_RESPUESTA_DISPONIBILIDAD4GLTE_OK)){
			
			plan4GLte.setDisponibilidadLte(responseType.getDisponibilidadLte());
			plan4GLte.setNombrePlanBscs(responseType.getNombrePlanBSCS());
			
		}
		else{
			
			LOGGER.info("Respuesta del Servicio : " + codigoRespuesta + " - "
					+ descripcionRespuesta);
			LOGGER.error( new ServiceException(codigoRespuesta, descripcionRespuesta));
			
		}
		
		return plan4GLte;
	}
	
	//SC 31-07-2014 Luis || Categoría de Cliente Dashboard alineada con ZonaMisPuntos
	public String consultarCategoriaCliente(String numeroPCSeleccionado) throws DAOException, ServiceException {
		
  	  String categoriaCliente = "";
  	  ClientePerfilServicePortType port = null;
  	  LOGGER.info("Instanciando el port-ClientePerfilService desde DashBoard" + ClientePerfilServicePortType.class);
  	  
  	  try {
            port = (ClientePerfilServicePortType) WebServiceLocator.getInstance().getPort(
          		  ClientePerfilService.class, ClientePerfilServicePortType.class);
        } catch (WebServiceLocatorException e) {
            LOGGER.error("Error al inicializar el Port " + ClientePerfilServicePortType.class, e);
            LOGGER.error( new DAOException(e));
        }
        
        ConsultarUsuarioBuicType request = new ConsultarUsuarioBuicType();
        ResultadoConsultarUsuarioBuicType response = null;

        try {

            LOGGER.info("Configurando Datos de la peticion");
            request.setMsisdn(numeroPCSeleccionado);

            LOGGER.info("Invocando servicio-ConsultarUsuarioBuic");
            response = port.consultarUsuarioBuic(request);

        } catch (Exception e) {
            LOGGER.error("Exception caught on Service invocation: consultarUsuarioBuic",
                    e);
            LOGGER.error( new DAOException(e));
        }

        String codigoRespuesta = response.getRespuesta().getCodigo();
        String descripcionRespuesta = response.getRespuesta().getDescripcion();

        LOGGER.info("codigoRespuesta " + codigoRespuesta);
        LOGGER.info("descripcionRespuesta " + descripcionRespuesta);

        if (Utils.isEmptyString(codigoRespuesta)) {
            LOGGER.error( new DAOException("consultarUsuarioBuic: Servicio no responde con codigoRespuesta"));
        }
        
        if (CODIGO_RESPUESTA_OK.equals(codigoRespuesta)) {

            try {
          	  UsuarioBuicType usuarioBuicType = response.getUsuarioBuic();
                
          	  if(usuarioBuicType == null){
                    LOGGER.info("Informacion de usuario (BUIC) no disponible");
                }
                else{
              	  //Categoria del cliente para la zona entel
                    categoriaCliente = usuarioBuicType.getVarCuantitativa2();
                }                
                
            } catch (Exception e) {
                LOGGER.error("Exception caught on Service response: "
                        + "consultarUsuarioBuic", e);
                LOGGER.error( new DAOException(e));
            }

        }
        else {
            LOGGER.info("consultarUsuarioBuic: Service error code received: "
                    + codigoRespuesta + " - " + descripcionRespuesta);
            LOGGER.error( new ServiceException(codigoRespuesta, descripcionRespuesta));
        }
        
        return categoriaCliente;
    }
	//FIN SC 31-07-2014 Luis

}
