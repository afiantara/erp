package org.apache.wicket.erp.logistic;

import java.util.Date;

import org.apache.axis2.AxisFault;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.IClusterable;
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

import sf.inventory.Gudang;

import java.text.SimpleDateFormat;

public class GudangPage extends ERPWebPage implements IActionHandler{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	final Input input = new Input();
	private RequiredTextField<String> kode_gudang;
	private RequiredTextField<String> address1;
	private TextField<String> address2;
	private TextField<String> address3;
	private RequiredTextField<String> city;
	private TextField<String> postcode;
	private TextField<String> phone;
	private TextField<String> fax;
	private TextField<String> kepala_gudang;
	boolean isReadOnly=true;
	private Form<Gudang> form;
	final AttributeModifier ro = new AttributeModifier("readonly", isReadOnly);
	private Gudang gudang = new Gudang();
	public static GudangPage app;
	private Service _service;
	
	public String getTitle()
	{
		return "Gudang";
	}
	
	private void init()
	{
		try {
			_service=new Service(Service.INVENTORY_SERVICE_URL);
		} catch (AxisFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static GudangPage get()
	{
		return app;
	}
	
	public GudangPage(PageParameters p)
	{
		this(new Gudang());
		kode_gudang.remove(ro);
		address1.remove(ro);
		address2.remove(ro);
		address3.remove(ro);
		city.remove(ro);
		postcode.remove(ro);
		phone.remove(ro);
		fax.remove(ro);
		kepala_gudang.remove(ro);
	}
	public GudangPage()
	{
		this(new Gudang());
	}
	public GudangPage(Gudang gudang)
	{
		super("Logistic | Master Gudang");
		init();
		this.gudang = gudang;
		add(new GudangFilter("gudangfilter",this));
		form = new Form<Gudang>("form", new CompoundPropertyModel<Gudang>(this.gudang));
		add(form);
		
		// add a simple text field that uses Input's 'text' property. Nothing
		// can go wrong here
		kode_gudang=new RequiredTextField<String>("kgudang");//,new Model<String>(gudang.getKgudang()));
		address1=new RequiredTextField<String>("alamat1");//,new Model<String>(gudang.getAlamat1()));
		address2=new TextField<String>("alamat2");//,new Model<String>(gudang.getAlamat2()));
		address3=new TextField<String>("alamat3");//,new Model<String>(gudang.getAlamat3()));
		city=new RequiredTextField<String>("kota");//,new Model<String>(gudang.getKota()));
		postcode=new TextField<String>("kodepos");//,new Model<String>(gudang.getKodepos()));
		phone=new TextField<String>("notelp");//,new Model<String>(gudang.getNotelp()));
		fax=new TextField<String>("nofax");//,new Model<String>(gudang.getNofax()));
		kepala_gudang=new TextField<String>("kepgudang");//,new Model<String>(gudang.getKepgudang()));
		this.readOnly();
		form.add(kode_gudang);
		form.add(address1);
		form.add(address2);
		form.add(address3);
		form.add(city);
		form.add(postcode);
		form.add(phone);
		form.add(fax);
		form.add(kepala_gudang);
		final FeedbackPanel feedback = new FeedbackPanel("feedback");
		add(feedback);
		form.add(new PanelAction("actionpanel",this));
		app=this;
		ERPApplication.pageTitle="Logistic | Master Gudang";
	}
	
	/**
	 * Simple data class that acts as a holder for the data for the input fields.
	 */
	private static class Input implements IClusterable
	{
		// Normally we would have played nice and made it a proper JavaBean with
		// getters and
		// setters for its properties. But this is an example which we like to
		// keep small.

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		/** some plain text. */
		public String kode_gudang = "";
		public String address1 = "";
		public String address2 = "";
		public String address3 = "";
		public String city = "";
		public String postcode = "";
		/**
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString()
		{
			return "Kode Gudang = '" + kode_gudang + "',Alamat1= '" + address1 + "," + address2 + "," + address3 + ",City=" + city + 
				",PostCode= '" + postcode + "'";
		}
	}
	
	public boolean updateGudang()
	{
		form.process(null);
		Gudang gudang = (Gudang)form.getModelObject();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String dateNow = sdf.format(new Date());
		gudang.setTglupdate(Long.parseLong(dateNow));
		gudang.setUserupdate(UserInfo.USERID);
		
    	Object[] params=new Object[]{gudang};
    	Class[] retTypes =new Class[]{Boolean.class};
    	
    	Object[] response;
		try {
			response = _service.callServiceInventory("updateGudang", params, retTypes);
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
		Gudang gudang = (Gudang)form.getModelObject();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String dateNow = sdf.format(new Date());
		gudang.setTglupdate(Long.parseLong(dateNow));
		gudang.setUserupdate(UserInfo.USERID);
		
    	Object[] params=new Object[]{gudang};
    	Class[] retTypes =new Class[]{Boolean.class};
    	
    	Object[] response;
		try {
			response = _service.callServiceInventory("deleteGudang", params, retTypes);
			Boolean ret=(Boolean)response[0];
			if(ret)
			{
				setResponsePage(GudangPage.class);
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
		Gudang gudang = (Gudang)form.getModelObject();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String dateNow = sdf.format(new Date());
		gudang.setTglupdate(Long.parseLong(dateNow));
		gudang.setUserupdate(UserInfo.USERID);
		
    	Object[] params=new Object[]{gudang};
    	Class[] retTypes =new Class[]{Boolean.class};
    	
    	Object[] response;
    	boolean duplicate=false;
		try {
			response = _service.callServiceInventory("insertGudang", params, retTypes);
			Boolean ret=(Boolean)response[0];
			if(ret)
			{
				setResponsePage(GudangPage.class);
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
		Boolean bRet=updateGudang();
		if(bRet)
		{
			info("Update Gudang has been successfully.");
			return 1;
		}
		else
			error("Failed to update Gudang.");
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
			kepala_gudang.remove(ro);
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
			kode_gudang.remove(ro);
			address1.remove(ro);
			address2.remove(ro);
			address3.remove(ro);
			city.remove(ro);
			postcode.remove(ro);
			phone.remove(ro);
			fax.remove(ro);
			kepala_gudang.remove(ro);
		}
		catch(Exception ex){}
	}

	public void readOnly() {
		// TODO Auto-generated method stub
		try
		{
			kode_gudang.add(ro);
			address1.add(ro);
			address2.add(ro);
			address3.add(ro);
			city.add(ro);
			postcode.add(ro);
			phone.add(ro);
			fax.add(ro);
			kepala_gudang.add(ro);
		}
		catch(Exception ex){}
	}

	public void activate() {
		// TODO Auto-generated method stub
		setResponsePage(GudangPage.class);
	}

	public void print() {
		// TODO Auto-generated method stub
		
	}

	public void cancel(boolean flag) {
		// TODO Auto-generated method stub
		if(flag)
			setResponsePage(new GudangPage(GudangFilter.selectedGudang));
		else
			setResponsePage(GudangPage.class);
	}
}
