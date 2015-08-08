package com.epcs.bean;

public class ZonaPerfilBean {	   
     
    private String numeroPcs;
     
    private String rut;       
     
    private String statusRespuesta;
        

	/**
	 * @return the numeroPcs
	 */
	public String getNumeroPcs() {
		return numeroPcs;
	}

	/**
	 * @param numeroPcs the numeroPcs to set
	 */
	public void setNumeroPcs(String numeroPcs) {
		this.numeroPcs = numeroPcs;
	}

	/**
	 * @param rut the rut to set
	 */
	public void setRut(String rut) {
		this.rut = rut;
	}

	/**
	 * @return the rut
	 */
	public String getRut() {
		return rut;
	}

	
	/**
	 * @param statusRespuesta the statusRespuesta to set
	 */
	public void setStatusRespuesta(String statusRespuesta) {
		this.statusRespuesta = statusRespuesta;
	}

	/**
	 * @return the statusRespuesta
	 */
	public String getStatusRespuesta() {
		if(null != statusRespuesta){
		return statusRespuesta;
		}
		return "";
	}
	
}
