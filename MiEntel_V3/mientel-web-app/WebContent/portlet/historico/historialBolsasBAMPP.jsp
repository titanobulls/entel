<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="f"  uri="http://java.sun.com/jsf/core"%>
<%@ taglib prefix="h"  uri="http://java.sun.com/jsf/html"%>
<%@ taglib prefix="c"  uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="it" uri="http://i2b.cl/jsf/iterator/" %>
<%@ taglib prefix="cm" uri="http://www.bea.com/servers/portal/tags/content" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>


<cm:search id="infoDebesSaber" query="idContenido = 'historico_recargas'" useCache="true"  />

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">



<f:view beforePhase="#{historialBolsasController.initHistorialBolsas}">
<script type="text/javascript">


var pagTotal;
	$(document).ready(function(){

		$('#paginadorTraficoDiv').css('display','none');

		$('#recargas').click(function(){

			$('#paginadorTraficoDiv').css('display','');
			activarPaginador2();
			
			});

		$('#bolsas').click(function(){

			$('#paginadorTraficoDiv').css('display','');
            activarPaginador();
		}); 
		
		$('#recargas').ready(function(){

			$('#paginadorTraficoDiv').css('display','');
			activarPaginador2();
					
			});
	});


	function activarPaginador(){

		var numPagina = 1;
		pagTotal = '<h:outputText value="#{historialBolsasController.paginaTotal}"></h:outputText>';
		//alert(pagTotal);

		if(pagTotal <= 1){
			$('#paginadorTraficoDiv').css('display','none');
		}	
		
		if(pagTotal>0){
			$("#paginadorTrafico").paginate({
				count 		: pagTotal,
				start 		: numPagina,
				display     : 5,
				border					: false,
				text_color  			: '#3a3a3a',
				background_color    	: 'none',	
				text_hover_color  		: '#2573AF',
				background_hover_color	: 'none', 
				rotate		: false,
				images		: false,
				mouse		: 'press',
				onChange: function(currval){				
					loadTableTraficoEnLinea(currval);
					desactivarBotones(currval);

				

					$("#tooltip1").remove();
					
					//eventualizaTooltips2();
					//eventualizaTextoTooltips();
					//alert('test');
				}
			});

			$('.jPag-control-back, .jPag-control-front').css('opacity',0).css('cursor','default');
	
			
			if(pagTotal < 5){
				var ancho = (pagTotal * 30);	
				$('.ulwrapdiv').css('width',ancho+'px');
			
				
				//$('.jPag-control-front').attr('style','left:'+ancho+'px !important;');
			}else{
				ancho=150;
				//$('.jPag-pages').css('width','240px');
				//$('.jPag-pages').css('margin-left','20px');
				//$('.jPag-control-front').css('left',248+'px !important');
			}
			
			
			$('.jPag-backk').click(function(){
				$('.jPag-current').parent().prev().children('a').trigger('click');
			});
	
			$('.jPag-nextt').click(function(){
				$('.jPag-current').parent().next().children('a').trigger('click');
			});

			if(pagTotal <= 5){
				$('.jPag-control-back, .jPag-control-front').css('display','none');				
				$('.ulwrapdiv').css('float','none');
				$('.ulwrapdiv').css('margin','0 auto');
			}
			
			
			loadTableTraficoEnLinea(numPagina);
			desactivarBotones("1");
			ancho2 = ancho+180;
			setTimeout('anchopaginador(ancho2);',1000);

	}

	}	

	function activarPaginador2(){

		var numPagina = 1;
		pagTotal = '<h:outputText value="#{historialBolsasController.paginaTotal2}"></h:outputText>';

		if(pagTotal <= 1){
			$('#paginadorTraficoDiv').css('display','none');
		}	 

		if(pagTotal>0){
			$("#paginadorTrafico").paginate({
				count 		: pagTotal,
				start 		: numPagina,
				display     : 5,
				border					: false,
				text_color  			: '#3a3a3a',
				background_color    	: 'none',	
				text_hover_color  		: '#2573AF',
				background_hover_color	: 'none', 
				rotate		: false,
				images		: false,
				mouse		: 'press',
				onChange: function(currval){				
					loadTableTraficoEnLinea2(currval);
						desactivarBotones(currval);

				

					//$("#tooltip1").remove();
					
					//eventualizaTooltips2();
					//eventualizaTextoTooltips();
					//alert('test');
				}
			});
			
			$('.jPag-control-back, .jPag-control-front').css('opacity',0).css('cursor','default');
						
			if(pagTotal < 5){
				var ancho = (pagTotal * 30);	
				$('.ulwrapdiv').css('width',ancho+'px');
			
				
				//$('.jPag-control-front').attr('style','left:'+ancho+'px !important;');
			}else{
				ancho=150;
				//$('.jPag-pages').css('width','240px');
				//$('.jPag-pages').css('margin-left','20px');
				//$('.jPag-control-front').css('left',248+'px !important');
				
			}
			
			
			$('.jPag-backk').click(function(){
					$('.jPag-current').parent().prev().children('a').trigger('click');
			});
		
			$('.jPag-nextt').click(function(){
					$('.jPag-current').parent().next().children('a').trigger('click');
			});

			if(pagTotal <= 5){
				$('.jPag-control-back, .jPag-control-front').css('display','none');				
				$('.ulwrapdiv').css('float','none');
				$('.ulwrapdiv').css('margin','0 auto');
			}
				
				
			
			loadTableTraficoEnLinea2(numPagina);
			desactivarBotones("1");
			ancho2 = ancho+180;
			setTimeout('anchopaginador(ancho2);',1000);
			
	}

	}
		
	
	var ancho2;
	function anchopaginador(ancho){
		
		$('.jPaginate').css('width',ancho+'px').css('margin','0 auto');
	}
	
	var lastIndex;
	function desactivarBotones(actual){
		
		if(actual==pagTotal){
			$('.jPag-control-front').css('opacity',0).children('a').css('cursor','default');
			
		}else{
			$('.jPag-control-front').css('opacity',1).children('a').css('cursor','pointer');
		}
		if(actual==1){
			$('.jPag-control-back').css('opacity',0).children('a').css('cursor','default');
		}else{
			$('.jPag-control-back').css('opacity',1).children('a').css('cursor','pointer');
		}
	}
	function loadTableTraficoEnLinea(numPagina){
		
        var path = '<%=request.getContextPath()%>';
        var url='<%=request.getContextPath()%>/portlet/historico/historialBolsasJson.faces';
		//var url='<%=request.getContextPath()%>/portlet/trafico/detalleTraficoEnLineaPPCCJson.faces';
		

		     //$('.tableTraficoEnLinea').html("<center><br><img src='../framework/skins/mientel/img/thickbox/TB_cargando.gif'/></b><br><br></center>");
		     $('.tableTraficoEnLinea').html("<center><br><img src=' "+path+"/app/framework/skins/mientel/img/thickbox/TB_cargando.gif'/></b><br><br></center>");
		     
			 $.ajax({
		            type: 'POST',
		            url: url,
		            dataType: 'html',
		            data: {numPagina:numPagina},
		            success: function (resp){
			            
			            //console.log($.trim(resp));
			            if($.trim(resp)=='noSesion'){
				            location.reload(true);
				            }
			            else{
			            	 $('.tableTraficoEnLinea').html(resp);
				            }
		            	
		            	//alert(resp);
						
		            	
		           } 
	        });

			 //eventualizaTooltips2();

	}

	function loadTableTraficoEnLinea2(numPagina){
		
        var path = '<%=request.getContextPath()%>';
        var url='<%=request.getContextPath()%>/portlet/historico/historialRecargaJson.faces';
		//var url='<%=request.getContextPath()%>/portlet/trafico/detalleTraficoEnLineaPPCCJson.faces';
		
		 
		     //$('.tableTraficoEnLinea').html("<center><br><img src='../framework/skins/mientel/img/thickbox/TB_cargando.gif'/></b><br><br></center>");
		     $('.tableTraficoEnLinea').html("<center><br><img src=' "+path+"/app/framework/skins/mientel/img/thickbox/TB_cargando.gif'/></b><br><br></center>");
		     
			 $.ajax({
		            type: 'POST',
		            url: url,
		            dataType: 'html',
		            data: {numPagina:numPagina},
		            success: function (resp){
			            
			            //console.log($.trim(resp));
			            if($.trim(resp)=='noSesion'){
				            location.reload(true);
				            }
			            else{
			            	 $('.tableTraficoEnLinea').html(resp);
				            }
		            	
		            	//alert(resp);
						
		            	
		           } 
	        });

			 //eventualizaTooltips2();

	}



	
	function moverTableTraficoEnLinea(element){
		
		if($(element).hasClass('desactivado')){
			return false;
		}
			
		if($(element).hasClass('back')){
			loadTableTraficoEnLinea(lastIndex-1);
		}else{
			loadTableTraficoEnLinea(lastIndex+1);		
		}
	}

	function tooltipClick(el){
            var el = $(el);
			if (typeof el.attr('tooltip') == 'undefined') {

				eventualizaTooltips2();
				eventualizaTextoTooltips();
				
				el.click();
			}
			return false;

		}

	function eventualizaTooltips2() {
		/**
		 * Tooltips
		 */
		 
		TOOLTIPS = $(".autoTooltip:not(.TTready), .toolTip:not(.TTready)");
		if(TOOLTIPS.length > 0) {
			
			var html = '<div id="tooltip1" class="tooltip1" style="top: 142px; left: 459px; display: none"><div class="flecha"><div class="texto">tooltip</div></div></div>';
			$('body').append(html);
			if($.browser.msie) {
				$("#tooltip1").css("backgroundImage", $("#tooltip1").css("backgroundImage").replace(/\.png/i, '.gif'));
				var divInterior = $("#tooltip1 div.flecha:first");
				divInterior.css("backgroundImage", divInterior.css("backgroundImage").replace(/\.png/i, '.gif'));
			}
			
			var prepareFunction = function() {
				var el = $(this);
				
				//if (typeof el.attr('toolTip') !== 'undefined' && el.attr('toolTip') !== false) return true;
				
				el.removeClass('activo').addClass("TTready");
				if (typeof el.attr('title') !== 'undefined' && el.attr('title') !== false) {
					el.attr('tooltip', el.attr('title')).removeAttr('title');
				}
				var inlineTooltip = (el.hasClass("toolTip") && !el.hasClass("titleTT")); //si es este tipo de tooltip entonces el texto esta en el <a>(.*)</a>
				if (inlineTooltip) {
					el.data("tooltipText", el.html()).html("&nbsp &nbsp &nbsp").addClass("ico_interrogacionNuevo").addClass("visible").css("display", "inline-block");
				}
				
				if (el.hasClass("titleTT")) {
					el.addClass('toolTip');
					el.data("tooltipText", "<strong>" + el.attr('tooltip') + "</strong>");
				}/**/
			};

			TOOLTIPS.each(prepareFunction);
		 
			TOOLTIPS.bind('click', function(){
				
				var el = $(this);
				var tt = $('#tooltip1');
				var xy = el.offset();
				var text = el.attr('tooltip');
				var inlineTooltip = el.hasClass("toolTip"); //si es este tipo de tooltip entonces el texto esta en el <a>(.*)</a>
				var titleTooltip = el.hasClass("titleTT");
				
				var href = el.attr('href');
				//var activo = (tt.find('div').text() == text || (href == tempTooltip && href != 'javascript:;' && href != '#')) && tt.is(':visible'); //estoy haciendo click a un tooltip que esta activo ?
				var activo = (el.hasClass('activo')) && tt.is(':visible');
				
				if (activo) {
					
					var buttonBackg = el.css('backgroundImage').replace('/sobre_a.gif', '/sobre_c.gif');
					el.css('backgroundImage', buttonBackg);
					
					var startTop = tt.offset().top;
					
					//if($.browser.msie && $.browser.version <= 6.0) {
					//	tt.hide();
						
					//} else {
						tt.animate({
							opacity: 0,
							top: startTop + 50 + 'px'
						}, 150);
					//}			
					
					tt.queue(function() {
						$(this).css('display','none').dequeue();
					});
					
					TOOLTIPS.removeClass('activo');
					tempTooltip = null;
				}
				else {
					
					if(el.hasClass("ext")) {
						tt.addClass("extendido");
					} else {
						tt.removeClass("extendido");
					}
					
					TOOLTIPS.each(function() {
						var buttonBackg = $(this).css('backgroundImage').replace('/sobre_a.gif', '/sobre_c.gif');
						$(this).css('backgroundImage', buttonBackg);
					});
					
					var buttonBackg = el.css('backgroundImage').replace('/sobre_c.gif', '/sobre_a.gif');
					el.css('backgroundImage', buttonBackg);
					
					if (inlineTooltip) {
						text = el.data("tooltipText");
					} else {
					var idTooltip = el.attr('href');
						if(/^[#]{1,}(\s|[a-zA-Z])*/i.test(idTooltip)) {
							text = $(idTooltip).html();
						}
					}
					
					TOOLTIPS.removeClass('activo');
					el.addClass('activo');
					tempTooltip = idTooltip;
					
					tt.find('div.texto:first').html(text)
						.end().css({
							top: xy.top - tt.height(),
							left: xy.left - 112,
							display: 'block'/*,
							zIndex: 149,
							opacity: 0 /**/
						});
						
					xy.top -= (parseInt(tt.height()) + 15);
					xy.left -= 112;
					
					if($.browser.msie && $.browser.version <= 5.0) {
						tt.css('opacity',1).css({
							top: xy.top,
							left: xy.left
						});
						
					} else {
						tt.css({ top: xy.top-50 });
						tt.animate({
							top: xy.top, 
							opacity: 1
						}, 100, function() {
							setTimeout(function() {
								if (el.hasClass('activo')) {
									el.click();
								}
							}, 10000);
						});
					}
				}
				
				return false;
			});
		 }
		/*------------------ FIN TOOLTIP -----------------*/	
	}
	

	
</script>

	<div class="linea_tabs clearfix">


				<div class="tab contenido1">

					<span class="" id="recargas" style="margin-right:14px;">
						Recargas
					</span>
				</div>

				<div class="tab contenido2">
					<span class="" id="bolsas" style="margin-right:14px;">
						Bolsas compradas
					</span>
				</div>

	</div>

	<div class="contenido_tabs">

	
	

	<div class="contenido_tab contenido1">
	
	 <h:panelGroup rendered="#{historialBolsasController.existeHistoricoRecargas!=null}">
	
		 
            <p>&nbsp;</p>
			<h1>Hist&oacute;rico de recargas</h1>
			
                <div class="header_tabla_historico clearfix">                
                    <div class="top"><span></span></div>
                    <div class="main">
                        <table>
                            <tbody>
	                            <tr>
	                                <th width="25%">Fecha</th>
	                                <th width="15%">Hora</th>
	                                <th width="35%">Medio</th>
	                                <th width="25%">Monto</th>
	                            </tr>
                        	</tbody>
                        </table>					
                    </div>
                    <div class="bottom"><span></span></div>
                </div>
              
        <h:panelGroup rendered="#{historialBolsasController.existeHistoricoRecargas}">        
                
<!--                <div class="contenido_tabla">-->
<!--				<table class="tablaFacturacion">-->
<!--					<tbody>					-->
<!--						-->
<!--						<it:iterator var="item" value="#{recargaHistoricoController.recargaHistoricoBean.detalleRecargas}" rowIndexVar="row">	-->
<!--							<c:set var="style" value="#{row % 2 == 0 ? '': 'impar'}" scope="page" />-->
<!--							-->
<!--							<tr class="<h:outputText value="#{style}"/>" >-->
<!--								<td width="25%"><h:outputText value="#{item.fechaRecarga}"><f:convertDateTime pattern="dd-MM-yyyy" locale="es" /></h:outputText></td>-->
<!--								<td width="15%"><h:outputText value="#{item.fechaRecarga}"><f:convertDateTime pattern="HH:mm" locale="es" /></h:outputText></td>-->
<!--								<td width="35%"><h:outputText value="#{item.plataformaRecarga}#{item.tipoRecarga}"></h:outputText></td>-->
<!--								<td width="25%">$ <h:outputText value="#{item.montoRecarga}"><f:convertNumber currencyCode="CLP" locale="es" /></h:outputText></td>																							-->
<!--							</tr>-->
<!--							-->
<!--						</it:iterator>-->
<!--																							-->
<!--					</tbody>-->
<!--				</table>						-->
<!--			</div>	-->
			<div class="tableTraficoEnLinea"></div>
			<div><br/></div>
			

                
			<cm:getProperty node="${infoDebesSaber[0]}" name="html" />
			
		</h:panelGroup>

		<h:panelGroup rendered="#{ !historialBolsasController.existeHistoricoRecargas }">
				<p>&nbsp;</p>
				<p align="center">No registras informaci&oacute;n de hist&oacute;rico de recargas.</p>
		</h:panelGroup>

<!--        <h:panelGroup rendered="#{!recargaHistoricoController.existeHistoricoRecargas}">     -->
<!--			<div class="mensaje-alerta-sistema-pequeno"> -->
<!--       			<div class="clearfix sub-contenedor"> -->
<!--           			<div class="contenedor-imagen"> -->
<!--           				<div class="imagen"></div> -->
<!--           			</div> -->
<!--           			<div class="texto">No se obtuvo informaci&oacute;n del hist&oacute;rico de recargas.</div> -->
<!--       			</div> -->
<!--   			</div>-->
<!--        </h:panelGroup>-->
	</h:panelGroup>

	<h:panelGroup rendered="#{historialBolsasController.existeHistoricoRecargas==null}">     
			<div class="mensaje-alerta-sistema-pequeno"> 
       			<div class="clearfix sub-contenedor"> 
           			<div class="contenedor-imagen"> 
           				<div class="imagen"></div> 
           			</div> 
           			<div class="texto">Servicio de consulta de hist&oacute;rico no disponible, intente m�s tarde.</div> 
       			</div> 
   			</div>
    </h:panelGroup>



	<!-- MENSAJES -->
	<jsp:include page="../common/messages_table.jsp"></jsp:include>
	   
	
	
	</div>
	
	
	<div class="contenido_tab contenido2">

	<h:panelGroup rendered="#{historialBolsasController.registros!=null}">
	
	<div class="estructuraTrafico">
	
    <p>Se presentan a continuaci�n las bolsas compradas en los <strong>�ltimos 2 meses a contar del d�a 23 de mayo de 2013.</strong></p>
	<h1>
		Historial Bolsas Compradas
	</h1>
					<div class="tabla tabla-trafico">
   					 <h:form>
   					 	<jsp:include page="/token.jsp" flush="true"/>
						<div  class="icono-descarga">
						<h:panelGroup rendered="#{ historialBolsasController.registros }">
							<a class="icono-descarga" href="/personas/historialBolsasXLS">Descargar en formato excel</a>
						</h:panelGroup>
						</div>
							
							<h:panelGroup rendered="#{!traficoEnLineaController.errorDetalleTraficoEnLineaPPCC}">
								<div class="header_tabla trafico clearfix">
									<div class="top"><span></span></div>
									<div class="main">
									
										<table>
											<tr class="titulos">
												<th width="106px">Nombre de la Bolsa</th>
												<th width="69px">Valor</th>
												<th width="85px">Canal de compra</th>
	                                            <th width="68px">Fecha y Hora compra</th>
												<th width="68px">Fecha y Hora env�o de SMS</th>
	                                            <th width="52px">SMS</th>
											</tr>
										</table>	
													
									</div>
									<div class="bottom"><span></span></div>
								</div>
								</h:panelGroup>	
							
															
									<div class="contenido_tabla">
											<h:panelGroup rendered="#{ !historialBolsasController.registros }">
											    <p>&nbsp;</p>
											 	<p align="center">No registras compra de bolsas en los �ltimos 2 meses.</p>
											</h:panelGroup>
											
											<h:panelGroup rendered="#{historialBolsasController.registros}">
													<div class="tableTraficoEnLinea"></div>
													<div><br/></div>
													
											</h:panelGroup>
											
											
									</div>
					</h:form>			
					</div>
					<!--/tabla-->	
						
</div>

	</h:panelGroup>
	
	<h:panelGroup rendered="#{historialBolsasController.registros==null}">     
			<div class="mensaje-alerta-sistema-pequeno"> 
       			<div class="clearfix sub-contenedor"> 
           			<div class="contenedor-imagen"> 
           				<div class="imagen"></div> 
           			</div> 
           			<div class="texto">Servicio de consulta de hist&oacute;rico no disponible, intente m�s tarde.</div> 
       			</div> 
   			</div>
    </h:panelGroup>
	
	
	
	</div>
	
	
	
	
	
	</div>
	
	<div id="paginadorTraficoDiv">
	<div id="paginadorTrafico"></div>
	
	
	
	</div>
	
						
		
	
</f:view>
