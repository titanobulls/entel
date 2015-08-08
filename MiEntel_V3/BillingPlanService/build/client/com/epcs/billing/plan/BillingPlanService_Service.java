
package com.epcs.billing.plan;

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
@WebServiceClient(name = "BillingPlanService", targetNamespace = "http://www.epcs.com/billing/plan", wsdlLocation = "BillingPlanService.wsdl")
public class BillingPlanService_Service
    extends Service
{

    private final static URL BILLINGPLANSERVICE_WSDL_LOCATION;
    private final static Logger logger = Logger.getLogger(com.epcs.billing.plan.BillingPlanService_Service.class.getName());

    static {
        URL url = null;
        try {
            URL baseUrl;
            baseUrl = com.epcs.billing.plan.BillingPlanService_Service.class.getResource(".");
            url = new URL(baseUrl, "BillingPlanService.wsdl");
        } catch (MalformedURLException e) {
            logger.warning("Failed to create URL for the wsdl Location: 'BillingPlanService.wsdl', retrying as a local file");
            logger.warning(e.getMessage());
        }
        BILLINGPLANSERVICE_WSDL_LOCATION = url;
    }

    public BillingPlanService_Service(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public BillingPlanService_Service() {
        super(BILLINGPLANSERVICE_WSDL_LOCATION, new QName("http://www.epcs.com/billing/plan", "BillingPlanService"));
    }

    /**
     * 
     * @return
     *     returns BillingPlanService
     */
    @WebEndpoint(name = "BillingPlanServicePort")
    public BillingPlanService getBillingPlanServicePort() {
        return super.getPort(new QName("http://www.epcs.com/billing/plan", "BillingPlanServicePort"), BillingPlanService.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns BillingPlanService
     */
    @WebEndpoint(name = "BillingPlanServicePort")
    public BillingPlanService getBillingPlanServicePort(WebServiceFeature... features) {
        return super.getPort(new QName("http://www.epcs.com/billing/plan", "BillingPlanServicePort"), BillingPlanService.class, features);
    }

}
