package org.apache.wicket.erp.accounting;

import java.text.SimpleDateFormat;
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
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import sf.accounting.Branch;


public class BranchPage extends ERPWebPage implements IActionHandler{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private RequiredTextField<String> kode_cabang;
	private RequiredTextField<String> address1;
	private TextField<String> address2;
	private TextField<String> address3;
	private RequiredTextField<String> city;
	private TextField<String> postcode;
	private TextField<String> phone;
	private TextField<String> fax;
	private RequiredTextField<String> kacab;
	boolean isReadOnly=true;
	private Form<Branch> form;
	final AttributeModifier ro = new AttributeModifier("readonly", isReadOnly);
	private Branch branch = new Branch();
	private static BranchPage app;
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
	
	public static BranchPage get()
	{
		return app;
	}
	
	public BranchPage(PageParameters p)
	{
		this(new Branch());
		kode_cabang.remove(ro);
		address1.remove(ro);
		address2.remove(ro);
		address3.remove(ro);
		city.remove(ro);
		postcode.remove(ro);
		phone.remove(ro);
		fax.remove(ro);
		kacab.remove(ro);
	}
	public BranchPage()
	{
		this(new Branch());
	}
	public BranchPage(Branch branch)
	{
		super("Accounting | Master Cabang");
		init();
		this.branch = branch;
		add(new BranchFilter("branchfilter",this));
		form = new Form<Branch>("form", new CompoundPropertyModel<Branch>(this.branch));
		add(form);
		
		// add a simple text field that uses Input's 'text' property. Nothing
		// can go wrong here
		kode_cabang=new RequiredTextField<String>("kcabang");
		address1=new RequiredTextField<String>("alamat1");
		address2=new TextField<String>("alamat2");
		address3=new TextField<String>("alamat3");
		city=new RequiredTextField<String>("kota");
		postcode=new TextField<String>("kodepos");
		phone=new TextField<String>("notelp");
		fax=new TextField<String>("nofax");
		kacab=new RequiredTextField<String>("kacab");
		this.readOnly();
		form.add(kode_cabang);
		form.add(address1);
		form.add(address2);
		form.add(address3);
		form.add(city);
		form.add(postcode);
		form.add(phone);
		form.add(fax);
		form.add(kacab);
		final FeedbackPanel feedback = new FeedbackPanel("feedback");
		add(feedback);
		form.add(new PanelAction("actionpanel",this));
		app=this;
		ERPApplication.pageTitle="Accounting | Master Cabang";
	}
	public boolean updateCabang()
	{
		form.process(null);
		Branch cabang = (Branch)form.getModelObject();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String dateNow = sdf.format(new Date());
		cabang.setTglupdate(Long.parseLong(dateNow));
		cabang.setUserupdate(UserInfo.USERID);
		
    	Object[] params=new Object[]{cabang};
    	Class[] retTypes =new Class[]{Boolean.class};
    	
    	Object[] response;
		try {
			response = _service.callServiceAccounting("updateBranch", params, retTypes);
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
		Branch cabang = (Branch)form.getModelObject();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String dateNow = sdf.format(new Date());
		cabang.setTglupdate(Long.parseLong(dateNow));
		cabang.setUserupdate(UserInfo.USERID);
		
    	Object[] params=new Object[]{cabang};
    	Class[] retTypes =new Class[]{Boolean.class};
    	
    	Object[] response;
		try {
			response = _service.callServiceAccounting("deleteBranch", params, retTypes);
			Boolean ret=(Boolean)response[0];
			if(ret)
			{
				setResponsePage(BranchPage.class);
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
		Branch cabang = (Branch)form.getModelObject();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String dateNow = sdf.format(new Date());
		cabang.setTglupdate(Long.parseLong(dateNow));
		cabang.setUserupdate(UserInfo.USERID);
		
    	Object[] params=new Object[]{cabang};
    	Class[] retTypes =new Class[]{Boolean.class};
    	
    	Object[] response;
    	boolean duplicate=false;
		try {
			response = _service.callServiceAccounting("insertBranch", params, retTypes);
			Boolean ret=(Boolean)response[0];
			if(ret)
			{
				setResponsePage(BranchPage.class);
				return 1;
			}
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
		Boolean bRet=updateCabang();
		if(bRet)
		{
			info("Update Branch has been successfully.");
			return 1;
		}
		else
			error("Failed to update Branch.");
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
			address1.remove(ro);
			address2.remove(ro);
			address3.remove(ro);
			city.remove(ro);
			postcode.remove(ro);
			phone.remove(ro);
			fax.remove(ro);
			kacab.remove(ro);
		}
		catch(Exception ex){}
	}

	public void newAction() {
		// TODO Auto-generated method stub
		PageParameters p= new PageParameters();
		p.add("new", 1);
		setResponsePage(this);
		try
		{
			kode_cabang.remove(ro);
			address1.remove(ro);
			address2.remove(ro);
			address3.remove(ro);
			city.remove(ro);
			postcode.remove(ro);
			phone.remove(ro);
			fax.remove(ro);
			kacab.remove(ro);
		}
		catch(Exception ex){}
	}

	public void readOnly() {
		// TODO Auto-generated method stub
		try
		{
			kode_cabang.add(ro);
			address1.add(ro);
			address2.add(ro);
			address3.add(ro);
			city.add(ro);
			postcode.add(ro);
			phone.add(ro);
			fax.add(ro);
			kacab.add(ro);
		}
		catch(Exception ex){}
	}

	public void cancel() {
		// TODO Auto-generated method stub
		this.readOnly();
	}

	public void activate() {
		// TODO Auto-generated method stub
		setResponsePage(BranchPage.class);
	}

	public void print() {
		// TODO Auto-generated method stub
		
	}

	public void cancel(boolean flag) {
		// TODO Auto-generated method stub
		if(flag)
			setResponsePage(new BranchPage(BranchFilter.selectedItem));
		else
			setResponsePage(BranchPage.class);
		
		this.readOnly();
	}
}
