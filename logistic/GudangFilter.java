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

import sf.inventory.Gudang;

public class GudangFilter extends MenuPanel{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String[] kodes=null ;  
	private Service _service;
	public static Gudang selectedGudang;
	HashMap<String, sf.inventory.Gudang> maps;
	IActionHandler _iActionHandler; 
	public GudangFilter(String id,IActionHandler iActionHandler)
	{
		super(id);
		_iActionHandler=iActionHandler;
		init();
		getGudang();
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
				String kode_gudang = item.split("\\-")[0];
				if(maps.containsKey(kode_gudang))
				{
					PanelAction.actionType=2;//set panel action with 2//edit and delete button is enabled.
					selectedGudang = maps.get(kode_gudang);
					setResponsePage(new GudangPage(selectedGudang));
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
		maps=new HashMap<String, sf.inventory.Gudang>();
		try {
			_service=new Service(Service.INVENTORY_SERVICE_URL);
		} catch (AxisFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void getGudang()
    {
    	Object[] params=new Object[]{null};
    	
    	Class[] retTypes =new Class[]{sf.inventory.Gudang.class};
    	try {
			Object[] response=_service.callServiceInventory("getGudang", params,retTypes);
			sf.inventory.Gudang item = (sf.inventory.Gudang)response[0];
			if(item==null) return;
			int count=item.getGudangs().length;
			kodes=new String[count];
			for(int i=0; i < count;i++)
			{
				sf.inventory.Gudang gudang=item.getGudangs()[i];
				if(null!=gudang) 
				{
					if(!maps.containsKey(gudang.getKgudang()))
					{
						kodes[i]=gudang.getKgudang()+ "-" + gudang.getKota();
						maps.put(gudang.getKgudang(), gudang);
					}
				}
			}
		} catch (AxisFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
