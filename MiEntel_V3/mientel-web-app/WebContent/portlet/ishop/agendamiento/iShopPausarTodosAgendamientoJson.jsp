<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="f" uri="http://java.sun.com/jsf/core"%>
<%@ taglib prefix="h" uri="http://java.sun.com/jsf/html"%>

<f:view beforePhase="#{iShopAgendamientoController.pausarTodosAgendamiento}">
	<h:outputText value="#{iShopAgendamientoController.respuestaJson}"/>	
</f:view>