/* Propiedad de Entel PCS. Todos los derechos reservados */
package com.epcs.ishop.suscripcioncdf.dao;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import com.epcs.administracion.suscripciones.AdminSuscripcionService;
import com.epcs.administracion.suscripciones.AdminSuscripcionService_Service;
import com.epcs.administracion.suscripciones.types.DesuscribirCDFType;
import com.epcs.administracion.suscripciones.types.ObtenerCompatibilidadType;
import com.epcs.administracion.suscripciones.types.ObtenerSuscripcionCDFOrqType;
import com.epcs.administracion.suscripciones.types.ObtenerSuscripcionesActivasType;
import com.epcs.administracion.suscripciones.types.ResultadoDesuscribirCDFType;
import com.epcs.administracion.suscripciones.types.ResultadoObtenerCompatibilidadType;
import com.epcs.administracion.suscripciones.types.ResultadoObtenerSuscripcionCDFOrqType;
import com.epcs.administracion.suscripciones.types.ResultadoObtenerSuscripcionesActivasType;
import com.epcs.administracion.suscripciones.types.ResultadoSuscribirCDFType;
import com.epcs.administracion.suscripciones.types.ResultadoValidarRoamingType;
import com.epcs.administracion.suscripciones.types.SuscribirCDFType;
import com.epcs.administracion.suscripciones.types.SuscripcionActivaType;
import com.epcs.administracion.suscripciones.types.SuscripcionCDFOrqType;
import com.epcs.administracion.suscripciones.types.ValidarRoamingType;
import com.epcs.bean.RespuestaBean;
import com.epcs.bean.ResultadoConsultarCatalogoCDFBean;
import com.epcs.bean.ResultadoDesuscribirCDFBean;
import com.epcs.bean.ResultadoObtenerCompatibilidadBean;
import com.epcs.bean.ResultadoObtenerSuscripcionCDFOrqBean;
import com.epcs.bean.ResultadoObtenerSuscripcionesActivasBean;
import com.epcs.bean.ResultadoSuscribirCDFBean;
import com.epcs.bean.ResultadoValidarRoamingBean;
import com.epcs.bean.SuscripcionCDFBean;
import com.epcs.bean.SuscripcionCDFOrqBean;
import com.epcs.cliente.perfil.ClientePerfilService;
import com.epcs.cliente.perfil.ClientePerfilServicePortType;
import com.epcs.cliente.perfil.types.CatalogoType;
import com.epcs.cliente.perfil.types.ConsultarCatalogoType;
import com.epcs.cliente.perfil.types.ResultadoConsultarCatalogoType;
import com.epcs.recursoti.configuracion.DateHelper;
import com.epcs.recursoti.configuracion.MiEntelProperties;
import com.epcs.recursoti.configuracion.Utils;
import com.epcs.recursoti.configuracion.locator.WebServiceLocator;
import com.epcs.recursoti.configuracion.locator.WebServiceLocatorException;
import com.epcs.recursoti.excepcion.DAOException;
import com.epcs.recursoti.excepcion.ServiceException;

/**
 * @author jivasquez (I2B) en nombre de Absalon Opazo (Atencion al Cliente, EntelPcs)
 *
 */
public class SuscripcionCDFDAO {
    

    /**
     * Logger para SuscripcionCDFDAO
     */
    private static final Logger LOGGER = Logger.getLogger(SuscripcionCDFDAO.class);
    public static final String CODIGO_RESPUESTA_OK = MiEntelProperties
        .getProperty("servicios.codigoRespuesta.exito");
    public static final String CODIGO_ERROR_EN_ROAMING_SIN_3G = MiEntelProperties
    	.getServiceMessages().getErrorMessage("suscripcionesCDFService","sin3GRoaming");
    public static final String CODIGO_ERROR_EN_ROAMING = MiEntelProperties
    	.getServiceMessages().getErrorMessage("suscripcionesCDFService","roaming");
    public static final String CODIGO_ERROR_SIN_3G = MiEntelProperties
    	.getServiceMessages().getErrorMessage("suscripcionesCDFService","sin3G");
    public static final String FECHA_ACTIVACION_DEFAULT = MiEntelProperties
		.getProperty("ishop.suscripcioncdf.fechaActivacion.default");

    
    
    /**
     * Obtiene el catalogo de suscripciones CDF
     * @return {@link ResultadoConsultarCatalogoCDFBean} con el catalogo de suscripciones CDF
     * @throws DAOException
     * @throws ServiceException
     */
    public ResultadoConsultarCatalogoCDFBean consultarCatalogo () 
        throws DAOException, ServiceException {
        
        ResultadoConsultarCatalogoCDFBean resultado = new ResultadoConsultarCatalogoCDFBean();
        
        ClientePerfilServicePortType port = null;
        LOGGER.info("Instanciando el port " + ClientePerfilServicePortType.class);
        try {
            port = (ClientePerfilServicePortType) WebServiceLocator.getInstance().getPort(
            		ClientePerfilService.class, ClientePerfilServicePortType.class);
        } catch (WebServiceLocatorException e) {
            LOGGER.error("Error al inicializar el Port " + ClientePerfilServicePortType.class, e);
            LOGGER.error( new DAOException(e));
        }
        

        ConsultarCatalogoType request = new ConsultarCatalogoType();
        ResultadoConsultarCatalogoType response = null;

        try {

            LOGGER.info("Configurando Datos de la peticion");
            request.setAppTag(MiEntelProperties
                    .getProperty("ishop.suscripcioncdf.consultarCatalogo.appTag"));
            

            LOGGER.info("Invocando servicio");
            response = port.consultarCatalogo(request);

        } catch (Exception e) {
            LOGGER.error("Exception caught on Service invocation: consultarCatalogo",
                    e);
            LOGGER.error( new DAOException(e));
        }

        String codigoRespuesta = response.getRespuesta().getCodigo();
        String descripcionRespuesta = response.getRespuesta().getDescripcion();

        LOGGER.info("codigoRespuesta " + codigoRespuesta);
        LOGGER.info("descripcionRespuesta " + descripcionRespuesta);

        if (Utils.isEmptyString(codigoRespuesta)) {
            LOGGER.error( new DAOException("consultarCatalogo: Servicio no responde "
                    + "con codigoRespuesta"));
        }

        if (CODIGO_RESPUESTA_OK.equals(codigoRespuesta)) {

            try {
                
                List<CatalogoType> catalogo = response.getCatalogo();
                List<SuscripcionCDFBean> suscripciones = new ArrayList<SuscripcionCDFBean>();
                
                if(catalogo == null || catalogo.isEmpty()){
                    LOGGER.info("No hay suscripciones CDF disponibles en este momento");
                    resultado.setCatalogo(suscripciones);
                }else {
                    
                    //Extraemos la informacion de las suscripciones CDF disponibles
                    for(CatalogoType suscripcionType : catalogo){
                        SuscripcionCDFBean suscripcion = new SuscripcionCDFBean();
                        suscripcion.setIdSuscripcion(suscripcionType.getIdSuscripcion());
                        suscripcion.setNombre(suscripcionType.getNombre());
                        suscripcion.setPrecio((Double) Utils.parseNumber(suscripcionType.getPrecio(), 3));
                        suscripcion.setMercado(suscripcionType.getMercado());
                        suscripcion.setStreaming((Integer) Utils.parseNumber(suscripcionType.getStreaming(), 1));
                        suscripcion.setVideo((Integer) Utils.parseNumber(suscripcionType.getVideo(), 1));
                        suscripciones.add(suscripcion);
                    }
                    resultado.setCatalogo(suscripciones);
                }

            } catch (Exception e) {
                LOGGER.error("Exception caught on Service response: "
                        + "consultarCatalogo", e);
                LOGGER.error( new DAOException(e));
            }

        }
        else {
            LOGGER.error("consultarCatalogo: Service error code received: "
                    + codigoRespuesta + " - " + descripcionRespuesta);
            LOGGER.error( new ServiceException(codigoRespuesta, descripcionRespuesta));
        }
        
        return resultado;
    }
    
    /**
     * Obtiene las suscripciones CDF activadas por el usuario
     * @param msisdn
     * @return {@link ResultadoObtenerSuscripcionesActivasBean} con las suscripciones CDF 
     * que el usuario tiene activas.
     * @throws DAOException
     * @throws ServiceException
     */
    public ResultadoObtenerSuscripcionesActivasBean obtenerSuscripcionesActivas(String msisdn) 
        throws DAOException, ServiceException {
        
        ResultadoObtenerSuscripcionesActivasBean resultado = 
            new ResultadoObtenerSuscripcionesActivasBean();
        
        

        AdminSuscripcionService port = null;
        LOGGER.info("Instanciando el port " + AdminSuscripcionService.class);
        try {
            port = (AdminSuscripcionService) WebServiceLocator.getInstance().getPort(
                    AdminSuscripcionService_Service.class, AdminSuscripcionService.class);
        } catch (WebServiceLocatorException e) {
            LOGGER.error("Error al inicializar el Port " + AdminSuscripcionService.class, e);
            LOGGER.error( new DAOException(e));
        }

        

        ObtenerSuscripcionesActivasType request = new ObtenerSuscripcionesActivasType();
        ResultadoObtenerSuscripcionesActivasType response = null;

        try {

            LOGGER.info("Configurando Datos de la peticion");
            request.setMsisdn(msisdn);
            request.setAppTag(MiEntelProperties.
                    getProperty("ishop.suscripcioncdf.obtenerSuscripcionesActivas.appTag"));
            

            LOGGER.info("Invocando servicio");
            response = port.obtenerSuscripcionesActivas(request);

        } catch (Exception e) {
            LOGGER.error("Exception caught on Service invocation: obtenerSuscripcionesActivas",
                    e);
            LOGGER.error( new DAOException(e));
        }

        String codigoRespuesta = response.getRespuesta().getCodigo();
        String descripcionRespuesta = response.getRespuesta().getDescripcion();

        LOGGER.info("codigoRespuesta " + codigoRespuesta);
        LOGGER.info("descripcionRespuesta " + descripcionRespuesta);

        if (Utils.isEmptyString(codigoRespuesta)) {
            LOGGER.error( new DAOException("obtenerSuscripcionesActivas: Servicio no responde "
                    + "con codigoRespuesta"));
        }

        if (CODIGO_RESPUESTA_OK.equals(codigoRespuesta)) {

            try {

                List<SuscripcionActivaType> suscripcionesType = response.getSuscripciones();
                List<SuscripcionCDFBean> suscripciones = new ArrayList<SuscripcionCDFBean>();
                
                if(suscripciones == null || suscripciones.isEmpty()){
                    LOGGER.info("No hay suscripciones CDF disponibles en este momento");
                    resultado.setSuscripciones(suscripciones);
                }else {
                    
                    //Extraemos la informacion de las suscripciones CDF disponibles
                    for(SuscripcionActivaType suscripcionType : suscripcionesType){
                        SuscripcionCDFBean suscripcion = new SuscripcionCDFBean();
                        suscripcion.setIdSuscripcion(suscripcionType.getIdSuscripcion());
                        suscripcion.setNombre(suscripcionType.getNombre());
                        suscripcion.setPrecio((Double) Utils.parseNumber(
                                suscripcionType.getPrecio(), 3));
                        suscripcion.setMercado(suscripcionType.getMercado());
                        suscripcion.setFechaSuscripcion(DateHelper.parseDate(
                                suscripcionType.getFechaSuscripcion(),
                                DateHelper.FORMAT_ddMMyyyy_SLASH));
                        suscripciones.add(suscripcion);
                    }
                    resultado.setSuscripciones(suscripciones);
                }

            } catch (Exception e) {
                LOGGER.error("Exception caught on Service response: "
                        + "obtenerSuscripcionesActivas", e);
                LOGGER.error( new DAOException(e));
            }

        }
        else {
            LOGGER.error("obtenerSuscripcionesActivas: Service error code received: "
                    + codigoRespuesta + " - " + descripcionRespuesta);
            LOGGER.error( new ServiceException(codigoRespuesta, descripcionRespuesta));
        }
        
        return resultado;
        
    }
    
    /**
     * Consulta el estado de roaming del usuario.
     * @param msisdn
     * @return {@link ResultadoValidarRoamingBean} con el estado de roaming del usuario
     * @throws DAOException
     * @throws ServiceException
     */
    public ResultadoValidarRoamingBean validarRoaming(String msisdn) 
        throws DAOException, ServiceException {
        
        ResultadoValidarRoamingBean resultado = new ResultadoValidarRoamingBean();        
        
        AdminSuscripcionService port = null;
        LOGGER.info("Instanciando el port " + AdminSuscripcionService.class);
        try {
            port = (AdminSuscripcionService) WebServiceLocator.getInstance().getPort(
                    AdminSuscripcionService_Service.class, AdminSuscripcionService.class);
        } catch (WebServiceLocatorException e) {
            LOGGER.error("Error al inicializar el Port " + AdminSuscripcionService.class, e);
            LOGGER.error( new DAOException(e));
        }
        

        ValidarRoamingType request = new ValidarRoamingType();
        ResultadoValidarRoamingType response = null;

        try {

            LOGGER.info("Configurando Datos de la peticion");
            request.setMsisdn(msisdn);
            

            LOGGER.info("Invocando servicio");
            response = port.validarRoaming(request);

        } catch (Exception e) {
            LOGGER.error("Exception caught on Service invocation: validarRoaming", e);
            LOGGER.error( new DAOException(e));
        }

        String codigoRespuesta = response.getRespuesta().getCodigo();
        String descripcionRespuesta = response.getRespuesta().getDescripcion();

        LOGGER.info("codigoRespuesta " + codigoRespuesta);
        LOGGER.info("descripcionRespuesta " + descripcionRespuesta);

        if (Utils.isEmptyString(codigoRespuesta)) {
            LOGGER.error( new DAOException("validarRoaming: Servicio no responde "
                    + "con codigoRespuesta"));
        }

        if (CODIGO_RESPUESTA_OK.equals(codigoRespuesta)) {

            try {

                resultado.setEnRoaming(Boolean.parseBoolean(response.getEnRoaming()));

            } catch (Exception e) {
                LOGGER.error("Exception caught on Service response: "
                        + "validarRoaming", e);
                LOGGER.error( new DAOException(e));
            }

        }
        else {
            LOGGER.error("validarRoaming: Service error code received: "
                    + codigoRespuesta + " - " + descripcionRespuesta);
            LOGGER.error( new ServiceException(codigoRespuesta, descripcionRespuesta));
        }
        
        return resultado;
    }
    
    /**
     * Obtiene la compatibilidad del movil del usuario para video y streaming
     * @param msisdn numero del movil
     * @return {@link ResultadoObtenerCompatibilidadBean} con la info de compatibilidad
     * @throws DAOException
     * @throws ServiceException
     */
    public ResultadoObtenerCompatibilidadBean obtenerCompatibilidad (String msisdn)
        throws DAOException, ServiceException {
        
        ResultadoObtenerCompatibilidadBean resultado = new ResultadoObtenerCompatibilidadBean();
        

        AdminSuscripcionService port = null;
        LOGGER.info("Instanciando el port " + AdminSuscripcionService.class);
        try {
            port = (AdminSuscripcionService) WebServiceLocator.getInstance().getPort(
                    AdminSuscripcionService_Service.class, AdminSuscripcionService.class);
        } catch (WebServiceLocatorException e) {
            LOGGER.error("Error al inicializar el Port " + AdminSuscripcionService.class, e);
            LOGGER.error( new DAOException(e));
        }

        
        ObtenerCompatibilidadType request = new ObtenerCompatibilidadType();
        ResultadoObtenerCompatibilidadType response = null;

        try {

            LOGGER.info("Configurando Datos de la peticion");
            request.setMsisdn(msisdn);
            

            LOGGER.info("Invocando servicio");
            response = port.obtenerCompatibilidad(request);

        } catch (Exception e) {
            LOGGER.error("Exception caught on Service invocation: obtenerCompatibilidad", e);
            LOGGER.error( new DAOException(e));
        }

        String codigoRespuesta = response.getRespuesta().getCodigo();
        String descripcionRespuesta = response.getRespuesta().getDescripcion();

        LOGGER.info("codigoRespuesta " + codigoRespuesta);
        LOGGER.info("descripcionRespuesta " + descripcionRespuesta);

        if (Utils.isEmptyString(codigoRespuesta)) {
            LOGGER.error( new DAOException("obtenerCompatibilidad: Servicio no responde "
                    + "con codigoRespuesta"));
        }

        if (CODIGO_RESPUESTA_OK.equals(codigoRespuesta)) {

            try {

                resultado.setStreaming(Integer.parseInt(response.getStreaming()));
                resultado.setVideo(Integer.parseInt(response.getVideo()));

            } catch (Exception e) {
                LOGGER.error("Exception caught on Service response: "
                        + "obtenerCompatibilidad", e);
                LOGGER.error( new DAOException(e));
            }

        }
        else {
            LOGGER.error("obtenerCompatibilidad: Service error code received: "
                    + codigoRespuesta + " - " + descripcionRespuesta);
            LOGGER.error( new ServiceException(codigoRespuesta, descripcionRespuesta));
        }
        
        return resultado;
    }
    
    /**
     * Activa la suscripcion <CODE>idSuscripcion</CODE> para el usuario con numero <CODE>msisdn</CODE>
     * @param idSuscripcion
     * @param msisdn numero del movil
     * @return {@link ResultadoSuscribirCDFBean} con el resultado de la operacion
     * @throws DAOException
     * @throws ServiceException
     */
    public ResultadoSuscribirCDFBean suscribirCDF (String idSuscripcion, String msisdn)
        throws DAOException, ServiceException {
        
        ResultadoSuscribirCDFBean resultado = new ResultadoSuscribirCDFBean();
        
        

        AdminSuscripcionService port = null;
        LOGGER.info("Instanciando el port " + AdminSuscripcionService.class);
        try {
            port = (AdminSuscripcionService) WebServiceLocator.getInstance().getPort(
                    AdminSuscripcionService_Service.class, AdminSuscripcionService.class);
        } catch (WebServiceLocatorException e) {
            LOGGER.error("Error al inicializar el Port " + AdminSuscripcionService.class, e);
            LOGGER.error( new DAOException(e));
        }

        SuscribirCDFType request = new SuscribirCDFType();
        ResultadoSuscribirCDFType response = null;

        try {

            LOGGER.info("Configurando Datos de la peticion");
            request.setAction(MiEntelProperties.getProperty(
                "ishop.suscripcioncdf.suscribirCDF.action"));
            request.setAppTag(MiEntelProperties.getProperty(
                "ishop.suscripcioncdf.suscribirCDF.appTag"));
            request.setIdSuscripcion(idSuscripcion);
            request.setMsisdn(msisdn);

            LOGGER.info("Invocando servicio");
            response = port.suscribirCDF(request);

        } catch (Exception e) {
            LOGGER.error("Exception caught on Service invocation: suscribirCDF", e);
            LOGGER.error( new DAOException(e));
        }

        String codigoRespuesta = response.getRespuesta().getCodigo();
        String descripcionRespuesta = response.getRespuesta().getDescripcion();

        LOGGER.info("codigoRespuesta " + codigoRespuesta);
        LOGGER.info("descripcionRespuesta " + descripcionRespuesta);

        if (Utils.isEmptyString(codigoRespuesta)) {
            LOGGER.error( new DAOException("suscribirCDF: Servicio no responde "
                    + "con codigoRespuesta"));
        }

        if (CODIGO_RESPUESTA_OK.equals(codigoRespuesta)) {

            try {
                
                RespuestaBean respuesta = new RespuestaBean();
                respuesta.setCodigo(codigoRespuesta);
                respuesta.setDescripcion(descripcionRespuesta);
                resultado.setRespuesta(respuesta);

            } catch (Exception e) {
                LOGGER.error("Exception caught on Service response: "
                        + "suscribirCDF", e);
                LOGGER.error( new DAOException(e));
            }

        }
        else {
            LOGGER.error("suscribirCDF: Service error code received: "
                    + codigoRespuesta + " - " + descripcionRespuesta);
            LOGGER.error( new ServiceException(codigoRespuesta, descripcionRespuesta));
        }

        return resultado;
        
    }
    
    /**
     * Inactiva la suscripcion <CODE>idSuscripcion</CODE> para el usuario con numero <CODE>msisdn</CODE>
     * @param idSuscripcion
     * @param msisdn numero del movil
     * @return {@link ResultadoDesuscribirCDFBean} con el resultado de la operacion
     * @throws DAOException
     * @throws ServiceException
     */
    public ResultadoDesuscribirCDFBean deSuscribirCDF (String idSuscripcion, String msisdn)
        throws DAOException, ServiceException {
        
        ResultadoDesuscribirCDFBean resultado = new ResultadoDesuscribirCDFBean();
        
    
        AdminSuscripcionService port = null;
        LOGGER.info("Instanciando el port " + AdminSuscripcionService.class);
        try {
            port = (AdminSuscripcionService) WebServiceLocator.getInstance().getPort(
                    AdminSuscripcionService_Service.class, AdminSuscripcionService.class);
        } catch (WebServiceLocatorException e) {
            LOGGER.error("Error al inicializar el Port " + AdminSuscripcionService.class, e);
            LOGGER.error( new DAOException(e));
        }
    
        DesuscribirCDFType request = new DesuscribirCDFType();
        ResultadoDesuscribirCDFType response = null;
    
        try {
    
            LOGGER.info("Configurando Datos de la peticion");
            request.setAction(MiEntelProperties.getProperty(
                "ishop.suscripcioncdf.deSuscribirCDF.action"));
            request.setAppTag(MiEntelProperties.getProperty(
                "ishop.suscripcioncdf.suscribirCDF.appTag"));
            request.setIdSuscripcion(idSuscripcion);
            request.setMsisdn(msisdn);
    
            LOGGER.info("Invocando servicio");
            response = port.desuscribirCDF(request);
    
        } catch (Exception e) {
            LOGGER.error("Exception caught on Service invocation: desuscribirCDF", e);
            LOGGER.error( new DAOException(e));
        }
    
        String codigoRespuesta = response.getRespuesta().getCodigo();
        String descripcionRespuesta = response.getRespuesta().getDescripcion();
    
        LOGGER.info("codigoRespuesta " + codigoRespuesta);
        LOGGER.info("descripcionRespuesta " + descripcionRespuesta);
    
        if (Utils.isEmptyString(codigoRespuesta)) {
            LOGGER.error( new DAOException("desuscribirCDF: Servicio no responde "
                    + "con codigoRespuesta"));
        }
    
        if (CODIGO_RESPUESTA_OK.equals(codigoRespuesta)) {
    
            try {
                
                RespuestaBean respuesta = new RespuestaBean();
                respuesta.setCodigo(codigoRespuesta);
                respuesta.setDescripcion(descripcionRespuesta);
                resultado.setRespuesta(respuesta);
    
            } catch (Exception e) {
                LOGGER.error("Exception caught on Service response: "
                        + "desuscribirCDF", e);
                LOGGER.error( new DAOException(e));
            }
    
        }
        else {
            LOGGER.error("desuscribirCDF: Service error code received: "
                    + codigoRespuesta + " - " + descripcionRespuesta);
            LOGGER.error( new ServiceException(codigoRespuesta, descripcionRespuesta));
        }
    
        return resultado;
        
    }
    
    /**
     * Realiza una orquestacion de suscripciones CDF para el usuario con numero <CODE>msisdn</CODE>.
     * Esta orquestacion verifica, para cada suscripcion del catalogo, si el usuario la tiene activa
     * o no; tambien verifica que el movil del usuario sea compatible con el tipo de suscripcion. Al
     * final, se devuelve una lista de suscripciones con la info de compatibilidad y activacion. 
     * @param msisdn
     * @return {@link ResultadoObtenerSuscripcionCDFOrqBean} resultado de la orquestacion
     * @throws DAOException
     * @throws ServiceException
     */
    public ResultadoObtenerSuscripcionCDFOrqBean orquestarSuscripcionesCDF(String msisdn)
        throws DAOException, ServiceException {
        
        ResultadoObtenerSuscripcionCDFOrqBean resultado = new ResultadoObtenerSuscripcionCDFOrqBean();
        

        AdminSuscripcionService port = null;
        LOGGER.info("Instanciando el port " + AdminSuscripcionService.class);
        try {
            port = (AdminSuscripcionService) WebServiceLocator.getInstance().getPort(
                    AdminSuscripcionService_Service.class, AdminSuscripcionService.class);
        } catch (WebServiceLocatorException e) {
            LOGGER.error("Error al inicializar el Port " + AdminSuscripcionService.class, e);
            LOGGER.error( new DAOException(e));
        }
        

        ObtenerSuscripcionCDFOrqType request = new ObtenerSuscripcionCDFOrqType();
        ResultadoObtenerSuscripcionCDFOrqType response = null;

        try {

            LOGGER.info("Configurando Datos de la peticion");
            request.setMsisdn(msisdn);
            

            LOGGER.info("Invocando servicio");
            response = port.obtenerSuscripcionCDFOrq(request);

        } catch (Exception e) {
            LOGGER.error("Exception caught on Service invocation: " + "obtenerSuscripcionCDFOrq",
                    e);
            LOGGER.error( new DAOException(e));
        }

        String codigoRespuesta = response.getRespuesta().getCodigo();
        String descripcionRespuesta = response.getRespuesta().getDescripcion();
        
        /*
         * Necesitamos esta informacion en el controller para determinar cual
         * de las diferentes respuestas aceptables se presento
         */
        resultado.getRespuesta().setCodigo(codigoRespuesta);
        resultado.getRespuesta().setDescripcion(descripcionRespuesta);

        LOGGER.info("codigoRespuesta " + codigoRespuesta);
        LOGGER.info("descripcionRespuesta " + descripcionRespuesta);

        if (Utils.isEmptyString(codigoRespuesta)) {
            LOGGER.error( new DAOException("obtenerSuscripcionCDFOrq: Servicio no responde "
                    + "con codigoRespuesta"));
        }

        if (CODIGO_RESPUESTA_OK.equals(codigoRespuesta) || 
        		CODIGO_ERROR_EN_ROAMING_SIN_3G.equals(codigoRespuesta) ||
        		CODIGO_ERROR_EN_ROAMING.equals(codigoRespuesta) ||
        		CODIGO_ERROR_SIN_3G.equals(codigoRespuesta)) {

            try {
                List<SuscripcionCDFOrqBean> suscripciones = new LinkedList<SuscripcionCDFOrqBean>(); 
                List<SuscripcionCDFOrqType> suscripcionesType = response.getSuscripcion();
                if(suscripcionesType == null || suscripcionesType.isEmpty()){
                    LOGGER.info("No hay suscripciones CDF disponibles");
                }else {
                    for(SuscripcionCDFOrqType suscripcionType : suscripcionesType){
                        SuscripcionCDFOrqBean suscripcion = new SuscripcionCDFOrqBean();
                        suscripcion.setIdSuscripcion(suscripcionType.getIdSuscripcion());
                        suscripcion.setNombre(suscripcionType.getNombre());
                        suscripcion.setPrecio((Double)Utils.
                                        parseNumber(suscripcionType.getPrecio(), 3));
                        suscripcion.setDuracion((Integer)Utils.
                                        parseNumber(suscripcionType.getDuracion(), 1));
                        suscripcion.setMercado(suscripcionType.getMercado());
                        if(Utils.isNotEmptyString(suscripcionType.getFechaSuscripcion()) &&
                            //Si no existe, el servicio devuelve la fecha por defecto
                            !suscripcionType.getFechaSuscripcion().equals(FECHA_ACTIVACION_DEFAULT)){
                            suscripcion.setFechaSuscripcion(DateHelper.parseDate(
                            suscripcionType.getFechaSuscripcion(), DateHelper.FORMAT_yyyyMMddhhmmss));
                        }
                        suscripcion.setActiva(suscripcionType.getEstado());
                        suscripcion.setCompatibilidad(suscripcionType.getCompatibilidad().equals("1"));
                        suscripciones.add(suscripcion);
                    }
                    resultado.setSuscripciones(suscripciones);
                }

            } catch (Exception e) {
                LOGGER.error("Exception caught on Service response: "
                        + "obtenerSuscripcionCDFOrq", e);
                LOGGER.error( new DAOException(e));
            }

        }
        else {
            LOGGER.error("obtenerSuscripcionCDFOrq: Service error code received: "
                    + codigoRespuesta + " - " + descripcionRespuesta);
            LOGGER.error( new ServiceException(codigoRespuesta, descripcionRespuesta));
        }
        
        return resultado;
        
    }

}
