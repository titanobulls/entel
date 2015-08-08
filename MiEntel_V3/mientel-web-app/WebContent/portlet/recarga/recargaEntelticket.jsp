<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="f"  uri="http://java.sun.com/jsf/core"%>
<%@ taglib prefix="h"  uri="http://java.sun.com/jsf/html"%>
<%@ taglib prefix="c"  uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="it" uri="http://i2b.cl/jsf/iterator/" %>


<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<!-- <script type="text/javascript" src="https://mi.entel.cl/personas/mientelresources/js/ampliacion.numeracion.js"></script> -->
 
<f:view beforePhase="#{recargaController.init}">

	<h1>Recargas</h1>

	<!-- MENSAJES -->
	<jsp:include page="../common/messages_table.jsp"></jsp:include>
	
	<h:form id="formRecEntelTicket" styleClass="formRecEntelTicket">
	<jsp:include page="/token.jsp" flush="true"/>
    <div class="margen_azul_superior"></div>
    <div class="margen_azul_contenido clearfix">
    	<span class="marginador clearfix">Selecciona como quieres recargar:</span>
    	
		<script type="text/javascript">
			var esVozBAM = "<h:outputText value="#{profile.flagBam == miEntelBean.siglaUsuarioBAM ? 'Banda Ancha Movil' : 'Telefonia'}"/>";
			
			$(document).ready(function() {
				//$("#top-select select").selectBox();
				$('.select-recarga')[0].setValue('<h:outputText value="#{recargaController.tipoRecarga}"></h:outputText>');

				$('.prefijo').change(function(){
					var fecha = '<h:outputText value="#{recargaController.fechaActualFormat}"></h:outputText>';
					$('.ampliacionNumerica').attr('maxlength',ampliacionNumerica( parseInt($('.prefijo').val(), 10),fecha));
					$('.ampliacionNumerica').val('');
				});

				$('.producto').change(function() {
					var prod = $(this).val();
					var prefijo = $('select[id*=prefijo]');
					var mercado = '<h:outputText value="#{recargaController.mercado}" />';
					var inputTelefono = $('input[id*=numero_telefono]');
					if (prod == 'Telefon�a M�vil' || prod == 'BAM' || prod == 'BAM Hogar') {
						if (!$.browser.msie || ($.browser.msie && $.browser.version > '6.0')) {
							if (prefijo.find("option[value='09']").length == 0) {
								prefijo.append("<option value='09'>09</option>");
							}
						}
						prefijo[0].setValue('09');
						prefijo.get(0).disable();
						if (mercado == 'PP' || mercado == 'CC') {
							inputTelefono.val('<h:outputText value="#{recargaController.numeroPcs}"></h:outputText>');
						}
					} else if (prod == 'Telefon�a Hogar') {
						prefijo[0].setValue('02');
						if (!$.browser.msie || ($.browser.msie && $.browser.version > '6.0')) {
							prefijo.find("option[value='09']").remove();
						}
						inputTelefono.val('');
						prefijo.get(0).enable();
					}
				});

				$('select[id*=prefijo]')[0].setValue('09');
				$('.prefijo').trigger("change");
				$('.producto').trigger("change");

				dataLayer = dataLayer||[];
  				dataLayer.push({
    				'mx_content': 'Personas/Mi Entel/Mis Productos/' + esVozBAM + '/Recargas/Entelticket',
    				'event': 'pageview'
  				});					
			});
		</script>
    	
    	<h:selectOneMenu id="select-recarga" styleClass="selectBox select-recarga" enabledClass="select-recarga" style="width: 350px;" 
    		value="#{recargaController.tipoRecarga}" valueChangeListener="#{recargaController.seleccionTipoRecarga}" onchange="submit()">
    		<f:selectItems value="#{recargaController.tiposRecarga}" /> 
    	</h:selectOneMenu>
    </div>
    <div class="margen_azul_inferior"></div>
	

	<h2 style="padding-left: 0;">Entelticket</h2>
	
	<p><strong>Paso 1 de 3</strong></p>
	
	<div id="tabla_formulario" class="tabla_formulario">
	
	    <span class="marginador clearfix">Ingresa el n&uacute;mero de un Entelticket.<br />
	    Tambi&eacute;n puedes recargar otro prepago con tu tarjeta Entelticket.</span>

	     <div class="tabla_formulario_fila clearfix" style="position:relative;z-index:40;">
	     	<div class="tabla_formulario_label recortador">Selecciona producto a recargar:</div>
			<div class="tabla_formulario_dato formulario_input campo">
		        <div class="campo">
					<h:selectOneMenu id="producto" style="width:140px;" styleClass="selectBox producto monto_recarga_safari" value="#{recargaController.productoRecargar}">
	        			<f:selectItems value="#{recargaController.productosRecarga}"/>
	        		</h:selectOneMenu>
	             </div>
	        </div>
	     </div>

	     <div class="tabla_formulario_fila clearfix" style="position:relative;z-index:20;">
	        <div class="tabla_formulario_label recortador">N&uacute;mero a recargar:</div>
	        <div class="tabla_formulario_dato formulario_input campo">
		        <div class="campo" >
						<h:selectOneMenu id="prefijo" style="width:50px;" styleClass="selectBox prefijo codigo_tel_adicional required" value="#{recargaController.indicativoTelefono}">
		        			<f:selectItems value="#{recargaController.prefijosTelefono}"/>
		        		</h:selectOneMenu>
	             </div>
		      <div class="campo" style="position: relative; float: left; margin: 0 0 0 5px;">
		        <h:inputText styleClass="inputBox numero_prepago_ticket required ampliacionNumerica"
		            	value="#{recargaController.numeroPcs}" id="numero_telefono"
		            	style="width:85px;" maxlength="8"
		            	onkeypress="return soloNumeros(event);" />
	           </div>
	        </div>
	        <div class="tabla_formulario_dato texto_ejemplo">
                Ej. 92227755    
            </div>
	        <div class="mensaje_alerta0">
	        	<table class="tabla_alerta2">
	                <tbody>
	
	                    <tr>
	                        <td class="tabla_alerta2">Ingrese un n&uacute;mero.</td>
	                    </tr>
	                </tbody>
	            </table>
	        </div>
	        <div class="mensaje_alerta1">
	        	<table class="tabla_alerta2">
	
	                <tbody>
	                    <tr>
	                        <td class="tabla_alerta2">Ingrese un n&uacute;mero v&aacute;lido.</td>
	                    </tr>
	                </tbody>
	            </table>
	        </div>
	
	    </div>
	    
	    <div class="tabla_formulario_fila clearfix">
	        <div class="tabla_formulario_label recortador">N&uacute;mero secreto Entelticket:</div>
	        <div class="tabla_formulario_dato formulario_input campo" style="width:150px;">
		        <div class="campo">
		            <h:inputText styleClass="inputBox numero_eticket required"
		            	value="#{recargaController.numeroEntelTicket}" id="numero_eticket"
		            	style="width:140px;" maxlength="16"
		            	onkeypress="return soloNumeros(event);" />
		         </div>
	        </div>
	        <div class="mensaje_alerta2">
	        	Ingrese su n&uacute;mero secreto de Entelticket.
	        </div>
	
	        <div class="tabla_formulario_dato texto_ejemplo">Ej. 1234567891213</div>
	    </div>
	    
	    <div class="tabla_formulario_fila clearfix">
	        <div class="tabla_formulario_label recortador">Reingresa el n&uacute;mero de tu Entelticket:</div>
	        <div class="tabla_formulario_input campo">
	          <div class="campo">
	            <h:inputText styleClass="inputBox numero_eticket_repeticion required" value="#{recargaController.numeroEntelTicket}" id="numero_eticket_repeticion"
	            style="width:140px;" maxlength="16"
	            onkeypress="return soloNumeros(event);" />
	           </div>
	        </div>
	
	        <div class="mensaje_alerta3">
	        	Los n&uacute;meros no coinciden.
	        </div>
	        <div class="mensaje_alerta4">
	        	Reingrese su n&uacute;mero secreto de Entelticket.
	        </div>
	    </div>
	</div>
	
	<div class="botonera_paso1">
	    <h:commandButton styleClass="botonContinuarNaranja"
	    	action="#{recargaController.solicitarRecargaEntelTicket}"></h:commandButton>
	</div>
	
	</h:form>

</f:view>