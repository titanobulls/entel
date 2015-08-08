/* Propiedad de Entel PCS. Todos los derechos reservados */
package com.epcs.seguridad.spi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.auth.login.FailedLoginException;
import javax.security.auth.login.LoginException;
import javax.security.auth.spi.LoginModule;

import org.apache.log4j.Logger;

import weblogic.security.principal.WLSAbstractPrincipal;
import weblogic.security.principal.WLSGroupImpl;
import weblogic.security.principal.WLSUserImpl;

import com.epcs.seguridad.bean.ApplicationLoginBean;
import com.epcs.seguridad.bean.ApplicationLoginRespuestaBean;

/** 
 * Implementacion de LoginModule que reconoce a las aplicaciones que desean utilizar MiEntel, 
 * a traves de un identificador IDP valido.
 * 
 * @author mmartinez (I2B) en nombre de Absalon Opazo (Atencion al Cliente, EntelPcs)
 */
final public class EPCSApplicationLoginModuleImpl implements LoginModule {
    
    /**
     * Log del autenticador.
     */
    private static final Logger logger = Logger.getLogger(EPCSApplicationLoginModuleImpl.class);
  
    /**
     * Constante que define el nombre por defecto del grupo en el que se inscriben las aplicaciones que se autentican.
     */
    private static final String DEFAULT_USER_GROUPNAME = "Administrators";
    
    /**
     * Valor que se espera que el servicio entregue, para cuando una aplicacion tiene un idp valido para ingresar.
     */
    private static final String DEFAULT_VALIDATED_APP_KEY = "0000";

    /**
     * Separador en username
     */
    private static final String DEFAULT_SEPARATOR = "\\|";
    
    /**
     * Subject (JAAS).
     */
    private Subject subject;
    
    /**
     * CallBackHandler que maneja las credenciales de autenticacion (IDP).
     */
    private CallbackHandler callbackHandler;
    
    /**
     * DAO que se comunica con el servicio de autenticacion de aplicaciones.
     */
    private ApplicationLoginDAO loginService;
    
    /**
     * Estado de la autenticacion
     */
    private Boolean loginSucceeded;
    
    /**
     * Define si dentro del Subject existe al menos un principal validado.
     */
    private boolean principalsInSubject;
    
    /**
     * Lista en la que se inscribe la aplicacion autenticada.
     */
    private final List<WLSAbstractPrincipal> principalsForSubject = new ArrayList<WLSAbstractPrincipal>();
    
    /**
     * Inicializacion de artefactos de autenticacion.
     */
    public void initialize(final Subject subject, final CallbackHandler callbackHandler,
            final Map<String,?> sharedState, final Map<String,?> options) {
        logger.info("initialize");
		logger.info("2");
        this.subject = subject;
        this.callbackHandler = callbackHandler;
        try {
        	loginService = (ApplicationLoginDAO) options.get(EPCSApplicationAuthenticatorImpl.LOGIN_SERVICE);
			logger.info("LOGIN SERVICE",loginService);
        } catch (Exception e) {
			logger.error("loginService not initialized", e);
		}
        
        
    }

    /**
     * Metodo que permite autenticar una aplicacion.
     * @return true si el idp es valido.
     * @throws LoginException si el idp no es valido (falla la autenticacion)
     */
    public boolean login() throws LoginException {
        logger.info("login");
        final Callback[] callbacks = getCallbacks();

        final String[] splitted = getIdp(callbacks).split(DEFAULT_SEPARATOR);
		logger.info("SPPLITED LOGIN => ",spplited);
        final String idp = splitted[0];
        final String token = splitted[1];
		
		logger.info("IDP=> ",idp);
        
		final ApplicationLoginBean bean = new ApplicationLoginBean();
        bean.setIdp(idp);

        logger.info("login => idp [" + idp + "]");
        logger.info("login => token [" + token + "]");
        ApplicationLoginRespuestaBean respuesta = null;

        try {
        	respuesta = loginService.login(bean);
        } catch (Exception e) {
			logger.error("Exception caught on login", e);
			throw new LoginException(e.getMessage());
		} catch (Throwable e) {
			logger.error("Throwable caught on login. Check MiEntel-V3 mbeantypes jars!", e);
			throw new LoginException(e.getMessage());
		}

		if(respuesta != null) {
			logger.info("codigoRespuesta " + respuesta.getCodigoRespuesta());
			logger.info("mensajeRespuesta " + respuesta.getMensajeRespuesta());
			logger.info("nombreSujeto " + respuesta.getNombreSujeto());
		}

		if (!DEFAULT_VALIDATED_APP_KEY.equals(respuesta.getCodigoRespuesta())
				|| isEmptyString(respuesta.getNombreSujeto())) {
			throwFailedLoginException("Fallo en la autenticacion de la aplicacion: idp ["
					+ idp + "] no es valido.");
		}

        try {
            loginSucceeded = true;
            String principal = respuesta.getNombreSujeto() + "|" + token;
            principalsForSubject.add(new WLSUserImpl(principal));
            logger.info("Principal user constructed: " + principalsForSubject.toString());
            addDefaultGroupForSubject();
        } catch (Exception e) {
			logger.error("login failed!", e);
			loginSucceeded = false;
		} catch (Throwable th) {
			logger.error("Unexpected Throwable in loggin", th);
			loginSucceeded = false;
		}

        logger.info("login => result " + loginSucceeded);
        return loginSucceeded;
    }

    /**
     * Los principal se agregan en el metodo commit ya que no hay que olvidar que nuestro 
     * LoginModule convive eventualmente en una cadena de modulos de autenticacion y es posible que 
     * alguno de ellos no valide con lo que si completamos el subject antes de llamar a todos
     * los login() del dominio vamos a darle identidades a nuestro sujeto que no queremos.
     * 
     */
    public boolean commit() throws LoginException {

        logger.info("loggin succeeded... [" + loginSucceeded + "]");

        try {
	        boolean result = false;
	        if (loginSucceeded) {
	            subject.getPrincipals().addAll(principalsForSubject);
	            principalsInSubject = true;
	            result = true;
	        }
	        return result;
        } catch (Exception e) {
			logger.error("commit failed", e);
			throw new LoginException(e.getMessage());
		}
    }

    /**
     * Este metodo permite realizar un "rollback" en caso de que la autencion falle.
     */
    public boolean abort() throws LoginException {
        
        logger.info("login abborting... [" + principalsInSubject + "]");
        try {
	        if (principalsInSubject) {
	            subject.getPrincipals().removeAll(principalsForSubject);
	            principalsInSubject = false;
	        }
	        return true;
        } catch (Exception e) {
			logger.error("abort failed", e);
			throw new LoginException(e.getMessage());
		}
    }

    /**
     * Este metodo no deberia ser llamado!.
     */
    public boolean logout() throws LoginException {
        logger.info("loggout...");
        return true;
    }

    /**
     * metodo que permite registrar un error un arrojar una excepcion de tipo LoginException.
     * @param msg mensaje de la excepcion correspondiente.
     * @throws LoginException el wrapper del error arrojado.
     */
    private void throwLoginException(final String msg) throws LoginException {
        logger.error(msg);
        throw new LoginException(msg);
    }
    
    /**
     * metodo que permite registrar un error un arrojar la excepcion correspondiente.
     * @param msg mensaje de la excepcion correspondiente.
     * @throws LoginException el wrapper del error arrojado.
     */
    private void throwLoginException(final Throwable exception) throws LoginException {
        logger.error(exception.getMessage(),exception);
        throw new LoginException(exception.getMessage());
    }
    
    /**
     * metodo que permite registrar un error un arrojar una excepcion de tipo FailedLoginException.
     * @param msg mensaje de la excepcion correspondiente.
     * @throws LoginException el wrapper del error arrojado.
     */
    private void throwFailedLoginException(final String msg)
            throws FailedLoginException {
        logger.error(msg);
        throw new FailedLoginException(msg);
    }

    /**
     * Metodo que se encarga de recuperar el callback de autenticacion para llamar al handler.
     * @return el callback donde se encuentra el idp
     * @throws LoginException en caso de que no se hayan definido elementos necesarios para obtener el idp (CallbackHandler, LoginDAO).
     */
    private Callback[] getCallbacks() throws LoginException {
        if (callbackHandler == null) {
            throwLoginException("No se especifico ningun CallbackHandler");
        }
        if (loginService == null) {
            throwLoginException("No se ha definido un DAO para autenticacion de aplicaciones");
        }

        final Callback[] callbacks = new Callback[]{new NameCallback("idp: ")};        

        try {
            callbackHandler.handle(callbacks);
        } catch (IOException ioe) {
            throwLoginException(ioe);
        } catch (UnsupportedCallbackException uce) {
            throwLoginException(uce);
        }
        return callbacks;
    }

    /**
     * Metodo que se encarga de obtener el valor del IDP encapsulado en el Callback de autenticacion.
     * @param callbacks callback de autenticacion (JAAS).
     * @return el valor del idp que se pretende validar
     * @throws LoginException en caso de que no se haya definido un valor para el idp
     */
    private String getIdp(final Callback[] callbacks) throws LoginException {
        final String idp = ((NameCallback) callbacks[0]).getName();        
        if (idp == null) {
            throwLoginException("No existe un valor de IDP asociado");
        }
        return idp;
    }

    /**
     * Agrega en la lista de entidades de autenticacion al grupo por defecto para las aplicaciones externas.
     */
    private void addDefaultGroupForSubject() {
        logger.info("Adding group ["+DEFAULT_USER_GROUPNAME+"]");
        principalsForSubject.add(new WLSGroupImpl(DEFAULT_USER_GROUPNAME));
    }
    
    private boolean isEmptyString(String str) {
    	return (str == null || str.trim().equals(""));
    }
}