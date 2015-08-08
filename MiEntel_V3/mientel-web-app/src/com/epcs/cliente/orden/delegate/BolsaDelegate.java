/* Propiedad de Entel PCS. Todos los derechos reservados */
package com.epcs.cliente.orden.delegate;

import java.util.List;

import com.epcs.bean.BolsaBean;
import com.epcs.bean.BolsaPPBean;
import com.epcs.bean.ResultadoContratarBolsaBean;
import com.epcs.bean.SimultaneidadBolsasBean;
import com.epcs.bean.ValidacionCompraBolsaRegaloBean;
import com.epcs.cliente.orden.dao.BolsaDAO;
import com.epcs.cliente.perfil.dao.BolsaBlindajeDAO;
import com.epcs.provision.suscripcion.bolsaspp.types.ListarBolsasActivasResponseType.Mensaje.Movil;
import com.epcs.recursoti.excepcion.DAOException;
import com.epcs.recursoti.excepcion.ServiceException;
import java.io.Serializable;

/**
 * @author jmanzur (I2B) en nombre de Absalon Opazo (Atencion al Cliente,
 *         EntelPcs)
 * 
 */
public class BolsaDelegate implements Serializable{
	
	private static final long serialVersionUID = 1L;

    private BolsaDAO bolsaDAO;
    private BolsaBlindajeDAO bolsaBlindajeDAO;

    /**
     * @param bolsaDAO
     *            the bolsaDAO to set
     */
    public void setBolsaDAO(BolsaDAO bolsaDAO) {
        this.bolsaDAO = bolsaDAO;
    }

   

    

    /**
     * Listado de Bolsas Regaladas
     * 
     * @param msisdn
     * @return
     * @throws DAOException
     * @throws ServiceException
     */
    public List<BolsaBean> consultarBolsasRegaladasSS(final String msisdn)
            throws DAOException, ServiceException {
        return bolsaDAO.consultarBolsasRegaladasSS(msisdn);
    }

   
    
    /**
     * Contratar una Bolsa para un usuario SS o CC 
     * @param msisdn
     * @param codBolsa
     * @param valorBolsa
     * @param opcionActivacion
     * @throws DAOException
     * @throws ServiceException
     */
    public ResultadoContratarBolsaBean contratarBolsaSSCC(final String msisdn,
            final String codBolsa, final double valorBolsa,
            final String opcionActivacion) throws DAOException,
            ServiceException {
        return bolsaDAO.contratarBolsaSSCC(msisdn, codBolsa, valorBolsa,
                opcionActivacion);
    }
    
    
    /**
     * Contrata una bolsa bby mercado ss o cc
     * @param msisdn
     * @param codBolsa
     * @throws DAOException
     * @throws ServiceException
     */
    public void contratarBolsaBBerrySSCC(final String msisdn,
            final String codBolsa) throws DAOException, ServiceException {
        bolsaDAO.contratarBolsaBBerrySSCC(msisdn, codBolsa);
    }
    
    public void marcaOfertaBlindajeContratada(long codOferta){
    	getBolsaBlindajeDAO().marcaOfertaBlindajeContratada(codOferta);    	
    }
    
    public void rechazaOfertaBlindaje(long codOferta){
    	getBolsaBlindajeDAO().rechazaOfertaBlindaje(codOferta);    	
    }
    
    public void marcarOtroCanalBlindaje(long codOferta){
    	getBolsaBlindajeDAO().marcarOtroCanalBlindaje(codOferta);
    }
    /**
     * Determina si el usuario ya tiene esa bolsa
     * @param msisdn
     * @param codBolsa
     * @return SimultaneidadBolsasBean
     * @throws DAOException
     * @throws ServiceException
     */
    public SimultaneidadBolsasBean validarSimultaneidadBolsaCC(final String msisdn,
            final String codBolsa) throws DAOException, ServiceException {
        return bolsaDAO.validarSimultaneidadBolsaCC(msisdn, codBolsa);
    }
    
    
    /**
     * Dtermina si se puede realizar la compra de la bolsa.
     * @param msisdnFrom
     * @param msisdnTo
     * @param tipoMercado
     * @param codBolsa
     * @param valorBolsa
     * @return ValidacionCompraBolsaRegaloBean
     * @throws DAOException
     * @throws ServiceException
     */
    public ValidacionCompraBolsaRegaloBean validarCompraBolsaBolsaRegaloSSCC(final String msisdnFrom,final String msisdnTo,final String tipoMercado,
            final String codBolsa, final double valorBolsa) throws DAOException, ServiceException {
       return bolsaDAO.validarCompraBolsaBolsaRegaloSSCC(msisdnFrom, msisdnTo, tipoMercado, codBolsa, valorBolsa);
    }
    
    /**
     * Regala una bolsa de un usuario ss.
     * @param msisdnFrom
     * @param msisdnTo
     * @param tipoMercado
     * @param codBolsa
     * @param valorBolsa
     * @throws DAOException
     * @throws ServiceException
     */
    public void comprarBolsaRegaloSSCC(final String msisdnFrom,final String msisdnTo,final String tipoMercado,
            final String codBolsa, final double valorBolsa) throws DAOException, ServiceException {
        bolsaDAO.comprarBolsaRegaloSSCC(msisdnFrom, msisdnTo, tipoMercado, codBolsa, valorBolsa);
    }
    
    /**
     * Obtiene bolsas compradas y regaladas
     * @param msisdn
     * @return List<BolsaBean>
     * @throws DAOException
     * @throws ServiceException
     */
    public List<BolsaPPBean> obtenerBolsasOneShot(final String msisdn)
            throws DAOException, ServiceException {
        return bolsaDAO.obtenerBolsasOneShot(msisdn);
    }
    
    /**
     * Comprar una bolsa para un usuario pp
     * 
     * @param msisdn
     * @param cartaServicio
     * @param valorBolsa
     * @throws DAOException
     * @throws ServiceException
     */
    public void comprarBolsasOneShotPP(final String msisdn, final String cartaServicio,
            final double valorBolsa) throws DAOException, ServiceException {
        bolsaDAO.comprarBolsasOneShotPP(msisdn, cartaServicio, valorBolsa);
    }
    
    /**
     * Comprar una bolsa para un usuario pp BAM
     * 
     * @param msisdn
     * @param cartaServicio
     * @param valorBolsa
     * @throws DAOException
     * @throws ServiceException
     */
    public void comprarBolsasPPBAM(final String msisdn, final String cartaServicio) throws DAOException, ServiceException {
        bolsaDAO.comprarBolsasPPBAM(msisdn, cartaServicio);          
    }





	public void setBolsaBlindajeDAO(BolsaBlindajeDAO bolsaBlindajeDAO) {
		this.bolsaBlindajeDAO = bolsaBlindajeDAO;
	}





	public BolsaBlindajeDAO getBolsaBlindajeDAO() {
		return bolsaBlindajeDAO;
	}
	
	
	 /**
     * Obtiene bolsas servicio scob
     * 
     * @param msisdn
     * @return List<BolsaBean>
     * @throws DAOException
     * @throws ServiceException
     */
    public List<BolsaPPBean> obtenerBolsasScob(String msisdn)
            throws DAOException, ServiceException {
        return bolsaDAO.obtenerBolsasScob(msisdn);
    }
    
	 /**
     * Obtiene bolsas servicio bam scob
     * 
     * @param msisdn
     * @return List<BolsaBean>
     * @throws DAOException
     * @throws ServiceException
     */
    public List<BolsaPPBean> obtenerBolsasActivasBAMScob(String msisdn)
            throws DAOException, ServiceException {
        return bolsaDAO.obtenerBolsasActivasBAMScob(msisdn);
    }
    
    /**
     * Comprar una bolsa para un usuario pp
     * 
     * @param msisdn
     * @param cartaServicio
     * @param valorBolsa
     * @throws DAOException
     * @throws ServiceException*/
    
    public void comprarBolsasScobPP(final String msisdn, final String cartaServicio
           ) 
            throws DAOException, ServiceException {
        bolsaDAO.comprarBolsasScobPP(msisdn, cartaServicio);
    }

    /**
     * Comprar una bolsa para un usuario bam pp
     * 
     * @param msisdn
     * @param cartaServicio
     * @param valorBolsa
     * @return 
     * @throws DAOException
     * @throws ServiceException*/
    
    public int comprarBolsasScobBAMPP(final String msisdn, final String cartaServicio
           ) 
            throws DAOException, ServiceException {
        return bolsaDAO.comprarBolsasScobBAMPP(msisdn, cartaServicio);
    }
    
    /**
     * Ejecuta teardown luego de comprar una bolsa con tiempo cuota para un usuario bam pp
     * 
     * @param msisdn
     * @throws DAOException
     * @throws ServiceException*/
    
    public void ejectuarTeardown(final String msisdn) 
            throws DAOException, ServiceException {
        bolsaDAO.ejectuarTeardown(msisdn);
    }





	public Movil obtenerMovil(String movilAsociado) throws DAOException, ServiceException {
		 return bolsaDAO.obtenerMovil(movilAsociado);
	}
}
