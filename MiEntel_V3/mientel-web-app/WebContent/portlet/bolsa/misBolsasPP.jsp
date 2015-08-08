<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="f"  uri="http://java.sun.com/jsf/core"%>
<%@ taglib prefix="h"  uri="http://java.sun.com/jsf/html"%>
<%@ taglib prefix="it" uri="http://i2b.cl/jsf/iterator/" %>
<%@ taglib prefix="r"  uri="http://www.bea.com/servers/portal/tags/netuix/render"%>
<%@ taglib uri="http://www.bea.com/servers/portal/tags/content" prefix="cm"%>
<f:view beforePhase="#{misBolsasPPController.init}">
<cm:search id="infoMsjRecarga" query="idContenido = 'infoMsjRecarga'" useCache="true"  />
<script type="text/javascript">

$("a.underline").click(function(){
	   $(this).parent().parent().find('.nota').toggle();
});	



	$(document).ready(function() {
		var param = obtenerParametroURL("tab");
		if (param == 'activas') {
			$('.tab.contenido1').removeClass('seleccionado');
			$('.contenido_tab.contenido1').css({'display': 'none'});
			$('.tab.contenido2').addClass('seleccionado');			
			$('.contenido_tab.contenido2').css({'display': 'block'});
		}
		if ($.browser.msie && parseInt($.browser.version) <= 6) {			
			if($(".masvd")!=null && $(".masvd")!='undefined'){
				$(".masvd").css( {					
				    'margin-left':'2px',					    	
				     background: 'url("../framework/skins/mientel/img/icons/icon_close.gif")' 			    	  
				});		
			} 
	     };	

	     if ($.browser.msie) {		    	
    	        	  $(".item_descripcion").css( {
	    	        	  'padding-left':'0px' 
		    	      });				    
	      }
		      
	     $('div.linea_tabs').find('div.tab.seleccionado').trigger('click');
	     
		 crearMarcaTransaccionGTM();	     
	});

	
	
	
	
	function obtenerParametroURL(name) {		
		var regexS = "[\\?&]"+name+"=([^&#]*)";
		var regex = new RegExp(regexS);
		var tmpURL = window.location.href
		var results = regex.exec(tmpURL);
	
		if (results == null) {
			return "" ;
		} else {
			return results[1];
		}
	}
</script>

<h:panelGroup rendered="#{misBolsasPPController.transGTM == null}">
	<script type="text/javascript">
		function crearMarcaTransaccionGTM() { }				
	</script>
</h:panelGroup>

<h:panelGroup rendered="#{misBolsasPPController.transGTM != null}">
	<script type="text/javascript">
		function crearMarcaTransaccionGTM() {
			var idTransaccion = '<h:outputText value="#{misBolsasPPController.transGTM.idTransaccion}" />';
			var codigoProducto = '<h:outputText value="#{misBolsasPPController.transGTM.skuID}" />';
			var nombreProducto = '<h:outputText value="#{misBolsasPPController.transGTM.nombre}" />';
			var valorTransaccion = '<h:outputText value="#{misBolsasPPController.transGTM.valorTransaccion}" />';
			var nuevoValor = '<h:outputText value="#{misBolsasPPController.transGTM.nuevoValor}" />';
			var costoOperacional = '<h:outputText value="#{misBolsasPPController.transGTM.costoOperacional}" />';
			var tipoBolsa = '<h:outputText value="#{misBolsasPPController.transGTM.tipoProducto}" />';

			dataLayer = dataLayer||[];
			dataLayer.push({
				'mx_content': 'Personas/Mi Entel/Mis Productos/Telefonia/Bolsas/Comprar Bolsas/' + tipoBolsa + '/Conversion',
				'event': 'pageview'
			});
			
			dataLayer.push({
				'transactionId': idTransaccion, 'sku': '', 'price': valorTransaccion,
				'event': 'addTrans'
			});
			
			dataLayer.push({
				'transactionId': idTransaccion, 'sku': codigoProducto, 'name': nombreProducto,
				'category': 'Fee tarifa', 'price': nuevoValor, 'quantity': '1',
				'event': 'addItem'
			});

			dataLayer.push({
				'transactionId': idTransaccion, 'sku': codigoProducto, 'name': nombreProducto,
				'category': 'Costo cambio tarifa', 'price': costoOperacional, 'quantity': '1',
				'event': 'addItem'
			});

			dataLayer.push({'event': 'tracktrans', 'tracktrans': true});
		}				
	</script>
</h:panelGroup>

<h2 class="bullet">Bolsas</h2>			
			
			<div class="linea_tabs clearfix">


				<div class="tab contenido1" onclick="mxTracker._trackPageview('Personas/Mi Entel/Mis Productos/Telefonia/Bolsas/Comprar Bolsas');">

					<span class="contratar_bolsas">
						Comprar bolsas
					</span>
				</div>

				<div class="tab contenido2" onclick="mxTracker._trackPageview('Personas/Mi Entel/Mis Productos/Telefonia/Bolsas/Bolsas Activas');">
					<span class="bolsas_contratadas">
						Bolsas Activas
					</span>
				</div>

			</div>

			
	   <div class="contenido_tabs">

		    <div class="contenido_tab contenido1">
					<br />
                    <h:panelGroup rendered="#{misBolsasPPController.resumenPlan != null}">				
					<div class="mi_saldo contratarBolsaPrepago clearfix" style="background-position: 20px 15px;">
						<div style="float:left; padding-top: 8px;">
							<strong>Saldo: $<h:outputText value="#{misBolsasPPController.resumenPlan.saldo}"><f:convertNumber currencyCode="CLP" locale="es" /></h:outputText></strong>
						</div>
						<div class="msjRecarga">
							<cm:getProperty node="${infoMsjRecarga[0]}" name="html" />
						</div>
						<div style="float:left;">
							<a class="botonRecargarNaranja" style="padding-left: 0" href="<r:pageUrl pageLabel='${planController.pageLabelRecargaEnLinea}'></r:pageUrl>"></a>
						</div>
					</div>
					
					<h:panelGroup layout="block" rendered="#{!misBolsasPPController.validoPrestaLuka}"  
								  styleClass="mensaje-alerta-sistema-pequeno">						
				        <div class="clearfix sub-contenedor">
				            <div style="width:5%" class="contenedor-imagen">
				            	<div class="imagen"></div>
				            </div>
				            <div style="width:88%" class="texto"><h:outputText escape="false" value="#{misBolsasPPController.mensajeErrorPrestaLuka}" /></div>
				        </div>
				    </h:panelGroup>
					
					<p class="descripcion" style="margin-bottom:5px;">Aqu&iacute; puedes comprar bolsas para tu Entel. Esta bolsa ser&aacute; 
					descontada solamente una vez.</p>					
		          <div class="contenedor-mensajes-verde clearfix">
	                   <div></div>
	   	               <h:messages id="messageList" 
			           styleClass="mensajes-lista"
			           errorClass="mensaje-error" 
			           infoClass="mensaje-informacion" showSummary="true" />
	               </div>	               
	          <style>
				div.confirmar {
    				background: none repeat scroll 0 0 #FFF1A8;
				}
				div.lista-bolsas .fila .contratar a.contratar {
				    display: inline;
				    margin-left: 185px;
				    text-decoration: none;
				}
				div#menu-desplegable-planes-bolsas .contratar a.confirmar {
				    text-decoration: none;
				}
				</style>
                   <h:panelGroup rendered="#{misBolsasPPController.existenBolsasDispReg}">
					<!-- Menu expandible de familias de bolsas -->					
					<div id="menu-desplegable-planes-bolsas">
					<!--bolsas plus -->	
                <h:panelGroup rendered="#{misBolsasPPController.validoBolsasPlus}">
                <cm:search id="tituloBolsasPlus" query="idContenido ='tituloBolsas'" useCache="false" />
				<div class="bolsa destacar">
				<div class="header"><a href="javascript:;"><cm:getProperty node="${tituloBolsasPlus[0]}" name="html" /></a></div>
				<div class="lista-bolsas clearfix" style="display: none">
				<it:iterator value="#{misBolsasPPController.bolsasDispPPScobAltoValor}"	var="grupoBolsa" rowIndexVar="indexGrupo">

					<!--Cuerpo de las bolsas  -->

					<it:iterator var="bolsaDisponible" 	value="#{grupoBolsa.bolsasPPDisponiblesAltoValor}" 	rowIndexVar="bolsaDispoIndex">
						<!-- fila-->
						<div class="fila clearfix">
						<div class="nombre"><a href="javascript:;" class="underline"><h:outputText value="#{bolsaDisponible.nombreBolsa}" /> </a></div>
						<div class="precio">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;$<h:outputText value="#{bolsaDisponible.valorBolsa}">
							<f:convertNumber currencyCode="CLP" locale="es" />
						</h:outputText>
						</div>
						<div class="contratar">
						<h:panelGroup rendered="#{(misBolsasPPController.resumenPlan.saldo < bolsaDisponible.valorBolsa)}">
							<div class="mensajeSaldoComprarBolsaPrepago">No tienes saldo suficiente para <br />
							realizar esta operaci&oacute;n. <a href="<r:pageUrl pageLabel='${planController.pageLabelRecargaEnLinea}'></r:pageUrl>">Ir a Recargas</a></div>
						</h:panelGroup> 
						<h:panelGroup rendered="#{(misBolsasPPController.resumenPlan.saldo > bolsaDisponible.valorBolsa)}">


							<f:verbatim rendered="#{!misBolsasPPController.validoPrestaLuka}">
								<a href="#" class="bolsa-disabled btnDesactivado"
									onclick="mxTracker._trackPageview('Personas/Mi Entel/Mis Productos/Telefonia/Bolsas/Contratar Bolsas/<h:outputText value="#{bolsaDisponible.tipoBolsaPlus}"/>/Comprar');"><span>Comprar</span></a>
							</f:verbatim>
							<f:verbatim rendered="#{misBolsasPPController.validoPrestaLuka}">
								<a href="javascript:;"	class="btnVerdeDelgado alargar contratar"
									onclick="mxTracker._trackPageview('Personas/Mi Entel/Mis Productos/Telefonia/Bolsas/Contratar Bolsas/<h:outputText value="#{bolsaDisponible.tipoBolsaPlus}"/>/Comprar');"><span>Comprar</span></a>
							</f:verbatim>

						</h:panelGroup>
						
						
						
						<div class="paso_2" style="display: none; width: 270px;">
						<div class="titulo">Vas a comprar esta bolsa</div>
						<h:form>
							<jsp:include page="/token.jsp" flush="true"/>
							<h:commandLink immediate="true" value="" action="#{misBolsasPPController.comprarBolsaPP}" styleClass="btnVerdeDelgado alargar"> <span>Confirmar</span>
								<f:param name="cartaServicio" value="#{bolsaDisponible.codBolsa }" />
								<f:param name="valorBolsa" value="#{bolsaDisponible.valorBolsa}" />
								<f:param name="nombreBolsa" value="#{bolsaDisponible.nombreBolsa}" />
								<f:param name="tipoBolsa" value="#{bolsaDisponible.tipoBolsaPlus}" />
							</h:commandLink>
							<a href="javascript:;" class="cancelar">Cancelar</a>
						</h:form>
						</div>
						
						
						
						<div class="paso_3" style="display: none;">
						<h5>Has comprado esta bolsa</h5>

						<p>La bolsa queda pendiente de activaci&oacute;n, se
						activar&aacute; dentro de 4 horas</p>
						</div>
						</div>
						<!-- nota (Descripcion -->
						<div>
						<div class="nota" style="display: none;">
						<ul>
							<li style="list-style: none; padding: 0 0 0 8px !important;" 	class="item_descripcion">
							<h:outputText escape="false" value="#{bolsaDisponible.descComercial}" />
							</li>
						</ul>
						</div>
						</div>
						<!--/nota/descripcion--></div>
						<!--/fila-->
					</it:iterator>

					<!--Fin Cuerpo de las bolsas  -->


				</it:iterator></div>
				</div>

			</h:panelGroup>
                        <!--fin bolsasplus -->
						<!-- bolsa -->
						<it:iterator value="#{misBolsasPPController.bolsasDispPPScobRegalo}" var="grupoBolsa" rowIndexVar="indexGrupo">
							<div class='<h:outputText value="#{indexGrupo % 2 == 0 ? 'bolsa par' : 'bolsa impar'}"/> <h:outputText value="#{grupoBolsa.codBolsa == misBolsasPPController.tiposBolsasMasVendidas ? ' abierto':' ' }"/>' style="position:relative;">
							   <div class="header"><a href="javascript:;" onclick="mxTracker._trackPageview('Personas/Mi Entel/Mis Productos/Telefonia/Bolsas/Comprar Bolsas/<h:outputText value="#{grupoBolsa.tipoBolsaSinAcento}"/>/Inicio');" class="<h:outputText value="#{grupoBolsa.codBolsa == misBolsasPPController.tiposBolsasMasVendidas ? 'abierto masvd':'cerrado masvd' }"/>"><h:outputText value="#{grupoBolsa.tipoBolsa}"/></a></div>
							      <div class="lista-bolsas" style="display:<h:outputText value="#{grupoBolsa.codBolsa == misBolsasPPController.tiposBolsasMasVendidas ? 'block':'none' }"/>">
							         <!--Cuerpo de las bolsas  -->
							         
							           <it:iterator var="bolsaDisponible" value="#{grupoBolsa.bolsasPPDisponibles}" rowIndexVar="bolsaDispoIndex">
										<!-- fila-->
										<div class="fila clearfix">
						                    <div class="nombre"><a href="javascript:;" class="underline"><h:outputText value="#{bolsaDisponible.nombreBolsa}"/></a></div>
						                    <div class="precio">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;$<h:outputText value="#{bolsaDisponible.valorBolsa}"><f:convertNumber currencyCode="CLP" locale="es" /></h:outputText></div>
						                    <div class="contratar">
						                       <h:panelGroup rendered="#{(misBolsasPPController.resumenPlan.saldo < bolsaDisponible.valorBolsa)}">
												<div class="mensajeSaldoComprarBolsaPrepago">
													No tienes saldo suficiente para <br/>realizar esta operaci&oacute;n. 
													<a href="<r:pageUrl pageLabel='${planController.pageLabelRecargaEnLinea}'></r:pageUrl>">Ir a Recargas</a>
												</div>
		                                        </h:panelGroup>
		                                        <h:panelGroup rendered="#{(misBolsasPPController.resumenPlan.saldo > bolsaDisponible.valorBolsa)}">
		                                        
		                                        
		                                       	<f:verbatim rendered="#{!misBolsasPPController.validoPrestaLuka}">						
											    	<a href="#" class="bolsa-disabled btnDesactivado" onclick="mxTracker._trackPageview('Personas/Mi Entel/Mis Productos/Telefonia/Bolsas/Comprar Bolsas/<h:outputText value="#{grupoBolsa.tipoBolsaSinAcento}"/>/Comprar');"><span>Comprar</span></a>   
											    </f:verbatim>
											    <f:verbatim rendered="#{misBolsasPPController.validoPrestaLuka}">						
											    	<a href="javascript:;" class="btnVerdeDelgado alargar contratar" onclick="mxTracker._trackPageview('Personas/Mi Entel/Mis Productos/Telefonia/Bolsas/Comprar Bolsas/<h:outputText value="#{grupoBolsa.tipoBolsaSinAcento}"/>/Comprar');"><span>Comprar</span></a>
											    </f:verbatim>                                        
		                                        
		                                        </h:panelGroup>
						                        <div class="paso_2" style="display:none; width:270px;">
						                            <div class="titulo">Vas a comprar esta bolsa</div>
						                            <h:form>
														<jsp:include page="/token.jsp" flush="true"/>
						                            <h:commandLink immediate="true" value="" action="#{misBolsasPPController.comprarBolsaPP}" styleClass="btnVerdeDelgado alargar"><span>Confirmar</span>
						                            <f:param name="cartaServicio" value="#{bolsaDisponible.codBolsa }"/>
						                            <f:param name="valorBolsa" value="#{bolsaDisponible.valorBolsa}"/>
						                            <f:param name="nombreBolsa" value="#{bolsaDisponible.nombreBolsa}"/>
						                            <f:param name="tipoBolsa" value="#{grupoBolsa.tipoBolsaSinAcento}"/>
						                            </h:commandLink>
						                            <a href="javascript:;" class="cancelar">Cancelar</a>
						                            </h:form>
						                        </div>
						                        <div class="paso_3" style="display:none;">
						                            <h5>Has comprado esta bolsa</h5>
		
						                            <p>La bolsa queda pendiente de activaci&oacute;n, se activar&aacute; dentro de 4 horas</p>
						                        </div>
						  					 </div>							  					 
										 	 <!-- nota (Descripcion -->
										 	<div> 
												<div class="nota"  style="display:none;" >
													<ul>													
														<li style="list-style: none;padding:0 0 0 8px;" class="item_descripcion" ><h:outputText escape="false" value="#{bolsaDisponible.descComercial}"/></li>
													</ul>
												</div>
											</div>
											<!--/nota/descripcion-->
						                </div>
				                <!--/fila-->
				                </it:iterator>
							         
							 <!--Fin Cuerpo de las bolsas  -->					         
							         
							         
							      </div>							      
							</div>
						</it:iterator>
						<!-- /bolsa -->
						
					</div>
				</h:panelGroup>
		         <h:panelGroup rendered="#{!misBolsasPPController.existenBolsasDispReg}">
				     <div class="caja amarilla margen">
						<h6><strong ><center>No hay Bolsas Disponibles.</center></strong></h6>
						</div>
				     </h:panelGroup>
				  	<!-- /Menu expandible-->
				</h:panelGroup>
				<h:panelGroup rendered="#{misBolsasPPController.resumenPlan == null}">
					     <div class="caja amarilla margen">
							<h6><strong>No se obtuvo informacion de Saldo.</strong></h6>
							</div>
				</h:panelGroup>
		</div>
		<div class="contenido_tab contenido2">
					
					<!-- tabla bolsas-->
					<table class="tabla-azul">
		
							<tr class="cabecera">
								<th width="140px">Bolsa</th>
								<th width="130px">Vencimiento</th>
								<th width="160px">Saldo</th>
							</tr>
						    <it:iterator var="bolsaComprada" value="#{misBolsasPPController.bolsasActualesPP}" rowIndexVar="bcIndex">
							<!-- fila normal -->
							<tr class="<h:outputText value="#{(bcIndex % 2) == 0 ? 'impar' : 'par'}"/>">
								<td><h:outputText value="#{bolsaComprada.nombreBolsa}"/></td>
								<td><span class="icon-activa">Vence el 
								<h:outputText value="#{bolsaComprada.fechaExpiracion}">
								<f:convertDateTime pattern="dd-MM-yyyy HH:mm:ss"/>
								</h:outputText></span></td>		                       
		                        <td><h:outputText value="#{bolsaComprada.descSaldo}"/></td>
							</tr>
							</it:iterator>
							<!-- /fila normal -->
					</table>
			    <h:panelGroup rendered="#{!misBolsasPPController.existenBolsasContratadas}">
			      <div class="mensaje-alerta-sistema-pequeno" style="width:548px;">
                            <div class="clearfix sub-contenedor">
                            <div class="contenedor-imagen">
                            <div class="imagen"></div>
                            </div>
                            <div class="texto">No tienes bolsas Activas.</div>
                            </div>
                           </div>
			     </h:panelGroup>
					<!--/tabla bolsas-->
 			</div>
	</div>
</f:view>
