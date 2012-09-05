package org.apache.wicket.erp.accounting;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.axis2.AxisFault;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormSubmitBehavior;
import org.apache.wicket.erp.MenuPanel;
import org.apache.wicket.erp.utils.IActionHandler;
import org.apache.wicket.erp.utils.PanelAction;
import org.apache.wicket.erp.utils.Service;
import org.apache.wicket.extensions.ajax.markup.html.autocomplete.AutoCompleteTextField;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.string.Strings;

import sf.accounting.User;

public class UserFilter extends MenuPanel{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String[] kodes=null ;  
	private Service _service;
	public static User selectedItem;
	HashMap<String, User> maps;
	IActionHandler _iActionHandler; 
	public UserFilter(String id,IActionHandler iActionHandler)
	{
		super(id);
		_iActionHandler=iActionHandler;
		init();
		getUser();
		Form<Void> form = new Form<Void>("searchform");
		add(form);
		
		final AutoCompleteTextField<String> field = new AutoCompleteTextField<String>("ac",
				new Model<String>(""))
		{
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			protected Iterator<String> getChoices(String input)
			{
				if (Strings.isEmpty(input))
				{
					List<String> emptyList = Collections.emptyList();
					return emptyList.iterator();
				}
				List<String> choices = new ArrayList<String>(kodes.length);
				for(String item :kodes)
				{
					choices.add(item);
				}
				return choices.iterator();
			}
		};
		form.add(field);
		
		field.add(new AjaxFormSubmitBehavior(form, "onchange")
		{
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit(AjaxRequestTarget target)
			{
				String item = field.getModelObject().toString();
				String kstaff= item.split("\\-")[0];
				if(maps.containsKey(kstaff))
				{
					PanelAction.actionType=2;//set panel action with 2//edit and delete button is enabled.
					selectedItem = maps.get(kstaff);
					setResponsePage(new UserPage(selectedItem));
				}
			}

			@Override
			protected void onError(AjaxRequestTarget target)
			{
			}
		});
	}
	
	
	private void init()
	{
		maps=new HashMap<String, User>();
		try {
			_service=new Service(Service.ACCOUNTING_SERVICE_URL);
		} catch (AxisFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void getUser()
    {
    	Object[] params=new Object[]{null};
    	
    	Class[] retTypes =new Class[]{User.class};
    	try {
			Object[] response=_service.callServiceAccounting("getUsers", params,retTypes);
			User item = (User)response[0];
			if(item==null) return;
			int count=item.getUsers().length;
			kodes=new String[count];
			for(int i=0; i < count;i++)
			{
				User user=item.getUsers()[i];
				if(null!=user) 
				{
					if(!maps.containsKey(user.getKstaff()))
					{
						kodes[i]=user.getKstaff()+ "-" + user.getNstaff();
						maps.put(user.getKstaff(), user);
					}
				}
			}
		} catch (AxisFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
