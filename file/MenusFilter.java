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

import sf.file.Menus;

public class MenusFilter extends MenuPanel{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String[] kodes=null ;
	private Service _service;
	public static Menus selectedItem;
	HashMap<String, Menus> maps;
	IActionHandler _iActionHandler; 
	public MenusFilter(String id,IActionHandler iActionHandler)
	{
		super(id);
		_iActionHandler=iActionHandler;
		init();
		getMenus();
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
					setResponsePage(new MenusPage(selectedItem));
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
		maps=new HashMap<String, Menus>();
		try {
			_service=new Service(Service.ACCOUNTING_SERVICE_URL);
		} catch (AxisFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void getMenus()
    {
    	Object[] params=new Object[]{null};
    	
    	Class[] retTypes =new Class[]{sf.file.Menus.class};
    	try {
			Object[] response=_service.callServiceAccounting("getMenus", params,retTypes);
			sf.file.Menus item = (sf.file.Menus)response[0];
			if(item==null) return;
			if(item.getMenus()==null) return;
			int count=item.getMenus().length;
			kodes=new String[count];
			for(int i=0; i < count;i++)
			{
				Menus acc=item.getMenus()[i];
				if(null!=acc) 
				{
					if(!maps.containsKey(acc.getMenuname()))
					{
						kodes[i]=acc.getMenuname();
						maps.put(acc.getMenuname(), acc);
					}
				}
			}
		} catch (AxisFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
