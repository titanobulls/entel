<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c"  uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="f"  uri="http://java.sun.com/jsf/core"%>
<%@ taglib prefix="h"  uri="http://java.sun.com/jsf/html"%>
<%@ taglib prefix="it" uri="http://i2b.cl/jsf/iterator/" %>
<%@ taglib prefix="cm" uri="http://www.bea.com/servers/portal/tags/content"%>
<%@ taglib prefix="pref" uri="http://www.bea.com/servers/portal/tags/netuix/preferences"%>
<%@ taglib prefix="render" uri="http://www.bea.com/servers/portal/tags/netuix/render"%>

<render:defineObjects/>

<!-- Preferences -->
<pref:getPreference name="idContenido" var="id_contenido" defaultValue="" />
<pref:getPreference name="tituloCaja" var="titulo_caja" defaultValue="" />
<pref:getPreference name="tipoBloqueLinkUlPackEsencial" var="tipo_pack_esencial" defaultValue="" />

<!-- Contenidos -->
<cm:search id="nodosPromo" query="idContenido = '${id_contenido}'"/>
<cm:search id="titulo" query="idContenido = '${titulo_caja}'" useCache="false" />

<!-- Variables -->
<c:set var="otrasPromo" value="false"/>
<c:set var="promoMcAfee" value="false"/>

<c:forEach items="${nodosPromo}" var="promo">
	<cm:getProperty node="${promo}" name="tipo" resultId="tipo"/>
	<c:if test="${tipo != tipo_pack_esencial}">
		<c:set var="otrasPromo" value="true"/>
	</c:if>
	<c:if test="${tipo == tipo_pack_esencial}">
		<c:set var="promoMcAfee" value="true"/>
	</c:if>
</c:forEach>

<f:view beforePhase="#{bannerSeguridadController.initBannerSeguridad}">
	
	<c:choose>
		<c:when test="${otrasPromo}">
			<div class="cajalinks">
				<div class="cabecera morada">
					<h1><cm:getProperty node="${titulo[0]}" name="titulo" /></h1>
				</div>
				<div class="cuerpo">
					<ul>
						<c:forEach items="${nodosPromo}" var="promo">
							<cm:getProperty node="${promo}" name="tipo" resultId="tipo"/>
							<c:choose>
								<c:when test="${tipo == tipo_pack_esencial}">
									<h:panelGroup rendered="#{bannerSeguridadController.mostrarBannerSeguridad}">
										<li>
											<h:form>
												<jsp:include page="/token.jsp" flush="true"/>
												<h:commandLink styleClass="redirect" actionListener="#{bannerSeguridadController.direccionarToPlanesSeguridad}">
													<cm:getProperty node="${promo}" name="html" />
												</h:commandLink>
											</h:form>
										</li>
									</h:panelGroup>
								</c:when>
								<c:otherwise>
									<cm:getProperty node="${promo}" name="html" />
								</c:otherwise>
							</c:choose>
						</c:forEach>
					</ul>
				</div>
				<div class="pie"></div>
			</div>
		</c:when>
		<c:otherwise>
			<c:if test="${promoMcAfee}">
				<h:panelGroup rendered="#{bannerSeguridadController.mostrarBannerSeguridad}">
					<div class="cajalinks">
						<div class="cabecera morada">
							<h1><cm:getProperty node="${titulo[0]}" name="titulo" /></h1>
						</div>
						<div class="cuerpo">
							<ul>
								<c:forEach items="${nodosPromo}" var="promo">
									<cm:getProperty node="${promo}" name="tipo" resultId="tipo"/>
									<c:if test="${tipo == tipo_pack_esencial}">
										<li>
											<h:form>
												<jsp:include page="/token.jsp" flush="true"/>
												<h:commandLink styleClass="redirect" actionListener="#{bannerSeguridadController.direccionarToPlanesSeguridad}">
													<cm:getProperty node="${promo}" name="html" />
												</h:commandLink>
											</h:form>
										</li>
									</c:if>
								</c:forEach>
							</ul>
						</div>
						<div class="pie"></div>
					</div>
				</h:panelGroup>
			</c:if>
		</c:otherwise>
	</c:choose>
</f:view>