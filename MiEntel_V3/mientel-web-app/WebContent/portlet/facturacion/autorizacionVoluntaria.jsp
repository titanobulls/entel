<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib prefix="f"  uri="http://java.sun.com/jsf/core"%>
<%@ taglib prefix="h"  uri="http://java.sun.com/jsf/html"%>
<%@ taglib prefix="c"  uri="http://java.sun.com/jsp/jstl/core"%>
    
<f:view>
    
	<h:outputText value="#{facturacionElectronicaController.autorizacionVoluntariaContent}" escape="false"/>
	
	<br/>
	<div align="center">
	<h:form>
		<jsp:include page="/token.jsp" flush="true"/>
	<h:commandLink action="#{facturacionElectronicaController.cancelarServicioFacturacionElectronica}" styleClass="btnAzul btnAzulLargo"><span>Cancelar</span></h:commandLink>
	<h:commandLink action="#{facturacionElectronicaController.inscribirServicioFacturacionElectronica}" styleClass="btnAzul btnAzulLargo"><span>Aceptar</span></h:commandLink>
	</h:form>
	</div>
	
    <!-- /CONTENIDO CENTRAL -->
    
</f:view>