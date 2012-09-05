package org.apache.wicket.erp.logistic;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.apache.axis2.AxisFault;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.erp.accounting.BasePage;
import org.apache.wicket.erp.utils.IActionHandler;
import org.apache.wicket.erp.utils.PanelAction;
import org.apache.wicket.erp.utils.Service;
import org.apache.wicket.erp.utils.UserInfo;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.ListDataProvider;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import java.text.SimpleDateFormat;

public class SubLevel2Page extends BasePage implements IActionHandler{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Service _service;
	HashMap<String, Group2> maps;
	private ArrayList<Group2> list=new ArrayList<Group2>();
	private Form<Group2> form;
	public static Group2 group2;
	private RequiredTextField<String> kgroup;
	private RequiredTextField<String> kgroup1;
	private RequiredTextField<String> kgroup2;
	private RequiredTextField<String> ngroup2;
	boolean isReadOnly=true;
	private static String kgroupValue;
	private static String kgroup1Value;
	final AttributeModifier ro = new AttributeModifier("readonly", isReadOnly);
	
	public SubLevel2Page(PageParameters p)
	{
		super("Sub Group Level2");
		kgroupValue=p.get("kgroup").toString();
		kgroup1Value=p.get("kgroup1").toString();
		if(group2==null)
		{
			group2=new Group2();
		}
		group2.setKgroup(kgroupValue);
		group2.setKgroup1(kgroup1Value);
		init();
		getGroup2(kgroupValue,kgroup1Value);
		buildPage();
		try
		{
			if(p.get("new")!=null)
			{
				if(p.get("new").toString().equals("1"))
				{
					kgroup2.remove(ro);
					ngroup2.remove(ro);
				}
		
			}
		}
		catch(Exception ex){}
		
	}
	
	public SubLevel2Page()
	{
		super("Sub Group Level2");
		init();
		getGroup2(group2.getKgroup(),group2.getKgroup1());
		buildPage();
	}

	@SuppressWarnings("unchecked")
	private void buildPage()
	{
		final DataView<Group2> dataView = new DataView<Group2>("pageable", new ListDataProvider(
                list)) {
            /**
					 * 
					 */
			private static final long serialVersionUID = 1L;

			public void populateItem(final Item item) {
                final Group2 _group2 = (Group2) item.getModelObject();
				item.add(new Label("no", String.valueOf(_group2.getNo())));
				item.add(new Label("kgroup", _group2.getKgroup()));
				item.add(new Label("kgroup1", _group2.getKgroup1()));
				item.add(new Label("kgroup2", _group2.getKgroup2()));
				item.add(new Label("ngroup2", _group2.getNgroup2()));
				item.add(new AjaxLink<Group2>("select")
				{
					/**
					 * 
					 */
					private static final long serialVersionUID = 1L;

					@Override
					public void onClick(AjaxRequestTarget target) {
						// TODO Auto-generated method stub
						Group2 selected = (Group2)getParent().getDefaultModelObject();
						group2=selected;
						PanelAction.actionType=2;
						setResponsePage(SubLevel2Page.class);
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
		add(dataView);

		add(new PagingNavigator("navigator", dataView));
		
		
		form = new Form<Group2>("form", new CompoundPropertyModel<Group2>(this.group2));
		add(form);
		// add a simple text field that uses Input's 'text' property. Nothing
		// can go wrong here
		 kgroup=new RequiredTextField<String>("kgroup");
		 kgroup1=new RequiredTextField<String>("kgroup1");
		 kgroup2=new RequiredTextField<String>("kgroup2");
		 ngroup2=new RequiredTextField<String>("ngroup2");
		 
		 
		 readOnly();
		form.add(kgroup);
		form.add(kgroup1);
		form.add(kgroup2);
		form.add(ngroup2);
		
		form.add(new PanelAction("actionpanel",this));
	}
	
	private void init()
	{
		maps=new HashMap<String, Group2>();
		try {
			_service=new Service(Service.INVENTORY_SERVICE_URL);
		} catch (AxisFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void getGroup2(String kgroup,String kgroup1)
    {
    	Object[] params=new Object[]{kgroup,kgroup1};
    	
    	Class[] retTypes =new Class[]{Group2.class};
    	try {
			Object[] response=_service.callServiceInventory("getGroup2ByCode", params,retTypes);
			Group2 item = (Group2)response[0];
			if(item==null) return;
			if(item.getGroups()==null) return;
			int count=item.getGroups().length;
			list = new ArrayList<Group2>();
			for(int i=0; i < count;i++)
			{
				sf.inventory.Group2 group2=item.getGroups()[i];
				Group2 _group2 = new Group2();
				_group2.setKgroup(group2.getKgroup());
				_group2.setKgroup1(group2.getKgroup1());
				_group2.setNo(i+1);
				_group2.setKgroup2(group2.getKgroup2());
				_group2.setNgroup2(group2.getNgroup2());
				if(null!=_group2) 
				{
					list.add(_group2);
				}
			}
		} catch (AxisFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

	public void readOnly() {
		// TODO Auto-generated method stub
		try
		{
			kgroup.add(ro);
			kgroup1.add(ro);
			kgroup2.add(ro);
			ngroup2.add(ro);
		}
		catch(Exception ex){}
	}
	public void activate() {
		// TODO Auto-generated method stub
		setResponsePage(SubLevel2Page.class);
	}

	public void cancel(boolean flag) {
		// TODO Auto-generated method stub
		PageParameters p = new PageParameters();
		p.set("kgroup", group2.getKgroup());
		p.set("kgroup1", group2.getKgroup1());
		setResponsePage(SubLevel2Page.class,p);
	}

	public int delete() {
		// TODO Auto-generated method stub
		form.process(null);
		Group2 valuta= (Group2)form.getModelObject();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String dateNow = sdf.format(new Date());
		valuta.setTglupdate(Long.parseLong(dateNow));
		valuta.setUserupdate(UserInfo.USERID);
		
    	Object[] params=new Object[]{valuta};
    	Class[] retTypes =new Class[]{Boolean.class};
    	
    	Object[] response;
		try {
			response = _service.callServiceInventory("deleteGroup2", params, retTypes);
			Boolean ret=(Boolean)response[0];
			if(ret)
			{
				PanelAction.actionType=2;
				PageParameters p = new PageParameters();
				p.set("kgroup", group2.getKgroup());
				p.set("kgroup1", group2.getKgroup1());
				group2 = null;
				setResponsePage(SubLevel2Page.class,p);
				return 1;
			}
			return 0;
		} catch (AxisFault e) {
			// TODO Auto-generated catch block
			error("Data tidak berhasil disimpan\r\n" + e.getMessage());
		}
		return 2;
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
			kgroup2.remove(ro);
			ngroup2.remove(ro);
		}
		catch(Exception ex){}
	}

	public int insert() {
		// TODO Auto-generated method stub
		form.process(null);
		Group2 valuta = (Group2)form.getModelObject();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String dateNow = sdf.format(new Date());
		valuta.setTglupdate(Long.parseLong(dateNow));
		valuta.setUserupdate(UserInfo.USERID);
		
    	Object[] params=new Object[]{valuta};
    	Class[] retTypes =new Class[]{Boolean.class};
    	
    	Object[] response;
    	boolean duplicate=false;
		try {
			response = _service.callServiceInventory("insertGroup2", params, retTypes);
			Boolean ret=(Boolean)response[0];
			if(ret)
			{
				PageParameters p = new PageParameters();
				p.set("kgroup", group2.getKgroup());
				p.set("kgroup1", group2.getKgroup1());
				setResponsePage(SubLevel2Page.class,p);
				PanelAction.actionType=0;
			}
			return ret?1:0;
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

	public void newAction() {
		// TODO Auto-generated method stub
		PageParameters p= new PageParameters();
		p.add("new", 1);
		if(group2!=null)
		{
			group2.setKgroup2("");
			group2.setNgroup2("");
			p.set("kgroup", group2.getKgroup());
			p.set("kgroup1", group2.getKgroup1());
		}
		setResponsePage(SubLevel2Page.class,p);
		try
		{
			kgroup2.remove(ro);
			ngroup2.remove(ro);
		}
		catch(Exception ex){}
	}

	public void print() {
		// TODO Auto-generated method stub
		
	}

	

	public int update() {
		// TODO Auto-generated method stub
		form.process(null);
		Group2 group2 = (Group2)form.getModelObject();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String dateNow = sdf.format(new Date());
		group2.setTglupdate(Long.parseLong(dateNow));
		group2.setUserupdate(UserInfo.USERID);
		
    	Object[] params=new Object[]{group2};
    	Class[] retTypes =new Class[]{Boolean.class};
    	
    	Object[] response;
		try {
			response = _service.callServiceInventory("updateGroup2", params, retTypes);
			Boolean ret=(Boolean)response[0];
			if(ret)
			{
				PanelAction.actionType=2;
				setResponsePage(SubLevel2Page.class);
			}
			return ret?1:0;
		} catch (AxisFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}
}
