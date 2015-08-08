/**
 * 
 */
package com.epcs.bean;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.text.ParseException;

import org.apache.log4j.Logger;

/**
 * @author jroman (I2B) en nombre de Absalon Opazo (Atencion al Cliente,
 *         EntelPcs)
 * 
 */
public class DatosEquipoRobadoBean implements Serializable {
	private static final Logger LOGGER = Logger.getLogger(DatosEquipoRobadoBean.class);
	private static final long serialVersionUID = 6078851483477219858L;
	
	private String codOperacion;
	
	private String codCompEnvio;
	
	private String codImei;
	
	private String fechaPerdida;
	
	private String fechaIoper;
	
	private String fechaDenuncia;
	
	private String descMarcaEquipo;
	
	private String descModeloEquipo;
	
	private String descTecnologia;
	
	private String indcModalidad;
	
	private String nmroTelefono;
	
	private String razonBloqueo;
	
	private String errorCode;
	
	private String descError;
	
	private String fechaEjecucion;
	
	private String codImsi;
	
	private String codTerminal;
	
	private String fechaPerdidaFormated;
	
	
	

	/**
	 * @return the fechPerdidaFormated
	 */
	public String getFechaPerdidaFormated() {
		
		//20120704000000
		
		//yyyyMMddhhmmss
		String fechaSinHora = this.fechaPerdida.substring(0, 8); //20120704
		String horaFecha = this.fechaPerdida.substring(8, 14); //000000
		
		SimpleDateFormat formatoSalidaSinHora = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat formatoSalidaConHora = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			
			if( "000000".equals(horaFecha) ){
				SimpleDateFormat fecha = new SimpleDateFormat("yyyyMMdd");
				Date date;
				
					date = fecha.parse(fechaSinHora);
				
				fechaPerdidaFormated = formatoSalidaSinHora.format(date);
				
			}else{
				SimpleDateFormat fecha = new SimpleDateFormat("yyyyMMddhhmmss");
				Date date = fecha.parse(this.fechaPerdida);
				fechaPerdidaFormated = formatoSalidaConHora.format(date);
			}
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			LOGGER.error(e);
		}
		return fechaPerdidaFormated;
	}

	/**
	 * @param fechPerdidaFormated the fechPerdidaFormated to set
	 */
	public void setFechaPerdidaFormated(String fechaPerdidaFormated) {
		this.fechaPerdidaFormated = fechaPerdidaFormated;
	}

	/**
	 * @return the codOperacion
	 */
	public String getCodOperacion() {
		return codOperacion;
	}

	/**
	 * @param codOperacion the codOperacion to set
	 */
	public void setCodOperacion(String codOperacion) {
		this.codOperacion = codOperacion;
	}

	/**
	 * @return the codCompEnvio
	 */
	public String getCodCompEnvio() {
		return codCompEnvio;
	}

	/**
	 * @param codCompEnvio the codCompEnvio to set
	 */
	public void setCodCompEnvio(String codCompEnvio) {
		this.codCompEnvio = codCompEnvio;
	}

	/**
	 * @return the codImei
	 */
	public String getCodImei() {
		return codImei;
	}

	/**
	 * @param codImei the codImei to set
	 */
	public void setCodImei(String codImei) {
		this.codImei = codImei;
	}

	/**
	 * @return the fechaPerdida
	 */
	public String getFechaPerdida() {
		return fechaPerdida;
	}

	/**
	 * @param fechaPerdida the fechaPerdida to set
	 */
	public void setFechaPerdida(String fechaPerdida) {
		this.fechaPerdida = fechaPerdida;
	}

	/**
	 * @return the fechaIoper
	 */
	public String getFechaIoper() {
		return fechaIoper;
	}

	/**
	 * @param fechaIoper the fechaIoper to set
	 */
	public void setFechaIoper(String fechaIoper) {
		this.fechaIoper = fechaIoper;
	}

	/**
	 * @return the fechaDenuncia
	 */
	public String getFechaDenuncia() {
		return fechaDenuncia;
	}

	/**
	 * @param fechaDenuncia the fechaDenuncia to set
	 */
	public void setFechaDenuncia(String fechaDenuncia) {
		this.fechaDenuncia = fechaDenuncia;
	}

	/**
	 * @return the descMarcaEquipo
	 */
	public String getDescMarcaEquipo() {
		return descMarcaEquipo;
	}

	/**
	 * @param descMarcaEquipo the descMarcaEquipo to set
	 */
	public void setDescMarcaEquipo(String descMarcaEquipo) {
		this.descMarcaEquipo = descMarcaEquipo;
	}

	/**
	 * @return the descModeloEquipo
	 */
	public String getDescModeloEquipo() {
		return descModeloEquipo;
	}

	/**
	 * @param descModeloEquipo the descModeloEquipo to set
	 */
	public void setDescModeloEquipo(String descModeloEquipo) {
		this.descModeloEquipo = descModeloEquipo;
	}

	/**
	 * @return the descTecnologia
	 */
	public String getDescTecnologia() {
		return descTecnologia;
	}

	/**
	 * @param descTecnologia the descTecnologia to set
	 */
	public void setDescTecnologia(String descTecnologia) {
		this.descTecnologia = descTecnologia;
	}

	/**
	 * @return the indcModalidad
	 */
	public String getIndcModalidad() {
		return indcModalidad;
	}

	/**
	 * @param indcModalidad the indcModalidad to set
	 */
	public void setIndcModalidad(String indcModalidad) {
		this.indcModalidad = indcModalidad;
	}

	/**
	 * @return the nmroTelefono
	 */
	public String getNmroTelefono() {
		return nmroTelefono;
	}

	/**
	 * @param nmroTelefono the nmroTelefono to set
	 */
	public void setNmroTelefono(String nmroTelefono) {
		this.nmroTelefono = nmroTelefono;
	}

	/**
	 * @return the razonBloqueo
	 */
	public String getRazonBloqueo() {
		return razonBloqueo;
	}

	/**
	 * @param razonBloqueo the razonBloqueo to set
	 */
	public void setRazonBloqueo(String razonBloqueo) {
		this.razonBloqueo = razonBloqueo;
	}

	/**
	 * @return the errorCode
	 */
	public String getErrorCode() {
		return errorCode;
	}

	/**
	 * @param errorCode the errorCode to set
	 */
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	/**
	 * @return the descError
	 */
	public String getDescError() {
		return descError;
	}

	/**
	 * @param descError the descError to set
	 */
	public void setDescError(String descError) {
		this.descError = descError;
	}

	/**
	 * @return the fechaEjecucion
	 */
	public String getFechaEjecucion() {
		return fechaEjecucion;
	}

	/**
	 * @param fechaEjecucion the fechaEjecucion to set
	 */
	public void setFechaEjecucion(String fechaEjecucion) {
		this.fechaEjecucion = fechaEjecucion;
	}

	/**
	 * @return the codImsi
	 */
	public String getCodImsi() {
		return codImsi;
	}

	/**
	 * @param codImsi the codImsi to set
	 */
	public void setCodImsi(String codImsi) {
		this.codImsi = codImsi;
	}

	/**
	 * @return the codTerminal
	 */
	public String getCodTerminal() {
		return codTerminal;
	}

	/**
	 * @param codTerminal the codTerminal to set
	 */
	public void setCodTerminal(String codTerminal) {
		this.codTerminal = codTerminal;
	}
	
	

}
