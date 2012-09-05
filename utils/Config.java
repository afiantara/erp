package org.apache.wicket.erp.utils;

import org.apache.wicket.erp.ERPApplication;

public class Config {
	private static Config INSTANCE=null;
	private String url;
	private Config()
	{
		String value = ERPApplication.get().getServletContext().getInitParameter("URL");
		this.setUrl(value);
	}
	
	private synchronized static void createInstance()
	{
		INSTANCE=new Config();
	}
	public synchronized static Config getInstance()
	{
		if(INSTANCE==null) createInstance();
		return INSTANCE;
	}
	
	public synchronized static void removeInstance()
	{
		if(INSTANCE!=null)
			INSTANCE=null;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUrl() {
		return url;
	}
}
