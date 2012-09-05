package org.apache.wicket.erp.file;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.axis2.AxisFault;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.erp.ERPApplication;
import org.apache.wicket.erp.ERPWebPage;
import org.apache.wicket.erp.utils.IActionHandler;
import org.apache.wicket.erp.utils.PanelAction;
import org.apache.wicket.erp.utils.Service;
import org.apache.wicket.erp.utils.UserInfo;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RadioChoice;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import java.text.SimpleDateFormat;

public class MenusInput extends ERPWebPage implements IActionHandler{
	/**
	 * 
	 */
	private static final List<String> STATUS = Arrays
	.asList(new String[] { "Y", "N"});
	
	private static List<String> MENU = Arrays
	.asList(new String[] { ""});
	
	private static final long serialVersionUID = 1L;
	private RequiredTextField<String> menuname;
	private RadioChoice<String> recstatus;
	boolean isReadOnly=true;
	private Form<Menus> form;
	final AttributeModifier ro = new AttributeModifier("readonly", isReadOnly);
	private Menus exp = new Menus();
	private static MenusInput app;
	private Service _service;
	private void init()
	{
		try {
			_service=new Service(Service.ACCOUNTING_SERVICE_URL);
		} catch (AxisFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static MenusInput get()
	{
		return app;
	}
	
	public MenusInput(PageParameters p)
	{
		this(new Menus());
		readOnly(false);
		if(p.getNamedKeys().contains("error"))
			error(p.get("error"));
		if(p.getNamedKeys().contains("info"))
			info(p.get("info"));
	}
	public MenusInput()
	{
		this(new Menus());
	}
	public MenusInput(Menus exp)
	{
		super("File | Menus");
		init();
		this.exp = exp;
		form = new Form<Menus>("form", new CompoundPropertyModel<Menus>(this.exp));
		add(form);
		
		// add a simple text field that uses Input's 'text' property. Nothing
		// can go wrong here
		menuname=new RequiredTextField<String>("menuname");
		recstatus=new RadioChoice<String>("recstatus",STATUS);
		recstatus.setSuffix(" ");
		recstatus.setRequired(true);
		
		readOnly(true);
		form.add(menuname);
		form.add(recstatus);
		
		final FeedbackPanel feedback = new FeedbackPanel("feedback");
		add(feedback);
		add(new BookmarkablePageLink<Void>("homeLink", MenusPage.class));
		form.add(new PanelAction("actionpanel",this));
		app=this;
		ERPApplication.pageTitle="File | User Access";
	}
	public boolean updateMenus()
	{
		form.process(null);
		Menus item = (Menus)form.getModelObject();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String dateNow = sdf.format(new Date());
		item.setTglupdate(Long.parseLong(dateNow));
		item.setUserupdate(UserInfo.USERID);
		Object[] params=new Object[]{item};
    	Class[] retTypes =new Class[]{Boolean.class};
    	
    	Object[] response;
		try {
			response = _service.callServiceAccounting("updateMenus", params, retTypes);
			Boolean ret=(Boolean)response[0];
			if(ret)
			{
				setResponsePage(this);
				
			}
			return ret;
		} catch (AxisFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	public int delete() {
		// TODO Auto-generated method stub
		form.process(null);
		Menus item = (Menus)form.getModelObject();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String dateNow = sdf.format(new Date());
		item.setTglupdate(Long.parseLong(dateNow));
		item.setUserupdate(UserInfo.USERID);
		sf.file.Menus menu= (sf.file.Menus)item;
    	Object[] params=new Object[]{menu};
    	Class[] retTypes =new Class[]{Boolean.class};
    	
    	Object[] response;
		try {
			response = _service.callServiceAccounting("deleteMenus", params, retTypes);
			Boolean ret=(Boolean)response[0];
			if(ret)
			{
				setResponsePage(MenusInput.class);
				return 1;
			}
			return 0;
		} catch (AxisFault e) {
			// TODO Auto-generated catch block
			error("Data tidak berhasil disimpan\r\n" + e.getMessage());
		}
		return 2;
	}

	public int insert() {
		// TODO Auto-generated method stub
		form.process(null);
		Menus item = (Menus)form.getModelObject();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String dateNow = sdf.format(new Date());
		item.setTglupdate(Long.parseLong(dateNow));
		item.setUserupdate(UserInfo.USERID);
		item.setTglinput(item.getTglupdate());
		item.setUserinput(item.getUserupdate());
		
    	Object[] params=new Object[]{item};
    	Class[] retTypes =new Class[]{Boolean.class};
    	
    	Object[] response;
    	boolean duplicate=false;
		try {
			response = _service.callServiceAccounting("insertMenus", params, retTypes);
			Boolean ret=(Boolean)response[0];
			if(ret)
			{
				PageParameters pg=new PageParameters();
				pg.remove("error");
				info("Insert Menus has been successfully.");
				PanelAction.actionType=0;
				setResponsePage(MenusInput.class,pg);
				return 1;
			}
			error("Failed to insert Menus.");
			return 0;
		} catch (AxisFault e) {
			// TODO Auto-generated catch block
			if(e.getMessage().indexOf("Duplicate")>=0)
			{
				error(e.getMessage() + ",Click activate button if you would like to activate.");
				duplicate=true;
			}
			else
				error(e.getMessage());
		}
		return duplicate? 2: 0;
	}

	public int update() {
		// TODO Auto-generated method stub
		Boolean bRet=updateMenus();
		if(bRet)
		{
			info("Update Menus has been successfully.");
			return 1;
		}
		else
			error("Failed to update Menus.");
		return 0;
	}

	public void deleteAction() {
		// TODO Auto-generated method stub
		setResponsePage(this);
	}

	public void editAction() {
		// TODO Auto-generated method stub
		try
		{
			setResponsePage(this);
			readOnly(false);
			menuname.add(ro);
		}
		catch(Exception ex){}
	}

	public void newAction() {
		// TODO Auto-generated method stub
		PageParameters p= new PageParameters();
		p.add("new", 1);
		setResponsePage(MenusInput.class,p);
		try
		{
			readOnly(false);
		}
		catch(Exception ex){}
	}
	
	public void readOnly(Boolean flag)
	{
		if(!flag)
		{
			menuname.remove(ro);
			recstatus.remove(ro);

		}
		else
		{
			menuname.add(ro);
			recstatus.add(ro);
		}
	}

	public void readOnly() {
		// TODO Auto-generated method stub
		try
		{
			readOnly(true);
		}
		catch(Exception ex){}
	}

	public void cancel() {
		// TODO Auto-generated method stub
		this.readOnly();
	}

	public void activate() {
		// TODO Auto-generated method stub
		setResponsePage(MenusInput.class);
	}

	public void print() {
		// TODO Auto-generated method stub
		
	}

	public void cancel(boolean flag) {
		// TODO Auto-generated method stub
		setResponsePage(MenusInput.class);
		this.readOnly();
	}
}
