<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="f" uri="http://java.sun.com/jsf/core"%>
<%@ taglib prefix="h" uri="http://java.sun.com/jsf/html"%>
<%@ taglib prefix="pref" uri="http://www.bea.com/servers/portal/tags/netuix/preferences" %>

<pref:getPreference name="windowOpenRegistroFM" var="paramsUrlRegistro" />
<pref:getPreference name="windowOpenAccederFM" var="paramsUrlAcceder" />
<pref:getPreference name="urlTarifasFullMessenger" var="urlTarifas" />
<pref:getPreference name="urlMasInfoFullMessenger" var="urlMasInfo" />
<pref:getPreference name="urlAccederFullMessenger" var="urlAccederFM" />


<f:view beforePhase="#{fullMessengerController.initParamsFullMessenger}">
	<div class="ishop">
	
	    <h1>Full Messenger</h1>
	
	    <p>
	    	Full Messenger es una aplicaci&oacute;n que te permite enviar mensajes de texto a m&oacute;viles Entel 
	    	desde la comodidad de tu computador y conectarte al Chat MSN Messenger. Adem&aacute;s, podr&aacute;s 
	    	enviar mensajes desde tu m&oacute;vil a los computadores de tus amigos.
	    </p><br />
	    
	    <form id="Form1" method="post" action="${urlAccederFM}">
	    	<jsp:include page="/token.jsp" flush="true"/>
	    	<p>Para acceder directamente al servicio debes primero 
		    	<a href="#" class="enlace_ishop" onclick="window.open(${paramsUrlRegistro})">registrarte en &eacute;l.</a>
		    </p><br />
		    
		    <a class="enlace_ishop enlaceExterno" href="#" onclick="enviar(${paramsUrlAcceder})"><strong>Acceder al servicio</strong></a>
			<a class="enlace_ishop enlaceExterno" target="_blank" href="${urlTarifas}"><strong>Tarifas</strong></a>
			<a class="enlace_ishop enlaceExterno" target="_blank" href="${urlMasInfo}"><strong>M&aacute;s informaci&oacute;n</strong></a>
		    
		    <input type="hidden" name="u" id="u" value="<h:outputText value="#{fullMessengerController.numeroPcsEncriptado}" />" />
    		<input type="hidden" name="p" id="p" value="<h:outputText value="#{fullMessengerController.claveEncriptada}" />" />		
		</form>
		
		<script language="javascript">
			function enviar(url,name,params){
				var popWindow = window.open(url,name,params);
			    document.forms['Form1'].target="fullMsn";
			    document.forms['Form1'].submit();
			}
		</script>
	
	    <div class="bannerFullMsn">&nbsp;</div>
	
	</div>
	
</f:view>