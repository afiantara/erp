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

import sf.accounting.Account;

public class COAFilter extends MenuPanel{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String[] kodes=null ;
	private String[] desc=null ;
	private Service _service;
	public static sf.accounting.Account selectedItem;
	HashMap<String, sf.accounting.Account> maps;
	HashMap<String, sf.accounting.Account> maps1;
	IActionHandler _iActionHandler; 
	public COAFilter(String id,IActionHandler iActionHandler)
	{
		super(id);
		_iActionHandler=iActionHandler;
		init();
		getAccount();
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
					choices.add(item);
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
					choices.add(item);
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
					setResponsePage(new COAPage(selectedItem));
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
					setResponsePage(new COAPage(selectedItem));
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
		maps=new HashMap<String, Account>();
		maps1=new HashMap<String, Account>();
		try {
			_service=new Service(Service.ACCOUNTING_SERVICE_URL);
		} catch (AxisFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void getAccount()
    {
    	Object[] params=new Object[]{null};
    	
    	Class[] retTypes =new Class[]{sf.accounting.Account.class};
    	try {
			Object[] response=_service.callServiceAccounting("getAccounts", params,retTypes);
			sf.accounting.Account item = (sf.accounting.Account)response[0];
			if(item==null) return;
			if(item.getAccounts()==null) return;
			int count=item.getAccounts().length;
			kodes=new String[count];
			desc = new String[count];
			for(int i=0; i < count;i++)
			{
				sf.accounting.Account acc=item.getAccounts()[i];
				if(null!=acc) 
				{
					if(!maps.containsKey(acc.getAccno()))
					{
						kodes[i]=acc.getAccno();
						maps.put(acc.getAccno(), acc);
					}
					
					if(!maps1.containsKey(acc.getAccdesc()))
					{
						desc[i]=acc.getAccdesc();
						maps1.put(acc.getAccdesc(), acc);
					}
					
				}
			}
		} catch (AxisFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
