/* Propiedad de Entel PCS. Todos los derechos reservados */
package com.epcs.recursoti.configuracion.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

/**
 * Servlet utilizado, para obtener la imagen del equipo del Usuario
 * Recibe por parametro el msisdn (/image?id=msisdn)
 * 
 * @author jmanzur (I2B) en nombre de Absalon Opazo (Atencion al Cliente, EntelPcs)
 *
 */
public class Image extends javax.servlet.http.HttpServlet implements
        javax.servlet.Servlet {

    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(Image.class);

    /*
     * (non-Java-doc)
     * 
     * @see javax.servlet.http.HttpServlet#HttpServlet()
     */
    public Image() {
        super();
    }

    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        String msisdn = null;
        StringBuilder urlImagen = null;

        // Se rescata el msisdn del request
        // archivos.
        msisdn = (req.getParameter("id") == null) ? "imagennoexiste.png" : req.getParameter("id");  
      
        InputStream in = null;
        OutputStream out = null;
        HttpURLConnection con = null;

        try {
            // Url de para obtener la imagen
            // mobiprof.vas.entelpcs.cl = 172.29.79.11
            urlImagen = new StringBuilder("http://mobiprof.vas.entelpcs.cl/v3/569");
            urlImagen.append(msisdn);
            urlImagen.append("/mobileequipment/img");
                    
            URL url = new URL(urlImagen.toString());

            con = (HttpURLConnection) url.openConnection();
            con.connect();

            in = con.getInputStream();
            out = resp.getOutputStream();


        } catch (IOException e) {
            LOGGER.error("Image|doGet: IOException " + msisdn);
			/* Soporte Incidencia 6047930
            urlImagen = new StringBuilder("http://mobiprof.vas.entelpcs.cl/v3/56998853486/mobileequipment/img");
            URL url = new URL(urlImagen.toString());
            con = (HttpURLConnection) url.openConnection();
            con.connect();
            in = con.getInputStream();
            out = resp.getOutputStream();
			*/
        } finally {
            if (in!=null && out!=null){
            // Copy the contents of the file to the output stream
            byte[] buf = new byte[1024];
            int count = 0;
            while ((count = in.read(buf)) >= 0) {
                out.write(buf, 0, count);
            }}
            if (in != null)
                in.close();
            if (out != null)
                out.close();
            if (con != null)
                con.disconnect();
        }
    }

    /*
     * (non-Java-doc)
     * 
     * @see javax.servlet.http.HttpServlet#doPost(HttpServletRequest request,
     * HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}