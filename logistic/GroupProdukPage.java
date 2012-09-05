package org.apache.wicket.erp.logistic;

import java.util.Date;

import org.apache.axis2.AxisFault;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.erp.ERPApplication;
import org.apache.wicket.erp.ERPWebPage;
import org.apache.wicket.erp.utils.IActionHandler;
import org.apache.wicket.erp.utils.PanelAction;
import org.apache.wicket.erp.utils.Service;
import org.apache.wicket.erp.utils.UserInfo;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.link.PopupSettings;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import sf.inventory.Group;

import java.text.SimpleDateFormat;

public class GroupProdukPage extends ERPWebPage implements IActionHandler{
	/**
	 * 
	 */
	
	private static final long serialVersionUID = 1L;
	private RequiredTextField<String> kgroup;
	private RequiredTextField<String> ngroup;
	boolean isReadOnly=true;
	private Form<Group> form;
	final AttributeModifier ro = new AttributeModifier("readonly", isReadOnly);
	private Group exp = new Group();
	private static GroupProdukPage app;
	private Service _service;
	private Link subgrouplevel1Button;
	private void init()
	{
		try {
			_service=new Service(Service.INVENTORY_SERVICE_URL);
		} catch (AxisFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static GroupProdukPage get()
	{
		return app;
	}
	
	public GroupProdukPage(PageParameters p)
	{
		this(new Group());
		readOnly(false);
	}
	public GroupProdukPage()
	{
		this(new Group());
	}
	public GroupProdukPage(Group exp)
	{
		super("Logistic | Group Produk");
		init();
		this.exp = exp;
		add(new GroupProdukFilter("groupproductfilter",this));
		form = new Form<Group>("form", new CompoundPropertyModel<Group>(this.exp));
		add(form);
		
		// add a simple text field that uses Input's 'text' property. Nothing
		// can go wrong here
		kgroup=new RequiredTextField<String>("kgroup");
		ngroup=new RequiredTextField<String>("ngroup");
		readOnly(true);
		
		form.add(kgroup);
		form.add(ngroup);

		PopupSettings popupSettings = new PopupSettings(PopupSettings.LOCATION_BAR | PopupSettings.RESIZABLE | PopupSettings.SCROLLBARS).
		setHeight(300).setWidth(730).setTop(300).setLeft(180);
		
		subgrouplevel1Button=new Link("subgrouplevel1Button")
		{
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				// TODO Auto-generated method stub
				//setResponsePage(ModalKursHarianPage.class, new PageParameters());
				PageParameters p =new PageParameters();
				p.add("kgroup", kgroup.getModelObject());
				setResponsePage(SubLevel1Page.class,p);
			}
		};
		subgrouplevel1Button.setPopupSettings(popupSettings);
		subgrouplevel1Button.setEnabled((PanelAction.actionType==2||PanelAction.actionType==3)? true:false);
		form.add(subgrouplevel1Button);
		
		final FeedbackPanel feedback = new FeedbackPanel("feedback");
		add(feedback);
		form.add(new PanelAction("actionpanel",this));
		app=this;
		ERPApplication.pageTitle="Logistic | Group Produk";
	}
	public boolean updateGroup()
	{
		form.process(null);
		Group item = (Group)form.getModelObject();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String dateNow = sdf.format(new Date());
		item.setTglupdate(Long.parseLong(dateNow));
		item.setUserupdate(UserInfo.USERID);
		Object[] params=new Object[]{item};
    	Class[] retTypes =new Class[]{Boolean.class};
    	
    	Object[] response;
		try {
			response = _service.callServiceInventory("updateGroup", params, retTypes);
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
		Group item = (Group)form.getModelObject();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String dateNow = sdf.format(new Date());
		item.setTglupdate(Long.parseLong(dateNow));
		item.setUserupdate(UserInfo.USERID);
		
    	Object[] params=new Object[]{item};
    	Class[] retTypes =new Class[]{Boolean.class};
    	
    	Object[] response;
		try {
			response = _service.callServiceInventory("deleteGroup", params, retTypes);
			Boolean ret=(Boolean)response[0];
			if(ret)
			{
				PanelAction.actionType=0;
				setResponsePage(GroupProdukPage.class);
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
		Group item = (Group)form.getModelObject();
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
			response = _service.callServiceInventory("insertGroup", params, retTypes);
			Boolean ret=(Boolean)response[0];
			if(ret)
			{
				//filter.getInvoice();// try to query 
				info("Insert Group has been successfully.");
				setResponsePage(GroupProdukPage.class);
				return 1;
			}
			error("Failed to insert Group.");
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
		Boolean bRet=updateGroup();
		if(bRet)
		{
			info("Update Group Produk has been successfully.");
			return 1;
		}
		else
			error("Failed to update Group Produk.");
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
			kgroup.add(ro);
		}
		catch(Exception ex){}
	}

	public void newAction() {
		// TODO Auto-generated method stub
		PageParameters p= new PageParameters();
		p.add("new", 1);
		setResponsePage(GroupProdukPage.class,p);
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
			kgroup.remove(ro);
			ngroup.remove(ro);
		}
		else
		{
			kgroup.add(ro);
			ngroup.add(ro);
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
		setResponsePage(GroupProdukPage.class);
	}

	public void print() {
		// TODO Auto-generated method stub
		
	}

	public void cancel(boolean flag) {
		// TODO Auto-generated method stub
		if(flag)
			setResponsePage(new GroupProdukPage(GroupProdukFilter.selectedItem));
		else
			setResponsePage(GroupProdukPage.class);
		
		this.readOnly();
	}
}
