/* Propiedad de Entel PCS. Todos los derechos reservados */
package com.epcs.seguridad.spi;

import java.util.HashMap;
import java.util.Map;

import javax.security.auth.login.AppConfigurationEntry;
import javax.security.auth.login.AppConfigurationEntry.LoginModuleControlFlag;

import org.apache.log4j.Logger;

import weblogic.management.security.ProviderMBean;
import weblogic.security.provider.PrincipalValidatorImpl;
import weblogic.security.spi.AuthenticationProviderV2;
import weblogic.security.spi.IdentityAsserterV2;
import weblogic.security.spi.PrincipalValidator;
import weblogic.security.spi.SecurityServices;

/**
 * Un proveedor de autenticacion para los servicios de autenticacion que expone
 * el framework de seguridad de Weblogic. Este le permite manipular el proveedor
 * de seguridad (initialized, started, stopped, etc).
 * 
 * @author mmartinez (I2B) en nombre de Absalon Opazo (Atencion al Cliente,
 *         EntelPcs)
 */
public final class EPCSAuthenticatorImpl implements AuthenticationProviderV2 {
    /**
     * Constante que encapsula el valor que identifica el control de acceso
     * "SUFFICIENT".
     */
    private static final String SUFFICIENT = "SUFFICIENT";

    /**
     * Constante que encapsula el valor que identifica el control de acceso
     * "REQUISITE".
     */
    private static final String REQUISITE = "REQUISITE";

    /**
     * Constante que encapsula el valor que identifica el control de acceso
     * "OPTIONAL".
     */
    private static final String OPTIONAL = "OPTIONAL";

    /**
     * Constante que encapsula el valor que identifica el control de acceso
     * "REQUIRED".
     */
    private static final String REQUIRED = "REQUIRED";

    /**
     * Constante que define si corresponde a una asercion de identidad.
     */
    private static final String IDENTITY_ASSERTION = "IdentityAssertion";

    /**
     * Constante con el nombre de la clave para obtener el servicio.
     */
    public static final String LOGIN_SERVICE = "loginService";

    /**
     * atributo de descripcion del provider.
     */
    private String description;

    /**
     * DAO que implementa la logica de llamada al servicio de autenticacion.
     */
    private LoginDAO loginDAO;

    /**
     * politica de manejo de errores del provider.
     */
    private LoginModuleControlFlag controlFlag;

    /**
     * Log del autenticador.
     */
    private static final Logger logger = Logger.getLogger(EPCSAuthenticatorImpl.class);

    /**
     * Mapeo de opciones que require el autenticador.
     */
    private final Map<String, Object> options = new HashMap<String, Object>();

    /**
     * inicializacion del provider.
     */
	public void initialize(final ProviderMBean mbean,
			final SecurityServices services) {
		
		logger.info("EPCSAuthenticatorImpl initialize...");
		loginDAO = new LoginDAO();
		EPCSAuthenticationProviderMBean myMBean = (EPCSAuthenticationProviderMBean) mbean;
		logger.info("1");
		StringBuffer sb = new StringBuffer();
		sb.append(myMBean.getDescription());
		sb.append("\n");
		sb.append(myMBean.getVersion());
		logger.info("2");
		description = sb.toString();
		logger.info("EPCSAuthenticatorImpl -> Description:" + description);
		logger.info("3");
		String flag = myMBean.getControlFlag();
		logger.info("EPCSAuthenticatorImpl -> ControlFlag:" + flag);
		logger.info("4");
		if (REQUIRED.equalsIgnoreCase(flag)) {
			controlFlag = LoginModuleControlFlag.REQUIRED;
		} else if (OPTIONAL.equalsIgnoreCase(flag)) {
			controlFlag = LoginModuleControlFlag.OPTIONAL;
		} else if (REQUISITE.equalsIgnoreCase(flag)) {
			controlFlag = LoginModuleControlFlag.REQUISITE;
		} else if (SUFFICIENT.equalsIgnoreCase(flag)) {
			controlFlag = LoginModuleControlFlag.SUFFICIENT;
		} else {
			String errorMsg = "flag de manejo de errores invalido [" + flag
					+ "]";
			logger.error(errorMsg);
			throw new IllegalArgumentException(errorMsg);
		}
	}

    /**
     * Permite obtener la descripcion del provider.
     */
    public String getDescription() {
        return description;
    }

    /*
     * @see weblogic.security.spi.SecurityProvider#shutdown()
     */
    public void shutdown() {
        logger.info("EPCSAuthenticatorImpl -> SecurityProvider#shutdown...");
    }

    /**
     * Permite obtener la configuracion para el LoginModule del provider de autenticacion de usuarios.
     * @return la configuracion especifica JAAS para este provider que es necesaria para validar el usuario.
     */
    public AppConfigurationEntry getLoginModuleConfiguration() {
        options.put(LOGIN_SERVICE, loginDAO);
        return new AppConfigurationEntry(EPCSLoginModuleImpl.class
                .getName(), controlFlag, options);
    }

    /**
     * Permite obtener la configuracion para el LoginModule del provider de aserciones de identidad.
     * @return la configuracion especifica JAAS para este provider que es necesaria para ejecutar la autenticacion.
     */
    public AppConfigurationEntry getAssertionModuleConfiguration() {
        options.put(IDENTITY_ASSERTION, true);
        return new AppConfigurationEntry(EPCSLoginModuleImpl.class
                .getName(), controlFlag, options);
    }

    /**
     * Obtiene el provider de la autenticacion asociado al provider de validacion del principal.
     */
    public PrincipalValidator getPrincipalValidator() {
        return new PrincipalValidatorImpl();
    }

    /**
     * obtiene el provider de la autenticacion asociado al provider de asercion de identidades.
     */
    public IdentityAsserterV2 getIdentityAsserter() {
        return null;
    }
}
