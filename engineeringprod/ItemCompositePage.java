package org.apache.wicket.erp.engineeringprod;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.axis2.AxisFault;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.erp.ERPApplication;
import org.apache.wicket.erp.ERPWebPage;
import org.apache.wicket.erp.logistic.Group1;
import org.apache.wicket.erp.utils.IActionHandler;
import org.apache.wicket.erp.utils.PanelAction;
import org.apache.wicket.erp.utils.Service;
import org.apache.wicket.erp.utils.UserInfo;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.ListDataProvider;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import sf.inventory.Produk;

import java.text.SimpleDateFormat;

public class ItemCompositePage extends ERPWebPage implements IActionHandler{
	/**
	 * 
	 */
	private static List<String> KBARANG = Arrays
	.asList(new String[] { ""});
	
	private static String selectedKBarang;
	private static String selectedKBarang1;
	
	private static final long serialVersionUID = 1L;
	private DropDownChoice<String> kbarang;
	private RequiredTextField<String> nbarang;
	private RequiredTextField<String> satuan;
	//private RequiredTextField<String> kbarang1;
	private RequiredTextField<String> nbarang1;
	private RequiredTextField<String> satuan1;
	private RequiredTextField<String> kqty;
	private DropDownChoice<String> kbarang1;
	boolean isReadOnly=true;
	private Form<ProdukSet> form;
	private ArrayList<ProdukSet> list=new ArrayList<ProdukSet>();
	final AttributeModifier ro = new AttributeModifier("readonly", isReadOnly);
	private static ProdukSet produkset =null;
	private static ItemCompositePage app;
	private Service _service;
	private void init()
	{
		try {
			_service=new Service(Service.INVENTORY_SERVICE_URL);
		} catch (AxisFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static ItemCompositePage get()
	{
		return app;
	}
	
	public ItemCompositePage(ProdukSet p)
	{
		this(ItemCompositeFilter.selectedItem);
		produkset=p;
	}
	
	public ItemCompositePage(PageParameters p)
	{
		this(new Produk());
		
		if(p.getNamedKeys().contains("choose"))
			readOnly(false);
		if(p.getNamedKeys().contains("error"))
			error(p.get("error"));
		if(p.getNamedKeys().contains("info"))
			info(p.get("info"));
	}
	public ItemCompositePage()
	{
		this(ItemCompositeFilter.selectedItem==null?new Produk():ItemCompositeFilter.selectedItem);
	}
	public ItemCompositePage(Produk exp)
	{
		super("Engineering | Item Composite");
		init();
		getProduct();
		if(produkset==null)
		{
			produkset=new ProdukSet();
		}
		if(exp==null)
		{
			exp=new Produk();
			selectedKBarang=null;
			selectedKBarang1=null;
		}
		
		if(exp.getKbarang()!=null)
		{
			selectedKBarang=exp.getKbarang();
			selectedKBarang1=exp.getKbarang1();
			getProdukset(exp.getKbarang());
			if(PanelAction.actionType!=2)
				produkset = new ProdukSet();
			
			produkset.setKbarang(exp.getKbarang());
			produkset.setNbarang(exp.getNbarang());
			produkset.setSatuan(exp.getSatuan());
			produkset.setKbarang1(exp.getKbarang1());
		}
		
		add(new ItemCompositeFilter("itemcompositefilter",this));
		form = new Form<ProdukSet>("form", new CompoundPropertyModel<ProdukSet>(produkset));
		add(form);
		
		// add a simple text field that uses Input's 'text' property. Nothing
		// can go wrong here
		kbarang = new DropDownChoice<String>("kbarang",
				new PropertyModel<String>(this, "selectedKBarang"), KBARANG);
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
				Produk prod = mapProduk.get(selectedKBarang);
				if(prod!=null)
				{
					produkset=new ProdukSet();
					produkset.setKbarang(prod.getKbarang());
					produkset.setNbarang(prod.getNbarang());
					produkset.setSatuan(prod.getSatuan());
					PageParameters p = new PageParameters();
					p.add("choose", selectedKBarang);
					setResponsePage(ItemCompositePage.class,p);
				}
			}
		});
		
		
		nbarang=new RequiredTextField<String>("nbarang");
		nbarang.setOutputMarkupId(true);
		satuan=new RequiredTextField<String>("satuan");
		satuan.setOutputMarkupId(true);
		
		kbarang1 = new DropDownChoice<String>("kbarang1",
				new PropertyModel<String>(this, "selectedKBarang1"), KBARANG);
		kbarang1.setRequired(true);
		kbarang1.add(new AjaxFormComponentUpdatingBehavior("onchange")
		{
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			protected void onUpdate(AjaxRequestTarget target)
			{
				if(selectedKBarang1.equals(selectedKBarang))
				{
					PageParameters p = new PageParameters();
					p.add("error", "kode detail should have different with kode master.");
					setResponsePage(ItemCompositePage.class,p);
					return;
				}
				Produk prod1 = mapProduk.get(selectedKBarang1);
				Produk prod = mapProduk.get(selectedKBarang);
				if(prod1!=null)
				{
					produkset.setKbarang1(prod1.getKbarang());
					produkset.setNbarang1(prod1.getNbarang());
					produkset.setSatuan1(prod1.getSatuan());
					target.add(nbarang1);
					target.add(satuan1);
				}
				if(prod!=null)
				{
					produkset.setKbarang(prod.getKbarang());
					produkset.setNbarang(prod.getNbarang());
					produkset.setSatuan(prod.getSatuan());
					target.add(nbarang);
					target.add(satuan);
				}
				PageParameters p = new PageParameters();
				p.add("choose", selectedKBarang);
				setResponsePage(ItemCompositePage.class,p);
			}
		});
		
		nbarang1=new RequiredTextField<String>("nbarang1");
		nbarang1.setOutputMarkupId(true);
		satuan1=new RequiredTextField<String>("satuan1");
		satuan1.setOutputMarkupId(true);
		kqty=new RequiredTextField<String>("kqty");
		
		readOnly(true);
		
		form.add(kbarang);
		form.add(nbarang);
		form.add(satuan);
		form.add(kbarang1);
		form.add(nbarang1);
		form.add(satuan1);
		form.add(kqty);
		form.add(new PanelAction("actionpanel",this));
		final DataView<ProdukSet> dataView = new DataView<ProdukSet>("pageable", new ListDataProvider(
                list)) {
            /**
					 * 
					 */
			private static final long serialVersionUID = 1L;

			public void populateItem(final Item<ProdukSet> item) {
                final ProdukSet produk = (ProdukSet) item.getModelObject();
				item.add(new Label("no", String.valueOf(produk.getNo())));
				item.add(new Label("kbarang1", produk.getKbarang1()));
				item.add(new Label("nbarang1", produk.getNbarang1()));
				item.add(new Label("satuan1", produk.getSatuan1()));
				item.add(new Label("kqty", String.valueOf(produk.getKqty())));
				item.add(new AjaxLink<Group1>("select")
				{
					/**
					 * 
					 */
					private static final long serialVersionUID = 1L;

					@Override
					public void onClick(AjaxRequestTarget target) {
						// TODO Auto-generated method stub
						ProdukSet selected = (ProdukSet)getParent().getDefaultModelObject();
						produkset=selected;
						selectedKBarang1=produkset.getKbarang1();
						ItemCompositeFilter.selectedItem.setKbarang1(selectedKBarang1);
						PanelAction.actionType=2;
						setResponsePage(new ItemCompositePage(ItemCompositeFilter.selectedItem));
					}
				});

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
		form.add(dataView);

		form.add(new PagingNavigator("navigator", dataView));
		
		
		final FeedbackPanel feedback = new FeedbackPanel("feedback");
		add(feedback);
		
		app=this;
		ERPApplication.pageTitle="Engineering | Item Composite";
	}
	public boolean updateProdukSet()
	{
		form.process(null);
		ProdukSet item = (ProdukSet)form.getModelObject();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String dateNow = sdf.format(new Date());
		item.setTglupdate(Long.parseLong(dateNow));
		item.setUserupdate(UserInfo.USERID);

		Object[] params=new Object[]{item,UserInfo.COMPANY};
    	Class[] retTypes =new Class[]{Boolean.class};
    	
    	Object[] response;
		try {
			response = _service.callServiceInventory("updateProdukSetByCompany", params, retTypes);
			Boolean ret=(Boolean)response[0];
			if(ret)
			{
				PanelAction.actionType=2;//set panel action with 2//edit and delete button is enabled.
				setResponsePage(new ItemCompositePage(ItemCompositeFilter.selectedItem));
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
		ProdukSet item = (ProdukSet)form.getModelObject();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String dateNow = sdf.format(new Date());
		item.setTglupdate(Long.parseLong(dateNow));
		item.setUserupdate(UserInfo.USERID);
		
    	Object[] params=new Object[]{item};
    	Class[] retTypes =new Class[]{Boolean.class};
    	
    	Object[] response;
		try {
			response = _service.callServiceInventory("deleteProdukSet", params, retTypes);
			Boolean ret=(Boolean)response[0];
			if(ret)
			{
				PanelAction.actionType=2;
				setResponsePage(new ItemCompositePage(ItemCompositeFilter.selectedItem));
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
		ProdukSet item = (ProdukSet)form.getModelObject();
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
			response = _service.callServiceInventory("insertProdukSet", params, retTypes);
			Boolean ret=(Boolean)response[0];
			if(ret)
			{
				//filter.getInvoice();// try to query 
				PanelAction.actionType=2;
				
				produkset=new ProdukSet();
				Produk p= mapProduk.get(selectedKBarang);
				if(p!=null)
				{
					produkset.setKbarang(p.getKbarang());
					produkset.setNbarang(p.getNbarang());
					produkset.setSatuan(p.getSatuan());
				}
				
				selectedKBarang1=null;
				PageParameters pg = new PageParameters();
				pg.add("choose", selectedKBarang);
				pg.add("info", "Insert Item Composite has been successfully.");
				pg.remove("error");
				ItemCompositeFilter.selectedItem=p;
				
				setResponsePage(ItemCompositePage.class,pg);
				//setResponsePage(new ItemCompositePage(ItemCompositeFilter.selectedItem));
				return 1;
			}
			error("Failed to insert Item Composite.");
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
		Boolean bRet=updateProdukSet();
		if(bRet)
		{
			info("Update Item Composite has been successfully.");
			return 1;
		}
		else
			error("Failed to update Item Composite.");
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
			kbarang.add(ro);
			nbarang.add(ro);
			satuan.add(ro);
			kbarang1.add(ro);
		}
		catch(Exception ex){}
	}

	public void newAction() {
		// TODO Auto-generated method stub
		produkset=new ProdukSet();
		try
		{
			kbarang.remove(ro);
			nbarang.remove(ro);
			satuan.remove(ro);
			kbarang1.remove(ro);
			nbarang1.remove(ro);
			satuan1.remove(ro);
			kqty.remove(ro);
			setResponsePage(this);
		}
		catch(Exception ex){}
	}
	
	public void readOnly(Boolean flag)
	{
		try
		{
			if(!flag)
			{
				kbarang.remove(ro);
				nbarang.remove(ro);
				satuan.remove(ro);
				kbarang1.remove(ro);
				nbarang1.remove(ro);
				satuan1.remove(ro);
				kqty.remove(ro);
			}
			else
			{
				kbarang.add(ro);
				nbarang.add(ro);
				satuan.add(ro);
				kbarang1.add(ro);
				nbarang1.add(ro);
				satuan1.add(ro);
				kqty.add(ro);
			}
		}
		catch(Exception ex){}
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
		//setResponsePage(ItemCompositePage.class);
		form.clearInput();
	}

	public void print() {
		// TODO Auto-generated method stub
		
	}

	public void cancel(boolean flag) {
		// TODO Auto-generated method stub
		if(flag)
			setResponsePage(new ItemCompositePage(ItemCompositeFilter.selectedItem));
		else
			setResponsePage(ItemCompositePage.class);
		
		this.readOnly();
	}
	
	public void getProdukset(String kbarang)
    {
    	Object[] params=new Object[]{kbarang,UserInfo.COMPANY};
    	
    	Class[] retTypes =new Class[]{sf.inventory.ProdukSet.class};
    	try {
			Object[] response=_service.callServiceInventory("getProdukSetByCode", params,retTypes);
			sf.inventory.ProdukSet item = (sf.inventory.ProdukSet)response[0];
			if(item==null) return ;
			if(item.getProduksets()==null) return ;
			int count=item.getProduksets().length;
			list = new ArrayList<ProdukSet>();
			for(int i=0; i < count;i++)
			{
				sf.inventory.ProdukSet ps=(sf.inventory.ProdukSet)item.getProduksets()[i];
				if(null!=ps)
				{
					ProdukSet prodset=new ProdukSet();
					prodset.setNo(i+1);
					prodset.setKbarang(ps.getKbarang());
					prodset.setNbarang(ps.getNbarang());
					prodset.setSatuan(ps.getSatuan());
					prodset.setKbarang1(ps.getKbarang1());
					prodset.setNbarang1(ps.getNbarang1());
					prodset.setSatuan1(ps.getSatuan1());
					prodset.setKqty(ps.getKqty());
					list.add(prodset);
				}
			}
		} catch (AxisFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
	
	private String[] kodes=null ;
	private HashMap<String, Produk> mapProduk=new HashMap<String, Produk>();
	private void getProduct()
	{
		Object[] params=new Object[]{UserInfo.COMPANY};
    	
    	Class[] retTypes =new Class[]{Produk.class};
    	try {
			Object[] response=_service.callServiceInventory("getProduk", params,retTypes);
			Produk item = (Produk)response[0];
			if(item==null) return;
			if(item.getProduks()==null) return;
			
			int count=item.getProduks().length;
			kodes=new String[count];
			for(int i=0; i < count;i++)
			{
				Produk prod=item.getProduks()[i];
				if(null!=prod) 
				{
					kodes[i]=prod.getKbarang();
					mapProduk.put(prod.getKbarang(), prod);
				}
			}
			KBARANG=Arrays.
			asList(kodes);
		} catch (AxisFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
