/* Propiedad de Entel PCS. Todos los derechos reservados */
package com.epcs.recursoti.configuracion.tagext;

import java.util.MissingResourceException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.log4j.Logger;

import com.epcs.recursoti.configuracion.MiEntelProperties;

/**
 * @author jlopez (I2B) en nombre de Absalon Opazo (Atencion al Cliente,
 *         EntelPcs)
 * 
 */
public class PropertyTag extends TagSupport {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Logger para PropertyTag
     */
    private static final Logger LOGGER = Logger.getLogger(PropertyTag.class);

    private String name;

    private String var;

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     *            the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the var
     */
    public String getVar() {
        return var;
    }

    /**
     * @param var
     *            the var to set
     */
    public void setVar(String var) {
        this.var = var;
    }

    /* (non-Javadoc)
     * @see javax.servlet.jsp.tagext.TagSupport#doStartTag()
     */
    @Override
    public int doStartTag() throws JspException {
        
        String property = "";
        try {
            property = MiEntelProperties.getProperty(getName());
            LOGGER.debug("property '" + getName() + "' : '" + property + "'");
        } catch (MissingResourceException e) {
            LOGGER.debug("propiedad '" + getName() + "' no presente");
        } catch (Exception e) {
            LOGGER.error("Exception al consultar propiedad '" + getName() + "'", e);
            LOGGER.error( new JspException(e));
        }

        pageContext.setAttribute(getVar(), property);
        return SKIP_BODY;
    }
    
    
}
