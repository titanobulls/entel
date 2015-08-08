/* Propiedad de Entel PCS. Todos los derechos reservados */
package com.epcs.recursoti.configuracion.filter;

import java.io.IOException;
import java.util.Calendar;
import javax.security.auth.login.LoginException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import com.bea.p13n.security.Authentication;
import com.bea.p13n.usermgmt.SessionHelper;
import com.bea.p13n.usermgmt.profile.ProfileNotFoundException;
import com.bea.p13n.usermgmt.profile.ProfileWrapper;
import com.bea.portlet.GenericURL;
import com.bea.portlet.PageURL;
import com.epcs.bean.MarcaEstadisticaBean;
import com.epcs.inteligencianegocio.satisfaccioncliente.delegate.EstadisticasDelegate;
import com.epcs.inteligencianegocio.satisfaccioncliente.util.EstadisticasHelper;
import com.epcs.recursoti.configuracion.ExternalAppsHelper;
import com.epcs.recursoti.configuracion.MiEntelProperties;
import com.epcs.recursoti.configuracion.Utils;
import com.epcs.recursoti.configuracion.uup.ProfileWrapperHelper;

/**
 * Este filtro permite saber cuando un usuario esta autenticado. Se utiliza el
 * framework de Weblogic para conocer si lo esta, o si se ha enviado un atributo
 * de request que se llame idp. Si viene el idp se hara la autenticacion con el,
 * sino es valido o si no viene el atributo se hara una redireccion a la
 * pantalla de login.
 * 
 * @author mmartinez (I2B) en nombre de Absalon Opazo (Atencion al Cliente,
 *         EntelPcs)<br>
 *         Version inicial.<br>
 * @author jlopez (I2B) en nombre Absalon Opazo (Atencion al Cliente, EntelPcs)<br>
 *         Se agrega soporte a protocolo seguro (https) y mantencion para nuevo
 *         esquema de redireccion via "contexto"
 * 
 * 
 */
public class AuthenticationFilter implements Filter {

	private static final Logger LOGGER = Logger
			.getLogger(AuthenticationFilter.class);

    private static final String DEFAULT_IDP_ATTTR_NAME = MiEntelProperties
            .getProperty("miEntel.login.idp.attr");

	private static final String CONTEXTO_ATTTR_NAME = MiEntelProperties
    		.getProperty("miEntel.login.contexto.attr");

    private static final String SEPARATOR_CHAR = MiEntelProperties
    		.getProperty("miEntel.separador.movil_rut");
    
    private static final String URL_LINEA_HOGAR_VOZ = MiEntelProperties
            .getProperty("miEntel.lineaHogar.urlVoz");

    private static final String URL_LINEA_HOGAR_DATOS = MiEntelProperties
            .getProperty("miEntel.lineaHogar.urlDatos");
    
    private static final String SUBMERCADO_LINEA_HOGAR_VOZ = MiEntelProperties
            .getProperty("miEntel.lineaHogar.subMercadoVoz");

    private static final String SUBMERCADO_LINEA_HOGAR_DATOS = MiEntelProperties
            .getProperty("miEntel.lineaHogar.subMercadoDatos");
	
	private static final String SUBMERCADO_FDT = MiEntelProperties
    		.getProperty("miEntel.subMercadoFDT");

	private static final String SUBMERCADO_CCPP = MiEntelProperties
    		.getProperty("miEntel.subMercadoCCPP");
	
	private static final String EMP_SGO = MiEntelProperties
			.getProperty("miEntel.subMercadoSGO");
	
	private static final String SGO = MiEntelProperties
			.getProperty("consulta.plan.sgo.submercado");
	
	//Parametros del contexto para Formulario de Inscripcion
	private static final String TIPO_INSCRIPCION_ATTTR_NAME = MiEntelProperties
			.getProperty("inscripcion.tipo.attr.name");
	
	private static final String START_PARAMETER_URL = "?";
    
	private static final String DEFAULT_PARAMETER_SEPARATOR = "&";
	
	private EstadisticasDelegate estadisticasDelegate;
	   
	private MarcaEstadisticaBean marcaEstadisticaBean;

	/**
	 * Implementacion de metodo que ejecuta la tarea de validar la aplicacion y
	 * redigir a miEntel.
	 */
	public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain) throws IOException, ServletException {

	    
        LOGGER.info("Request de autenticacion externa recibido: ");
        LOGGER.info("    Host: " + request.getRemoteHost());
        LOGGER.info("    Addr: " + request.getRemoteAddr());

	    //Parametros recibidos en query String del request
        String idp = this.getNonSensitiveParameterValue(DEFAULT_IDP_ATTTR_NAME, request);
        String id = this.getNonSensitiveParameterValue("ID", request);        
        String contexto = this.getNonSensitiveParameterValue(CONTEXTO_ATTTR_NAME, request);
        //String fdt = this.getNonSensitiveParameterValue(FDT_ATTTR_NAME, request);
        String tipo_inscripcion = this.getNonSensitiveParameterValue(TIPO_INSCRIPCION_ATTTR_NAME, request);

        HttpServletRequest servletRequest = (HttpServletRequest) request;
        HttpServletResponse servletResponse = (HttpServletResponse) response;
        
        //Path portal de la aplicacion
        String portalPath = Utils.getMiEntelPortalPath(servletRequest);

        //URL por defecto: Login de Mientel, en protocolo http
        GenericURL defaultURL = GenericURL.createGenericURL(
                servletRequest, servletResponse);
        defaultURL.setTemplate("logout-http");
        defaultURL.setContextualPath(portalPath);

        //Url final hacia donde se enviara el request entrante
        String url = defaultURL.toString(true);
        
        try {
            
            /*
             * Si no hay IDP indicado se redirige al login de MiEntel con protocolo http
             */
            if (idp == null && id ==null) {

                // La url se inicializa con la url base de la aplicacion
                url = defaultURL.toString(true);
                LOGGER.warn("Idp no encontrado, se redirecciona hacia Login de MiEntel");
            }
            else {
                //Autenticacion usuario
                if (!Authentication.isAnonymous(Authentication
                        .getCurrentSubject())) {
                    Authentication.logout(servletRequest, true);
                }
                String username="";

                if (id != null){
	                LOGGER.info("Autenticando usuario con ID " + id);	                
	                username = id;
                }else{
                LOGGER.info("Autenticando usuario con IDP " + idp);
                String token = String.valueOf(Calendar.getInstance().getTimeInMillis());
	                username = idp + SEPARATOR_CHAR + token;
                }
                
                Authentication.login(username, "", servletRequest,
                        (HttpServletResponse) response);
                
                ProfileWrapper profile = SessionHelper.getProfile(servletRequest);
                LOGGER.info("Usuario autenticado.");
                LOGGER.info("numeroPcs : "
                        + ProfileWrapperHelper.getPropertyAsString(profile,
                                "numeroPcs"));
                LOGGER.info("nombreUsuario : "
                        + ProfileWrapperHelper.getPropertyAsString(profile,
                                "nombreUsuario"));
                LOGGER.info("subMercado : "
                        + ProfileWrapperHelper.getPropertyAsString(profile,
                                "subMercado"));                 

                String subMercado = ProfileWrapperHelper.getPropertyAsString(
                        profile, "subMercado");
                
                
                
                /**
                 * Redireccionamiento para subMercados de lineaHogar
                 */
                if (subMercado.equals(SUBMERCADO_LINEA_HOGAR_VOZ)) {
                    url = URL_LINEA_HOGAR_VOZ.replace("{IDP}", idp);
                } else if (subMercado.equals(SUBMERCADO_LINEA_HOGAR_DATOS)) {
                    url = URL_LINEA_HOGAR_DATOS.replace("{IDP}", idp);                
                } 
                // Si se indica parametro 'contexto' se obtiene su path 
                // por medio del Helper de Aplicaciones Externas
                else if (Utils.isEmptyString(contexto)) {
                	
                	LOGGER.info("contexto isEmptyString ");
                	
                	// La url se inicializa con la url base de la aplicacion
                    GenericURL genericURL = GenericURL.createGenericURL(
                            servletRequest, servletResponse);
                    genericURL.setTemplate("login-https");
                    genericURL.setContextualPath(portalPath);

                    url = genericURL.toString(true);
                    
                   	url = url.concat(START_PARAMETER_URL+servletRequest.getQueryString());
                    
                } else {
                	
                	
                    LOGGER.info("contexto: '" + contexto + "'");

                    String mercado = ProfileWrapperHelper.getPropertyAsString(
                            profile, "mercado");
                    
                    String flagBam = ProfileWrapperHelper.getPropertyAsString(
                    		profile,"flagBam");


                    if( flagBam.equals("1") && subMercado.equals(SUBMERCADO_FDT) && contexto.equals("comprarBolsa") && mercado.equals("CC") ){
                    	LOGGER.info("REDIRECIONANDO comprarBolsaFDT");
                    	contexto = "comprarBolsaFDT";

                    }else if( flagBam.equals("1") && subMercado.equals(SUBMERCADO_CCPP) && contexto.equals("comprarBolsa") && mercado.equals("CC") ){
                    	LOGGER.info("REDIRECIONANDO comprarBolsaCCPP");
                    	contexto = "comprarBolsaCCPP";
                		
                	}
                    
                    /*if(contexto.equalsIgnoreCase("servicioTecnico") && 
                    		!MiEntelBusinessHelper.isMercadoSuscripcion(mercado)){
                    	contexto = "";
                    }else */
                    if(contexto.equalsIgnoreCase("saldoRecargosSGO") && 
                			!subMercado.equals(SGO) && !subMercado.equals(EMP_SGO)){
                		contexto = "";
                	}
                    
                    /*PageURL pageURL = ExternalAppsHelper.getPageURL(
                                servletRequest, servletResponse, contexto, mercado);*/

                    PageURL pageURL = ExternalAppsHelper.getPageURL(
                            servletRequest, servletResponse, contexto, mercado, flagBam);
                    LOGGER.info("pageURL ::: " + pageURL );

                    if(pageURL == null) {

                        LOGGER.warn("No se encontro URL para el contexto '"
                                + contexto + "', mercado '" + contexto + "' y flagBam ["+flagBam+"]");
                        LOGGER.warn("redirect hacia Home de MiEntel");

                        /*
                        LOGGER.warn("No se encontro URL para el contexto '"
                                + contexto + "' y mercado '" + contexto + "'");
                        LOGGER.warn("redirect hacia Home de MiEntel");
						*/
                        
                    } else {
                    	pageURL.setTemplate("external-app-https");
                        pageURL.setContextualPath(portalPath);
                        
                        url = pageURL.toString(false).trim();
                        //Obtenemos la lista de parametros de la peticion.
                    	String queryString = servletRequest.getQueryString();
                    	
                        if(tipo_inscripcion != null){
                        	//No necesitamos que el usuario este logeado
                        	Authentication.logout(servletRequest, true);
                        	//Formamos una nueva lista, sin los parametros idp y contexto.
                        	String [] params_values = queryString.split("[&]");
                        	queryString = "";
                        	/*
                        	 * Pasamos a minusculas para identificar el par "param=value"
                        	 * que no sera tenido en cuenta en el nuevo queryString
                        	 */
                        	String idp_param = DEFAULT_IDP_ATTTR_NAME.toLowerCase();
                        	String context_param = pageURL.getParameter("_pageLabel").toLowerCase();
                        	for(int i = 0; i < params_values.length; i++){
                        		//Paso a minusculas
                        		String pair_param_value = params_values[i].toLowerCase();
                        		if(!(pair_param_value.startsWith(idp_param) || 
                        				pair_param_value.startsWith(context_param))){
                        			queryString += (params_values[i] + DEFAULT_PARAMETER_SEPARATOR);
                        		}
                        	}
                        	queryString = queryString.substring(0, queryString.length()-1);
                        	
                        }
                        
                        url = url.concat(DEFAULT_PARAMETER_SEPARATOR + queryString);

                        LOGGER.info("URL para contexto: '" + pageURL.getParameter("_pageLabel")
                                + "' ,mercado '" + mercado + "' y flagBam ["+flagBam+"] obtenida");
                        
                        /*
                        pageURL.setTemplate("external-app-https");
                        pageURL.setContextualPath(portalPath);

                        url = pageURL.toString(false);
                        LOGGER.info("URL para contexto: '" + contexto
                                + "' y mercado '" + mercado + "' obtenida");
                         */

                    }
                }
                LOGGER.info("Agregando estadistica de login por contexto");
                this.agregarMarcaEstadistica(servletRequest, profile, EstadisticasHelper.TRANSACCION_OK ,"", "", request.getParameter("flagCautivo"));
            }

        } catch (LoginException e) {
            LOGGER.error(e.getMessage(), e);
            url = defaultURL.toString(true);            
            
        } catch (ProfileNotFoundException e) {
            LOGGER.error(e.getMessage(), e);
            url = defaultURL.toString(true);
            
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            url = defaultURL.toString(true);
            
        } finally {
            
            // realiza un refresco. en caso de que se haya autenticado y
            // obtenido el perfil, se autorizaran
            // los recursos portal a los que tenga derecho.
            LOGGER.info("redireccionando usuario hacia: '" + url + "'");
            servletResponse.sendRedirect(url);
//            chain.doFilter(request, response);

        }
        
    }

	public void init(FilterConfig filterConfig) throws ServletException {
	}

	public void destroy() {
	}

	/**
	 * Obtiene el parametro presente en <code>request</code> con el nombre
	 * <code>attributeName</code>.<br>
	 * Este metodo busca el parametro para los nombres
	 * <code>parameterName.toLowerCase()</code> y
	 * <code>parameterName.toUpperCase()</code>
	 * 
	 * @param parameterName
	 * @param request
	 * @return
	 */
	private String getNonSensitiveParameterValue(String parameterName,
			ServletRequest request) {

		String paramValue = null;

		paramValue = (String) request.getParameter(parameterName.toLowerCase());
		if (Utils.isEmptyString(paramValue)) {
			paramValue = (String) request.getParameter(parameterName
					.toUpperCase());
		}
		return paramValue;
	}
	
	
    /**
     * 
     * @param request
     * @param response
     */
    private void agregarMarcaEstadistica(HttpServletRequest servletRequest, ProfileWrapper profileWrapper, String casoOperacion, String msisdnNO, String rutNO, String flagCautivo){
        try{
            
            marcaEstadisticaBean = new MarcaEstadisticaBean();
            estadisticasDelegate = new EstadisticasDelegate();
            
            
            if(EstadisticasHelper.TRANSACCION_OK.equals(casoOperacion)){
         
            //ProfileWrapper profileWrapper = SessionHelper.getProfile(servletRequest);
            
            String atributo = ProfileWrapperHelper.getPropertyAsString(profileWrapper, "aaa");
            String msisdn = ProfileWrapperHelper.getPropertyAsString(profileWrapper, "numeroPcs");
            String rut = ProfileWrapperHelper.getPropertyAsString(profileWrapper, "rutUsuarioSeleccionado");
            String mercado = ProfileWrapperHelper.getPropertyAsString(profileWrapper, "mercado");
            String flagBam = ProfileWrapperHelper.getPropertyAsString(profileWrapper, "flagBam");
         
            marcaEstadisticaBean.setAtributoCliente(atributo);  
            marcaEstadisticaBean.setGrupo(flagBam != null && flagBam.equals("0") ? MiEntelProperties.getProperty("parametros.estadistica.login.grupo") : MiEntelProperties.getProperty("parametros.estadistica.login.grupobam"));
            marcaEstadisticaBean.setMsisdn(msisdn);
            marcaEstadisticaBean.setRut(rut);
            marcaEstadisticaBean.setSegmento(mercado);
            marcaEstadisticaBean.setServicio(flagBam != null && flagBam.equals("0") ? MiEntelProperties.getProperty("parametros.estadistica.login.servicio") : MiEntelProperties.getProperty("parametros.estadistica.login.serviciobam"));
            }else{
                marcaEstadisticaBean.setMsisdn(msisdnNO);
                marcaEstadisticaBean.setRut(rutNO);
            }
            //Datos comunes
            marcaEstadisticaBean.setFlagExitoFracasoOperacion(casoOperacion);
            marcaEstadisticaBean.setCampoOpcional1("");
            marcaEstadisticaBean.setCampoOpcional2("");
            marcaEstadisticaBean.setIp(servletRequest.getRemoteAddr());
            marcaEstadisticaBean.setFuncionalidad(MiEntelProperties.getProperty("parametros.estadistica.funcionalidad"));
            marcaEstadisticaBean.setOrigen(MiEntelProperties.getProperty("parametros.estadistica.origenweb"));
            
            //Validar si el usuario viene desde el Portal Cautivo	                       
            if(flagCautivo != null){
            	if ("2".equals(flagCautivo)){
            		LOGGER.info("Ingreso desde alerta");
            		marcaEstadisticaBean.setGrupo(MiEntelProperties.getProperty("parametros.estadistica.login.grupoalerta"));            		
            		marcaEstadisticaBean.setServicio(MiEntelProperties.getProperty("parametros.estadistica.login.servicio.alerta"));
            		marcaEstadisticaBean.setOrigen(MiEntelProperties.getProperty("parametros.estadistica.origenalerta"));
            	}else{
            	LOGGER.info("Ingreso desde home");
            	marcaEstadisticaBean.setServicio(MiEntelProperties.getProperty("parametros.estadistica.login.servicio.home"));
            	marcaEstadisticaBean.setOrigen(MiEntelProperties.getProperty("parametros.estadistica.origenhome"));         
            }
            }
            
            estadisticasDelegate.agregarMarcaEstadistica(marcaEstadisticaBean);
            
            
        }catch (Exception e) {
            LOGGER.error("No fue posible agregar marca estadistica de login");
        }
    }
	
}
