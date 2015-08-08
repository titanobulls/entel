<%@ page language="java" contentType="text/html; charset=ISO-8859-1" %>
<%@ taglib prefix="f"  uri="http://java.sun.com/jsf/core"%>
<%@ taglib prefix="h"  uri="http://java.sun.com/jsf/html"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="cm" uri="http://www.bea.com/servers/portal/tags/content" %>
<%@ taglib prefix="r"  uri="http://www.bea.com/servers/portal/tags/netuix/render"%>
<%@ taglib prefix="pref"  uri="http://www.bea.com/servers/portal/tags/netuix/preferences" %>
<pref:getPreference name="4GLte" var="activar"  />


<pref:getPreference name="idContenido" var="idContenido" defaultValue=" " />
<c:set var="query">idContenido = '${idContenido}'</c:set>

<f:view>

<script type="text/javascript">

	function loadTuEquipo(){
		var url='<%=request.getContextPath()%>/portlet/equipo/tuEquipoJson.faces';

		setTimeout(function(){
  			 alertaReintento('tuEquipo');    
   		},TIEMPOREINTENTO);
		
		$.ajax({
			type: 'POST',
			url: url,
			dataType: 'json',
			success: function (data){

				flagRespuestas['tuEquipo'] = '1';
				
				if(data.estado == 'Ok'){													
					var urlImg = '/personas/image?id='+data.respuesta.numeroPcs;					
					var marca = data.respuesta.marca;
					var modelo = data.respuesta.modelo;										
					
					$('#tuEquipo-img').attr('src',urlImg);					
					$('#tuEquipo-marca').text(marca);
					$('#tuEquipo-modelo').text(modelo);															

					$('#loading-tabla-tuEquipo').hide();
					$('#alerta-tabla-reintento-tuEquipo').hide();
	    			$('#contenido-tabla-tuEquipo').fadeIn();  
	    			$('.db-ultimo-equipo-lista').fadeIn();	    				    			
				}
				else{
					showErrorMessages('tuEquipo',data.detalle);
			    }								  		
				
			}
		});
	}

	function load4GLTE(){

		var url='<%=request.getContextPath()%>/portlet/equipo/tuEquipo4GLTEJson.faces';

		setTimeout(function(){
  			 alertaReintento('tuEquipo4GLTE');    
   		},TIEMPOREINTENTO);
		
		$.ajax({
			type: 'POST',
			url: url,
			dataType: 'json',
			success: function (data){

				flagRespuestas['tuEquipo'] = '1';
				
				if(data.estado == 'Ok'){
																								
					var msjEquipo4GLte = data.respuesta.msjEquipo4GLte;
					var msjSimCard4GLte = data.respuesta.msjSimCard4GLte;
					var msjPlan4GLte = data.respuesta.msjPlan4GLte;
					var mostrar4Glte = data.respuesta.mostrar4Glte;
					var url4GLte = data.respuesta.url4GLte;
					var numMensaje = data.respuesta.numMensaje;
					var idOferta = data.respuesta.idOferta;
					var tipoOferta = data.respuesta.tipoOferta;
																									
					$('#tuEquipo-simCard4GLte').html(msjSimCard4GLte);
					if(url4GLte !='' && numMensaje == '3'){						
						var men = '<span href="#" class="checkWarning activeInfo activeCursor"><div class="wrapComponentTT  wrapComponentStandardToolTip"><span>Revisa <a href="javascript:levantarLightboxTuEquipo('+"'"+url4GLte+"'"+','+"'"+idOferta+"'"+','+"'"+tipoOferta+"'"+');"  style="color:#FF9966;">AC&Aacute;</a> la oferta de planes compatibles con 4G LTE.</span></div></span><span href="#" onclick="return false;" class="checkGood"><div class="wrapComponentTT  wrapComponentStandardToolTip"><span>Revisa <a>AC&Aacute;</a>la oferta de planes compatibles con 4G LTE.</span></div></span>';	    				
	    				$('#tuEquipo-plan4GLte').html(men);	    					    					    				 				    								 				 				 								 						 	
	    			}else{
	    				$('#tuEquipo-plan4GLte').html(msjPlan4GLte);	    				 
		    		}
					if(url4GLte !='' && numMensaje == '14' || numMensaje == '12'){						
						var men = '<span href="#" class="checkWarning activeInfo activeCursor"><div class="wrapComponentTT  wrapComponentStandardToolTip"><span>Revisa <a href="javascript:levantarLightboxTuEquipo('+"'"+url4GLte+"'"+','+"'"+idOferta+"'"+','+"'"+tipoOferta+"'"+');"  style="color:#FF9966;">AC&Aacute;</a> la oferta de equipo compatibles con 4G LTE.</span></div></span><span href="#" onclick="return false;" class="checkGood"><div class="wrapComponentTT  wrapComponentStandardToolTip"><span>Revisa <a>AC&Aacute;</a>la oferta de equipos compatibles con 4G LTE.</span></div></span>';	    				
	    				$('#tuEquipo-equipo4GLte').html(men);	    					    					    				 				    								 				 				 								 						 	
	    			}else{
	    				$('#tuEquipo-equipo4GLte').html(msjEquipo4GLte);	    				 
		    		}								
	    			if(mostrar4Glte != 'off'){
		    			$("#compatibilidad4G").fadeIn();
	    			}
				}
				else{
					showErrorMessages('tuEquipo',data.detalle);
			    }								  						
			}
		});
		
	}
	

	function levantarLightboxTuEquipo(urlOferta, codOferta, grupoOferta){			
		$('#CVM').val("Si");
		codOfertaBlindaje = codOferta;
		grupoOfertaBlindaje = grupoOferta;
		url = urlOferta;
		if ($.browser.msie && parseInt($.browser.version) <= 6) {										
			$('.enlaceOferta').trigger('click');
		}else{
			$('.thickbox').click();						
		}				
		 
	}

	$(document).ready(function() {
		loadTuEquipo();
		load4GLTE();	
		$("a.enlaceOferta").fancybox({
			    'overlayOpacity' : 0.5,
			    'overlayColor' : '#000000',
			    'hideOnContentClick' : false,
			    'hideOnOverlayClick' : true,
			    'showCloseButton'	:false,
			    'padding' : 'auto',
			    'scrolling' : 'no',
			    'frameWidth'  : 701,
			    'type':'iframe',			 				    			 				  
			    'frameHeight' : 1000   
			   });
			  $("#fancy_overlay").css( {
					width: "1400px"
			   });			
	});
</script>

<h:outputLink value="TB_Blindaje_NBA_Equipo.faces" styleClass="thickbox"></h:outputLink>
<h:outputLink value="TB_Blindaje_NBA_Equipo.faces" styleClass="enlaceOferta"></h:outputLink>
<input id="CVM" type="hidden" value="" />

	<c:if test="${activar!='on'}">
<div class="cajalinks">
	<div class="cabecera naranja">

		<h1>Tu equipo</h1>
	</div>
	<div class="cuerpo">
	
		
		<div class="db-ultimo-equipo">
			<div class="alerta-tabla-reintento" id="alerta-tabla-reintento-tuEquipo"></div>
			<div id="loading-tabla-tuEquipo"></div>				
		    <div id="contenido-tabla-tuEquipo">
		    	<p>El &uacute;ltimo equipo registrado en la red es:</p><br>
				<div align="center"><img id="tuEquipo-img" src="" alt="Ultimo Equipo Registrado"/></div>
				<p class="db-ultimo-equipo-nombre">
					<strong>									
						<span id="tuEquipo-marca"></span>&nbsp;
						<span id="tuEquipo-modelo"></span>
				    </strong>
				</p>
		    </div>		    
		</div>				
		
		<br>
		<!-- LISTA DE LINKS -->
		  <cm:search id="linkTuEquipo" query="${query}" useCache="false"  />
		  <cm:getProperty node="${linkTuEquipo[0]}" name="html" default=" "/>
		<!--/ LISTA DE LINKS -->
		
	</div>
	
	<div class="pie"></div>	
</div>	
	</c:if>

	<c:if test="${activar=='on'}">
					<div class="wrapRowInfo">
					<div class="cellRowInfoHeader">
						<h3>Tu equipo</h3>
					</div>
					<h:panelGroup rendered="#{equipoController.marcaEquipo == 'Equipo no reconocido'}">
						<div class="cellRowInfoCellphone">
							<div class="cellImg">						
								<img id="tuEquipo-img2" src="<%=request.getContextPath()%>/portlet/equipo/img/generico.png"  />
							</div>						
							<div class="cellTitle">
								<h4><span id="tuEquipo-marca"></span>
								<span id="tuEquipo-modelo"></span></h4>
							</div>
						</div>
					</h:panelGroup>
					
					<h:panelGroup rendered="#{equipoController.marcaEquipo != 'Equipo no reconocido'}">
						<div class="cellRowInfoCellphone">
							<div class="cellImg">
								<img id="tuEquipo-img" src="" />                                                        
                            </div>
							<div class="cellTitle">
								<h4>
									<span id="tuEquipo-marca"></span>
									<span id="tuEquipo-modelo"></span>
								</h4>
                            </div>
                        </div>
					</h:panelGroup>
					
					<div id="compatibilidad4G" class="cellRowInfoVerify" style="display:none;">
						<div class="wrapCellVerify">
							<h5>Compatibilidad de tu equipo con 4G LTE:</h5>
							<div class="rowVerifyItem">
								<p>Equipo compatible</p>
								<span id="tuEquipo-equipo4GLte"></span>
								<a href="#" onclick="return false;" class="checkGood"></a>	
							</div>
							<div class="rowVerifyItem">
								<p>Plan compatible</p>																								 									 		  								 
										<span id="tuEquipo-plan4GLte"></span>
										<a href="#" onclick="return false;" class="checkGood"></a>									 	
							</div>
							<div class="rowVerifyItem">
								<p>SIM Card compatible</p>
								<span id="tuEquipo-simCard4GLte"></span>
							</div>
						</div>
					</div>
	 				
					<div class="cellRowInfoLinks">
						<div class="cellBlock">
							<a target="_self" href="https://mi.entel.cl/personas/app/portal/mientel?_nfpb=true&_pageLabel=ss_equipo_page&action=bloquear">Bloquear</a>
						</div>
						<div class="cellTecService">
							<a target="_self" href="https://mi.entel.cl/personas/app/portal/mientel?_nfpb=true&_pageLabel=ss_equipo_page&action=servicio">Servicio t&eacute;cnico</a>
						</div>					
						<div class="cellStepDialog">
							<a href="http://ayudaequipos.entel.cl/web/" target="_blank">Manual paso a paso</a>
						</div>						
					</div>
				</div>
	</c:if>
</f:view>
