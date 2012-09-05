package org.apache.wicket.erp;

import java.io.Serializable;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.request.mapper.parameter.PageParameters;

public class ERPWebPage extends WebPage implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public ERPWebPage()
	{
		this(new PageParameters(),"");
	}
	public ERPWebPage(String title)
	{
		this(new PageParameters(),title);
	}
	
	public ERPWebPage(final PageParameters pageParameters,String title)
	{
		super(pageParameters);
		add(new ERPWebHeader("mainNavigation", title, this));
	}
}
