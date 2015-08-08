package com.epcs.billing.registrouso.controller;

import java.io.IOException;
import java.io.OutputStream;
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
import com.epcs.bean.TraficoEnLineaPPCCBean;
import com.epcs.billing.balance.delegate.FacturacionDelegate;
import com.epcs.billing.registrouso.delegate.TraficoDelegate;
import com.epcs.dashboard.mensajes.controller.DetalleLlamadosPDFServlet;
import com.epcs.recursoti.configuracion.jsf.utils.JSFPortletHelper;
import com.epcs.recursoti.configuracion.uup.ProfileWrapperHelper;

/**
 * @author jroman (I2B) en nombre de Absalon Opazo (Atencion al Cliente,
 *         EntelPcs)
 * 
 */
/**
 * Servlet implementation class TraficoEnLineaXLSServlet
 */
public class TraficoEnLineaXLSServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	private TraficoDelegate traficoDelegate;
	
	private static final Logger LOGGER = Logger.getLogger(TraficoEnLineaXLSServlet.class);
	
	private List<TraficoEnLineaPPCCBean> traficoEnLineaPPCC;   
	
	
	
    /**
	 * @return the traficoEnLineaPPCC
	 */
	public List<TraficoEnLineaPPCCBean> getTraficoEnLineaPPCC() {
		return traficoEnLineaPPCC;
	}

	/**
	 * @param traficoEnLineaPPCC the traficoEnLineaPPCC to set
	 */
	public void setTraficoEnLineaPPCC(
			List<TraficoEnLineaPPCCBean> traficoEnLineaPPCC) {
		this.traficoEnLineaPPCC = traficoEnLineaPPCC;
	}

	/**
     * @see HttpServlet#HttpServlet()
     */
    public TraficoEnLineaXLSServlet() {
    	traficoDelegate = new TraficoDelegate();
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
            

            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-Disposition", "attachment; filename=traficoEnLinea.xls");

        	
            WritableWorkbook w = Workbook.createWorkbook(response.getOutputStream());
            WritableSheet s = w.createSheet("Trafico", 0);

            s.addCell(new Label(0, fila, "Tipo"));
            s.addCell(new Label(1, fila, "Destino o Emision"));
            s.addCell(new Label(2, fila, "Fecha/Hora"));
            s.addCell(new Label(3, fila, "Duracion"));
            s.addCell(new Label(4, fila, "Unidad"));
            s.addCell(new Label(5, fila, "Costo"));
            s.addCell(new Label(6, fila, "Saldo"));


            traficoEnLineaPPCC = this.traficoDelegate.getTraficoEnLineaPPCC(numeroPcs , "0");
            Iterator iter = traficoEnLineaPPCC.iterator();
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
            TraficoEnLineaPPCCBean elemento = (TraficoEnLineaPPCCBean)iter.next();

            try {
                s.addCell(new Label(0, fila, elemento.getTipoServicio().replaceAll("&nbsp;", "")));
                s.addCell(new Label(1, fila, elemento.getDestino().replaceAll("&nbsp;", "")));
                s.addCell(new Label(2, fila, elemento.getFechaLlamada().replaceAll("&nbsp;", "")+" - "+ elemento.getHoraLlamada().replaceAll("&nbsp;", "")));
                s.addCell(new Label(3, fila, elemento.getDuracion().replaceAll("&nbsp;", "")));
                s.addCell(new Label(4, fila, elemento.getUnidad().replaceAll("&nbsp;", "")));
                s.addCell(new Label(5, fila, elemento.getCosto().replaceAll("&nbsp;", "")));
                s.addCell(new Label(6, fila, elemento.getSaldo().replaceAll("&nbsp;", "")));
            } catch (java.util.NoSuchElementException e) {
                LOGGER.info("NO EXITEN MAS ELEMENTOS"); 
            } catch (Exception ee) {
                LOGGER.error(ee);
            }
        }
        return fila;
    }

}
