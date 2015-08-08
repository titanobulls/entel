<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="f"  uri="http://java.sun.com/jsf/core"%>
<%@ taglib prefix="h"  uri="http://java.sun.com/jsf/html"%>
<f:view>

<script type="text/javascript">
	$(document).ready(function() {
		var esVozBAM = "<h:outputText value="#{profile.flagBam == miEntelBean.siglaUsuarioBAM ? 'Banda Ancha Movil' : 'Telefonia'}"/>";

		dataLayer = dataLayer||[];
		dataLayer.push({
			'mx_content': 'Personas/Mi Entel/Mis Productos/'+ esVozBAM +'/Recargas/Entelticket/Confirmar',
			'event': 'pageview'
		});			
	});
</script>

<h:form id="formEntelTicket">
<jsp:include page="/token.jsp" flush="true"/>

<h2 style="padding-left: 0;">Entelticket</h2>

<p><strong>Paso 2 de 3</strong></p>

<div class="alerta_amarilla">
    <span class="marginador clearfix">Confirma que todos los datos est&eacute;n correctos.</span>
    <div class="tabla_formulario_fila clearfix">
        <div class="tabla_formulario_label">N&uacute;mero Entel a recargar:</div>

        <div class="tabla_formulario_label"><strong><h:outputText value="#{recargaController.facibilidadEntelticketBean.numeroPcs}"></h:outputText></strong></div>
    </div>
    <div class="tabla_formulario_fila clearfix">
        <div class="tabla_formulario_label">Monto:</div>
        <div class="tabla_formulario_label"><strong>
        $<h:outputText value="#{recargaController.facibilidadEntelticketBean.montoDispTicket}">
        	<f:convertNumber currencyCode="CLP" locale="es" />
        </h:outputText>
        </strong></div>
    </div>
    <div class="tabla_formulario_fila clearfix">

        <div class="tabla_formulario_label">N&uacute;mero secreto Entelticket:</div>
        <div class="tabla_formulario_label"><strong><h:outputText value="#{recargaController.facibilidadEntelticketBean.numeroEntelTicket}"/></strong></div>
    </div>
</div>

<div class="centrar_boton">

    <h:commandLink styleClass="btnVerdeDelgado alargar recargar_eticket" 
    		action="#{recargaController.confirmarRecargaEntelTicket}">
    
	    <f:param name="monto" value="#{recargaController.facibilidadEntelticketBean.montoDispTicket}"></f:param>
		<f:param name="numeroPcs" value="#{recargaController.facibilidadEntelticketBean.numeroPcs}"></f:param>
		<f:param name="nroEntelTicket" value="#{recargaController.facibilidadEntelticketBean.numeroEntelTicket}"></f:param>	
		<f:param name="folio" value="#{recargaController.facibilidadEntelticketBean.folioTicket}"></f:param>
		<f:param name="agencia" value="#{recargaController.facibilidadEntelticketBean.agenciaTicket}"></f:param>
	  
	    <span>Recargar</span>
	</h:commandLink>

    <h:commandLink styleClass="btnVerdeDelgado alargar recargar_eticket" 
    		action="cancelar"><span>Cancelar</span></h:commandLink>
</div>

</h:form>

</f:view>
