package com.epcs.historial.bolsas;

import java.io.IOException;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import org.apache.log4j.Logger;

import com.bea.p13n.usermgmt.profile.ProfileWrapper;
import com.epcs.bean.PerfilUsuarioBean;
import com.epcs.bean.TraficoEnLineaPPCCBean;
import com.epcs.billing.registrouso.controller.TraficoEnLineaXLSServlet;
import com.epcs.billing.registrouso.delegate.TraficoDelegate;
import com.epcs.cliente.perfil.delegate.CuentaDelegate;
import com.epcs.historial.bolsas.delegate.HistorialDelegate;
import com.epcs.recursoti.configuracion.uup.ProfileWrapperHelper;
import com.esa.billing.recargas.registrobolsas.types.BolsaSMSType;



	/**
	 * Servlet implementation class TraficoEnLineaXLSServlet
	 */
	public class HistorialBolsasXLSServlet extends HttpServlet {
		
		private static final long serialVersionUID = 1L;
		
		private HistorialDelegate historialDelegate;
		
		private static final Logger LOGGER = Logger.getLogger(TraficoEnLineaXLSServlet.class);
		
		private List<BolsaSMSType> listaBolsasPP;   
		
		private CuentaDelegate cuentaDelegate;
		
		
		public CuentaDelegate getCuentaDelegate() {
			return cuentaDelegate;
		}


		public void setCuentaDelegate(CuentaDelegate cuentaDelegate) {
			this.cuentaDelegate = cuentaDelegate;
		}
		
	   

		public HistorialDelegate getHistorialDelegate() {
			return historialDelegate;
		}

		public void setHistorialDelegate(HistorialDelegate historialDelegate) {
			this.historialDelegate = historialDelegate;
		}

		public List<BolsaSMSType> getListaBolsasPP() {
			return listaBolsasPP;
		}

		public void setListaBolsasPP(List<BolsaSMSType> listaBolsasPP) {
			this.listaBolsasPP = listaBolsasPP;
		}

		/**
	     * @see HttpServlet#HttpServlet()
	     */
	    public HistorialBolsasXLSServlet() {
	    	historialDelegate = new HistorialDelegate();
	        // TODO Auto-generated constructor stub
	    }

		/**
		 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
		 */
		protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			this.exportXLS(request, response);
			// TODO Auto-generated method stub
		}

		/**
		 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
		 */
		protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			this.exportXLS(request, response);
			// TODO Auto-generated method stub
		}

		private void exportXLS(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	        
	        OutputStream out 	= null;
	        int fila 			= 0;

	        
	        try {
	             
	            //HttpServletResponse response = JSFPortletHelper.getResponse();
	        	//ProfileWrapper profile = ProfileWrapperHelper.getProfile(JSFPortletHelper.getRequest());
	            
	            ProfileWrapper profileWrapper = ProfileWrapperHelper.getProfile(request);
	            String numeroPcs = ProfileWrapperHelper.getPropertyAsString(profileWrapper,"numeroPcsSeleccionado");
	            String mercado = "";
	            int flagBam;
	            
	            if(numeroPcs.equals(ProfileWrapperHelper.getPropertyAsString(profileWrapper, "numeroPcsSeleccionado"))){
	            	mercado = ProfileWrapperHelper.getPropertyAsString(profileWrapper, "mercado");
	            	flagBam = ProfileWrapperHelper.getPropertyAsInt(profileWrapper, "flagBam");
	            	if(mercado.equals("PP") && flagBam == 1){
	            		mercado = "PPBAM";
	            	}
	            }
	            else{
	            	PerfilUsuarioBean pub = cuentaDelegate.obtenerPerfilUsuario(numeroPcs);
	            	mercado = pub.getMercado();  
	            	flagBam = pub.getFlagBam();
	            	if(mercado.equals("PP") && flagBam == 1){
	            		mercado = "PPBAM";
	            	}
	            }

	            response.setContentType("application/vnd.ms-excel");
	            response.setHeader("Content-Disposition", "attachment; filename=historialBolsas.xls");

	        	
	            WritableWorkbook w = Workbook.createWorkbook(response.getOutputStream());
	            WritableSheet s = w.createSheet("HistorialBolsas", 0);

	            s.addCell(new Label(0, fila, "Nombre de la Bolsa"));
	            s.addCell(new Label(1, fila, "Valor"));
	            s.addCell(new Label(2, fila, "Canal de compra"));
	            s.addCell(new Label(3, fila, "Fecha y Hora compra"));
	            s.addCell(new Label(4, fila, "Fecha y Hora env\u00EDo de SMS"));
	            s.addCell(new Label(5, fila, "SMS"));


	            List<BolsaSMSType> listaBolsas = new ArrayList<BolsaSMSType>();	            
	            
	            listaBolsas = this.historialDelegate.getHistorialBolsas(numeroPcs, mercado);
	            
	            Iterator iter = listaBolsas.iterator();
	            
	            if (iter.hasNext()){
	                fila = detalleXLS(iter, s, fila);
	            }else{
	                w.write();
	                w.close();
	                //return null;
	            }
	            w.write();
	            w.close();
	        } catch (Exception e) {
	        	LOGGER.error("No ha sido posible descargar el archivo excel ", e);
	        } finally {
	            if (out != null){
	                try {
	                    out.close();
	                }
	                catch(IOException io) {
	                }
	            }
	        }
	        //return null;
	    }
	    
	    private int detalleXLS(Iterator iter, WritableSheet s, int fila) {
	        while (iter.hasNext()) {
	            fila++;
	            BolsaSMSType elemento = (BolsaSMSType)iter.next();

	            try {
	                s.addCell(new Label(0, fila, elemento.getDescBolsacontratada().replaceAll("&nbsp;", "")));
	                s.addCell(new Label(1, fila, elemento.getPrecioOPuntosZonaEntel().replaceAll("&nbsp;", "")));
	                s.addCell(new Label(2, fila, getCanalMapeo( elemento.getCanalContratacion() ).replaceAll("&nbsp;", "")));
	                s.addCell(new Label(3, fila, getFechaParse(elemento.getFechaContratacion()).replaceAll("&nbsp;", "")));
	                s.addCell(new Label(4, fila, getFechaParse(elemento.getFechaEnvioSMS()).replaceAll("&nbsp;", "")));
	                s.addCell(new Label(5, fila, elemento.getTextoSMSConfirmacion().replaceAll("&nbsp;", "")));
	            } catch (java.util.NoSuchElementException e) {
	                LOGGER.info("NO EXITEN MAS ELEMENTOS"); 
	            } catch (Exception ee) {
	                LOGGER.error(ee);
	            }
	        }
	        return fila;
	    }
	    
	    private String getFechaParse(String fecha){
	    	
	    	String nuevaFecha = "";
	    	
	    	String OLD_FORMAT = "yyyy/MM/dd HH:mm:ss";
	    	
	    	String NEW_FORMAT = "dd/MM/yyyy HH:mm:ss";
	    	
	    	fecha = fecha.replace("-", "/");
	    	
	    	SimpleDateFormat sdf = new SimpleDateFormat(OLD_FORMAT);
	    	
	    	try {
				
	    		Date d = sdf.parse(fecha);
				
	    		sdf.applyPattern(NEW_FORMAT);
	    		
	    		nuevaFecha = sdf.format(d);
				
			} catch (ParseException e) {
				
				LOGGER.error(e);
			}
	    	
	    	
	    	
	    	return nuevaFecha;
	    }
	    
	    private String getCanalMapeo(String canalOld){
	    	
	    	
	    	if(canalOld.compareToIgnoreCase("MiEntel")==0){
				
				return  "Portal Mi Entel";
				
			}
	    	if(canalOld.compareToIgnoreCase("SGA")==0){
				
				return "Atenci\u00F3n Ejecutivo";
				
			}
			if(canalOld.compareToIgnoreCase("PID")==0){
				
				return "Portal Tarifa Diaria";
				
			}
			if(canalOld.compareToIgnoreCase("PIM")==0){
				
				return "Portal Wap";
				
			}
			if(canalOld.compareToIgnoreCase("PMOVIL")==0){
				
				return "Portal Mi Entel m\u00F3vil";
				
			}
	        if(canalOld.compareToIgnoreCase("IVR")==0){
	        	
	        	return "Autoatenci\u00F3n telef\u00F3nica";
				
			}
	        if(canalOld.compareToIgnoreCase("Call Center")==0){
				
			}
	        if(canalOld.compareToIgnoreCase("Sucursal")==0){
	        	
	        	return "Tienda";
				
			}
	        if(canalOld.compareToIgnoreCase("USSD")==0){
	        	
	        	return "C\u00F3digo *119";
				
			}
	        if(canalOld.compareToIgnoreCase("Portal WAP")==0){
	        	
	        	return "Portal Wap";
				
			}
	        if(canalOld.compareToIgnoreCase("Portal m\u00F3vil")==0){
	        	
	        	return "Portal Mi Entel m\u00F3vil";
				
			}
	        if(canalOld.compareToIgnoreCase("SMS")==0){
	        	
	        	return "SMS al 301";
				
			}
	    	
	    	return canalOld;
	    }

	}

