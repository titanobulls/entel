<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="f"  uri="http://java.sun.com/jsf/core"%>
<%@ taglib prefix="h"  uri="http://java.sun.com/jsf/html"%>
<%@ taglib prefix="it"  uri="/WEB-INF/tld/IteratorTag.tld"%>
<%@ taglib prefix="cm" uri="http://www.bea.com/servers/portal/tags/content" %>
<%@ taglib prefix="pref" uri="http://www.bea.com/servers/portal/tags/netuix/preferences" %>
<%@ taglib prefix="r"  uri="http://www.bea.com/servers/portal/tags/netuix/render"%>
<cm:search id="infoPlanTraficoAnterior" query="idContenido = 'infoPlanTraficoAnterior'" useCache="true"  />
<cm:search id="mensaje_error_plan_mm_excedido" query="idContenido = 'mensaje_error_plan_mm_excedido'" useCache="true"  />
<cm:search id="link_historial_alertas" query="idContenido = 'link_historial_alertas'" useCache="true"  />

<f:view beforePhase="#{miTraficoController.init}">

<script type="text/javascript">

$(document).ready(function() {
	initMenuDesplegableTrafico();
	$("#mensajePlanesAnteriores").hide();
	
	if(<h:outputText value="#{miTraficoController.tienePlanAnteriores}"/>){
		$(".contenedor_central").prepend($("#mensajePlanesAnteriores").html());
	}

	var periodo = obtenerParametroURL("periodo");
	if (periodo == '1') {
		$('#centro').find('h1:first').html('Trafico Mes Anterior');
	}
});

function obtenerParametroURL(name) {
	var regexS = "[\\?&]"+name+"=([^&#]*)";
	var regex = new RegExp(regexS);
	var tmpURL = window.location.href;
	var results = regex.exec(tmpURL);

	if (results == null) {
		return "" ;
	} else {
		return results[1];
	}
}

</script>

<!-- MENSAJES -->
<jsp:include page="../common/messages_table.jsp"></jsp:include>



<h:panelGroup binding="#{miTraficoController.panelTrafico}">
<div class="estructuraTrafico">

	<div class="clearfix">
		<h2 class="ico_grafico">
			<strong>Mi Tr�fico Plan Vigente</strong> (Desde el  <h:outputText value="#{miTraficoController.detalleMiTraficoPlanVigente.fecha}">
							<f:convertDateTime pattern="dd/MM/yyyy"/></h:outputText>)
			<h:panelGroup rendered="#{miTraficoController.periodoActual}">
				<a id="traficoMesActual"  href="<r:pageUrl pageLabel="${miTraficoController.currentPageLabel}"></r:pageUrl>&periodo=1" onclick="mxTracker._trackPageview('Personas/Mi Entel/Mis Productos/Telefonia/Trafico/Mes Anterior');">Tr&aacute;fico Mes Anterior</a>
			</h:panelGroup>
			<h:panelGroup rendered="#{!miTraficoController.periodoActual}">
				<a id="traficoMesActual"  href="<r:pageUrl pageLabel="${miTraficoController.currentPageLabel}"></r:pageUrl>">Tr&aacute;fico Mes Actual</a>
			</h:panelGroup>				
		</h2>
	</div>
						
	<!-- Tabla Voz -->
	<div class="tabla">	
		<div class="header_tabla trafico clearfix">
			<div class="top"><span></span></div>
			<div class="main trafico">
				<table>
					<tr>
						<th width="160" rowspan="2"><strong>VOZ (Minutos)</strong></th>
						<th rowspan="2">Minutos utilizados</th>
						<th width="240" colspan="2" class="bordeInferior">Tr&aacute;fico seg&uacute;n plan</th>
					</tr>
					<tr>
					<it:iterator rowIndexVar="index" var="row" value="#{miTraficoController.detalleMiTraficoPlanVigente.traficoVoz.detallePorHorario}">
						<th><h:outputText value="#{row.descripcion}"/></th>
				    </it:iterator>
					</tr>
				</table>					
			</div>

			<div class="bottom"><span></span></div>
		</div>
		<div class="contenido_tabla">
			<table>
				<tbody>
					<tr>
						<td width="160">Hasta el <h:outputText value="#{miTraficoController.detalleMiTraficoPlanVigente.traficoVoz.fechaFinal}">
						<f:convertDateTime pattern="dd/MM/yyyy"/>
						</h:outputText></td>
						<td><h:outputText value="#{miTraficoController.detalleMiTraficoPlanVigente.traficoVoz.total}" converter="traficoVozMinSecConverter"/></td>
						 <it:iterator var="row" value="#{miTraficoController.detalleMiTraficoPlanVigente.traficoVoz.detallePorHorario}">
						 <td><h:outputText value="#{row.consumo}" converter="traficoVozMinSecConverter"/></td>
				        </it:iterator>
					</tr>
				</tbody>
			</table>
		</div>
	</div><!--/tabla-->	
						
	<!-- Tabla Datos -->

 <h:panelGroup rendered="#{miTraficoController.planesMultimediaBean == null && miTraficoController.errorPlanMMexcedido}">
    <div class="tabla">
		<div class="header_tabla trafico clearfix">
			<div class="top"><span></span></div>
			<div class="main trafico">
				<table>
					<tr>
						<th width="130"><strong>DATOS (Megabytes)</strong></th>
						<it:iterator var="row" value="#{miTraficoController.detalleMiTraficoPlanVigente.traficoDatos}">
						<th width="95"><h:outputText value="#{row.tipo}"/></th>
                                       </it:iterator>
					</tr>
				</table>					
			</div>
			<div class="bottom"><span></span></div>
		</div>
		<div class="contenido_tabla">

			<table>
				<tbody>
					<tr>
						<it:iterator var="row" rowIndexVar="rowIndex" value="#{miTraficoController.detalleMiTraficoPlanVigente.traficoDatos}">
                                       <h:panelGroup rendered="#{rowIndex == 0}"><td width="140">Hasta el <h:outputText value="#{row.fechaFinal}">
						<f:convertDateTime pattern="dd/MM/yyyy"/>
						</h:outputText></td></h:panelGroup>
						<td width="105"><h:outputText value="#{row.totalFormated}" /> MB</td>
                                       </it:iterator>
						
					</tr>
				</tbody>
			</table>
		</div>
	</div><!--/tabla-->	
</h:panelGroup>						
	<!-- Tabla Mensajes -->
	<div class="tabla">	
		<div class="header_tabla trafico clearfix">
			<div class="top"><span></span></div>
			<div class="main trafico">

				<table>
					<tr>
						<th width="160"><strong>MENSAJES</strong></th>
				      	<th width="243">MMS y SMS</th>
						<th>OTROS</th>
                                  </tr>
				</table>					
			</div>

			<div class="bottom"><span></span></div>
		</div>
		<div class="contenido_tabla">
			<table>
				<tbody>
					<tr>
						<it:iterator var="row" rowIndexVar="rowIndex" value="#{miTraficoController.detalleMiTraficoPlanVigente.traficoMensajes}">
						    <h:panelGroup rendered="#{rowIndex == 0}"><td width="190">Hasta&nbsp;el&nbsp;<h:outputText value="#{row.fechaFinal}">
						<f:convertDateTime pattern="dd/MM/yyyy"/>
						</h:outputText></td></h:panelGroup>
	 					<td width="243"><h:outputText value="#{row.total}" converter="javax.faces.Integer"/>
                                       </it:iterator>
			
					</tr>
				</tbody>
			</table>
		</div>
	</div><!--/tabla-->	
						
</div>
<h:panelGroup rendered="#{miTraficoController.errorPlanMMexcedido}">
	<h:panelGroup rendered="#{miTraficoController.planesMultimediaBean != null}">
	<div class="estructuraTrafico" style="padding-top:0px;">
	
		<div class="tabla" style="padding: 10px 0 8px;">	
			<div class="header_tabla trafico clearfix">
				<div class="top"><span></span></div>
				<div class="main trafico">
	
					<table>
						<tr>
							<th width="100%" style="text-align: left;padding-left: 30px !important;"><strong>DATOS</strong> incuidos en tu plan : <strong><h:outputText value="#{miTraficoController.planesMultimediaBean.valorTotalTrafico}"/></strong> MB</th>				      
	                    </tr>
					</table>					
				</div>
				<div class="bottom"><span></span></div>
			</div>
		</div>
	    <div class="db_tabla_interna" style="border-bottom:1px solid #E3E3E3;">
			      <div class="dbn_tabla_interna_barra clearfix">												
					   <div class="flotar_izq" style="width:245px;font-size:1.5em;">
			                <span class="db_texto_fecha_detalle_trafico" id="datos_disponibles" ><h:outputText value="#{miTraficoController.planesMultimediaBean.cuotaTraficoUtil}"/> <strong>MB</strong> al <h:outputText value="#{miTraficoController.planesMultimediaBean.fechaDiaMesHoraFormat}"/></span>                                          
					   </div>
					   <div class="flotar_der" style="width:300px;">
							<img src="../framework/skins/mientel/img/trafico/barras/img_barra_naranja_grande_<h:outputText value="#{miTraficoController.planesMultimediaBean.promConsumoSinDec}"/>.png"  width="298" height="15">
			                     <p class="dbn_texto_porcent_detalle_trafico clearfix" style="font-size:12px !important;"><span ><h:outputText value="#{miTraficoController.planesMultimediaBean.promConsumo}"/> % </span>consumido</p>                                           
						</div>		                     
				 </div>
		 </div>
		 <h:panelGroup rendered="#{!miTraficoController.planesMultimediaBean.tieneValorExcedido}">
			 <div class="dbn_row_comp_detalle_trafico" style="margin-top:10px;">
				 <span>El valor de cada <strong>MB</strong> adicional es de $<h:outputText value="#{miTraficoController.planesMultimediaBean.valorMBExcedido}"/></span> 
			 </div>	
		 </h:panelGroup> 
		 <h:panelGroup rendered="#{miTraficoController.planesMultimediaBean.tieneValorExcedido}">
			 <div class="dbn_row_comp_detalle_trafico"  style="margin-top:10px;">
				 <span>Datos Excedidos <h:outputText value="#{miTraficoController.planesMultimediaBean.traficoExcedido}"/> MB al <h:outputText value="#{miTraficoController.planesMultimediaBean.fechaDiaMesHoraFormat}"/> &emsp;&emsp;&emsp;&emsp; El valor de cada <strong>MB</strong> adicional es de $<h:outputText value="#{miTraficoController.planesMultimediaBean.valorMBExcedido}"/></span> 
			 </div>
		 </h:panelGroup>	 
		 <div class="dbn_row_consideracion">
			<p><span >Consideraci&oacute;n : </span><h:outputText value="#{miTraficoController.planesMultimediaBean.condProrrateo}"/> </p>
		</div>					
		<div class="dbn_row_mensaje_amarillo">
			<p><cm:getProperty node="${link_historial_alertas[0]}" name="html" /></p>
		</div>
	</div>	
	</h:panelGroup>	
	<br/>
	<br/>
</h:panelGroup>	
<h:panelGroup rendered="#{!miTraficoController.errorPlanMMexcedido}">
 <div id="mensaje_error_mm_excedido" class="mensaje-alerta-sistema-pequeno">		      
		  	   <div id="alerta-tabla-mensaje" style="display: block;"><cm:getProperty node="${mensaje_error_plan_mm_excedido[0]}" name="html" /></div>		  	  
 </div>	
 <br/>
 <br/>
</h:panelGroup>	
 				
<h:panelGroup binding="#{miTraficoController.panelTraficoAnterior}">					

<!-- TRAFICOS ANTERIORES -->

    <h1>Plan y Tr�fico anterior</h1>
	<div style="float: left; margin-left: 0;">
	
	<div id="menu-desplegable" style="float: none;">

		<div class="trafico-anterior-titulo">
			<it:iterator var="trafico" rowIndexVar="index" value="#{miTraficoController.listDetalleMiTraficoAnteriores}">
            <a href="javascript:;" class="btn-trafico-anterior">Ver tr�fico plan anterior (Desde  
            	<h:outputText value="#{trafico.fecha}">
					<f:convertDateTime pattern="dd/MM/yyyy"/>
				</h:outputText> al 
				<h:outputText value="#{trafico.fechaFin}">
					<f:convertDateTime pattern="dd/MM/yyyy"/>
				</h:outputText>)</a>
                
                <div style="display: none;" class="lista-trafico-anterior">
				<!-- ############################################### -->
      
      <!-- PLAN ANTERIOR -->
     	<!-- ESTRUCTURA TARIFARIA -->
					<div class="estructuraTarifaria">
						<h2 class="superchip">
							<strong>Plan anterior:</strong> <h:outputText value="#{trafico.planBean.nombrePlan}" />: <h:outputText value="#{miTraficoController.descripcionPlanTrafico}"/>
						</h2>	
							
						<!-- Tabla Tarificacion -->
						<div class="tabla">	
							<div class="planes_header_tabla clearfix">
								<div class="top"><span></span></div>
								<div class="main plan">
									<table>
										<tbody>
										
											<!--   ESTRUCTURA DEL CONTENIDO SUPERIOR DE LA TABLA - COLUMNAS   -->
				
										<tr>
											<th rowspan="2" width="30%"><b>Cargo fijo</b></th>
											
											<!-- PLAN TARIFA PLANA - SS CC -->
												<!-- TipoPlanNormal SS -->
													<h:panelGroup rendered="#{(trafico.planBean.tipoPlan eq tipoPlanBean.planTarifaPlanaSs) and
													(trafico.planBean.tipoMercado eq miEntelBean.siglaSuscripcion)}">
														<th rowspan="2"><b>Minutos Incluidos</b></th>
														<th class="borde_inferior" colspan="2"><b>Valor por minuto Adicional</b></th>
													</h:panelGroup>
												<!-- TipoPlanNormal SS -->
						
												
												<!-- TipoPlanNormal CC -->
													<h:panelGroup rendered="#{(trafico.planBean.tipoPlan eq tipoPlanBean.planTarifaPlanaCc) and
													(trafico.planBean.tipoMercado eq miEntelBean.siglaCuentaControlada)}">
														<th class="borde_inferior" colspan="2"><b>Valor por minuto Adicional</b></th>
													</h:panelGroup>
												<!-- TipoPlanNormal CC -->
											<!-- PLAN TARIFA PLANA - SS CC  -->
											
											
											<!-- PLAN RED - SS CC -->
												<!-- TipoPlanRed SS -->
														<h:panelGroup rendered="#{(trafico.planBean.tipoPlan eq tipoPlanBean.planRedSs) and
														(trafico.planBean.tipoMercado eq miEntelBean.siglaSuscripcion)}">
															
															<h:panelGroup rendered="#{(trafico.planBean.totalMinutosAdicional ne tipoPlanBean.flagHidden)}">
																<th class="borde_inferior" colspan="2"><b>Incluido<b></th>
																<th class="borde_inferior" colspan="4"><b>Adicional</b></th>
															</h:panelGroup>
															<h:panelGroup rendered="#{(trafico.planBean.totalMinutosAdicional eq tipoPlanBean.flagHidden)}">
																<th class="borde_inferior" colspan="1"><b>Incluido<b></th>
																<th class="borde_inferior" colspan="4"><b>Adicional</b></th>
															</h:panelGroup>
														</h:panelGroup>
												<!-- TipoPlanRed SS -->
												
												
												<!-- TipoPlanRed CC -->
														<h:panelGroup rendered="#{(trafico.planBean.tipoPlan eq tipoPlanBean.planRedCc) and
														(trafico.planBean.tipoMercado eq miEntelBean.siglaCuentaControlada)}">
															<th class="borde_inferior" colspan="2"><b>Valor por Minuto</b></th>
														</h:panelGroup>
												<!-- TipoPlanRed CC -->
											<!-- PLAN RED - SS CC -->
											
											
											<!--   PLAN GLOBAL - TARIFA UNICA FRECUENTE SS  -->
												<h:panelGroup rendered="#{(trafico.planBean.tipoPlan eq tipoPlanBean.planTarifaUnicaFrec)}">
														<th class="borde_inferior" colspan="2"><b>Minutos incluidos</b></th>
														<th class="borde_inferior" colspan="2"><b>Valor por Minuto<b></th>
												</h:panelGroup>
											<!--  PLAN GLOBAL - TARIFA UNICA FRECUENTE SS  -->
											
				
											<!--   PLAN JOVEN - SS     -->
											<!--   PLAN FULL - SS      -->
											<!--   PLAN TARIFA UNICA - SS  -->
												<h:panelGroup rendered="#{(trafico.planBean.tipoPlan eq tipoPlanBean.planJoven or 
													trafico.planBean.tipoPlan eq tipoPlanBean.planFull or 
													trafico.planBean.tipoPlan eq tipoPlanBean.planTarifaUnica)}">
												
													<th rowspan="2"><b>Minutos incluidos</b></th>
													 <it:iterator value="#{trafico.planBean.listaTasaciones}" var="tasacion" rowIndexVar="tasacionIndex">
															<th><b><h:outputText value="#{tasacion.nombre}"/></b></th>
													</it:iterator>
													
												</h:panelGroup>
											<!--   PLAN JOVEN - SS     -->
											<!--   PLAN FULL - SS      -->
											<!--   PLAN TARIFA UNICA - SS  -->
											
				
											<!--  PLAN RED FIJA - SS CC -->
											<!-- TipoPlanRedFija SS-->
													<h:panelGroup rendered="#{(trafico.planBean.tipoPlan eq tipoPlanBean.planRedFija) and
													(trafico.planBean.tipoMercado eq miEntelBean.siglaSuscripcion)}">
														<th class="borde_inferior" colspan="1"><b>Incluido<b></th>
														<th class="borde_inferior" colspan="5"><b>Adicional</b></th>
													</h:panelGroup>
											<!-- TipoPlanRedFija SS-->
											
											<!-- TipoPlanRedFijaCC-->
													<h:panelGroup rendered="#{(trafico.planBean.tipoPlan eq tipoPlanBean.planRedFija) and
													(trafico.planBean.tipoMercado eq miEntelBean.siglaCuentaControlada)}">
														<th class="borde_inferior" colspan="1"><b>Incluido<b></th>
														<th class="borde_inferior" colspan="5"><b>Adicional</b></th>
													</h:panelGroup>
											<!-- TipoPlanRedFijaCC-->
											<!--  PLAN RED FIJA - SS CC -->
											
				
											<!--  PLAN FAMILIA - SS  -->
												<h:panelGroup rendered="#{(trafico.planBean.tipoPlan eq tipoPlanBean.planFamilia)}">
					
													 <it:iterator value="#{trafico.planBean.listaTasaciones}" var="tasacion" rowIndexVar="tasacionIndex">
														<h:panelGroup rendered="#{(tasacionIndex ge 1) and (tasacionIndex le 2) }">
														<th><b><h:outputText value="#{tasacion.nombre}"/></b></th>
														</h:panelGroup>
													</it:iterator>
					
													<it:iterator value="#{trafico.planBean.listaTasaciones}" var="tasacion" rowIndexVar="tasacionIndex">
														<h:panelGroup rendered="#{tasacionIndex eq 3}">
														<th><b><h:outputText value="#{tasacion.nombre}"/></b></th>
														</h:panelGroup>
													</it:iterator>
													
													<it:iterator value="#{trafico.planBean.listaTasaciones}" var="tasacion" rowIndexVar="tasacionIndex">
														<h:panelGroup rendered="#{tasacionIndex eq 0}">
														<th><b><h:outputText value="#{tasacion.nombre}"/></b></th>
														</h:panelGroup>
													</it:iterator>
												</h:panelGroup>
											<!--  PLAN FAMILIA - SS  -->
											
											<!--  PLAN RED EMPRESA - SS  -->
												<h:panelGroup rendered="#{(trafico.planBean.tipoPlan eq tipoPlanBean.planRedEmpresa)}">
													<th rowspan="2">A m&oacute;viles Entel</th>
													 <it:iterator value="#{trafico.planBean.listaTasaciones}" var="tasacion">
														<th><b><h:outputText value="#{tasacion.nombre}"/></b></th>
													</it:iterator>
												</h:panelGroup>
											<!--  PLAN RED EMPRESA - SS -->
											
											
											<!--  PLAN MULTIMEDIA - CC -->
												<h:panelGroup rendered="#{(trafico.planBean.tipoPlan eq tipoPlanBean.planMultimediaCc)}">
													<th class="borde_inferior" colspan="2"><b>Incluido<b></th>
													<th class="borde_inferior" colspan="3"><b>Tarifas</b></th>
												</h:panelGroup>
											<!--  PLAN MULTIMEDIA - CC -->
											
											
											<!--  PLAN MULTIMEDIA IPHONE - SS  -->
												<h:panelGroup rendered="#{(trafico.planBean.tipoPlan eq tipoPlanBean.planMultimediaIphone)}">
												<h:panelGroup rendered="#{(trafico.planBean.totalMinutosAdicional ne tipoPlanBean.flagHidden)}">
													<th class="borde_inferior" colspan="4"><b>Incluido<b></th>
													<th class="borde_inferior" colspan="4"><b>Adicional</b></th>
												</h:panelGroup>
												<h:panelGroup rendered="#{(trafico.planBean.totalMinutosAdicional eq tipoPlanBean.flagHidden)}">
													<th class="borde_inferior" colspan="3"><b>Incluido<b></th>
													<th class="borde_inferior" colspan="3"><b>Adicional</b></th>
												</h:panelGroup>
												
												</h:panelGroup>
											<!--  PLAN MULTIMEDIA IPHONE - SS  -->
											
											<!--  PLAN MULTIMEDIA RED - SS  -->
												<h:panelGroup rendered="#{(trafico.planBean.tipoPlan eq tipoPlanBean.planMultimediaRed)}">
													<th class="borde_inferior" colspan="4"><b>Incluido<b></th>
													<th class="borde_inferior" colspan="4"><b>Adicional</b></th>
												</h:panelGroup>
											<!--  PLAN MULTIMEDIA RED - SS  -->
											
							<!-- --------------------------------------------------- INICIO -------------------------------------------------------------------------- -->			
						
								<!--  ESTRUCTURA TARIFARIA Planes Empresa -->
								<h:panelGroup rendered="#{(trafico.planBean.tipoPlan eq tipoPlanBean.planEmpresa ) and
							(trafico.cargarCargoFijo eq '1' )}">
							
							<h:panelGroup rendered="#{trafico.planBean.totalMinutos ne '0'}">
							  <it:iterator value="#{trafico.planBean.listaTasaciones}" var="tasaciones" rowIndexVar="tasacionIndex">
										<h:panelGroup rendered="#{(tasacionIndex eq 0) and (tasaciones.valor eq 0) }">
										<th class="borde_inferior" colspan="2"><b>Incluidos<b></th>
										</h:panelGroup>
							  </it:iterator>				
							
							<it:iterator value="#{trafico.planBean.listaTasaciones}" var="tasaciones" rowIndexVar="tasacionIndex">
										<h:panelGroup rendered="#{(tasacionIndex eq 0) and (tasaciones.valor ne 0) }">
										<th class="borde_inferior" colspan="3"><b>Incluidos<b></th>
										<th class="borde_inferior" colspan="4"><b>Tarifas adicionales</b></th>
										</h:panelGroup>
							  </it:iterator>
							  </h:panelGroup>				
							
							<h:panelGroup rendered="#{trafico.planBean.totalMinutos eq '0'}">
							<it:iterator value="#{trafico.planBean.listaTasaciones}" var="tasaciones" rowIndexVar="tasacionIndex">
										<h:panelGroup rendered="#{(tasacionIndex eq 0) and (tasaciones.valor eq 0) }">
										<th class="borde_inferior" colspan="1"><b>Incluidos<b></th>
										</h:panelGroup>
							  </it:iterator>
							  <it:iterator value="#{trafico.planBean.listaTasaciones}" var="tasaciones" rowIndexVar="tasacionIndex">
										<h:panelGroup rendered="#{(tasacionIndex eq 0) and (tasaciones.valor ne 0) }">
										<th class="borde_inferior" colspan="1"><b>Incluido<b></th>
										<th class="borde_inferior" colspan="4"><b>Tarifas adicionales</b></th>
										</h:panelGroup>
							  </it:iterator>
							
							</h:panelGroup>
							
							
									<!--  <th class="borde_inferior" colspan="4"><b>Tarifas adicionales</b></th>  -->
								</h:panelGroup>
								
								
								<h:panelGroup rendered="#{(trafico.planBean.tipoPlan eq tipoPlanBean.planEmpresa ) and
							(trafico.cargarCargoFijo ne '1' )}">
							
							<h:panelGroup rendered="#{trafico.planBean.totalMinutos ne '0'}">
								<th class="borde_inferior" colspan="3"><b>Incluido<b></th>
                            </h:panelGroup>
							<h:panelGroup rendered="#{trafico.planBean.totalMinutos eq '0'}">
								<th class="borde_inferior" colspan="1"><b>Incluido<b></th>
							</h:panelGroup>
								</h:panelGroup>
								
<!--  PLAN MULTIMEDIA RED EXCEDIDO TODO DESTINO - SS  MODIFICADO ESTRUCTURA TARIFARIA-->      
							
						
					<!-- -----------------------------------------------------------FIN------------------------------------------------------------------ -->
											
											<!--  OTROS PLANES - SS  -->
												<h:panelGroup rendered="#{((trafico.planBean.tipoPlan gt tipoPlanBean.planMultimediaRed))}">
												   <it:iterator value="#{trafico.planBean.listaTasaciones}" var="tasacion">
														<th><b><h:outputText value="#{tasacion.nombre}"/></b></th>
													</it:iterator>
												</h:panelGroup>
											<!--  OTROS PLANES - SS  -->
				
										</tr>
											<!--   ESTRUCTURA DEL CONTENIDO SUPERIOR DE LA TABLA - COLUMNAS   -->
										    
				
											<!--   ESTRUCTURA DEL CONTENIDO SUPERIOR DE LA TABLA - SUBCOLUMNAS   -->
											
											<!--  PLAN TARIFA PLANA - SS CC  -->
											<!-- TipoPlanNormal SS -->
												<h:panelGroup rendered="#{(trafico.planBean.tipoPlan eq tipoPlanBean.planTarifaPlanaSs) and
												(trafico.planBean.tipoMercado eq miEntelBean.siglaSuscripcion)}">
													<tr>
													   	<!-- Tasacion Horario Normal -->
														<it:iterator value="#{trafico.planBean.listaTasaciones}" var="tasacion" rowIndexVar="tasacionIndex">
																<h:panelGroup rendered="#{tasacionIndex eq 1}"> 
																		<th><h:outputText value="#{tasacion.nombre}"></h:outputText></th> 
															  	</h:panelGroup> 
													   </it:iterator>
													   
													   <!-- Tasacion Horario Bajo -->
													   <it:iterator value="#{trafico.planBean.listaTasaciones}" var="tasacion" rowIndexVar="tasacionIndex">
															  	<h:panelGroup rendered="#{tasacionIndex eq 0}"> 
															  	 		<th><h:outputText value="#{tasacion.nombre}"></h:outputText></th> 
															  	</h:panelGroup>
														</it:iterator>
													</tr>
												</h:panelGroup>
											<!-- TipoPlanNormal SS -->
											
											<!-- TipoPlanNormal CC -->
											<h:panelGroup rendered="#{(trafico.planBean.tipoPlan eq tipoPlanBean.planTarifaPlanaCc) and
												(trafico.planBean.tipoMercado eq miEntelBean.siglaCuentaControlada)}">
													<tr>
													   	<!-- Tasacion Horario Normal -->
														<it:iterator value="#{trafico.planBean.listaTasaciones}" var="tasacion" rowIndexVar="tasacionIndex">
																<h:panelGroup rendered="#{tasacionIndex eq 1}"> 
																		<th><h:outputText value="#{tasacion.nombre}"></h:outputText></th> 
															  	</h:panelGroup> 
													   </it:iterator>
													   
													   <!-- Tasacion Horario Bajo -->
													   <it:iterator value="#{trafico.planBean.listaTasaciones}" var="tasacion" rowIndexVar="tasacionIndex">
															  	<h:panelGroup rendered="#{tasacionIndex eq 0}"> 
															  	 		<th><h:outputText value="#{tasacion.nombre}"></h:outputText></th> 
															  	</h:panelGroup>
														</it:iterator>
													</tr>
												</h:panelGroup>
											
											<!-- TipoPlanNormal CC -->
											<!--  PLAN TARIFA PLANA - SS CC -->
				
				
											<!--  PLAN RED - SS CC  -->
											<!-- TipoPlanRed SS -->
												<h:panelGroup rendered="#{(trafico.planBean.tipoPlan eq tipoPlanBean.planRedSs) and
												(trafico.planBean.tipoMercado eq miEntelBean.siglaSuscripcion)}">
													<tr>
														<h:panelGroup rendered="#{(trafico.planBean.totalMinutosAdicional ne tipoPlanBean.flagHidden)}">
															 <it:iterator value="#{trafico.planBean.listaTasaciones}" var="tasacion" rowIndexVar="tasacionIndex">
															    <h:panelGroup rendered="#{tasacionIndex lt 2}">
																 <th><h:outputText value="#{tasacion.nombre}"/></th>
																</h:panelGroup>
															 </it:iterator>
														</h:panelGroup>
														
														<h:panelGroup rendered="#{(trafico.planBean.totalMinutosAdicional eq tipoPlanBean.flagHidden)}">
															<th>Minutos todo destino</th>
														</h:panelGroup>
				
														 <it:iterator value="#{trafico.planBean.listaTasaciones}" var="tasacion">
															<th><h:outputText value="#{tasacion.nombre}"/></th>
														 </it:iterator>
													</tr>
												</h:panelGroup>
											<!-- TipoPlanRed SS -->
											
											<!-- TipoPlanRed CC -->		
												<h:panelGroup rendered="#{(trafico.planBean.tipoPlan eq tipoPlanBean.planRedCc) and
												(trafico.planBean.tipoMercado eq miEntelBean.siglaCuentaControlada)}">
													<tr>
														 <it:iterator value="#{trafico.planBean.listaTasaciones}" var="tasacion">
															<th><h:outputText value="#{tasacion.nombre}"/></th>
														</it:iterator>
													</tr>
												</h:panelGroup>
											<!-- TipoPlanRed CC-->
											<!--  PLAN RED - SS CC  -->
				
				
											<!--   PLAN GLOBAL - TARIFA UNICA FRECUENTE SS   -->
											<h:panelGroup rendered="#{(trafico.planBean.tipoPlan eq tipoPlanBean.planTarifaUnicaFrec)}">
												     <tr>
														 
														 <th>Todo Destino</th>
														 <it:iterator value="#{trafico.planBean.listaTasaciones}" var="tasacion" rowIndexVar="tasacionIndex">
																 <h:panelGroup rendered="#{tasacionIndex eq 3}">
																	<th><h:outputText value="#{tasacion.nombre}"/></th>
																</h:panelGroup>
														 </it:iterator>	
														 <it:iterator value="#{trafico.planBean.listaTasaciones}" var="tasacion" rowIndexVar="tasacionIndex">
																 <h:panelGroup rendered="#{tasacionIndex lt 2}">
																	<th><h:outputText value="#{tasacion.nombre}"/></th>
																</h:panelGroup>
														 </it:iterator>				
													 </tr>
												</h:panelGroup>
											<!--   PLAN GLOBAL - TARIFA UNICA FRECUENTE SS   -->
				
				
											<!--  PLAN RED FIJA - SS CC   -->
												<!-- TipoPlanRedFija SS -->
												    <h:panelGroup rendered="#{(trafico.planBean.tipoPlan eq tipoPlanBean.planRedFija) and
												    (trafico.planBean.tipoMercado eq miEntelBean.siglaSuscripcion)}">
													 <th>Minutos incluidos</th>
													 <it:iterator value="#{trafico.planBean.listaTasaciones}" var="tasacion" rowIndexVar="tasacionIndex">
														<th><h:outputText value="#{tasacion.nombre}"/></th>
													 </it:iterator>
													</h:panelGroup>
												<!-- TipoPlanRedFija SS -->
												
												<!-- TipoPlanRedFija CC -->
												    <h:panelGroup rendered="#{(trafico.planBean.tipoPlan eq tipoPlanBean.planRedFija) and
												    (trafico.planBean.tipoMercado eq miEntelBean.siglaCuentaControlada)}">
												     <th>Minutos Red Fija y 1 Nro Frecuente</th>
													 <it:iterator value="#{trafico.planBean.listaTasaciones}" var="tasacion" rowIndexVar="tasacionIndex">
														<th><h:outputText value="#{tasacion.nombre}"/></th>
													 </it:iterator>
													</h:panelGroup>
												<!-- TipoPlanRedFija CC -->
											<!--  PLAN RED FIJA - SS CC  -->
											
											<!--  PLAN MULTIMEDIA - CC  -->
												<h:panelGroup rendered="#{(trafico.planBean.tipoPlan eq tipoPlanBean.planMultimediaCc)}">
													 <it:iterator value="#{trafico.planBean.listaTasaciones}" var="tasacion" rowIndexVar="tasacionIndex">
														<h:panelGroup rendered="#{(tasacionIndex eq 0)}">
														  <th><h:outputText value="#{tasacion.nombre}"/></th>
														</h:panelGroup>
													</it:iterator>
													
														<th rowspan="2">Internet M&oacute;vil</th>
														
													<it:iterator value="#{trafico.planBean.listaTasaciones}" var="tasacion" rowIndexVar="tasacionIndex">
														<h:panelGroup rendered="#{(tasacionIndex gt 0)}">
														   <th><h:outputText value="#{tasacion.nombre}"/></th>
														</h:panelGroup>
													</it:iterator>
													
												</h:panelGroup>
											<!--  PLAN MULTIMEDIA - CC  -->
				
											<!--  PLAN MM IPHONE - SS  -->	
											<h:panelGroup rendered="#{(trafico.planBean.tipoPlan eq tipoPlanBean.planMultimediaIphone)}">
													<tr>
													<!-- COLUMNA 1 -->
													<h:panelGroup rendered="#{(trafico.planBean.totalMinutosAdicional ne tipoPlanBean.flagHidden)}">
														<th>Minutos On-Net</th>
													</h:panelGroup>
													<!-- COLUMNA 1 -->
													
													<!-- COLUMNA 2 -->
													<th>Minutos todo destino</th>
													<!-- COLUMNA 2 -->
													
													<!-- COLUMNA 3 -->
													<h:panelGroup rendered="#{(trafico.planBean.totalMinutosAdicional ne tipoPlanBean.flagHidden)}"> 
														<it:iterator value="#{trafico.planBean.listaTasaciones}" var="tasacion" rowIndexVar="tasacionIndex">
															<h:panelGroup rendered="#{(tasacionIndex eq 0)}">
															<th><h:outputText value="#{tasacion.nombre}"/></th>
															</h:panelGroup>
														</it:iterator>
													</h:panelGroup>
													<h:panelGroup rendered="#{(trafico.planBean.totalMinutosAdicional eq tipoPlanBean.flagHidden)}">
														<th width='70px'>SMS y MMS</th>
													</h:panelGroup>
													<!-- COLUMNA 3 -->
													
													<!-- COLUMNA 4 -->
													<th>Internet M&oacute;vil</th>
													<!-- COLUMNA 4 -->
													
													<!-- COLUMNA 5 -->
													<h:panelGroup rendered="#{(trafico.planBean.totalMinutosAdicional ne tipoPlanBean.flagHidden)}">
														<it:iterator value="#{trafico.planBean.listaTasaciones}" var="tasacion" rowIndexVar="tasacionIndex">
															<h:panelGroup rendered="#{(tasacionIndex gt 0)}">
															<th><h:outputText value="#{tasacion.nombre}"/></th>
															</h:panelGroup>
														</it:iterator>
													</h:panelGroup>
			
													<h:panelGroup rendered="#{(trafico.planBean.totalMinutosAdicional eq tipoPlanBean.flagHidden)}">
														<th>Minutos</th>
														<it:iterator value="#{trafico.planBean.listaTasaciones}" var="tasacion" rowIndexVar="tasacionIndex">
															<h:panelGroup rendered="#{(tasacionIndex gt 1)}">
															<th><h:outputText value="#{tasacion.nombre}"/></th>
															</h:panelGroup>
														</it:iterator>
													</h:panelGroup>
													<!-- COLUMNA 5 -->
													</tr>
												</h:panelGroup>
											<!--  PLAN MM IPHONE - SS  -->
											
											
											<!--  PLAN MM RED - SS  -->	
												<h:panelGroup rendered="#{(trafico.planBean.tipoPlan eq tipoPlanBean.planMultimediaRed)}">
														<tr>
														<th>Minutos On-Net y Red Fija</th>
														<th>Minutos Otras compa&ntilde;&iacute;as</th>
														 <it:iterator value="#{trafico.planBean.listaTasaciones}" var="tasacion" rowIndexVar="tasacionIndex">
															<h:panelGroup rendered="#{(tasacionIndex eq 0)}">
															<th><h:outputText value="#{tasacion.nombre}"/></th>
															</h:panelGroup>
														</it:iterator>
														<th>Internet M&oacute;vil</th>
														
														<it:iterator value="#{trafico.planBean.listaTasaciones}" var="tasacion" rowIndexVar="tasacionIndex">
															<h:panelGroup rendered="#{(tasacionIndex gt 0)}">
															<th><h:outputText value="#{tasacion.nombre}"/></th>
															</h:panelGroup>
														</it:iterator>
														</tr>
												</h:panelGroup>
											<!--  PLAN MM RED - SS  -->
											
											<!-- --------------------------------------------------- INICIO -------------------------------------------------------------------------- -->
						
											<!--  SS  MODIFICADO ESTRUCTURA TARIFARIA-->
																	
									<h:panelGroup rendered="#{(trafico.planBean.tipoPlan eq tipoPlanBean.planEmpresa )}">
										<tr>
										<h:panelGroup rendered="#{trafico.planBean.totalMinutos ne '0'}">	
										<th>Minutos todo destino</th>
										</h:panelGroup>
										<th>Cuota tr�fico Internet m�vil</th>											
										<h:panelGroup rendered="#{(trafico.cargarCargoFijo ne '1' )}">	
										 <it:iterator value="#{trafico.planBean.listaTasaciones}" var="tasacion" rowIndexVar="tasacionIndex">
											<h:panelGroup rendered="#{(tasacionIndex eq 0) and (tasacion.valor ne 0)}">
											<th><h:outputText value="#{tasacion.nombre}"/></th>
											</h:panelGroup>
										</it:iterator>
										</h:panelGroup>
										<h:panelGroup rendered="#{(trafico.cargarCargoFijo ne '1' )}">																			
										<it:iterator value="#{trafico.planBean.listaTasaciones}" var="tasacion" rowIndexVar="tasacionIndex">
											<h:panelGroup rendered="#{(tasacionIndex gt 0)}">
											<th><h:outputText value="#{tasacion.nombre}"/></th>
											</h:panelGroup>
										</it:iterator>
										</h:panelGroup>
										</tr>
								</h:panelGroup>
						
						<!--  SS  MODIFICADO ESTRUCTURA TARIFARIA-->		
								
													
										    <!-- --------------------------------------------------- FIN -------------------------------------------------------------------------- -->
										    
											<!--   ESTRUCTURA DEL CONTENIDO SUPERIOR DE LA TABLA - SUBCOLUMNAS   -->
										
										
										<tr><td></td></tr>
										<tr><td colspan="10"><div class="bottom"><span></span></div></td></tr>	
						
										</tbody>
										
										
										<tbody class="contenido_plan">
										
											<!--   ESTRUCTURA DEL CONTENIDO INTERNO DE LA TABLA - DATA   -->
										
										<tr>
											<td class="primera">$ <h:outputText value="#{trafico.planBean.cargoFijoPlan}"><f:convertNumber currencyCode="CLP" locale="es" /></h:outputText></td>
											
												<!--  PLAN TARIFA PLANA - SS CC  -->
												<!-- TipoPlanNormal SS -->
													<h:panelGroup rendered="#{(trafico.planBean.tipoPlan eq tipoPlanBean.planTarifaPlanaSs) and
													(trafico.planBean.tipoMercado eq miEntelBean.siglaSuscripcion)}">
													   <td><h:outputText value="#{trafico.planBean.totalMinutos}" converter="planConverter"/></td>
													    
														<it:iterator value="#{trafico.planBean.listaTasaciones}" var="tasaciones" rowIndexVar="tasacionIndex">
																<h:panelGroup rendered="#{tasacionIndex eq 1}"> 
																		<td>$ <h:outputText value="#{tasaciones.valor}" converter="planConverter"/></td> 
															  	</h:panelGroup> 
													   </it:iterator>
													   
													   <it:iterator value="#{trafico.planBean.listaTasaciones}" var="tasaciones" rowIndexVar="tasacionIndex">
															  	<h:panelGroup rendered="#{tasacionIndex eq 0}"> 
															  	 		<td>$ <h:outputText value="#{tasaciones.valor}" converter="planConverter"/></td> 
															  	</h:panelGroup>
														</it:iterator>
													</h:panelGroup>
												<!-- TipoPlanNormal SS -->
					
												<!-- TipoPlanNormal CC -->
													<h:panelGroup rendered="#{(trafico.planBean.tipoPlan eq tipoPlanBean.planTarifaPlanaCc) and
													(trafico.planBean.tipoMercado eq miEntelBean.siglaCuentaControlada)}">
														<it:iterator value="#{trafico.planBean.listaTasaciones}" var="tasaciones" rowIndexVar="tasacionIndex">
																<h:panelGroup rendered="#{tasacionIndex eq 1}"> 
																		<td>$ <h:outputText value="#{tasaciones.valor}" converter="planConverter"/></td> 
															  	</h:panelGroup> 
													   </it:iterator>
													   
													   <it:iterator value="#{trafico.planBean.listaTasaciones}" var="tasaciones" rowIndexVar="tasacionIndex">
															  	<h:panelGroup rendered="#{tasacionIndex eq 0}"> 
															  	 		<td>$ <h:outputText value="#{tasaciones.valor}" converter="planConverter"/></td> 
															  	</h:panelGroup>
														</it:iterator>
													</h:panelGroup>
											   <!-- TipoPlanNormal CC -->
											
											<!--  PLAN TARIFA PLANA - SS CC  -->
												
				
											<!--  PLAN RED - SS CC -->
												<!-- TipoPlanRed SS -->
													<h:panelGroup rendered="#{(trafico.planBean.tipoPlan eq tipoPlanBean.planRedSs) and
													(trafico.planBean.tipoMercado eq miEntelBean.siglaSuscripcion)}">
															<td><h:outputText value="#{trafico.planBean.totalMinutos}" converter="planConverter"/></td>
															
															<h:panelGroup rendered="#{(trafico.planBean.totalMinutosAdicional ne tipoPlanBean.flagHidden)}">
																<td><h:outputText value="#{trafico.planBean.totalMinutosAdicional}" converter="planConverter"/></td>
															</h:panelGroup>
															
															<it:iterator value="#{trafico.planBean.listaTasaciones}" var="tasaciones" rowIndexVar="tasacionIndex">
																<td>$ <h:outputText value="#{tasaciones.valor}"  converter="planConverter"/></td>
															</it:iterator>
													</h:panelGroup>
												<!-- TipoPlanRed SS -->
				
											<!-- TipoPlanRed CC -->
												<h:panelGroup rendered="#{(trafico.planBean.tipoPlan eq tipoPlanBean.planRedCc) and
												(trafico.planBean.tipoMercado eq miEntelBean.siglaCuentaControlada)}">
														<it:iterator value="#{trafico.planBean.listaTasaciones}" var="tasaciones" rowIndexVar="tasacionIndex">
															<td>$ <h:outputText value="#{tasaciones.valor}"  converter="planConverter"/></td>
														</it:iterator>
												</h:panelGroup>
											<!-- TipoPlanRed CC -->
											<!--  PLAN RED - SS CC -->
											
				
											<!--   PLAN GLOBAL - TARIFA UNICA FRECUENTE SS   -->
												<h:panelGroup rendered="#{(trafico.planBean.tipoPlan eq tipoPlanBean.planTarifaUnicaFrec)}" >
													<td><h:outputText value="#{trafico.planBean.totalMinutos}" converter="planConverter"/></td>
													
													<it:iterator value="#{trafico.planBean.listaTasaciones}" var="tasaciones" rowIndexVar="tasacionIndex">
														 <h:panelGroup rendered="#{tasacionIndex eq 3}">
														 	<td><h:outputText value="#{tasaciones.valor}"  converter="planConverter"/></td>
														 </h:panelGroup>
													</it:iterator>
													
													<it:iterator value="#{trafico.planBean.listaTasaciones}" var="tasaciones" rowIndexVar="tasacionIndex">
														 <h:panelGroup rendered="#{tasacionIndex lt 2}">
														 	<td>$ <h:outputText value="#{tasaciones.valor}"  converter="planConverter"/></td>
														 </h:panelGroup>
													</it:iterator>
													
												</h:panelGroup>
											<!--   PLAN GLOBAL - TARIFA UNICA FRECUENTE SS   -->
											
				
											<!--  PLAN JOVEN - SS          -->
											<!--  PLAN FULL - SS           -->
											<!--  PLAN TARIFA UNICA - SS   -->
												<h:panelGroup rendered="#{(trafico.planBean.tipoPlan eq tipoPlanBean.planJoven) or 
												(trafico.planBean.tipoPlan eq tipoPlanBean.planFull) or 
												(trafico.planBean.tipoPlan eq tipoPlanBean.planTarifaUnica)}" >
													<td><h:outputText value="#{trafico.planBean.totalMinutos}" converter="planConverter"/></td>
													
													<it:iterator value="#{trafico.planBean.listaTasaciones}" var="tasaciones" rowIndexVar="tasacionIndex">
														<td>$ <h:outputText value="#{tasaciones.valor}"  converter="planConverter"/></td>
													</it:iterator>
												</h:panelGroup>
											<!--    PLAN JOVEN - SS          -->
											<!--    PLAN FULL - SS           -->
											<!--    PLAN TARIFA UNICA - SS   -->
											
				
											<!--  PLAN RED FIJA - SS CC -->
											<!-- TipoPlanRedFija SS -->
												<h:panelGroup rendered="#{(trafico.planBean.tipoPlan eq tipoPlanBean.planRedFija) and
												(trafico.planBean.tipoMercado eq miEntelBean.siglaSuscripcion)}">
													<td><h:outputText value="#{trafico.planBean.totalMinutos}" converter="planConverter"/></td>
													
													<it:iterator value="#{trafico.planBean.listaTasaciones}" var="tasaciones" rowIndexVar="tasacionIndex">
														<td>$ <h:outputText value="#{tasaciones.valor}"  converter="planConverter"/></td>
													</it:iterator>
					
												</h:panelGroup>
											<!-- TipoPlanRedFija SS -->
											
											<!-- TipoPlanRedFija CC -->
												<h:panelGroup rendered="#{(trafico.planBean.tipoPlan eq tipoPlanBean.planRedFija) and
												(trafico.planBean.tipoMercado eq miEntelBean.siglaCuentaControlada)}">
												
												    <td><h:outputText value="#{trafico.planBean.totalMinutos}" converter="planConverter"/></td>
												
													<it:iterator value="#{trafico.planBean.listaTasaciones}" var="tasaciones" rowIndexVar="tasacionIndex">
														<td>$ <h:outputText value="#{tasaciones.valor}"  converter="planConverter"/></td>
													</it:iterator>
					
												</h:panelGroup>
											<!-- TipoPlanRedFija CC -->
											<!--  PLAN RED FIJA - SS CC -->
											
											<!--  PLAN FAMILIA - SS  -->
												<h:panelGroup rendered="#{(trafico.planBean.tipoPlan eq tipoPlanBean.planFamilia)}">
					
													<it:iterator value="#{trafico.planBean.listaTasaciones}" var="tasaciones" rowIndexVar="tasacionIndex">
														<h:panelGroup rendered="#{(tasacionIndex ge 1) and (tasacionIndex le 2)}">
														<td><h:outputText value="#{tasaciones.valor}"  converter="planConverter"/></td>
														</h:panelGroup>
													</it:iterator>
													
													<it:iterator value="#{trafico.planBean.listaTasaciones}" var="tasaciones" rowIndexVar="tasacionIndex">
														<h:panelGroup rendered="#{tasacionIndex eq 3}">
														<td>$ <h:outputText value="#{tasaciones.valor}"  converter="planConverter"/></td>
														</h:panelGroup>
													</it:iterator>
													
													<it:iterator value="#{trafico.planBean.listaTasaciones}" var="tasaciones" rowIndexVar="tasacionIndex">
														<h:panelGroup rendered="#{tasacionIndex eq 0}">
														<td>$ <h:outputText value="#{tasaciones.valor}"  converter="planConverter"/></td>
														</h:panelGroup>
													</it:iterator>
												
												</h:panelGroup>
											<!--  PLAN FAMILIA - SS  -->
											
											<!--  PLAN RED EMPRESA - SS  -->
												<h:panelGroup rendered="#{(trafico.planBean.tipoPlan eq tipoPlanBean.planRedEmpresa)}">
													<td>Ilimitados</td>
													<it:iterator value="#{trafico.planBean.listaTasaciones}" var="tasaciones" rowIndexVar="tasacionIndex">
														<td>$ <h:outputText value="#{tasaciones.valor}"  converter="planConverter"/></td>
													</it:iterator>
												</h:panelGroup>
											<!--   PLAN RED EMPRESA - SS  -->
											
											
											<!--  PLAN MULTIMEDIA - CC -->
												<h:panelGroup rendered="#{(trafico.planBean.tipoPlan eq tipoPlanBean.planMultimediaCc)}">
													 <it:iterator value="#{trafico.planBean.listaTasaciones}" var="tasaciones" rowIndexVar="tasacionIndex">
														<h:panelGroup rendered="#{(tasacionIndex eq 0)}">
														<td>$ <h:outputText value="#{tasaciones.valor}" converter="planConverter"/></td>
														</h:panelGroup>
													</it:iterator>
													
														<td><h:outputText value="#{trafico.planBean.descIMovil}"/></td>
														
													<it:iterator value="#{trafico.planBean.listaTasaciones}" var="tasaciones" rowIndexVar="tasacionIndex">
														<h:panelGroup rendered="#{(tasacionIndex gt 0)}">
														<td>$ <h:outputText value="#{tasaciones.valor}" converter="planConverter"/></td>
														</h:panelGroup>
													</it:iterator>
													
												</h:panelGroup>
											<!--  PLAN MULTIMEDIA - CC -->
					
											<!--  PLAN MULTIMEDIA IPHONE - SS -->
												<h:panelGroup rendered="#{(trafico.planBean.tipoPlan eq tipoPlanBean.planMultimediaIphone)}">
													
													<td><h:outputText value="#{trafico.planBean.totalMinutos}" converter="planConverter"/></td>
													
													<h:panelGroup rendered="#{(trafico.planBean.totalMinutosAdicional ne tipoPlanBean.flagHidden)}">
														<td><h:outputText value="#{trafico.planBean.totalMinutosAdicional}" converter="planConverter"/></td>
													</h:panelGroup>
																										 
													 <it:iterator value="#{trafico.planBean.listaTasaciones}" var="tasaciones" rowIndexVar="tasacionIndex">
														<h:panelGroup rendered="#{(tasacionIndex eq 0)}">
														<td><h:outputText value="#{tasaciones.valor}"  converter="planConverter"/></td>
														</h:panelGroup>
													</it:iterator>
													
													<td><h:outputText value="#{trafico.planBean.descIMovil}"/></td>
														
													<it:iterator value="#{trafico.planBean.listaTasaciones}" var="tasaciones" rowIndexVar="tasacionIndex">
														<h:panelGroup rendered="#{(tasacionIndex gt 0)}">
														<td>$ <h:outputText value="#{tasaciones.valor}"  converter="planConverter"/></td>
														</h:panelGroup>
													</it:iterator>
													
												</h:panelGroup>
											<!--  PLAN MULTIMEDIA IPHONE - SS  -->
											
											
											<!--  PLAN MM RED - SS -->
												<h:panelGroup rendered="#{(trafico.planBean.tipoPlan eq tipoPlanBean.planMultimediaRed)}">
												
													<td><h:outputText value="#{trafico.planBean.totalMinutos}" converter="planConverter"/></td>
													<td><h:outputText value="#{trafico.planBean.totalMinutosAdicional}" converter="planConverter"/></td>
													 
													 <it:iterator value="#{trafico.planBean.listaTasaciones}" var="tasaciones" rowIndexVar="tasacionIndex">
														<h:panelGroup rendered="#{(tasacionIndex eq 0)}">
														<td><h:outputText value="#{tasaciones.valor}"  converter="planConverter"/></td>
														</h:panelGroup>
													</it:iterator>
													
													<td><h:outputText value="#{trafico.planBean.descIMovil}"/></td>
														
													<it:iterator value="#{trafico.planBean.listaTasaciones}" var="tasaciones" rowIndexVar="tasacionIndex">
														<h:panelGroup rendered="#{(tasacionIndex gt 0)}">
														<td>$<h:outputText value="#{tasaciones.valor}"  converter="planConverter"/></td>
														</h:panelGroup>
													</it:iterator>
													
												</h:panelGroup>
											<!--  PLAN MM RED - SS  -->
											
											
											<!-- --------------------------------------------------- INICIO -------------------------------------------------------------------------- -->
							
								<!--  ESTRUCTURA TARIFARIA-->
								
								<h:panelGroup rendered="#{(trafico.planBean.tipoPlan eq tipoPlanBean.planEmpresa )}">
									<h:panelGroup rendered="#{(trafico.planBean.totalMinutos ne '0' )}">	
										<td><h:outputText value="#{trafico.planBean.totalMinutos}" converter="planConverter"/></td>
									</h:panelGroup>
									<td><h:outputText value="#{trafico.planBean.descIMovil}"/></td>
									
									<h:panelGroup rendered="#{(trafico.cargarCargoFijo ne '1' )}">									 
									 <it:iterator value="#{trafico.planBean.listaTasaciones}" var="tasaciones" rowIndexVar="tasacionIndex">
										<h:panelGroup rendered="#{(tasacionIndex eq 0) and (tasaciones.valor ne 0)}">
										<td>$<h:outputText value="#{tasaciones.valor}"  converter="planConverter"/></td>
										</h:panelGroup>
									</it:iterator>
									</h:panelGroup>
									
									<h:panelGroup rendered="#{(trafico.cargarCargoFijo eq '1' )}">	
									<it:iterator value="#{trafico.planBean.listaTasaciones}" var="tasaciones" rowIndexVar="tasacionIndex">
										<h:panelGroup rendered="#{(tasacionIndex gt 0)}">
										<td>$<h:outputText value="#{tasaciones.valor}"  converter="planConverter"/></td>
										</h:panelGroup>
									</it:iterator>
									</h:panelGroup>
									
								</h:panelGroup>
							<!--   ESTRUCTURA TARIFARIA -->
							
							<!-- --------------------------------------------------- FIN -------------------------------------------------------------------------- -->
											
											<!--  OTROS PLANES SS CC  -->
												<h:panelGroup rendered="#{(trafico.planBean.tipoPlan gt tipoPlanBean.planMultimediaRed)}">
													<it:iterator value="#{trafico.planBean.listaTasaciones}" var="tasaciones" rowIndexVar="tasacionIndex">
														<td>$ <h:outputText value="#{tasaciones.valor}"  converter="planConverter"/></td>
													</it:iterator>
												</h:panelGroup>
											<!--  OTROS PLANES SS CC  -->
										</tr>
										
									    <!--   ESTRUCTURA DEL CONTENIDO INTERNO DE LA TABLA  DATA   -->
				
										</tbody>
								    </table>
								</div>
								<!-- DIV main plan -->
							</div>
							<!-- DIV planes_header_tabla clearfix -->
						</div>
						<!--/tabla-->				
					
					</div>	
					<!-- /ESTRUCTURA TARIFARIA -->	
					<div class="disclaimer">Tasaci&oacute;n al segundo.</div>		
	  		<!-- /PLAN ANTERIOR -->
	  	                                                                 
                                    
				<!-- ESTRUCTURA TRAFICO -->
				<div class="estructuraTrafico">
								<h2 class="ico_grafico">
									<strong>Tr&aacute;fico Anterior</strong>
								</h2>
		
								<!-- Tabla Voz -->
								<div class="tabla">	
									<div class="header_tabla trafico clearfix">
										<div class="top anchoFijo"><span></span></div>
										<div class="main trafico">
											<table>
												<tr>
													<th width="160" rowspan="2"><strong>VOZ (Minutos)</strong></th>
													<th rowspan="2">Minutos utilizados</th>
		
													<th width="240" colspan="2" class="bordeInferior">Minutos adicionales utilizados</th>
												</tr>
												<tr>
												<it:iterator var="row" value="#{trafico.traficoVoz.detallePorHorario}">
													<th><h:outputText value="#{row.descripcion}"/></th>
											    </it:iterator>
												</tr>
											</table>					
										</div>
		
										<div class="bottom anchoFijo"><span></span></div>
									</div>
									<div class="contenido_tabla">
										<table>
											<tbody>
												<tr>
													<td width="160">Hasta el <h:outputText value="#{trafico.traficoVoz.fechaFinal}">
													<f:convertDateTime pattern="dd/MM/yyyy"/>
													</h:outputText></td>
													<td><h:outputText value="#{trafico.traficoVoz.total}" converter="traficoVozMinSecConverter"/></td>
													 <it:iterator var="row" value="#{trafico.traficoVoz.detallePorHorario}">
													<td><h:outputText value="#{row.consumo}" converter="traficoVozMinSecConverter"/></td>
											        </it:iterator>
												</tr>
											</tbody>
										</table>
									</div>
								</div>
								<!--/tabla-->	
								
								<!-- Tabla Datos -->
								<div class="tabla">	
									<div class="header_tabla trafico clearfix">
										<div class="top anchoFijo"><span></span></div>
										<div class="main trafico">
											<table>
												<tr>
													<th width="145"><strong>DATOS (Megabytes)</strong></th>
													<it:iterator var="row" value="#{trafico.traficoDatos}">
													<th width="95"><h:outputText value="#{row.tipo}"/></th>
		                                            </it:iterator>
												</tr>
											</table>					
										</div>
										<div class="bottom anchoFijo"><span></span></div>
									</div>
									<div class="contenido_tabla">
		
										<table>
											<tbody>
												<tr>
													
													<it:iterator var="row" rowIndexVar="rowIndex" value="#{trafico.traficoDatos}">
													<h:panelGroup rendered="#{rowIndex == 0}"><td width="140">&nbsp;&nbsp;Hasta el <h:outputText value="#{row.fechaFinal}">
													<f:convertDateTime pattern="dd/MM/yyyy"/>
													</h:outputText></td></h:panelGroup>
													<td width="105"><h:outputText value="#{row.totalFormated}" /> MB</td>
		                                            </it:iterator>
													
												</tr>
											</tbody>
										</table>
									</div>
								</div>
								<!--/tabla-->	
								
								<!-- Tabla Mensajes -->
								<div class="tabla">	
									<div class="header_tabla trafico clearfix">
										<div class="top anchoFijo"><span></span></div>
										<div class="main trafico">
		
											<table>
												<tr>
													<th width="160"><strong>MENSAJES</strong></th>
											      	<th width="220">MMS y SMS</th>
													<th>OTROS</th>
		                                       </tr>
											</table>					
										</div>
		
										<div class="bottom anchoFijo"><span></span></div>
									</div>
									<div class="contenido_tabla">
										<table>
											<tbody>
												<tr>
													<it:iterator var="row" rowIndexVar="rowIndex" value="#{trafico.traficoMensajes}">
													<h:panelGroup rendered="#{rowIndex == 0}"><td width="190">Hasta&nbsp;el&nbsp;<h:outputText value="#{row.fechaFinal}">
													<f:convertDateTime pattern="dd/MM/yyyy"/>
													</h:outputText></td></h:panelGroup>
													<td width="220"><h:outputText value="#{row.total}" converter="javax.faces.Integer"/></td>
		                                            </it:iterator>
										
												</tr>
											</tbody>
										</table>
									</div>
								</div>
								<!--/tabla-->	
								
		          </div>
				  <!-- ESTRUCTURA TRAFICO -->
		  	
			</div>
			<!-- DIV lista-trafico-anterior  -->	
				
            </it:iterator>
		  </div>
		  <!-- DIV trafico-anterior-titulo -->
	    </div>
	    <!-- DIV menu-desplegable -->
	  </div>
	  
	  
	</h:panelGroup>
</h:panelGroup>

<!-- ALERTA  -->
<div id="mensajePlanesAnteriores">
	<div class="traficoAlerta">
		<cm:getProperty node="${infoPlanTraficoAnterior[0]}" name="html" />
	</div>
</div>
<!--/ ALERTA  -->

</f:view>