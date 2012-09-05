package org.apache.wicket.erp.file;

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
import sf.purchasing.Supplier;

public class UserAccessFilter extends MenuPanel{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String[] kodes=null ;
	private Service _service;
	public static User selectedItem;
	HashMap<String, User> maps;
	IActionHandler _iActionHandler; 
	public UserAccessFilter(String id,IActionHandler iActionHandler)
	{
		super(id);
		_iActionHandler=iActionHandler;
		init();
		getUserID();
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
				if (Strings.isEmpty(input) || kodes==null)
				{
					List<String> emptyList = Collections.emptyList();
					return emptyList.iterator();
				}
				List<String> choices = new ArrayList<String>(kodes.length);
				for(String item :kodes)
				{
					if (item.toUpperCase().startsWith(input.toUpperCase()))
					{
						choices.add(item);
						if (choices.size() == 10)
						{
							break;
						}
					}
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
				if(item==null) return;
				String kode= item;
				if(maps.containsKey(kode))
				{
					PanelAction.actionType=2;//set panel action with 2//edit and delete button is enabled.
					selectedItem = maps.get(kode);
					setResponsePage(new UserAccessPage(selectedItem));
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
	
	public void getUserID()
    {
    	Object[] params=new Object[]{null};
    	
    	Class[] retTypes =new Class[]{User.class};
    	try {
			Object[] response=_service.callServiceAccounting("getUsers", params,retTypes);
			User item = (User)response[0];
			if(item==null) return;
			if(item.getUsers()==null) return;
			int count=item.getUsers().length;
			kodes=new String[count];
			for(int i=0; i < count;i++)
			{
				User acc=item.getUsers()[i];
				if(null!=acc) 
				{
					if(!maps.containsKey(acc.getKstaff()))
					{
						kodes[i]=acc.getKstaff();
						maps.put(acc.getKstaff(), acc);
					}
				}
			}
		} catch (AxisFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
