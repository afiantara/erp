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
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.link.PopupSettings;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.ListDataProvider;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import java.text.SimpleDateFormat;

public class SubLevel1Page extends BasePage implements IActionHandler{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Service _service;
	HashMap<String, Group1> maps;
	private ArrayList<Group1> list=new ArrayList<Group1>();
	private Form<Group1> form;
	public static Group1 group1;
	private Link subgrouplevel2Button;
	private RequiredTextField<String> kgroup;
	private RequiredTextField<String> kgroup1;
	private RequiredTextField<String> ngroup1;
	boolean isReadOnly=true;
	private static String kgroupValue;
	final AttributeModifier ro = new AttributeModifier("readonly", isReadOnly);
	
	public SubLevel1Page(PageParameters p)
	{
		super("Sub Group Level1");
		kgroupValue=p.get("kgroup").toString();
		if(group1==null)
		{
			group1=new Group1();
		}
		group1.setKgroup(kgroupValue);
		init();
		getGroup1(kgroupValue);
		buildPage();
		try
		{
			if(p.get("new")!=null)
			{
				if(p.get("new").toString().equals("1"))
				{
					kgroup1.remove(ro);
					ngroup1.remove(ro);
				}
		
			}
		}
		catch(Exception ex){}
		
	}
	
	public SubLevel1Page()
	{
		super("Sub Group Level1");
		init();
		getGroup1(group1.getKgroup());
		buildPage();
	}

	@SuppressWarnings("unchecked")
	private void buildPage()
	{
		final DataView<Group1> dataView = new DataView<Group1>("pageable", new ListDataProvider(
                list)) {
            /**
					 * 
					 */
			private static final long serialVersionUID = 1L;

			public void populateItem(final Item item) {
                final Group1 _group1 = (Group1) item.getModelObject();
				item.add(new Label("no", String.valueOf(_group1.getNo())));
				item.add(new Label("kgroup", _group1.getKgroup()));
				item.add(new Label("kgroup1", _group1.getKgroup1()));
				item.add(new Label("ngroup1", _group1.getNgroup1()));
				item.add(new AjaxLink<Group1>("select")
				{
					/**
					 * 
					 */
					private static final long serialVersionUID = 1L;

					@Override
					public void onClick(AjaxRequestTarget target) {
						// TODO Auto-generated method stub
						Group1 selected = (Group1)getParent().getDefaultModelObject();
						group1=selected;
						PanelAction.actionType=2;
						setResponsePage(SubLevel1Page.class);
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
		
		
		form = new Form<Group1>("form", new CompoundPropertyModel<Group1>(this.group1));
		add(form);
		// add a simple text field that uses Input's 'text' property. Nothing
		// can go wrong here
		 kgroup=new RequiredTextField<String>("kgroup");
		 kgroup1=new RequiredTextField<String>("kgroup1");
		 ngroup1=new RequiredTextField<String>("ngroup1");
		 
		 PopupSettings popupSettings = new PopupSettings(PopupSettings.LOCATION_BAR | PopupSettings.RESIZABLE | PopupSettings.SCROLLBARS).
			setHeight(300).setWidth(730).setTop(300).setLeft(180);
			
			subgrouplevel2Button=new Link("subgrouplevel2Button")
			{
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				@Override
				public void onClick() {
					// TODO Auto-generated method stub
					//setResponsePage(ModalKursHarianPage.class, new PageParameters());
					PageParameters p =new PageParameters();
					p.add("kgroup", kgroup.getModelObject());
					p.add("kgroup1", kgroup1.getModelObject());
					setResponsePage(SubLevel2Page.class,p);
				}
			};
			subgrouplevel2Button.setPopupSettings(popupSettings);
			subgrouplevel2Button.setEnabled((PanelAction.actionType==2||PanelAction.actionType==3)? true:false);
			form.add(subgrouplevel2Button);
			
		 readOnly();
		form.add(kgroup);
		form.add(kgroup1);
		form.add(ngroup1);
		
		form.add(new PanelAction("actionpanel",this));
	}
	
	private void init()
	{
		maps=new HashMap<String, Group1>();
		try {
			_service=new Service(Service.INVENTORY_SERVICE_URL);
		} catch (AxisFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void getGroup1(String kgroup)
    {
    	Object[] params=new Object[]{kgroup};
    	
    	Class[] retTypes =new Class[]{Group1.class};
    	try {
			Object[] response=_service.callServiceInventory("getGroup1ByCode", params,retTypes);
			Group1 item = (Group1)response[0];
			if(item==null) return;
			if(item.getGroups()==null) return;
			int count=item.getGroups().length;
			list = new ArrayList<Group1>();
			for(int i=0; i < count;i++)
			{
				sf.inventory.Group1 group1=item.getGroups()[i];
				Group1 _group1 = new Group1();
				_group1.setKgroup(group1.getKgroup());
				_group1.setNo(i+1);
				_group1.setKgroup1(group1.getKgroup1());
				_group1.setNgroup1(group1.getNgroup1());
				if(null!=_group1) 
				{
					list.add(_group1);
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
			ngroup1.add(ro);
		}
		catch(Exception ex){}
	}
	public void activate() {
		// TODO Auto-generated method stub
		setResponsePage(SubLevel1Page.class);
	}

	public void cancel(boolean flag) {
		// TODO Auto-generated method stub
		PageParameters p = new PageParameters();
		p.set("kgroup", group1.getKgroup());
		setResponsePage(SubLevel1Page.class,p);
	}

	public int delete() {
		// TODO Auto-generated method stub
		form.process(null);
		Group1 valuta= (Group1)form.getModelObject();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String dateNow = sdf.format(new Date());
		valuta.setTglupdate(Long.parseLong(dateNow));
		valuta.setUserupdate(UserInfo.USERID);
		
    	Object[] params=new Object[]{valuta};
    	Class[] retTypes =new Class[]{Boolean.class};
    	
    	Object[] response;
		try {
			response = _service.callServiceInventory("deleteGroup1", params, retTypes);
			Boolean ret=(Boolean)response[0];
			if(ret)
			{
				PanelAction.actionType=2;
				PageParameters p = new PageParameters();
				p.set("kgroup", group1.getKgroup());
				group1 = null;
				setResponsePage(SubLevel1Page.class,p);
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
			kgroup1.remove(ro);
			ngroup1.remove(ro);
		}
		catch(Exception ex){}
	}

	public int insert() {
		// TODO Auto-generated method stub
		form.process(null);
		Group1 valuta = (Group1)form.getModelObject();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String dateNow = sdf.format(new Date());
		valuta.setTglupdate(Long.parseLong(dateNow));
		valuta.setUserupdate(UserInfo.USERID);
		
    	Object[] params=new Object[]{valuta};
    	Class[] retTypes =new Class[]{Boolean.class};
    	
    	Object[] response;
    	boolean duplicate=false;
		try {
			response = _service.callServiceInventory("insertGroup1", params, retTypes);
			Boolean ret=(Boolean)response[0];
			if(ret)
			{
				PageParameters p = new PageParameters();
				p.set("kgroup", group1.getKgroup());
				setResponsePage(SubLevel1Page.class,p);
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
		if(group1!=null)
		{
			group1.setKgroup1("");
			group1.setNgroup1("");
			p.set("kgroup", group1.getKgroup());
		}
		setResponsePage(SubLevel1Page.class,p);
		try
		{
			kgroup1.remove(ro);
			ngroup1.remove(ro);
		}
		catch(Exception ex){}
	}

	public void print() {
		// TODO Auto-generated method stub
		
	}

	

	public int update() {
		// TODO Auto-generated method stub
		form.process(null);
		Group1 group1 = (Group1)form.getModelObject();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String dateNow = sdf.format(new Date());
		group1.setTglupdate(Long.parseLong(dateNow));
		group1.setUserupdate(UserInfo.USERID);
		
    	Object[] params=new Object[]{group1};
    	Class[] retTypes =new Class[]{Boolean.class};
    	
    	Object[] response;
		try {
			response = _service.callServiceInventory("updateGroup1", params, retTypes);
			Boolean ret=(Boolean)response[0];
			if(ret)
			{
				PanelAction.actionType=2;
				setResponsePage(SubLevel1Page.class);
			}
			return ret?1:0;
		} catch (AxisFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}
}
