<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="f"  uri="http://java.sun.com/jsf/core"%>
<%@ taglib prefix="h"  uri="http://java.sun.com/jsf/html"%>
<%@ taglib prefix="c"  uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="it" uri="http://i2b.cl/jsf/iterator/" %>
<%@ taglib prefix="r"  uri="http://www.bea.com/servers/portal/tags/netuix/render"%>
<%@ taglib prefix="cm" uri="http://www.bea.com/servers/portal/tags/content" %>
<%@ taglib prefix="entel" uri="/WEB-INF/tld/entel-tags.tld" %>

<cm:search id="infoDebesSaber" query="idContenido = 'debesSaberNumeroFrecuente'" useCache="true"  />

<div class="numeroFrecuentePP">

<h:panelGroup rendered="#{planController.planActualPP != null && !planController.comunik2}">	

					 <it:iterator var="item" value="#{planController.planActualPP.numerosFrecuentes}" rowIndexVar="row" >	
	                  
	                   <h:panelGroup rendered="#{item.idSlot eq slotBean.slotNumeroFrecuente}">
	                   
	                   	<h2 class="movil">N&uacute;mero Frecuente</h2> 
	                  	
						   <h:panelGroup rendered="#{item.numeroFrecuente!= ''}">
								<!-- posee numero frecuente -->	
					
									<div class="numero_frecuente">
										<div class="paso_1 clearfix">
											<div class="numero"><h:outputText value="#{item.numeroFrecuente}"/></div>
						                    <div class="nombre"><h:outputText value="#{item.nombreSlot}"/></div>
											<div class="botonera">
												<a href="#" class="eliminar"><u>Eliminar n&uacute;mero</u></a>
												<a href="#" class="btnAzulDelgado cambiar"><span>Cambiar n&uacute;mero</span></a>
						
											</div>
											<div class="msg"></div>
											<div class="alerta_borrar">
												<span>Vas a eliminar tu N&uacute;mero Frecuente </span>
												<a href="javascript:void(0)" class="btnAzul administrar" onClick="eliminarNumeroFrecuente('2','<h:outputText value="#{item.idSlot}"/>');"><span>Confirmar</span></a>
												<a href="#" class="cancelar"><u>Cancelar</u></a>
												
												<a id="loadingNumeroFrecuente"></a>
											</div>
						
										</div>
										<div class="paso_modificar clearfix">
											<div class="opciones">
												<div class="clearfix">
													<div class="campo_radio">
														<input type="radio" name="tipo" id="movil" checked="checked" />
														<label for="movil">M&oacute;vil EPCS</label>
													</div>
						
													<div class="campo_radio">
														<input type="radio" name="tipo" id="red" />
														<label for="red">Red Fija</label>
													</div>
												</div>
						
						                        
												<div class="clearfix">
													<div class="campo">
														<h:selectOneMenu styleClass="nf_select prefijo">
														<f:selectItems value="#{planController.prefijosRedFija}"/>
														</h:selectOneMenu>
													</div>
													<div class="campo mostrar_globo">
						                            	<div class="contenedor-text flotar_izq">
															<input type="text" maxlength="8" name="numero_frecuente" class="nf_input ampliacionNumerica" onkeypress="return soloNumeros(event);" />
						                                </div>
														<div id="globoError" class="mensaje"><table><tbody><tr><td class="body">El n&uacute;mero es inv&aacute;lido.</td></tr></tbody></table></div>
													</div>
												</div>
											</div>
											<div class="botonera">
												<a href="#" class="cancelar"><u>Cancelar</u></a>
												<a href="javascript:void(0)" class="btnAzul administrar" onClick="administrarNumeroFrecuente('3','<h:outputText value="#{item.idSlot}"/>');"><span>Aceptar</span></a>
												
												<a id="loadingNumeroFrecuente"></a>
											</div>
										</div>
										<!--/paso_modificar-->
					
										
										<div class="paso_nuevo clearfix">
											<strong>El n&uacute;mero ha sido eliminado</strong>
											<a href="#" class="btnAzul flotar_der nuevoNumero"><span>Agregar nuevo n&uacute;mero</span></a>
										</div>
										
										</div>
										
								<!-- / posee numero frecuente -->
							</h:panelGroup>
						
			
							<h:panelGroup rendered="#{item.numeroFrecuente == ''}">
								<!-- no posee numero frecuente -->	
					
									<div class="numero_frecuente">
									
										<div class="paso_1 clearfix" style="display: none">
											<div class="numero"></div>
						                    <div class="nombre"><h:outputText value="#{item.nombreSlot}"></h:outputText></div>
			
											<div class="msg"></div>
		
										</div>
										<div class="paso_modificar clearfix" style="display: none">
											<div class="opciones">
												<div class="clearfix">
													<div class="campo_radio">
														<input type="radio" name="tipo" id="movil" checked="checked" />
														<label for="movil">M&oacute;vil EPCS</label>
													</div>
						
													<div class="campo_radio">
														<input type="radio" name="tipo" id="red" />
														<label for="red">Red Fija</label>
													</div>
						          
												</div>
						
						                        
												<div class="clearfix">
													<div class="campo">
														
														<h:selectOneMenu styleClass="nf_select prefijo">
														<f:selectItems value="#{planController.prefijosRedFija}"/>
														</h:selectOneMenu>
							
													</div>
													<div class="campo mostrar_globo">
						                            	<div class="contenedor-text flotar_izq">
															<input type="text" maxlength="8" name="numero_frecuente" class="nf_input ampliacionNumerica" onkeypress="return soloNumeros(event);" />
						                                </div>
														<div id="globoError" class="mensaje"><table><tbody><tr><td class="body">El n&uacute;mero es inv&aacute;lido.</td></tr></tbody></table></div>
													</div>
												</div>
											</div>
											<div class="botonera">
												<a href="#" class="cancelar"><u>Cancelar</u></a>
												<a href="javascript:void(0)" class="btnAzul administrar" onClick="administrarNumeroFrecuente('1','<h:outputText value="#{item.idSlot}"/>');"><span>Aceptar</span></a>
												
												<a id="loadingNumeroFrecuente"></a>
											</div>
										</div>
										<!--/paso_modificar-->
					
										
										<div class="paso_nuevo clearfix" style="display: block">
											<a href="#" class="btnAzul flotar_der nuevoNumero"><span>Agregar nuevo n&uacute;mero</span></a>
										</div>
										
										</div>
										
								<!-- /no posee numero frecuente -->
							  </h:panelGroup>
							  
							  <cm:getProperty node="${infoDebesSaber[0]}" name="html" />
						</h:panelGroup>
				</it:iterator>
</h:panelGroup>

	
<!--  COMUNIK2 -->		
<h:panelGroup rendered="#{planController.planActualPP != null && planController.comunik2}">
    <h2 class="movil"> N&uacute;mero unido a Comunik2</h2>  
	<it:iterator var="item" value="#{planController.planActualPP.numerosFrecuentes}" rowIndexVar="row" >		
		 <h:panelGroup rendered="#{planController.cantidadNumeroFrecuentes == 1}">		             
			  <h:form id="ingresar" rendered="#{item.numeroFrecuente == ''}">
			  		<jsp:include page="/token.jsp" flush="true"/>
					<div class="numero_frecuente">
						 <div class="caso_ingresar">
							 <div class="clearfix">
							    <span class="titulo_opciones">
								       No tienes un n&uacute;mero unido al plan Comunik2. Si deseas, ingresa uno ahora
							    </span>
							    <div class="formulario_ingresar clearfix">							
								   <div class="campo mostrar_globo">
				                       <h:inputText maxlength="8" id="numero_frecuente"  styleClass="nf_input numero_frecuente"  onkeypress="return soloNumeros(event);"    style="width: 115px;"   value="#{planController.numeroComunik2}" />
								   </div>
								   <div class="ubicador_boton">
									    <h:commandLink id="cambiar_numero_comunik2" styleClass="btnAzul ingresarNumero"  action="#{planController.cambioPlanComunik2PP}"  ><span>Ingresar</span></h:commandLink>											
								   </div>
								</div>
							  </div>
						 </div>								 
					 </div>						   
				   <cm:search id="debes_saber_ingresar" query="idContenido = 'debesSaberComunik2Ingresar'" useCache="true"  />
			       <cm:getProperty node="${debes_saber_ingresar[0]}" name="html" /> 
			       <jsp:include page="../common/messages_table.jsp"></jsp:include>		
				</h:form>				 
		         
   		        <h:panelGroup rendered="#{item.numeroFrecuente != ''}">	 
			    	 <div class="numero_frecuente">		    		
						<div class="caso_exito div_exito">
							 <div class="clearfix">
								  <span class="titulo_opciones">
									   Tu invitaci&oacute;n ha sido enviada al m&oacute;vil <h:outputText value="#{item.numeroFrecuente}" />, la cu&aacute;l estar&aacute; activa durante 24 horas.
								   </span>
									<span class="texto_opciones">
										El n&uacute;mero <h:outputText value="#{item.numeroFrecuente}" /> sigue activo, a la espera de que el usuario del m&oacute;vil reci&eacute;n ingresado acepte la invitaci&oacute;n.
									</span>
							 </div>
						  </div>
						  <div class="caso_no_fondos div_no_fondos">
							   <div class="clearfix">
						            <cm:search id="sin_saldo" query="idContenido = 'sinSaldoComunik2Ingresar'" useCache="true"  />
	                                <cm:getProperty node="${sin_saldo[0]}" name="html" /> 							    
							  </div>
						  </div>
						   <div class="caso_cambiar clearfix">
								<div class="numero"><h:outputText value="#{item.numeroFrecuente}" /></div>
									<div class="botonera">						
										<a href="#" class="btnAzulDelgado cambiarNumero"><span>Cambiar n&uacute;mero</span></a>
									</div>
								<div class="msg"></div>
								<div class="alerta_borrar">
									<span>Vas a eliminar tu N&uacute;mero Frecuente </span>
									<a href="#" class="btnAzul confirmar"><span>Confirmar</span></a>
									<a href="#" class="cancelar">Cancelar</a>
								</div>								
							</div>
							<div class="caso_nuevo">
								<h:form id="cambiar" >
									<jsp:include page="/token.jsp" flush="true"/>
									<div class="clearfix">
										<div class="formulario_ingresar clearfix">
											<div class="campo">									
											</div>
											<div class="campo mostrar_globo">
											    <h:inputText maxlength="8" id="numero_frecuente_cambiar"  styleClass="nf_inputAmarillo numero_frecuente_cambiar" onkeypress="return soloNumeros(event);" style="width: 115px;"   value="#{planController.numeroComunik2}" />
											</div>
											<cm:search id="mensaje_nuevo_numero" query="idContenido = 'nuevoNumeroComunik2'" useCache="true"  />
	                                        <cm:getProperty node="${mensaje_nuevo_numero[0]}" name="html" />                                        																				
											<div class="botonera_caso_nuevo">
											    <h:commandLink id="nuevoEditarNumero" styleClass="btnAzul nuevoEditarNumero" action="#{planController.cambioPlanComunik2PP}"><span>Aceptar</span></h:commandLink>
												<a href="#" class="colorEnlace cancelarEditarNumero">Cancelar</a>
											</div>										
										 </div>
									</div>								
								</h:form>
							</div>
						</div>
						 <jsp:include page="../common/messages_table.jsp"></jsp:include> 					 
					     <cm:search id="debes_saber_editar" query="idContenido = 'debesSaberComunik2Editar'" useCache="true"  />
		                 <cm:getProperty node="${debes_saber_editar[0]}" name="html" /> 						 	                  							
				</h:panelGroup>
			</h:panelGroup>	
		</it:iterator> 	 
 </h:panelGroup>	
<!--  COMUNIK2 -->
 
 </div>