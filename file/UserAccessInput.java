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
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RadioChoice;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import sf.accounting.User;
import sf.file.Menus;

import java.text.SimpleDateFormat;

public class UserAccessInput extends ERPWebPage implements IActionHandler{
	/**
	 * 
	 */
	private static final List<String> STATUS = Arrays
	.asList(new String[] { "Y", "N"});
	
	private static List<String> MENU = Arrays
	.asList(new String[] { ""});
	
	private static List<String> USERID = Arrays
	.asList(new String[] { ""});
	
	private static final long serialVersionUID = 1L;
	private DropDownChoice<String> userid;
	private DropDownChoice<String> menuname;
	private RadioChoice<String> recstatus;
	boolean isReadOnly=true;
	private Form<UserAccess> form;
	final AttributeModifier ro = new AttributeModifier("readonly", isReadOnly);
	private UserAccess exp = new UserAccess();
	private static UserAccessInput app;
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
	
	public static UserAccessInput get()
	{
		return app;
	}
	
	public UserAccessInput(PageParameters p)
	{
		this(new UserAccess());
		if(p.getNamedKeys().contains("error"))
			error(p.get("error"));
		if(p.getNamedKeys().contains("info"))
			info(p.get("info"));
		readOnly(false);
	}
	public UserAccessInput()
	{
		this(new UserAccess());
	}
	public UserAccessInput(UserAccess exp)
	{
		super("File | User Akses");
		init();
		getUser();
		getMenu();
		this.exp = exp;
		form = new Form<UserAccess>("form", new CompoundPropertyModel<UserAccess>(this.exp));
		add(form);
		
		// add a simple text field that uses Input's 'text' property. Nothing
		// can go wrong here
		userid=new DropDownChoice<String>("userid",USERID);
		userid.setRequired(true);
		menuname=new DropDownChoice<String>("menuname",MENU);
		menuname.setRequired(true);
		recstatus=new RadioChoice<String>("recstatus",STATUS);
		recstatus.setSuffix(" ");
		recstatus.setRequired(true);
		
		readOnly(true);
		
		form.add(userid);
		form.add(menuname);
		form.add(recstatus);
		
		final FeedbackPanel feedback = new FeedbackPanel("feedback");
		add(feedback);
		add(new BookmarkablePageLink<Void>("homeLink", UserAccessPage.class));
		form.add(new PanelAction("actionpanel",this));
		app=this;
		ERPApplication.pageTitle="File | User Access";
	}
	public boolean updateUserAccess()
	{
		form.process(null);
		UserAccess item = (UserAccess)form.getModelObject();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String dateNow = sdf.format(new Date());
		item.setTglupdate(Long.parseLong(dateNow));
		item.setUserupdate(UserInfo.USERID);
		Object[] params=new Object[]{item};
    	Class[] retTypes =new Class[]{Boolean.class};
    	
    	Object[] response;
		try {
			response = _service.callServiceAccounting("updateUserAccess", params, retTypes);
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
		UserAccess item = (UserAccess)form.getModelObject();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String dateNow = sdf.format(new Date());
		item.setTglupdate(Long.parseLong(dateNow));
		item.setUserupdate(UserInfo.USERID);
		sf.file.UserAccess ua = (sf.file.UserAccess)item;
    	Object[] params=new Object[]{ua};
    	Class[] retTypes =new Class[]{Boolean.class};
    	
    	Object[] response;
		try {
			response = _service.callServiceAccounting("deleteUserAccess", params, retTypes);
			Boolean ret=(Boolean)response[0];
			if(ret)
			{
				PageParameters pg=new PageParameters();
				pg.remove("error");
				info("Delete User Access has been successfully.");
				PanelAction.actionType=0;
				setResponsePage(UserAccessInput.class,pg);
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
		UserAccess item = (UserAccess)form.getModelObject();
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
			response = _service.callServiceAccounting("insertUserAccess", params, retTypes);
			Boolean ret=(Boolean)response[0];
			if(ret)
			{
				PageParameters pg=new PageParameters();
				pg.remove("error");
				info("Insert User Access has been successfully.");
				PanelAction.actionType=0;
				setResponsePage(UserAccessInput.class,pg);
				
				return 1;
			}
			error("Failed to insert User Access.");
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
		Boolean bRet=updateUserAccess();
		if(bRet)
		{
			info("Update User Access has been successfully.");
			return 1;
		}
		else
			error("Failed to update User Access.");
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
			userid.add(ro);
		}
		catch(Exception ex){}
	}

	public void newAction() {
		// TODO Auto-generated method stub
		PageParameters p= new PageParameters();
		p.add("new", 1);
		setResponsePage(UserAccessInput.class,p);
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
			userid.remove(ro);
			menuname.remove(ro);
			recstatus.remove(ro);

		}
		else
		{
			userid.add(ro);
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
		setResponsePage(UserAccessInput.class);
	}

	public void print() {
		// TODO Auto-generated method stub
		
	}

	public void cancel(boolean flag) {
		// TODO Auto-generated method stub
		setResponsePage(UserAccessInput.class);
		this.readOnly();
	}
	
	private String[] kodes=null ;
	private void getMenu()
	{
		Object[] params=new Object[]{null};
    	
    	Class[] retTypes =new Class[]{Menus.class};
    	try {
			Object[] response=_service.callServiceAccounting("getMenus", params,retTypes);
			Menus item = (Menus)response[0];
			if(item==null) return;
			if(item.getMenus()==null) return;
			
			int count=item.getMenus().length;
			kodes=new String[count];
			for(int i=0; i < count;i++)
			{
				Menus title=item.getMenus()[i];
				if(null!=title) 
				{
					kodes[i]=title.getMenuname();
				}
			}
			MENU=Arrays.
			asList(kodes);
		} catch (AxisFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void getUser()
	{
		Object[] params=new Object[]{null};
    	
    	Class[] retTypes =new Class[]{User.class};
    	try {
			Object[] response=_service.callServiceAccounting("getUsers", params,retTypes);
			User item = (User)response[0];
			if(item==null) return;
			if(item.getUsers()==null) return;
			
			int count=item.getUsers().length;
			kodes=new String[count];
			for(int i=0; i < count;i++)
			{
				User title=item.getUsers()[i];
				if(null!=title) 
				{
					kodes[i]=title.getKstaff();
				}
			}
			USERID=Arrays.
			asList(kodes);
		} catch (AxisFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
