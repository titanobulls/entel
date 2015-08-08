<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="f"  uri="http://java.sun.com/jsf/core"%>
<%@ taglib prefix="h"  uri="http://java.sun.com/jsf/html"%>
<%@ taglib prefix="c"  uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="r"  uri="http://www.bea.com/servers/portal/tags/netuix/render"%>
<%@ taglib uri="http://www.bea.com/servers/portal/tags/content" prefix="cm"%>
<f:view beforePhase="#{reposicionServicioController.init}">

<cm:search id="nodo" query="idContenido = 'msj_duracion_reposicion'" useCache="false"  />
<cm:search id="nodoControl" query="idContenido = 'msj_grupo_control'" useCache="false"  />

<h1>Reposici&oacute;n de servicio</h1>

<p><cm:getProperty node="${nodo[0]}" name="html" /></p>		
		
		<h:panelGroup rendered="#{reposicionServicioController.estadoReposicion != 'ACTIVO' && 
		reposicionServicioController.estadoReposicion != 'REALIZADA' && 
		reposicionServicioController.estadoReposicion != 'SUSPENDIDO' && 
		reposicionServicioController.estadoReposicion != 'CONTROL' && 
		reposicionServicioController.estadoReposicion != 'PERMITE' 
		}">		
		<div class="alerta_amarilla" style="margin:0 auto; float:none; padding:10px;">
				<p class="tipoAlerta" align="center">
					<strong>Estimado cliente, usted no cumple con las condiciones comerciales para ejecutar este servicio.</strong><br />
				</p>
			</div>	
		 </h:panelGroup>
		
   		<h:panelGroup rendered="#{reposicionServicioController.estadoReposicion == 'ACTIVO'}">
   		<div class="caja verde clearfix" style="padding:10px;">
				<div></div>
				<div class="caja_texto" align="center" style="margin:0 auto; float:none;">
					<p class="tipoAlerta_3">
						<span class="caja_verde_titulo"></span>
						<strong>El servicio est&aacute; activo, no necesitas reponer.</strong>
					</p>
				</div>									
		</div>		
   		</h:panelGroup>
   
   		<h:panelGroup rendered="#{reposicionServicioController.estadoReposicion == 'REALIZADA'}">
   		<div class="alerta_amarilla" style="margin:0 auto; float:none; padding:10px;">
				<p class="tipoAlerta" align="center">
					<strong>Ya realizaste una reposic&oacute;n de servicio este mes.<br />Solamente puedes realizar una reposic&oacute;n al mes.</strong><br />
				</p>
		</div>		
   		</h:panelGroup>
   	
   		<h:panelGroup rendered="#{reposicionServicioController.estadoReposicion == 'SUSPENDIDO'}">
   		<div class="alerta_amarilla"  style="margin:0 auto; float:none; padding:10px;">
				<p class="tipoAlerta">
					<strong>Usted se encuentra actualmente suspendido.</strong><br />
				</p>
		</div>		
   		</h:panelGroup>
   		
   		<h:panelGroup rendered="#{reposicionServicioController.estadoReposicion == 'CONTROL'}">
   		<div class="alerta_amarilla" style="margin:0 auto; float:none; padding:10px;">
				<p class="tipoAlerta" align="center">
					<cm:getProperty node="${nodoControl[0]}" name="html" />
				</p>
		</div>		
   		</h:panelGroup>
   		
       <h:panelGroup rendered="#{reposicionServicioController.estadoReposicion == 'PERMITE'}">
			<div id="reposicion_paso_1">
			<h:panelGroup rendered="#{reposicionServicioController.estadoFacturacion != reposicionServicioController.facturaPagada}">
				
				<div class="mensaje-alerta-sistema">
                   <div class="clearfix sub-contenedor">
                   <div class="contenedor-imagen">
                   <div class="imagen"></div>
                   </div>
                   <div class="texto">Tu cuenta registra saldo pendiente.</div>
                  </div>
                </div>
	       
	           <cm:search id="nodo2" query="idContenido = 'tabla_reposicion'" useCache="false"  />
	           <cm:getProperty node="${nodo2[0]}" name="html" />

				 </h:panelGroup>
				<div class="pago_cuenta clearfix">
					<p class="parrafo"></p>
					<h:outputLink disabled="#{reposicionServicioController.estadoReposicion != 'PERMITE'}" value="javascript:void(0)" styleClass="btnAzulGrande btnAzulGrandeLargo boton_parrafo_left reposicion_continuar" onclick="mxTracker._trackPageview('Personas/Mi Entel/Mi Cuenta/Pago de cuenta/Reposicion de servicios/Confirmar');"><span>Continuar</span></h:outputLink>
         		</div>

			</div>
			
			<div id="reposicion_paso_2" style="display:none;">
				<h:panelGroup rendered="#{reposicionServicioController.estadoFacturacion != reposicionServicioController.facturaPagada}">
				<div class="caja verde clearfix">
					<div></div>
					<div class="caja_texto">
						<p class="tipoAlerta_3">
							<span class="caja_verde_titulo"></span><br />
							  <cm:search id="nodo3" query="idContenido = 'msj_valor_duracion'" useCache="false"  />
	                          						
							<strong><cm:getProperty node="${nodo3[0]}" name="html" /></strong><br />

						</p>
					</div>									
				</div>
				</h:panelGroup>
				<div class="pago_cuenta clearfix">
					<p class="parrafo"></p>
					<h:form>
						<jsp:include page="/token.jsp" flush="true"/>
					<h:commandLink styleClass="btnAzulGrande btnAzulGrandeLargo boton_parrafo_left reposicion_continuar" value="" immediate="true"  action="#{reposicionServicioController.reponerServicio}"><span>Reponer servicio</span>
					<f:param name="estadoFacturas" value="#{reposicionServicioController.estadoFacturacion}"/>
					</h:commandLink>
					</h:form>
					<a href="#" class="reposicion_cancelar">Cancelar</a>
				</div>

			</div>
			
			<div id="reposicion_paso_3" style="display:none;">
				<div class="alerta_amarilla">
					<p class="tipoAlerta">
						<strong>El servicio estar&aacute; repuesto dentro de 15 minutos.<br />Recibir&aacute;s un mensaje de texto por esta transacci&oacute;n.</strong><br />
					</p>

				</div>
			</div>						
			
			<div id="pago-caja" class="reposicion_pago clearfix">
				<div class="pago_caja_bloque">
					<p>Tambi&eacute;n puedes pagar tus cuentas en l&iacute;nea.</p>
					<div class="pago_caja_enlace clearfix">

					<a class="ver_detalle link_2" 
					title="Realiza tu Pago en L&iacute;nea" 
					href="<r:pageUrl pageLabel='${reposicionServicioController.pageLabelPagoEnLinea}'></r:pageUrl>">
					<span>Ir a pago de cuenta</span>
					</a>
					
					</div>					
				</div>				
			</div>	
	</h:panelGroup>	
	<!-- MENSAJES -->
	<br/>

	<!-- MENSAJES -->
	<jsp:include page="../common/messages_table.jsp"></jsp:include>

</f:view>
