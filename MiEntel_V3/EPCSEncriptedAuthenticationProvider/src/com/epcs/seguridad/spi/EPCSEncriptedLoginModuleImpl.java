/* Propiedad de Entel PCS. Todos los derechos reservados */
package com.epcs.seguridad.spi;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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

/** 
 * Implementacion de LoginModule que reconoce a las aplicaciones que desean utilizar MiEntel, 
 * a traves de un identificador IDP valido.
 * 
 * @author esuarez (Tecnova)
 */
final public class EPCSEncriptedLoginModuleImpl implements LoginModule {
    
    /**
     * Log del autenticador.
     */
    private static final Logger logger = Logger.getLogger(EPCSEncriptedLoginModuleImpl.class);
  
    /**
     * Constante que define el nombre por defecto del grupo en el que se inscriben las aplicaciones que se autentican.
     */
    private static final String DEFAULT_USER_GROUPNAME = "Administrators";

    /**
     * Subject (JAAS).
     */
    private Subject subject;
    
    /**
     * CallBackHandler que maneja las credenciales de autenticacion (IDP).
     */
    private CallbackHandler callbackHandler;
    
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
        this.subject = subject;
        this.callbackHandler = callbackHandler;        
    }

    /**
     * Metodo que permite autenticar una aplicacion.
     * @return true si el idp es valido.
     * @throws LoginException si el idp no es valido (falla la autenticacion)
     */
    public boolean login() throws LoginException {
        logger.info("login");
        final Callback[] callbacks = getCallbacks();
        final String id = getId(callbacks);
        final String clave = "ClaveIPBAMPP_2013";
        final Date date=new Date();
    	long actual=date.getTime();
    	
        ArcFour a4=new ArcFour();  
        final String desencriptado = a4.desencriptar(id, clave);
        final String[] splitted = desencriptado.split(":");
        logger.error("SPLITTED =>", splitted);
        if (splitted.length != 4) {
			logger.error("Fallo en la autenticacion de la aplicacion: id ["
					+ id + "] no es valido.");
		}
		
        final String msisdn = splitted[0];
        final String ip = splitted[1];
        final String schar = splitted[2];
        final String timestamp = splitted[3];

        logger.info("login => id [" + id + "]");
        logger.info("msisdn " + msisdn);
		logger.info("ip " + ip);
		logger.info("schar " + schar);
		logger.info("timestamp " + timestamp);

		long stamp=0;
		
		try{
			stamp=Long.parseLong(timestamp);
		}catch (NumberFormatException e) {
			throwFailedLoginException("Fallo en la autenticacion de la aplicacion: id ["
					+ id + "] posee un timestamp que no es valido.");
		}finally{
			stamp+=60000;//Le damos 1 minuto 
		}
		
		if (stamp<actual) {
			throwFailedLoginException("Fallo en la autenticacion de la aplicacion: id ["
					+ id + "] posee un timestamp que esta vencido.");
		}
		
        try {
            loginSucceeded = true;
            String principal = msisdn + "|" + timestamp;
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
        
        final Callback[] callbacks = new Callback[]{new NameCallback("id: ")};        

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
     * Metodo que se encarga de obtener el valor del ID encapsulado en el Callback de autenticacion.
     * @param callbacks callback de autenticacion (JAAS).
     * @return el valor del idp que se pretende validar
     * @throws LoginException en caso de que no se haya definido un valor para el id
     */
    private String getId(final Callback[] callbacks) throws LoginException {
        final String id = ((NameCallback) callbacks[0]).getName();        
        if (id == null) {
            throwLoginException("No existe un valor de ID asociado");
        }
		logger.info("GETID => ", id);
        return id;
    }

    /**
     * Agrega en la lista de entidades de autenticacion al grupo por defecto para las aplicaciones externas.
     */
    private void addDefaultGroupForSubject() {
        logger.info("Adding group ["+DEFAULT_USER_GROUPNAME+"]");
        principalsForSubject.add(new WLSGroupImpl(DEFAULT_USER_GROUPNAME));
    }
    
    public static void main(String[] args){
    	//MSISDN:IP:SCHAR:TIMESTAMP
    	Date date=new Date();
    	long diff=date.getTime();
    	final String normal="56996961873:127.0.0.1:0402:"+diff;
    	final String clave = "ClaveIPBAMPP_2013";
        //ID=B37296DB374C69AE34DAB934639C948BD21F223B944E461315BEF838A8F378
        ArcFour a4=new ArcFour();  
        final String encriptado = a4.encriptar(normal, clave);
    	System.out.println("Encriptado: "+encriptado);
    	final String desencriptado = a4.desencriptar(encriptado, clave);
    	System.out.println("Desencriptado: "+desencriptado);
    	
    	final String[] splitted = desencriptado.split(":");        
        final String timestamp = splitted[3];
        
        date=new Date();
        long actual=date.getTime();
        long stamp=0;
		
		try{
			stamp=Long.parseLong(timestamp);
		}catch (NumberFormatException e) {
			System.out.println("Timestamp invalido.");
		}finally{
			stamp+=60000;//Le damos 1 minuto 
		}
		
		if (stamp<actual) {
			System.out.println("Timestamp vencido.");
			System.out.println("."+stamp+":"+actual);
		}else{
			System.out.println("Timestamp ok."+new SimpleDateFormat("hhmmss").format(stamp)+":"+new SimpleDateFormat("hhmmss").format(actual));
		}
    }
}