<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="f"  uri="http://java.sun.com/jsf/core"%>
<%@ taglib prefix="h"  uri="http://java.sun.com/jsf/html"%>
<%@ taglib prefix="c"  uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="it" uri="http://i2b.cl/jsf/iterator/" %>

<!-- <script type="text/javascript" src="https://mi.entel.cl/personas/mientelresources/js/ampliacion.numeracion.js"></script> -->
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<script>
    jQuery.each(jQuery.browser, function(i, val) {
      if(i=="msie"){
          $("select[id*=tarjeta_multitienda]").attr("style", "width: 142px;");
          $("select[id*=monto_recarga]").attr("style", "width: 142px;");
          $("select[id*=cuotas]").attr("style", "width: 142px;");
      } else if(navigator.userAgent.indexOf('Safari/') != -1 && navigator.userAgent.indexOf('Chrome/') == -1) {
      	  $("select[id*=tarjeta_multitienda]").attr("style", "width: 166px;");
          $("select[id*=monto_recarga]").attr("style", "width: 166px;");
          $("select[id*=cuotas]").attr("style", "width: 166px;");
      }
    });
</script>


<script type="text/javascript">

function getMontos(obj){
	if(obj.value == "" ){
		reset();
		return;
	}
	var index = obj.selectedIndex;
	$("input[id*=nombreMultitienda]").val(obj.options[index].text);	
	var url='<%=request.getContextPath()%>/portlet/recarga/cargarMontos.faces?multitienda='+obj.value;
	$.ajax({
         type: 'POST',
         url: url,
         dataType: 'json',
         success: function (resp){        	
        	var optionsselect = "<option value=''></option>";
			for(var i = 0; i< resp.length;i++){
            	optionsselect += '<option value="'+resp[i].codigo+'">'+resp[i].descripcion+'</option>';
	       	}
	       	$("select[id*=monto_recarga]").html(optionsselect);
	       	$("input[id*=monto]").val(resp[0].codigo);
	       	getCuotas(obj);
	       	$("select[id*=monto_recarga]").val("");
		 }
	});	
	
}

function getCuotas(obj){
	if(obj.value == "" ){
		reset();
		return;
	}
	var url='<%=request.getContextPath()%>/portlet/recarga/cargarCuotas.faces?multitienda='+obj.value;
	$.ajax({
         type: 'POST',
         url: url,
         dataType: 'json',
         success: function (resp){
			var optionsselect = "<option value=''></option>";
			for(var i = 0; i< resp.length;i++){
            	optionsselect += '<option value="'+resp[i].codigo+'">'+resp[i].descripcion+'</option>';
	       	}
	       	$("select[id*=cuotas]").html(optionsselect);
	       	$("input[id*=cuota]").val(resp[0].codigo);
	       	$("select[id*=cuotas]").val("");
		 }
	});
}

function setMonto(obj){
	$("input[id*=monto]").val(obj.value);		
}

function setCuotas(obj){		
	$("input[id*=cuota]").val(obj.value);
}

function reset(){
	$("select[id*=cuotas]").html("");
	$("select[id*=monto_recarga]").html("");
	$("input[id*=cuota]").val("");
	$("input[id*=monto]").val("");
}

</script>

<f:view beforePhase="#{recargaController.init}">

<script>
var esVozBAM = "<h:outputText value="#{profile.flagBam == miEntelBean.siglaUsuarioBAM ? 'Banda Ancha Movil' : 'Telefonia'}"/>";

$(document).ready(function() {
	$("select[id*=tarjeta_multitienda]")[0].setValue('<h:outputText value="#{recargaController.multitienda}"/>');
	getMontosLoad($("select[id*=tarjeta_multitienda]"));

	$('.prefijo').change(function(){
	    var fecha = '<h:outputText value="#{recargaController.fechaActualFormat}"></h:outputText>';	
	   $('.ampliacionNumerica').attr('maxlength',ampliacionNumerica( parseInt($('.prefijo').val(), 10),fecha));
	   $('.ampliacionNumerica').val('');
    });

	$('.producto').change(function() {
		var prod = $(this).val();
		var prefijo = $('select[id*=prefijo]');
		var mercado = '<h:outputText value="#{recargaController.mercado}" />';
		var inputTelefono = $('input[id*=numero_prepago]'); 
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
		'mx_content': 'Personas/Mi Entel/Mis Productos/' + esVozBAM + '/Recargas/CMR Falabella',
		'event': 'pageview'
	});
});

function getMontosLoad(obj){
	if(obj.val() == "" ){
		reset();
		return;
	}
	var index = obj.attr("selectedIndex");	
	$("input[id*=nombreMultitienda]").val($("select[id*=tarjeta_multitienda] option:selected").text());	
	var url='<%=request.getContextPath()%>/portlet/recarga/cargarMontos.faces?multitienda=' + obj.val();
	$.ajax({
         type: 'POST',
         url: url,
         dataType: 'json',
         success: function (resp){        	
        	var optionsselect = "<option value=''></option>";
			for(var i = 0; i< resp.length;i++){
            	optionsselect += '<option value="'+resp[i].codigo+'">'+resp[i].descripcion+'</option>';
	       	}
	       	$("select[id*=monto_recarga]").html(optionsselect);
	       	$("input[id*=monto]").val(resp[0].codigo);	       		       	
	       	$("select[id*=monto_recarga]")[0].setValue('<h:outputText value="#{recargaController.montoRecarga}"/>');
	       	getCuotasLoad(obj);	       	
		 }
	});
}

function getCuotasLoad(obj){
	if(obj.val() == "" ){
		reset();
		return;
	}
	var url='<%=request.getContextPath()%>/portlet/recarga/cargarCuotas.faces?multitienda='+obj.val();
	$.ajax({
         type: 'POST',
         url: url,
         dataType: 'json',
         success: function (resp){
			var optionsselect = "<option value=''></option>";
			for(var i = 0; i< resp.length;i++){
            	optionsselect += '<option value="'+resp[i].codigo+'">'+resp[i].descripcion+'</option>';
	       	}
	       	$("select[id*=cuotas]").html(optionsselect);
	       	$("input[id*=cuota]").val(resp[0].codigo);	       	
	       	$("select[id*=cuotas]")[0].setValue('<h:outputText value="#{recargaController.cuotas}"/>');
		 }
	});
}

</script>

<h1>Recargas</h1>

<!-- MENSAJES -->
<jsp:include page="../common/messages_table.jsp"></jsp:include>


	<h:form id="formMultitienda" styleClass="formMultitienda">
	<jsp:include page="/token.jsp" flush="true"/>
	<div class="margen_azul_superior"></div>
	   <div class="margen_azul_contenido clearfix">
	   	<span class="marginador clearfix">Selecciona como quieres recargar:</span>
	   	
		<script type="text/javascript">
			$(document).ready(function() {
				//$("#top-select select").selectBox();
				$('.select-recarga')[0].setValue('<h:outputText value="#{recargaController.tipoRecarga}"></h:outputText>');
			});
		</script>
	   	
	   	<h:selectOneMenu id="select-recarga" styleClass="selectBox select-recarga" enabledClass="select-recarga" style="width: 350px;" 
	   		value="#{recargaController.tipoRecarga}" valueChangeListener="#{recargaController.seleccionTipoRecarga}" onchange="submit()">
	   		<f:selectItems value="#{recargaController.tiposRecarga}" /> 
	   	</h:selectOneMenu>
	   </div>
	   <div class="margen_azul_inferior"></div>
	   


	
	<h:inputHidden id="monto" value="#{recargaController.montoRecarga}"></h:inputHidden>
	<h:inputHidden id="cuota" value="#{recargaController.cuotas}"></h:inputHidden>
	<h:inputHidden id="nombreMultitienda" value="#{recargaController.nombreMultitienda}"></h:inputHidden>
	
	<h2 style="padding-left: 0;">Multitiendas</h2>
	
	<p><strong>Paso 1 de 3</strong></p>
	
	<div id="tabla_formulario" class="tabla_formulario">
	
	    <span class="marginador clearfix">Selecciona el tipo de tarjeta, el n&uacute;mero de cuotas y el monto que deseas recargar.</span>
	    
	    <div class="tabla_formulario_fila clearfix" style="position:relative;z-index:40;">	        
	        <div class="tabla_formulario_label recortador">Tarjeta multitienda:</div>
	        <div class="tabla_formulario_input campo">
	            <h:selectOneMenu value="#{recargaController.multitienda}" onchange="getMontos(this)" id="tarjeta_multitienda" styleClass="selectBox tarjeta_multitienda required" style="width: 140px;" >
	        		<f:selectItems value="#{recargaController.multitiendas}"/>
	        	</h:selectOneMenu>	        	                        
	        </div>
	    </div>
	    
	     <div class="tabla_formulario_fila clearfix" style="position:relative;z-index:30;">
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
	        
	        
	        <div class="campo " style="position:relative;z-index:10;">					
						<h:selectOneMenu  id="prefijo" style="width:50px;" styleClass="codigo_tel_adicional selectBox prefijo" value="#{recargaController.indicativoTelefono}">
		        			<f:selectItems value="#{recargaController.prefijosTelefono}"/>
		        		</h:selectOneMenu>					
	        </div>  
		    <div  class="tabla_formulario_dato formulario_input campo" style="width:90px;position:relative;z-index:20;margin-left:5px;">
		            
			      <h:inputText styleClass="inputBox numero_prepago_multi required ampliacionNumerica" 
			        value="#{recargaController.numeroPcs}" id="numero_prepago" 
			        style="width: 85px; border: medium none; background: none repeat scroll 0% 0% transparent; text-align: right;" 
			        maxlength="8" onkeypress="return soloNumeros(event);" />
		
		     </div>
	        <div class="tabla_formulario_dato texto_ejemplo">
                Ej. 92227755    
            </div>
			<!--<div class="tabla_formulario_numero"><strong class="posicion_numero">93799181</strong></div>-->
	    </div>
	                    
	    <div class="tabla_formulario_fila clearfix aceptar">
	        <div class="tabla_formulario_label recortador">N&uacute;mero de la tarjeta:</div>
	        <div class="tabla_formulario_dato formulario_input campo">
	            
				<h:inputText styleClass="inputBox numero_tarjeta required" value="#{recargaController.numeroTarjetaMultitienda}"   
	        style="width: 140px; border: medium none; background: none repeat scroll 0% 0% transparent; text-align: left;" 
	        maxlength="20" />
	
	        </div>
	
	    </div>
	    
	    <div class="tabla_formulario_fila clearfix aceptar">
	        <div class="tabla_formulario_label recortador">Clave internet CMR:</div>
	        <div class="tabla_formulario_dato tabla_formulario_input campo" style="width:145px;">
	            
	            <h:inputSecret styleClass="inputBox clave required" value="#{recargaController.claveTarjetaMultitienda}"  
	        style="width: 140px; border: medium none; background: none repeat scroll 0% 0% transparent; text-align: left;" 
	        maxlength="4" />
	            
	        </div>
	        <div class="tabla_formulario_dato texto_ejemplo">
	            Ej. 1244	
	        </div>
	    </div>
	                    
	    <div class="tabla_formulario_fila clearfix" style="position:relative;z-index:10;">
	        <div class="tabla_formulario_label recortador">Monto a cargar:</div>
	        <div class="tabla_formulario_dato tabla_formulario_input">
	            <select class="selectBox monto_recarga required" id="monto_recarga" name="monto_recarga" onchange="setMonto(this)" style="width: 140px;"></select>
	        </div>
	    </div>
	    
	    <div class="tabla_formulario_fila clearfix" style="position:relative;z-index:5;">
	        <div class="tabla_formulario_label recortador">Cuotas:</div>
	
	        <div class="tabla_formulario_input">
	            <select class="selectBox cuotas required" onchange="setCuotas(this)" name="cuotas" id="cuotas" style="width: 140px;"></select>
	        </div>
	    </div>
	</div>
	
	<div class="botonera_paso1">    
	  <h:commandButton styleClass="botonContinuarNaranja"
	  		action="#{recargaController.solicitarRecargaMultitienda}"></h:commandButton>  
	</div>

	</h:form>

</f:view>