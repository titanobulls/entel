<%--
  - Author(s): david.roig@i2b.cl
  - Date: 7 Febrero 2011
  - Description: JSP del portlet (PortletMenuStretcher) que renderiza un stretcher desde algun punto 
    del menu dado el numero de saltos en el portlet preference 
--%>
<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.bea.netuix.servlets.controls.page.BookPresentationContext" %>
<%@ page import="com.epcs.portlet.menu.TreeMenuHelper"%>
<%@ taglib uri="http://www.bea.com/servers/portal/tags/netuix/preferences" prefix="pref" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="cm" uri="http://www.bea.com/servers/portal/tags/content" %>
<%@ taglib uri="http://www.bea.com/servers/portal/tags/netuix/render" prefix="render" %>
<%@ taglib uri="/WEB-INF/tld/entel-tags.tld" prefix="entel" %>

<pref:getPreference name="profundidad" var="saltos" defaultValue="3" />

<style>
	a.btnStretcher{
		background-color: #ff6702;
		color: #FFF;
		display: inline-block;
		text-decoration: none;
		padding-top: 10px;
		height: 28px;
		width: 174px;
		text-align: center;
		font-family: Arial;
		font-weight: bold;
	}
	a.btnStretcher:hover{
		background-color: #F37F42;
	}
</style>

<%!
public boolean tieneBooks(java.util.List<Object> lista) {
	for (Object item : lista) {
		if (item instanceof BookPresentationContext) {
			return true;
		}
	}
	return false;
}
%>
<%
	int saltosInt = Integer.parseInt(saltos);
	BookPresentationContext books = 
		TreeMenuHelper.getBookTree((HttpServletRequest)pageContext.getRequest(), saltosInt);
%>

<%--
	indexBookFijo: esta variable se utiliza si se quiere dejar un book siempre abierto 
	de forma permanente, independiente de si esta activo o no.
--%>
<c:set var="indexBookFijo" value="0" />

<c:set var="hijos" value="<%= books.getEntitledPagePresentationContexts() %>" />

<div id="cabecera_mientel">
<table>
<tr>
<td><img src="/personas/framework/skins/mientel/img/cabecera/borde.jpg" /></td>
<td>Mi Entel</td>
</tr>
</table>
</div>

<entel:property name="parametro.pageIshop.ss" var="ss_Ishop_page" />
<entel:property name="parametro.pageIshop.cc" var="cc_Ishop_page" />
<entel:property name="parametro.pageIshop.pp" var="pp_Ishop_page" />

<c:choose>
	<c:when test="${profile.mercado eq miEntelBean.siglaCuentaControlada}">
		<entel:property name="miEntel.mainloginPage.label.cc" var="mainloginPage" />
	</c:when>
	<c:when test="${profile.mercado eq miEntelBean.siglaPrepago}">
		<entel:property name="miEntel.mainloginPage.label.pp" var="mainloginPage" />
	</c:when>
	<c:when test="${profile.mercado eq miEntelBean.siglaSuscripcion}">
		<entel:property name="miEntel.mainloginPage.label.ss" var="mainloginPage" />
	</c:when>
</c:choose>

<c:if test="${saltos < 2}">

<div id="menu_vertical" class="despliegues_categoria azul">
	<div class="stretch_no_amin abierto">
	    <div class="stretch_top"></div>
	    <div class="stretch_main"><h3><a id="link_dashboard" href="<render:pageUrl pageLabel="${mainloginPage}" />">P&aacute;gina de inicio</a></h3></div>
		<div class="stretch_bottom"></div>
	</div>

	<div class="stretch abierto"> 
	    <div class="stretch_top"></div>
	    <div class="stretch_main"><h3><%= books.getTitle() %></h3></div>
		<div class="stretch_bottom"></div>
	</div>
	<div class="contenido_stretcher abierto">
    	<div class="cuerpo_categoria">
    		<ul class="segundo">
    		<c:forEach var="item" items="${hijos}">  		
	    		<c:choose>
			 		<c:when test="${item.displayed}">
			 			<c:set var="seleccionado" value="seleccionado" />
			 			<c:set var="bloque" value="block" />
			 		</c:when>
			 		<c:otherwise>
			 			<c:set var="seleccionado" value="" />
			 			<c:set var="bloque" value="none" />
			 		</c:otherwise>
		 		</c:choose>
				<c:if test="${item.class.simpleName == 'PagePresentationContext'}">
					<!-- (Esa es la actual)  ${item.visible} -->				
					<c:if test="${item.visible}">						
					 	<render:pageUrl pageLabel="${item.label}" var="url">
					 		<render:param name="_nfls" value="false"></render:param>
					 	</render:pageUrl>	
						<li class="${seleccionado}">
							<c:choose>
							    <c:when test="${item.label == ss_Ishop_page || item.label == cc_Ishop_page || item.label == pp_Ishop_page}">									
									 <a href="${url}" target="_blank">${item.title}<img src="../framework/skins/mientel/img/icons/ico_enlace_externo_transp.gif"></img></a>
								</c:when>									
								<c:otherwise>
								   <a href="${url}">${item.title}</a>
								</c:otherwise>
							</c:choose>
						</li> 
					</c:if>		
				</c:if>
			</c:forEach>
    		</ul>
    	</div>
    </div>	
</div>
</c:if>
<c:if test="${saltos >= 2}">
<div id="menu_vertical" class="despliegues_categoria azul">

	<div class="stretch_no_amin abierto">
	    <div class="stretch_top"></div>
	    <div class="stretch_main"><h3><a id="link_dashboard" href="<render:pageUrl pageLabel="${mainloginPage}" />">P&aacute;gina de inicio</a></h3></div>
		<div class="stretch_bottom"></div>
	</div>
	<c:forEach var="item" items="${hijos}" varStatus="loop">
	<c:set var="fijo" value="" />
	
	<!-- Si es un book, es parte del menu(Asi excluimos la home o landings) -->
	<c:if test="${item.class.simpleName == 'BookPresentationContext'}">
	 	<!-- Esta activo el menu  -->
	 	<c:choose>
	 		<c:when test="${item.displayed}">
	 			<c:set var="abierto" value="abierto" />
	 			<c:if test="${loop.index==indexBookFijo}">
	 				<c:set var="fijo" value=" menu_abierto" />
	 			</c:if>
	 		</c:when>
	 		<c:otherwise>
	 			<c:set var="abierto" value="" />
	 		</c:otherwise>
 		</c:choose>
		<div class="stretch ${abierto}${fijo}"> 
		    <div class="stretch_top"></div>
		    <div class="stretch_main"><h3>${item.title}</h3></div>
			<div class="stretch_bottom"></div>
		</div>
		<div class="contenido_stretcher ${abierto}${fijo}">
	    	<div class="cuerpo_categoria">
	    		<ul class="segundo">
					<c:set var="itemChilds" value="${item.entitledPagePresentationContexts}" />
					<c:forEach var="item" items="${itemChilds}">
						<!-- Si es pagina, la agregamos al menu -->
						<!-- Si NO es pagina, es un book, obtenemos los hijos -->
						<!-- Esta activo el menu  -->
						<c:set var="seleccionado" value="" />
					 	<c:choose>
					 		<c:when test="${item.displayed}">
					 			<c:set var="seleccionado" value="seleccionado" />
					 			<c:set var="bloque" value="block" />
					 		</c:when>
					 		<c:when test="${loop.index==indexBookFijo}"><c:set var="bloque" value="block" /></c:when>
					 		<c:otherwise>
					 			<c:set var="bloque" value="none" />
					 		</c:otherwise>
				 		</c:choose>

					 	<c:choose>													
							<c:when test="${item.class.simpleName == 'PagePresentationContext'}">
								<!-- (Esa es la actual)  ${item.visible} -->				
								<c:if test="${item.visible && item.label!='Suscripcion' && item.label!='Cuenta Controlada' && item.label!='Prepago' && item.title != 'Dashboard'}">

									<entel:property name="mientel.menu.url.${item.label}" var="url" />
									<c:if test="${empty url}">
										<render:pageUrl pageLabel="${item.label}" var="url" >
											<render:param name="_nfls" value="false"></render:param>											
										</render:pageUrl>
									</c:if>
									<c:if test="${item.displayed}"><c:set var="noHome" value=" stretch_amin" /></c:if>
									<li class="${seleccionado}${noHome} ">									
									<c:choose>
									    <c:when test="${item.label == ss_Ishop_page || item.label == cc_Ishop_page || item.label == pp_Ishop_page}">
											<a  href="${url}" target="_blank" >${item.title}<img src="../framework/skins/mientel/img/icons/ico_enlace_externo_transp.gif" style="padding-left:10px;"></img></a>
										</c:when>									
										<c:otherwise>
										   <a href="${url}">${item.title}</a>
										</c:otherwise>
									</c:choose>
									</li>
								</c:if>		
							</c:when>
							<c:otherwise>
								<li class="submenu_cabecera ${seleccionado} as"><a href="#">${item.title}</a></li>
								<li class="submenu_contenido ULContainer" style="display: ${bloque};">							
									<c:set var="itemChilds" value="${item.entitledPagePresentationContexts}" />
									<ul class="tercero">
										<c:forEach var="item" items="${itemChilds}">		
											<c:if test="${item.visible && item.label!='Suscripcion' && item.label!='Cuenta Controlada' && item.label!='Prepago' && item.title != 'Dashboard'}">

												<entel:property name="mientel.menu.url.${item.label}" var="url" />
												<c:if test="${empty url}">
													<render:pageUrl pageLabel="${item.label}" var="url">
												 		<render:param name="_nfls" value="false"></render:param>
												 	</render:pageUrl>
												</c:if>
													
												<!-- Esta activo el menu  -->
											 	<c:choose>											 	
											 		<c:when test="${item.displayed}">											 		  
											 			<li class="seleccionado stretch_amin">											 			  
											 			   <c:choose>
															    <c:when test="${item.label == ss_Ishop_page || item.label == cc_Ishop_page || item.label == pp_Ishop_page}">																	
																	 <a href="${url}" target="_blank" style="padding-left:10px;">${item.title}<img style="padding-left:10px;" src="../framework/skins/mientel/img/icons/ico_enlace_externo_transp.gif"></img></a>
																</c:when>									
																<c:otherwise>																 
																    <a href="${url}">${item.title}</a>
																</c:otherwise>
															</c:choose>
											 			</li>											 		 
											 		</c:when>
											 		<c:otherwise>
											 			<li>											 			   
															<c:choose>
															    <c:when test="${item.label == ss_Ishop_page || item.label == cc_Ishop_page || item.label == pp_Ishop_page}">
																	<a  href="${url}&_nfls=false" target="_blank">${item.title}<img style="padding-left:10px;" src="../framework/skins/mientel/img/icons/ico_enlace_externo_transp.gif"></img></a>
																</c:when>									
																<c:otherwise>																 
																   <a href="${url}&_nfls=false">${item.title}</a>
																</c:otherwise>
															</c:choose>
											 			 </li>
											 		</c:otherwise>
										 		</c:choose>
												
											</c:if>				
										</c:forEach>				
									</ul>
								</li>
							</c:otherwise>
						</c:choose>
					</c:forEach>
				</ul>
			</div>
		</div>
	</c:if>
	</c:forEach>
</div>
</c:if>
<div class="cierre_menu"></div>

<br/>
<!-- PageLabel de la nueva seccion de benficios PP Plus -->
<cm:search id="bannerPPPlus" query="idContenido = 'bannerBeneficiosPPPlus'" useCache="true"  />
<cm:getProperty node="${bannerPPPlus[0]}" name="html" resultId="pageLabelBeneficios"/>
<cm:getProperty node="${bannerPPPlus[0]}" name="titulo" resultId="nombreOpcionMenu"/>

<c:if test="${(not empty bannerPPPlus) and (profile.mercado eq miEntelBean.siglaPrepago) and (profile.categoriaCliente eq miEntelBean.siglaPrepagoPlus)}">
	<a class="btnStretcher" href="<render:pageUrl pageLabel="${pageLabelBeneficios[0]}" />"><c:out value="${nombreOpcionMenu[0]}"/></a>
</c:if>