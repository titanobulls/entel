<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="f"  uri="http://java.sun.com/jsf/core"%>
<%@ taglib prefix="h"  uri="http://java.sun.com/jsf/html"%>
<%@ taglib prefix="c"  uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="it" uri="http://i2b.cl/jsf/iterator/" %>
<%@ taglib prefix="r"  uri="http://www.bea.com/servers/portal/tags/netuix/render"%>
<%@ taglib prefix="cm" uri="http://www.bea.com/servers/portal/tags/content" %>
<%@ taglib prefix="entel" uri="/WEB-INF/tld/entel-tags.tld" %>
<cm:search id="infoCambioPlan" query="idContenido = 'infoCambioPlan'" useCache="true"  />
<cm:search id="infoEfectivoCambio" query="idContenido = 'infoEfectivoCambio'" useCache="true"  />
<cm:search id="infoCambioPlanCC" query="idContenido = 'infoCambioPlanCC'" useCache="true"  />
<cm:search id="infoEfectivoCambioCC" query="idContenido = 'infoEfectivoCambioCC'" useCache="true"  />
<cm:search id="infoCondiciones" query="idContenido = 'infoCondiciones'" useCache="true"  />

<f:view beforePhase="#{planController.obtenerPlanesDisponibles}">

 	
	<input class="codbscs2" type="hidden" value=""/>
	<input class="nombreNuevoPlan" type="hidden" value=""/>
	<input class="valorNuevoPlan" type="hidden" value=""/>
	
	<h1>Cambio de Plan</h1>
		
	<h2 class="superchipOK">Planes disponibles</h2>
	
	<h:panelGroup layout="block" rendered="#{profile.mercado == miEntelBean.siglaCuentaControlada && !planController.validoPrestaLuka}"  
				  styleClass="mensaje-alerta-sistema-pequeno">
        <div class="clearfix sub-contenedor">
            <div style="width:5%" class="contenedor-imagen">
            	<div class="imagen"></div>
            </div>
            <div style="width:88%" class="texto"><h:outputText escape="false" value="#{planController.mensajeErrorPrestaLuka}" /></div>
        </div>
    </h:panelGroup>

	<h:panelGroup rendered="#{planController.existenPlanesDisponibles}">
		
		<p>Selecciona de la siguiente lista de planes que tenemos para ti a cual deseas cambiarte:</p>

		<div id="menu-desplegable-planes">
			
			<!-- Planes Disponibles  -->
			<it:iterator var="grupoPlan" value="#{planController.grupoPlanesDisponibles}" rowIndexVar="indexPlan">	
			<c:set var="style" value="#{indexPlan % 2 == 0 ? 'impar': 'par'}" scope="page" />
					

			<!-- bolsa -->
			<div class="bolsa <h:outputText value="#{style}"/> clearfix">
				<div class="header">
				
					<!-- Estructura - Nombre del Plan y Descripcion del mismo -->
					<a href="javascript:;" class="cerrado" onclick="mxTracker._trackPageview('Personas/Mi Entel/Mis Productos/Telefonia/Plan o cambio de plan/<h:outputText value="#{grupoPlan.nombrePlanSinAcentos}"/>/Inicio');"><h:outputText value="#{grupoPlan.nombrePlan}"/></a>
					<span><h:outputText value="#{grupoPlan.descPlan}"/></span>

				</div>
				<div id="red" class="lista-bolsas" style="display: none;">
				
					<div class="linea_tabs clearfix">
						
						<div class="tab contenido1 seleccionado" onclick="mxTracker._trackPageview('Personas/Mi Entel/Mis Productos/Telefonia/Plan o cambio de plan/<h:outputText value="#{grupoPlan.nombrePlanSinAcentos}"/>/Informacion');">
							<span class="informacion"> Informaci&oacute;n </span>
						</div>
						<f:verbatim rendered="#{!empty grupoPlan.tablaVelocidades}">  
							<div class="tab contenido2" onclick="mxTracker._trackPageview('Personas/Mi Entel/Mis Productos/Telefonia/Plan o cambio de plan/<h:outputText value="#{grupoPlan.nombrePlanSinAcentos}"/>/Velocidades');">
								<span class="velocidades"> Velocidades </span>
							</div>
						</f:verbatim>
						
					</div>
				
					<div class="contenido_tabs" sytle="border: #000 solid 1px;">
					
						<!-- Inicio Informaci�n General -->
						<div class="contenido_tab altEquipo contenido1" style="display: block;">
						
					<div class="tabla_precios">
					
						<div class="planes_header_tabla clearfix">
			
							<div class="top"><span></span></div>
							<div class="main">
								<table>
										<tr>
										
											<!-- Estructura Datos Fijos-->
											<h:panelGroup rendered="#{((grupoPlan.tipoPlan eq tipoPlanBean.planRedSs) && (grupoPlan.mercadoPlan eq miEntelBean.siglaSuscripcion)) || (grupoPlan.tipoPlan ne tipoPlanBean.planRedSs)}">
											<th width="60px" class="primera" rowspan="2">Nombre<br />del plan</th>
											</h:panelGroup>
											<h:panelGroup rendered="#{(grupoPlan.tipoPlan eq tipoPlanBean.planRedCc) && (grupoPlan.mercadoPlan eq miEntelBean.siglaCuentaControlada)}">
											<th width="80px" class="primera" rowspan="2">Nombre<br />del plan</th>
											</h:panelGroup>
											<th width="50px" rowspan="2">Cargo<br />fijo</th>
											<!-- //Estructura Datos Fijos-->
											
											<!--  PLAN TARIFA PLANA - SS CC  -->
											<h:panelGroup rendered="#{((grupoPlan.tipoPlan eq tipoPlanBean.planTarifaPlanaSs))}">
												<it:iterator value="#{grupoPlan.tiposTasacionPlan}" var="tasacion" rowIndexVar="tasacionIndex">
													<h:panelGroup rendered="#{tasacionIndex > 0}">
														<th width="50px"><h:outputText value="#{tasacion}"/></th>
													</h:panelGroup>
												</it:iterator>
											</h:panelGroup>
											<!--  PLAN TARIFA PLANA - SS CC  -->
											
											<!--  PLAN RED - SS CC   -->
											<!-- TipoPlanRed SS-->
											<h:panelGroup rendered="#{(grupoPlan.tipoPlan eq tipoPlanBean.planRedSs) && (grupoPlan.mercadoPlan eq miEntelBean.siglaSuscripcion)}">
												<th width="100px" class="borde_inferior" colspan="2">Minutos incluidos</td></th>
												<th width="100px" class="borde_inferior" colspan="2">Tarifa min. m&oacute;viles</td></th>
											</h:panelGroup>
											<!-- TipoPlanRed SS-->
											
											<!-- Estructura Plan-->
											
											<!-- TipoPlanRed CC-->
											<h:panelGroup rendered="#{(grupoPlan.tipoPlan eq tipoPlanBean.planRedCc) && (grupoPlan.mercadoPlan eq miEntelBean.siglaCuentaControlada)}">
												<th width="100px" class="borde_inferior" colspan="2">Tarifa min. m&oacute;viles</td></th>
											</h:panelGroup>
											<!-- TipoPlanRed CC-->
											<!--  PLAN RED - SS CC   -->
											
											<!--  PLAN MULTIMEDIA RED EXCEDIDO - SS  -->
												<h:panelGroup rendered="#{(grupoPlan.tipoPlan eq tipoPlanBean.planMultimediaRedExcedido )}">
													<th class="borde_inferior" colspan="4"><b>Incluido<b></th>
													<th class="borde_inferior" colspan="2"><b>Tarifas adicionales</b></th>
													<th width="17%" class="ultimo" rowspan="1">&nbsp;</th>
												</h:panelGroup>
											<!--  PLAN MULTIMEDIA RED EXCEDIDO - SS  -->
															
											<!--  PLAN MULTIMEDIA RED EXCEDIDO TODO DESTINO - SS  -->
												<h:panelGroup rendered="#{(grupoPlan.tipoPlan eq tipoPlanBean.planMultimediaRedExcedidoTodoDestino )}">
													<th class="borde_inferior" colspan="3"><b>Incluido<b></th>
													<th class="borde_inferior" colspan="2"><b>Tarifas adicionales</b></th>
													<th width="20%" class="ultimo" rowspan="1">&nbsp;</th>
												</h:panelGroup>
											<!--  PLAN MULTIMEDIA RED EXCEDIDO TODO DESTINO - SS  -->
											
											
											
											<!--   PLAN GLOBAL - TARIFA UNICA FRECUENTE SS  ------------------------- -->
											<!--   PLAN JOVEN - SS         -->
											<!--   PLAN FULL - SS          -->
											<!--   PLAN TARIFA UNICA - SS  -->
											<h:panelGroup rendered="#{(grupoPlan.tipoPlan eq tipoPlanBean.planTarifaUnicaFrec || 
											grupoPlan.tipoPlan eq tipoPlanBean.planJoven || 
											grupoPlan.tipoPlan eq tipoPlanBean.planFull || 
											grupoPlan.tipoPlan eq tipoPlanBean.planTarifaUnica)}">
												<th width="50px" rowspan="2">Minutos incluidos</th>
												 <it:iterator value="#{grupoPlan.tiposTasacionPlan}" var="tasacion" rowIndexVar="tasacionIndex">
													<h:panelGroup rendered="#{(tasacionIndex < 2)}">
													<th width="50px"><h:outputText value="#{tasacion}"/></th>
													</h:panelGroup>
												</it:iterator>
											</h:panelGroup>
											<!--   PLAN GLOBAL - TARIFA UNICA FRECUENTE SS  ------------------------- -->
											<!--   PLAN JOVEN - SS         -->
											<!--   PLAN FULL - SS          -->
											<!--   PLAN TARIFA UNICA - SS  -->
											
											
											<!--  PLAN RED FIJA - SS CC     -->
											<!-- TipoPlanRedFija SS CC-->
											<h:panelGroup rendered="#{(grupoPlan.tipoPlan eq tipoPlanBean.planRedFija)}">
												<th width="50px" rowspan="2">Minutos incluidos</th>
												 <it:iterator value="#{grupoPlan.tiposTasacionPlan}" var="tasacion" rowIndexVar="tasacionIndex">
													<h:panelGroup rendered="#{(tasacionIndex == 1)}">
														<th width="50px"><h:outputText value="#{tasacion}"/></th>
													</h:panelGroup>
												</it:iterator>
												<it:iterator value="#{grupoPlan.tiposTasacionPlan}" var="tasacion" rowIndexVar="tasacionIndex">
													<h:panelGroup rendered="#{(tasacionIndex == 0)}">
														<th width="50px"><h:outputText value="#{tasacion}"/></th>
													</h:panelGroup>
												</it:iterator>
												<it:iterator value="#{grupoPlan.tiposTasacionPlan}" var="tasacion" rowIndexVar="tasacionIndex">
													<h:panelGroup rendered="#{(tasacionIndex == 2)}">
														<th width="50px"><h:outputText value="#{tasacion}"/></th>
													</h:panelGroup>
												</it:iterator>
											</h:panelGroup>
											<!-- TipoPlanRedFija SS CC-->
											<!--  PLAN RED FIJA - SS CC     -->
											
											<!--  PLAN FAMILIA - SS  -->
											<h:panelGroup rendered="#{(grupoPlan.tipoPlan eq tipoPlanBean.planFamilia)}">
												
												<it:iterator value="#{grupoPlan.tiposTasacionPlan}" var="tasacion" rowIndexVar="tasacionIndex">
													<h:panelGroup rendered="#{(tasacionIndex == 1)}">
													<th width="50px"><h:outputText value="#{tasacion}"/></th>
													</h:panelGroup>
												</it:iterator>
												
												 <it:iterator value="#{grupoPlan.tiposTasacionPlan}" var="tasacion" rowIndexVar="tasacionIndex">
													<h:panelGroup rendered="#{(tasacionIndex == 2)}">
													<th width="50px"><h:outputText value="#{tasacion}"/></th>
													</h:panelGroup>
												</it:iterator>
													
													<it:iterator value="#{grupoPlan.tiposTasacionPlan}" var="tasacion" rowIndexVar="tasacionIndex">
													<h:panelGroup rendered="#{(tasacionIndex == 3)}">
													<th width="50px"><h:outputText value="#{tasacion}"/></th>
													</h:panelGroup>
												</it:iterator>
													
												<it:iterator value="#{grupoPlan.tiposTasacionPlan}" var="tasacion" rowIndexVar="tasacionIndex">
													<h:panelGroup rendered="#{(tasacionIndex == 0)}">
													<th width="50px"><h:outputText value="#{tasacion}"/></th>
													</h:panelGroup>
												</it:iterator>
											</h:panelGroup>
											<!--  PLAN FAMILIA - SS  -->
											
											
											<!--  PLAN RED EMPRESA - SS  -->
											<h:panelGroup rendered="#{(grupoPlan.tipoPlan eq tipoPlanBean.planRedEmpresa)}">
												<th width="50px" rowspan="2">A m&oacute;viles Entel</th>
												 <it:iterator value="#{grupoPlan.tiposTasacionPlan}" var="tasacion" rowIndexVar="tasacionIndex">
													<th width="50px"><h:outputText value="#{tasacion}"/></th>
												</it:iterator>
											</h:panelGroup>
											<!--  PLAN RED EMPRESA - SS  -->
											
											
											<!--  PLAN MULTIMEDIA - CC  -->
											<h:panelGroup rendered="#{(grupoPlan.tipoPlan eq tipoPlanBean.planMultimediaCc)}">
												 <it:iterator value="#{grupoPlan.tiposTasacionPlan}" var="tasacion" rowIndexVar="tasacionIndex">
													<h:panelGroup rendered="#{(tasacionIndex == 0)}">
													<th width="50px"><h:outputText value="#{tasacion}"/></th>
													</h:panelGroup>
												</it:iterator>
												
													<th width="50px">Cuota de tr&aacute;fico</th>
													
												<it:iterator value="#{grupoPlan.tiposTasacionPlan}" var="tasacion" rowIndexVar="tasacionIndex">
													<h:panelGroup rendered="#{(tasacionIndex > 0)}">
													<th width="50px"><h:outputText value="#{tasacion}"/></th>
													</h:panelGroup>
												</it:iterator>
												
											</h:panelGroup>
											<!--  PLAN MULTIMEDIA - CC  -->
											
											<!--  PLAN MM IPHONE - SS  -->	
											<h:panelGroup rendered="#{(grupoPlan.tipoPlan eq tipoPlanBean.planMultimediaIphone)}">
													<th width="50px">Minutos On-Net</th>
													<th width="50px">Minutos todo destino</th>
													 <it:iterator value="#{grupoPlan.tiposTasacionPlan}" var="tasacion" rowIndexVar="tasacionIndex">
														<h:panelGroup rendered="#{(tasacionIndex eq 0)}">
														<th width="50px"><h:outputText value="#{tasacion}"/></th>
														</h:panelGroup>
													</it:iterator>
													<th width="50px">Internet M&oacute;vil</th>
													
													<it:iterator value="#{grupoPlan.tiposTasacionPlan}" var="tasacion" rowIndexVar="tasacionIndex">
														<h:panelGroup rendered="#{(tasacionIndex gt 0)}">
														<th width="50px"><h:outputText value="#{tasacion}"/></th>
														</h:panelGroup>
													</it:iterator>
											</h:panelGroup>
										    <!--  PLAN MM IPHONE - SS  -->
										    
										    <!--  PLAN MM RED - SS  -->	
											<h:panelGroup rendered="#{(grupoPlan.tipoPlan eq tipoPlanBean.planMultimediaRed)}">
													<th width="50px">Minutos On-Net y Red Fija</th>
													<th width="50px">Minutos Otras compa&ntilde;&iacute;as</th>
													 <it:iterator value="#{grupoPlan.tiposTasacionPlan}" var="tasacion" rowIndexVar="tasacionIndex">
														<h:panelGroup rendered="#{(tasacionIndex eq 0)}">
														<th width="50px"><h:outputText value="#{tasacion}"/></th>
														</h:panelGroup>
													</it:iterator>
													<th width="50px">Internet M&oacute;vil</th>
													
													<it:iterator value="#{grupoPlan.tiposTasacionPlan}" var="tasacion" rowIndexVar="tasacionIndex">
														<h:panelGroup rendered="#{(tasacionIndex gt 0)}">
														<th width="50px"><h:outputText value="#{tasacion}"/></th>
														</h:panelGroup>
													</it:iterator>
											</h:panelGroup>
										    <!--  PLAN MM RED - SS  -->
										    
										    
										    <!--  PLAN MM RED EXCEDIDO - SS  -->	
													<h:panelGroup rendered="#{(grupoPlan.tipoPlan eq tipoPlanBean.planMultimediaRedExcedido)}">
															<tr>
															<th>Minutos a Entel o red Fija</th>
															<th>Minutos a otras compa&ntilde;&iacute;as</th>										
															<th>Cuota tr�fico Internet m�vil</th>	
															 <it:iterator value="#{grupoPlan.tiposTasacionPlan}" var="tasacion" rowIndexVar="tasacionIndex">
																<h:panelGroup rendered="#{(tasacionIndex eq 0)}">
																<th><h:outputText value="#{tasacion}"/></th>
																</h:panelGroup>
															</it:iterator>
																								
															<it:iterator value="#{grupoPlan.tiposTasacionPlan}" var="tasacion" rowIndexVar="tasacionIndex">
																<h:panelGroup rendered="#{(tasacionIndex == 1)}">
																<th><h:outputText value="#{tasacion}"/></th>
																</h:panelGroup>
															</it:iterator>
															
															<it:iterator value="#{grupoPlan.tiposTasacionPlan}" var="tasacion" rowIndexVar="tasacionIndex">
																<h:panelGroup rendered="#{(tasacionIndex == 2)}">
																<th><h:outputText value="#{tasacion}"/></th>
																</h:panelGroup>
															</it:iterator>															
															
															</tr>
															
													</h:panelGroup>
												<!--  PLAN MM RED EXCEDIDO - SS  -->														
												<!--  PLAN MM RED EXCEDIDO TODO DESTINO - SS  -->	
													<h:panelGroup rendered="#{(grupoPlan.tipoPlan eq tipoPlanBean.planMultimediaRedExcedidoTodoDestino)}">
																	<tr>
																	<th>Minutos todo destino</th>
										                            <th>Cuota tr�fico Internet m�vil</th>										
																	 <it:iterator value="#{grupoPlan.tiposTasacionPlan}" var="tasacion" rowIndexVar="tasacionIndex">
																		<h:panelGroup rendered="#{(tasacionIndex eq 0)}">
																		<th><h:outputText value="#{tasacion}"/></th>
																		</h:panelGroup>
																	</it:iterator>
																									
																	<it:iterator value="#{grupoPlan.tiposTasacionPlan}" var="tasacion" rowIndexVar="tasacionIndex">
																		<h:panelGroup rendered="#{(tasacionIndex == 1)}">
																		<th><h:outputText value="#{tasacion}"/></th>
																		</h:panelGroup>
																	</it:iterator>
																	
																	<it:iterator value="#{grupoPlan.tiposTasacionPlan}" var="tasacion" rowIndexVar="tasacionIndex">
																		<h:panelGroup rendered="#{(tasacionIndex == 2)}">
																		<th><h:outputText value="#{tasacion}"/></th>
																		</h:panelGroup>
																	</it:iterator>
																	</tr>
														</h:panelGroup>
													<!--  PLAN MM RED EXCEDIDO TODO DESTINO - SS  -->
													
													<!--  OTROS PLANES - SS  -->
													<h:panelGroup rendered="#{(grupoPlan.tipoPlan gt tipoPlanBean.planMultimediaRed && 
													                           grupoPlan.tipoPlan gt tipoPlanBean.planMultimediaRedExcedido && 
													                           grupoPlan.tipoPlan gt tipoPlanBean.planMultimediaRedExcedidoTodoDestino
													                           )}">
													   <it:iterator value="#{grupoPlan.tiposTasacionPlan}" var="tasacion" rowIndexVar="tasacionIndex">
															<th width="50px"><h:outputText value="#{tasacion}"/></th>
														</it:iterator>
													</h:panelGroup>
													<!--  OTROS PLANES - SS  -->
				
												   <!--  OTROS PLANES - SS  -->
													<h:panelGroup rendered="#{(	grupoPlan.tipoPlan != tipoPlanBean.planMultimediaRedExcedido && 
													                            grupoPlan.tipoPlan != tipoPlanBean.planMultimediaRedExcedidoTodoDestino
													                           )}">
													  <th width="20%" class="ultimo" rowspan="2">&nbsp;</th> 
													</h:panelGroup>											
											
										</tr>
										
										<!--  PLAN RED - SS CC   -->
										<!-- TipoPlanRed SS-->
										<h:panelGroup rendered="#{(grupoPlan.tipoPlan eq tipoPlanBean.planRedSs) && (grupoPlan.mercadoPlan eq miEntelBean.siglaSuscripcion)}">
											<tr>
												<it:iterator value="#{grupoPlan.tiposTasacionPlan}" var="tasacion" rowIndexVar="tasacionIndex">
													<th width="50px"><h:outputText value="#{tasacion}"/></th>
												</it:iterator>
												 <it:iterator value="#{grupoPlan.tiposTasacionPlan}" var="tasacion" rowIndexVar="tasacionIndex">
													<th width="50px"><h:outputText value="#{tasacion}"/></td></th>
												</it:iterator>
												
											</tr>
										</h:panelGroup>
										<!-- TipoPlanRed SS-->
										
										<!-- TipoPlanRed CC-->		
										<h:panelGroup rendered="#{(grupoPlan.tipoPlan eq tipoPlanBean.planRedCc) && (grupoPlan.mercadoPlan eq miEntelBean.siglaCuentaControlada)}">
												<tr>
												 <it:iterator value="#{grupoPlan.tiposTasacionPlan}" var="tasacion" rowIndexVar="tasacionIndex">
													<th width="50px"><h:outputText value="#{tasacion}"/></td></th>
												</it:iterator>
												</tr>
										</h:panelGroup>
										<!-- TipoPlanRed CC-->
										<!--  PLAN RED - SS CC   -->
				
								</table>					
							</div>
							<div class="bottom"><span></span></div>
									<div id="<h:outputText value="#{grupoPlan.tipoPlan}"/>" style="display: none;"></div>
						</div>
			
						
						<table class="planes_contabla">
						 <tbody>
							<!-- Planes Disponibles Agrupados -->
							<it:iterator value="#{grupoPlan.planesDisponibles}" var="plan" rowIndexVar="planIndex">
								 <c:set var="style" value="#{planIndex % 2 == 0 ? 'color': ''}" scope="page" />

								 <tr class="<h:outputText value="#{style}"/>">
								 
									<!-- Estructura Datos Fijos-->
									<td width="60px" class="primera" id="<h:outputText value='#{plan.codbscs2}'/>"><h:outputText value="#{plan.nombrePlan}"/></td>
									<td width="50px">$<h:outputText value="#{plan.cargoFijoPlan}"><f:convertNumber currencyCode="CLP" locale="es" /></h:outputText></td>
									<!-- //Estructura Datos Fijos-->
									
									
									<!--  PLAN TARIFA PLANA - SS CC  -->
									
									
									<h:panelGroup rendered="#{((grupoPlan.tipoPlan eq tipoPlanBean.planTarifaPlanaSs))}">									
										<it:iterator value="#{plan.listaTasaciones}" var="listaTasaciones" rowIndexVar="tasacionIndex">
											<h:panelGroup rendered="#{tasacionIndex > 0}">
												<td width="50px">$<h:outputText value="#{listaTasaciones.valor}" converter="planConverter"/></td>
											</h:panelGroup>
										</it:iterator>
									 </h:panelGroup>
									<!--  PLAN TARIFA PLANA - SS CC  -->
									
									
									<!--  PLAN RED - SS CC   -->
								    <!-- TipoPlanRed SS-->
									<h:panelGroup rendered="#{(grupoPlan.tipoPlan eq tipoPlanBean.planRedSs) && (grupoPlan.mercadoPlan eq miEntelBean.siglaSuscripcion)}">
										<td width="50px"><h:outputText value="#{plan.totalMinutos}" converter="planConverter"/></td>
										<td width="50px"><h:outputText value="#{plan.totalMinutosAdicional}" converter="planConverter"/></td>
										
										<it:iterator value="#{plan.listaTasaciones}" var="listaTasaciones">
											<td width="50px">$<h:outputText value="#{listaTasaciones.valor}" converter="planConverter"/></td>
										</it:iterator>
									</h:panelGroup>
									<!-- TipoPlanRed SS-->
									
									
									<!-- TipoPlanRed CC-->
									<h:panelGroup rendered="#{(grupoPlan.tipoPlan eq tipoPlanBean.planRedCc) && (grupoPlan.mercadoPlan eq miEntelBean.siglaCuentaControlada)}">
										<it:iterator value="#{plan.listaTasaciones}" var="listaTasaciones">
											<td width="50px">$<h:outputText value="#{listaTasaciones.valor}" converter="planConverter"/></td>
										</it:iterator>
									</h:panelGroup>
									<!-- TipoPlanRed CC-->
									<!--  PLAN RED - SS CC   -->
									
									
									<!--   PLAN GLOBAL - TARIFA UNICA FRECUENTE SS  ------------------------- -->
									<!--   PLAN JOVEN - SS         -->
									<!--   PLAN FULL - SS          -->
									<!--   PLAN TARIFA UNICA - SS  -->
									<h:panelGroup rendered="#{(grupoPlan.tipoPlan eq tipoPlanBean.planTarifaUnicaFrec || 
									grupoPlan.tipoPlan eq tipoPlanBean.planJoven || 
									grupoPlan.tipoPlan eq tipoPlanBean.planFull || 
									grupoPlan.tipoPlan eq tipoPlanBean.planTarifaUnica)}">
										<td width="50px"><h:outputText value="#{plan.totalMinutos}" converter="planConverter"/></td>
										
										<it:iterator value="#{plan.listaTasaciones}" var="listaTasaciones" rowIndexVar="tasacionIndex">
											<h:panelGroup rendered="#{(tasacionIndex < 2)}">
											<td width="50px">$<h:outputText value="#{listaTasaciones.valor}" converter="planConverter"/></td>
											</h:panelGroup>
										</it:iterator>
									</h:panelGroup>
									<!--   PLAN GLOBAL - TARIFA UNICA FRECUENTE SS  ------------------------- -->
									<!--   PLAN JOVEN - SS         -->
									<!--   PLAN FULL - SS          -->
									<!--   PLAN TARIFA UNICA - SS  -->
									
									
									<!--  PLAN RED FIJA - SS CC   -->
									<!-- TipoPlanRedFija SS CC-->
									<h:panelGroup rendered="#{(grupoPlan.tipoPlan eq tipoPlanBean.planRedFija)}">
										<td width="50px"><h:outputText value="#{plan.totalMinutos}" converter="planConverter"/></td>
										
										<it:iterator value="#{plan.listaTasaciones}" var="listaTasaciones" rowIndexVar="tasacionIndex">
											<h:panelGroup rendered="#{(tasacionIndex == 1)}">
												<td width="50px">$<h:outputText value="#{listaTasaciones.valor}" converter="planConverter"/></td>
											</h:panelGroup>
										</it:iterator>
										
										<it:iterator value="#{plan.listaTasaciones}" var="listaTasaciones" rowIndexVar="tasacionIndex">
											<h:panelGroup rendered="#{(tasacionIndex == 0)}">
												<td width="50px">$<h:outputText value="#{listaTasaciones.valor}" converter="planConverter"/></td>
											</h:panelGroup>
										</it:iterator>
										
										<it:iterator value="#{plan.listaTasaciones}" var="listaTasaciones" rowIndexVar="tasacionIndex">
											<h:panelGroup rendered="#{(tasacionIndex == 2)}">
												<td width="50px">$<h:outputText value="#{listaTasaciones.valor}" converter="planConverter"/></td>
											</h:panelGroup>
										</it:iterator>
									</h:panelGroup>
									<!-- TipoPlanRedFija SS CC-->
									<!--  PLAN RED FIJA - SS CC   -->
									
									
									<!--  PLAN FAMILIA - SS  -->
									<h:panelGroup rendered="#{(grupoPlan.tipoPlan eq tipoPlanBean.planFamilia)}">

										 <it:iterator value="#{plan.listaTasaciones}" var="listaTasaciones" rowIndexVar="tasacionIndex">
											<h:panelGroup rendered="#{(tasacionIndex == 1)}">
											<td width="50px"><h:outputText value="#{listaTasaciones.valor}" converter="planConverter"/></td>
											</h:panelGroup>
										</it:iterator>
										
										 <it:iterator value="#{plan.listaTasaciones}" var="listaTasaciones" rowIndexVar="tasacionIndex">
											<h:panelGroup rendered="#{(tasacionIndex == 2)}">
											<td width="50px"><h:outputText value="#{listaTasaciones.valor}" converter="planConverter"/></td>
											</h:panelGroup>
										</it:iterator>
											
											
										 <it:iterator value="#{plan.listaTasaciones}" var="listaTasaciones" rowIndexVar="tasacionIndex">
											<h:panelGroup rendered="#{(tasacionIndex == 3)}">
											<td width="50px">$<h:outputText value="#{listaTasaciones.valor}" converter="planConverter"/></td>
											</h:panelGroup>
										</it:iterator>
											
										<it:iterator value="#{plan.listaTasaciones}" var="listaTasaciones" rowIndexVar="tasacionIndex">
											<h:panelGroup rendered="#{(tasacionIndex == 0)}">
											<td width="50px">$<h:outputText value="#{listaTasaciones.valor}" converter="planConverter"/></td>
											</h:panelGroup>
										</it:iterator>
									</h:panelGroup>
									<!--  PLAN FAMILIA - SS  -->
									
									
									<!--  PLAN RED EMPRESA - SS  -->
									<h:panelGroup rendered="#{(grupoPlan.tipoPlan eq tipoPlanBean.planRedEmpresa)}">
										<td width="50px">Ilimitados</td>
										
										<it:iterator value="#{plan.listaTasaciones}" var="listaTasaciones">
											<td width="50px">$<h:outputText value="#{listaTasaciones.valor}" converter="planConverter"/></td>
										</it:iterator>
									</h:panelGroup>
									<!--  PLAN RED EMPRESA - SS  -->
									
									
									<!--  PLAN MULTIMEDIA - CC  -->
									<h:panelGroup rendered="#{(grupoPlan.tipoPlan eq tipoPlanBean.planMultimediaCc)}">
										<it:iterator value="#{plan.listaTasaciones}" var="listaTasaciones" rowIndexVar="tasacionIndex">
											<h:panelGroup rendered="#{(tasacionIndex == 0)}">
											<td width="50px">$<h:outputText value="#{listaTasaciones.valor}" converter="planConverter"/></td>
											</h:panelGroup>
										</it:iterator>
										
										<td width="50px"><h:outputText value="#{plan.descIMovil}"/></td>
										
										<it:iterator value="#{plan.listaTasaciones}" var="listaTasaciones" rowIndexVar="tasacionIndex">
											<h:panelGroup rendered="#{(tasacionIndex > 0)}">
											<td width="50px">$<h:outputText value="#{listaTasaciones.valor}" converter="planConverter"/></td>
											</h:panelGroup>
										</it:iterator>
										
									</h:panelGroup>
									<!--  PLAN MULTIMEDIA - CC  -->
									
									<!--  PLAN MM IPHONE - SS  -->	
									<h:panelGroup rendered="#{(grupoPlan.tipoPlan eq tipoPlanBean.planMultimediaIphone)}">
											<th width="50px"><h:outputText value="#{plan.totalMinutos}" converter="planConverter"/></th>
											<th width="50px"><h:outputText value="#{plan.totalMinutosAdicional}" converter="planConverter"/></th>
											 <it:iterator value="#{plan.listaTasaciones}" var="listaTasaciones" rowIndexVar="tasacionIndex">
												<h:panelGroup rendered="#{(tasacionIndex eq 0)}">
												<th width="50px"><h:outputText value="#{listaTasaciones.valor}"/></th>
												</h:panelGroup>
											</it:iterator>
											<th width="50px"><h:outputText value="#{plan.descIMovil}"/></th>
											
											<it:iterator value="#{grupoPlan.tiposTasacionPlan}" var="tasacion" rowIndexVar="tasacionIndex">
												<h:panelGroup rendered="#{(tasacionIndex gt 0)}">
												<th width="50px">$<h:outputText value="#{listaTasaciones.valor}" converter="planConverter"/></th>
												</h:panelGroup>
											</it:iterator>
									</h:panelGroup>
								    <!--  PLAN MM IPHONE - SS  -->
								    
								    <!--  PLAN MM RED - SS  -->	
									<h:panelGroup rendered="#{(grupoPlan.tipoPlan eq tipoPlanBean.planMultimediaRed)}">
											<th width="50px"><h:outputText value="#{plan.totalMinutos}" converter="planConverter"/></th>
											<th width="50px"><h:outputText value="#{plan.totalMinutosAdicional}" converter="planConverter"/></th>
											 <it:iterator value="#{plan.listaTasaciones}" var="listaTasaciones" rowIndexVar="tasacionIndex">
												<h:panelGroup rendered="#{(tasacionIndex eq 0)}">
												<th width="50px"><h:outputText value="#{listaTasaciones.valor}"/></th>
												</h:panelGroup>
											</it:iterator>
											<th width="50px"><h:outputText value="#{plan.descIMovil}"/></th>
											
											<it:iterator value="#{grupoPlan.tiposTasacionPlan}" var="tasacion" rowIndexVar="tasacionIndex">
												<h:panelGroup rendered="#{(tasacionIndex gt 0)}">
												<th width="50px">$<h:outputText value="#{listaTasaciones.valor}" converter="planConverter"/></th>
												</h:panelGroup>
											</it:iterator>
									</h:panelGroup>
								    <!--  PLAN MM RED - SS  -->
									
									<!--  PLAN MM RED EXCEDIDO - SS O PLAN MM RED EXCEDIDO TODO DESTINO - SS -->
											<h:panelGroup rendered="#{(grupoPlan.tipoPlan eq tipoPlanBean.planMultimediaRedExcedido)}">
											
												<td><h:outputText value="#{plan.totalMinutos}" converter="planConverter"/></td>
												<td style="width:15%"><h:outputText value="#{plan.totalMinutosAdicional}" converter="planConverter"/></td>
												<td><h:outputText value="#{plan.descIMovil}"/></td>
												 
												 <it:iterator value="#{plan.listaTasaciones}" var="listaTasaciones" rowIndexVar="tasacionIndex">
													<h:panelGroup rendered="#{(tasacionIndex eq 0)}">
													<td><h:outputText value="#{listaTasaciones.valor}"  converter="planConverter"/></td>
													</h:panelGroup>
												</it:iterator>
																									
												<it:iterator value="#{plan.listaTasaciones}" var="tasaciones" rowIndexVar="tasacionIndex">
													<h:panelGroup rendered="#{(tasacionIndex eq 1)}">
													<td>$<h:outputText value="#{tasaciones.valor}" converter="planConverter"/></td>
													</h:panelGroup>
												</it:iterator>
												
												<it:iterator value="#{plan.listaTasaciones}" var="tasaciones" rowIndexVar="tasacionIndex">
													<h:panelGroup rendered="#{(tasacionIndex eq 2)}">
													<td>$<h:outputText value="#{tasaciones.valor}" converter="planConverter"/></td>
													</h:panelGroup>
												</it:iterator>
												
											</h:panelGroup>
									<!--  PLAN MM RED  EXCEDIDO - SS  -->
											
								    <!--  PLAN MM RED EXCEDIDO TODO DESTINO - SS -->
										    <h:panelGroup rendered="#{(grupoPlan.tipoPlan eq tipoPlanBean.planMultimediaRedExcedidoTodoDestino)}">
																								
													<td><h:outputText value="#{plan.totalMinutos}" converter="planConverter"/></td>
													<td><h:outputText value="#{plan.descIMovil}"/></td>
													
													 <it:iterator value="#{plan.listaTasaciones}" var="listaTasaciones" rowIndexVar="tasacionIndex">
														<h:panelGroup rendered="#{(tasacionIndex eq 0)}">
														<td><h:outputText value="#{listaTasaciones.valor}"  converter="planConverter"/></td>
														</h:panelGroup>
													</it:iterator>
														
													<it:iterator value="#{plan.listaTasaciones}" var="tasaciones" rowIndexVar="tasacionIndex">
														<h:panelGroup rendered="#{(tasacionIndex eq 1)}">
														<td>$<h:outputText value="#{tasaciones.valor}" converter="planConverter"/></td>
														</h:panelGroup>
												   </it:iterator>
												   
												   <it:iterator value="#{plan.listaTasaciones}" var="tasaciones" rowIndexVar="tasacionIndex">
														<h:panelGroup rendered="#{(tasacionIndex eq 2)}">
														<td>$<h:outputText value="#{tasaciones.valor}" converter="planConverter"/></td>
														</h:panelGroup>
												   </it:iterator>
												   
													
											</h:panelGroup>
										<!--  PLAN MM RED  EXCEDIDO TODO DESTINO - SS  -->											
											
										<!--  OTROS PLANES - SS CC  -->
										<h:panelGroup rendered="#{(grupoPlan.tipoPlan gt tipoPlanBean.planMultimediaRed &&
										                           grupoPlan.tipoPlan gt tipoPlanBean.planMultimediaRedExcedido &&
										                           grupoPlan.tipoPlan gt tipoPlanBean.planMultimediaRedExcedidoTodoDestino
										                  )}">
											<it:iterator value="#{plan.listaTasaciones}" var="listaTasaciones">
												<td width="50px">$<h:outputText value="#{listaTasaciones.valor}" converter="planConverter"/></td>
											</it:iterator>
										</h:panelGroup>
										<!--  OTROS PLANES - SS CC  -->									
									
									
									<td width="20%">

										<entel:view name="cambiarPlan">
										
										 	<h:inputHidden value="#{grupoPlan.tipoPlan}" />
										 	
										 	<f:verbatim rendered="#{profile.mercado == miEntelBean.siglaCuentaControlada}">
										 		<f:verbatim rendered="#{!planController.validoPrestaLuka}">						
											    	<a href="#" style="display:none" class="flotar_der btnDesactivado"><span>Contratar</span></a>   
											    </f:verbatim>
											    <f:verbatim rendered="#{planController.validoPrestaLuka}">						
											    	<a href="#" class="btnCambiarmePlan cambiarPlan" onclick="mxTracker._trackPageview('Personas/Mi Entel/Mis Productos/Telefonia/Plan o cambio de plan/<h:outputText value="#{grupoPlan.nombrePlanSinAcentos}"/>/Contratar');">Cambiarme a este plan</a>
											    </f:verbatim>
										 	</f:verbatim>									 	
										 										 	
										 
											<!--  OTROS PLANES - SS CC  -->
												<h:panelGroup rendered="#{(grupoPlan.tipoPlan != tipoPlanBean.planMultimediaRedExcedido &&
												                           grupoPlan.tipoPlan != tipoPlanBean.planMultimediaRedExcedidoTodoDestino
												                          )}">
												       <f:verbatim rendered="#{profile.mercado == miEntelBean.siglaSuscripcion}">
									 		            <a href="#" class="btnCambiarmePlan cambiarPlan" onclick="mxTracker._trackPageview('Personas/Mi Entel/Mis Productos/Telefonia/Plan o cambio de plan/<h:outputText value="#{grupoPlan.nombrePlanSinAcentos}"/>/Contratar');">Cambiarme a este plan</a>
									 	               </f:verbatim>
												</h:panelGroup>
										    <!--  OTROS PLANES - SS CC  -->	
										    
										    <!--   PLANES - EXCEDIDOS SS   -->
												<h:panelGroup rendered="#{(grupoPlan.tipoPlan == tipoPlanBean.planMultimediaRedExcedido ||
												                           grupoPlan.tipoPlan == tipoPlanBean.planMultimediaRedExcedidoTodoDestino
												                          )}">
													<f:verbatim rendered="#{profile.mercado == miEntelBean.siglaSuscripcion}">
									 		          <a href="#" style="width:70px;" class="btnCambiarmePlan cambiarPlan" onclick="mxTracker._trackPageview('Personas/Mi Entel/Mis Productos/Telefonia/Plan o cambio de plan/<h:outputText value="#{grupoPlan.nombrePlanSinAcentos}"/>/Contratar');">Cambiarme a este plan</a>
									 	             </f:verbatim>
												</h:panelGroup>
										    <!--   PLANES - EXCEDIDOS SS CC  -->										 
												
										 	
										</entel:view>

										<entel:view name="cambiarPlan" inverse="true">
										<!-- Mensaje grande -->
										<div class="mensajeDeshabilitado">
											Deshabilitado<br />Solo el titular o el administrador de la cuenta puede cambiar el estado de la cuenta.
											<br />
											<h:form>
												<jsp:include page="/token.jsp" flush="true"/>
												<h:commandLink action="mis_usuarios" styleClass="enlace">Ir a tabla de atributos</h:commandLink>
											</h:form>
										</div>
										</entel:view>

												<input class='TarifaUnicaConSMS' type='hidden' value='<h:outputText value="#{plan.tarifaUnicaConSMS}"/>'/>

									</td>
								</tr>
							 </it:iterator>
							 
						 </tbody>
						</table>
						<div class="disclaimer"></div>
					</div><!--/tabla_precios-->
								
							</div>
						<!-- Fin Informaci�n General -->
						
						<!-- Inicio Velocidades de Planes Multimedia -->
						
						<div class="contenido_tab altEquipo contenido2">
						
						
                           <div class="planes_header_tabla clearfix">	
                              <f:verbatim rendered="#{!empty grupoPlan.tablaVelocidades}">                              			
								<div class="top"><span></span></div>
								<div class="main">						
							
										<table>
											<tr>
												<th class="primera" width="60px" rowspan="2">Nombre <br>del plan</th>
												<th class="borde_inferior" colspan="2" width="140px">Velocidad M&aacute;xima</th>
												<th class="borde_inferior" colspan="2" style="width:50px;">Velocidad Promedio Nacional</th>
												<th class="borde_inferior" colspan="2" style="width:50px;">Velocidad Promedio Internacional</th>
											</tr>
											<tr>
												<th width="70px;">Descarga</th>
												<th width="70px;">Subida</th>
												<th width="70px;">Descarga</th>
												<th width="70px;">Subida</th>
												<th width="70px;">Descarga</th>
												<th width="70px;">Subida</th>
											</tr>											
										 </table>
										 <div class="bottom"><span></span></div>
								 </div>	
							   </f:verbatim>
							 </div>	
							
							<table class="planes_contabla">
								<tbody>
									<!-- Planes Disponibles Agrupados -->
									<it:iterator var="velocidades" value="#{grupoPlan.tablaVelocidades}" rowIndexVar="iVelocidades">
										 <c:set var="style" value="#{iVelocidades % 2 == 0 ? 'color': ''}" scope="page" />
		
										 <tr class="<h:outputText value="#{style}"/>" id="filaPrimera<h:outputText value="#{velocidades.codbscs2}"/>">
												<th class="primera"  style="font-size: 0.9em; padding: 6px 5px;text-align: center;border-bottom:1px solid #E3E3E3;" width="60px" rowspan="2"><h:outputText value="#{velocidades.nombrePlan}"/></th>
												<th class="borde_inferior" colspan="2" style="font-size: 0.9em padding: 6px 5px;text-align: center;" > </th>
												<th class="borde_inferior" colspan="2" style="font-size: 0.9em;padding: 6px 5px;text-align: center; width:50px;"></th>
												<th class="borde_inferior" colspan="2" style="font-size: 0.9em;padding: 6px 5px;text-align: center; width:50px;"></th>
										</tr>
										 <tr class="<h:outputText value="#{style}"/>" id="filaSegunda<h:outputText value="#{velocidades.codbscs2}"/>">
												<th width="70px;" style="font-size: 0.9em;padding: 6px 5px;text-align: center;border-bottom:1px solid #E3E3E3;" width="70px;"><h:outputText value="#{velocidades.velMaxDescarga}"/></th>
												<th width="70px;" style="font-size: 0.9em;padding: 6px 5px;text-align: center;border-bottom:1px solid #E3E3E3;" width="70px;"><h:outputText value="#{velocidades.velMaxSubida}"/></th>
												<th width="70px;"style="font-size: 0.9em;padding: 6px 5px;text-align: center;border-bottom:1px solid #E3E3E3;"><h:outputText value="#{velocidades.velPromNacDescarga}"/></th>
												<th width="70px;"style="font-size: 0.9em;padding: 6px 5px;text-align: center;border-bottom:1px solid #E3E3E3;"><h:outputText value="#{velocidades.velPromNacSubida}"/></th>
												<th width="70px;"style="font-size: 0.9em;padding: 6px 5px;text-align: center;border-bottom:1px solid #E3E3E3;"><h:outputText value="#{velocidades.velPromInterDescarga}"/></th>
												<th width="70px;"style="font-size: 0.9em;padding: 6px 5px;text-align: center;border-bottom:1px solid #E3E3E3;"><h:outputText value="#{velocidades.velPromInterSubida}"/></th>
										</tr>			
								    </it:iterator>
								</tbody>			
							</table>
							<div class="disclaimer"></div>
							  
						</div>
						<!-- Fin contenido2 -->
						
						
					<div class="paso_2 normal">
							<!-- 
						<div class="nombre_plan">
							<div class="nombre_plan_top anchoFijo"><span></span></div>
							<div class="nombre_plan_main">Nombre del plan</div>
							<div class="nombre_plan_bottom anchoFijo"><span></span></div>
						</div>
							 -->
						
						<div class="paso_confirmar normal clearfix">
							<span class="plan"></span>				
							
							<span class="camPlan">Vas a cambiar a este plan</span>
							<a href="#" class="cancelar">Cancelar</a>
							<a href="#" class="btnVerdeDelgado confirma"><span>Confirmar</span></a>
							
							<a id="loadingCambioPlan"></a>
						</div>
						
						<div class="paso_confirmar advertencia clearfix">
				                <span class="plan"></span>
				                <br />
				                <br />
				                <div class="clearfix">
				                    <p><strong>Antes de continuar</strong><br /><br /><cm:getProperty node="${infoCondiciones[0]}" name="html" /></p>
				                </div>
				                <div class="condicionesCheck">           
				                    <input type="checkbox" class="aceptoCondiciones" />
				                    <label><strong>Acepto las condiciones</strong></label>
				                </div>
				                <div style="float:left"><a id="loadingCambioPlan"></a></div>
				                <div class="botonesAlternos clearfix">
				                    <a href="#" class="btnVerdeDelgado confirma" onclick="mxTracker._trackPageview('Personas/Mi Entel/Mis Productos/Telefonia/Plan o cambio de plan/<h:outputText value="#{grupoPlan.nombrePlanSinAcentos}"/>/Condiciones');"><span>Confirmar</span></a>
				                    <a href="#" class="cancelar">Cancelar</a>
				                </div>
				          </div>
						
						<div class="disclaimer">
							
								<!--  PLAN TARIFA PLANA - CC  -->
			 						<h:panelGroup rendered="#{((grupoPlan.tipoPlan eq tipoPlanBean.planTarifaPlanaCc))}">
			 						     <h:outputText escape="false" value="#{planController.planesCuentaControladaTarifaPlana}"/>
									</h:panelGroup>
				 				<!--  PLAN TARIFA PLANA - CC  -->
								
								<!--  PLAN RED - CC   -->
									<h:panelGroup rendered="#{(grupoPlan.tipoPlan eq tipoPlanBean.planRedCc)}">
										<h:outputText escape="false" value="#{planController.planesCuentaControladaTarifaRed}"/>
									</h:panelGroup>	
								<!--  PLAN RED - CC   -->
								
								<!--   PLAN GLOBAL - TARIFA UNICA FRECUENTE SS  ------------------------- -->
									<h:panelGroup rendered="#{((grupoPlan.tipoPlan eq tipoPlanBean.planTarifaUnicaFrec))}">
										<h:panelGroup rendered="#{(grupoPlan.mercadoPlan ne miEntelBean.siglaCuentaControlada)}">
											<cm:getProperty node="${infoCambioPlan[0]}" name="html" />
										</h:panelGroup>
										<h:panelGroup rendered="#{(grupoPlan.mercadoPlan eq miEntelBean.siglaCuentaControlada)}">
											<cm:getProperty node="${infoCambioPlanCC[0]}" name="html" />
										</h:panelGroup>
									</h:panelGroup>
								<!--   PLAN GLOBAL - TARIFA UNICA FRECUENTE SS  ------------------------- -->
								
								<!--   PLAN JOVEN - SS         -->
									<h:panelGroup rendered="#{((grupoPlan.tipoPlan eq tipoPlanBean.planJoven))}">
										<h:panelGroup rendered="#{(grupoPlan.mercadoPlan ne miEntelBean.siglaCuentaControlada)}">
											<cm:getProperty node="${infoCambioPlan[0]}" name="html" />
										</h:panelGroup>
										<h:panelGroup rendered="#{(grupoPlan.mercadoPlan eq miEntelBean.siglaCuentaControlada)}">
											<cm:getProperty node="${infoCambioPlanCC[0]}" name="html" />
										</h:panelGroup>
									</h:panelGroup>
								<!--   PLAN JOVEN - SS         -->
								
								<!--   PLAN FULL - SS          -->
									<h:panelGroup rendered="#{((grupoPlan.tipoPlan eq tipoPlanBean.planFull))}">
										<h:outputText escape="false" value="#{planController.planesFullRed}"/>
									</h:panelGroup>
								<!--   PLAN FULL - SS          -->
								
								<!--   PLAN TARIFA UNICA - SS  -->
									<h:panelGroup rendered="#{((grupoPlan.tipoPlan eq tipoPlanBean.planTarifaUnica))}">
										<h:outputText escape="false" value="#{planController.planesTarifaUnicaSinSMS}"/>
								    </h:panelGroup>
								<!--   PLAN TARIFA UNICA - SS  -->
								
								<!--  PLAN RED FIJA - SS CC     -->
									<h:panelGroup rendered="#{(grupoPlan.tipoPlan eq tipoPlanBean.planRedFija)}">
						<h:panelGroup rendered="#{(grupoPlan.mercadoPlan ne miEntelBean.siglaCuentaControlada)}">
							<cm:getProperty node="${infoCambioPlan[0]}" name="html" />
						</h:panelGroup>
						<h:panelGroup rendered="#{(grupoPlan.mercadoPlan eq miEntelBean.siglaCuentaControlada)}">
							<cm:getProperty node="${infoCambioPlanCC[0]}" name="html" />
						</h:panelGroup>
									</h:panelGroup>
								<!--  PLAN RED FIJA - SS CC     -->
								
								<!--  PLAN FAMILIA - SS  -->
									<h:panelGroup rendered="#{(grupoPlan.tipoPlan eq tipoPlanBean.planFamilia)}">
										<h:panelGroup rendered="#{(grupoPlan.mercadoPlan ne miEntelBean.siglaCuentaControlada)}">
											<cm:getProperty node="${infoCambioPlan[0]}" name="html" />
										</h:panelGroup>
										<h:panelGroup rendered="#{(grupoPlan.mercadoPlan eq miEntelBean.siglaCuentaControlada)}">
											<cm:getProperty node="${infoCambioPlanCC[0]}" name="html" />
										</h:panelGroup>
									</h:panelGroup>
								<!--  PLAN FAMILIA - SS  -->
						
								<!--  PLAN RED EMPRESA - SS  -->
									<h:panelGroup rendered="#{(grupoPlan.tipoPlan eq tipoPlanBean.planRedEmpresa)}">
										<h:panelGroup rendered="#{(grupoPlan.mercadoPlan ne miEntelBean.siglaCuentaControlada)}">
											<cm:getProperty node="${infoCambioPlan[0]}" name="html" />
										</h:panelGroup>
										<h:panelGroup rendered="#{(grupoPlan.mercadoPlan eq miEntelBean.siglaCuentaControlada)}">
											<cm:getProperty node="${infoCambioPlanCC[0]}" name="html" />
										</h:panelGroup>
									</h:panelGroup>
								<!--  PLAN RED EMPRESA - SS  -->
								
								<!--  PLAN MM IPHONE - SS  -->	
									<h:panelGroup rendered="#{(grupoPlan.tipoPlan eq tipoPlanBean.planMultimediaIphone)}">
										<h:panelGroup rendered="#{(grupoPlan.mercadoPlan ne miEntelBean.siglaCuentaControlada)}">
											<cm:getProperty node="${infoCambioPlan[0]}" name="html" />
										</h:panelGroup>
										<h:panelGroup rendered="#{(grupoPlan.mercadoPlan eq miEntelBean.siglaCuentaControlada)}">
											<cm:getProperty node="${infoCambioPlanCC[0]}" name="html" />
										</h:panelGroup>
									</h:panelGroup>
								<!--  PLAN MM IPHONE - SS  -->
								<!--  PLAN MULTIMEDIA - CC  -->
									<h:panelGroup rendered="#{(grupoPlan.tipoPlan eq tipoPlanBean.planMultimediaCc)}">	
										<h:outputText escape="false" value="#{planController.planesMultimediaCuentaControlada}"/>
									</h:panelGroup>	
								<!--  PLAN MULTIMEDIA - CC  -->
								
								<!--  PLAN MM RED - SS  -->	
									<h:panelGroup rendered="#{(grupoPlan.tipoPlan eq tipoPlanBean.planMultimediaCc)}">	
										<h:outputText escape="false" value="#{planController.planesMultimediaRed}"/>
									</h:panelGroup>	
								<!--  PLAN MM RED - SS  -->
								
								<!--  PLAN MM RED EXCEDIDO- SS  -->	
									<h:panelGroup rendered="#{(grupoPlan.tipoPlan eq tipoPlanBean.planMultimediaRedExcedido)}">	
										<h:outputText escape="false" value="#{planController.planesMultimediaRedExcedido}"/>
									</h:panelGroup>	
								<!--  PLAN MM RED EXCEDIDO - SS  -->
								
								<!--  PLAN MM RED EXCEDIDO TODO DESTINO - SS  -->	
									<h:panelGroup rendered="#{(grupoPlan.tipoPlan eq tipoPlanBean.planMultimediaRedExcedidoTodoDestino)}">	
										<h:outputText escape="false" value="#{planController.planesMultimediaRedExcedidoTodoDestino}"/>
									</h:panelGroup>	
								<!--  PLAN MM RED EXCEDIDO TODO DESTINO - SS  -->
								
								<!--  OTROS PLANES - SS  -->
								<h:panelGroup rendered="#{(grupoPlan.tipoPlan gt tipoPlanBean.planMultimediaRed && 
									                           grupoPlan.tipoPlan gt tipoPlanBean.planMultimediaRedExcedido &&
									                           grupoPlan.tipoPlan gt tipoPlanBean.planMultimediaRedExcedidoTodoDestino
									  )}">
										<h:panelGroup rendered="#{(grupoPlan.mercadoPlan ne miEntelBean.siglaCuentaControlada)}">
											<cm:getProperty node="${infoCambioPlan[0]}" name="html" />
										</h:panelGroup>
										<h:panelGroup rendered="#{(grupoPlan.mercadoPlan eq miEntelBean.siglaCuentaControlada)}">
											<cm:getProperty node="${infoCambioPlanCC[0]}" name="html" />
										</h:panelGroup>
								</h:panelGroup>
								<!--  OTROS PLANES - SS  -->
						
						</div>
					</div><!--/paso_2-->
					
					
					<div class="paso_3">
						<div class="nombre_plan">
			
							<div class="nombre_plan_top"><span></span></div>
							<div class="nombre_plan_main">Nombre del plan</div>
							<div class="nombre_plan_bottom"><span></span></div>
						</div>
						<div class="paso_confirmar clearfix">
							<span class="plan"></span>
							<div>
								<h5>Has cambiado a este plan</h5>
								<div>
									<h:panelGroup rendered="#{(grupoPlan.mercadoPlan ne miEntelBean.siglaCuentaControlada)}">
										<cm:getProperty node="${infoEfectivoCambio[0]}" name="html" />
									</h:panelGroup>
									<h:panelGroup rendered="#{(grupoPlan.mercadoPlan eq miEntelBean.siglaCuentaControlada)}">
										<cm:getProperty node="${infoEfectivoCambioCC[0]}" name="html" />
									</h:panelGroup>
								</div>
							</div>
						</div>
		
						<div class="disclaimer">
								<!--  PLAN TARIFA PLANA - CC  -->
			 						<h:panelGroup rendered="#{((grupoPlan.tipoPlan eq tipoPlanBean.planTarifaPlanaCc))}">
			 						     <h:outputText escape="false" value="#{planController.planesCuentaControladaTarifaPlana}"/>
									</h:panelGroup>
				 				<!--  PLAN TARIFA PLANA - CC  -->
								
								<!--  PLAN RED - CC   -->
									<h:panelGroup rendered="#{(grupoPlan.tipoPlan eq tipoPlanBean.planRedCc)}">
										<h:outputText escape="false" value="#{planController.planesCuentaControladaTarifaRed}"/>
									</h:panelGroup>	
								<!--  PLAN RED - CC   -->
								
								<!--   PLAN GLOBAL - TARIFA UNICA FRECUENTE SS  ------------------------- -->
									<h:panelGroup rendered="#{((grupoPlan.tipoPlan eq tipoPlanBean.planTarifaUnicaFrec))}">
										<h:panelGroup rendered="#{(grupoPlan.mercadoPlan ne miEntelBean.siglaCuentaControlada)}">
											<cm:getProperty node="${infoCambioPlan[0]}" name="html" />
										</h:panelGroup>
										<h:panelGroup rendered="#{(grupoPlan.mercadoPlan eq miEntelBean.siglaCuentaControlada)}">
											<cm:getProperty node="${infoCambioPlanCC[0]}" name="html" />
										</h:panelGroup>
									</h:panelGroup>
								<!--   PLAN GLOBAL - TARIFA UNICA FRECUENTE SS  ------------------------- -->
								
								<!--   PLAN JOVEN - SS         -->
									<h:panelGroup rendered="#{((grupoPlan.tipoPlan eq tipoPlanBean.planJoven))}">
										<h:panelGroup rendered="#{(grupoPlan.mercadoPlan ne miEntelBean.siglaCuentaControlada)}">
											<cm:getProperty node="${infoCambioPlan[0]}" name="html" />
										</h:panelGroup>
										<h:panelGroup rendered="#{(grupoPlan.mercadoPlan eq miEntelBean.siglaCuentaControlada)}">
											<cm:getProperty node="${infoCambioPlanCC[0]}" name="html" />
										</h:panelGroup>
									</h:panelGroup>
								<!--   PLAN JOVEN - SS         -->
								
								<!--   PLAN FULL - SS          -->
									<h:panelGroup rendered="#{((grupoPlan.tipoPlan eq tipoPlanBean.planFull))}">
										<h:outputText escape="false" value="#{planController.planesFullRed}"/>
									</h:panelGroup>
								<!--   PLAN FULL - SS          -->
								
								<!--   PLAN TARIFA UNICA - SS  -->
									<h:panelGroup rendered="#{((grupoPlan.tipoPlan eq tipoPlanBean.planTarifaUnica))}">
										<h:outputText escape="false" value="#{planController.planesTarifaUnicaSinSMS}"/>
								    </h:panelGroup>
								<!--   PLAN TARIFA UNICA - SS  -->
								
								<!--  PLAN RED FIJA - SS CC     -->
									<h:panelGroup rendered="#{(grupoPlan.tipoPlan eq tipoPlanBean.planRedFija)}">
										<h:panelGroup rendered="#{(grupoPlan.mercadoPlan ne miEntelBean.siglaCuentaControlada)}">
											<cm:getProperty node="${infoCambioPlan[0]}" name="html" />
										</h:panelGroup>
										<h:panelGroup rendered="#{(grupoPlan.mercadoPlan eq miEntelBean.siglaCuentaControlada)}">
											<cm:getProperty node="${infoCambioPlanCC[0]}" name="html" />
										</h:panelGroup>
									</h:panelGroup>
								<!--  PLAN RED FIJA - SS CC     -->
								
								<!--  PLAN FAMILIA - SS  -->
									<h:panelGroup rendered="#{(grupoPlan.tipoPlan eq tipoPlanBean.planFamilia)}">
										<h:panelGroup rendered="#{(grupoPlan.mercadoPlan ne miEntelBean.siglaCuentaControlada)}">
											<cm:getProperty node="${infoCambioPlan[0]}" name="html" />
										</h:panelGroup>
										<h:panelGroup rendered="#{(grupoPlan.mercadoPlan eq miEntelBean.siglaCuentaControlada)}">
											<cm:getProperty node="${infoCambioPlanCC[0]}" name="html" />
										</h:panelGroup>
									</h:panelGroup>
								<!--  PLAN FAMILIA - SS  -->
								
								<!--  PLAN RED EMPRESA - SS  -->
									<h:panelGroup rendered="#{(grupoPlan.tipoPlan eq tipoPlanBean.planRedEmpresa)}">
										<h:panelGroup rendered="#{(grupoPlan.mercadoPlan ne miEntelBean.siglaCuentaControlada)}">
											<cm:getProperty node="${infoCambioPlan[0]}" name="html" />
										</h:panelGroup>
										<h:panelGroup rendered="#{(grupoPlan.mercadoPlan eq miEntelBean.siglaCuentaControlada)}">
											<cm:getProperty node="${infoCambioPlanCC[0]}" name="html" />
										</h:panelGroup>
									</h:panelGroup>
								<!--  PLAN RED EMPRESA - SS  -->
								
								<!--  PLAN MM IPHONE - SS  -->	
									<h:panelGroup rendered="#{(grupoPlan.tipoPlan eq tipoPlanBean.planMultimediaIphone)}">
										<h:panelGroup rendered="#{(grupoPlan.mercadoPlan ne miEntelBean.siglaCuentaControlada)}">
											<cm:getProperty node="${infoCambioPlan[0]}" name="html" />
										</h:panelGroup>
										<h:panelGroup rendered="#{(grupoPlan.mercadoPlan eq miEntelBean.siglaCuentaControlada)}">
											<cm:getProperty node="${infoCambioPlanCC[0]}" name="html" />
										</h:panelGroup>
									</h:panelGroup>
								<!--  PLAN MM IPHONE - SS  -->
								<!--  PLAN MULTIMEDIA - CC  -->
									<h:panelGroup rendered="#{(grupoPlan.tipoPlan eq tipoPlanBean.planMultimediaCc)}">	
										<h:outputText escape="false" value="#{planController.planesMultimediaCuentaControlada}"/>
									</h:panelGroup>	
								<!--  PLAN MULTIMEDIA - CC  -->
								
								<!--  PLAN MM RED - SS  -->	
									<h:panelGroup rendered="#{(grupoPlan.tipoPlan eq tipoPlanBean.planMultimediaCc)}">	
										<h:outputText escape="false" value="#{planController.planesMultimediaRed}"/>
									</h:panelGroup>	
								<!--  PLAN MM RED - SS  -->
								
								<!--  PLAN MM RED EXCEDIDO- SS  -->	
									<h:panelGroup rendered="#{(grupoPlan.tipoPlan eq tipoPlanBean.planMultimediaRedExcedido)}">	
										<h:outputText escape="false" value="#{planController.planesMultimediaRedExcedido}"/>
									</h:panelGroup>	
								<!--  PLAN MM RED EXCEDIDO - SS  -->
								
								<!--  PLAN MM RED EXCEDIDO TODO DESTINO - SS  -->									    
									<h:panelGroup rendered="#{(grupoPlan.tipoPlan eq tipoPlanBean.planMultimediaRedExcedidoTodoDestino)}">	
										<h:outputText escape="false" value="#{planController.planesMultimediaRedExcedidoTodoDestino}"/>
									</h:panelGroup>	
								<!--  PLAN MM RED EXCEDIDO TODO DESTINO - SS  -->
								
								<!--  OTROS PLANES - SS  -->
									<h:panelGroup rendered="#{(grupoPlan.tipoPlan gt tipoPlanBean.planMultimediaRed &&
															   grupoPlan.tipoPlan gt tipoPlanBean.planMultimediaRedExcedido &&
															   grupoPlan.tipoPlan gt tipoPlanBean.planMultimediaRedExcedidoTodoDestino )}">
									
										<h:panelGroup rendered="#{(grupoPlan.mercadoPlan ne miEntelBean.siglaCuentaControlada)}">
											<cm:getProperty node="${infoCambioPlan[0]}" name="html" />
										</h:panelGroup>
										<h:panelGroup rendered="#{(grupoPlan.mercadoPlan eq miEntelBean.siglaCuentaControlada)}">
											<cm:getProperty node="${infoCambioPlanCC[0]}" name="html" />
										</h:panelGroup>
									</h:panelGroup>
								
																				
							</div>
						</div><!--/paso_3-->
						
						<div class="tarifa_unica_con_SMS" style="display: none;"><h:outputText escape="false" value="#{planController.planesTarifaUnicaConSMS}"/></div>
						<div class="tarifa_unica_sin_SMS" style="display: none;"><h:outputText escape="false" value="#{planController.planesTarifaUnicaSinSMS}"/></div>
						</div>
					
				</div>
			</div>
			<!-- /bolsa -->	
				
			</it:iterator>
			
		</div>	
		
		<script type="text/javascript">
		 /* variables globales para controlar el mensaje de advertencia cambio de plan  */
		  var planOrigen  = "";
		  var planDestino = "";
		  var planesMdia  = ""; 
		  
			$(document).ready(function(){ 
				initMenuDesplegablePlanes();
				$('div.paso_2 a.confirma').click(function() {
					
					var tr = $(this).parents('.lista-bolsas:first');
					var codbscs2 = $('input.codbscs2').val();
					var nombreNuevoPlan = $('input.nombreNuevoPlan').val();
					var valorNuevoPlan = $('input.valorNuevoPlan').val();
					var url='<%=request.getContextPath()%>/portlet/plan/cambioPlanSSCCJson.faces';
					tr.find('span.camPlan').hide();
					tr.find('a.cancelar').hide(); 
					tr.find('a.confirma').hide();

					   //Loading 
					   tr.find('a#loadingCambioPlan').html("&nbsp;&nbsp;<img width='130px' src='../framework/skins/mientel/img/thickbox/TB_cargando.gif'/>&nbsp;&nbsp;<b>Enviando...</b>");

						   $.ajax({
								type: 'POST',
								url: url,
								async: false,
								dataType: 'json',
								data: {codigoNuevoPlan:codbscs2, nombreNuevoPlan:nombreNuevoPlan, valorNuevoPlan:valorNuevoPlan},
								success: function (resp){
									 if(resp.estado == 'Ok'){
										 
										 tr.find('.paso_2').hide();
										 tr.find('.paso_3').show();

										 planOrigen =  planDestino ; //Se hace el cambio del Origen/Actual por el plan Escogido/Destino.

										 crearMarcaTransaccionGTM(resp);
										 
									 }else{
										 tr.find('a#loadingCambioPlan').html('');
										 tr.find('a#loadingCambioPlan').find('div.paso_confirmar').find('div:first').html('<h5>'+resp.detalle+'</h5><div>- Tu solicitud ha sido efectuada.</div>');
										 tr.find('.paso_2').show();
										 tr.find('.paso_3').hide();
									 }
							}
					   });

					return false;
				});

					
				    planOrigen  = "<h:outputText value='#{planController.planOrigen}' />";						
					planesMdia  = "<h:outputText value='#{planController.multimedia}' />";			      				

					
					
					//$('.contenido1').css({'display': 'block'});
					
					return false;

					$('div.linea_tabs').find('div.tab.seleccionado').trigger('click');
						      				
			});

			function crearMarcaTransaccionGTM(resp) {
				var idTransaccion = resp.respuesta.idTransaccion;
				var codigoProducto = resp.respuesta.skuID;
				var nombreProducto = resp.respuesta.nombre;

				mxTracker._trackPageview('Personas/Mi Entel/Mis Productos/Telefonia/Plan o cambio de plan/' + nombreProducto + '/Conversion');

				mxTracker._addTrans(idTransaccion, '', resp.respuesta.valorTransaccion);
				mxTracker._addItem(idTransaccion, codigoProducto, nombreProducto, 'Fee plan', resp.respuesta.nuevoValor, '1');
				mxTracker._addItem(idTransaccion, codigoProducto, nombreProducto, 'Costo cambio plan', resp.respuesta.costoOperacional, '1');

				dataLayer = dataLayer||[];
				dataLayer.push({'event': 'tracktrans', 'tracktrans': true});
			}
		</script>

	
	</h:panelGroup>	
			
	<!-- MENSAJES -->
	<jsp:include page="../common/messages_table.jsp"></jsp:include>

</f:view>
