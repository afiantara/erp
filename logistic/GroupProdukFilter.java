package org.apache.wicket.erp.logistic;

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

import sf.inventory.Group;

public class GroupProdukFilter extends MenuPanel{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String[] kodes=null ;
	private String[] desc=null ;
	private Service _service;
	public static Group selectedItem;
	HashMap<String, Group> maps;
	IActionHandler _iActionHandler; 
	public GroupProdukFilter(String id,IActionHandler iActionHandler)
	{
		super(id);
		_iActionHandler=iActionHandler;
		init();
		getGroup();
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
					setResponsePage(new GroupProdukPage(selectedItem));
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
		maps=new HashMap<String, Group>();
		try {
			_service=new Service(Service.INVENTORY_SERVICE_URL);
		} catch (AxisFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void getGroup()
    {
    	Object[] params=new Object[]{null};
    	
    	Class[] retTypes =new Class[]{Group.class};
    	try {
			Object[] response=_service.callServiceInventory("getGroup", params,retTypes);
			Group item = (Group)response[0];
			if(item==null) return;
			if(item.getGroups()==null) return;
			int count=item.getGroups().length;
			kodes=new String[count];
			for(int i=0; i < count;i++)
			{
				Group acc=item.getGroups()[i];
				if(null!=acc) 
				{
					if(!maps.containsKey(acc.getKgroup()))
					{
						kodes[i]=acc.getKgroup();
						maps.put(acc.getKgroup(), acc);
					}
				}
			}
		} catch (AxisFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
