<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="f"  uri="http://java.sun.com/jsf/core"%>
<%@ taglib prefix="h"  uri="http://java.sun.com/jsf/html"%>
<%@ taglib prefix="c"  uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="cm" uri="http://www.bea.com/servers/portal/tags/content" %>
<%@ taglib prefix="pref" uri="http://www.bea.com/servers/portal/tags/netuix/preferences" %>

<pref:getPreference name="idContenido" var="idContenido" defaultValue="" />
<c:set var="query">idContenido = '${idContenido}'</c:set>
<cm:search id="accesoDashboard" query="${query}" useCache="false"/>

<f:view>

	<div <cm:getProperty node="${accesoDashboard[0]}" name="html"/> >

		<!-- MENSAJES -->
		<jsp:include page="../common/messages_table.jsp"></jsp:include>

		<div id="top-select">
			<div class="consultando clearfix"><label>Estás consultando el número: </label>

			<c:if test="${profile.aaa == miEntelBean.AAATitular}" var="isUserAdmin">
				<script type="text/javascript">
					
					$(document).ready(function() {
						$("#top-select select").selectorCuenta();
						//$('.sel-estas-consultando')[0].setValue('<h:outputText value="#{selectorCuentaController.currentMsisdn}"></h:outputText>');
						$('.sel-estas-consultando')[0].setValue($('#currentMsisdn').val());
					});
			    </script>
			    
			    <input id="currentMsisdn" type="hidden" value="<c:out value="${profile.numeroPcsSeleccionado}"></c:out>"/>

				<div class="select_contenedor">
					<h:form>
						<jsp:include page="/token.jsp" flush="true"/>
						<h:selectOneMenu enabledClass="sel-estas-consultando" onchange="this.onchange = null;submit();"
							style="width: 265px;" styleClass="sel-estas-consultando" id="sel-estas-consultando"
							valueChangeListener="#{selectorCuentaController.cambiarCuenta}">
							<f:selectItems value="#{selectorCuentaController.msisdnAsociadoList}" />
						</h:selectOneMenu>
					</h:form>
				</div>
			</c:if>
			<c:if test="${!isUserAdmin}">
				<label><h:outputText value="#{selectorCuentaController.currentMsisdn}"></h:outputText></label>
			</c:if>

			</div>
		</div>

	</div>

</f:view>