/* Propiedad de Entel PCS. Todos los derechos reservados */
package com.epcs.recursoti.configuracion.jsf.validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import org.apache.log4j.Logger;
/**
 * @author jmanzur (I2B) en nombre de Absalon Opazo (Atencion al Cliente,
 *         EntelPcs)
 * 
 */
public class EmailValidator implements Validator {
	
	private static final Logger LOGGER = Logger.getLogger(EmailValidator.class);

    /*
     * Determina si el email introducido es valido.
     * 
     * @see
     * javax.faces.validator.Validator#validate(javax.faces.context.FacesContext
     * , javax.faces.component.UIComponent, java.lang.Object)
     */
    @Override
    public void validate(FacesContext facesContext, UIComponent component,
            Object value) throws ValidatorException {
        String enteredEmail = (String) value;
        // Configuracion del patron de email valido.
        Pattern p = Pattern.compile(".+@.+\\.[a-z]+");

        // Compara el email introducido con el patron.
        Matcher m = p.matcher(enteredEmail);

        boolean matchFound = m.matches();

        if (!matchFound) {
            FacesMessage message = new FacesMessage();
            message.setDetail("Ingresa un Email Valido");
            message.setSummary("Ingresa un Email Valido");
            message.setSeverity(FacesMessage.SEVERITY_ERROR);
            LOGGER.error( new ValidatorException(message));
        }

    }

}
