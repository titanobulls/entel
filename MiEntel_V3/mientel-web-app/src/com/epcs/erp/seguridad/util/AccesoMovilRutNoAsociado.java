/* Propiedad de Entel PCS. Todos los derechos reservados */
package com.epcs.erp.seguridad.util;

import org.apache.log4j.Logger;

import com.epcs.erp.seguridad.controller.LoginController;
import com.epcs.inscripcion.util.InscripcionHelper;
import com.epcs.recursoti.configuracion.MiEntelProperties;

/**
 * @author gcastro (I2B) en nombre de Absalon Opazo (Atencion al Cliente,
 *         EntelPcs)
 * 
 */
public class AccesoMovilRutNoAsociado {
	
    private static final long serialVersionUID = 1L;

    private static final Logger LOGGER = Logger
            .getLogger(LoginController.class);
    
	private static final String TIPO_INSCRIPCION = MiEntelProperties
	.getProperty("inscripcion.tipo.movilRutNoAsociado.attr");
	
	
	/**
	 * 
	 * @param msisdn
	 * @param rut
	 * @param clave
	 * @return
	 */
	public boolean acceder(String msisdn, String rut, String clave) {
		boolean response = false;

		try{
			    
			response = InscripcionHelper
			.verificarRestriccionDias(msisdn, rut);

			   if(response){
				   InscripcionHelper.redirectInscripcionByLogin(
						msisdn,rut,clave,TIPO_INSCRIPCION);
			   }

		}catch (Exception e) {
			LOGGER.info("Exception MovilRutNoAsociado al intentar redireccionar al formulario de inscripcion.");
		}		
				
		return response;
	}

}
