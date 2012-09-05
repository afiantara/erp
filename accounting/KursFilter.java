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
import org.apache.wicket.erp.utils.PanelActionKurs;
import org.apache.wicket.erp.utils.Service;
import org.apache.wicket.extensions.ajax.markup.html.autocomplete.AutoCompleteTextField;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.string.Strings;

import sf.accounting.Valuta;

public class KursFilter extends MenuPanel{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String[] kodes=null ;  
	private Service _service;
	public static Valuta selectedItem;
	HashMap<String, Valuta> maps;
	IActionHandler _iActionHandler; 
	public KursFilter(String id,IActionHandler iActionHandler)
	{
		super(id);
		_iActionHandler=iActionHandler;
		init();
		getValuta();
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
					PanelActionKurs.actionType=2;//set panel action with 2//edit and delete button is enabled.
					selectedItem = maps.get(kstaff);
					setResponsePage(new KursPage(selectedItem));
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
		maps=new HashMap<String, Valuta>();
		try {
			_service=new Service(Service.ACCOUNTING_SERVICE_URL);
		} catch (AxisFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void getValuta()
    {
    	Object[] params=new Object[]{null};
    	
    	Class[] retTypes =new Class[]{Valuta.class};
    	try {
			Object[] response=_service.callServiceAccounting("getValutas", params,retTypes);
			Valuta item = (Valuta)response[0];
			if(item==null) return;
			int count=item.getValutas().length;
			kodes=new String[count];
			for(int i=0; i < count;i++)
			{
				Valuta valuta=item.getValutas()[i];
				if(null!=valuta) 
				{
					if(!maps.containsKey(valuta.getKvaluta()))
					{
						kodes[i]=valuta.getKvaluta()+ "-" + valuta.getKetvaluta();
						maps.put(valuta.getKvaluta(), valuta);
					}
				}
			}
		} catch (AxisFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
