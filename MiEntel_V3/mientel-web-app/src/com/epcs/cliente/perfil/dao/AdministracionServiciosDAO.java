/* Propiedad de Entel PCS. Todos los derechos reservados */
package com.epcs.cliente.perfil.dao;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import org.apache.log4j.Logger;

import weblogic.utils.classfile.FieldBean;


import com.epcs.bean.AdminServicioBuzonBean;
import com.epcs.bean.AdministracionServiciosBean;
import com.epcs.bean.BundleContent;
import com.epcs.bean.CampanaSAGENBean;
import com.epcs.bean.CatalogoSAGENBean;
import com.epcs.bean.CatalogoServiciosBean;
import com.epcs.bean.CategoriaSAGENBean;
import com.epcs.bean.CategorySagenBean;
import com.epcs.bean.CiudadBean;
import com.epcs.bean.CombinacionVas;
import com.epcs.bean.ComunaBean;
import com.epcs.bean.FamiliaSuscripcionBean;
import com.epcs.bean.PackContent;
import com.epcs.bean.PageIndexBean;
import com.epcs.bean.PaginaServiciosSagenBean;
import com.epcs.bean.CustomSAGENBean;
import com.epcs.bean.DireccionBean;
import com.epcs.bean.MmsIshopImovilBean;
import com.epcs.bean.MsisdnCobroRevertidoBean;
import com.epcs.bean.RegionBean;
import com.epcs.bean.ResultadoConsultarSAGENBean;
import com.epcs.bean.ResultadoCrearSuscripcionSAGENBean;
import com.epcs.bean.ResultadoEliminarSuscripcionSAGENBean;
import com.epcs.bean.ResultadoFamiliaSegmentate;
import com.epcs.bean.ResultadoServicioBean;
import com.epcs.bean.ResultadoServiciosSSCCBean;
import com.epcs.bean.RutBean;
import com.epcs.bean.ServicioBean;
import com.epcs.bean.ServicioBuzonBean;
import com.epcs.bean.ServicioCobroRevertidoBean;
import com.epcs.bean.ServicioEmasBean;
import com.epcs.bean.ServicioFactElectronicaBean;
import com.epcs.bean.ServicioGprsMmsBean;
import com.epcs.bean.ServicioListaAvisaleBean;
import com.epcs.bean.ServicioMmsIshopImovilBean;
import com.epcs.bean.ServicioRecepcionCobroRevertidoBean;
import com.epcs.bean.ServicioSagenBean;
import com.epcs.bean.SubcategorySagenBean;
import com.epcs.bean.SuscripcionSAGENBean;
import com.epcs.bean.UsuarioSAGENBean;
import com.epcs.cliente.perfil.ClientePerfilService;
import com.epcs.cliente.perfil.ClientePerfilServicePortType;
import com.epcs.cliente.perfil.dao.servicios.ServiciosAvisaleDAO;
import com.epcs.cliente.perfil.dao.servicios.ServiciosBuzonDAO;
import com.epcs.cliente.perfil.dao.servicios.ServiciosCobroRevertidoDAO;
import com.epcs.cliente.perfil.dao.servicios.ServiciosFacturaElectronicaDAO;
import com.epcs.cliente.perfil.dao.servicios.ServiciosIVRDAO;
import com.epcs.cliente.perfil.dao.servicios.ServiciosPPDAO;
import com.epcs.cliente.perfil.dao.servicios.ServiciosSSCCDAO;
import com.epcs.cliente.perfil.types.ConsultarEstadoServicioDisponiblesSSCCType;
import com.epcs.cliente.perfil.types.ConsultarListaComunik2Type;
import com.epcs.cliente.perfil.types.ListaComunik2;
import com.epcs.cliente.perfil.types.RespuestaServCobroRevertidoType;
import com.epcs.cliente.perfil.types.RespuestaServEmasType;
import com.epcs.cliente.perfil.types.RespuestaServFactElectronicaType;
import com.epcs.cliente.perfil.types.RespuestaServGprsMMSType;
import com.epcs.cliente.perfil.types.RespuestaServListaAvisaleType;
import com.epcs.cliente.perfil.types.RespuestaServMmsIshopImovilType;
import com.epcs.cliente.perfil.types.RespuestaServicioType;
import com.epcs.cliente.perfil.types.ResultadoConsultarEstadoServicioDisponiblesSSCCType;
import com.epcs.cliente.perfil.types.ResultadoConsultarListaComunik2Type;
import com.epcs.recursoti.configuracion.DateHelper;
import com.epcs.recursoti.configuracion.MiEntelProperties;
import com.epcs.recursoti.configuracion.Utils;
import com.epcs.recursoti.configuracion.locator.WebServiceLocator;
import com.epcs.recursoti.configuracion.locator.WebServiceLocatorException;
import com.epcs.recursoti.excepcion.DAOException;
import com.epcs.recursoti.excepcion.ServiceException;

//catalogo
import com.epcs.provision.suscripcion.CatalogSuscriptionResponse;
import com.epcs.provision.suscripcion.Sagen;
import com.epcs.provision.suscripcion.wssagenservice.ObtenerCatalogoServicePortType; 
import com.epcs.provision.suscripcion.wssagenservice.ObtenerCatalogoService;
import com.epcs.provision.suscripcion.Custom;

import com.epcs.provision.suscripcion.wssagenservice.types.CategoryType;
import com.epcs.provision.suscripcion.wssagenservice.types.ContentType;
import com.epcs.provision.suscripcion.wssagenservice.types.MktCategoryType;
import com.epcs.provision.suscripcion.wssagenservice.types.ResultadoCatalogSuscriptionsType;
import com.epcs.provision.suscripcion.wssagenservice.types.CatalogSuscriptionsType;
import com.epcs.provision.suscripcion.wssagenservice.types.SubcategoryType;
import com.epcs.provision.suscripcion.wssagenservice.types.SuscriptionType;
import com.epcs.provision.suscripcion.wssagenservice.types.RespuestaType;



//buscar suscripcion
import com.epcs.provision.suscripcion.wssagenservice.buscarSuscripcionService.BuscarSuscripcionServicePortType;
import com.epcs.provision.suscripcion.wssagenservice.buscarSuscripcionService.BuscarSuscripcionService;
import com.epcs.provision.suscripcion.wssagenservice.buscarSuscripcionService.SearchSuscriptionType;
import com.epcs.provision.suscripcion.wssagenservice.buscarSuscripcionService.ResultadoSearchSuscriptionType;

//crear suscripcion

import com.epcs.provision.suscripcion.wssagenservice.crearsuscripcionservice.ContentBundleType;
import com.epcs.provision.suscripcion.wssagenservice.crearsuscripcionservice.CrearSuscripcionServicePortType;
import com.epcs.provision.suscripcion.wssagenservice.crearsuscripcionservice.CrearSuscripcionService;
import com.epcs.provision.suscripcion.wssagenservice.crearsuscripcionservice.CreateSuscriptionType;
import com.epcs.provision.suscripcion.wssagenservice.crearsuscripcionservice.OptionalContentsType;
import com.epcs.provision.suscripcion.wssagenservice.crearsuscripcionservice.ResultadoCreateSuscriptionType;
import com.epcs.provision.suscripcion.wssagenservice.crearsuscripcionservice.CustomType;
import com.epcs.provision.suscripcion.wssagenservice.crearsuscripcionservice.MktCustomerType;
import com.epcs.provision.suscripcion.wssagenservice.crearsuscripcionservice.MktDireccionType;
import com.epcs.provision.suscripcion.wssagenservice.crearsuscripcionservice.PackContentsType;
import com.epcs.provision.suscripcion.wssagenservice.crearsuscripcionservice.BundleContentsType;


//eliminar suscripcion
import com.epcs.provision.suscripcion.wssagenservice.eliminarSuscripcionService.EliminarSuscripcionServicePortType;
import com.epcs.provision.suscripcion.wssagenservice.eliminarSuscripcionService.EliminarSuscripcionService;
import com.epcs.provision.suscripcion.wssagenservice.eliminarSuscripcionService.DeleteSuscriptionType;
import com.epcs.provision.suscripcion.wssagenservice.eliminarSuscripcionService.ResultadoDeleteSuscriptionType;


// estado de la familia o servicio
import com.esa.provision.suscripcion.serviciossegmentate.ClientServiceStatusService;
import com.esa.provision.suscripcion.serviciossegmentate.ClientServiceStatusServicePortType;
import com.esa.provision.suscripcion.serviciossegmentate.types.ResultadoClientServiceStatusRequestType;
import com.esa.provision.suscripcion.serviciossegmentate.types.ClientServiceStatusRequestType;
import com.esa.provision.suscripcion.serviciossegmentate.types.ServiceType;


//colocar la familia en lista negra

import com.esa.provision.suscripcion.serviciossegmentate.PutIntoBlackListServicePortType;
import com.esa.provision.suscripcion.serviciossegmentate.PutIntoBlackListService;
import com.esa.provision.suscripcion.serviciossegmentate.types.ResultadoPutIntoBlackListRequestType;
import com.esa.provision.suscripcion.serviciossegmentate.types.PutIntoBlackListRequestType;

//sacar la familia la lista negra
import com.esa.provision.suscripcion.serviciossegmentate.DeleteFromBlackListService;
import com.esa.provision.suscripcion.serviciossegmentate.DeleteFromBlackListServicePortType;
import com.esa.provision.suscripcion.serviciossegmentate.types.ResultadoDeleteFromBlackListRequestType;
import com.esa.provision.suscripcion.serviciossegmentate.types.DeleteFromBlackListRequestType;
import com.epcs.recursoti.configuracion.Utils;






/*
import com.epcs.provision.suscripcion.ResultSuscription;
import com.epcs.provision.suscripcion.WSSagenService;
import com.epcs.provision.suscripcion.Sagen;

import com.epcs.provision.suscripcion.CreateSuscriptionRequest;
import com.epcs.provision.suscripcion.CreateSuscriptionResponse;

import com.epcs.provision.suscripcion.DeleteSuscriptionRequest;
import com.epcs.provision.suscripcion.DeleteSuscriptionResponse;

import com.epcs.provision.suscripcion.Categories;
import com.epcs.provision.suscripcion.Subcategory;
import com.epcs.provision.suscripcion.CategoryCatalog;
import com.epcs.provision.suscripcion.Custom;
import com.epcs.provision.suscripcion.Customer;
import com.epcs.provision.suscripcion.MktDireccion;
import com.epcs.provision.suscripcion.SearchSuscriptionRequest;
import com.epcs.provision.suscripcion.SearchSuscriptionResponse;
import com.epcs.provision.suscripcion.Suscripcion;
import com.epcs.provision.suscripcion.CatalogSuscriptionResponse;
import com.epcs.provision.suscripcion.Suscription;
import com.epcs.provision.suscripcion.Category;
import com.epcs.provision.suscripcion.Campaigns;
import com.epcs.provision.suscripcion.Campaign;
*/



/**
 * @author jmanzur (I2B) en nombre de Absalon Opazo (Atencion al Cliente,
 *         EntelPcs)
 * 
 */
public class AdministracionServiciosDAO {

    private static final Logger LOGGER = Logger
            .getLogger(AdministracionServiciosDAO.class);

    /**
     * Valor de exito para la respuesta a un servicio
     */
    private static final String CODIGO_RESPUESTA_OK = MiEntelProperties
            .getProperty("servicios.codigoRespuesta.exito");

    /**
     * Representa el Estado de mostrar servicio que entrega el OSB 
     */
    private static final String MOSTRAR_TRUE = MiEntelProperties
            .getProperty("parametros.adminServicios.mostrar");
    
// No usado de momento    
//    /**
//     * Estado de No mostrar servicio
//     */
//    private static final String MOSTRAR_FALSE = MiEntelProperties
//            .getProperty("parametros.adminServicios.noMostrar");

    /**
     * Representa esatdo 'Hablitado' que entrega el OSB
     */
    private static final String HABILITADO = MiEntelProperties
            .getProperty("parametros.adminServicios.estado.habilitado");

    /**
     * Representa esatdo 'Deshablitado' que entrega el OSB
     */
    private static final String DESHABILITADO = MiEntelProperties
            .getProperty("parametros.adminServicios.estado.deshabilitado");

    /**
     * Representa estado 'Error' que entrega el OSB
     */
    private static final String ERROR = MiEntelProperties
            .getProperty("parametros.adminServicios.estado.error");
    
    /**
     * Representa estado 'Error' que entrega el OSB
     */
    private static final String CODIGO_ESTADO_ERROR = MiEntelProperties
            .getProperty("parametros.adminServicios.codigoEstado.error");
    
    /**
     * Representa estado 'Error' que entrega el OSB
     */
    private static final String DESC_ESTADO_ERROR = MiEntelProperties
            .getProperty("parametros.adminServicios.descEstado.inconsistencia");
    
    /**
     * Representa estado 'Error' que entrega el OSB
     */
    private static final String MOSTRAR_ESTADO_ERROR = MiEntelProperties
            .getProperty("parametros.adminServicios.mostrarEstado.error");
    
    private static final String SAGEN_VAS_ID = MiEntelProperties
    		.getProperty("parametros.adminServicios.MMKT.vasID");
    
    private static final String SAGEN_PROVIDER_ID = MiEntelProperties
    		.getProperty("parametros.adminServicios.MMKT.providerID");

    private static final String SAGEN_SERVICE_ID = MiEntelProperties
    		.getProperty("parametros.adminServicios.MMKT.serviceID");
    
    private static final String SAGEN_SUSCRIPTION_ID = MiEntelProperties
    		.getProperty("parametros.adminServicios.MMKT.suscriptionID");
    
    private static final String SAGEN_LA = MiEntelProperties
    		.getProperty("parametros.adminServicios.MMKT.la");
    
    private static final String SAGEN_CANAL = MiEntelProperties
    		.getProperty("parametros.adminServicios.MMKT.canal");
    
    
    //hcastillo
	/** The Constant TOPE_REGISTROS_PAGINA_SERVICIOS. */
	public static final String TOPE_REGISTROS_PAGINA_HISTORIAL = MiEntelProperties
			.getProperty("parametros.adminServicios.topeRegistrosPagina");
	 //hcastillo
	public static final String ESTADO_HABILITADO_SERVICIO = MiEntelProperties
	.getProperty("parametros.adminServicios.estado.habilitado");
	//hcastillo
	public static final String ESTADO_DESHABILITADO_SERVICIO = MiEntelProperties
	.getProperty("parametros.adminServicios.estado.deshabilitado");
	//hcastillo
	public static final String ESTADO_BLOQUEADO_SERVICIO = MiEntelProperties
	.getProperty("parametros.adminServicios.estado.bloqueado");
	
	
	public static final String SERVICIO_INLISTA_NEGRA=MiEntelProperties
	.getProperty("parametros.adminServicios.listanegra.servicein");
	
	public static final String SERVICIO_OUTLISTA_NEGRA=MiEntelProperties
	.getProperty("parametros.adminServicios.listanegra.serviceout");
	
	
	public static final String FAMILIA_MOTIVO=MiEntelProperties
	.getProperty("parametros.familias.codmotivo");
	
	public static final String CANAL = MiEntelProperties
	.getProperty("parametros.adminServicios.canal");

	
    /**
     * DAO para servicios comunes mercados SS y CC
     */
    private ServiciosSSCCDAO serviciosSSCCDAO;
    
    /**
     * DAO para servicios de IVR
     */
    private ServiciosIVRDAO serviciosIVRDAO;
    
    /**
     * DAO para servicios de Buzon
     */
    private ServiciosBuzonDAO serviciosBuzonDAO;

    /**
     * DAO para servicios de Avisale
     */
    private ServiciosAvisaleDAO serviciosAvisaleDAO;
    
    /**
     * DAO para servicios de Avisale
     */
    private ServiciosFacturaElectronicaDAO serviciosFacturaEDAO;
    
    /**
     * DAO para servicios Cobro Revertido
     */
    private ServiciosCobroRevertidoDAO serviciosCobroRevertidoDAO;
        

    /**
     * DAO para servicios de mercado PP
     */
    private ServiciosPPDAO serviciosPPDAO;
    
    public AdministracionServiciosDAO() {
        super();
        this.serviciosSSCCDAO = new ServiciosSSCCDAO();
        this.serviciosIVRDAO = new ServiciosIVRDAO();
        this.serviciosBuzonDAO = new ServiciosBuzonDAO();
        this.serviciosAvisaleDAO = new ServiciosAvisaleDAO();
        this.serviciosPPDAO = new ServiciosPPDAO();
        this.serviciosFacturaEDAO = new ServiciosFacturaElectronicaDAO();
        this.serviciosCobroRevertidoDAO = new ServiciosCobroRevertidoDAO();
    }
    
    /**
     * Consulta los servicios para un usuario
     * 
     * @param msisdn
     * @param mercado 
     * @return bean AdministracionServiciosBean
     * @throws DAOException
     * @throws ServiceException
     */
    public AdministracionServiciosBean consultarEstadoServiciosDisponibles(
            String msisdn, String mercado) throws DAOException, ServiceException {

        LOGGER.info("Instanciando el Port.");
        ClientePerfilServicePortType port = null;
        AdministracionServiciosBean adminServicios = null;
        try {
            port = (ClientePerfilServicePortType) WebServiceLocator
                    .getInstance().getPort(ClientePerfilService.class,
                            ClientePerfilServicePortType.class);

        } catch (WebServiceLocatorException e) {
            LOGGER.error("Error al inicializar el Port "
                    + ClientePerfilService.class);
            LOGGER.error( new DAOException(e));
        }

        ConsultarEstadoServicioDisponiblesSSCCType request = new ConsultarEstadoServicioDisponiblesSSCCType();
        ResultadoConsultarEstadoServicioDisponiblesSSCCType response = null;

        try {
            LOGGER.info("Configurando parametros de la peticion");
            request.setMsisdn(msisdn);
            request.setMercado(mercado);
            LOGGER.info("Invocando servicio");
            response = port.consultarEstadoServicioDisponiblesSSCC(request);

        } catch (Exception e) {
            LOGGER.error("Exception caught on Service invocation", e);
            LOGGER.error( new DAOException(e));
        }

        String codigoRespuesta = response.getRespuesta().getCodigo();
        String descripcionRespuesta = response.getRespuesta().getDescripcion();

        LOGGER.info("codigoRespuesta " + codigoRespuesta);
        LOGGER.info("descripcionRespuesta " + descripcionRespuesta);

        if (CODIGO_RESPUESTA_OK.equals(codigoRespuesta)) {

            try {

                adminServicios = buildServiciosSSCC(response);

            } catch (Exception e) {
                LOGGER.error("Exception caught on Service response", e);
                LOGGER.error( new DAOException(e));
            }
        }
        else {
            LOGGER.info("Service error code received: " + codigoRespuesta
                    + " - " + descripcionRespuesta);
            LOGGER.error( new ServiceException(codigoRespuesta, descripcionRespuesta));
        }

        return adminServicios;
    }
    
    
    /**
     * Consulta la lista de numeros asociadas
     * 
     * @param msisdn
     * @return bean ServicioCobroRevertidoBean
     * @throws DAOException
     * @throws ServiceException
     */
    public ServicioCobroRevertidoBean consultarListaComunik2(
            String msisdn) throws DAOException, ServiceException {

        LOGGER.info("Instanciando el Port.");
        ClientePerfilServicePortType port = null;
        ServicioCobroRevertidoBean servicioCobroRevertidoBean = null;
        try {
            port = (ClientePerfilServicePortType) WebServiceLocator
                    .getInstance().getPort(ClientePerfilService.class,
                            ClientePerfilServicePortType.class);

        } catch (WebServiceLocatorException e) {
            LOGGER.error("Error al inicializar el Port "
                    + ClientePerfilService.class);
            LOGGER.error( new DAOException(e));
        }

        ConsultarListaComunik2Type  request = new ConsultarListaComunik2Type();
        ResultadoConsultarListaComunik2Type response = null;

        try {
            LOGGER.info("Configurando parametros de la peticion");
            
            request.setMsisdn(msisdn);
            LOGGER.info("Invocando servicio");
            response = port.consultarListaComunik2(request);

        } catch (Exception e) {
            LOGGER.error("Exception caught on Service invocation", e);
            LOGGER.error( new DAOException(e));
        }

        String codigoRespuesta = response.getRespuesta().getCodigo();
        String descripcionRespuesta = response.getRespuesta().getDescripcion();

        LOGGER.info("codigoRespuesta " + codigoRespuesta);
        LOGGER.info("descripcionRespuesta " + descripcionRespuesta);

        if (CODIGO_RESPUESTA_OK.equals(codigoRespuesta)) {

            try {
                servicioCobroRevertidoBean = new ServicioCobroRevertidoBean();
                servicioCobroRevertidoBean.setMostrar(response.getMostrar());
                servicioCobroRevertidoBean.setMostrarEstado(response.getMostrarEstado());
                servicioCobroRevertidoBean.setEstadoSponsor(response.getEstadoSponsor());
                try{
                servicioCobroRevertidoBean.setMaximoMensual(Double.valueOf(response.getMaximoMensual()));
                }catch(Exception e){
                    LOGGER.warn("Maximo mensual no valido servicio cobro revrtido :"+msisdn);
                }
                try{
                servicioCobroRevertidoBean.setSaldoMes(Double.valueOf(response.getSaldoMes()));
                }catch(Exception e){
                    LOGGER.warn("Saldo Mes no valido servicio cobro revrtido :"+msisdn);
                }
                try{
                servicioCobroRevertidoBean.setSaldoMesMinutos(response.getSaldoMesMinutos());
                }catch(Exception e){
                    LOGGER.warn("Saldo Minutos no valido servicio cobro revrtido :"+msisdn);
                }
                try{
                servicioCobroRevertidoBean.setValorMinutoPrepago(Double.valueOf(response.getValorMinutoPrepago()));
                }catch(Exception e){
                    LOGGER.warn("Valor minuto Prepago no valido servicio cobro revrtido :"+msisdn);
                }
                
                for(ListaComunik2 lista :  response.getListaComunik2()){
                    servicioCobroRevertidoBean.addMsisdn(new MsisdnCobroRevertidoBean(lista.getMsisdn(), DateHelper.parseDate(lista.getFecha(),DateHelper.FORMAT_yyyyMMddhhmmss)));
                }
                
                servicioCobroRevertidoBean.setActivo(solveStatusServicio(response.getMostrarEstado()));
                
                if(MOSTRAR_TRUE.equals(response.getMostrar())) {
                    servicioCobroRevertidoBean.setVisible(true);
                }

            } catch (Exception e) {
                LOGGER.error("Exception caught on Service response", e);
                LOGGER.error( new DAOException(e));
            }
        }
        else {
            LOGGER.info("Service error code received: " + codigoRespuesta
                    + " - " + descripcionRespuesta);
            LOGGER.error( new ServiceException(codigoRespuesta, descripcionRespuesta));
        }

        return servicioCobroRevertidoBean;
    }
    
    

    /**
     * Construye cada Servicio del Usuario
     * 
     * @param servicios
     * @return AdministracionServiciosBean
     * @throws Exception
     */
    private AdministracionServiciosBean buildServiciosSSCC(
            ResultadoConsultarEstadoServicioDisponiblesSSCCType servicios)
            throws Exception {
        AdministracionServiciosBean adminServicios = new AdministracionServiciosBean();

        // Servicio AsisTotal
        ServicioBean servicioAsisTotal = buildServiceGeneric(servicios
                .getServicioAsistotal());
        adminServicios.setServicioAsisTotal(servicioAsisTotal);

        // Servicio Avisale
        ServicioBean servicioAvisale = buildServiceGeneric(servicios
                .getServicioAvisale());
        adminServicios.setServicioAvisale(servicioAvisale);

        // Servicio Buzon
        ServicioBuzonBean servicioBuzon = buildServiceBuzon(servicios
                .getServicioBuzon());
        adminServicios.setServicioBuzon(servicioBuzon);

        // Servicio cmk2
        ServicioBean servicioCmk2 = buildServiceGeneric(servicios
                .getServicioCmk2());
        adminServicios.setServicioCmk2(servicioCmk2);

        // Servicio Interarea
        ServicioBean servicioInterarea = buildServiceGeneric(servicios
                .getServicioInterarea());
        adminServicios.setServicioInterarea(servicioInterarea);

        // Servicio LDI
        ServicioBean servicioLdi = buildServiceGeneric(servicios
                .getServicioLdi());
        adminServicios.setServicioLdi(servicioLdi);

        // Servicio Nip
        ServicioBean servicioNip = buildServiceGeneric(servicios
                .getServicioNip());
        adminServicios.setServicioNip(servicioNip);

        // Servicio Num3000
        ServicioBean servicioNum300 = buildServiceGeneric(servicios
                .getServicioNum300());
        adminServicios.setServicioNum300(servicioNum300);

        // Servicio Num606
        ServicioBean servicioNum606 = buildServiceGeneric(servicios
                .getServicioNum606());
        adminServicios.setServicioNum606(servicioNum606);

        // Servicio Num609
        ServicioBean servicioNum609 = buildServiceGeneric(servicios
                .getServicioNum609());
        adminServicios.setServicioNum609(servicioNum609);

        // Servicio Num700
        ServicioBean servicioNum700 = buildServiceGeneric(servicios
                .getServicioNum700());
        adminServicios.setServicioNum700(servicioNum700);

        // Servicio Roaming
        ServicioBean servicioRoaming = buildServiceGeneric(servicios
                .getServicioRoaming());
        adminServicios.setServicioRoaming(servicioRoaming);

        // Servicio Wap
        ServicioBean servicioWap = buildServiceGeneric(servicios
                .getServicioWap());
        adminServicios.setServicioWap(servicioWap);

        // Servicio Recepcion Cobro Revertido
        ServicioRecepcionCobroRevertidoBean servicioCobroRevertido = buildServicioCobroRevertido(servicios
                .getServicioCobroRevertido());
        adminServicios.setServicioRecepcionCobroRevertidoBean(servicioCobroRevertido);

        // Servicio Emas
        ServicioEmasBean servicioEmasBean = buildServicioEmas(servicios
                .getServicioEmas());
        adminServicios.setServicioEmasBean(servicioEmasBean);

        // Servicio FactElectronica
        ServicioFactElectronicaBean factElectronicaBean = buildFactElectronica(servicios
                .getServicioFactElectronica());
        adminServicios.setServicioFactElectronicaBean(factElectronicaBean);

        // Servicio GprsMms
        ServicioGprsMmsBean servicioGprsMms = buildGprs(servicios
                .getServicioGprs());
        adminServicios.setServicioGprsMms(servicioGprsMms);

        // Servicio ListaAvisale
        ServicioListaAvisaleBean listaAvisaleBean = buildServicioListaAvisale(servicios
                .getServicioListaAvisale());
        adminServicios.setServicioListaAvisaleBean(listaAvisaleBean);

        // Servicio MmsIshopImovil
        ServicioMmsIshopImovilBean bean = buildServicioMmsIshopImovil(servicios
                .getServicioMmsIshopImovil());
        adminServicios.setMmsIshopImovilBean(bean);

        return adminServicios;

    }

    /**
     * Construye un servicio Generico.
     * 
     * @param servicio
     * @return ServicioBean
     */
    private ServicioBean buildServiceGeneric(RespuestaServicioType servicio) {
        ServicioBean servicioBean = new ServicioBean();
        try {

            servicioBean.setIdServicio(servicio.getIdServicio());
            servicioBean.setRespuesta(servicio.getCodRespServ());
            servicioBean.setDescRespuesta(servicio.getDescEstado());
            servicioBean.setTercerEstado(servicio.getTercerEstado());
            if(MOSTRAR_TRUE.equals(servicio.getMostrar())) {
                servicioBean.setVisible(true);
            }
            servicioBean.setMostrar(servicio.getMostrar());
            servicioBean.setMostrarEstado(servicio.getMostrarEstado());
            
            servicioBean.setActivo(solveStatusServicio(servicio.getMostrarEstado()));

        } catch (Exception e) {
            // El servicio no se mostrara
            servicioBean.setMostrar("N");
            servicioBean.setVisible(false);
        }
        return servicioBean;
    }

    /**
     * Construye un servicio Generico.
     * 
     * @param servicio
     * @return ServicioBean
     */
    private ServicioBuzonBean buildServiceBuzon(RespuestaServicioType servicio) {
        ServicioBuzonBean servicioBean = new ServicioBuzonBean();
        try {

            servicioBean.setIdServicio(servicio.getIdServicio());
            servicioBean.setRespuesta(servicio.getCodRespServ());
            servicioBean.setDescRespuesta(servicio.getDescEstado());
            if(MOSTRAR_TRUE.equals(servicio.getMostrar())) {
                servicioBean.setVisible(true);
            }
            servicioBean.setMostrar(servicio.getMostrar());
            servicioBean.setMostrarEstado(servicio.getMostrarEstado());
            
            servicioBean.setActivo(solveStatusServicio(servicio.getMostrarEstado()));

            // Determina si es buzon premium mms o email, o si es basico
            solveTipoBuzon(servicioBean, servicio);
            
        } catch (Exception e) {
            // El servicio no se mostrara
            servicioBean.setMostrar("N");
            servicioBean.setVisible(false);
        }
        return servicioBean;
    }

    
    /**
     * 
     * @param servicio
     * @return ServicioCobroRevertidoBean
     */
    private ServicioRecepcionCobroRevertidoBean buildServicioCobroRevertido(
            RespuestaServCobroRevertidoType servicio) {
        ServicioRecepcionCobroRevertidoBean servicioCobroRev = new ServicioRecepcionCobroRevertidoBean();
        try {
            servicioCobroRev.setDescRespuesta(servicio.getDescEstado());
            servicioCobroRev.setIdServicio(servicio.getIdServicio());
            
            if(MOSTRAR_TRUE.equals(servicio.getMostrar())) {
                servicioCobroRev.setVisible(true);
            }
            servicioCobroRev.setMostrar(servicio.getMostrar());
            
            servicioCobroRev.setMostrarEstado(servicio.getMostrarEstado());
            servicioCobroRev.setActivo(solveStatusServicio(servicio.getMostrarEstado()));

            
            servicioCobroRev.setRespuesta(servicio.getCodRespServ());
            servicioCobroRev.setTercerEstado(servicio.getTercerEstado());

            servicioCobroRev.setParametro5(servicio.getParametro5());
            servicioCobroRev.setMsisdn(servicio.getMsisdn());
        } catch (Exception e) {
            // El servicio no se mostrara
            servicioCobroRev.setMostrar("N");
            servicioCobroRev.setVisible(false);
        }
        return servicioCobroRev;
    }

    /**
     * 
     * @param servicio
     * @return ServicioEmasBean
     */
    private ServicioEmasBean buildServicioEmas(RespuestaServEmasType servicio) {
        ServicioEmasBean servicioEmas = new ServicioEmasBean();
        try {
            servicioEmas.setDescRespuesta(servicio.getDescEstado());
            servicioEmas.setIdServicio(servicio.getIdServicio());
            
            if(MOSTRAR_TRUE.equals(servicio.getMostrar())) {
                servicioEmas.setVisible(true);
            }
            servicioEmas.setMostrar(servicio.getMostrar());
            
            servicioEmas.setMostrarEstado(servicio.getMostrarEstado());
            servicioEmas.setActivo(solveStatusServicio(servicio.getMostrarEstado()));
            
            servicioEmas.setRespuesta(servicio.getCodRespServ());
            servicioEmas.setTercerEstado(servicio.getTercerEstado());

            servicioEmas.setParametro5(servicio.getParametro5());
            servicioEmas.setParametro7(servicio.getParametro7());
            servicioEmas.setMsisdn(servicio.getMsisdn());

        } catch (Exception e) {
            // El servicio no se mostrara
            servicioEmas.setMostrar("N");
            servicioEmas.setVisible(false);
        }
        return servicioEmas;
    }

    /**
     * 
     * @param servicio
     * @return ServicioFactElectronicaBean
     */
    private ServicioFactElectronicaBean buildFactElectronica(
            RespuestaServFactElectronicaType servicio) {
        ServicioFactElectronicaBean servicioFactEle = new ServicioFactElectronicaBean();
        try {
            servicioFactEle.setDescRespuesta(servicio.getDescEstado());
            servicioFactEle.setIdServicio(servicio.getIdServicio());
            
            if(MOSTRAR_TRUE.equals(servicio.getMostrar())) {
                servicioFactEle.setVisible(true);
            }
            servicioFactEle.setMostrar(servicio.getMostrar());

            servicioFactEle.setMostrarEstado(servicio.getMostrarEstado());
            servicioFactEle.setActivo(solveStatusServicio(servicio.getMostrarEstado()));
            
            servicioFactEle.setRespuesta(servicio.getCodRespServ());
            servicioFactEle.setTercerEstado(servicio.getTercerEstado());

            servicioFactEle.setNumeroCuenta(servicio.getNumeroCuenta());
            servicioFactEle.setParametro6(servicio.getParametro6());
            servicioFactEle.setMsisdn(servicio.getMsisdn());

        } catch (Exception e) {
            // El servicio no se mostrara
            servicioFactEle.setMostrar("N");
            servicioFactEle.setVisible(false);
        }
        return servicioFactEle;
    }

    /**
     * 
     * @param servicio
     * @return ServicioGprsMms
     */
    private ServicioGprsMmsBean buildGprs(RespuestaServGprsMMSType servicio) {
        ServicioGprsMmsBean servicioGprsMms = new ServicioGprsMmsBean();
        try {
            servicioGprsMms.setDescRespuesta(servicio.getDescEstado());
            servicioGprsMms.setIdServicio(servicio.getIdServicio());
            
            if(MOSTRAR_TRUE.equals(servicio.getMostrar())) {
                servicioGprsMms.setVisible(true);
            }
            servicioGprsMms.setMostrar(servicio.getMostrar());
            
            servicioGprsMms.setMostrarEstado(servicio.getMostrarEstado());
            servicioGprsMms.setActivo(solveStatusServicio(servicio.getMostrarEstado()));
            
            servicioGprsMms.setRespuesta(servicio.getCodRespServ());
            servicioGprsMms.setTercerEstado(servicio.getTercerEstado());

            servicioGprsMms.setParametro6(servicio.getParametro6());
            servicioGprsMms.setMsisdn(servicio.getMsisdn());
            
            if(CODIGO_ESTADO_ERROR.equalsIgnoreCase(servicio.getEstado())
            		&& DESC_ESTADO_ERROR.equalsIgnoreCase(servicio.getDescEstado())
            		&& MOSTRAR_ESTADO_ERROR.equalsIgnoreCase(servicio.getMostrarEstado())){
            	servicioGprsMms.setVisible(false);
            }

        } catch (Exception e) {
            // El servicio no se mostrara
            servicioGprsMms.setMostrar("N");
            servicioGprsMms.setVisible(false);
        }
        return servicioGprsMms;
    }

    /**
     * 
     * @param servicio
     * @return ServicioListaAvisaleBean
     */
    private ServicioListaAvisaleBean buildServicioListaAvisale(
            RespuestaServListaAvisaleType servicio) {
        ServicioListaAvisaleBean listaAvisaleBean = new ServicioListaAvisaleBean();
        try {
            listaAvisaleBean.setIdServicio(servicio.getIdServicio());
            
            if(MOSTRAR_TRUE.equals(servicio.getMostrar())) {
                listaAvisaleBean.setVisible(true);
            }
            listaAvisaleBean.setMostrar(servicio.getMostrar());
            
            listaAvisaleBean.setMostrarEstado(servicio.getMostrarEstado());
            listaAvisaleBean.setActivo(solveStatusServicio(servicio.getMostrarEstado()));
            
            listaAvisaleBean.setRespuesta(servicio.getCodRespServ());
            listaAvisaleBean.setTercerEstado(servicio.getTercerEstado());

            listaAvisaleBean.setDescMetodo(servicio.getDescMetodo());
            listaAvisaleBean.setMetodo(servicio.getMetodo());

            listaAvisaleBean.setMsisdn(servicio.getMsisdn());
            listaAvisaleBean.setParametro9(servicio.getParametro9());
            listaAvisaleBean.setPerfil(servicio.getPerfil());
            listaAvisaleBean.setTipoLista(servicio.getTipoLista());
            listaAvisaleBean.setUser(servicio.getUser());
            listaAvisaleBean.setFechaHora(servicio.getFechaHora());
            listaAvisaleBean.setNumerosAvisale(servicio.getNumerosAvisale());

        } catch (Exception e) {
            listaAvisaleBean.setMostrar("N");
            listaAvisaleBean.setVisible(false);
        }
        return listaAvisaleBean;
    }

    /**
     * ServicioMmsIshopImovilBean
     * 
     * @param servicio
     * @return
     */
    private ServicioMmsIshopImovilBean buildServicioMmsIshopImovil(
            RespuestaServMmsIshopImovilType servicio) {
        ServicioMmsIshopImovilBean bean = new ServicioMmsIshopImovilBean();
        try {

            bean.setIdServicio(servicio.getIdServicio());
            
            if(MOSTRAR_TRUE.equals(servicio.getMostrar())) {
                bean.setVisible(true);
            }
            bean.setMostrar(servicio.getMostrar());
            
            bean.setMostrarEstado(servicio.getMostrarEstado());
            bean.setActivo(solveStatusServicio(servicio.getMostrarEstado()));
            
            bean.setTercerEstado(servicio.getTercerEstado());

            MmsIshopImovilBean bam = new MmsIshopImovilBean();
            bam.setCodRespServ(servicio.getImovil().getCodRespServ());
            bam.setDescEstado(servicio.getImovil().getDescEstado());
            bam.setEstado(servicio.getImovil().getEstado());
            
            if(MOSTRAR_TRUE.equals(servicio.getImovil().getMostrar())) {
                bam.setVisible(true);
            }
            bam.setMostrar(servicio.getImovil().getMostrar());
            
            bam.setMostrarEstado(servicio.getImovil().getMostrarEstado());
            bam.setActivo(solveStatusServicio(servicio.getImovil().getMostrarEstado()));
            
            bam.setMsisdn(servicio.getImovil().getMsisdn());
            bean.setBam(bam);

            MmsIshopImovilBean mms = new MmsIshopImovilBean();
            mms.setCodRespServ(servicio.getMms().getCodRespServ());
            mms.setDescEstado(servicio.getMms().getDescEstado());
            mms.setEstado(servicio.getMms().getEstado());
            
            if(MOSTRAR_TRUE.equals(servicio.getMms().getMostrar())) {
                mms.setVisible(true);
            }
            mms.setMostrar(servicio.getMms().getMostrar());
            
            mms.setMostrarEstado(servicio.getMms().getMostrarEstado());
            mms.setActivo(solveStatusServicio(servicio.getMms().getMostrarEstado()));
            
            mms.setMsisdn(servicio.getMms().getMsisdn());
            bean.setMms(mms);

            MmsIshopImovilBean iMovil = new MmsIshopImovilBean();
            iMovil.setCodRespServ(servicio.getBam().getCodRespServ());
            iMovil.setDescEstado(servicio.getBam().getDescEstado());
            iMovil.setEstado(servicio.getBam().getEstado());
            if(MOSTRAR_TRUE.equals(servicio.getBam().getMostrar())) {
                iMovil.setVisible(true);
            }
            iMovil.setMostrar(servicio.getBam().getMostrar());
            
            iMovil.setMostrarEstado(servicio.getBam().getMostrarEstado());
            iMovil.setActivo(solveStatusServicio(servicio.getBam().getMostrarEstado()));

            
            iMovil.setMsisdn(servicio.getBam().getMsisdn());
            bean.setImovil(iMovil);

        } catch (Exception e) {
            // El servicio no se mostrara
            bean.setMostrar("N");
        }
        return bean;
    }

    
    /**
     * Resuelve si el servicio esta activo o inactivo, segun el campo
     * mostrarEstado de <code>servicio</code>
     * 
     * @param servicio
     * @return boolean true si esta activo, false si no u ocurre un error
     */
    private boolean solveStatusServicio(String mostarEstado) {
        
        if(HABILITADO.equals(mostarEstado)) {
            return true;
        } 
        else if(DESHABILITADO.equals(mostarEstado)) {
            return false;
        }
        else if (ERROR.equals(mostarEstado)) {
//            LOGGER.warn("Servicio tiene estado Error, indicara activo==false");
            return false;
        }
        
//        LOGGER.warn("Servicio no contiene valor 'mostrarEstado' conocido");
        return false;
    }
    
    /**
     * Resuelve si es buzon premium mms o email, o si es basico
     * @param servicioBean. Objeto a retornar
     * @param tercerEstado. <code>string</code> que identifica el tipo de buzon
     */
    private void solveTipoBuzon(ServicioBuzonBean servicioBean, RespuestaServicioType servicio) {
    	
    	String codRespServ = servicio.getCodRespServ();
    	String tercerEstado = servicio.getTercerEstado();

        if(Utils.isNotEmptyString(tercerEstado)) {
        	//basico
            if(tercerEstado.equals("1") || tercerEstado.equals("4")){
            	servicioBean.setBasico(true);
            	servicioBean.setTipoPerfil(
            			MiEntelProperties.getProperty("adminServicios.buzon.tipoPerfil.basico"));
            }
            //mms
            else if(tercerEstado.equals("9")) {
            	servicioBean.setPremium(true);
            	servicioBean.setTipoPerfil(
            			MiEntelProperties.getProperty("adminServicios.buzon.tipoPerfil.vm2mms"));
            }
            //mail
            else if(tercerEstado.equals("7")) {
            	servicioBean.setPremium(true);
            	servicioBean.setTipoPerfil(
            			MiEntelProperties.getProperty("adminServicios.buzon.tipoPerfil.vm2email"));
            }
            //Actualizacion en proceso
            else if (tercerEstado.equalsIgnoreCase("TEP") && Utils.isNotEmptyString(servicio.getParametro4())){
            	servicioBean.setTep(true);
            	servicioBean.setTipoPerfil("");
            }
        }
        else {
            LOGGER.warn("Estado de Buzon de Voz no identificado: " + tercerEstado);
        }        
        
    }
    
    /**
     * Realiza la activacion del Buzon de Voz
     * 
     * @return {@link ResultadoServicioBean}
     * @throws DAOException
     * @throws ServiceException
     */
    public ResultadoServicioBean activarBuzonVoz(
            AdminServicioBuzonBean adminServicioBuzonBean) throws DAOException,
            ServiceException {
        return serviciosBuzonDAO.activarServicio(adminServicioBuzonBean);
    }

    /**
     * Realiza la desactivacion del Buzon de Voz
     * 
     * @return {@link ResultadoServicioBean}
     * @throws DAOException
     * @throws ServiceException
     */
    public ResultadoServicioBean desactivarBuzonVoz(
            AdminServicioBuzonBean adminServicioBuzonBean) throws DAOException,
            ServiceException {
        return serviciosBuzonDAO.desactivarServicio(adminServicioBuzonBean);
    }
  
    /**
     * Realiza la activacion del servicio de numero interarea
     * 
     * @param numeroPcs
     * @return {@link ResultadoServicioBean}
     * @throws DAOException
     * @throws ServiceException
     */
    public ResultadoServiciosSSCCBean activarNumeroInterarea(String numeroPcs)
            throws DAOException, ServiceException {
        return serviciosSSCCDAO.activarServicio(numeroPcs, "iarea");
    }

    /**
     * Realiza la desactivacion del servicio de numero interarea
     * 
     * @param numeroPcs
     * @return {@link ResultadoServicioBean}
     * @throws DAOException
     * @throws ServiceException
     */
    public ResultadoServiciosSSCCBean desactivarNumeroInterarea(String numeroPcs)
            throws DAOException, ServiceException {
        return serviciosSSCCDAO.desactivarServicio(numeroPcs, "iarea");
    }

    /**
     * Realiza la activacion del servicio de roaming
     * 
     * @param numeroPcs
     * @return {@link ResultadoServicioBean}
     * @throws DAOException
     * @throws ServiceException
     */
    public ResultadoServiciosSSCCBean activarRoaming(String numeroPcs)
            throws DAOException, ServiceException {
        return serviciosSSCCDAO.activarServicio(numeroPcs, "roaming");
    }

    /**
     * Realiza la desactivacion del servicio de roaming
     * 
     * @param numeroPcs
     * @return {@link ResultadoServicioBean}
     * @throws DAOException
     * @throws ServiceException
     */
    public ResultadoServiciosSSCCBean desactivarRoaming(String numeroPcs)
            throws DAOException, ServiceException {
        return serviciosSSCCDAO.desactivarServicio(numeroPcs, "roaming");
    }

    /**
     * Realiza la activacion del servicio de larga distancia internacional
     * 
     * @param numeroPcs
     * @return {@link ResultadoServicioBean}
     * @throws DAOException
     * @throws ServiceException
     */
    public ResultadoServiciosSSCCBean activarLDI(String numeroPcs)
            throws DAOException, ServiceException {
        return serviciosSSCCDAO.activarServicio(numeroPcs, "ldi");
    }

    /**
     * Realiza la desactivacion del servicio de larga distancia internacional
     * 
     * @param numeroPcs
     * @return {@link ResultadoServicioBean}
     * @throws DAOException
     * @throws ServiceException
     */
    public ResultadoServiciosSSCCBean desactivarLDI(String numeroPcs)
            throws DAOException, ServiceException {
        return serviciosSSCCDAO.desactivarServicio(numeroPcs, "ldi");
    }

    /**
     * Realiza la activacion del servicio numero 300
     * 
     * @param numeroPcs
     * @return {@link ResultadoServicioBean}
     * @throws DAOException
     * @throws ServiceException
     */
    public ResultadoServiciosSSCCBean activarNumero300(String numeroPcs)
            throws DAOException, ServiceException {
        return serviciosSSCCDAO.activarServicio(numeroPcs, "num300");
    }

    /**
     * Realiza la desactivacion del servicio numero 300
     * 
     * @param numeroPcs
     * @return {@link ResultadoServicioBean}
     * @throws DAOException
     * @throws ServiceException
     */
    public ResultadoServiciosSSCCBean desactivarNumero300(String numeroPcs)
            throws DAOException, ServiceException {
        return serviciosSSCCDAO.desactivarServicio(numeroPcs, "num300");
    }

    /**
     * Realiza la activacion del servicio numero 700
     * 
     * @param numeroPcs
     * @return {@link ResultadoServicioBean}
     * @throws DAOException
     * @throws ServiceException
     */
    public ResultadoServiciosSSCCBean activarNumero700(String numeroPcs)
            throws DAOException, ServiceException {
        return serviciosSSCCDAO.activarServicio(numeroPcs, "num700");
    }
    
    /**
     * Realiza la activacion del servicio Internet Movil
     * 
     * @param numeroPcs
     * @return {@link ResultadoServicioBean}
     * @throws DAOException
     * @throws ServiceException
     */
    public ResultadoServiciosSSCCBean activarInternetMovil(String numeroPcs)
            throws DAOException, ServiceException {
        return serviciosSSCCDAO.activarServicioDatos(numeroPcs, "bam");
    }
    
    /**
     * Realiza la activacion del servicio Banda Ancha Movil
     * 
     * @param numeroPcs
     * @return {@link ResultadoServicioBean}
     * @throws DAOException
     * @throws ServiceException
     */
    public ResultadoServiciosSSCCBean activarBandaAnchaMovil(String numeroPcs)
            throws DAOException, ServiceException {
        return serviciosSSCCDAO.activarServicioDatos(numeroPcs, "imovil");
    }
    
    /**
     * Realiza la activacion del servicio Internet Movil / Banda Ancha Movil
     * 
     * @param numeroPcs
     * @return {@link ResultadoServicioBean}
     * @throws DAOException
     * @throws ServiceException
     */
    public ResultadoServiciosSSCCBean activarInternetMovilBAM(String numeroPcs)
            throws DAOException, ServiceException {
        return serviciosSSCCDAO.activarServicioDatos(numeroPcs, "gprs");
    }
    
    /**
     * Realiza la activacion del servicio MMS / Banda Ancha Movil
     * 
     * @param numeroPcs
     * @return {@link ResultadoServicioBean}
     * @throws DAOException
     * @throws ServiceException
     */
    public ResultadoServiciosSSCCBean activarMMSBAM(String numeroPcs)
            throws DAOException, ServiceException {
        return serviciosSSCCDAO.activarServicioDatos(numeroPcs, "mms");
    }

    /**
     * Realiza la desactivacion del servicio numero 700
     * 
     * @param numeroPcs
     * @return {@link ResultadoServicioBean}
     * @throws DAOException
     * @throws ServiceException
     */
    public ResultadoServiciosSSCCBean desactivarNumero700(String numeroPcs)
            throws DAOException, ServiceException {
        return serviciosSSCCDAO.desactivarServicio(numeroPcs, "num700");
    }
    
    /**
     * Realiza la desactivacion del servicio Internet Movil
     * 
     * @param numeroPcs
     * @return {@link ResultadoServicioBean}
     * @throws DAOException
     * @throws ServiceException
     */
    public ResultadoServiciosSSCCBean desactivarInternetMovil(String numeroPcs)
            throws DAOException, ServiceException {
        return serviciosSSCCDAO.desactivarServicioDatos(numeroPcs, "bam");
    }
    
    /**
     * Realiza la desactivacion del servicio Banda Ancha Movil
     * 
     * @param numeroPcs
     * @return {@link ResultadoServicioBean}
     * @throws DAOException
     * @throws ServiceException
     */
    public ResultadoServiciosSSCCBean desactivarBandaAnchaMovil(String numeroPcs)
            throws DAOException, ServiceException {
        return serviciosSSCCDAO.desactivarServicioDatos(numeroPcs, "imovil");
    }
    
    /**
     * Realiza la desactivacion del servicio Internet Movil / Banda Ancha Movil
     * 
     * @param numeroPcs
     * @return {@link ResultadoServicioBean}
     * @throws DAOException
     * @throws ServiceException
     */
    public ResultadoServiciosSSCCBean desactivarInternetMovilBAM(String numeroPcs)
            throws DAOException, ServiceException {
        return serviciosSSCCDAO.desactivarServicioDatos(numeroPcs, "gprs");
    }
    
    /**
     * Realiza la desactivacion del servicio MMS / Banda Ancha Movil
     * 
     * @param numeroPcs
     * @return {@link ResultadoServicioBean}
     * @throws DAOException
     * @throws ServiceException
     */
    public ResultadoServiciosSSCCBean desactivarMMSBAM(String numeroPcs)
            throws DAOException, ServiceException {
        return serviciosSSCCDAO.desactivarServicioDatos(numeroPcs, "mms");
    }

    /**
     * Realiza la activacion del servicio numero 606
     * 
     * @param numeroPcs
     * @return {@link ResultadoServicioBean}
     * @throws DAOException
     * @throws ServiceException
     */
    public ResultadoServiciosSSCCBean activarNumero606(String numeroPcs)
            throws DAOException, ServiceException {
        return serviciosSSCCDAO.activarServicio(numeroPcs, "num606");
    }

    /**
     * Realiza la desactivacion del servicio numero 606
     * 
     * @param numeroPcs
     * @return {@link ResultadoServicioBean}
     * @throws DAOException
     * @throws ServiceException
     */
    public ResultadoServiciosSSCCBean desactivarNumero606(String numeroPcs)
            throws DAOException, ServiceException {
        return serviciosSSCCDAO.desactivarServicio(numeroPcs, "num606");
    }

    /**
     * Realiza la activacion del servicio numero 300
     * 
     * @param numeroPcs
     * @return {@link ResultadoServicioBean}
     * @throws DAOException
     * @throws ServiceException
     */
    public ResultadoServiciosSSCCBean activarNumero609(String numeroPcs)
            throws DAOException, ServiceException {
        return serviciosSSCCDAO.activarServicio(numeroPcs, "num609");
    }

    /**
     * Realiza la desactivacion del servicio numero 609
     * 
     * @param numeroPcs
     * @return {@link ResultadoServicioBean}
     * @throws DAOException
     * @throws ServiceException
     */
    public ResultadoServiciosSSCCBean desactivarNumero609(String numeroPcs)
            throws DAOException, ServiceException {
        return serviciosSSCCDAO.desactivarServicio(numeroPcs, "num609");
    }

    /**
     * Realiza la activacion del servicio WAP
     * 
     * @param numeroPcs
     * @return {@link ResultadoServicioBean}
     * @throws DAOException
     * @throws ServiceException
     */
    public ResultadoServiciosSSCCBean activarWap(String numeroPcs)
            throws DAOException, ServiceException {
        return serviciosSSCCDAO.activarServicio(numeroPcs, "wap");
    }

    /**
     * Realiza la desactivacion del servicio WAP
     * 
     * @param numeroPcs
     * @return {@link ResultadoServicioBean}
     * @throws DAOException
     * @throws ServiceException
     */
    public ResultadoServiciosSSCCBean desactivarWap(String numeroPcs)
            throws DAOException, ServiceException {
        return serviciosSSCCDAO.desactivarServicio(numeroPcs, "wap");
    }

    /**
     * Realiza la solicitud de activacion para el servicio de Notificacion SMS
     * 
     * @param numeroPcs
     *            String numero para quien se pide la solicitud
     * @return {@link ResultadoServicioBean} si le solicitud es exitosa, con el
     *         correlativo de solicitud.
     * @throws DAOException
     * @throws ServiceException
     */
    public ResultadoServicioBean activarNotificacionSMS(String numeroPcs)
            throws DAOException, ServiceException {
        return serviciosIVRDAO.activarServicioIVR(numeroPcs, "sms");
    }

    /**
     * Realiza la solicitud de desactivacion para el servicio de Notificacion
     * SMS
     * 
     * @param numeroPcs
     *            String numero para quien se pide la solicitud
     * @return {@link ResultadoServicioBean} si le solicitud es exitosa, con el
     *         correlativo de solicitud.
     * @throws DAOException
     * @throws ServiceException
     */
    public ResultadoServicioBean desactivarNotificacionSMS(String numeroPcs)
            throws DAOException, ServiceException {
        return serviciosIVRDAO.desactivarServicioIVR(numeroPcs, "sms");
    }

    /**
     * Realiza la solicitud de activacion para el servicio de Buzon de Voz PP
     * 
     * @param numeroPcs
     *            String numero para quien se pide la solicitud
     * @return {@link ResultadoServicioBean} si le solicitud es exitosa, con el
     *         correlativo de solicitud.
     * @throws DAOException
     * @throws ServiceException
     */
    public ResultadoServicioBean activarBuzonPP(String numeroPcs)
            throws DAOException, ServiceException {
        return serviciosIVRDAO.activarServicioIVRPP(numeroPcs, "buzonPP");
    }

    /**
     * Realiza la solicitud de desactivacion para el servicio de Buzon de voz PP
     * 
     * @param numeroPcs
     *            String numero para quien se pide la solicitud
     * @return {@link ResultadoServicioBean} si le solicitud es exitosa, con el
     *         correlativo de solicitud.
     * @throws DAOException
     * @throws ServiceException
     */
    public ResultadoServicioBean desactivarBuzonPP(String numeroPcs)
            throws DAOException, ServiceException {
        return serviciosIVRDAO.desactivarServicioIVRPP(numeroPcs, "buzonPP");
    }

    /**
     * Realiza la solicitud de activacion para el servicio de Notificacion SMS
     * 
     * @param numeroPcs
     *            String numero para quien se pide la solicitud
     * @return {@link ResultadoServicioBean} si le solicitud es exitosa, con el
     *         correlativo de solicitud.
     * @throws DAOException
     * @throws ServiceException
     */
    public ResultadoServicioBean activarNotificacionSMSPP(String numeroPcs)
            throws DAOException, ServiceException {
        return serviciosIVRDAO.activarServicioIVRPP(numeroPcs, "nip");
    }

    /**
     * Realiza la solicitud de desactivacion para el servicio de Notificacion
     * SMS
     * 
     * @param numeroPcs
     *            String numero para quien se pide la solicitud
     * @return {@link ResultadoServicioBean} si le solicitud es exitosa, con el
     *         correlativo de solicitud.
     * @throws DAOException
     * @throws ServiceException
     */
    public ResultadoServicioBean desactivarNotificacionSMSPP(String numeroPcs)
            throws DAOException, ServiceException {
        return serviciosIVRDAO.desactivarServicioIVRPP(numeroPcs, "nip");
    }

    
    
    /**
     * Realiza solicitud de activacion de servicio Avisale
     * 
     * @param numeroPcs
     *            String numero para quien se pide activar
     * @param tipoLista
     *            String indica si es lista negra o blanca
     * @param listaNumeros
     *            {@link List} con los numeros a agregar a a lista.
     * @return {@link ResultadoServicioBean}
     * @throws DAOException
     * @throws ServiceException
     */
    public ResultadoServicioBean activarAvisale(String numeroPcs,
            String tipoLista, List<String> listaNumeros) throws DAOException,
            ServiceException {
        return serviciosAvisaleDAO.activarServicio(numeroPcs, tipoLista,
                listaNumeros);
    }

    /**
     * Realiza solicitud de desactivacion de servicio Avisale
     * 
     * @param numeroPcs
     *            String numero para quien se pide activar
     * @return {@link ResultadoServicioBean}
     * @throws DAOException
     * @throws ServiceException
     */
    public ResultadoServicioBean desactivarAvisale(String numeroPcs)
            throws DAOException, ServiceException {
        return serviciosAvisaleDAO.desactivarServicio(numeroPcs);
    }

    /**
     * Actualiza lista de numeros del servicio Avisale
     * 
     * @param numeroPcs
     *            String numero para quien se pide activar
     * @param tipoLista
     *            String indica si es lista negra o blanca
     * @param listaNumeros
     *            {@link List} con los numeros a agregar a a lista.
     * @return {@link ResultadoServicioBean}
     * @throws DAOException
     * @throws ServiceException
     */
    public ResultadoServicioBean actualizarAvisale(String numeroPcs,
            String tipoLista, List<String> listaNumeros) throws DAOException,
            ServiceException {
        return serviciosAvisaleDAO.actualizarServicio(numeroPcs, tipoLista,
                listaNumeros);
    }

    /**
     * @param numeroPcs
     * @return
     * @throws DAOException
     * @throws ServiceException
     * @see com.epcs.cliente.perfil.dao.servicios.ServiciosPPDAO#activarMMSGPRS(java.lang.String)
     */
    public ResultadoServicioBean activarMMSGPRS(String numeroPcs)
            throws DAOException, ServiceException {
        return serviciosPPDAO.activarMMSGPRS(numeroPcs);
    }

    /**
     * @param numeroPcs
     * @return
     * @throws DAOException
     * @throws ServiceException
     * @see com.epcs.cliente.perfil.dao.servicios.ServiciosPPDAO#desactivarMMSGPRS(java.lang.String)
     */
    public ResultadoServicioBean desactivarMMSGPRS(String numeroPcs)
            throws DAOException, ServiceException {
        return serviciosPPDAO.desactivarMMSGPRS(numeroPcs);
    }

    /**
     * @param numeroPcs
     * @return
     * @throws DAOException
     * @throws ServiceException
     * @see com.epcs.cliente.perfil.dao.servicios.ServiciosPPDAO#activarWap(java.lang.String)
     */
    public ResultadoServicioBean activarWapPP(String numeroPcs)
            throws DAOException, ServiceException {
        return serviciosPPDAO.activarWap(numeroPcs);
    }

    /**
     * @param numeroPcs
     * @return
     * @throws DAOException
     * @throws ServiceException
     * @see com.epcs.cliente.perfil.dao.servicios.ServiciosPPDAO#desactivarWap(java.lang.String)
     */
    public ResultadoServicioBean desactivarWapPP(String numeroPcs)
            throws DAOException, ServiceException {
        return serviciosPPDAO.desactivarWap(numeroPcs);
    }
    
    
    /**
     * @param numeroPcs
     * @return
     * @throws DAOException
     * @throws ServiceException
     * @see com.epcs.cliente.perfil.dao.servicios.ServiciosPPDAO#activarWap(java.lang.String)
     */
    public ResultadoServicioBean activarFacturaElectronica(String numeroPcs, String diasAviso)
            throws DAOException, ServiceException {
        return serviciosFacturaEDAO.activarServicio(numeroPcs, diasAviso);
    }

    /**
     * @param numeroPcs
     * @return
     * @throws DAOException
     * @throws ServiceException
     * @see com.epcs.cliente.perfil.dao.servicios.ServiciosPPDAO#desactivarWap(java.lang.String)
     */
    public ResultadoServicioBean desactivarFacturaElectronica(String numeroPcs)
            throws DAOException, ServiceException {
        return serviciosFacturaEDAO.desactivarServicio(numeroPcs);
    }

    /**
     * @param numeroPcs
     * @return
     * @throws DAOException
     * @throws ServiceException
     * @see com.epcs.cliente.perfil.dao.servicios.ServiciosPPDAO#desactivarWap(java.lang.String)
     */
    public ResultadoServicioBean modificarFacturaElectronica(String numeroPcs, String diasAviso)
            throws DAOException, ServiceException {
        return serviciosFacturaEDAO.modificarServicio(numeroPcs, diasAviso);
    }
    
    
    /**
     * @param numeroPcs
     * @return
     * @throws DAOException
     * @throws ServiceException
     * @see com.epcs.cliente.perfil.dao.servicios.ServiciosPPDAO#activarWap(java.lang.String)
     */
    public ResultadoServicioBean agregarNumeroCobroRevertido(String numeroPcs, String numeroAgregar)
            throws DAOException, ServiceException {
        return serviciosCobroRevertidoDAO.agregarNumero(numeroPcs, numeroAgregar);
    }
    
    /**
     * @param numeroPcs
     * @return
     * @throws DAOException
     * @throws ServiceException
     * @see com.epcs.cliente.perfil.dao.servicios.ServiciosPPDAO#desactivarWap(java.lang.String)
     */
    public ResultadoServicioBean eliminarNumeroCobroRevertido(String numeroPcs, String numeroEliminar)
            throws DAOException, ServiceException {
        return serviciosCobroRevertidoDAO.eliminarNumero(numeroPcs,numeroEliminar);
    }

    /**
     * @param numeroPcs
     * @return
     * @throws DAOException
     * @throws ServiceException
     * @see com.epcs.cliente.perfil.dao.servicios.ServiciosPPDAO#desactivarWap(java.lang.String)
     */
    public ResultadoServicioBean validarNumeroCobroRevertido(String numeroPcs, String numeroAgregar)
            throws DAOException, ServiceException {
        return serviciosCobroRevertidoDAO.validarNumero(numeroPcs, numeroAgregar);
    }

    /**
     * 
     * @param numeroPcs
     * @param rutUsuario
     * @return
     * @throws ServiceException
     * @throws DAOException
     * @see ServiciosBuzonDAO#obtenerCorreoBuzonPremium(String, RutBean)
     */
    public String obtenerEmailBuzonPremium(String numeroPcs, RutBean rutUsuario)
            throws DAOException, ServiceException {
        return serviciosBuzonDAO.obtenerCorreoBuzonPremium(numeroPcs,
                rutUsuario);
    }


    /**
     * @param numeroPcs
     * @return
     * @throws DAOException
     * @throws ServiceException
     * @see com.epcs.cliente.perfil.dao.servicios.ServiciosPPDAO#desactivarWap(java.lang.String)
     */
    public ResultadoServicioBean activarRecepcionCobroRevertido(String numeroPcs)
            throws DAOException, ServiceException {
        return serviciosCobroRevertidoDAO.activarRecepcionCobroRevertido(numeroPcs);
    }
    
    /**
     * @param numeroPcs
     * @return
     * @throws DAOException
     * @throws ServiceException
     * @see com.epcs.cliente.perfil.dao.servicios.ServiciosPPDAO#desactivarWap(java.lang.String)
     */
    public ResultadoServicioBean desactivarRecepcionCobroRevertido(String numeroPcs)
            throws DAOException, ServiceException {
        return serviciosCobroRevertidoDAO.desactivarRecepcionCobroRevertido(numeroPcs);
    }
    
    //Servicio SAGEN  
    
    public ResultadoConsultarSAGENBean consultarSuscripcionSAGEN(String msisdn)    
    		throws DAOException, ServiceException{
    	
    	
		LOGGER.info("Instanciando el Port.");
		BuscarSuscripcionServicePortType port = null;
		
		try {        
			port = (BuscarSuscripcionServicePortType) WebServiceLocator.getInstance().getPort(
					BuscarSuscripcionService.class, BuscarSuscripcionServicePortType.class);   
			
		} catch (WebServiceLocatorException e) {
		    LOGGER.error("Error al inicializar el Port "
		            + BuscarSuscripcionService.class);
		    LOGGER.error( new DAOException(e));
		}
		
		SearchSuscriptionType request = new SearchSuscriptionType();
		ResultadoSearchSuscriptionType response;
		
		List<com.epcs.provision.suscripcion.wssagenservice.buscarSuscripcionService.SuscriptionType> resultadoConsulta = null;
    	LOGGER.info("Instanciando el Port.");


        try {
            LOGGER.info("Configurando parametros de la peticion");
            request.setMsisdn(msisdn);

            LOGGER.info("Invocando servicio");
            response = port.searchSuscription(request);
            
            resultadoConsulta = response.getSuscription();

        } catch (Exception e) {
            LOGGER.error("Exception caught on Service invocation", e);
        }

        
        ResultadoConsultarSAGENBean resultadoConsultaSAGEN = null;
		for (com.epcs.provision.suscripcion.wssagenservice.buscarSuscripcionService.SuscriptionType resultadoSAGEN: resultadoConsulta){
            
        	if ("MZZO".equals(resultadoSAGEN.getVasId())){
        		if (resultadoConsultaSAGEN==null){
        			resultadoConsultaSAGEN = new ResultadoConsultarSAGENBean();
        		}  		
      		
        		resultadoConsultaSAGEN.setVasID(resultadoSAGEN.getVasId());
        		resultadoConsultaSAGEN.setServiceID(resultadoSAGEN.getServiceID());
        		resultadoConsultaSAGEN.setSuscriptionID(resultadoSAGEN.getSuscriptionID());
        		resultadoConsultaSAGEN.setDescriptionID(resultadoSAGEN.getDescription());
        		 
        		CustomSAGENBean customSAGENBean = new CustomSAGENBean();
		
			
				List<CategoriaSAGENBean> categoriasSAGENBean = new ArrayList<CategoriaSAGENBean>();
				
		        if (resultadoSAGEN.getCustom()!=null){
		        	
		         if (resultadoSAGEN.getCustom().getMktCategory()!=null){
					for (com.epcs.provision.suscripcion.wssagenservice.buscarSuscripcionService.MktCategoryType categoria: resultadoSAGEN.getCustom().getMktCategory()){
						CategoriaSAGENBean categoriaSAGENBean = new CategoriaSAGENBean();
						
						categoriaSAGENBean.setIdCategoria(categoria.getId());
						categoriaSAGENBean.setNombreCategoria(categoria.getName());					
						categoriaSAGENBean.setEstado(0);
						
						if (categoria.getMktCampaigns()!=null){
							
							List<CampanaSAGENBean> campanas = new ArrayList<CampanaSAGENBean>();
							for (com.epcs.provision.suscripcion.wssagenservice.buscarSuscripcionService.MktCampaignsType campana: categoria.getMktCampaigns()){
								CampanaSAGENBean campanaSAGENBean = new CampanaSAGENBean();
								campanaSAGENBean.setNombre(campana.getName());
								campanaSAGENBean.setDescripcion(campana.getDescription());
								campanaSAGENBean.setFechaInicio(campana.getStartdate());
								campanaSAGENBean.setFechaFin(campana.getEnddate());
								
								campanas.add(campanaSAGENBean);
							}
							categoriaSAGENBean.setCampanas(campanas);
						}		
						
						categoriasSAGENBean.add(categoriaSAGENBean);
					}
				}
		         
		         if(resultadoSAGEN.getCustom()!=null){  
		        	 customSAGENBean.setCategorias(categoriasSAGENBean); 
		        	 customSAGENBean.setDiasSusc(resultadoSAGEN.getCustom().getSuscriptionDays());
		        	 customSAGENBean.setHoraInicial(resultadoSAGEN.getCustom().getSuscriptionFromTime());
		        	 customSAGENBean.setHoraFinal(resultadoSAGEN.getCustom().getSuscriptionToTime());
		        	 if(resultadoSAGEN.getCustom().getMktCustomer()!=null){
		        		 UsuarioSAGENBean usuarioSAGENBean = new UsuarioSAGENBean();
		        		 usuarioSAGENBean.setNombre(resultadoSAGEN.getCustom().getMktCustomer().getName());
		        		 usuarioSAGENBean.setEmail(resultadoSAGEN.getCustom().getMktCustomer().getMktEmail());
		        		 usuarioSAGENBean.setFechaNacimiento(resultadoSAGEN.getCustom().getMktCustomer().getMktFechaNacimiento());
		        		 usuarioSAGENBean.setSexo(resultadoSAGEN.getCustom().getMktCustomer().getMktPersona());

		        		 DireccionBean direccionSAGENBean = new DireccionBean();
		        		 direccionSAGENBean.setRegion(new RegionBean(null, resultadoSAGEN.getCustom().getMktCustomer().getMktDireccion().getMktRegion()));
		        		 direccionSAGENBean.setCiudad(new CiudadBean(null, resultadoSAGEN.getCustom().getMktCustomer().getMktDireccion().getMktCiudad()));
		        		 direccionSAGENBean.setComuna(new ComunaBean(null, resultadoSAGEN.getCustom().getMktCustomer().getMktDireccion().getMktComuna()));
		        		 direccionSAGENBean.setCalle(resultadoSAGEN.getCustom().getMktCustomer().getMktDireccion().getMktCalle());
		        		 direccionSAGENBean.setNumero(resultadoSAGEN.getCustom().getMktCustomer().getMktDireccion().getMktNumero());
		        		 direccionSAGENBean.setDepartamento(resultadoSAGEN.getCustom().getMktCustomer().getMktDireccion().getMktDpto());

		        		 usuarioSAGENBean.setDireccion(direccionSAGENBean);	
		        		 customSAGENBean.setUsuario(usuarioSAGENBean);
		        	 }
		         }
				
				resultadoConsultaSAGEN.setCustom(customSAGENBean);
		        }
            }       	
        }      	
    	return resultadoConsultaSAGEN;
    }
    
    
    /**
     * @param msisdn
     * @return
     * @throws DAOException
     * @throws ServiceException
     */
    public List<ResultadoConsultarSAGENBean> consultarSuscripcionServicios(String msisdn)    
	throws DAOException, ServiceException{

		LOGGER.info("Instanciando el Port.");
		BuscarSuscripcionServicePortType port = null;
		
		try {        
			port = (BuscarSuscripcionServicePortType) WebServiceLocator.getInstance().getPort(
					BuscarSuscripcionService.class, BuscarSuscripcionServicePortType.class);   
			
		} catch (WebServiceLocatorException e) {
		    LOGGER.error("Error al inicializar el Port "
		            + BuscarSuscripcionService.class);
		    LOGGER.error( new DAOException(e));
		}
		
		SearchSuscriptionType request = new SearchSuscriptionType();
		ResultadoSearchSuscriptionType response;
		
		List<com.epcs.provision.suscripcion.wssagenservice.buscarSuscripcionService.SuscriptionType> resultadoConsulta = null;
		
		try {
		    LOGGER.info("Configurando parametros de la peticion");
		    request.setMsisdn(msisdn);
		
		    LOGGER.info("Invocando servicio");
		    response = port.searchSuscription(request);
		    resultadoConsulta = response.getSuscription();
		
		} catch (Exception e) {
		    LOGGER.error("Exception caught on Service invocation", e);
		}
		
		ResultadoConsultarSAGENBean resultadoConsultaSAGEN = null;
		List<ResultadoConsultarSAGENBean> listadoservices= new ArrayList<ResultadoConsultarSAGENBean>();
		//System.out.println("responsewww"+resultadoConsulta.size());
		for (com.epcs.provision.suscripcion.wssagenservice.buscarSuscripcionService.SuscriptionType resultadoSAGEN: resultadoConsulta){
		    
			//if ("MZZO".equals(resultadoSAGEN.getVasID())){
				//if (resultadoConsultaSAGEN==null){
					resultadoConsultaSAGEN = new ResultadoConsultarSAGENBean();
				//}  		
				
				resultadoConsultaSAGEN.setVasID(resultadoSAGEN.getVasId());
				resultadoConsultaSAGEN.setProviderID(resultadoSAGEN.getProviderID());
				resultadoConsultaSAGEN.setServiceID(resultadoSAGEN.getServiceID());
				resultadoConsultaSAGEN.setSuscriptionID(resultadoSAGEN.getSuscriptionID());
				resultadoConsultaSAGEN.setDescriptionID(resultadoSAGEN.getDescription());
				resultadoConsultaSAGEN.setLa(resultadoSAGEN.getLa());
				

				
				CustomSAGENBean customSAGENBean = new CustomSAGENBean();
				List<CategoriaSAGENBean> categoriasSAGENBean = new ArrayList<CategoriaSAGENBean>();

		        if (resultadoSAGEN.getCustom()!=null){
		        	
		        	if (resultadoSAGEN.getCustom().getMktCategory()!=null){
					
		        		for (com.epcs.provision.suscripcion.wssagenservice.buscarSuscripcionService.MktCategoryType categoria: resultadoSAGEN.getCustom().getMktCategory()){
		        			CategoriaSAGENBean categoriaSAGENBean = new CategoriaSAGENBean();

		        			categoriaSAGENBean.setIdCategoria(categoria.getId());
		        			categoriaSAGENBean.setNombreCategoria(categoria.getName());		
		        			categoriaSAGENBean.setDescripcion(categoria.getDescription());
		        			//categoriaSAGENBean.setEstado(0);

		        			if (categoria.getMktCampaigns()!=null){

		        				List<CampanaSAGENBean> campanas = new ArrayList<CampanaSAGENBean>();
		        				for (com.epcs.provision.suscripcion.wssagenservice.buscarSuscripcionService.MktCampaignsType campana: categoria.getMktCampaigns()){
		        					CampanaSAGENBean campanaSAGENBean = new CampanaSAGENBean();
		        					campanaSAGENBean.setNombre(campana.getName());
		        					campanaSAGENBean.setDescripcion(campana.getDescription());
		        					campanaSAGENBean.setFechaInicio(campana.getStartdate());
		        					campanaSAGENBean.setFechaFin(campana.getEnddate());

		        					campanas.add(campanaSAGENBean);
		        				}
		        				categoriaSAGENBean.setCampanas(campanas);
		        			}		

		        			categoriasSAGENBean.add(categoriaSAGENBean);
		        		}
		        	}
		        
		        	if(resultadoSAGEN.getCustom()!=null){ 
		        		customSAGENBean.setCategorias(categoriasSAGENBean); 
		        		customSAGENBean.setDiasSusc(resultadoSAGEN.getCustom().getSuscriptionDays());
		        		customSAGENBean.setHoraInicial(resultadoSAGEN.getCustom().getSuscriptionFromTime());
		        		customSAGENBean.setHoraFinal(resultadoSAGEN.getCustom().getSuscriptionToTime());

		        		
		        		if(resultadoSAGEN.getCustom().getMktCustomer()!=null){
		        			UsuarioSAGENBean usuarioSAGENBean = new UsuarioSAGENBean();
		        			usuarioSAGENBean.setNombre(resultadoSAGEN.getCustom().getMktCustomer().getName());
		        			usuarioSAGENBean.setEmail(resultadoSAGEN.getCustom().getMktCustomer().getMktEmail());
		        			usuarioSAGENBean.setFechaNacimiento(resultadoSAGEN.getCustom().getMktCustomer().getMktFechaNacimiento());
		        			usuarioSAGENBean.setSexo(resultadoSAGEN.getCustom().getMktCustomer().getMktPersona());

		        			DireccionBean direccionSAGENBean = new DireccionBean();
		        			direccionSAGENBean.setRegion(new RegionBean(null, resultadoSAGEN.getCustom().getMktCustomer().getMktDireccion().getMktRegion()));
		        			direccionSAGENBean.setCiudad(new CiudadBean(null, resultadoSAGEN.getCustom().getMktCustomer().getMktDireccion().getMktCiudad()));
		        			direccionSAGENBean.setComuna(new ComunaBean(null, resultadoSAGEN.getCustom().getMktCustomer().getMktDireccion().getMktComuna()));
		        			direccionSAGENBean.setCalle(resultadoSAGEN.getCustom().getMktCustomer().getMktDireccion().getMktCalle());
		        			direccionSAGENBean.setNumero(resultadoSAGEN.getCustom().getMktCustomer().getMktDireccion().getMktNumero());
		        			direccionSAGENBean.setDepartamento(resultadoSAGEN.getCustom().getMktCustomer().getMktDireccion().getMktDpto());

		        			usuarioSAGENBean.setDireccion(direccionSAGENBean);	
		        			customSAGENBean.setUsuario(usuarioSAGENBean);
		        		}
		        	}
				if (resultadoSAGEN.getCustom().getPackContents()!=null){
					if (resultadoSAGEN.getCustom().getPackContents().getContent()!=null){
						List<PackContent> pkcontent = new ArrayList<PackContent>();
						for (com.epcs.provision.suscripcion.wssagenservice.buscarSuscripcionService.ContentType packcontent: resultadoSAGEN.getCustom().getPackContents().getContent()){
							PackContent packcontenido = new PackContent();
							packcontenido.setId(packcontent.getId());
							packcontenido.setActivationdate(packcontent.getActivationDate());
							packcontenido.setStatus(packcontent.getStatus());
							pkcontent.add(packcontenido);
						}

						customSAGENBean.setPackcontents(pkcontent);
					}
				}
				if (resultadoSAGEN.getCustom().getBundleContents()!=null) {
					if (resultadoSAGEN.getCustom().getBundleContents().getObligatoryContents()!=null) {	
						if (resultadoSAGEN.getCustom().getBundleContents().getObligatoryContents().getContentBundle()!=null){
							List<BundleContent> obligatorycontent = new ArrayList<BundleContent>();
							for (com.epcs.provision.suscripcion.wssagenservice.buscarSuscripcionService.ContentBundleType obligcontent: resultadoSAGEN.getCustom().getBundleContents().getObligatoryContents().getContentBundle()){
								BundleContent bundcontenido = new BundleContent();
								bundcontenido.setId(obligcontent.getId());
								bundcontenido.setName(obligcontent.getName());
								bundcontenido.setDescription(obligcontent.getDescription());
								obligatorycontent.add(bundcontenido);
							}

							customSAGENBean.setObligatorycontents(obligatorycontent);
						}
					}

					if (resultadoSAGEN.getCustom().getBundleContents().getOptionalContents()!=null) {	
						if (resultadoSAGEN.getCustom().getBundleContents().getOptionalContents().getContentBundle()!=null){
							List<BundleContent> optionalcontent = new ArrayList<BundleContent>();
							for (com.epcs.provision.suscripcion.wssagenservice.buscarSuscripcionService.ContentBundleType optcontent: resultadoSAGEN.getCustom().getBundleContents().getOptionalContents().getContentBundle()){
								BundleContent bundcontenido = new BundleContent();
								bundcontenido.setId(optcontent.getId());
								bundcontenido.setName(optcontent.getName());
								bundcontenido.setDescription(optcontent.getDescription());
								optionalcontent.add(bundcontenido);
							}

							customSAGENBean.setOptionalcontents(optionalcontent);
						}
					}
				}
				
				
				resultadoConsultaSAGEN.setCustom(customSAGENBean); 
		        }

				
				
				listadoservices.add(resultadoConsultaSAGEN);
		    //}       	
		}      	
		return listadoservices;
		}
    
    /**
     * @return
     * @throws DAOException
     * @throws ServiceException
     */
    public CatalogoSAGENBean obtenerCatalogoSAGEN() 
    		throws DAOException, ServiceException{
    	
    	LOGGER.info("Instanciando el Port.");
    	ObtenerCatalogoServicePortType port = null;

    	 
        try {        
        	port = (ObtenerCatalogoServicePortType) WebServiceLocator.getInstance().getPort(
        			ObtenerCatalogoService.class, ObtenerCatalogoServicePortType.class);   
        	
        } catch (WebServiceLocatorException e) {
            LOGGER.error("Error al inicializar el Port "
                    + ObtenerCatalogoService.class);
            LOGGER.error( new DAOException(e));
        }
        
        CatalogSuscriptionsType request = new CatalogSuscriptionsType();
        ResultadoCatalogSuscriptionsType response = null;
               
        try {
            LOGGER.info("Configurando parametros de la peticion");
            request.setCanal("WEB");
            LOGGER.info("Invocando servicio");
            response = port.catalogSuscriptions(request);

        } catch (Exception e) {
            LOGGER.error("Exception caught on Service invocation", e);
        }
        
        //List<CategoyItem> catalogoSAGEN = new ArrayList<CategoyItem>();
        List<SuscriptionType> suscripcionSAGEN = new ArrayList<SuscriptionType>();
        List<SubcategoryType> subcategorySAGEN = new ArrayList<SubcategoryType>();

        CatalogoSAGENBean catalogoSAGENBean = new CatalogoSAGENBean();
        for (CategoryType resultCategoria: response.getCategory()){  	
        	//se comentaria debido al nuevo cambio de servicios VAS
        	//if ("MMKT".equals(resultCategoria.getId())){ 
        		subcategorySAGEN = resultCategoria.getSubcategory();
        		if (subcategorySAGEN!=null){
        			for (SubcategoryType resultSubcategoria: subcategorySAGEN){
        				suscripcionSAGEN=resultSubcategoria.getSuscription();
        				if(suscripcionSAGEN!=null){
        					for (SuscriptionType resultSuscripcion: suscripcionSAGEN){
        						if ("MZZO".equals(resultSuscripcion.getVasId())){
        							catalogoSAGENBean.setVasID(resultSuscripcion.getVasId());
        							catalogoSAGENBean.setProviderID(resultSuscripcion.getProviderID());
        							catalogoSAGENBean.setSuscriptionID(resultSuscripcion.getSuscriptionID());
        							catalogoSAGENBean.setDescription(resultSuscripcion.getDescription());
        							List<CategoriaSAGENBean> categoriasSAGENBean = new ArrayList<CategoriaSAGENBean>();
        							if (resultSuscripcion.getCustom()!=null){
        								if (resultSuscripcion.getCustom().getMktCategory()!=null){
        									for (MktCategoryType categoria:  resultSuscripcion.getCustom().getMktCategory()){
        										CategoriaSAGENBean categoriaSAGENBean = new CategoriaSAGENBean();

        										categoriaSAGENBean.setIdCategoria(categoria.getId());
        										categoriaSAGENBean.setNombreCategoria(categoria.getName());

        										categoriasSAGENBean.add(categoriaSAGENBean);
        									}
        								}
        								catalogoSAGENBean.setCategoria(categoriasSAGENBean);   				       				
        							} 
        						}
        					//}
        				}
        			}	
        		}
        	}
        }	
        
    	return catalogoSAGENBean;
    }
    
    
    /**
     * @return
     * @throws DAOException
     * @throws ServiceException
     */
    public CatalogoServiciosBean obtenerCatalogoServicios() 
    throws DAOException, ServiceException{

    	LOGGER.info("Instanciando el Port.");

    	ObtenerCatalogoServicePortType port2 = null;
    	
    	
    	

    	try {        
    		port2 = (ObtenerCatalogoServicePortType) WebServiceLocator.getInstance().getPort(
    				ObtenerCatalogoService.class, ObtenerCatalogoServicePortType.class);   

    	} catch (WebServiceLocatorException e) {
    		LOGGER.error("Error al inicializar el Port "
    				+ ObtenerCatalogoServicePortType.class);
    		LOGGER.error( new DAOException(e));
    	}

    	CatalogSuscriptionsType request = new CatalogSuscriptionsType();
    	ResultadoCatalogSuscriptionsType response = null;
        RespuestaType respuesta=null;
    	try {
    		LOGGER.info("Configurando parametros de la peticion");
            request.setCanal(CANAL);

    		LOGGER.info("Invocando servicio");
    		response = port2.catalogSuscriptions(request);
    		respuesta=response.getRespuesta();

    	} catch (Exception e) {
    		LOGGER.error("Exception caught on Service invocation", e);
    	} 
    	
   
    	CatalogoServiciosBean catalogo= new CatalogoServiciosBean();
    	List<CategorySagenBean> catalogoSAGEN = new ArrayList<CategorySagenBean>();
    	List<SuscriptionType> suscripcionSAGEN = new ArrayList<SuscriptionType>();
    	List<SubcategoryType> subcategorySAGEN = new ArrayList<SubcategoryType>();
    	String idcategoria="";
    	String subcategoria="";
    	String nomcategoria="";
    	String nomsubcategoria="";
    	List<ServicioSagenBean> listaservicios =new ArrayList<ServicioSagenBean>();
    	CatalogoSAGENBean catalogoSAGENBean = new CatalogoSAGENBean();
    	if (response.getCategory()!=null){
    		for (CategoryType resultCategoria: response.getCategory()){  	
    			CategorySagenBean category= new CategorySagenBean();
    			idcategoria=Utils.isEmptyString(resultCategoria.getId())?"":resultCategoria.getId();
    			nomcategoria=Utils.isEmptyString(resultCategoria.getName())?"":resultCategoria.getName();
    			category.setIdcategoria(idcategoria);
    			category.setNomcategoria(nomcategoria);
    			List<SubcategorySagenBean> listSubcategory= new ArrayList<SubcategorySagenBean>(); 
    			subcategorySAGEN = resultCategoria.getSubcategory();
    			if (subcategorySAGEN!=null){
    				for (SubcategoryType resultSubcategoria: subcategorySAGEN){
    					subcategoria=Utils.isEmptyString(resultSubcategoria.getId())?"":resultSubcategoria.getId();
    					nomsubcategoria=Utils.isEmptyString(resultSubcategoria.getName())?"":resultSubcategoria.getName();
    					SubcategorySagenBean subcategory= new SubcategorySagenBean();
    					suscripcionSAGEN =resultSubcategoria.getSuscription();	
    					subcategory.setIdsubcategoria(subcategoria);
    					subcategory.setNomsubcategoria(nomsubcategoria);
    					if (suscripcionSAGEN!=null){
    						for (SuscriptionType resultSuscripcion: suscripcionSAGEN){
    							ServicioSagenBean servicio= new ServicioSagenBean();
    							servicio.setServiceId(Utils.isEmptyString(resultSuscripcion.getServiceID())?"":resultSuscripcion.getServiceID());
    							servicio.setCategoria(idcategoria);
    							servicio.setDescripcion(Utils.isEmptyString(resultSuscripcion.getDescription())?"":resultSuscripcion.getDescription());
    							servicio.setSubcategoria(subcategoria);
    							servicio.setNomcategoria(nomcategoria);
    							servicio.setVasId(Utils.isEmptyString(resultSuscripcion.getVasId())?"":resultSuscripcion.getVasId());
    							servicio.setProviderId(Utils.isEmptyString(resultSuscripcion.getProviderID())?"":resultSuscripcion.getProviderID());
    							servicio.setSuscriptionId(Utils.isEmptyString(resultSuscripcion.getSuscriptionID())?"":resultSuscripcion.getSuscriptionID());
    							servicio.setCommercial(Utils.isEmptyString(resultSuscripcion.getCommercial())?"":resultSuscripcion.getCommercial());
    							servicio.setNomsubcategoria(nomsubcategoria);
    							servicio.setValor(Utils.isEmptyString(resultSuscripcion.getValue())?"":resultSuscripcion.getValue());
    						    servicio.setLa(Utils.isEmptyString(resultSuscripcion.getLa())?"":resultSuscripcion.getLa());
    							servicio.setCanal(Utils.isEmptyString(resultSuscripcion.getCanal())?"":resultSuscripcion.getCanal());
    							servicio.setRecurrence(Utils.isEmptyString(resultSuscripcion.getRecurrence())?"":resultSuscripcion.getRecurrence());
    							servicio.setSubscribable(Utils.isEmptyString(resultSuscripcion.getSubscribable())?"":resultSuscripcion.getSubscribable());
    							servicio.setHigh_detail(Utils.isEmptyString(resultSuscripcion.getHighDetail())?"":resultSuscripcion.getHighDetail());
    							servicio.setSuscripciones(null);
    							catalogoSAGENBean.setVasID(Utils.isEmptyString(resultSuscripcion.getVasId())?"":resultSuscripcion.getVasId());
    							catalogoSAGENBean.setProviderID(Utils.isEmptyString(resultSuscripcion.getProviderID())?"":resultSuscripcion.getProviderID());
    							catalogoSAGENBean.setSuscriptionID(Utils.isEmptyString(resultSuscripcion.getSuscriptionID())?"":resultSuscripcion.getSuscriptionID());
    							catalogoSAGENBean.setDescription(Utils.isEmptyString(resultSuscripcion.getDescription())?"":resultSuscripcion.getDescription());

    							List<CategoriaSAGENBean> categoriasSAGENBean = new ArrayList<CategoriaSAGENBean>();
    							if (resultSuscripcion.getCustom()!=null){
    								
    								if (resultSuscripcion.getCustom().getMktCategory()!=null){
    									for (MktCategoryType categoria:  resultSuscripcion.getCustom().getMktCategory()){
    										CategoriaSAGENBean categoriaSAGENBean = new CategoriaSAGENBean();

    										categoriaSAGENBean.setIdCategoria(Utils.isEmptyString(categoria.getId())?"":categoria.getId());
    										categoriaSAGENBean.setNombreCategoria(Utils.isEmptyString(categoria.getName())?"":categoria.getName());

    										categoriasSAGENBean.add(categoriaSAGENBean);
    										categoriaSAGENBean=null;
    									}
    									
    									catalogoSAGENBean.setCategoria(categoriasSAGENBean); 
    									categoriasSAGENBean=null;
    								}
    								

    								
    								if(resultSuscripcion.getCustom().getBundleContents()!=null){
    									String htmlPack="";
    									if(resultSuscripcion.getCustom().getBundleContents().getOptionalContents()!=null){
    										if(resultSuscripcion.getCustom().getBundleContents().getOptionalContents().getContentBundle()!=null){
    											for (com.epcs.provision.suscripcion.wssagenservice.types.ContentBundleType packcontent:  resultSuscripcion.getCustom().getBundleContents().getOptionalContents().getContentBundle()){
    												htmlPack+="<input type='radio' name='pack' class='required' value='"+packcontent.getId()+"'/> "+packcontent.getId()+"</br>";
    											} 
    											servicio.setPackcontents(htmlPack);
    										}
    									}
    								}

    								
    								
    							}
    							listaservicios.add(servicio);
    							servicio=null;

    						}
    						listSubcategory.add(subcategory);
    						subcategory=null;
    					}
    				}
    				category.setSubcategoria(listSubcategory);
    				listSubcategory=null;
    				catalogoSAGEN.add(category);
    				category=null;
    			}
    		}
        	catalogo.setCategorias(catalogoSAGEN);
        	catalogoSAGEN=null;
        	catalogo.setServicios(listaservicios);
        	listaservicios=null;
    	}



    	return catalogo;
    }
    
    /**
     * @param msisdn
     * @return
     * @throws DAOException
     * @throws ServiceException
     */
    public CatalogoSAGENBean obtenerCatalogoActualSAGEN(String msisdn) 
			throws DAOException, ServiceException{
    	
    	CatalogoSAGENBean catalogoSAGEN = this.obtenerCatalogoSAGEN();
    	ResultadoConsultarSAGENBean consultaSuscSAGEN = this.consultarSuscripcionSAGEN(msisdn);
    	
    	//Modificamos el catalogo para indicar cuales son las categorias que estan actualmente
    	//habilitadas    	
    	if (consultaSuscSAGEN.getCustom().getCategorias()!=null){		
    		for (int indexActual=0; indexActual<catalogoSAGEN.getCategoria().size(); indexActual++){    			
    			for (int indexSel=0; indexSel<consultaSuscSAGEN.getCustom().getCategorias().size(); indexSel++){
    				if (catalogoSAGEN.getCategoria().get(indexActual).getIdCategoria().equals(consultaSuscSAGEN.getCustom().getCategorias().get(indexSel).getIdCategoria())){
    					catalogoSAGEN.getCategoria().get(indexActual).setEstado(1);
    					catalogoSAGEN.getCategoria().get(indexActual).setCampanas(consultaSuscSAGEN.getCustom().getCategorias().get(indexSel).getCampanas());
    				}    				
    			}    			
    		}
    	}    	
    	return catalogoSAGEN;    
    }
    
  /**
 * @param suscripcion
 * @return
 * @throws DAOException
 * @throws ServiceException
 */
public ResultadoCrearSuscripcionSAGENBean crearSuscripcionSAGEN(SuscripcionSAGENBean suscripcion) 
			throws DAOException, ServiceException{
    	
    	LOGGER.info("Instanciando el Port.");
    	CrearSuscripcionServicePortType port = null;
    	
    	try {        
        	port = (CrearSuscripcionServicePortType) WebServiceLocator.getInstance().getPort(
        			CrearSuscripcionService.class, CrearSuscripcionServicePortType.class);   
        	
        } catch (WebServiceLocatorException e) {
            LOGGER.error("Error al inicializar el Port "
                    + CrearSuscripcionService.class);
            LOGGER.error( new DAOException(e));
        }
        
        CreateSuscriptionType request = new CreateSuscriptionType();
        ResultadoCreateSuscriptionType response = null;
        
        try {
            LOGGER.info("Configurando parametros de la peticion");
            
            request.setMsisdn(suscripcion.getMsisdn());
            request.setVasID(SAGEN_VAS_ID);
            request.setProviderID(SAGEN_PROVIDER_ID);
            request.setServiceID(SAGEN_SERVICE_ID);
            request.setSuscriptionID(SAGEN_SUSCRIPTION_ID);
            request.setLa(SAGEN_LA);
            request.setCanal(SAGEN_CANAL);
            
            //hcastillo
           
            
            CustomType custom = new CustomType();
            //hcastillo
            custom.setDay("");
            custom.setDuration("");
            custom.setHour("");
            custom.setOverriding("");
            custom.setSendSmsSucess("");
            custom.setTime("");
            
            custom.setSuscriptionDays(suscripcion.getCustom().getDiasSusc());
            custom.setSuscriptionFromTime(suscripcion.getCustom().getHoraInicial());
            custom.setSuscriptionToTime(suscripcion.getCustom().getHoraFinal());
            
            com.epcs.provision.suscripcion.wssagenservice.crearsuscripcionservice.MktCategoryType categoria;
            
            if (suscripcion.getCustom().getCategorias()!=null){
	            for (CategoriaSAGENBean categoriaSAGEN: suscripcion.getCustom().getCategorias()){
	            	categoria = new  com.epcs.provision.suscripcion.wssagenservice.crearsuscripcionservice.MktCategoryType();
	            	categoria.setId(categoriaSAGEN.getIdCategoria());
	            	categoria.setName(categoriaSAGEN.getNombreCategoria());
	            	categoria.setDescription(categoriaSAGEN.getDescripcion());
	            	
	            	custom.getMktCategory().add(categoria);
	            }
            }
          
            //custom.getMktCategories().add(categorias);
            if (suscripcion.getCustom()!=null){
            	if (suscripcion.getCustom().getUsuario()!=null){
            		MktCustomerType usuario = new MktCustomerType();
            		usuario.setName(suscripcion.getCustom().getUsuario().getNombre());
            		usuario.setInfo(suscripcion.getCustom().getUsuario().getInfo());
            		usuario.setMktEmail(suscripcion.getCustom().getUsuario().getEmail());
            		usuario.setMktFechaNacimiento(suscripcion.getCustom().getUsuario().getFechaNacimiento());
            		usuario.setMktPersona(suscripcion.getCustom().getUsuario().getSexo());
            		if (suscripcion.getCustom().getUsuario().getDireccion()!=null){
            			MktDireccionType direccion = new MktDireccionType();
            			direccion.setMktRegion(suscripcion.getCustom().getUsuario().getDireccion().getRegion().getDescripcion());
            			direccion.setMktCiudad(suscripcion.getCustom().getUsuario().getDireccion().getCiudad().getDescripcion());
            			direccion.setMktComuna(suscripcion.getCustom().getUsuario().getDireccion().getComuna().getDescripcion());
            			direccion.setMktCalle(suscripcion.getCustom().getUsuario().getDireccion().getCalle());
            			direccion.setMktNumero(suscripcion.getCustom().getUsuario().getDireccion().getNumero());
            			direccion.setMktDpto(suscripcion.getCustom().getUsuario().getDireccion().getDepartamento());

            			usuario.setMktDireccion(direccion);

            		}
            		custom.setMktCustomer(usuario);
            	}

            }
            
            PackContentsType pack=new PackContentsType();
            
            BundleContentsType bundle=new BundleContentsType();
            custom.setPackContents(pack);
            custom.setBundleContents(bundle);
            request.setCustom(custom);

            LOGGER.info("Invocando servicio");
            response = port.createSuscription(request);
            
            ResultadoCrearSuscripcionSAGENBean resultCrearSuscripcionSAGENBean = new ResultadoCrearSuscripcionSAGENBean();
            
            resultCrearSuscripcionSAGENBean.setInfo(response.getInfo());
            resultCrearSuscripcionSAGENBean.setTxID(response.getTxID());
            
            return resultCrearSuscripcionSAGENBean;           

        } catch (Exception e) {
            LOGGER.error("Exception caught on Service invocation", e);
            return null;
        }
    }
    
    /** disable a sagen suscription 
     * @param msisdn
     * @return
     * @throws DAOException
     * @throws ServiceException
     */
    public ResultadoEliminarSuscripcionSAGENBean eliminarSuscripcionSAGEN(String msisdn) 
			throws DAOException, ServiceException{
		
		LOGGER.info("Instanciando el Port.");
		EliminarSuscripcionServicePortType port = null;
		
		try {        
			port = (EliminarSuscripcionServicePortType) WebServiceLocator.getInstance().getPort(
					EliminarSuscripcionService.class, EliminarSuscripcionServicePortType.class);   
			
		} catch (WebServiceLocatorException e) {
		    LOGGER.error("Error al inicializar el Port "
		            + EliminarSuscripcionService.class);
		    LOGGER.error( new DAOException(e));
		}	
		
		DeleteSuscriptionType request = new DeleteSuscriptionType();
		ResultadoDeleteSuscriptionType response = null;
		
		try {
			LOGGER.info("Configurando parametros de la peticion");
			
			request.setMsisdn(msisdn);
            request.setVasID(SAGEN_VAS_ID);
            request.setProviderID(SAGEN_PROVIDER_ID);
            request.setServiceID(SAGEN_SERVICE_ID);
            request.setSuscriptionID(SAGEN_SUSCRIPTION_ID);
            request.setLa(SAGEN_LA);
            request.setCanal(SAGEN_CANAL);	            
            LOGGER.info("Invocando el Servicio");
			response = port.deleteSuscription(request);
			ResultadoEliminarSuscripcionSAGENBean resultadoEliminarSuscSAGENBean = new ResultadoEliminarSuscripcionSAGENBean();
			return resultadoEliminarSuscSAGENBean;
		}
		catch (Exception e) {
			LOGGER.error("Exception caught on Service invocation", e);
			return null;
	    }	
	}
    
    
    
	/**
	 * Allow us loads suscriptions catalog  
	 * @param msisdn
	 * @param suscripcion
	 * @return
	 * @throws DAOException
	 * @throws ServiceException
	 */
	public CatalogoServiciosBean obtenerCatalogoActualServicios(String msisdn, List<FamiliaSuscripcionBean> suscripcion) 
    throws DAOException, ServiceException{
       
    	CatalogoServiciosBean catalogo= this.obtenerCatalogoServicios();
    	List<ServicioSagenBean> servicioSAGEN = catalogo.getServicios();
    	List<ResultadoConsultarSAGENBean> consultaSuscSAGEN = this.consultarSuscripcionServicios(msisdn);
        List<ServicioSagenBean> servicio = null;
    	List<PaginaServiciosSagenBean> paginas= new ArrayList<PaginaServiciosSagenBean>();
    	List<FamiliaSuscripcionBean> listadofamilias=null;
		double topeRegistros = Double.parseDouble(TOPE_REGISTROS_PAGINA_HISTORIAL);
	    double numeroPaginas = Math.ceil((double) servicioSAGEN.size() / topeRegistros);
		int paginaTotal = servicioSAGEN.size() > topeRegistros ? ((int) numeroPaginas >= numeroPaginas ? (int) numeroPaginas : (int) (numeroPaginas + 1))	: 1;
		int contador=0;
		String resultServiceId=null;
		String resultVasId=null;
		String resultSuscriptionId=null;
		String resultProviderId=null;
		String resultLa=null;
        boolean sw =true;
        for(int i=0; i < paginaTotal;i++){
        	PaginaServiciosSagenBean paginaServicios = new PaginaServiciosSagenBean();	    	
        	int l=0;			
        	servicio = new ArrayList<ServicioSagenBean>();
        	while( l < topeRegistros && sw  ){ 	    	    
        		if (contador < servicioSAGEN.size()) {
        			ServicioSagenBean  detalle = (ServicioSagenBean) servicioSAGEN.get(contador);
        			String serviceId=detalle.getServiceId()!=null?detalle.getServiceId().trim():"";
        			String providerId=detalle.getProviderId()!=null?detalle.getProviderId().trim():"";
        			String vasId=detalle.getVasId()!=null?detalle.getVasId().trim():"";
        			String suscriptionId=detalle.getSuscriptionId()!=null?detalle.getSuscriptionId().trim():"";
                    String la=detalle.getLa()!=null?detalle.getLa().trim():"";
                    detalle.setFamilia("");
                    detalle.setSuscripciones(null);
                    detalle.setFamiliaStatus("");
        			FieldSearchServicios(detalle,suscripcion);
        			detalle.setEstado(ESTADO_DESHABILITADO_SERVICIO);
        			if(consultaSuscSAGEN!=null){
        				for (int indexSel=0; indexSel<consultaSuscSAGEN.size(); indexSel++){
        					ResultadoConsultarSAGENBean result=consultaSuscSAGEN.get(indexSel);	
        					if(result!=null){
        						resultServiceId=result.getServiceID()!=null?result.getServiceID().trim():"";
        						resultVasId=result.getVasID()!=null?result.getVasID().trim():"";
        						resultSuscriptionId=result.getSuscriptionID()!=null?result.getSuscriptionID().trim():"";
        						resultProviderId=result.getProviderID()!=null?result.getProviderID().trim():"";
        						resultLa=result.getLa()!=null?result.getLa().trim():"";
        						if(resultServiceId.equalsIgnoreCase(serviceId) && resultVasId.equalsIgnoreCase(vasId) && resultSuscriptionId.equalsIgnoreCase(suscriptionId) && resultProviderId.equalsIgnoreCase(providerId) && resultLa.equalsIgnoreCase(la))
        							detalle.setEstado(ESTADO_HABILITADO_SERVICIO);
        					}
        					result=null;

        				}
        			}
        			
        			if (detalle.getFamiliaStatus()!=null){
	        			if(detalle.getFamiliaStatus().equals(SERVICIO_INLISTA_NEGRA)){
	        				detalle.setEstado(ESTADO_BLOQUEADO_SERVICIO);
	        			}
        			}
        			detalle.setIndex(l);
        			detalle.setPageIndex(i);
        			servicio.add(detalle);
        			contador++;
                    l++;
        			
        		} else{
        			sw=false;
        		}


        	}
        	
    		paginaServicios.setListadoservicios(servicio);
    		paginas.add(paginaServicios);
    		paginaServicios=null;
        }
        
        
        listadofamilias=loadListPageIndex(suscripcion,paginas);
        fieldDescriptionFamily(listadofamilias,paginas);
        catalogo.setListFamily(listadofamilias);
		catalogo.setPaginas(paginas);
    	
    	return catalogo;   

    }
	
	
	private static void fieldDescriptionFamily(List<FamiliaSuscripcionBean> listadofamilias,	List<PaginaServiciosSagenBean> paginas) {
		String htmlFamilias="";
		for (FamiliaSuscripcionBean family : listadofamilias) {
			for (PaginaServiciosSagenBean pag : paginas) {
				for (ServicioSagenBean service : pag.getListadoservicios()) {
					if (!service.getFamilia().equals("")) {
						if (family.getFamilia().equals(service.getFamilia())) {
						service.setSuscripciones(family.getDescription());
						htmlFamilias=getHtmlFamilias(family.getDescription());
						service.setHtmlFamilias(htmlFamilias);
							
						}
					}
				}
			}
		}
	}
	
	
	/**
	 * @param suscripcion
	 * @param paginas
	 * @return
	 */
	private static List<FamiliaSuscripcionBean> loadListPageIndex(List<FamiliaSuscripcionBean> suscripcion,	List<PaginaServiciosSagenBean> paginas) {
		List<FamiliaSuscripcionBean> listafamilias=new ArrayList<FamiliaSuscripcionBean>();
		ArrayList<PageIndexBean> listaIndices = null;	
		ArrayList<String> desc=null;
		int cont;
		for (FamiliaSuscripcionBean family : suscripcion) {
			listaIndices = new ArrayList<PageIndexBean>();
            cont=0;
            desc= new ArrayList<String>();
			for (PaginaServiciosSagenBean pag : paginas) {
				for (ServicioSagenBean service : pag.getListadoservicios()) {
					if (!service.getFamilia().equals("")) {
						if (family.getFamilia().equals(service.getFamilia())) {
							PageIndexBean page = new PageIndexBean();
							page.setNumberPage(service.getPageIndex());
							page.setPositionPage(service.getIndex());
							listaIndices.add(page);
							desc.add(service.getDescripcion());
							cont++;
							pag = null;
						}

					}
				}
			}
			family.setPageIndices(listaIndices);
			//Funcion para eliminar Repetidos HashSet
			HashSet<String> hs = new HashSet<String>();
			hs.addAll(desc);
		    desc.clear();
		    desc.addAll(hs);
		    //Convertir Array en Cadena String
		    String[] temp=desc.toArray(new String[desc.size()]);
		    family.setDescription(temp);
			listaIndices=null;
			listafamilias.add(family);
			family=null;
			desc=null;

		}

		return listafamilias;
	}

	
	/**
	 * @param ed
	 * @param suscripcion
	 * @return
	 */
	public ServicioSagenBean FieldSearchServicios(ServicioSagenBean ed,List<FamiliaSuscripcionBean> suscripcion){
       boolean sw=false;
       boolean existe=false;
	   int cont=0;	
	   //String htmlFamilias="";
		for (int i=0;i<suscripcion.size();i++){
			FamiliaSuscripcionBean familia=suscripcion.get(i);
			cont=0;
			for(int j=0;j<familia.getListadoVasid().size();j++){
				CombinacionVas ci=(CombinacionVas) familia.getListadoVasid().get(j);
				String value= findCombinacion(ed,ci.getSearch());
				sw=false;
				if(value!=null) sw=findValueListado(value,ci.getListadosearch());
		        if(sw){
		        	cont++;
		        }else{
		        	if(!ci.getSearch().equals("serviceID")){
		        		break;
		        	}else{
		        		if(value.equals("")) existe=true;
		        	}
		        }
			}

			if((cont==familia.getListadoVasid().size()) || (cont==(familia.getListadoVasid().size()-1) && existe)){
				//htmlFamilias=getHtmlFamilias(familia.getDescription());
	            //ed.setHtmlFamilias(htmlFamilias);
				//ed.setSuscripciones(familia.getDescription());
				ed.setFamiliaStatus(familia.getEstado());
				ed.setFamilia(familia.getFamilia());
				break;
				
			}else{
				ed.setFamilia("");
				ed.setSuscripciones(null);
				ed.setFamiliaStatus("");
			}
			
		}
       return ed;
		
	}
	
	
	   private static String getHtmlFamilias(String [] familias){
			  String cadena="";
			   for (int i=0;i<familias.length;i++){
				   cadena+="-"+familias[i]+"<br>";
			   }
			   return cadena;
		   }
	
	
	/**
	 * @param value
	 * @param listado
	 * @return
	 */
	private boolean findValueListado(String value,String[] listado){
		boolean existe=false;
		for(int k=0;k<listado.length;k++){
			if(value.equals(listado[k])){
				existe=true;
				break;
			}
		}
		return existe;
	}
	

	/**
	 * @param ed
	 * @param name
	 * @return
	 */
	private String findCombinacion (ServicioSagenBean ed,String name){
		String str=null;
		ServicioSagenBean temp=ed;
		Field[] h= temp.getClass().getDeclaredFields();
		for (Field m: h){
			if(m.getName().equalsIgnoreCase(name)){
				try {
					m.setAccessible(true);
					str = (String) m.get(ed);
					m.setAccessible(false);
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					LOGGER.error(e);
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					LOGGER.error(e);
				}

				break;

			}
		}
		return str;
	}


	/**
	 * @param suscripcion
	 * @return
	 * @throws DAOException
	 * @throws ServiceException
	 */
	public ResultadoCrearSuscripcionSAGENBean ResultadoCrearSuscriptionServices(SuscripcionSAGENBean suscripcion) throws DAOException, ServiceException{
    	LOGGER.info("Instanciando el Port.");
    	CrearSuscripcionServicePortType port = null;
    	
    	try {        
        	port = (CrearSuscripcionServicePortType) WebServiceLocator.getInstance().getPort(
        			CrearSuscripcionService.class, CrearSuscripcionServicePortType.class);   
        	
        } catch (WebServiceLocatorException e) {
            LOGGER.error("Error al inicializar el Port "
                    + CrearSuscripcionService.class);
            LOGGER.error( new DAOException(e));
        }
        
        CreateSuscriptionType request = new CreateSuscriptionType();
        ResultadoCreateSuscriptionType response = null;
        
        try {
            LOGGER.info("Configurando parametros de la peticion");
            
            request.setMsisdn(suscripcion.getMsisdn());
            request.setVasID(suscripcion.getVasID());
            request.setProviderID(suscripcion.getProviderID());
            request.setServiceID(suscripcion.getServiceID());
            request.setSuscriptionID(suscripcion.getSuscriptionID());
            request.setLa(suscripcion.getLa());
            request.setCanal(suscripcion.getCanal());
            
            StringBuilder requestParams = new StringBuilder();
            requestParams.append("request.msisdn: ").append(suscripcion.getMsisdn()).append("\n");
            requestParams.append("request.vasID: ").append(suscripcion.getVasID()).append("\n");
            requestParams.append("request.providerID: ").append(suscripcion.getProviderID()).append("\n");
            requestParams.append("request.serviceID: ").append(suscripcion.getServiceID()).append("\n");
            requestParams.append("request.suscriptionID: ").append(suscripcion.getSuscriptionID()).append("\n");
            requestParams.append("request.la: ").append(suscripcion.getLa()).append("\n");
            requestParams.append("request.canal: ").append(suscripcion.getCanal()).append("\n\n");           
            
           if(suscripcion.getVasID().equalsIgnoreCase("MMSINFO")){
        	   requestParams.append("request.day: ").append(suscripcion.getCustom().getDay()).append("\n");
               requestParams.append("request.hour: ").append(suscripcion.getCustom().getHour()).append("\n");
               requestParams.append("request.duration: ").append(suscripcion.getCustom().getOverriding()).append("\n");
               requestParams.append("request.overriding: ").append(suscripcion.getCustom().getDuration()).append("\n");
               requestParams.append("request.sendsmssuccess: ").append(suscripcion.getCustom().getSendSmsSuccess()).append("\n");
           }
           if(suscripcion.getVasID().equalsIgnoreCase("SMSINFO")){
            requestParams.append("request.time: ").append(suscripcion.getCustom().getTime()).append("\n");
           }
            
            CustomType custom = new CustomType();
            custom.setTime(suscripcion.getCustom().getTime());
            custom.setDay(suscripcion.getCustom().getDay());
            custom.setHour(suscripcion.getCustom().getHour());
            custom.setDuration(suscripcion.getCustom().getDuration());
            custom.setOverriding(suscripcion.getCustom().getOverriding());
            custom.setSendSmsSucess(suscripcion.getCustom().getSendSmsSuccess());
            //custom.setSuscriptionDays(suscripcion.getCustom().getDiasSusc());
            //custom.setSuscriptionFromTime(suscripcion.getCustom().getHoraInicial());
            //custom.setSuscriptionToTime(suscripcion.getCustom().getHoraFinal());
            
            com.epcs.provision.suscripcion.wssagenservice.crearsuscripcionservice.MktCategoryType categoria;
            
            if (suscripcion.getCustom().getCategorias()!=null){
	            for (CategoriaSAGENBean categoriaSAGEN: suscripcion.getCustom().getCategorias()){
	            	categoria = new  com.epcs.provision.suscripcion.wssagenservice.crearsuscripcionservice.MktCategoryType();
	            	categoria.setId(categoriaSAGEN.getIdCategoria());
	            	categoria.setName(categoriaSAGEN.getNombreCategoria());
	            	categoria.setDescription(categoriaSAGEN.getDescripcion());	            	
	            	custom.getMktCategory().add(categoria);
	            }
            }
          
            //custom.getMktCategories().add(categorias);
            
            MktCustomerType usuario = new MktCustomerType();
            MktDireccionType direccion = new MktDireccionType();
            usuario.setMktDireccion(direccion);
            custom.setMktCustomer(usuario);
            PackContentsType pack=new PackContentsType();
            
            BundleContentsType bundle=new BundleContentsType();
            custom.setPackContents(pack);
            if (suscripcion.getCustom()!=null){
            	if (suscripcion.getCustom().getOptionalcontents()!=null){
            		for (int j=0;j<suscripcion.getCustom().getOptionalcontents().size();j++){
            			BundleContent bund=suscripcion.getCustom().getOptionalcontents().get(j);
            			OptionalContentsType opt=new OptionalContentsType();
            			ContentBundleType ctb=new ContentBundleType();
            			requestParams.append("request.custom.optionalcontents.contentbunle: ").append(bund.getId()).append("\n");
            			ctb.setId(bund.getId());
            			opt.getContentBundle().add(ctb);
            			bundle.setOptionalContents(opt);
            		}
            	}
            }
            custom.setBundleContents(bundle);
            request.setCustom(custom);

            custom.setPackContents(pack);
            custom.setBundleContents(bundle);

            
            request.setCustom(custom);

            LOGGER.info("Invocando servicio");
            LOGGER.info("Parametros de la peticion: " + requestParams.toString());
            response = port.createSuscription(request);
            
            ResultadoCrearSuscripcionSAGENBean resultCrearSuscripcionSAGENBean = new ResultadoCrearSuscripcionSAGENBean();
            
            resultCrearSuscripcionSAGENBean.setInfo(response.getRespuesta().getCodigo());
            resultCrearSuscripcionSAGENBean.setTxID(response.getRespuesta().getDescripcion());
            
            StringBuilder responseValues = new StringBuilder();  
            if(response!=null){
            	if(response.getRespuesta()!=null){
            		responseValues.append("response.respuesta.codigo: ").append(response.getRespuesta().getCodigo()).append("\n");
            		responseValues.append("response.respuesta.descripcion: ").append(response.getRespuesta().getDescripcion()).append("\n");
            	}
            }
            
            LOGGER.info("Respuesta del servicio");
            LOGGER.info("Valores de respuesta: " + responseValues.toString());
            
            responseValues.append("response.info: ").append(response.getInfo()).append("\n");
            responseValues.append("response.TxID: ").append(response.getTxID()).append("\n");
            
            return resultCrearSuscripcionSAGENBean;           

        } catch (Exception e) {
            LOGGER.error("Exception caught on Service invocation", e);
            return null;
        }
	}
	
	//hcastillo
	
	/**
	 * @param suscripcion
	 * @return
	 * @throws DAOException
	 * @throws ServiceException
	 */
	public ResultadoEliminarSuscripcionSAGENBean eliminarSuscripcionServiciosSagen(SuscripcionSAGENBean suscripcion) 
	throws DAOException, ServiceException{

		LOGGER.info("Instanciando el Port.");
		EliminarSuscripcionServicePortType port = null;

		try {        
			port = (EliminarSuscripcionServicePortType) WebServiceLocator.getInstance().getPort(
					EliminarSuscripcionService.class, EliminarSuscripcionServicePortType.class);   

		} catch (WebServiceLocatorException e) {
			LOGGER.error("Error al inicializar el Port "
					+ EliminarSuscripcionService.class);
			LOGGER.error( new DAOException(e));
		}	

		DeleteSuscriptionType request = new DeleteSuscriptionType();
		ResultadoDeleteSuscriptionType response = null;

		try {
			LOGGER.info("Configurando parametros de la peticion");

			request.setMsisdn(suscripcion.getMsisdn());
			request.setVasID(suscripcion.getVasID());
			request.setProviderID(suscripcion.getProviderID());
			request.setServiceID(suscripcion.getServiceID());
			request.setSuscriptionID(suscripcion.getSuscriptionID());
			request.setLa(suscripcion.getLa());
			request.setCanal(suscripcion.getCanal());
			
			StringBuilder requestParams = new StringBuilder();
            requestParams.append("request.msisdn: ").append(suscripcion.getMsisdn()).append("\n");
            requestParams.append("request.vasID: ").append(suscripcion.getVasID()).append("\n");
            requestParams.append("request.providerID: ").append(suscripcion.getProviderID()).append("\n");
            requestParams.append("request.serviceID: ").append(suscripcion.getServiceID()).append("\n");
            requestParams.append("request.suscriptionID: ").append(suscripcion.getSuscriptionID()).append("\n");
            requestParams.append("request.la: ").append(suscripcion.getLa()).append("\n");
            requestParams.append("request.canal: ").append(suscripcion.getCanal()).append("\n\n");			

            LOGGER.info("Invocando servicio");
            LOGGER.info("Parametros de la peticion: " + requestParams.toString());
            response = port.deleteSuscription(request);


			

            
            StringBuilder responseValues = new StringBuilder();
            if(response!=null){
            	if(response.getRespuesta()!=null){
                  responseValues.append("response.respuesta.codigo: ").append(response.getRespuesta().getCodigo()).append("\n");
                  responseValues.append("response.respuesta.descripcion: ").append(response.getRespuesta().getDescripcion()).append("\n");
            	}
            }
            
			ResultadoEliminarSuscripcionSAGENBean resultadoEliminarSuscSAGENBean = new ResultadoEliminarSuscripcionSAGENBean();
			if(response!=null){
				if(response.getRespuesta()!=null){
					resultadoEliminarSuscSAGENBean.setInfo(response.getRespuesta().getCodigo());
					resultadoEliminarSuscSAGENBean.setTxID(response.getRespuesta().getDescripcion());
				}
			} 	
            
            responseValues.append("response.info: ").append(response.getInfo()).append("\n");
            responseValues.append("response.TxID: ").append(response.getTxID()).append("\n");
            
            LOGGER.info("Respuesta del servicio");
			LOGGER.info("Valores de respuesta: " + responseValues.toString());
			
			return resultadoEliminarSuscSAGENBean;
		}
		catch (Exception e) {
			LOGGER.error("Exception caught on Service invocation", e);
			return null;
		}	
	}
	
	
	/**
	 * @param suscripcion
	 * @return
	 * @throws DAOException
	 * @throws ServiceException
	 */
	public  ResultadoFamiliaSegmentate bloquearFamiliaSegmentate(SuscripcionSAGENBean suscripcion) 
	throws DAOException, ServiceException{

		LOGGER.info("Instanciando el Port.");


		PutIntoBlackListServicePortType port = null;

		try {        
			port = (PutIntoBlackListServicePortType) WebServiceLocator.getInstance().getPort(
					PutIntoBlackListService.class, PutIntoBlackListServicePortType.class);   

		} catch (WebServiceLocatorException e) {
			LOGGER.error("Error al inicializar el Port "
					+ PutIntoBlackListService.class);
			LOGGER.error( new DAOException(e));
		}	

		PutIntoBlackListRequestType request = new PutIntoBlackListRequestType();
		ResultadoPutIntoBlackListRequestType response = null;
		ResultadoFamiliaSegmentate resultadoFamiliaSegmentate=null;

		try {
			LOGGER.info("Configurando parametros de la peticion");

			request.setMsisdn(suscripcion.getMsisdn());
            request.setService(suscripcion.getFamilia());
            request.setCodMotivo(FAMILIA_MOTIVO);
            
            
            StringBuilder requestParams = new StringBuilder();
            requestParams.append("request.msisdn: ").append(suscripcion.getMsisdn()).append("\n");
            requestParams.append("request.service: ").append(suscripcion.getFamilia()).append("\n\n");
            requestParams.append("request.motivo: ").append(FAMILIA_MOTIVO).append("\n\n");

            LOGGER.info("Invocando servicio");
            LOGGER.info("Parametros de la peticion: " + requestParams.toString());
            response = port.putIntoBlackListRequest(request);
            
            StringBuilder responseValues = new StringBuilder();     
            if(response!=null){
            	if(response.getRespuesta()!=null){
            		responseValues.append("response.respuesta.codigo: ").append(response.getRespuesta().getCodigo()).append("\n");
            		responseValues.append("response.respuesta.descripcion: ").append(response.getRespuesta().getDescripcion()).append("\n");
           		    LOGGER.info("Respuesta del servicio");
            		LOGGER.info("Valores de respuesta: " + responseValues.toString());

            	}
            }


            if (response!=null){
            	if(response.getClient()!=null){
            		List<com.esa.provision.suscripcion.serviciossegmentate.types.ServiceType> lista=response.getClient().getService();
            		LOGGER.info("Respuesta del servicio");
                    resultadoFamiliaSegmentate=new ResultadoFamiliaSegmentate();
            		for(int i=0;i<lista.size();i++){
            			com.esa.provision.suscripcion.serviciossegmentate.types.ServiceType ed=lista.get(i);
                		responseValues.append("response.client.service.name: ").append(ed.getName()).append("\n");
                		responseValues.append("response.client.service.status: ").append(ed.getStatus()).append("\n");
    					LOGGER.info("Respuesta del servicio");
    					LOGGER.info("Valores de respuesta: " + responseValues.toString());
                        resultadoFamiliaSegmentate.setName(ed.getName());
                        resultadoFamiliaSegmentate.setStatus(ed.getStatus());
            		}
            	}
            }
		


			
			return resultadoFamiliaSegmentate;
		}
		catch (Exception e) {
			LOGGER.error("Exception caught on Service invocation", e);
			return null;
		}	
	}
	
	/**
	 * @param suscripcion
	 * @return
	 * @throws DAOException
	 * @throws ServiceException
	 */
	public  String getEstadoFamiliaService(String movil,String familia) 
	throws DAOException, ServiceException{

	
	LOGGER.info("Instanciando el Port.");

		ClientServiceStatusServicePortType port = null;
		String estado="";

		try {        
			port = (ClientServiceStatusServicePortType) WebServiceLocator.getInstance().getPort(
					ClientServiceStatusService.class, ClientServiceStatusServicePortType.class);   

		} catch (WebServiceLocatorException e) {
			LOGGER.error("Error al inicializar el Port "
					+ ClientServiceStatusService.class);
			LOGGER.error( new DAOException(e));
		}	

		ClientServiceStatusRequestType request = new ClientServiceStatusRequestType();
		ResultadoClientServiceStatusRequestType response = null;

		try {
			LOGGER.info("Configurando parametros de la peticion");

			request.setMsisdn(movil);
			request.setService(familia);	

			LOGGER.info("Invocando Servicio");
			response = port.clientServiceStatusRequest(request);
			if (response!=null){
				if(response.getClient()!=null){
					List<ServiceType> listadoservices=response.getClient().getService();

					for (int j=0;j<listadoservices.size();j++){
						ServiceType st=listadoservices.get(j);
						if(st.getName().equals(familia)){
							estado=st.getStatus();
							break;
						}
					}
				}
			}
				return estado;
		}
		catch (Exception e) {
			LOGGER.error("Exception caught on Service invocation", e);
			return null;
		}	
	}
	
	
	/**
	 * @param suscripcion
	 * @return
	 * @throws DAOException
	 * @throws ServiceException
	 */
	public  ResultadoFamiliaSegmentate desbloquearFamiliaSegmentate(String movil,String familia) 
	throws DAOException, ServiceException{

		LOGGER.info("Instanciando el Port.");


		DeleteFromBlackListServicePortType port = null;
		ResultadoFamiliaSegmentate resultadoFamiliaSegmentate=null;
		try {        
			port = (DeleteFromBlackListServicePortType) WebServiceLocator.getInstance().getPort(
					DeleteFromBlackListService.class, DeleteFromBlackListServicePortType.class);   

		} catch (WebServiceLocatorException e) {
			LOGGER.error("Error al inicializar el Port "
					+ DeleteFromBlackListService.class);
			LOGGER.error( new DAOException(e));
		}	

		DeleteFromBlackListRequestType request = new DeleteFromBlackListRequestType();
		ResultadoDeleteFromBlackListRequestType response = null;

		try {
			LOGGER.info("Configurando parametros de la peticion");

			request.setMsisdn(movil);
            request.setService(familia);
            request.setCodMotivo(FAMILIA_MOTIVO);
            
            StringBuilder requestParams = new StringBuilder();
            requestParams.append("request.msisdn: ").append(movil).append("\n");
            requestParams.append("request.service: ").append(familia).append("\n");
            requestParams.append("request.motivo: ").append(FAMILIA_MOTIVO).append("\n");

			response = port.deleteFromBlackListRequest(request);
			
			StringBuilder responseValues = new StringBuilder();    
			if(response!=null){
				if(response.getRespuesta()!=null){
					responseValues.append("response.respuesta.codigo: ").append(response.getRespuesta().getCodigo()).append("\n");
					responseValues.append("response.respuesta.descripcion: ").append(response.getRespuesta().getDescripcion()).append("\n");

					LOGGER.info("Respuesta del servicio");
					LOGGER.info("Valores de respuesta: " + responseValues.toString());
				}
			}

			

			if (response!=null){
				if(response.getClient()!=null){
					List<ServiceType> lista=response.getClient().getService();
					resultadoFamiliaSegmentate=new ResultadoFamiliaSegmentate();
					for(int i=0;i<lista.size();i++){
						ServiceType ed=lista.get(i);
                		responseValues.append("response.client.service.name: ").append(ed.getName()).append("\n");
                		responseValues.append("response.client.service.status: ").append(ed.getStatus()).append("\n");
    					LOGGER.info("Respuesta del servicio");
    					LOGGER.info("Valores de respuesta: " + responseValues.toString());
						resultadoFamiliaSegmentate.setName(ed.getName());
						resultadoFamiliaSegmentate.setStatus(ed.getStatus());
					}
				}
			}
			

			
			return resultadoFamiliaSegmentate;
		}
		catch (Exception e) {
			LOGGER.error("Exception caught on Service invocation", e);
			return null;
		}	
	}
	
	
	
	
    
}
