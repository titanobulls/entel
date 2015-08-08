<%@ taglib prefix="f"  uri="http://java.sun.com/jsf/core"%>
<%@ taglib prefix="h"  uri="http://java.sun.com/jsf/html"%>
<%@ taglib prefix="entel" uri="/WEB-INF/tld/entel-tags.tld" %>
<%@ taglib prefix="cm" uri="http://www.bea.com/servers/portal/tags/content" %>

<% 
	String resaltador = "";
	if(request.getParameter("rowNumber") != null){
		resaltador = (Integer.parseInt(request.getParameter("rowNumber"))) % 2 == 0? "resaltador":"";
	}
%>

<cm:search id="mensajeDeshabilitar" query="idContenido = 'mensajeDeshabilitar'" useCache="true"  />

<entel:view name="internetMovil" inverse="true"> 
<!-- Fila de informaci�n -->     
<div class="tabla_fila clearfix <%=resaltador%>">

	<div class="columna1">
       	<strong class="clearfix">Internet M&oacute;vil</strong>
       	<label class="titulo_inferior">$250 por sesi&oacute;n</label>
    </div>
         
    <div class="columna2">
       	<a class="ico_interrogacionNuevo autoTooltip" href="#TooltipInetMovil"></a>
   	</div>

    <div class="columna3">
    	<!-- Mensaje grande -->
        <h:panelGroup layout="block" styleClass="mensaje_grande #{administracionServicios.administracionServiciosBean.mmsIshopImovilBean.imovil.activo ? 'mensaje_grande_habilitado' :'mensaje_grande_deshabilitado'}">
        	<h:outputText value="#{administracionServicios.administracionServiciosBean.mmsIshopImovilBean.imovil.activo ? 'Habilitado' :'Deshabilitado'}"/>
        	<br />Solo el titular o administrador de la cuenta, puede cambiar el estado.
        	<br />
        	<h:form>
				<jsp:include page="/token.jsp" flush="true"/>
        		<h:commandLink action="mis_usuarios" styleClass="enlace">Ir a tabla de atributos</h:commandLink>
        	</h:form>
        </h:panelGroup>

    </div>
</div>
<!-- /Fila de informaci�n -->
</entel:view>


<entel:view name="internetMovil">
<!-- Fila de informaci�n -->
<h:panelGroup rendered="#{administracionServicios.administracionServiciosBean.mmsIshopImovilBean.imovil.visible}">

<div class="tabla_fila clearfix <%=resaltador%>">
    <h:form>
		<jsp:include page="/token.jsp" flush="true"/>
		<div class="estadoHabilitado clearfix <h:outputText value="#{administracionServicios.administracionServiciosBean.mmsIshopImovilBean.imovil.activo ? '' :'ocultar'}"/>">
	    
	    	<div class="columna1">
	           	<strong class="clearfix">Internet M&oacute;vil</strong>
	           	<label class="titulo_inferior">$250 por sesi&oacute;n</label>
	        </div>
	             
	        <div class="columna2">
	           	<a class="ico_interrogacionNuevo autoTooltip" href="#TooltipInetMovil"></a>
	       	</div>
	         
	        <div class="columna3">
	        	<!-- Mensaje habilitado/deshabilitado -->
	            <div class="mensaje habilitado">Habilitado</div>
	
				<cm:getProperty node="${mensajeDeshabilitar[0]}" name="html" />
	           	<!-- <label class="titulo_inferior">Para desactivar este servicio deber&aacute;s llamar desde tu m&oacute;vil al 103</label>  -->
	                       
	        </div>
	     </div>
	     
	     <div class="estadoHabilitado1 clearfix ocultar">
	      	<div class="columna1">
	          	<strong class="clearfix">Internet M&oacute;vil</strong>
	            <label class="titulo_inferior">$250 por sesi&oacute;n</label>
			</div>
	             
	        <div class="columna2">
		       	<a class="ico_interrogacionNuevo autoTooltip" href="#TooltipInetMovil"></a>
			</div>
	             
			<div class="columna3">
	             
	           	<!-- Mensaje grande -->
	            <div class="mensaje_grande mensaje_grande_reloj">
	                 	En proceso de <strong>habilitaci&oacute;n</strong>
				</div>
	     	</div>
		</div>
		
		<!-- Paso Confirmar Habilitar -->
		<div class="confirmarHabilitar clearfix ocultar">
	       	<div class="columna1">
	           	<strong class="clearfix">Internet M&oacute;vil</strong>
	            <label class="titulo_inferior">$250 por sesi&oacute;n</label>
	        </div>
	        <div class="columna2">
	          	<a class="ico_interrogacionNuevo autoTooltip" href="#TooltipInetMovil"></a>
	        </div>
	        <div class="columna3">
				<!-- Mensaje habilitado/deshabilitado -->
	           	<div class="mensaje_ancho"><strong>Habilitar&aacute;s Internet M&oacute;vil</strong><br />
	            </div>
	         	
	         	<!-- Bto�n deshabilitado/habilitado -->
	           	<div class="btnHabilitar">
	           		<h:commandLink  styleClass="btnAzulDelgado btnAncho caso3" action="#{servicioInetMovilController.activarInetMovil}" onclick="mxTracker._trackPageview('Personas/Mi Entel/Mis Productos/Telefonia/Adm. de Servicios/Datos/Internet movil/Habilitado');"><span>Habilitar</span></h:commandLink>
	           	</div>
	           	
	           	<!-- Bot�n Cancelar -->
	           	<div class="enlaceCancelar">
	           		<a href="#" class="enlaceCancelarH">Cancelar</a>
	       		</div>
	   		</div>
		</div>
		<!-- Fin Paso Confirmar Habilitar -->
	                 
	    <div class="estadoDeshabilitado clearfix <h:outputText value="#{administracionServicios.administracionServiciosBean.mmsIshopImovilBean.imovil.activo? 'ocultar' : ''}"/>">
	       	<div class="columna1">
				<strong class="clearfix">Internet M&oacute;vil</strong>
	            <label class="titulo_inferior">$250 por sesi&oacute;n</label>
	        </div>
	           
	        <div class="columna2">
	           	<a class="ico_interrogacionNuevo autoTooltip" href="#TooltipInetMovil"></a>
			</div>
	           
	        <div class="columna3">
	           
	        	<!-- Mensaje habilitado/deshabilitado -->
	            <div class="mensaje deshabilitado">Deshabilitado</div>
	                      
	            <!-- Bto�n deshabilitado/habilitado -->
	             <div class="btnHabilitar">
	             	<a class="btnAzulDelgado btnAncho caso1H" href="#" onclick="mxTracker._trackPageview('Personas/Mi Entel/Mis Productos/Telefonia/Adm. de Servicios/Datos/Internet movil/Habilitar');"><span>Habilitar</span></a>
	             </div>
	                       
	       	</div>
		</div>
	                
	    <div class="estadoDeshabilitado1 clearfix ocultar">
	          
	       	<div class="columna1">
	           	<strong class="clearfix">Internet M&oacute;vil</strong>
	            <label class="titulo_inferior">$250 por sesi&oacute;n</label>
	        </div>
	             
	        <div class="columna2">
	          	<a class="ico_interrogacionNuevo autoTooltip" href="#TooltipInetMovil"></a>
	        </div>
	             
	        <div class="columna3">
	              
				<!-- Mensaje habilitado/deshabilitado -->
	           	<div class="mensaje_ancho">
	            	<strong>Deshabilitar&aacute;s Internet M&oacute;vil</strong>
	             	<br />
	         	</div>
	                    
	         	<!-- Bto�n deshabilitado/habilitado -->
	           	<div class="btnDeshabilitar">
	           		<a class="btnAzulDelgado btnAncho2 caso4" href="#" onclick="mxTracker._trackPageview('Personas/Mi Entel/Mis Productos/Telefonia/Adm. de Servicios/Datos/Internet movil/Deshabilitar');"><span>Deshabilitar</span></a>
	           	</div>
	                    
	           	<!-- Bot�n Cancelar -->
	           	<div class="enlaceCancelar">
	           		<a href="#" class="enlaceCancelar">Cancelar</a>
	       		</div>
	   		</div>
		</div>
	                 
	    <div class="estadoDeshabilitado2 clearfix ocultar">
	          
	    	<div class="columna1">
	        	<strong class="clearfix">Internet M&oacute;vil</strong>
	            <label class="titulo_inferior">$250 por sesi&oacute;n</label>
			</div>
	              
	        <div class="columna2">
	        	<a class="ico_interrogacionNuevo autoTooltip" href="#TooltipInetMovil"></a>
	        </div>
	              
	        <div class="columna3">
	              
	        	<!-- Mensaje grande -->
	            <div class="mensaje_grande mensaje_grande_reloj">
	            	En proceso de <strong>deshabilitaci&oacute;n</strong>
	            </div>
	        </div>
	    </div>

    </h:form>
</div>	

</h:panelGroup>

<h:panelGroup rendered="#{!administracionServicios.administracionServiciosBean.mmsIshopImovilBean.imovil.visible}"> 
	<jsp:include page="../servicioNoDisponible.jsp" flush="true">
	    <jsp:param value="Internet M&oacute;vil" name="serviceName"/>
	    <jsp:param value="$250 por sesi&oacute;n" name="cargo"/>
	    <jsp:param value="TooltipInetMovil" name="tooltip"/>
    </jsp:include>
</h:panelGroup>	                

<!-- /Fila de informaci�n -->
</entel:view>