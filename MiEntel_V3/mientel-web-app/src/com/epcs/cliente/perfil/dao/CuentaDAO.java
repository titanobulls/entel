/* Propiedad de Entel PCS. Todos los derechos reservados */
package com.epcs.cliente.perfil.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import com.epcs.bean.CiudadBean;
import com.epcs.bean.CodeDescBean;
import com.epcs.bean.ComunaBean;
import com.epcs.bean.CuentaClienteBean;
import com.epcs.bean.DatosUsuarioTitularBean;
import com.epcs.bean.DireccionBean;
import com.epcs.bean.InformacionTitularBean;
import com.epcs.bean.MsisdnAsociadoBean;
import com.epcs.bean.PerfilUsuarioBean;
import com.epcs.bean.PlanContratadoBean;
import com.epcs.bean.RegionBean;
import com.epcs.bean.RutBean;
import com.epcs.bean.UsuarioBean;
import com.epcs.bean.UsuarioSeleccionadoBean;
import com.epcs.cliente.contacto.ClienteContactoService;
import com.epcs.cliente.contacto.ClienteContactoServicePortType;
import com.epcs.cliente.contacto.types.CuentasClienteType;
import com.epcs.cliente.contacto.types.ObtenerCuentasClienteType;
import com.epcs.cliente.contacto.types.ObtenerDatosUsuarioBloqueoType;
import com.epcs.cliente.contacto.types.ResultadoObtenerCuentasClienteType;
import com.epcs.cliente.perfil.ClientePerfilService;
import com.epcs.cliente.perfil.ClientePerfilServicePortType;
import com.epcs.cliente.perfil.dao.util.SelectorCuentaHelper;
import com.epcs.cliente.perfil.types.ActualizarCorreoBuzonType;
import com.epcs.cliente.perfil.types.ActualizarDireccionPostalResponseType;
import com.epcs.cliente.perfil.types.ActualizarDireccionPostalType;
import com.epcs.cliente.perfil.types.ActualizarMisDatosType;
import com.epcs.cliente.perfil.types.ConsultarDireccionPostalType;
import com.epcs.cliente.perfil.types.ConsultarMisDatosType;
import com.epcs.cliente.perfil.types.ConsultarMisUsuariosType;
import com.epcs.cliente.perfil.types.ConsultarPerfilamientoType;
import com.epcs.cliente.perfil.types.ConsultarPlanContratadoPPType;
import com.epcs.cliente.perfil.types.ConsultarSelectorCuentaType;
import com.epcs.cliente.perfil.types.ConsultarUsuarioBuicType;
import com.epcs.cliente.perfil.types.DetalleMsisdnType;
import com.epcs.cliente.perfil.types.MovilType;
import com.epcs.cliente.perfil.types.ObtenerMercadoMovilType;
import com.epcs.cliente.perfil.types.ObtenerRutTitularType;
import com.epcs.cliente.perfil.types.OrqDatosUsuarioType;
import com.epcs.cliente.perfil.types.ResPlanContratadoPPType;
import com.epcs.cliente.perfil.types.ResultadoActualizarCorreoBuzonType;
import com.epcs.cliente.perfil.types.ResultadoActualizarMisDatosType;
import com.epcs.cliente.perfil.types.ResultadoConsultarDireccionPostalType;
import com.epcs.cliente.perfil.types.ResultadoConsultarMisDatosType;
import com.epcs.cliente.perfil.types.ResultadoConsultarMisUsuariosType;
import com.epcs.cliente.perfil.types.ResultadoConsultarPerfilamientoType;
import com.epcs.cliente.perfil.types.ResultadoConsultarPlanContratadoPPType;
import com.epcs.cliente.perfil.types.ResultadoConsultarSelectorCuentaType;
import com.epcs.cliente.perfil.types.ResultadoConsultarUsuarioBuicType;
import com.epcs.cliente.perfil.types.ResultadoObtenerMercadoMovilType;
import com.epcs.cliente.perfil.types.ResultadoObtenerRutTitularType;
import com.epcs.cliente.perfil.types.ResumenPerfilamientoType;
import com.epcs.cliente.perfil.types.UsuarioBuicType;
import com.epcs.recursoti.configuracion.DateHelper;
import com.epcs.recursoti.configuracion.MiEntelProperties;
import com.epcs.recursoti.configuracion.ParametrosHelper;
import com.epcs.recursoti.configuracion.Utils;
import com.epcs.recursoti.configuracion.locator.WebServiceLocator;
import com.epcs.recursoti.configuracion.locator.WebServiceLocatorException;
import com.epcs.recursoti.excepcion.DAOException;
import com.epcs.recursoti.excepcion.ServiceException;
import com.epcs.recursoti.parametros.dao.ParametrosDAO;

/**
 * @author jmanzur,mmartinez (I2B) en nombre de Absalon Opazo (Atencion al Cliente,
 *         EntelPcs)
 * 
 */
public class CuentaDAO implements Serializable {
    
	/**
     * 
     */
    private static final long serialVersionUID = 1L;

    private static final Logger LOGGER = Logger.getLogger(CuentaDAO.class);	
	
    /**
     * Valor de exito para la respuesta a un servicio
     */
    public static final String CODIGO_RESPUESTA_OK = MiEntelProperties
            .getProperty("servicios.codigoRespuesta.exito");
    
    public static final String CODIGO_RESPUESTA_OK_CORTO = MiEntelProperties
    		.getServiceMessages().getSuccessMessage("planContratadoPP", "gestionDePerfiles");

    private static final String DEFAULT_MOVIL_REGISTERED_STATUS = MiEntelProperties
            .getProperty("mientel.estado.registrado");
    
    private static final String DEFAULT_ATRIBUTO_SIN_DATO = MiEntelProperties
    .getProperty("mientel.estado.atributo.sindato");

    
    /**
     * DAO de Parametros para obtener informacion de Region, ciudad y comuna de
     * usuarios.
     */
    private ParametrosDAO pDao = new ParametrosDAO();
    
     /**
     * Obtener Informacion de Un usuario
     * 
     * @param numeroPcs
     * @param rut
     * @return
     * @throws DAOException
     * @throws ServiceException
     */
    public UsuarioBean obtenerUsuario(String numeroPcs, String rut)
            throws DAOException, ServiceException {

        LOGGER.info("Instanciando el Port.");
        ClientePerfilServicePortType port = null;
        try {
            port = (ClientePerfilServicePortType) WebServiceLocator
                    .getInstance().getPort(ClientePerfilService.class,
                            ClientePerfilServicePortType.class);

        } catch (WebServiceLocatorException e) {
            LOGGER.error("Error al inicializar el Port "
                    + ClientePerfilService.class);
            LOGGER.error( new DAOException(e));
        }

        ConsultarMisDatosType misDatosRequest = new ConsultarMisDatosType();
        ResultadoConsultarMisDatosType orqMisdatosResponse = null;

        try {
            LOGGER.info("Configurando parametros de la peticion");
            
            /*
             * El parametro 'idp' del servicio esta mal bautizado.
             * corresponde al canal o idSistema asociado al servicio de obtener un usuario
             */
            String idSistema = MiEntelProperties.getProperty("usuarios.consultarDatos.idSistema");
            misDatosRequest.setIdp(idSistema);
            
            misDatosRequest.setMsisdn(numeroPcs);
            misDatosRequest.setRut(rut);

            LOGGER.info("Invocando servicio");
            orqMisdatosResponse = port.consultarMisDatos(misDatosRequest);

        } catch (Exception e) {
            LOGGER.error("Exception caught on Service invocation: consultarMisDatos", e);            
            LOGGER.error( new DAOException(e));
        }

        String codigoRespuesta = orqMisdatosResponse.getRespuesta().getCodigo();
        String descripcionRespuesta = orqMisdatosResponse.getRespuesta()
                .getDescripcion();

        LOGGER.info("codigoRespuesta " + codigoRespuesta);
        LOGGER.info("descripcionRespuesta " + descripcionRespuesta);

        if (CODIGO_RESPUESTA_OK.equals(codigoRespuesta)) {
            
            try {

                OrqDatosUsuarioType datosUsuario = orqMisdatosResponse.getDatosUsuario();
                UsuarioBean usuarioBean = buildUsuarioBean(datosUsuario);
                return usuarioBean;
                
            } catch (Exception e) {
                LOGGER.error("Exception caught on Service response", e);            
                LOGGER.error( new DAOException(e));            }
        }
        else {
            LOGGER.info("Service error code received: " + codigoRespuesta
                    + " - " + descripcionRespuesta);
            LOGGER.error( new ServiceException(codigoRespuesta, descripcionRespuesta));
        }
        return new UsuarioBean();
    }

    
    /**
     * Actualiza la informacion de Un usuario
     * 
     * @param usuarioBean
     * @throws DAOException
     * @throws ServiceException
     */
    public void actualizarDatos(UsuarioBean usuarioBean) throws DAOException,
            ServiceException {

        LOGGER.info("Instanciando el Port.");
        ClientePerfilServicePortType port = null;
        try {
            port = (ClientePerfilServicePortType) WebServiceLocator
                    .getInstance().getPort(ClientePerfilService.class,
                            ClientePerfilServicePortType.class);

        } catch (WebServiceLocatorException e) {
            LOGGER.error("Error al inicializar el Port "
                    + ClientePerfilService.class);
            LOGGER.error( new DAOException(e));
        }

        ResultadoActualizarMisDatosType actualizacionResponse = null;

        try {
            LOGGER.info("Configurando parametros de la peticion");
            ActualizarMisDatosType actualizacionRequest = buildRequestType(usuarioBean);
            
            
            LOGGER.info("1 ::::"+actualizacionRequest.getMsisdn());
            LOGGER.info("2 ::::"+actualizacionRequest.getRut());
            //LOGGER.info("3 ::::"+actualizacionRequest.getRutTitular());
            //LOGGER.info("4 ::::"+actualizacionRequest.getDvTitular());
            LOGGER.info("5 ::::"+actualizacionRequest.getEmail());
            LOGGER.info("6 ::::"+actualizacionRequest.getEmailDirec2());
            LOGGER.info("7 ::::"+actualizacionRequest.getEmailDominio());
            LOGGER.info("8 ::::"+actualizacionRequest.getEmailDominio2());
            LOGGER.info("9 ::::"+actualizacionRequest.getTelefonoFijoArea());
            LOGGER.info("10 ::::"+actualizacionRequest.getTelefonoFijoNumero());
            
            LOGGER.info("Invocando Servicio");
            actualizacionResponse = port
                    .actualizarMisDatos(actualizacionRequest);

            this.configurarDescripciones(usuarioBean);
        } catch (Exception e) {
            LOGGER.error("Exception caught on Service invocation: actualizarMisDatos", e);
            LOGGER.error( new DAOException(e));
        }
        
        String codigoRespuesta = actualizacionResponse.getRespuesta()
                .getCodigo();
        String descripcionRespuesta = actualizacionResponse.getRespuesta()
                .getDescripcion();

        LOGGER.info("codigoRespuesta " + codigoRespuesta);
        LOGGER.info("descripcionRespuesta " + descripcionRespuesta);

        if (CODIGO_RESPUESTA_OK.equals(codigoRespuesta)) {
            LOGGER.info("Usuario Actualizado");
        }
        else {
            LOGGER.info("Service error code received: " + codigoRespuesta
                    + " - " + descripcionRespuesta);
            LOGGER.error( new ServiceException(codigoRespuesta, descripcionRespuesta));
        }

    }

    /**
     * Obtiene los usuarios asociados a la cuenta indicada en <code>nroCta</code>
     * 
     * @return java.util.List de {@link UsuarioBean} con los datos de los
     *         usuarios
     */

    public List<UsuarioBean> getUsuarios(final String nroCta) throws DAOException,
            ServiceException {

        ClientePerfilServicePortType port = null;
        List<UsuarioBean> listUsuarios = new LinkedList<UsuarioBean>();

        try {
            LOGGER.info("Instanciando el Port.");
            port = (ClientePerfilServicePortType) WebServiceLocator
                    .getInstance().getPort(ClientePerfilService.class,
                            ClientePerfilServicePortType.class);

        } catch (WebServiceLocatorException e) {
            LOGGER.error("Error al inicializar el Port "
                    + ClientePerfilService.class);
            LOGGER.error( new DAOException(e));
        }


        ConsultarMisUsuariosType misUsuariosRequest = new ConsultarMisUsuariosType();
        ResultadoConsultarMisUsuariosType misUsuariosResponse = null;
        
        try {

            LOGGER.info("Configurando Datos de la peticion");
            misUsuariosRequest.setNroCuenta(nroCta);

            LOGGER.info("Invocando servicio");
            misUsuariosResponse = port.consultarMisUsuarios(misUsuariosRequest);

        } catch (Exception e) {
            LOGGER.error("Exception caught on Service invocation: " +
            		"consultarMisUsuarios", e);
            LOGGER.error( new DAOException(e));
        }

        String codigoRespuesta = misUsuariosResponse.getRespuesta().getCodigo();
        String descripcionRespuesta = misUsuariosResponse.getRespuesta()
                .getDescripcion();

        LOGGER.info("codigoRespuesta " + codigoRespuesta);
        LOGGER.info("descripcionRespuesta " + descripcionRespuesta);

        if (CODIGO_RESPUESTA_OK.equals(codigoRespuesta)) {

            List<MovilType> listMovil = misUsuariosResponse.getMovil();

            //Lista de posibles valores de aaa
            List<CodeDescBean> aaaList = ParametrosHelper.getAAABeanList();
            
            try {
                
				for (MovilType movilType : listMovil) {

					UsuarioBean usuarioBean = new UsuarioBean();
					usuarioBean.setAaa(movilType.getAaa() == null
							|| movilType.getAaa().equals("") ? -1 : Integer
							.parseInt(movilType.getAaa()));

					CodeDescBean aaa = ParametrosHelper.getAAABean(String
							.valueOf(usuarioBean.getAaa()), aaaList);
					usuarioBean.setAaaDesc(aaa.getDescripcion());

					usuarioBean.setApellidoMaterno(movilType
							.getApellidoMaterno());
					usuarioBean.setApellidoPaterno(movilType
							.getApellidoPaterno());
					usuarioBean.setPrimerNombre(movilType.getPrimerNombre());
					usuarioBean.setSegundoNombre(movilType.getSegundoNombre());
					
					try {
						String rut = movilType.getRut();
						usuarioBean.setRut(RutBean.parseRut(rut));
					} catch (Exception e) {
						LOGGER.warn("rut de numeroPcs '"
								+ movilType.getMsisdn()
								+ "' no puede ser obtenido. ");
						usuarioBean.setRut(null);
					}

					usuarioBean.setNumeroPCS(movilType.getMsisdn());
					listUsuarios.add(usuarioBean);

				}

            } catch (Exception e) {
                LOGGER.error("Exception caught on Service response: " +
                        "consultarMisUsuarios", e);
                LOGGER.error( new DAOException(e));
            }

        }
        else {
            LOGGER.info("Service error code received: " + codigoRespuesta
                    + " - " + descripcionRespuesta);
            LOGGER.error( new ServiceException(codigoRespuesta, descripcionRespuesta));
        }

        return listUsuarios;

    }

    /**
     * Actualiza los permisos de un Usuario
     * 
     * @param numeroPcs
     * @param rut
     * @param sesionAaa
     * @param nuevoAAA
     * @throws DAOException
     * @throws ServiceException
     */
    public void actualizarAAA(String numeroPcs, String rut, String sesionAaa,
            String nuevoAAA) throws DAOException, ServiceException {

        int nuevoAAAInt = 0;
        LOGGER.info("Inicio ActualizarAAA");

        try {
            nuevoAAAInt = Integer.parseInt(nuevoAAA);
        } catch (Exception e) {
            LOGGER.error( new DAOException("Atributo de Auto Atencion No Valido :"
                    + nuevoAAA, e));
        }

        LOGGER.info("Buscando Informacion del Usuario : nroCta:" + numeroPcs
                + " Rut:" + rut);
        UsuarioBean userBean = this.obtenerUsuario(numeroPcs, rut);

        if (userBean == null) {
            LOGGER.error( new DAOException("No se obtuvo Informacion del Usuario"));
        }

        LOGGER.info("Configurando nuevo valor de aaa");
        userBean.setAaa(nuevoAAAInt);

        LOGGER.info("Actualizando Usuario");

         this.actualizarDatos(userBean);

        LOGGER.info("Datos de aaa Actualizados");
    }

    /**
     * 
     * Obtener la informacion del perfil de un usuario a partir de su numeroPcs.<br>
     * 
     * @param numeropcs
     *            el numero del movil del cual se requiere la informacion
     * 
     * @return {@link PerfilUsuarioBean} con la informacion del perfil de
     *         usuario.
     * 
     * @throws DAOException
     *             Si <code>numeropcs</code> es invalido.<br>
     *             Si no puede conectarse al servicio u ocurre un error en la
     *             llamada a el.<br>
     *             Los datos de la respuesta no son del tipo o formato esperado.
     * 
     * @throws ServiceException
     *             Si el servicio retorna con codigo distinto a
     *             {@link #CODIGO_RESPUESTA_OK}
     * 
     */
    public PerfilUsuarioBean obtenerPerfilUsuario(String numeropcs)
            throws DAOException, ServiceException {

        if (Utils.isEmptyString(numeropcs)){
            LOGGER.error("Numero de movil invalido");
            LOGGER.error( new DAOException("Numero de movil invalido!"));
        }
        
        
        ClientePerfilServicePortType port = null;
        LOGGER.info("Instanciando el port");
        try {
            port = (ClientePerfilServicePortType) WebServiceLocator
                    .getInstance().getPort(ClientePerfilService.class,
                            ClientePerfilServicePortType.class);

        } catch (WebServiceLocatorException e) {
            LOGGER.error("Error al inicializar el Port "
                    + ClientePerfilService.class);
            LOGGER.error( new DAOException(e));
        }
        
        LOGGER.info("Configurando Datos de la peticion");
        ConsultarPerfilamientoType perfilType = new ConsultarPerfilamientoType();
        perfilType.setMsisdn(numeropcs);
        
        LOGGER.info("Invocando servicio");
        ResultadoConsultarPerfilamientoType response = null;
        try {
            response = port.consultarPerfilamiento(perfilType);
        } catch (Exception e) {
            LOGGER.error("Exception caught on Service invocation: consultarPerfilamiento", e);
            LOGGER.error( new DAOException(e));
        }
    
        String codigoRespuesta = response.getRespuesta().getCodigo();
        String descripcionRespuesta = response.getRespuesta()
                .getDescripcion();
        
        LOGGER.info("codigoRespuesta " + codigoRespuesta);
        LOGGER.info("descripcionRespuesta " + descripcionRespuesta);
        
        if (CODIGO_RESPUESTA_OK.equals(codigoRespuesta)) {

            ResumenPerfilamientoType perfil = response.getResumenPerfilamiento();
            
            if (perfil == null) {
                LOGGER.error("Servicio no retorno resumen de "
                        + "perfilamiento para numeroPcs " + numeropcs);
                LOGGER.error( new DAOException("Servicio no retorno resumen de "
                        + "perfilamiento para numeroPcs " + numeropcs));
            }
                
            perfil = response.getResumenPerfilamiento();
            PerfilUsuarioBean result = new PerfilUsuarioBean();
            result.setMercado(perfil.getMercado());
            try {
                result.setFlagBam(Integer.parseInt(perfil.getFlagBam()));
            }                    
            catch (NumberFormatException e) {
                LOGGER.error("Atributo flagBam no es numerico: "
                        + perfil.getFlagBam(), e);
                LOGGER.error( new DAOException("Atributo flagBam no es numerico: "
                        + perfil.getFlagBam(), e));
            }
            
            UsuarioSeleccionadoBean usuarioSeleccionado = new UsuarioSeleccionadoBean();
            usuarioSeleccionado.setNumeroPcs(numeropcs);
            usuarioSeleccionado.setNombreUsuario(perfil.getNombreUsuario());
            usuarioSeleccionado.setSubMercado(perfil.getSubMercado());
            usuarioSeleccionado.setRut(RutBean.parseRut(perfil.getRutUsuario()));
            result.setUsuarioSeleccionado(usuarioSeleccionado);
            return result;

        }
        else {
            LOGGER.info("Service error code received: " + codigoRespuesta
                    + " - " + descripcionRespuesta);
            LOGGER.error( new ServiceException(codigoRespuesta, descripcionRespuesta));
        }
		return new PerfilUsuarioBean();
        
    }
    
    public List<MsisdnAsociadoBean> obtenerNumerosAsociados(
            String numeroPcs) throws DAOException, ServiceException {

        
        
        ClientePerfilServicePortType port = null;
        LOGGER.info("Instanciando el port " + ClientePerfilServicePortType.class);
        try {
            port = (ClientePerfilServicePortType) WebServiceLocator.getInstance().getPort(
                    ClientePerfilService.class, ClientePerfilServicePortType.class);
        } catch (WebServiceLocatorException e) {
            LOGGER.error("Error al inicializar el Port " + ClientePerfilServicePortType.class, e);
            LOGGER.error( new DAOException(e));
        }
        
        LOGGER.info("Configurando Datos de la peticion");
        ConsultarSelectorCuentaType request = new ConsultarSelectorCuentaType();
        request.setMsisdn(numeroPcs);

        LOGGER.info("Invocando servicio");
        ResultadoConsultarSelectorCuentaType response = null;
        try {
            response = port.consultarSelectorCuenta(request);
        } catch (Exception e) {
            LOGGER.error("Exception caught on Service invocation: consultarSelectorCuenta", e);
            LOGGER.error( new DAOException(e));
        }

        String codigoRespuesta = response.getRespuesta().getCodigo();
        String descripcionRespuesta = response.getRespuesta().getDescripcion();

        LOGGER.info("codigoRespuesta " + codigoRespuesta);
        LOGGER.info("descripcionRespuesta " + descripcionRespuesta);

        if (Utils.isEmptyString(codigoRespuesta)) {
            LOGGER.error( new DAOException("consultarSelectorCuenta: Servicio no responde "
                    + "con codigoRespuesta"));
        }

        if (CODIGO_RESPUESTA_OK.equals(codigoRespuesta)) {

            List<DetalleMsisdnType> detalleMsisdnTypes = response.getDetalleMsisdn();
            
            List<MsisdnAsociadoBean> msisdnAsociados = new ArrayList<MsisdnAsociadoBean>();
            
            if (detalleMsisdnTypes == null || detalleMsisdnTypes.isEmpty()) {
                LOGGER.info("numeroPcs " + numeroPcs + " no contiene numeros asociados");
            }
            else {
                
                for (DetalleMsisdnType msisdnType : detalleMsisdnTypes) {
                    
                    
                    MsisdnAsociadoBean msisdnAsociado = new MsisdnAsociadoBean();
                    msisdnAsociado.setNumeroPcs(msisdnType.getMsisdn());

                    if (DEFAULT_MOVIL_REGISTERED_STATUS
                            .equalsIgnoreCase(msisdnType.getFlagRegistro())) {
                        
                        try {
                            msisdnAsociado.setFlagBam(Integer
                                    .parseInt(msisdnType.getFlagBam()));
//                            msisdnAsociado
//                                    .setDescripcionTipoMsisdn(formatearDescripcion(
//                                            Integer.parseInt(msisdnType
//                                                    .getFlagBam()), msisdnType
//                                                    .getMsisdn(), msisdnType
//                                                    .getMercado()));
                        } catch (Exception e) {
                            LOGGER.error("valor de flagBam invalido. Debe ser numerico: '"
                                           + msisdnType.getFlagBam() + "'", e);
                        }
                        
                        msisdnAsociado.setFlagEstadoMsisdn(msisdnType
                                .getFlagEstadoMsisdn());
                        msisdnAsociado.setFlagRegistrado(msisdnType
                                .getFlagRegistro());
                        msisdnAsociado.setMercado(msisdnType.getMercado());
                    }
                    else {
                        msisdnAsociado.setDescripcionTipoMsisdn(msisdnType
                                .getMsisdn().concat(" No Registrado"));
                    }
                    msisdnAsociados.add(msisdnAsociado);
                }
            }
            
            return msisdnAsociados;

        }
        else {
            LOGGER.info("consultarSelectorCuenta: Service error code received: "
                    + codigoRespuesta + " - " + descripcionRespuesta);
            LOGGER.error( new ServiceException(codigoRespuesta, descripcionRespuesta));
        }
		return new ArrayList<MsisdnAsociadoBean>();

   
    }
    
    /**
     * Metodo que permite desplegar una lista con los moviles asociados a una
     * cuenta de administrador, de un usuario suscripcion
     * 
     * @param msisdnUsuario
     *            numero del administrador de la cuenta
     * @return una lista con los datos de los moviles asociados
     * @throws DAOException
     *             en caso de algun problema al obtener la informacion del
     *             servicio de perfilamiento.
     */
    public List<MsisdnAsociadoBean> getListaMsisdnAsociados(String msisdnUsuario, String subMercado, String flagBam)
            throws DAOException {
        List<MsisdnAsociadoBean> msisdnAsociados = new ArrayList<MsisdnAsociadoBean>();
        LOGGER.info("Inicio getListaMsisdnAsociados");

        ClientePerfilServicePortType port = null;
        ConsultarSelectorCuentaType selectorCuentaType = new ConsultarSelectorCuentaType();
        selectorCuentaType.setMsisdn(msisdnUsuario);
        ResultadoConsultarSelectorCuentaType response = null;
        try {

            port = (ClientePerfilServicePortType) WebServiceLocator
                    .getInstance().getPort(ClientePerfilService.class,
                            ClientePerfilServicePortType.class);

        } catch (WebServiceLocatorException e) {
            LOGGER.error("Error al inicializar el Port "
                    + ClientePerfilService.class);
            LOGGER.error( new DAOException(e));

        }
        response = port.consultarSelectorCuenta(selectorCuentaType);

        if (response != null && response.getRespuesta() != null) {
            LOGGER
                    .info("Respuesta del servicio [ClientePerfilService/consultarSelectorCuenta]: codigo ["
                            + response.getRespuesta().getCodigo()
                            + "], descripcion ["
                            + response.getRespuesta().getDescripcion() + "]");
            if (response.getDetalleMsisdn() != null
                    && response.getDetalleMsisdn().size() > 0) {
                List<DetalleMsisdnType> msisdns = response.getDetalleMsisdn();
                for (DetalleMsisdnType msisdnType : msisdns) {
                    MsisdnAsociadoBean msisdnAsociado = new MsisdnAsociadoBean();
                    msisdnAsociado.setNumeroPcs(msisdnType.getMsisdn());
                    msisdnAsociado.setFlagRegistrado(msisdnType.getFlagRegistro());
                    if (DEFAULT_MOVIL_REGISTERED_STATUS
                            .equalsIgnoreCase(msisdnType.getFlagRegistro())) {
                        try {
                            String resultFlagBam = msisdnType.getFlagBam().equals("SESION") ? flagBam : msisdnType.getFlagBam();
                            msisdnAsociado.setFlagBam(Integer.parseInt(resultFlagBam));
                            msisdnAsociado.setDescripcionTipoMsisdn(SelectorCuentaHelper.formatearDescripcion(
                                            Integer.parseInt(resultFlagBam), msisdnType
                                                    .getMsisdn(), msisdnType
                                                    .getMercado()));
                        } catch (NumberFormatException nfe) {
                            LOGGER
                                    .error("No se pudo parsear el numero de flagBAM. El servicio trajo el valor ["
                                            + msisdnType.getFlagBam() + "]");
                        }
                        msisdnAsociado.setFlagEstadoMsisdn(msisdnType.getFlagEstadoMsisdn());                        
                        msisdnAsociado.setMercado(msisdnType.getMercado());
                        msisdnAsociado.setAaa(msisdnType.getAAA());
                        
                        String resultSubMercado = msisdnType.getSubMercado().equals("SESION") ? subMercado : msisdnType.getSubMercado();
                        msisdnAsociado.setSubMercado(resultSubMercado);
                    }
                    else {
                        msisdnAsociado.setDescripcionTipoMsisdn(msisdnType
                                .getMsisdn().concat(" No Registrado"));
                    }
                    msisdnAsociados.add(msisdnAsociado);
                }
            }
        }
        else {
            LOGGER
                    .error("No hubo respuesta del servicio [ClientePerfilService/consultarSelectorCuenta]");
            LOGGER.error( new DAOException(
                    "No hubo respuesta del servicio [ClientePerfilService/consultarSelectorCuenta] "));
        }
        return msisdnAsociados;

    }

   

    private ActualizarMisDatosType buildRequestType(UsuarioBean usuarioBean) {

        ActualizarMisDatosType actualizacionRequest = new ActualizarMisDatosType();
        actualizacionRequest.setAaa(String.valueOf(usuarioBean.getAaa()));
        actualizacionRequest.setMsisdn(usuarioBean.getNumeroPCS());

        actualizacionRequest.setRut(usuarioBean.getRut().getStringValue());

        actualizacionRequest.setPrimerNombre(usuarioBean.getPrimerNombre());
        actualizacionRequest.setSegundoNombre(usuarioBean.getSegundoNombre());
        actualizacionRequest
                .setPrimerApellido(usuarioBean.getApellidoPaterno());
        actualizacionRequest.setSegundoApellido(usuarioBean
                .getApellidoMaterno());
        actualizacionRequest.setSexo(usuarioBean.getSexo());

        //Direccion
        DireccionBean direccionContacto = usuarioBean.getDireccionContacto();
        
        actualizacionRequest.setDireccionCalle(direccionContacto.getCalle());
        actualizacionRequest.setDireccionNumero(direccionContacto.getNumero());
        actualizacionRequest.setDireccionResto(direccionContacto
                .getDepartamento());        
        
        actualizacionRequest.setComuna(direccionContacto.getComuna()
                .getCodigo());
        
        actualizacionRequest.setCiudad(direccionContacto.getCiudad()
                .getCodigo());
        
        actualizacionRequest.setRegion(direccionContacto.getRegion()
                .getCodigo());
        
        actualizacionRequest.setCodPostal("");

        String email = "";
        String emailDominio = "";
        String arrayEmail[] = usuarioBean.getEmail().split("@");
        if (arrayEmail != null && arrayEmail.length > 1) {
            email = arrayEmail[0];
            emailDominio = arrayEmail[1];
        }

        actualizacionRequest.setEmail(email);
        actualizacionRequest.setEmailDominio(emailDominio);
        /*
         * Primero validamos si usuarioBean.getFechaNacimiento() es nulo
         * porque el metodo DateHelper.format lanza NullPointerException 
         * cuando le pasamos una fecha nula */
        Date fecNac = usuarioBean.getFechaNacimiento();
        if(fecNac != null)
        	actualizacionRequest.setFechaNacimiento(DateHelper.format(fecNac, DateHelper.FORMAT_ddMMyyyy));
        else
        	actualizacionRequest.setFechaNacimiento("");
        actualizacionRequest.setEstadoCivil(usuarioBean.getEstadoCivil());
        actualizacionRequest.setTelefonoFijoNumero(usuarioBean
                .getTelefonoAdicional());
        actualizacionRequest.setTelefonoFijoArea(usuarioBean
                .getPrefijoTelefonoAdicional());
        actualizacionRequest.setNivelEstudios(usuarioBean.getEstudio());
        actualizacionRequest.setActividad(usuarioBean.getActividadLaboral());

        actualizacionRequest.setIntereses("");
        actualizacionRequest.setDeportes("");
        actualizacionRequest.setCoacte("");
        actualizacionRequest.setCtavista("");
        actualizacionRequest.setOrigen("");
        actualizacionRequest.setTiendas("");
        actualizacionRequest.setInternet("");
        actualizacionRequest.setFrecUsoInternet("");
        actualizacionRequest.setUsoMovil("");
        actualizacionRequest.setEquipos("");
        actualizacionRequest.setEsUsted("");
        actualizacionRequest.setRelacionTitular(usuarioBean
                .getRelacionTitular());
        actualizacionRequest.setPagoCuenta("");
        actualizacionRequest.setSms("");
        actualizacionRequest.setMailInfo("");
        actualizacionRequest.setCanalPref("");
        actualizacionRequest.setTipoUsuario("");

        // Rut Titular
        if (usuarioBean.getRutTitular() != null) {
            actualizacionRequest.setRutTitular(String.valueOf(usuarioBean
                    .getRutTitular().getNumero()));
            actualizacionRequest.setDvTitular(String.valueOf(usuarioBean
                    .getRutTitular().getDigito()));
        }

        actualizacionRequest.setEmailDirec2(email);
        actualizacionRequest.setEmailDominio2(emailDominio);
        actualizacionRequest.setProfesion(usuarioBean.getAreaLaboral());

        actualizacionRequest.setHijo(usuarioBean.getHijos());

        return actualizacionRequest;
    }

    /**
     * Metodo utilitario, para la construccion de un {@link UsuarioBean} a partir de un {@link OrqDatosUsuarioType}.<br>
     * Este metodo se crea para disminuir la complejidad ciclomatica de los metodos de negocios.
     * 
     * @param datosUsuario {@link OrqDatosUsuarioType} datos del usuario obtenidos desde servicio
     * @return {@link UsuarioBean} bean de Mi Entel que representa al usuario
     * @throws ServiceException En caso que el servicio que recupera informacion de regiones falle.
     * @throws DAOException En caso que el metodo de recuperar la region falle.
     */
    private UsuarioBean buildUsuarioBean(final OrqDatosUsuarioType datosUsuario)
            throws DAOException, ServiceException {
        
        UsuarioBean usuarioBean = new UsuarioBean();
        
        //Si aaa que informa servicio es vacio o null, se asigna valor -1
        Integer aaa = Utils.isEmptyString(datosUsuario.getAaa()) ? -1 : Integer
                .parseInt(datosUsuario.getAaa());
        usuarioBean.setAaa(aaa);

        // Fecha Nacimiento ddMMyyyy
        Date fechaNacimiento = DateHelper.parseDate(datosUsuario.getFechaNacimento(),
                DateHelper.FORMAT_ddMMyyyy);
        usuarioBean.setFechaNacimiento(fechaNacimiento);

        usuarioBean.setPrimerNombre(datosUsuario
                .getPrimerNombre());

        usuarioBean.setSegundoNombre(datosUsuario
                .getSegundoNombre());

        usuarioBean.setApellidoPaterno(datosUsuario.getApellidoPaterno());
        usuarioBean.setApellidoMaterno(datosUsuario.getApellidoMaterno());
        
        usuarioBean
                .setSexo(datosUsuario.getSexo());
        
        String rutUsuario = datosUsuario.getRut();
        usuarioBean.setRut(RutBean.parseRut(rutUsuario));

        usuarioBean.setNumeroPCS(datosUsuario
                .getMsisdn());
        usuarioBean.setEmail(datosUsuario
                .getEmail());
        usuarioBean.setEmailFacturaElectronica(datosUsuario.getEmailFacturacionElectronica());
        usuarioBean.setRelacionTitular(datosUsuario.getIdRelacionTitular());

        usuarioBean.setActividadLaboral(datosUsuario.getActividadLaboral());
        usuarioBean.setAreaLaboral(datosUsuario
                .getAreaLaboral());
        usuarioBean.setHijos(String.valueOf(datosUsuario.getHijos()));
        usuarioBean.setEstadoCivil(datosUsuario
                .getEstadoCivil());
        usuarioBean.setEstudio(datosUsuario
                .getNivelEstudio());
        usuarioBean.setTelefonoAdicional(datosUsuario.getTelefonoAdicional());
        usuarioBean.setPrefijoTelefonoAdicional(datosUsuario.getAreaTelefonoRes());

        usuarioBean.setEstudioDesc(ParametrosHelper
                .getCodeDescBeanByTypeId(
                        "nivelEstudios",
                        datosUsuario
                                .getNivelEstudio()).getDescripcion());
        usuarioBean.setSexoDesc(ParametrosHelper.getCodeDescBeanByTypeId(
                "sexo", datosUsuario.getSexo())
                .getDescripcion());
        usuarioBean.setEstadoCivilDesc(ParametrosHelper
                .getCodeDescBeanByTypeId("estadoCivil", datosUsuario.getEstadoCivil())
                .getDescripcion());
        usuarioBean.setActividadLaboralDesc(ParametrosHelper
                .getCodeDescBeanByTypeId("actividad", datosUsuario.getActividadLaboral())
                .getDescripcion());
        usuarioBean.setAreaLaboralDesc(ParametrosHelper
                .getCodeDescBeanByTypeId("areaLaboral", datosUsuario.getAreaLaboral())
                .getDescripcion());
        usuarioBean
                .setRelacionTitularDesc(ParametrosHelper
                        .getCodeDescBeanByTypeId("relacionTitular",
                                datosUsuario
                                        .getIdRelacionTitular())
                        .getDescripcion());
        usuarioBean.setHijosDesc(ParametrosHelper
                .getCodeDescBeanByTypeId("hijo", String
                        .valueOf(datosUsuario
                                .getHijos())).getDescripcion());

        usuarioBean.setDireccionFactura(new DireccionBean());
        
        /*
         * Direccion contacto
         */
        DireccionBean dirContactoBean = new DireccionBean();
        dirContactoBean.setCalle(datosUsuario
                .getCalle());

        //Ciudad
        if (Utils.isEmptyString(datosUsuario.getCiudad())) {
            LOGGER.warn("Usuario " + datosUsuario.getRut()
                    + " no contiene informacion de ciudad");
            dirContactoBean.setCiudad(CiudadBean.emptyBean());
        } else {
            CiudadBean ciudadBean = new CiudadBean(datosUsuario.getCiudad(),
                    datosUsuario.getCiudad());
            dirContactoBean.setCiudad(ciudadBean);
        } 

        //Comuna
        if (Utils.isEmptyString(datosUsuario.getComuna())) {
            LOGGER.warn("Usuario " + datosUsuario.getRut()
                    + " no contiene informacion de comuna");
            dirContactoBean.setComuna(ComunaBean.emptyBean());
        } else {
            ComunaBean comunaBean = new ComunaBean(datosUsuario.getComuna(),
                    datosUsuario.getComuna());
            dirContactoBean.setComuna(comunaBean);
        }

        //region
        String regionId = datosUsuario.getRegion();
        RegionBean regionBean = RegionBean.emptyBean();
        if(Utils.isEmptyString(regionId)) {
            LOGGER.warn("Usuario " + datosUsuario.getRut()
                    + " no contiene id de region, "
                    + "el valor no podra sera informado");
        } else {
            regionBean = this.findRegionById(regionId);
        }
        dirContactoBean.setRegion(regionBean);
        
        dirContactoBean.setNumero(datosUsuario.getNumero());
        dirContactoBean.setDepartamento(datosUsuario.getDepartamento());

        usuarioBean.setDireccionContacto(dirContactoBean);

        //Este dato solo trae informacion en caso de clientes empresas.
        usuarioBean.setComunaFactura(ComunaBean.emptyBean());

        
        return usuarioBean;
    }

    /**
     * Metodo utilitario para obtencion de la region a partir del id.<br>
     * Este metodo se apoya en {@link ParametrosDAO}.<br>
     * 
     * @see ParametrosDAO#findRegionById(String)
     * @param regionId
     * @return
     * @throws DAOException
     * @throws ServiceException
     */
    private RegionBean findRegionById(String regionId) throws DAOException,
            ServiceException {
        return pDao.findRegionById(regionId);
    }

    /**
     * Obtener la direccion de factura.
     * 
     * @throws DAOException
     * @throws ServiceException
     * 
     */
    public DireccionBean obtenerDireccionFactura(String rut, String numeroCuenta) throws DAOException,
            ServiceException {

        ClientePerfilServicePortType port = null;
        DireccionBean direccionBean = new DireccionBean();

        LOGGER.info("Instanciando el port "
                + ClientePerfilServicePortType.class);
        try {
            port = (ClientePerfilServicePortType) WebServiceLocator
                    .getInstance().getPort(ClientePerfilService.class,
                            ClientePerfilServicePortType.class);
        } catch (WebServiceLocatorException e) {
            LOGGER.error("Error al inicializar el Port "
                    + ClientePerfilServicePortType.class, e);
            LOGGER.error( new DAOException(e));
        }

        LOGGER.info("Configurando Datos de la peticion");
        ConsultarDireccionPostalType request = new ConsultarDireccionPostalType();

        
        String IDS_ISTEMA = MiEntelProperties
                .getProperty("parametros.direccionpostal.idsistema");

        String PASSWORD_SISTEMA = MiEntelProperties
                .getProperty("parametros.direccionpostal.passwordsistema");
        
        request.setIdSistema(IDS_ISTEMA);
        request.setPasswordSistema(PASSWORD_SISTEMA);
        request.setRut(rut);
        request.setNumeroCuenta(numeroCuenta);

        LOGGER.info("Invocando servicio");
        ResultadoConsultarDireccionPostalType response = null;
        try {
            response = port.consultarDireccionPostal(request);
        } catch (Exception e) {
            LOGGER.error("Exception caught on Service invocation: consultarDireccionPostal", e);
            LOGGER.error( new DAOException(e));
        }

        String codigoRespuesta = response.getRespuesta().getCodigo();
        String descripcionRespuesta = response.getRespuesta().getDescripcion();

        LOGGER.info("codigoRespuesta " + codigoRespuesta);
        LOGGER.info("descripcionRespuesta " + descripcionRespuesta);

        if (Utils.isEmptyString(codigoRespuesta)) {
            LOGGER.error( new DAOException("consultarDireccionPostal: Servicio no respondio "
                    + "con codigoRespuesta"));
        }

        if (CODIGO_RESPUESTA_OK.equals(codigoRespuesta)) {

        	try{
        		direccionBean.setDireccionCompleta(response.getDireccionPostal()
                        .getCallePostal());
                direccionBean.setCiudad(new CiudadBean(response
                        .getDireccionPostal().getComunaPostal(), response
                        .getDireccionPostal().getComunaPostal()));
                direccionBean.setComuna(new ComunaBean(response
                        .getDireccionPostal().getComunaPostal(), response
                        .getDireccionPostal().getComunaPostal()));
                direccionBean.setCodigoPostal(response.getDireccionPostal()
                        .getCodigoPostal());
                direccionBean.setNumero(response.getDireccionPostal()
                        .getNumeroPostal());
        	} catch (Exception e) {
                LOGGER.error("Exception caught on Service response: consultarDireccionPostal", e);
                LOGGER.error( new DAOException(e));
            }

        }
        else {
            LOGGER.info("consultarDireccionPostal: Service error code received: "
                    + codigoRespuesta + " - " + descripcionRespuesta);
            LOGGER.error( new ServiceException(codigoRespuesta, descripcionRespuesta));
        }

        return direccionBean;
    }
    
    /**
     * 
     * @param ip
     * @param direccionBean
     * @throws DAOException
     * @throws ServiceException
     */
    public void actualizarDireccionPostal(String nroCuenta, String ip,
            DireccionBean direccionBean) throws DAOException, ServiceException {
        ClientePerfilServicePortType port = null;

        LOGGER.info("Instanciando el port "
                + ClientePerfilServicePortType.class);
        try {
            port = (ClientePerfilServicePortType) WebServiceLocator
                    .getInstance().getPort(ClientePerfilService.class,
                            ClientePerfilServicePortType.class);
        } catch (WebServiceLocatorException e) {
            LOGGER.error("Error al inicializar el Port "
                    + ClientePerfilServicePortType.class, e);
            LOGGER.error( new DAOException(e));
        }

        String IDS_ISTEMA = MiEntelProperties
                .getProperty("parametros.direccionpostal.idsistema");

        String PASSWORD_SISTEMA = MiEntelProperties
                .getProperty("parametros.direccionpostal.passwordsistema");
        
        ActualizarDireccionPostalResponseType response = null;
        
        
        try {

            LOGGER.info("Configurando Datos de la peticion");
            ActualizarDireccionPostalType request = new ActualizarDireccionPostalType();
            request.setCodigoPostal(direccionBean.getCodigoPostal());
            request.setDireccionPostal(direccionBean.getDireccionCompleta());
            request.setNumeroPostal(direccionBean.getNumero());
            request.setComunaPostal(direccionBean.getComuna().getCodigo());
            request.setRegionPostal("");
            request.setCiudadPostal(direccionBean.getComuna().getCodigo());
            request.setIdSistema(IDS_ISTEMA);
            request.setPasswordSistema(PASSWORD_SISTEMA);
            request.setIpServidor(ip);
            request.setNumeroCuenta(nroCuenta);

            LOGGER.info("Invocando servicio");

            response = port.actualizarDireccionPostal(request);

        } catch (Exception e) {
            LOGGER.error("Exception caught on Service invocation: actualizarDireccionPostal", e);
            LOGGER.error( new DAOException(e));
        }

        String codigoRespuesta = response.getRespuesta().getCodigo();
        String descripcionRespuesta = response.getRespuesta().getDescripcion();

        LOGGER.info("codigoRespuesta " + codigoRespuesta);
        LOGGER.info("descripcionRespuesta " + descripcionRespuesta);

        if (CODIGO_RESPUESTA_OK.equals(codigoRespuesta)) {

            LOGGER.info("Direccion Postal Actualizada.");

        }
        else {

            LOGGER.info("method: Service error code received: "
                    + codigoRespuesta + " - " + descripcionRespuesta);
            LOGGER.error( new ServiceException(codigoRespuesta, descripcionRespuesta));
        }

    }
    
    /**
     * Actualiza las descipciones de los combos al hacer una actualizacion de
     * datos
     * 
     * @param usuarioBean
     */
    private void configurarDescripciones(UsuarioBean usuarioBean) {
        try {
            usuarioBean.setEstudioDesc(ParametrosHelper
                    .getCodeDescBeanByTypeId("nivelEstudios",
                            usuarioBean.getEstudio()).getDescripcion());
            usuarioBean.setSexoDesc(ParametrosHelper.getCodeDescBeanByTypeId(
                    "sexo", usuarioBean.getSexo()).getDescripcion());
            usuarioBean.setEstadoCivilDesc(ParametrosHelper
                    .getCodeDescBeanByTypeId("estadoCivil",
                            usuarioBean.getEstadoCivil()).getDescripcion());
            usuarioBean
                    .setActividadLaboralDesc(ParametrosHelper
                            .getCodeDescBeanByTypeId("actividad",
                                    usuarioBean.getActividadLaboral())
                            .getDescripcion());
            usuarioBean.setAreaLaboralDesc(ParametrosHelper
                    .getCodeDescBeanByTypeId("areaLaboral",
                            usuarioBean.getAreaLaboral()).getDescripcion());
            usuarioBean.setRelacionTitularDesc(ParametrosHelper
                    .getCodeDescBeanByTypeId("relacionTitular",
                            usuarioBean.getRelacionTitular()).getDescripcion());
            usuarioBean.setHijosDesc(ParametrosHelper.getCodeDescBeanByTypeId(
                    "hijo", String.valueOf(usuarioBean.getHijos()))
                    .getDescripcion());

            String regionId = usuarioBean.getDireccionContacto().getRegion()
                    .getCodigo();

            RegionBean regionBean = null;
            if (Utils.isEmptyString(regionId)) {
                LOGGER.warn("Usuario " + usuarioBean.getRut()
                        + " no contiene id de region, "
                        + "el valor no podra ser informado");
            }
            else {

                regionBean = this.findRegionById(regionId);
                usuarioBean.getDireccionContacto().setRegion(regionBean);

            }

        } catch (Exception e) {
            LOGGER.error(
                    "No se puedieron configurar las descripciones de las propiedades :"
                            + usuarioBean.getRut(), e);
        }
    }
    
    
    
	/**
     * Metodo que obtiene el mercado del usuario dependiendo a su numeroPcs.
     * 
     * @param numeroPcs
     * @return
     * @throws DAOException
     * @throws ServiceException
     */
     public String obtenerMercadoMovil(String numeroPcs) throws DAOException, ServiceException{
    	 
    	 ClientePerfilServicePortType port = null;
         LOGGER.info("Instanciando el port "
                 + ClientePerfilServicePortType.class);
         try {
             port = (ClientePerfilServicePortType) WebServiceLocator
                     .getInstance().getPort(ClientePerfilService.class,
                    		 ClientePerfilServicePortType.class);
         } catch (WebServiceLocatorException e) {
             LOGGER.error("Error al inicializar el Port "
                     + ClientePerfilServicePortType.class, e);
             LOGGER.error( new DAOException(e));
         }
         
         LOGGER.info("Configurando parametros de la peticion");
         ObtenerMercadoMovilType request = new ObtenerMercadoMovilType();
         request.setMsisdn(numeroPcs);
         
         LOGGER.info("Invocando servicio");
         ResultadoObtenerMercadoMovilType response = null;
         try{
        	 response = port.obtenerMercadoMovil(request);
         }catch (Exception e) {
        	 LOGGER.error("Exception caught on Service invocation: obtenerMercadoMovil", e);
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
				
				return response.getMercado();
				
			}catch (Exception e) {
				LOGGER.error(
                        "Exception inesperada al obtener mercado del Usuario", e);
                LOGGER.error( new DAOException(e));
			}
 	    
 	   }else {
 		    LOGGER.info("Respuesta del Servicio : " +codigoRespuesta+ " - " + descripcionRespuesta);
 		    LOGGER.error( new ServiceException(codigoRespuesta, descripcionRespuesta));
 		}
	return descripcionRespuesta;

     }
     
     
     
     /**
      * Metodo que obtiene el rut del titular asociado al numeroPcs.
      * 
      * @param numeroPcs
      * @return
      * @throws DAOException
      * @throws ServiceException
      */
      public String obtenerRutTitular(String numeroPcs) throws DAOException, ServiceException{
     	 
     	 ClientePerfilServicePortType port = null;
          LOGGER.info("Instanciando el port "
                  + ClientePerfilServicePortType.class);
          try {
              port = (ClientePerfilServicePortType) WebServiceLocator
                      .getInstance().getPort(ClientePerfilService.class,
                     		 ClientePerfilServicePortType.class);
          } catch (WebServiceLocatorException e) {
              LOGGER.error("Error al inicializar el Port "
                      + ClientePerfilServicePortType.class, e);
              LOGGER.error( new DAOException(e));
          }
          
          LOGGER.info("Configurando parametros de la peticion");
          ObtenerRutTitularType request = new ObtenerRutTitularType();
          request.setMsisdn(numeroPcs);
          
          LOGGER.info("Invocando servicio");
          ResultadoObtenerRutTitularType response = null;
          try{
         	 response = port.obtenerRutTitular(request);
          }catch (Exception e) {
         	 LOGGER.error("Exception caught on Service invocation: obtenerRutTitular", e);
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
 				
 				return response.getRutTitular();
 				
 			}catch (Exception e) {
 				LOGGER.error(
                         "Exception inesperada al obtener rut del Usuario", e);
                 LOGGER.error( new DAOException(e));
 			}
  	    
  	   }else {
  		    LOGGER.info("Respuesta del Servicio : " +codigoRespuesta+ " - " + descripcionRespuesta);
  		    LOGGER.error( new ServiceException(codigoRespuesta, descripcionRespuesta));
  		}
	return "";

      }
      
      /**
       * Metodo que obtiene el rut del titular asociado al numeroPcs.
       * 
       * @param numeroPcs
       * @return
       * @throws DAOException
       * @throws ServiceException
       */
       public DatosUsuarioTitularBean obtenerDatosTitular(String numeroPcs) throws DAOException, ServiceException{
      	 
    	 DatosUsuarioTitularBean datosUsuarioTitular = null;
    	   
      	 ClientePerfilServicePortType port = null;
           LOGGER.info("Instanciando el port "
                   + ClientePerfilServicePortType.class);
           try {
               port = (ClientePerfilServicePortType) WebServiceLocator
                       .getInstance().getPort(ClientePerfilService.class,
                      		 ClientePerfilServicePortType.class);
           } catch (WebServiceLocatorException e) {
               LOGGER.error("Error al inicializar el Port "
                       + ClientePerfilServicePortType.class, e);
               LOGGER.error( new DAOException(e));
           }
           
           LOGGER.info("Configurando parametros de la peticion");
           ObtenerRutTitularType request = new ObtenerRutTitularType();
           request.setMsisdn(numeroPcs);
           
           LOGGER.info("Invocando servicio");
           ResultadoObtenerRutTitularType response = null;
           try{
          	 response = port.obtenerRutTitular(request);
           }catch (Exception e) {
          	 LOGGER.error("Exception caught on Service invocation: obtenerRutTitular", e);
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
  				
  				datosUsuarioTitular = new DatosUsuarioTitularBean();
  				
  				datosUsuarioTitular.setRutTitular(response.getRutTitular());
  				datosUsuarioTitular.setNumeroCuenta(response.getNumeroCuenta());
  				datosUsuarioTitular.setNombreCompletoTitular(response.getNombreCompletoTitular());  				
  				
  				return datosUsuarioTitular;
  				
  			}catch (Exception e) {
  				LOGGER.error(
                          "Exception inesperada al obtener rut del Usuario", e);
                  LOGGER.error( new DAOException(e));
  			}
   	    
   	   }else {
   		    LOGGER.info("Respuesta del Servicio : " +codigoRespuesta+ " - " + descripcionRespuesta);
   		    LOGGER.error( new ServiceException(codigoRespuesta, descripcionRespuesta));
   		}
   	return new DatosUsuarioTitularBean();
       }
      
      /**
       * Obtiene el plan actual de seguridad
       * @param msisdn
       * @return
       * @throws DAOException
       * @throws ServiceException
       */
      public InformacionTitularBean obtenerInformacionTitular(String msisdn) 
      	throws DAOException, ServiceException {
      	
    	InformacionTitularBean resultado = null;
      	
      	ClientePerfilServicePortType port = null;
          LOGGER.info("Instanciando el port " + ClientePerfilServicePortType.class);
          try {
              port = (ClientePerfilServicePortType) WebServiceLocator.getInstance().getPort(
            		  ClientePerfilService.class, ClientePerfilServicePortType.class);
          } catch (WebServiceLocatorException e) {
              LOGGER.error("Error al inicializar el Port " + ClientePerfilServicePortType.class, e);
              LOGGER.error( new DAOException(e));
          }
          
          ObtenerRutTitularType request = new ObtenerRutTitularType();
          ResultadoObtenerRutTitularType response = null;

          try {

              LOGGER.info("Configurando Datos de la peticion");
              request.setMsisdn(msisdn);
              
              LOGGER.info("Invocando servicio");
              response = port.obtenerRutTitular(request);

          } catch (Exception e) {
              LOGGER.error("Exception caught on Service invocation: obtenerRutTitular",
                      e);
              LOGGER.error( new DAOException(e));
          }

          String codigoRespuesta = response.getRespuesta().getCodigo();
          String descripcionRespuesta = response.getRespuesta().getDescripcion();

          LOGGER.info("codigoRespuesta " + codigoRespuesta);
          LOGGER.info("descripcionRespuesta " + descripcionRespuesta);

          if (Utils.isEmptyString(codigoRespuesta)) {
              LOGGER.error( new DAOException("obtenerRutTitular: Servicio no responde "
                      + "con codigoRespuesta"));
          }

          if (CODIGO_RESPUESTA_OK.equals(codigoRespuesta)) {

              try {
            	  //Se inicializa el objeto a devolver por este metodo.
            	  resultado = new InformacionTitularBean();
            	  //Extrayendo datos de la respuesta
            	  resultado.setCiudadTitular(response.getCiudadTitular());
            	  resultado.setComunaTitular(response.getComunaTitular());
            	  resultado.setDireccionTitular(response.getDireccionTitular());
            	  resultado.setRutTitular(response.getRutTitular());
            	  resultado.setNombreCompletoTitular(response.getNombreCompletoTitular());
            	  resultado.setNumeroCuenta(response.getNumeroCuenta());
            	  resultado.setNumeroDireccionTitular(response.getNumeroDireccionTitular());
            	  resultado.setParametro10Titular(response.getParametro10Titular());
            	  resultado.setParametro11Titular(response.getParametro11Titular());
            	  resultado.setParametro12Titular(response.getParametro12Titular());
            	  resultado.setParametro13Titular(response.getParametro13Titular());
            	  resultado.setParametro14Titular(response.getParametro14Titular());
            	  resultado.setParametro5Titular(response.getParametro5Titular());
              } catch (Exception e) {
                  LOGGER.error("Exception caught on Service response: "
                          + "obtenerRutTitular", e);
                  LOGGER.error( new DAOException(e));
              }

          }
          else {
              LOGGER.info("obtenerRutTitular: Service error code received: "
                      + codigoRespuesta + " - " + descripcionRespuesta);
              LOGGER.error( new ServiceException(codigoRespuesta, descripcionRespuesta));
          }
          
          return resultado;
          
      }
     
      public String consultarCategoriaCliente(String msisdn) throws DAOException, ServiceException {
    	  String categoriaCliente = "";

    	  
    	  ClientePerfilServicePortType port = null;
          LOGGER.info("Instanciando el port " + ClientePerfilServicePortType.class);
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
              request.setMsisdn(msisdn);

              LOGGER.info("Invocando servicio");
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
              LOGGER.error( new DAOException("consultarUsuarioBuic: Servicio no responde "
                      + "con codigoRespuesta"));
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
      
      
      /**
       * Metodo que recupea la informacion sobre plan contratado por un usuario
       * @param numeroPcs
       * @return
       * @throws DAOException
       * @throws ServiceException
       */
      public PlanContratadoBean consultarPlanContratadoPP(String numeroPcs)
		      throws DAOException, ServiceException {
		
		  LOGGER.info("Instanciando el Port.");
		  ClientePerfilServicePortType port = null;
		  try {
		      port = (ClientePerfilServicePortType) WebServiceLocator
		              .getInstance().getPort(ClientePerfilService.class,
		                      ClientePerfilServicePortType.class);
		
		  } catch (WebServiceLocatorException e) {
		      LOGGER.error("Error al inicializar el Port "
		              + ClientePerfilService.class);
		      LOGGER.error( new DAOException(e));
		  }
		
		  ConsultarPlanContratadoPPType request = new ConsultarPlanContratadoPPType();
		  ResultadoConsultarPlanContratadoPPType response = null;
		
		  try {
		      LOGGER.info("Configurando parametros de la peticion");
		      request.setMsisdn(numeroPcs);
		      
		      LOGGER.info("Invocando servicio");
		      response = port.consultarPlanContratadoPP(request);
		
		  } catch (Exception e) {
		      LOGGER.error("Exception caught on Service invocation: consultarPlanContratadoPP", e);            
		      LOGGER.error( new DAOException(e));
		  }
		
		  String codigoRespuesta = response.getRespuesta().getCodigo();
		  String descripcionRespuesta = response.getRespuesta()
		          .getDescripcion();
		
		  LOGGER.info("codigoRespuesta " + codigoRespuesta);
		  LOGGER.info("descripcionRespuesta " + descripcionRespuesta);
		  
		  if (Utils.isEmptyString(codigoRespuesta)) {
			  LOGGER.error( new DAOException("consultarPlanContratadoPP: Servicio no responde "
		        + "con codigoRespuesta"));
		  }
		
		  if (CODIGO_RESPUESTA_OK_CORTO.equals(codigoRespuesta) || 
				  CODIGO_RESPUESTA_OK.equals(codigoRespuesta)) {
		      
		      try {
		    	  ResPlanContratadoPPType planType = response.getResPlanContratadoPP();
		          PlanContratadoBean usuarioBean = buildPlanContratadoBean(planType);
		          return usuarioBean;
		          
		      } catch (Exception e) {
		          LOGGER.error("Exception caught on Service response: "
	                    + "consultarPlanContratadoPP", e);            
		          LOGGER.error( new DAOException(e));            }
		  }
		  else {
		      LOGGER.info("consultarPlanContratadoPP: Service error code received: "
		    		  + codigoRespuesta + " - " + descripcionRespuesta);
		      LOGGER.error( new ServiceException(codigoRespuesta, descripcionRespuesta));
		  }
		  return new PlanContratadoBean();
		}
      
      
      /**
       * Actualiza solo el correo del usuario
       * @param numeroPcs
       * @param rut
       * @param usuario
       * @param dominio
       * @return
       * @throws DAOException
       * @throws ServiceException
       */
      public boolean actualizarCorreoBuzon(
    		  String numeroPcs, String rut, String usuario, String dominio)
		      throws DAOException, ServiceException {
		
    	  boolean resultado = false;
    	  
		  LOGGER.info("Instanciando el Port.");
		  ClientePerfilServicePortType port = null;
		  try {
		      port = (ClientePerfilServicePortType) WebServiceLocator
		              .getInstance().getPort(ClientePerfilService.class,
		                      ClientePerfilServicePortType.class);
		
		  } catch (WebServiceLocatorException e) {
		      LOGGER.error("Error al inicializar el Port "
		              + ClientePerfilService.class);
		      LOGGER.error( new DAOException(e));
		  }
		
		  ActualizarCorreoBuzonType request = new ActualizarCorreoBuzonType();
		  ResultadoActualizarCorreoBuzonType response = null;
		
		  try {
		      LOGGER.info("Configurando parametros de la peticion");
		      request.setMsisdn(numeroPcs);
		      request.setRutUsuario(rut);
		      request.setUsuarioMail(usuario);
		      request.setDominioMail(dominio);
		      
		      LOGGER.info("Invocando servicio");
		      response = port.actualizarCorreoBuzon(request);
		
		  } catch (Exception e) {
		      LOGGER.error("Exception caught on Service invocation: actualizarCorreoBuzon", e);            
		      LOGGER.error( new DAOException(e));
		  }
		
		  String codigoRespuesta = response.getRespuesta().getCodigo();
		  String descripcionRespuesta = response.getRespuesta()
		          .getDescripcion();
		
		  LOGGER.info("codigoRespuesta " + codigoRespuesta);
		  LOGGER.info("descripcionRespuesta " + descripcionRespuesta);
		  
		  if (Utils.isEmptyString(codigoRespuesta)) {
			  LOGGER.error( new DAOException("actualizarCorreoBuzon: Servicio no responde "
		        + "con codigoRespuesta"));
		  }
		
		  if (CODIGO_RESPUESTA_OK.equals(codigoRespuesta)) {
		      try {
		    	  resultado = !Utils.isEmptyString(response.getRutUsuario().getRutUsuario());
		          
		      } catch (Exception e) {
		          LOGGER.error("Exception caught on Service response: "
		                + "actualizarCorreoBuzon", e);            
		          LOGGER.error( new DAOException(e));            }
		  }
		  else {
		      LOGGER.info("actualizarCorreoBuzon: Service error code received: "
		    		  + codigoRespuesta + " - " + descripcionRespuesta);
		      LOGGER.error( new ServiceException(codigoRespuesta, descripcionRespuesta));
		  }
		  
		  return resultado;
		}
      
      
      /**
       * Metodo utilitario para construir un objeto de tipo PlanContratadoBean
       * @param plan objeto que viene en la respuesta de la operacion de servicio
       * consultarPlanContratadoPP en ClientePerfilService
       * @return
       * @throws Exception
       */
      private PlanContratadoBean buildPlanContratadoBean(ResPlanContratadoPPType plan) 
      		throws DAOException {
    	  
    	  PlanContratadoBean planContratadoBean = null;
    	  
    	  if (plan == null) {
			  LOGGER.error( new DAOException("buildPlanContratadoBean: No existe informacion de saldo. "));
		  } else {
			  planContratadoBean = new PlanContratadoBean();
			  
			  planContratadoBean.setCdgPlan(plan.getCdgPlan());
	    	  planContratadoBean.setDscPlan(plan.getDscPlan());
	    	  planContratadoBean.setEstMovil(plan.getEstMovil());
	    	  
	    	  try{
	    		  /* Formato de fecha 20111115161749 */
		    	  planContratadoBean.setFechaActivacion(DateHelper.parseDate(
		    			  plan.getFechaActivacion(), DateHelper.FORMAT_yyyyMMddhhmmss));
		    	  planContratadoBean.setFechaDesactivacion(DateHelper.parseDate(
		    			  plan.getFechaDesactivacion(), DateHelper.FORMAT_yyyyMMddhhmmss));
		    	  planContratadoBean.setFechaExpiracion(DateHelper.parseDate(
		    			  plan.getFechaExpiracion(), DateHelper.FORMAT_yyyyMMddhhmmss));
		    	  planContratadoBean.setFechaUltimoCambio(DateHelper.parseDate(
		    			  plan.getFechaUltimoCambio(), DateHelper.FORMAT_yyyyMMddhhmmss));
	    	  }catch(Exception e){
	    		  LOGGER.error( new DAOException("buildPlanContratadoBean: Informacion de fechas invalida. "));
	    	  }
	    	  
	    	  planContratadoBean.setIccid(plan.getIccid());
	    	  planContratadoBean.setImsi(plan.getImsi());
	    	  planContratadoBean.setMsisdn(plan.getMsisdn());
	    	  planContratadoBean.setNumeroRecarga(plan.getNumeroRecarga());
	    	  planContratadoBean.setPin1(plan.getPin1());
	    	  planContratadoBean.setPin2(plan.getPin2());
	    	  planContratadoBean.setPuk1(plan.getPuk1());
	    	  planContratadoBean.setPuk2(plan.getPuk2());
	    	  
	    	  if(!Utils.isEmptyString(plan.getSaldo())){
	    		  planContratadoBean.setSaldo(plan.getSaldo());
		      }else{
		    	  LOGGER.warn("Saldo invalido para movil: " + plan.getMsisdn());
		    	  planContratadoBean.setSaldo("0");
		      }
		  }
    	  
    	  return planContratadoBean;
      }


/**
	 * Obtiene datos de cuenta de un cliente
	 * @param rut
	 * @return
	 * @throws DAOException
	 * @throws ServiceException
	 */
	public List<CuentaClienteBean> obtenerCuentaCliente(String rut)
			throws DAOException, ServiceException {

		List<CuentaClienteBean> cuentaClienteBeans = new ArrayList<CuentaClienteBean>();

		LOGGER.info("Instanciando el Port.");
		ClienteContactoServicePortType port = null;
		try{
			port = (ClienteContactoServicePortType) WebServiceLocator.getInstance().getPort(ClienteContactoService.class, ClienteContactoServicePortType.class);

		} catch (WebServiceLocatorException e) {
			LOGGER.error("Error al inicializar el Port "+ ClienteContactoService.class);
			LOGGER.error( new DAOException(e));
		}

		ObtenerCuentasClienteType request = new ObtenerCuentasClienteType();
		ResultadoObtenerCuentasClienteType response = null;

		try {
			
			LOGGER.info("Configurando parametros de la peticion");
			request.setRut(rut);
			LOGGER.info("Invocando servicio");
			response = port.obtenerCuentasCliente(request);

		} catch (Exception e) {
			LOGGER.error("Exception caught on Service invocation: consultarMisDatos",e);
			LOGGER.error( new DAOException(e));
		}

		String codigoRespuesta = response.getRespuesta().getCodigo();
		String descripcionRespuesta = response.getRespuesta().getDescripcion();

		LOGGER.info("codigoRespuesta " + codigoRespuesta);
		LOGGER.info("descripcionRespuesta " + descripcionRespuesta);

		if (CODIGO_RESPUESTA_OK.equals(codigoRespuesta)) {

			try {

				String codigoSegmento = response.getCodSegmento();

				for (CuentasClienteType iterable_element : response.getCuentasCliente()) {
					CuentaClienteBean cuentaClienteBean = new CuentaClienteBean();
					cuentaClienteBean.setCodGrupo(iterable_element.getCodGrupo());
					cuentaClienteBean.setDescGrupo(iterable_element.getDescGrupo());
					cuentaClienteBean.setNroCuenta(iterable_element.getNroCuenta());
					cuentaClienteBean.setDireccion(iterable_element.getDireccion());
					cuentaClienteBean.setRazonSocial(iterable_element.getRazonSocial());
					cuentaClienteBean.setCodigoSegmento(codigoSegmento);
					cuentaClienteBeans.add(cuentaClienteBean);
				}

			} catch (Exception e) {
				LOGGER.error("Exception caught on Service response", e);
				LOGGER.error( new DAOException(e));
			}
			
		} else {
			LOGGER.info("Service error code received: " + codigoRespuesta+ " - " + descripcionRespuesta);
			LOGGER.error( new ServiceException(codigoRespuesta, descripcionRespuesta));
		}

		return cuentaClienteBeans;

	}
	
	/**
     * Metodo que permite desplegar una lista con los moviles asociados a una
     * a una cuenta, en principio este metodo tiene logica pensada para desplegar 
     * moviles en el formulario de reclamos.
     * 
     * @param msisdnUsuario
     *            numero del administrador de la cuenta
     * @return una lista con los datos de los moviles asociados
     * @throws DAOException
     *             en caso de algun problema al obtener la informacion del
     *             servicio de perfilamiento.
     */
    public List<MsisdnAsociadoBean> getListaMsisdnAsociadosReclamos(String msisdnUsuario, String subMercado, String flagBam)
            throws DAOException {
        List<MsisdnAsociadoBean> msisdnAsociados = new ArrayList<MsisdnAsociadoBean>();
        LOGGER.info("Inicio getListaMsisdnAsociados");

        ClientePerfilServicePortType port = null;
        ConsultarSelectorCuentaType selectorCuentaType = new ConsultarSelectorCuentaType();
        selectorCuentaType.setMsisdn(msisdnUsuario);
        ResultadoConsultarSelectorCuentaType response = null;
        try {

            port = (ClientePerfilServicePortType) WebServiceLocator
                    .getInstance().getPort(ClientePerfilService.class,
                            ClientePerfilServicePortType.class);

        } catch (WebServiceLocatorException e) {
            LOGGER.error("Error al inicializar el Port "
                    + ClientePerfilService.class);
            LOGGER.error( new DAOException(e));

        }
        response = port.consultarSelectorCuenta(selectorCuentaType);

        if (response != null && response.getRespuesta() != null) {
            LOGGER
                    .info("Respuesta del servicio [ClientePerfilService/consultarSelectorCuenta]: codigo ["
                            + response.getRespuesta().getCodigo()
                            + "], descripcion ["
                            + response.getRespuesta().getDescripcion() + "]");
            if (response.getDetalleMsisdn() != null
                    && response.getDetalleMsisdn().size() > 0) {
                List<DetalleMsisdnType> msisdns = response.getDetalleMsisdn();
                for (DetalleMsisdnType msisdnType : msisdns) {
                    MsisdnAsociadoBean msisdnAsociado = new MsisdnAsociadoBean();
                    msisdnAsociado.setNumeroPcs(msisdnType.getMsisdn());
                    msisdnAsociado.setFlagRegistrado(msisdnType.getFlagRegistro());
                    if (DEFAULT_MOVIL_REGISTERED_STATUS
                            .equalsIgnoreCase(msisdnType.getFlagRegistro())) {
                        try {
                            String resultFlagBam = msisdnType.getFlagBam().equals("SESION") ? flagBam : msisdnType.getFlagBam();
                            msisdnAsociado.setFlagBam(Integer.parseInt(resultFlagBam));
                            msisdnAsociado.setDescripcionTipoMsisdn(SelectorCuentaHelper.formatearDescripcion(
                                            Integer.parseInt(resultFlagBam), msisdnType
                                                    .getMsisdn(), msisdnType
                                                    .getMercado()));
                        } catch (NumberFormatException nfe) {
                            LOGGER
                                    .error("No se pudo parsear el numero de flagBAM. El servicio trajo el valor ["
                                            + msisdnType.getFlagBam() + "]");
                        }
                        msisdnAsociado.setFlagEstadoMsisdn(msisdnType.getFlagEstadoMsisdn());                        
                        msisdnAsociado.setMercado(msisdnType.getMercado());
                        msisdnAsociado.setAaa(msisdnType.getAAA());
                        
                        String resultSubMercado = msisdnType.getSubMercado().equals("SESION") ? subMercado : msisdnType.getSubMercado();
                        msisdnAsociado.setSubMercado(resultSubMercado);
                    }
                    else {
                    	if(msisdnType.getMercado()!=null && msisdnType.getMercado()!= DEFAULT_ATRIBUTO_SIN_DATO){
							String resultFlagBam = (msisdnType.getFlagBam()
									.equals("SESION") || msisdnType
									.getFlagBam().equals(
											DEFAULT_ATRIBUTO_SIN_DATO)) ? flagBam
									: msisdnType.getFlagBam();
                    		msisdnAsociado.setDescripcionTipoMsisdn(SelectorCuentaHelper.formatearDescripcion(
                                    Integer.parseInt(resultFlagBam), msisdnType
                                            .getMsisdn(), msisdnType
                                            .getMercado()));
                    		msisdnAsociado.setMercado(msisdnType.getMercado());
							String resultSubMercado = (msisdnType
									.getSubMercado().equals("SESION") || msisdnType
									.getSubMercado().equals(
											DEFAULT_ATRIBUTO_SIN_DATO)) ? subMercado
									: DEFAULT_ATRIBUTO_SIN_DATO;
                            msisdnAsociado.setSubMercado(resultSubMercado);
                            msisdnAsociado.setDescripcionTipoMsisdn(msisdnType
                                    .getMsisdn().concat(" No Registrado"));
                    	}
                    }
                    msisdnAsociados.add(msisdnAsociado);
                }
            }
        }
        else {
            LOGGER
                    .error("No hubo respuesta del servicio [ClientePerfilService/consultarSelectorCuenta]");
            LOGGER.error( new DAOException(
                    "No hubo respuesta del servicio [ClientePerfilService/consultarSelectorCuenta] "));
        }
        return msisdnAsociados;

    }
	
	
}
