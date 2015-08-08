<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="f"  uri="http://java.sun.com/jsf/core"%>
<%@ taglib prefix="h"  uri="http://java.sun.com/jsf/html"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="entel" uri="/WEB-INF/tld/entel-tags.tld" %>
<%@ taglib prefix="it" uri="http://i2b.cl/jsf/iterator/" %>
<%@ taglib uri="http://www.bea.com/servers/portal/tags/content" prefix="cm"%>
<cm:search id="contentDescripcionTiposComercial" query="idContenido = 'idContentDescripcionTiposComercial'" useCache="true"  />
<cm:search id="contentDescripcionTiposTecnico" query="idContenido = 'idContentDescripcionTiposTecnico'" useCache="true"  />
<cm:search id="mensajeConfirmacionExitoCM" query="idContenido = 'idContentMensajeConfirmacionExitoCM'" useCache="true"  />
<cm:search id="mensajeConfirmacion" query="idContenido = 'idContentMensajeConfirmacion'" useCache="true"  />

<f:view beforePhase="#{formularioReclamoController.init}">

<script type="text/javascript">

		var urlIgresarReclamo='<%=request.getContextPath()%>/portlet/formularioreclamo/ingresarReclamoJson.faces';        
		var urlBuscarMontoDocumento = '<%=request.getContextPath()%>/portlet/formularioreclamo/consultarDocumentoReclamadoJson.faces';
		var urlDetalleReclamo = '<%=request.getContextPath()%>/portlet/formularioreclamo/obtenerDetalleReclamoJson.faces';

        var numeroBackup = '<h:outputText value="#{formularioReclamoController.numeroPcs}" />';
        var emailBackup  = '<h:outputText value="#{cuentaController.usuario.email}" />';
        var isPPEMP      = '<h:outputText value="#{formularioReclamoController.PPEMP}" />';
        var relamoComercial  = '<h:outputText value="#{formularioReclamoController.dificultadComercial}" />';
        var relamoTecnico      = '<h:outputText value="#{formularioReclamoController.dificultadTecnica}" />';
        var relamoBoletaFactura      = '<h:outputText value="#{formularioReclamoController.boletaFactura}" />';

        var direccionBackup='';      
        var numeroSMSBachup='';
        var documentoBuscadoBackup='';

        var listaPaginas='';
        var datosBorrados='';
        var swBusqueda='0';
            
	   function selectNumeroReclamo(){
		   var tipo;
		 
		   if($(".reclamoNumero").val()!=undefined){			    
			     caracteristicas = $(".reclamoNumero").val().split('|');			   
			     $("#numeroReclamoDefoult").val(caracteristicas[0]); //Asignar numero de reclamo;
			     tipo = caracteristicas[1];	
		    }else{			   		   
               if(isPPEMP=='true'){            	
            	   tipo='1';
               }else{
            	   tipo='0';
               }
			}
		 
          if(tipo=='0'){              
        	  listTipoComercial = new Array('<h:outputText value="#{formularioReclamoController.tiposReclamoComercial}"/>');
             }else{            	
              listTipoComercial = new Array('<h:outputText value="#{formularioReclamoController.tiposReclamoComercialPPEMP}"/>');
          }          
          $(".reclamoTipoReclamo").html("");
          $(".reclamoTipoReclamo").append('<option value="">Seleccionar</option>');
          $.each(listTipoComercial, function(indexAll, valueAll) {
			   var arrayCodDesc=valueAll;
			   var myStringArray = arrayCodDesc.split('],');
			   $.each(myStringArray, function(index, value) {   
				   var sParameterName = value.split(',');  
				   var value=sParameterName[0].indexOf("=");
				   var desc=sParameterName[1].indexOf("=");	
				   $(".reclamoTipoReclamo").append('<option value='+sParameterName[0].substr(value+1)+'>'+sParameterName[1].substr(desc+1).replace(']]','')+'</option>');
				     				  
			   });
		   });    

          $('.bloqueTipoReclamoComercial').find('.options_div').css('height','auto');
          $('select[name="reclamoTipoReclamo"]').get(0).setValue("");
          $('.nota_tipo_reclamo').hide();
          $('.bloqueNumeroDocumento').hide();

          $(".reclamoMontoCobrosTotal").val('');
          $(".reclamoMontoCobrosTotalHidden").val('');
          $(".reclamoMontoImpugnadoTotal").val('');
          $('.solo_comercial').hide();	
          $('.solo_tecnico').hide();	
          $("#reclamoCobrosDocumentoReclamado").val('');    
          $(".reclamoCobrosMontoDocumento").val(''); 
	   } 
	   

	   function descripcionTipoReclamo(){
		   $('.nota_tipo_reclamo').hide();		   
          if($('select[name="reclamoReclamo"]').val()!=''){
        	  if($('select[name="reclamoReclamo"]').val()==relamoComercial){
        		  if($('select[name="reclamoTipoReclamo"]').val()!=''){
            		 $("#1"+$('select[name="reclamoTipoReclamo"]').val()).show();
            		
            	   }        		
              }else if($('select[name="reclamoReclamo"]').val()==relamoTecnico){
            	  if($('select[name="reclamoTipoReclamoTecnico"]').val()!=''){            		  
           		      $("#2"+$('select[name="reclamoTipoReclamoTecnico"]').val()).show();
            		 
           	      }
              }
          }
	   }	   
	   
		function fillSelects(selectid){ 

			// Vector para saber cuál es el siguiente combo a llenar
			var combos = new Array();
			combos['regionpromocionesTecnico'] = "ciudadpromocionesTecnico"; 
			combos['ciudadpromocionesTecnico'] = "comunapromocionesTecnico"; 
			// Tomo el nombre del combo al que se le a dado el clic por ejemplo: país 
			
			posicion = selectid.indexOf('regionpromocionesTecnico') != -1 ? 'regionpromocionesTecnico' : 'ciudadpromocionesTecnico';
			 
			// Tomo el valor de la opción seleccionada 
			valor = $("select[id*="+posicion+"]").val(); 
		
			  
			// Evaluó que si es país y el valor es 0, vacié los combos de estado y ciudad
			if(posicion == 'regionpromocionesTecnico' && valor == "0"){ 
				  $("select[id*=ciudadpromocionesTecnico]").html(' <option value="0" selected="selected">Seleccionar</option>');
			      $("select[id*=comunapromocionesTecnico]").html(' <option value="0" selected="selected">Seleccionar</option>');
			      $("select[id*=ciudadpromocionesTecnico]")[0].setValue('0');
			      $("select[id*=comunapromocionesTecnico]")[0].setValue('0');
			} else { 
				/* En caso contrario agregado el letreo de cargando a el combo siguiente 
				 Ejemplo: Si seleccione país voy a tener que el siguiente según mi vector combos es: estado por qué combos [país] = estado
				*/ 
				$("select[id*="+combos[posicion]+"]").html('<option value="0">Cargando...</option>');
				$("select[id*="+combos[posicion]+"]")[0].setValue('0');
				$("select[id*=comunapromocionesTecnico]").html('<option value="0" selected="selected">Seleccionar</option>');
				$("select[id*=comunapromocionesTecnico]")[0].setValue('0');
				/* Verificamos si el valor seleccionado es diferente de 0 y si el combo es diferente de ciudad, esto porque no tendría caso hacer la consulta a ciudad porque no existe un combo dependiente de este */
				if(valor != "0") { 
					// Llamamos a pagina de donde ejecuto las consultas para llenar los combos
					var combo = $("select[id*="+posicion+"]").attr("name"); // Nombre del combo 
					var id =  $("select[id*="+posicion+"]").val();
					  
					var reg = $("select[id*=regionpromocionesTecnico]").val();
					  
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
							optionsselect = '<option value="0">Seleccionar</option>';
							for(var i = 0; i< opt.length;i++){
			                	optionsselect += '<option value="'+opt[i].codigo+'">'+opt[i].descripcion+'</option>';
							}
							     
							$("select[id*="+combos[posicion]+"]").html(optionsselect); //Tomo el resultado de pagina e inserto los datos en el combo indicado
							$("select[id*="+combos[posicion]+"]").val('0');
						}
					});
				}
			}
		}
		
		
		function setAreas(){
			$("input[id*=hcpromocionesTecnico]").val($("select[id*=ciudadpromocionesTecnico]").val());
			$("input[id*=hcompromocionesTecnico]").val($("select[id*=comunapromocionesTecnico]").val());
		}
		
		
		
		
		//LISTA LOCALIZACION PROBLEMA		     	
			
	   	    function fillSelectsLocalizacion(selectid){ 
	   	    
					// Vector para saber cuál es el siguiente combo a llenar
					var combos = new Array();
					combos['regionpromocionesLocalizacion'] = "ciudadLocalizacionProblema"; 
					combos['ciudadLocalizacionProblema'] = "comunaLocalizacionProblema"; 
					// Tomo el nombre del combo al que se le a dado el clic por ejemplo: país 
					
					posicion = selectid.indexOf('regionpromocionesLocalizacion') != -1 ? 'regionpromocionesLocalizacion' : 'ciudadLocalizacionProblema';
					 
					// Tomo el valor de la opción seleccionada 
					valor = $("select[id*="+posicion+"]").val(); 
				
					  
					// Evaluó que si es país y el valor es 0, vacié los combos de estado y ciudad
					if(posicion == 'regionpromocionesLocalizacion' && valor == "0"){ 
						  $("select[id*=ciudadLocalizacionProblema]").html(' <option value="0" selected="selected">Seleccionar</option>');
					      $("select[id*=comunaLocalizacionProblema]").html(' <option value="0" selected="selected">Seleccionar</option>');
					      $("select[id*=ciudadLocalizacionProblema]").val('0');
					      $("select[id*=comunaLocalizacionProblema]").val('0');
					} else { 
						/* En caso contrario agregado el letreo de cargando a el combo siguiente 
						 Ejemplo: Si seleccione país voy a tener que el siguiente según mi vector combos es: estado por qué combos [país] = estado
						*/ 
						$("select[id*="+combos[posicion]+"]").html('<option value="0">Cargando...</option>');
						$("select[id*="+combos[posicion]+"]").val('0');
						$("select[id*=comunaLocalizacionProblema]").html('<option value="0" selected="selected">Seleccionar</option>');
						$("select[id*=comunaLocalizacionProblema]").val('0');
						/* Verificamos si el valor seleccionado es diferente de 0 y si el combo es diferente de ciudad, esto porque no tendría caso hacer la consulta a ciudad porque no existe un combo dependiente de este */
						if(valor != "0") { 
							// Llamamos a pagina de donde ejecuto las consultas para llenar los combos
							var combo = $("select[id*="+posicion+"]").attr("name"); // Nombre del combo 
							var id =  $("select[id*="+posicion+"]").val();
							  
							var reg = $("select[id*=regionpromocionesLocalizacion]").val();
							  
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
									 if(selectid =="regionpromocionesLocalizacion" ){
                                         optionsselect = '<option value="0">Ciudad</option>';
                                    }else if(selectid =="ciudadLocalizacionProblema"){
                                         optionsselect = '<option value="0">Comuna</option>';
                                    }
	                                    
									for(var i = 0; i< opt.length;i++){
					                	optionsselect += '<option value="'+opt[i].codigo+'">'+opt[i].descripcion+'</option>';
									}
									     
									$("select[id*="+combos[posicion]+"]").html(optionsselect); //Tomo el resultado de pagina e inserto los datos en el combo indicado
									$("select[id*="+combos[posicion]+"]").val('0');
								}
							});
						}
					}
				}	   	
	   	
	   	//FIN LISTA LOCALIZACION PROBLEMA 
		
		
		
		$(document).ready(function(){		
		
			$('.btnValidarDirecion_Reclamo').click(function(e){
				if($('.reclamoTecnicoRegion').val() < 1){
					$('#globoError').find('td:first').html("Seleccione una regi&oacute;n.");
					mostrarError($('.reclamoTecnicoRegion'),'0');
					return false;
				}else if ($('.reclamoTecnicoCiudad').val() < 1){
					$('#globoError').find('td:first').html("Seleccione una ciudad.");
					mostrarError($('.reclamoTecnicoCiudad'),'0');
					return false;
				}else if ($('.reclamoTecnicoComuna').val() < 1){
					$('#globoError').find('td:first').html("Seleccione una comuna");
					mostrarError($('.reclamoTecnicoComuna'),'0');
					return false;
				}else if ($('.reclamoTecnicoCalle').val() == ""){
					$('#globoError').find('td:first').html("Escriba la calle");
					mostrarError($('.reclamoTecnicoCalle'),'0');
					return false;
				}else if ($('.reclamoTecnicoNumero').val() == ""){
					$('#globoError').find('td:first').html("Escriba el n&uacute;mero");
					mostrarError($('.reclamoTecnicoNumero'),'0');
					return false;				
				}

				$(".regionSelect").hide();
				$(".regionText").html($('.region-promociones option:selected').text());
				$(".regionText").show();
				
				$(".ciudadSelect").hide();
				$(".ciudadText").html($('.reclamoTecnicoCiudad option:selected').text());
				$(".ciudadText").show();
				
				$(".comunaSelect").hide();
				$(".comunaText").html($('.reclamoTecnicoComuna option:selected').text());
				$(".comunaText").show();
				
				$(".calleInput").hide();
				$(".calleText").html($('.calle').val());
				$(".calleText").show();
				
				$(".numeroInput").hide();
				$(".numeroText").html($('.numero').val());
				$(".numeroText").show();
				
				$(".deptoInput").hide();
				$(".dptoText").html($('.depto_casa_otro').val());
				$(".dptoText").show();		
				
				
				$(this).hide();
		    	//$(this).parents('.fila').next('.mensaje').show();
				
				$('.modificarDirBloqueo').removeClass('abierto');
				$('.modificarDirBloqueo').parent().removeClass('cancelar');
				$('.modificarDirBloqueo').parent().parent().removeClass('sinPadding');
				$('#modificarDirCaja').hide();
				$('.modificarDirBloqueo').html('Modificar');
				$('.btnGuardarDirbloqueo').show();
				$('.btnGuardarDirbloqueo').parents('.fila').next('.mensaje').hide();
				
				var dptoExist = $('.depto_casa_otro').val();
				
				
				if(dptoExist == null || dptoExist == ""){
					
					$('.reclamoTecnicoDireccion').val($('.calle').val()+', '+$('.numero').val()+', '+$('.reclamoTecnicoComuna option:selected').text()+', '+$('.reclamoTecnicoCiudad option:selected').text()+', '+$('.region-promociones option:selected').text());
				}else{
					
					$('.reclamoTecnicoDireccion').val($('.calle').val()+', '+$('.numero').val()+', Depto/Oficina '+$('.depto_casa_otro').val()+', '+$('.reclamoTecnicoComuna option:selected').text()+', '+$('.reclamoTecnicoCiudad  option:selected').text()+', '+$('.region-promociones option:selected').text());
				}
			});
			
			$('.inputReclamo').inputBox();
			$('.selectReclamo').selectBox();
			
			//$('select[name="reclamoReclamo"]').get(0).setValue(2);
			
			$('select[name="reclamoReclamo"]').change(function() {				
				$('#globoError').fadeOut();		
				var element = $(this);
				var value = (element.val() != "") ? element.val() : false;
								
				if(value){
					if(value == relamoComercial){
						$('select[name="reclamoTipoReclamo"]').get(0).setValue("");				
						$('.bloqueTipoReclamoComercial').show();
						$('.bloqueTipoReclamoTecnico').hide();				
						$('.bloqueTecnicoUbicacion').hide();
						$('.bloqueTipoReclamoDefault').hide();
						
					}else if(value == relamoTecnico){
							$('select[name="reclamoTipoReclamoTecnico"]').get(0).setValue("");					
							$('.bloqueTipoReclamoComercial').hide();
							$('.bloqueTipoReclamoTecnico').show();
							$('.bloqueTecnicoUbicacion').show();
							$('.bloqueTipoReclamoDefault').hide();							
					}
				}else{
					$('select[name="reclamoTipoReclamo"]').get(0).setValue("");	
					$('select[name="reclamoTipoReclamoTecnico"]').get(0).setValue("");
					$('.bloqueTipoReclamoComercial').hide();
					$('.bloqueTipoReclamoTecnico').hide();				
					$('.bloqueTecnicoUbicacion').hide();
					$('.bloqueTipoReclamoDefault').show();
				}
				 $('.nota_tipo_reclamo').hide();
			});


			$('.modificarDirBloqueo').click(function(e){
				$('.btnValidarDirecion_Reclamo').show();
			});

			$('.btnGuardarMailReclamo').click(function(e){
				if ($(".modificarMail").valid()){
					$('.reclamoCobrosNotificacionEmail').val($('.nuevoEmailBloqueo').val()); 
					$(this).hide();
		        	$(this).parents('.fila').next('.mensaje').show();
		        	$('.nuevoEmailBloqueo').attr('readonly','readonly');
		        	$('.modificarMailBloqueo').removeClass('abierto');
					$('.modificarMailBloqueo').parent().removeClass('cancelar');
					$('.modificarMailBloqueo').parent().parent().removeClass('sinPadding');
					$('.modificarMailBloqueo').html('Modificar');
					$('#modificarMailCaja').hide();										
				}
		    });	


			$('.modificarMailBloqueo').click(function(e){
				$('.btnGuardarMailReclamo').show();	
				//cerramos del numero para el envio del SMS
				if ($('.modificarSMSBloqueo').hasClass('abierto')){
					$('.modificarSMSBloqueo').removeClass('abierto');
					$('.modificarSMSBloqueo').parent().removeClass('cancelar');
					$('.modificarSMSBloqueo').parent().parent().removeClass('sinPadding');
					$('#modificarSMSCaja').hide();
					$('.modificarSMSBloqueo').html('Modificar');
					$('.btnGuardarDirbloqueo').show();
					$('.btnGuardarDirbloqueo').parents('.fila').next('.mensaje').hide();
					return false;
				}			
		    });	

			var urlIgresarReclamo='<%=request.getContextPath()%>/portlet/formularioreclamo/ingresarReclamoJson.faces';

           /*Se cargar los select de region,ciudad,comuna*/

		       /*var param = obtenerParametroURL("contexto"); 
				if (param == 'servicioTecnico') {
					$('.servicio_tecnico').trigger("click");
				}*/
		
				var aux;
				aux =  $(".reclamoTecnicoRegion").val();	
				if(aux!=null){					
					$(".reclamoTecnicoRegion").val('<h:outputText value="#{cuentaController.usuario.direccionContacto.region.codigo}"/>');
			    }	

				var aux='<h:outputText value="#{cuentaController.usuario.direccionContacto.region.codigo}"/>';
                if(aux==''){  
                	$(".reclamoTecnicoRegion").append('<option value="">Seleccionar</option>');                  
                	$(".reclamoTecnicoRegion")[0].setValue('');
                }		    
		
				aux =  $("select[id*=ciudadpromociones]").val();	
				if(aux!=null){										
					$("select[id*=ciudadpromociones]").val('<h:outputText value="#{cuentaController.usuario.direccionContacto.ciudad.codigo}"/>');
			    }

				aux='<h:outputText value="#{cuentaController.usuario.direccionContacto.ciudad.codigo}"/>';
				if(aux==''){
					$("select[id*=ciudadpromociones]").append('<option value="">Seleccionar</option>');                  
                	$("select[id*=ciudadpromociones]")[0].setValue('');
				 }
		
				aux =  $("select[id*=comunapromociones]").val();	
			    if(aux!=null){
			    	$("select[id*=comunapromociones]").val('<h:outputText value="#{cuentaController.usuario.direccionContacto.comuna.codigo}"/>');
			    }	

			    aux='<h:outputText value="#{cuentaController.usuario.direccionContacto.comuna.codigo}"/>';
				if(aux==''){
					$("select[id*=comunapromociones]").append('<option value="">Seleccionar</option>');                  
                	$("select[id*=comunapromociones]")[0].setValue('');
				 }		

				$("select[id*=regionpromocionesLocalizacion]").append('<option value="">Regi&oacute;n</option>'); 
				$("select[id*=regionpromocionesLocalizacion]")[0].setValue('');	 
                
				 
				$('.reclamoTecnicoDireccion').val($('.calle').val());
				direccionBackup=$('.reclamoTecnicoDireccion').val();
				numeroSMSBackup=$('.reclamoCobrosNotificacionSMS').val();

			
			$('#formularioImpresion').submit(function() {
	        	Xpos=(screen.width/2)- 400;
	        	Ypos=(screen.height/2)- 300;	        	
	            window.open('', 'formpopup', 'width=605,height=550,menubar=yes,resizeable=yes,scrollbars=yes,toolbar=no,left = '+Xpos+',top = '+Ypos);
	            this.target = 'formpopup';
	        });

			$('#imp_rut').val('<h:outputText value="#{formularioReclamoController.rutUsuario}" converter="rutConverter" />');
			$('#imp_nombre').val('<h:outputText value="#{formularioReclamoController.nombreUsuario}" />');   

				        
	        $('.botonImpresion').click(function() {
	    		$('#formularioImpresion').submit();
	    	});


	        (function($) {
	    	    $.fn.extend( {
	    	        limiter: function(limit, elem) {
	    	            $(this).live("keyup blur", function() {
	    	                setCount(this, elem);
	    	            });
	    	            function setCount(src, elem) {
	    	                var chars = src.value.length;
	    	                if (chars > limit) {
	    	                    src.value = src.value.substr(0, limit);
	    	                    chars = limit;
	    	                }
	    	                elem.html( limit - chars );
	    	            }
	    	            setCount($(this)[0], elem);
	    	        }
	    	    });
	    	})(jQuery);
	    	
	    	var elem = $("#chars");
	    	$(".reclamoCobrosAntecedentes").limiter(800, elem);	    

	    $('.borrarFormulario').click(function(e){	
		     
	    	 $('.bloqueTipoReclamoTecnico').hide();	    	 
	   		 $('.bloqueTipoReclamoComercial').hide();
	   		 $('.reclamoCobrosAntecedentes').val('');
	   		 $('.reclamoCobrosNotificacionSMS').val('');
	   		 $('.reclamoCobrosNotificacionEmail').val(emailBackup);
	   		 $('.reclamoTecnicoDireccion').val(direccionBackup);	
	   		 $('.reclamoCobrosNotificacionSMS').val(numeroSMSBackup);		
	   		 $('input[name="checkReclamoCobrosNotificacionEmail"]').removeAttr("checked");
	   		 $('input[name="checkreclamoCobrosNotificacionSMS"]').removeAttr("checked");
	   		 $('input[name="checkReclamoCobrosNotificacionCarta"]').removeAttr("checked");
	   		 //$('select[name="reclamoReclamo"]').get(0).setValue(2);
	   		 $(".numeroReclamoDefoult").val(direccionBackup);
	   		 $(".numeroReclamoDefoult").val('');
	   		 $(".reclamoCobrosMontoDocumento").val('');		 
	   		 $('.bloqueNumeroDocumento').hide();
	   		 $('.bloqueTipoReclamoDefault').show();
	   		 //$(".reclamoReclamo").html('');
	         //$(".reclamoReclamo").append('<option value="">Seleccionar</option>')
	         //.append('<option value="1">Comercial</option>')
	         //.append('<option value="2">Técnico</option>');
	   		 $('select[name="reclamoReclamo"]').get(0).setValue('');
	   		 $('select[name="reclamoTipoReclamo"]').get(0).setValue('');
	   		 $('select[name="reclamoTipoReclamoTecnico"]').get(0).setValue('');

	   		$(".reclamoMontoCobrosTotal").val('');
            $(".reclamoMontoCobrosTotalHidden").val('');
            $(".reclamoMontoImpugnadoTotal").val('');
            $('.solo_comercial').hide();	
            $('.solo_tecnico').hide();	
            $("#reclamoCobrosDocumentoReclamado").val('');    
            $(".reclamoCobrosMontoDocumento").val('');             

	        if(datosBorrados==''){  
	            $(".comunaLocalizacionProblema").append('<option value="">Comuna</option>');
	         }	
 
	         $("select[id*=regionpromocionesLocalizacion]")[0].setValue('');	
	         $(".ciudadLocalizacionProblema").append('<option value="">Ciudad</option>');
	    	 $(".ciudadLocalizacionProblema")[0].setValue('');
	    	 $(".comunaLocalizacionProblema")[0].setValue(''); 
	    	 $(".comunaLocalizacionProblema")[0].setValue('0');
	    	 $(".calleLocalizacionProblema").val('Direcci\u00F3n y n\u00FAmero');

	   		if($(".nombreUsuario").val()!=undefined){
	   			$(".nombreUsuario").val('');
			}

	   		datosBorrados='1';
	         	        		
	   	   });
	   	   

	    $("#numeroReclamoDefoult").val('<h:outputText value="#{formularioReclamoController.numeroPcs}" />'); 
	    
	    //LLamado inicio de carga del historial de reclamos.
	       loadTableHistorialReclamos();

	       $(".calleLocalizacionProblema").val('Direcci\u00F3n y n\u00FAmero');

	       $( ".calleLocalizacionProblema" ).focus(function() {
	    	    if($(".calleLocalizacionProblema").val()=='Direcci\u00F3n y n\u00FAmero'){
	    	    	$(".calleLocalizacionProblema").val('');
		    	}
	    	});

	       $( ".calleLocalizacionProblema" ).blur(function() {
	    	    if($(".calleLocalizacionProblema").val()==''){
	    	    	$(".calleLocalizacionProblema").val('Direcci\u00F3n y n\u00FAmero');
		    	}
	       });

	    
	});
		
</script>

<script type="text/javascript">
function showModificarDireccion(el) {
	
	var $input = $(el).parents('div:first');
	var punto = $input.offset();
	var $globo = $('#globoErrorBloqueo');
	
	if ($globo.is(':hidden')) {
		$globo.fadeIn(200, function() {
			$(el).focus();
		});
	}
	
	$globo.css({
		'top': $input.offset().top + 13,
		'left': $input.offset().left + parseInt($input.width()) + 5 
	});

	$(window).resize(function() {
		$globo.css({
			'top': $input.offset().top + 13,
			'left': $input.offset().left + parseInt($input.width()) + 5 
		});
	});
	return false;
}

// BLOQUE HISTORIAL RECLAMOS________
  
  var htmlMensajeHistorial = "<div id='mensaje-historial' class='mensaje-alerta-sistema-pequeno' style='width:545px;margin:5px 2px'>"+
									        "<div class='clearfix sub-contenedor'>"+
									        "<div class='contenedor-imagen'>"+
									        	"<div class='imagen'></div>"+
									        "</div>"+
									        "<div class='texto'></div>"+
									    "</div>"+
							  "</div>";


   var htmlLoading        = "<tr id='tr-loading-historial-reclamos'>"+
	                         "<td id='td-cargando' colspan='6' style='text-align:center'><br><img src='../framework/skins/mientel/img/thickbox/TB_cargando.gif'/></b><br><br></td>"+
	                        "</tr>";
	              
    var htmlPaginador     = '<div id="paginador-historial-reclamos" style="margin: auto; margin-top:20px;width: 330px;"></div>';	              
  
    var htmlFilaHistorial = '<tr class="tr-historial-reclamos nueva-fila" style="font-size:12px;">'+
							'<td class="fecha-registro" width="80px" ></td>'+
							'<td class="motivo" width="90px"></td>'+
							'<td class="numero-de-reclamo" width="90px"></td>'+
							'<td class="estado" width="100px"></td>'+
							'<td class="fecha-de-respuesta" width="90px"></td>'+
							'<td class="ver" width="80px" align="center"><a  class="ver-detalle-historial-reclamo" onClick=\"javascript:verDetalleReclamo();\">Ver detalle</a></td>'+
						'</tr>'; 
    var pagTotal;
    
	function paginarHistorial(){
	    
		var numPagina = 1;		
		$("#paginador-historial-reclamos").paginate({
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
				cambiarPaginaHistorialReclamos(currval-1,'all');
				desactivarBotones(currval);	
			}
		});
		
		$('.jPag-control-back, .jPag-control-front').css('opacity',0).css('cursor','default');
	
				
			if(pagTotal < 5){
				var ancho = (pagTotal * 30);	
				$('.ulwrapdiv').css('width',ancho+'px');				
			}else{
				ancho=150;				
			}			
				
			$('.jPag-backk').click(function(){
				$('.jPag-current').parent().prev().children('a').trigger('click');
			});

			$('.jPag-nextt').click(function(){
				$('.jPag-current').parent().next().children('a').trigger('click');
			});

			ancho2 = ancho+180;
			setTimeout('anchopaginador(ancho2);',1000);
			desactivarBotones(1);
	};
	
	
	  function cambiarPaginaHistorialReclamos(current,option){	
		   	     	
	     	if((option=='all' || $('#mssisdn').val()=='') && listaPaginas.length>0){	

	     		   if($('#mssisdn').val()!='' && swBusqueda=='1'){
	       			   $('#mssisdn').val("");
	       			    swBusqueda='0';
	       		   }      	        
	     	       
					$('.tr-historial-reclamos').remove();
					$('#table-historial-reclamos tbody').append(htmlLoading);
					var listaDetalleCurrent = listaPaginas[current].listaDetalle;
					
					for(index in listaDetalleCurrent){
						var detalle = listaDetalleCurrent[index];
						$('#table-historial-reclamos tbody').append(htmlFilaHistorial);
						var nuevaFila = $('#table-historial-reclamos tbody .tr-historial-reclamos:last');
						if(index%2 == 0){
							nuevaFila.addClass('impar');
						}
						else{
							nuevaFila.addClass('par');
						}																				
	                        var nuevaFila = $('#table-historial-reclamos tbody .tr-historial-reclamos:last');								
	                        if(detalle.estado.toUpperCase()!="SOLUCIONADO"){
							    nuevaFila.addClass('pendiente');
							    nuevaFila.find('.estado').html("<span class='icon-pendiente'><strong>"+detalle.estado+"</strong></span>");
							}else{
							   nuevaFila.find('.estado').html(detalle.estado);
							   
							}																										
							nuevaFila.find('.fecha-registro').html(detalle.fechaDeIngresoTest);
							nuevaFila.find('.motivo').html(detalle.motivo);
							nuevaFila.find('.numero-de-reclamo').html(detalle.numeroReclamo);										
							nuevaFila.find('.fecha-de-respuesta').html(detalle.fechaDeRespuestaTest);
							nuevaFila.find('.ver').html("<a class='ver-detalle-historial-reclamo' onClick=\'javascript:verDetalleReclamo("+detalle.numeroReclamo+");\'>Ver detalle</a>");
					}	

									
						
			}else if (option=='search' && listaPaginas.length>0){

			  swBusqueda='1';
				 
              if($('#mssisdn').val()!=''){            	              	
            	 
 			      $('.tr-historial-reclamos').remove();
				  $('#table-historial-reclamos tbody').append(htmlLoading);					
					
					var sw = true;					
					var l=0;	
					while (sw && l <listaPaginas.length){
					
					    var listaDetalleCurrent = listaPaginas[l].listaDetalle;
					    var i=0;
					    
						while(sw && i < listaDetalleCurrent.length){
					
							var detalle = listaDetalleCurrent[i];
							
						    if(detalle.numeroReclamo==$('#mssisdn').val()){
							  
							  $('#table-historial-reclamos tbody').append(htmlFilaHistorial);
							        var nuevaFila = $('#table-historial-reclamos tbody .tr-historial-reclamos:last');
									
							        if(detalle.estado.toUpperCase()!="SOLUCIONADO"){
									    nuevaFila.addClass('pendiente');
									    nuevaFila.find('.estado').html("<span class='icon-pendiente'><strong>"+detalle.estado+"</strong></span>");
									}else{
									   nuevaFila.find('.estado').html(detalle.estado);
									   
									}																										
									nuevaFila.find('.fecha-registro').html(detalle.fechaDeIngresoTest);
									nuevaFila.find('.motivo').html(detalle.motivo);
									nuevaFila.find('.numero-de-reclamo').html(detalle.numeroReclamo);										
									nuevaFila.find('.fecha-de-respuesta').html(detalle.fechaDeRespuestaTest);
									nuevaFila.find('.ver').html("<a class='ver-detalle-historial-reclamo' onClick=\'javascript:verDetalleReclamo("+detalle.numeroReclamo+");\'>Ver detalle</a>");
									sw=false;
							}							
							i++;
						}
						l++;
					}
					$('#div-footer-tabla-historial').append(htmlPaginador);
			      }else{
			    	   $('#globoError').find('td:first').html("Ingresa el n&uacute;mero del reclamo.");
						mostrarError($('#mssisdn'),'1');  
				  } 	
					
					
			        ///paginarHistorial();
			}	
			
			 $('#tr-loading-historial-reclamos').remove();				 		
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
	
	
	function loadTableHistorialReclamos(){
    	
		var url='<%=request.getContextPath()%>/portlet/formularioreclamo/obtenerHistorialReclamosJson.faces';		
		
		$('.tr-historial-reclamos, #tr-total-puntos, #paginador-historial-reclamos, #mensaje-historial').remove();		    
	    $('#table-historial-reclamos tbody').append(htmlLoading);	
		     
			 $.ajax({
		            type: 'POST',
		            url: url,
		            dataType: 'json',
		            //data: {numPagina:numPagina},
		            success: function (resp){		            		
				            if(resp.estado == 'Ok'){ 
				            	if(resp.respuesta.length !=0 ){				            	
									listaPaginas = resp.respuesta;
									pagTotal = listaPaginas.length;
									var listaDetallePrimera = listaPaginas[0].listaDetalle;																			
									for(index in listaDetallePrimera){
										var detalle = listaDetallePrimera[index];
										$('#table-historial-reclamos tbody').append(htmlFilaHistorial);										
										var nuevaFila = $('#table-historial-reclamos tbody .tr-historial-reclamos:last');
										
										if(index%2 == 0){
											nuevaFila.addClass('impar');
										}else{
											nuevaFila.addClass('par');
										}			
										    
										if(detalle.estado.toUpperCase()!="SOLUCIONADO"){
										    nuevaFila.addClass('pendiente');
										    nuevaFila.find('.estado').html("<span class='icon-pendiente'><strong>"+detalle.estado+"</strong></span>");
										}else{
										   nuevaFila.find('.estado').html(detalle.estado);
										   
										}																										
										nuevaFila.find('.fecha-registro').html(detalle.fechaDeIngresoTest);
										nuevaFila.find('.motivo').html(detalle.motivo);
										nuevaFila.find('.numero-de-reclamo').html(detalle.numeroReclamo);										
										nuevaFila.find('.fecha-de-respuesta').html(detalle.fechaDeRespuestaTest);
										nuevaFila.find('.ver').html("<a class='ver-detalle-historial-reclamo' onClick=\'javascript:verDetalleReclamo("+detalle.numeroReclamo+");\'>Ver detalle</a>");
								  }
									
									$('#tr-loading-historial-reclamos').remove();
									$('#div-footer-tabla-historial').append(htmlPaginador);
									paginarHistorial();
									
									
						        }
				            	else{
				            		$('#div-footer-tabla-historial').append(htmlMensajeHistorial);
				            		$('#tr-loading-historial-reclamos').remove();			            		
					            	$('#mensaje-historial .texto').html('No hay Informaci&oacute;n Hist&oacute;rica Disponible.');
					            }

				            	reclamoGenerado='0';
					        }		            
				            else{
				            	$('#div-footer-tabla-historial').append(htmlMensajeHistorial);
				            	$('#tr-loading-historial-reclamos').remove();
				            	$('#mensaje-historial .texto').html(resp.detalle);
					        }	
					       
		           } 
	        });

	}

	function moverTableTraficoEnLinea(element){
		
		if($(element).hasClass('desactivado')){
			return false;
		}
			
		if($(element).hasClass('back')){
			loadTableHistorialReclamos(lastIndex-1);
		}else{
			loadTableHistorialReclamos(lastIndex+1);		
		}
	}
	
  
	
	$(document).ready(function() {
		
		$(".btnDetalleReclamos").fancybox({		
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

		  /*$("#fancy_overlay").css( {	
				width: "1400px"
		   });

		  $("#TB_ajaxContent").css( {
				width:"705px",
				height: "685px"		
		    });    */  
		   
    });
    
    function imprimir(){		  
		 $('#imprimir_detalle').printElement(); //imprimir dierectamente Element		
	}
	
	// BUSQUEDA DOCUMENTO
	
  function formato_numero(numero, decimales, separador_decimal, separador_miles){ // v2007-08-06
	                numero=parseFloat(numero);
	                if(isNaN(numero)){
	                    return "";
	                }

	                if(decimales!==undefined){
	                    // Redondeamos
	                    numero=numero.toFixed(decimales);
	                }

	                // Convertimos el punto en separador_decimal
	                numero=numero.toString().replace(".", separador_decimal!==undefined ? separador_decimal : ",");

	                if(separador_miles){
	                    // Añadimos los separadores de miles
	                    var miles=new RegExp("(-?[0-9]+)([0-9]{3})");
	                    while(miles.test(numero)) {
	                        numero=numero.replace(miles, "$1" + separador_miles + "$2");
	                    }
	                }

	                var parts = numero.split(",");
	                if(parts.length >= 1){
	                	numero=parts[0];
	                }

	                return numero;
	            } 

  /*function comma(num){
	    while (/(\d+)(\d{3})/.test(num.toString())){
	        num = num.toString().replace(/(\d+)(\d{3})/, '$1'+','+'$2');
	    }
	    return val;
	} */
    
</script>


<c:if test="${profile.aaa == miEntelBean.AAATitular}">
	 <script type="text/javascript">
	 $(document).ready(function(){
		 var tipo = isPPEMP?'0':'1';		 
		 $('.reclamoNumero')[0].setValue('<h:outputText value="#{formularioReclamoController.numeroPcs}"/>'+'|'+tipo);		 
		 
	  });
	 </script>
</c:if>

<!-- JSP MANEJO DE LOS MENSAJE DE ERROR-->
	<!-- CENTRO -->	
 	      <h1>Ingreso de Reclamo</h1>
 	      
			<div class="linea_tabs clearfix">
				<div class="tab contenido1 seleccionado" style="display: block;">
					<span class="contratar_bolsas">
						Histórico Reclamos
					</span>
				</div>
				<div class="tab contenido2" style="display: block;">
					<span class="bolsas_contratadas">
						Ingresos Reclamos
					</span>
				</div>               
			</div>	
			 	      
	 	   <div id="contenido_tabs">
	 	   
	 	      <div class="contenido_tab contenido1" style="display:block;"> 
	 	            <br/>
	 	            <br/>		
				    <p>A continuación podr&aacute;s revisar todos los reclamos ingresados durante los &uacute;ltimos 12 meses.</p>
				    
				    <div class="tabla-contenedor tabla-contenedor-historial-reclamos" style="padding:10px 0 0;">
                       <div class="tabla-arriba"></div>
                           <div class="tabla-centro clearfix">
                              <div class="texto"><strong>Ingresa tu número de reclamo:</strong></div>                                  
                                  <div class="campo mostrar_globo">
                                  	<input name="mssisdn" id="mssisdn" type="text"  class="inputBox" style="width:180px;" maxlength="8" onkeypress="return soloNumeros(event);" />

                                  </div>
                                  <div class="boton">
                                      <a class="btnAzulLargo" onclick="cambiarPaginaHistorialReclamos('0', 'search');" >
                                          <span>Buscar</span>
                                      </a>
                                  </div>                                        
                           </div>                                    
                           <div class="tabla-abajo"></div>
                   </div>
				  
				        <table class="tabla-azul tabla-azul-historial-reclamos" id="table-historial-reclamos">	
				          <thead>	
	                             <tr class="cabecera">
	                                 <th width="80px">Fecha de ingreso</th>
	                                 <th width="90px">Motivo</th>
	                                 <th width="90px">N° de reclamo</th>
	                                 <th width="100px">Estado</th>
	                                 <th width="90px">Fecha de respuesta</th>
	                                 <th width="80px">&nbsp;</th>
	                             </tr>
	                      </thead>       
	                      <tbody class="alterada">
																
						  </tbody>	
						                                  
					  </table>	
					  
					  <div id="div-footer-tabla-historial"></div>				    
			  </div>
			   
	 	      <div class="contenido_tab contenido2" style="display:none;"> 
	 	          <br/>	     
	              <div class="bloqueReclamo clearfix">						
							<div class="bloqueReclamoSuperior">
								
								<div class="clearfix">
									<p class="nota">* Campos obligatorios</p>
								</div>								
								<!-- FILA -->								 
								<h:panelGroup rendered="#{formularioReclamoController.nombreVacio}">								    							 
											<div class="reclamoFila clearfix">
												<label style="padding-top:11px;">Nombre:</label>
												<div class="bloqueCampos ">
												   <div>									  											
													   <input name="nombreUsuario" type="text" class="inputReclamo nombreUsuario" style="width:307px;" />
												   </div>   									
											       <div style="width: 308px;">
										               <p class="nota">Ingresa tu nombre y apellido.</p>
									               </div>															
												</div>	
											</div>								  
								</h:panelGroup>	
								<h:panelGroup rendered="#{!formularioReclamoController.nombreVacio}">								 
									<div class="reclamoFila clearfix" style="padding-bottom:10px;">
										<label>Nombre:</label>
										<div class="bloqueCampos ">										      
											   <div class="textoCampo"><h:outputText value="#{formularioReclamoController.nombreUsuario}" /></div>																													
										</div>	
									</div>								 
							    </h:panelGroup>
								<!-- /FILA -->
								<!-- FILA -->
								<div class="reclamoFila clearfix" style="padding-top:1px;">
									<label>RUT:</label>
									<div class="bloqueCampos clearfix">
										<div class="textoCampo"><h:outputText value="#{formularioReclamoController.rutUsuario}" converter="rutConverter" /></div>
									</div>
								</div>
								<!-- /FILA -->
								<!-- FILA -->
								<div class="reclamoFila clearfix" style="position: relative; z-index: 21;">
									<label>N&uacute;mero reclamado:</label>
									<div class="bloqueCampos clearfix">
									     
									   <c:if test="${profile.aaa == miEntelBean.AAATitular}" var="isUserAdmin">
									        <h:selectOneMenu style="width: 307px;" styleClass="selectReclamo reclamoNumero" id="reclamoNumero" onchange="selectNumeroReclamo();">														
														<f:selectItems value="#{formularioReclamoController.msisdnAsociadoList}" />
									        </h:selectOneMenu>
									   </c:if>
									   <c:if test="${!isUserAdmin}">
										    <label><h:outputText value="#{selectorCuentaController.currentMsisdn}"></h:outputText></label>
									   </c:if>
											  
									   <input id="numeroReclamoDefoult" type="hidden" value="<h:outputText value="#{selectorCuentaController.currentMsisdn}" />"/>																														
									</div>
								</div>
								<!-- /FILA -->														
								<!-- FILA -->
								<div class="reclamoFila clearfix" style="position: relative; z-index: 20;">
									<label>Reclamo:*</label>
									<div class="bloqueCampos clearfix">
									    <select name="reclamoReclamo" class="selectReclamo reclamoReclamo" style="width: 307px;height:auto;" onchange="selectNumeroReclamo();">
											<option value="">Seleccionar</option>
											<it:iterator var="item" value="#{formularioReclamoController.tiposReclamo}">
														<option value="<h:outputText value="#{item.codigo}"/>"><h:outputText value="#{item.descripcion}"/></option>
											</it:iterator>											
										</select>
									</div>								   
								</div>
								<!-- /FILA -->							
							</div>
							
					  <!-- BLOQUE TIPO RECLAMO DEFAUT-->						
						<div class="bloqueTipoReclamoDefault">
							<!-- FILA -->
							<div class="reclamoFila filaCorta clearfix" style="position: relative; z-index: 19;">
								<label>Tipo de reclamo:*</label>
								<div class="bloqueCampos clearfix">
									<div class="clearfix">
										<select name="reclamoTipoReclamoDefault" class="selectReclamo reclamoTipoReclamoDefault" style="width: 307px;">
											<option value="">Seleccionar</option>											   
										</select>
									</div>
								</div>	
							</div>
							<!-- /FILA -->
						</div>		
						
						<!-- BLOQUE TIPO RECLAMO COMERCIAL-->						
						<div class="bloqueTipoReclamoComercial" style="display:none;">
							<!-- FILA -->
							<div class="reclamoFila filaCorta clearfix" style="position: relative; z-index: 19;">
								<label>Tipo de reclamo:*</label>
								<div class="bloqueCampos clearfix">
									<div class="clearfix">
										<select name="reclamoTipoReclamo" class="selectReclamo reclamoTipoReclamo" style="width: 307px;" onchange="descripcionTipoReclamo();">
											<option value="">Seleccionar</option>											   
										</select>
									</div>
									<div style="width: 308px;" class="tipoReclamoMensaje clearfix altoFijo">
			                              <cm:getProperty node="${contentDescripcionTiposComercial[0]}" name="html"/>
                                    </div>	
								</div>	
							</div>
							<!-- /FILA -->
						</div>
						
						
						<!-- BLOQUE TIPO RECLAMO TECNICO-->						
						<div class="bloqueTipoReclamoTecnico" style="display:none;">
							<!-- FILA -->
							<div class="reclamoFila filaCorta clearfix" style="position: relative; z-index: 19;">
								<label>Tipo de reclamo:*</label>
								<div class="bloqueCampos clearfix">
									<div class="clearfix">
										<select name="reclamoTipoReclamoTecnico" class="selectReclamo reclamoTipoReclamoTecnico" style="width: 307px;" onchange="descripcionTipoReclamo();">
											<option value="">Seleccionar</option>
											<it:iterator var="itemTecnico" value="#{formularioReclamoController.tiposReclamoTecnico}">
														<option value="<h:outputText value="#{itemTecnico.codigo}"/>"><h:outputText value="#{itemTecnico.descripcion}"/></option>
											</it:iterator>
										</select>
									</div>									
									<div style="width: 316px;" class="tipoReclamoMensaje clearfix altoFijo">
			                            <cm:getProperty node="${contentDescripcionTiposTecnico[0]}" name="html"/>	
                                    </div>	
								</div>							    			
							</div>
							<!-- /FILA -->
						    <!-- FILA -->
                             <div class="reclamoFila clearfix" style="position: relative; z-index: 18;">
                                        <label>Localizaci&oacute;n del problema:</label>
                                        <div class="bloqueCampos clearfix">
                                            <div class="subFila clearfix" style="z-index: 4;">
                                                <div class="campo regionSelectLocalizacion">
													<div class="campo-amarillo ">
														<h:selectOneMenu onchange="fillSelectsLocalizacion(this.id)" 
													 		id="regionpromocionesLocalizacion"	value="" 
													 		styleClass="selectBoxAmarillo regionpromocionesLocalizacion" style="width: 307px;">													 		     													 		    
							                                     <f:selectItems value="#{cuentaController.regionesList}"/>
							                        	</h:selectOneMenu>
													</div>
												</div>
												
                                            </div>
                                            <div class="subFila altoFijo clearfix" style="z-index: 3;">
                                               <div class="campo ciudadSelectLocalizacion">
													<div class="campo-amarillo select_ciudad_bloqueo">
														<select onchange="fillSelectsLocalizacion(this.id)"
															id="ciudadLocalizacionProblema" 
															class="selectBoxAmarillo ciudadLocalizacionProblema" style="width: 307px;">
															<option value="">Ciudad</option>																					
															<it:iterator var="item" value="#{cuentaController.ciudadesList}">
															    <option value="<h:outputText value="#{item.codigo}"/>"><h:outputText value="#{item.codigo}"/></option>
															</it:iterator>
							                            </select>							                          
													</div>																			
												</div>
												
                                            </div>
                                            <div class="subFila altoFijo clearfix" style="z-index: 2;">
	                                                <div class="campo comunaSelectLocalizacion">
														<div class="campo-amarillo ">
															<select onchange="setAreas()"
																id="comunaLocalizacionProblema"
																class="selectBoxAmarillo comunaLocalizacionProblema" style="width: 307px;">	
																<option value="">Comuna</option>																				 
																<it:iterator var="itemc" value="#{cuentaController.comunasList}">																				    
																    <option value="<h:outputText value="#{itemc.codigo}"/>"><h:outputText value="#{itemc.codigo}"/></option>
																</it:iterator>
								                             </select>								                            
														</div>																		
													</div>
												
                                            </div>
                                            <div class="subFila altoFijo clearfix" style="z-index: 1;">
                                               <div class="campo calleInputLocalizacion">
	                                                <div class="campo-amarillo ">
															<span class="left"></span>															
															  <input   class="calleLocalizacionProblema" id="calleLocalizacionProblema" maxlength="30" style="width:240px;color:#999999" />
															<span class="right"></span>
													</div>	
												</div>	
                                            </div>
                                        </div>
                               </div>
                               <!-- /FILA -->						
							
						</div>						
	
						<!-- BLOQUE TECNICO UBICACIÓN -->
						
						<!-- BLOQUE NUMERO DOCUMENTO -->
						
						<div class="bloqueNumeroDocumento">
							<!-- FILA -->
						<%-- 	<div class="reclamoFila filaCorta clearfix" style="position: relative; z-index: 17;">
								<label>N&uacute;mero Documento:*</label>
								<div class="bloqueCampos clearfix">
									<div class="clearfix">
										<input name="reclamoCobrosDocumento" class="inputReclamo reclamoCobrosDocumento" style="width: 307px;" />
									</div>
								</div>
								
							</div>
							<!-- /FILA -->
							<!-- FILA -->
							<div class="reclamoFila filaCorta clearfix" style="position: relative; z-index: 17;">
								<label>Monto Impugnado:*</label>
								<div class="bloqueCampos clearfix">
									<div class="clearfix">
										<input name="reclamoCobrosMontoDocumento" class="inputReclamo reclamoCobrosMontoDocumento input_numerico" style="width: 307px;" />
									</div>
								</div>
							</div>
						--%>	
							<!-- /FILA -->
				        <form class="reclamo_form_numero_documento" action="" method="post">
							<jsp:include page="/token.jsp" flush="true"/>
                                <div class="bloqueNumeroDocumento">
                                    <!-- FILA -->
                                    <div class="reclamoFila filaCorta clearfix" style="position: relative; z-index: 17;">
                                        <label>N&uacute;mero Documento:</label>
                                        <div class="bloqueCampos clearfix">
                                            <div class="clearfix" style="width:210px; float:left;">
                                                <input id="reclamoCobrosDocumentoReclamado" name="reclamoCobrosDocumentoReclamado" class="inputReclamo reclamoCobrosDocumento" style="width: 200px;"  onkeypress="return soloNumeros(event);" onpaste="return false;"/>
                                                
                                            </div>
                                            
                                            <a id="reclamoVerifNroNoc" class="btnAzulLargo"  style="float:left; margin-top:-5px;"><span>Verificar</span></a>
                                            
                                        </div>
                                    </div>
                                    
                                  
                                    
                                  <div class="clearfix recCajaGris">
                                         <div class="tablaNumDoc">
                                        	<div class="tndRow clearfix">
                                            	<div class="tndCol tndCol1">&nbsp;<div class="cargandoBuscarDocumento" style="display:none;"><center><img src='../framework/skins/mientel/img/thickbox/TB_cargando.gif'/></center></div> </div>
                                                <div class="tndCol tndCol2">
                                                	<span><strong>Monto cobrado</strong></span>
                                                </div>
                                                <!--<div class="tndCol tndCol2">
                                                	<span><strong>Monto impugnado</strong></span>                                                    
                                                </div>-->
                                            </div>
                                        </div>    
                                        
                                        <div class="tnLineaGris"></div>                                        
	                                        <div class="tablaNumDoc" style="margin:0;">
	                                        	<div class="tndRow clearfix">   
	                                        	   <div class="tndCol tndCol1"><label for="reclamoPorItem2"><strong>Total documento</strong></label></div>                                             
	                                                <div class="tndCol tndCol2"><input name="reclamoMontoCobrosTotal" class="inputReclamo reclamoMontoCobrosTotal disabled" style="width: 95px;" readonly value="" /></div>
	                                                <!--<div class="tndCol tndCol2">--><input name="reclamoMontoImpugnadoTotal" type="hidden" value="" /><!--</div>-->
	                                                <input name="reclamoMontoCuenta" id="reclamoMontoCobrosTotalHidden" type="hidden" value=""></input>
	                                                <input name="reclamoMontoCuenta" id="reclamoMontoCuenta" type="hidden" value=""></input>
	                                                <input name="reclamoTipoDocumento" id="reclamoTipoDocumento" type="hidden" value=""></input>
	                                            </div>                                            
	                                        </div>                                        
                                    </div> 
                                </div>
                                </form>
                                <!-- /BLOQUE NUMERO DOCUMENTO -->
                                <br/>							
						</div>						
						
						<div class="bloqueInferior" style="position:relative;">							
							<!-- FILA -->
							<div class="reclamoFila filaCorta clearfix">
								<label>Antecedentes:*</label>
								
								<div class="bloqueCampos clearfix">
									<div class="clearfix">
										<div class="textAreaReclamo">
											<textarea name="reclamoCobrosAntecedentes" class="reclamoCobrosAntecedentes" ></textarea>
										</div>
									</div>
									<div class="clearfix altoFijo" style="width: 308px;">
										<p class="nota">Ingresa los antecedentes relacionados con su reclamo, dispone de un l&iacute;mite de <span id="chars">800</span> caracteres.</p>
									</div>							
									
								</div>								
							</div>
							<!-- /FILA -->
							<!-- FILA -->
							<div class="reclamoFila filaCorta clearfix">
								<label>Selecciona medio de respuesta:*</label>
								<div class="bloqueCampos clearfix">
									<!-- FILA -->
									<div class="filaAlto clearfix" style="position:relative;z-index:3;">
										<div class="flotarIzq altoFijo" style="width: 129px;">
											<div class="flotarIzq">
												<div class="flotarIzq">
												
													<input name="checkReclamoCobrosNotificacionEmail" class="reclamoCobrosNotificacion" type="checkbox" value="1" />
												</div>											
												<span class="textoNotificacion">Email</span>
											</div>											
										</div>
										<div class="reclamo">
											<div class="clearfix" style="position:none;">
												<div class="campo"><input id="reclamoCobrosNotificacionEmail" type="text" name="emailBloqueo" class="inputReclamo reclamoCobrosNotificacionEmail" style="width:200px;" readonly="readonly" value="<h:outputText value="#{cuentaController.usuario.email}"/>"/></div>
							                        <div class="campo modificar" style="left:30px;top:-27px;">
							                        	<a class="modificarMailBloqueo" href="#">Modificar</a>
									                 	<div class="bloqueo">											    
															<div id="modificarMailCaja" class="box-numero corto modificarMailCaja" style="display:none;top:26px;z-index: 5;right:-3px;position:absolute;">
								                            	<!-- formulario (paso 1) -->
									                            <form class="modificarMail" style="" action="#">
																	<jsp:include page="/token.jsp" flush="true"/>
									                                <fieldset>
									                                    <div class="fila ancho clearfix">
									                                        <label>Ingresa nuevo e-mail:*</label>
									                                        <div class="campo"><input name="nuevoEmailBloqueo" type="text" class="inputReclamo inputBoxBloqueo nuevoEmailBloqueo" style="width:170px;" /></div>
									                                        <div class="campo boton"><a class="btnGuardarMailReclamo btnAzul"><span>Guardar</span></a></div>
									                                    </div>									                                    
									                                </fieldset>
									                           </form>
								                            	<div class="cuadro-boton">&nbsp;</div>
								                        	</div>		
														</div>  
						                        	</div>
	                                        	</div>
	                                        </div>	                                                                       
	                                     </div>
									<br/>
									<!-- /FILA -->
									
									
									<!-- FILA -->
									<div class="filaAlto clearfix" id="filaMedioRespuestaSMS" style="position:relative;z-index:2;">
										<div class="flotarIzq altoFijo" style="width: 129px;">
											<div class="flotarIzq">
												<div class="flotarIzq">
													<input name="checkreclamoCobrosNotificacionSMS" class="reclamoCobrosNotificacion" type="checkbox" value="2" />
												</div>											
												<span class="textoNotificacion" >SMS</span>
											</div>										
										</div>
										<div class="reclamo">
											<div class="clearfix" style="position:none;">
												<div class="campo"><input id="reclamoCobrosNotificacionSMS" type="text" name="" class="inputReclamo reclamoCobrosNotificacionSMS" style="width:200px;" readonly="readonly" value="<h:outputText value="#{formularioReclamoController.numeroPcs}"/>"/>
												</div>
							                        <div class="campo modificar" style="left:30px;top:-27px;">
							                        	<a class="modificarSMSBloqueo btnModificarGnerico" href="#">Modificar</a>
									                 	<div class="bloqueo">											    
															<div id="modificarSMSCaja" class="box-numero corto modificarSMSCaja" style="display:none;top:26px;z-index: 5;right:-3px;position:absolute;">
								                            	<!-- formulario (paso 1) -->
									                            <form class="modificarSMS" style="" action="#">
																	<jsp:include page="/token.jsp" flush="true"/>
									                                <fieldset>
									                                    <div class="fila ancho clearfix">
									                                        <label>Ingresa el m&oacute;vil:*</label>
									                                        <div class="campo"><input name="nuevoSMSBloqueo" type="text" class="inputReclamo inputBoxBloqueo nuevoSMSBloqueo input_numerico" maxlength="8" style="width:170px;" /></div>
									                                        <div class="campo boton"><a class="btnGuardarSMSReclamo btnAzul"><span>Guardar</span></a></div>
									                                    </div>									                                    
									                                </fieldset>
									                           </form>
								                            	<div class="cuadro-boton">&nbsp;</div>
								                        	</div>		
														</div>  
						                        	</div>
	                                        	</div>	
	                                        </div>
	                                                                               
	                                     </div>
									<br/>
									<!-- /FILA -->									
				
									<!-- FILA -->
									<div class="filaAlto clearfix" style="position:relative;z-index:1;">
										<div class="flotarIzq altoFijo" style="width: 129px;">
											<div class="flotarIzq">
												<div class="flotarIzq">
													<input name="checkReclamoCobrosNotificacionCarta" class="reclamoCobrosNotificacion" type="checkbox" value="3" />
												</div>											
												<span class="textoNotificacion">Carta Certificada</span>
											</div>
										</div>
										<div class="campo">
                                              <input id="reclamoTecnicoDireccion" type="text" value="<h:outputText value="#{cuentaController.usuario.direccionContacto.calle}"/>" name="reclamoTecnicoDireccion" class="inputBoxBloqueo inputReclamo reclamoTecnicoDireccion" style="width: 197px;" readonly="readonly" />
                                        </div>
                                   
									   <div class="bloqueo">
										<div class="campo modificar" style="width: 53px;position:relative;float:right;padding-right:20px;z-index:1;top:-30px;right:-25px;"><a class="modificarDirBloqueo" href="#">Modificar</a></div>
										<div id="modificarDirCaja" class="box-numero modificarDirCaja" style="position:absolute;top:27px;z-index:5;right:-25px;display:none;padding:26px 0 5px 20px;">
										 	<form id="modificarDireccion" class="modificarDireccion" name="modificarDireccion" style="" action="#">
												<jsp:include page="/token.jsp" flush="true"/>
					                                <input type="hidden" name="s_region" value="">
					                                <input type="hidden" name="s_ciudad" value="">
					                                <input type="hidden" name="s_comuna" value="">
					                                
										      <fieldset>										        
													<!-- FILA -->
													    														
																<div class="mis-datos-fila clearfix" style="width:500px;">
																	<label style="width: 100px;">Regi&oacute;n:*</label>
																	<div class="campo regionSelect">
																		<div class="campo-amarillo ">
																			<h:selectOneMenu onchange="fillSelects(this.id)" 
																		 		id="regionpromocionesTecnico"
																		 		value="#{cuentaController.usuario.direccionContacto.region.codigo}" 
																		 		styleClass="selectBoxAmarillo reclamoTecnicoRegion region-promociones region_Reclamo" style="width: 307px;">
												                                     <f:selectItems value="#{cuentaController.regionesList}"/>
												                        	</h:selectOneMenu>
																		</div>
																	</div>
																	<div class="regionText" style="display:none;" ></div>																	
																</div>
																<div class="mis-datos-fila clearfix" style="width:500px;">
																		<label style="width: 100px;">Ciudad:*</label>
																		<div class="campo ciudadSelect">
																			<div class="campo-amarillo select_ciudad_bloqueo">
																				<select onchange="fillSelects(this.id)"
																					id="ciudadpromocionesTecnico" 
																					class="selectBoxAmarillo reclamoTecnicoCiudad ciudad_Reclamo" style="width: 307px;">																					
																					<it:iterator var="item" value="#{cuentaController.ciudadesList}">
																					    <option value="<h:outputText value="#{item.codigo}"/>"><h:outputText value="#{item.codigo}"/></option>
																					</it:iterator>
													                            </select>
													                            <h:inputHidden id="hcpromocionesTecnico" value="#{cuentaController.usuario.direccionContacto.ciudad.codigo}"/> 
																			</div>																			
																		</div>
																		<div class="ciudadText" style="display: none" ></div>																		
																</div>															
																
																
																<div class="mis-datos-fila clearfix" style="width:500px;">
																	<label style="width: 100px;">Comuna:*</label>
																	<div class="campo comunaSelect">
																		<div class="campo-amarillo ">
																			<select onchange="setAreas()"
																				id="comunapromocionesTecnico"
																				class="selectBoxAmarillo reclamoTecnicoComuna comuna_Reclamo" style="width: 307px;">																				 
																				<it:iterator var="itemc" value="#{cuentaController.comunasList}">																				    
																				    <option value="<h:outputText value="#{itemc.codigo}"/>"><h:outputText value="#{itemc.codigo}"/></option>
																				</it:iterator>
												                             </select>
												                            <h:inputHidden id="hcompromocionesTecnico" value="#{cuentaController.usuario.direccionContacto.comuna.codigo}"/> 
																		</div>																		
																	</div>
																	<div class="comunaText" style="display: none;" ></div>
																</div>
																
																<div class="mis-datos-fila clearfix" style="width:500px;">
																	<label style="width: 100px;">Calle:*</label>
																	<div class="campo calleInput">
																		<div class="campo-amarillo ">
																			<span class="left"></span><h:inputText value="#{cuentaController.usuario.direccionContacto.calle}" styleClass="calle reclamoTecnicoCalle" id="calleReclamoTecnico" maxlength="30" style="width:240px;" /><span class="right"></span>
																		</div>																		
																	</div>																	
																	<div class="calleText" style="display: none;" ></div>																	
																</div>
																
																<div class="fila clearfix">										
																     <label style="width: 100px;">N&uacute;mero:*</label>											
																		<div class="campo numeroInput">
																			<div class="campo-amarillo ">
																				<span class="left"></span><h:inputText id="numero" styleClass="numero reclamoTecnicoNumero" maxlength="30" style="width:50px" onkeypress="return soloNumeros(event);" value="#{cuentaController.usuario.direccionContacto.numero}" /><span class="right"></span>
																			</div>
																		</div>
																	    <div class="campo numeroText" style="width:50px" ></div>											
																	<div class="subcampo clearfix subcampo_reclamo">
																		<label style="width: 100px;">Depto / Oficina:</label>												
																		<div class="campo-amarillo deptoInput">
																			<span class="left"></span><h:inputText styleClass="depto_casa_otro" value="#{cuentaController.usuario.direccionContacto.departamento}" maxlength="30" style="width:50px" /><span class="right"></span>
																		</div>												
																		<div class="campo dptoText" style="width:50px"  ></div>												
																		<div class="campo"><a class="btnValidarDirecion_Reclamo btnAzul"><span>Guardar</span></a></div>
																	</div>
															  </div>
															   <!-- /MIS DATOS FILA -->
							                                    <div class="fila mensaje clearfix">
							                                        <div class="campo">Este cambio afectar&aacute; el registro de la direcci&oacute;n de tus datos de usuario.<br />&iquest;Est&aacute;s seguro que deseas modificarlos?</div>
							                                        <div class="subcampo"><a class="btnConfirmarDirReclamo btnAzul"><span>Confirmar</span></a></div>
							                                    </div> 
																<!-- DIRECCION -->							
												 </fieldset>			
											  </form>
											<div class="cuadro-boton">&nbsp;</div>
										</div>										
										</div>
									</div>									
									<!-- FILA -->
								</div>								
							</div>
							<!-- /FILA -->
							<!-- FILA -->
							<div class="reclamoFila altoTriple clearfix" style="padding-bottom:80px;">
								<label></label>
								<div class="bloqueCampos clearfix">
									<div class="flotarIzq">
										<a href="#" class="btnAzulGrande btnAzulGrandeLargo btnReclamoCobros"><span>Siguiente</span></a>
									</div>
									<div class="flotarIzq">
										<a href="#" class="botonEnlace borrarFormulario">Borrar</a>
									</div>
								</div>								
							</div>													
						</div>			
						<!-- /FILA -->	
						
						<!-- BLOQUE RESUMEN COBROS -->
						<div class="bloqueResumen" style="display:none;">
						    <!-- FILA -->
							<div class="reclamoFila clearfix">
								<label><strong>Concesionaria:</strong></label>
								<div class="bloqueCampos clearfix">
									<div class="textoCampo">Entel PCS Telecomunicaciones S. A.</div>
								</div>
							</div>
							<!-- /FILA -->
							<!-- FILA -->
							<div class="reclamoFila clearfix">
								<label><strong>Nombre:</strong></label>
								<div class="bloqueCampos clearfix">
									<div class="textoCampo resumen_nombre"></div>
								</div>
							</div>
							<!-- /FILA -->
							<!-- FILA -->
							<div class="reclamoFila  clearfix">
								<label><strong>RUT:</strong></label>
								<div class="bloqueCampos clearfix">
									<div class="textoCampo resumen_rut"><h:outputText value="#{formularioReclamoController.rutUsuario}" converter="rutConverter" /></div>
								</div>
							</div>
							<!-- /FILA -->
							<!-- FILA -->
							<div class="reclamoFila  clearfix">
								<label><strong>N&uacute;mero reclamado:</strong></label>
								<div class="bloqueCampos clearfix">
									<div class="textoCampo resumen_numero_reclamo"></div>
								</div>
							</div>
							<!-- /FILA -->
							<!-- /FILA -->
							<div class="reclamoFila  clearfix">
								<label><strong>Reclamo:</strong></label>
								<div class="bloqueCampos clearfix">
									<div class="textoCampo resumen_reclamo"></div>
								</div>
							</div>
							<!-- /FILA -->
							<!-- FILA -->
							<div class="reclamoFila  clearfix">
								<label><strong>Tipo de reclamo:</strong></label>
								<div class="bloqueCampos clearfix">
									<div class="textoCampo resumen_tipo_reclamo"></div>
								</div>
							</div>
							<!-- /FILA -->
							<!-- FILA -->
							<div class="reclamoFila  clearfix solo_comercial" style="display:none;">
								<label><strong>N&uacute;mero de Documento:</strong></label>
								<div class="bloqueCampos clearfix">
									<div class="textoCampo textoCobroNumero resumen_numero_docuemnto"></div>
								</div>
							</div>
							<!-- /FILA -->
							<!-- FILA -->
							<div class="reclamoFila  clearfix solo_comercial" style="display:none;">
								<label><strong>Monto Impugnado:</strong></label>
								<div class="bloqueCampos clearfix">
									<div class="textoCampo textoCobroMonto resumen_monto"></div>
								</div>
							</div>
							<!-- /FILA -->
							<!-- FILA -->
							<div class="reclamoFila  clearfix solo_tecnico" style="display:none;">
								<label><strong>Localizaci&oacute;n del problema:</strong></label>
								<div class="bloqueCampos clearfix">
									<div class="textoCampo textoCobroLocalizacion resumen_localizacion_problema"></div>
								</div>
							</div>
							<!-- /FILA -->							
							<!-- FILA -->
							<div class="reclamoFila  clearfix">
								<label><strong>Antecedentes:</strong></label>
								<div class="bloqueCampos clearfix">
									<div class="textoCampo textoCobroAntecedentes resumen_antecedentes"></div>
								</div>
							</div>
							<!-- /FILA -->
							<!-- FILA -->
							<div class="reclamoFila  clearfix">
								<label><strong>Medio de respuesta:</strong></label>
								<div class="bloqueCampos clearfix">
									<div class="textoCampo textoCobroNotificacion resumen_medio_respuesta"></div>
								</div>
							</div>
							<!-- /FILA -->							
							<!-- MENSAJE CONFIRMACION -->
							<div class="mensajeAlertaMediano mensajeConfirmacionResumen distanciaTriple clearfix">
							    <p style="padding-left:20px;"><cm:getProperty node="${mensajeConfirmacion[0]}" name="html"/></p>
								<span class="texto">&iquest;Desea ingresar el reclamo?</span>
								<div class="flotarIzq">
									<a href="#" id="botonConfirmar" class="btnAzul btnAzulMediano btnConfirmarResumen">
										<span>Confirmar</span>
									</a>
								</div>
								<div class="flotarIzq">
									<a href="#" class="botonEnlace btnResumenCobroCancelar">Cancelar</a>
								</div>
							</div>
							<!-- /MENSAJE CONFIRMACION -->
							<div class="bloqueConfirmacionExito">
								<!-- MENSAJE CONFIRMACION EXITO -->
								<div class="mensajeConfirmacionExito distanciaTriple">
									<cm:getProperty node="${mensajeConfirmacionExitoCM[0]}" name="html"/>
								</div>
								<!-- /MENSAJE CONFIRMACION EXITO -->
								<div class="clearfix" style="padding-top: 15px;">
									<div class="flotarIzq" style="padding-left: 155px;">
										<a href="#" class="btnAzul btnAzulMediano botonImpresion">
											<span>Imprimir</span>
										</a>
									</div>
								</div>
							</div>
							
							<div class="cargandoReclamo" style="display:none;"><center><br/><img src='../framework/skins/mientel/img/thickbox/TB_cargando.gif'/><br/><br/></center></div>
							
							<!-- MENSAJE DE ALERTA -->
							<div class="mensajeAlertaGrande distanciaTriple clearfix" style="display:none;">
									<span class="textoArriba">En este momento no podemos recibir su reclamo, vuelva a intentarlo por favor.</span>
									<div class="clearfix">
										<div class="clearfix" style="margin: 0 auto; width: 210px;">
											<div class="flotarIzq">
												<a class="btnAzul btnAzulReclamo volverAIntentar">
													<span>Volver a intentar</span>
												</a>
											</div>
											<div class="flotarDer">
												<a class="botonEnlace btnResumenCobroCancelar">Cancelar</a>
											</div>
										</div>
									</div>
							</div>
							<!-- /MENSAJE DE ALERTA -->	
							
						</div>
						<!-- /BLOQUE RESUMEN  -->
						
					</div>	
				</div>				
				
			</div>
	
		  
	<form id="formularioImpresion" action="<%=request.getContextPath()%>/portlet/formularioreclamo/comprobanteReclamoImprimir.jsp" method="post">
		<jsp:include page="/token.jsp" flush="true"/>
		<input name="imp_nombre" id="imp_nombre" type="hidden" value=""/>
		<input name="imp_rut" id="imp_rut" type="hidden" value=""/>
		<input name="imp_numero_reclamado" id="imp_numero_reclamado" type="hidden" value=""/>		
		<input name="imp_reclamo" id="imp_reclamo" type="hidden" value=""/>
		<input name="id_imp_reclamo" id="id_imp_reclamo" type="hidden" value=""/>
		<input name="imp_tipo_reclamo" id="imp_tipo_reclamo" type="hidden" value=""/>
		<input name="id_imp_tipo_reclamo" id="id_imp_tipo_reclamo" type="hidden" value=""/>
		
		<input name="imp_localizacion" id="imp_localizacion" type="hidden" value=""/>
		<input name="id_imp_localizacion" id="id_imp_localizacion" type="hidden" value="0"/>		
		
		<input name="imp_nro_doc" id="imp_nro_doc" type="hidden" value=""/>		
		<input name="imp_monto_doc" id="imp_monto_doc" type="hidden" value=""/>
		
		<input name="imp_antecedentes" id="imp_antecedentes" type="hidden" value=""/>		
		<input name="imp_medio_respuesta" id="imp_medio_respuesta" type="hidden" value=""/>
		<input name="imp_cod_reclamo" id="imp_cod_reclamo" type="hidden" value=""/>		
		
	</form>	

	<body >
	    <form id="form_detalle_reclamo" action="#">
			<jsp:include page="/token.jsp" flush="true"/>
          <a href="#TB_inline?height=300px&width=590&inlineId=myOnPageContent" class="thickbox"></a>
    </form>
	  
	 <!-- INICIO BLOQUE DETALLE RECLAMOS --> 
	   <div id="myOnPageContent"  style="display:none;"> 
	     <div  class="recDetalleComercial" style="OVERFLOW:auto;TOP:10px;height:430px;">  
	      <div id="imprimir_detalle">  	        
	        <div class="clearfix rdcCajaTop">
	        	<div class="rdcColIzq" style="width:235px;">
	            	<span><strong>Fecha de ingreso:</strong></span >&nbsp;<span id="detalle_fecha_ingreso"></span>
	            </div>
	            <div class="rdcColDer">
	            	<span><strong>N&uacute;mero de Ticket:</strong></span>&nbsp;<span id="detalle_numero_ticket"></span>
	            </div>            
	        </div>        
	        <h2 align="center">DETALLE RECLAMO</h2>        
	        <table class="rdcTabla" style="width: 550px;">
	        	<tbody style="width: 550px;">
	        	<tr>
	            	<th style="width:180px;" valign="top"><span>Concesionaria:</span></th>
	                <td style="width:350px;" valign="top" ><span>Entel PCS Telecomunicaciones S.A.</span></td>
	            </tr>
	            <tr>
	            	<th style="width:180px;" valign="top"><span>Nombre:</span></th>
	                <td style="width:350px;" valign="top" id="detalle_nombre"><span><h:outputText value="#{formularioReclamoController.nombreUsuario}" /></span></td>
	            </tr> 
	            <tr>
	            	<th valign="top"><span>RUT:</span></th>
	                <td valign="top"><span id="detalle_rut"></span></td>
	            </tr>
	            <tr>
	            	<th valign="top"><span>N&uacute;mero reclamado:</span></th>
	                <td valign="top"><span id="detalle_numero_reclamo"></span></td>
	            </tr>
	            <!-- 
		            <tr>
		            	<th valign="top"><span>Producto reclamado:</span></th>
		                <td valign="top"><span id="detalle_producto"></span></td>
		            </tr>
	             -->	           
	            <tr>
	            	<th valign="top"><span>Reclamo:</span></th>
	                <td valign="top"><span id="detalle_reclamo"></span></td>
	            </tr>
	            <tr style="display:none;">
	            	<th valign="top"><span>Tipo de reclamo:</span></th>
	                <td valign="top"><span id="detalle_tipo_reclamo"></span></td>
	            </tr>
	            <tr class="detalle_solo_numero_docuemnto" style="display:none;">
	            	<th valign="top"><span>N&uacute;mero de Documento:</span></th>
	                <td valign="top"><span id="detalle_numero_documento"></span></td>
	            </tr>
	            <tr class="detalle_solo_numero_docuemnto" style="display:none;">
	            	<th valign="top"><span>Monto:</span></th>
	                <td valign="top"><span id="detalle_monto_documento"></span></td>
	            </tr>	
	            <tr id="detalle_solo_direccion_reclamada" style="display:none;">
	            	<th valign="top"><span>Localizaci&oacute;n del problema:</span></th>
	                <td valign="top"><span id="detalle_direccion_reclamada"></span></td>
	            </tr>            
	            <tr>
	            	<th valign="top"><span>Antecedentes:</span></th>
	                <td valign="top"><div id="detalle_antecedentes" style="padding: 3px !important;"></div></td>
	            </tr>
	            <tr>
	            	<th valign="top"><span>Notificaci&oacute;n:</span></th>
	                <td valign="top"><span id="detalle_notificacion" ></span></td>
	            </tr>
	            <tr>
	            	<th valign="top"><span>Estado:</span></th>
	                <td valign="top" ><span id="detalle_estado"></span></td>
	            </tr>       	
	            </tbody>
	        </table> 
	        <p style="font-weight: bold; color:#C0C0C0;padding:0px!important;border-top: 1px solid #999999; margin-left: 170px;width:250px; padding-top: 20px;text-align: center;margin-top:30px;" >Firma del cliente o representante legal</p>	        
	      </div> 
	    </div>  
	      <div class="clearfix rdcBotonImpReclamo" style="padding-top:5px;padding-bottom:5px;">
				<div class="clearfix rdcbImprime" style="margin-bottom:5px;">
					<a href="javascript:imprimir();" class="btnAzulGrande btnAzulGrandeLargo"><span>Imprimir</span></a>					
				</div>
			</div>	         
	          
		     
    </div>   
	  
	  
	<!-- FIN BLOQUEO DETALLE RECLAMOS -->  
	</body>
</html>

</f:view> 
