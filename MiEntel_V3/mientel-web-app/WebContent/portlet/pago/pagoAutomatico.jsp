<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="f"  uri="http://java.sun.com/jsf/core"%>
<%@ taglib prefix="h"  uri="http://java.sun.com/jsf/html"%>
<%@ taglib prefix="cm" uri="http://www.bea.com/servers/portal/tags/content" %>
<%@ taglib prefix="entel" uri="/WEB-INF/tld/entel-tags.tld" %>

<!-- Contenidos -->
<cm:search id="infoRestriccionAAA" query="idContenido = 'infoRestriccionAAA'" useCache="false"/>

<f:view beforePhase="#{pagoAutomaticoController.init}">
<h:panelGroup binding="#{pagoAutomaticoController.panelInscrito}">
	<h1>Pago autom&aacute;tico</h1>

	<!-- Alerta de AAA -->
	<entel:view name="alertaAAAPAT">
	    <div class="mensaje-alerta-sistema">
	        <div class="clearfix sub-contenedor">
	            <div class="contenedor-imagen">
	            	<div class="imagen"></div>
	            </div>
	            <div class="texto">
	            	<span><cm:getProperty node="${infoRestriccionAAA[0]}" name="html"/></span>
	            </div>
	        </div>
	    </div>
	</entel:view>
	
	<entel:view name="bloquePAT">
		<p>Entel facilita el pago de tu cuenta a trav&eacute;s de estas dos modalidades de pago autom&aacute;tico.</p><br />

		<h2 class="sin_icono">PAT</h2>
		<p><strong>Pago Autom&aacute;tico con Tarjeta de Cr&eacute;dito</strong><br />Paga en forma r&aacute;pida, f&aacute;cil y autom&aacute;tica tu cuenta de Entel PCS con tu tarjeta de cr&eacute;dito.</p><br />

			
		<div class="caja verde clearfix">
			<div class="caja_imagen_correcto_1"></div>
			<div class="caja_texto">
				<p class="tipoVerde"><span class="caja_verde_titulo_pat"><strong>Actualmente tienes inscrito PAT</strong></span><br /><strong>Fecha de Inscripci&oacute;n: </strong><h:outputText value="#{pagoAutomaticoController.estadoPAT.fechaSuscripcion}">
				<f:convertDateTime pattern="dd MMMM yyyy" locale="es"/>
				</h:outputText><h:outputText value="#{pagoAutomaticoController.fechaEstadoPromo}"></h:outputText><br /><br /><span class="caja_linea"></span><br /><strong>Para desactivar PAT debes contactarte con tu banco.</strong>
				</p>
			</div>									
		</div>
	</entel:view>		
</h:panelGroup>

<h:panelGroup binding="#{pagoAutomaticoController.panelProceso}">
	<h1>Pago autom&aacute;tico</h1>

	<!-- Alerta de AAA -->
	<entel:view name="alertaAAAPAT">
	    <div class="mensaje-alerta-sistema">
	        <div class="clearfix sub-contenedor">
	            <div class="contenedor-imagen">
	            	<div class="imagen"></div>
	            </div>
	            <div class="texto">
	            	<span><cm:getProperty node="${infoRestriccionAAA[0]}" name="html"/></span>
	            </div>
	        </div>
	    </div>
	</entel:view>
	
	<entel:view name="bloquePAT">
		<p>Entel facilita el pago de tu cuenta a trav&eacute;s de estas dos modalidades de pago autom&aacute;tico.</p><br />

		<h2 class="sin_icono">PAT</h2>
		<p><strong>Pago Autom&aacute;tico con Tarjeta de Cr&eacute;dito</strong><br />Paga en forma r&aacute;pida, f&aacute;cil y autom&aacute;tica tu cuenta de Entel PCS con tu tarjeta de cr&eacute;dito.</p><br />

			
		<div class="caja verde clearfix">
			<div class="caja_imagen_en_proceso"></div>
			<div class="caja_texto">
				<p class="tipoVerde"><span class="caja_verde_titulo_pat"><strong>Actualmente PAT se encuentra en proceso de inscripci&oacute;n</strong></span><br /><strong>Fecha de Inscripci&oacute;n: </strong><h:outputText value="#{pagoAutomaticoController.estadoPAT.fechaSuscripcion}">
				<f:convertDateTime pattern="dd MMMM yyyy" locale="es"/>
				</h:outputText><br /><br /><span class="caja_linea"></span><br /><strong>Para desactivar PAT debes contactarte con tu banco.</strong>
				</p>
			</div>									
		</div>
	</entel:view>
</h:panelGroup>


<h:panelGroup binding="#{pagoAutomaticoController.panelNoInscrito}">

	<h1 id="titulo_pat_cambio">Pago autom&aacute;tico</h1>

	<!-- Alerta de AAA -->
	<entel:view name="alertaAAAPAT">
	    <div class="mensaje-alerta-sistema">
	        <div class="clearfix sub-contenedor">
	            <div class="contenedor-imagen">
	            	<div class="imagen"></div>
	            </div>
	            <div class="texto">
	            	<span><cm:getProperty node="${infoRestriccionAAA[0]}" name="html"/></span>
	            </div>
	        </div>
	    </div>
	</entel:view>
	
	<entel:view name="bloquePAT">
		<p>Entel facilita el pago de tu cuenta a trav&eacute;s de estas dos modalidades de pago autom&aacute;tico.</p><br />
		
		<div id="pago-automatico" class="clearfix">
			<div class="pago_automatico_bloque pago_automatico_espacio">
				<h2 class="sin_icono">PAT</h2>
				<p><strong>Pago Autom&aacute;tico con Tarjeta de Cr&eacute;dito</strong><br /><br />Suscr&iacute;bete para pagar en forma r&aacute;pida, f&aacute;cil y autom&aacute;tica tu cuenta de Entel PCS con tu tarjeta de cr&eacute;dito.</p><br />
	
				
				<div class="caja verde clearfix">
					<div class="caja_imagen_alerta"></div>
					<div class="caja_texto_pat">
						<p class="tipoVerde"><strong>Actualmente no tienes inscrito el sistema PAT.</strong><br /><br />
	    <h:form>
	    <jsp:include page="/token.jsp" flush="true"/>
		<h:commandLink styleClass="btnAzulGrande btnAzulGrandeLargo boton_parrafo_left" action="formulariopat"><span>Inscribe tu PAT</span></h:commandLink>
		</h:form></p>
		</div>								
				</div>
			</div>
			<div class="pago_automatico_bloque">
	
				<h2 class="sin_icono">PAC</h2>
				<p><strong>Pago Autom&aacute;tico con Cargo a Cuenta Corriente</strong><br /><br />Paga tu cuenta de Entel PCS en forma f&aacute;cil y autom&aacute;tica con PAC.</p><br />
				<div class="caja verde clearfix">
					<div></div>
					<div class="caja_texto_pac">
						<p>La inscripci&oacute;n de Pago Autom&aacute;tico la debes realizar en tu <a href="#" title="Pronto Podr�s Consultar las Sucursales" class="autoTooltip titleTT link_pago"><u>sucursal</u></a> Entel PCS.<br /></p>
	
					</div>									
				</div>					
			</div>				
		</div>
	</entel:view>
</h:panelGroup>

	<div class="contenedor-mensajes-verde clearfix">
	   <div></div>
	   	<h:messages id="messageList" 
			styleClass="mensajes-lista"
			errorClass="mensaje-error" 
			infoClass="mensaje-informacion" showSummary="true" />
	</div>
	
</f:view>

