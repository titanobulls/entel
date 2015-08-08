/* Propiedad de Entel PCS. Todos los derechos reservados */
package com.epcs.inteligencianegocio.satisfaccioncliente.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.epcs.inteligencianegocios.satisfaccioncliente.SatisfaccionClienteService;
import com.epcs.inteligencianegocios.satisfaccioncliente.SatisfaccionClienteServicePortType;
import com.epcs.inteligencianegocios.satisfaccioncliente.types.ConsultarEncuestasPerfiladasOrqType;
import com.epcs.inteligencianegocios.satisfaccioncliente.types.EncuestasPerfiladasType;
import com.epcs.inteligencianegocios.satisfaccioncliente.types.ResultadoConsultarEncuestasPerfiladasOrqType;
import com.epcs.inteligencianegocios.satisfaccioncliente.types.UrlType;
import com.epcs.recursoti.configuracion.MiEntelProperties;
import com.epcs.recursoti.configuracion.locator.WebServiceLocator;
import com.epcs.recursoti.configuracion.locator.WebServiceLocatorException;
import com.epcs.recursoti.excepcion.DAOException;
import com.epcs.recursoti.excepcion.ServiceException;

/**
 * @author zmanotas (I2B)
 */
public class EncuestasPerfiladasDAO {

    private static final Logger LOGGER = Logger
            .getLogger(EncuestasPerfiladasDAO.class);
    public static final String CODIGO_RESPUESTA_OK = MiEntelProperties
            .getProperty("servicios.codigoRespuesta.exito");
    public static final String COD_VISUALIZACION = MiEntelProperties
    		.getProperty("encuestasPerfiladas.codVisualizacion");

    /**
     * Metodo que realiza una consulta para obtener las URL de encuestas
     * perfiladas, en caso de que el servicio no retorne ninguna URL, se
     * devolvera null
     * 
     * @param numero
     * @param rut
     * @param ipCliente
     * @param codVisualizacion
     * @param codMercado
     * @param codZona
     * @return Array de URLs que trae el servicio
     * @throws DAOException
     * @throws ServiceException
     */
    public List<String> consultarEncuestasPerfiladasOrq(String numero,
            String rut, String ipCliente, String codMercado, String codZona)
            throws DAOException, ServiceException {
        LOGGER.info("Instanciando el Port.");
        SatisfaccionClienteServicePortType port = null;
        
        try {
            port = (SatisfaccionClienteServicePortType) WebServiceLocator
                    .getInstance().getPort(SatisfaccionClienteService.class,
                            SatisfaccionClienteServicePortType.class);
        } catch (WebServiceLocatorException e) {
            LOGGER.error("Error al inicializar el Port " + SatisfaccionClienteService.class);
            LOGGER.error( new DAOException(e));
        }

        ConsultarEncuestasPerfiladasOrqType request = new ConsultarEncuestasPerfiladasOrqType();
        ResultadoConsultarEncuestasPerfiladasOrqType response = null;

        LOGGER.info("Configurando parametros de la peticion");

        request.setMsisdn(numero);
        request.setRut(rut);
        request.setIpCliente(ipCliente);
        request.setCodVisualizacion(COD_VISUALIZACION);
        request.setCodMercado(codMercado);
        request.setCodZona(codZona);

        LOGGER.info("Invocando servicio");
        try {
            response = port.consultarEncuestasPerfiladasOrq(request);
        } catch (Exception e) {
            LOGGER.error("Exception caught on Service invocation: "
            		+ "consultarEncuestasPerfiladasOrq", e);
            LOGGER.error( new DAOException(e));
        }

        String codigoRespuesta = response.getRespuesta().getCodigo();
        String descripcionRespuesta = response.getRespuesta().getDescripcion();

        LOGGER.info("codigoRespuesta " + codigoRespuesta);
        LOGGER.info("descripcionRespuesta " + descripcionRespuesta);

        if (CODIGO_RESPUESTA_OK.equals(codigoRespuesta)) {
            try {
                List<String> strUrls = null;
                int cantidadUrl = Integer.parseInt(response.getCantidadUrl());                
                
                if (cantidadUrl > 0) {
                    EncuestasPerfiladasType datosEncuesta = response.getConsultarEncuestasPerfiladasPS();
                    List<UrlType> urls = datosEncuesta.getUrls();
                    
                    if (urls != null && !urls.isEmpty()) {
                        strUrls = new ArrayList<String>();

                        for (UrlType url : urls) {
                            strUrls.add(url.getUrl());
                        }
                    }
                }
                
                return strUrls;
            } catch (Exception e) {
                LOGGER.error("Exception caught on Service response: "
                		+ "consultarEncuestasPerfiladasOrq", e);
                LOGGER.error( new DAOException(e));
            }
        }
        else {
            LOGGER.error("Service error code received: " + codigoRespuesta
                    + " - " + descripcionRespuesta);
            LOGGER.error( new ServiceException(codigoRespuesta, descripcionRespuesta));
        }
        return new ArrayList<String>();
    }
    
    
    
    
    
    
    
}
