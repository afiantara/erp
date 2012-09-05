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
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RadioChoice;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import sf.accounting.Account;
import sf.accounting.Invoice;

import java.text.SimpleDateFormat;

public class InvoicePage extends ERPWebPage implements IActionHandler{
	/**
	 * 
	 */
	private static final List<String> KENAPPN = Arrays
	.asList(new String[] { "Ya", "Tidak"});
	
	private static final List<String> KONFIRMPPN = Arrays
	.asList(new String[] { "Ya", "Tidak"});
	
	private static List<String> ACCSALES=Arrays.asList("");
	private static List<String> ACCPIUTANG=Arrays.asList("");
	private static List<String> ACCDP=Arrays.asList("");
	private static List<String> ACCPPN=Arrays.asList("");
	
	private static final long serialVersionUID = 1L;
	private RequiredTextField<String> kinvoice;
	private RequiredTextField<String> direksi;
	private TextField<String> keterangan;
	
	private DropDownChoice<String> accsales;
	private DropDownChoice<String> accpiutang;
	private DropDownChoice<String> accdp;
	private DropDownChoice<String> accppn;
	private RadioChoice<String> kenappn ;
	private RadioChoice<String> konfirmppn;
	
	boolean isReadOnly=true;
	private Form<Invoice> form;
	final AttributeModifier ro = new AttributeModifier("readonly", isReadOnly);
	private Invoice inv = new Invoice();
	private static InvoicePage app;
	private Service _service;
	private InvoiceFilterPage filter;
	private void init()
	{
		try {
			_service=new Service(Service.ACCOUNTING_SERVICE_URL);
		} catch (AxisFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static InvoicePage get()
	{
		return app;
	}
	
	public InvoicePage(PageParameters p)
	{
		this(new Invoice());
		readOnly(false);
	}
	public InvoicePage()
	{
		this(new Invoice());
	}
	public InvoicePage(Invoice inv)
	{
		super("Accounting | Invoice");
		init();
		getAccount();
		this.inv = inv;
		filter=new InvoiceFilterPage("invoicefilter",this);
		add(filter);
		form = new Form<Invoice>("form", new CompoundPropertyModel<Invoice>(this.inv));
		add(form);
		
		// add a simple text field that uses Input's 'text' property. Nothing
		// can go wrong here
		kinvoice=new RequiredTextField<String>("kinvoice");
		keterangan=new TextField<String>("keterangan");
		direksi=new RequiredTextField<String>("direksi");
		
		kenappn=new RadioChoice<String>("kenappndesc",KENAPPN);
		kenappn.setSuffix(" ");
		kenappn.setRequired(true);
		
		konfirmppn=new RadioChoice<String>("konfirmppndesc",KONFIRMPPN);
		konfirmppn.setSuffix(" ");
		
		accsales=new DropDownChoice<String>("accsalesdesc",ACCSALES);
		accsales.setRequired(true);
		accpiutang=new DropDownChoice<String>("accpiutangdesc",ACCPIUTANG);
		accpiutang.setRequired(true);
		accdp=new DropDownChoice<String>("accdpdesc",ACCDP);
		accdp.setRequired(true);
		accppn=new DropDownChoice<String>("accppndesc",ACCPPN);
		accppn.setRequired(true);
		readOnly(true);
		
		form.add(kinvoice);
		form.add(keterangan);
		form.add(direksi);
		form.add(kenappn);
		form.add(konfirmppn);
		form.add(accsales);
		form.add(accpiutang);
		form.add(accdp);
		form.add(accppn);
		
		final FeedbackPanel feedback = new FeedbackPanel("feedback");
		add(feedback);
		form.add(new PanelAction("actionpanel",this));
		app=this;
		ERPApplication.pageTitle="Accounting | Invoice";
	}
	public boolean updateInvoice()
	{
		form.process(null);
		Invoice item = (Invoice)form.getModelObject();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String dateNow = sdf.format(new Date());
		item.setTglupdate(Long.parseLong(dateNow));
		item.setUserupdate(UserInfo.USERID);
		item.setKenappn(item.getKenappn().substring(0,1));
		item.setKonfirmppn(item.getKonfirmppn().substring(0,1));
		String[] codes = item.getAccsalesdesc().split("\\-");
		item.setAccsales(codes[0]);
		codes = item.getAccpiutangdesc().split("\\-");
		item.setAccpiutang(codes[0]);
		codes = item.getAccdpdesc().split("\\-");
		item.setAccdp(codes[0]);
		codes = item.getAccppndesc().split("\\-");
		item.setAccppn(codes[0]);
		
    	Object[] params=new Object[]{item};
    	Class[] retTypes =new Class[]{Boolean.class};
    	
    	Object[] response;
		try {
			response = _service.callServiceAccounting("updateInvoice", params, retTypes);
			Boolean ret=(Boolean)response[0];
			if(ret)
			{
				/*
				if(item.getKenappn().equals("Ya"))
					item.setKenappn("Y");
				else
					item.setKenappn("T");
				form.setModelObject(inv);
				System.out.println(inv.getKinvoice() + "," + inv.getKenappn() + "," + inv.getKonfirmppn() + "," + inv.getAccsales() + "," + inv.getAccpiutang() + "," + inv.getAccdp() + "," + inv.getAccppn());
				*/
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
		Invoice item = (Invoice)form.getModelObject();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String dateNow = sdf.format(new Date());
		item.setTglupdate(Long.parseLong(dateNow));
		item.setUserupdate(UserInfo.USERID);
		
    	Object[] params=new Object[]{item};
    	Class[] retTypes =new Class[]{Boolean.class};
    	
    	Object[] response;
		try {
			response = _service.callServiceAccounting("deleteInvoice", params, retTypes);
			Boolean ret=(Boolean)response[0];
			if(ret)
			{
				PanelAction.actionType=0;
				setResponsePage(InvoicePage.class);
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
		Invoice item = (Invoice)form.getModelObject();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String dateNow = sdf.format(new Date());
		item.setTglupdate(Long.parseLong(dateNow));
		item.setUserupdate(UserInfo.USERID);
		item.setTglinput(item.getTglupdate());
		item.setUserinput(item.getUserupdate());
		
		item.setKenappn(item.getKenappn().substring(0,1));
		item.setKonfirmppn(item.getKonfirmppn().substring(0,1));
		String[] codes = item.getAccsalesdesc().split("\\-");
		item.setAccsales(codes[0]);
		codes = item.getAccpiutangdesc().split("\\-");
		item.setAccpiutang(codes[0]);
		codes = item.getAccdpdesc().split("\\-");
		item.setAccdp(codes[0]);
		codes = item.getAccppndesc().split("\\-");
		item.setAccppn(codes[0]);
    	Object[] params=new Object[]{item};
    	Class[] retTypes =new Class[]{Boolean.class};
    	
    	Object[] response;
    	boolean duplicate=false;
		try {
			response = _service.callServiceAccounting("insertInvoice", params, retTypes);
			Boolean ret=(Boolean)response[0];
			if(ret)
			{
				//filter.getInvoice();// try to query 
				info("Insert invoice has been successfully.");
				setResponsePage(InvoicePage.class);
				return 1;
			}
			info("Failed to insert invoice.");
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
		Boolean bRet=updateInvoice();
		if(bRet)
		{
			info("Update Invoice has been successfully.");
			return 1;
		}
		else
			error("Failed to update Invoice.");
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
			kinvoice.add(ro);
		}
		catch(Exception ex){}
	}

	public void newAction() {
		// TODO Auto-generated method stub
		PageParameters p= new PageParameters();
		p.add("new", 1);
		setResponsePage(InvoicePage.class,p);
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
			kinvoice.remove(ro);
			keterangan.remove(ro);
			direksi.remove(ro);
			kenappn.remove(ro);
			konfirmppn.remove(ro);
			
			accsales.remove(ro);
			accpiutang.remove(ro);
			accdp.remove(ro);
			accppn.remove(ro);
		}
		else
		{
			kinvoice.add(ro);
			keterangan.add(ro);
			direksi.add(ro);
			kenappn.add(ro);
			konfirmppn.add(ro);
			
			accsales.add(ro);
			accpiutang.add(ro);
			accdp.add(ro);
			accppn.add(ro);
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
		setResponsePage(InvoicePage.class);
	}

	public void print() {
		// TODO Auto-generated method stub
		
	}

	public void cancel(boolean flag) {
		// TODO Auto-generated method stub
		if(flag)
			setResponsePage(new InvoicePage(InvoiceFilterPage.selectedItem));
		else
			setResponsePage(InvoicePage.class);
		
		this.readOnly();
	}
	
	private String[] kodes=null ;
	private void getAccount()
    {
		Object[] params=new Object[]{"D"};//level D
    	
    	Class[] retTypes =new Class[]{Account.class};
    	try {
    		
    		Object[] response=_service.callServiceAccounting("getAccountByLevel", params,retTypes);;
			Account item = (Account)response[0];
			if(item==null) return;
			if(item.getAccounts()==null) return;
			int count=item.getAccounts().length;
			kodes=new String[count];
			for(int i=0; i < count;i++)
			{
				Account account=item.getAccounts()[i];
				if(null!=account) 
				{
					kodes[i]=account.getAccno()+ "-" + account.getAccdesc();
				}
			}
			ACCSALES=Arrays.
			asList(kodes);
			ACCPIUTANG=Arrays.
			asList(kodes);
			ACCDP=Arrays.
			asList(kodes);
			ACCPPN=Arrays.
			asList(kodes);
		} catch (AxisFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
