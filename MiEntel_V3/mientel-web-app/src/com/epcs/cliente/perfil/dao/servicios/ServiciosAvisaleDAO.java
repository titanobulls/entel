/* Propiedad de Entel Pcs. Todos los derechos reservados */
package com.epcs.cliente.perfil.dao.servicios;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.epcs.bean.ResultadoServicioBean;
import com.epcs.cliente.orden.ClienteOrdenService;
import com.epcs.cliente.orden.ClienteOrdenService_Service;
import com.epcs.cliente.orden.types.AdministrarServicioAvisaleOrqType;
import com.epcs.cliente.orden.types.ListaMsisdnType;
import com.epcs.cliente.orden.types.MsisdnAvisaleType;
import com.epcs.cliente.orden.types.ResultadoAdministrarServicioAvisaleOrqType;
import com.epcs.cliente.perfil.dao.AdministracionServiciosDAO;
import com.epcs.cliente.perfil.dao.util.ServiciosAvisalePropertiesHelper;
import com.epcs.recursoti.configuracion.MiEntelProperties;
import com.epcs.recursoti.configuracion.Utils;
import com.epcs.recursoti.configuracion.locator.WebServiceLocator;
import com.epcs.recursoti.configuracion.locator.WebServiceLocatorException;
import com.epcs.recursoti.excepcion.DAOException;
import com.epcs.recursoti.excepcion.ServiceException;

/**
 * Clase con metodos de administracion de servicios de Avisale.<br>
 * Esta clase no esta pensada para ser usada directamente, sino que como
 * Composicion de {@link AdministracionServiciosDAO}
 * 
 * @author jlopez (I2B) en nombre de Absalon Opazo (Atencion al Cliente,
 *         EntelPcs)
 * 
 */
public class ServiciosAvisaleDAO {

    private static final Logger LOGGER = Logger
            .getLogger(ServiciosAvisaleDAO.class);

    /**
     * Valor de exito para la respuesta a un servicio
     */
    public static final String CODIGO_RESPUESTA_OK = MiEntelProperties
            .getProperty("servicios.codigoRespuesta.exito");

    /**
     * 
     */
    public ServiciosAvisaleDAO() {
    }

    /**
     * Activa el servicio Avisale para el numero indicado en
     * <code>numeroPcs</code>.<br>
     * <code>tipoLista</code> indica si los numeros de <code>listaNumeros</code>
     * seran para la lista negra o blanca
     * 
     * @param numeroPcs
     * @param tipoLista
     * @param listaNumeros
     * @return {@link ResultadoServicioBean}
     * @throws DAOException
     * @throws ServiceException
     */
    public ResultadoServicioBean activarServicio(String numeroPcs,
            String tipoLista, List<String> listaNumeros) throws DAOException,
            ServiceException {
        
        /*
         * Activar en modo "solo avisar a" requiere al menos 1 numero
         * (caso de lista blanca)
         */
        if (tipoLista.equals(ServiciosAvisalePropertiesHelper
                .getTipoListaSoloAvisarA())) {
            if (listaNumeros.isEmpty()) {
                LOGGER.error( new DAOException(
                        "Lista de numeros avisale no puede ser vacia "
                                + "al activar en modo 'solo avisar a'"));
            }
        }
        
        /*
         * Cuando se activa sin numeros en listaNumeros, el tipo de lista
         * se cambia a 'default'
         */
        if(tipoLista.equals(ServiciosAvisalePropertiesHelper.getTipoListaAvisarTodos())) {
            if(listaNumeros.isEmpty()) {
                tipoLista = ServiciosAvisalePropertiesHelper.getTipoListaDefault();
            }
        }

        String accion = ServiciosAvisalePropertiesHelper.getActivar();
        return administrarServicioAvisale(numeroPcs, accion, tipoLista,
                listaNumeros);
    }

    /**
     * Desactiva el servicio avisale para el numero indicado en
     * <code>numeroPcs</code>
     * 
     * @param numeroPcs
     * @return {@link ResultadoServicioBean}
     * @throws DAOException
     * @throws ServiceException
     */
    public ResultadoServicioBean desactivarServicio(String numeroPcs)
            throws DAOException, ServiceException {
        String accion = ServiciosAvisalePropertiesHelper.getDesactivar();
        String tipoLista = ServiciosAvisalePropertiesHelper.getTipoListaDesactivar();
        List<String> listaNumeros = new ArrayList<String>();
        return administrarServicioAvisale(numeroPcs, accion, tipoLista,
                listaNumeros);
    }

    /**
     * Modifica la lista de numeros del tipo indicado <code>tipoLista</code> con
     * los valores inicados en <code>listaNumeros</code>
     * 
     * @param numeroPcs
     * @param tipoLista
     * @param listaNumerosPcs
     * @return {@link ResultadoServicioBean}
     * @throws DAOException
     * @throws ServiceException
     */
    public ResultadoServicioBean actualizarServicio(String numeroPcs,
            String tipoLista, List<String> listaNumeros) throws DAOException,
            ServiceException {
        
        /*
         * Actualizar en modo "solo avisar a" requiere al menos 1 numero
         * (caso de lista blanca)
         */
        if (tipoLista.equals(ServiciosAvisalePropertiesHelper
                .getTipoListaSoloAvisarA())) {
            if (listaNumeros.isEmpty()) {
                LOGGER.error( new DAOException(
                        "Lista de numeros avisale no puede ser vacia "
                                + "al modificar en modo 'solo avisar a'"));
            }
        }
        
        
        String accion = ServiciosAvisalePropertiesHelper.getModificar();
        return administrarServicioAvisale(numeroPcs, accion, tipoLista,
                listaNumeros);
    }

    /**
     * 
     * @param numeroPcs
     * @param accion
     * @param tipoLista
     * @param listaNumeros
     * @return
     * @throws DAOException
     * @throws ServiceException
     */
    public ResultadoServicioBean administrarServicioAvisale(String numeroPcs,
            String accion, String tipoLista, List<String> listaNumeros)
            throws DAOException, ServiceException {

        ClienteOrdenService port = null;
        LOGGER.info("Instanciando el port " + ClienteOrdenService.class);
        try {
            port = (ClienteOrdenService) WebServiceLocator.getInstance().getPort(
                    ClienteOrdenService_Service.class, ClienteOrdenService.class);
        } catch (WebServiceLocatorException e) {
            LOGGER.error("Error al inicializar el Port " + ClienteOrdenService.class, e);
            LOGGER.error( new DAOException(e));
        }

        AdministrarServicioAvisaleOrqType request = new AdministrarServicioAvisaleOrqType();
        ResultadoAdministrarServicioAvisaleOrqType response = null;

        try {

            LOGGER.info("Configurando Datos de la peticion");
            request.setMsisdn(numeroPcs);
            request.setAccion(accion);
            request.setParametros(tipoLista);
            
            for (String numero : listaNumeros) {
                ListaMsisdnType listaMsisdnType = new ListaMsisdnType();
                listaMsisdnType.setListamsisdn(numero);
                request.getListaMsisdn().add(listaMsisdnType);
            }
            
            if(LOGGER.isDebugEnabled()) {
                LOGGER.debug("Administracion de Servicio Avisale para: " + numeroPcs);
                LOGGER.debug("    accion : " + accion);
                LOGGER.debug("    tipoLista : " + tipoLista);
                LOGGER.debug("    listaNumeros : " + listaNumeros);
            }

            LOGGER.info("Invocando servicio");
            response = port.administrarServicioAvisaleOrq(request);

        } catch (Exception e) {
            LOGGER.error("Exception caught on Service invocation: " + "administrarServicioAvisaleOrq",
                    e);
            LOGGER.error( new DAOException(e));
        }

        String codigoRespuesta = response.getRespuesta().getCodigo();
        String descripcionRespuesta = response.getRespuesta().getDescripcion();

        LOGGER.info("codigoRespuesta " + codigoRespuesta);
        LOGGER.info("descripcionRespuesta " + descripcionRespuesta);

        if (Utils.isEmptyString(codigoRespuesta)) {
            LOGGER.error( new DAOException("administrarServicioAvisaleOrq: Servicio no responde "
                    + "con codigoRespuesta"));
        }

        if (CODIGO_RESPUESTA_OK.equals(codigoRespuesta)) {

            try {

                MsisdnAvisaleType avisale = response
                        .getAdministrarServicioAvisale();
                ResultadoServicioBean resultado = new ResultadoServicioBean();
                resultado.setNumeroPcs(avisale.getMsisdn());
                resultado.setCorrelativoTransferencia(avisale
                        .getIdTransaccion());
                
                if(LOGGER.isDebugEnabled()) {
                    LOGGER.debug("Administracion de Servicio Avisale realizada para: " + numeroPcs);
                    LOGGER.debug("    idTransaccion : " + avisale.getIdTransaccion());
                }

                return resultado;

            } catch (Exception e) {
                LOGGER.error("Exception caught on Service response: "
                        + "administrarServicioAvisaleOrq", e);
                LOGGER.error( new DAOException(e));
            }

        }
        else {
            LOGGER.info("administrarServicioAvisaleOrq: Service error code received: "
                    + codigoRespuesta + " - " + descripcionRespuesta);
            LOGGER.error( new ServiceException(codigoRespuesta, descripcionRespuesta));
        }
        return new ResultadoServicioBean();
    }

}
