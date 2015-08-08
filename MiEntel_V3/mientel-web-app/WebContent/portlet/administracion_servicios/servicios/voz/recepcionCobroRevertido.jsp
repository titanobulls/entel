<%@ taglib prefix="f"  uri="http://java.sun.com/jsf/core"%>
<%@ taglib prefix="h"  uri="http://java.sun.com/jsf/html"%>
<%@ taglib prefix="entel" uri="/WEB-INF/tld/entel-tags.tld" %>

<% 		
	String resaltador = "";
	if(request.getParameter("rowNumber") != null){
		resaltador = (Integer.parseInt(request.getParameter("rowNumber"))) % 2 == 0? "resaltador":"";
	}
%>

<entel:view name="recepcionCobroRevertido" inverse="true"> 
<!-- Fila de información -->     
<div class="tabla_fila clearfix <%=resaltador%>">

	<div class="columna1">
		<strong class="clearfix">Recepci&oacute;n Cobro Revertido</strong>
	</div>
	<div class="columna2">
		<a class="ico_interrogacionNuevo autoTooltip" href="#TTCobroRevertido"></a>
	</div>

    <div class="columna3">
    	<!-- Mensaje grande -->
        <h:panelGroup layout="block" styleClass="mensaje_grande #{administracionServicios.administracionServiciosBean.servicioRecepcionCobroRevertidoBean.activo ? 'mensaje_grande_habilitado' :'mensaje_grande_deshabilitado'}">
        	<h:outputText value="#{administracionServicios.administracionServiciosBean.servicioRecepcionCobroRevertidoBean.activo ? 'Habilitado' :'Deshabilitado'}"/>
        	<br />Solo el titular o administrador de la cuenta, puede cambiar el estado.
        	<br />
        	<h:form>
				<jsp:include page="/token.jsp" flush="true"/>
        		<h:commandLink action="mis_usuarios" styleClass="enlace">Ir a tabla de atributos</h:commandLink>
        	</h:form>
        </h:panelGroup>

    </div>
</div>
<!-- /Fila de información -->
</entel:view>

<entel:view name="recepcionCobroRevertido">
<h:panelGroup rendered="#{administracionServicios.administracionServiciosBean.servicioRecepcionCobroRevertidoBean.visible}">
<h:form id="recepcionCobroRevertidoForm">
	<jsp:include page="/token.jsp" flush="true"/>
<!-- Fila de informacion -->
<div class="tabla_fila clearfix <%=resaltador%>">
    <h:panelGroup rendered="#{administracionServicios.administracionServiciosBean.servicioRecepcionCobroRevertidoBean.activo}">
	<div class="estadoHabilitado clearfix">
		<div class="columna1">
			<strong class="clearfix">Recepci&oacute;n Cobro Revertido</strong>
		</div>
		<div class="columna2">
			<a class="ico_interrogacionNuevo autoTooltip" href="#TTCobroRevertido"></a>
		</div>
		<div class="columna3">
			<!-- Mensaje habilitado/deshabilitado -->
			<div class="mensaje habilitado">Habilitado</div>
			
			<!-- Btoon deshabilitado/habilitado -->
			<div class="btnDeshabilitar">
				<a class="btnAzulDelgado btnAncho2 caso1" href="#" onclick="mxTracker._trackPageview('Personas/Mi Entel/Mis Productos/Telefonia/Adm. de Servicios/Voz/Recepcion cobro revertido/Deshabilitar');"><span>Deshabilitar</span></a>
			</div>
			
			<!-- Boton Configurar -->
			<div class="enlaceConfigurar">
				<a href="#" class="enlaceConfigurar ocultar">Configurar</a>
			</div>
		</div>
	</div>
	</h:panelGroup>
	
	<!-- Paso Confirmar Habilitar -->
  <div class="confirmarHabilitar clearfix ocultar">	
		<div class="columna1">
			<strong class="clearfix">Recepci&oacute;n Cobro Revertido</strong>
		</div>
		<div class="columna2">
			<a class="ico_interrogacionNuevo autoTooltip" href="#TTCobroRevertido"></a>
		</div>
		<div class="columna3">
			<!-- Mensaje habilitado/deshabilitado -->
			<div class="mensaje_ancho"><strong>Habilitar&aacute;s Recepci&oacute;n Cobro Revertido</strong>
			</div>
			
			<!-- Btoon deshabilitado/habilitado -->
		<!-- Btoon deshabilitado/habilitado -->
			<div class="btnHabilitar">
			    <h:commandLink  styleClass="btnAzulDelgado btnAncho caso3" action="#{cobroRevertidoController.activarRecepcionCobroRevertido}" onclick="mxTracker._trackPageview('Personas/Mi Entel/Mis Productos/Telefonia/Adm. de Servicios/Voz/Recepcion cobro revertido/Habilitado');"><span>Habilitar</span></h:commandLink>
			</div>
			
			<!-- Boton Cancelar -->
			<div class="enlaceCancelar">
				<a href="#" class="enlaceCancelarH">Cancelar</a>
			</div>
		</div>
	</div>
	<!-- Fin Paso Confirmar Habilitar -->
	
	<div class="estadoHabilitado1 clearfix ocultar">
		<div class="columna1">
			<strong class="clearfix">Recepci&oacute;n Cobro Revertido</strong>
		</div>
		<div class="columna2">
			<a class="ico_interrogacionNuevo autoTooltip" href="#TTCobroRevertido"></a>
		</div>
		<div class="columna3">
			<!-- Mensaje grande -->
			<div class="mensaje_grande mensaje_grande_reloj">
				En proceso de <strong>habilitaci&oacute;n</strong>
			</div>
		</div>
	</div>	
		
	<h:panelGroup rendered="#{!administracionServicios.administracionServiciosBean.servicioRecepcionCobroRevertidoBean.activo}">
	<div class="estadoDeshabilitado clearfix">
		<div class="columna1">
			<strong class="clearfix">Recepci&oacute;n Cobro Revertido</strong>
		</div>
		<div class="columna2">
			<a class="ico_interrogacionNuevo autoTooltip" href="#TTCobroRevertido"></a>
		</div>
		<div class="columna3">
			<!-- Mensaje habilitado/deshabilitado -->
			<div class="mensaje deshabilitado">Deshabilitado</div>
			
			<!-- Btoon deshabilitado/habilitado -->
			<div class="btnHabilitar">
			    <a class="btnAzulDelgado btnAncho caso1H" href="#" onclick="mxTracker._trackPageview('Personas/Mi Entel/Mis Productos/Telefonia/Adm. de Servicios/Voz/Recepcion cobro revertido/Habilitar');"><span>Habilitar</span></a>			    
			</div>			
			<!-- Boton Configurar -->
			<div class="enlaceConfigurar">
				<a href="#" class="enlaceConfigurar ocultar">Configurar</a>
			</div>
		</div>
	</div>
	</h:panelGroup>
	
	<div class="estadoDeshabilitado1 clearfix ocultar">

		<div class="columna1">
			<strong class="clearfix">Recepci&oacute;n Cobro Revertido</strong>
		</div>
		<div class="columna2">
			<a class="ico_interrogacionNuevo autoTooltip" href="#TTCobroRevertido"></a>
		</div>
		<div class="columna3">
			<!-- Mensaje habilitado/deshabilitado -->
			<div class="mensaje_ancho"><strong>Deshabilitar&aacute;s Recepci&oacute;n Cobro Revertido</strong><br />Los n&uacute;meros configurados ser&aacute;n eliminados.
			</div>
			
			<!-- Btoon deshabilitado/habilitado -->
			<div class="btnDeshabilitar">
			    <h:commandLink action="#{cobroRevertidoController.desactivarRecepcionCobroRevertido}" styleClass="btnAzulDelgado btnAncho2 caso4" onclick="mxTracker._trackPageview('Personas/Mi Entel/Mis Productos/Telefonia/Adm. de Servicios/Voz/Recepcion cobro revertido/Deshabilitado');"><span>Deshabilitar</span></h:commandLink>
			</div>
			
			<!-- Boton Cancelar -->
			<div class="enlaceCancelar">
				<a href="#" class="enlaceCancelar">Cancelar</a>
			</div>
		</div>
	</div>
	
	<div class="estadoDeshabilitado2 clearfix ocultar">
		<div class="columna1">
			<strong class="clearfix">Recepci&oacute;n Cobro Revertido</strong>
		</div>
		<div class="columna2">
			<a class="ico_interrogacionNuevo autoTooltip" href="#TTCobroRevertido"></a>
		</div>
		<div class="columna3">
			<!-- Mensaje grande -->
			<div class="mensaje_grande mensaje_grande_reloj">
				En proceso de <strong>deshabilitaci&oacute;n</strong>
			</div>
		</div>
	</div>
	
</div>
<!-- /Fila de informacion -->
</h:form>                
</h:panelGroup>
<h:panelGroup rendered="#{!administracionServicios.administracionServiciosBean.servicioRecepcionCobroRevertidoBean.visible}"> 
	<jsp:include page="../servicioNoDisponible.jsp" flush="true">
    	<jsp:param value="Recepci&oacute;n Cobro Revertido" name="serviceName"/>
        <jsp:param value="" name="cargo"/>
        <jsp:param value="TTCobroRevertido" name="tooltip"/>
     </jsp:include>
</h:panelGroup>	   
</entel:view>