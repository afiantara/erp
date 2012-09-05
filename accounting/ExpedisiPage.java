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
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RadioChoice;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import sf.accounting.Expedisi;

import java.text.SimpleDateFormat;

public class ExpedisiPage extends ERPWebPage implements IActionHandler{
	/**
	 * 
	 */
	private static final List<String> KODEVIA = Arrays
	.asList(new String[] { "Darat", "Laut","Udara"});
	
	private static final long serialVersionUID = 1L;
	private RequiredTextField<String> kexpedisi;
	private RequiredTextField<String> nexpedisi;
	private RequiredTextField<String> alamat1;
	private RequiredTextField<String> kota;

	private TextField<String> alamat2;
	private TextField<String> kodepos;
	private TextField<String> notelp;
	private TextField<String> nofax;
	private TextField<String> email;
	private TextField<String> kontak1;
	private TextField<String> kontak2;
	private TextField<String> nohp1;
	private TextField<String> nohp2;
	
	private RadioChoice<String> kodeviadesc;
	
	boolean isReadOnly=true;
	private Form<Expedisi> form;
	final AttributeModifier ro = new AttributeModifier("readonly", isReadOnly);
	private Expedisi exp = new Expedisi();
	private static ExpedisiPage app;
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
	
	public static ExpedisiPage get()
	{
		return app;
	}
	
	public ExpedisiPage(PageParameters p)
	{
		this(new Expedisi());
		readOnly(false);
	}
	public ExpedisiPage()
	{
		this(new Expedisi());
	}
	public ExpedisiPage(Expedisi exp)
	{
		super("Accounting | Expedisi");
		init();
		this.exp = exp;
		add(new ExpedisiFilter("expedisifilter",this));
		form = new Form<Expedisi>("form", new CompoundPropertyModel<Expedisi>(this.exp));
		add(form);
		
		// add a simple text field that uses Input's 'text' property. Nothing
		// can go wrong here
		kexpedisi=new RequiredTextField<String>("kexpedisi");
		nexpedisi=new RequiredTextField<String>("nexpedisi");
		alamat1=new RequiredTextField<String>("alamat1");
		kota=new RequiredTextField<String>("kota");
		
		alamat2=new TextField<String>("alamat2");
		kodepos=new TextField<String>("kodepos");
		notelp=new TextField<String>("notelp");
		nofax=new TextField<String>("nofax");
		email=new TextField<String>("email");
		kontak1=new TextField<String>("kontak1");
		kontak2=new TextField<String>("kontak2");
		nohp1=new TextField<String>("nohp1");
		nohp2=new TextField<String>("nohp2");
		
		kodeviadesc=new RadioChoice<String>("kodeviadesc",KODEVIA);
		kodeviadesc.setSuffix(" ");
		kodeviadesc.setRequired(true);
		
		
		readOnly(true);
		
		form.add(kexpedisi);
		form.add(nexpedisi);
		form.add(alamat1);
		form.add(kota);
		form.add(alamat2);
		form.add(kodepos);
		form.add(notelp);
		form.add(nofax);
		form.add(email);
		form.add(kontak1);
		form.add(kontak2);
		form.add(nohp1);
		form.add(nohp2);
		form.add(kodeviadesc);
		
		final FeedbackPanel feedback = new FeedbackPanel("feedback");
		add(feedback);
		form.add(new PanelAction("actionpanel",this));
		app=this;
		ERPApplication.pageTitle="Accounting | Expedisi";
	}
	public boolean updateExpedisi()
	{
		form.process(null);
		Expedisi item = (Expedisi)form.getModelObject();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String dateNow = sdf.format(new Date());
		item.setTglupdate(Long.parseLong(dateNow));
		item.setUserupdate(UserInfo.USERID);
		item.setKodevia(item.getKodeviadesc().substring(0,1));
		
    	Object[] params=new Object[]{item};
    	Class[] retTypes =new Class[]{Boolean.class};
    	
    	Object[] response;
		try {
			response = _service.callServiceAccounting("updateEkspedisi", params, retTypes);
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
		Expedisi item = (Expedisi)form.getModelObject();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String dateNow = sdf.format(new Date());
		item.setTglupdate(Long.parseLong(dateNow));
		item.setUserupdate(UserInfo.USERID);
		
    	Object[] params=new Object[]{item};
    	Class[] retTypes =new Class[]{Boolean.class};
    	
    	Object[] response;
		try {
			response = _service.callServiceAccounting("deleteEkspedisi", params, retTypes);
			Boolean ret=(Boolean)response[0];
			if(ret)
			{
				PanelAction.actionType=0;
				setResponsePage(ExpedisiPage.class);
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
		Expedisi item = (Expedisi)form.getModelObject();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String dateNow = sdf.format(new Date());
		item.setTglupdate(Long.parseLong(dateNow));
		item.setUserupdate(UserInfo.USERID);
		item.setTglinput(item.getTglupdate());
		item.setUserinput(item.getUserupdate());
		
		item.setKodevia(item.getKodeviadesc().substring(0,1));
    	Object[] params=new Object[]{item};
    	Class[] retTypes =new Class[]{Boolean.class};
    	
    	Object[] response;
    	boolean duplicate=false;
		try {
			response = _service.callServiceAccounting("insertEkspedisi", params, retTypes);
			Boolean ret=(Boolean)response[0];
			if(ret)
			{
				//filter.getInvoice();// try to query 
				info("Insert invoice has been successfully.");
				setResponsePage(ExpedisiPage.class);
				return 1;
			}
			info("Failed to insert expedisi.");
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
		Boolean bRet=updateExpedisi();
		if(bRet)
		{
			info("Update Expedisi has been successfully.");
			return 1;
		}
		else
			error("Failed to update Expedisi.");
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
			kexpedisi.add(ro);
		}
		catch(Exception ex){}
	}

	public void newAction() {
		// TODO Auto-generated method stub
		PageParameters p= new PageParameters();
		p.add("new", 1);
		setResponsePage(ExpedisiPage.class,p);
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
			kexpedisi.remove(ro);
			nexpedisi.remove(ro);
			alamat1.remove(ro);
			kota.remove(ro);
			alamat2.remove(ro);
			kodepos.remove(ro);
			notelp.remove(ro);
			nofax.remove(ro);
			email.remove(ro);
			kontak1.remove(ro);
			kontak2.remove(ro);
			nohp1.remove(ro);
			nohp2.remove(ro);
			kodeviadesc.remove(ro);
		}
		else
		{
			kexpedisi.add(ro);
			nexpedisi.add(ro);
			alamat1.add(ro);
			kota.add(ro);
			alamat2.add(ro);
			kodepos.add(ro);
			notelp.add(ro);
			nofax.add(ro);
			email.add(ro);
			kontak1.add(ro);
			kontak2.add(ro);
			nohp1.add(ro);
			nohp2.add(ro);
			kodeviadesc.add(ro);
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
		setResponsePage(ExpedisiPage.class);
	}

	public void print() {
		// TODO Auto-generated method stub
		
	}

	public void cancel(boolean flag) {
		// TODO Auto-generated method stub
		if(flag)
			setResponsePage(new ExpedisiPage(ExpedisiFilter.selectedItem));
		else
			setResponsePage(ExpedisiPage.class);
		
		this.readOnly();
	}
}
