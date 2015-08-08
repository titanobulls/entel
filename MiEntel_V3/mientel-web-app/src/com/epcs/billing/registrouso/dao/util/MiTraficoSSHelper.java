/* Propiedad de Entel PCS. Todos los derechos reservados */
package com.epcs.billing.registrouso.dao.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.epcs.bean.ResumenTraficoBean;
import com.epcs.bean.TraficoDatosYMensajes;
import com.epcs.bean.TraficoVoz;
import com.epcs.bean.TraficoVozHorario;
import com.epcs.billing.registrouso.dao.TraficoDAO;
import com.epcs.billing.registrouso.types.DetalleTraficoPlanType;
import com.epcs.billing.registrouso.types.ProductoType;
import com.epcs.recursoti.configuracion.DateHelper;
import com.epcs.recursoti.configuracion.MiEntelProperties;
import com.epcs.recursoti.configuracion.Utils;
import com.epcs.recursoti.configuracion.jsf.utils.JsfUtil;

/**
 * Clase utilitaria para identificar el servicio/producto al que corresponden
 * los traficos obtenidos desde los servicios.<br>
 * Esta clase esta preparada trabajar con instancias de {@link ProductoType} en
 * vista que fue hecha para el cliente de WS BillingRegitroUsoService
 * 
 * @author jlopez (I2B) en nombre de Absalon Opazo (Atencion al Cliente,
 *         EntelPcs)
 * 
 */
public class MiTraficoSSHelper {
	
	private static final Logger LOGGER = Logger.getLogger(MiTraficoSSHelper.class);
	
    /**
     * Lista con id de Productos Voz.<br>
     * Se llena de manera sincronizada, para evitar problemas de concurrencia
     */
    private static List<String> productoVozIdsList;

    /**
     * List con los tipos de Plan para Trafico Voz.<br>
     * Se llena de manera sincronizada, para evitar problemas de concurrencia
     */
    private static List<String> tiposPlanList;

    /**
     * Responde si <code>producto</code> corresponde a un Producto Voz.<br>
     * Este metodo toma todos los valores indicados en la propiedad
     * <code>producto.voz</code> y por cada uno compara su id de producto con el
     * valor de idProducto de <code>producto</code>
     * 
     * @param producto
     *            {@link ProductoType} producto a evaluar
     * @return <code>true</code> si <code>produto</code> es Producto Voz.
     *         <code>false</code> en caso contrario
     */
    public static boolean isProductoVoz(String idProducto) {
        List<String> productoVozIdsList = getProductoVozIds();
        return productoVozIdsList.contains(idProducto);
    }

    
      
    /**
     * Metodo utilitario para recuperar todos los id de producto validos para un
     * Producto Voz.<br>
     * Este metodo recupera el id de producto de cada producto voz definido en
     * la propiedad <code>producto.voz</code>
     * 
     * @return {@link List} de String con los id de producto validos para
     *         productos Voz
     */
    private static List<String> getProductoVozIds() {

        if (productoVozIdsList == null) {
            
            productoVozIdsList = new ArrayList<String>();
            
            //Recupera producto.voz definidos en propiedades
            String[] availableProductosVoz = MiEntelProperties.getProperty(
                    "producto.voz").split(",");

            //por cada uno obtiene su id y lo agrega a la lista
            for (int i = 0; i < availableProductosVoz.length; i++) {
                String productoVozId = MiEntelProperties
                        .getProperty("producto.voz."
                                + availableProductosVoz[i] + ".id");
                if (Utils.isEmptyString(productoVozId)) {
                    continue;
                }
                else {
                    productoVozIdsList.add(productoVozId);
                }
            }
        }

        return productoVozIdsList;
    }

    /**
     * Asigna al bean <code>resumenTraficoBean</code> el trafico de Voz que
     * contenga el <code>producto</code>.<br>
     * Este metodo asume que <code>producto</code> corresponde a un Producto Voz
     * valido (ver {@link MiTraficoSSHelper#isProductoVoz(ProductoType)}).<br>
     * 
     * @param resumenTraficoBean
     *            Bean a quien se asignara el trafico
     * @param producto
     *            {@link ProductoType} con la informacion del trafico
     * @return {@link ResumenTraficoBean} corresponde a la misma instancia de
     *         <code>resumenTraficoBean</code> con el trafico de Voz asignado
     * @throws Exception
     *             Si el tipo de plan de <code>producto</code> no es reconocido
     *             y no puede obtener el trafico de voz
     */
    public static ResumenTraficoBean asignarTraficoVoz(
            final ResumenTraficoBean resumenTraficoBean, ProductoType producto)
            throws Exception {

        Double trafico = getTraficoVozByTipoPlan(producto);

        resumenTraficoBean.setTraficoVoz(trafico);

        Date fechaActualizacion = DateHelper.parseDate(producto
                .getFechaRegistro(), DateHelper.FORMAT_ddMMyyyy_HYPHEN);
        resumenTraficoBean
                .setTraficoVozFechaActualizacion(fechaActualizacion);

        return resumenTraficoBean;
    }

    /**
     * Obtiene el trafico de Voz de <code>producto</code><br>
     * 
     * 
     * @param producto
     *            {@link ProductoType} de donde se obtenra trafico de Voz
     * @return Double con el trafico de voz de <code>producto</code>
     * @throws Exception
     *             Si el tipo de plan de <code>producto</code> no es reconocido
     */
    private static Double getTraficoVozByTipoPlan(ProductoType producto)
            throws Exception {

        // Obtengo tipos de plan indicados en .properties
        List<String> tiposPlanList = getTiposPlanList();

        // busco el tipo de plan al que pertenece producto
        for (String tipoPlan : tiposPlanList) {

            /*
             * Obtengo id del tipo de plan, para compararlo con el id de tipo de
             * plan que contiene producto
             */
            String idTipoPlan = MiEntelProperties
                    .getProperty("producto.voz.tipoPlan." + tipoPlan + ".id");

            if (producto.getTipoPlan().equals(idTipoPlan)) {
                return TraficoVozSSHelper.getTraficoVoz(producto, tipoPlan);
            }
        }

        // Si sale del ciclo, producto no pertenece a ningun tipo de plan
        // conocido por la aplicacion
        LOGGER.error( new Exception("Tipo de plan desconocido: "
                + producto.getTipoPlan()));
		return null;
    }

    /**
     * Entrega un {@link List} con los valores ingresados como tipos de plan en
     * la propiedad <code>producto.voz.tipoPlan</code>
     * 
     * @return {@link List} de String con tipos de plan
     */
    private static List<String> getTiposPlanList() {

        if (tiposPlanList == null) {
            
            tiposPlanList = new ArrayList<String>();
            
            // Obtengo tipos de plan indicados en .properties
            String[] availableTiposPlan = MiEntelProperties.getProperty(
                    "producto.voz.tipoPlan").split(",");

            tiposPlanList = Arrays.asList(availableTiposPlan);
        }

        return tiposPlanList;

    }

    /**
     * Responde si <code>producto</code> corresponde a un Producto Mensajes.<br>
     * Este metodo compara contra la propiedad
     * <code>producto.mensajeria.id</code>
     * 
     * @param producto
     *            {@link ProductoType} producto a evaluar
     * @return <code>true</code> si <code>produto</code> es Producto Mensajes.
     *         <code>false</code> en caso contrario
     */
    public static boolean isProductoMensajes(String idProducto) {
        return MiEntelProperties.getProperty("parametros.idproducto.mensaje").contains(idProducto);
    }

    /**
     * Asigna al bean <code>resumenTraficoBean</code> el trafico de Mensajes que
     * contenga el <code>producto</code>.<br>
     * Este metodo asume que <code>producto</code> corresponde a un Producto
     * Mensajes valido (ver
     * {@link MiTraficoSSHelper#isProductoMensajes(ProductoType)}).<br>
     * 
     * @param resumenTraficoBean
     *            Bean a quien se asignara el trafico
     * @param producto
     *            {@link ProductoType} con la informacion del trafico
     * @return {@link ResumenTraficoBean} corresponde a la misma instancia de
     *         <code>resumenTraficoBean</code> con el trafico de Mensajes
     *         asignado
     */
    public static List<TraficoDatosYMensajes> asignarTraficoMensajes(
            final DetalleTraficoPlanType detalleTraficoPlanType) {
        List<TraficoDatosYMensajes> lisTraficoMEnsajes = new ArrayList<TraficoDatosYMensajes>();
        
        TraficoDatosYMensajes traficoDatosYMensajes = new TraficoDatosYMensajes();
        TraficoDatosYMensajes traficoDatosYMensajes1 = new TraficoDatosYMensajes(); 
                
        traficoDatosYMensajes.setTotal(Integer.parseInt(detalleTraficoPlanType.getValorTasacionPro1()));
        traficoDatosYMensajes1.setTotal(Integer.parseInt(detalleTraficoPlanType.getValorTasacionPro2()));
        
        Date fechaInicio = DateHelper.parseDate(detalleTraficoPlanType.getFechaInicioTraf(), DateHelper.FORMAT_ddMMyyyy_HYPHEN);
        Date fechaFin = DateHelper.parseDate(detalleTraficoPlanType.getFechaFinTraf(), DateHelper.FORMAT_ddMMyyyy_HYPHEN);
        
        traficoDatosYMensajes.setFechaInicial(fechaInicio);
        traficoDatosYMensajes.setFechaFinal(fechaFin);
        traficoDatosYMensajes.setTipo(detalleTraficoPlanType.getNombreTasacionPro1());
        traficoDatosYMensajes1.setTipo(detalleTraficoPlanType.getNombreTasacionPro2());
        lisTraficoMEnsajes.add(traficoDatosYMensajes);
        lisTraficoMEnsajes.add(traficoDatosYMensajes1);
        
        return lisTraficoMEnsajes;
    }

    /**
     * Responde si <code>producto</code> corresponde a un Producto Internet
     * Movil
     * Este metodo compara contra la propiedad
     * <code>producto.internetMovil.id</code>
     * 
     * @param producto
     *            {@link ProductoType} producto a evaluar
     * @return <code>true</code> si <code>produto</code> es Producto Internet
     *         Movil. <code>false</code> en caso contrario
     */
    public static boolean isProductoInternetMovil(String idProducto) {
        return idProducto.equals(
                MiEntelProperties.getProperty("parametros.idproducto.internetmovil"));
    }

    /**
     * Asigna al bean <code>resumenTraficoBean</code> el trafico de Internet
     * Movil que contenga el <code>producto</code>.<br>
     * Este metodo asume que <code>producto</code> corresponde a un Producto
     * Internet Movil valido (ver
     * {@link MiTraficoSSHelper#isProductoInternetMovil(ProductoType)}).<br>
     * 
     * @param resumenTraficoBean
     *            Bean a quien se asignara el trafico
     * @param producto
     *            {@link ProductoType} con la informacion del trafico
     * @return {@link ResumenTraficoBean} corresponde a la misma instancia de
     *         <code>resumenTraficoBean</code> con el trafico de Internet Movil
     *         asignado
     */
    public static TraficoDatosYMensajes asignarTraficoInternetMovil(
            final DetalleTraficoPlanType detalleTraficoPlanType) {
        
        TraficoDatosYMensajes traficoDatosYMensajes= new TraficoDatosYMensajes();
        
        Long traficoInternerMovil = Long.parseLong(detalleTraficoPlanType.getValorTasacionPro1());
        
        traficoDatosYMensajes.setTotal(traficoInternerMovil);
        traficoDatosYMensajes.setTotalFormated(JsfUtil.getAsConvertedString(traficoInternerMovil,"traficoDatosConverterBTMB"));
        
        traficoDatosYMensajes.setFechaFinal(DateHelper.parseDate(detalleTraficoPlanType.getFechaFinTraf(), DateHelper.FORMAT_ddMMyyyy_HYPHEN));
        traficoDatosYMensajes.setFechaInicial(DateHelper.parseDate(detalleTraficoPlanType.getFechaInicioTraf(), DateHelper.FORMAT_ddMMyyyy_HYPHEN));
        traficoDatosYMensajes.setTipo(MiEntelProperties.getProperty("parametros.descproducto.internetmovil"));
    
        return traficoDatosYMensajes;
    }

    /**
     * Responde si <code>producto</code> corresponde a un Producto banda ancha
     * Este metodo compara contra la propiedad
     * <code>producto.bam.id</code>
     * 
     * @param producto
     *            {@link ProductoType} producto a evaluar
     * @return <code>true</code> si <code>produto</code> es Producto Voz.
     *         <code>false</code> en caso contrario
     */
    public static boolean isProductoBandaAnchaMovil(String idProducto) {
        return idProducto.equals(
                MiEntelProperties.getProperty("parametros.idproducto.bandaanchamovil"));
    }

    /**
     * Asigna al bean <code>resumenTraficoBean</code> el trafico de Banda Ancha
     * Movil que contenga el <code>producto</code>.<br>
     * Este metodo asume que <code>producto</code> corresponde a un Producto
     * Banda Ancha Movil valido (ver
     * {@link MiTraficoSSHelper#isProductoBandaAnchaMovil(ProductoType)}).<br>
     * 
     * @param resumenTraficoBean
     *            Bean a quien se asignara el trafico
     * @param producto
     *            {@link ProductoType} con la informacion del trafico
     * @return {@link ResumenTraficoBean} corresponde a la misma instancia de
     *         <code>resumenTraficoBean</code> con el trafico de Banda Ancha
     *         Movil asignado
     */
    public static TraficoDatosYMensajes asignarTraficoBandaAnchaMovil(
            final DetalleTraficoPlanType detalleTraficoPlanType) {
        TraficoDatosYMensajes traficoDatosYMensajes = new TraficoDatosYMensajes();
        
        long trafico = Long.parseLong(detalleTraficoPlanType.getValorTasacionPro1());
        
        traficoDatosYMensajes.setTotal(trafico);
        traficoDatosYMensajes.setTotalFormated(JsfUtil.getAsConvertedString(trafico,"traficoDatosConverterBTMB"));
        
        traficoDatosYMensajes.setFechaFinal(DateHelper.parseDate(detalleTraficoPlanType.getFechaFinTraf(), DateHelper.FORMAT_ddMMyyyy_HYPHEN));
        traficoDatosYMensajes.setFechaInicial(DateHelper.parseDate(detalleTraficoPlanType.getFechaInicioTraf(), DateHelper.FORMAT_ddMMyyyy_HYPHEN));
        traficoDatosYMensajes.setTipo(MiEntelProperties.getProperty("parametros.descproducto.bandaanchamovil"));
        
        return traficoDatosYMensajes;
    }

    /**
     * Responde si <code>producto</code> corresponde a un Producto Blackberry
     * Este metodo compara contra la propiedad
     * <code>producto.blackberry.id</code>
     * 
     * @param producto
     *            {@link ProductoType} producto a evaluar
     * @return <code>true</code> si <code>produto</code> es Producto Voz.
     *         <code>false</code> en caso contrario
     */
    public static boolean isProductoBlackberry(String idProducto) {
        return idProducto.equals(
                MiEntelProperties.getProperty("parametros.idproducto.blackberry"));
    }

    /**
     * Asigna al bean <code>resumenTraficoBean</code> el trafico de Blackberry
     * que contenga el <code>producto</code>.<br>
     * Este metodo asume que <code>producto</code> corresponde a un Producto
     * Blackberry valido (ver
     * {@link MiTraficoSSHelper#isProductoBlackberry(ProductoType)}).<br>
     * 
     * @param resumenTraficoBean
     *            Bean a quien se asignara el trafico
     * @param producto
     *            {@link ProductoType} con la informacion del trafico
     * @return {@link ResumenTraficoBean} corresponde a la misma instancia de
     *         <code>resumenTraficoBean</code> con el trafico de Blackberry
     *         asignado
     */
    public static TraficoDatosYMensajes asignarTraficoBlackberry(
            final DetalleTraficoPlanType detalleTraficoPlanType) {

         TraficoDatosYMensajes traficoDatosYMensajes = new TraficoDatosYMensajes();
        
        Long trafico = Long.parseLong(detalleTraficoPlanType.getValorTasacionPro1());
        
        traficoDatosYMensajes.setTotal(trafico);
        traficoDatosYMensajes.setTotalFormated(JsfUtil.getAsConvertedString(trafico,"traficoDatosConverterBTMB"));
        
        traficoDatosYMensajes.setFechaFinal(DateHelper.parseDate(detalleTraficoPlanType.getFechaFinTraf(), DateHelper.FORMAT_ddMMyyyy_HYPHEN));
        traficoDatosYMensajes.setFechaInicial(DateHelper.parseDate(detalleTraficoPlanType.getFechaInicioTraf(), DateHelper.FORMAT_ddMMyyyy_HYPHEN));
        traficoDatosYMensajes.setTipo(MiEntelProperties.getProperty("parametros.descproducto.blackberry"));
        return traficoDatosYMensajes;
    }
    
    /**
     * 
     * @param planType
     * @param cosbsc
     * @param detalleTraficoPlanType
     * @return
     */
    public static TraficoVoz buildTraficoVoz(String planType,String cosbsc, DetalleTraficoPlanType detalleTraficoPlanType){
        TraficoVoz traficoVoz = new TraficoVoz();
        List<TraficoVozHorario> listTraficoVozHorario = new ArrayList<TraficoVozHorario>();
       
        int type = Integer.parseInt(planType);
        
        
        switch(type){
        
        case 1:
            
            if(cosbsc.equals("1134") || cosbsc.equals("1135")){
                TraficoVozHorario traficoVozHorario = new TraficoVozHorario();
                traficoVozHorario.setDescripcion(MiEntelProperties.getProperty("parametros.traficovozhorario.onnet"));
                traficoVozHorario.setConsumo(calculaSegundos(detalleTraficoPlanType.getValorTasacionPro1()));
                TraficoVozHorario traficoVozHorario2 = new TraficoVozHorario();

                traficoVozHorario2.setDescripcion(MiEntelProperties.getProperty("parametros.traficovozhorario.offnet"));
                traficoVozHorario2.setConsumo(calculaSegundos(detalleTraficoPlanType.getValorTasacionPro2()));
                
                listTraficoVozHorario.add(traficoVozHorario);
                listTraficoVozHorario.add(traficoVozHorario2);
                
                traficoVoz.setTipoTasacion("PLANRED");
                traficoVoz.setFechaFinal(DateHelper.parseDate(detalleTraficoPlanType.getFechaFinTraf(),DateHelper.FORMAT_ddMMyyyy_HYPHEN));
                traficoVoz.setFechaInicial(DateHelper.parseDate(detalleTraficoPlanType.getFechaInicioTraf(),DateHelper.FORMAT_ddMMyyyy_HYPHEN));
                traficoVoz.setDetallePorHorario(listTraficoVozHorario);
                traficoVoz.setTotal(traficoVozHorario.getConsumo()+traficoVozHorario2.getConsumo());
                  
            }else{
                TraficoVozHorario traficoVozHorario = new TraficoVozHorario();
                traficoVozHorario.setDescripcion(MiEntelProperties.getProperty("parametros.traficovozhorario.horariobajo"));
                traficoVozHorario.setConsumo(calculaSegundos(detalleTraficoPlanType.getValorTasacionPro1()));
                TraficoVozHorario traficoVozHorario2 = new TraficoVozHorario();
                traficoVozHorario2.setDescripcion(MiEntelProperties.getProperty("parametros.traficovozhorario.horarionormal"));
                traficoVozHorario2.setConsumo(calculaSegundos(detalleTraficoPlanType.getValorTasacionPro2()));
                listTraficoVozHorario.add(traficoVozHorario);
                listTraficoVozHorario.add(traficoVozHorario2);
                traficoVoz.setTipoTasacion("PLANNORMAL");
                traficoVoz.setFechaFinal(DateHelper.parseDate(detalleTraficoPlanType.getFechaFinTraf(),DateHelper.FORMAT_ddMMyyyy_HYPHEN));
                traficoVoz.setFechaInicial(DateHelper.parseDate(detalleTraficoPlanType.getFechaInicioTraf(),DateHelper.FORMAT_ddMMyyyy_HYPHEN));
                traficoVoz.setDetallePorHorario(listTraficoVozHorario);
                traficoVoz.setTotal(traficoVozHorario.getConsumo()+traficoVozHorario2.getConsumo());
            }
        break;   
            
        case 2:
        case 9:
        case 11:
        case 12:
        case 13:
        case 14:
        case 15:
        	
            TraficoVozHorario traficoVozHorario = new TraficoVozHorario();
            traficoVozHorario.setDescripcion(MiEntelProperties.getProperty("parametros.traficovozhorario.onnet"));
            traficoVozHorario.setConsumo(calculaSegundos(detalleTraficoPlanType.getValorTasacionPro1()));
            TraficoVozHorario traficoVozHorario2 = new TraficoVozHorario();

            traficoVozHorario2.setDescripcion(MiEntelProperties.getProperty("parametros.traficovozhorario.offnet"));
            traficoVozHorario2.setConsumo(calculaSegundos(detalleTraficoPlanType.getValorTasacionPro2()));
            
            listTraficoVozHorario.add(traficoVozHorario);
            listTraficoVozHorario.add(traficoVozHorario2);
            
            traficoVoz.setTipoTasacion("PLANRED");
            traficoVoz.setFechaFinal(DateHelper.parseDate(detalleTraficoPlanType.getFechaFinTraf(),DateHelper.FORMAT_ddMMyyyy_HYPHEN));
            traficoVoz.setFechaInicial(DateHelper.parseDate(detalleTraficoPlanType.getFechaInicioTraf(),DateHelper.FORMAT_ddMMyyyy_HYPHEN));
            traficoVoz.setDetallePorHorario(listTraficoVozHorario);
            traficoVoz.setTotal(traficoVozHorario.getConsumo()+traficoVozHorario2.getConsumo());
            
            break;
         
        case 3:
                
                TraficoVozHorario traficoVozHorario3 = new TraficoVozHorario();
                traficoVozHorario3.setDescripcion(MiEntelProperties.getProperty("parametros.traficovozhorario.adicional"));
                traficoVozHorario3.setConsumo(calculaSegundos(detalleTraficoPlanType.getValorTasacionPro1()));
                TraficoVozHorario traficoVozHorario4 = new TraficoVozHorario();
                traficoVozHorario4.setDescripcion(MiEntelProperties.getProperty("parametros.traficovozhorario.nrofrecuente"));
                traficoVozHorario4.setConsumo(calculaSegundos(detalleTraficoPlanType.getValorTasacionPro2()));
                
                TraficoVozHorario traficoVozHorario5 = new TraficoVozHorario();
                traficoVozHorario5.setDescripcion(MiEntelProperties.getProperty("parametros.traficovozhorario.tododestino"));
                traficoVozHorario5.setConsumo(calculaSegundos(detalleTraficoPlanType.getValorTasacionPro3()));
                
                listTraficoVozHorario.add(traficoVozHorario3);
                listTraficoVozHorario.add(traficoVozHorario4);
                listTraficoVozHorario.add(traficoVozHorario5);
                
                traficoVoz.setTipoTasacion("PLANGLOBAL");
                traficoVoz.setFechaFinal(DateHelper.parseDate(detalleTraficoPlanType.getFechaFinTraf(),DateHelper.FORMAT_ddMMyyyy_HYPHEN));
                traficoVoz.setFechaInicial(DateHelper.parseDate(detalleTraficoPlanType.getFechaInicioTraf(),DateHelper.FORMAT_ddMMyyyy_HYPHEN));
                traficoVoz.setDetallePorHorario(listTraficoVozHorario);
                traficoVoz.setTotal(traficoVozHorario3.getConsumo()+traficoVozHorario4.getConsumo()+traficoVozHorario5.getConsumo());
        break;
        case 4:
            
            TraficoVozHorario traficoVozHorario6 = new TraficoVozHorario();
            traficoVozHorario6.setDescripcion(MiEntelProperties.getProperty("parametros.traficovozhorario.adicional"));
            traficoVozHorario6.setConsumo(calculaSegundos(detalleTraficoPlanType.getValorTasacionPro1()));
            TraficoVozHorario traficoVozHorario7 = new TraficoVozHorario();
            traficoVozHorario7.setDescripcion(MiEntelProperties.getProperty("parametros.traficovozhorario.nrofrecuente"));
            traficoVozHorario7.setConsumo(calculaSegundos(detalleTraficoPlanType.getValorTasacionPro2()));
            
            TraficoVozHorario traficoVozHorario8 = new TraficoVozHorario();
            traficoVozHorario8.setDescripcion(MiEntelProperties.getProperty("parametros.traficovozhorario.tododestino"));
            traficoVozHorario8.setConsumo(calculaSegundos(detalleTraficoPlanType.getValorTasacionPro3()));
            
            listTraficoVozHorario.add(traficoVozHorario6);
            listTraficoVozHorario.add(traficoVozHorario7);
            listTraficoVozHorario.add(traficoVozHorario8);
            
            traficoVoz.setTipoTasacion("PLANJOVEN");
            traficoVoz.setFechaFinal(DateHelper.parseDate(detalleTraficoPlanType.getFechaFinTraf(),DateHelper.FORMAT_ddMMyyyy_HYPHEN));
            traficoVoz.setFechaInicial(DateHelper.parseDate(detalleTraficoPlanType.getFechaInicioTraf(),DateHelper.FORMAT_ddMMyyyy_HYPHEN));
            traficoVoz.setDetallePorHorario(listTraficoVozHorario);
            traficoVoz.setTotal(traficoVozHorario6.getConsumo()+traficoVozHorario7.getConsumo()+traficoVozHorario8.getConsumo());
            
            break;
            
        case 5:
            
            TraficoVozHorario traficoVozHorario9 = new TraficoVozHorario();
            traficoVozHorario9.setDescripcion(MiEntelProperties.getProperty("parametros.traficovozhorario.adicional"));
            traficoVozHorario9.setConsumo(calculaSegundos(detalleTraficoPlanType.getValorTasacionPro1()));
            TraficoVozHorario traficoVozHorario10 = new TraficoVozHorario();
            traficoVozHorario10.setDescripcion(MiEntelProperties.getProperty("parametros.traficovozhorario.nrofrecuente"));
            traficoVozHorario10.setConsumo(calculaSegundos(detalleTraficoPlanType.getValorTasacionPro2()));
            
            TraficoVozHorario traficoVozHorario11 = new TraficoVozHorario();
            traficoVozHorario11.setDescripcion(MiEntelProperties.getProperty("parametros.traficovozhorario.tododestino"));
            traficoVozHorario11.setConsumo(calculaSegundos(detalleTraficoPlanType.getValorTasacionPro3()));
            
            listTraficoVozHorario.add(traficoVozHorario9);
            listTraficoVozHorario.add(traficoVozHorario10);
            listTraficoVozHorario.add(traficoVozHorario11);
            
            traficoVoz.setTipoTasacion("PLANFULL");
            traficoVoz.setFechaFinal(DateHelper.parseDate(detalleTraficoPlanType.getFechaFinTraf(),DateHelper.FORMAT_ddMMyyyy_HYPHEN));
            traficoVoz.setFechaInicial(DateHelper.parseDate(detalleTraficoPlanType.getFechaInicioTraf(),DateHelper.FORMAT_ddMMyyyy_HYPHEN));
            traficoVoz.setDetallePorHorario(listTraficoVozHorario);
            traficoVoz.setTotal(traficoVozHorario9.getConsumo()+traficoVozHorario10.getConsumo()+traficoVozHorario11.getConsumo());
            
            break;
            
        case 6:
            
            if(cosbsc.equals("1179")  || 
                    cosbsc.equals("1180") || 
                    cosbsc.equals("1181") || 
                    cosbsc.equals("1182") || 
                    cosbsc.equals("1183") || 
                    cosbsc.equals("1184") ||
                    cosbsc.equals("1185") ||
                    cosbsc.equals("1186")){
                
                TraficoVozHorario traficoVozHorario13 = new TraficoVozHorario();
                traficoVozHorario13.setDescripcion(MiEntelProperties.getProperty("parametros.traficovozhorario.nrofrecuente"));
                traficoVozHorario13.setConsumo(calculaSegundos(detalleTraficoPlanType.getValorTasacionPro1()));
                
                TraficoVozHorario traficoVozHorario14 = new TraficoVozHorario();
                traficoVozHorario14.setDescripcion(MiEntelProperties.getProperty("parametros.traficovozhorario.tododestino"));
                traficoVozHorario14.setConsumo(calculaSegundos(detalleTraficoPlanType.getValorTasacionPro2()));
                
                listTraficoVozHorario.add(traficoVozHorario13);
                listTraficoVozHorario.add(traficoVozHorario14);
                
                traficoVoz.setTipoTasacion("PLANUNICAFRECUENTE");
                traficoVoz.setFechaFinal(DateHelper.parseDate(detalleTraficoPlanType.getFechaFinTraf(),DateHelper.FORMAT_ddMMyyyy_HYPHEN));
                traficoVoz.setFechaInicial(DateHelper.parseDate(detalleTraficoPlanType.getFechaInicioTraf(),DateHelper.FORMAT_ddMMyyyy_HYPHEN));
                traficoVoz.setDetallePorHorario(listTraficoVozHorario);
                traficoVoz.setTotal(traficoVozHorario13.getConsumo()+traficoVozHorario14.getConsumo());
               
            }else{
                
                TraficoVozHorario traficoVozHorario15 = new TraficoVozHorario();
                traficoVozHorario15.setDescripcion(MiEntelProperties.getProperty("parametros.traficovozhorario.minutosutilizados"));
                traficoVozHorario15.setConsumo(calculaSegundos(detalleTraficoPlanType.getValorTasacionPro1()));
                
                listTraficoVozHorario.add(traficoVozHorario15);
                
                traficoVoz.setTipoTasacion("PLANUNICA");
                traficoVoz.setFechaFinal(DateHelper.parseDate(detalleTraficoPlanType.getFechaFinTraf(),DateHelper.FORMAT_ddMMyyyy_HYPHEN));
                traficoVoz.setFechaInicial(DateHelper.parseDate(detalleTraficoPlanType.getFechaInicioTraf(),DateHelper.FORMAT_ddMMyyyy_HYPHEN));
                traficoVoz.setDetallePorHorario(listTraficoVozHorario);
                traficoVoz.setTotal(traficoVozHorario15.getConsumo());
         
                
            }
            break;
        case 7:
            
            TraficoVozHorario traficoVozHorario16 = new TraficoVozHorario();
            traficoVozHorario16.setDescripcion(MiEntelProperties.getProperty("parametros.traficovozhorario.redfija"));
            traficoVozHorario16.setConsumo(calculaSegundos(detalleTraficoPlanType.getValorTasacionPro1()));
            TraficoVozHorario traficoVozHorario17 = new TraficoVozHorario();
            traficoVozHorario17.setDescripcion(MiEntelProperties.getProperty("parametros.traficovozhorario.adicional"));
            traficoVozHorario17.setConsumo(calculaSegundos(detalleTraficoPlanType.getValorTasacionPro2()));
            
            TraficoVozHorario traficoVozHorario18 = new TraficoVozHorario();
            traficoVozHorario18.setDescripcion(MiEntelProperties.getProperty("parametros.traficovozhorario.nrofrecuente"));
            traficoVozHorario18.setConsumo(calculaSegundos(detalleTraficoPlanType.getValorTasacionPro3()));
            
            listTraficoVozHorario.add(traficoVozHorario16);
            listTraficoVozHorario.add(traficoVozHorario17);
            listTraficoVozHorario.add(traficoVozHorario18);
            
            traficoVoz.setTipoTasacion("PLANREDFIJA");
            traficoVoz.setFechaFinal(DateHelper.parseDate(detalleTraficoPlanType.getFechaFinTraf(),DateHelper.FORMAT_ddMMyyyy_HYPHEN));
            traficoVoz.setFechaInicial(DateHelper.parseDate(detalleTraficoPlanType.getFechaInicioTraf(),DateHelper.FORMAT_ddMMyyyy_HYPHEN));
            traficoVoz.setDetallePorHorario(listTraficoVozHorario);
            traficoVoz.setTotal(traficoVozHorario16.getConsumo()+traficoVozHorario17.getConsumo()+traficoVozHorario18.getConsumo());
           
            break;
            
        case 8:
            
            TraficoVozHorario traficoVozHorario19 = new TraficoVozHorario();
            traficoVozHorario19.setDescripcion(MiEntelProperties.getProperty("parametros.traficovozhorario.adicional"));
            traficoVozHorario19.setConsumo(calculaSegundos(detalleTraficoPlanType.getValorTasacionPro1()));
            TraficoVozHorario traficoVozHorario20 = new TraficoVozHorario();
            traficoVozHorario20.setDescripcion(MiEntelProperties.getProperty("parametros.traficovozhorario.familia"));
            traficoVozHorario20.setConsumo(calculaSegundos(detalleTraficoPlanType.getValorTasacionPro2()));
            
            TraficoVozHorario traficoVozHorario21 = new TraficoVozHorario();
            traficoVozHorario21.setDescripcion(MiEntelProperties.getProperty("parametros.traficovozhorario.tododestino"));
            traficoVozHorario21.setConsumo(calculaSegundos(detalleTraficoPlanType.getValorTasacionPro3()));
            
            listTraficoVozHorario.add(traficoVozHorario19);
            listTraficoVozHorario.add(traficoVozHorario20);
            listTraficoVozHorario.add(traficoVozHorario21);
            
            traficoVoz.setTipoTasacion("PLANREDFIJA");
            traficoVoz.setFechaFinal(DateHelper.parseDate(detalleTraficoPlanType.getFechaFinTraf(),DateHelper.FORMAT_ddMMyyyy_HYPHEN));
            traficoVoz.setFechaInicial(DateHelper.parseDate(detalleTraficoPlanType.getFechaInicioTraf(),DateHelper.FORMAT_ddMMyyyy_HYPHEN));
            traficoVoz.setDetallePorHorario(listTraficoVozHorario);
            traficoVoz.setTotal(traficoVozHorario19.getConsumo()+traficoVozHorario20.getConsumo()+traficoVozHorario21.getConsumo());
            
            break;
        
        }
       
        return traficoVoz;
        
        
    }
    
    
    /**
     * Devuelve el total de segundos
     * 
     * @param minutos
     * @return
     */
    public static int calculaSegundos(String minutosString) {
        int totalSegundos = 0;
        try {
            double minutos = Double.parseDouble(minutosString);
            String calculoParteEnteraString = String.valueOf(Math
                    .floor(minutos) * 60);
            int posicionPuntoDecimal = calculoParteEnteraString.indexOf(".");
            calculoParteEnteraString = calculoParteEnteraString.substring(0,
                    posicionPuntoDecimal);

            String calculoParteDecimalString = String.valueOf(minutos);
            posicionPuntoDecimal = calculoParteDecimalString.indexOf(".");
            calculoParteDecimalString = calculoParteDecimalString.substring(
                    posicionPuntoDecimal + 1, calculoParteDecimalString
                            .length());

            totalSegundos = Integer.parseInt(calculoParteEnteraString)
                    + Integer.parseInt(calculoParteDecimalString);

        } catch (Exception e) {
            // En caso de exception se retorna 0
        }
        return totalSegundos;
    }

}
