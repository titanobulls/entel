<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="f" uri="http://java.sun.com/jsf/core"%>
<%@ taglib prefix="h" uri="http://java.sun.com/jsf/html"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="r"  uri="http://www.bea.com/servers/portal/tags/netuix/render"%>
<%@ taglib uri="http://www.bea.com/servers/portal/tags/content" prefix="cm"%>
<%@ taglib prefix="pref" uri="http://www.bea.com/servers/portal/tags/netuix/preferences" %>
<f:view>
<cm:search id="scriptAdwords" query="idContenido = 'scriptAdwords'" useCache="false"/>

<pref:getPreference name="grupoOferta" var="grupoOferta"/>
<pref:getPreference name="criteriosGrupoOfertaMes" var="criteriosGrupoOfertaMes"/>
<pref:getPreference name="gruposMultiOferta" var="gruposMultiOferta"/>
<pref:getPreference name="simultaneidadPPPlus" var="simultaneidadPPPlus" defaultValue="0"/>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<script type="text/javascript">
var codOfertaBlindaje = 0;
var grupoOfertaBlindaje = "";
var prefSimultaneidadPPAV = '${simultaneidadPPPlus}';

function consultarOfertaBlindaje(){
		
	    var parametros = {"grupoOferta":'${grupoOferta}',"idCriteriosGrupo":'${criteriosGrupoOfertaMes}',"gruposMultiOferta":'${gruposMultiOferta}'};
	    var mostrarSiteIntercept = false;
		var url='<%=request.getContextPath()%>/portlet/blindaje_oferta/blindajeOfertaJson.faces';
		$.ajax( {
			type : 'POST',
			url : url,	
			data:parametros,
			dataType:'json',		
			success : function(data) {
			 
					if(data.estado == 'Ok'){
						if(data.respuesta.codigo=='0000'){
							mostrarSiteIntercept = true;
						} 
						if (data.respuesta.codigo=='0001') {
							$('#CVM').val("Si");
							codOfertaBlindaje = data.respuesta.codOferta;
							grupoOfertaBlindaje = data.respuesta.grupo;
							if ($.browser.msie && parseInt($.browser.version) <= 6) {
								$('.enlaceOferta.ofertaLBSumate').trigger('click');
							}else{
								$('.thickbox.ofertaLBSumate').click();
							}
							document.cookie='cookieSumate=1';
							
						}else if(data.respuesta.codigo=='0002'){
							$('#caja_oferta_visa').show();							
						    $('#estado_oferta_visa').html(data.respuesta.estado+"!!");

						}
						if(mostrarSiteIntercept==true){
							$('#CVM').val("No");

						}

						if($("a.enlaceTDE").length != 0){
							checkMensajes();
					    }
				   }
			   },
			 complete : function( jqXHR, status){
			 	if(prefSimultaneidadPPAV == '1' && $("a.enlaceLightboxPPAV").length != 0){
			   		checkLightboxPPAV();
			    }
			 }
		   });
}

$(document).ready(function() {
	$("a.enlaceOferta").fancybox({		
	    'overlayOpacity' : 0.5,
	    'overlayColor' : '#000000',
	    'hideOnContentClick' : false,
	    'hideOnOverlayClick' : false,
	    'showCloseButton'	:true,
	    'padding' : 'auto',
	    'scrolling' : 'no',
	    'frameWidth'  : 701,
	    'frameHeight' : 1000   
	   });
	  $("#fancy_overlay").css( {	
			width: "1400px"
	   });		
	consultarOfertaBlindaje();
});
</script>			
</head>
	<body >		
		<h:outputLink value="TB_Blindaje.faces" styleClass="thickbox ofertaLBSumate"></h:outputLink>
		<h:outputLink value="TB_Blindaje.faces" styleClass="enlaceOferta ofertaLBSumate"></h:outputLink>
		<input id="CVM" type="hidden" value="" />		
		<div class="marca-oferta" id="marca-oferta"><cm:getProperty node="${scriptAdwords[0]}" name="html" /></div>
	</body>
</html>

</f:view>