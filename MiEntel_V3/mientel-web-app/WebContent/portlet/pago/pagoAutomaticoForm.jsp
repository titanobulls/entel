<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="f"  uri="http://java.sun.com/jsf/core"%>
<%@ taglib prefix="h"  uri="http://java.sun.com/jsf/html"%>
<script type="text/javascript">

/*****************************************************************/
//ESTA ES LA FUNCION QUE VALIDA EL FORMATO DEL NUMERO DE TARJETA
function EsTarjeta(sNTarjeta, sTipoTarjeta)
{//Funcion que indica si la tarjeta es o no valida
	
	//si sNTarjeta no tiene el largo suficiente
	if (sNTarjeta.length<2) return false;

	//verificar que tipo de tarjeta es.
	switch(sTipoTarjeta) 

	{ 
	case 'VISA': 
		if (sNTarjeta.charAt(0)!=4) return false; 
		if (sNTarjeta.length<16 || sNTarjeta.length>19) return false;
		break; 
	case 'MASTER': 
		if (sNTarjeta.charAt(0)!=5) return false; 
		if (sNTarjeta.length<16 || sNTarjeta.length>19) return false;
		break; 
	case 'AMERICAN': 
		if (sNTarjeta.charAt(0)!=3) return false; 
		if (sNTarjeta.charAt(1)!=7) return false; 
		if (sNTarjeta.length!=15 ) return false;
		break; 
	case 'DINERS': 
		if (sNTarjeta.charAt(0)!=3) return false; 
		if (sNTarjeta.charAt(1)!=6) return false; 
		if (sNTarjeta.length!=14 ) return false;
		break; 
	case 'MAGNA': 
		if (sNTarjeta.charAt(0)!=5) return false; 
		if (sNTarjeta.charAt(1)!=6) return false; 
              if (sNTarjeta.charAt(2)!=8) return false; 
              if (sNTarjeta.charAt(3)!=0) return false; 
		if (sNTarjeta.length!=16 ) return false;
		break;
	}; 

	var MultDigito=0;
	var sumaDigitos=0;
	var iMultiplo=2;
	var sDigito =0;
	
//sumar digitos de numero de tarjeta
	for(i=sNTarjeta.length-2;i>=0;i--)
	{
		if (sNTarjeta.charAt(i)< '0' || sNTarjeta.charAt(i)>'9') return false;
	//alert(i + ' - ' + sNTarjeta.charAt(i));

		MultDigito = sNTarjeta.charAt(i) * iMultiplo;
	
		if (MultDigito>9) 
		{
			sumaDigitos +=parseInt(MultDigito/10);
			sumaDigitos +=MultDigito%10;
		}
		else
			sumaDigitos += MultDigito;
		
		if(iMultiplo==2)
			iMultiplo=1;
		else
			iMultiplo=2;	
	}
	
	sDigito = 10 - (sumaDigitos % 10);

	if (sDigito==10)
		sDigito='0';

	if (sDigito!=sNTarjeta.charAt(sNTarjeta.length-1)) return false;

	return true;	
}

function valida()
{
 
      if($("select[id*=tipo_tarjeta]").val() == 'Seleccione')
        {
         alert('Debe ingresar la Tarjeta de Cr\xe9dito.');
         $("select[id*=tipo_tarjeta]").focus();
         return false;
         }
      else
         {
          if ($("select[id*=tipo_tarjeta]").val() == 'VISA')
             {
             strnombre_tarjeta='VISA';
             }
          if ($("select[id*=tipo_tarjeta]").val() == 'MASTER')
             {
             strnombre_tarjeta='MASTER';
             }
          if ($("select[id*=tipo_tarjeta]").val() == 'AMERICAN')
             {
             strnombre_tarjeta='AMERICAN';
             }   
          if ($("select[id*=tipo_tarjeta]").val() == 'DINERS')
             {
             strnombre_tarjeta='DINERS';
             } 
          if ($("select[id*=tipo_tarjeta]").val() == 'MAGNA')
             {
             strnombre_tarjeta='MAGNA';
             } 
             
         }
         

      if($("input[id*=tarjeta]").val() == '')
        {
         alert('Debe ingresar el N\xfamero de Tarjeta.');
         $("input[id*=tarjeta]").focus();
         return false;
         }
      
      if (isNaN($("input[id*=tarjeta]").val())) 
         { 
         alert('El n\xfamero tarjeta ingresado debe ser num\xfarico');
         $("input[id*=tarjeta]").focus(); 
         return false;    
         }
    
      if(!EsTarjeta($("input[id*=tarjeta]").val(),strnombre_tarjeta))
	{
	alert('El n\xfamero de tarjeta no es v\xe1lido o tipo de tarjeta incorrecto.');
	 $("input[id*=tarjeta]").focus();
	return false;
        }
        
       
      if($("input[id*=chechboxTerminos]").checked==false)
        {
         alert('Para poder finalizar la inscripci�n, debe Aceptar las condiciones expuestas m�s abajo.');
         return false;
         }   
      
   return true;
}

function setTipoTarjeta(){
	$("input[id*=hctarjt]").val($("select[id*=tipo_tarjeta]").val());
}
</script>
<f:view beforePhase="#{pagoAutomaticoController.init}">
<script type="text/javascript">
$(document).ready(function() {
	$("select[id*=tipo_tarjeta]")[0].setValue($("input[id*=hctarjt]").val());
});
function aceptarTerminos(){
	$("input[id*=chechboxTerminos]").attr('checked',true);
	$(".checkbox").css("background-position","0 -50px");
}
</script>
<h1>Pago autom&aacute;tico</h1>

			<p>Entel facilita el pago de tu cuenta a trav&eacute;s de estas dos modalidades de pago autom&aacute;tico.</p><br />
			
			<div id="pago-automatico-paso1" class="clearfix" style="display:block;">
				<h2 class="sin_icono">Paso 1 de 3</h2>
					<p><strong>Los datos de mi tarjeta </strong><span class="obligatorio">(Todos los campos son obligatorios)</span></p><br />
				<h:form id="formulario_pat" styleClass="formulario_pat" onsubmit="return validar()" title="formPagoAutomatico">
					<jsp:include page="/token.jsp" flush="true"/>
					<div class="formulario">

						<div class="formulario_item clearfix">
							<div class="formulario_label formulario_label_pat">Nombre del titular:
								<div class="formulario_label_pat_descripcion">El nombre que tiene impreso la tarjeta</div>
							</div>
							<div class="formulario_input ancho_input_cargar mostrar_globo">
							    <h:inputText required="true" requiredMessage="Ingresa el nombre del Titular" value="#{pagoAutomaticoController.pagoAutomaticoBean.titular}" id="titular" onkeypress="return soloAlfabetico(event);" styleClass="inputBox required titular" maxlength="30" style="width:170px" title="nombre" />
							</div>
						</div>
						<div class="formulario_item clearfix">

							<div class="formulario_label formulario_label_pat">Rut del titular:</div>
							<div class="formulario_input ancho_input_cargar mostrar_globo">
							<h:inputText required="true" requiredMessage="Ingresa el Rut del Titular" value="#{pagoAutomaticoController.pagoAutomaticoBean.rut}"  id="rut" onkeypress="return soloRut(event);" styleClass="inputBox required rut" maxlength="12" style="width:170px" title="rut"/>
							</div>
						</div>									
						<div class="formulario_item clearfix">
							<div class="formulario_label formulario_label_pat">E-mail:
								<div class="formulario_label_pat_descripcion">Para confirmaciones y relacionados</div>
							</div>

							<div class="formulario_input ancho_input_cargar mostrar_globo">
							<h:inputText required="true" requiredMessage="Ingresa un email" value="#{pagoAutomaticoController.pagoAutomaticoBean.email}" id="email" styleClass="inputBox required emailpat" style="width:170px" title="email"/>
							</div>
						</div>
						<div class="formulario_item clearfix">
							<div class="formulario_label formulario_label_pat">Tel&eacute;fono:
								<div class="formulario_label_pat_descripcion">Para confirmaci&oacute;n de datos</div>
							</div>

							<div class="formulario_input ancho_input_cargar">
								<div class="input_pat_telefono input_pat_area mostrar_globo">
                                    <!-- /RGUZMAN -->
                                    <h:selectOneMenu style="width:50px;" styleClass="codigo_tel_adicional selectBox" value="#{pagoAutomaticoController.pagoAutomaticoBean.telefonoArea}" title="indTelefono">
		        						<f:selectItems value="#{pagoAutomaticoController.indicativosTelefono}"/>
		        					</h:selectOneMenu>
                                    <!-- / 
                                    <h:inputText required="true" requiredMessage="Ingresa el Area del Telefono" value="#{pagoAutomaticoController.pagoAutomaticoBean.telefonoArea}" id="telefono_area" onkeypress="return soloNumeros(event);"  styleClass="inputBox required digits" maxlength="3" style="width:35px"/>
                                    -->
								</div>
								<div class="input_pat_telefono mostrar_globo">
                                    <h:inputText required="true" requiredMessage="Ingresa un Telefono" value="#{pagoAutomaticoController.pagoAutomaticoBean.telefono}" id="telefono" onkeypress="return soloNumeros(event);" styleClass="inputBox required digits" maxlength="8" style="width:129px" title="telefono"/>
								</div>
							</div>
						</div>

						<div class="formulario_item clearfix">
							<div class="formulario_label formulario_label_pat">Tipo de tajeta:</div>
							<div class="formulario_input ancho_input_cargar mostrar_globo">
							<h:inputHidden id="hctarjt" value="#{pagoAutomaticoController.pagoAutomaticoBean.tipoTarjeta}"/>
							<h:selectOneMenu onchange="setTipoTarjeta()" id="tipo_tarjeta" styleClass="tipo-tarjeta required min" style="width:170px;" title="tipoTarjeta">
							<f:selectItems value="#{pagoAutomaticoController.tiposTarjetas}"/>
							</h:selectOneMenu>
							</div>
						</div>
						<div class="formulario_item clearfix">
							<div class="formulario_label formulario_label_pat">N&uacute;mero de tarjeta:
								<div class="formulario_label_pat_descripcion">Ingresa todos los n&uacute;meros impresos</div>

							</div>
							<div class="formulario_input ancho_input_cargar mostrar_globo">
							    <h:inputText required="true" requiredMessage="Ingresa el Numero de la Tarjeta" value="#{pagoAutomaticoController.pagoAutomaticoBean.numeroTarjeta}" id="tarjeta" onkeypress="return soloNumeros(event);" styleClass="inputBox required digits tarjeta" maxlength="16" style="width:170px" title="tarjeta"/>
							</div>
						</div>
						<div class="formulario_item clearfix">
							<div class="formulario_label formulario_label_pat">
								<div class="formulario_label_pat_descripcion">
								<h:outputLink value="autorizacionCargo.jsp" styleClass="thickbox enlace_terminos"><u>Acepto las condiciones para inscribir cargo en tarjeta de Cr&eacute;dito operada por Transbank S.A.</u></h:outputLink>
								
                                </div>

							</div>
							<div class="formulario_input ancho_input_cargar mostrar_globo">
								<div class="clearfix">
								    <h:selectBooleanCheckbox required="true" validator="#{pagoAutomaticoController.validateTerminos}" requiredMessage="No haz Aceptado los Terminos" value="#{pagoAutomaticoController.aceptaTerminos}" styleClass="styled chechboxTerminos" id="chechboxTerminos" title="condiciones"/>
								</div>
							</div>
						</div>														
						<div class="formulario_item clearfix">
							<div class="formulario_label formulario_label_pat">&nbsp;</div>

							<div class="formulario_input ancho_input_cargar clearfix">
							<h:commandButton styleClass="boton-azul-confirmar" onclick="return valida();" action="#{pagoAutomaticoController.confirmarPAT}"/>
							<h:commandLink styleClass="Cancelar" immediate="true" action="patcancelar">Cancelar</h:commandLink>
							</div>
						</div>							
					</div>
				
				</h:form>
			</div>							
	<div class="contenedor-mensajes-verde clearfix">
	   <div></div>
	   	<h:messages id="messageList" 
			styleClass="mensajes-lista"
			errorClass="mensaje-error" 
			infoClass="mensaje-informacion" showSummary="true" />
	</div>
	
</f:view>

