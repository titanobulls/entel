<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib uri="http://www.bea.com/servers/portal/tags/content" prefix="cm"%>
<div class="cajalinks">
<cm:search id="nodo" query="idContenido = 'db_terecomendamos'" useCache="false"  />
					<div class="cabecera morada">
						<h1><cm:getProperty node="${nodo[0]}" name="titulo" /></h1>
					</div>
					<div class="cuerpo">
						<cm:getProperty node="${nodo[0]}" name="html" />
					</div>
					<div class="pie"></div>

				</div>
