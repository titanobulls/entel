package com.epcs.bam.migracionpp.controller;

import javax.faces.event.PhaseEvent;
import org.apache.log4j.Logger;
import com.bea.p13n.usermgmt.profile.ProfileWrapper;
import com.epcs.bam.migracionpp.delegate.MisBamDelegate;
import com.epcs.recursoti.configuracion.jsf.utils.JSFPortletHelper;
import com.epcs.recursoti.configuracion.uup.ProfileWrapperHelper;

public class MisbamController {
	private static final Logger LOGGER = Logger.getLogger(MisbamController.class);
	private MisBamDelegate misBamDelegate;
	String misBAM = "";
	Integer misBAMSolicitud = 0;
	
	public void init(PhaseEvent event) {       
		if (JSFPortletHelper.getRequest().getSession().getAttribute("misBAM") == null || JSFPortletHelper.getRequest().getSession().getAttribute("misBAMSolicitud") == null){			
			loadData();  	
			LOGGER.info("MISBAM desde WS: " + misBAM);
			LOGGER.info("Solicitud MISBAM desde WS: " + misBAMSolicitud);
		}else{
			misBAM = (String)JSFPortletHelper.getRequest().getSession().getAttribute("misBAM");
			misBAMSolicitud = (Integer)JSFPortletHelper.getRequest().getSession().getAttribute("misBAMSolicitud");
			LOGGER.info("MISBAM desde session: " + misBAM);
			LOGGER.info("Solicitud MISBAM desde session: " + misBAMSolicitud);
		}
    }
	
	private void loadData(){ 
		ProfileWrapper profileWrapper = ProfileWrapperHelper.getProfile(JSFPortletHelper.getRequest());
		String rut = "";
		String movil = "";
		this.misBamDelegate = new MisBamDelegate();
				
		try {
			movil = ProfileWrapperHelper.getPropertyAsString(profileWrapper,"numeroPcsSeleccionado");
			rut = ProfileWrapperHelper.getPropertyAsString(profileWrapper, "rutUsuarioSeleccionado");		
			misBAM = this.misBamDelegate.getMisBAM(rut, movil);
			misBAMSolicitud = this.misBamDelegate.getMisBAMSolicitud(movil);
		} catch (Exception e) {
			LOGGER.error(e);
		}
		
		
	}

	public String getMisBAM() {
		return misBAM;
	}

	public void setMisBAM(String misBAM) {
		this.misBAM = misBAM;
	}

	public MisBamDelegate getMisBamDelegate() {
		return misBamDelegate;
	}

	public void setMisBamDelegate(MisBamDelegate misBamDelegate) {
		this.misBamDelegate = misBamDelegate;
	}

	public Integer getMisBAMSolicitud() {
		return misBAMSolicitud;
	}

	public void setMisBAMSolicitud(Integer misBAMSolicitud) {
		this.misBAMSolicitud = misBAMSolicitud;
	}

	
	
}
