<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="f"  uri="http://java.sun.com/jsf/core"%>
<%@ taglib prefix="h"  uri="http://java.sun.com/jsf/html"%>
<f:view>

<script type="text/javascript">
	$(document).ready(function() {
		var esVozBAM = "<h:outputText value="#{profile.flagBam == miEntelBean.siglaUsuarioBAM ? 'Banda Ancha Movil' : 'Telefonia'}"/>";

		dataLayer = dataLayer||[];
		dataLayer.push({
			'mx_content': 'Personas/Mi Entel/Mis Productos/' + esVozBAM + '/Recargas/Web Pay/Pago',
			'event': 'pageview'
		});		
	});
</script>

<h1>Recargas</h1>

<h2 style="padding-left: 0;">Tarjeta de Cr&eacute;dito</h2>

<form name="formulario_webpay" id="formulario_webpay" method="POST" target="iframe_webpay"
	action="${recargaController.pagoWebPay.urlFormAction}">
	<jsp:include page="/token.jsp" flush="true"/>
	<!-- action="http://testjsp2.entelpcs.com/webPay/comunes/recargas/paginaCierreRecargaWebPay.do">  -->       
	<!-- action="http://testjsp2.entelpcs.com/cgi-bin/kcc5/tbk_bp_pago.cgi"> -->
	<!-- action="http://localhost:8080/webPay/comunes/recargas/paginaCierreRecargaWebPay.do"> -->
	<!-- action="${pageContext.request.contextPath}/portlet/webpay/dummy/webpayDummy.portlet"> -->
	
	<input type="hidden" name="TBK_TIPO_TRANSACCION"   value="${recargaController.pagoWebPay.tipoTransaccion}"/>
	<input type="hidden" name="TBK_MONTO"              value="${recargaController.recargaWebPay.TBKMontoRecarga}" />
	<input type="hidden" name="TBK_ORDEN_COMPRA"       value="${recargaController.recargaWebPay.ordenCompra}"/>
	<input type="hidden" name="TBK_ID_SESION"          value="${recargaController.tbkIdSesion}"/>               
	<input type="hidden" name="TBK_URL_RESULTADO"      
			value="${recargaController.pagoWebPay.urlResultado}&ordenCompra=${recargaController.recargaWebPay.ordenCompra}" />
	<input type="hidden" name="TBK_URL_FRACASO"        
			value="${recargaController.pagoWebPay.urlFracaso}&ordenCompra=${recargaController.recargaWebPay.ordenCompra}" />   
	<input type="hidden" name="TBK_NUM_TRX"            value="${recargaController.pagoWebPay.numTrx}" />
	<input type="hidden" name="TBK_CODIGO_TIENDA_M001" value="${recargaController.pagoWebPay.codigoTienda}" />
	<input type="hidden" name="TBK_ORDEN_TIENDA_M001"  value="${recargaController.recargaWebPay.ordenCompra}" />
	<input type="hidden" name="TBK_MONTO_TIENDA_M001"  value="${recargaController.recargaWebPay.TBKMontoRecarga}" />

</form>


<div class="iframe_transbank">
	<iframe id="iframe_webpay" name="iframe_webpay" src="#" frameborder="0" 
		height="700px" width="550px" scrolling="no" ></iframe>
</div>

<h:form id="formFinalizarRecargaWebpay" styleClass="formFinalizarRecargaWebpay" >
	<jsp:include page="/token.jsp" flush="true"/>
	<input type="hidden" name="ordenCompra" value="" />
	<input type="hidden" name="RESULTADO" value="" />
	<input type="hidden" name="TBK_ID_TRANSACCION"  value="" />
	<input type="hidden" name="TBK_ID_SESION"  value="" />

	<h:commandButton id="finalizarRecargaWebpayButton" styleClass="finalizarRecargaWebpayButton"
		action="#{cierreRecargaWebpayController.finalizarRecargaTarjetaCredito}" value="" style="display:none">
	</h:commandButton>


</h:form>

<script type="text/javascript">
	function submitWebpay(){ 
		var theForm = document.getElementById("formulario_webpay");
		theForm.submit();
	}

	submitWebpay();

   /**
	* Este metodo es llamado desde la pagina de cierre de webpay
	* Su proposito es setear los valores que retorna transbank en el formulario
	* de finalizacion de recarga webpay y luego hace submir del formulario
	*/
	function finalizarRecargaWebpay(ordenCompra, resultado, idTransaccion, idSesion) {

	    // Los elementos descritos con tags de JSF ven afectados sus valores de ID
	    // en tiempo de ejecucion, por esta razon, los obtenemos via 'class' de css
		var theForm = document.getElementsByClassName("formFinalizarRecargaWebpay")[0];
		theForm.ordenCompra.value = ordenCompra;
		theForm.RESULTADO.value = resultado;
		theForm.TBK_ID_TRANSACCION.value = idTransaccion;
		theForm.TBK_ID_SESION.value = idSesion;

		document.getElementsByClassName("finalizarRecargaWebpayButton")[0].click();
	}
	
</script>


</f:view>
