<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="f"  uri="http://java.sun.com/jsf/core"%>
<%@ taglib prefix="h"  uri="http://java.sun.com/jsf/html"%>
<%@ taglib prefix="c"  uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="it" uri="http://i2b.cl/jsf/iterator/" %>
<%@ taglib prefix="r"  uri="http://www.bea.com/servers/portal/tags/netuix/render"%>

	<f:view>
		<!-- formulario direccion -->
		<div id="modificarDirCaja" class="box-numero modificarDirCaja">
           <form id="modificarDireccion" class="modificarDireccion" name="modificarDireccion" style="" action="#">
				<jsp:include page="/token.jsp" flush="true"/>
              <input type="hidden" name="s_region" value="">
              <input type="hidden" name="s_ciudad" value="">
              <input type="hidden" name="s_comuna" value="">
              <fieldset>
				
				
     
                                       
              </fieldset>
         </form>
        <div class="cuadro-boton">&nbsp;</div>
      </div>

      <!-- /formulario direccion -->

</f:view>