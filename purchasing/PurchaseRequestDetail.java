package org.apache.wicket.erp.purchasing;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.apache.axis2.AxisFault;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.erp.logistic.Group1;
import org.apache.wicket.erp.logistic.Group2;
import org.apache.wicket.erp.utils.IActionHandler;
import org.apache.wicket.erp.utils.PanelAction;
import org.apache.wicket.erp.utils.Service;
import org.apache.wicket.erp.utils.UserInfo;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.ListDataProvider;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import sf.inventory.Produk;

public class PurchaseRequestDetail extends WebPage implements IActionHandler {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private RequiredTextField<String> nobukti;
	private RequiredTextField<String> tglbukti;
	private TextArea<String> keterangan;
	private TextField<String> approvedby;
	private TextField<String> approvedtgl;
	
	private DropDownChoice<String> kbarang;
	private RequiredTextField<String> jumlah;
	private DropDownChoice<String> satuan;
	private TextField<String> nobukti1;
	private TextArea<String> ket;
	private ArrayList<PRDetail> list=new ArrayList<PRDetail>();
	private HashMap<String, Produk> produks=new HashMap<String, Produk>();
	
	private final Map<String, List<String>> modelsMap = new HashMap<String, List<String>>();
	
	boolean isReadOnly=true;
	private Form<PRHeader> form;
	
	final AttributeModifier ro = new AttributeModifier("readonly", isReadOnly);
	private static PRHeader pheader = new PRHeader();
	
	private static PurchaseRequestDetail app;
	private Service _service;
	private Service _service1;
	public String selectedKBarang;
	public String selectedSatuan;
	private boolean isDisable=false;
	private void init()
	{
		try {
			_service=new Service(Service.PURCHASING_SERVICE_URL);
			_service1=new Service(Service.INVENTORY_SERVICE_URL);
		} catch (AxisFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static PurchaseRequestDetail get()
	{
		return app;
	}
	
	public PurchaseRequestDetail(PageParameters p,PRHeader ph)
	{
		this(ph);
		readOnly(false);
		if(p.getNamedKeys().contains("error"))
			error(p.get("error"));
		if(p.getNamedKeys().contains("info"))
			info(p.get("info"));
		
	}
	public PurchaseRequestDetail()
	{
		this(new PRHeader());
		
	}
	
	public PurchaseRequestDetail(PRHeader ph)
	{
		
		init();
		getProduk();
		PurchaseRequestDetail.pheader = ph;
		if(ph.getNobukti()==null || ph.getNobukti().equals(""))
		{
			PurchaseRequestDetail.pheader.setNobukti(getNoPR());
			selectedKBarang=null;
			selectedSatuan=null;
		}
		Produk p = produks.get(ph.getKbarang());
		if(p!=null)
		{
			selectedKBarang = p.getKbarang() + " " + p.getNbarang();
		}
		selectedSatuan = pheader.getSatuan();
		
		getPRequestDetail(ph.getNobukti());
		form = new Form<PRHeader>("form", new CompoundPropertyModel<PRHeader>(this.pheader));
		add(form);
		// add a simple text field that uses Input's 'text' property. Nothing
		// can go wrong here
		nobukti = new RequiredTextField<String>("nobukti");
		tglbukti= new RequiredTextField<String>("tglbukti");
		keterangan=new TextArea<String>("keterangan");
		approvedby= new TextField<String>("approvedby");
		approvedtgl= new TextField<String>("approvedtgl");
		
		//kbarang = new DropDownChoice<String>("kbarang",PRODUKS);
		
		
		IModel<List<? extends String>> modelChoices = new AbstractReadOnlyModel<List<? extends String>>()
		{
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public List<String> getObject()
			{
				return new ArrayList<String>(modelsMap.keySet());
			}

		};
		
		IModel<List<? extends String>> satuanChoices = new AbstractReadOnlyModel<List<? extends String>>()
		{
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public List<String> getObject()
			{
				List<String> models = modelsMap.get(selectedKBarang);
				if (models == null)
				{
					models = Collections.emptyList();
				}
				return models;
			}

		};
		kbarang=new DropDownChoice<String>("kbarang",
				 new PropertyModel<String>(this, "selectedKBarang"), modelChoices);
		kbarang.setRequired(true);
		
		kbarang.add(new AjaxFormComponentUpdatingBehavior("onchange")
			{
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;
	
				@Override
				protected void onUpdate(AjaxRequestTarget target)
				{
					target.add(satuan);
				}
			});
		 
		satuan=new DropDownChoice<String>("satuan",
				 new PropertyModel<String>(this, "selectedSatuan"), satuanChoices);
		
		satuan.setOutputMarkupId(true);
		jumlah = new RequiredTextField<String>("jumlah");
		nobukti1= new TextField<String>("nobukti1");
		ket= new TextArea<String>("ket");
		this.readOnly();
		
		form.add(nobukti);
		form.add(tglbukti);
		form.add(keterangan);
		form.add(approvedby);
		form.add(approvedtgl);
		
		form.add(kbarang);
		form.add(satuan);
		form.add(jumlah);
		form.add(nobukti1);
		form.add(ket);
		
		if(PurchaseRequestDetail.pheader.getApprovedby()!=null && !PurchaseRequestDetail.pheader.getApprovedby().equals(""))
		{
			isDisable=true;
			form.add(new PanelAction("actionpanel",true,this));
		}
		else
			form.add(new PanelAction("actionpanel",this));
		
		final DataView<Group2> dataView = new DataView<Group2>("pageable", new ListDataProvider(
                list)) {
            /**
					 * 
					 */
			private static final long serialVersionUID = 1L;

			public void populateItem(final Item item) {
                final PRDetail _hj = (PRDetail) item.getModelObject();
				item.add(new Label("no", String.valueOf(_hj.getNo())));
				item.add(new Label("kbarang", _hj.getKbarang()));
				item.add(new Label("nbarang", _hj.getNbarang()));
				item.add(new Label("satuan", _hj.getSatuan()));
				item.add(new Label("jumlah", String.valueOf(_hj.getJumlah())));
				item.add(new Label("nobukti1", _hj.getNobukti1()));
				item.add(new Label("ket", _hj.getKet()));
				item.add(new AjaxLink<Group1>("select")
				{
					/**
					 * 
					 */
					private static final long serialVersionUID = 1L;

					@Override
					public void onClick(AjaxRequestTarget target) {
						// TODO Auto-generated method stub
						PRDetail detail=(PRDetail)getParent().getDefaultModelObject();
						PanelAction.actionType=2;
						selectedKBarang = detail.getKbarang() + " " + detail.getNbarang();
						System.out.println("selectedKBarang: " + selectedKBarang);
						selectedSatuan=detail.getSatuan();
						System.out.println("selectedSatuan: " + selectedSatuan);
						pheader.setKbarang(selectedKBarang);
						pheader.setSatuan(selectedSatuan);
						pheader.setNobukti1(detail.getNobukti1());
						pheader.setKet(detail.getKet());
						pheader.setJumlah(detail.getJumlah());
						setResponsePage(new PurchaseRequestDetail(pheader));
					}
				}).setEnabled(!isDisable);
					
				item.add(AttributeModifier.replace("class", new AbstractReadOnlyModel<String>()
				{
					private static final long serialVersionUID = 1L;

					@Override
					public String getObject()
					{
						return (item.getIndex() % 2 == 1) ? "even" : "odd";
					}
				}));
            }
        };
        
		dataView.setItemsPerPage(8);
		add(dataView);

		add(new PagingNavigator("navigator", dataView));
		
		
		final FeedbackPanel feedback = new FeedbackPanel("feedback");
		add(feedback);
		add(new BookmarkablePageLink<Void>("homeLink", PurchaseRequestPage.class));
		app=this;
	}
	
	public boolean updatePRequest()
	{
		form.process(null);
		PRHeader item = (PRHeader)form.getModelObject();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String dateNow = sdf.format(new Date());
		item.setTglupdate(Long.parseLong(dateNow));
		item.setUserupdate(UserInfo.USERID);
		item.setTglupdate(Long.parseLong(dateNow));
		item.setUserupdate(UserInfo.USERID);
		
		sf.purchasing.PRDetail item1 = new sf.purchasing.PRDetail();
		Produk p = produks.get(selectedKBarang);
		item1.setNobukti(item.getNobukti());
		item1.setTglbukti(item.getTglbukti());
		item1.setKbarang(p.getKbarang());
		item1.setSatuan(selectedSatuan);
		item1.setNobukti1(item.getNobukti1());
		item1.setJumlah(item.getJumlah());
		item1.setKet(item.getKet());
		item1.setKcompany(item.getKcompany());
		Produk produk = produks.get(selectedKBarang);
		item1.setKgroup(produk.getKgroup());
		item1.setTglupdate(item.getTglupdate());
		item1.setUserupdate(item.getUserupdate());
    	Object[] params=new Object[]{item,item1};
    	Class[] retTypes =new Class[]{Boolean.class};
    	
    	Object[] response;
		try {
			response = _service.callServicePurchasing("updateSinglePR", params, retTypes);
			Boolean ret=(Boolean)response[0];
			if(ret)
			{
				PanelAction.actionType=2;//set panel action with 2//edit and delete button is enabled.
				setResponsePage(new PurchaseRequestDetail(pheader));
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
		PRHeader hp = (PRHeader)form.getModelObject();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String dateNow = sdf.format(new Date());
		hp.setTglupdate(Long.parseLong(dateNow));
		hp.setUserupdate(UserInfo.USERID);
		
		sf.purchasing.PRDetail detail = new sf.purchasing.PRDetail();
		detail.setTglupdate(Long.parseLong(dateNow));
		detail.setUserupdate(UserInfo.USERID);
		detail.setNobukti(hp.getNobukti());
		Produk prod = produks.get(selectedKBarang);
		if(prod!=null)
		{
			detail.setKbarang(prod.getKbarang());
		}
		Object[] params=new Object[]{detail};
		Class[] retTypes =new Class[]{Boolean.class};
	
    	Object[] response;
		try {
			response = _service.callServicePurchasing("deletePrequisitionDetail", params, retTypes);
			Boolean ret=(Boolean)response[0];
			if(ret)
			{
				PageParameters pg=new PageParameters();
				pg.add("info", "Delete Purchase Request has been successfully.");
				pg.remove("error");
				PanelAction.actionType=0;
				
				selectedKBarang=null;
				selectedSatuan=null;
				pheader.setKbarang(null);
				pheader.setSatuan(null);
				pheader.setKet("");
				pheader.setJumlah(0);
				pheader.setNobukti1("");
				
				setResponsePage(new PurchaseRequestDetail(pg, pheader));
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
		boolean duplicate=false;
		form.process(null);
		PRHeader item = (PRHeader)form.getModelObject();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String dateNow = sdf.format(new Date());
		item.setTglupdate(Long.parseLong(dateNow));
		item.setUserupdate(UserInfo.USERID);
		item.setTglinput(item.getTglupdate());
		item.setUserinput(item.getUserupdate());
		item.setKcompany(UserInfo.COMPANY);
		Produk produk = produks.get(selectedKBarang);
		PRDetail item1=new PRDetail();
		item1.setKbarang(produk.getKbarang());
		item1.setSatuan(selectedSatuan);
		item1.setNbarang(produk.getNbarang());
		item1.setJumlah(item.getJumlah());
		item1.setNobukti1(item.getNobukti1());
		item1.setKet(item.getKet());
		item1.setNobukti(item.getNobukti());
		item1.setTglbukti(item.getTglbukti());
		item1.setKgroup(produk.getKgroup());
		//item1.setKvaluta(produk.getKvaluta());
		//item1.setNvaluta(produk.getNvaluta());
		item1.setTglupdate(Long.parseLong(dateNow));
		item1.setUserupdate(UserInfo.USERID);
		item1.setTglinput(item.getTglupdate());
		item1.setUserinput(item.getUserupdate());
		item1.setKcompany(UserInfo.COMPANY);
		
    	Object[] params=new Object[]{item,item1};
    	Class[] retTypes =new Class[]{Boolean.class};
    	
    	Object[] response;
    	
		try {
			response = _service.callServicePurchasing("insertSinglePR", params, retTypes);
			Boolean ret=(Boolean)response[0];
			if(ret)
			{
				PageParameters pg=new PageParameters();
				pg.add("info", "Insert Purchase Request has been successfully.");
				pg.remove("error");
				pheader.setJumlah(0);
				pheader.setKet("");
				pheader.setNobukti1("");
				PanelAction.actionType=0;
				setResponsePage(new PurchaseRequestDetail(pg,pheader));
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
		return duplicate ? 2: 0;
	}

	public int update() {
		// TODO Auto-generated method stub
		Boolean bRet=updatePRequest();
		if(bRet)
		{
			info("Update Purchasing Request has been successfully.");
			return 1;
		}
		else
			error("Failed to update Purchasing Request.");
		return 0;
	}

	public void deleteAction() {
		// TODO Auto-generated method stub
		setResponsePage(this);
	}

	public void editAction() {
		// TODO Auto-generated method stub
		readOnly(false);
	}

	public void newAction() {
		// TODO Auto-generated method stub
		//PR-yyyy-999999-xxx
		try
		{
			selectedKBarang=null;
			selectedSatuan=null;
			pheader.setKbarang(null);
			pheader.setSatuan(null);
			pheader.setKet("");
			pheader.setJumlah(0);
			pheader.setNobukti1("");
			setResponsePage(new PurchaseRequestDetail(new PageParameters(),pheader));
		}
		catch(Exception ex){}
	}

	private void readOnly(boolean flag)
	{
		if(!flag)
		{
			//nobukti.remove(ro);
			tglbukti.remove(ro);
			keterangan.remove(ro);
			//approvedby.remove(ro);
			//approvedtgl.remove(ro);
			
			kbarang.remove(ro);
			jumlah.remove(ro);
			satuan.remove(ro);
			nobukti1.remove(ro);
			ket.remove(ro);
		}
		else
		{
			nobukti.add(ro);
			tglbukti.add(ro);
			keterangan.add(ro);
			approvedby.add(ro);
			approvedtgl.add(ro);
			
			kbarang.add(ro);
			jumlah.add(ro);
			satuan.add(ro);
			nobukti1.add(ro);
			ket.add(ro);
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
		
		selectedKBarang=null;
		selectedSatuan=null;
		pheader.setKbarang(null);
		pheader.setSatuan(null);
		pheader.setKet("");
		pheader.setJumlah(0);
		pheader.setNobukti1("");
		
		setResponsePage(this);
		/*
		if(flag)
			setResponsePage(new PurchaseRequestDetail(PriceListFilter.selectedHargaJual,PriceListFilter.selectedHargaPokok));
		else
			setResponsePage(PurchaseRequestDetail.class);
		*/
		this.readOnly();
	}

	public void activate() {
		// TODO Auto-generated method stub
		form.clearInput();
	}

	public void print() {
		// TODO Auto-generated method stub
		
	}
	
	
	public void activate(int idx) {
		// TODO Auto-generated method stub
		
	}

	public void cancel(int idx, boolean flag) {
		// TODO Auto-generated method stub
		
	}

	public int delete(int idx) {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
		
		return 2;
	}

	public void deleteAction(int idx) {
		
	}

	public void editAction(int idx) {
		// TODO Auto-generated method stub
		
	}

	public int insert(int idx) {
		// TODO Auto-generated method stub
		return 0;
	}

	public void newAction(int idx) {
		// TODO Auto-generated method stub
		if(idx==0)
		{
			
		}
		else
		{
			
			
			
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
			return 0;
		}
		else
		{
			return 0 ;
		}
	}
	
	private String[] kodes;
	public void getProduk()
    {
    	Object[] params=new Object[]{UserInfo.COMPANY};
    	
    	Class[] retTypes =new Class[]{Produk.class};
    	try {
			Object[] response=_service1.callServiceInventory("getProduk", params,retTypes);
			Produk item = (Produk)response[0];
			if(item==null) return;
			if(item.getProduks()==null) return;
			int count=item.getProduks().length;
			kodes=new String[count];
			String[] satuan = new String[]{""};
			Vector<String> vSatuan = new Vector<String>();
			if(produks!=null)
			{
				produks=null;
			}
			produks=new HashMap<String, Produk>();
			if(modelsMap!=null)
				modelsMap.clear();
			for(int i=0; i < count;i++)
			{
				Produk acc=item.getProduks()[i];
				if(null!=acc) 
				{
					kodes[i]=acc.getKbarang()+ " " + acc.getNbarang();
					produks.put(kodes[i], acc);
					vSatuan.add(acc.getSatuan());
					if(acc.getSatuan1()!=null)
					{
						vSatuan.add(acc.getSatuan1());
					}
					if(acc.getSatuan2()!=null)
					{
						vSatuan.add(acc.getSatuan2());
					}
					satuan=new String[vSatuan.size()];
					for(int idx=0;idx < vSatuan.size();idx++)
					{
						satuan[idx]=(String)vSatuan.get(idx);
					}
					modelsMap.put(kodes[i],  Arrays.asList(satuan));
					vSatuan.clear();
				}
			}
		} catch (AxisFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
	
	public String getNoPR()
    {
    	Object[] params=new Object[]{null};
    	
    	Class[] retTypes =new Class[]{Integer.class};
    	try {
			Object[] response=_service.callServicePurchasing("getNoPR", params,retTypes);
			Integer item = (Integer)response[0];
			//PR-yyyy-999999-xxx
			SimpleDateFormat sdf =new SimpleDateFormat("yyyy");
			DecimalFormat df=new DecimalFormat("000000");
			return "PR-" +sdf.format(new Date()) + "-" +df.format(item+=1) + "-" + UserInfo.COMPANY;
		} catch (AxisFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
    }
	
	private void getPRequestDetail(String nobukti)
    {
		sf.purchasing.PRHeader _header=new sf.purchasing.PRHeader();
		_header.setNobukti(nobukti);
    	Object[] params=new Object[]{_header};
    	
    	Class[] retTypes =new Class[]{PRDetail.class};
    	try {
			Object[] response=_service.callServicePurchasing("getPrequisitionDetail", params,retTypes);
			sf.purchasing.PRDetail item = (sf.purchasing.PRDetail)response[0];
			if(item==null) return;
			if(item.getDetails()==null) return;
			int count=item.getDetails().length;
			list = new ArrayList<PRDetail>();
			for(int i=0; i < count;i++)
			{
				sf.purchasing.PRDetail hj=item.getDetails()[i];
				PRDetail _hj = new PRDetail();
				_hj.setKbarang(hj.getKbarang());
				_hj.setNbarang(hj.getNbarang());
				_hj.setNo(i+1);
				_hj.setSatuan(hj.getSatuan());
				_hj.setJumlah(hj.getJumlah());
				_hj.setNobukti1(hj.getNobukti1());
				_hj.setKet(hj.getKet());
				if(null!=_hj) 
				{
					list.add(_hj);
				}
			}
		} catch (AxisFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
