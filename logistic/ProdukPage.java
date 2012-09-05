package org.apache.wicket.erp.logistic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.axis2.AxisFault;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.erp.ERPApplication;
import org.apache.wicket.erp.ERPWebPage;
import org.apache.wicket.erp.crm.CustomerPage;
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
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import sf.inventory.Group;
import sf.inventory.Group1;
import sf.inventory.Group2;
import sf.inventory.PType;
import sf.inventory.Produk;

import antlr.collections.impl.Vector;

import java.text.SimpleDateFormat;
public class ProdukPage extends ERPWebPage implements IActionHandler{
	/**
	 * 
	 */
	private static final List<String> BSTOCKBAL = Arrays
	.asList(new String[] { "Ada", "Tidak"});
	
	private static List<String> PSTATUS = Arrays
	.asList(new String[] { "Aktif","Discontinue"});
	
	private static List<String> PTYPES = Arrays
	.asList(new String[] { ""});
	
	private static List<String> GROUPS1 = Arrays
	.asList(new String[] { ""});
	
	private static List<String> GROUPS2 = Arrays
	.asList(new String[] { ""});
	
	private final Map<String, List<String>> modelsMap = new HashMap<String, List<String>>();
	private final Map<String, List<String>> modelsMap1 = new HashMap<String, List<String>>();
	
	private static final long serialVersionUID = 1L;
	
	private RequiredTextField<String> kbarang;
	private TextField<String> companyType;
	private RequiredTextField<String> nbarang;
	private RequiredTextField<String> satuan;
	private TextField<String> satuan1;
	private TextField<String> satuan2;
	private RadioChoice<String> pstatusdesc;
	private RadioChoice<String> stockbaldesc;
	private DropDownChoice<String> kbarang1;
	private TextField<String> kbarangp;
	private TextField<String> qty1;
	private TextField<String> qty2;
	private TextField<String> desc1;
	private TextField<String> desc2;
	private TextField<String> desc3;
	private TextField<String> desc4;
	private TextField<String> desc5;
	private TextField<String> desc6;
	private TextField<String> desc7;
	private TextField<String> desc8;
	private TextField<String> desc9;
	private TextField<String> desc10;
	private DropDownChoice<String> kgroup;
	private DropDownChoice<String> kgroup1;
	private DropDownChoice<String> kgroup2;
	private TextField<String> minstock;
	private TextField<String> maxstock;
	private TextField<String> hppnval;
	private TextField<String> hppnval1;
	private TextField<String> hppkval ;
	private TextField<String> hppkval1;
	private TextField<String> maxDisc;
	private DropDownChoice<String> ptype;
	private TextField<String> nvaluta;
	private TextField<String> kvaluta;
	
	boolean isReadOnly=true;
	private Form<Produk> form;
	final AttributeModifier ro = new AttributeModifier("readonly", isReadOnly);
	private Produk exp = new Produk();
	private static ProdukPage app;
	private Service _service;
	
	public String selectedGroup;
	public String selectedGroup1;
	public String selectedGroup2;
	private void init()
	{
		try {
			_service=new Service(Service.INVENTORY_SERVICE_URL);
		} catch (AxisFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static ProdukPage get()
	{
		return app;
	}
	
	public ProdukPage(PageParameters p)
	{
		this(new Produk());
		if(p.getNamedKeys().contains("error"))
			error(p.get("error"));
		if(p.getNamedKeys().contains("info"))
			info(p.get("info"));
		readOnly(false);
	}
	public ProdukPage()
	{
		this(new Produk());
	}
	public ProdukPage(Produk exp)
	{
		super("Logistic | Master Produk");
		init();
		getPType();
		getGroup();
		getGroup1();
		getGroup2();
		
		selectedGroup= exp.getKgroup();
		selectedGroup1=exp.getKgroup1();
		selectedGroup2=exp.getKgroup2();
		
		if(exp.getCompanyType()==null || exp.getCompanyType().equals(""))
			exp.setCompanyType(UserInfo.COMPANY);
		
		IModel<List<? extends String>> groupChoices = new AbstractReadOnlyModel<List<? extends String>>()
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

		IModel<List<? extends String>> group1Choices = new AbstractReadOnlyModel<List<? extends String>>()
		{
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public List<String> getObject()
			{
				List<String> models = modelsMap.get(selectedGroup);
				if (models == null)
				{
					models = Collections.emptyList();
				}
				return models;
			}

		};
		
		IModel<List<? extends String>> group2Choices = new AbstractReadOnlyModel<List<? extends String>>()
		{
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public List<String> getObject()
			{
				List<String> models = modelsMap1.get(selectedGroup1);
				if (models == null)
				{
					models = Collections.emptyList();
				}
				return models;
			}

		};
		
		
		this.exp = exp;
		add(new ProdukFilter("produkfilter",this));
		form = new Form<Produk>("form", new CompoundPropertyModel<Produk>(this.exp));
		add(form);
		
		// add a simple text field that uses Input's 'text' property. Nothing
		// can go wrong here
		
		 kbarang=new RequiredTextField<String>("kbarang");
		 companyType=new TextField<String>("companyType");
		 nbarang=new RequiredTextField<String>("nbarang");
		 satuan=new RequiredTextField<String>("satuan");
		 satuan1=new TextField<String>("satuan1");
		 satuan2=new TextField<String>("satuan2");
		 pstatusdesc=new RadioChoice<String>("pstatusdesc",PSTATUS);
		 pstatusdesc.setSuffix(" ");
		 pstatusdesc.setRequired(true);
		 
		 stockbaldesc=new RadioChoice<String>("stockbaldesc",BSTOCKBAL);
		 stockbaldesc.setSuffix(" ");
		 stockbaldesc.setRequired(true);
		 
		 kbarang1=new DropDownChoice<String>("kbarang1");
		 kbarangp=new TextField<String>("kbarangp");
		 qty1=new TextField<String>("qty1");
		 qty2=new TextField<String>("qty2");
		 desc1=new TextField<String>("desc1");
		 desc2=new TextField<String>("desc2");
		 desc3=new TextField<String>("desc3");
		 desc4=new TextField<String>("desc4");
		 desc5=new TextField<String>("desc5");
		 desc6=new TextField<String>("desc6");
		 desc7=new TextField<String>("desc7");
		 desc8=new TextField<String>("desc8");
		 desc9=new TextField<String>("desc9");
		 desc10=new TextField<String>("desc10");
		 kgroup=new DropDownChoice<String>("kgroup",
				 new PropertyModel<String>(this, "selectedGroup"), groupChoices);
		 
		 kgroup.add(new AjaxFormComponentUpdatingBehavior("onchange")
			{
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				@Override
				protected void onUpdate(AjaxRequestTarget target)
				{
					target.add(kgroup1);
				}
			});
		 
		 kgroup1=new DropDownChoice<String>("kgroup1",
				 new PropertyModel<String>(this, "selectedGroup1"), group1Choices);
		 kgroup1.setOutputMarkupId(true);
			
		 kgroup1.add(new AjaxFormComponentUpdatingBehavior("onchange")
			{
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				@Override
				protected void onUpdate(AjaxRequestTarget target)
				{
					target.add(kgroup2);
				}
			});
		 
		 kgroup2=new DropDownChoice<String>("kgroup2",
				 new PropertyModel<String>(this, "selectedGroup2"), group2Choices);
		 kgroup2.setOutputMarkupId(true);
		 
		 minstock=new TextField<String>("minstock");
		 maxstock=new TextField<String>("maxstock");
		 hppnval=new TextField<String>("hppnval");
		 hppnval1=new TextField<String>("hppnval1");
		 hppkval =new TextField<String>("hppkval");
		 hppkval1=new TextField<String>("hppkval1");
		 maxDisc=new TextField<String>("maxDisc");
		 ptype=new DropDownChoice<String>("ptype",PTYPES);
		 nvaluta=new TextField<String>("nvaluta");
		 kvaluta=new TextField<String>("kvaluta");
		
		
		
		readOnly(true);
		
		form.add(kbarang);
		form.add(companyType);
		form.add(nbarang);
		form.add(satuan);
		form.add(satuan1);
		form.add(satuan2);
		form.add(pstatusdesc);
		form.add(stockbaldesc);
		form.add(kbarang1);
		form.add(kbarangp);
		form.add(qty1);
		form.add(qty2);
		form.add(desc1);
		form.add(desc2);
		form.add(desc3);
		form.add(desc4);
		form.add(desc5);
		form.add(desc6);
		form.add(desc7);
		form.add(desc8);
		form.add(desc9);
		form.add(desc10);
		form.add(kgroup);
		form.add(kgroup1);
		form.add(kgroup2);
		form.add(minstock);
		form.add(maxstock);
		form.add(hppnval);
		form.add(hppnval1);
		form.add(hppkval);
		form.add(hppkval1);
		form.add(maxDisc);
		form.add(ptype);
		form.add(nvaluta);
		form.add(kvaluta);
		
		final FeedbackPanel feedback = new FeedbackPanel("feedback");
		add(feedback);
		form.add(new PanelAction("actionpanel",this));
		app=this;
		ERPApplication.pageTitle="Logistic | Master Produk";
	}
	public boolean updateProduk()
	{
		form.process(null);
		Produk item = (Produk)form.getModelObject();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String dateNow = sdf.format(new Date());
		item.setTglupdate(Long.parseLong(dateNow));
		item.setUserupdate(UserInfo.USERID);
		item.setKgroup(selectedGroup);
		item.setKgroup1(selectedGroup1);
		item.setKgroup2(selectedGroup2);
		if(item.getStockbaldesc().equals("Ada"))
			item.setStockbal("Y");
		else
			item.setStockbal("N");
		
		if(item.getPstatusdesc().equals("Aktif"))
			item.setPstatus("C");
		else
			item.setPstatus("S");
		
		Object[] params=new Object[]{item};
    	Class[] retTypes =new Class[]{Boolean.class};
    	
    	Object[] response;
		try {
			response = _service.callServiceInventory("updateProduk", params, retTypes);
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
		Produk item = (Produk)form.getModelObject();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String dateNow = sdf.format(new Date());
		item.setTglupdate(Long.parseLong(dateNow));
		item.setUserupdate(UserInfo.USERID);
		
    	Object[] params=new Object[]{item,item.getCompanyType()};
    	Class[] retTypes =new Class[]{Boolean.class};
    	
    	Object[] response;
		try {
			response = _service.callServiceInventory("deleteProduk", params, retTypes);
			Boolean ret=(Boolean)response[0];
			if(ret)
			{
				PageParameters pg=new PageParameters();
				pg.add("info", "Delete Produk has been successfully.");
				pg.remove("error");
				PanelAction.actionType=0;
				setResponsePage(CustomerPage.class,pg);
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
		Produk item = (Produk)form.getModelObject();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String dateNow = sdf.format(new Date());
		item.setTglupdate(Long.parseLong(dateNow));
		item.setUserupdate(UserInfo.USERID);
		item.setTglinput(item.getTglupdate());
		item.setUserinput(item.getUserupdate());
		
		item.setKgroup(selectedGroup);
		item.setKgroup1(selectedGroup1);
		item.setKgroup2(selectedGroup2);
		
		if(item.getStockbaldesc().equals("Ada"))
			item.setStockbal("Y");
		else
			item.setStockbal("N");
		
		if(item.getPstatusdesc().equals("Aktif"))
			item.setPstatus("C");
		else
			item.setPstatus("S");
		
    	Object[] params=new Object[]{item};
    	Class[] retTypes =new Class[]{Boolean.class};
    	
    	Object[] response;
    	boolean duplicate=false;
		try {
			response = _service.callServiceInventory("insertProduk", params, retTypes);
			Boolean ret=(Boolean)response[0];
			if(ret)
			{
				//filter.getInvoice();// try to query 
				PageParameters pg=new PageParameters();
				pg.add("info", "Insert Produk has been successfully.");
				pg.remove("error");
				setResponsePage(ProdukPage.class,pg);
				return 1;
			}
			error("Failed to insert Produk.");
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
		Boolean bRet=updateProduk();
		if(bRet)
		{
			info("Update Produk has been successfully.");
			return 1;
		}
		else
			error("Failed to update Produk.");
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
		}
		catch(Exception ex){}
	}

	public void newAction() {
		// TODO Auto-generated method stub
		PageParameters p= new PageParameters();
		p.add("new", 1);
		setResponsePage(ProdukPage.class,p);
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
			 kbarang.remove(ro);
			 nbarang.remove(ro);
			 satuan.remove(ro);
			 satuan1.remove(ro);
			 satuan2.remove(ro);
			 pstatusdesc.remove(ro);
			 stockbaldesc.remove(ro);
			 kbarang1.remove(ro);
			 kbarangp.remove(ro);
			 qty1.remove(ro);
			 qty2.remove(ro);
			 desc1.remove(ro);
			 desc2.remove(ro);
			 desc3.remove(ro);
			 desc4.remove(ro);
			 desc5.remove(ro);
			 desc6.remove(ro);
			 desc7.remove(ro);
			 desc8.remove(ro);
			 desc9.remove(ro);
			 desc10.remove(ro);
			 kgroup.remove(ro);
			 kgroup1.remove(ro);
			 kgroup2.remove(ro);
			 minstock.remove(ro);
			 maxstock.remove(ro);
			 hppnval.remove(ro);
			 hppnval1.remove(ro);
			 hppkval .remove(ro);
			 hppkval1.remove(ro);
			 maxDisc.remove(ro);
			 ptype.remove(ro);
			 nvaluta.remove(ro);
			 kvaluta.remove(ro);
		}
		else
		{
			kbarang.add(ro);
			companyType.add(ro);
			 nbarang.add(ro);
			 satuan.add(ro);
			 satuan1.add(ro);
			 satuan2.add(ro);
			 pstatusdesc.add(ro);
			 stockbaldesc.add(ro);
			 kbarang1.add(ro);
			 kbarangp.add(ro);
			 qty1.add(ro);
			 qty2.add(ro);
			 desc1.add(ro);
			 desc2.add(ro);
			 desc3.add(ro);
			 desc4.add(ro);
			 desc5.add(ro);
			 desc6.add(ro);
			 desc7.add(ro);
			 desc8.add(ro);
			 desc9.add(ro);
			 desc10.add(ro);
			 kgroup.add(ro);
			 kgroup1.add(ro);
			 kgroup2.add(ro);
			 minstock.add(ro);
			 maxstock.add(ro);
			 hppnval.add(ro);
			 hppnval1.add(ro);
			 hppkval .add(ro);
			 hppkval1.add(ro);
			 maxDisc.add(ro);
			 ptype.add(ro);
			 nvaluta.add(ro);
			 kvaluta.add(ro);
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
		setResponsePage(ProdukPage.class);
	}

	public void print() {
		// TODO Auto-generated method stub
		
	}

	public void cancel(boolean flag) {
		// TODO Auto-generated method stub
		if(flag)
			setResponsePage(new ProdukPage(ProdukFilter.selectedItem));
		else
			setResponsePage(ProdukPage.class);
		
		this.readOnly();
	}
	
	private String[] kodes;
	private void getPType()
	{
		Object[] params=new Object[]{null};
    	
    	Class[] retTypes =new Class[]{sf.inventory.PType.class};
    	try {
			Object[] response=_service.callServiceInventory("getPType", params,retTypes);
			sf.inventory.PType item = (sf.inventory.PType)response[0];
			if(item==null) return;
			if(item.getPtypes()==null) return;
			
			int count=item.getPtypes().length;
			kodes=new String[count];
			for(int i=0; i < count;i++)
			{
				PType cust=item.getPtypes()[i];
				if(null!=cust) 
				{
					kodes[i]=cust.getKtype();
				}
			}
			PTYPES=Arrays.
			asList(kodes);
		} catch (AxisFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	private void getGroup()
	{
		Object[] params=new Object[]{null};
    	
    	Class[] retTypes =new Class[]{sf.inventory.Group.class};
    	try {
			Object[] response=_service.callServiceInventory("getGroup", params,retTypes);
			sf.inventory.Group item = (sf.inventory.Group)response[0];
			if(item==null) return;
			if(item.getGroups()==null) return;
			
			int count=item.getGroups().length;
			kodes=new String[count];
			for(int i=0; i < count;i++)
			{
				Group cust=item.getGroups()[i];
				if(null!=cust) 
				{
					modelsMap.put(cust.getKgroup(),  Arrays.asList(""));
				}
			}
		} catch (AxisFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private HashMap<String, Vector> group1=new HashMap<String, Vector>();
	private void getGroup1()
	{
		Object[] params=new Object[]{null};
    	
    	Class[] retTypes =new Class[]{sf.inventory.Group1.class};
    	try {
			Object[] response=_service.callServiceInventory("getGroup1", params,retTypes);
			sf.inventory.Group1 item = (sf.inventory.Group1)response[0];
			if(item==null) return;
			if(item.getGroups()==null) return;
			
			int count=item.getGroups().length;
			for(int i=0; i < count;i++)
			{
				Group1 cust=item.getGroups()[i];
				if(null!=cust) 
				{
					if(group1.containsKey(cust.getKgroup()))
					{
						Vector v = group1.get(cust.getKgroup());
						v.appendElement(cust.getKgroup1());
						group1.put(cust.getKgroup(), v);
					}
					else
					{
						Vector v = new Vector();
						v.appendElement(cust.getKgroup1());
						group1.put(cust.getKgroup(), v);
					}
					if(!modelsMap1.containsKey(cust.getKgroup1()))
						modelsMap1.put(cust.getKgroup1(),  Arrays.asList(""));
				}
			}
			
			for (String key : group1.keySet()) {
			    Vector v= group1.get(key);
			    kodes=new String[v.size()];
			    for(int i=0; i <v.size();i++)
			    {
			    	kodes[i]=(String)v.elementAt(i);
			    }
			    if (modelsMap.containsKey(key))
				{
			    	GROUPS1=Arrays.
					asList(kodes);
					modelsMap.put(key,GROUPS1);
				}
			}
			
		} catch (AxisFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private HashMap<String, Vector> group2=new HashMap<String, Vector>();
	private void getGroup2()
	{
		Object[] params=new Object[]{null};
    	
    	Class[] retTypes =new Class[]{sf.inventory.Group2.class};
    	try {
			Object[] response=_service.callServiceInventory("getGroup2", params,retTypes);
			sf.inventory.Group2 item = (sf.inventory.Group2)response[0];
			if(item==null) return;
			if(item.getGroups()==null) return;
			
			int count=item.getGroups().length;
			kodes=new String[count];
			for(int i=0; i < count;i++)
			{
				Group2 cust=item.getGroups()[i];
				if(null!=cust) 
				{
					if (modelsMap.containsKey(cust.getKgroup()))
					{
						if(modelsMap1.containsKey(cust.getKgroup1()))
						{
							if(group2.containsKey(cust.getKgroup1()))
							{
								Vector v = group2.get(cust.getKgroup1());
								v.appendElement(cust.getKgroup2());
								group2.put(cust.getKgroup1(), v);
							}
							else
							{
								Vector v = new Vector();
								v.appendElement(cust.getKgroup2());
								group2.put(cust.getKgroup1(), v);
							}
							/*
							GROUPS2  = modelsMap1.get(cust.getKgroup1());
							GROUPS2.add(cust.getKgroup2());
							modelsMap1.put(cust.getKgroup1(), GROUPS2);
							*/
						}
					}
				}
			}
			for (String key : group2.keySet()) {
			    Vector v= group2.get(key);
			    kodes=new String[v.size()];
			    for(int i=0; i <v.size();i++)
			    {
			    	kodes[i]=(String)v.elementAt(i);
			    }
			    if (modelsMap1.containsKey(key))
				{
			    	GROUPS2=Arrays.
					asList(kodes);
					modelsMap1.put(key,GROUPS2);
				}
			}
		} catch (AxisFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
