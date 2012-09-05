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

import sf.accounting.User;

public class UserAccessPage extends ERPWebPage implements IActionHandler{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static UserAccessPage app;
	private Service _service;
	private Link newmenu;
	
	private ArrayList<UserAccess> list=new ArrayList<UserAccess>();
	private void init()
	{
		try {
			_service=new Service(Service.ACCOUNTING_SERVICE_URL);
		} catch (AxisFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static UserAccessPage get()
	{
		return app;
	}
	
	public UserAccessPage(User user)
	{
		super("File | User Akses");
		init();
		getUserAccess(user.getKstaff());
		add(new UserAccessFilter("useraccessfilter",this));
		
		final DataView<UserAccess> dataView = new DataView<UserAccess>("pageable", new ListDataProvider(
                list)) {
            /**
					 * 
					 */
			private static final long serialVersionUID = 1L;

			public void populateItem(final Item item) {
                final UserAccess _hj = (UserAccess) item.getModelObject();
				item.add(new Label("no", String.valueOf(_hj.getNo())));
				item.add(new Label("userid", _hj.getUserid()));
				item.add(new Label("menuname", _hj.getMenuname()));
				item.add(new Label("recstatus", _hj.getRecstatus()));
				item.add(new AjaxLink<UserAccess>("select")
				{
					/**
					 * 
					 */
					private static final long serialVersionUID = 1L;

					@Override
					public void onClick(AjaxRequestTarget target) {
						// TODO Auto-generated method stub
						UserAccess header=(UserAccess)getParent().getDefaultModelObject();
						PanelAction.actionType=2;
						setResponsePage(new UserAccessInput(header));
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
				setResponsePage(UserAccessInput.class);
			}
		};
		
		add(newmenu);
		app=this;
	}
	public UserAccessPage(PageParameters p)
	{
		this(new User());
	}
	public UserAccessPage()
	{
		this(new PageParameters());
	}
		
	public void getUserAccess(String userid)
    {
		Object[] params=new Object[]{userid};
    	Class[] retTypes =new Class[]{sf.file.UserAccess.class};
    	try {
    		String method="getUserAccess";
			Object[] response=_service.callServiceAccounting(method, params,retTypes);
			sf.file.UserAccess item = (sf.file.UserAccess)response[0];
			if(item==null) return;
			if(item.getUsers()==null) return;
			int count=item.getUsers().length;
			list = new ArrayList<UserAccess>();
			for(int i=0; i < count;i++)
			{
				sf.file.UserAccess prheader=item.getUsers()[i];
				if(prheader==null)
					continue;
				UserAccess _prheader = new UserAccess();
				_prheader.setNo(i+1);
				_prheader.setUserid(prheader.getUserid());
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
