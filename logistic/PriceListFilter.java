package org.apache.wicket.erp.logistic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.axis2.AxisFault;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormSubmitBehavior;
import org.apache.wicket.erp.MenuPanel;
import org.apache.wicket.erp.utils.IActionHandlerIndex;
import org.apache.wicket.erp.utils.PanelAction;
import org.apache.wicket.erp.utils.Service;
import org.apache.wicket.erp.utils.UserInfo;
import org.apache.wicket.extensions.ajax.markup.html.autocomplete.AutoCompleteTextField;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.string.Strings;

import sf.inventory.HargaJual;
import sf.inventory.HargaPokok;
import sf.inventory.Produk;

import java.text.SimpleDateFormat;
public class PriceListFilter extends MenuPanel{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String[] kodes=null ;
	private String[] desc=null ;
	private Service _service;
	public static HargaJual selectedHargaJual;
	public static HargaPokok selectedHargaPokok;
	HashMap<String, Produk> maps;
	HashMap<String, Produk> maps1;
	IActionHandlerIndex _iActionHandler; 
	public PriceListFilter(String id,IActionHandlerIndex iActionHandler)
	{
		super(id);
		_iActionHandler=iActionHandler;
		init();
		getProduk();
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
					Produk produk = maps.get(kode);
					getHargaJual(produk.getKbarang());
					getHargaPokok(produk.getKbarang());
					if(selectedHargaJual==null)
						selectedHargaJual=new HargaJual();
					if(selectedHargaJual!=null)
					{
						selectedHargaJual.setKbarang(produk.getKbarang());
						selectedHargaJual.setNbarang(produk.getNbarang());
						selectedHargaJual.setSatuan(produk.getSatuan());
						selectedHargaJual.setKvaluta(produk.getKvaluta());
					}
					if(selectedHargaPokok==null)
						selectedHargaPokok=new HargaPokok();
					if(selectedHargaPokok!=null)
					{
						selectedHargaPokok.setKbarang(produk.getKbarang());
						selectedHargaPokok.setNbarang(produk.getNbarang());
						selectedHargaPokok.setSatuan(produk.getSatuan());
						selectedHargaPokok.setKvaluta(produk.getKvaluta());
					}
					setResponsePage(new PriceListPage(selectedHargaJual, selectedHargaPokok));
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
					getHargaJual(kode);
					getHargaPokok(kode);
					setResponsePage(new PriceListPage(selectedHargaJual,selectedHargaPokok));
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
		maps=new HashMap<String, Produk>();
		maps1=new HashMap<String, Produk>();
		try {
			_service=new Service(Service.INVENTORY_SERVICE_URL);
		} catch (AxisFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void getProduk()
    {
    	Object[] params=new Object[]{UserInfo.COMPANY};
    	
    	Class[] retTypes =new Class[]{Produk.class};
    	try {
			Object[] response=_service.callServiceInventory("getProduk", params,retTypes);
			Produk item = (Produk)response[0];
			if(item==null) return;
			if(item.getProduks()==null) return;
			int count=item.getProduks().length;
			kodes=new String[count];
			desc = new String[count];
			for(int i=0; i < count;i++)
			{
				Produk acc=item.getProduks()[i];
				if(null!=acc) 
				{
					if(!maps.containsKey(acc.getKbarang()))
					{
						kodes[i]=acc.getKbarang();
						maps.put(acc.getKbarang(), acc);
					}
					
					if(!maps1.containsKey(acc.getNbarang()))
					{
						desc[i]=acc.getNbarang();
						maps1.put(acc.getNbarang(), acc);
					}
					
				}
			}
		} catch (AxisFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
	
	public void getHargaJual(String kbarang)
    {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
    	Object[] params=new Object[]{kbarang,sdf.format(new Date())};
    	
    	Class[] retTypes =new Class[]{HargaJual.class};
    	try {
			Object[] response=_service.callServiceInventory("getLatestHargaJual", params,retTypes);
			selectedHargaJual = (HargaJual)response[0];
		} catch (AxisFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
	
	public void getHargaPokok(String kbarang)
    {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
    	Object[] params=new Object[]{kbarang,sdf.format(new Date())};
    	
    	Class[] retTypes =new Class[]{HargaPokok.class};
    	try {
			Object[] response=_service.callServiceInventory("getLatestHargaPokok", params,retTypes);
			selectedHargaPokok = (HargaPokok)response[0];
		} catch (AxisFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
	
}
