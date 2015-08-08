<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="f" uri="http://java.sun.com/jsf/core"%>
<%@ taglib prefix="h" uri="http://java.sun.com/jsf/html"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="entel" uri="/WEB-INF/tld/entel-tags.tld" %>
<%@ taglib uri="http://www.bea.com/servers/portal/tags/content" prefix="cm"%>
<f:view>
<script>
 $(document).ready(function() {
    $('.cargarOfertaVisa').click();
 });
</script>
<!-- MENSAJES -->
<h:form>
	<jsp:include page="/token.jsp" flush="true"/>
   <h:commandLink  styleClass="cargarOfertaVisa" actionListener="#{ofertaBlindajeController.cargarTipoOfertaVisa}"></h:commandLink>
</h:form>
	
</f:view>