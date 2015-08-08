/* Propiedad de Entel PCS. Todos los derechos reservados */
package com.epcs.ishop.suscripciones.dao;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import com.epcs.administracion.suscripciones.AdminSuscripcionService;
import com.epcs.administracion.suscripciones.AdminSuscripcionService_Service;
import com.epcs.administracion.suscripciones.types.DesuscribirType;
import com.epcs.administracion.suscripciones.types.HistoricoSuscripcionType;
import com.epcs.administracion.suscripciones.types.HistoricoType;
import com.epcs.administracion.suscripciones.types.ObtenerHistoricoSuscripcionesType;
import com.epcs.administracion.suscripciones.types.ObtenerSuscripcionesType;
import com.epcs.administracion.suscripciones.types.ObtenerSuscripcionesUsuarioOrqType;
import com.epcs.administracion.suscripciones.types.ObtenerSuscripcionesUsuarioType;
import com.epcs.administracion.suscripciones.types.ResultadoDesuscribirType;
import com.epcs.administracion.suscripciones.types.ResultadoObtenerHistoricoSuscripcionesType;
import com.epcs.administracion.suscripciones.types.ResultadoObtenerSuscripcionesType;
import com.epcs.administracion.suscripciones.types.ResultadoObtenerSuscripcionesUsuarioOrqType;
import com.epcs.administracion.suscripciones.types.ResultadoObtenerSuscripcionesUsuarioType;
import com.epcs.administracion.suscripciones.types.ResultadoSuscribirType;
import com.epcs.administracion.suscripciones.types.SuscribirType;
import com.epcs.administracion.suscripciones.types.SuscripcionType;
import com.epcs.administracion.suscripciones.types.SuscripcionUsuarioOrqType;
import com.epcs.administracion.suscripciones.types.SuscripcionUsuarioType;
import com.epcs.bean.ResumenHistoricoSuscripcionesBean;
import com.epcs.bean.ResumenSuscripcionesBean;
import com.epcs.bean.ResumenSuscripcionesOrqBean;
import com.epcs.recursoti.configuracion.MiEntelProperties;
import com.epcs.recursoti.configuracion.Utils;
import com.epcs.recursoti.configuracion.locator.WebServiceLocator;
import com.epcs.recursoti.configuracion.locator.WebServiceLocatorException;
import com.epcs.recursoti.excepcion.DAOException;
import com.epcs.recursoti.exception.ServiceException;

/**
 * @author zmanotas (I2B) en nombre de Absalon Opazo (Atencion al Cliente, EntelPcs)
 *
 */
public class IShopSuscripcionesDAO {

    /**
     * Logger para IShopSuscripcionesDAO
     */
    private static final Logger LOGGER = Logger
            .getLogger(IShopSuscripcionesDAO.class);
    public static final String CODIGO_RESPUESTA_OK = MiEntelProperties
            .getProperty("servicios.codigoRespuesta.exito");
    public static final String VIA = MiEntelProperties
            .getProperty("iShop.suscripciones.via");
    
    public void desuscribir(String msisdn, String idSuscripcion) throws DAOException, ServiceException {
        AdminSuscripcionService port;
        LOGGER.info("Instanciando el port " + AdminSuscripcionService.class);
        try {
            port = (AdminSuscripcionService) WebServiceLocator.getInstance().getPort(
                    AdminSuscripcionService_Service.class, AdminSuscripcionService.class);            

            DesuscribirType request = new DesuscribirType();
            ResultadoDesuscribirType response = null;

            try {
                LOGGER.info("Configurando Datos de la peticion");
                
                request.setMsisdn(msisdn);
                request.setIdSuscripcion(idSuscripcion);
                request.setVia(VIA);

                LOGGER.info("Invocando servicio");
                response = port.desuscribir(request);
            } catch (Exception e) {
                LOGGER.error("Exception caught on Service invocation: "
                        + "desuscribir", e);
                LOGGER.error( new DAOException(e));
            }

            String codigoRespuesta = response.getRespuesta().getCodigo();
            String descripcionRespuesta = response.getRespuesta()
                    .getDescripcion();
            
            LOGGER.info("codigoRespuesta " + codigoRespuesta);
            LOGGER.info("descripcionRespuesta " + descripcionRespuesta);

            if (Utils.isEmptyString(codigoRespuesta)) {
                LOGGER.error( new DAOException("desuscribir: Servicio no responde "
                        + "con codigoRespuesta"));
            }

            if (CODIGO_RESPUESTA_OK.equals(codigoRespuesta)) {
                try {
                    return;
                } catch (Exception e) {
                    LOGGER.error("Exception caught on Service response: "
                            + "desuscribir", e);
                    LOGGER.error( new DAOException(e));
                }
            }
            else {
                LOGGER.error("desuscribir: Service error code received: "
                        + codigoRespuesta + " - " + descripcionRespuesta);
                LOGGER.error( new ServiceException(codigoRespuesta,
                        descripcionRespuesta));
            }
        } catch (WebServiceLocatorException e) {
            LOGGER.error("Error al inicializar el Port " + AdminSuscripcionService.class, e);
            LOGGER.error( new DAOException(e));
        }        
    }
    
    public List<ResumenHistoricoSuscripcionesBean> obtenerHistoricoSuscripciones(String msisdn, String fechaDesde, String fechaHasta, String idSuscripcion) throws DAOException, ServiceException {
        AdminSuscripcionService port;
        LOGGER.info("Instanciando el port " + AdminSuscripcionService.class);
        try {
            port = (AdminSuscripcionService) WebServiceLocator.getInstance().getPort(
                    AdminSuscripcionService_Service.class, AdminSuscripcionService.class);

            ObtenerHistoricoSuscripcionesType request = new ObtenerHistoricoSuscripcionesType();
            ResultadoObtenerHistoricoSuscripcionesType response = null;

            try {
                LOGGER.info("Configurando Datos de la peticion");
                
                request.setMsisdn(msisdn);
                request.setFechaDesde(fechaDesde);
                request.setFechaHasta(fechaHasta);
                request.setIdSuscripcion(idSuscripcion);

                LOGGER.info("Invocando servicio");
                response = port.obtenerHistoricoSuscripciones(request);

            } catch (Exception e) {
                LOGGER.error("Exception caught on Service invocation: "
                        + "obtenerHistoricoSuscripciones", e);
                LOGGER.error( new DAOException(e));
            }

            String codigoRespuesta = response.getRespuesta().getCodigo();
            String descripcionRespuesta = response.getRespuesta()
                    .getDescripcion();

            LOGGER.info("codigoRespuesta " + codigoRespuesta);
            LOGGER.info("descripcionRespuesta " + descripcionRespuesta);

            if (Utils.isEmptyString(codigoRespuesta)) {
                LOGGER.error( new DAOException("obtenerHistoricoSuscripciones: Servicio no responde "
                        + "con codigoRespuesta"));
            }

            if (CODIGO_RESPUESTA_OK.equals(codigoRespuesta)) {
                List<ResumenHistoricoSuscripcionesBean> resumen = new LinkedList<ResumenHistoricoSuscripcionesBean>();
                try {
                    for (HistoricoSuscripcionType historico : response.getSuscripciones()) {
                        ResumenHistoricoSuscripcionesBean bean = new ResumenHistoricoSuscripcionesBean();
                        bean.setIdSuscripcion(historico.getIdSuscripcion());
                        bean.setArtista(historico.getArtista());
                        bean.setDescripcion(historico.getDescripcion());
                        bean.setFecha(historico.getFecha());
                        bean.setTipoContenido(historico.getTipoContenido());
                        resumen.add(bean);
                    }
                    return resumen;
                } catch (Exception e) {
                    LOGGER.error("Exception caught on Service response: "
                            + "obtenerHistoricoSuscripciones", e);
                    LOGGER.error( new DAOException(e));
                }
            }
            else {
                LOGGER.error("obtenerHistoricoSuscripciones: Service error code received: "
                        + codigoRespuesta + " - " + descripcionRespuesta);
                LOGGER.error( new ServiceException(codigoRespuesta,
                        descripcionRespuesta));
            }
        } catch (WebServiceLocatorException e) {
            LOGGER.error("Error al inicializar el Port " + AdminSuscripcionService.class, e);
            LOGGER.error( new DAOException(e));
        } 
        return new ArrayList<ResumenHistoricoSuscripcionesBean>();
    }
    
    public List<ResumenSuscripcionesBean> obtenerSuscripciones() throws DAOException, ServiceException {
        AdminSuscripcionService port;
        LOGGER.info("Instanciando el port " + AdminSuscripcionService.class);
        try {
            port = (AdminSuscripcionService) WebServiceLocator.getInstance().getPort(
                    AdminSuscripcionService_Service.class, AdminSuscripcionService.class);

            ObtenerSuscripcionesType request = new ObtenerSuscripcionesType();
            ResultadoObtenerSuscripcionesType response = null;

            try {
                LOGGER.info("Configurando Datos de la peticion");
                
                request.setVia(VIA);

                LOGGER.info("Invocando servicio");
                response = port.obtenerSuscripciones(request);                
            } catch (Exception e) {
                LOGGER.error("Exception caught on Service invocation: "
                        + "obtenerSuscripciones", e);
                LOGGER.error( new DAOException(e));
            }

            String codigoRespuesta = response.getRespuesta().getCodigo();
            String descripcionRespuesta = response.getRespuesta()
                    .getDescripcion();

            LOGGER.info("codigoRespuesta " + codigoRespuesta);
            LOGGER.info("descripcionRespuesta " + descripcionRespuesta);

            if (Utils.isEmptyString(codigoRespuesta)) {
                LOGGER.error( new DAOException("obtenerSuscripciones: Servicio no responde "
                        + "con codigoRespuesta"));
            }

            if (CODIGO_RESPUESTA_OK.equals(codigoRespuesta)) {
                List<ResumenSuscripcionesBean> resumen = new LinkedList<ResumenSuscripcionesBean>();
                try {
                    for (SuscripcionType suscripcion : response.getSuscripciones()) {
                        ResumenSuscripcionesBean bean = new ResumenSuscripcionesBean();
                        bean.setIdSuscripcion(suscripcion.getIdSuscripcion());
                        bean.setMaxDescargas(suscripcion.getMaxDescargas());
                        bean.setNombre(suscripcion.getNombre());
                        bean.setPeriodoHoras(suscripcion.getPeriodoHoras());
                        bean.setPrecio(suscripcion.getPrecio());
                        resumen.add(bean);
                    }
                    return resumen;
                } catch (Exception e) {
                    LOGGER.error("Exception caught on Service response: "
                            + "obtenerSuscripciones", e);
                    LOGGER.error( new DAOException(e));
                }
            }
            else {
                LOGGER.error("obtenerSuscripciones: Service error code received: "
                        + codigoRespuesta + " - " + descripcionRespuesta);
                LOGGER.error( new ServiceException(codigoRespuesta,
                        descripcionRespuesta));
            }
        } catch (WebServiceLocatorException e) {
            LOGGER.error("Error al inicializar el Port " + AdminSuscripcionService.class, e);
            LOGGER.error( new DAOException(e));
        } 
        return new ArrayList<ResumenSuscripcionesBean>();
    }
    
    public List<ResumenSuscripcionesBean> obtenerSuscripcionesUsuario(String msisdn) throws DAOException, ServiceException {
        AdminSuscripcionService port;
        LOGGER.info("Instanciando el port " + AdminSuscripcionService.class);
        try {
            port = (AdminSuscripcionService) WebServiceLocator.getInstance().getPort(
                    AdminSuscripcionService_Service.class, AdminSuscripcionService.class);

            ObtenerSuscripcionesUsuarioType request = new ObtenerSuscripcionesUsuarioType();
            ResultadoObtenerSuscripcionesUsuarioType response = null;

            try {
                LOGGER.info("Configurando Datos de la peticion");
                
                request.setMsisdn(msisdn);

                LOGGER.info("Invocando servicio");
                response = port.obtenerSuscripcionesUsuario(request);
            } catch (Exception e) {
                LOGGER.error("Exception caught on Service invocation: "
                        + "obtenerSuscripcionesUsuario", e);
                LOGGER.error( new DAOException(e));
            }

            String codigoRespuesta = response.getRespuesta().getCodigo();
            String descripcionRespuesta = response.getRespuesta()
                    .getDescripcion();

            LOGGER.info("codigoRespuesta " + codigoRespuesta);
            LOGGER.info("descripcionRespuesta " + descripcionRespuesta);

            if (Utils.isEmptyString(codigoRespuesta)) {
                LOGGER.error( new DAOException("obtenerSuscripcionesUsuario: Servicio no responde "
                        + "con codigoRespuesta"));
            }

            if (CODIGO_RESPUESTA_OK.equals(codigoRespuesta)) {
                List<ResumenSuscripcionesBean> resumen = new LinkedList<ResumenSuscripcionesBean>();
                try {
                    for (SuscripcionUsuarioType suscripcion : response.getSuscripciones()) {
                        ResumenSuscripcionesBean bean = new ResumenSuscripcionesBean();
                        bean.setIdSuscripcion(suscripcion.getIdSuscripcion());
                        bean.setMaxDescargas(suscripcion.getMaxDescargas());
                        bean.setNombre(suscripcion.getNombre());
                        bean.setPeriodoHoras(suscripcion.getPeriodoHoras());
                        bean.setPrecio(suscripcion.getPrecio());
                        bean.setFechaActivacion(suscripcion.getFechaActivacion());
                        bean.setEstado(suscripcion.getEstado());
                        resumen.add(bean);
                    }
                    return resumen;
                } catch (Exception e) {
                    LOGGER.error("Exception caught on Service response: "
                            + "obtenerSuscripcionesUsuario", e);
                    LOGGER.error( new DAOException(e));
                }

            }
            else {
                LOGGER.error("obtenerSuscripcionesUsuario: Service error code received: "
                        + codigoRespuesta + " - " + descripcionRespuesta);
                LOGGER.error( new ServiceException(codigoRespuesta,
                        descripcionRespuesta));
            }
        } catch (WebServiceLocatorException e) {
            LOGGER.error("Error al inicializar el Port " + AdminSuscripcionService.class, e);
            LOGGER.error( new DAOException(e));
        } 
        return new ArrayList<ResumenSuscripcionesBean>();
    }
    
    public void suscribir(String msisdn, String idSuscripcion) throws DAOException, ServiceException {
        AdminSuscripcionService port;
        LOGGER.info("Instanciando el port " + AdminSuscripcionService.class);
        try {
            port = (AdminSuscripcionService) WebServiceLocator.getInstance().getPort(
                    AdminSuscripcionService_Service.class, AdminSuscripcionService.class);            

            SuscribirType request = new SuscribirType();
            ResultadoSuscribirType response = null;

            try {
                LOGGER.info("Configurando Datos de la peticion");
                
                request.setMsisdn(msisdn);
                request.setIdSuscripcion(idSuscripcion);
                request.setVia(VIA);

                LOGGER.info("Invocando servicio");
                response = port.suscribir(request);
            } catch (Exception e) {
                LOGGER.error("Exception caught on Service invocation: "
                        + "suscribir", e);
                LOGGER.error( new DAOException(e));
            }

            String codigoRespuesta = response.getRespuesta().getCodigo();
            String descripcionRespuesta = response.getRespuesta()
                    .getDescripcion();

            LOGGER.info("codigoRespuesta " + codigoRespuesta);
            LOGGER.info("descripcionRespuesta " + descripcionRespuesta);

            if (Utils.isEmptyString(codigoRespuesta)) {
                LOGGER.error( new DAOException("suscribir: Servicio no responde "
                        + "con codigoRespuesta"));
            }

            if (CODIGO_RESPUESTA_OK.equals(codigoRespuesta)) {
                try {
                    return;
                } catch (Exception e) {
                    LOGGER.error("Exception caught on Service response: "
                            + "suscribir", e);
                    LOGGER.error( new DAOException(e));
                }
            }
            else {
                LOGGER.error("suscribir: Service error code received: "
                        + codigoRespuesta + " - " + descripcionRespuesta);
                LOGGER.error( new ServiceException(codigoRespuesta,
                        descripcionRespuesta));
            }
        } catch (WebServiceLocatorException e) {
            LOGGER.error("Error al inicializar el Port " + AdminSuscripcionService.class, e);
            LOGGER.error( new DAOException(e));
        }        
    }
    
    public List<ResumenSuscripcionesOrqBean> obtenerSuscripcionesUsuarioOrq(String msisdn) throws DAOException, ServiceException {
        AdminSuscripcionService port;
        LOGGER.info("Instanciando el port " + AdminSuscripcionService.class);
        try {
            port = (AdminSuscripcionService) WebServiceLocator.getInstance().getPort(
                    AdminSuscripcionService_Service.class, AdminSuscripcionService.class);

            ObtenerSuscripcionesUsuarioOrqType request = new ObtenerSuscripcionesUsuarioOrqType();
            ResultadoObtenerSuscripcionesUsuarioOrqType response = null;

            try {
                LOGGER.info("Configurando Datos de la peticion");
                
                request.setMsisdn(msisdn);
                
                LOGGER.info("Invocando servicio");
                response = port.obtenerSuscripcionesUsuarioOrq(request);
            } catch (Exception e) {
                LOGGER.error("Exception caught on Service invocation: "
                        + "obtenerSuscripcionesUsuarioOrq", e);
                LOGGER.error( new DAOException(e));
            }

            String codigoRespuesta = response.getRespuesta().getCodigo();
            String descripcionRespuesta = response.getRespuesta()
                    .getDescripcion();

            LOGGER.info("codigoRespuesta " + codigoRespuesta);
            LOGGER.info("descripcionRespuesta " + descripcionRespuesta);

            if (Utils.isEmptyString(codigoRespuesta)) {
                LOGGER.error( new DAOException("obtenerSuscripcionesUsuarioOrq: Servicio no responde "
                        + "con codigoRespuesta"));
            }

            if (CODIGO_RESPUESTA_OK.equals(codigoRespuesta)) {
                List<ResumenSuscripcionesOrqBean> resumen = new LinkedList<ResumenSuscripcionesOrqBean>();
                try {
                    for (SuscripcionUsuarioOrqType suscUsuario : response.getSuscripciones()) {
                        ResumenSuscripcionesOrqBean bean = new ResumenSuscripcionesOrqBean();
                        bean.setIdSuscripcion(suscUsuario.getIdSuscripcion());
                        bean.setNombre(suscUsuario.getNombre());
                        bean.setPrecio(suscUsuario.getPrecio());
                        bean.setMaxDescargas(suscUsuario.getMaxDescargas());
                        bean.setDescargasDisponibles(suscUsuario.getDescargasDisponibles());
                        bean.setDescargasHechas(suscUsuario.getDescargasHechas());
                        bean.setFechaActivacion(suscUsuario.getFechaActivacion());
                        bean.setActivado(Boolean.parseBoolean(suscUsuario.getActivado()));
                        bean.setEstado(suscUsuario.getEstado());
                        
                        for (HistoricoType historico : suscUsuario.getHistorico()) {
                            ResumenHistoricoSuscripcionesBean beanHistorico = new ResumenHistoricoSuscripcionesBean();
                            beanHistorico.setIdSuscripcion(historico.getIdSuscripcion());
                            beanHistorico.setArtista(historico.getArtista());
                            beanHistorico.setDescripcion(historico.getDescContenido());
                            beanHistorico.setFecha(historico.getFecha());
                            beanHistorico.setTipoContenido(historico.getTipoContenido());
                            bean.getHistorico().add(beanHistorico);
                        }
                        
                        resumen.add(bean);
                    }                    
                    return resumen;
                } catch (Exception e) {
                    LOGGER.error("Exception caught on Service response: "
                            + "obtenerSuscripcionesUsuarioOrq", e);
                    LOGGER.error( new DAOException(e));
                }
            }
            else {
                LOGGER.error("obtenerSuscripcionesUsuarioOrq: Service error code received: "
                        + codigoRespuesta + " - " + descripcionRespuesta);
                LOGGER.error( new ServiceException(codigoRespuesta,
                        descripcionRespuesta));
            }
        } catch (WebServiceLocatorException e) {
            LOGGER.error("Error al inicializar el Port " + AdminSuscripcionService.class, e);
            LOGGER.error( new DAOException(e));
        }

        return new ArrayList<ResumenSuscripcionesOrqBean>();
    }
}
