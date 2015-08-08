<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="f"  uri="http://java.sun.com/jsf/core"%>
<%@ taglib prefix="h"  uri="http://java.sun.com/jsf/html"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="r"  uri="http://www.bea.com/servers/portal/tags/netuix/render"%>
<%@ taglib uri="http://www.bea.com/servers/portal/tags/content" prefix="cm"%>
<%@ taglib prefix="pref" uri="http://www.bea.com/servers/portal/tags/netuix/preferences" %>
<cm:search id="linkSoporteEntelPCS" query="idContenido = 'linkSoporteEntelPCS'" useCache="true"  />
<pref:getPreference name="4GLte" var="activar"/>

<f:view beforePhase="#{equipoController.initEquipos}">
<h1>Equipo</h1>
<!-- MENSAJES -->
<jsp:include page="../common/messages_table.jsp"></jsp:include>

<h:panelGroup rendered="#{equipoController.pinPuk!=null}">
	<c:if test="${activar!='on'}">
				<div class="descripcion_equipo clearfix">
					<div class="imagen_equipo">
						<div class="pngFix"> 
	                    	<h:graphicImage url="/image?id=#{equipoController.numeroPcs}.png" width="90px" height="111px" alt=""/> 
	                    </div>						
						<div class="descripcion">
							<span><h:outputText value="#{equipoController.resumenEquipo.marca}" />&nbsp;<h:outputText value="#{equipoController.resumenEquipo.modelo}" /></span>
							<cm:getProperty node="${linkSoporteEntelPCS[0]}" name="html" />						
						</div>
					</div>
	
					<div class="pinpuk">
						<div class="cuadropinpuk">
							<span><strong>Pin:</strong> <h:outputText value="#{equipoController.pinPuk.pin1}" /></span>
							<span><strong>Puk:</strong> <h:outputText value="#{equipoController.pinPuk.puk1}" /></span>
						</div>
						<h:outputLink value="pinPuk.faces" styleClass="pinPukBox" onclick="mxTracker._trackPageview('Personas/Mi Entel/Mis Productos/Telefonia/Equipo/Pin y Puk');">&iquest;Qu&eacute; es Pin y Puk?</h:outputLink>					
					</div>
				</div>
	</c:if>	
	<c:if test="${activar=='on'}">		
				<div class="descripcion_equipo description_equ clearfix">
					<div class="imagen_equipo equipImage">
					
						<h:panelGroup rendered="#{equipoController.marcaEquipo == 'Equipo no reconocido'}">
							<img id="tuEquipo-img2" src="<%=request.getContextPath()%>/portlet/equipo/img/generico.png"  />        
                        </h:panelGroup>
                        <h:panelGroup rendered="#{equipoController.marcaEquipo != 'Equipo no reconocido'}">
							<h:graphicImage url="/image?id=#{equipoController.numeroPcs}.png"  alt=""/>
                        </h:panelGroup>					
						
						<!--Begin info: -->
						<div class="fLeft cellRowInfoVerify">
							<div class="wrapCellVerify">
								<h4>
									<h:outputText value="#{equipoController.resumenEquipo.marca}" />
									&nbsp;<h:outputText value="#{equipoController.resumenEquipo.modelo}" />
								</h4>										
								<h:panelGroup rendered="#{equipoController.resumenEquipo.mostrar4Glte !='off'}">
									<h5>Compatibilidad de tu equipo con 4G LTE:</h5>																																																								
									<div class="rowVerifyItem">
										<p>Equipo compatible</p>
										<h:panelGroup rendered="#{equipoController.resumenEquipo.url4GLte !='' && !equipoController.dasBoard && equipoController.resumenEquipo.numMensaje == '12' || equipoController.resumenEquipo.numMensaje == '14'}">
											<span href="#" class="checkWarning activeInfo activeCursor">
												<div class="wrapComponentTT  wrapComponentStandardToolTip">
													<span>Revisa <a style="color:#FF9966;" href="javascript:levantarLightbox2('<h:outputText value="#{equipoController.resumenEquipo.url4GLte}"/>','<h:outputText value="#{equipoController.resumenEquipo.idOferta}"/>','<h:outputText value="#{equipoController.resumenEquipo.tipoOferta}"/>');">AC&Aacute;</a> la oferta de equipos compatibles con 4G LTE.</span>
												</div>
											</span>
											<span href="#" onclick="return false;" class="checkGood">
												<div class="wrapComponentTT  wrapComponentStandardToolTip">
													<span>Revisa <h:outputLink styleClass="thickbox" style="color:#FF9966;">AC&Aacute;</h:outputLink> la oferta de equipos compatibles con 4G LTE.</span>
												</div>
											</span>	
										</h:panelGroup>	
										<h:panelGroup rendered="#{!equipoController.dasBoard && equipoController.resumenEquipo.numMensaje != '12' || equipoController.resumenEquipo.numMensaje != '14'}">
											<h:outputText escape="false"  value="#{equipoController.resumenEquipo.msjEquipo4GLte}"></h:outputText>																							
											<a href="#" onclick="return false;" class="checkGood"></a>
										</h:panelGroup>										
									</div>
									<div class="rowVerifyItem">
										<p>Plan compatible</p>																													
										<h:panelGroup rendered="#{equipoController.resumenEquipo.url4GLte !='' && !equipoController.dasBoard && equipoController.resumenEquipo.numMensaje == '3'}">										
											<span href="#" class="checkWarning activeInfo activeCursor">
												<div class="wrapComponentTT  wrapComponentStandardToolTip">
													<span>Revisa <a style="color:#FF9966;" href="javascript:levantarLightbox2('<h:outputText value="#{equipoController.resumenEquipo.url4GLte}"/>','<h:outputText value="#{equipoController.resumenEquipo.idOferta}"/>','<h:outputText value="#{equipoController.resumenEquipo.tipoOferta}"/>');">AC&Aacute;</a> la oferta de planes compatibles con 4G LTE.</span>
												</div>
											</span>
											<span href="#" onclick="return false;" class="checkGood">
												<div class="wrapComponentTT  wrapComponentStandardToolTip">
													<span>Revisa <h:outputLink styleClass="thickbox" style="color:#FF9966;">AC&Aacute;</h:outputLink> la oferta de planes compatibles con 4G LTE.</span>
												</div>
											</span>										
										</h:panelGroup>
										<h:panelGroup rendered="#{equipoController.resumenEquipo.numMensaje != '3'}">										
											<h:outputText escape="false"  value="#{equipoController.resumenEquipo.msjPlan4GLte}"></h:outputText>									
										</h:panelGroup>																																					
									</div>
									<div class="rowVerifyItem">
										<p>SIM Card compatible</p>
										<h:outputText escape="false"  value="#{equipoController.resumenEquipo.msjSimCard4GLte}"></h:outputText>							
										<a href="#" onclick="return false;" class="checkGood"></a>																		
									</div>
								</h:panelGroup>
							</div>						
						</div>
					<!-- End info -->
					</div>
					<div class="pinpuk">
						<div class="cuadropinpuk">
							<span><strong>Pin:</strong> <h:outputText value="#{equipoController.pinPuk.pin1}" /></span>
							<span><strong>Puk:</strong> <h:outputText value="#{equipoController.pinPuk.puk1}" /></span>
						</div>
							<!-- <a href="#">&iquest;Qu&eacute; es Pin y Puk?</a> -->
							<h:outputLink value="pinPuk.faces" styleClass="pinPukBox" onclick="mxTracker._trackPageview('Personas/Mi Entel/Mis Productos/Telefonia/Equipo/Pin y Puk');">&iquest;Qu&eacute; es Pin y Puk?</h:outputLink>									
					</div>				
					<div class="metaInfo">
						<p>* Te invitamos  a conocer m&aacute;s sobre tu equipo en el <a href="http://ayudaequipos.entel.cl/web/" target="_blank">manual paso a paso</a></p>
					</div>				 
				</div>
	</c:if>
</h:panelGroup>

<script type="text/javascript">
function levantarLightbox2(urlOferta, codOferta, grupoOferta){			
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
			
</f:view>