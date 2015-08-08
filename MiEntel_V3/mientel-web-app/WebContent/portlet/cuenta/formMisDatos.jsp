<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="f"  uri="http://java.sun.com/jsf/core"%>
<%@ taglib prefix="h"  uri="http://java.sun.com/jsf/html"%>
<%@ taglib prefix="c"  uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="it" uri="http://i2b.cl/jsf/iterator/" %>
<%@ taglib prefix="r"  uri="http://www.bea.com/servers/portal/tags/netuix/render"%>
<%@ taglib prefix="cm" uri="http://www.bea.com/servers/portal/tags/content"%>

<cm:search id="msjBoletaInscrita" query="idContenido = 'msjBoletaInscrita'" useCache="true" />
<cm:search id="msjBoletaNoInscrita" query="idContenido = 'msjBoletaNoInscrita'" useCache="true" />
<cm:search id="msjCheckBoletaElectronica" query="idContenido = 'msjCheckBoletaElectronica'" useCache="true" />

<f:view beforePhase="#{cuentaController.initCheckBoletaElectronica}">

	<script type="text/javascript">
		function fillSelects(selectid){ 

			// Vector para saber cu�l es el siguiente combo a llenar
			var combos = new Array();
			combos['regionpromociones'] = "ciudadpromociones"; 
			combos['ciudadpromociones'] = "comunapromociones"; 
			// Tomo el nombre del combo al que se le a dado el clic por ejemplo: pa�s 
			
			posicion = selectid.indexOf('regionpromociones') != -1 ? 'regionpromociones' : 'ciudadpromociones';
			 
			// Tomo el valor de la opci�n seleccionada 
			valor = $("select[id*="+posicion+"]").val(); 
		
			  
			// Evalu� que si es pa�s y el valor es 0, vaci� los combos de estado y ciudad
			if(posicion == 'regionpromociones' && valor == "0"){ 
				  $("select[id*=ciudadpromociones]").html(' <option value="0" selected="selected">Seleccione</option>');
			      $("select[id*=comunapromociones]").html(' <option value="0" selected="selected">Seleccione</option>');
			      $("select[id*=ciudadpromociones]")[0].setValue('0');
			      $("select[id*=comunapromociones]")[0].setValue('0');
			} else { 
				/* En caso contrario agregado el letreo de cargando a el combo siguiente 
				 Ejemplo: Si seleccione pa�s voy a tener que el siguiente seg�n mi vector combos es: estado por qu� combos [pa�s] = estado
				*/ 
				$("select[id*="+combos[posicion]+"]").html('<option value="0">Cargando...</option>');
				$("select[id*="+combos[posicion]+"]")[0].setValue('0');
				$("select[id*=comunapromociones]").html('<option value="0" selected="selected"></option>');
				$("select[id*=comunapromociones]")[0].setValue('0');
				/* Verificamos si el valor seleccionado es diferente de 0 y si el combo es diferente de ciudad, esto porque no tendr�a caso hacer la consulta a ciudad porque no existe un combo dependiente de este */
				if(valor != "0") { 
					// Llamamos a pagina de donde ejecuto las consultas para llenar los combos
					var combo = $("select[id*="+posicion+"]").attr("name"); // Nombre del combo 
					var id =  $("select[id*="+posicion+"]").val();
					  
					var reg = $("select[id*=regionpromociones]").val();
					  
					var url = '<%= request.getContextPath()%>/ServletAreas';
				      //var url = '<%= request.getContextPath()%>/fillSelectAjax.faces';
					$.ajax({
				    	type: 'POST',
				        url: url,
				        dataType: 'json',
				        data : 'combo='+combo+'&id='+id+'&reg='+reg+'&r='+Math.random(),
				        success: function (opt){
							var optionsselect = "";
							//var opt = eval("(" + data + ")");
							optionsselect = '<option value="0">Seleccione</option>';
							for(var i = 0; i< opt.length;i++){
			                	optionsselect += '<option value="'+opt[i].codigo+'">'+opt[i].descripcion+'</option>';
							}
							     
							$("select[id*="+combos[posicion]+"]").html(optionsselect); //Tomo el resultado de pagina e inserto los datos en el combo indicado
							$("select[id*="+combos[posicion]+"]")[0].setValue('0');
						}
					});
				}
			}
		}
		
		
		function setAreas(){
			$("input[id*=hcpromociones]").val($("select[id*=ciudadpromociones]").val());
			$("input[id*=hcompromociones]").val($("select[id*=comunapromociones]").val());
		}
		
		function upper(txt) {
			var txtUpper = $(txt).val().toUpperCase();
			$(txt).val(txtUpper);	
		}
	</script>

	<script type="text/javascript">
	$(document).ready(function() {
		$("select[id*=regionpromociones]")[0].setValue('<h:outputText value="#{cuentaController.usuario.direccionContacto.region.codigo}"/>');

		$("input[id*=boletaElectronica]").attr('checked', false);
		$("input[id*=boletaElectronica]").click(function() {
			if ($('h2.icono-email a.modificar').hasClass('activo')) {
				$('.mis-datos-guardar-be').hide();
				if ($(this).is(':checked')) {
					$('.mis-datos-guardar-be').hide();
				}
			} else {
				if ($(this).is(':checked')) {
					$('.mis-datos-guardar-be').show();
				} else {
					$('.mis-datos-guardar-be').hide();
				}
			}			
		});
		
		 if ($('.mensaje-informacion').length) {
			dataLayer = dataLayer||[];
			dataLayer.push({
				'mx_content': 'Personas/Mi Entel/Mi Cuenta/Mis Datos/Guardar Cambios',
				'event': 'pageview'
			});	           
	     } else {
	    	dataLayer = dataLayer||[];
			dataLayer.push({
				'mx_content': 'Personas/Mi Entel/Mi Cuenta/Mis Datos',
				'event': 'pageview'
			});		     
	     }
	});
	</script>

	<script type="text/javascript">
		$(window).resize(function() {
			var iLeft = $('input[id*=fecha_nacimiento]').offset().left;
			$('#ui-datepicker-div').css({
				left: iLeft + 'px'
			});
		});
	</script>
					
	<h1>Mis Datos</h1>
	
	<!-- MENSAJES -->
	<jsp:include page="../common/messages_table.jsp"></jsp:include>
	
	<h:panelGroup rendered="#{cuentaController.activarCambiarUsuario && cuentaController.usuario != null}">
		<!-- CAMBIAR USUARIO NPCS -->
		<h:form id="cambioUsuarioNPCS">
			<jsp:include page="/token.jsp" flush="true"/>
		<div class="mensaje-exito formCambioUsuario">
	        <div class="clearfix sub-contenedor">
	            <div class="texto cambiarUsuario">
		            <strong>
		            	� Deseas asignar este n&uacute;mero m&oacute;vil a otro usuario ?
		            </strong>
	            </div>
	            <div>
					<h:commandLink value="" action="#{cuentaController.redirectFormIngresoRut}"	
					    styleClass="btnAzulGrande btnAzulLargo"><span>Modificar Usuario</span>
					<f:param name="nroPCS" value="#{cuentaController.usuario.numeroPCS}" />    
					</h:commandLink>
				</div>
	        </div>
		</div>
		</h:form>
		<!-- /CAMBIAR USUARIO NPCS -->
	</h:panelGroup>
	
<h:panelGroup rendered="#{cuentaController.usuario != null}">
	<!-- DATOS PERSONALES -->
	<div class="mis-datos-info misDatos_datos">
		<h:form id="formulario-datos-personales" styleClass="formDatosPersonales">
			<jsp:include page="/token.jsp" flush="true"/>
		<h2 class="icono-personas-blanco"><a style="text-decoration: underline;" href="#" class="modificar" onclick="mxTracker._trackPageview('Personas/Mi Entel/Mi Cuenta/Mis Datos/Modificar');">Modificar</a> Datos personales</h2>
            <!-- MIS DATOS FILA -->
			<div class="mis-datos-fila clearfix">
				<label>Nombres:</label>
				<div class="campo">
					<div class="campo-amarillo oculto">
						<span class="left"></span><h:inputText id="nombre_1" styleClass="nombre_1" value="#{cuentaController.usuario.primerNombre}" required="true" requiredMessage="Ingresa un Nombre" maxlength="20" onblur="upper(this);" onkeyup="upper(this);" onkeypress="return soloLetras(event);" style="width:150px;" /><span class="right"></span>
					</div>
					<strong><h:outputText styleClass="nombre_1" value="#{cuentaController.usuario.primerNombre}"/></strong>
				</div>
				<div class="campo">
					<div class="campo-amarillo oculto">
						<span class="left"></span><h:inputText id="nombre_2" styleClass="nombre_2" value="#{cuentaController.usuario.segundoNombre}" maxlength="20" onblur="upper(this);" onkeyup="upper(this);" onkeypress="return soloLetras(event);" style="width:150px;" /><span class="right"></span>
					</div>
					<strong><h:outputText value="#{cuentaController.usuario.segundoNombre}"/></strong>
				</div>
			</div>
            <!-- /MIS DATOS FILA -->

            <!-- MIS DATOS FILA -->
			<div class="mis-datos-fila clearfix">
				<label>Apellidos:</label>
				<div class="campo">
					<div class="campo-amarillo oculto">
						<span class="left"></span><h:inputText value="#{cuentaController.usuario.apellidoPaterno}" required="true" requiredMessage="Ingresa Tu Primer Apellido" id="apellido_1" styleClass="apellido_1" maxlength="20" onblur="upper(this);" onkeyup="upper(this);" onkeypress="return soloLetras(event);" style="width:150px;" /><span class="right"></span>
					</div>
					<strong><h:outputText value="#{cuentaController.usuario.apellidoPaterno}"/></strong>
				</div>
				<div class="campo">
					<div class="campo-amarillo oculto">
						<span class="left"></span><h:inputText value="#{cuentaController.usuario.apellidoMaterno}" required="true" requiredMessage="Ingres Tu Segundo Apellido" id="apellido_2" styleClass="apellido_2" maxlength="20" onblur="upper(this);" onkeyup="upper(this);" onkeypress="return soloLetras(event);" style="width:150px;" /><span class="right"></span>
					</div>
					<strong><h:outputText value="#{cuentaController.usuario.apellidoMaterno}"/></strong>
				</div>
			</div>
            <!-- /MIS DATOS FILA -->
            
            <!-- MIS DATOS FILA -->
			<div class="mis-datos-fila clearfix">
				<label>RUT:</label>
				<div class="campo">
					<div class="campo-amarillo checklist oculto"><span><h:outputText value="#{cuentaController.usuario.rut}" converter="rutConverter" /></span></div>
					<strong><h:outputText value="#{cuentaController.usuario.rut}" converter="rutConverter" /></strong>
				</div>
			</div>
            <!-- /MIS DATOS FILA -->

            <!-- MIS DATOS FILA -->
			<div class="mis-datos-fila clearfix">
				<label>N&uacute;mero:</label>
				<div class="campo">
					<div class="campo-amarillo checklist oculto"><span><h:outputText value="#{cuentaController.usuario.numeroPCS}"/></span></div>
					<strong><h:outputText value="#{cuentaController.usuario.numeroPCS}"/></strong>
				</div>
			</div>
            <!-- /MIS DATOS FILA -->

            <!-- MIS DATOS FILA -->	
			<div class="mis-datos-fila clearfix"><label>Fecha de Nacimiento:</label><div class="campo"><div class="campo-amarillo checklist oculto"><span class="left"></span><h:inputText id="fecha_nacimiento" value="#{cuentaController.usuario.fechaNacimiento}" styleClass="fecha_nacimiento" maxlength="10" style="width:90px;float:left;display:block;"><f:convertDateTime pattern="dd/MM/yyyy" type="date"/></h:inputText><span id="sr-fecha_nacimiento" class="right"></span></div><strong><h:outputText value="#{cuentaController.usuario.fechaNacimiento}"><f:convertDateTime pattern="dd/MM/yyyy" type="date"/></h:outputText></strong></div></div>
            <!-- /MIS DATOS FILA -->
			
            <!-- MIS DATOS GUARDAR -->
			<div class="mis-datos-guardar">
				<h:commandLink value="" action="#{cuentaController.actualizarDatos}" 
					styleClass="btnAzulGrande btnAzulLargo" ><span>Guardar Cambios</span></h:commandLink>
			</div>
            <!-- /MIS DATOS GUARDAR -->

		</h:form>
	</div>
	<!-- /DATOS PERSONALES -->
	
	<!-- EMAIL -->
	<div class="mis-datos-info misDatos_datos">
		<h:form id="formulario-datos-email" styleClass="formDatosEmail">
			<jsp:include page="/token.jsp" flush="true"/>
			<h2 class="icono-email"><a style="text-decoration: underline;" href="#" class="modificar" onclick="mxTracker._trackPageview('Personas/Mi Entel/Mi Cuenta/Mis Datos/Modificar');">Modificar</a>Email</h2>
			
			<!-- EMAIL PROMOCIONES -->
			<div id="email-promociones">
			
                <!-- MIS DATOS FILA -->
				<div class="mis-datos-fila clearfix">
					<label>Email:</label>
					<div class="campo">
						<div class="campo-amarillo oculto">
							<span class="left"></span>
							<h:inputText id="email" styleClass="email" value="#{cuentaController.usuario.email}" required="true"  
									requiredMessage="Ingresa Tu email" maxlength="50" style="width:240px;" >
								<f:validator validatorId="validateEmail"/>
							</h:inputText>
							<span class="right"></span>
						</div>
						<strong><h:outputText value="#{cuentaController.usuario.email}"/></strong>
					</div>
				</div>
                <!-- /MIS DATOS FILA -->
				
                <!-- MIS DATOS FILA -->
				<div class="mis-datos-fila clearfix emailConfirmacion">
					<label>Confirmaci&oacute;n Email:</label>
					<div class="campo">
						<div class="campo-amarillo oculto">
							<span class="left"></span>
							<h:inputText id="email2" styleClass="email2" value="#{cuentaController.usuario.email}" required="true" 
									requiredMessage="Reingresa Tu email" maxlength="50" style="width:240px;">
								<f:validator validatorId="validateEmail"/>
							</h:inputText>
							<span class="right"></span>
						</div>
						<strong><h:outputText value="#{cuentaController.usuario.email}"/></strong>
					</div>
				</div>
                <!-- MIS DATOS FILA -->
				
                <!-- MIS DATOS GUARDAR -->
				<div class="mis-datos-guardar">
				    <h:commandLink value="" action="#{cuentaController.actualizarDatos}" styleClass="btnAzulGrande btnAzulLargo"><span>Guardar Cambios</span></h:commandLink>
				</div>
                <!-- /MIS DATOS GUARDAR -->
                
                <!-- CHECK BOLETA ELECTRONICA -->
				<h:panelGroup rendered="#{cuentaController.mostrarCheckBoletaElectronica}">
					<div class="mis-datos-fila clearfix" style="padding-top: 45px">
						<h:panelGroup rendered="#{cuentaController.inscritoBoletaElectronica}">							
							<cm:getProperty node="${msjBoletaInscrita[0]}" name="html" />							
						</h:panelGroup>
						<h:panelGroup rendered="#{!cuentaController.inscritoBoletaElectronica}">
							<div style="float: left">
								<h:selectBooleanCheckbox id="boletaElectronica" value="#{cuentaController.suscripcionBoletaElectronica}" />
							</div>
							<div style="padding-left: 20px">								
								<p><cm:getProperty node="${msjCheckBoletaElectronica[0]}" name="html" /><h:outputLink value="msjTerminosCondiciones.faces" styleClass="thickbox">t&eacute;rminos y condiciones.</h:outputLink></p>
								<cm:getProperty node="${msjBoletaNoInscrita[0]}" name="html" />								
							</div>
							<!-- MIS DATOS GUARDAR -->
							<div class="mis-datos-guardar-be" style="display: none; padding: 15px 0 0 196px;">
				    			<h:commandLink value="" action="#{cuentaController.actualizarDatos}" styleClass="btnAzulGrande btnAzulLargo"><span>Guardar Cambios</span></h:commandLink>
							</div>
                			<!-- /MIS DATOS GUARDAR -->
						</h:panelGroup>
					</div>          						
				</h:panelGroup>
				<!-- /CHECK BOLETA ELECTRONICA -->
				
			</div>
            <!-- /EMAIL PROMOCIONES -->
			
			<!-- EMAIL FACTURA -->
			<div id="email-factura" class="oculto">
			  <h:panelGroup rendered="#{cuentaController.panelAdmin}">
				<div class="mis-datos-fila clearfix">
					<div class="campo">
						<div class="campo-amarillo oculto">
							<!--  <span>Para modificar este correo debes hacerlo en <a href="<h:outputText value="#{cuentaController.urlFacturacionElectronica}"/>" class="linkEmailFactura">Administraci&oacute;n Factura Electr&oacute;nica</a></span> -->
						   <span>Para modificar este correo debes hacerlo en <a class="linkEmailFactura" href="<r:pageUrl pageLabel='${cuentaController.pageLabelFacturaElectronica}'></r:pageUrl>">Administraci&oacute;n Factura Electr&oacute;nica</a></span>
						</div>
					</div>
				</div>
			
                <!-- MIS DATOS FILA -->
				<div class="mis-datos-fila clearfix">
					<p>Email: 
						<h:panelGroup rendered="#{!cuentaController.renderEmailFacturaElectronica}">
							<span>Para modificar este correo debes hacerlo en <a class="linkEmailFactura" href="<r:pageUrl pageLabel='${cuentaController.pageLabelFacturaElectronica}'></r:pageUrl>">Administraci&oacute;n Factura Electr&oacute;nica</a></span>										
						</h:panelGroup>
					</p>
					<div class="campo">
						<div class="campo-amarillo oculto">
							<strong><h:outputText value="#{cuentaController.usuario.emailFacturaElectronica}"/></strong>
						</div>
						<strong><h:outputText value="#{cuentaController.usuario.emailFacturaElectronica}"/></strong>	
					</div>
				</div>				
                <!-- /MIS DATOS FILA -->
               </h:panelGroup>        
               
               <h:panelGroup rendered="#{!cuentaController.panelAdmin}">
		           <h:outputText value="#{cuentaController.mensajeUsuarioNoAutorizado}" styleClass="mensaje-error"></h:outputText>
               </h:panelGroup>
               
                    
			</div>
			<!-- /EMAIL FACTURA -->
		
		</h:form>
	</div>
	<!-- /EMAIL -->
	
	<!-- DIRECCION -->
	<div class="mis-datos-info misDatos_datos">
		<h2 class="icono-casa">Direcci&oacute;n </h2>
		
		<ul class="tabs clearfix">
			<li class="seleccionado"><a href="#direccion-promociones">Promociones y contacto</a></li>
			<h:panelGroup rendered="#{cuentaController.panelSSCC}">
				<li class="direccion_factura"><a href="#direccionFactura">Direcci&oacute;n postal boleta</a></li>
			</h:panelGroup>			
			<li class="linkModificar"><a href="#" class="modificar" onclick="mxTracker._trackPageview('Personas/Mi Entel/Mi Cuenta/Mis Datos/Modificar');">Modificar</a></li>			
		</ul>
		
		<!-- DIRECCION PROMOCIONES -->
		<div id="direccion-promociones">
			<h:form styleClass="formulario-direccion-promociones" id="formDireccionPromociones">
                <jsp:include page="/token.jsp" flush="true"/>      
                <!-- MIS DATOS FILA -->
				<div class="mis-datos-fila clearfix">
					<label>Regi&oacute;n:</label>
					<div class="campo">
						<div class="campo-amarillo oculto">
							<h:selectOneMenu onchange="fillSelects(this.id)" 
						 		id="regionpromociones"
						 		value="#{cuentaController.usuario.direccionContacto.region.codigo}" 
						 		styleClass="selectBoxAmarillo region-promociones" style="width:250px;">
                                     <f:selectItems value="#{cuentaController.regionesList}"/>
                        	</h:selectOneMenu>
						</div>
						<strong><h:outputText value="#{cuentaController.usuario.direccionContacto.region.descripcion}"/></strong>
					</div>
				</div>
                <!-- MIS DATOS FILA -->

				<!-- MIS DATOS FILA -->
				<div class="mis-datos-fila clearfix">
					<label>Ciudad:</label>
					<div class="campo">
						<div class="campo-amarillo oculto">
							<select onchange="fillSelects(this.id)"
								id="ciudadpromociones" 
								class="selectBoxAmarillo" style="width:250px;">
								<it:iterator var="item" value="#{cuentaController.ciudadesList}">
								    <option value="<h:outputText value="#{item.codigo}"/>"><h:outputText value="#{item.codigo}"/></option>
								</it:iterator>
                            </select>
                            <h:inputHidden id="hcpromociones" value="#{cuentaController.usuario.direccionContacto.ciudad.codigo}"/> 
						</div>
						<strong><h:outputText value="#{cuentaController.usuario.direccionContacto.ciudad.codigo}"/></strong>
					</div>
				</div>
                <!-- /MIS DATOS FILA -->
				
                <!-- MIS DATOS FILA -->
				<div class="mis-datos-fila clearfix">
					<label>Comuna:</label>
					<div class="campo">
						<div class="campo-amarillo oculto">
							<select onchange="setAreas()"
								id="comunapromociones"
								class="selectBoxAmarillo" style="width:250px;">
								<it:iterator var="itemc" value="#{cuentaController.comunasList}">
								    <option value="<h:outputText value="#{itemc.codigo}"/>"><h:outputText value="#{itemc.codigo}"/></option>
								</it:iterator>
                             </select>
                            <h:inputHidden id="hcompromociones" value="#{cuentaController.usuario.direccionContacto.comuna.codigo}"/> 
						</div>
						<strong><h:outputText value="#{cuentaController.usuario.direccionContacto.comuna.codigo}"/></strong>
					</div>
				</div>
                <!-- /MIS DATOS FILA -->
				
                <!-- MIS DATOS FILA -->
				<div class="mis-datos-fila clearfix">
					<label>Direcci&oacute;n:</label>
					<div class="campo">
						<div class="campo-amarillo oculto">
							<span class="left"></span><h:inputText value="#{cuentaController.usuario.direccionContacto.calle}" styleClass="calle" id="calle" maxlength="30" style="width:240px" onblur="upper(this);" onkeyup="upper(this);" /><span class="right"></span>
						</div>
						<strong><h:outputText value="#{cuentaController.usuario.direccionContacto.calle}"/></strong>
					</div>
				</div>
                <!-- /MIS DATOS FILA -->
				
                <!-- MIS DATOS FILA -->
				<div class="mis-datos-fila clearfix">
					<label>Nro:</label>
					<div class="campo">
						<div class="campo-amarillo oculto">
							<span class="left"></span><h:inputText id="numero" styleClass="numero" maxlength="30" style="width:50px" onkeypress="return soloNumeros(event);" value="#{cuentaController.usuario.direccionContacto.numero}" /><span class="right"></span>
						</div>
						<strong><h:outputText value="#{cuentaController.usuario.direccionContacto.numero}"/></strong>
					</div>
					<div class="campo">
						<span class="text">Dpto / Casa / Otro:</span>
						<div class="campo-amarillo oculto">
							<span class="left"></span><h:inputText value="#{cuentaController.usuario.direccionContacto.departamento}" maxlength="30" style="width:50px" /><span class="right"></span>
						</div>
						<strong><h:outputText value="#{cuentaController.usuario.direccionContacto.departamento}"/></strong>
					</div>
				</div>
                <!-- /MIS DATOS FILA -->
				
                <!-- MIS DATOS GUARDAR -->
				<div class="mis-datos-guardar">
				    <h:commandLink  value="" action="#{cuentaController.actualizarDatos}" styleClass="btnAzulGrande btnAzulLargo" ><span>Guardar Cambios</span></h:commandLink>
				</div>
                <!-- /MIS DATOS GUARDAR -->
		
			</h:form>
		</div>
        <!-- /DIRECCION PROMOCIONES -->
		
		<!-- DIRECION FACTURA -->
		<div id="direccionFactura" class="oculto">
		<h:panelGroup rendered="#{cuentaController.panelAdmin}">
			<h:form styleClass="formDireccionFactura" id="formDireccionFactura">
                <jsp:include page="/token.jsp" flush="true"/>      
				<!-- MIS DATOS FILA -->
				<div class="mis-datos-fila clearfix">
					<label>Direcci&oacute;n:</label>
					<div class="campo">
						<div class="campo-amarillo oculto">
							<span class="left"></span>
							<h:inputText id="direccion" value="#{cuentaController.usuario.direccionFactura.direccionCompleta}" maxlength="150" style="width:240px" onblur="upper(this);" onkeyup="upper(this);" /><span class="right"></span>
						</div>
						<strong><h:outputText value="#{cuentaController.usuario.direccionFactura.direccionCompleta}"/></strong>
					</div>
				</div>
                <!-- /MIS DATOS FILA -->
				
                <!-- MIS DATOS FILA -->
				<div class="mis-datos-fila clearfix">
					<label>Comuna:</label>
					<div class="campo">
						<div class="campo-amarillo oculto">
						<h:selectOneMenu value="#{cuentaController.usuario.direccionFactura.comuna.codigo}" styleClass="selectBoxAmarillo" style="width:250px;">
                           <f:selectItems value="#{cuentaController.comunasListDirFactura}"/>
                         </h:selectOneMenu>
						</div>
						<strong><h:outputText value="#{cuentaController.usuario.direccionFactura.comuna.codigo}"/></strong>
					</div>
				</div>
                <!-- /MIS DATOS FILA -->
				
                <!-- MIS DATOS GUARDAR -->
				<div class="mis-datos-guardar">
				    <h:commandLink value="" action="#{cuentaController.actualizarDatos}"	styleClass="btnAzulGrande btnAzulLargo" ><span>Guardar Cambios</span></h:commandLink>
			    </div>
                <!-- /MIS DATOS GUARDAR -->
                            
			</h:form>
			</h:panelGroup>
			
			 <h:panelGroup rendered="#{!cuentaController.panelAdmin}">
		     	<h:outputText value="#{cuentaController.mensajeUsuarioNoAutorizado}" styleClass="mensaje-error"></h:outputText>
             </h:panelGroup>
			
			
		</div>
		<!-- DIRECION FACTURA -->
	
	</div>
	
	</h:panelGroup>
</f:view>
