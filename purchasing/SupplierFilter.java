package org.apache.wicket.erp.purchasing;

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

import sf.purchasing.Supplier;

public class SupplierFilter extends MenuPanel{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String[] kodes=null ;
	private String[] desc=null ;
	private Service _service;
	public static Supplier selectedItem;
	HashMap<String, Supplier> maps;
	HashMap<String, Supplier> maps1;
	IActionHandler _iActionHandler; 
	public SupplierFilter(String id,IActionHandler iActionHandler)
	{
		super(id);
		_iActionHandler=iActionHandler;
		init();
		getSupplier();
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
		
		final AutoCompleteTextField<String> fielddesc = new AutoCompleteTextField<String>("ac1",
				new Model<String>(""))
		{
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			protected Iterator<String> getChoices(String input)
			{
				if (Strings.isEmpty(input) || desc==null)
				{
					List<String> emptyList = Collections.emptyList();
					return emptyList.iterator();
				}
				List<String> choices = new ArrayList<String>(desc.length);
				for(String item :desc)
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
		form.add(fielddesc);
		
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
					setResponsePage(new SupplierPage(selectedItem));
				}
			}

			@Override
			protected void onError(AjaxRequestTarget target)
			{
			}
		});
		
		fielddesc.add(new AjaxFormSubmitBehavior(form, "onchange")
		{
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit(AjaxRequestTarget target)
			{
				String item = fielddesc.getModelObject().toString();
				if(item==null) return;
				String kode= item;
				if(maps1.containsKey(kode))
				{
					PanelAction.actionType=2;//set panel action with 2//edit and delete button is enabled.
					selectedItem = maps1.get(kode);
					setResponsePage(new SupplierPage(selectedItem));
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
		maps=new HashMap<String, Supplier>();
		maps1=new HashMap<String, Supplier>();
		try {
			_service=new Service(Service.PURCHASING_SERVICE_URL);
		} catch (AxisFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void getSupplier()
    {
    	Object[] params=new Object[]{null};
    	
    	Class[] retTypes =new Class[]{sf.purchasing.Supplier.class};
    	try {
			Object[] response=_service.callServiceAccounting("getSupplier", params,retTypes);
			Supplier item = (Supplier)response[0];
			if(item==null) return;
			if(item.getSuppliers()==null) return;
			int count=item.getSuppliers().length;
			kodes=new String[count];
			desc = new String[count];
			for(int i=0; i < count;i++)
			{
				Supplier acc=item.getSuppliers()[i];
				if(null!=acc) 
				{
					if(!maps.containsKey(acc.getKvendor()))
					{
						kodes[i]=acc.getKvendor();
						maps.put(acc.getKvendor(), acc);
					}
					
					if(!maps1.containsKey(acc.getNvendor()))
					{
						desc[i]=acc.getNvendor();
						maps1.put(acc.getNvendor(), acc);
					}
					
				}
			}
		} catch (AxisFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
