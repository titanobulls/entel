/* Propiedad de Entel PCS. Todos los derechos reservados */
package com.epcs.inscripcion.dao;

import java.util.Date;

import org.apache.log4j.Logger;

import com.epcs.bean.DireccionBean;
import com.epcs.bean.MercadoBean;
import com.epcs.bean.UsuarioBean;
import com.epcs.cliente.perfil.ClientePerfilService;
import com.epcs.cliente.perfil.ClientePerfilServicePortType;
import com.epcs.cliente.perfil.types.ActualizarMisDatosType;
import com.epcs.cliente.perfil.types.ResultadoActualizarMisDatosType;
import com.epcs.cliente.perfil.types.ResultadoValidarAsociacionMovilRutBuicType;
import com.epcs.cliente.perfil.types.ResultadoValidarMovilBuicType;
import com.epcs.cliente.perfil.types.ValidarAsociacionMovilRutBuicType;
import com.epcs.cliente.perfil.types.ValidarMovilBuicType;
import com.epcs.cliente.perfil.types.ObtenerMercadoType;
import com.epcs.cliente.perfil.types.ResultadoObtenerMercadoType;
import com.epcs.recursoti.configuracion.DateHelper;
import com.epcs.recursoti.configuracion.MiEntelProperties;
import com.epcs.recursoti.configuracion.Utils;
import com.epcs.recursoti.configuracion.locator.WebServiceLocator;
import com.epcs.recursoti.configuracion.locator.WebServiceLocatorException;
import com.epcs.recursoti.excepcion.DAOException;
import com.epcs.recursoti.excepcion.ServiceException;

/**
 * @author gcastro (I2B) en nombre de Absalon Opazo (Atencion al Cliente,
 *         EntelPcs)
 * 
 */
public class InscripcionDAO {
	
	private static final Logger LOGGER = Logger.getLogger(InscripcionDAO.class);	
	
    /**
     * Valor de exito para la respuesta a un servicio
     */
    public static final String CODIGO_RESPUESTA_OK = MiEntelProperties
            .getProperty("servicios.codigoRespuesta.exito");
    
    /**
     * Valor par rut movil no registrada en BUIC
     */
    public static final String CODIGO_RESPUESTA_MOVILRUT = MiEntelProperties
            .getProperty("inscripcion.formRegistro.codigo.movilRutNoBuic");
    
	
     /**
      * 
      * @param numeroPcs
      * @throws DAOException
      * @throws ServiceException
      */
     public void validarMovilBuic(String numeroPcs)throws DAOException, ServiceException{
    	 
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
         ValidarMovilBuicType request = new ValidarMovilBuicType();
         request.setMsisdn(numeroPcs);

         LOGGER.info("Invocando servicio");
         ResultadoValidarMovilBuicType response = null;

         try{
        	 response = port.validarMovilBuic(request);
         }catch (Exception e) {
        	 LOGGER.error("Exception caught on Service invocation: "
        			 + "validarMovilBuic", e);
             LOGGER.error( new DAOException(e));
		}
         
        String codigoRespuesta = response.getRespuesta().getCodigo();
 		String descripcionRespuesta = response.getRespuesta().getDescripcion();
 		
 	    LOGGER.info("codigoRespuesta " + codigoRespuesta);
 	    LOGGER.info("descripcionRespuesta " + descripcionRespuesta);
         
 	   if (CODIGO_RESPUESTA_OK.equals(codigoRespuesta)) {
			try{
				
				LOGGER.info("Validacion Movil Buic Correcta - Movil se encuentra registrado en BUIC");
				
			}catch (Exception e) {
				LOGGER.error(
                       "Exception inesperada al validar movil BUIC", e);
               LOGGER.error( new DAOException(e));
			}
	    
	   }else {
		    LOGGER.error("Respuesta del Servicio : " +codigoRespuesta+ " - " + descripcionRespuesta);
		    LOGGER.error( new ServiceException(codigoRespuesta, descripcionRespuesta));
		}

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

             LOGGER.info("Invocando Servicio");
             actualizacionResponse = port
                     .actualizarMisDatos(actualizacionRequest);
         } catch (Exception e) {
             LOGGER.error("Exception caught on Service invocation: actualizarMisDatos", e);
             LOGGER.error( new DAOException(e));
         }
         
         String codigoRespuesta = actualizacionResponse.getRespuesta().getCodigo();
         String descripcionRespuesta = actualizacionResponse.getRespuesta().getDescripcion();

         LOGGER.info("codigoRespuesta " + codigoRespuesta);
         LOGGER.info("descripcionRespuesta " + descripcionRespuesta);

         if (CODIGO_RESPUESTA_OK.equals(codigoRespuesta)) {
             LOGGER.info("Usuario Actualizado");
         }
         else {
             LOGGER.error("Service error code received: " + codigoRespuesta
                     + " - " + descripcionRespuesta);
             LOGGER.error( new ServiceException(codigoRespuesta, descripcionRespuesta));
         }

     }
     

     /**
      * 
      * @param usuarioBean
      * @return
      */
     private ActualizarMisDatosType buildRequestType(UsuarioBean usuarioBean) {

         ActualizarMisDatosType actualizacionRequest = new ActualizarMisDatosType();
         
         //Datos Personales
         actualizacionRequest.setAaa("");
         actualizacionRequest.setMsisdn(usuarioBean.getNumeroPCS());
         actualizacionRequest.setRut(usuarioBean.getRut().getStringValue());
         actualizacionRequest.setPrimerNombre(usuarioBean.getPrimerNombre()==null?"":usuarioBean.getPrimerNombre());
         actualizacionRequest.setSegundoNombre(usuarioBean.getSegundoNombre()==null?"":usuarioBean.getSegundoNombre());
         actualizacionRequest.setPrimerApellido(usuarioBean.getApellidoPaterno()==null?"":usuarioBean.getApellidoPaterno());
         actualizacionRequest.setSegundoApellido(usuarioBean.getApellidoMaterno()==null?"":usuarioBean.getApellidoMaterno());
         actualizacionRequest.setSexo(usuarioBean.getSexo()==null?"":usuarioBean.getSexo());
         actualizacionRequest.setFechaNacimiento(usuarioBean.getFechaNacimiento()==null?"":DateHelper.format(usuarioBean.getFechaNacimiento(), DateHelper.FORMAT_ddMMyyyy));

         //Direccion
         DireccionBean direccionContacto = usuarioBean.getDireccionContacto();
         
         actualizacionRequest.setDireccionCalle(direccionContacto==null?"":direccionContacto.getCalle());
         actualizacionRequest.setDireccionNumero(direccionContacto==null?"":direccionContacto.getNumero());
         actualizacionRequest.setDireccionResto(direccionContacto==null?"":direccionContacto.getDepartamento());        
         actualizacionRequest.setComuna(direccionContacto==null?"":direccionContacto.getComuna().getCodigo());
         actualizacionRequest.setCiudad(direccionContacto==null?"":direccionContacto.getCiudad().getCodigo());
         actualizacionRequest.setRegion(direccionContacto==null?"":direccionContacto.getRegion().getCodigo());
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
         actualizacionRequest.setEstadoCivil("");
         actualizacionRequest.setTelefonoFijoArea(usuarioBean.getPrefijoTelefonoAdicional()==null?"":usuarioBean.getPrefijoTelefonoAdicional());
         actualizacionRequest.setTelefonoFijoNumero(usuarioBean.getTelefonoAdicional()==null?"":usuarioBean.getTelefonoAdicional());         

         actualizacionRequest.setNivelEstudios("");
         actualizacionRequest.setActividad("");         

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
         actualizacionRequest.setRelacionTitular(usuarioBean.getRelacionTitular());
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
         actualizacionRequest.setProfesion("");
         actualizacionRequest.setHijo("");

         return actualizacionRequest;
     }
     
     
     
     
     /**
      * Valida asociacion movil rut en Buic
      * 
      * @param numeroPcs
      * @param rut
      * @throws DAOException
      * @throws ServiceException
      */
     public Date validarAsociacionMovilRutBuic(String numeroPcs, String rut)throws DAOException, ServiceException{
    	 
    	 Date fechaIngresoMasDiasRestriccion = null;
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
         ValidarAsociacionMovilRutBuicType request = new ValidarAsociacionMovilRutBuicType();
         request.setMsisdn(numeroPcs);
         request.setRut(rut);

         LOGGER.info("Invocando servicio");
         ResultadoValidarAsociacionMovilRutBuicType response = null;
         
         try{
        	 response = port.validarAsociacionMovilRutBuic(request);
         }catch (Exception e) {
        	 LOGGER.error("Exception caught on Service invocation: "
        			 + "validarAsociacionMovilRutBuic", e);
             LOGGER.error( new DAOException(e));
		}
         
        String codigoRespuesta = response.getRespuesta().getCodigo();
 		String descripcionRespuesta = response.getRespuesta().getDescripcion();
 		
 	    LOGGER.info("codigoRespuesta " + codigoRespuesta);
 	    LOGGER.info("descripcionRespuesta " + descripcionRespuesta);
         
 	   if (CODIGO_RESPUESTA_MOVILRUT.equals(codigoRespuesta)) {
			try{

				Date fechaIngreso = (DateHelper.parseDate(response.getFechaIngreso(), 
						DateHelper.FORMAT_ddMMyyyy));
				
				int diasRestriccion = Integer.parseInt(response
						.getDiasRestriccion());
				
				fechaIngresoMasDiasRestriccion = (DateHelper.parseDate(
						DateHelper.format(DateHelper.addDays(fechaIngreso, diasRestriccion + 1),
										  DateHelper.FORMAT_ddMMyyyy), 
				        DateHelper.FORMAT_ddMMyyyy));

				LOGGER.info("Validacion Movil Rut Buic - Par rut movil no registrada en BUIC");
				
			}catch (Exception e) {
				LOGGER.error(
                       "Exception inesperada al validar asociacion movil rut en BUIC", e);
               LOGGER.error( new DAOException(e));
			}
	    
	   }else {
		   if(CODIGO_RESPUESTA_OK.equals(codigoRespuesta)){
			   if (Utils.isEmptyString(response.getFechaIngreso())) {
				   LOGGER.warn("consultarPuntosSSCCPP: Servicio no responde con fechaIngreso");
		       }
			   if (Utils.isEmptyString(response.getDiasRestriccion())) {
				   LOGGER.warn("consultarPuntosSSCCPP: Servicio no responde con diasRestriccion");
		       }
		   }else{
			   LOGGER.error("Respuesta del Servicio : " +codigoRespuesta+ " - " + descripcionRespuesta);
			    LOGGER.error( new ServiceException(codigoRespuesta, descripcionRespuesta));
		   }
		}
 	   
 	   return fechaIngresoMasDiasRestriccion;

     }
     
     /**
      * Obtiene mercado mediante cola ---
      * 
      * @param numeroPcs      
      */
     public MercadoBean obtenerMercado(String numeroPcs)throws DAOException, ServiceException{
    	     	 
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
         ObtenerMercadoType request = new ObtenerMercadoType();
         request.setMsisdn(numeroPcs);         

         LOGGER.info("Invocando servicio");
         
         ResultadoObtenerMercadoType response = null;
         String codigoRespuesta;
         String descripcionRespuesta;
         
         try{
        	 
        	 response = port.obtenerMercado(request);
     		 
         }catch (Exception e) {
        	 LOGGER.error("Exception caught on Service invocation: "
        			 + "obtenerMercado", e);
             LOGGER.error( new DAOException(e));
		}
        
        codigoRespuesta = response.getRespuesta().getCodigo();
 		descripcionRespuesta = response.getRespuesta().getDescripcion(); 
        
 	    LOGGER.info("codigoRespuesta " + codigoRespuesta);
 	    LOGGER.info("descripcionRespuesta " + descripcionRespuesta);
       
 	    MercadoBean mercadoBean = new MercadoBean();
 	    
 	   if (CODIGO_RESPUESTA_OK.equals(codigoRespuesta)) {
 		  
 		  mercadoBean.setMsisdn(response.getMsisdn()); 
 		  mercadoBean.setMercado(response.getMercado()); 		  
	    
	   }else {
		    LOGGER.error("Respuesta del Servicio : " +codigoRespuesta+ " - " + descripcionRespuesta);
		    LOGGER.error( new ServiceException(codigoRespuesta, descripcionRespuesta));
		}
 	   
 	   return mercadoBean;

     }

}
