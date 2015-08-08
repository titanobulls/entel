<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="f" uri="http://java.sun.com/jsf/core"%>
<%@ taglib prefix="h" uri="http://java.sun.com/jsf/html"%>
<%@ taglib prefix="pref" uri="http://www.bea.com/servers/portal/tags/netuix/preferences" %>
<%@ taglib prefix="cm" uri="http://www.bea.com/servers/portal/tags/content" %>

<cm:search id="flagMostrarEligeTuPromo" query="idContenido = 'flagMostrarEligeTuPromo'" useCache="true" />
<cm:getProperty node="${flagMostrarEligeTuPromo[0]}" name="valorFlag" resultId="mostrarEligeTuPromo" isMultiple="false"  />

<f:view>

<script type="text/javascript">
	$(document).ready(function() {
		var esVozBAM = "<h:outputText value="#{profile.flagBam == miEntelBean.siglaUsuarioBAM ? 'Banda Ancha Movil' : 'Telefonia'}"/>";
		if ($.browser.msie && $.browser.version == '7.0') {
			$('.btnNaranjaGrande').css('height', '36px');
			$('.btnCancelar').css('padding-top', '5px');
			$('.btnCancelar').css('float', 'none');
			$('.btnCancelar').css('display', 'inline-block');			
		}

		dataLayer = dataLayer||[];
		dataLayer.push({
			'mx_content': 'Personas/Mi Entel/Mis Productos/' + esVozBAM + '/Recargas/Web Pay/Confirmar',
			'event': 'pageview'
		});		
	});
</script>

<h1>Recargas</h1>

<h2 style="padding-left: 0;">Tarjeta de Cr&eacute;dito</h2>

<p><strong>Paso 2 de 3</strong></p>

<h:form>
	<jsp:include page="/token.jsp" flush="true"/>
	<h:inputHidden id="ordenCompra" value="#{recargaController.recargaWebPay.ordenCompra}" />	

	<div class="alerta_amarilla">
	    <span class="marginador clearfix">Confirma que todos los datos est&eacute;n correctos.</span>
	    <div class="tabla_formulario_fila clearfix">
	        <div class="tabla_formulario_label">N&uacute;mero Entel a recargar:</div>
	        <div class="tabla_formulario_label"><strong>
	        	<h:outputText value="#{recargaController.recargaWebPay.numeroPcs}" />
	        </strong></div>
	    </div>
	    <div class="tabla_formulario_fila clearfix">
	        <div class="tabla_formulario_label">Monto:</div>
	        <div class="tabla_formulario_label"><strong> $ 
	        	<h:outputText value="#{recargaController.recargaWebPay.montoRecarga}">
	        		<f:convertNumber currencyCode="CLP" locale="es" />
	        	</h:outputText>
	        </strong></div>
	    </div>
	    <!-- SC 27/08 LUIS Bypass, mostrarEligeTuPromo eq false, retornar a true luego -->
	    <c:if test="${mostrarEligeTuPromo eq false}">
		    <h:panelGroup rendered="#{recargaController.promoRecarga != null}">
			    <div class="tabla_formulario_fila clearfix">
			        <div class="tabla_formulario_label">Promoci&oacute;n:</div>
			        <div class="tabla_formulario_label" style="width: 285px">		        	
			        	<strong><h:outputText value="#{recargaController.promoRecarga.mensaje}" escape="false"/></strong>
		        	</div>
			    </div>
		    </h:panelGroup>
	    </c:if>	    
	</div>
	
	<div class="centrar_boton">
	    
	    <h:commandLink styleClass="btnNaranjaGrande alargar recargar_eticket" 
	    	action="#{recargaController.confirmarRecargaTarjetaCredito}">
	    	<span>Continuar</span></h:commandLink>
		&nbsp;
	    <h:commandLink styleClass="btnCancelar cancelar_eticket" 
	    	action="cancelar">Volver</h:commandLink>
	    
	</div>

</h:form>

</f:view>
