package org.apache.wicket.erp.file;

import java.util.ArrayList;

import org.apache.axis2.AxisFault;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.erp.ERPWebPage;
import org.apache.wicket.erp.utils.IActionHandler;
import org.apache.wicket.erp.utils.PanelAction;
import org.apache.wicket.erp.utils.Service;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.ListDataProvider;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

public class MenusPage extends ERPWebPage implements IActionHandler{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static MenusPage app;
	private Service _service;
	private Link newmenu;
	private ArrayList<Menus> list=new ArrayList<Menus>();
	private void init()
	{
		try {
			_service=new Service(Service.ACCOUNTING_SERVICE_URL);
		} catch (AxisFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static MenusPage get()
	{
		return app;
	}
	
	public MenusPage(sf.file.Menus menu)
	{
		super("File | Menus");
		init();
		getMenus(menu.getMenuname());
		add(new MenusFilter("menusfilter",this));
		final DataView<Menus> dataView = new DataView<Menus>("pageable", new ListDataProvider(
                list)) {
            /**
					 * 
					 */
			private static final long serialVersionUID = 1L;

			public void populateItem(final Item item) {
                final Menus _hj = (Menus) item.getModelObject();
				item.add(new Label("no", String.valueOf(_hj.getNo())));
				item.add(new Label("menuname", _hj.getMenuname()));
				item.add(new Label("recstatus", _hj.getRecstatus()));
				item.add(new AjaxLink<Menus>("select")
				{
					/**
					 * 
					 */
					private static final long serialVersionUID = 1L;

					@Override
					public void onClick(AjaxRequestTarget target) {
						// TODO Auto-generated method stub
						Menus header=(Menus)getParent().getDefaultModelObject();
						PanelAction.actionType=2;
						setResponsePage(new MenusInput(header));
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
		final FeedbackPanel feedback = new FeedbackPanel("feedback");
		add(feedback);
		
		newmenu=new Link("newmenu")
		{
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				// TODO Auto-generated method stub
				//setResponsePage(ModalKursHarianPage.class, new PageParameters());
				setResponsePage(MenusInput.class);
			}
		};
		
		add(newmenu);
		app=this;
	}
	public MenusPage(PageParameters p)
	{
		this(new Menus());
	}
	public MenusPage()
	{
		this(new PageParameters());
	}
		
	public void getMenus(String menu)
    {
		Object[] params=new Object[]{menu};
    	Class[] retTypes =new Class[]{sf.file.Menus.class};
    	try {
    		String method="getMenus";
    		if(menu!=null && !menu.equals(""))
    			method="getMenuByName";
			Object[] response=_service.callServiceAccounting(method, params,retTypes);
			sf.file.Menus item = (sf.file.Menus)response[0];
			if(item==null) return;
			if(item.getMenus()==null) return;
			int count=item.getMenus().length;
			list = new ArrayList<Menus>();
			for(int i=0; i < count;i++)
			{
				sf.file.Menus prheader=item.getMenus()[i];
				if(prheader==null)
					continue;
				Menus _prheader = new Menus();
				_prheader.setNo(i+1);
				_prheader.setMenuname(prheader.getMenuname());
				_prheader.setRecstatus(prheader.getRecstatus());
				if(null!=_prheader) 
				{
					list.add(_prheader);
				}
			}
		} catch (AxisFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

	public void activate() {
		// TODO Auto-generated method stub
		
	}

	public void cancel(boolean flag) {
		// TODO Auto-generated method stub
		
	}

	public int delete() {
		// TODO Auto-generated method stub
		return 0;
	}

	public void deleteAction() {
		// TODO Auto-generated method stub
		
	}

	public void editAction() {
		// TODO Auto-generated method stub
		
	}

	public int insert() {
		// TODO Auto-generated method stub
		return 0;
	}

	public void newAction() {
		// TODO Auto-generated method stub
		
	}

	public void print() {
		// TODO Auto-generated method stub
		
	}

	public void readOnly() {
		// TODO Auto-generated method stub
		
	}

	public int update() {
		// TODO Auto-generated method stub
		return 0;
	}
}
