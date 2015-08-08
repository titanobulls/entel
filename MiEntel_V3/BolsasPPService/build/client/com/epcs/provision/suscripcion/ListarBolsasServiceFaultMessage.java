
package com.epcs.provision.suscripcion;

import javax.xml.ws.WebFault;
import com.epcs.provision.suscripcion.types.ListarBolsasServiceFaultType;


/**
 * This class was generated by the JAX-WS RI.
 * Oracle JAX-WS 2.1.4
 * Generated source version: 2.1
 * 
 */
@WebFault(name = "listarBolsasServiceFaultDocument", targetNamespace = "http://www.epcs.com/Provision/Suscripcion")
public class ListarBolsasServiceFaultMessage
    extends Exception
{

    /**
     * Java type that goes as soapenv:Fault detail element.
     * 
     */
    private ListarBolsasServiceFaultType faultInfo;

    /**
     * 
     * @param message
     * @param faultInfo
     */
    public ListarBolsasServiceFaultMessage(String message, ListarBolsasServiceFaultType faultInfo) {
        super(message);
        this.faultInfo = faultInfo;
    }

    /**
     * 
     * @param message
     * @param faultInfo
     * @param cause
     */
    public ListarBolsasServiceFaultMessage(String message, ListarBolsasServiceFaultType faultInfo, Throwable cause) {
        super(message, cause);
        this.faultInfo = faultInfo;
    }

    /**
     * 
     * @return
     *     returns fault bean: com.epcs.provision.suscripcion.types.ListarBolsasServiceFaultType
     */
    public ListarBolsasServiceFaultType getFaultInfo() {
        return faultInfo;
    }

}
