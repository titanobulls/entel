<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="f"  uri="http://java.sun.com/jsf/core"%>
<%@ taglib prefix="h"  uri="http://java.sun.com/jsf/html"%>
<%@ taglib prefix="c"  uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://www.bea.com/servers/portal/tags/content" prefix="cm"%>
<f:view>

<script type="text/javascript">
	// Variable Global para configurar el nuevo AAA
	var aaa;
	function setAAA() {
		$("input[id*=nuevoAAA]").val(aaa);
		}
</script>

<h:form>
<jsp:include page="/token.jsp" flush="true"/>
<c:choose>
<c:when test="${misUsuariosController.userAdmin}">
  <h:inputHidden id="nuevoAAA" value="#{misUsuariosController.nuevoAaa}"/>
<div id="centro">
					<h1>Mis Usuarios</h1>

					<p>Registra a los usuarios de tu cuenta o actualiza sus datos personales. Adem&aacute;s controla a qu&eacute; servicios le das acceso.</p>
					
					<!--CONTENIDO MIS USUARIOS-->
					<div class="mis-usuarios-contenido clearfix">
                    
                    	<!--BLOQUE USUARIO-->
						<div class="mis-usuarios-bloque ico_rojo">
							<h6 class="clearfix">
								<span>Titular de la Cuenta</span>
								<a class="ico_interrogacionNuevo autoTooltip" href="#ttUsuarioTitular" ></a>
							</h6>
							<p>Administra todos los servicios de todos los m&oacute;viles de la cuenta.</p>
						</div>
                        <!--/BLOQUE USUARIO-->
						
                        <!--BLOQUE USUARIO-->
						<div class="mis-usuarios-bloque ico_verde">
							<h6 class="clearfix">
								<span>Usuario control Total</span>
								<a class="ico_interrogacionNuevo autoTooltip" href="#ttUsuarioControlTotal" ></a>
							</h6>
							<p>Administra todos los servicios de su propio m&oacute;vil.</p>
						</div>
                        <!--/BLOQUE USUARIO-->
						
                        <!--BLOQUE USUARIO-->
						<div class="mis-usuarios-bloque ico_azul">
							<h6 class="clearfix">
								<span>Usuario control Parcial</span>
								<a class="ico_interrogacionNuevo autoTooltip" href="#ttUsuarioControlParcial" ></a>	
							</h6>
							<p>Administra algunos servicios de su propio m&oacute;vil. </p>
						</div>
                        <!--/BLOQUE USUARIO-->
						
                        <!--BLOQUE USUARIO-->
						<div class="mis-usuarios-bloque ico_amarillo">
							<h6 class="clearfix">
								<span>Usuario solo Consulta</span>
								<a class="ico_interrogacionNuevo autoTooltip" href="#ttUsuarioSoloConsulta" ></a>	
							</h6>
							<p class="fixIE6Relative">Consulta informaci&oacute;n del estado de servicios de su propio m&oacute;vil.</p>
						</div>
                        <!--/BLOQUE USUARIO-->
                        
					</div>
                    <!--/CONTENIDO MIS USUARIOS-->
					
					
					<!-- TABLA DE PERMISOS -->
					<div id="permisos">
						<div class="tabla-top clearfix">
							<div class="top"></div>
							<div class="main">
								<table>
									<tr>
										<th width="100">Nombre usuario</th>
										<th width="60">Nro. PCS</th>
										<th width="200">Permiso</th>
										<th class="ultimo">Ver / Modificar informaci&oacute;n</th>
									</tr>
								</table>					
							</div>
							<div class="bottom"></div>
						</div>
						<div class="tabla-permisos-contenido">
						
							<table class="contabla">
								<tbody>
                                
                                	<!--ITEM LISTA USUARIOS-->
                                	<c:forEach items="#{misUsuariosController.usuarios}" var="usuario" varStatus="status">
                                      <c:set var="trclass" value="${status.index % 2 eq 0 ? 'even' : 'odd'}"></c:set>                                	
									   <tr class="${trclass}">
										<c:choose>
											<c:when test="${empty usuario.rut}">
												<td width="100">
													No Registrado
												</td>										    		
											</c:when>
											<c:when test="${!empty usuario.rut}">
												<td width="100">
													<h:outputText value="#{usuario.primerNombre}"/>&nbsp;
													<h:outputText value="#{usuario.segundoNombre}"/>&nbsp;
													<h:outputText value="#{usuario.apellidoPaterno}"/>&nbsp;
													<h:outputText value="#{usuario.apellidoMaterno}"/>
												</td>
											</c:when>
									    </c:choose>
										<td width="60"><h:outputText value="#{usuario.numeroPCS}"/></td>
										<td width="200">
										<c:choose>
										
											<c:when test="${usuario.aaa eq miEntelBean.AAAConsultar}">
   											<div class="tabla-permisos-eventos">
                                       		<div class="tabla-permisos-estado clearfix">
                                            
                                            	<!--ESTADO PERMISOS-->
												<div class="estado">
													<span class="icono ico_amarillo"></span>
													<span>Solo Consulta</span>											
												</div>
                                                <!--/ESTADO PERMISOS-->
												
												<div class="tabla-permisos-modificar">
													<a style="text-decoration: underline;" href="#">Modificar</a>
												</div>	
																							
											</div>
											
                                            <!--MENU ESTADO-->
											<div class="tabla-permisos-estado-menu clearfix">
												<div class="estado-opcion menu_consulta">
													<a href="#" class="clearfix" onclick="aaa=0;return cambiarEstado(this, 'consulta');">
														<span class="icono ico_amarillo"></span>
														<span>Solo Consulta</span>
													</a>
												</div>
											
												<div class="estado-opcion menu_total">
													<a href="#" class="clearfix" onclick="aaa=2;return cambiarEstado(this, 'total');">
														<span class="icono ico_verde"></span>
														<span>Control Total</span>
													</a>
												</div>
												
												<div class="estado-opcion menu_parcial">
													<a href="#" class="clearfix" onclick="aaa=1;return cambiarEstado(this, 'parcial');">
														<span class="icono ico_azul"></span>
														<span>Control Parcial</span>
													</a>
												</div>
											</div>
                                            <!--/MENU ESTADO-->
											
											<!--ESTADO CONFIRMAR-->
                                            <div class="estado-confirmar ico_azul_b">
												<span>Usted va a cambiar los permisos de este usuario de <span class="prev">Solo consulta</span> a <strong>Control parcial</strong>.</span>
												<div class="botonera_cambiar clearfix">
													<h:commandLink onclick="setAAA()" action="#{misUsuariosController.actualizarAAA}" styleClass="btnAzul"><span>Cambiar</span>
													 <f:param name="nroPcs" value="#{usuario.numeroPCS}" />
                                                     <f:param name="rut" value="#{usuario.rut}" />
                                                     <f:param name="idp" value="#{misUsuariosController.aaaUsuarioSesion}" />
                                                    </h:commandLink>
												
													<a href="#" class="btnCancelar">Cancelar</a>
												</div>
											</div>
                                            <!--/ESTADO CONFIRMAR-->
                                            
										</div>
									 </c:when>
									 <c:when test="${usuario.aaa eq miEntelBean.AAAControlParcial}">
									   <div class="tabla-permisos-eventos">
                                        
                                       		<div class="tabla-permisos-estado clearfix">
                                            
                                            	<!--ESTADO PERMISOS-->
												<div class="estado">
													<span class="icono ico_azul"></span>
													<span>Control Parcial</span>											
												</div>
                                                <!--/ESTADO PERMISOS-->
												
												<div class="tabla-permisos-modificar">
													<a style="text-decoration: underline;" href="#">Modificar</a>
												</div>	
																							
											</div>
											
                                            <!--MENU ESTADO-->
											<div class="tabla-permisos-estado-menu clearfix">
												<div class="estado-opcion menu-consulta">
													<a href="#" class="clearfix" onclick="aaa=0;return cambiarEstado(this, 'consulta');">
														<span class="icono ico_amarillo"></span>
														<span>Solo Consulta</span>
													</a>
												</div>
											
												<div class="estado-opcion menu_total">
													<a href="#" class="clearfix" onclick="aaa=2;return cambiarEstado(this, 'total');">
														<span class="icono ico_verde"></span>
														<span>Control Total</span>
													</a>
												</div>
												
											</div>
                                            <!--/MENU ESTADO-->
											
											<!--ESTADO CONFIRMAR-->
                                            <div class="estado-confirmar ico_azul_b">
												<span>Usted va a cambiar los permisos de este usuario de <span class="prev">Solo consulta</span> a <strong>Control parcial</strong>.</span>
												<div class="botonera_cambiar clearfix">
												
													<h:commandLink styleClass="btnAzul" action="#{misUsuariosController.actualizarAAA}" onclick="setAAA()"><span>Cambiar</span>
													 <f:param name="nroPcs" value="#{usuario.numeroPCS}" />
                                                     <f:param name="rut" value="#{usuario.rut}" />
                                                     <f:param name="idp" value="#{misUsuariosController.aaaUsuarioSesion}" />
                                                     </h:commandLink>
													
													<a href="#" class="btnCancelar">Cancelar</a>
												</div>
											</div>
                                            <!--/ESTADO CONFIRMAR-->
                                            
										</div>
									 </c:when>
								<c:when test="${usuario.aaa eq miEntelBean.AAAControlTotal}">
									    <div class="tabla-permisos-eventos">
                                        
                                       		<div class="tabla-permisos-estado clearfix">
                                            
                                            	<!--ESTADO PERMISOS-->
												<div class="estado">
													<span class="icono ico_verde"></span>
													<span>Control Total</span>											
												</div>
                                                <!--/ESTADO PERMISOS-->
												
												<div class="tabla-permisos-modificar">
													<a style="text-decoration: underline;" href="#">Modificar</a>
												</div>	
																							
											</div>
											
                                            <!--MENU ESTADO-->
											<div class="tabla-permisos-estado-menu clearfix">
												<div class="estado-opcion menu-consulta">
													<a href="#" class="clearfix" onclick="aaa=0;return cambiarEstado(this, 'consulta');">
														<span class="icono ico_amarillo"></span>
														<span>Solo Consulta</span>
													</a>
												</div>
											
    										<div class="estado-opcion menu_parcial">
													<a href="#" class="clearfix" onclick="aaa=1;return cambiarEstado(this, 'parcial');">
														<span class="icono ico_azul"></span>
														<span>Control Parcial</span>
													</a>
												</div>
											</div>
                                            <!--/MENU ESTADO-->
											
											<!--ESTADO CONFIRMAR-->
                                            <div class="estado-confirmar ico_azul_b">
												<span>Usted va a cambiar los permisos de este usuario de <span class="prev">Control Total</span> a <strong>Control parcial</strong>.</span>
												<div class="botonera_cambiar clearfix">
												  	 <h:commandLink onclick="setAAA();" action="#{misUsuariosController.actualizarAAA}" styleClass="btnAzul"><span>Cambiar</span>
													 <f:param name="nroPcs" value="#{usuario.numeroPCS}" />
                                                     <f:param name="rut" value="#{usuario.rut}" />
                                                     <f:param name="idp" value="#{misUsuariosController.aaaUsuarioSesion}" />
                                                     </h:commandLink>
													<a href="#" class="btnCancelar">Cancelar</a>
												</div>
											</div>
                                            <!--/ESTADO CONFIRMAR-->
                                            
										</div>
									 </c:when>				
										<c:when test="${usuario.aaa eq miEntelBean.AAATitular}">
   										    <div class="tabla-permisos-eventos">
										    <div class="tabla-permisos-estado clearfix">
										    <div class="estado">
											<span class="icono ico_rojo"></span>
											<span>Titular</span>											
										    </div>
										    </div>
										    </div>
									   </c:when>
									  <c:when test="${usuario.aaa eq -1}">
   										   <div class="tabla-permisos-eventos">
												<div class="tabla-permisos-estado clearfix">
													<div class="estado">
														<span class="icono"></span>
														<span>No asignado</span>											
													</div>
												</div>
											</div>
									   </c:when>
									     </c:choose>
										</td>
										<td align="right">
										<!-- Cambiar 3 por aaa en sesion -->										
										<c:choose>																						
											<c:when test="${empty usuario.rut && misUsuariosController.userAdmin}">
										    	<h:commandLink action="#{misUsuariosController.redirectFormIngresoRut}" styleClass="btnAzulDelgado"><span>Registrar Usuario</span>
										    		<f:param name="nroPCS" value="#{usuario.numeroPCS}" />
										    	</h:commandLink>										    		
											</c:when>
											<c:when test="${(misUsuariosController.userAdmin || usuario.rut eq misUsuariosController.rutUsuarioSeleccionado) && !(empty usuario.rut) }">
									    		<h:commandLink styleClass="btnAzulDelgado" action="#{cuentaController.mostrarDatosUsuario}"><span>Ver / Modificar</span>
									         		<f:param name="nroPcs" value="#{usuario.numeroPCS}" />
                                                 	<f:param name="rut" value="#{usuario.rut}" />
                                                 	<f:param name="idp" value="#{misUsuariosController.aaaUsuarioSesion}" />
                                            	</h:commandLink>
											</c:when>
										</c:choose>
										</td>
									</tr>
									</c:forEach>
									<!--/ITEM LISTA USUARIOS-->
                                                                                               
								</tbody>
							</table>
							
						</div>
                        <!-- /TABLA DE PERMISOS -->
                        
					</div>
                   
				</div>
</c:when>
<c:otherwise>	
<cm:search id="infoRestriccionAAA0" query="idContenido = 'infoRestriccionAAA0'" useCache="false"/>
<div class="contenedor-mensajes">
	<ul>
		<li class="mensaje-alerta">
			<div align="center"> 
				<span><cm:getProperty node="${infoRestriccionAAA0[0]}" name="html"/></span>
			</div> 
		</li>
	</ul>
 </div>
</c:otherwise>
</c:choose>
</h:form>	
				
		<cm:search id="urlTabla" query="idContenido = 'urlTablaServiciosPorPerfil'" useCache="false"  />
		
		<!-- CONTENIDO TOOLTIPS -->
		<div id="ttUsuarioTitular" style="display:none">
		    <cm:search id="nodo" query="idContenido = 'ttUsuarioTitular'" useCache="false"  />
			<strong><cm:getProperty node="${nodo[0]}" name="titulo" /></strong>
			<p><cm:getProperty node="${nodo[0]}" name="html"/></p>
		</div>

		<div id="ttUsuarioControlTotal" style="display:none">
            <cm:search id="nodo" query="idContenido = 'ttUsuarioControlTotal'" useCache="false"  />
			<strong><cm:getProperty node="${nodo[0]}" name="titulo" /></strong>
			<p><cm:getProperty node="${nodo[0]}" name="html"/></p>
	    </div>

		<div id="ttUsuarioControlParcial" style="display:none">
		    <cm:search id="nodo" query="idContenido = 'ttUsuarioControlParcial'" useCache="false"  />
			<strong><cm:getProperty node="${nodo[0]}" name="titulo"/></strong>
			<p><cm:getProperty node="${nodo[0]}" name="html"/></p>
		</div>

		<div id="ttUsuarioSoloConsulta" style="display:none">
		    <cm:search id="nodo" query="idContenido = 'ttUsuarioSoloConsulta'" useCache="false"  />
			<strong><cm:getProperty node="${nodo[0]}" name="titulo"/></strong>
			<p><cm:getProperty node="${nodo[0]}" name="html"/></p>
		</div>
			
</f:view>
