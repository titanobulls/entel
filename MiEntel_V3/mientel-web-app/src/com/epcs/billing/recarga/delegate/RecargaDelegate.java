/* Propiedad de Entel PCS. Todos los derechos reservados */
package com.epcs.billing.recarga.delegate;

import com.epcs.bean.FactibilidadRecargaEntelTicketBean;
import com.epcs.bean.FactibilidadRecargaMultitiendaBean;
import com.epcs.bean.FactibilidadRecargaWebPayBean;
import com.epcs.bean.ParametrosWebPay;
import com.epcs.bean.RecargaEntelTicketBean;
import com.epcs.bean.RecargaHistoricoBean;
import com.epcs.bean.RecargaMultitiendaBean;
import com.epcs.bean.RecargaWebPayBean;
import com.epcs.billing.recarga.dao.RecargaDAO;
import com.epcs.billing.recarga.dao.RecargaPromoPPDAO;
import com.epcs.erp.seguridad.dao.SeguridadDAO;
import com.epcs.recursoti.excepcion.DAOException;
import com.epcs.recursoti.excepcion.ServiceException;

/**
 * Delegado de negocio para Recargas
 * 
 * @author jlopez (I2B) en nombre de Absalon Opazo (Atencion al Cliente,
 *         EntelPcs)
 * 
 */
public class RecargaDelegate {

    private RecargaDAO recargaDAO;

    private SeguridadDAO seguridadDAO;

    private RecargaPromoPPDAO recargaPromoPPDAO;

    /**
     * Default constructor
     */
    public RecargaDelegate() {
        super();
        this.recargaDAO = new RecargaDAO();
        this.seguridadDAO = new SeguridadDAO();
        this.recargaPromoPPDAO = new RecargaPromoPPDAO();

    }

    /**
     * @return the recargaDAO
     */
    public RecargaDAO getRecargaDAO() {
        return recargaDAO;
    }

    /**
     * @param recargaDAO
     *            the recargaDAO to set
     */
    public void setRecargaDAO(RecargaDAO recargaDAO) {
        this.recargaDAO = recargaDAO;
    }

    /**
     * @return the seguridadDAO
     */
    public SeguridadDAO getSeguridadDAO() {
        return seguridadDAO;
    }

    /**
     * @param seguridadDAO
     *            the seguridadDAO to set
     */
    public void setSeguridadDAO(SeguridadDAO seguridadDAO) {
        this.seguridadDAO = seguridadDAO;
    }

    /**
     * Consulta por la recarga de entelticket asociada a
     * <code>ordenCompra</code>
     * 
     * @param ordenCompra
     *            orden de compra de la recarga
     * @return {@link RecargaEntelTicketBean}
     * @throws DAOException
     *             ante errores en la ejecucion del metodo DAO subyacente
     * @throws ServiceException
     *             Si no hay factibilidad de recarga, o el Servcio subyacente
     *             retorna un mensaje de error
     * @see com.epcs.billing.recarga.dao.RecargaDAO#consultarRecargaEntelTicket(java.lang.String)
     */
    public RecargaEntelTicketBean consultarRecargaEntelTicket(String ordenCompra)
            throws DAOException, ServiceException {
        return recargaDAO.consultarRecargaEntelTicket(ordenCompra);
    }

    /**
     * Consulta por la recarga de multitienda asociada a
     * <code>ordenCompra</code>
     * 
     * @param ordenCompra
     *            orden de compra de la recarga
     * @return {@link RecargaMultitiendaBean}
     * @throws DAOException
     * @throws ServiceException
     * @see com.epcs.billing.recarga.dao.RecargaDAO#consultarMultitienda(java.lang.String)
     */
    public RecargaMultitiendaBean consultarRecargaMultitienda(String ordenCompra)
            throws DAOException, ServiceException {
        return recargaDAO.consultarRecargaMultitienda(ordenCompra);
    }

    /**
     * Evalua la factibilidad de recarga entelticket
     * 
     * @param recarga
     * @return
     * @throws DAOException
     * @throws ServiceException
     * @see com.epcs.billing.recarga.dao.RecargaDAO#factibilidadRecargaEntelTicket(com.epcs.bean.RecargaEntelTicketBean)
     */
    public FactibilidadRecargaEntelTicketBean factibilidadRecargaEntelTicket(
            RecargaEntelTicketBean recarga) throws DAOException,
            ServiceException {
        return recargaDAO.factibilidadRecargaEntelTicket(recarga);
    }

    /**
     * Evalua la factibilidad de recarga de multitienda
     * 
     * @param recarga
     * @return
     * @throws DAOException
     * @throws ServiceException
     * @see com.epcs.billing.recarga.dao.RecargaDAO#factibilidadRecargaMultitienda(com.epcs.bean.RecargaMultitiendaBean)
     */
    public FactibilidadRecargaMultitiendaBean factibilidadRecargaMultitienda(
            RecargaMultitiendaBean recarga) throws DAOException,
            ServiceException {
        return recargaDAO.factibilidadRecargaMultitienda(recarga);
    }

    /**
     * Efectua la recarga entelticket
     * 
     * @param recarga
     * @throws DAOException
     * @throws ServiceException
     * @see com.epcs.billing.recarga.dao.RecargaDAO#realizarRecargaEntelTicket(com.epcs.bean.RecargaEntelTicketBean)
     */
    public FactibilidadRecargaEntelTicketBean realizarRecargaEntelTicket(FactibilidadRecargaEntelTicketBean recarga)
            throws DAOException, ServiceException {
        return recargaDAO.realizarRecargaEntelTicket(recarga);
    }

    /**
     * Efectua la recarga multitienda
     * 
     * @param recarga
     * @throws DAOException
     * @throws ServiceException
     * @see com.epcs.billing.recarga.dao.RecargaDAO#realizarRecargaMultitienda(com.epcs.bean.RecargaMultitiendaBean)
     */
    public FactibilidadRecargaMultitiendaBean realizarRecargaMultitienda(RecargaMultitiendaBean recarga)
            throws DAOException, ServiceException {
        return recargaDAO.realizarRecargaMultitienda(recarga);
    }

    

    /**
     * @param numeroPcs
     * @param monto
     * @param idp
     * @return
     * @throws DAOException
     * @throws ServiceException
     * @see com.epcs.billing.recarga.dao.RecargaDAO#ingresarRecargaWebPay(java.lang.String,
     *      java.lang.Double, java.lang.String)
     */
    public RecargaWebPayBean ingresarRecargaWebPay(String numeroPcs,
            Double monto, String codPromo) throws DAOException, ServiceException {
        
        String idp = seguridadDAO.consultarIDPAmpliacion(numeroPcs);
        return recargaDAO.ingresarRecargaWebPay(numeroPcs, monto, idp + codPromo);
    }
    
    /**
     * Evalua la factibilidad de recarga Webpay
     * 
     * @param recarga
     * @return
     * @throws DAOException
     * @throws ServiceException
     * @see com.epcs.billing.recarga.dao.RecargaDAO#factibilidadRecargaWebPay(RecargaWebPayBean)
     */
    public FactibilidadRecargaWebPayBean factibilidadRecargaWebPay(
            RecargaWebPayBean recarga, boolean eligeTuPromo, String codPromoF) throws DAOException, ServiceException {
        return recargaDAO.factibilidadRecargaWebPay(recarga, eligeTuPromo, codPromoF);
    }

    
    
    /**
     * @param ordenCompra
     * @return
     * @throws DAOException
     * @throws ServiceException
     * @see com.epcs.billing.recarga.dao.RecargaDAO#consultarRecargaWebPayBean(java.lang.String)
     */
    public RecargaWebPayBean consultarRecargaWebPayBean(
            String ordenCompra) throws DAOException, ServiceException {
        return recargaDAO.consultarRecargaWebPayBean(ordenCompra);
    }

    /**
     * Actualiza la recarga con la respuesta de Transbank
     * 
     * @param ordenCompra
     * @param parametrosWebPay
     * @throws DAOException
     * @throws ServiceException
     * @see RecargaDAO#actualizarRecargaWebPay(String,
     *      ParametrosWebPay)
     */
    public void actualizarRecargaWebPay(String ordenCompra,
            String parametrosWebPay) throws DAOException,
            ServiceException {
        recargaDAO.actualizarRecargaWebPay(ordenCompra, parametrosWebPay);
    }

    /**
     * Hace efectiva la recarga webpay
     * 
     * @param recarga
     *            recarga a hacer efectiva
     * @return {@link RecargaWebPayBean} con recarga efectuada
     * @throws DAOException
     * @throws ServiceException
     * @see com.epcs.billing.recarga.dao.RecargaDAO#efectuarRecargaWebPay(RecargaWebPayBean)
     */
    public RecargaWebPayBean efectuarRecargaWebPay(RecargaWebPayBean recarga, boolean eligeTuPromo)
            throws DAOException, ServiceException {
        return recargaDAO.efectuarRecargaWebPay(recarga, eligeTuPromo);
    }

    /**
     * 
     * @param msisdn
     * @return
     * @throws DAOException
     * @throws ServiceException
     */    
    public RecargaHistoricoBean getHistoricoRecargas(String msisdn)
    throws DAOException, ServiceException {
        return this.recargaDAO.getHistoricoRecargas(msisdn);
    }
    
    /**
     * 
     * @param msisdn
     * @param fechaInicial
     * @param fechaFinal
     * @return
     * @throws DAOException
     * @throws ServiceException
     */    
    public RecargaHistoricoBean getHistoricoRecargasRango(String msisdn,String fechaInicial,String fechaFinal)
    throws DAOException, ServiceException {
        return this.recargaDAO.getHistoricoRecargasRango(msisdn,fechaInicial,fechaFinal);
    }

    /**
     * 
     * @param msisdn
     */
    public void cancelarDesplieguePromo(String msisdn) {
    	recargaPromoPPDAO.cancelarDesplieguePromo(msisdn);
    }
    
    public boolean validarDesplieguePromo(String msisdn, String fechaInicioPromo, String fechaFinPromo) {
    	return recargaPromoPPDAO.validarDesplieguePromo(msisdn, fechaInicioPromo, fechaFinPromo);    	
    }    
    
}
