package org.apache.wicket.erp.crm;

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

import sf.crm.Customer;

public class CustomerFilter extends MenuPanel{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String[] kodes=null ;  
	private Service _service;
	public static Customer selectedCustomer;
	HashMap<String, sf.crm.Customer> maps;
	IActionHandler _iActionHandler; 
	public CustomerFilter(String id,IActionHandler iActionHandler)
	{
		super(id);
		_iActionHandler=iActionHandler;
		init();
		getCustomer();
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
				String kode_customer = item.split("\\-")[0];
				if(maps.containsKey(kode_customer))
				{
					PanelAction.actionType=2;//set panel action with 2//edit and delete button is enabled.
					selectedCustomer = maps.get(kode_customer);
					setResponsePage(new CustomerPage(selectedCustomer));
				}
			}

			@Override
			protected void onError(AjaxRequestTarget target)
			{
			}
		});
	}
	
	public void updateModel(Customer cust)
	{
		if(maps.containsKey(cust.getKclient()))
		{
			maps.put(cust.getKclient(), cust);
		}
	}
	
	public void deleteModeil(Customer cust)
	{
		if(maps.containsKey(cust.getKclient()))
		{
			maps.remove(cust.getKclient());
		}
	}
	private void init()
	{
		maps=new HashMap<String, sf.crm.Customer>();
		try {
//			_service=new Service(Service.PURCHASING_SERVICE_URL);
			_service=new Service(Service.CRM_SERVICE_URL);
		} catch (AxisFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void getCustomer()
    {
    	Object[] params=new Object[]{new String("EPP")};
    	
    	Class[] retTypes =new Class[]{sf.crm.Customer.class};
    	try {
			Object[] response=_service.callServiceCRM("getCustomers", params,retTypes);
			sf.crm.Customer item = (sf.crm.Customer)response[0];
			if(item==null) return;
			
			int count = (item.getCustomers() != null) ? item.getCustomers().length : 0;
			kodes=new String[count];
			for(int i=0; i < count;i++)
			{
				sf.crm.Customer customer=item.getCustomers()[i];
				if(null!=customer) 
				{
					if(!maps.containsKey(customer.getKclient()))
					{
						kodes[i]=customer.getKclient()+ "-" + customer.getNclient();
						maps.put(customer.getKclient(), customer);
					}
					else
					{
						maps.put(customer.getKclient(), customer);
					}
					
				}
			}
		} catch (AxisFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
