<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="f"  uri="http://java.sun.com/jsf/core"%>
<%@ taglib prefix="h"  uri="http://java.sun.com/jsf/html"%>
<%@ taglib prefix="it" uri="http://i2b.cl/jsf/iterator/" %>
<%@ taglib prefix="entel" uri="/WEB-INF/tld/entel-tags.tld" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="r"  uri="http://www.bea.com/servers/portal/tags/netuix/render"%>
<%@ taglib uri="http://www.bea.com/servers/portal/tags/content" prefix="cm"%>
<%@ taglib uri="http://www.bea.com/servers/portal/tags/netuix/preferences" prefix="preferences" %>

<cm:search id="iphoneGrupoHabilError" query="idContenido = 'iphoneGrupoHabilError'" useCache="true"  />
<cm:search id="infoLiberacionEquipo" query="idContenido = 'infoLiberacionEquipo'" useCache="true"  />
<cm:search id="debesSaber" query="idContenido = 'debesSaberBloqueoP2'" useCache="true"  />
<cm:search id="debesSaberP1" query="idContenido = 'debesSaberBloqueoP1'" useCache="true"  />
<cm:search id="parrafo" query="idContenido = 'parrafoEquipo'" useCache="true"  />
<cm:search id="condicionesBloquear" query="idContenido = 'condicionesBloquear'" useCache="true"  />
<cm:search id="presupuesto" query="idContenido = 'debesSaberPresupuesto'" useCache="true"  />

<cm:search id="renovarEquipo" query="idContenido = 'infoRenovarEquipoHeader'" useCache="true"  />
<cm:search id="permisosAAA" query="idContenido = 'alertaPermisosAAA'" useCache="true"  />
<cm:search id="cuposDisponibles" query="idContenido = 'infoCuposDisponibles'" useCache="true"  />
<cm:search id="arriendoEquipo" query="idContenido = 'arriendoVigenteEquipo'" useCache="true"  />
<cm:search id="renovacionAnticipada" query="idContenido = 'totalRenovacionAnticipada'" useCache="true"  />
<cm:search id="infoEquiposPagados" query="idContenido = 'infoEquiposPagados'" useCache="true"  />
<cm:search id="ofertaEquipos" query="idContenido = 'bloqueOfertaEquipos'" useCache="true"  />
<cm:search id="contactoRenovacion" query="idContenido = 'bloqueContactoRenovacion'" useCache="true"  />
<cm:search id="msgEnvioSolicitud" query="idContenido = 'msgEnvioSolicitudContacto'" useCache="true"  />
<cm:search id="bloqueoExtravioHurto" query="idContenido = 'bloqueoExtravioHurto'" useCache="true"  />
<cm:search id="bloqueoPorHurto" query="idContenido = 'bloqueoPorHurto'" useCache="true"  />
<cm:search id="bloqueoPorExtravio" query="idContenido = 'bloqueoPorExtravio'" useCache="true"  />
<cm:search id="bloqueHoraRobo" query="idContenido = 'bloqueHoraRobo'" useCache="true"  />
<cm:search id="bloqueoUnidireccional" query="idContenido = 'bloqueoUnidireccional'" useCache="true"  />
<cm:search id="bloqueoBidireccional" query="idContenido = 'bloqueoBidireccional'" useCache="true"  />
<cm:search id="generarClave" query="idContenido = 'generarClaveDesbloqueo'" useCache="true"  />
<cm:search id="mailEnvioClave" query="idContenido = 'mailEnvioClaveDesbloqueo'" useCache="true"  />
<cm:search id="sucursalDesbloqueo" query="idContenido = 'sucursalDesbloqueoClave'" useCache="true"  />
<cm:search id="alertaBloqueoExistente" query="idContenido = 'alertaBloqueoExistente'" useCache="true"  />
<cm:search id="bloqueoCompletado" query="idContenido = 'bloqueoCompletado'" useCache="true"  />
<cm:search id="statusSoporte" query="idContenido = 'estadoEquipoServicioTecnico'" useCache="true"  />
<cm:search id="OrdenesDeTrabajo" query="idContenido = 'OrdenesDeTrabajo'" useCache="true"  />
<cm:search id="aceptacionPresupuesto" query="idContenido = 'aceptacionPresupuesto'" useCache="true"  />
<cm:search id="rechazoPresupuesto" query="idContenido = 'rechazoPresupuesto'" useCache="true"  />
<cm:search id="evaluacionComercial" query="idContenido = 'infoEvaluacionComercial'" useCache="true"  />
<cm:search id="errorDesBloqueo" query="idContenido = 'errorDesBloqueo'" useCache="true"  />
<cm:search id="errorBloqueo" query="idContenido = 'errorBloqueo'" useCache="true"  />
<cm:search id="errorBloqueoDatos" query="idContenido = 'errorBloqueoDatos'" useCache="true"  />
<cm:search id="preingreso_enlace" query="idContenido = 'preingreso_enlace'" useCache="true"  />
<cm:search id="msjRenovacionGrupoControl"
	query="idContenido = 'msjRenovacionGrupoControl'" useCache="true" />
<cm:search id="msjRenovacionGrupoPref"
	query="idContenido = 'msjRenovacionGrupoPref'" useCache="true" />

<preferences:getPreference name="flagBloqueoDesbloqueo"   var="flagBloqueoDesbloqueo"/>
<!-- <script type="text/javascript" src="https://mi.entel.cl/personas/mientelresources/js/ampliacion.numeracion.js"></script> -->
<preferences:getPreference name="pagePreingresoOt"   var="pagePreingresoOt"/>

<f:view beforePhase="#{equipoController.initEquipos}">

	<script type="text/javascript">
		function fillSelects(selectid){ 

			// Vector para saber cu�l es el siguiente combo a llenar
			var combos = new Array();
			combos['regionpromociones'] = "ciudadpromociones"; 
			combos['ciudadpromociones'] = "comunapromociones"; 
			// Tomo el nombre del combo al que se le a dado el clic por ejemplo: pa�s 
			
			posicion = selectid.indexOf('regionpromociones') != -1 ? 'regionpromociones' : 'ciudadpromociones';
			 
			// Tomo el valor de la opci�n seleccionada 
			valor = $("select[id*="+posicion+"]").val(); 
		
			  
			// Evalu� que si es pa�s y el valor es 0, vaci� los combos de estado y ciudad
			if(posicion == 'regionpromociones' && valor == "0"){ 
				  $("select[id*=ciudadpromociones]").html(' <option value="0" selected="selected">Seleccione</option>');
			      $("select[id*=comunapromociones]").html(' <option value="0" selected="selected">Seleccione</option>');
			      $("select[id*=ciudadpromociones]")[0].setValue('0');
			      $("select[id*=comunapromociones]")[0].setValue('0');
			} else { 
				/* En caso contrario agregado el letreo de cargando a el combo siguiente 
				 Ejemplo: Si seleccione pa�s voy a tener que el siguiente seg�n mi vector combos es: estado por qu� combos [pa�s] = estado
				*/ 
				$("select[id*="+combos[posicion]+"]").html('<option value="0">Cargando...</option>');
				$("select[id*="+combos[posicion]+"]")[0].setValue('0');
				$("select[id*=comunapromociones]").html('<option value="0" selected="selected"></option>');
				$("select[id*=comunapromociones]")[0].setValue('0');
				/* Verificamos si el valor seleccionado es diferente de 0 y si el combo es diferente de ciudad, esto porque no tendr�a caso hacer la consulta a ciudad porque no existe un combo dependiente de este */
				if(valor != "0") { 
					// Llamamos a pagina de donde ejecuto las consultas para llenar los combos
					var combo = $("select[id*="+posicion+"]").attr("name"); // Nombre del combo 
					var id =  $("select[id*="+posicion+"]").val();
					  
					var reg = $("select[id*=regionpromociones]").val();
					  
					var url = '<%= request.getContextPath()%>/ServletAreas';
				      //var url = '<%= request.getContextPath()%>/fillSelectAjax.faces';
					$.ajax({
				    	type: 'POST',
				        url: url,
				        dataType: 'json',
				        data : 'combo='+combo+'&id='+id+'&reg='+reg+'&r='+Math.random(),
				        success: function (opt){
							var optionsselect = "";
							//var opt = eval("(" + data + ")");
							optionsselect = '<option value="0">Seleccione</option>';
							for(var i = 0; i< opt.length;i++){
			                	optionsselect += '<option value="'+opt[i].codigo+'">'+opt[i].descripcion+'</option>';
							}
							     
							$("select[id*="+combos[posicion]+"]").html(optionsselect); //Tomo el resultado de pagina e inserto los datos en el combo indicado
							$("select[id*="+combos[posicion]+"]")[0].setValue('0');
						}
					});
				}
			}
		}
		
		
		function setAreas(){
			$("input[id*=hcpromociones]").val($("select[id*=ciudadpromociones]").val());
			$("input[id*=hcompromociones]").val($("select[id*=comunapromociones]").val());
		}
		
		function upper(txt) {
			var txtUpper = $(txt).val().toUpperCase();
			$(txt).val(txtUpper);	
		}

		function amplicarAmpliacion(prefijo){
			var fecha = new Number('<h:outputText value="#{cuentaController.fechaActualFormat}"></h:outputText>');  
		    $('.ampliacionNumerica').attr('maxlength',ampliacionNumerica(parseInt(prefijo, 10),fecha)); 
		    $('.ampliacionNumerica').val(''); 
		}	
		
		$(document).ready(function(){
			 $('.prefijo').change(function(){ 
			      var fecha = new Number('<h:outputText value="#{cuentaController.fechaActualFormat}"></h:outputText>');  
			     $('.ampliacionNumerica').attr('maxlength',ampliacionNumerica(parseInt($('.prefijo').val(), 10),fecha)); 
			     $('.ampliacionNumerica').val(''); 
			 }); 
			 
			 $('.prefijo').trigger("change");

			 $('.prefijoi').change(function(){ 
			      var fecha = new Number('<h:outputText value="#{cuentaController.fechaActualFormat}"></h:outputText>');  
			     $('.ampliacionNumerica').attr('maxlength',ampliacionNumerica(parseInt($('.prefijoi').val(), 10),fecha)); 
			     $('.ampliacionNumerica').val(''); 
			 }); 
			 
			 $('.prefijoi').trigger("change");

			 $('.prefijoRenova').change(function(){ 
			      var fecha = new Number('<h:outputText value="#{cuentaController.fechaActualFormat}"></h:outputText>');  
			     $('.ampliacionNumerica').attr('maxlength',ampliacionNumerica(parseInt($('.prefijoRenova').val(), 10),fecha)); 
			     $('.ampliacionNumerica').val(''); 
			 }); 
			 
			 $('.prefijoRenova').trigger("change");

			 $('div.linea_tabs').find('div.tab.seleccionado').trigger('click');
		});	
		
	</script>

<script type="text/javascript">

	var url='<%=request.getContextPath()%>/portlet/equipo/contactoRenovacion.faces';
	var urlPresupuesto = '<%=request.getContextPath()%>/portlet/equipo/aceptarRechazarPresupuesto.faces';
	var urlBloqueo = '<%=request.getContextPath()%>/portlet/equipo/bloquearEquipo.faces';


	var urlInitBloqueoDesbloqueo = '<%=request.getContextPath()%>/portlet/equipo/initBloqueoDesbloqueoJson.faces';
	var urlBtnBloqueoPaso0 = '<%=request.getContextPath()%>/portlet/equipo/bloquearEquipoPaso0Json.faces';
	var urlBtnBloqueoPaso2 = '<%=request.getContextPath()%>/portlet/equipo/bloquearEquipoPaso2Json.faces';														   
	var urlBtnBloqueoPaso3 = '<%=request.getContextPath()%>/portlet/equipo/bloquearEquipoPaso3Json.faces';
	
	var urlBtnDesbloqueoPaso1 = '<%=request.getContextPath()%>/portlet/equipo/desbloquearEquipoPaso1Json.faces';
	var urlBtnDesbloqueoPaso3 = '<%=request.getContextPath()%>/portlet/equipo/desbloquearEquipoPaso3Json.faces';

	var urlModificarDireccion = '<%=request.getContextPath()%>/portlet/equipo/modificarDireccionJson.faces';

	$(document).ready(function(){

		$('.divDesbloqueoPerdidaExtravio').hide();
		$('.divDesbloqueoRoboHurto').hide();
		$('.divBloqueoRoboPerdida').hide();

		var param = obtenerParametroURL("contexto"); 
		if (param == 'servicioTecnico') {
			$('.servicio_tecnico').trigger("click");
		}else if (param == 'bloqueo') {
			$('.bloquear').trigger("click");
		}

		var aux;
		aux =  $("select[id*=regionpromociones]").val();	
		if(aux!=null){
			$("select[id*=regionpromociones]")[0].setValue('<h:outputText value="#{cuentaController.usuario.direccionContacto.region.codigo}"/>');
	    }

		aux =  $("select[id*=ciudadpromociones]").val();	
		if(aux!=null){
			$("select[id*=ciudadpromociones]")[0].setValue('<h:outputText value="#{cuentaController.usuario.direccionContacto.ciudad.codigo}"/>');
	    }

		aux =  $("select[id*=comunapromociones]").val();	
	    if(aux!=null){
	    	$("select[id*=comunapromociones]")[0].setValue('<h:outputText value="#{cuentaController.usuario.direccionContacto.comuna.codigo}"/>');
	    }

	    $('.linea_tabs').find('div.tab.seleccionado').trigger('click');
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

	function clickBloqueo(){
		 var dataString = "random="+Math.random()*99999;
		$.ajax({
			type: 'POST',
			url: urlInitBloqueoDesbloqueo,
			async: true, 
			data: dataString,
			dataType: 'json',
			cache : false,
			success: function (resp){

				$('.cargandoBloqueo').hide();
				
				if(resp.estado == "Error"){
					$('.divError').show();
				}else{
					if ( resp.respuesta.desbloqueado == "0000" || resp.respuesta.desbloqueado == "5003" || resp.respuesta.desbloqueado == "5004"){
						$(".caja-ultimo .ultimo .nombre").html(resp.respuesta.descripcionEquipo);
						$('.datoSim').html(resp.respuesta.iccid);
						$('#imsiSeleccionada').html(resp.respuesta.msisdn);
						$('#numeroSeleccionado').html(resp.respuesta.msisdn);
						$('#equipo_bloqueo_0').val(resp.respuesta.imsi+'*'+ resp.respuesta.imei+'*'+resp.respuesta.descripcionEquipo+'*'+resp.respuesta.iccid);
						$('.divBloqueoRoboPerdida').show();
						
					}else if ( resp.respuesta.bloqueoRoboHurto == "5000" || resp.respuesta.desbloqueado == "5005"  || resp.respuesta.desbloqueado == "5008"  ){
						$('.divDesbloqueoRoboHurto').show();						
					}else if ( resp.respuesta.bloqueoRoboHurto == "5002" || resp.respuesta.desbloqueado == "5006"  || resp.respuesta.desbloqueado == "5009" ){
						$('.divDesbloqueoHurto').show();						
					}
					 else if ( resp.respuesta.bloqueoPerdidaExtravio == "5001" || resp.respuesta.desbloqueado == "5007"  || resp.respuesta.desbloqueado == "5010" ){
						$(".dato.desbloqueoSim").html(resp.respuesta.iccid);
						$(".dato.desbloqueoEquipo").html(resp.respuesta.equipo);
						$(".dato.desbloqueoImei").html(resp.respuesta.imei);
						$(".dato.desbloqueoFecha").html(resp.respuesta.fechaPerdidaFormated);
						
						$('.divDesbloqueoPerdidaExtravio').show();
					}else{
						 if(resp.respuesta.error=="4999"){
					         $('.divErrorDatos').show();
					     }else if(resp.respuesta.error=="6000"){
					         $('.pendienteBloqueoDesboqueo').show();
					         $('#mensaje_sol_pendiente').html('Actualmente existe una solicitud de bloqueo en curso, la cual quedar&aacute; finalizada en los pr&oacute;ximos minutos.');
					     }else if(resp.respuesta.error=="6001"){
					         $('.pendienteBloqueoDesboqueo').show();
					         $('#mensaje_sol_pendiente').html('Actualmente existe una solicitud de desbloqueo en curso, la cual quedar&aacute; finalizada en los pr&oacute;ximos minutos.');
					     }else if(resp.respuesta.error=="0020"){
					    	 $('.divErrorDesBloqueo').show();
					     }else if(resp.respuesta.error=="0019"){
					    	 $('.divErrorBloqueo').show();
					     }
					     else{
					    	 $('.divError').show();
					     }
					}
				}
			}
		});		
	}

	$('.bloquear').live('click',function(){
		
		if('${flagBloqueoDesbloqueo}' == 2 ){
			setTimeout("clickBloqueo();",500);
		}else{
			$('.cargandoBloqueo').hide();
			$('.divMensajeEquipoBeta').show();
		}
	});		

	function contactoRenovacion(){

		$('#msg').show();
		$.ajax({
			type: 'POST',
			url: url,
			async: false, 
			dataType: 'json',
			data: {n1:document.getElementById('nmb1').value, n2:document.getElementById('nmb2').value, 
			ap1:document.getElementById('ap1').value, ap2:document.getElementById('ap2').value, 
			tel:document.getElementById('prefijoRenova').value+document.getElementById('tel').value},
			success: function (resp){
			 if(resp.estado == 'Ok'){
				 $('#contactoBox').html('<p style="background: url(/personas/framework/skins/mientel/img/correcto_1.gif) no-repeat; width: 430px; height: 58px; padding-left: 70px; text-align: justify">'+resp.detalle+'</p>');
				 $('.capa_renovacion_contacto').hide();	
			 }else{
				 $('#contactoBox').html('<p><strong><cm:getProperty node="${contactoRenovacion[0]}" name="titulo" /></strong><br />'+resp.detalle+'</p>');
			 }
			}
		});
	}

	function aceptarRechazarPresupuesto(nOrden, op){

		$('#msgP').show();
		$('#presupuesto'+nOrden).hide();
		
		$.ajax({
			type: 'POST',
			url: urlPresupuesto,
			async: false, 
			dataType: 'json',
			data: {nroOrden:nOrden, accion:op},
			success: function (resp){
			 if(resp.estado == 'Ok'){
				 if(  op == "<h:outputText value="#{equipoController.atributoAceptacionPrespuesto}" />" ){
					$('#contactoBoxP').html('<p><strong><cm:getProperty node="${aceptacionPresupuesto[0]}" name="titulo" /></strong><br />'+resp.detalle+'<br><a href="javascript:volverOT(\'presupuesto'+nOrden+'\')"><u>Volver</u></a></p>');
					$('#presupuesto'+nOrden).html($('#msgP').html());
				 }	    	 
				 else{
					 $('#contactoBoxP').html('<p><strong><cm:getProperty node="${rechazoPresupuesto[0]}" name="titulo" /></strong><br />'+resp.detalle+'<br><a href="javascript:volverOT(\'presupuesto'+nOrden+'\')"><u>Volver</u></a></p>');
					 $('#presupuesto'+nOrden).html($('#msgP').html());
				 }
			 }else{		     
				 if(  op == "<h:outputText value="#{equipoController.atributoAceptacionPrespuesto}" />" ){
					$('#contactoBoxP').html('<p><strong>Aceptar Presupuesto</strong><br />'+resp.detalle+'<br><a href="javascript:volverOT(\'presupuesto'+nOrden+'\')"><u>Volver</u></a></p>');
				 }	    	 
				 else{
					 $('#contactoBoxP').html('<p><strong>Rechazar Presupuesto</strong><br />'+resp.detalle+'<br><a href="javascript:volverOT(\'presupuesto'+nOrden+'\')"><u>Volver</u></a></p>');
				 }
			 }
			}
		});

	}



	function iphoneGuardarCambios(){
		document.getElementById('nombrevalorI').innerHTML = document.getElementById('nmbi1').value+"&nbsp;"+document.getElementById('nmbi2').value;
		document.getElementById('apellidovalorI').innerHTML =  document.getElementById('api1').value+"&nbsp;"+document.getElementById('api2').value;
		document.getElementById('otromovilvalorI').innerHTML = document.getElementById('prefijoi').value+"&nbsp;"+document.getElementById('teli').value;
		$('#formulario_iphone_editable').hide();
		$('#formularioI').show();
	}

	function renovGuardarCambios(){
		document.getElementById('nombrevalor').innerHTML = document.getElementById('nmb1').value+"&nbsp;"+document.getElementById('nmb2').value;
		document.getElementById('apellidovalor').innerHTML =  document.getElementById('ap1').value+"&nbsp;"+document.getElementById('ap2').value;
		document.getElementById('otromovilvalor').innerHTML = document.getElementById('prefijoRenova').value+"&nbsp;"+document.getElementById('tel').value;	
		$('#formulario_editable').hide();
		$('#formulario').show();
	}

	function paso3(){

		document.getElementById('ps3_fecha_hora').innerHTML = "<strong>"+document.getElementById('seleccion_fecha').value+"&nbsp"+
		document.getElementById('hora_robo').value+":00:00</strong>";
		
		document.getElementById('ps3_clave').innerHTML = "<strong>"+$('.clave_desbloqueo').val()+"</strong>";	
		document.getElementById('ps3_mail').innerHTML = "<strong>"+$('.email_clave').val()+"</strong>";	
		document.getElementById('ps3_tel').innerHTML =  "<strong>"+ $('.telefono_contacto').val()+"</strong>";

		document.getElementById('tip_bloqueo').innerHTML = "<strong>"+($('input[id=tipo_bloqueo_unidireccional]').is(':checked') ? "Unidireccional" : "Bidireccional")+"</strong>";
		
	}

	function bloquearEquipo(){
		
		$('#bloqueo_3').html(
				'<div id="msg_bloqueo_3">'+
					'<div class="caja verde clearfix">'+
						'<div class="caja_texto" id="contactoBoxP">'+
						'<p><strong>Bloqueando equipo</strong><br /><img src="../framework/skins/mientel/img/thickbox/TB_cargando.gif"></img></p>'+
						'</div>'+
					'</div>'+
				'</div>'
				);
		$.ajax({
			type: 'POST',
			url: urlBloqueo,
			async: false, 
			dataType: 'json',
			
			data: {
			nombre:"<h:outputText value="#{cuentaController.usuario.primerNombre}" /> <h:outputText value="#{cuentaController.usuario.segundoNombre}" /> <h:outputText value="#{cuentaController.usuario.apellidoPaterno}" /> <h:outputText value="#{cuentaController.usuario.apellidoMaterno}" />",
			clave:$('.clave_desbloqueo').val(), 
			mail:$('.email_clave').val(), 
			tel:$('.telefono_contacto').val(),
			fecha:document.getElementById('seleccion_fecha').value,
			hora: document.getElementById('hora_robo').value,
			sentido: $('input[id=tipo_bloqueo_unidireccional]').is(':checked') ? 1 : 2,
			bloqueo:$('input[id=bloqueo_robo]').is(':checked') ? 1 : 2,
			area: "<h:outputText value="#{equipoController.datosUsuarioBloqueo.areaTelefono}" />",
			telefono: "<h:outputText value="#{equipoController.datosUsuarioBloqueo.telefono}" />" 
			},
			
			success: function (resp){
			 if(resp.estado == 'Ok'){
				 $('#bloqueo_3').hide();
				 $('#bloqueo_4').show();	    	 
			 }else{
				 $('#bloqueo_4').hide();
				 $('#bloqueo_3').html(
					'<div id="msg_bloqueo_3">'+
						'<div class="caja verde clearfix">'+
							'<div class="caja_texto" id="contactoBoxP">'+
							'<p><strong>Bloqueo equipo</strong><br />'+resp.detalle+'</p>'+									
							'</div>'+
						'</div>'+
					'</div>'
				 );
			 }
			}
			
		});

	}

	function verPresupuesto(pres){	
		$("#tablas_presupuesto").hide();
		$("#"+pres).show();
	}

	function volverOT(pres){	
		$('#msgP').hide();
		$("#"+pres).hide();
		$("#tablas_presupuesto").show();
	}

</script>

	<div class="linea_tabs clearfix">
		<div class="tab contenido1" onclick="mxTracker._trackPageview('Personas/Mi Entel/Mis Productos/Telefonia/Equipo/Renovar');">
			<span class="renovar">
				Renovar
			</span>
		</div>
		
		<div class="tab contenido2" onclick="mxTracker._trackPageview('Personas/Mi Entel/Mis Productos/Telefonia/Equipo/Bloquear');">
			<span class="bloquear">
				Bloquear
			</span>
		</div>
		
		<div class="tab contenido3" onclick="mxTracker._trackPageview('Personas/Mi Entel/Mis Productos/Telefonia/Equipo/Servicio Tecnico');">
			<span class="servicio_tecnico">
				Servicio t&eacute;cnico
			</span>
		</div>

		<div class="tab contenido4" onclick="mxTracker._trackPageview('Personas/Mi Entel/Mis Productos/Telefonia/Equipo/Liberacion');">
			<span class="liberacion">
				Liberaci&oacute;n
			</span>
		</div>
		
	 	<!--<div class="tab contenido5">
			<span class="iphone">
				Solicita Iphone
			</span>
		</div>
		-->
		
	</div>
			
	<div class="contenido_tabs" sytle="border: #000 solid 1px;">
		<div class="contenido_tab altEquipo contenido1">
			<br /><br />
			
		<c:if test="${profile.aaa eq miEntelBean.AAAConsultar || profile.aaa eq miEntelBean.AAAControlTotal || profile.aaa eq miEntelBean.AAAControlParcial}">

		<cm:search id="infoRestriccionAAA0" query="idContenido = 'infoRestriccionAAA0'" useCache="false"/>
		<div class="contenedor-mensajes">
			<ul>
				<li class="mensaje-alerta">
					<div align="center"> 
						<span><cm:getProperty node="${infoRestriccionAAA0[0]}" name="html"/></span>
					</div> 
				</li>
			</ul>
		 </div>
		
		</c:if>					
			
		<entel:view name="renovar">										
			<h:panelGroup rendered="#{!equipoController.grupoControl}">										
				<!-- parrafos -->
				<div class="contenido_parrafos">
					<p><cm:getProperty node="${renovarEquipo[0]}" name="html" /></p>
				</div>
				<!-- /parrafos -->													
				
				<!-- SITUACION ACTUAL -->
				<h2 class="celular">Situaci&oacute;n actual</h2>
				
				<h:panelGroup rendered="#{equipoController.situacionActualEquipo==null}">
				<!-- MENSAJES -->					
				<div id="errorMSG" align="center" class="contenedor-mensajes">
					<h:messages id="messageList" 
						styleClass="mensajes-lista"
						errorClass="mensaje-error" 
						infoClass="mensaje-informacion" showSummary="true" />
				</div>					
				</h:panelGroup>

				<h:panelGroup rendered="#{equipoController.situacionActualEquipo!=null}">
									
				<div class="cuadro_remarcado clearfix">
					<span class="ico_interrogacion">
						<strong><cm:getProperty node="${cuposDisponibles[0]}" name="titulo" /></strong>
					</span>
					<span class="orange">
						<strong><h:outputText value="#{equipoController.resumenLineaEquipoBean.cantMaxEquipoAcoc}"/></strong>
					</span>
					<div class="texto_cuadro">
						<h:outputText value="#{equipoController.infoCuposDisponibles}" />
					</div>
				</div>
				
				<div class="cuadro_remarcado clearfix">
					<span class="ico_interrogacion">
						<strong>Cantidad de l&iacute;neas contratadas:</strong>
					</span>
					<span class="orange">

						<strong><h:outputText value="#{equipoController.situacionActualEquipo.cantidadLineasContratadas}"/></strong>
					</span>
				</div>
				
				<div class="cuadro_remarcado clearfix">
					
					<div class="clearfix">
						<span class="ico_interrogacion">
							<strong><cm:getProperty node="${arriendoEquipo[0]}" name="titulo" /> </strong>
						</span>

						<span class="orange">
							<strong><h:outputText value="#{equipoController.situacionActualEquipo.equiposContratoVigente}"/></strong>
						</span>
					</div>
					
					<div class="texto_cuadro">
						<cm:getProperty node="${arriendoEquipo[0]}" name="html" />
					</div>
				
					<it:iterator value="#{equipoController.situacionActualEquipo.estadoRenovacionEquipo}" rowIndexVar="index" var="object">
				
					<div class="desplegable impar_despelgable clearfix">
						
						<div class="desplegable_colapsado clearfix">																
							
								<span class="titulo_cabecera"><h:outputText value="#{object.equipo}" /></span>
								<div class="desplegable_cerrardo_click clearfix">					
									<div class="icon"></div>
									<a href="#" class="enlace_desplegable_cerrado">Ver detalle de cuotas pendientes</a>
								</div>
							
						</div>
									
						<div class="desplegable_abierto">														
						
							<span class="titulo_cabecera"><h:outputText value="#{object.equipo}" /></span>
							<div class="desplegable_abierto_click clearfix">					
								<div class="icon"></div>
								<a href="#" class="enlace_desplegable">Cerrar detalle de cuotas pendientes</a>
							</div>
							
							<div class="contenido">

								<span class="texto_desplg">
									<label>Serie:</label><label class="derecha"><h:outputText value="#{object.serie}" /></label><br />
									<label>Cuotas totales:</label><label class="derecha"><h:outputText value="#{object.cuotasTotales}" /></label><br />
									<label>Valor cuota:</label><label class="derecha">
									$<h:outputText value="#{object.valorCuota}" >
										<f:convertNumber currencyCode="CLP" locale="es" />
									</h:outputText></label>
								</span>
								
								<!-- Tabla renovacion -->			
								<table class="tabla-azul tabla-azul-renovacion">

									<thead>
										<tr class="cabecera">
											<th>&nbsp;</th>
											<th>Cuotas</th>
											<th class="ultimo">Valor total</th>
										</tr>
									</thead>
									<tbody>

										<tr class="impar">
											<td>Cuotas pagadas</td>
											<td><h:outputText value="#{object.cuotasPagadas}" /></td>
											<td>
											$<h:outputText value="#{object.totalPagado}" >
												<f:convertNumber currencyCode="CLP" locale="es" />
											</h:outputText></td>
										</tr>
										<tr class="par">
											<td>Cuotas pendientes</td>

											<td><h:outputText value="#{object.cuotasPendientes}" /></td>
											<td>
											$<h:outputText value="#{object.pendientePagar}">
												<f:convertNumber currencyCode="CLP" locale="es" />
											</h:outputText></td>
										</tr>
										<tr class="impar">
											<td>&nbsp;</td>
											<td>Descuento cliente</td>
											<td>
											$<h:outputText value="#{object.descuentoCliente}">
												<f:convertNumber currencyCode="CLP" locale="es" />
											</h:outputText></td>

										</tr>
									</tbody>
								</table>
								<!--/tabla zonapuntos-->
								
								<div class="final_tabla">
									<div class="clearfix">
										<div class="label"><strong><cm:getProperty node="${renovacionAnticipada[0]}" name="titulo" /></strong></div>
										<div class="valor"><strong>
										$<h:outputText value="#{object.totalPagar}">
											<f:convertNumber currencyCode="CLP" locale="es" />
										</h:outputText></strong></div>

									</div>
								</div>

								<span class="mensaje"><cm:getProperty node="${renovacionAnticipada[0]}" name="html" /></span>

							</div>
							
					</div>
					
					
					
					</div>
					
					</it:iterator>
					
					
					<div class="texto_cuadro">
						<cm:getProperty node="${infoEquiposPagados[0]}" name="html" />
					</div>

				</div>
				
				<!-- OFERTA DE EQUIPOS -->
				<h2 class="lupa"><cm:getProperty node="${ofertaEquipos[0]}" name="titulo" /></h2>
				
				<div class="contenido_oferta clearfix">
					<cm:getProperty node="${ofertaEquipos[0]}" name="html" />

				</div>
									
				</h:panelGroup>
									
				<!-- CONTACTO POR RENOVACION -->			
				<div class="boton_formulario">
					<h2 class="maletin"><cm:getProperty node="${contactoRenovacion[0]}" name="titulo" /></h2>
					
					<div id="msg" style="display:none;">
						<div class="caja verde clearfix">
							<div class="caja_texto" id="contactoBox">
								<p><strong>Guardando contacto por renovacion</strong><br /><img src="../framework/skins/mientel/img/thickbox/TB_cargando.gif"></img></p>
							</div>
						</div>
					</div>
											
			 <div class="capa_renovacion_contacto">
						
					<div class="clearfix">
						<span class="texto"><cm:getProperty node="${contactoRenovacion[0]}" name="html" /></span>

					</div>						
					<div class="desplegable impar_despelgable desplegable_ultimo clearfix">
						<div class="desplegable_colapsado clearfix">
							<span class="titulo_cabecera_formulario">Solicitud de contacto</span>
							<div class="desplegable_cerrardo_click clearfix">					
								<div class="icon"></div>
								<a href="#" class="enlace_desplegable_cerrado">Ver formulario</a>
							</div>

						</div>
									
						<div class="desplegable_abierto">
							<div class="clearfix">
								<span class="titulo_cabecera_formulario">Solicitud de contacto</span>
								<div class="desplegable_abierto_click clearfix">
									<div class="icon"></div>
									<a href="#" class="enlace_desplegable">Cerrar formulario</a>
								</div>

							</div>				
							
							<div class="contenido clearfix">
								
								<div id="exitoEnvio">
									<cm:getProperty node="${msgEnvioSolicitud[0]}" name="html" />
								</div>						
								
								<div id="formulario" class="formulario_datos clearfix">
									
									<div class="titulo_formulario clearfix">
										<span class="enlace_modificar"><a href="#" class="btnModificarDatos">Modificar</a></span>
									</div>
												
									<div class="contenido_datos clearfix" style="padding-bottom: 15px;">

										
										<div class="contenedor_datos_fijos clearfix">
											<div class="dato">
												<label class="titulo">RUT:</label>													
												<label><h:outputText value="#{cuentaController.usuario.rut}" /></label>											
											</div>
											<div class="dato">
												<label class="titulo">M&oacute;vil Entel:</label>
												<label><h:outputText value="#{cuentaController.usuario.numeroPCS}" /></label>
											</div>
										</div>
																					
										<div class="dato_formulario_grande clearfix">
											<span class="dato">Nombres:</span>
											<span id="nombrevalor" class="valor clearfix">
											<h:outputText value="#{cuentaController.usuario.primerNombre}" /><h:outputText value=" " /><h:outputText value="#{cuentaController.usuario.segundoNombre}" />
											</span>
										</div>

										<div class="dato_formulario_grande clearfix">
											<span class="dato">Apellidos:</span>
											<span id="apellidovalor" class="valor clearfix">
											<h:outputText value="#{cuentaController.usuario.apellidoPaterno}" /><h:outputText value=" " /><h:outputText value="#{cuentaController.usuario.apellidoMaterno}" />
											</span>
										</div>
										<div class="dato_formulario_grande clearfix">
											<span class="dato">Tel&eacute;fono adicional de contacto:</span>

											<span id="otromovilvalor" class="valor clearfix"><h:outputText value=" " /></span>
										</div>
									</div>
									
									<div class="contenedor_botones_formulario clearfix">
										<a href="#" class="btnAzulGrande" onclick="mxTracker._trackPageview('Personas/Mi Entel/Mis Productos/Telefonia/Equipo/Renovar/Solicitado'); javascript:contactoRenovacion();"><span>Enviar solicitud</span></a>
										<span class="texto"><a href="#" class="cancelarSolicitud">Cancelar</a></span>
									</div>

									
								</div>
								
							</div>
								
								<div id="formulario_editable" class="formulario_datos clearfix">
									
									<div class="titulo_formulario clearfix">
										&nbsp;
									</div>
									
									<div class="contenido_datos clearfix" style="padding-bottom: 15px;">
										
										<div class="contenedor_datos_fijos clearfix">
											<div class="dato">
												<label class="titulo">RUT:</label>

												<label><h:outputText value="#{cuentaController.usuario.rut}" /></label>
												
											</div>
											<div class="dato">
												<label class="titulo">M&oacute;vil Entel:</label>
												<label><h:outputText value="#{cuentaController.usuario.numeroPCS}" /></label>
											</div>
										</div>
										
										<form class="formularioDatos" method="post">
											<jsp:include page="/token.jsp" flush="true"/>
											<div class="dato_formulario clearfix">
												<span class="dato">Nombres:</span>
												<div class="valor">
													<div class="valor_left">
														<input name="nombre1edit" id="nmb1" type="text" class="input_texto inputBoxAmarillo nombre1edit required" maxlength="20" style="width: 130px;" value="<h:outputText value="#{cuentaController.usuario.primerNombre}" />"/>
													</div>
													<div class="valor_left">

														<input name="nombre2edit" id="nmb2" type="text" class="input_texto inputBoxAmarillo nombre2edit required" maxlength="20" style="width: 130px;" value=" <h:outputText value="#{cuentaController.usuario.segundoNombre}" />"/>
													</div>
												</div>
											</div>
											<div class="dato_formulario clearfix">
												<span class="dato">Apellidos:</span>
												<div class="valor">
													<div class="valor_left">
														<input name="apellido1edit" id="ap1" type="text" class="input_texto inputBoxAmarillo apellido1edit required" maxlength="20" style="width: 130px;" value="<h:outputText value="#{cuentaController.usuario.apellidoPaterno}" />"/>
													</div>
													<div class="valor_left">
														<input name="apellido2edit" id="ap2" type="text" class="input_texto inputBoxAmarillo apellido2edit required" maxlength="20" style="width: 130px;" value="<h:outputText value="#{cuentaController.usuario.apellidoMaterno}" />"/>
													</div>
												</div>
											</div>
											<div class="dato_formulario clearfix">
												<span class="dato">Tel&eacute;fono adicional:</span>
												
												<div class="valor">
													<div style="display: block; float: left;">
														<select class="selectBoxAmarillo prefijoRenova" id="prefijoRenova" name="prefijoRenova" style="width:50px;" size="4">
															<it:iterator value="#{cuentaController.prefijosTelefono}" var="prefijo">
																<option value="<h:outputText value="#{prefijo.value}" />"><h:outputText value="#{prefijo.value}" /></option>
															</it:iterator>
														</select>
													</div>
													<div style="display: block; float: left;">
														<input name="telefonoadd" id="tel" type="text" class="input_numerico inputBoxAmarillo telefonoadd required ampliacionNumerica" maxlength="8" style="width: 80px;" onkeypress="return soloNumeros(event);"/>
													</div>													
												</div>
											</div>
										</form>
										
									</div>
									
									<div class="contenedor_botones_formulario clearfix">
										<a class="btnAzulGrande guardarDatos"><span>Guardar Cambios</span></a>

										<span class="texto"><a href="#" class="btnCancelarEdicion">Cancelar</a></span>
									</div>
									
								</div>
															
							</div>
						</div>
					</div>
											
				</div>
			</h:panelGroup>
			
			<h:panelGroup rendered="#{equipoController.grupoControl}">
				<cm:getProperty node="${msjRenovacionGrupoControl[0]}" name="html" />
			</h:panelGroup>
			<h:panelGroup rendered="#{equipoController.grupoPreferencial}">
				<cm:getProperty node="${msjRenovacionGrupoPref[0]}" name="html" />
			</h:panelGroup>
		</entel:view>
		</div>
		
		


		<!-- INICIO BLOQUEO - DESBLOQUEO EQUIPOS  --> 
		<div class="divMensajeEquipoBeta" style="display:none">
			<div class="contenido_tab altEquipo contenido2"> 
				</br> 
				<div class="mensajeServicio"> 
					<h:outputText value="#{equipoController.mensajeEquipoBeta}" escape="false"/> 
				</div> 
			</div>
		</div> 
		<!-- FIN BLOQUEO - DESBLOQUEO EQUIPOS  -->		
			
		
		<!-- INICIO BLOQUEO - DESBLOQUEO EQUIPOS  -->
		<div class="contenido_tab altEquipo contenido2">

			<br /><br />
					
				<div class="cargandoBloqueo" style="display:block"><center><br><img src='../framework/skins/mientel/img/thickbox/TB_cargando.gif'/></b><br><br></center></div>
				
				<div class="bloqueo">
                    <div class="divErrorBloqueo" style="display:none" >
                        <div class="caja-informacion">
                            <p><cm:getProperty node="${errorBloqueo[0]}" name="html" /></p>
                        </div>
                 	</div>  
                 	 
                 	<div class="divErrorDesBloqueo" style="display:none" >
                        <div class="caja-informacion">
                            <p><cm:getProperty node="${errorDesBloqueo[0]}" name="html" /></p>
                        </div>
                 	</div> 
                 	
                 	<div class="divError" style="display:none" >
                        <div class="caja-informacion">
                            <p>En este momento el servicio no se encuentra disponible.Por favor intente m&aacute;s tarde.</p>
                        </div>                 	
                    </div>
                     <div class="divErrorDatos" style="display:none" >
                        <div class="caja-informacion">
                            <p><cm:getProperty node="${errorBloqueoDatos[0]}" name="html" /></p>
                        </div>                 	
                    </div>
                 </div>
			
					 
                    
				<div id="bloqueo_0" class="bloqueo">
				
				
					<div class="divDesbloqueoRoboHurto" style="display:none" >
						<h1>Desbloqueo</h1><br />			

                        <div class="tabla">
							<div class="header_tabla clearfix">
								<div class="top"><span></span></div>
									<div class="main">
									<div class="contenido">
                                   	<br /><br />
                                   	<h4><strong>Actualmente tienes tu equipo y SIM bloqueada por Robo </strong></h4><br />
                                        <p>Para desbloquear debes acudir a unas de nuestras tiendas con tu c&eacute;dula de identidad.</p>
                                        <br /><br />
                                    </div>					
								</div>
								<div class="bottom"><span></span></div>
							</div>
						</div>
                        <div class="pie-pagina">
                        	<p>Tambi&eacute;n puedes llamar al 800367626, n&uacute;mero gratuito exclusivo para la atenci&oacute;n de bloqueos.</p>
                        </div>
                    </div>
                    
                   <div class="divDesbloqueoHurto" style="display:none" >
						<h1>Desbloqueo</h1><br />			

                        <div class="tabla">
							<div class="header_tabla clearfix">
								<div class="top"><span></span></div>
									<div class="main">
									<div class="contenido">
                                   	<br /><br />
                                   	<h4><strong>Actualmente tienes tu equipo y SIM bloqueada por Hurto</strong></h4><br />
                                        <p>Para desbloquear debes acudir a unas de nuestras tiendas con tu c&eacute;dula de identidad.</p>
                                        <br /><br />
                                    </div>					
								</div>
								<div class="bottom"><span></span></div>
							</div>
						</div>
                        <div class="pie-pagina">
                        	<p>Tambi&eacute;n puedes llamar al 800367626, n&uacute;mero gratuito exclusivo para la atenci&oacute;n de bloqueos.</p>
                        </div>
                    </div>
                    
                  <div class="pendienteBloqueoDesboqueo" style="display:none" >
						<h1>Solicitud en curso</h1><br />			

                        <div class="tabla">
							<div class="header_tabla clearfix">
								<div class="top"><span></span></div>
									<div class="main">
									<div class="contenido">
                                   	<br /><br />                                   	
                                        <p id="mensaje_sol_pendiente"></p>
                                    <br /><br />
                                    </div>					
								</div>
								<div class="bottom"><span></span></div>
							</div>
						</div>                        
                    </div>
 
                    
                    <div class="divBloqueoRoboPerdida" style="display:none" >
                    
					<!-- Bloqueo por Robo / Hurto && perdida o extravio -->
							<!-- Bloqueo paso 0 -->
								<h1>Bloqueo / Desbloqueo</h1><br />								
								
								<!-- parrafos -->
								<div class="contenido_parrafos">
									<p><strong>Inhabilita tanto el equipo como la tarjeta SIM (chip entel) asociado al N&deg;.</strong><span id="numeroSeleccionado"></span></p>
									<p>Los datos del equipo y SIM ingresan a la base de m&oacute;viles bloqueados intercompa&ntilde;&iacute;as lo que <strong>impide su utilizaci&oacute;n tanto para la emisi&oacute;n como para la recepci&oacute;n</strong> de llamados con cualquier proveedor de telefon&iacute;a del pa&iacute;s</p>
									<p>Ambas acciones son posibles de revertir en caso de recuperar tu SIM y Equipo, esto a trav&eacute;s del servicio de desbloqueo en l&iacute;nea (s&oacute;lo p&eacute;rdida) o llamando al <strong>800367626</strong> o acerc&aacute;ndote a una <a class="linea" href="http://www.entel.cl/personas/sucursales.iws" target="_blank">tienda</a> Entel (p&eacute;rdida o robo/hurto).</strong></p>
								</div>
							<!-- /parrafos -->
								
							<!-- tablas -->
		                        <div class="tabla clearfix">
									<div class="header_tabla_blanco caja-peq izquierda">
										<div class="top"><span></span></div>
										<div class="main_blanco">
											<table>
												<tr>
													<th width="100%" class="ultimo">
														<div class="tabla_contenido_titulo"><strong>Bloqueo de la tarjeta SIM asociada al N&deg;</strong> <span id="imsiSeleccionada"></span> </div><br />	
		                                                    <div>										
			                                                    <ul>
			                                                        <li>Debe ser la cuenta de quien realiza el bloqueo.</li>
			                                                        <li>Quedar&aacute; bloqueada inmediatamente al realizar el bloqueo.</li>
			                                                    </ul>
		                                                	</div>
													</th>
												</tr>							
											</table>					
										</div>
										<div class="bottom_blanco_1"><span></span></div>
									</div>
		
		                            <div class="header_tabla_blanco caja-peq">
										<div class="top"><span></span></div>
										<div class="main_blanco">
											<table>
												<tr>
													<th width="100%" class="ultimo">
														<div class="tabla_contenido_titulo"><strong>Bloqueo del equipo</strong></div>
		                                                	<br />	
		                                                    <div>										
			                                                    <ul>
			                                                        <li>Debe ser un equipo de Entel, en caso contrario debes llamar al 800 367 626.</li>
			                                                        <li>Debe haber sido utilizado con la tarjeta SIM que se bloquea.</li>
			                                                        <li>El equipo estar&aacute; bloqueado luego de 2 horas realizada la solicitud.</li>
			                                                    </ul>
		                                                </li>
													</th>
												</tr>							
											</table>					
										</div>
										<div class="bottom_blanco_1"><span></span></div>
									</div>
								</div>
		
		                        <div class="seleccion-motivo clearfix">
		                        	<p><strong>Selecciona el motivo de bloqueo:</strong></p>
		
		                            <div class="test opcion-motivo">
		                                <label>
		                                    <input type="radio" name="bloqueo-motivo" class="radio_button alineado" id="bloqueo_robo" />
		                                    <span class="texto_radio_button alineado">Robo</span>
		                                </label>
		                                <p>Si bloqueas tu equipo y SIM por robo tendr&aacute;s que acudir a alguna de nuestras <a class="linea" href="http://www.entel.cl/personas/sucursales.iws" target="_blank">tiendas</a> y presentar tu c&eacute;dula de identidad para realizar el desbloqueo.</p>
		                            </div>
		                            
		                            <div class="opcion-motivo">
		                                <label>
		                                    <input type="radio" name="bloqueo-motivo" class="radio_button alineado" id="bloqueo_hurto" />
		                                    <span class="texto_radio_button alineado">Hurto</span>
		                                </label>
		                                <p>Si bloqueas tu equipo y SIM por hurto tendr&aacute;s que acudir a alguna de nuestras <a class="linea" href="http://www.entel.cl/personas/sucursales.iws" target="_blank">tiendas</a> y presentar tu c&eacute;dula de identidad para realizar el desbloqueo.</p>
		                            </div>		                            
		
		                            <div class="opcion-motivo">
		                                <label>
		                                    <input type="radio" name="bloqueo-motivo" class="radio_button alineado" id="bloqueo_perdida" />
		                                    <span class="texto_radio_button alineado">P&eacute;rdida o extrav&iacute;o</span>
		                                </label>
		                                <p>Si bloqueas tu equipo y SIM por p&eacute;rdida o extrav&iacute;o podr&aacute;s realizar el desbloqueo en Mi Entel (Web) o a trav&eacute;s del Call Center (<strong>800 367 626</strong>)</p>
		                            </div>
		
		                            <div class="formulario_boton" style=" height: 48px">
		                                <a class="btnAzulGrandeDesactivado btnAzulGrandeDesactivadoLargo btnBloqueoPaso0" onclick="mxTracker._trackPageview('Personas/Mi Entel/Mis Productos/Telefonia/Equipo/Bloquear/Datos');"><span>Continuar</span></a>
		                                <div class="loading" ></div>
		                            </div>
		                        </div>
		
							<!-- /tabla -->
		
							<!-- debes saber-->
		
		                        <div class="tabla">
									<div class="header_tabla_blanco clearfix">
										<div class="top"><span></span></div>
										<div class="main_blanco">
											<div class="contenido">
		                                    	<h4><strong>Consideraciones</strong></h4><br />
		                                        <ul>
		                                        	<li>El bloqueo del equipo no te deja exento del pago del cargo fijo mensual.Tampoco te deja exento al pago de las cuotas pendientes por el equipo en caso de que &eacute;ste se encuentre bajo la modalidad de &quot;arriendo con opci&oacute;n de compra&quot;.</li>
		                                            <li>Si no recuperas tu tarjeta SIM (chip Entel) y quieres comprar una nueva, puedes adquirirla en cualquier tienda de Entel , pagando el costo de &eacute;sta. Presentando tu c&eacute;dula de identidad, podr&aacute;s mantener tu n&uacute;mero y plan. (Si el tr&aacute;mite es realizado por un tercero, deber&aacute; presentar un poder notarial).</li>
		                                            <li>Si necesitas comprar un nuevo equipo, lo puedes hacer en cualquier tienda de Entel.</li>
		                                            <li>Tus cuentas deben estar al d&iacute;a.</li>
		                                            <br />
		                                        </ul>
		                                    </div>					
										</div>
		
										<div class="bottom_blanco_1"><span></span></div>
									</div>
								</div>
		
							<!-- /debes saber-->
		
		                        <div class="pie-pagina">
		                        	<p>Tambi&eacute;n puedes llamar al 800367626, n&uacute;mero gratuito exclusivo para la atenci&oacute;n de bloqueos.</p>
		                        </div>
						<!-- /Bloqueo paso 0 -->                    
                    </div>
                    
				</div>							
							
                    

					<!-- Bloqueo paso 1 robo -->

					<div id="bloqueo_1" style="position: relative;" class="bloqueo">

						<div id="bloqueo_titulo">

							<h1>Bloqueo por Robo / Hurto<span>Paso 1 de 3</span></h1>

                        </div>

						<br />

                        

                        <!-- caja informacion -->

                        <div class="caja-informacion">

                            <p>Si bloqueas tu equipo y SIM por robo o hurto tendr&aacute;s que acudir a alguna de nuestras 
                            <a class="linea" href="http://www.entel.cl/personas/sucursales.iws" target="_blank" >tiendas</a>  y presentar tu c&eacute;dula de identidad para  realizar el desbloqueo.</p>

                        </div>

                        <!-- /caja informacion -->

                        

                        <!-- formulario -->

                        <p>La informaci&oacute;n mostrada a continuaci&oacute;n corresponde a la registrada en tus datos de usuario. Si deseas modificarla estar&aacute; actualizando  tu registro en Mi Entel.</p>

                        <form class="bloqueoFormulario" method="post" action="#" style="position:relative; z-index:1;" title="formBloqueo">
							<jsp:include page="/token.jsp" flush="true"/>
                            <div class="fieldset clearfix">

                                <div class="fila-campo clearfix">

                                    <label>Direcci&oacute;n:*</label>

                                    <div class="campo">
                                    <input id="dirBloqueo" type="text" value="<h:outputText value="#{cuentaController.usuario.direccionContacto.calle}"/>" name="dirBloqueo" class="inputBoxBloqueo dirBloqueo" style="width:280px;" readonly="readonly" title="direccion" /></div>

                                    <div class="campo modificar"><a class="modificarDirBloqueo" href="#">Modificar</a></div>

                                </div>                                

                                <div class="fila-campo clearfix">

                                    <label>e-mail:*</label>

                                    <div class="campo"><input id="emailBloqueo" type="text" name="emailBloqueo" class="inputBoxBloqueo emailBloqueo" style="width:200px;" readonly="readonly" title="email" /></div>

                                    <div class="campo modificar"><a class="modificarMailBloqueo" href="#">Modificar</a></div>

                                </div>                                

                                <div class="fila-campo clearfix">

                                    <label>Tel&eacute;fono contacto:*</label>

                                    <div class="campo">
										<h:selectOneMenu id="telAdicionalNumBloqueo" label="telAdicionalNumBloqueo" 
											styleClass="selectBoxBloqueo telAdicionalNumBloqueo prefijo codigo_tel_adicional" style="width:50px;" title="indTelAdicional">
											   <f:selectItems value="#{cuentaController.prefijosTelefono}"/>
									    </h:selectOneMenu>
									</div>
									
                                    <div class="campo"><input type="text" id="telAdicionalBloqueo" name="telAdicionalBloqueo" class="inputBoxBloqueo telAdicionalBloqueo input_numerico ampliacionNumerica" maxlength="7"  style="width:150px;" title="telAdicional" /></div>

                                    <div class="campo"><span class="ejemplo">Ej: 02 3333333</span></div>

                                </div>

                                <div class="fila-campo clearfix">

                                    <label>Fecha:*</label>

                                    <div class="campo" style="width:115px;padding:0px;"><input id="fechaRoboSeleccion" type="text" name="fechaRobo" class="inputBoxBloqueo fechaRobo" maxlength="30" style="width:90px;float:left;display:block;" readonly="readonly" title="fecha" /></div>

                                    <div class="campo" style="width: 220px" ><span class="ejemplo FechaAprox fecha">Fecha aproximada en la que ocurri&oacute; el robo o hurto</span></div>

                                </div>

                                <div class="fila-campo clearfix campohorarobo">

                                    <label>Hora:</label>

                                    <div class="campo campoHoraRobo">

                                        <select name="horaRobo" class="selectBoxBloqueo horaRobo" style="width:78px" title="hora">
                                        	
                                        	<option selected="selected" value="0">--</option>

                                            <option value="01">01</option>

                                            <option value="22">02</option>

                                            <option value="03">03</option>

                                            <option value="04">04</option>

                                            <option value="05">05</option>

                                            <option value="06">06</option>

                                            <option value="07">07</option>

                                            <option value="08">08</option>

                                            <option value="09">09</option>

                                            <option value="10">10</option>

                                            <option value="11">11</option>

                                            <option value="12">12</option>

                                        </select>

                                    </div>
                                    <div class="campo campoMinutoRobo">
                                        <select name="minutoRobo" class="selectBoxBloqueo minutoRobo" style="width:78px" title="minuto">
                                            </option>
                                            <option value="0" selected="selected">--</option>
                                            <option value="00">00</option>  
                                            <option value="01">01</option>  
                                            <option value="02">02</option>  
                                            <option value="03">03</option>  
                                            <option value="04">04</option>  
                                            <option value="05">05</option>  
                                            <option value="06">06</option>  
                                            <option value="07">07</option>  
                                            <option value="08">08</option>  
                                            <option value="09">09</option>  
                                            <option value="10">10</option>  <option value="11">11</option>  <option value="12">12</option>  <option value="13">13</option>  <option value="14">14</option>  <option value="15">15</option>  <option value="16">16</option>  <option value="17">17</option>  <option value="18">18</option>  <option value="19">19</option>  <option value="20">20</option>  <option value="21">21</option>  <option value="22">22</option>  <option value="23">23</option>  <option value="24">24</option>  <option value="25">25</option>  <option value="26">26</option>  <option value="27">27</option>  <option value="28">28</option>  <option value="29">29</option>  <option value="30">30</option>  <option value="31">31</option>  <option value="32">32</option>  <option value="33">33</option>  <option value="34">34</option>  <option value="35">35</option>  <option value="36">36</option>  <option value="37">37</option>  <option value="38">38</option>  <option value="39">39</option>  <option value="40">40</option>  <option value="41">41</option>  <option value="42">42</option>  <option value="43">43</option>  <option value="44">44</option>  <option value="45">45</option>  <option value="46">46</option>  <option value="47">47</option>  <option value="48">48</option>  <option value="49">49</option>  <option value="50">50</option>  <option value="51">51</option>  <option value="52">52</option>  <option value="53">53</option>  <option value="54">54</option>  <option value="55">55</option>  <option value="56">56</option>  <option value="57">57</option>  <option value="58">58</option>  <option value="59">59</option>
                                        </select>

                                    </div>

                                    <div class="campo campoAMPMRobo">

                                        <select name="ampmRobo" class="selectBoxBloqueo ampmRobo" style="width:78px" title="am_pm">
											<option selected="selected" value="0">--</option>
                                            <option value="1">AM</option>

                                            <option value="2">PM</option>

                                        </select>

                                    </div>

                                    <div class="campo campoDesHora" ><span class="ejemplo horaBloq corto">Hora del robo o hurto</span></div>

                                </div>

                                <div class="fila-campo clearfix">

                                    <label>Unidad policial,<br /> Fiscalia o Tribunal:</label>

                                    <div class="campo"><input id="denuncioBloqueo" type="text" name="denuncioBloqueo" class="inputBoxBloqueo denuncioBloqueo" style="width:200px;" title="unidadDenuncia" /></div>

                                    <div class="campo"><span class="ejemplo corto">Ingresa en caso de existir denuncia</span></div>

                                </div>

                                <!--
                                <div class="fila-campo clearfix">

                                    <label>Fecha denuncio:</label>

                                    <div class="campo" style="width:115px;padding:0px;"><input id="fechaDenuncioSeleccion" type="text" name="fechaDenuncioRobo" class="inputBoxBloqueo fechaDenuncioRobo" maxlength="30" style="width:90px;float:left;display:block;" readonly="readonly" /></div>

                                    <div class="campo"><span class="ejemplo fechaDenuncia fecha">Fecha de la denuncia en caso de existir</span></div>

                                </div>
                                -->

                                <div class="fila-campo clearfix">

                                    <label>N&deg; Parte o Rol de la causa:</label>

                                    <div class="campo"><input id="denuncioBloqueo1" type="text" name="denuncioBloqueo1" class="inputBoxBloqueo denuncioBloqueo1" style="width:200px;" title="numDenuncia" /></div>

                                    <div class="campo"><span class="ejemplo corto">Ingresa en caso de existir denuncia</span></div>

                                </div>

                                <div class="clave" style="display:none">

                                	<div class="fila-campo clearfix">

                                        <label>Ingresa clave:*</label>

                                        <div class="campo"><input id="claveBloqueo" type="text" name="claveBloqueo" class="inputBoxBloqueo claveBloqueo input_numerico" maxlength="4" style="width:120px;" title="clave"/></div>

                                        <div class="campo"><span class="ejemplo">La clave debe estar compuesta de 4 digitos num&eacute;ricos</span></div>

                                    </div>

                                    <div class="fila-campo clearfix">

                                        <label>Reingresa clave:*</label>

                                        <div class="campo"><input id="claveBloqueo1" type="text" name="claveBloqueo1" class="inputBoxBloqueo claveBloqueo1 input_numerico" maxlength="4" style="width:120px;" title="reClave"/></div>

                                    </div>

                                </div>

                            </div>

                            <!-- BTN CONTINUAR -->

                            <div class="fieldset">

                                <div class="fila-campo clearfix">

                                    <label class="ancho anchoLabel"><span>* Campos obligatorios</span></label>

                                    <div class="campo"><a id="btnCancelarPaso1" style="cursor: pointer;" class="btnCancelarPaso1 cancelar"><span>Cancelar</span></a></div>

                                    <div class="campo">
                                    	<a id="btnBloqueoPaso1" class="btnBloqueoPaso1 btnAzul" onclick="mxTracker._trackPageview('Personas/Mi Entel/Mis Productos/Telefonia/Equipo/Bloquear/Equipo');"><span>Continuar</span></a>
                                    	<div class="loading" ></div>
                                    </div>
                                    

                                </div>

                            </div>

                            <!-- /BTN CONTINUAR -->

                        </form>

                        <!-- /formulario -->


 						<!-- formulario direccion -->

                         <div id="modificarDirCaja" class="box-numero modificarDirCaja">
                            <form id="modificarDireccion" class="modificarDireccion" name="modificarDireccion" style="" action="#">
								<jsp:include page="/token.jsp" flush="true"/>
                            
                                <input type="hidden" name="s_region" value="">
                                <input type="hidden" name="s_ciudad" value="">
                                <input type="hidden" name="s_comuna" value="">
                            
                                <fieldset>
                                    
						              <!-- MIS DATOS FILA -->
										<div class="mis-datos-fila clearfix">
											<label>Regi&oacute;n:*</label>
											<div class="campo regionSelect">
												<div class="campo-amarillo ">
													<h:selectOneMenu onchange="fillSelects(this.id)" 
												 		id="regionpromociones"
												 		value="#{cuentaController.usuario.direccionContacto.region.codigo}" 
												 		styleClass="selectBoxAmarillo region-promociones select_regiones_bloqueo" style="width:250px;">
						                                     <f:selectItems value="#{cuentaController.regionesList}"/>
						                        	</h:selectOneMenu>
												</div>
											</div>
											
											<div class="regionText" style="display: none" ></div>
											
										</div>
						                <!-- MIS DATOS FILA -->
				
										<!-- MIS DATOS FILA -->
										<div class="mis-datos-fila clearfix">
											<label>Ciudad:*</label>
											<div class="campo ciudadSelect">
												<div class="campo-amarillo select_ciudad_bloqueo">
													<select onchange="fillSelects(this.id)"
														id="ciudadpromociones" 
														class="selectBoxAmarillo select_ciudad_bloqueo" style="width:250px;">
														<it:iterator var="item" value="#{cuentaController.ciudadesList}">
														    <option value="<h:outputText value="#{item.codigo}"/>"><h:outputText value="#{item.codigo}"/></option>
														</it:iterator>
						                            </select>
						                            <h:inputHidden id="hcpromociones" value="#{cuentaController.usuario.direccionContacto.ciudad.codigo}"/> 
												</div>
												
											</div>
											
											<div class="ciudadText" style="display: none" ></div>
											
										</div>
						                <!-- /MIS DATOS FILA -->
								
						                <!-- MIS DATOS FILA -->
										<div class="mis-datos-fila clearfix">
											<label>Comuna:*</label>
											<div class="campo comunaSelect">
												<div class="campo-amarillo ">
													<select onchange="setAreas()"
														id="comunapromociones"
														class="selectBoxAmarillo select_comuna_bloqueo" style="width:250px;">
														<it:iterator var="itemc" value="#{cuentaController.comunasList}">
														    <option value="<h:outputText value="#{itemc.codigo}"/>"><h:outputText value="#{itemc.codigo}"/></option>
														</it:iterator>
						                             </select>
						                            <h:inputHidden id="hcompromociones" value="#{cuentaController.usuario.direccionContacto.comuna.codigo}"/> 
												</div>
												
											</div>
											
											<div class="comunaText" style="display: none" ></div>
										</div>
						                <!-- /MIS DATOS FILA -->
								
						                <!-- MIS DATOS FILA -->
										<div class="mis-datos-fila clearfix">
											<label>Calle:*</label>
											<div class="campo calleInput">
												<div class="campo-amarillo ">
													<span class="left"></span><h:inputText value="#{cuentaController.usuario.direccionContacto.calle}" styleClass="calle" id="calle" maxlength="30" style="width:240px" onblur="upper(this);" onkeyup="upper(this);" /><span class="right"></span>
												</div>
												
											</div>
											
											<div class="calleText" style="display: none" ></div>
											
										</div>
						                <!-- /MIS DATOS FILA -->
								
						                <!-- MIS DATOS FILA -->
										<div class="fila clearfix">
										
										<label>N&uacute;mero:*</label>
											
											<div class="campo numeroInput">
												<div class="campo-amarillo ">
													<span class="left"></span><h:inputText id="numero" styleClass="numero" maxlength="30" style="width:50px" onkeypress="return soloNumeros(event);" value="#{cuentaController.usuario.direccionContacto.numero}" /><span class="right"></span>
												</div>
											</div>
											<div class="campo numeroText" style="width:50px" ></div>
											
											<div class="subcampo clearfix">
												<label>Depto / Oficina:</label>
												
												<div class="campo-amarillo deptoInput">
													<span class="left"></span><h:inputText styleClass="depto_casa_otro" value="#{cuentaController.usuario.direccionContacto.departamento}" maxlength="30" style="width:50px" /><span class="right"></span>
												</div>
												
												<div class="campo dptoText" style="width:50px"  ></div>
												
												<div class="campo"><a class="btnGuardarDirbloqueo btnVerde"><span>Guardar</span></a></div>
											</div>
										</div>
				               	
				               	 <!-- /MIS DATOS FILA -->
                                    <div class="fila mensaje clearfix">
                                        <div class="campo">Este cambio afectar&aacute; el registro de la direcci&oacute;n de tus datos de usuario.<br />&iquest;Est&aacute;s seguro que deseas modificarlos?</div>
                                        <div class="subcampo"><a class="btnConfirmarDirbloqueo btnVerde"><span>Confirmar</span></a></div>
                                    </div> 
                                                                             
                                </fieldset>
                             </form>
                            <div class="cuadro-boton">&nbsp;</div>
                        </div>

                        <!-- /formulario direccion -->

                        

                        <!-- formulario email -->

                        <div id="modificarMailCaja" class="box-numero corto modificarMailCaja" style="display:none;">

                            <!-- formulario (paso 1) -->

                            <form class="modificarMail" style="" action="#">
								<jsp:include page="/token.jsp" flush="true"/>
                                <fieldset>

                                    <div class="fila ancho clearfix">

                                        <label>Ingresa nuevo e-mail:*</label>

                                        <div class="campo"><input name="nuevoEmailBloqueo" type="text" class="inputBoxBloqueo nuevoEmailBloqueo" style="width:170px;" /></div>

                                        <div class="campo boton"><a class="btnGuardarMailbloqueo btnVerde"><span>Guardar</span></a></div>

                                    </div>

                                    <div class="fila mensaje corto clearfix">

                                        <div class="campo">Este cambio afectar&aacute; el registro de la direcci&oacute;n de tus datos de usuario.<br />&iquest;Est&aacute;s seguro que deseas modificarlos?</div>

                                        <div class="subcampo"><a class="btnConfirmarMailbloqueo btnVerde"><span>Confirmar</span></a></div>

                                    </div>                                          

                                </fieldset>

                           </form>

                            <div class="cuadro-boton">&nbsp;</div>

                        </div>

                        <!-- /formulario email -->

                        

                        <!-- formulario email -->

                        <div id="modificarMailCaja" class="box-numero corto modificarMailCaja" style="display:none;">

                            <!-- formulario (paso 1) -->

                            <form class="modificarMail" style="" action="#">
								<jsp:include page="/token.jsp" flush="true"/>
                                <fieldset>

                                    <div class="fila ancho clearfix">

                                        <label>Ingresa nuevo e-mail:*</label>

                                        <div class="campo"><input name="nuevoEmailBloqueo" type="text" class="inputBoxBloqueo nuevoEmailBloqueo" style="width:170px;" /></div>

                                        <div class="campo boton"><a class="btnGuardarMailbloqueo btnVerde"><span>Guardar</span></a></div>

                                    </div>

                                    <div class="fila mensaje corto clearfix">

                                        <div class="campo">Este cambio afectar&aacute; el registro de la direcci&oacute;n de tus datos de usuario.<br />&iquest;Est&aacute;s seguro que deseas modificarlos?</div>

                                        <div class="subcampo"><a class="btnConfirmarMailbloqueo btnVerde"><span>Confirmar</span></a></div>

                                    </div>                                          

                                </fieldset>

                            </form>

                            <div class="cuadro-boton">&nbsp;</div>

                        </div>

                        <!-- /formulario email -->

                        

                        <div class="pie-pagina">

                        	<p>Tambi&eacute;n puedes llamar al 800367626, n&uacute;mero gratuito exclusivo para la atenci&oacute;n de bloqueos.</p>

                        </div>

                        

					</div>

					<!-- /Bloqueo paso 1 robo-->

                    

					<!-- Bloqueo paso 2 -->

					<div id="bloqueo_2" class="bloqueo">

                    	<h1>Bloqueo (Motivo)<span>Paso 2 de 3</span></h1>

                        <br />

						<!-- parrafos -->

						<div class="contenido_parrafos">

							<p>Debes identificar el equipo que vas a bloquear, tu &uacute;ltimo equipo registrado es:</p>

						</div>

						<!-- /parrafos -->

                        

                        <!-- escoge movil -->

                        <div class="escoge-movil clearfix">

                        	<div class="caja-ultimo">

                                <div class="ultimo clearfix">

                                    <!-- <div class="nombre"><h:outputText value="#{bloqueoDesbloqueoEquipoController.consultarBloqueoDesbloqueoBean.equipoActual.descripcionEquipo}" /></div>-->
                                    <div class="nombre"></div> 

                                    <div class="radio">

                                        <input type="radio" name="equipo_bloqueo" id="equipo_bloqueo_0" />

                                    </div>

                                </div>

                                <div class="busca-movil clearfix">

                                    <div class="texto">&iquest; El equipo mostrado no corresponde al que deseas bloquear?</div>

                                    <div class="boton">

                                        <a class="btnAzulDelgado btnBuscarEquipos"><span>Buscar m&aacute;s equipos</span></a>

                                    </div>
                                    

                                </div>

                            </div>

                            <!-- listado equipos -->
                            
	                            <div class="cargandoBloqueoListadoEquipos" style="display:none">
	                            	<center><br><img src='../framework/skins/mientel/img/thickbox/TB_cargando.gif'/></b><br><br></center>
	                            </div>
	                            
								<div class="listado-equipos">
				                    
				                    <div class="listaEquipos"></div>
								
								
								</div>
							
							<!-- /listado equipos -->

                            <div class="fieldset">

                                <div class="fila-campo clearfix">

                                    <label class="ancho"><span>&nbsp;</span></label>

                                    <div class="campo"><a style="cursor: pointer;" class="btnCancelarPaso2 cancelar"><span>Cancelar</span></a></div>

                                    <div class="campo"><a class="btnBloqueoPaso2 btnDesactivado" onclick="mxTracker._trackPageview('Personas/Mi Entel/Mis Productos/Telefonia/Equipo/Bloquear/Confirmar');"><span>Continuar</span></a></div>

                                </div>

                            </div>

                        </div>

						<!-- /escoge movil -->

                        

                        <div class="pie-pagina">

                        	<p>Tambi&eacute;n puedes llamar al 800367626, n&uacute;mero gratuito exclusivo para la atenci&oacute;n de bloqueos.</p>

                        </div>						

					</div>

					<!-- /Bloqueo paso 2 -->

		

					<!-- Bloqueo paso 3 -->

					<div id="bloqueo_3" class="bloqueo">

                    	<h1>Bloqueo (Motivo)<span>Paso 3 de 3</span></h1>

                        <br />

                        

						<!-- parrafos -->

						<div class="contenido_parrafos">

							<p><strong>Confirma los datos antes de realizar el bloqueo:</strong></p>

						</div>

						<!-- /parrafos -->

                        

                        <!-- datos de bloqueo -->

                        <div class="datos-bloqueo clearfix">

                        	<div class="fila clearfix">

                                <div class="desc">Motivo</div>

                                <div class="dato datoMotivo">Robo</div>

                            </div>

                            <div class="fila impar clearfix">

                                <div class="desc">Tarjeta SIM a bloquear (Asociada al N&deg;)</div>

                                <div class="dato datoSim"></div>

                            </div>

                            <div class="fila clearfix">

                                <div class="desc">Equipo a bloquear</div>

                                <div class="dato datoEquipo">Modelo</div>

                            </div>

                            <div class="fila impar clearfix">

                                <div class="desc">Serie (IMEI)</div>

                                <div class="dato datoImei">N&uacute;mero</div>

                            </div>

                            <div class="fila clearfix">

                                <div class="desc">Fecha y hora del evento</div>

                                <div class="dato datoFecha"></div>

                            </div>

                            <div class="fila impar clearfix">

                                <div class="desc">Correo electr&oacute;nico</div>

                                <div class="dato datoCorreo"></div>

                            </div>

                            <div class="fila clearfix">

                                <div class="desc">Tel&eacute;fono</div>

                                <div class="dato datoTel"></div>

                            </div>

                            <div class="fila campoDatoClave impar clearfix">

                                <div class="desc">Clave</div>

                                <div class="dato datoClave"></div>

                            </div>

                        </div>

                        <!-- /datos de bloqueo -->

                        

                        <!-- debes saber-->

                        <div class="tabla">

							<div class="header_tabla_blanco clearfix">

								<div class="top"><span></span></div>

								<div class="main_blanco">

									<div class="contenido clearfix">

                                    	<h4><strong>Recuerda que...</strong></h4>

                                        <br />

                                        <ul>

                                        	<li>El bloqueo del equipo no te deja exento del pago del cargo fijo mensual. Tampoco te deja excento del pago de las cuotas pendientes por el equipo en caso de que &eacute;ste se encuentre bajo la modalidad de "arriendo con opci&oacute;n de compra".</li>

                                            <li>Si no recuperas tu tarjeta SIM (chip Entel) y quieres comprar una nueva, puedes adquirirla en cualquier tienda de Entel pagando el costo de &eacute;sta. Presentando tu c&eacute;dula de identidad, podr&aacute;s mantener tu n&uacute;mero y plan. (Si el tr&aacute;mite es realizado por un tercero, deber&aacute; presentar un poder notarial).</li>

                                            <li>Si necesitas comprar un nuevo equipo, lo puedes hacer en cualquier tienda Entel.</li>

                                            <li>Tus cuentas deben estar al d&iacute;a.</li>

                                            <br />

                                        </ul>

                                        <div class="condiciones clearfix">

                                        	<label>Acepto las condiciones</label>

                                            <input type="checkbox" name="condiciones-bloqueo" />

                                        </div>

                                    </div>					

								</div>

								<div class="bottom_blanco_1"><span></span></div>

							</div>

						</div>

						<!-- /debes saber-->

                        

                        <div class="fieldset" style="height: 50px;">

                            <div class="fila-campo clearfix">

                                <label class="ancho"><span>&nbsp;</span></label>

                                <div class="campo"><a style="cursor: pointer;" class="btnCancelarPaso3 cancelar"><span>Cancelar</span></a></div>

                                <div class="campo"><a class="btnBloqueoPaso3 btnDesactivado" onclick="mxTracker._trackPageview('Personas/Mi Entel/Mis Productos/Telefonia/Equipo/Bloquear/Bloqueado');"><span>Bloquear</span></a></div>

                            </div>

                        </div>

                        <div class="pie-pagina">

                        	<p>Tambi&eacute;n puedes llamar al 800367626, n&uacute;mero gratuito exclusivo para la atenci&oacute;n de bloqueos.</p>

                        </div>

					</div>

                    <!-- /Bloqueo paso 3 -->

                    

                    <!-- Bloqueo paso 4 -->
                    
                    <div class="cargandoBloqueoFinal" style="display:none"><center><br><img src='../framework/skins/mientel/img/thickbox/TB_cargando.gif'/></b><br><br></center></div>

					<div id="bloqueo_4" class="bloqueo bloqueo-exito">                        

                        <!-- datos de bloqueo -->

                        <div class="mensaje-exito">Tu equipo ha sido bloqueado exitosamente</div>

                        <div class="datos-bloqueo clearfix">

                        	<div class="fila clearfix">

                                <div class="desc">Motivo</div>

                                <div class="dato datoMotivo">Robo</div>

                            </div>

                            <div class="fila impar clearfix">

                                <div class="desc">Tarjeta SIM a bloquear (Asociada al N&deg;)</div>

                                <div class="dato datoSim"></div>

                            </div>

                            <div class="fila clearfix">

                                <div class="desc">Equipo a bloquear</div>

                                <div class="dato datoEquipo">Modelo</div>

                            </div>

                            <div class="fila impar clearfix">

                                <div class="desc">Serie (IMEI)</div>

                                <div class="dato datoImei">N&uacute;mero</div>

                            </div>

                            <div class="fila clearfix">

                                <div class="desc">Fecha y hora de (motivo)</div>

                                <div class="dato datoFecha"></div>

                            </div>

                            <div class="fila impar clearfix">

                                <div class="desc">Correo electr&oacute;nico</div>

                                <div class="dato datoCorreo"></div>

                            </div>

                            <div class="fila clearfix">

                                <div class="desc">Tel&eacute;fono</div>

                                <div class="dato datoTel"></div>

                            </div>

                            <div class="fila campoDatoClave impar clearfix">

                                <div class="desc">Clave</div>

                                <div class="dato datoClave"></div>

                            </div>

                        </div>

                        <!-- /datos de bloqueo -->                        

                        <div class="pie-pagina">

                        	<p>Tambi&eacute;n puedes llamar al 800367626, n&uacute;mero gratuito exclusivo para la atenci&oacute;n de bloqueos.</p>

                        </div>

					</div>
                    <!-- /Bloqueo paso 4 -->
                    
                    <div class="divDesbloqueoPerdidaExtravio" style="display:none" >
					
						<div id="desbloqueo_0" class="bloqueo" >
							<!-- Desbloqueo por perdida / extravio-->
							<h1>Desbloqueo</h1>
	                        <br />			
	                        <div class="tabla">
								<div class="header_tabla clearfix">
									<div class="top"><span></span></div>
									<div class="main">
										<div class="contenido clearfix">
	                                    	<br /><br />
	                                    	<h4><strong>Actualmente tienes tu equipo y SIM bloqueada por p&eacute;rdida o extrav&iacute;o</strong></h4>
	                                        <br />
	                                        <p>Si posees tu clave de 4 d&iacute;gitos puedes desbloquearlos aqu&iacute;.</p>
	                                        <br />
	                                        <div class="campo-desbloqueo">
												<a id="btnDesbloqueoPaso1" class="btnDesbloqueoPaso1 btnAzul">
													<span>Desbloquear</span>
												</a>
											</div>
	                                        <br />
	                                        <br />
	                                    </div>					
									</div>
									<div class="bottom"><span></span></div>
								</div>
							</div>
	                        <div class="pie-pagina">
	                        	<p>Tambi&eacute;n puedes llamar al 800367626, n&uacute;mero gratuito exclusivo para la atenci&oacute;n de bloqueos.</p>
	                        </div>                    
	                    </div>
                    </div>
 					
 					<!-- desbloqueo paso 1 -->
                    <div id="desbloqueo_1" class="bloqueo">
                    	<h1>Desbloqueo<span>Paso 1 de 2</span></h1>
                        <br />
                        
						<!-- parrafos -->
						<div class="contenido_parrafos">
							<p><strong>Tu equipo y simcard bloqueados son:</strong></p>
						</div>
						<!-- /parrafos -->
                        
                        <!-- datos de bloqueo -->
                        <div class="datos-bloqueo clearfix">
                        	
                        	<div class="fila clearfix">
                                <div class="desc">Motivo de Bloqueo</div>
                                <div class="dato desbloqueoMotivo">Perdida o Extravio</div>
                            </div>
                            <div class="fila impar clearfix">
                                <div class="desc">Tarjeta SIM bloqueada</div>
                                <div class="dato desbloqueoSim"></div>
                            </div>
                            <div class="fila clearfix">
                                <div class="desc">Equipo bloqueado</div>
                                <div class="dato desbloqueoEquipo"></div>
                            </div>
                            <div class="fila impar clearfix">
                                <div class="desc">Serie (IMEI)</div>
                                <div class="dato desbloqueoImei"></div>
                            </div>
                            <div class="fila clearfix">
                                <div class="desc">Fecha y hora de la p&eacute;rdida / extrav&iacute;o</div>
                                <div class="dato desbloqueoFecha"></div>
                            </div>
                            
                            <div class="fila impar">
                            	<form name="desbloqueoEquipoPerdida" class="desbloqueoEquipoPerdida" method="post" action="#">
                            		<jsp:include page="/token.jsp" flush="true"/>
                                    <div class="desc"><strong>Ingresa clave de 4 d&iacute;gitos para desbloquear:</strong></div>
                                    
                                    <div class="dato claveInput" style="width:70px;">
                                    	<input name="claveDesbloqueoPerdida" type="text" class="inputBox claveDesbloqueoPerdida" maxlength="4" style="width:60px; float:left" />
                                    </div>
                                    <div class="dato claveValid"  style="width:70px;" ></div>
                                    
                                    <div class="loading"></div>
                                    <div class="claveInvalida">La clave ingresada es incorrecta.</div>
                                    
                                </form>
                            </div>
                                                        
                        </div>
                        <!-- /datos de bloqueo -->
                        
                        <div class="tabla">
							<div class="header_tabla clearfix">
								<div class="top"><span></span></div>
								<div class="main">
									<div class="contenido clearfix">
                                    	<br /><br />
                                        <p>Si no recuerdas tu clave deber&aacute;s acudir a una de nuestras tiendas con tu c&eacute;dula de identidad para realizar el bloqueo de manera presencial.</p>
                                        <br /><br />
                                    </div>					
								</div>
								<div class="bottom"><span></span></div>
							</div>
						</div>
                        
                        <div class="fieldset">
                            <div class="fila-campo clearfix">
                                <div class="ancho-desbloquear"><span>&nbsp;</span></div>
                                <div class="campo"><a class="btnCancelarDesbloqueoPaso1 cancelar"><span>Cancelar</span></a></div>
                                <div class="campo"><a class="btnDesbloqueoPaso2 btnDesactivado"><span>Desbloquear</span></a></div>
                            </div>
                        </div>
                            
                        <div class="pie-pagina">
                        	<p>Tambi&eacute;n puedes llamar al 800367626, n&uacute;mero gratuito exclusivo para la atenci&oacute;n de bloqueos.</p>
                        </div>
					</div>
                    <!-- /desbloqueo paso 1 -->
                    
                    <!-- desbloqueo paso 2 -->
                    <div id="desbloqueo_2" class="bloqueo">
                    	<h1>Desbloqueo<span>Paso 2 de 2</span></h1>
                        <br />
                        
						<!-- parrafos -->
						<div class="contenido_parrafos">
							<p><strong>Tu equipo y simcard bloqueados son:</strong></p>
						</div>
						<!-- /parrafos -->
                        
                        <!-- datos de bloqueo -->
                        <div class="datos-bloqueo clearfix">
                        	<div class="fila clearfix">
                                <div class="desc">Motivo de Bloqueo</div>
                                <div class="dato desbloqueoMotivo">Perdida o Extravio</div>
                            </div>
                            <div class="fila impar clearfix">
                                <div class="desc">Tarjeta SIM bloqueada</div>
                                <div class="dato desbloqueoSim"></div>
                            </div>
                            <div class="fila  clearfix">
                                <div class="desc">Equipo bloqueado</div>
                                <div class="dato desbloqueoEquipo"></div>
                            </div>
                            <div class="fila impar clearfix">
                                <div class="desc">Serie (IMEI)</div>
                                <div class="dato desbloqueoImei"></div>
                            </div>
                            <div class="fila  clearfix">
                                <div class="desc">Fecha y hora de la p&eacute;rdida / extrav&iacute;o</div>
                                <div class="dato desbloqueoFecha"></div>
                            </div>                            
                        </div>
                        <!-- /datos de bloqueo -->
                        
                        <div class="tabla">
							<div class="header_tabla clearfix">
								<div class="top"><span></span></div>
								<div class="main">
									<div class="contenido clearfix">
                                    	<br /><br />
                                        <p>Tu equipo como tu simcard volver&aacute;n a estar operativos y podr&aacute;n recibir y emitir llamados.</p>
                                        <br /><br />
                                    </div>					
								</div>
								<div class="bottom"><span></span></div>
							</div>
						</div>
                        <div class="fieldset">
                            <div class="fila-campo clearfix">
                                <div class="ancho-desbloquear"><span>&nbsp;</span></div>
                                <div class="campo"><a class="btnCancelarDesbloqueoPaso2 cancelar"><span>Cancelar</span></a></div>
                                <div class="campo"><a class="btnDesbloqueoPaso3 btnAzul"><span>Confirmar</span></a></div>
                            </div>
                        </div>
                            
                        <div class="pie-pagina">
                        	<p>Tambien puedes llamar al 800367626, n&uacute;mero gratuito exclusivo para la atenci&oacute;n de bloqueos</p>
                        </div>
					</div>
                    <!-- /desbloqueo paso 2 -->
                    <div class="cargandoDesbloqueoFinal" style="display:none"><center><br><img src='../framework/skins/mientel/img/thickbox/TB_cargando.gif'/></b><br><br></center></div>
                    <!-- desbloqueo paso 3 -->
                    <div id="desbloqueo_3" class="bloqueo">
                    	<!-- datos de bloqueo -->
                        <div class="bloqueo bloqueo-exito">
                            <div class="mensaje-exito">Tu equipo y simcard han sido desbloqueados exitosamente.</div>
                            <div class="datos-bloqueo clearfix">
                                <div class="fila clearfix">
                                    <div class="desc">N&deg; de reporte</div>
                                    <div class="dato desbloqueoNumero"></div>
                                </div>
                                <div class="fila impar clearfix">
                                    <div class="desc">Tarjeta SIM bloqueada</div>
                                    <div class="dato desbloqueoSim"></div>
                                </div>
                                <div class="fila clearfix">
                                    <div class="desc">Equipo bloqueado</div>
                                    <div class="dato desbloqueoEquipo"></div>
                                </div>
                                <div class="fila impar clearfix">
                                    <div class="desc">Serie (IMEI)</div>
                                    <div class="dato desbloqueoImei"></div>
                                </div>
                                <div class="fila clearfix">
                                    <div class="desc">Fecha y hora de la p&eacute;rdida / extrav&iacute;o</div>
                                    <div class="dato desbloqueoFecha"></div>
                                </div>   
                            </div>
                            <!-- /datos de bloqueo --> 
                        </div>                       
                        <br>
                        <div class="pie-pagina">
                        	<p>Tambi&eacute;n puedes llamar al 800367626, n&uacute;mero gratuito exclusivo para la atenci&oacute;n de bloqueos.</p>
                        </div>                   
                        
					</div>
                    <!-- /desbloqueo paso 3 -->                    								
                    								
							
		
		</div>
		<!-- FIN BLOQUEO - DESBLOQUEO EQUIPOS  -->

		
		<div class="contenido_tab altEquipo contenido3">
			<br /><br />
			<!-- parrafos -->
			<div class="contenido_parrafos">
				<p><cm:getProperty node="${statusSoporte[0]}" name="html"/></p>

			</div>
			<!-- /parrafos -->			
			
			<!-- Tablas -->
			<div id="tablas_presupuesto" style="position:relative">
				<h2 class="sin_icono">Ordenes de trabajo vigentes</h2>
				
				<!-- nuevo enlace PreIngresoOT -->				
				<a class="boton_parrafo servicioTecnicoEnlace" title="<cm:getProperty node="${preingreso_enlace[0]}" name="titulo"/>"
					href="<r:pageUrl pageLabel='${pagePreingresoOt}'></r:pageUrl>">
					<span><cm:getProperty node="${preingreso_enlace[0]}" name="html"/></span>
				</a>				
				<!-- nuevo enlace PreIngresoOT -->
				
				<h:panelGroup rendered="#{equipoController.ordenesLength == 0 }">
					<div class="caja verde clearfix">
						<div class="caja_texto" id="contactoBox">
							<p><strong><cm:getProperty node="${OrdenesDeTrabajo[0]}" name="titulo"/></strong><br /><cm:getProperty node="${OrdenesDeTrabajo[0]}" name="html"/></p>	
						</div>
					</div>
				</h:panelGroup>
				
				<h:panelGroup rendered="#{equipoController.ordenesTrabajoVigentes != null }">
				
				<it:iterator value="#{equipoController.ordenesTrabajoVigentes.ordenes}" rowIndexVar="index" var="object">						
				<!-- Tabla estado pendiente -->
				<div id="tabla_pendiente">
					<!-- Tabla -->

					<!-- 
					<div class="tabla_seccion formulario_borde">
						<div class="tabla_fila_total clearfix">
							<div class="tabla_fila_izquierda">
								<h:outputLink value="informeTecnicoOT.faces" styleClass="thickbox ver_detalle_1 link_lupa">
								Ver informe t&eacute;cnico
								<f:param name="nOrden" value="#{object.nroOrden}" />
								</h:outputLink>
							</div>
						</div>
					</div>
				    -->
					 
					<div class="clearfix">

						<div class="header_tabla clearfix">
							<div class="top"><span></span></div>
							<div class="main">																		
								<table>										
									<tr>
										<th width="70%"><strong>Orden de trabajo N&deg; <h:outputText value="#{object.nroOrden}" /></strong></th>
										<th width="30%" class="ultimo">
										
										<!-- <a href="html/ss_lb_orden_trabajo.html?height=510&amp;width=490" class="thickbox ver_detalle link_lb">  -->
										
										<h:form>
										<h:commandLink action="#{equipoController.obtenerAperturaDocumentoOT}" styleClass="ver_detalle link_lb">
										Ver OT
										<f:param name="nroOrden" value="#{object.nroOrden}" />
										</h:commandLink>
										<!-- </a>  -->
										</h:form>  
																																
										</th>

									</tr>
								</table>					
							</div>
							<div class="bottom"><span></span></div>
						</div>
						<div class="contenido_tabla">
							<table class="tablaFacturacion">
								<tbody>
									<tr>

										<td width="31%">Marca y modelo:</td>
										<td width="69%"><strong><h:outputText value="#{object.marcaModelo}" /></strong></td>																			
									</tr>
									<tr class="impar">
										<td>Fecha ingreso:</td>
										<td><h:outputText value="#{object.fechaIngreso}" /></td>
									</tr>

									<tr>
										<td>Sucursal ingreso:</td>
										<td><h:outputText value="#{object.sucursalIngreso}" /></td>
									</tr>
									<tr class="estado_pendiente">
										<td>Estado:</td>
										<td class="clearfix">

											<div class="estado clearfix">
												<div class="celda_tabla_izquierda">
													<strong><h:outputText value="#{object.estado}" /></strong>
												</div>
												<div class="celda_tabla_derecha">
													<a href="#" class="ver_historial link_lupa mostrar_historial">Ver historial</a>
													<div class="bloque_historial_cerrar">
														<a href="#" class="link_normal mostrar_historial">Cerrar</a>

													</div>
												</div>
											</div>
											<div class="bloque_historial clearfix">
												<it:iterator value="#{object.historial}" rowIndexVar="ind" var="hist">
													<div class="bloque_historial_item"><strong><h:outputText value="#{hist.descEstado}" /></strong><br /><h:outputText value="#{hist.fechaEstado}" /></div>
													<div class="bloque_historial_item bloque_historial_borde"></div>															
												</it:iterator>
												<br><br>
												<br>
											</div>										
										</td>
									</tr>
									<tr>
										<td>Descripci&oacute;n:</td>

										<td><h:outputText value="#{object.descripcion}" /></td>
									</tr>
									<tr class="impar">
										<td>Servicios contratados:</td>
										<td><h:outputText value="#{object.servContratados}" /></td>
									</tr>
									<tr>

										<td>Soluci&oacute;n sucursal:</td>
										<td><h:outputText value="#{object.solSucursal}" /></td>
									</tr>
									<tr class="impar">
										<td>Resoluci&oacute;n de laboratorio:</td>
										<td><h:outputText value="#{object.resLaboratorio}" /></td>

									</tr>
								</tbody>
							</table>						
						</div>
					</div>
					<!-- Tabla -->
					
					<entel:view name="verPresupuesto">
					<h:panelGroup rendered="#{object.presupuesto != null && object.presupuesto.diagnosticos != null}">
						<!-- Boton -->
						<div class="bloque_boton clearfix">
							<div class="tabla_boton">

								<a href="javascript:verPresupuesto('presupuesto<h:outputText value="#{object.nroOrden}" />');" class="btnAzulGrande btnAzulGrandeLargo"><span>Ver Presupuesto</span></a>
							</div>
						</div>
						<!-- /Boton -->
					</h:panelGroup>
					</entel:view>
				</div>
				<!-- /Tabla estado pendiente -->
				
				</it:iterator>
				</h:panelGroup>
				<!-- Tabla estado rechazado -->						
				<!-- /Tabla estado rechazado -->						
				<!-- Tabla estado aceptado -->						
				<!-- /Tabla estado aceptado -->						
				<!-- Tabla estado recepcionado -->						
				<!-- /Tabla estado recepcionado -->				
			</div>

			<!-- /Tablas -->
			
			
			<div id="msgP" style="display:none;">
					<div class="caja verde clearfix">
						<div class="caja_texto" id="contactoBoxP">
							<p><strong>Enviando peticion de presupuesto</strong><br /><img src="../framework/skins/mientel/img/thickbox/TB_cargando.gif"></img></p>
						</div>
					</div>
			</div>	
			
			<it:iterator value="#{equipoController.ordenesTrabajoVigentes.ordenes}" rowIndexVar="index" var="object">
			
			<!-- Presupuesto -->
			<div id="presupuesto<h:outputText value="#{object.nroOrden}" />" style="display:none;">
				<!-- Titulo especial -->
				<div class="titulo_especial clearfix">
					<div class="titulo_texto">Presupuesto</div>
					<div class="titulo_enlace"><a href="#" class="link_normal" onclick="volverOT('presupuesto<h:outputText value="#{object.nroOrden}" />');">Volver a las &oacute;rdenes de trabajo</a></div>

					<div class="titulo_derecha"></div>
				</div>
				<!-- /Titulo especial -->
				
				<p><strong>N&deg; de orden <h:outputText value="#{object.nroOrden}" /></strong></p>
				
				<p><strong><h:outputText value="#{object.marcaModelo}" /></strong></p>
				
				<!-- Mensaje presupuesto -->

				<div id="mensaje_presupuesto_aceptado" class="caja verde clearfix">
					<div class="caja_texto">
						<p><strong><cm:getProperty node="${aceptacionPresupuesto[0]}" name="html" /></strong><br /></p>
					</div>
				</div>
				<div id="mensaje_presupuesto_rechazado" class="caja amarilla clearfix">
					<div class="caja_texto">
						<p><strong><cm:getProperty node="${rechazoPresupuesto[0]}" name="html" /></strong><br /></p>

					</div>
				</div>
				<!-- /Mensaje presupuesto -->
				
				<!-- Tabla presupuesto -->
				<div class="tabla_presupuesto">
					
					<div class="tabla_seccion">
						
						<div class="tabla_fila"><strong>Diagnosticos</strong></div>
						
						<it:iterator value="#{object.presupuesto.diagnosticos}" rowIndexVar="index" var="diags">
							<div class="tabla_fila clearfix">
								<div class="tabla_fila_izquierda"><h:outputText value="#{diags.diagnostico}" /></div>						
							</div>
						</it:iterator>
						
					</div>
					
					<div class="tabla_seccion">
						<div class="tabla_fila"><strong>Reparaciones</strong></div>

						<it:iterator value="#{object.presupuesto.reparaciones}" rowIndexVar="index" var="reps">
							<div class="tabla_fila clearfix">
								<div class="tabla_fila_izquierda"><h:outputText value="#{reps.reparacion}" /></div>
							</div>
						</it:iterator>
														
					</div>
					
					<div class="tabla_seccion formulario_borde">
						<div class="tabla_fila_total clearfix">

							<div class="tabla_fila_izquierda">																	
							
							<h:outputLink value="informeTecnicoOT.faces" styleClass="thickbox ver_detalle_1 link_lupa">
								Ver informe t&eacute;cnico
								<f:param name="nOrden" value="#{object.nroOrden}" />
							</h:outputLink>
							
							</div>
							<div class="tabla_fila_derecha"><span class="tabla_total">Total:</span>
							$<h:outputText value="#{object.presupuesto.valorTotal}" >
								<f:convertNumber currencyCode="CLP" locale="es" />
							</h:outputText>
							</div>
						</div>
					</div>
				</div>
				<!-- /Tabla presupuesto -->

				<!-- Boton -->
				<div id="presupuesto_botones" class="bloque_boton clearfix">
					<div class="tabla_boton clearfix">
						<a href="javascript:aceptarRechazarPresupuesto('<h:outputText value="#{object.nroOrden}" />','<h:outputText value="#{equipoController.atributoAceptacionPrespuesto}" />');" class="btnAzulGrande"><span>Aceptar Presupuesto</span></a>
						<a href="javascript:aceptarRechazarPresupuesto('<h:outputText value="#{object.nroOrden}" />','<h:outputText value="#{equipoController.atributoRechazoPrespuesto}" />');" class="btnAzulGrande"><span>Rechazar Presupuesto</span></a>								
					</div>
				</div>
				<!-- /Boton -->						
				<!-- debes saber-->
				<div class="debes-saber clearfix">
					<div class="img"><!--<img src="../framework/skins/mientel/img/debessaber.gif" alt="Debes saber" />--></div>
					<div class="contenido">
					<cm:getProperty node="${presupuesto[0]}" name="html" />
					</div>
				</div>
				<!-- /debes saber-->

			</div>
			<!-- /Presupuesto -->
			
			</it:iterator>
			
		</div>
		
		
						
		<div class="contenido_tab altEquipo contenido4">
			<br /><br />
			
			<cm:getProperty node="${parrafo[0]}" name="html" />
			
			<div class="contenido_parrafos">						
				<cm:getProperty node="${infoLiberacionEquipo[0]}" name="html" />
			</div>
			
		</div><!-- ACAAAA -->
		
	</div>		

	<h:panelGroup rendered="#{equipoController.datosUsuarioBloqueo.email==''}">
		<script>
			$(".fila_email").hide();
			 $(".fila_actualizar_email").show();
		</script>
	</h:panelGroup>								

</f:view>