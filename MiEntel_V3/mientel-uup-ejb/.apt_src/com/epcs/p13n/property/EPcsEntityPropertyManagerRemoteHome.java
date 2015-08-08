package com.epcs.p13n.property;

/*
 ** This file was automatically generated by 
 ** EJBGen WebLogic Server 10.3.2.0  Tue Oct 20 12:16:15 PDT 2009 1267925 
 */

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;

/**
 * * Esta clase implementa los metodos necesarios para unificar las propiedades
 * de perfiles de un usuario determinado a traves de los metodos que provee la
 * interface EntityPropertyManager. Este EJB esta registrado con el
 * administrador de perfiles del framework de Weblogic en el EJB deployment
 * descriptor. Este EntityPropertyManager gestiona las propiedades de usuarios
 * con base en un servicio web externo consultado por medio del numero del
 * movil.
 */

public interface EPcsEntityPropertyManagerRemoteHome extends EJBHome {

	public EPcsEntityPropertyManagerRemote create() throws CreateException,
			RemoteException, javax.ejb.CreateException;

}
