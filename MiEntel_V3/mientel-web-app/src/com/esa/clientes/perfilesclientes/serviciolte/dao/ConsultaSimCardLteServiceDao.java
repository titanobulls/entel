package com.esa.clientes.perfilesclientes.serviciolte.dao;

import org.apache.log4j.Logger;

import cl.allware.wsconsultasim.services.Consulta;
import cl.allware.wsconsultasim.services.ConsultasWS;
import cl.allware.wsconsultasim.services.RespuestaConsulta;
import cl.allware.wsconsultasim.services.WSConsultaSIM;

import com.epcs.bean.SimCard4GLteBean;
import com.epcs.cliente.perfil.ClientePerfilServicePortType;
import com.epcs.recursoti.configuracion.MiEntelProperties;
import com.epcs.recursoti.configuracion.locator.WebServiceLocator;
import com.epcs.recursoti.configuracion.locator.WebServiceLocatorException;
import com.epcs.recursoti.excepcion.DAOException;
import com.epcs.recursoti.excepcion.ServiceException;


public class ConsultaSimCardLteServiceDao {
	
	 private static final Logger LOGGER = Logger.getLogger(ConsultaSimCardLteServiceDao.class);
	 
	 public static final String CODIGO_RESPUESTA_ERROR = MiEntelProperties
     .getProperty("servicios.codigoRespuesta.error");
	 
	 public SimCard4GLteBean ConsultaSimCardLteService(String imsi,String msisdns,String iccid)
	 throws DAOException, ServiceException{
		 
		 SimCard4GLteBean resultado = new SimCard4GLteBean();
		 		 
		 WSConsultaSIM port = null;
		 
		 LOGGER.info("Instanciando el port " + ClientePerfilServicePortType.class);
		 try{
			 port= (WSConsultaSIM) WebServiceLocator.getInstance().getPort(
					 ConsultasWS.class, WSConsultaSIM.class);
		 }catch (WebServiceLocatorException e) {
			 LOGGER.error("Error al inicializar el Port " + ClientePerfilServicePortType.class, e);
	            LOGGER.error( new DAOException(e));
		}
		 
		 Consulta consulta = new Consulta();
		 RespuestaConsulta response = null;		 
		 
		 try{
			 LOGGER.info("Configurando Datos de la peticion");
			 
			 consulta.setIccid(iccid);
			 consulta.setMsisdn(msisdns);
			 consulta.setImsi(imsi);			 			 
			 
			 LOGGER.info("Invocando servicio");
			 response = port.consultaSIM(consulta);
			 
		 }catch (Exception e) {
			 LOGGER.error("Exception caught on Service invocation: consultaSimCardLteService",
	                    e);
	            LOGGER.error( new DAOException(e));
		}
		 
		 int codigoRespuesta = response.getEstado().getCode();
		 LOGGER.info("codigoRespuesta " + codigoRespuesta);		 		
		 
		 if (!CODIGO_RESPUESTA_ERROR.equals(String.valueOf(codigoRespuesta))) {		 			 
			 
			 try{
				 
				 resultado.setCodigo(response.getEstado().getCode());
				 resultado.setEstado(response.getEstado().getValue());				 				 
				 
			 }catch (Exception e) {
				 LOGGER.error("Exception caught on Service response: "
	                        + "consultaSimCardLteService", e);
	                LOGGER.error( new DAOException(e));
			}
			 
		 }else {
	            LOGGER.error("consultaSimCardLteService: Service error code received: "
	                    + codigoRespuesta);	            
	           LOGGER.error( new ServiceException(String.valueOf(codigoRespuesta),"Error en consultaSimCardLteService"));
	     }
				 		 		
		 return resultado;		
	 }

}
