<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="f"  uri="http://java.sun.com/jsf/core"%>
<%@ taglib prefix="h"  uri="http://java.sun.com/jsf/html"%>
<%@ taglib prefix="c"  uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="it" uri="http://i2b.cl/jsf/iterator/" %>
<%@ taglib prefix="r"  uri="http://www.bea.com/servers/portal/tags/netuix/render"%>
<%@ taglib uri="http://www.bea.com/servers/portal/tags/content" prefix="cm"%>

<f:view beforePhase="#{planController.consultarSolicitudComunik2}">

 <h:panelGroup rendered="#{planController.solicitudComunik2Bean!=null}">         
     <h:panelGroup rendered="#{planController.solicitudComunik2Bean.solicitudRecibida}">	
			<div class="mensaje_invitado_comunik2">
				<div class="titulo_resaltado">
					El n&uacute;mero <h:outputText value="#{planController.solicitudComunik2Bean.msisdnEnvia}"/> te ha invitado a unirte al plan Cominik2.
					<span>
					   <h:outputLink value="condicionesComunik2.faces" styleClass="pinPukBox">Ver detalles y condiciones del plan aqu&iacute;.</h:outputLink>
					</span>
				</div>
			</div>
   </h:panelGroup> 
  <h2 class="icon_comunik2_solicitud" style="border-bottom: 1px solid #CDDAE7">Solicitudes activas Tarifa Comunik2</h2>
     <div class="tabla_invitado_comunik2 clearfix">
	    <div class="linea-azul"></div>				
	      <table class="tabla-azul correccion_margen">
	        <tbody>
		      <tr class="cabecera">
		         <th width="67px">N&deg; EPCS</th>
                 <th width="109px">Invitaci&oacute;n</th>
                 <th width="70%">Vencimiento</th>
		      </tr>							
			 <!-- fila edicion -->						
		   <h:panelGroup rendered="#{planController.solicitudComunik2Bean.solicitudRecibida}">			       
			  <h:form>
			  	<jsp:include page="/token.jsp" flush="true"/>		       		
				 <tr class="edicion">
                    <td colspan="3" class="correccion_padding">	                        	
					   <div class="clearfix">
					     <div class="formulario_ingresar clearfix">																		
						    <div class="mensaje_numero_tabla">
						  	   Vas a rechazar la invitaci&oacute;n del m&oacute;vil <h:outputText value="#{planController.solicitudComunik2Bean.msisdnEnvia}"/> y no podr&aacute;s hablar gratis con esa persona. &iquest;Est&aacute;s seguro?
						    </div>										
						    <div class="botonera_caso_nuevo">
							    <h:commandLink  id="rechazar_solicitud" styleClass="btnAzul eliminar_numero aceptar_rechazar2" actionListener="#{planController.administrarSolicitudComunik2}"  ><span>Aceptar</span>
							    </h:commandLink>											
								<a class="colorEnlace cancelar_rechazar" href="#">Cancelar</a>
						    </div>
						  </div>
					    </div>								
                     </td>
                 </tr>
	             <!-- /fila edicion -->						
				 <!-- fila normal -->
			     <tr class="edicion_previa impar">
			         <td class="correccion_padding">
			           <strong><h:outputText value="#{planController.solicitudComunik2Bean.msisdnEnvia}"/></strong>
			         </td>
			         <td class="correccion_padding">
			           <strong>Recibida</strong>		
			         </td>
			         <td class="correccion_padding">
			            <div class="dato_tabla">
			               <strong>
			                   <h:outputText value="#{planController.solicitudComunik2Bean.fechaSolicitud}"> 
							     <f:convertDateTime pattern="dd/MM/yyyy - hh:mm" locale="es" />
						      </h:outputText>
							   hrs	
			               </strong>
					    </div>
						<div class="botones_tabla">
				            <a class="btnAzul cambiar_edicion" href="#"><span>Aceptar</span></a>											
						    <a href="#" class="eliminar colorEnlace rechazar_edicion">Rechazar</a>		
						</div>
			         </td>
			      </tr>
		          <!-- /fila normal -->				      			
				  <!-- fila edicion -->
				  <tr class="edicion_aceptada">
	                 <td colspan="3" class="correccion_padding">	                        	
						<div class="clearfix">
					    	<div class="formulario_ingresar clearfix">																		
								<div class="mensaje_numero_tabla">
									Vas a aceptar la invitaci&oacute;n del m&oacute;vil <h:outputText value="#{planController.solicitudComunik2Bean.msisdnEnvia}"/>. Ahora tu plan ser&aacute; Comunik2. &iquest;Est&aacute;s seguro?
								</div>										
							     <div class="botonera_caso_nuevo">
										     <h:commandLink  id="aceptar_solictud" styleClass="btnAzul eliminar_numero" actionListener="#{planController.administrarSolicitudComunik2}" ><span>Aceptar</span>										      
										     </h:commandLink>											
											<a class="colorEnlace cancelar_aceptar cancelar_aceptar" href="#">Cancelar</a>
								  </div>
							 </div>
					      </div>								
	                  </td>
	               </tr>
	             </h:form>      
	         </h:panelGroup>      
                  <!-- /fila edicion -->
              <h:panelGroup rendered="#{!planController.solicitudComunik2Bean.solicitudRecibida}">         
			      <tr class="par">
	                 <td>
	                    <h:outputText value="#{planController.solicitudComunik2Bean.msisdnRecibe}"/>
	                 </td>
	                 <td>
	                     Enviada
                     </td>
                     <td>
		                 <div class="dato_tabla">
		                     <h:outputText value="#{planController.solicitudComunik2Bean.fechaSolicitud}"> 
						        <f:convertDateTime pattern="dd / MMMM / yyyy - hh:mm" locale="es" />
						     </h:outputText>
						        hrs	                            
						 </div>
					     <div class="mensaje_tabla">
							<span class="icon-pendiente">En espera</span>
						 </div>
                     </td>
                   </tr>
                </h:panelGroup>        
	         </tbody>
	     </table>	
	 </div>  
	 
	 <jsp:include page="../common/messages_table.jsp"></jsp:include>
	 <cm:search id="debes_saber" query="idContenido = 'debesSaberSolicitudComunik2'" useCache="true"  />
	 <cm:getProperty node="${debes_saber[0]}" name="html" />
</h:panelGroup>    
</f:view>