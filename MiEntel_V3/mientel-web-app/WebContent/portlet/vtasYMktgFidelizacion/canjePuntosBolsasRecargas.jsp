<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="f" uri="http://java.sun.com/jsf/core"%>
<%@ taglib prefix="h" uri="http://java.sun.com/jsf/html"%>
<%@ taglib prefix="it" uri="http://i2b.cl/jsf/iterator/" %>
<%@ taglib prefix="cm" uri="http://www.bea.com/servers/portal/tags/content" %>
<%@ taglib prefix="entel" uri="/WEB-INF/tld/entel-tags.tld" %>
<%@ taglib prefix="pref" uri="http://www.bea.com/servers/portal/tags/netuix/preferences" %>


<!-- Contenidos -->
<cm:search id="necesitasMasPuntosBolsas" query="idContenido = 'necesitasMasPuntosBolsas'" useCache="false"/>
<cm:search id="necesitasMasPuntosRecargas" query="idContenido = 'necesitasMasPuntosRecargas'" useCache="false"/>
<cm:search id="noBolsasDisp" query="idContenido = 'noBolsasDisp'" useCache="false"/>
<cm:search id="noRecargasDisp" query="idContenido = 'noRecargasDisp'" useCache="false"/>
<cm:search id="pendientePrestaLukaDestino" query="idContenido = 'pendientePrestaLukaDestino'" useCache="false"/>


<f:view beforePhase="#{vtasYMktgFidelizacionController.initCargarBolsasYRecargasCanje}">
	<entel:view name="zonaCanjeBolsasRecargas">
	
		<script type="text/javascript">
	
			var msisdn = "<h:outputText value="#{profile.numeroPcs}"/>";								
			var cantPasos = "<h:outputText value="#{profile.mercado == miEntelBean.siglaPrepago ? '2' : '3'}"/>";
			var esPP = "<h:outputText value="#{profile.mercado == miEntelBean.siglaPrepago ? '1' : '0'}"/>";
			var esVozBAM = "<h:outputText value="#{profile.flagBam == miEntelBean.siglaUsuarioBAM ? 'Banda Ancha Movil' : 'Telefonia'}"/>";
			var pageActual = "<h:outputText value="#{vtasYMktgFidelizacionController.pageLabelActual}"/>";
			var textoExitoBolsa = "";
			var textoExitoRecarga = "";
			var textoConfirmacionBolsa = "";
			var textoConfirmacionRecarga = "";

			$(document).ready(function(){
				
				// Se inicializa con el numero de pasos 2 para PP y 3 para SS y CC				
				$('#cont-pasos-bolsas').text('Paso 1 de '+cantPasos);
				$('#cont-pasos-recargas').text('Paso 1 de '+cantPasos);

				$('#ul_bolsas_canje a:first').addClass('seleccionado');

				// Conf mensajes confirmacion y exito
				if(esPP == '1'){					
					textoConfirmacionBolsa = "Vas a canjear la bolsa (nombre) por (puntos) puntos zona.";
					textoConfirmacionRecarga = "Vas a realizar una recarga de $(monto) por (puntos) puntos zona.";
				}
				else{					
					textoConfirmacionBolsa = "Vas a canjear la bolsa (nombre) por (puntos) puntos zona para el n\xfamero (numero).";
					textoConfirmacionRecarga = "Vas a realizar una recarga de $(monto) por (puntos) puntos zona para el n\xfamero (numero).";
				}		

				$('.fila_bolsa').addClass('clearfix');				

				tipoBolsaSeleccionada($('#ul_bolsas_canje a:first'));				
			});
			
			function canjearRecarga(e){						
				
				var msisdnDestinoRecarga = $.trim($('#msisdn_destino_recarga').val());							
				if( msisdn == msisdnDestinoRecarga){
					canjearRecargaSiMismo(e);					
				}else{
					canjearRecargaOtro(e);				
				}
				
			}

			function canjearRecargaSiMismo(e){
		    	var url='<%=request.getContextPath()%>/portlet/vtasYMktgFidelizacion/canjePuntosRecargaJson.faces';
		    	
		    	var montoRecarga = $.trim($('#monto_canje_recarga').val());
		    			    			    	
		    	var fila = $(e).parents('.marcador_fila:first');		    			    
		    	var codigoProd = fila.find('.info_producto').val().split('|')[0];
		    	var puntos = fila.find('.info_producto').val().split('|')[1];
		    	var montoRecarga = fila.find('.info_producto').val().split('|')[2];
		    	var nombreBolsa = fila.find('.desc_producto').val();		    	
		    		    	    
		    	fila.find('.fila_estado3').show();
		    	fila.find('.mensaje_exito_bolsa, .mensaje_error_bolsa').hide();
		    	
			    $.ajax({
		            type: 'POST',
		            url: url,
		            dataType: 'json',
		            data: {isRecarga: true, monto: montoRecarga, codProducto: codigoProd, puntosCambio: puntos, bolsaNombre: nombreBolsa},
		            success: function (resp){			            						                    
		    	        if(resp.estado == 'Ok'){
		    	        	if (pageActual.indexOf("puntos") != -1) { // Ingreso por page de Zona Puntos
		    	        		crearMarcaTransaccionGTM(resp, 'Personas/Mi Entel/Zona Puntos/Canje de Puntos/Recargas/Conversion', 'recargas');
			    	        } else if (pageActual.indexOf("recargas") != -1) { // Ingreso por page de Recargas
			    	        	crearMarcaTransaccionGTM(resp, 'Personas/Mi Entel/Mis Productos/' + esVozBAM + '/Recargas/Puntos Zona/Conversion', 'recargas');
			    	        } else {
			    	        	crearMarcaTransaccionGTM(resp, 'Personas/Mi Entel/Zona Puntos/Canje de Puntos/Recargas/Conversion', 'recargas');
		    	        	}
			                fila.find('.mensaje_exito_recarga .texto').html(resp.detalle);
			                fila.find('.mensaje_exito_recarga').show();			                
			                actualizarPuntos('<%=request.getContextPath()%>/portlet/vtasYMktgFidelizacion/actualizarPuntosJson.faces');		                
		    	        }else{
		    	        	fila.find('.mensaje_error_recarga .texto').html(resp.detalle);
		    	        	fila.find('.mensaje_error_recarga').show();
			    	    }		

		    	        fila.find('.fila_estado3').hide();        
						fila.find('.resultados_recarga').show();    	        
		            }
				});			
				
			    fila.find('.inputBox').val("");	
			    
		    }
			
		    function canjearRecargaOtro(e){	 			       
		    	var url='<%=request.getContextPath()%>/portlet/vtasYMktgFidelizacion/regalarPuntosJson.faces';		    	

		    	var montoRecarga = $.trim($('#monto_canje_recarga').val());
		    	var msisdnDestino = $.trim($('#msisdn_destino_recarga').val());	
		    		    			    	
		    	var fila = $(e).parents('.marcador_fila:first');
		    	var codigoProd = fila.find('.info_producto').val().split('|')[0];
		    	var puntos = fila.find('.info_producto').val().split('|')[1];
		    	var montoRecarga = fila.find('.info_producto').val().split('|')[2];
		    	var nombreBolsa = fila.find('.desc_producto').val();
		    	
		    	fila.find('.fila_estado3').show();
		    	fila.find('.mensaje_exito_bolsa, .mensaje_error_bolsa').hide();

			    $.ajax({
		            type: 'POST',
		            url: url,
		            dataType: 'json',
		            data:{isRecarga: true, monto: montoRecarga, msisdnRecibe: msisdnDestino, codProducto: codigoProd, puntosCambio: puntos, bolsaNombre: nombreBolsa},
		            success: function (resp){			            
		    	        if(resp.estado == 'Ok'){
		    	        	if (pageActual.indexOf("puntos") != -1) { // Ingreso por page de Zona Puntos
		    	        		crearMarcaTransaccionGTM(resp, 'Personas/Mi Entel/Zona Puntos/Canje de Puntos/Recargas/Conversion', 'recargas');		    	        		
			    	        } else if (pageActual.indexOf("recargas") != -1) { // Ingreso por page de Recargas
			    	        	crearMarcaTransaccionGTM(resp, 'Personas/Mi Entel/Mis Productos/' + esVozBAM + '/Recargas/Puntos Zona/Conversion', 'recargas');
			    	        } else {
			    	        	crearMarcaTransaccionGTM(resp, 'Personas/Mi Entel/Zona Puntos/Canje de Puntos/Recargas/Conversion', 'recargas');
		    	        	}
		    	        	fila.find('.mensaje_exito_recarga .texto').html(resp.detalle);
		    	        	fila.find('.mensaje_exito_recarga').show();		    	        	
			                actualizarPuntos('<%=request.getContextPath()%>/portlet/vtasYMktgFidelizacion/actualizarPuntosJson.faces');		                
		    	        }else{
		    	        	fila.find('.mensaje_error_recarga .texto').html(resp.detalle);
		    	        	fila.find('.mensaje_error_recarga').show();
			    	    }		    	        

		    	        fila.find('.fila_estado3').hide();
						fila.find('.resultados_recarga').show();
		            }
				});							
				
				fila.find('.inputBox').val("");					    	
		    }
			
			function canjearBolsa(elem){
				var msisdnDestinoBolsa = $('#msisdn_destino_bolsa').val();
				if(msisdnDestinoBolsa == msisdn){
					canjearBolsaSiMismo(elem);						
				}
				else{
					canjearBolsaOtro(elem);					
				}
			}

			function canjearBolsaSiMismo(elem){
		    	var url='<%=request.getContextPath()%>/portlet/vtasYMktgFidelizacion/canjePuntosRecargaJson.faces';
		    	
		    	var fila = $(elem).parents('.marcador_fila:first');
		    	var codigoProd = fila.find('.info_producto').val().split('|')[0];
		    	var puntos = fila.find('.info_producto').val().split('|')[1];
		    	var montoCanje = fila.find('.info_producto').val().split('|')[2];		    	
		    	var nombreBolsa = fila.find('.desc_producto').val();		    			    	
		    	
		    	fila.find('.fila_estado3').show();		

		    	fila.find('.mensaje_exito_bolsa, .mensaje_error_bolsa').hide();    	

		    	$.ajax({
		            type: 'POST',
		            url: url,
		            dataType: 'json',
		            data: {isRecarga:false, monto: montoCanje, codProducto: codigoProd, puntosCambio: puntos, bolsaNombre: nombreBolsa},
		            success: function (resp){		            	
		    	        if(resp.estado == 'Ok'){
		    	        	if (pageActual.indexOf("recargas") == -1) { // Ingreso por page de Zona Puntos
		    	        		crearMarcaTransaccionGTM(resp, 'Personas/Mi Entel/Zona Puntos/Canje de Puntos/Bolsas/' + $('#tipoBolsaActual').val() + '/Conversion', 'bolsas');		    	        		
			    	        }			    	        			    	        		    	        	
		    	        	fila.find('.mensaje_exito_bolsa .texto').html(resp.detalle);
		 	    			fila.find('.mensaje_exito_bolsa').show();
		 	    			actualizarPuntos('<%=request.getContextPath()%>/portlet/vtasYMktgFidelizacion/actualizarPuntosJson.faces');
		    	        }else{
		    	        	fila.find('.mensaje_error_bolsa .texto').html(resp.detalle);
		    	        	fila.find('.mensaje_error_bolsa').show();
			    	    }	
			    	    
		    	        fila.find('.fila_estado3').hide();        
						fila.find('.resultados_bolsa').show();       
		            }
		        });		              	      
			    
	        }

	        function canjearBolsaOtro(elem){
	        	var url='<%=request.getContextPath()%>/portlet/vtasYMktgFidelizacion/regalarPuntosJson.faces';
	        	
	        	var fila = $(elem).parents('.marcador_fila:first');
	        	var codigoProd = fila.find('.info_producto').val().split('|')[0];
	        	var puntos = fila.find('.info_producto').val().split('|')[1];
		    	var montoCanje = fila.find('.info_producto').val().split('|')[2];		    	
		    	var msisdnDestino = fila.find('.numero_destino_canje').val();
		    	var nombreBolsa = fila.find('.desc_producto').val();	        	
	        	
	        	fila.find('.fila_estado3').show();
	        	fila.find('.mensaje_exito_bolsa, .mensaje_error_bolsa').hide();

	        	$.ajax({
		            type: 'POST',
		            url: url,
		            dataType: 'json',
		            data: {isRecarga: false, monto: montoCanje, msisdnRecibe: msisdnDestino, codProducto: codigoProd, puntosCambio: puntos, bolsaNombre: nombreBolsa},
		            success: function (resp){		            	
		    	        if(resp.estado == 'Ok'){
		    	        	if (pageActual.indexOf("recargas") == -1) { // Ingreso por page de Zona Puntos
		    	        		crearMarcaTransaccionGTM(resp, 'Personas/Mi Entel/Zona Puntos/Canje de Puntos/Bolsas/' + $('#tipoBolsaActual').val() + '/Conversion', 'bolsas');
			    	        }
		    	        	fila.find('.mensaje_exito_bolsa .texto').html(resp.detalle);
		 	    			fila.find('.mensaje_exito_bolsa').show();
		 	    			actualizarPuntos('<%=request.getContextPath()%>/portlet/vtasYMktgFidelizacion/actualizarPuntosJson.faces');
		    	        }else{
		    	        	fila.find('.mensaje_error_bolsa .texto').html(resp.detalle);
		    	        	fila.find('.mensaje_error_bolsa').show();
			    	    }	
			    	    
		    	        fila.find('.fila_estado3').hide();        
						fila.find('.resultados_bolsa').show();       
		            }
		        });
	        	
		    }

	        function tipoBolsaSeleccionada(elem) {
		        if ($(elem).find('span').length) {		        
			    	var spanBolsa = $(elem).find('span').html();
		    		var tipoBolsaActual = spanBolsa.indexOf('Internet') != -1 ? 'IM' : spanBolsa.indexOf('Banda') != -1 ? 'BAM' : spanBolsa;		    	
		    		$('#tipoBolsaActual').val(tipoBolsaActual);
		    		mxTracker._trackPageview('Personas/Mi Entel/Zona Puntos/Canje de Puntos/Bolsas/' + tipoBolsaActual);
		        }
		    }

	    	function crearMarcaTransaccionGTM(resp, pageView, tipo) {
	    		var idTransaccion = resp.respuesta.idTransaccion;
	    		var codigoProducto = resp.respuesta.skuID;
	    		var nombreProducto = resp.respuesta.nombre;

	    		mxTracker._trackPageview(pageView);

				if (tipo == "bolsas") {	    		
	    			mxTracker._addTrans(idTransaccion, '', resp.respuesta.valorTransaccion);
	    			mxTracker._addItem(idTransaccion, codigoProducto, nombreProducto, 'Fee tarifa', resp.respuesta.nuevoValor, '1');
	    			mxTracker._addItem(idTransaccion, codigoProducto, nombreProducto, 'Costo cambio tarifa', resp.respuesta.costoOperacional, '1');
				} else if (tipo == "recargas") {
					var nuevoValor = resp.respuesta.nuevoValor;
					mxTracker._addTrans(idTransaccion, 'Puntos Zona', nuevoValor);
					mxTracker._addItem(idTransaccion, 'Recarga_' + nuevoValor , 'Recarga_' + nuevoValor, 'Recargas', nuevoValor, '1');
				}
				
				dataLayer = dataLayer||[];
				dataLayer.push({'event': 'tracktrans', 'tracktrans': true});
	    	}
	
		</script>							
	
		<div id="div_canje_recargas_bolsas">			
			<!-- Tabs zonapuntos -->
			<entel:view name="canjePuntos">
			
			<input type="hidden" id="tipoBolsaActual" value="" />
			
			<h2 class="ico_bolsas_zonapuntos">				
				<strong>Bolsas</strong>			
				<span id="cont-pasos-bolsas" style="display:inline-block; float:right;" class="cont-paso"></span>				
			</h2>
			
			<p>				
				<h:outputText escape="false" value="#{vtasYMktgFidelizacionController.mensajeIntroduccionBolsas}"/>			
			</p>
		
		
			<div id="div_bolsas_canje" class="caja_tabs clearfix">
								
			    <ul id="ul_bolsas_canje" class="clearfix">
			    	<f:verbatim rendered="#{!empty vtasYMktgFidelizacionController.bolsasVoz}">
			    		<li><a class="tabsZona tabZonaPunto" href="#tab-1" onclick="tipoBolsaSeleccionada(this);"><span>Voz</span></a></li>
			    	</f:verbatim>
			    	<f:verbatim rendered="#{!empty vtasYMktgFidelizacionController.bolsasSMS}">
			    		<li><a class="tabsZona tabZonaPunto" href="#tab-2" onclick="tipoBolsaSeleccionada(this);"><span>Mensajes</span></a></li>
			    	</f:verbatim>
			    	<f:verbatim rendered="#{!empty vtasYMktgFidelizacionController.bolsasMixtas}">
			    		<li><a class="tabsZona tabZonaPunto" href="#tab-3" onclick="tipoBolsaSeleccionada(this);"><span>Mixta</span></a></li>
			    	</f:verbatim>
			    	<f:verbatim rendered="#{!empty vtasYMktgFidelizacionController.bolsasBAM}">
			    		<h:panelGroup rendered="#{vtasYMktgFidelizacionController.showBolsasBAM}">
				        	<li><a class="tabsZona tabZonaPunto" href="#tab-4" onclick="tipoBolsaSeleccionada(this);"><span>Banda Ancha M&oacute;vil</span></a></li>
				        </h:panelGroup>
			    	</f:verbatim>
			    	<f:verbatim rendered="#{!empty vtasYMktgFidelizacionController.bolsasInternetMovil}">
			    		<li><a class="tabsZona tabZonaPunto" href="#tab-5" onclick="tipoBolsaSeleccionada(this);"><span>Internet M&oacute;vil</span></a></li>
			    	</f:verbatim>
					<f:verbatim rendered="#{!empty vtasYMktgFidelizacionController.bolsasCanjeBAM}">			    		
						     <li><a class="tabsZona tabZonaPunto" href="#tab-6" onclick="tipoBolsaSeleccionada(this);"><span>Banda Ancha M&oacute;vil</span></a></li>				       
					</f:verbatim>	
			    				    					    			    	  			        			        			        			       
			    </ul>
			    
			    <input id="msisdn_destino_bolsa" type="hidden" value="<h:outputText value="#{profile.numeroPcs}"/>" />			    
			    
			    <it:iterator var="listaTipoBolsa" value="#{vtasYMktgFidelizacionController.bolsas}" rowIndexVar="index">	
			    	<f:verbatim rendered="#{!empty listaTipoBolsa}">
			    		<div id="tab-<h:outputText value="#{index+1}"/>" class="tabs-container" style="display:<h:outputText value="#{index == 0 ? 'block' : 'none'}"/>">
				    		<div class="tabla_bolsas">
				    			<it:iterator var="bolsaCurrent" value="#{listaTipoBolsa}" rowIndexVar="indexBolsaCurrent">
				    				<div class="marcador_fila fila_bolsa">
				    				
				    					<div id="tooltip_tab<h:outputText value="#{index+1}"/>_bolsa<h:outputText value="#{indexBolsaCurrent+1}"/>" style="display:none">
											<strong><h:outputText value="#{bolsaCurrent.descProducto}"/></strong>
										    <p><h:outputText value="#{bolsaCurrent.letraChica}"/></p>
										</div>
				    				
				    					<input type="hidden" class="info_producto" value="<h:outputText value="#{bolsaCurrent.codProducto}|#{bolsaCurrent.puntos}|#{bolsaCurrent.monto}"/>"/>
				    					<input class="desc_producto" type="hidden" value="<h:outputText value="#{bolsaCurrent.descProducto}"/>"/>				    					
				    					
				    					<div class="fila_bolsas fila_estado1 <h:outputText value="#{(indexBolsaCurrent % 2) == 0 ? 'fila_impar' : 'fila_par'}"/> clearfix">
				    						<div class="info_bolsa_recarga" style="display:none">
												<a class="ico_interrogacionNuevo autoTooltip" href="#tooltip_tab<h:outputText value="#{index+1}"/>_bolsa<h:outputText value="#{indexBolsaCurrent+1}"/>"></a>
											</div>
											<div class="nombre_bolsa"><h:outputText value="#{bolsaCurrent.descProducto}"/></div>
											<div class="precio_bolsa"><h:outputText value="#{bolsaCurrent.puntos}"/> puntos</div>
											<h:panelGroup rendered="#{vtasYMktgFidelizacionController.tiempoMinimoRegistro}">
												<h:panelGroup rendered="#{vtasYMktgFidelizacionController.detallePuntos.saldoPuntos >= bolsaCurrent.puntos}">
													<div class="botones">												
														<c:if test="${profile.mercado eq miEntelBean.siglaPrepago}">														
															<f:verbatim rendered="#{vtasYMktgFidelizacionController.validoPrestaLuka}">
																<a class="btnVerdeDelgado ir_estado2" href="#" onclick="mxTracker._trackPageview('Personas/Mi Entel/Zona Puntos/Canje de Puntos/Bolsas/' + $('#tipoBolsaActual').val() + '/Datos');"><span>Canjear</span></a>
															</f:verbatim>
															<f:verbatim rendered="#{!vtasYMktgFidelizacionController.validoPrestaLuka}">
																<a class="btnDesactivado" href="#" onclick="return false;"><span>Canjear</span></a>
															</f:verbatim>																																							
														</c:if>
														<c:if test="${(profile.mercado eq miEntelBean.siglaCuentaControlada) or (profile.mercado eq miEntelBean.siglaSuscripcion)}">												
															<a class="btnVerdeDelgado ir_estado4" href="#" onclick="mxTracker._trackPageview('Personas/Mi Entel/Zona Puntos/Canje de Puntos/Bolsas/' + $('#tipoBolsaActual').val() + '/Datos');"><span>Canjear</span></a>
														</c:if>
													</div>
												</h:panelGroup>
												<h:panelGroup rendered="#{vtasYMktgFidelizacionController.detallePuntos.saldoPuntos < bolsaCurrent.puntos}">
													<div class="puntos_insuficientes"><cm:getProperty node="${necesitasMasPuntosBolsas[0]}" name="html"/></div>
												</h:panelGroup>
											</h:panelGroup>
											<h:panelGroup rendered="#{!vtasYMktgFidelizacionController.tiempoMinimoRegistro}">
												<div class="botones">												
													<a class="btnDesactivado" href="#" onclick="return false;"><span>Canjear</span></a>
												</div>
											</h:panelGroup>
				    					</div>
				    					
				    					
				    					<div class="fila_canje_alerta fila_estado2 clearfix">											
											<div class="mensaje_bolsa mensaje_confirmacion"></div>
											<div class="botones">
												<a id="confirmarCanjeBolsaVoz" class="btnVerdeDelgado ir_estado3" onclick="canjearBolsa(this)" onclick="mxTracker._trackPageview('Personas/Mi Entel/Zona Puntos/Canje de Puntos/Bolsas/Confirmar');" >
													<span>Confirmar</span></a>
												<a class="btnCancelar ir_estado1" href="#">Cancelar</a>											
											</div>
								   		</div>
								   		
								   		
								   		<div class="fila_estado3 fila_impar fila_bolsas" style="display:none">
								   			
									   		<div class="loading loading_canje"></div>
									   		<p style="font-weight:bold; text-align:center;">Un momento por favor...</p>																						
									   		
										</div>
										
										
										<h:panelGroup layout="block" styleClass="fila_bolsas fila_estado4 clearfix fila_impar" style="display: block;">
											
											<form name="formBolsasCanje" class="formBolsasCanje" method="get" action="#">
												<jsp:include page="/token.jsp" flush="true"/>
																								
												<div class="clearfix">
													<div class="info_bolsa_recarga" style="display:none">
														<a class="ico_interrogacionNuevo autoTooltip" href="#tooltip_tab<h:outputText value="#{index+1}"/>_bolsa<h:outputText value="#{indexBolsaCurrent+1}"/>"></a>
													</div>
													<div class="nombre_bolsa"><h:outputText value="#{bolsaCurrent.descProducto}"/></div>
													<div class="precio_bolsa"><h:outputText value="#{bolsaCurrent.puntos}"/> puntos</div>
													<div class="botones">																																									
														<a class="btnCancelarTab ir_estado1" href="#">Cancelar</a>
													</div>
												</div>
												
												
												<div class="contenido_tab clearfix">
													<div class="contenido_tab1">
														<span class="texto">Ingresa un n&uacute;mero(tipos compatibles) de destino</span>
														<div class="clearfix">
															<div class="formulario_fila clearfix">
																<div class="tabla_formulario_label">Ingresa un n&uacute;mero Entel:</div>	
																<div class="tabla_formulario_dato formulario_input campo">
																	<input type="text" maxlength="8" class="inputBox input_numerico numero_destino_canje" tabindex="1" name="numero_destino_canje">
																</div>																									
																<div class="tabla_formulario_dato texto_ejemplo">Ej. 93799181</div>																																															
															</div>
															<div class="formulario_fila clearfix">
																<div class="tabla_formulario_label">Reingresa el n&uacute;mero Entel:</div>
																<div class="tabla_formulario_dato formulario_input campo">
																	<input type="text" maxlength="8" class="inputBox input_numerico numero_destino_canje_repeticion" tabindex="1" name="numero_destino_canje_repeticion">
																</div>													
															</div>
															<div class="formulario_fila clearfix">
																<div class="tabla_formulario_label">&nbsp;</div>
																<div class="tabla_formulario_dato formulario_input campo">
																	<a class="btnVerde validar_form_bolsa" href="#" onclick="mxTracker._trackPageview('Personas/Mi Entel/Zona Puntos/Canje de Puntos/Bolsas/' + $('#tipoBolsaActual').val() + '/Confirmar');">
																		<span>Aceptar</span>
																	</a>
																</div>
															</div>																						
														</div>
													</div>																									
												</div>
												
											</form>
											
										</h:panelGroup>
																				
										
										
										<div class="resultados_bolsa fila_bolsas fila_impar" style="display:none">
											
											<div class="mensaje_exito_bolsa clearfix" style="display:none">											
										        <div class="mensaje_exito_canje">
										        	<span class="texto"></span>							        	
										        </div>										        						        							      
										        <p class="pvolver_estado1">
													<a class="ir_estado1" href="#">Realizar nuevo canje</a>
												</p>												
											</div>
											
											<div class="mensaje_error_bolsa clearfix" style="display:none">
												<div class="mensaje-error-pequeno">
													<div class="clearfix sub-contenedor">
														<div class="contenedor-imagen">
															<div class="imagen"></div>
														</div>
														<div class="texto"></div>
													</div>
												</div>
												<p class="pvolver_estado1">
													<a class="ir_estado1" href="#">Realizar nuevo canje</a>
												</p>
											</div>
										</div>																				
										
				    				</div>
				    			</it:iterator>
				    		</div>
				    	</div>
			    	</f:verbatim>		    				    	
			    </it:iterator>			      
			    
			    <h:panelGroup rendered="#{empty vtasYMktgFidelizacionController.bolsasVoz && empty vtasYMktgFidelizacionController.bolsasSMS
			    						 && empty vtasYMktgFidelizacionController.bolsasBAM && empty vtasYMktgFidelizacionController.bolsasMixtas
			    						 && empty vtasYMktgFidelizacionController.bolsasInternetMovil && empty vtasYMktgFidelizacionController.bolsasCanjeBAM}">
			    	<div class="mensaje-alerta-sistema-pequeno">
				        <div class="clearfix sub-contenedor">
				            <div class="contenedor-imagen">
				            	<div class="imagen"></div>
				            </div>
				            <div class="texto"><cm:getProperty node="${noBolsasDisp[0]}" name="html"/></div>
				        </div>
				    </div>
			    </h:panelGroup>
			    
			</div>
		 
	</entel:view>
		
			<!-- Recargas -->
			<h2 class="ico_mas_recarga">
				<strong>Recargas</strong>
				<span id="cont-pasos-recargas" style="display:inline-block; float:right;" class="cont-paso"></span>
			</h2>
			<p>				
				<h:outputText escape="false" value="#{vtasYMktgFidelizacionController.mensajeIntroduccionRecargas}"/>			
			</p>
			<br />
									
			
			<div id="div_recargas_canje" class="tabla_bolsas">
				
				<input id="monto_canje_recarga" type="hidden" value="" />
				<input id="msisdn_destino_recarga" type="hidden" value="<h:outputText value="#{profile.numeroPcs}"/>" />				
				
				<it:iterator var="recarga" value="#{vtasYMktgFidelizacionController.recargas}" rowIndexVar="index">										
										
					
					   	<div class="marcador_fila fila_recarga">
					   	
					   		<div id="tooltip_recarga<h:outputText value="#{index+1}"/>" style="display:none">
								<strong><h:outputText value="#{recarga.descProducto}"/></strong>
							    <p><h:outputText value="#{recarga.letraChica}"/></p>
							</div>
							
							<input class="desc_producto" type="hidden" value="<h:outputText value="#{recarga.descProducto}"/>"/>
					   	
					   		<input type="hidden" class="info_producto" value="<h:outputText value="#{recarga.codProducto}|#{recarga.puntos}|#{recarga.monto}"/>"/>
							
							
					   		<div class="fila_bolsas fila_estado1 <h:outputText value="#{(index % 2) == 0 ? 'fila_impar' : 'fila_par'}"/> clearfix">
								<div class="info_bolsa_recarga" style="display:none">
									<a class="ico_interrogacionNuevo autoTooltip" href="#tooltip_recarga<h:outputText value="#{index+1}"/>"></a>
								</div>
								<div class="monto_recarga">$<h:outputText value="#{recarga.monto}"><f:convertNumber locale="es" currencyCode="CLP" /></h:outputText></div>
								<div class="puntos_recarga"><h:outputText value="#{recarga.puntos}"/> puntos zona</div>
								<h:panelGroup rendered="#{vtasYMktgFidelizacionController.tiempoMinimoRegistro}">
									<h:panelGroup rendered="#{vtasYMktgFidelizacionController.detallePuntos.saldoPuntos >= recarga.puntos}">
										<div class="botones"> 
											<c:if test="${profile.mercado eq miEntelBean.siglaPrepago}">												
												<f:verbatim rendered="#{vtasYMktgFidelizacionController.validoPrestaLuka}">
													<a class="btnVerdeDelgado ir_estado2" href="#" onclick="mxTracker._trackPageview('Personas/Mi Entel/Zona Puntos/Canje de Puntos/Bolsas/' + $('#tipoBolsaActual').val() + '/Confirmar');"><span>Canjear</span></a>
												</f:verbatim>
												<f:verbatim rendered="#{!vtasYMktgFidelizacionController.validoPrestaLuka}">
													<a class="btnDesactivado" href="#" onclick="return false;"><span>Recargar</span></a>
												</f:verbatim>																						
											</c:if>
											<c:if test="${(profile.mercado eq miEntelBean.siglaCuentaControlada) or (profile.mercado eq miEntelBean.siglaSuscripcion)}">
												<h:panelGroup rendered="#{profile.flagBam != miEntelBean.siglaUsuarioBAM}">
													<a class="botonRecargarNaranja ir_estado4" href="#" onclick="mxTracker._trackPageview('Personas/Mi Entel/Mis Productos/Telefonia/Recargas/Puntos Zona/Datos');"><span></span></a>
												</h:panelGroup>
												<h:panelGroup rendered="#{profile.flagBam == miEntelBean.siglaUsuarioBAM}">
													<a class="botonRecargarNaranja ir_estado4" href="#" onclick="mxTracker._trackPageview('Personas/Mi Entel/Mis Productos/Banda Ancha Movil/Recargas/Puntos Zona/Datos');"><span></span></a>
												</h:panelGroup>																										
											</c:if>
										</div>
									</h:panelGroup>									
									<h:panelGroup layout="block" styleClass="puntos_insuficientes_recarga" rendered="#{vtasYMktgFidelizacionController.detallePuntos.saldoPuntos < recarga.puntos}">									
										<cm:getProperty node="${necesitasMasPuntosRecargas[0]}" name="html"/>									
									</h:panelGroup>
								</h:panelGroup>
								<h:panelGroup rendered="#{!vtasYMktgFidelizacionController.tiempoMinimoRegistro}">
									<div class="botones"> 
										<a class="btnDesactivado" href="#" onclick="return false;"><span>Recargar</span></a>
									</div>
								</h:panelGroup>
					   		</div>
					   		
					   		
					   		<div class="fila_canje_alerta fila_estado2 clearfix">					   										
								<div class="mensaje_bolsa mensaje_confirmacion"></div>
								
								<div class="botones">
									<a class="btnVerdeDelgado ir_estado3" onclick="canjearRecarga(this)">
										<span>Confirmar</span>
									</a>
									<a class="btnCancelar ir_estado1" href="#">Cancelar</a>								
								</div>
					   		</div>
					   		
					   		
					   		<div class="fila_estado3 fila_impar fila_bolsas" style="display:none">
					   			
					   			<!--Loading -->
						   		<div class="loading loading_canje"></div>
						   		<p style="font-weight:bold; text-align:center;">Un momento por favor...</p>																						
						   		
							</div>														
							
							
							<h:panelGroup layout="block" styleClass="fila_bolsas fila_estado4 clearfix fila_impar" style="display: block;">
								<form name="formularioRecargar" class="formularioRecargar" method="get" action="#">
									<jsp:include page="/token.jsp" flush="true"/>
									<div class="desc_canje clearfix">
										<div class="info_bolsa_recarga" style="display:none">
											<a class="ico_interrogacionNuevo autoTooltip" href="#tooltip_recarga<h:outputText value="#{index+1}"/>"></a>
										</div>
										<div class="monto_recarga">$<h:outputText value="#{recarga.monto}"><f:convertNumber locale="es" currencyCode="CLP" /></h:outputText></div>							
										<div class="puntos_recarga"><h:outputText value="#{recarga.puntos}"/> puntos zona</div>
										<div class="botones">											
											<a class="btnCancelarTab ir_estado1" href="#">Cancelar</a>
										</div>
									</div>
									<div class="contenido_tab clearfix">
										<div class="contenido_tab1">
											<span class="texto">Ingresa el n&uacute;mero del m&oacute;vil a recargar</span>
											<div class="clearfix">
												<div class="formulario_fila clearfix">
													<div class="tabla_formulario_label">Ingresa un n&uacute;mero Entel:</div>	
													<div class="tabla_formulario_dato formulario_input campo">																															
														<input type="text" maxlength="8" class="inputBox input_numerico numero_destino_canje" tabindex="1" name="numero_destino_canje">
													</div>																									
													<div class="tabla_formulario_dato texto_ejemplo">Ej. 93799181</div>																																															
												</div>
												<div class="formulario_fila clearfix">
													<div class="alerta_numero2" style="display: none;"> Ingrese un n&acute;mero Entel v&acute;lido. </div>
													<div class="tabla_formulario_label">Reingresa el n&uacute;mero Entel:</div>
													<div class="tabla_formulario_dato formulario_input campo">											
														<input type="text" maxlength="8" class="inputBox input_numerico numero_destino_canje_repeticion" tabindex="1" name="numero_destino_canje_repeticion">
													</div>													
												</div>
												<div class="formulario_fila clearfix">
													<div class="tabla_formulario_label">&nbsp;</div>
													<div class="tabla_formulario_dato formulario_input campo">
														<h:panelGroup rendered="#{profile.flagBam != miEntelBean.siglaUsuarioBAM}">
															<a class="botonAceptarNaranja validar_form_recarga" href="#" onclick="mxTracker._trackPageview('Personas/Mi Entel/Mis Productos/Telefonia/Recargas/Puntos Zona/Confirmar');"></a>
														</h:panelGroup>
														<h:panelGroup rendered="#{profile.flagBam == miEntelBean.siglaUsuarioBAM}">
															<a class="botonAceptarNaranja validar_form_recarga" href="#" onclick="mxTracker._trackPageview('Personas/Mi Entel/Mis Productos/Banda Ancha Movil/Recargas/Puntos Zona/Confirmar');"></a>
														</h:panelGroup>
													</div>
												</div>																						
											</div>
										</div>	
									</div>								
								</form>
							</h:panelGroup>														
							
							
							<div class="fila_bolsas resultados_recarga fila_impar clearfix" style="display:none">
								
								<div class="mensaje_exito_recarga clearfix" style="display:none">									
							        <div class="mensaje_exito_canje">
							        	<span class="texto"></span>							        	
							        </div>							        						       
							        <p class="pvolver_estado1">
										<a class="ir_estado1" href="#">Realizar nueva recarga</a>
									</p>
								</div>
								
								<div class="mensaje_error_recarga clearfix" style="display:none">
									<div class="mensaje-error-pequeno">
										<div class="clearfix sub-contenedor">
											<div class="contenedor-imagen">
												<div class="imagen"></div>
											</div>
											<div class="texto"></div>
										</div>
									</div>
									<p class="pvolver_estado1">
										<a class="ir_estado1" href="#">Realizar nueva recarga</a>
									</p>
								</div>
								
					   		</div>
							
						</div>					
				   	
			   	</it:iterator>	
			   	
			   	<h:panelGroup rendered="#{empty vtasYMktgFidelizacionController.recargas}">
			    	<div class="mensaje-alerta-sistema-pequeno">
				        <div class="clearfix sub-contenedor">
				            <div class="contenedor-imagen">
				            	<div class="imagen"></div>
				            </div>
				            <div class="texto"><cm:getProperty node="${noRecargasDisp[0]}" name="html"/></div>
				        </div>
				    </div>
			    </h:panelGroup>
			   	
	        </div>
		</div>
		
					
		
	</entel:view>
			
</f:view>