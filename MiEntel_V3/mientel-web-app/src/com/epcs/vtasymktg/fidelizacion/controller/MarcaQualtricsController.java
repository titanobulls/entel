package com.epcs.vtasymktg.fidelizacion.controller;

import java.util.Iterator;
import java.util.List;

import javax.faces.event.PhaseEvent;
import javax.portlet.PortletPreferences;

import org.apache.log4j.Logger;

import com.bea.content.Node;
import com.bea.p13n.usermgmt.profile.ProfileWrapper;
import com.epcs.bean.CuentaClienteBean;
import com.epcs.bean.MarcaQualtricsParams;
import com.epcs.cliente.perfil.delegate.CuentaDelegate;
import com.epcs.recursoti.configuracion.JsonHelper;
import com.epcs.recursoti.configuracion.MiEntelBusinessHelper;
import com.epcs.recursoti.configuracion.MiEntelProperties;
import com.epcs.recursoti.configuracion.ParametrosHelper;
import com.epcs.recursoti.configuracion.Utils;
import com.epcs.recursoti.configuracion.jsf.utils.JSFPortletHelper;
import com.epcs.recursoti.configuracion.uup.ProfileWrapperHelper;
import com.epcs.recursoti.excepcion.DAOException;
import com.epcs.recursoti.excepcion.ServiceException;

public class MarcaQualtricsController {
	private static final Logger LOGGER = Logger.getLogger(MarcaQualtricsController.class);

	private MarcaQualtricsParams marcaQualtricsParams;
	private String respuestaJson;
	private CuentaDelegate cuentaDelegate;
	private static final String ACTIVIDAD_SERVICIO = MiEntelProperties.getProperty("parametros.qualtrics.actividad.servicio");
	private static final String ACTIVIDAD_DISTRIBUCION = MiEntelProperties.getProperty("parametros.qualtrics.actividad.distribucion");
	
	//Contenidos con codigo de Site Intercept
	private static final String CONTENT_SERVICIO_CC = MiEntelProperties.getProperty("parametros.qualtrics.servicio.cc.idContenido");
	private static final String CONTENT_SERVICIO_PP = MiEntelProperties.getProperty("parametros.qualtrics.servicio.pp.idContenido");
	private static final String CONTENT_SERVICIO_SS = MiEntelProperties.getProperty("parametros.qualtrics.servicio.ss.idContenido");
	private static final String CONTENT_DISTRIBUCION_CC = MiEntelProperties.getProperty("parametros.qualtrics.distribucion.cc.idContenido");
	private static final String CONTENT_DISTRIBUCION_PP = MiEntelProperties.getProperty("parametros.qualtrics.distribucion.pp.idContenido");
	private static final String CONTENT_DISTRIBUCION_SS = MiEntelProperties.getProperty("parametros.qualtrics.distribucion.ss.idContenido");

	public void generarMarca(PhaseEvent phase) {
		try {

			marcaQualtricsParams = new MarcaQualtricsParams();

			ProfileWrapper profile = ProfileWrapperHelper.getProfile(JSFPortletHelper.getRequest());

			String numeroPCS = ProfileWrapperHelper.getPropertyAsString(profile, "numeroPcsSeleccionado");
			marcaQualtricsParams.setMovil(numeroPCS);
			LOGGER.info("El numeroPCS es: " + numeroPCS);

			String mercado = ProfileWrapperHelper.getPropertyAsString(profile, "mercado");
			marcaQualtricsParams.setMercado(mercado.equals("") ? MiEntelProperties.getProperty("parametros.qualtrics.mercado.noinfo") : mercado);
			LOGGER.info("El mercado es: " + mercado);

			String rutUsuario = ProfileWrapperHelper.getPropertyAsString(profile, "rutUsuarioSeleccionado").toUpperCase();
			LOGGER.info("El rutUSuario es: " + rutUsuario);

			String segmento = obtenerSegmento(rutUsuario);
			marcaQualtricsParams.setSegmento(segmento.equals("") ? MiEntelProperties.getProperty("parametros.qualtrics.segmento.noinfo") : segmento);
			LOGGER.info("El segmento es: " + segmento);

			String tipoAct = this.getValorPreference(MiEntelProperties.getProperty("parametros.qualtrics.preferences.tipoActividad"));
			String idContenido = "";
			
			if(MiEntelBusinessHelper.isMercadoSuscripcion(mercado)){
				if(tipoAct.equalsIgnoreCase(ACTIVIDAD_SERVICIO))
					idContenido = CONTENT_SERVICIO_SS;
				else if(tipoAct.equalsIgnoreCase(ACTIVIDAD_DISTRIBUCION))
					idContenido = CONTENT_DISTRIBUCION_SS;
			} else if(MiEntelBusinessHelper.isMercadoCuentaControlada(mercado)){
				if(tipoAct.equalsIgnoreCase(ACTIVIDAD_SERVICIO))
					idContenido = CONTENT_SERVICIO_CC;
				else if(tipoAct.equalsIgnoreCase(ACTIVIDAD_DISTRIBUCION))
					idContenido = CONTENT_DISTRIBUCION_CC;
			} else if(MiEntelBusinessHelper.isMercadoPrepago(mercado)){
				if(tipoAct.equalsIgnoreCase(ACTIVIDAD_SERVICIO))
					idContenido = CONTENT_SERVICIO_PP;
				else if(tipoAct.equalsIgnoreCase(ACTIVIDAD_DISTRIBUCION))
					idContenido = CONTENT_DISTRIBUCION_PP;
			}
			
			LOGGER.info("El id del contenido es: " + idContenido);
			Node contenidoNode = JSFPortletHelper.getContentNode("idContenido", idContenido);
			String contenidoScript = "";
			if(null != contenidoNode){
				contenidoScript = contenidoNode.getProperty("html").getValue().getStringValue();
			}
			
			marcaQualtricsParams.setContenidoScript(contenidoScript);
			
			respuestaJson = JsonHelper.toJsonResponse(marcaQualtricsParams);

		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			respuestaJson = JsonHelper.toJsonServiceErrorMessage("noDisponible");
		}

	}
	

	private String getValorPreference(String preference) {
		try {
			LOGGER.info("EL valor del preference es: " + preference);
			PortletPreferences prefs = JSFPortletHelper.getPreferencesObject();
			return JSFPortletHelper.getPreference(prefs, preference, "");
		} catch (Exception e) {
			LOGGER.error("No se ha podido obtener el valor del preference" + e.getMessage(), e);
			return "";
		}

	}

	private String obtenerSegmento(String rut) {
		String segmento = "";
		try {
			List<CuentaClienteBean> cuentaClienteBeans = cuentaDelegate.obtenerCuentaCliente(rut);
			Iterator<CuentaClienteBean> cuentas = cuentaClienteBeans.iterator();
			boolean swPersona = false, swEmpresa = false;
			while (cuentas.hasNext()) {
				CuentaClienteBean cuentaClienteBean = cuentas.next();
				if (swPersona == false && ParametrosHelper.getExisteParametro("qualtrics.grupos.persona", cuentaClienteBean.getCodGrupo())) {
					segmento += MiEntelProperties.getProperty("parametros.qualtrics.segmento.persona") + ",";
					swPersona = true;
				}
				if (swEmpresa == false && ParametrosHelper.getExisteParametro("qualtrics.grupos.empresa", cuentaClienteBean.getCodGrupo())) {
					segmento += MiEntelProperties.getProperty("parametros.qualtrics.segmento.empresa") + ",";
					swEmpresa = true;
				}
				LOGGER.info("El segmento es " + segmento);
			}
			
			if (segmento.endsWith(",")) {
				segmento = segmento.substring(0, segmento.length() - 1);
			}
			if (segmento.equals("")) {
				segmento = MiEntelProperties.getProperty("parametros.qualtrics.segmento.persona");
			}

		} catch (DAOException e) {
			LOGGER.error("Error obteniendo segmento de cliente", e);
			segmento = "";
		} catch (ServiceException e) {
			LOGGER.info("ServiceException: codigo " + e.getCodigoRespuesta()
	                   + " - " + e.getDescripcionRespuesta());
			segmento = "";
		} catch (Exception e) {
			LOGGER.error("Error obteniendo segmento de cliente", e);
			segmento = "";
		}
		return segmento;
	}

	public CuentaDelegate getCuentaDelegate() {
		return cuentaDelegate;
	}

	public void setCuentaDelegate(CuentaDelegate cuentaDelegate) {
		this.cuentaDelegate = cuentaDelegate;
	}

	public String getRespuestaJson() {
		return respuestaJson;
	}

	public void setRespuestaJson(String respuestaJson) {
		this.respuestaJson = respuestaJson;
	}

	public MarcaQualtricsParams getMarcaQualtricsParams() {
		return marcaQualtricsParams;
	}

	public void setMarcaQualtricsParams(MarcaQualtricsParams marcaQualtricsParams) {
		this.marcaQualtricsParams = marcaQualtricsParams;
	}
	
}
