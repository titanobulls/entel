
package com.epcs.billing.registrouso;

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
@WebServiceClient(name = "BillingRegistroUsoService", targetNamespace = "http://www.epcs.com/billing/registrouso", wsdlLocation = "BillingRegistroUsoService.wsdl")
public class BillingRegistroUsoService
    extends Service
{

    private final static URL BILLINGREGISTROUSOSERVICE_WSDL_LOCATION;
    private final static Logger logger = Logger.getLogger(com.epcs.billing.registrouso.BillingRegistroUsoService.class.getName());

    static {
        URL url = null;
        try {
            URL baseUrl;
            baseUrl = com.epcs.billing.registrouso.BillingRegistroUsoService.class.getResource(".");
            url = new URL(baseUrl, "BillingRegistroUsoService.wsdl");
        } catch (MalformedURLException e) {
            logger.warning("Failed to create URL for the wsdl Location: 'BillingRegistroUsoService.wsdl', retrying as a local file");
            logger.warning(e.getMessage());
        }
        BILLINGREGISTROUSOSERVICE_WSDL_LOCATION = url;
    }

    public BillingRegistroUsoService(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public BillingRegistroUsoService() {
        super(BILLINGREGISTROUSOSERVICE_WSDL_LOCATION, new QName("http://www.epcs.com/billing/registrouso", "BillingRegistroUsoService"));
    }

    /**
     * 
     * @return
     *     returns BillingRegistroUsoServicePortType
     */
    @WebEndpoint(name = "BillingRegistroUsoServicePort")
    public BillingRegistroUsoServicePortType getBillingRegistroUsoServicePort() {
        return super.getPort(new QName("http://www.epcs.com/billing/registrouso", "BillingRegistroUsoServicePort"), BillingRegistroUsoServicePortType.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns BillingRegistroUsoServicePortType
     */
    @WebEndpoint(name = "BillingRegistroUsoServicePort")
    public BillingRegistroUsoServicePortType getBillingRegistroUsoServicePort(WebServiceFeature... features) {
        return super.getPort(new QName("http://www.epcs.com/billing/registrouso", "BillingRegistroUsoServicePort"), BillingRegistroUsoServicePortType.class, features);
    }

}
