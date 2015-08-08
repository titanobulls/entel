/* Propiedad de Entel Pcs. Todos los derechos reservados */
package com.epcs.recursoti.configuracion.tagext;

import javax.portlet.PortletPreferences;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.apache.log4j.Logger;

import com.bea.netuix.servlets.controls.portlet.PortletPresentationContext;
import com.bea.netuix.servlets.controls.portlet.backing.PortletBackingContext;
import com.bea.p13n.usermgmt.profile.ProfileWrapper;
import com.epcs.recursoti.configuracion.ArrayUtils;
import com.epcs.recursoti.configuracion.MiEntelProperties;
import com.epcs.recursoti.configuracion.Utils;
import com.epcs.recursoti.configuracion.uup.ProfileWrapperHelper;

/**
 * Custom Tag JSP para implemenar esquema de Derechos de Vistas (Views
 * Entitlements)<br>
 * 
 * El proceso de validacion consta de 2 pasos:
 * 
 * <p>
 * 
 * <b>1 Chequeo y seteo de variables</b><br>
 * 
 * Los atributos del tag que no son obligatorios son revisados, de no estar
 * definidos se toman los valores por defecto definidos en
 * <i>aplicacion.properties</i>:<br>
 * 
 * <ul>
 * 
 * <li><b><code>miEntel.views.security.value.default</code></b>: Debe indicar la
 * propiedad del perfil de usuario contra la cual se verifican los derechos de
 * vistas</li>
 * 
 * <li><b><code>miEntel.views.security.preference.default</code></b>: Debe
 * indicar el prefijo de la Portlet Preference que tiene las vistas permitidas
 * para un determinado valor de 'value'.<br>
 * El nombre de la preferencia se establece de la forma "prefijo" concatenado al
 * valor de <code>value</code>.</li>
 * 
 * </ul>
 * 
 * </p>
 * 
 * <p>
 * 
 * <b>2 Chequeo de derechos de vistas</b><br>
 * 
 * Se verifica que el usuario en sesion tenga derechos sobre la vista indicada
 * en el tag. Para ello se obtienen las vistar permitidas indicadas en la
 * preferencia y propiedad de usuario obtenidas en el paso 1
 * 
 * </p>
 * 
 * @author jlopez (I2B) en nombre de Absalon Opazo (Atencion al Cliente,
 *         EntelPcs)
 * 
 */
public class ViewTag extends BodyTagSupport {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Logger para ViewTag
     */
    private static final Logger LOGGER = Logger.getLogger(ViewTag.class);

    private ProfileWrapper profile;

    /*
     * Nombre de la vista
     */
    private String name;

    /*
     * Valor de la propiedad del perfil de usuario con que se evalua si tiene derechos o no
     */
    private String value;

    /*
     * Nombre portlet preference que indica las vistar permitidas
     */
    private String preference;

    /*
     * Flag para indicar que el tag debe validar inverso, es decir, si no tiene la vista permite ver
     * Por omision, el valor es false
     */
    private String inverse;
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getPreference() {
        return preference;
    }

    public void setPreference(String preference) {
        this.preference = preference;
    }

    public String getInverse() {
		return inverse;
	}
    
    public void setInverse(String inverse) {
		this.inverse = inverse;
	}

    /**
     * Wrapper para propiedad inverse, para consultarla como variable booleana
     * @return
     */
	private boolean isInverse() {
		if (Utils.isEmptyString(inverse)) {
			return false;
		} else {
			return Boolean.valueOf(inverse);
		}
	}
    
    /**
     * Este metodo se divide en 2 momentos:
     * <ul>
     * 
     * <li>1 - Chequeo y seteo de variables no obligatorias: value y preference.
     * <br>
     * Si alguna no esta definida se emplean los valores default, definidos en
     * las propiedades de aplicacion.properties siguiente:<br>
     * <ul>
     * <li><b><code>miEntel.views.security.value.default</code></b>: Debe
     * indicar la propiedad del perfil de usuario contra la cual se verifican
     * los derechos de vistas</li>
     * <li><b><code>miEntel.views.security.preference.default</code></b>: Debe
     * indicar el prefijo de la Portlet Preference que tiene las vistas
     * permitidas para un determinado valor de 'value'.<br>
     * El nombre de la preferencia se establece de la forma "prefijo"
     * concatenado al valor de <code>value</code>.
     * 
     * </li>
     * </ul>
     * 
     * <li>2 - Chequeo si el usuario en sesion tiene derechos sobre al vista
     * indicada, de acuerdo a los valores obtenidos en 1er punto</li>
     * 
     * </ul>
     */
    public int doStartTag() throws JspException {

        profile = ProfileWrapperHelper.getProfile(pageContext.getSession());

        // Paso 1: chequeo y seteo de variables no obligatorias
        initialVarsCheck();

        // Paso 2: evaluacion de derechos de vistas
        if (checkViewsEntitlements()) {
            return EVAL_BODY_INCLUDE;
        }
        else {
            return SKIP_BODY;
        }

    }

    public void doInitBody() throws JspException {
        super.doInitBody();
    }

    public int doAfterBody() throws JspException {
        return SKIP_BODY;
    }

    public int doEndTag() throws JspException {
        return EVAL_PAGE;
    }

    // -------------------------------------- private methods

    /**
     * Metodo utilitario para paso 1 de doStartTag()
     */
    protected void initialVarsCheck() throws JspException {
        try {
            if (Utils.isEmptyString(value)) {
                value = getDefaultValue();
            }

            if (Utils.isEmptyString(preference)) {
                preference = getDefaultPreferenceName();
            }
            
            if(Utils.isEmptyString(inverse)) {
            	inverse = "false";
            }

        } catch (Exception e) {
            LOGGER.error("Exception caught on initial vars check", e);
            LOGGER.error( new JspException("Las variables iniciales 'value' y "
                    + "'preference' no pudieron ser determindas", e));
        }
    }

    
	/**
     * Este metodo retorna el valor de la propiedad
     * <code>miEntel.views.security.preference.value</code> concatenado al valor
     * de <code>value</code>
     */
    private String getDefaultPreferenceName() throws Exception {

        return MiEntelProperties
                .getProperty("miEntel.views.security.preference.default")
                + value;
    }

    /**
     * Entrega el AAA que es la propiedad por default para evaluar una vista
     * 
     * @return
     * @throws Exception
     */
    private String getDefaultValue() throws Exception {

        String defaultValue = MiEntelProperties
                .getProperty("miEntel.views.security.value.default");

        if (Utils.isEmptyString(defaultValue)) {
            LOGGER.error( new Exception("No esta definida la propiedad '"
                    + defaultValue + "' necesaria para que opere el tag 'view'"));
        }

        try {
            return ProfileWrapperHelper.getPropertyAsString(profile,
                    defaultValue);
        } catch (Exception e) {
            LOGGER.error( new Exception("No fue posible obtener propiedad '"
                    + defaultValue + "'", e));
        }
        return "";
    }

    /**
     * Metodo utilitario para paso 2 de doStartTag()<br>
     * 
     * Este metodo recupera las vistas definidas en la Portlet prefecence
     * <code>preference</code> y revisa que <code>name</code> este en esa lista.<br>
     * Si <code>name</code> esta contenido, el metodo retorna true, en caso
     * contrario false
     */
    protected boolean checkViewsEntitlements() {

        String[] views = getAvailablesViews(getPreference());

        boolean check = false;
        if (views == null) {
            check = false;
        }
        else {
        	check = ArrayUtils.contains(views, getName());
        }
        
        /*
         * Si inverse=true: la presencia de la vista significa NO permitir ver.
         * Por el contrario, si inverse=false (valor por default) la presencia de la vista
         * significa SI permite ver
         */
        if(isInverse()) {
        	return !check;
        } else {
        	return check;
        }

    }

	/**
     * Entrega la lista de vistas definidas para la Portlet preference indicada
     * en <code>preferenceName</code>
     * 
     * @param preferenceName
     * @return String[] con las vistas, null en caso de no se encontradas, o no
     *         existir vistas definidas para la Portlet preference
     */
    private String[] getAvailablesViews(String preferenceName) {

        HttpServletRequest request = (HttpServletRequest) pageContext
                .getRequest();

        String[] views = null;

        try {

            // Obtencion vistas definidas para la preferencia
            String availableViews = getPreference(request, preferenceName, "");
            if (Utils.isEmptyString(availableViews)) {
                LOGGER.warn("No hay vistas definidas para '" + preferenceName
                        + "'");
            }
            else {
                // Separador
                String separator = getViewsSeparator();
                return ArrayUtils.parse(availableViews, separator);
            }

        } catch (Exception e) {
            LOGGER.error("Exception caught recovering availables "
                    + "views for '" + preferenceName + "'", e);
        }

        return views;
    }

    private String getPreference(HttpServletRequest request,
            String preferenceName, String defaultValue) throws Exception {

        PortletPreferences prefs = null;
        PortletBackingContext pbc = PortletBackingContext
                .getPortletBackingContext(request);

        if (pbc != null) {
            prefs = pbc.getPortletPreferences(request);
        }
        else {
            PortletPresentationContext ppc = PortletPresentationContext
                    .getPortletPresentationContext(request);
            if (ppc != null) {
                prefs = ppc.getPortletPreferences(request);
            }
        }

        if (prefs != null) {
            return prefs.getValue(preferenceName, defaultValue);
        }
        else {
            LOGGER.error( new Exception(
                    "No es posible obtener el contexto de preferencias del portlet"));
        }
        return "";
    }

    private String getViewsSeparator() {

        String separator = MiEntelProperties
                .getProperty("miEntel.views.security.preference.separator");
        if (Utils.isEmptyString(separator)) {
            LOGGER
                    .warn("No existe definicion para el caracter separador de "
                            + "vistas, o fue definido vacio, empleara el caracter coma (,)");
            separator = ",";
        }
        return separator;

    }

}
