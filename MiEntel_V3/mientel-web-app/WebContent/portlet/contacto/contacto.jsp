<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="f" uri="http://java.sun.com/jsf/core"%>
<%@ taglib prefix="h" uri="http://java.sun.com/jsf/html"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="pref" uri="http://www.bea.com/servers/portal/tags/netuix/preferences" %>
<%@ taglib uri="http://www.bea.com/servers/portal/tags/netuix/render" prefix="render"%>

<pref:getPreference name="verLinkPortabilidad" var="verLinkPortabilidad" defaultValue="1"/>

<f:view beforePhase="#{formularioContactoController.verificaLinkFormContacto}">

<script type="text/javascript">
	$(document).ready(function(){
		var param = obtenerParametroURL("contexto");
		if (param == 'contactanos') {
			$('.mail').trigger("click");
		}
	});
</script>

<div class="contactanosBanner"></div>
<h1>Cont&aacute;ctanos</h1>
<p>Queremos escucharte, comunicate con nosotros por estos medios disponibles para ti.</p>
	 
<div>
	<div style="float:left; width:50%; ">
		<h1 style="background:none;color:#064687;padding-bottom:0px;font-size:2em">Por Internet</h1>
	<div class="contactanosItem clearfix">
			<div class="icono" style="margin-right:5px">
				<img style="width:25px; height:20px; left:50%; position:relative; margin-left: -12px; top:50%;margin-top:-10px; " src="<%=request.getContextPath()%>/portlet/contacto/img/mail.png" />
			</div>
		<h:form>
				<jsp:include page="/token.jsp" flush="true"/>
				<h:commandLink action="#{formularioContactoController.formularioContactoForm}" style="color: #63656A; font: 12px 'Trebuchet ms',Arial; width:240px;"><p>Sugerencias y Consultas</p></h:commandLink>
		</h:form>
	</div> 
		<div class="contactanosItem clearfix" id="linkContactoReclamo">
			<div class="icono" style="margin-right:5px">
				<img style="width:25px; height:20px; left:50%; position:relative; margin-left: -12px; top:50%;margin-top:-10px; " src="<%=request.getContextPath()%>/portlet/contacto/img/mail.png" />
			</div>			
			<div ><a href="<render:pageUrl pageLabel='${formularioContactoController.pageLabelReclamos}'/>" style="color: #63656A; font: 12px 'Trebuchet ms',Arial; width:120px;" >Reclamos</a></div>
		</div>
	</div>
	<div style="float:left; width:50%; ">
		<h1 style="background:none;color:#064687;padding-bottom:0px;font-size:2em">Asistida</h1>
		<div class="contactanosItem clearfix">
			<div class="icono" style="margin-right:5px">
				<img style="width:15px; height:25px; left:50%; position:relative; margin-left: -7px; top:50%;margin-top:-12px; " src="<%=request.getContextPath()%>/portlet/contacto/img/cel-movil.png" />
			</div>
			<a href="#" style="color: #63656A; font: 12px 'Trebuchet ms',Arial;text-decoration: none; width:240px;">
				<p>Desde tu celular al 103</p>
			</a>				
		</div>		
		<div class="contactanosItem clearfix">
			<div class="icono" style="margin-right:5px">
				<img style="width:25px; height:25px; left:50%; position:relative; margin-left: -13px; top:50%;margin-top:-12px; " src="<%=request.getContextPath()%>/portlet/contacto/img/tel-movil.png" />
			</div>
			<a href="#" style="color: #63656A; font: 12px 'Trebuchet ms',Arial;text-decoration: none; width:260px;">
				<p>Desde tu t&eacute;lefono fijo al 600 3600 103</p>
			</a>				
		</div>

</div>
</div>
<div style="clear:both;padding-bottom:30px"></div>

<!--		<c:if test="${verLinkPortabilidad != 0}">-->
<!--			<div class="contactanosItem clearfix">-->
<!--				<div class="icono iconoMail"></div>-->
<!--				<h:form>-->
<!--					<h:commandLink styleClass="consultaPortabilidad" action="#{formularioContactoPortabilidadController.formularioContactoPortabilidadForm}"><p>Consulta Portabilidad Num&eacute;rica</p></h:commandLink>-->
<!--				</h:form>-->
<!--			</div> -->
<!--		</c:if>-->
	 



<!-- 
<h:panelGroup rendered="#{formularioContactoController.mostrarLinkFormContacto}">
	<div class="contactanosItem clearfix">
		<div class="icono iconoMail"></div>
		<p>Email: Estamos trabajando en un formulario de contacto online, por ahora encuentra ayuda o realiza tu comentarios a tráves de nuestras redes con atención existentes</p>
	</div>
</h:panelGroup>
-->
<div class="contactanosItem clearfix">
	<div class="icono" style="margin-right:5px">
		<img style="width:25px; height:25px; left:50%; position:relative; margin-left: -12px; top:50%;margin-top:-12px; " src="<%=request.getContextPath()%>/portlet/contacto/img/twitter.png" />
	</div>
	<a href="http://twitter.com/entel_ayuda" target="_blank" style="color: #63656A;	font: 12px 'Trebuchet ms',Arial;text-decoration: none; width:140px;">
		<p>@entel_ ayuda
		<img src="<%=request.getContextPath()%>/portlet/contacto/img/ico-link-azul.png" />
		</p>
	</a>
</div>

<div class="contactanosItem clearfix">
	<div class="icono" style="margin-right:5px">
		<img style="width:30px; height:20px; left:50%; position:relative; margin-left: -15px; top:50%;margin-top:-10px; " src="<%=request.getContextPath()%>/portlet/contacto/img/msm.png" />
	</div>
	<a href="http://comunidad.entel.cl/forums?order=title&asc=true" target="_blank" style="color: #63656A; font: 8px 'Trebuchet ms',Arial;text-decoration: none; width:120px;">
		<p>Foro Entel
		<img src="<%=request.getContextPath()%>/portlet/contacto/img/ico-link-azul.png" />
		</p>
	</a>
</div>

<div class="contactanosItem clearfix">
	<div class="icono" style="margin-right:5px">
		<img style="width:20px; height:20px; left:50%; position:relative; margin-left: -10px; top:50%;margin-top:-10px; " src="<%=request.getContextPath()%>/portlet/contacto/img/ico-facebook.png" />
	</div>
	<a href="https://www.facebook.com/entelSA" target="_blank" style="color: #63656A; font: 12px 'Trebuchet ms',Arial;text-decoration: none; width:140px;">
		<p>Facebook Entel
		<img src="<%=request.getContextPath()%>/portlet/contacto/img/ico-link-azul.png" />
		</p>
	</a>	
</div>

</f:view>