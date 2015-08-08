/* Propiedad de Entel PCS. Todos los derechos reservados */
package com.epcs.recursoti.parametros.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.epcs.bean.CiudadBean;
import com.epcs.bean.ComunaBean;
import com.epcs.bean.RegionBean;
import com.epcs.cliente.perfil.ClientePerfilService;
import com.epcs.cliente.perfil.ClientePerfilServicePortType;
import com.epcs.cliente.perfil.types.CiudadType;
import com.epcs.cliente.perfil.types.ComunaType;
import com.epcs.cliente.perfil.types.ConsultarCiudadesPorRegionType;
import com.epcs.cliente.perfil.types.ConsultarComunasPorCiudadType;
import com.epcs.cliente.perfil.types.ConsultarRegionesType;
import com.epcs.cliente.perfil.types.RegionesType;
import com.epcs.cliente.perfil.types.ResultadoConsultarCiudadesPorRegionType;
import com.epcs.cliente.perfil.types.ResultadoConsultarComunasPorCiudadType;
import com.epcs.cliente.perfil.types.ResultadoConsultarRegionesType;
import com.epcs.recursoti.configuracion.MiEntelProperties;
import com.epcs.recursoti.configuracion.Utils;
import com.epcs.recursoti.configuracion.locator.WebServiceLocator;
import com.epcs.recursoti.configuracion.locator.WebServiceLocatorException;
import com.epcs.recursoti.excepcion.DAOException;
import com.epcs.recursoti.excepcion.ServiceException;

/**
 * DAO para la obtencion de listas de parametros, tales como ubicacion
 * geografica o listas de valores para formularios de Usuarios, y cuentas
 * 
 * @author jlopez (I2B) en nombre de Absalon Opazo (Atencion al Cliente,
 *         EntelPcs)
 * 
 */
public class ParametrosDAO implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private static final Logger LOGGER = Logger.getLogger(ParametrosDAO.class);

    public static final String CODIGO_RESPUESTA_OK = MiEntelProperties.getProperty("servicios.codigoRespuesta.exito");

    
    public ParametrosDAO() {
        super();
    }
    
    
    /**
     * Devuelve el listado de Regiones
     * @return
     * @throws DAOException
     */
    public List<RegionBean> getRegionesList() throws DAOException {

        List<RegionBean> regionesList = new ArrayList<RegionBean>();

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
        ConsultarRegionesType consultarRegionesType = new ConsultarRegionesType();
        consultarRegionesType.setIdRegion("");
        
        ResultadoConsultarRegionesType resultadoConsultarRegionesType = null;
        LOGGER.info("Invocando servicio");
        
        try{
        	resultadoConsultarRegionesType = port.consultarRegiones(consultarRegionesType);
        }catch (Exception e) {
        	LOGGER.error("Exception caught on Service invocation: "
            		+ "consultarRegiones", e);
			LOGGER.error( new DAOException(e));
		}

        String codigoRespuesta = resultadoConsultarRegionesType.getRespuesta().getCodigo();
        String descripcionRespuesta = resultadoConsultarRegionesType.getRespuesta()
                .getDescripcion();

        LOGGER.info("codigoRespuesta " + codigoRespuesta);
        LOGGER.info("descripcionRespuesta " + descripcionRespuesta);
        
        if (Utils.isEmptyString(codigoRespuesta)) {
            LOGGER.error( new DAOException("consultarRegiones: Servicio no respondio "
                    + "con codigoRespuesta"));
        }

        if (CODIGO_RESPUESTA_OK.equals(codigoRespuesta)) {
            
            List<RegionesType> listRegionesType = resultadoConsultarRegionesType.getRegion();
            if(listRegionesType != null && listRegionesType.size() > 0){
                for (RegionesType regionType : listRegionesType) {
                    regionesList.add(new RegionBean(regionType.getIdRegion(),regionType.getNombreRegion()));    
                }
    
            }
               
        }else{
            LOGGER.error("Error :"+codigoRespuesta+" Descripcion Resp :"+descripcionRespuesta );
            
        }
        
        return regionesList;

    }

    /**
     * Recupera la region que corresponda al id <code>regionId</code>.<br>
     * Si el servicio no encuentra una region para el id indicado, este metodo
     * retorna null.
     * 
     * @param regionId
     *            String id de la region solicitada
     * @return {@link RegionBean} con la region. null si para el id no se
     *         encuentra una region
     * @throws DAOException
     *             Si ocurre algun problema con la comunicacion del servicio
     * @throws ServiceException
     *             Si el servicio retorna algun codigo de error conocido
     */
    public RegionBean findRegionById(final String regionId)
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

        LOGGER.info("Configurando Datos de la peticion");
        ConsultarRegionesType request = new ConsultarRegionesType();
        request.setIdRegion(regionId);

        LOGGER.info("Invocando servicio");
        ResultadoConsultarRegionesType response = null;
        try {
            response = port.consultarRegiones(request);
        } catch (Exception e) {
            LOGGER.error("Exception caught on Service invocation: consultarRegiones", e);
            LOGGER.error( new DAOException(e));
        }

        String codigoRespuesta = response.getRespuesta().getCodigo();
        String descripcionRespuesta = response.getRespuesta().getDescripcion();

        LOGGER.info("codigoRespuesta " + codigoRespuesta);
        LOGGER.info("descripcionRespuesta " + descripcionRespuesta);

        if (Utils.isEmptyString(codigoRespuesta)) {
            LOGGER.error( new DAOException("consultarRegiones: Servicio no respondio "
                    + "con codigoRespuesta"));
        }

        if (CODIGO_RESPUESTA_OK.equals(codigoRespuesta)) {

            RegionBean regionBean = null;
            
            List<RegionesType> regionesList = response.getRegion();
            
            if(regionesList == null || regionesList.isEmpty()) {
                LOGGER.warn("Region no encontrada para el id '" + regionId + "'");
                
            } 
            else {

                if(regionesList.size() > 1) {
                    
                    LOGGER.warn("Servicio 'consultarRegiones' encontro mas de una region para '"
                            + regionId + "'"
                            + " Solo se considerara la primera ocurrencia");
                }
 
                RegionesType regionesType = regionesList.get(0);
                
                regionBean = new RegionBean(regionesType.getIdRegion(),
                        regionesType.getNombreRegion());

            }

            return regionBean;

        }
        else {
            LOGGER.error("consultarRegiones: Service error code received: "
                    + codigoRespuesta + " - " + descripcionRespuesta);
            LOGGER.error( new ServiceException(codigoRespuesta, descripcionRespuesta));
        }
        return null;
    }


    /**
     * Entrega la lista de ciudades de la region indicada en <code>region</code>
     * 
     * @param region
     *            {@link RegionBean} region de la que se desean las ciudades
     * @return List<CiudadBean> con las ciudades de la region
     *         <code>region</code>
     * @throws DAOException 
     */
    public List<CiudadBean> getCiudadesList(RegionBean region) throws DAOException {

        List<CiudadBean> ciudadesList = new ArrayList<CiudadBean>();

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

        ConsultarCiudadesPorRegionType consultarCiudadesPorRegion = new ConsultarCiudadesPorRegionType();
        consultarCiudadesPorRegion.setRegion(region.getCodigo());

        ResultadoConsultarCiudadesPorRegionType resultadoCiudadesPorRegion = null;
        LOGGER.info("Invocando servicio");
        try{
        	resultadoCiudadesPorRegion = port.consultarCiudadesPorRegion(consultarCiudadesPorRegion);
        }catch (Exception e) {
        	LOGGER.error("Exception caught on Service invocation: "
            		+ "consultarCiudadesPorRegion", e);
			LOGGER.error( new DAOException(e));
		}

        String codigoRespuesta = resultadoCiudadesPorRegion.getRespuesta().getCodigo();
        String descripcionRespuesta = resultadoCiudadesPorRegion.getRespuesta()
                .getDescripcion();

        LOGGER.info("codigoRespuesta " + codigoRespuesta);
        LOGGER.info("descripcionRespuesta " + descripcionRespuesta);
        
        if (Utils.isEmptyString(codigoRespuesta)) {
            LOGGER.error( new DAOException("consultarCiudadesPorRegion: Servicio no respondio "
                    + "con codigoRespuesta"));
        }

        if (CODIGO_RESPUESTA_OK.equals(codigoRespuesta)) {
            
            List<CiudadType> listCiudadType = resultadoCiudadesPorRegion.getCiudad();
            if(listCiudadType != null && listCiudadType.size() > 0){
                for (CiudadType ciudadType : listCiudadType) {
                    ciudadesList.add(new CiudadBean(ciudadType.getNombre(),ciudadType.getNombre()));    
                }
                    
            }
               
        }else{
            LOGGER.error("Error :"+codigoRespuesta+" Descripcion Resp :"+descripcionRespuesta );
            
        }
        
        return ciudadesList;

    }

    /**
     * Entrega la lista de comunas de la ciudad indicada en <code>ciudad</code>
     * 
     * @param ciudad
     *            {@link CiudadBean} ciudad de la que se desean las comunas
     * @return List<ComunaBean> con las comunas de la ciudad <code>ciudad</code>
     * @throws DAOException 
     */
    public List<ComunaBean> getComunasList(RegionBean region, CiudadBean ciudad) throws DAOException {

        List<ComunaBean> comunasList = new ArrayList<ComunaBean>();

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

        ConsultarComunasPorCiudadType consultarComunasPorCiudad = new ConsultarComunasPorCiudadType();
        consultarComunasPorCiudad.setCiudad(ciudad.getCodigo());
        consultarComunasPorCiudad.setRegion(region.getCodigo());
        consultarComunasPorCiudad.setIdComuna("");
        
        LOGGER.info("Invocando servicio");
        ResultadoConsultarComunasPorCiudadType resultadoComunaCiudades = null;
        try{
        	resultadoComunaCiudades = port.consultarComunasPorCiudad(consultarComunasPorCiudad);
        }catch (Exception e) {
        	LOGGER.error("Exception caught on Service invocation: "
            		+ "consultarComunasPorCiudad", e);
			LOGGER.error( new DAOException(e));
		}

        String codigoRespuesta = resultadoComunaCiudades.getRespuesta().getCodigo();
        String descripcionRespuesta = resultadoComunaCiudades.getRespuesta()
                .getDescripcion();

        LOGGER.info("codigoRespuesta " + codigoRespuesta);
        LOGGER.info("descripcionRespuesta " + descripcionRespuesta);
        
        if (Utils.isEmptyString(codigoRespuesta)) {
            LOGGER.error( new DAOException("consultarComunasPorCiudad: Servicio no respondio "
                    + "con codigoRespuesta"));
        }

        if (CODIGO_RESPUESTA_OK.equals(codigoRespuesta)) {
            
            List<ComunaType> comunaList = resultadoComunaCiudades.getComuna();
            for (ComunaType comunaType : comunaList) {
                comunasList.add(new ComunaBean(comunaType.getNombreComuna(),comunaType.getNombreComuna()));    
            }
               
        }else{
            
            LOGGER.error("Error :"+codigoRespuesta+" Descripcion Resp :"+descripcionRespuesta );
            
        }
        
     return comunasList;

    }
    
    /**
     * Obtener la descripcion de una comuna dado su codigo, region y ciudad
     * @param region
     * @param ciudad
     * @param codigo
     * @return ComunaBean
     * @throws DAOException
     */
    public ComunaBean getComuna(RegionBean region, CiudadBean ciudad, String codigo) throws DAOException{
        
        ComunaBean comuna = new ComunaBean("","");
        
        List<ComunaBean> listComunas = this.getComunasList(region, ciudad);
        
        for (ComunaBean comunaBean : listComunas) {
            if(comunaBean.getCodigo().equals(codigo)){
                return comunaBean;
            }
            
        }
         
        return comuna;
    }
}
