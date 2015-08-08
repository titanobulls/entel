<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="f"  uri="http://java.sun.com/jsf/core"%>
<%@ taglib prefix="h"  uri="http://java.sun.com/jsf/html"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="it" uri="http://i2b.cl/jsf/iterator/"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
 

<f:view beforePhase="#{traficoEnLineaController.initTraficoEnLineaPPCC}">
<script type="text/javascript">


var pagTotal;
	$(document).ready(function(){
		var numPagina = 1;
		pagTotal = '<h:outputText value="#{fn:length(traficoEnLineaController.paginaTotal)}"></h:outputText>';
		
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
			}
		});

		$('.jPag-control-back, .jPag-control-front').css('opacity',0).css('cursor','default');

		
		if(pagTotal < 5){
			var ancho = (pagTotal * 30);	
			$('.ulwrapdiv').css('width',ancho+'px');
			
			
			//$('.jPag-control-front').attr('style','left:'+ancho+'px !important;');
		}else{
			ancho=150;
			//$('.jPag-control-front').css('left',248+'px !important');
		}
		
		
		$('.jPag-backk').click(function(){
			$('.jPag-current').parent().prev().children('a').trigger('click');
		});

		$('.jPag-nextt').click(function(){
			$('.jPag-current').parent().next().children('a').trigger('click');
		});

		
		loadTableTraficoEnLinea(numPagina);
		desactivarBotones("1");
		ancho2 = ancho+180;
		setTimeout('anchopaginador(ancho2);',1000);
	});
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

    	/*
    	$('#cpage_'+lastIndex).removeClass('currentPage');
    	lastIndex = numPagina;
    	$('#cpage_'+numPagina).addClass('currentPage');

    	var pagTotal = '<h:outputText value="#{fn:length(traficoEnLineaController.paginaTotal)}"></h:outputText>';
		
    	$('.next, .back').removeClass('desactivado');
    	if(lastIndex == 1){
			$('.back').addClass('desactivado');
			if(pagTotal == 1){
				$('.next').addClass('desactivado');
			}
        }else if(lastIndex == pagTotal){
        	$('.next').addClass('desactivado');
        }
        */
    	
		var url='<%=request.getContextPath()%>/portlet/trafico/detalleTraficoEnLineaPPCCJson.faces';

		     $('.tableTraficoEnLinea').html("<center><br><img src='../framework/skins/mientel/img/thickbox/TB_cargando.gif'/></b><br><br></center>");
		     
			 $.ajax({
		            type: 'POST',
		            url: url,
		            dataType: 'html',
		            data: {numPagina:numPagina},
		            success: function (resp){
		            	$('.tableTraficoEnLinea').html(resp);
		           } 
	        });

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

	

	
</script>



<!-- ESTRUCTURA DE TRAFICO -->

<div class="estructuraTrafico">

	<h2 class="ico_grafico">
		<strong>Tr&aacute;fico en L&iacute;nea </strong>
	</h2>
					<div class="tabla tabla-trafico">
   					 <h:form> 
   					  	<jsp:include page="/token.jsp" flush="true"/>
						<div  class="icono-descarga">
						<h:panelGroup rendered="#{!traficoEnLineaController.errorDetalleTraficoEnLineaPPCC}">
							<a class="icono-descarga" href="/personas/traficoEnLineaXLS">Descargar en formato excel</a>
						</h:panelGroup>
						</div>
							
							<h:panelGroup rendered="#{!traficoEnLineaController.errorDetalleTraficoEnLineaPPCC}">
								<div class="header_tabla trafico clearfix">
									<div class="top"><span></span></div>
									<div class="main">
									
										<table>
											<tr class="titulos">
												<th width="106px">Tipo</th>
												<th width="69px">Destino o Emisi�n</th>
												<th width="135px">Fecha/Hora</th>
	                                            <th width="68px">Duraci�n</th>
												<th width="68px">Unidad</th>
	                                            <th width="52px">Costo</th>
												<th width="52px" class="ultimo">Saldo</th>
											</tr>
										</table>	
													
									</div>
									<div class="bottom"><span></span></div>
								</div>
								</h:panelGroup>	
							
															
									<div class="contenido_tabla align_left">
											<h:panelGroup rendered="#{traficoEnLineaController.errorDetalleTraficoEnLineaPPCC}">
												<div class="alerta-tabla-mensaje">
										            <div class="texto"><h:outputText value="#{traficoEnLineaController.errorMessage}" /></div>
											    </div>
											</h:panelGroup>
											
											<h:panelGroup rendered="#{!traficoEnLineaController.errorDetalleTraficoEnLineaPPCC}">
													<div class="tableTraficoEnLinea"></div>
													<div><br/></div>
													<div id="paginadorTrafico"></div>
											</h:panelGroup>
											
											
									</div>
					</h:form>			
					</div>
					<!--/tabla-->	
						
</div>
					<!-- /ESTRUCTURA DE TRAFICO -->	                                        						
				
</f:view>	