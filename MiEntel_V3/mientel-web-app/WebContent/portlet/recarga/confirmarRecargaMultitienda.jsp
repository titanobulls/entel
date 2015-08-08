<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="f"  uri="http://java.sun.com/jsf/core"%>
<%@ taglib prefix="h"  uri="http://java.sun.com/jsf/html"%>
<f:view>

<script type="text/javascript">
	$(document).ready(function() {
		var esVozBAM = "<h:outputText value="#{profile.flagBam == miEntelBean.siglaUsuarioBAM ? 'Banda Ancha Movil' : 'Telefonia'}"/>";

		dataLayer = dataLayer||[];
		dataLayer.push({
			'mx_content': 'Personas/Mi Entel/Mis Productos/' + esVozBAM + '/Recargas/CMR Falabella/Confirmar',
			'event': 'pageview'
		});		
	});
</script>

<h2 style="padding-left: 0;">Multitiendas</h2>

<p><strong>Paso 2 de 3</strong></p>

<h:form>
<jsp:include page="/token.jsp" flush="true"/>
<div class="alerta_amarilla">
    <span class="marginador clearfix">Confirma que todos los datos est&eacute;n correctos.</span>
    <div class="tabla_formulario_fila clearfix">
        <div class="tabla_formulario_label">N&uacute;mero Entel a recargar:</div>

        <div class="tabla_formulario_label"><strong><h:outputText value="#{recargaController.factibilidadMultitienda.numeroPcs}"></h:outputText></strong></div>
    </div>
    <div class="tabla_formulario_fila clearfix">
        <div class="tabla_formulario_label">Multitienda:</div>
        <div class="tabla_formulario_label"><strong><h:outputText value="#{recargaController.nombreMultitienda}"></h:outputText></strong></div>
    </div>
    <div class="tabla_formulario_fila clearfix">

        <div class="tabla_formulario_label">N&uacute;mero de tarjeta:</div>
        <div class="tabla_formulario_label"><strong><h:outputText value="#{recargaController.numeroTarjetaMultitienda}"></h:outputText></strong></div>
    </div>
    <div class="tabla_formulario_fila clearfix">
        <div class="tabla_formulario_label">Monto a cargar:</div>
        <div class="tabla_formulario_label">
        <strong>
        $<h:outputText value="#{recargaController.factibilidadRecarga.montoRecarga}">
        	<f:convertNumber currencyCode="CLP" locale="es" />
        </h:outputText>
        </strong>
        </div>

    </div>
    <div class="tabla_formulario_fila clearfix">
        <div class="tabla_formulario_label">Cuotas:</div>
        <div class="tabla_formulario_label"><strong><h:outputText value="#{recargaController.cuotas}"></h:outputText></strong></div>
    </div>
</div>

<div class="centrar_boton">
    <h:commandLink styleClass="btnVerdeDelgado alargar recargar_multitiendas" value="" action="#{recargaController.confirmarRecargaMultitienda}"><span>Recargar</span>
	<f:param name="monto" value="#{recargaController.montoRecarga}"></f:param>
	<f:param name="numeroPcs" value="#{recargaController.factibilidadMultitienda.numeroPcs}"></f:param>
	<f:param name="nroTarjeta" value="#{recargaController.numeroTarjetaMultitienda}"></f:param>
	<f:param name="claveTarjeta" value="#{recargaController.claveTarjetaMultitienda}"></f:param>
	<f:param name="cuotas" value="#{recargaController.cuotas}"></f:param>
	<f:param name="nombreMultitienda" value="#{recargaController.nombreMultitienda}"></f:param>
	</h:commandLink>	
	<h:commandLink styleClass="btnVerdeDelgado alargar recargar_eticket" action="#{recargaController.confirmarRecargaMultitienda}"><span>Cancelar</span></h:commandLink>

</div>

</h:form>

</f:view>
