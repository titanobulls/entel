<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="f"  uri="http://java.sun.com/jsf/core"%>
<%@ taglib prefix="h"  uri="http://java.sun.com/jsf/html"%>
<%@ taglib prefix="c"  uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="it" uri="http://i2b.cl/jsf/iterator/" %>
<%@ taglib prefix="r"  uri="http://www.bea.com/servers/portal/tags/netuix/render"%>
<%@ taglib prefix="pref" uri="http://www.bea.com/servers/portal/tags/netuix/preferences" %>

<f:view beforePhase="#{inscripcionController.init}">

<style type="text/css">
			#bloque-izq, #bloque-der { 
		display: none !important; 
		}
</style>
 
<script type="text/javascript">
	$(document).ready(function(){
		widthDivCentro();

		dataLayer = dataLayer||[];
			dataLayer.push({
			'mx_content': 'Personas/Mi Entel/Registro/Confirmar',
			'event': 'pageview'
		});
	});

	function widthDivCentro(){
	 	 $('#centro').width("889px");
	  }
 </script>




<!-- MENSAJES -->
<jsp:include page="../common/messages_table.jsp"></jsp:include>
			
<!--CONTENEDOR PRINCIPAL-->
		<div id="contenedorForm" class="confirmacion">
		<h:form id="registrarRegistroTitular" styleClass="registrarRegistroTitular">
					<jsp:include page="/token.jsp" flush="true"/>
					<div class="registro_top">
						<h1>Formulario de Registro</h1>
						<p>Comprueba que todos los datos que ingresaste son correctos.</p>
					</div>
					
					<!-- DATOS PERSONALES -->
					
						<h2 class="icono-personas-blanco clearfix">
							<span>Datos personales</span>
						</h2>
						<div class="fieldset">
							<div class="fila-campo clearfix">
								<label>N&uacute;mero:</label>
								<div class="campo"><strong><h:outputText value="#{inscripcionController.usuarioBean.numeroPCS}"/></strong></div>
								<h:inputHidden value="#{inscripcionController.usuarioBean.numeroPCS}"/>
							</div>
							<div class="fila-campo clearfix">
								<label>RUT</label>
								<div class="campo"><strong><h:outputText value="#{inscripcionController.usuarioBean.rut}" converter="rutConverter"/></strong></div>
							    <h:inputHidden value="#{inscripcionController.rutUsuario}"/>
							</div>
						</div>
					
					<!-- /DATOS PERSONALES -->
					
					<!-- EMAIL -->
					<h2 class="icono-email clearfix">
						<span>Email</span>
					</h2>
					<div class="fieldset">
						<div class="fila-campo clearfix">
							<label>Email:</label>
							<div class="campo">
								<strong>
									<h:outputText value="#{inscripcionController.usuarioBean.email}"/>
								</strong>
							</div>
							<h:inputHidden value="#{inscripcionController.usuarioBean.email}"/>
						</div>
					</div>
                    <!-- /EMAIL -->
					
					 <!-- TELEFONO ADICIONAL -->
					<h2 class="icono-telefono clearfix">
						<span>Tel&eacute;fono adicional</span>
						<span class="info"></span>
						<!-- <a class="icono-interrogacion autoTooltip" href="#TTDireccion"></a> -->
					</h2>	
					<div class="fieldset">
						<div class="mis-datos-fila clearfix">
							<label>Tel&eacute;fono adicional:</label>
							<div class="campo" style="position: relative; z-index: 1;padding-left:20px;">
								<strong>
									<h:outputText value="#{inscripcionController.usuarioBean.prefijoTelefonoAdicional}" />
									<h:inputHidden value="#{inscripcionController.usuarioBean.prefijoTelefonoAdicional}"/>
									-
									<h:outputText value="#{inscripcionController.usuarioBean.telefonoAdicional}" />
									<h:inputHidden value="#{inscripcionController.usuarioBean.telefonoAdicional}"/>
								</strong>
							</div>
						</div>
					</div>
					
					<!-- BTN CONTINUAR -->
					<div class="fieldset">
						<div class="fila-campo clearfix">
							<div class="campo">
								<!--<a href="#" id="btnVolver" class="btnConfirmarVolver">Volver</a> -->
							</div>
							
							<div class="campo">
							 	<h:commandLink value="" action="#{inscripcionController.registrarUsuario}"	
			    	styleClass="btnAzulGrande btnAzulLargo registrarFormulario"><span>Registrarme</span></h:commandLink>
							</div>
						</div>			
					</div>
					<!-- /BTN CONTINUAR -->

					<!-- INSCRIBIR A BOLETA ELECTRONICA -->
					<h:inputHidden value="#{inscripcionController.suscripcionFactElectronica}" />					
		</h:form>	
		
		</div>
	<!--/CONTENEDOR PRINCIPAL-->
</f:view>				
