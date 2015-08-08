/* Propiedad de Entel PCS. Todos los derechos reservados */
package com.epcs.dashboard.mensajes.util;

import org.apache.log4j.Logger;

import com.bea.content.Node;
import com.bea.p13n.usermgmt.profile.ProfileWrapper;
import com.epcs.bean.MensajeParaTiBean;
import com.epcs.dashboard.mensajes.delegate.MensajesParaTiDelegate;
import com.epcs.recursoti.configuracion.Utils;
import com.epcs.recursoti.configuracion.uup.ProfileWrapperHelper;

/**
 * Abstraccion para los {@link MensajeHandler} de mensajes para ti
 * 
 * @author jlopez (I2B) en nombre de Absalon Opazo (Atencion al Cliente,
 *         EntelPcs)
 * 
 */
public abstract class AbstractMensajeHandler implements MensajeHandler {

    /**
     * Logger para AbstractMensajeHandler
     */
    private static final Logger LOGGER = Logger
            .getLogger(AbstractMensajeHandler.class);

    private MensajesParaTiDelegate delegate = new MensajesParaTiDelegate();

    public MensajesParaTiDelegate getDelegate() {
        return delegate;
    }
    
    public void setDelegate(MensajesParaTiDelegate delegate) {
        this.delegate = delegate;
    }
    

    @Override
    public MensajeParaTiBean getMensaje(ProfileWrapper profile) throws Exception {

        MensajeParaTiBean mensaje = null;
        
        String flagBam = null;
        String mercado = null;

        try {

            flagBam = ProfileWrapperHelper.getPropertyAsString(profile,
                    "flagBam");
            mercado = ProfileWrapperHelper.getPropertyAsString(profile,
                    "mercado");
        }
        catch (Exception e) {
            LOGGER.error("Mensaje no procesado: bad profile", e);
            LOGGER.error( new Exception("Mensaje no procesado: bad profile", e));
        }

        /*
         * Se obtiene el idContenido con el mensaje 'tipo'
         */
        String idContenido = getIdContenido(flagBam, mercado);
        if (Utils.isEmptyString(idContenido)) {
            LOGGER.error( new Exception("Mensaje no procesado: idContenido no encontrado"));
        }

        /*
         * Instancia del contenido portal con el mensaje 'tipo'
         */
        Node nodeContenidoMensaje = getNodeMensajes(idContenido);

        /*
         * Se obtiene el mensaje final para el usuario
         */
        mensaje = resolverMensaje(nodeContenidoMensaje, profile);

        return mensaje;
    }

    /**
     * Entrega el valor de idContenido para el contenido portal del 'mensaje
     * para ti' correspondiente
     * 
     * @return String con el idContenido
     */
    public String getIdContenido(String flagBam, String mercado) {
        return MensajesParaTiHelper.obtenerIdContenidoMensaje(getMensajeId(),
                flagBam, mercado);
    }

    /**
     * Entrega una instancia de {@link Node} con CM asociados a este Mensaje
     * 
     * @param idContenido
     * @return
     */
    protected Node getNodeMensajes(String idContenido) {
        try {
            return MensajesParaTiHelper.obtenerMensajeCM(idContenido);
        } catch (Exception e) {
            LOGGER.error("Contenido para el mensaje '" + getMensajeId()
                    + "' no pudo ser obtenido", e);
            return null;
        }
    }

    /**
     * Este metodo debe resolver la logica de llamada al servicio asociado al
     * mensaje y entregar un String con el mensaje a mostrar al usuario
     * 
     * @param nodeMensajes
     *            {@link Node} Contenido Portal con los mensajes 'tipo' del
     *            servicio
     * @param profile
     *            Perfil de usuario para quien se debe personalizar el mensaje
     * @return {@link MensajeParaTiBean} con el mensaje al usuario
     * @throws Exception
     */
    protected abstract MensajeParaTiBean resolverMensaje(Node nodeMensajes,
            ProfileWrapper profile) throws Exception;

    @Override
    public abstract String getMensajeId();

}
