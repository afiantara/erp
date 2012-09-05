package org.apache.wicket.erp.accounting;

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
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RadioChoice;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import sf.accounting.Account;

import java.text.SimpleDateFormat;

public class COAPage extends ERPWebPage implements IActionHandler{
	/**
	 * 
	 */
	private static final List<String> GROUP = Arrays
	.asList(new String[] { "Aktiva", "Kewajiban","Modal","Biaya","Pendapatan"});
	
	private static final List<String> TYPES = Arrays
	.asList(new String[] { "Kas", "Bank","Non K/B"});
	
	private static final List<String> LEVELS = Arrays
	.asList(new String[] { "Header", "Detail (Journal Account)"});
	
	private static final long serialVersionUID = 1L;
	private RequiredTextField<String> accno;
	private RequiredTextField<String> accdesc;

	private TextField<String> kodein;
	private TextField<String> kodeout;
	private TextField<String> noin;
	private TextField<String> noout;
	
	private RadioChoice<String> accgroupdesc;
	private RadioChoice<String> acctypedesc;
	private RadioChoice<String> accleveldesc;
	
	private CheckBox accbiayab;
	private CheckBox accbiayas;
	private CheckBox accbiayap;
	private CheckBox accbiayak;
	
	boolean isReadOnly=true;
	private Form<Account> form;
	final AttributeModifier ro = new AttributeModifier("readonly", isReadOnly);
	private Account exp = new Account();
	private static COAPage app;
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
	
	public static COAPage get()
	{
		return app;
	}
	
	public COAPage(PageParameters p)
	{
		this(new Account());
		readOnly(false);
	}
	public COAPage()
	{
		this(new Account());
	}
	public COAPage(Account exp)
	{
		super("Accounting | Account No");
		init();
		this.exp = exp;
		add(new COAFilter("coafilter",this));
		form = new Form<Account>("form", new CompoundPropertyModel<Account>(this.exp));
		add(form);
		
		// add a simple text field that uses Input's 'text' property. Nothing
		// can go wrong here
		accno=new RequiredTextField<String>("accno");
		accdesc=new RequiredTextField<String>("accdesc");
		
		kodein=new TextField<String>("kodein");
		kodeout=new TextField<String>("kodeout");
		noin=new TextField<String>("noin");
		noout=new TextField<String>("noout");
				
		accgroupdesc=new RadioChoice<String>("accgroupdesc",GROUP);
		accgroupdesc.setSuffix(" ");
		accgroupdesc.setRequired(true);
		
		acctypedesc=new RadioChoice<String>("acctypedesc",TYPES);
		acctypedesc.setSuffix(" ");
		acctypedesc.setRequired(true);
		
		accleveldesc=new RadioChoice<String>("accleveldesc",LEVELS);
		accleveldesc.setSuffix(" ");
		accleveldesc.setRequired(true);
		
		accbiayab=new CheckBox("boolaccbiayab");
		accbiayap=new CheckBox("boolaccbiayap");
		accbiayas=new CheckBox("boolaccbiayas");
		accbiayak=new CheckBox("boolaccbiayak");
		readOnly(true);
		
		form.add(accno);
		form.add(accdesc);
		form.add(kodein);
		form.add(kodeout);
		form.add(noin);
		form.add(noout);
		form.add(accgroupdesc);
		form.add(acctypedesc);
		form.add(accleveldesc);
		form.add(accbiayab);
		form.add(accbiayap);
		form.add(accbiayas);
		form.add(accbiayak);
		
		final FeedbackPanel feedback = new FeedbackPanel("feedback");
		add(feedback);
		form.add(new PanelAction("actionpanel",this));
		app=this;
		ERPApplication.pageTitle="Accounting | Account No";
	}
	public boolean updateCOA()
	{
		form.process(null);
		Account item = (Account)form.getModelObject();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String dateNow = sdf.format(new Date());
		item.setTglupdate(Long.parseLong(dateNow));
		item.setUserupdate(UserInfo.USERID);
		
		item.setAccno(item.getAccno().replace(".", ""));
		item.setAccgroup(item.getAccgroupdesc().substring(0,1));
		item.setAcclevel(item.getAccleveldesc().substring(0,1));
		item.setAcctype(item.getAcctypedesc().substring(0,1));
		
		if(item.isBoolaccbiayab())
			item.setAccbiayab("Y");
		else
			item.setAccbiayab("N");
		
		if(item.isBoolaccbiayas())
			item.setAccbiayas("Y");
		else
			item.setAccbiayas("N");
		
		if(item.isBoolaccbiayap())
			item.setAccbiayap("Y");
		else
			item.setAccbiayap("N");
		
		if(item.isBoolaccbiayak())
			item.setAccbiayak("Y");
		else
			item.setAccbiayak("N");
		
    	Object[] params=new Object[]{item};
    	Class[] retTypes =new Class[]{Boolean.class};
    	
    	Object[] response;
		try {
			response = _service.callServiceAccounting("updateCOA", params, retTypes);
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
		Account item = (Account)form.getModelObject();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String dateNow = sdf.format(new Date());
		item.setTglupdate(Long.parseLong(dateNow));
		item.setUserupdate(UserInfo.USERID);
		
    	Object[] params=new Object[]{item};
    	Class[] retTypes =new Class[]{Boolean.class};
    	
    	Object[] response;
		try {
			response = _service.callServiceAccounting("deleteCOA", params, retTypes);
			Boolean ret=(Boolean)response[0];
			if(ret)
			{
				PanelAction.actionType=0;
				setResponsePage(COAPage.class);
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
		Account item = (Account)form.getModelObject();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String dateNow = sdf.format(new Date());
		item.setTglupdate(Long.parseLong(dateNow));
		item.setUserupdate(UserInfo.USERID);
		item.setTglinput(item.getTglupdate());
		item.setUserinput(item.getUserupdate());
		item.setAccno(item.getAccno().replace(".", ""));
		item.setAccgroup(item.getAccgroupdesc().substring(0,1));
		item.setAcclevel(item.getAccleveldesc().substring(0,1));
		item.setAcctype(item.getAcctypedesc().substring(0,1));
		
		if(item.isBoolaccbiayab())
			item.setAccbiayab("Y");
		else
			item.setAccbiayab("N");
		
		if(item.isBoolaccbiayas())
			item.setAccbiayas("Y");
		else
			item.setAccbiayas("N");
		
		if(item.isBoolaccbiayap())
			item.setAccbiayap("Y");
		else
			item.setAccbiayap("N");
		
		if(item.isBoolaccbiayak())
			item.setAccbiayak("Y");
		else
			item.setAccbiayak("N");
		
    	Object[] params=new Object[]{item};
    	Class[] retTypes =new Class[]{Boolean.class};
    	
    	Object[] response;
    	boolean duplicate=false;
		try {
			response = _service.callServiceAccounting("insertCOA", params, retTypes);
			Boolean ret=(Boolean)response[0];
			if(ret)
			{
				//filter.getInvoice();// try to query 
				info("Insert Account has been successfully.");
				setResponsePage(COAPage.class);
				return 1;
			}
			info("Failed to insert Account.");
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
		Boolean bRet=updateCOA();
		if(bRet)
		{
			info("Update Account has been successfully.");
			return 1;
		}
		else
			error("Failed to update Account.");
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
			accno.add(ro);
		}
		catch(Exception ex){}
	}

	public void newAction() {
		// TODO Auto-generated method stub
		PageParameters p= new PageParameters();
		p.add("new", 1);
		setResponsePage(COAPage.class,p);
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
			accno.remove(ro);
			accdesc.remove(ro);
			kodein.remove(ro);
			kodeout.remove(ro);
			noin.remove(ro);
			noout.remove(ro);
			accgroupdesc.remove(ro);
			acctypedesc.remove(ro);
			accleveldesc.remove(ro);
			accbiayab.remove(ro);
			accbiayas.remove(ro);
			accbiayap.remove(ro);
			accbiayak.remove(ro);
		}
		else
		{
			accno.add(ro);
			accdesc.add(ro);
			kodein.add(ro);
			kodeout.add(ro);
			noin.add(ro);
			noout.add(ro);
			accgroupdesc.add(ro);
			acctypedesc.add(ro);
			accleveldesc.add(ro);
			accbiayab.add(ro);
			accbiayas.add(ro);
			accbiayap.add(ro);
			accbiayak.add(ro);
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
		setResponsePage(COAPage.class);
	}

	public void print() {
		// TODO Auto-generated method stub
		
	}

	public void cancel(boolean flag) {
		// TODO Auto-generated method stub
		if(flag)
			setResponsePage(new COAPage(COAFilter.selectedItem));
		else
			setResponsePage(COAPage.class);
		
		this.readOnly();
	}
}
