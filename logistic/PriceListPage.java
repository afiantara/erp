package org.apache.wicket.erp.logistic;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import org.apache.axis2.AxisFault;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.erp.ERPApplication;
import org.apache.wicket.erp.ERPWebPage;
import org.apache.wicket.erp.utils.IActionHandlerIndex;
import org.apache.wicket.erp.utils.PanelAction;
import org.apache.wicket.erp.utils.Service;
import org.apache.wicket.erp.utils.UserInfo;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.link.PopupSettings;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import sf.accounting.Valuta;
import sf.inventory.HargaJual;
import sf.inventory.HargaPokok;

import java.text.SimpleDateFormat;

public class PriceListPage extends ERPWebPage implements IActionHandlerIndex {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static List<String> VALUTAS = Arrays
	.asList(new String[] { ""});
	
	private RequiredTextField<String> kbarang;
	private RequiredTextField<String> nbarang;
	private RequiredTextField<String> satuan;
	private RequiredTextField<String> ktanggaljual;
	private DropDownChoice<String> kvalutajual;
	private TextField<String> nvalutajual;
	private TextField<String> potonganjual;
	private RequiredTextField<String> ktanggalpokok;
	private DropDownChoice<String> kvalutapokok;
	private TextField<String> nvalutapokok;
	private TextField<String> potonganpokok;
	private Link hargajuallist;
	private Link hargapokoklist;
	private Link uploadhargajual;
	private Link uploadhargapokok;
	boolean isReadOnly=true;
	private Form<HargaJual> form;
	private Form<HargaPokok> form1;
	final AttributeModifier ro = new AttributeModifier("readonly", isReadOnly);
	private HargaJual hargaJual = new HargaJual();
	private HargaPokok hargaPokok = new HargaPokok();
	private static PriceListPage app;
	private Service _service;
	private Service _service1;
	
	public String kValutaJual;
	public String kValutaPokok;
	
	private void init()
	{
		try {
			_service=new Service(Service.INVENTORY_SERVICE_URL);
			_service1=new Service(Service.ACCOUNTING_SERVICE_URL);
		} catch (AxisFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static PriceListPage get()
	{
		return app;
	}
	
	public PriceListPage(PageParameters p)
	{
		this(new HargaJual(),new HargaPokok());
		readOnly(false);
		if(p.getNamedKeys().contains("error"))
			error(p.get("error"));
		if(p.getNamedKeys().contains("info"))
			info(p.get("info"));
		
	}
	public PriceListPage()
	{
		this(new HargaJual(),new HargaPokok());
	}
	
	public PriceListPage(HargaJual hargajual,HargaPokok hargapokok)
	{
		super("Logistic | Price List");
		init();
		getKvaluta();
		this.hargaJual = hargajual;
		this.hargaPokok=hargapokok;
		kValutaJual = hargajual.getKvaluta();
		kValutaPokok= hargapokok.getKvaluta();
		add(new PriceListFilter("pricelistfilter",this));
		form = new Form<HargaJual>("form", new CompoundPropertyModel<HargaJual>(this.hargaJual));
		add(form);
		form1 = new Form<HargaPokok>("form1", new CompoundPropertyModel<HargaPokok>(this.hargaPokok));
		add(form1);
		
		// add a simple text field that uses Input's 'text' property. Nothing
		// can go wrong here
		kbarang = new RequiredTextField<String>("kbarang");
		kbarang.setOutputMarkupId(true);
		nbarang = new RequiredTextField<String>("nbarang");
		nbarang.setOutputMarkupId(true);
		satuan= new RequiredTextField<String>("satuan");
		satuan.setOutputMarkupId(true);
		ktanggaljual= new RequiredTextField<String>("ktanggal");

		kvalutajual= new DropDownChoice<String>("kvaluta",VALUTAS);
		kvalutajual.setOutputMarkupId(true);
		nvalutajual= new TextField<String>("nvaluta");
		potonganjual= new TextField<String>("potongan");
		
		PopupSettings popupSettings = new PopupSettings(PopupSettings.LOCATION_BAR | PopupSettings.RESIZABLE | PopupSettings.SCROLLBARS).
		setHeight(300).setWidth(550).setTop(300).setLeft(180);
		
		hargajuallist=new Link("hargajuallist")
		{
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				// TODO Auto-generated method stub
				getSession().clear();
				PageParameters p =new PageParameters();
				p.add("kbarang", kbarang.getModelObject());
				setResponsePage(PriceListHargaJual.class,p);
			}
		};
		hargajuallist.setPopupSettings(popupSettings);
		hargajuallist.setEnabled((PanelAction.actionType==2||PanelAction.actionType==3)? true:false);
		form.add(hargajuallist);
		
		ktanggalpokok= new RequiredTextField<String>("ktanggal");
		kvalutapokok= new DropDownChoice<String>("kvaluta",VALUTAS);
		kvalutapokok.setOutputMarkupId(true);
		nvalutapokok= new TextField<String>("nvaluta");
		potonganpokok= new TextField<String>("potongan");
		
		hargapokoklist=new Link("hargapokoklist")
		{
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				// TODO Auto-generated method stub
				getSession().clear();
				PageParameters p =new PageParameters();
				p.add("kbarang", kbarang.getModelObject());
				setResponsePage(PriceListHargaPokok.class,p);
			}
		};
		hargapokoklist.setPopupSettings(popupSettings);
		hargapokoklist.setEnabled((PanelAction.actionType==2||PanelAction.actionType==3)? true:false);
		form1.add(hargapokoklist);
		
		uploadhargajual=new Link("uploadhargajual")
		{
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				// TODO Auto-generated method stub
				getSession().clear();
				setResponsePage(UploadHargaJual.class);
			}
		};
		uploadhargajual.setPopupSettings(popupSettings);
		//uploadhargajual.setEnabled((PanelAction.actionType==2||PanelAction.actionType==3)? true:false);
		form.add(uploadhargajual);
		
		uploadhargapokok=new Link("uploadhargapokok")
		{
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				// TODO Auto-generated method stub
				getSession().clear();
				PageParameters p =new PageParameters();
				p.add("kbarang", kbarang.getModelObject());
				setResponsePage(PriceListHargaJual.class,p);
			}
		};
		uploadhargapokok.setPopupSettings(popupSettings);
		//uploadhargapokok.setEnabled((PanelAction.actionType==2||PanelAction.actionType==3)? true:false);
		form1.add(uploadhargapokok);
		this.readOnly();
		
		form.add(kbarang);
		form.add(nbarang);
		form.add(satuan);
		form.add(ktanggaljual);
		form.add(kvalutajual);
		form.add(nvalutajual);
		form.add(potonganjual);
		form.add(hargajuallist);
		form1.add(ktanggalpokok);
		form1.add(kvalutapokok);
		form1.add(nvalutapokok);
		form1.add(potonganpokok);
		form1.add(hargapokoklist);
		
		final FeedbackPanel feedback = new FeedbackPanel("feedback");
		add(feedback);
		form.add(new PanelAction("actionpanel",0,this));
		form1.add(new PanelAction("actionpanel1",1,this));
		app=this;
		ERPApplication.pageTitle="Logistic | Price List";
	}
	
	public boolean updateHargaJual()
	{
		form.process(null);
		HargaJual item = (HargaJual)form.getModelObject();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String dateNow = sdf.format(new Date());
		item.setTglupdate(Long.parseLong(dateNow));
		item.setUserupdate(UserInfo.USERID);
		
    	Object[] params=new Object[]{item};
    	Class[] retTypes =new Class[]{Boolean.class};
    	
    	Object[] response;
		try {
			response = _service.callServiceInventory("updateHargaJual", params, retTypes);
			Boolean ret=(Boolean)response[0];
			if(ret)
			{
				PanelAction.actionType=2;//set panel action with 2//edit and delete button is enabled.
				setResponsePage(new PriceListPage(PriceListFilter.selectedHargaJual,PriceListFilter.selectedHargaPokok));
			}
			return ret;
		} catch (AxisFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	public boolean updateHargaPokok()
	{
		form1.process(null);
		HargaPokok item = (HargaPokok)form1.getModelObject();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String dateNow = sdf.format(new Date());
		item.setTglupdate(Long.parseLong(dateNow));
		item.setUserupdate(UserInfo.USERID);
		
    	Object[] params=new Object[]{item};
    	Class[] retTypes =new Class[]{Boolean.class};
    	
    	Object[] response;
		try {
			response = _service.callServiceInventory("updateHargaPokok", params, retTypes);
			Boolean ret=(Boolean)response[0];
			if(ret)
			{
				PanelAction.actionType=2;//set panel action with 2//edit and delete button is enabled.
				setResponsePage(new PriceListPage(PriceListFilter.selectedHargaJual,PriceListFilter.selectedHargaPokok));
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
		return 0;
	}

	public int insert() {
		// TODO Auto-generated method stub
		return 0;
	}

	public int update() {
		// TODO Auto-generated method stub
		return 0;
	}

	public void deleteAction() {
		// TODO Auto-generated method stub
		setResponsePage(this);
	}

	public void editAction() {
		// TODO Auto-generated method stub
		
	}

	public void newAction() {
		// TODO Auto-generated method stub
		
	}

	private void readOnly(boolean flag)
	{
		if(!flag)
		{
			ktanggaljual.remove(ro);
			ktanggalpokok.remove(ro);
			kvalutajual.remove(ro);
			nvalutajual.remove(ro);
			kvalutapokok.remove(ro);
			nvalutapokok.remove(ro);
			potonganjual.remove(ro);
			potonganpokok.remove(ro);
		}
		else
		{
			kbarang.add(ro);
			nbarang.add(ro);
			satuan.add(ro);
			ktanggaljual.add(ro);
			ktanggalpokok.add(ro);
			kvalutajual.add(ro);
			kvalutapokok.add(ro);
			nvalutajual.add(ro);
			nvalutapokok.add(ro);
			potonganjual.add(ro);
			potonganpokok.add(ro);
			
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

	public void cancel(boolean flag) {
		// TODO Auto-generated method stub
		
	}

	public void activate() {
		// TODO Auto-generated method stub
		setResponsePage(PriceListPage.class);
	}

	public void print() {
		// TODO Auto-generated method stub
		
	}
	
	
	public void activate(int idx) {
		// TODO Auto-generated method stub
		
	}

	public void cancel(int idx, boolean flag) {
		// TODO Auto-generated method stub
		if(flag)
			setResponsePage(new PriceListPage(PriceListFilter.selectedHargaJual,PriceListFilter.selectedHargaPokok));
		else
			setResponsePage(PriceListPage.class);
		
		this.readOnly();
	}

	public int delete(int idx) {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
		if(idx==0)
		{
			form.process(null);
			HargaJual hj = (HargaJual)form.getModelObject();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			String dateNow = sdf.format(new Date());
			hj.setTglupdate(Long.parseLong(dateNow));
			hj.setUserupdate(UserInfo.USERID);
		
			Object[] params=new Object[]{hj};
			Class[] retTypes =new Class[]{Boolean.class};
    	
	    	Object[] response;
			try {
				response = _service.callServiceInventory("deleteHargaJual", params, retTypes);
				Boolean ret=(Boolean)response[0];
				if(ret)
				{
					PageParameters pg=new PageParameters();
					pg.add("info", "Delete Harga Jual has been successfully.");
					pg.remove("error");
					PanelAction.actionType=0;
					setResponsePage(PriceListPage.class,pg);
					return 1;
				}
				return 0;
			} catch (AxisFault e) {
				// TODO Auto-generated catch block
				error("Data tidak berhasil disimpan\r\n" + e.getMessage());
			}
		}
		else
		{
			form1.process(null);
			HargaPokok hp = (HargaPokok)form1.getModelObject();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			String dateNow = sdf.format(new Date());
			hp.setTglupdate(Long.parseLong(dateNow));
			hp.setUserupdate(UserInfo.USERID);
		
			Object[] params=new Object[]{hp};
			Class[] retTypes =new Class[]{Boolean.class};
    	
	    	Object[] response;
			try {
				response = _service.callServiceInventory("deleteHargaPokok", params, retTypes);
				Boolean ret=(Boolean)response[0];
				if(ret)
				{
					PageParameters pg=new PageParameters();
					pg.add("info", "Delete Harga Jual has been successfully.");
					pg.remove("error");
					PanelAction.actionType=0;
					setResponsePage(PriceListPage.class,pg);
					return 1;
				}
				return 0;
			} catch (AxisFault e) {
				// TODO Auto-generated catch block
				error("Data tidak berhasil disimpan\r\n" + e.getMessage());
			}
		}
		return 2;
	}

	public void deleteAction(int idx) {
		
	}

	public void editAction(int idx) {
		// TODO Auto-generated method stub
		try
		{
			setResponsePage(this);
			if(idx==0)
			{
				ktanggaljual.remove(ro);
				kvalutajual.remove(ro);
				nvalutajual.remove(ro);
				potonganjual.remove(ro);
			}
			else
			{
				ktanggalpokok.remove(ro);
				kvalutapokok.remove(ro);
				nvalutapokok.remove(ro);
				potonganpokok.remove(ro);
			}
		}
		catch(Exception ex){}
	}

	public int insert(int idx) {
		// TODO Auto-generated method stub
		boolean duplicate=false;
		if(idx==0)
		{
			form.process(null);
			HargaJual item = (HargaJual)form.getModelObject();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			String dateNow = sdf.format(new Date());
			item.setTglupdate(Long.parseLong(dateNow));
			item.setUserupdate(UserInfo.USERID);
			item.setTglinput(item.getTglupdate());
			item.setUserinput(item.getUserupdate());
			
	    	Object[] params=new Object[]{item};
	    	Class[] retTypes =new Class[]{Boolean.class};
	    	
	    	Object[] response;
	    	
			try {
				response = _service.callServiceInventory("insertHargaJual", params, retTypes);
				Boolean ret=(Boolean)response[0];
				if(ret)
				{
					PageParameters pg=new PageParameters();
					pg.add("info", "Insert Harga Jual has been successfully.");
					pg.remove("error");
					setResponsePage(PriceListPage.class,pg);
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
		}
		else
		{
			form1.process(null);
			HargaPokok item = (HargaPokok)form1.getModelObject();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			String dateNow = sdf.format(new Date());
			item.setTglupdate(Long.parseLong(dateNow));
			item.setUserupdate(UserInfo.USERID);
			item.setTglinput(item.getTglupdate());
			item.setUserinput(item.getUserupdate());
			
	    	Object[] params=new Object[]{item};
	    	Class[] retTypes =new Class[]{Boolean.class};
	    	
	    	Object[] response;
			try {
				response = _service.callServiceInventory("insertHargaPokok", params, retTypes);
				Boolean ret=(Boolean)response[0];
				if(ret)
				{
					PageParameters pg=new PageParameters();
					pg.add("info", "Insert Harga Pokok has been successfully.");
					pg.remove("error");
					setResponsePage(PriceListPage.class,pg);
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
		}
		return duplicate ? 2: 0;
	}

	public void newAction(int idx) {
		// TODO Auto-generated method stub
		if(idx==0)
		{
			hargaJual=new HargaJual();
			if(PriceListFilter.selectedHargaJual!=null)
				hargaJual=PriceListFilter.selectedHargaJual;
			form.setModelObject(hargaJual);
			
		}
		else
		{
			hargaPokok=new HargaPokok();
			if(PriceListFilter.selectedHargaPokok!=null)
				hargaPokok=PriceListFilter.selectedHargaPokok;
			form1.setModelObject(hargaPokok);
		}
		this.readOnly(false);
	}

	public void print(int idx) {
		// TODO Auto-generated method stub
		
	}

	public void readOnly(int idx) {
		// TODO Auto-generated method stub
		
	}

	public int update(int idx) {
		// TODO Auto-generated method stub
		if(idx==0)
		{
			Boolean bRet=updateHargaJual();
			if(bRet)
			{
				info("Update Harga Jual has been successfully.");
				return 1;
			}
			else
				error("Failed to update Harga Jual.");
			return 0;
		}
		else
		{
			Boolean bRet=updateHargaPokok();
			if(bRet)
			{
				info("Update Harga Pokok has been successfully.");
				return 1;
			}
			else
				error("Failed to update Harga Pokok.");
			return 0;
		}
	}
	private String[] kodes;
	public void getKvaluta()
    {
    	Object[] params=new Object[]{null};
    	
    	Class[] retTypes =new Class[]{Valuta.class};
    	try {
			Object[] response=_service1.callServiceAccounting("getValutas", params,retTypes);
			Valuta item = (Valuta)response[0];
			if(item==null) return;
			if(item.getValutas()==null) return;
			int count=item.getValutas().length;
			kodes=new String[count];
			String[] satuan = new String[]{""};
			Vector<String> vSatuan = new Vector<String>();
			for(int i=0; i < count;i++)
			{
				Valuta acc=item.getValutas()[i];
				if(null!=acc) 
				{
					kodes[i]=acc.getKvaluta();
				}
			}
			VALUTAS=Arrays
			.asList(kodes);
			
		} catch (AxisFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
