/* Propiedad de Entel PCS. Todos los derechos reservados */
package com.epcs.inteligencianegocio.satisfaccioncliente.dao;

import java.io.Serializable;

import org.apache.log4j.Logger;

import com.epcs.bean.MarcaEstadisticaBean;
import com.epcs.inteligencianegocios.satisfaccioncliente.SatisfaccionClienteService;
import com.epcs.inteligencianegocios.satisfaccioncliente.SatisfaccionClienteServicePortType;
import com.epcs.inteligencianegocios.satisfaccioncliente.types.AgregarMarcaEstadisticaType;
import com.epcs.inteligencianegocios.satisfaccioncliente.types.ResultadoAgregarMarcaEstadisticaType;
import com.epcs.recursoti.configuracion.DateHelper;
import com.epcs.recursoti.configuracion.MiEntelProperties;
import com.epcs.recursoti.configuracion.locator.WebServiceLocator;
import com.epcs.recursoti.configuracion.locator.WebServiceLocatorException;
import com.epcs.recursoti.excepcion.DAOException;
import com.epcs.recursoti.excepcion.ServiceException;

/**
 * @author jmanzur (I2B) en nombre de Absalon Opazo (Atencion al Cliente, EntelPcs)
 *
 */
public class EstadisticasDAO implements Serializable {
    
    
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger
    		.getLogger(EstadisticasDAO.class);
    public static final String CODIGO_RESPUESTA_OK = MiEntelProperties
    		.getProperty("servicios.codigoRespuesta.exito");
    
    
    
    /**
     * 
     * @param marcaEstadisticaBean
     * @throws DAOException
     * @throws ServiceException
     */
    public void agregarMarcaEstadistica(MarcaEstadisticaBean  marcaEstadisticaBean)
            throws DAOException, ServiceException {
        LOGGER.info("Instanciando el Port.");
        SatisfaccionClienteServicePortType port = null;
        
        try {
            port = (SatisfaccionClienteServicePortType) WebServiceLocator
                    .getInstance().getPort(SatisfaccionClienteService.class,
                            SatisfaccionClienteServicePortType.class);
        } catch (WebServiceLocatorException e) {
            LOGGER.error("Error al inicializar el Port " + SatisfaccionClienteServicePortType.class);
            LOGGER.error( new DAOException(e));
        }

        AgregarMarcaEstadisticaType  request = new AgregarMarcaEstadisticaType();
        ResultadoAgregarMarcaEstadisticaType response = null;

        try {
            LOGGER.info("Configurando parametros de la peticion");
            
            String fechaOperacion = DateHelper.format(new java.util.Date(), "yyyyMMddHHmmss");
            
            request.setAtributoCliente(marcaEstadisticaBean.getAtributoCliente());
            request.setCampoOpcional1(marcaEstadisticaBean.getCampoOpcional1());
            request.setCampoOpcional2(marcaEstadisticaBean.getCampoOpcional2());
            request.setFechaHoraOperacion(fechaOperacion);
            request.setFlagExitoFracasoOperacion(marcaEstadisticaBean.getFlagExitoFracasoOperacion());
            request.setFuncionalidad(marcaEstadisticaBean.getFuncionalidad());
            request.setGrupo(marcaEstadisticaBean.getGrupo());
            request.setIp(marcaEstadisticaBean.getIp());
            request.setMsisdn(marcaEstadisticaBean.getMsisdn());
            request.setOrigen(marcaEstadisticaBean.getOrigen());
            request.setRut(marcaEstadisticaBean.getRut());
            request.setSegmento(marcaEstadisticaBean.getSegmento());
            request.setServicio(marcaEstadisticaBean.getServicio());
            
            LOGGER.info("Invocando servicio Agregar Estadisticas con datos : "
            		+ "[fechaOperacion="+fechaOperacion+"]"+marcaEstadisticaBean.toString());
            response = port.agregarMarcaEstadistica(request);
        } catch (Exception e) {
            LOGGER.error("Exception caught on Service invocation: "
            		+ "agregarMarcaEstadistica", e);
            LOGGER.error( new DAOException(e));
        }

        String codigoRespuesta = response.getRespuesta().getCodigo();
        String descripcionRespuesta = response.getRespuesta().getDescripcion();

        LOGGER.info("codigoRespuesta " + codigoRespuesta);
        LOGGER.info("descripcionRespuesta " + descripcionRespuesta);

        if (CODIGO_RESPUESTA_OK.equals(codigoRespuesta)) {
            try {
               
                
            } catch (Exception e) {
                LOGGER.error("Exception caught on Service response: " +
                		"agregarMarcaEstadistica", e);
                LOGGER.error( new DAOException(e));
            }
        }
        else {
            LOGGER.error("Service error code received: " + codigoRespuesta
                    + " - " + descripcionRespuesta);
            LOGGER.error( new ServiceException(codigoRespuesta, descripcionRespuesta));
        }
    }
    

}
