/* Propiedad de Entel Pcs. Todos los derechos reservados */
package com.epcs.cliente.perfil.dao.servicios;

import org.apache.log4j.Logger;

import com.epcs.bean.AdminServicioBuzonBean;
import com.epcs.bean.ResultadoServicioBean;
import com.epcs.bean.RutBean;
import com.epcs.cliente.orden.ClienteOrdenService;
import com.epcs.cliente.orden.ClienteOrdenService_Service;
import com.epcs.cliente.orden.types.AdministrarServiciosBuzonOrqType;
import com.epcs.cliente.orden.types.ResultadoAdministrarServiciosBuzonOrqType;
import com.epcs.cliente.perfil.ClientePerfilService;
import com.epcs.cliente.perfil.ClientePerfilServicePortType;
import com.epcs.cliente.perfil.dao.AdministracionServiciosDAO;
import com.epcs.cliente.perfil.dao.util.ServiciosBuzonPropertiesHelper;
import com.epcs.cliente.perfil.types.ConsultarCorreoBuzonType;
import com.epcs.cliente.perfil.types.RespCorreoBuzonType;
import com.epcs.cliente.perfil.types.ResultadoConsultarCorreoBuzonType;
import com.epcs.recursoti.configuracion.MiEntelProperties;
import com.epcs.recursoti.configuracion.Utils;
import com.epcs.recursoti.configuracion.locator.WebServiceLocator;
import com.epcs.recursoti.configuracion.locator.WebServiceLocatorException;
import com.epcs.recursoti.excepcion.DAOException;
import com.epcs.recursoti.excepcion.ServiceException;

/**
 * Clase con metodos de administracion de servicios de Buzon .<br>
 * Esta clase no esta pensada para ser usada directamente, sino que como
 * Composicion de {@link AdministracionServiciosDAO}
 * 
 * @author jlopez (I2B) en nombre de Absalon Opazo (Atencion al Cliente,
 *         EntelPcs)
 * 
 */
public class ServiciosBuzonDAO {

    private static final Logger LOGGER = Logger
            .getLogger(ServiciosBuzonDAO.class);

    /**
     * Valor de exito para la respuesta a un servicio
     */
    public static final String CODIGO_RESPUESTA_OK = MiEntelProperties
            .getProperty("servicios.codigoRespuesta.exito");

    /**
     * 
     */
    public ServiciosBuzonDAO() {
    }

    /**
     * Realiza la activacion de un servicio de Buzon de Voz
     * 
     * @return {@link ResultadoServicioBean}
     * @throws DAOException
     * @throws ServiceException
     */
    public ResultadoServicioBean activarServicio(
            AdminServicioBuzonBean adminServicioBuzonBean) throws DAOException,
            ServiceException {
        String accion = ServiciosBuzonPropertiesHelper.getActivar();
        return this.administrarServiciosBuzon(adminServicioBuzonBean, accion);
    }

    /**
     * Realiza la desactivacion de un servicio Buzon de Voz
     * 
     * @return {@link ResultadoServicioBean}
     * @throws DAOException
     * @throws ServiceException
     */
    public ResultadoServicioBean desactivarServicio(
            AdminServicioBuzonBean adminServicioBuzonBean) throws DAOException,
            ServiceException {
        String accion = ServiciosBuzonPropertiesHelper.getDesactivar();
        return this.administrarServiciosBuzon(adminServicioBuzonBean, accion);
    }

    /**
     * Metodo para activacion/desactivacion de los servicios de Buzon de Voz<br>
     * <br>
     * Este metodo realiza la activacion/desactivacion del servicio.<br>
     * No se recomienda usar este metodo directamente, mas bien, emplear los
     * metodos de activar/desactivar especificos para cada servicio, puesto
     * estos resuelven los valores para el parametro <code>accion</code>
     * 
     * @param adminServicioBuzonBean
     *            {@link AdminServicioBuzonBean} con informacion de numero,
     *            email, rut y tipo de perfil del usuario
     * @param accion
     *            String que indica activar o desactivar
     * 
     * @see #activarServicio(AdminServicioBuzonBean)
     * @see #desactivarServicio(AdminServicioBuzonBean)
     * 
     * @return {@link ResultadoServicioBean} sin informacion de Correlativo de
     *         transaccion
     * 
     * @throws DAOException
     * @throws ServiceException
     */
    public ResultadoServicioBean administrarServiciosBuzon(
            AdminServicioBuzonBean adminServicioBuzonBean, String accion)
            throws DAOException, ServiceException {

        ClienteOrdenService port = null;
        LOGGER.info("Instanciando el port " + ClienteOrdenService.class);
        try {
            port = (ClienteOrdenService) WebServiceLocator.getInstance()
                    .getPort(ClienteOrdenService_Service.class,
                            ClienteOrdenService.class);
        } catch (WebServiceLocatorException e) {
            LOGGER.error("Error al inicializar el Port "
                    + ClienteOrdenService.class, e);
            LOGGER.error( new DAOException(e));
        }

        AdministrarServiciosBuzonOrqType request = new AdministrarServiciosBuzonOrqType();
        ResultadoAdministrarServiciosBuzonOrqType response = null;

        try {

            LOGGER.info("Configurando Datos de la peticion");
            request.setAccion(accion);
            request.setEmail(adminServicioBuzonBean.getEmail());
            request.setMsisdn(adminServicioBuzonBean.getNumeroPcs());
            request.setRutUsuario(adminServicioBuzonBean.getRutUsuario()
                    .getStringValue());
            request.setTipoPerfil(adminServicioBuzonBean.getTipoPerfil());

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Solicitud de administracion de servicio buzon para "
                        + adminServicioBuzonBean.getNumeroPcs());
                LOGGER.debug("    email : " + adminServicioBuzonBean.getEmail());
                LOGGER.debug("    rutUsuario : "
                        + adminServicioBuzonBean.getRutUsuario()
                                .getStringValue());
                LOGGER.debug("    tipoPerfil : "
                        + adminServicioBuzonBean.getTipoPerfil());
                LOGGER.debug("    accion : " + accion);
            }

            LOGGER.info("Invocando servicio");
            response = port.administrarServiciosBuzonOrq(request);

        } catch (Exception e) {
            LOGGER.error("Exception caught on Service invocation: "
                    + "administrarServiciosBuzonOrq", e);
            LOGGER.error( new DAOException(e));
        }

        String codigoRespuesta = response.getRespuesta().getCodigo();
        String descripcionRespuesta = response.getRespuesta().getDescripcion();

        LOGGER.info("codigoRespuesta " + codigoRespuesta);
        LOGGER.info("descripcionRespuesta " + descripcionRespuesta);

        if (Utils.isEmptyString(codigoRespuesta)) {
            LOGGER.error( new DAOException(
                    "administrarServiciosBuzonOrq: Servicio no responde "
                            + "con codigoRespuesta"));
        }

        if (CODIGO_RESPUESTA_OK.equals(codigoRespuesta)) {

            try {

                ResultadoServicioBean resultado = new ResultadoServicioBean();
                resultado.setNumeroPcs(adminServicioBuzonBean.getNumeroPcs());

                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug(" Administracion de servicio de Buzon "
                            + "realizada para : "
                            + adminServicioBuzonBean.getNumeroPcs());
                }

                return resultado;

            } catch (Exception e) {
                LOGGER.error("Exception caught on Service response: "
                        + "administrarServiciosBuzonOrq", e);
                LOGGER.error( new DAOException(e));
            }

        }
        else {
            LOGGER.info("administrarServiciosBuzonOrq: Service error code received: "
                    + codigoRespuesta + " - " + descripcionRespuesta);
            LOGGER.error( new ServiceException(codigoRespuesta, descripcionRespuesta));
        }
        return new ResultadoServicioBean();
    }

    /**
     * Obtiene el correo electronico resgistrado para el buzon premium via mail
     * para el numero y rut de usuario indicados
     * 
     * @param numeroPcs
     * @param rutUsuario
     * @return
     * @throws DAOException
     * @throws ServiceException
     */
    public String obtenerCorreoBuzonPremium(String numeroPcs, RutBean rutUsuario)
            throws DAOException, ServiceException {
        
        ClientePerfilServicePortType port = null;
        LOGGER.info("Instanciando el port " + ClientePerfilServicePortType.class);
        try {
            port = (ClientePerfilServicePortType) WebServiceLocator.getInstance().getPort(
                    ClientePerfilService.class, ClientePerfilServicePortType.class);
        } catch (WebServiceLocatorException e) {
            LOGGER.error("Error al inicializar el Port " + ClientePerfilServicePortType.class, e);
            LOGGER.error( new DAOException(e));
        }

        ConsultarCorreoBuzonType request = new ConsultarCorreoBuzonType();
        ResultadoConsultarCorreoBuzonType response = null;

        try {

            LOGGER.info("Configurando Datos de la peticion");
            request.setMsisdn(numeroPcs);
            request.setRutUsuario(rutUsuario.getStringValue());

            LOGGER.info("Invocando servicio");
            response = port.consultarCorreoBuzon(request);

        } catch (Exception e) {
            LOGGER.error("Exception caught on Service invocation: " + "consultarCorreoBuzon",
                    e);
            LOGGER.error( new DAOException(e));
        }

        String codigoRespuesta = response.getRespuesta().getCodigo();
        String descripcionRespuesta = response.getRespuesta().getDescripcion();

        LOGGER.info("codigoRespuesta " + codigoRespuesta);
        LOGGER.info("descripcionRespuesta " + descripcionRespuesta);

        if (Utils.isEmptyString(codigoRespuesta)) {
            LOGGER.error( new DAOException("consultarCorreoBuzon: Servicio no responde "
                    + "con codigoRespuesta"));
        }

        if (CODIGO_RESPUESTA_OK.equals(codigoRespuesta)) {

            try {
                
                RespCorreoBuzonType resp = response.getRespCorreoBuzon();
                return resp.getEmail();

            } catch (Exception e) {
                LOGGER.error("Exception caught on Service response: "
                        + "consultarCorreoBuzon", e);
                LOGGER.error( new DAOException(e));
            }

        }
        else {
            LOGGER.info("consultarCorreoBuzon: Service error code received: "
                    + codigoRespuesta + " - " + descripcionRespuesta);
            LOGGER.error( new ServiceException(codigoRespuesta, descripcionRespuesta));
        }
        return "";
        
    }
}
