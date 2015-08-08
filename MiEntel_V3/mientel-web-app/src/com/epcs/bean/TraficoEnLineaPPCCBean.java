/**
 * 
 */
package com.epcs.bean;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import org.apache.log4j.Logger;
/**
 * @author Juan Carlos en nombre de Absalon Opazo (Atencion al Cliente,
 *         EntelPcs)
 *
 */
public class TraficoEnLineaPPCCBean implements Serializable,Comparable<TraficoEnLineaPPCCBean> {

	private static final Logger LOGGER = Logger.getLogger(TraficoEnLineaPPCCBean.class);
	
	private static final long serialVersionUID = 1L;
	
	private String costo;
	
	private String destino;
	
	private String duracion;
	
	private String fechaLlamada;
	
	private String horaLlamada;
	
	private String paginas;
	
	private String saldo;
	
	private String tipoServicio;
	
	private String unidad;
	
	
	
    /**
	 * @return the costo
	 */
	public String getCosto() {
		return costo;
	}



	/**
	 * @param costo the costo to set
	 */
	public void setCosto(String costo) {
		this.costo = costo;
	}



	/**
	 * @return the destino
	 */
	public String getDestino() {
		return destino;
	}



	/**
	 * @param destino the destino to set
	 */
	public void setDestino(String destino) {
		this.destino = destino;
	}



	/**
	 * @return the duracion
	 */
	public String getDuracion() {
		return duracion;
	}



	/**
	 * @param duracion the duracion to set
	 */
	public void setDuracion(String duracion) {
		this.duracion = duracion;
	}



	/**
	 * @return the fechaLlamada
	 */
	public String getFechaLlamada() {
		return fechaLlamada;
	}



	/**
	 * @param fechaLlamada the fechaLlamada to set
	 */
	public void setFechaLlamada(String fechaLlamada) {
		this.fechaLlamada = fechaLlamada;
	}



	/**
	 * @return the horaLlamada
	 */
	public String getHoraLlamada() {
		return horaLlamada;
	}



	/**
	 * @param horaLlamada the horaLlamada to set
	 */
	public void setHoraLlamada(String horaLlamada) {
		this.horaLlamada = horaLlamada;
	}



	/**
	 * @return the paginas
	 */
	public String getPaginas() {
		return paginas;
	}



	/**
	 * @param paginas the paginas to set
	 */
	public void setPaginas(String paginas) {
		this.paginas = paginas;
	}



	/**
	 * @return the saldo
	 */
	public String getSaldo() {
		return saldo;
	}



	/**
	 * @param saldo the saldo to set
	 */
	public void setSaldo(String saldo) {
		this.saldo = saldo;
	}



	/**
	 * @return the tipoServicio
	 */
	public String getTipoServicio() {
		return tipoServicio;
	}



	/**
	 * @param tipoServicio the tipoServicio to set
	 */
	public void setTipoServicio(String tipoServicio) {
		this.tipoServicio = tipoServicio;
	}



	/**
	 * @return the unidad
	 */
	public String getUnidad() {
		return unidad;
	}



	/**
	 * @param unidad the unidad to set
	 */
	public void setUnidad(String unidad) {
		this.unidad = unidad;
	}


	String DATE_FORMAT = "dd/MM/yyyy";
	SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
	SimpleDateFormat dateFormat = new SimpleDateFormat ("hh:mm:ss");
	
    @Override
    public int compareTo(TraficoEnLineaPPCCBean traficoEnLineaPPCCBean) {
    	
    	int resul = 0;
    	
		try {
			
			if( (sdf.parse(this.getFechaLlamada()).before( sdf.parse(traficoEnLineaPPCCBean.getFechaLlamada()) )) ||
					( dateFormat.parse(this.getHoraLlamada()).before( dateFormat.parse(traficoEnLineaPPCCBean.getHoraLlamada()))) ){
			    resul = -1;
			}else if( (sdf.parse(this.getFechaLlamada()).after(sdf.parse(traficoEnLineaPPCCBean.getFechaLlamada())) ) ||
					(dateFormat.parse(this.getHoraLlamada()).after(dateFormat.parse(traficoEnLineaPPCCBean.getHoraLlamada()))) ){
				resul = 0;
			}else{
				resul = 1;
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			LOGGER.error(e);
		}
		return resul;
           
        //return traficoEnLineaPPCCBean.getFechaLlamada().compareTo(this.getFechaLlamada());
    }
}
