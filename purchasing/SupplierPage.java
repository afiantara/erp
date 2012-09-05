package org.apache.wicket.erp.purchasing;

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

import sf.purchasing.Supplier;
import sf.purchasing.Title;

import java.text.SimpleDateFormat;
public class SupplierPage extends ERPWebPage implements IActionHandler{
	/**
	 * 
	 */
	private static final List<String> STATUS = Arrays
	.asList(new String[] { "Supplier", "Cabang","Lain-lain"});
	
	private static List<String> TITLES = Arrays
	.asList(new String[] { ""});
	
	private static final List<String> KCOMPANy = Arrays
	.asList(new String[] { "EPP", "WKP"});
	
	private static final long serialVersionUID = 1L;
	private RequiredTextField<String> kvendor;
	private RequiredTextField<String> nvendor;
	private RequiredTextField<String> alamat1;
	private DropDownChoice<String> ktitel;
	private TextField<String> alamat2;
	private TextField<String> alamat3;
	private TextField<String> kota;
	private TextField<String> kodepos;
	private TextField<String> notelp;
	private TextField<String> nofax;
	private TextField<String> website;
	private TextField<String> email;
	private TextField<String> jtempo;
	private TextField<String> kontak1;
	private TextField<String> kontak2;
	private TextField<String> nohp1;
	private TextField<String> nohp2;
	private TextField<String> email1;
	private TextField<String> email2;
	private RadioChoice<String> kstatusdesc;
	private RadioChoice<String> kcompany;
	boolean isReadOnly=true;
	private Form<Supplier> form;
	final AttributeModifier ro = new AttributeModifier("readonly", isReadOnly);
	private Supplier exp = new Supplier();
	private static SupplierPage app;
	private Service _service;
	private void init()
	{
		try {
			_service=new Service(Service.PURCHASING_SERVICE_URL);
		} catch (AxisFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static SupplierPage get()
	{
		return app;
	}
	
	public SupplierPage(PageParameters p)
	{
		this(new Supplier());
		readOnly(false);
	}
	public SupplierPage()
	{
		this(new Supplier());
	}
	public SupplierPage(Supplier exp)
	{
		super("Purchasing | Supplier");
		exp.setKcompany(UserInfo.COMPANY);
		init();
		getTitel();
		this.exp = exp;
		add(new SupplierFilter("supplierfilter",this));
		form = new Form<Supplier>("form", new CompoundPropertyModel<Supplier>(this.exp));
		add(form);
		
		// add a simple text field that uses Input's 'text' property. Nothing
		// can go wrong here
		kvendor=new RequiredTextField<String>("kvendor");
		nvendor=new RequiredTextField<String>("nvendor");
		ktitel=new DropDownChoice<String>("ktitel",TITLES);
		ktitel.setRequired(true);
		
		alamat1=new RequiredTextField<String>("alamat1");
		
		alamat2=new TextField<String>("alamat2");
		alamat3=new TextField<String>("alamat3");
		kota=new TextField<String>("kota");
		kodepos=new TextField<String>("kodepos");
		notelp=new TextField<String>("notelp");
		nofax=new TextField<String>("nofax");
		website=new TextField<String>("website");
		email=new TextField<String>("email");
		jtempo=new TextField<String>("jtempo");
		kontak1=new TextField<String>("kontak1");
		kontak2=new TextField<String>("kontak2");
		nohp1=new TextField<String>("nohp1");
		nohp2=new TextField<String>("nohp2");
		email1=new TextField<String>("email1");
		email2=new TextField<String>("email2");
		
		kstatusdesc=new RadioChoice<String>("kstatusdesc",STATUS);
		kstatusdesc.setSuffix(" ");
		kstatusdesc.setRequired(true);
		
		kcompany=new RadioChoice<String>("kcompany",KCOMPANy);
		kcompany.setSuffix(" ");
		kcompany.setRequired(true);
		
		readOnly(true);
		
		form.add(kvendor);
		form.add(kcompany);
		form.add(nvendor);
		form.add(ktitel);
		form.add(alamat1);
		form.add(alamat2);
		form.add(alamat3);
		form.add(kota);
		form.add(kodepos);
		form.add(notelp);
		form.add(nofax);
		form.add(website);
		form.add(email);
		form.add(jtempo);
		form.add(kontak1);
		form.add(kontak2);
		form.add(nohp1);
		form.add(nohp2);
		form.add(email1);
		form.add(email2);
		form.add(kstatusdesc);
		
		final FeedbackPanel feedback = new FeedbackPanel("feedback");
		add(feedback);
		form.add(new PanelAction("actionpanel",this));
		app=this;
		ERPApplication.pageTitle="Purchasing | Supplier";
	}
	public boolean updateSupplier()
	{
		form.process(null);
		Supplier item = (Supplier)form.getModelObject();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String dateNow = sdf.format(new Date());
		item.setTglupdate(Long.parseLong(dateNow));
		item.setUserupdate(UserInfo.USERID);
		item.setKstatus(item.getKstatusdesc().substring(0,1));

		Object[] params=new Object[]{item};
    	Class[] retTypes =new Class[]{Boolean.class};
    	
    	Object[] response;
		try {
			response = _service.callServicePurchasing("updateSupplier", params, retTypes);
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
		Supplier item = (Supplier)form.getModelObject();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String dateNow = sdf.format(new Date());
		item.setTglupdate(Long.parseLong(dateNow));
		item.setUserupdate(UserInfo.USERID);
		
    	Object[] params=new Object[]{item,item.getKcompany()};
    	Class[] retTypes =new Class[]{Boolean.class};
    	
    	Object[] response;
		try {
			response = _service.callServicePurchasing("deleteSupplier", params, retTypes);
			Boolean ret=(Boolean)response[0];
			if(ret)
			{
				setResponsePage(SupplierPage.class);
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
		Supplier item = (Supplier)form.getModelObject();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String dateNow = sdf.format(new Date());
		item.setTglupdate(Long.parseLong(dateNow));
		item.setUserupdate(UserInfo.USERID);
		item.setTglinput(item.getTglupdate());
		item.setUserinput(item.getUserupdate());
		item.setKstatus(item.getKstatusdesc().substring(0,1));
		
		
    	Object[] params=new Object[]{item};
    	Class[] retTypes =new Class[]{Boolean.class};
    	
    	Object[] response;
    	boolean duplicate=false;
		try {
			response = _service.callServicePurchasing("insertSupplier", params, retTypes);
			Boolean ret=(Boolean)response[0];
			if(ret)
			{
				//filter.getInvoice();// try to query 
				info("Insert Supplier has been successfully.");
				setResponsePage(SupplierPage.class);
				return 1;
			}
			error("Failed to insert Supplier.");
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
		Boolean bRet=updateSupplier();
		if(bRet)
		{
			info("Update Supplier has been successfully.");
			return 1;
		}
		else
			error("Failed to update Supplier.");
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
			kvendor.add(ro);
		}
		catch(Exception ex){}
	}

	public void newAction() {
		// TODO Auto-generated method stub
		PageParameters p= new PageParameters();
		p.add("new", 1);
		setResponsePage(SupplierPage.class,p);
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
			kvendor.remove(ro);
			nvendor.remove(ro);
			ktitel.remove(ro);
			alamat1.remove(ro);

			alamat2.remove(ro);
			alamat3.remove(ro);
			kota.remove(ro);
			kodepos.remove(ro);
			notelp.remove(ro);
			nofax.remove(ro);
			website.remove(ro);
			email.remove(ro);
			jtempo.remove(ro);
			kontak1.remove(ro);
			kontak2.remove(ro);
			nohp1.remove(ro);
			nohp2.remove(ro);
			email1.remove(ro);
			email2.remove(ro);
			kstatusdesc.remove(ro);
		}
		else
		{
			kvendor.add(ro);
			nvendor.add(ro);
			ktitel.add(ro);
			alamat1.add(ro);

			alamat2.add(ro);
			alamat3.add(ro);
			kota.add(ro);
			kodepos.add(ro);
			notelp.add(ro);
			nofax.add(ro);
			website.add(ro);
			email.add(ro);
			jtempo.add(ro);
			kontak1.add(ro);
			kontak2.add(ro);
			nohp1.add(ro);
			nohp2.add(ro);
			email1.add(ro);
			email2.add(ro);
			kstatusdesc.add(ro);
		}
		kcompany.add(ro);
		kcompany.setEnabled(false);
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
		setResponsePage(SupplierPage.class);
	}

	public void print() {
		// TODO Auto-generated method stub
		
	}

	public void cancel(boolean flag) {
		// TODO Auto-generated method stub
		if(flag)
			setResponsePage(new SupplierPage(SupplierFilter.selectedItem));
		else
			setResponsePage(SupplierPage.class);
		
		this.readOnly();
	}
	
	private String[] kodes=null ;
	private void getTitel()
	{
		Object[] params=new Object[]{null};
    	
    	Class[] retTypes =new Class[]{Title.class};
    	try {
			Object[] response=_service.callServicePurchasing("getTitle", params,retTypes);
			Title item = (Title)response[0];
			if(item==null) return;
			if(item.getTitles()==null) return;
			
			int count=item.getTitles().length;
			kodes=new String[count];
			for(int i=0; i < count;i++)
			{
				Title title=item.getTitles()[i];
				if(null!=title) 
				{
					kodes[i]=title.getKtitel();
				}
			}
			TITLES=Arrays.
			asList(kodes);
		} catch (AxisFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
