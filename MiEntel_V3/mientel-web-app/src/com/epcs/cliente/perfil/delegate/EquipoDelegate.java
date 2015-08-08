/* Propiedad de Entel PCS. Todos los derechos reservados */
package com.epcs.cliente.perfil.delegate;

import java.io.Serializable;
import java.util.List;

import com.epcs.bean.BloqueoDesbloqueoEquipoBean;
import com.epcs.bean.BloqueoEquipoBean;
import com.epcs.bean.ConsultarBloqueoDesbloqueoBean;
import com.epcs.bean.ConsultarDatosBuicBean;
import com.epcs.bean.ContactoPorRenovacionBean;
import com.epcs.bean.DatosUsuarioBloqueoBean;
import com.epcs.bean.DocumentoAperturaOTBean;
import com.epcs.bean.EquipoFullBean;
import com.epcs.bean.Equipo4GLteBean;
import com.epcs.bean.GrupoClienteBean;
import com.epcs.bean.InformeTecnicoOTBean;
import com.epcs.bean.ResumenLineaEquipoBean;
import com.epcs.bean.TraficoEquiposBean;
import com.epcs.bean.OrdenTrabajoVigenteBean;
import com.epcs.bean.PinPukBean;
import com.epcs.bean.ResumenEquipoBean;
import com.epcs.bean.SituacionActualEquipoBean;
import com.epcs.bean.SolicitaIphoneBean;
import com.epcs.cliente.perfil.dao.EquipoDAO;
import com.epcs.erp.legal.consultaequiposrobados.ConsultaEquipoRobadoFaultMessage;
import com.epcs.recursoti.excepcion.DAOException;
import com.epcs.recursoti.excepcion.ServiceException;
import com.epcs.bean.EquipoFullBean;

/**
 * @author jlopez (I2B) en nombre de Absalon Opazo (Atencion al Cliente,
 *         EntelPcs)
 * 
 */
public class EquipoDelegate implements Serializable {
	

	private static final long serialVersionUID = 1L;
	
//    private static final Logger LOGGER = Logger.getLogger(EquipoDelegate.class);

    private EquipoDAO equipoDAO;

    /**
     * @return the equipoDAO
     */
    public EquipoDAO getEquipoDAO() {
        return equipoDAO;
    }

    /**
     * @param equipoDAO
     *            the equipoDAO to set
     */
    public void setEquipoDAO(EquipoDAO equipoDAO) {
        this.equipoDAO = equipoDAO;
    }

    /**
     * 
     * @return bean con los datos del equipo del usuario en Sesion
     */
    public ResumenEquipoBean getResumenEquipo(String numeroPcs)
            throws DAOException, ServiceException {
        return this.equipoDAO.getResumenEquipo(numeroPcs);
    }
    
    /**
     * 
     * @return bean con los datos del situacion actual equipo
     */
    public SituacionActualEquipoBean obtenerSituacionActualEquipo(
    		String numeroPCS, String rut, String nroCuenta,
    		String rutSinDV, String DV
    		)throws DAOException, ServiceException {
    	return this.equipoDAO.obtenerSituacionActualEquipo(numeroPCS, rut, nroCuenta, rutSinDV, DV);    	
    }
    
    /**
     * 
     * @return bean con los datos del PIN-PUK
     */
    public PinPukBean consultarPinPuk(String numeroPCS)throws Exception{
    	return this.equipoDAO.consultarPinPuk(numeroPCS);
    }

    /**
     * 
     * Delegate solicita iphone
     */
    public void solicitaIphone(SolicitaIphoneBean solicitaIphone)throws DAOException, ServiceException{
    	equipoDAO.solicitaIphone(solicitaIphone);
    }
    
    /**
     * 
     * Delegate contacto por renovacion
     */
    public void contactoPorRenovacion(ContactoPorRenovacionBean contacto)throws DAOException, ServiceException{
    	equipoDAO.contactoPorRenovacion(contacto);
    }
    
    /**
     * 
     * Delegate contacto por renovacion
     * @return DatosUsuarioBloqueoBean
     */
    public DatosUsuarioBloqueoBean obtenerDatosUsuarioBloqueo(String rutSinDV, String DV)
    throws DAOException, ServiceException {
    	return equipoDAO.obtenerDatosUsuarioBloqueo(rutSinDV, DV);
    }
    
    /**
     * 
     * Actualizar datos de bloueo de usuario
     */
    public void actualizarDatosUsuarioBloqueo(
    		DatosUsuarioBloqueoBean datosUsuarioBloqueo, String rutSinDV, 
    		String DV, String numeroPCS)
    throws DAOException, ServiceException {
    	equipoDAO.actualizarDatosUsuarioBloqueo(datosUsuarioBloqueo, rutSinDV, DV, numeroPCS);
    }
    
    /**
     * 
     * Delegado Bloqueo por extravio
     * 
     */
    public void bloquearEquipoPorExtravio(BloqueoEquipoBean bloq)throws DAOException, ServiceException {
    	equipoDAO.bloquearEquipoPorExtravio(bloq);
    }
    
    /**
     * 
     * Delegado Bloqueo por robo
     * 
     */
    public void bloquearEquipoPorRobo(BloqueoEquipoBean bloq)throws DAOException, ServiceException {
    	equipoDAO.bloquearEquipoPorRobo(bloq);
    }
    
    /**
     * 
     * Delegado bloqueo existente
     * @return boolean
     */
    public boolean tieneBloqueoExistente(String numeroPCS,String mercado,String DVImei)throws DAOException, ServiceException {
    	return equipoDAO.tieneBloqueoExistente(numeroPCS,mercado,DVImei);
    }
    
    
    /**
     * 
     * Delegado Ordenes vigentes
     * @return OrdenTrabajoVigenteBean
     */
    public OrdenTrabajoVigenteBean obtenerOrdenesDeTrabajoVigente(String numeroPCS)throws DAOException, ServiceException {
    	return equipoDAO.obtenerOrdenesDeTrabajoVigente(numeroPCS);
    }
    
    /**
     * 
     * Delegado Imei
     * @return String
     */
    public String obtenerImei(String numeroPCS)throws DAOException, ServiceException {
    	return equipoDAO.obtenerImei(numeroPCS);
    }
    
    /**
     * 
     * Delegado aceptar presupuesto
     */
    public void aceptarRechazarPresupuesto(String nroOrden, String accion)
    throws DAOException, ServiceException{
    	equipoDAO.aceptarRechazarPresupuesto(nroOrden, accion);
    }
    
    /**
     * 
     * Delegado obtener apertura de documento de OT
     */
    public DocumentoAperturaOTBean obtenerAperturaDocumentoOT(String nroOrden)throws DAOException, ServiceException{
    	return equipoDAO.obtenerAperturaDocumentoOT(nroOrden);
    }
    
    /**
     * 
     * Delegado obtener informe tecnico
     */
    public InformeTecnicoOTBean obtenerInformeTecnicoOT(String nroOrden)throws DAOException, ServiceException{
    	return equipoDAO.obtenerInformeTecnicoOT(nroOrden);
    }
    
    /**
     * 
     * Delegado verifica si usuario esta en grupo habil
     */
    public boolean estaEnGrupoHabil(String rut)throws DAOException, ServiceException {
    	return equipoDAO.estaEnGrupoHabil(rut);
    }
    
    public void solicitaLineaAdicional(String msisdn, String nombre, String formulario, String rut, String telefonoAdicional) throws DAOException, ServiceException {
        equipoDAO.solicitaLineaAdicional(msisdn, nombre, formulario, rut, telefonoAdicional);
    }
    
    /**
     * 
     * @return bean con los datos del equipo a nivel de compatibilidad
     */
    public EquipoFullBean getEquipoCompatibilidad(String numeroPcs)
            throws DAOException, ServiceException {
        return this.equipoDAO.getEquipoCompatibilidad(numeroPcs);
    }
    
    /**
     * 
     * @return bean con los datos del equipo actual y bloqueado si tiene
     */
    public ConsultarBloqueoDesbloqueoBean getConsultarBloqueoDesbloqueo(String msisdn , String mercado , String imeiDV) throws DAOException, ServiceException, ConsultaEquipoRobadoFaultMessage {
        return this.equipoDAO.getConsultarBloqueoDesbloqueo(msisdn, mercado, imeiDV);
    }
    

    /**
     * 
     * @return bean con los datos BUIC
     */
    public ConsultarDatosBuicBean getConsultarDatosBuic(String mercado , String  rutSinDV , String rutDV) throws DAOException, ServiceException {
        return this.equipoDAO.getConsultarDatosBuic(mercado , rutSinDV , rutDV);
    }
    
    /**
     * 
     * @return valida la clave para desbloqueo
     */
    public String validarClaveDesbloqueo(String mercado , String numeroPcs , String claveBloqueo) throws DAOException, ServiceException {
        return this.equipoDAO.validarClaveDesbloqueo(mercado , numeroPcs , claveBloqueo);
    }
    
    /**
     * 
     * @return consulta historico equipos
     */
    public List<TraficoEquiposBean> consultarHistoricoEquipos(String numeroPcs , String imei , String imsi ) throws DAOException, ServiceException {
        return this.equipoDAO.consultarHistoricoEquipos(numeroPcs , imei , imsi);
    }

    /**
     * 
     * @return consulta descripcion historico equipos
     */
    public String consultaDescripcionMovilTraficado(String imei , String dvImei ) throws DAOException, ServiceException {
        return this.equipoDAO.consultaDescripcionMovilTraficado(imei, dvImei);
    }

    /**
     * 
     * Delegado registrarBloqueoEquipo
     * 
     */
    public void ingresarBloqueoEquipo(BloqueoDesbloqueoEquipoBean bloq)throws DAOException, ServiceException {
    	equipoDAO.ingresarBloqueoEquipo(bloq);
    }
    
    /**
     * 
     * Delegado registrarDesbloqueoEquipo
     * 
     */
    public void ingresarDesbloqueoEquipo(BloqueoDesbloqueoEquipoBean desbloq)throws DAOException, ServiceException {
    	equipoDAO.ingresarDesbloqueoEquipo(desbloq);
    }
    
    /**
     * 
     * 
     * @param msisdn    
     * @return boolean
     * @throws DAOException
     * @throws ServiceException
     */
    
    public boolean tieneBloqueoPorFacturaImpaga(String msisdn) throws DAOException, ServiceException {
    	return this.equipoDAO.tieneBloqueoPorFacturaImpaga(msisdn);
    }
    
	/**
	 * 
	 * @param rut
	 * @param numeroCuenta
	 * @return
	 * @throws DAOException
	 * @throws ServiceException
	 */
    public GrupoClienteBean obtenerGrupoCliente(String rut, String numeroCuenta)
			throws DAOException, ServiceException {
		return this.equipoDAO.obtenerGrupoCliente(rut, numeroCuenta);
	}
    
    /**
     * 
     * @return bean con los datos del equipo a nivel de compatibilidad
     */
    public Equipo4GLteBean compatibilidad(String numeroPcs)
            throws DAOException, ServiceException {
        return equipoDAO.compatibilidad(numeroPcs);
    }
    
	/**
	 * 
	 * @param rut
	 * @param numeroCuenta
	 * @param grupoCliente
	 * @return
	 * @throws DAOException
	 * @throws ServiceException
	 */
    public ResumenLineaEquipoBean obtenerResumenLineaEquipos(String rut,
			String numeroCuenta, String grupoCliente) throws DAOException, ServiceException {
		return equipoDAO.obtenerResumenLineaEquipos(rut, numeroCuenta, grupoCliente);
	}
}
