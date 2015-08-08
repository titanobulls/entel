/* Propiedad de Entel PCS. Todos los derechos reservados */
package com.epcs.administracion.suscripciones.dao;

import org.apache.log4j.Logger;

import com.epcs.bean.BolsaActivaBAMCCPP;
import com.epcs.bean.BolsaDisponibleBAMCCPP;
import com.epcs.bean.ResumenBolsasActivasBAMCCPP;
import com.epcs.bean.ResumenBolsasDisponiblesBAMCCPP;
import com.epcs.provision.suscripcion.ComprarServiceFaultMessage;
import com.epcs.provision.suscripcion.ListarBolsasActivasBAMServiceFaultMessage;
import com.epcs.provision.suscripcion.ListarBolsasDisponiblesBAMServiceFaultMessage;
import com.epcs.provision.suscripcion.SCOBPortType;
import com.epcs.provision.suscripcion.SCOBPortTypeSOAPBindingQSService;
import com.epcs.provision.suscripcion.types.ComprarRequestType;
import com.epcs.provision.suscripcion.types.ComprarResponseType;
import com.epcs.provision.suscripcion.types.ListarBolsasActivasBAMResponseType;
import com.epcs.provision.suscripcion.types.ListarBolsasDisponiblesBAMResponseType;
import com.epcs.provision.suscripcion.types.RequestType;
import com.epcs.provision.suscripcion.types.ServiceFaultType;
import com.epcs.recursoti.configuracion.DateHelper;
import com.epcs.recursoti.configuracion.MiEntelProperties;
import com.epcs.recursoti.configuracion.Utils;
import com.epcs.recursoti.configuracion.WordNumberHelper;
import com.epcs.recursoti.configuracion.locator.WebServiceLocator;
import com.epcs.recursoti.configuracion.locator.WebServiceLocatorException;
import com.epcs.recursoti.excepcion.DAOException;
import com.epcs.recursoti.excepcion.ServiceException;

/**
 * @author zmanotas (I2B) en nombre de Absalon Opazo (Atencion al Cliente,
 *         EntelPcs)
 * 
 */
public class BolsasBAMCCPPDAO {
    
    private static final String CANAL = MiEntelProperties.getProperty("bolsasBAMCCPP.canal");    

    /**
     * Logger para BolsasBAMCCPPDAO
     */
    private static final Logger LOGGER = Logger
            .getLogger(BolsasBAMCCPPDAO.class);

    public ResumenBolsasDisponiblesBAMCCPP listarBolsasDisponibles(String msisdn) throws DAOException,
            ServiceException {
        SCOBPortType port;
        LOGGER.info("Instanciando el port " + SCOBPortType.class);
        try {
            port = (SCOBPortType) WebServiceLocator.getInstance().getPort(
                    SCOBPortTypeSOAPBindingQSService.class, SCOBPortType.class);

            RequestType request = new RequestType();
            ListarBolsasDisponiblesBAMResponseType.Mensaje response = null;

            try {
                LOGGER.info("Configurando Datos de la peticion");
                request.setMsisdn(WordNumberHelper.getPrefijoEntel() + msisdn);
                request.setCanal(CANAL);
                LOGGER.info("Invocando servicio");
                response = port.listarBolsasDisponiblesBAMRequestDocument(request);
            } catch (ListarBolsasDisponiblesBAMServiceFaultMessage e) {
                ServiceFaultType mensaje = e.getFaultInfo().getMensaje();
                String codigoRespuesta = mensaje.getCodigo();
                String descripcionRespuesta = mensaje.getDescripcion();
                
                LOGGER.error("Service error code received: " + codigoRespuesta
                        + " - " + descripcionRespuesta);
                LOGGER.error( new ServiceException(codigoRespuesta, descripcionRespuesta));                
            } catch (Exception e) {
                LOGGER.error("Exception caught on Service invocation: "
                        + "listarBolsasDisponiblesBAMRequestDocument", e);
            }
            
            try {
                ResumenBolsasDisponiblesBAMCCPP resumen = new ResumenBolsasDisponiblesBAMCCPP();
                resumen.setMercado(response.getMovil().getMercado());
                resumen.setMsisdn(response.getMovil().getMsisdn());
                resumen.setSaldoPlan(response.getMovil().getSaldoPlan());                
                resumen.setFechaExpiracionSaldo(response.getMovil().getFechaExpiracionSaldo());
                resumen.setGrupo(response.getMovil().getGrupo());
                resumen.setLimiteCreditoTotal(new Double(response.getMovil().getLimiteCreditoTotal()));
                resumen.setLimiteCreditoAcumulado(new Double(response.getMovil().getLimiteCreditoAcumulado()));
                
                for (ListarBolsasDisponiblesBAMResponseType.Mensaje.ListadoCartas.DetalleCartaServicio det : response
                        .getListadoCartas().getDetalleCartaServicio()) {
                    BolsaDisponibleBAMCCPP b = new BolsaDisponibleBAMCCPP();
                    
                    b.setCodigo(det.getCodigo());
                    b.setNombre(det.getNombre());
                    b.setDescripcion(det.getDescripcion());
                    b.setCuotaInicialVoucher(det.getCuotaInicialVoucher());
                    b.setPrecio(det.getPrecio());                    
                    
                    resumen.addBolsa(b);
                }
                return resumen;
            } catch (Exception e) {
                LOGGER.error("Exception caught on Service response: "
                        + "listarBolsasDisponiblesBAMRequestDocument", e);
                LOGGER.error( new DAOException(e));
            }
        } catch (WebServiceLocatorException e) {
            LOGGER.error("Error al inicializar el Port " + SCOBPortType.class,
                    e);
            LOGGER.error( new DAOException(e));
        }
        return new ResumenBolsasDisponiblesBAMCCPP();
    }

    public ResumenBolsasActivasBAMCCPP listarBolsasActivas(String msisdn) throws DAOException,
            ServiceException {
        SCOBPortType port;
        LOGGER.info("Instanciando el port " + SCOBPortType.class);
        try {
            port = (SCOBPortType) WebServiceLocator.getInstance().getPort(
                    SCOBPortTypeSOAPBindingQSService.class, SCOBPortType.class);

            RequestType request = new RequestType();
            ListarBolsasActivasBAMResponseType.Mensaje response = null;

            try {
                LOGGER.info("Configurando Datos de la peticion");
                request.setMsisdn(WordNumberHelper.getPrefijoEntel() + msisdn);
                request.setCanal(CANAL);
                LOGGER.info("Invocando servicio");
                response = port.listarBolsasActivasBAMRequestDocument(request);
            } catch (ListarBolsasActivasBAMServiceFaultMessage e) {
                ServiceFaultType mensaje = e.getFaultInfo().getMensaje();
                String codigoRespuesta = mensaje.getCodigo();
                String descripcionRespuesta = mensaje.getDescripcion();
                
                LOGGER.error("Service error code received: " + codigoRespuesta
                        + " - " + descripcionRespuesta);
                LOGGER.error( new ServiceException(codigoRespuesta, descripcionRespuesta));                
            } catch (Exception e) {
                LOGGER.error("Exception caught on Service invocation: "
                        + "listarBolsasActivasBAMRequestDocument", e);
                LOGGER.error( new DAOException(e));
            }

            try {
                ListarBolsasActivasBAMResponseType.Mensaje.Movil mov = response.getMovil();
                
                ResumenBolsasActivasBAMCCPP resumen = new ResumenBolsasActivasBAMCCPP();
                resumen.setMercado(response.getMovil().getMercado());
                resumen.setMsisdn(response.getMovil().getMsisdn());
                resumen.setTariffIdOPSC(response.getMovil().getTariffIdOPSC());
                resumen.setSaldoPlan(mov.getSaldoPlan());
                resumen.setFechaExpiracionSaldo(response.getMovil().getFechaExpiracionSaldo());
                resumen.setCuotaUtilizadaPlan(Utils.formatMoney(new Double(mov.getCuotaUtilizadaPlan())));
                
                for (ListarBolsasActivasBAMResponseType.Mensaje.CartasActivas.DetalleCartaServicio det : response
                        .getCartasActivas().getDetalleCartaServicio()) {
                    BolsaActivaBAMCCPP b = new BolsaActivaBAMCCPP();
                    
                    b.setCodigo(det.getCodigo());
                    b.setNombre(det.getNombre());
                    b.setFechaActivacion(DateHelper.format(DateHelper.parseDate(det.getFechaActivacion(), DateHelper.FORMAT_yyyyMMddhhmmss), DateHelper.FORMAT_ddMMyyyy_SLASH));
                    b.setFechaExpiracion(DateHelper.format(DateHelper.parseDate(det.getFechaExpiracion(), DateHelper.FORMAT_yyyyMMddhhmmss), DateHelper.FORMAT_ddMMyyyy_SLASH));                    
                    b.setDescripcion(det.getDescripcion());
                    b.setCuotaInicialVoucher(det.getCuotaInicialVoucher());
                    b.setCuotaUtilizadaVoucher(det.getCuotaUtilizadaVoucher());
                    b.setSaldoBolsa(det.getCuotaInicialVoucher() - det.getCuotaUtilizadaVoucher());
                    
                    resumen.addBolsa(b);
                }                
                return resumen;
            } catch (Exception e) {
                LOGGER.error("Exception caught on Service response: "
                        + "listarBolsasActivasBAMRequestDocument", e);
                LOGGER.error( new DAOException(e));
            }
        } catch (WebServiceLocatorException e) {
            LOGGER.error("Error al inicializar el Port " + SCOBPortType.class,
                    e);
            LOGGER.error( new DAOException(e));
        }
        return new ResumenBolsasActivasBAMCCPP();
    }
    
    public void comprarBolsa(String msisdn, String codBolsa, String tipoCobro)
            throws DAOException, ServiceException {
        SCOBPortType port;
        LOGGER.info("Instanciando el port " + SCOBPortType.class);
        try {
            port = (SCOBPortType) WebServiceLocator.getInstance().getPort(
                    SCOBPortTypeSOAPBindingQSService.class, SCOBPortType.class);

            ComprarRequestType.Mensaje request = new ComprarRequestType.Mensaje();
            ComprarResponseType.Mensaje response = null;            

            try {
                LOGGER.info("Configurando Datos de la peticion");
                request.setMsisdn(WordNumberHelper.getPrefijoEntel() + msisdn);
                request.setCanal(CANAL);
                request.setCodigo(codBolsa);
                request.setPlataforma("");
                request.setUsuario("");
                request.setCobro(tipoCobro);
                LOGGER.info("Invocando servicio");
                response = port.comprarRequestDocument(request);
            } catch (ComprarServiceFaultMessage e) {
                ServiceFaultType mensaje = e.getFaultInfo().getMensaje();
                String codigoRespuesta = mensaje.getCodigo();
                String descripcionRespuesta = mensaje.getDescripcion();
                
                LOGGER.error("Service error code received: " + codigoRespuesta
                        + " - " + descripcionRespuesta);
               LOGGER.error( new ServiceException(codigoRespuesta, descripcionRespuesta));          
            } catch (Exception e) {
                LOGGER.error("Exception caught on Service invocation: "
                        + "comprarRequestDocument", e);
                LOGGER.error( new DAOException(e));
            }

            try {
                String codigoRespuesta = response.getCodigo();
                String descripcionRespuesta = response.getDescripcion();
                
                if (codigoRespuesta.equals("200")) {
                    return;
                } else {
                    LOGGER.error( new ServiceException(codigoRespuesta, descripcionRespuesta));
                }
            } catch (Exception e) {
                LOGGER.error("Exception caught on Service response: "
                        + "comprarRequestDocument", e);
                LOGGER.error( new DAOException(e));
            }
        } catch (WebServiceLocatorException e) {
            LOGGER.error("Error al inicializar el Port " + SCOBPortType.class,
                    e);
            LOGGER.error( new DAOException(e));
        }        
    }
}
