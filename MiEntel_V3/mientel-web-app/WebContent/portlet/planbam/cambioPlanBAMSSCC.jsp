<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="f"  uri="http://java.sun.com/jsf/core"%>
<%@ taglib prefix="h"  uri="http://java.sun.com/jsf/html"%>
<%@ taglib prefix="c"  uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="it" uri="http://i2b.cl/jsf/iterator/" %>
<%@ taglib prefix="r"  uri="http://www.bea.com/servers/portal/tags/netuix/render"%>
<%@ taglib prefix="cm" uri="http://www.bea.com/servers/portal/tags/content" %>
<%@ taglib prefix="entel" uri="/WEB-INF/tld/entel-tags.tld" %>

<cm:search id="infoCambioPlan" query="idContenido = 'infoCambioPlanBam'" useCache="true"  />
<cm:search id="infoEfectivoCambio" query="idContenido = 'infoEfectivoCambio'" useCache="true"  />

<f:view beforePhase="#{planBAMController.obtenerPlanesDisponibles}">
			
            <input class="codbscs2" type="hidden" value=""/>
            <input class="nombreNuevoPlan" type="hidden" value=""/>
            <input class="valorNuevoPlan" type="hidden" value=""/>
	        <h1>Cambio de Plan</h1>
			<h:panelGroup rendered="#{planBAMController.existenPlanesDisponibles}">
				
					<h2 class="superchipOK">Planes disponibles</h2>
					<p>Selecciona de la siguiente lista de planes que tenemos para ti a cual deseas cambiarte:</p>
		
					<div id="menu-desplegable-planes">
					
					<!-- Planes Disponibles  -->
					<it:iterator var="grupoPlan" value="#{planBAMController.grupoPlanesDisponibles}" rowIndexVar="indexPlan">	
					<c:set var="style" value="#{indexPlan % 2 == 0 ? 'impar': 'par'}" scope="page" />
							

				    <!-- bolsa -->
					<div class="bolsa <h:outputText value="#{style}"/> clearfix">
						<div class="header">
						
							<!-- Estructura - Nombre del Plan y Descripcion del mismo -->
							<a href="javascript:;" class="cerrado" onclick="mxTracker._trackPageview('Personas/Mi Entel/Mis Productos/Banda Ancha Movil/Plan o cambio de plan/<h:outputText value="#{grupoPlan.nombrePlanSinAcentos}"/>/Inicio');"><h:outputText value="#{grupoPlan.nombrePlan}"/></a>
							<span><h:outputText value="#{grupoPlan.descPlan}"/></span>

						</div>
						<div id="red" class="lista-bolsas" style="display: none;">
						
							<div class="tabla_precios">
							
								<div class="planes_header_tabla clearfix">
					
									<div class="top"><span></span></div>
									<div class="main" style="background-color: #E3EDF8;">
										<table>
												<tr>
												
												    <th width="30%" class="primera" rowspan="2">Nombre del plan</th>
													<th width="10%" rowspan="2">Cargo fijo</th>
													<th width="20%" rowspan="2">Cuota Tr&aacute;fico Incluido</th>
													<th width="20%" rowspan="2">Velocidad M&aacute;xima de Descarga</th>
													<!-- //Estructura Datos Fijos-->
													<th width="20%" class="ultimo" rowspan="2">&nbsp;</th>
													
												</tr>
										</table>					
									</div>
									<div class="bottom"><span></span></div>
								</div>
																		
								<!-- Para los planes FDT no se debe mostrar los planes disponibles --> 	
								<h:panelGroup rendered="#{planBAMController.submercado == 'FDT'}">	
									<div class="amarilla margen" style="padding-top: 10px; padding-bottom: 10px; background: none repeat scroll 0 0 #FFF1A8; border-bottom: 1px solid #CFC489; border-top: 1px solid #CFC489;">
										<h6><strong><center>No existe oferta disponible.</center></strong></h6>
									</div>
								</h:panelGroup>	
								
								 
								<h:panelGroup rendered="#{planBAMController.submercado != 'FDT'}">	 
									<table class="planes_contabla">
									 <tbody>
										<!-- Planes Disponibles Agrupados -->
										<it:iterator value="#{grupoPlan.planesDisponibles}" var="plan" rowIndexVar="planIndex">
										     <c:set var="style" value="#{planIndex % 2 == 0 ? 'color': ''}" scope="page" />
	
											 <tr class="<h:outputText value="#{style}"/>">
											 
												<!-- Estructura Datos Fijos-->
												<td width="30%" class="primera" id="<h:outputText value='#{plan.codbscs2}'/>"><h:outputText value="#{plan.nombrePlan}"/></td>
												<td width="10%">$<h:outputText value="#{plan.cargoFijoPlan}"><f:convertNumber currencyCode="CLP" locale="es" /></h:outputText></td>
												<td width="20%"><h:outputText value="#{plan.umbralFairUseMb}" converter="traficoDatosConverterBAMDouble"></h:outputText></td>
												<td width="20%"><h:outputText value="#{plan.velocidadMaxPlan}"></h:outputText></td>
												<!-- //Estructura Datos Fijos-->
												
												<td width="20%">
													<entel:view name="cambiarPlan">
														<a href="#" class="btnCambiarmePlan cambiarPlan" onclick="mxTracker._trackPageview('Personas/Mi Entel/Mis Productos/Banda Ancha Movil/Plan o cambio de plan/<h:outputText value="#{grupoPlan.nombrePlanSinAcentos}"/>/Contratar');">Cambiarme a este plan</a>
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
												</td>
											</tr>
										 </it:iterator>
										 
									 </tbody>
									</table>
								</h:panelGroup>	
								
								
								<div class="disclaimer">
								
								</div>
							</div><!--/tabla_precios-->
					
							
							<div class="paso_2">
								<div class="nombre_plan">
									<div class="nombre_plan_top anchoFijo"><span></span></div>
									<div class="nombre_plan_main">Nombre del plan</div>
									<div class="nombre_plan_bottom anchoFijo"><span></span></div>
								</div>
								<div class="paso_confirmar clearfix">
									<span class="plan"></span>
									
									
									<span class="camPlan">Vas a cambiar a este plan</span>
									<a href="#" class="cancelar">Cancelar</a>
									<a href="#" class="btnVerdeDelgado confirma"><span>Confirmar</span></a>
									
									<a id="loadingCambioPlan"></a>
								</div>
								<div class="disclaimer">
								<cm:getProperty node="${infoCambioPlan[0]}" name="html" />
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
											<cm:getProperty node="${infoEfectivoCambio[0]}" name="html" />
										</div>
									</div>
								</div>
				
								<div class="disclaimer">
									<cm:getProperty node="${infoCambioPlan[0]}" name="html" />
								</div>
							</div><!--/paso_2-->
							
						</div>
					</div>
					<!-- /bolsa -->	
						
					</it:iterator>
					
			        </div>	
			        
	        <script type="text/javascript">
			$(document).ready(function(){ 
				initMenuDesplegablePlanes();

				$('div.paso_2 a.confirma').click(function() {
					
					var tr = $(this).parents('.lista-bolsas:first');
					var codbscs2 = $('input.codbscs2').val();
					var nombreNuevoPlan = $('input.nombreNuevoPlan').val();
					var valorNuevoPlan = $('input.valorNuevoPlan').val();
					var url='<%=request.getContextPath()%>/portlet/planbam/cambioPlanBAMSSCCJson.faces';
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
								
			});

			function crearMarcaTransaccionGTM(resp) {
				var idTransaccion = resp.respuesta.idTransaccion;
				var codigoProducto = resp.respuesta.skuID;
				var nombreProducto = resp.respuesta.nombre;
				var mercadoPlanes = '<h:outputText value="#{profile.mercado == miEntelBean.siglaSuscripcion ? 'Planes Suscripcion' : 'Planes Cuenta Controlada'}" />';

				mxTracker._trackPageview('Personas/Mi Entel/Mis Productos/Banda Ancha Movil/Plan o cambio de plan/' + mercadoPlanes + '/Conversion');				

				mxTracker._addTrans(idTransaccion, '', resp.respuesta.valorTransaccion);
				mxTracker._addItem(idTransaccion, codigoProducto, nombreProducto, 'Fee plan', resp.respuesta.nuevoValor, '1');
				mxTracker._addItem(idTransaccion, codigoProducto, nombreProducto, 'Costo cambio plan', resp.respuesta.costoOperacional, '1');

				dataLayer = dataLayer||[];
				dataLayer.push({'event': 'tracktrans', 'tracktrans': true});
			}			
			</script>

			
			</h:panelGroup>	
			
<!-- MENSAJES  -->
<jsp:include page="../common/messages_table.jsp"></jsp:include>
<!--  /MENSAJES -->

</f:view>
