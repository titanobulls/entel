<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="f"  uri="http://java.sun.com/jsf/core"%>
<%@ taglib prefix="h"  uri="http://java.sun.com/jsf/html"%>
<f:view beforePhase="#{ofertaBlindajeController.controlOfertas}">
<h:outputText value="#{ofertaBlindajeController.respuestaJson}"/>
</f:view>
