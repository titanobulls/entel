<%@ page language="java" contentType="text/html;charset=UTF-8"%>

<%@ taglib prefix="f"  uri="http://java.sun.com/jsf/core"%>
<%@ taglib prefix="h"  uri="http://java.sun.com/jsf/html"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="cm" uri="http://www.bea.com/servers/portal/tags/content" %>

<!-- Contenidos - HTML -->
<cm:search id="htmlMensajeServicios" query="idContenido = 'htmlMensajeServicios'" useCache="true" />
<cm:search id="htmlFilaHeadHistorial" query="idContenido = 'htmlFilaHeadHistorial'" useCache="true" />
<cm:search id="htmlFilaHeadParHistorial" query="idContenido = 'htmlFilaHeadParHistorial'" useCache="true" />
<cm:search id="htmlColumna1" query="idContenido = 'htmlColumna1'" useCache="true" />
<cm:search id="htmlColumna2" query="idContenido = 'htmlColumna2'" useCache="true" />
<cm:search id="htmlColumna3" query="idContenido = 'htmlColumna3'" useCache="true" />
<cm:search id="htmlColumna4" query="idContenido = 'htmlColumna4'" useCache="true" />
<cm:search id="htmlFilaBodyParHistorial" query="idContenido = 'htmlFilaBodyParHistorial'" useCache="true" />
<cm:search id="htmlBodyBloqueado" query="idContenido = 'htmlBodyBloqueado'" useCache="true" />
<cm:search id="htmlCuadroTooltip" query="idContenido = 'htmlCuadroTooltip'" useCache="true" />
<!-- Contenidos - Mensajes -->
<cm:search id="msjConfirmarDeshabilitarBloquearFamilia1" query="idContenido = 'msjConfirmarDeshabilitarBloquearFamilia1'" useCache="true" />
<cm:search id="msjConfirmarDeshabilitarBloquearFamilia2" query="idContenido = 'msjConfirmarDeshabilitarBloquearFamilia2'" useCache="true" />
<cm:search id="msjConfirmarDesbloquearFamilia1" query="idContenido = 'msjConfirmarDesbloquearFamilia1'" useCache="true" />
<cm:search id="msjConfirmarDesbloquearFamilia2" query="idContenido = 'msjConfirmarDesbloquearFamilia2'" useCache="true" />
<cm:search id="msjConfirmarHabilitarFamilia1" query="idContenido = 'msjConfirmarHabilitarFamilia1'" useCache="true" />
<cm:search id="msjConfirmarHabilitarFamilia2" query="idContenido = 'msjConfirmarHabilitarFamilia2'" useCache="true" />
<cm:search id="msjNoInfoServicios" query="idContenido = 'msjNoInfoServicios'" useCache="true" />
<cm:search id="msjServicioSinFamilia" query="idContenido = 'msjServicioSinFamilia'" useCache="true" />
<cm:search id="msjDescFiltrosServicio" query="idContenido = 'msjDescFiltrosServicio'" useCache="true" />
<cm:search id="msjNoHabilitarServicios" query="idContenido = 'msjNoHabilitarServicios'" useCache="true" />
<cm:search id="msjNoDeshabilitarServicios" query="idContenido = 'msjNoDeshabilitarServicios'" useCache="true" />
<cm:search id="msjNoBloqueoServicios" query="idContenido = 'msjNoBloqueoServicios'" useCache="true" />
<cm:search id="msjNoDesbloqueoServicios" query="idContenido = 'msjNoDesbloqueoServicios'" useCache="true" />

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<f:view >

<c:set var="aaa" value="${profile.aaa}" />


<script type="text/javascript">

var pagTotal;

var listaPaginas;
var catalogoflag;
var sampleData=[];
var paramData=[];
var Numxpaginas = '<h:outputText value="#{administracionServicios.paginastotal}"></h:outputText>';
var habilitado=  '<h:outputText value="#{administracionServicios.habilitados}"></h:outputText>';
var deshabilitado= '<h:outputText value="#{administracionServicios.deshabilitados}"></h:outputText>';
var bloqueado= '<h:outputText value="#{administracionServicios.bloqueados}"></h:outputText>';
var enableCerrar=true;
var catalogoservicios; // variable global de servicios

var htmlBody="";
$(document).ready(function() {
    $(".tabla_informacion .tabla_fila").find('.columna3').html("<strong>Categor&iacute;as</strong>");
    $('select[name="ssSubcategoria"]').get(0).disable();
	loadTableHistorialReclamos();



	//activarPaginador();
	//$('input.inputBox').inputBox();
	//$('select.selectBox').selectBox();
});



$(function(){

	   var zIndexNumber = 10000;
	    $('div').each(function() {
	        $(this).css('zIndex', zIndexNumber);
	        zIndexNumber -= 10;
	    });




	$('#btSSBuscar').click(function(){
		if(!$(this).hasClass('btnAzulGrandeDesactivado')){
			console.log('Buscar...');
		}
		return false;
	});




	$('select[name="ssCategoria"]').live('change',function(){

		if($(".autoTooltip").hasClass('activo')){
			$(".autoTooltip.activo").trigger("click");
		}

		if($(".autoTooltip").hasClass('activo')){
			$(".autoTooltip.activo").trigger("click");
		}


		$('select[name="ssSubcategoria"]').get(0).setValue("");

		if($(this).val() != "" && $(this).val() !=null){

			$('select[name="ssSubcategoria"]').get(0).enable();

		}else{

			$('select[name="ssSubcategoria"]').get(0).disable();
		}

		$("select[name*=ssSubcategoria]").html('');



			$("select[name*=ssSubcategoria]").append("<option value='' selected>Selecciona</option>");
			var listaCategorias = catalogo.categorias;
			for(index in listaCategorias){

				var category = listaCategorias[index];
				if(category.idcategoria==$(this).val()){
					var SubCategorias = category.subcategoria;
					for(index2 in SubCategorias){
						 var subcategory = SubCategorias[index2];
						 $("select[name*=ssSubcategoria]").append('<option value="'+subcategory.idsubcategoria+'">'+subcategory.nomsubcategoria+'</option>');
					}

				}
			}


			/*if($(this).val() != ""){
	          $("#tabla_informacion").show();
	          $(".ssTituloTabla").show();

			  cambiarPaginaHistorialServicios('0','search');
			}else{
		          $("#tabla_informacion").show();
		          $(".ssTituloTabla").text("Todos los Servicios");
		          $(".ssTituloTabla").show();
		      	$("#paginador-servicios").show();
				cambiarPaginaHistorialServicios('0','all');


			}*/

			var ssCategoria = $(this).val()!=null? $(this).val():"";
			var ssEstado = $(".ssEstado").val()!=null?$(".ssEstado").val():"";
			var ssSubcategoria = $('select[name="ssSubcategoria"]').val();

			if (ssCategoria == "" && ssSubcategoria == "" && ssEstado == "") {

				  $("#tabla_informacion").show();
		          $(".ssTituloTabla").text("Todos los Servicios");
		          $(".ssTituloTabla").show();
		          $("#paginador-servicios").show();
		      	  $('#mensaje-servicios').hide();
				   cambiarPaginaHistorialServicios('0', 'all');
			}else{
				cambiarPaginaHistorialServicios('0','search');
			}



	});

	$('select[name="ssSubcategoria"]').live('change',function(){

		if($(".autoTooltip").hasClass('activo')){
			$(".autoTooltip.activo").trigger("click");
		}

		var ssCategoria = $('select[name="ssCategoria"]').val()!=null?$('select[name="ssCategoria"]').val():"";
		var ssEstado = $(".ssEstado").val()!=null?$(".ssEstado").val():"";
		var ssSubcategoria = $(this).val();

		if (ssCategoria == "" && ssSubcategoria == "" && ssEstado == "") {
			  $("#tabla_informacion").show();
	          $(".ssTituloTabla").text("Todos los Servicios");
	          $(".ssTituloTabla").show();
	          $("#paginador-servicios").show();
	      	  $('#mensaje-servicios').hide();
			   cambiarPaginaHistorialServicios('0', 'all');
		}else{
			cambiarPaginaHistorialServicios('0','search');
		}


	});
	$('.ssEstado').change(function(){

		if($(".autoTooltip").hasClass('activo')){
			$(".autoTooltip.activo").trigger("click");
		}

		/*if($(this).val()!="" && $(this).val()!=null){


		}else{

			cambiarPaginaHistorialServicios('0','all');
		}*/


		var ssCategoria = $('select[name="ssCategoria"]').val()!=null?$('select[name="ssCategoria"]').val():"";
		var ssSubcategoria = $('select[name="ssSubcategoria"]').val();
		var ssEstado = $(this).val()!=null? $(this).val():"";

		if (ssCategoria == "" && ssSubcategoria == "" && ssEstado == "") {

			  $("#tabla_informacion").show();
	          $(".ssTituloTabla").text("Todos los Servicios");
	          $(".ssTituloTabla").show();
	          $("#paginador-servicios").show();
	      	  $('#mensaje-servicios').hide();
			  cambiarPaginaHistorialServicios('0', 'all');
		}else{
			cambiarPaginaHistorialServicios('0','search');
		}

	});

	$(".input_numerico").live('keypress',function(evt){
		var key = evt.keyCode ? evt.keyCode : evt.which ;
		return (key <= 31 || (key >= 48 && key <= 57));
	});




	$('.btSSHabilitaContinuar').live('click',function(){

		var vertag= $(this).parents('.tabla_fila');
		if(!$(this).hasClass('btnAzulGrandeDesactivado')){
			var $LFromHab = $(this).parents('form[name="formServicioHabilitaBloquea"]');
			if($LFromHab.find('input[name="fshHabilitar"]:checked').val() == "Habilitar"){
				dataLayer.push({
        			'mx_content': 'Personas/Mi Entel/Mis Productos/Telefonia/Servicios de Suscripcion/' + vertag.find('.columna3').text() + '/' + vertag.find('.columna1').text() + '/'+ vertag.find('.columna4').text() +'/Habilitar',
        			'event': 'pageview'});
				 $(this).parents('.tsvContent').hide();
				  $(this).parents('.tsvContent').next('.tsvContent').show();

	       	}else{
        		dataLayer.push({
        			'mx_content': 'Personas/Mi Entel/Mis Productos/Telefonia/Servicios de Suscripcion/' + vertag.find('.columna3').text() + '/' + vertag.find('.columna1').text() + '/'+ vertag.find('.columna4').text() +'/Bloquear',
        			'event': 'pageview'});
	            $(this).parents('.tsvContent').hide();
	            $(this).parents('.tsvContent').nextAll('.tsvPasoFormDeshab').show();
	       }

		}
        return false;
    });


	$('.btSSHabilitaConfirmar').live('click', function(){
    var vertag= $(this).parents('.tabla_fila');
    dataLayer.push({
	'mx_content': 'Personas/Mi Entel/Mis Productos/Telefonia/Servicios de Suscripcion/' + vertag.find('.columna3').text() + '/' + vertag.find('.columna1').text() + '/'+ vertag.find('.columna4').text() +'/Habilitado',
	'event': 'pageview'});
		var $LFrmHab = $(this).parents('.tiSSCajaVerMas').find('form[name="formServicioHabilitaBloquea"]');

			if($LFrmHab.find('input[name="fshHabilitar"]:checked').val() == "Habilitar"){
				$('.ffhLabel').css("width","141px");
				var $LFromHabil = $(this).parents('.tiSSCajaVerMas').find('.fsaFormHabilitar');
				if ($LFromHabil.find("#vasId").val()=="MMSINFO"){
					$LFromHabil.find("#houragen").removeClass("required");
					$LFromHabil.find("#minutoagen").removeClass("required");
					var selectInicioAgen=$LFromHabil.find("#horatime");
					selectInicioAgen.empty().append("<option value="+">--</option>");
					for (i=0;i<=23;i++){
						if(i<10){
							selectInicioAgen.append("<option value=0"+i+">0"+i+"</option>");
						}else{
							selectInicioAgen.append("<option value="+i+">"+i+"</option>");
						}

					}


					var selectFinAgen=$LFromHabil.find("#minutotime");
					selectFinAgen.empty().append("<option value=''>--</option>");
					for (i=0;i<60;i++){
						if(i<10){
							selectFinAgen.append("<option value=0"+i+">0"+i+"</option>");
						}else{
							selectFinAgen.append("<option value="+i+">"+i+"</option>");
						}

					}
					$(this).parents('.tsvContent').hide();
					$(this).parents('.tsvContent').nextAll('.tsvPasoForm').show();
				}else{
					if ($LFromHabil.find("#vasId").val()=="SMSINFO" ){
						$LFromHabil.find("#dia").removeClass("required");
						$LFromHabil.find("#diasusc").removeClass("required");
						$LFromHabil.find("#overriding").removeClass("required");
						$LFromHabil.find("#smsagenda").removeClass("required");
						$LFromHabil.find("#selectdias").removeClass("required");
						$LFromHabil.find("#horatime").removeClass("required");
						$LFromHabil.find("#minutotime").removeClass("required");
						$LFromHabil.find("#fsaParametro4").removeClass("required");

						var selectInicioAgen=$LFromHabil.find("#houragen");
						selectInicioAgen.empty().append("<option value=''>--</option>");
						for (i=0;i<=23;i++){
							if(i<10){
								selectInicioAgen.append("<option value=0"+i+">0"+i+"</option>");
							}else{
								selectInicioAgen.append("<option value="+i+">"+i+"</option>");
							}

						}

						var selectFinAgen=$LFromHabil.find("#minutoagen");
						selectFinAgen.empty().append("<option value=''>--</option>");
						for (i=0;i<60;i=i+15){
							if(i<10){
								selectFinAgen.append("<option value="+i+">0"+i+"</option>");
							}else{
								selectFinAgen.append("<option value="+i+">"+i+"</option>");
							}

						}
						$(this).parents('.tsvContent').hide();
						$(this).parents('.tsvContent').nextAll('.tsvPasoForm').show();

					}else{



						habilitarServicio($LFromHabil);
					}
				}


		}else{

			var $LFila = $(this).parents('.tabla_fila');
			$LFila.find('.tsvCerrar').hide();
			$(this).parents('.tiSSCajaVerMas').hide();
			$LFila.find('.columna3').hide();
			$LFila.find('.columna4').hide();
			$LFila.find('.columna5').hide();
			$LFila.find('.ccMsjBloqueado').show();
			$LFila.find('.tiSSEnlVerMas').show();
		}

		return false;
	});



	/* /DATAPICKER */

	$('.tiSSEnlVerMas').live('click',function(){
		var vertag= $(this).parents('.tabla_fila');
		dataLayer = dataLayer||[];
		dataLayer.push({
			'mx_content': 'Personas/Mi Entel/Mis Productos/Telefonia/Servicios de Suscripcion/' + vertag.find('.columna3').text() + '/' + vertag.find('.columna1').text() + '/'+ vertag.find('.columna4').text() +'/Ver',
			'event': 'pageview'
		});
		if ($(".autoTooltip").hasClass('activo')) {
			$(".autoTooltip.activo").trigger("click");
		}
		if(enableCerrar){
			var $LTablePar = $('.tiSSEnlVerMas').parents('.tiServicioSuscri');
			$LTablePar.find('.tsvCerrar').hide();
			$LTablePar.find('.tiSSCajaVerMas').hide();
			$LTablePar.find('.tiSSEnlVerMas').show();
			$(this).hide();
			$(this).next().show();
	        $(this).parents('.tabla_fila').find('.tiSSCajaVerMas').show();
	        return false;
		}
	});

	$('.tsvCerrar').live('click',function(){
		if(enableCerrar){

			$(this).hide();
	        $(this).parents('.tabla_fila').find('.tiSSCajaVerMas').hide();
	        $(this).prev().show();
		}

	});


	$('input[name="fshHabilitar"]').live('click',function(){
		var $LSSCont = $(this).parents('form[name="formServicioHabilitaBloquea"]').find('.btSSHabilitaContinuar');
		if($(this).val() == "Habilitar"){
			$(this).parents('.fsdItem').nextAll('.fsAceptCondiciones').show();
			if(!$(this).parents('.fsdItem').nextAll('.fsAceptCondiciones').find('input[name="fshTerCondiciones"]').is(':checked')){
				if(!$LSSCont.hasClass('btnAzulGrandeDesactivado')){
					$LSSCont.addClass('btnAzulGrandeDesactivado').removeClass('btnAzulGrande');
				}
			}
		}else{
			$(this).parents('.fsdItem').nextAll('.fsAceptCondiciones').hide();
			if($LSSCont.hasClass('btnAzulGrandeDesactivado')){
				$LSSCont.addClass('btnAzulGrande').removeClass('btnAzulGrandeDesactivado');
			}
		}

	});

	$('input[name="fshTerCondiciones"]').live('change',function(){
		var $LSSCont = $(this).parents('form[name="formServicioHabilitaBloquea"]').find('.btSSHabilitaContinuar');
		if($(this).is(':checked')){
			if($LSSCont.hasClass('btnAzulGrandeDesactivado')){
				$LSSCont.addClass('btnAzulGrande').removeClass('btnAzulGrandeDesactivado');
			}
		}else{
			if(!$LSSCont.hasClass('btnAzulGrandeDesactivado')){
				$LSSCont.addClass('btnAzulGrandeDesactivado').removeClass('btnAzulGrande');
			}
		}
	});

    $('.fsdVerDetServ').live('click',function(){
		if($(this).parents('.fsdDisable').length == 0){
			$(this).toggleClass('fsdVerDetOpen');
			$(this).parent().nextAll('.fsCajaMsj').toggle();

		}
        return false;
    });


    $('.fsaFormHabilitar select.required, .fsaFormHabilitar input[type="text"].required').live('change',function(){
    	var $LFromHab = $(this).parents('.fsaFormHabilitar');
    	formHabilitarValidar($LFromHab);
    });

    $('.fsaFormHabilitar input[type="radio"].required, .fsaFormHabilitar input[type="checkbox"].required').live('click',function(){
    	var $LFromHab = $(this).parents('.fsaFormHabilitar');
    	formHabilitarValidar($LFromHab);
    });

	$('.btSSFormHabConfirmar').live('click',function(){
		var $LFromHab = $(this).parents('.fsaFormHabilitar');
       if(!$(this).hasClass('btnAzulGrandeDesactivado')){
		  habilitarServicio($LFromHab);
       }
		return false;
	});




	/*function habServicio(valor){
		alert(valor);
		if(!$(this).hasClass('btnAzulGrandeDesactivado')){
			alert($("#serviceId").val());
			alert($(".serviceId").attr("id"));
			var $LFila = $(this).parents('.tabla_fila');
			$LFila.find('.tsvCerrar').hide();
			$(this).parents('.tiSSCajaVerMas').hide();
			$LFila.find('.columna3').hide();
			$LFila.find('.columna4').hide();
			$LFila.find('.columna5').hide();
			$LFila.find('.ccMsjHabilitado').show();
			$LFila.find('.tiSSEnlVerMas').show();

		}
		return false;
	}	*/

    //habilitar

	$('input[name="fsDeshabilitar"]').live('click',function(){
		var $LSSCont = $(this).parents('.formServicioDeshabilitar').find('.btSSDesContinuar');
		if($LSSCont.hasClass('btnAzulGrandeDesactivado')){
			$LSSCont.addClass('btnAzulGrande').removeClass('btnAzulGrandeDesactivado');
		}

	});

	$('.btSSDesContinuar').live('click',function(){
		var vertag= $(this).parents('.tabla_fila');
		if(!$(this).hasClass('btnAzulGrandeDesactivado')){
			var $LFromHab = $(this).parents('.formServicioDeshabilitar');

         	if($LFromHab.find('input[name="fsDeshabilitar"]:checked').val() == "Deshabilitar"){
        		dataLayer = dataLayer||[];
        		dataLayer.push({
        			'mx_content': 'Personas/Mi Entel/Mis Productos/Telefonia/Servicios de Suscripcion/' + vertag.find('.columna3').text() + '/' + vertag.find('.columna1').text() + '/'+ vertag.find('.columna4').text() +'/Deshabilitar',
        			'event': 'pageview'});
                $(this).parents('.tsvContent').hide();
                $(this).parents('.tsvContent').nextAll('.tsvPasoFormHab').show();
           	}else{
        		dataLayer.push({
        			'mx_content': 'Personas/Mi Entel/Mis Productos/Telefonia/Servicios de Suscripcion/' + vertag.find('.columna3').text() + '/' + vertag.find('.columna1').text() + '/'+ vertag.find('.columna4').text() +'/Bloquear',
        			'event': 'pageview'});
           	   $(this).parents('.tsvContent').hide();
  			  $(this).parents('.tsvContent').next('.tsvContent').show();
           }

		}
        return false;
    });


	$('.btSSDesConfirmar').live('click',function(){
		var vertag= $(this).parents('.tabla_fila');
		dataLayer.push({
			'mx_content': 'Personas/Mi Entel/Mis Productos/Telefonia/Servicios de Suscripcion/' + vertag.find('.columna3').text() + '/' + vertag.find('.columna1').text() + '/'+ vertag.find('.columna4').text() +'/Bloqueado',
			'event': 'pageview'});
	    var $LFromHab = $(this).parents('.formServicioDeshabilitar');
	    bloquearFamilia($LFromHab);
		return false;
	});

	$('.btSSDesConfirmarServ').live('click',function(){
		var vertag= $(this).parents('.tabla_fila');
		dataLayer.push({
			'mx_content': 'Personas/Mi Entel/Mis Productos/Telefonia/Servicios de Suscripcion/' + vertag.find('.columna3').text() + '/' + vertag.find('.columna1').text() + '/'+ vertag.find('.columna4').text() +'/Deshabilitado',
			'event': 'pageview'});
	    var $LFromHab = $(this).parents('.fsaFormDesHabilitar');
        deshabilitarServicio($LFromHab);
		return false;
	});

	$('.btSSbloDesbloquear').live('click',function(){
		var vertag= $(this).parents('.tabla_fila');
		dataLayer.push({
			'mx_content': 'Personas/Mi Entel/Mis Productos/Telefonia/Servicios de Suscripcion/' + vertag.find('.columna3').text() + '/' + vertag.find('.columna1').text() + '/'+ vertag.find('.columna4').text() +'/Desbloquear',
			'event': 'pageview'});
		if(!$(this).hasClass('btnAzulGrandeDesactivado')){
			$(this).parents('.tsvContent').hide();
			$(this).parents('.tsvContent').next('.tsvContent').show();
		}
		return false;
	});

	$('.btSSBloConfirmar').live('click',function(){
		var vertag= $(this).parents('.tabla_fila');
		dataLayer.push({
			'mx_content': 'Personas/Mi Entel/Mis Productos/Telefonia/Servicios de Suscripcion/' + vertag.find('.columna3').text() + '/' + vertag.find('.columna1').text() + '/'+ vertag.find('.columna4').text() +'/Desbloqueado',
			'event': 'pageview'});
		/*var $LFila = $(this).parents('.tabla_fila');
		$LFila.find('.tsvCerrar').hide();
        $(this).parents('.tiSSCajaVerMas').hide();
		$LFila.find('.columna3').hide();
		$LFila.find('.columna4').hide();
		$LFila.find('.columna5').hide();
		$LFila.find('.ccMsjDesbloqueado').show();
        $LFila.find('.tiSSEnlVerMas').show();*/
        var $LFromHab = $(this).parents('.fsaFormDesHabilitarServ');
        desbloquearServicio($LFromHab);
		return false;

	});


	function replaceAll( text, busca, reemplaza ){
		  while (text.toString().indexOf(busca) != -1)
		      text = text.toString().replace(busca,reemplaza);
		  return text;
	}



});




function formHabilitarValidar($LFromHab){
	var LSWfh = true;
	$LFromHab.find('.required').each(function(index, element) {
		if($(this).is('select') || $(this).is('input[type="text"]')){
			if($(this).val() == ""){
				LSWfh = false;
			}
		}else{
			if($(this).is('input[type="radio"]') || $(this).is('input[type="checkbox"]')){
				var $LParent = $(this).parent();
				if(!$LParent.find('input[name="'+$(this).attr('name')+'"]').is(':checked')){
					LSWfh = false;
				}

			}
		}

    });


	if(LSWfh){
		$LFromHab.find('.btSSFormHabConfirmar').addClass('btnAzulGrande').removeClass('btnAzulGrandeDesactivado');
	}else{
		$LFromHab.find('.btSSFormHabConfirmar').addClass('btnAzulGrandeDesactivado').removeClass('btnAzulGrande');
	}
}

function vermas(id){
	$("#vermas"+id).attr("style","display:none;");
	$("#cerrar"+id).attr("style","display:block;");
	$("#body"+id).attr("style","display:block;");
	$(this).fadeOut('fast', function(){
		$(this).next().fadeIn();
	});
	return false;
}

function cerrar(id){
	$("#vermas"+id).attr("style","display:block;");
	$("#cerrar"+id).attr("style","display:none;");
	$("#body"+id).attr("style","display:none;");
	$(this).fadeOut('fast', function(){
		$(this).next().fadeIn();
	});
	return false;
}


$(".autoTooltip").live('click', function() {
	var el = $(this);
	eventualizaTooltips();
	el.click();
	return false;
});

function cargarDatapicker(){
	// handle date picker
	$("input[name='fsaPara2Desde']").inputBox();
	$("input[name='fsaPara2Hasta']").inputBox();
	$("select[name='fsaParametro1']").selectBox();
	$("input[name='fsaParametro5']").inputBox();


		$( ".inputCalDesde" ).datepicker({
			showOn: "button",
			buttonImage: "../framework/skins/mientel/img/icons/icon_calendar_red.gif",
			buttonImageOnly: true,
			inline: true,
			minDate: 'today',
			maxDate: '+3m',
			onClose: function(selectedDate){
				var LFechaDia = selectedDate.split('/');
				if(selectedDate != ""){
					$(".inputCalHasta").datepicker( "option", "minDate", new Date(LFechaDia[2], LFechaDia[1]-1, LFechaDia[0]));
				}
			}
		});

		$( ".inputCalHasta" ).datepicker({
			showOn: "button",
			buttonImage: "../framework/skins/mientel/img/icons/icon_calendar_red.gif",
			buttonImageOnly: true,
			minDate: 'today',
			maxDate: '+3m',
			onClose: function(selectedDate){
				var LFechaDia = selectedDate.split('/');
				if(selectedDate != ""){
					$(".inputCalDesde").datepicker( "option", "maxDate", new Date(LFechaDia[2], LFechaDia[1]-1, LFechaDia[0]));
				}
			}
		});




    }

var htmlMensajeServicios = '<cm:getProperty node="${htmlMensajeServicios[0]}" name="html" />';
var htmlFilaHeadHistorial = '<cm:getProperty node="${htmlFilaHeadHistorial[0]}" name="html" />';
var htmlFilaHeadParHistorial = '<cm:getProperty node="${htmlFilaHeadParHistorial[0]}" name="html" />';
var htmlcolumna1 = '<cm:getProperty node="${htmlColumna1[0]}" name="html" />';
var htmlcolumna2 = '<cm:getProperty node="${htmlColumna2[0]}" name="html" />';
var htmlcolumna3 = '<cm:getProperty node="${htmlColumna3[0]}" name="html" />';
var htmlcolumna4 = '<cm:getProperty node="${htmlColumna4[0]}" name="html" />';
var htmlFilaBodyParHistorial = '<cm:getProperty node="${htmlFilaBodyParHistorial[0]}" name="html" />';
var htmlfooter = '</div>';
var htmlBodyhabilitado = '<h:outputText value="#{administracionServicios.htmlBodyHabilitado}" escape="false" />';
var htmlBodybloqueado = '<cm:getProperty node="${htmlBodyBloqueado[0]}" name="html" />';
var htmlBodydeshabilitado = '<h:outputText value="#{administracionServicios.htmlBodyDeshabilitado}" escape="false" />';
var cuadrotooltip = '<cm:getProperty node="${htmlCuadroTooltip[0]}" name="html" />';


function loadBodyTable(estado,index,subscribable,familia){

    if (estado==habilitado){
     //htmlBody=htmlBodyhabilitado;
			if("${aaa}"=="2"){


             $("#formServiDes"+index).show();
           	 $("#tituloHabilitar"+index).show();
       	     $("#textbodyHabilitar"+index).show();
             $("input[name=fshHabilitarDisable]"+index+"").attr("disabled","disabled");
				$("#Bloquear"+index).addClass("fsdDisable");
				$("#Alerta"+index).show();
				$("#Bloquear"+index).hide();
            }else{
           	 if(("${aaa}"=="0")|| ("${aaa}"=="1")){
           		 $("#formServiDes"+index).hide();
           		 $("#Bloquear"+index).hide();
           		 $("#tituloHabilitar"+index).hide();
           	     $("#textbodyHabilitar"+index).hide();
           	     $("#AlertaAdmin"+index).show();
                }else{
               	 if("${aaa}"=="3") {

                   	 if(familia==""){
                      	 $("#formServiDes"+index).show();
                       	 $("#tituloHabilitar"+index).show();
                   	     $("#textbodyHabilitar"+index).show();
                   	     $("#Bloquear"+index).hide();
                   	     $("#AlertaBloqueo"+index).show();
                   	 }else{
                       	 $("#formServiDes"+index).show();
                       	 $("#tituloHabilitar"+index).show();
                   	     $("#textbodyHabilitar"+index).show();

                   	 }


               	 }

                }
            }
    }else{

        if  (estado==deshabilitado){
       			if("${aaa}"=="2"){

       			 if(subscribable=="0"){
       				$("#fshHabilitar"+index).hide();
            		 $("#formServiDes"+index).hide();
            		 $("#tituloHabilitar"+index).hide();
            	     $("#textbodyHabilita"+index).hide();
            	 	 $("#Bloquear"+index).hide();
            	 	 $("#textbodyHabilitar"+index).hide();
            	     $("#AlertaSuscrito"+index).show();

       			 }else{
           			$("#fshHabilitar"+index).hide();
                	$("#formServiDes"+index).show();
                	$("#tituloHabilitar"+index).show();
            	    $("#textbodyHabilitar"+index).show();
                    $("input[name=fshDeshabilitarDisable]"+index+"").attr("disabled","disabled");
    				$("#Bloquear"+index).addClass("fsdDisable");
    				$("#Bloquear"+index).hide();

       			 }

             }else{
            	 if(("${aaa}"=="0")|| ("${aaa}"=="1")){

            		 $("#formServiDes"+index).hide();
            		 $("#fshHabilitar"+index).hide();
            		 $("#textbodyHabilitar"+index).show();
            	     $("#tituloHabilitar"+index).hide();
            	     $("#textbodyHabilitar"+index).hide();
            	 	 $("#Bloquear"+index).hide();
            	     $("#AlertaAdmin"+index).show();
                 }else{
                	 if("${aaa}"=="3") {

                    	 if(subscribable=="0"){

                        	 if(familia==""){
                          		$("#formServiDes"+index).hide();
                        		$("#tituloHabilitar"+index).hide();
                        	    $("#textbodyHabilita"+index).hide();
                        	 	$("#fshHabilitar"+index).hide();
                        	 	$("#textbodyHabilitar"+index).hide();
                                $("#textoAlerta"+index).text($("#textoAlerta"+index).text()+ '<cm:getProperty node="${msjServicioSinFamilia[0]}" name="html" />').show();
                                $("#AlertaSuscrito"+index).show();
                        	  }else{
                              		$("#formServiDes"+index).hide();
                            		$("#tituloHabilitar"+index).hide();
                            	    $("#textbodyHabilita"+index).hide();
                            	 	$("#fshHabilitar"+index).show();
                            	 	$("#textbodyHabilitar"+index).hide();
                            	    $("#AlertaSuscrito"+index).show();
                         	  }

                    	 }else{

                    		 if(familia==""){
	                    		 $("#formServiDes"+index).show();
	                        	 $("#tituloHabilitar"+index).show();
	                    	     $("#textbodyHabilitar"+index).show();
	                    	     $("#fshHabilitar"+index).hide();
	                    	     $("#AlertaBloqueo"+index).show();
                    		 }else{
	                    		 $("#formServiDes"+index).show();
	                        	 $("#tituloHabilitar"+index).show();
	                    	     $("#textbodyHabilitar"+index).show();
	                    	     $("#fshHabilitar"+index).show();
	                    	     $("#AlertaBloqueo"+index).hide();


                    		 }
                    	 }



                	 }

                 }
             }



        }else{
        	if  (estado==bloqueado){
        		if(("${aaa}"=="0")|| ("${aaa}"=="1") || (("${aaa}"=="2"))){
        			$("#AlertaAdmin"+index).show();
        			$("#BotonBloquear"+index).hide();
        			$("#formServiBloDes"+index).hide();

        		}


        	}
        }
    }



}


function habilitarServicio(LFromHab){

	var ele = LFromHab.find('input[name=fsaParametro4]:checked').map(function(){
		return this.value;
		}).get();

	var horamms=LFromHab.find("#horatime").val()+":"+LFromHab.find("#minutotime").val();
	var overriding=LFromHab.find("#overriding").val();
	var smsagenda=LFromHab.find("#smsagenda").val();
	var diasusc=LFromHab.find("#diasusc").val();
	var horasms=LFromHab.find("#houragen").val()+":"+LFromHab.find("#minutoagen").val();
	var vasid=LFromHab.find("#vasId").val();
	pack=null;

	LFromHab.find('input[name=pack]:checked').each(function() {
		pack=this.value;
	});

	var params="";

    params+="vasid="+LFromHab.find("#vasId").val();
    params+="&providerid="+LFromHab.find("#providerId").val();
    params+="&la="+LFromHab.find("#la").val();
    params+="&canal="+LFromHab.find("#canal").val();
    params+="&suscriptionid="+LFromHab.find("#suscriptionId").val();
    params+="&serviceid="+LFromHab.find("#service").val();

	if(vasid=="SMSINFO"){
		 params+="&horasms="+horasms;
		 if (pack!=null){
		    params+="&packcontent="+pack;
		 }
	}else{
		if(vasid=="MMSINFO"){
			params+="&horamms="+horamms;
			params+="&selectdias="+ele;
			params+="&overriding="+overriding;
			params+="&smsagenda="+smsagenda;
			params+="&diasusc="+diasusc;

		}

	}


     $("#confirm"+LFromHab.find("#indice").val()).addClass('btnAzulGrandeDesactivado').removeClass('btnAzulGrande');


       $('input:checkbox:checked.required').removeAttr('checked');

        enableCerrar =false;
    var url='<%=request.getContextPath()%>/portlet/servicios_suscripcion/habilitarServiciosSuscripcionJson.faces';
		 $.ajax({
	            type: 'GET',
	            url: url,
	            contentType: "application/json; charset=utf-8",
	            dataType: 'json',
	            data: params,
	            cache: false,
	            success: function (resp){

				    if(resp.estado == 'Ok'){
				    	enableCerrar =true;
						$("#cerrar"+LFromHab.find("#indice").val()).hide();
				        $("#colthree"+LFromHab.find("#indice").val()).hide();
				        $("#colfour"+LFromHab.find("#indice").val()).hide();
				        $("#colfive"+LFromHab.find("#indice").val()).hide();
				        $("#cajavermas"+LFromHab.find("#indice").val()).hide();
				        $("#msghab"+LFromHab.find("#indice").val()).show();
						$("#vermas"+LFromHab.find("#indice").val()).hide();
						pagina=LFromHab.find("#paginaindex").val();
						indice=LFromHab.find("#index").val();
						pagina=LFromHab.find("#pageindex").val();
						indice=LFromHab.find("#index").val();
						catalogoservicios.paginas[pagina].listadoservicios[indice].estado=habilitado;
				    }else{
				    	enableCerrar =true;
						$("#cerrar"+LFromHab.find("#indice").val()).hide();
				        $("#colthree"+LFromHab.find("#indice").val()).hide();
				        $("#colfour"+LFromHab.find("#indice").val()).hide();
				        $("#colfive"+LFromHab.find("#indice").val()).hide();
				        $("#cajavermas"+LFromHab.find("#indice").val()).hide();
				        $("#msghab"+LFromHab.find("#indice").val()).text('<cm:getProperty node="${msjNoHabilitarServicios[0]}" name="html" />').show();
						$("#vermas"+LFromHab.find("#indice").val()).hide();

				    }

		        }
		 });

}


function deshabilitarServicio(LFromHab){
    enableCerrar =false;   
    LFromHab.find("#confirm"+LFromHab.find("#indice").val()).addClass('btnAzulGrandeDesactivado').removeClass('btnAzulGrande');
    var url='<%=request.getContextPath()%>/portlet/servicios_suscripcion/deshabilitarServiciosSuscripcionJson.faces';	
		 $.ajax({
	            type: 'GET',
	            url: url,
	            contentType: "application/json; charset=utf-8",
	            dataType: 'json',
	            cache: false,
	            data: LFromHab.serialize(),
	            success: function (resp){

				if(resp.estado == 'Ok'){
				    	enableCerrar =true;
						$("#cerrar"+LFromHab.find("#indice").val()).hide();
				        $("#colthree"+LFromHab.find("#indice").val()).hide();
				        $("#colfour"+LFromHab.find("#indice").val()).hide();
				        $("#colfive"+LFromHab.find("#indice").val()).hide();
				        $("#cajavermas"+LFromHab.find("#indice").val()).hide();
				        $("#msgdeshab"+LFromHab.find("#indice").val()).show();
						$("#vermas"+LFromHab.find("#indice").val()).hide();
						pagina=LFromHab.find("#pageindex").val();
						indice=LFromHab.find("#index").val();
						catalogoservicios.paginas[pagina].listadoservicios[indice].estado=deshabilitado;
				    }else{
				    	enableCerrar =true;
						$("#cerrar"+LFromHab.find("#indice").val()).hide();
				        $("#colthree"+LFromHab.find("#indice").val()).hide();
				        $("#colfour"+LFromHab.find("#indice").val()).hide();
				        $("#colfive"+LFromHab.find("#indice").val()).hide();
				        $("#cajavermas"+LFromHab.find("#indice").val()).hide();
				        $("#msgdeshab"+LFromHab.find("#indice").val()).text('<cm:getProperty node="${msjNoDeshabilitarServicios[0]}" name="html" />').show();
						$("#vermas"+LFromHab.find("#indice").val()).hide();

				    }
		        }
		 });

}


function bloquearFamilia(LFromHab){
	var familia=LFromHab.find("#family").val();
	LFromHab.find("#confirm"+LFromHab.find("#indice").val()).addClass('btnAzulGrandeDesactivado').removeClass('btnAzulGrande');
	var est=LFromHab.find("#estado").val();
    enableCerrar =false;
    var url='<%=request.getContextPath()%>/portlet/servicios_suscripcion/bloquearFamiliaServicioJson.faces';
		 $.ajax({
	            type: 'GET',
	            url: url,
	            contentType: "application/json; charset=utf-8",
	            dataType: 'json',
	            cache:false,
	            data: LFromHab.serialize(),
	            success: function (resp){

			    if(resp.estado == 'Ok'){
			    	enableCerrar =true;
					$("#cerrar"+LFromHab.find("#indice").val()).hide();
			        $("#colthree"+LFromHab.find("#indice").val()).hide();
			        $("#colfour"+LFromHab.find("#indice").val()).hide();
			        $("#colfive"+LFromHab.find("#indice").val()).hide();

			        if(est==habilitado){
			        	 $("#msgblodes"+LFromHab.find("#indice").val()).show();
			        }else{
			        	 $("#msgblo"+LFromHab.find("#indice").val()).show();
				    }

			        $("#cajavermas"+LFromHab.find("#indice").val()).hide();

					$("#vermas"+LFromHab.find("#indice").val()).hide();
					pagina=LFromHab.find("#pageindex").val();
					indice=LFromHab.find("#index").val();
					catalogoservicios=updateBloqueado(catalogoservicios,familia, bloqueado);
				
                  
			    }else{
			    	enableCerrar =true;
					$("#cerrar"+LFromHab.find("#indice").val()).hide();
			        $("#colthree"+LFromHab.find("#indice").val()).hide();
			        $("#colfour"+LFromHab.find("#indice").val()).hide();
			        $("#colfive"+LFromHab.find("#indice").val()).hide();
			        $("#cajavermas"+LFromHab.find("#indice").val()).hide();
			        $("#msgblo"+LFromHab.find("#indice").val()).text('<cm:getProperty node="${msjNoBloqueoServicios[0]}" name="html" />').show();
					$("#vermas"+LFromHab.find("#indice").val()).hide();

			    }
		        }
		 });
	 		            
} 

function updateBloqueado(catalog, family, status){
var listado="";

    for (var i=0;i<catalog.listFamily.length;i++){
        if(catalog.listFamily[i].familia==family){
               listado=catalog.listFamily[i].pageIndices;
               break;
        }    

	}

	if(listado!=null){
		 for (var k=0;k<listado.length;k++){
			 page=listado[k].numberPage;
			 index=listado[k].positionPage;
			 catalogoservicios.paginas[page].listadoservicios[index].estado=status;
		 }	 

	}	
 return catalogoservicios;

}


function desbloquearServicio(LFromHab){
	var familia=LFromHab.find("#family").val();
	
	 $('#confirm'+LFromHab.find("#indice").val()).addClass('btnAzulGrandeDesactivado').removeClass('btnAzulGrande');
    enableCerrar =false;
	    var url='<%=request.getContextPath()%>/portlet/servicios_suscripcion/desbloquearFamiliaServicioJson.faces';
			 $.ajax({
		            type: 'GET',
		            url: url,
		            contentType: "application/json; charset=utf-8",
		            dataType: 'json',
		            cache:false,
		            data: LFromHab.serialize(),
		            success: function (resp){

					if(resp.estado == 'Ok'){
					    	enableCerrar =true;
							$("#cerrar"+LFromHab.find("#indice").val()).hide();
					        $("#colthree"+LFromHab.find("#indice").val()).hide();
					        $("#colfour"+LFromHab.find("#indice").val()).hide();
					        $("#colfive"+LFromHab.find("#indice").val()).hide();
					        $("#cajavermas"+LFromHab.find("#indice").val()).hide();
					        $("#msgbloqueo"+LFromHab.find("#indice").val()).show();
							$("#vermas"+LFromHab.find("#indice").val()).hide();
							catalogoservicios=updateBloqueado(catalogoservicios,familia, deshabilitado);
		                  
					    }else{
					    	enableCerrar =true;
							$("#cerrar"+LFromHab.find("#indice").val()).hide();
					        $("#colthree"+LFromHab.find("#indice").val()).hide();
					        $("#colfour"+LFromHab.find("#indice").val()).hide();
					        $("#colfive"+LFromHab.find("#indice").val()).hide();
					        $("#cajavermas"+LFromHab.find("#indice").val()).hide();
					        $("#msgbloqueo"+LFromHab.find("#indice").val()).text('<cm:getProperty node="${msjNoDesbloqueoServicios[0]}" name="html" />').show();
							$("#vermas"+LFromHab.find("#indice").val()).hide();

					    }
				     }
			 });


}




function loadTableHistorialReclamos(){

    var url='<%=request.getContextPath()%>/portlet/servicios_suscripcion/listaServiciosSuscripcionJson.faces';
    $(".loading-tabla-servicios").show();
    $(".ssTituloTabla").hide();
    $(".tabla_informacion").hide();
    var dataString = "&random="+Math.random()*99999;
		 $.ajax({
	            type: 'POST',
	            url: url,
	            dataType: 'json',
	            data:dataString,
	            cache : false,
	            success: function (resp){

			            if(resp.estado == 'Ok'){

				           // if(typeof(resp.respuesta.paginas)!= "undefined"){
				           if(resp.respuesta.paginas!=null){
			            	$(".loading-tabla-servicios").hide();
			                $(".ssTituloTabla").show();
			                $(".tabla_informacion").show();
			            	var mensaje=resp.respuesta.paginas[0].listadoservicios.length;


			            	if(mensaje!=0 ){

								catalogo = resp.respuesta;
								catalogoservicios=catalogo;
								var listaDetallePrimera = catalogo.paginas[0].listadoservicios;
								pagTotal=catalogo.paginas.length;

								fillselectCategoria();

								for(index in listaDetallePrimera){
									var detalle = listaDetallePrimera[index];

									htmlvalor1=htmlcolumna1.replace("?",detalle.descripcion);
									htmlvalor2=htmlcolumna2.replace("?","ssCategoria"+index);
									htmlvalor3=htmlcolumna3.replace("?",detalle.nomcategoria);
									htmlvalor4=htmlcolumna4.replace("?",detalle.estado);

									cuadro=cuadrotooltip.replace("#1","ssCategoria"+index)
									                     .replace("#2",detalle.descripcion)
									                     .replace("#3",detalle.valor)
									                     .replace("#4",detalle.recurrence);





								    if (detalle.estado==habilitado){

								        htmlBody=replaceAll(htmlBodyhabilitado,"#10",detalle.serviceId);
								        htmlBody=replaceAll(htmlBody,"#11",detalle.vasId);
								        htmlBody=replaceAll(htmlBody,"#12",detalle.providerId);
								        htmlBody=replaceAll(htmlBody,"#13",detalle.suscriptionId);
								        htmlBody=replaceAll(htmlBody,"#14",detalle.la);
								        htmlBody=htmlBody.replace("#15",detalle.familia);
								        htmlBody=replaceAll(htmlBody,"#20",detalle.canal);
								                                    htmlBody=replaceAll(htmlBody,"#2",index);
								                                    htmlBody=replaceAll(htmlBody,"#9",detalle.descripcion);
                                                                    htmlBody=replaceAll(htmlBody,"#num",detalle.index);
								                                    htmlBody=replaceAll(htmlBody,"#page",detalle.pageIndex);

													               if($("${aaa}"=="3")){
												            	    htmlBody=htmlBody.replace("#5","fsDeshabilitar");

													                }

													                if($("${aaa}")=="2"){

													                	htmlBody=htmlBody.replace("#5","fsDeshabilitarDisable"+index);
													                }
													                htmlBody=replaceAll(htmlBody,"#19",index);

													                htmlBody=htmlBody.replace("desc"+index,detalle.descripcion)
									                                  .replace("valor"+index,detalle.valor)
									                                  .replace("recurrencia"+index,detalle.recurrence)
									                                  .replace("commercial"+index,detalle.commercial)
									                                  .replace("status"+index,habilitado);

								    }else{

								        if  (detalle.estado==deshabilitado){
								                htmlBody=htmlBodydeshabilitado//.replace("#1",detalle.descripcion)
								                .replace("#20",detalle.familia);



								                htmlBody=replaceAll(htmlBody,"#10",detalle.serviceId);
								                htmlBody=replaceAll(htmlBody,"#14",detalle.vasId);
								                htmlBody=replaceAll(htmlBody,"#15",detalle.providerId);
								                htmlBody=replaceAll(htmlBody,"#16",detalle.suscriptionId);
								                htmlBody=replaceAll(htmlBody,"#17",detalle.la);
								                htmlBody=replaceAll(htmlBody,"#18",detalle.canal);





								                htmlBody=replaceAll(htmlBody,"#12",detalle.descripcion);
								                htmlBody=replaceAll(htmlBody,"#13",detalle.valor);
								                htmlBody=replaceAll(htmlBody,"#19",index);
								                htmlBody=replaceAll(htmlBody,"#num",detalle.index);
								                htmlBody=replaceAll(htmlBody,"#page",detalle.pageIndex);
								                //.replace("#10",index);

								                htmlBody=htmlBody.replace("desc"+index,detalle.descripcion)
								                                  .replace("valor"+index,detalle.valor)
								                                  .replace("recurrencia"+index,detalle.recurrence)
								                                  .replace("commercial"+index,detalle.commercial)
								                                  .replace("suscrito"+index,detalle.high_detail)
								                                   .replace("status"+index,deshabilitado);

								                if($("${aaa}"=="3")){
								                	htmlBody=htmlBody.replace("#5","fshHabilitar"+index)
								                	            .replace("#6","fshHabilitar");
								                }

								                if($("${aaa}")=="2"){
								                	htmlBody=htmlBody.replace("#5","fshHabilitarDisable"+index)
								                	          .replace("#6","Bloquear"+index);
								                }


								        }else{
								        	 if  (detalle.estado==bloqueado){
									        	 htmlBody=htmlBodybloqueado.replace("#3",detalle.familia);

									        	 htmlBody=replaceAll(htmlBody,"#19",index);
									        	 htmlBody=replaceAll(htmlBody,"#num",detalle.index);
									        	 htmlBody=replaceAll(htmlBody,"#page",detalle.pageIndex);
									        	 htmlBody=htmlBody.replace("desc"+index,detalle.descripcion)
					                                              .replace("valor"+index,detalle.valor)
					                                              .replace("recurrencia"+index,detalle.recurrence)
					                                              .replace("commercial"+index,detalle.commercial);


								        	 }
								        }
								    }




								    htmlFilaBodyPar2=replaceAll(htmlFilaBodyParHistorial,"#19",index);

									htmlvalor3=replaceAll(htmlvalor3,"#19",index);
									htmlvalor4=replaceAll(htmlvalor4,"#19",index);


									if(index%2 == 0){
										htmlFilaHeadTemp=htmlFilaHeadHistorial.replace("#19",index);
										$('#paginador-servicios').before(htmlFilaHeadTemp+htmlvalor1+htmlvalor2+htmlvalor3+htmlvalor4+htmlFilaBodyPar2+htmlBody);
									}else{
										htmlFilaHeadParTemp=htmlFilaHeadParHistorial.replace("#19",index);
										$('#paginador-servicios').before(htmlFilaHeadParTemp+htmlvalor1+htmlvalor2+htmlvalor3+htmlvalor4+htmlFilaBodyPar2+htmlBody);

									}



									if(detalle.vasId=="MMSINFO"){
						                $("#diagen"+index).show();
						                $("#horagen"+index).show();
						                $("#numsusc"+index).show();
						                $("#suscexist"+index).show();
						                $("#smsagen"+index).show();
						                $("#selectdias"+index).show();
						                $("#timegen"+index).hide();
						                $("#paquetes"+index).hide();
						                $("#title"+index).show();
						                $("#campos"+index).show();
									}else{

										if(detalle.vasId=="SMSINFO"){
											if (detalle.packcontents!=null){
											  $("#paquetes"+index).show();
											}else{
											  $("#paquetes"+index).hide();
											}
											  $("#timegen"+index).show();
								              $("#diagen"+index).hide();
								              $("#horagen"+index).hide();
								              $("#numsusc"+index).hide();
								              $("#selectdias"+index).hide();
								              $("#suscexist"+index).hide();
								              $("#smsagen"+index).hide();
								              $("#smsagen"+index).hide();
								              $("#title"+index).show();
								              $("#campos"+index).show();
										}else{

							                $("#diagen"+index).hide();
							                $("#horagen"+index).hide();
							                $("#numsusc"+index).hide();
							                $("#suscexist"+index).hide();
							                $("#selectdias"+index).hide();
							                $("#smsagen"+index).hide();
							                $("#ndiasusc"+index).hide();
							                $("#horaini"+index).hide();
							                $("#horafin"+index).hide();
							                $("#timegen"+index).hide();
							                $("#paquetes"+index).hide();
							                $("#title"+index).hide();
							                $("#campos"+index).hide();
										}
									}

					                if(detalle.packcontents!=null){
					                	$("#packoption"+index).append(detalle.packcontents);
					                }

									if (detalle.htmlFamilias!=null) {
										    $(".CajaMsj"+index).text("");
										    $(".CajaMsjHab"+index).text("");
										    $(".CajaConfMsjDes"+index).text("");
											$(".CajaConfMsj"+index).text("");
											$(".CajaConfMsj"+index).html('<cm:getProperty node="${msjConfirmarDeshabilitarBloquearFamilia1[0]}" name="html" /> ' + detalle.descripcion + ' <cm:getProperty node="${msjConfirmarDeshabilitarBloquearFamilia2[0]}" name="html" />');
											$(".CajaMsjBlo"+index).text("");
											$(".CajaConfBloMsj"+index).html('<cm:getProperty node="${msjConfirmarDesbloquearFamilia1[0]}" name="html" /> ' + detalle.descripcion + ' <cm:getProperty node="${msjConfirmarDesbloquearFamilia2[0]}" name="html" />');
											$(".CajaConfMsjDes"+index).html('<cm:getProperty node="${msjConfirmarHabilitarFamilia1[0]}" name="html" /> ' + detalle.descripcion + ' <cm:getProperty node="${msjConfirmarHabilitarFamilia2[0]}" name="html" />');
								      	    $(".CajaMsj"+index).html($(".CajaMsj"+index).html()+"<br>"+detalle.htmlFamilias);
								      	    $(".CajaMsjHab"+index).html($(".CajaMsjHab"+index).html()+"<br>"+detalle.htmlFamilias);
								            $(".CajaConfMsj"+index).html($(".CajaConfMsj"+index).html()+"<br>"+detalle.htmlFamilias);
								            $(".CajaMsjBlo"+index).html($(".CajaMsjBlo"+index).html()+"<br>"+detalle.htmlFamilias);
								            $(".CajaConfBloMsj"+index).html($(".CajaConfBloMsj"+index).html()+"<br>"+detalle.htmlFamilias);
								            $(".CajaConfMsjDes"+index).html($(".CajaConfMsjDes"+index).html()+"<br>"+detalle.htmlFamilias);

									}


									// if (detalle.estado==deshabilitado){
									   loadBodyTable(detalle.estado,index,detalle.subscribable,detalle.familia);
									 //}

									//$(".fsdItem").addClass("fsdDisable");

									//$('#tabla_informacion').append(htmlFilaBodyParHistorial); loading

									if(detalle.estado==0){
										//$('#tabla_informacion').append(htmlBodyhabilitado);
									}
									$('#paginador-servicios').before(htmlfooter);
									$('#paginador-servicios').after(cuadro);
								}
								//cargarDatapicker();
								//fillselectCategoria();

								if (catalogo.servicios.length>=Numxpaginas){
							     	paginarServicios(0);
								}

								$(".ssEstado").get(0).setValue(habilitado);
								$(".ssEstado").change();
								            	
			            	}else{
			            		$('#div-footer-tabla-servicios').append(htmlMensajeServicios);
			            		//$('#tr-loading-historial-reclamos').remove();

				            	$('#mensaje-servicios .texto').html('<cm:getProperty node="${msjNoInfoServicios[0]}" name="html" />');
				            	$('#mensaje-servicios').show();

			            	}
				            }else{
                              //alert("aa");
				            }
			            }else{

			            	$('#div-footer-tabla-servicios').append(htmlMensajeServicios);
			            	//$('#tr-loading-historial-reclamos').remove();
			            	$('#mensaje-historial .texto').html(resp.detalle);

			            }


	           }
        });

}


function fillselectCategoria(){
	var listaCategorias = catalogo.categorias;
	$("select[name*=ssCategoria]").empty();
	$("select[name*=ssCategoria]").append("<option value=''>Selecciona</option>");
	for(index in listaCategorias){
		var category = listaCategorias[index];

	   $("select[name*=ssCategoria]").append('<option value="'+category.idcategoria+'">'+category.nomcategoria+'</option>');
	}
}

function adjustPaginas(){
	  if(jQuery.browser.msie && jQuery.browser.version.substring(0, 1) >= 7)
	  {
		  $(".ulwrapdiv").css('width', '145px');
	      $("ul.jPag-pages li a").css('padding', '2px 23px');
		  $("span.jPag-current").css('margin-left', '16px');
	  }else{
			$("ul.jPag-pages li a").css('padding', '2px 14px');
			$("span.jPag-current").css('margin-left', '10px');
	  }
}

function paginarServicios(current){

	var numPagina = 1;
		$("#paginador-servicios").paginate({
			count 		: pagTotal,
			start 		: 1,
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
			   if(current==0){
				cambiarPaginaHistorialServicios(currval-1,'all');
			   }else{

				   loadServicesFlag(currval);
			   }


  				desactivarBotones(currval);


			}
		});


	$('.jPag-control-back, .jPag-control-front').css('opacity',0).css('cursor','default');


		if(pagTotal < 5){
			var ancho = (pagTotal * 30);
			$('.ulwrapdiv').css('width',ancho+'px');
		}else{
			ancho=170;
		}

		$('.jPag-backk').click(function(){
			$('.jPag-current').parent().prev().children('a').trigger('click');

			adjustPaginas();

		});

		$('.jPag-nextt').click(function(){
			$('.jPag-current').parent().next().children('a').trigger('click');
			adjustPaginas();
		});

		ancho2 = ancho+180;
		anchopaginador(ancho2);
		desactivarBotones(1);


};


var ancho2;
function anchopaginador(ancho){
	if(pagTotal < 5){
		if(jQuery.browser.msie)
		  {
	          $('.jPaginate').css('width','325px').css('margin','0 auto');
		  }else{

			  $('.jPaginate').css('width','310px').css('margin','0 auto');
		  }
	}else{

		$('.jPaginate').css('width',ancho+'px').css('margin','0 auto');
	}

}


var lastIndex;
function desactivarBotones(actual){

	if(actual==pagTotal){
		$('.jPag-control-front').css('opacity',0).children('a').css('cursor','default');
		adjustPaginas();

	}else{
		adjustPaginas();
		$('.jPag-control-front').css('opacity',1).children('a').css('cursor','pointer');
	}
	if(actual==1){
		 adjustPaginas();
		$('.jPag-control-back').css('opacity',0).children('a').css('cursor','default');
	}else{
		$('.jPag-control-back').css('opacity',1).children('a').css('cursor','pointer');
		adjustPaginas();
	}
}



function cambiarPaginaHistorialServicios(current,option){

	if(option=='all'){
		if($(".autoTooltip").hasClass('activo')){
			$(".autoTooltip.activo").trigger("click");
		}

		$('#tabla_informacion .rowservices').empty();
		$('#tabla_informacion .rowservices').hide();
		var listaDetalleCurrent = catalogo.paginas[current].listadoservicios;
		for(index in listaDetalleCurrent){
			var detalle = listaDetalleCurrent[index];

			htmlvalor1=htmlcolumna1.replace("?",detalle.descripcion);
			htmlvalor2=htmlcolumna2.replace("?","ssCategoria"+index);
			htmlvalor3=htmlcolumna3.replace("?",detalle.nomcategoria);
			htmlvalor4=htmlcolumna4.replace("?",detalle.estado);
			//htmlvalor4=htmlcolumna4.replace("?",detalle.nomsubcategoria);

			cuadro=cuadrotooltip.replace("#1","ssCategoria"+index)
			                     .replace("#2",detalle.descripcion)
			                     .replace("#3",detalle.valor)
			                     .replace("#4",detalle.recurrence);




		    if (detalle.estado==habilitado){

		        htmlBody=replaceAll(htmlBodyhabilitado,"#10",detalle.serviceId);
		        htmlBody=replaceAll(htmlBody,"#11",detalle.vasId);
		        htmlBody=replaceAll(htmlBody,"#12",detalle.providerId);
		        htmlBody=replaceAll(htmlBody,"#13",detalle.suscriptionId);
		        htmlBody=replaceAll(htmlBody,"#14",detalle.la);
		        htmlBody=htmlBody.replace("#15",detalle.familia);
		        htmlBody=replaceAll(htmlBody,"#20",detalle.canal);

		                                   htmlBody=replaceAll(htmlBody,"#2",index);
		                                   htmlBody=replaceAll(htmlBody,"#9",detalle.descripcion);
		                                   htmlBody=replaceAll(htmlBody,"#num",detalle.index);
		                                   htmlBody=replaceAll(htmlBody,"#page",detalle.pageIndex);

							               if($("${aaa}"=="3")){
							                	htmlBody=htmlBody.replace("#5","fsDeshabilitar");
							                }

							                if($("${aaa}")=="2"){
							                	htmlBody=htmlBody.replace("#5","fsDeshabilitarDisable"+index);
							                }
							                htmlBody=replaceAll(htmlBody,"#19",index);
							                htmlBody=htmlBody.replace("desc"+index,detalle.descripcion)
			                                  .replace("valor"+index,detalle.valor)
			                                  .replace("recurrencia"+index,detalle.recurrence)
			                                  .replace("commercial"+index,detalle.commercial)
			                                  .replace("status"+index,habilitado);
		    }else{

		        if  (detalle.estado==deshabilitado){

	                htmlBody=htmlBodydeshabilitado//.replace("#1",detalle.descripcion)
								                .replace("#20",detalle.familia);



	                htmlBody=replaceAll(htmlBody,"#10",detalle.serviceId);
	                htmlBody=replaceAll(htmlBody,"#14",detalle.vasId);
	                htmlBody=replaceAll(htmlBody,"#15",detalle.providerId);
	                htmlBody=replaceAll(htmlBody,"#16",detalle.suscriptionId);
	                htmlBody=replaceAll(htmlBody,"#17",detalle.la);
	                htmlBody=replaceAll(htmlBody,"#18",detalle.canal);




	                htmlBody=replaceAll(htmlBody,"#12",detalle.descripcion);
	                htmlBody=replaceAll(htmlBody,"#13",detalle.valor);
	                htmlBody=replaceAll(htmlBody,"#19",index);
	                htmlBody=replaceAll(htmlBody,"#num",detalle.index);
	                htmlBody=replaceAll(htmlBody,"#page",detalle.pageIndex);

	                htmlBody=htmlBody.replace("desc"+index,detalle.descripcion)
                    .replace("valor"+index,detalle.valor)
                    .replace("recurrencia"+index,detalle.recurrence)
					.replace("commercial"+index,detalle.commercial)
					.replace("suscrito"+index,detalle.high_detail)
					.replace("status"+index,deshabilitado);

	                //.replace("#10",index);

	                if($("${aaa}"=="3")){
	                	htmlBody=htmlBody.replace("#5","fshHabilitar"+index)
	                	            .replace("#6","fshHabilitar");
	                }

	                if($("${aaa}")=="2"){
	                	htmlBody=htmlBody.replace("#5","fshHabilitarDisable"+index)
	                	          .replace("#6","Bloquear"+index);
	                }


		        }else{
		        	 if  (detalle.estado==bloqueado){
			        	 htmlBody=htmlBodybloqueado.replace("#3",detalle.familia);

			        	 htmlBody=replaceAll(htmlBody,"#19",index);
			        	 htmlBody=replaceAll(htmlBody,"#num",detalle.index);
			        	 htmlBody=replaceAll(htmlBody,"#page",detalle.pageIndex);
			        	 htmlBody=htmlBody.replace("desc"+index,detalle.descripcion)
                                          .replace("valor"+index,detalle.valor)
                                          .replace("recurrencia"+index,detalle.recurrence)
                                          .replace("commercial"+index,detalle.commercial);
                     }
		        }
		    }


		    htmlFilaBodyPar2=replaceAll(htmlFilaBodyParHistorial,"#19",index);

			htmlvalor3=replaceAll(htmlvalor3,"#19",index);
			htmlvalor4=replaceAll(htmlvalor4,"#19",index);

			if(index%2 == 0){
				$('#head'+index).html(htmlvalor1+htmlvalor2+htmlvalor3+htmlvalor4+htmlFilaBodyPar2+htmlBody);
			}else{
				$('#head'+index).html(htmlvalor1+htmlvalor2+htmlvalor3+htmlvalor4+htmlFilaBodyPar2+htmlBody);

			}

			$('#head'+index).show();



			if(detalle.vasId=="MMSINFO"){
                $("#diagen"+index).show();
                $("#horagen"+index).show();
                $("#numsusc"+index).show();
                $("#suscexist"+index).show();
                $("#smsagen"+index).show();
                $("#selectdias"+index).show();
                $("#timegen"+index).hide();
                $("#paquetes"+index).hide();
                $("#title"+index).show();
                $("#campos"+index).show();
			}else{

				if(detalle.vasId=="SMSINFO"){
					  $("#timegen"+index).show();
					  if (detalle.packcontents!=null){
					    $("#paquetes"+index).show();
					  }else{
						$("#paquetes"+index).hide();
					  }
		              $("#diagen"+index).hide();
		              $("#horagen"+index).hide();
		              $("#numsusc"+index).hide();
		              $("#selectdias"+index).hide();
		              $("#suscexist"+index).hide();
		              $("#smsagen"+index).hide();
		              $("#smsagen"+index).hide();
		              $("#title"+index).show();
		              $("#campos"+index).show();
				}else{

	                $("#diagen"+index).hide();
	                $("#horagen"+index).hide();
	                $("#numsusc"+index).hide();
	                $("#suscexist"+index).hide();
	                $("#selectdias"+index).hide();
	                $("#smsagen"+index).hide();
	                $("#ndiasusc"+index).hide();
	                $("#horaini"+index).hide();
	                $("#horafin"+index).hide();
	                $("#timegen"+index).hide();
	                $("#paquetes"+index).hide();
	                $("#title"+index).hide();
	                $("#campos"+index).hide();
				}
			}

            if(detalle.packcontents!=null){
            	$("#packoption"+index).append(detalle.packcontents);
            }

			if (detalle.htmlFamilias!=null) {
				$(".CajaMsj"+index).text("");
				$(".CajaConfMsj"+index).text("");
				$(".CajaMsjBlo"+index).text("");
				$(".CajaMsjHab"+index).text("");
				$(".CajaConfBloMsj"+index).text("");
				$(".CajaConfMsjDes"+index).text("");
				$(".CajaConfMsj"+index).html('<cm:getProperty node="${msjConfirmarDeshabilitarBloquearFamilia1[0]}" name="html" /> ' + detalle.descripcion + ' <cm:getProperty node="${msjConfirmarDeshabilitarBloquearFamilia2[0]}" name="html" />');
				$(".CajaConfBloMsj"+index).html('<cm:getProperty node="${msjConfirmarDesbloquearFamilia1[0]}" name="html" /> ' + detalle.descripcion + ' <cm:getProperty node="${msjConfirmarDesbloquearFamilia2[0]}" name="html" />');
				$(".CajaConfMsjDes"+index).html('<cm:getProperty node="${msjConfirmarHabilitarFamilia1[0]}" name="html" /> ' + detalle.descripcion + ' <cm:getProperty node="${msjConfirmarHabilitarFamilia2[0]}" name="html" />');
			    $(".CajaMsj"+index).html($(".CajaMsj"+index).html()+"<br>"+detalle.htmlFamilias);
			    $(".CajaConfMsj"+index).html($(".CajaConfMsj"+index).html()+"<br>"+detalle.htmlFamilias);
			    $(".CajaMsjBlo"+index).html($(".CajaMsjBlo"+index).html()+"<br>"+detalle.htmlFamilias);
			    $(".CajaConfBloMsj"+index).html($(".CajaConfBloMsj"+index).html()+"<br>"+detalle.htmlFamilias);
			    $(".CajaConfMsjDes"+index).html($(".CajaConfMsjDes"+index).html()+"<br>"+detalle.htmlFamilias);
			    $(".CajaMsjHab"+index).html($(".CajaMsjHab"+index).html()+"<br>"+detalle.htmlFamilias);

			}

			loadBodyTable(detalle.estado,index,detalle.subscribable,detalle.familia);



			$('#paginador-servicios').before(htmlfooter);
			$('#paginador-servicios').after(cuadro);
		}

		//cargarDatapicker();
		if ((current==0) && (catalogo.servicios.length>=Numxpaginas)){

	      pagTotal=catalogo.paginas.length;
		  paginarServicios(0);
		}


	}else if (option=='search'){

		$('#tabla_informacion .rowservices').empty();
		$('#tabla_informacion .rowservices').hide();
		var nomvalor=$("select[name*=ssCategoria] option:selected").text();
		var valor=$("select[name*=ssCategoria]").val();
		var nomvalorsub=$("select[name*=ssSubcategoria] option:selected").text();
		var valorsub=$("select[name*=ssSubcategoria]").val();
        var valorest=$(".ssEstado").val()!=null?$(".ssEstado").val():"";
        var titulo="Servicios ";
        var swcat=false;
        if(valor!="" && valor!=null){
            swcat=true;
        }
        var swsub=false;
        if(valorsub!="" && valorsub!=null){
            swsub=true;
        }

        var swest=false;
        if(valorest!="" && valorest!=null){
            swest=true;
        }


		var cont=0;
		contflag=1;
		sw=false;
		var registros=0;
		sampleData=[];


        for (indice in catalogoservicios.paginas){

    		var listaDetalle = catalogoservicios.paginas[indice].listadoservicios;
    		for(index in listaDetalle){
    			var detail = listaDetalle[index];


		    	if ((detail.categoria==valor && swcat && detail.estado==valorest && swest && !swsub) || (detail.categoria==valor && swcat && !swest && !swsub) || (detail.categoria==valor && detail.subcategoria==valorsub && !swest && swsub && swcat ) ||  (detail.categoria==valor && detail.subcategoria==valorsub && detail.estado==valorest && swcat && swsub && swest)  ||   (detail.estado==valorest && !swcat && !swsub && swest)){

		            		if(contflag==1){

			    				htmlvalor1=htmlcolumna1.replace("?",detail.descripcion);
			    				htmlvalor2=htmlcolumna2.replace("?","ssCategoria"+cont);
			    				htmlvalor3=htmlcolumna3.replace("?",detail.nomcategoria);
			    				//htmlvalor4=htmlcolumna4.replace("?",detail.nomsubcategoria);
			    				htmlvalor4=htmlcolumna4.replace("?",detail.estado);
			    				cuadro=cuadrotooltip.replace("#1","ssCategoria"+cont)
			    				                      .replace("#2",detail.descripcion)
			    				                     .replace("#3",detail.valor)
			    				                     .replace("#4",detail.recurrence);



							    if (detail.estado==habilitado){
							    	htmlBody="";
							        htmlBody=replaceAll(htmlBodyhabilitado,"#10",detail.serviceId);
							        htmlBody=replaceAll(htmlBody,"#11",detail.vasId);
							        htmlBody=replaceAll(htmlBody,"#12",detail.providerId);
							        htmlBody=replaceAll(htmlBody,"#13",detail.suscriptionId);
							        htmlBody=replaceAll(htmlBody,"#14",detail.la);
							        htmlBody=htmlBody.replace("#15",detail.familia);
							        htmlBody=replaceAll(htmlBody,"#20",detail.canal);
							        htmlBody=replaceAll(htmlBody,"#num",detail.index);
							        htmlBody=replaceAll(htmlBody,"#page",detail.pageIndex);

							        htmlBody=replaceAll(htmlBody,"#2",cont);
							        htmlBody=replaceAll(htmlBody,"#9",detail.descripcion);

												               if($("${aaa}"=="3")){
												                	htmlBody=htmlBody.replace("#5","fsDeshabilitar");
												                }

												                if($("${aaa}")=="2"){
												                	htmlBody=htmlBody.replace("#5","fsDeshabilitarDisable"+cont);
												                }

												                htmlBody=replaceAll(htmlBody,"#19",cont);
												                htmlBody=htmlBody.replace("desc"+cont,detail.descripcion)
								                                  .replace("valor"+cont,detail.valor)
								                                  .replace("recurrencia"+cont,detail.recurrence)
								                                  .replace("commercial"+cont,detail.commercial)
								                                  .replace("status"+cont,habilitado);
							    }else{

							        if  (detail.estado==deshabilitado){
								        htmlBody="";
						                htmlBody=htmlBodydeshabilitado//.replace("#1",detalle.descripcion)
								                .replace("#20",detail.familia);



						                htmlBody=replaceAll(htmlBody,"#10",detail.serviceId);
						                htmlBody=replaceAll(htmlBody,"#14",detail.vasId);
						                htmlBody=replaceAll(htmlBody,"#15",detail.providerId);
						                htmlBody=replaceAll(htmlBody,"#16",detail.suscriptionId);
						                htmlBody=replaceAll(htmlBody,"#17",detail.la);
						                htmlBody=replaceAll(htmlBody,"#18",detail.canal);




						                htmlBody=replaceAll(htmlBody,"#12",detail.descripcion);
						                htmlBody=replaceAll(htmlBody,"#13",detail.valor);
						                htmlBody=replaceAll(htmlBody,"#19",cont);
						                htmlBody=replaceAll(htmlBody,"#num",detail.index);
						                htmlBody=replaceAll(htmlBody,"#page",detail.pageIndex);

						                htmlBody=htmlBody.replace("desc"+cont,detail.descripcion)
		                                  .replace("valor"+cont,detail.valor)
								          .replace("recurrencia"+cont,detail.recurrence)
								          .replace("commercial"+cont,detail.commercial)
								          .replace("suscrito"+cont,detail.high_detail)
								           .replace("status"+cont,deshabilitado);

						                if($("${aaa}"=="3")){
						                	htmlBody=htmlBody.replace("#5","fshHabilitar"+cont)
						                	            .replace("#6","fshHabilitar");
						                }

						                if($("${aaa}")=="2"){
						                	htmlBody=htmlBody.replace("#5","fshHabilitarDisable"+cont)
						                	          .replace("#6","Bloquear"+index);
						                }

							        }else{

							        	 if  (detail.estado==bloqueado){
							        		 htmlBody="";
								        	 htmlBody=htmlBodybloqueado.replace("#3",detail.familia);

								        	 htmlBody=replaceAll(htmlBody,"#19",cont);
								        	 htmlBody=replaceAll(htmlBody,"#num",detail.index);
								        	 htmlBody=replaceAll(htmlBody,"#page",detail.pageIndex);
								        	 htmlBody=htmlBody.replace("desc"+cont,detail.descripcion)
				                                              .replace("valor"+cont,detail.valor)
				                                              .replace("recurrencia"+cont,detail.recurrence)
				                                              .replace("commercial"+cont,detail.commercial);
							        	 }

							        }
							    }

							    htmlFilaBodyPar2=replaceAll(htmlFilaBodyParHistorial,"#19",cont);

								htmlvalor3=replaceAll(htmlvalor3,"#19",cont);
								htmlvalor4=replaceAll(htmlvalor4,"#19",cont);


			    				if(cont%2 == 0){

			    					$('#head'+cont).html(htmlvalor1+htmlvalor2+htmlvalor3+htmlvalor4+htmlFilaBodyPar2+htmlBody);
			    				}else{
			    					$('#head'+cont).html(htmlvalor1+htmlvalor2+htmlvalor3+htmlvalor4+htmlFilaBodyPar2+htmlBody);

			    				}
			    				$('#head'+cont).show();

								if(detail.vasId=="MMSINFO"){
					                $("#diagen"+cont).show();
					                $("#horagen"+cont).show();
					                $("#numsusc"+cont).show();
					                $("#suscexist"+cont).show();
					                $("#smsagen"+cont).show();
					                $("#selectdias"+cont).show();
					                $("#timegen"+cont).hide();
					                $("#paquetes"+cont).hide();
					                $("#title"+cont).show();
					                $("#campos"+cont).show();
								}else{

									if(detail.vasId=="SMSINFO"){
										  $("#timegen"+cont).show();
										  if (detail.packcontents!=null){
										    $("#paquetes"+cont).show();
										  }else{
											  $("#paquetes"+cont).hide();
										  }
							              $("#diagen"+cont).hide();
							              $("#horagen"+cont).hide();
							              $("#numsusc"+cont).hide();
							              $("#selectdias"+cont).hide();
							              $("#suscexist"+cont).hide();
							              $("#smsagen"+cont).hide();
							              $("#smsagen"+cont).hide();
							              $("#title"+cont).show();
							              $("#campos"+cont).show();
									}else{

						                $("#diagen"+cont).hide();
						                $("#horagen"+cont).hide();
						                $("#numsusc"+cont).hide();
						                $("#suscexist"+cont).hide();
						                $("#selectdias"+cont).hide();
						                $("#smsagen"+cont).hide();
						                $("#ndiasusc"+cont).hide();
						                $("#horaini"+cont).hide();
						                $("#horafin"+cont).hide();
						                $("#timegen"+cont).hide();
						                $("#paquetes"+cont).hide();
						                $("#title"+cont).hide();
						                $("#campos"+cont).hide();
									}
								}


			    				loadBodyTable(detail.estado,cont,detail.subscribable,detail.familia);

				                if(detail.packcontents!=null){
				                	$("#packoption"+cont).append(detail.packcontents);
				                }

								var suscrip="";

			    				if (detail.htmlFamilias!= null) {
			    					if (detail.htmlFamilias!= "") {
				    					$(".CajaMsj"+cont).text("");
				    					$(".CajaConfMsj"+cont).text("");
				    					$(".CajaMsjBlo"+cont).text("");
				    					$(".CajaMsjHab"+cont).text("");
				    					$(".CajaConfBloMsj"+cont).text("");
				    					$(".CajaConfMsjDes"+cont).text("");
										$(".CajaConfMsj"+cont).html('<cm:getProperty node="${msjConfirmarDeshabilitarBloquearFamilia1[0]}" name="html" /> ' + detail.descripcion + ' <cm:getProperty node="${msjConfirmarDeshabilitarBloquearFamilia2[0]}" name="html" />');
				    					$(".CajaConfBloMsj"+cont).html('<cm:getProperty node="${msjConfirmarDesbloquearFamilia1[0]}" name="html" /> ' + detail.descripcion + ' <cm:getProperty node="${msjConfirmarDesbloquearFamilia2[0]}" name="html" />');
				    					$(".CajaConfMsjDes"+cont).html('<cm:getProperty node="${msjConfirmarHabilitarFamilia1[0]}" name="html" /> ' + detail.descripcion + ' <cm:getProperty node="${msjConfirmarHabilitarFamilia2[0]}" name="html" />');
				    					//for (i=0;i<detail.suscripciones.length;i++){
				    				      	  $(".CajaMsj"+cont).html( $(".CajaMsj"+cont).html()+"<br>"+detail.htmlFamilias);
				    				          $(".CajaConfMsj"+cont).html($(".CajaConfMsj"+cont).html()+"<br>"+detail.htmlFamilias);
				    				          $(".CajaMsjBlo"+cont).html( $(".CajaMsjBlo"+cont).html()+"<br>"+detail.htmlFamilias);
				    				          $(".CajaConfBloMsj"+cont).html( $(".CajaConfBloMsj"+cont).html()+"<br>"+detail.htmlFamilias);
				    				          $(".CajaConfMsjDes"+cont).html($(".CajaConfMsjDes"+cont).html()+"<br>"+detail.htmlFamilias);
				    				          $(".CajaMsjHab"+cont).html($(".CajaMsjHab"+cont).html()+"<br>"+detail.htmlFamilias);

				    			        //}
			    					}
			    				}



			    				$('#paginador-servicios').before(htmlfooter);
			    				$('#paginador-servicios').after(cuadro);
			    				cont++;
		            		   }

		            		    //cargarDatapicker();




		            		    sampleData.push({serviceId: detail.serviceId, nomcategoria: detail.nomcategoria, subcategoria: detail.nomsubcategoria, subscribable:detail.subscribable, commercial:detail.commercial,
		                		                 descripcion: detail.descripcion, valor: detail.valor, recurrence: detail.recurrence, flagservicio: contflag+"true", estado: detail.estado, familia: detail.familia,
		                		                 la: detail.la, high_detail: detail.high_detail, canal: detail.canal,suscripciones: detail.htmlFamilias, suscriptionId: detail.suscriptionId,
		                		                 providerId: detail.providerId,vasId: detail.vasId, index: detail.index, packcontents: detail.packcontents,pageindex: detail.pageindex });

			    				registros++;
		        		  }

                  var tam=(parseInt(contflag)*parseInt(Numxpaginas))-1;
        		  if(registros>tam){
                      contflag++;
        		  }



    			}

    		}



               if (registros>0){
			        var numeroPaginas = Math.ceil(parseFloat(registros/Numxpaginas));
			        pagTotal = registros > numeroPaginas ? (parseInt(numeroPaginas) >= parseInt(numeroPaginas) ?  numeroPaginas :  (numeroPaginas + 1))	: 1;
			        $("#tabla_informacion").show();
			       if (registros>=Numxpaginas){
			        $("#paginador-servicios").show();

				        paginarServicios(1);
			       } else{

			    	   $("#paginador-servicios").hide();
			       }

			        if(swcat){
				       titulo+="de '"+nomvalor+"'";


			        }
				    if (swsub){
	                       titulo+=" /'"+nomvalorsub+"'";
				    }

				    if(swest){
					       titulo+=" "+valorest+"s";
				    }



			        $(".ssTituloTabla").text(titulo);

			        $('#div-footer-tabla-servicios').hide();

			        $(".ssTituloTabla").show();
		        }else{
			        $("#tabla_informacion").hide();
			        $(".ssTituloTabla").hide();
			        $('#div-footer-tabla-servicios').show();
	            	$('#div-footer-tabla-servicios').html(htmlMensajeServicios);
	        	    $('#mensaje-servicios .texto').html('<cm:getProperty node="${msjNoInfoServicios[0]}" name="html" />');
	            	$('#mensaje-servicios').show();

		        }

        }





}


function loadServicesFlag(current){


	if($(".autoTooltip").hasClass('activo')){
		$(".autoTooltip.activo").trigger("click");
	}


	$('#tabla_informacion .rowservices').empty();
	$('#tabla_informacion .rowservices').hide();


    var cont=0;
    var prueba=sampleData;

	$.each(prueba, function(i, item) {

 		 if(item.flagservicio==current+"true"){


		    htmlvalor1=htmlcolumna1.replace("?",item.descripcion);
			htmlvalor2=htmlcolumna2.replace("?","ssCategoria"+cont);
			htmlvalor3=htmlcolumna3.replace("?",item.nomcategoria);
			htmlvalor4=htmlcolumna4.replace("?",item.estado);

			cuadro=cuadrotooltip.replace("#1","ssCategoria"+cont)
			                     .replace("#2",item.descripcion)
			                     .replace("#3",item.valor)
			                     .replace("#4",item.recurrence);




		    if (item.estado==habilitado){

		        htmlBody=replaceAll(htmlBodyhabilitado,"#10",item.serviceId);
		        htmlBody=replaceAll(htmlBody,"#11",item.vasId);
		        htmlBody=replaceAll(htmlBody,"#12",item.providerId);
		        htmlBody=replaceAll(htmlBody,"#13",item.suscriptionId);
		        htmlBody=replaceAll(htmlBody,"#14",item.la);
		        htmlBody=htmlBody.replace("#15",item.familia);
		        htmlBody=replaceAll(htmlBody,"#20",item.canal);
		        htmlBody=replaceAll(htmlBody,"#num",item.index);
		        htmlBody=replaceAll(htmlBody,"#page",item.pageIndex);

		                                     htmlBody=replaceAll(htmlBody,"#2",cont);
		                                     htmlBody=replaceAll(htmlBody,"#9",item.descripcion);
							               if($("${aaa}"=="3")){
							                	htmlBody=htmlBody.replace("#5","fsDeshabilitar");
							                }

							                if($("${aaa}")=="2"){
							                	htmlBody=htmlBody.replace("#5","fsDeshabilitarDisable"+cont);
							                }

							                htmlBody=replaceAll(htmlBody,"#19",cont);
								            htmlBody=htmlBody.replace("desc"+cont,item.descripcion)
			                                  .replace("valor"+cont,item.valor)
			                                  .replace("recurrencia"+cont,item.recurrence)
			                                  .replace("commercial"+cont,item.commercial)
			                                  .replace("status"+cont,habilitado);


		    }else{

		        if  (item.estado==deshabilitado){
	                htmlBody=htmlBodydeshabilitado//.replace("#1",detalle.descripcion)
								                .replace("#20",item.familia);



	                htmlBody=replaceAll(htmlBody,"#10",item.serviceId);
	                htmlBody=replaceAll(htmlBody,"#14",item.vasId);
	                htmlBody=replaceAll(htmlBody,"#15",item.providerId);
	                htmlBody=replaceAll(htmlBody,"#16",item.suscriptionId);
	                htmlBody=replaceAll(htmlBody,"#17",item.la);
	                htmlBody=replaceAll(htmlBody,"#18",item.canal);




					htmlBody=replaceAll(htmlBody,"#12",item.descripcion);
					htmlBody=replaceAll(htmlBody,"#13",item.valor);
					htmlBody=replaceAll(htmlBody,"#19",cont);
					htmlBody=replaceAll(htmlBody,"#num",item.index);
					htmlBody=replaceAll(htmlBody,"#page",item.pageIndex);


	                htmlBody=htmlBody.replace("desc"+cont,item.descripcion)
                    .replace("valor"+cont,item.valor)
					.replace("recurrencia"+cont,item.recurrence)
					.replace("commercial"+cont,item.commercial)
					.replace("suscrito"+cont,item.high_detail)
					.replace("status"+cont,deshabilitado);


	                if($("${aaa}"=="3")){
	                	htmlBody=htmlBody.replace("#5","fshHabilitar"+cont)
	                	            .replace("#6","fshHabilitar");
	                }

	                if($("${aaa}")=="2"){
	                	htmlBody=htmlBody.replace("#5","fshHabilitarDisable"+cont)
	                	          .replace("#6","Bloquear"+cont);
	                }


		        }else{
		        	 if  (item.estado==bloqueado){
			        	 htmlBody=htmlBodybloqueado.replace("#3",item.familia);

			        	 htmlBody=replaceAll(htmlBody,"#19",cont);
			        	 htmlBody=replaceAll(htmlBody,"#num",item.index);
			        	 htmlBody=replaceAll(htmlBody,"#page",item.pageIndex);
			        	 htmlBody=htmlBody.replace("desc"+cont,item.descripcion)
                                          .replace("valor"+cont,item.valor)
                                          .replace("recurrencia"+cont,item.recurrence)
                                          .replace("commercial"+cont,item.commercial);
                     }

		        }
		    }
		    htmlFilaBodyPar2=replaceAll(htmlFilaBodyParHistorial,"#19",cont);

			htmlvalor3=replaceAll(htmlvalor3,"#19",cont);
			htmlvalor4=replaceAll(htmlvalor4,"#19",cont);

			if(cont%2 == 0){
				$('#head'+cont).html(htmlvalor1+htmlvalor2+htmlvalor3+htmlvalor4+htmlFilaBodyPar2+htmlBody);
			}else{
				$('#head'+cont).html(htmlvalor1+htmlvalor2+htmlvalor3+htmlvalor4+htmlFilaBodyPar2+htmlBody);


			}
			$('#head'+cont).show();

			if(item.vasId=="MMSINFO"){
                $("#diagen"+cont).show();
                $("#horagen"+cont).show();
                $("#numsusc"+cont).show();
                $("#suscexist"+cont).show();
                $("#smsagen"+cont).show();
                $("#selectdias"+cont).show();
                $("#timegen"+cont).hide();
                $("#paquetes"+cont).hide();
                $("#title"+cont).show();
                $("#campos"+cont).show();
			}else{

				if(item.vasId=="SMSINFO"){
					  $("#timegen"+cont).show();
					  if (item.packcontents!=null){
						    $("#paquetes"+cont).show();
					  }else{
						    $("#paquetes"+cont).hide();
					  }
		              $("#diagen"+cont).hide();
		              $("#horagen"+cont).hide();
		              $("#numsusc"+cont).hide();
		              $("#selectdias"+cont).hide();
		              $("#suscexist"+cont).hide();
		              $("#smsagen"+cont).hide();
		              $("#smsagen"+cont).hide();
		              $("#title"+cont).show();
		              $("#campos"+cont).show();
				}else{

	                $("#diagen"+cont).hide();
	                $("#horagen"+cont).hide();
	                $("#numsusc"+cont).hide();
	                $("#suscexist"+cont).hide();
	                $("#selectdias"+cont).hide();
	                $("#smsagen"+cont).hide();
	                $("#ndiasusc"+cont).hide();
	                $("#horaini"+cont).hide();
	                $("#horafin"+cont).hide();
	                $("#timegen"+cont).hide();
	                $("paquetes"+cont).hide();
	                $("#title"+cont).hide();
	                $("#campos"+cont).hide();
				}
			}

            if(item.packcontents!=null){
            	$("#packoption"+cont).append(item.packcontents);
            }


			if (item.suscripciones!= "") {
					$(".CajaMsj"+cont).text("");
					$(".CajaConfMsj"+cont).text("");
					$(".CajaMsjBlo"+cont).text("");
					$(".CajaMsjHab"+cont).text("");
					$(".CajaConfBloMsj"+cont).text("");
					$(".CajaConfMsjDes"+cont).text("");
					$(".CajaConfMsj"+cont).html('<cm:getProperty node="${msjConfirmarDeshabilitarBloquearFamilia1[0]}" name="html" /> ' + item.descripcion + ' <cm:getProperty node="${msjConfirmarDeshabilitarBloquearFamilia2[0]}" name="html" />');
					$(".CajaConfBloMsj"+cont).html('<cm:getProperty node="${msjConfirmarDesbloquearFamilia1[0]}" name="html" /> ' + item.descripcion + ' <cm:getProperty node="${msjConfirmarDesbloquearFamilia2[0]}" name="html" />');
					$(".CajaConfMsjDes"+cont).html('<cm:getProperty node="${msjConfirmarHabilitarFamilia1[0]}" name="html" /> ' + item.descripcion + ' <cm:getProperty node="${msjConfirmarHabilitarFamilia2[0]}" name="html" />');
                    $(".CajaMsj"+cont).html($(".CajaMsj"+cont).html()+"<br>"+item.suscripciones);
				    $(".CajaConfMsj"+cont).html($(".CajaConfMsj"+cont).html()+"<br>"+item.suscripciones);
				    $(".CajaMsjBlo"+cont).html($(".CajaMsjBlo"+cont).html()+"<br>"+item.suscripciones);
				    $(".CajaConfBloMsj"+cont).html($(".CajaConfBloMsj"+cont).html()+"<br>"+item.suscripciones);
				    $(".CajaConfMsjDes"+cont).html($(".CajaConfMsjDes"+cont).html()+"<br>"+item.suscripciones);
				    $(".CajaMsjHab"+cont).html($(".CajaMsjHab"+cont).html()+"<br>"+item.suscripciones);


			}

			loadBodyTable(item.estado,cont,item.subscribable,item.familia);

			$('#paginador-servicios').before(htmlfooter);
			$('#paginador-servicios').after(cuadro);

			//cargarDatapicker();


			cont++;

		 }

	});




}

</script>

  <h1>Servicios de Suscripci&oacute;n</h1>
  <div class="ssFiltro clearfix">
      <div class="ssfItem">
      	<label>Categor&iacute;a</label>
          <select name="ssCategoria" class="selectBox" style="width:130px;">
          	<option value="">Selecciona</option>
          </select>

      </div>

      <div class="ssfItem">
          <label>Subcategor&iacute;a</label>
          <select name="ssSubcategoria" id="ssSubcategoria" class="selectBox ssSubcategoria" style="width:130px;">
           <option value="">Selecciona</option>
          </select>
      </div>
	  <div class="ssfItem">
	       <label>Estado</label>
	       <h:selectOneMenu id="ssEstado" style="width:130px;" styleClass="ssEstado selectBox">
	        			<f:selectItems value="#{administracionServicios.estados}"/>
	       </h:selectOneMenu>
	  </div>

      
      <div class="ssfItemBoton" style="display:none"> <!--btnAzulGrande btnAzulGrandeDesactivado btnAzulGrandeLargo -->
      	<a href="#" class="btnAzulGrandeDesactivado btnAzulGrandeLargo" id="btSSBuscar"><span>Buscar</span></a>
      </div>

  </div>

  <cm:getProperty node="${msjDescFiltrosServicio[0]}" name="html"/>

  <div class="loading-tabla-servicios" style="display:none">
      <center><br><b><img src='../framework/skins/mientel/img/thickbox/TB_cargando.gif'/></b><br><br></center>
  </div>

  <span class="ssTituloTabla">Todos los Servicios</span>

   <div id="tabla_informacion" class="tabla_informacion tiServicioSuscri clearfix">
       <div class="tabla_fila resaltador clearfix">
           	<div class="columna1"><strong>Servicio</strong></div>
            <div class="columna2">&nbsp;</div>
            <div class="columna3"><strong>Categorias</strong></div>
            <div class="columna4"><strong>Estado</strong></div>
       </div>

      <div id="paginador-servicios" class="clearfix tiSSCajaPaginador" style="margin: auto; margin-top: 22px">
		  <div class="tiSSPaginador"></div>
	  </div>



   </div>

   <div id="div-footer-tabla-servicios"></div>





</f:view>
