
package com.esa.clientes.perfilesclientes.resumenlineasequiposcliente;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Logger;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceFeature;


/**
 * This class was generated by the JAX-WS RI.
 * Oracle JAX-WS 2.1.4
 * Generated source version: 2.1
 * 
 */
@WebServiceClient(name = "resumenLineasEquiposService", targetNamespace = "http://www.esa.com/Clientes/PerfilesClientes/resumenLineasEquiposCliente", wsdlLocation = "ResumenLineasEquiposWS.wsdl")
public class ResumenLineasEquiposService
    extends Service
{

    private final static URL RESUMENLINEASEQUIPOSSERVICE_WSDL_LOCATION;
    private final static Logger logger = Logger.getLogger(com.esa.clientes.perfilesclientes.resumenlineasequiposcliente.ResumenLineasEquiposService.class.getName());

    static {
        URL url = null;
        try {
            URL baseUrl;
            baseUrl = com.esa.clientes.perfilesclientes.resumenlineasequiposcliente.ResumenLineasEquiposService.class.getResource(".");
            url = new URL(baseUrl, "ResumenLineasEquiposWS.wsdl");
        } catch (MalformedURLException e) {
            logger.warning("Failed to create URL for the wsdl Location: 'ResumenLineasEquiposWS.wsdl', retrying as a local file");
            logger.warning(e.getMessage());
        }
        RESUMENLINEASEQUIPOSSERVICE_WSDL_LOCATION = url;
    }

    public ResumenLineasEquiposService(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public ResumenLineasEquiposService() {
        super(RESUMENLINEASEQUIPOSSERVICE_WSDL_LOCATION, new QName("http://www.esa.com/Clientes/PerfilesClientes/resumenLineasEquiposCliente", "resumenLineasEquiposService"));
    }

    /**
     * 
     * @return
     *     returns ResumenLineasEquiposPort
     */
    @WebEndpoint(name = "resumenLineasEquiposPort")
    public ResumenLineasEquiposPort getResumenLineasEquiposPort() {
        return super.getPort(new QName("http://www.esa.com/Clientes/PerfilesClientes/resumenLineasEquiposCliente", "resumenLineasEquiposPort"), ResumenLineasEquiposPort.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns ResumenLineasEquiposPort
     */
    @WebEndpoint(name = "resumenLineasEquiposPort")
    public ResumenLineasEquiposPort getResumenLineasEquiposPort(WebServiceFeature... features) {
        return super.getPort(new QName("http://www.esa.com/Clientes/PerfilesClientes/resumenLineasEquiposCliente", "resumenLineasEquiposPort"), ResumenLineasEquiposPort.class, features);
    }

}
