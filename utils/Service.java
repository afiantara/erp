package org.apache.wicket.erp.utils;

import java.io.Serializable;

import javax.xml.namespace.QName;

import org.apache.axis2.AxisFault;
import org.apache.axis2.Constants;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.Options;
import org.apache.axis2.rpc.client.RPCServiceClient;
import org.apache.axis2.transport.http.HTTPConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Service implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	RPCServiceClient serviceClient = null;//new RPCServiceClient();
	
	public static String ACCOUNTING_SERVICE_URL=Config.getInstance().getUrl() + "Accounting";
	public static String CRM_SERVICE_URL=Config.getInstance().getUrl() + "CRM";
	public static String PURCHASING_SERVICE_URL=Config.getInstance().getUrl() + "Purchasing";
	public static String INVENTORY_SERVICE_URL=Config.getInstance().getUrl() + "Inventory";
	public static String SALES_SERVICE_URL=Config.getInstance().getUrl() + "Sales";
	private String url;
	
	private static final Logger log = LoggerFactory.getLogger(Service.class);
	
	public Service(String url) throws AxisFault
	{
		serviceClient = new RPCServiceClient();
		this.url=url;
		
	}
	
	@SuppressWarnings("unchecked")
	public Object[] callServiceSales(String method,Object[] params,Class[] retTypes) throws AxisFault
	{
		return callService("sales",method,params,retTypes);
	    //String result = (String) response[0];
	}
	
	@SuppressWarnings("unchecked")
	public Object[] callServiceAccounting(String method,Object[] params,Class[] retTypes) throws AxisFault
	{
		return callService("accounting",method,params,retTypes);
	    //String result = (String) response[0];
	}
	
	@SuppressWarnings("unchecked")
	public Object[] callServiceInventory(String method,Object[] params,Class[] retTypes) throws AxisFault
	{
		return callService("inventory",method,params,retTypes);
	}
	
	@SuppressWarnings("unchecked")
	public Object[] callServiceCRM(String method,Object[] params,Class[] retTypes) throws AxisFault
	{
		return callService("crm",method,params,retTypes);
	    //String result = (String) response[0];
	}
	
	@SuppressWarnings("unchecked")
	public Object[] callServicePurchasing(String method,Object[] params,Class[] retTypes) throws AxisFault
	{
		return callService("purchasing",method,params,retTypes);
	    //String result = (String) response[0];
	}
	
	@SuppressWarnings("unchecked")
	private Object[] callService(String name,String method,Object[] params,Class[] retTypes) throws AxisFault
	{
		log.info("callService->" + name + ";" + method);
		
		Options options = serviceClient.getOptions();
		EndpointReference targetEPR = new EndpointReference(
        //"http://localhost:8080/axis2/services/Accounting");
				url);
		options.setProperty(HTTPConstants.AUTO_RELEASE_CONNECTION, Constants.VALUE_TRUE);
		options.setTo(targetEPR);
		
		QName opSayHello=
	        new QName("http://epp.com." + name + "/xsd", method);
		
		Object[] opSayHelloArgs = params;
		
		Class[] returnTypes = retTypes;
	    
	    Object[] response = serviceClient.invokeBlocking(opSayHello,
	    		opSayHelloArgs, returnTypes);
	    
	    serviceClient.cleanupTransport();
	    return response;
		
	}
}
