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
import org.apache.wicket.erp.utils.Service;
import org.apache.wicket.extensions.ajax.markup.html.autocomplete.AutoCompleteTextField;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.string.Strings;


public class PurchaseOrderFilter extends MenuPanel{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String[] filter=new String[]{"All","No.Bukti","Tanggal","Bulan","Requester","Belum PO","Sudah PO","Belum Approved","Sudah Approved"} ;
	private String[] search=null ;
	private Service _service;
	public static String selectedFilter;
	public static sf.purchasing.PRHeader selectedItem;
	public static PRHeader header;
	HashMap<String, String> maps;
	HashMap<String, sf.purchasing.PRHeader> maps1;
	private AutoCompleteTextField<String> fielddesc;
	private int idxFilter=0;
	public PurchaseOrderFilter(String id)
	{
		super(id);
		init();
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
				if (Strings.isEmpty(input) || filter==null)
				{
					List<String> emptyList = Collections.emptyList();
					return emptyList.iterator();
				}
				List<String> choices = new ArrayList<String>(filter.length);
				for(String item :filter)
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
		
		fielddesc = new AutoCompleteTextField<String>("ac1",
				new Model<String>(""))
		{
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			protected Iterator<String> getChoices(String input)
			{
				if (Strings.isEmpty(input) || search==null)
				{
					List<String> emptyList = Collections.emptyList();
					return emptyList.iterator();
				}
				List<String> choices = new ArrayList<String>(search.length);
				for(String item :search)
				{
					if(item==null) continue;
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
					//PanelAction.actionType=2;//set panel action with 2//edit and delete button is enabled.
					selectedFilter = maps.get(kode);
					searchByFilter(selectedFilter);
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
				try
				{
					String item = fielddesc.getModelObject().toString();
					if(item==null) return;
					String kode= item;
					if(maps1.containsKey(kode))
					{
						selectedItem = maps1.get(kode);
						
						PageParameters p=new PageParameters();
						p.add("filter", idxFilter);
						setResponsePage(PurchaseRequestPage.class,p);
					}
				}
				catch(Exception ex){}
			}

			@Override
			protected void onError(AjaxRequestTarget target)
			{
			}
		});
	}
	
	private void assignFilterMap()
	{
		for(String item : filter)
		{
			maps.put(item, item);
		}
	}
	
	private void init()
	{
		maps=new HashMap<String, String>();
		assignFilterMap();
		maps1=new HashMap<String, sf.purchasing.PRHeader>();
		try {
			_service=new Service(Service.PURCHASING_SERVICE_URL);
		} catch (AxisFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void searchByFilter(String filter)
	{
		fielddesc.clearInput();
		//"All","No.Bukti","Tanggal","Bulan","Requester","Belum PO","Sudah PO","Belum Approved","Sudah Approved"} ;
		if(filter.equals("All"))
		{
			//ger all request
			idxFilter=0;
			PageParameters p=new PageParameters();
			p.add("filter", "0");
			fielddesc.setEnabled(false);
			setResponsePage(PurchaseRequestPage.class,p);
		}
		else if(filter.equals("No.Bukti"))
		{
			idxFilter=1;
			getPRHeaderequest(0);
		}
		else if(filter.equals("Tanggal"))
		{
			idxFilter=2;
			getPRHeaderequest(1);
		}
		else if(filter.equals("Bulan"))
		{
			idxFilter=3;
			getPRHeaderequest(2);
		}
		else if(filter.equals("Requester"))
		{
			idxFilter=4;
			getPRHeaderequest(3);
		}
		else if(filter.equals("Belum PO"))
		{
			idxFilter=5;
			PageParameters p=new PageParameters();
			p.add("filter", idxFilter);
			fielddesc.setEnabled(false);
			setResponsePage(PurchaseRequestPage.class,p);
		}
		else if(filter.equals("Sudah PO"))
		{
			idxFilter=6;
			PageParameters p=new PageParameters();
			p.add("filter", idxFilter);
			fielddesc.setEnabled(false);
			setResponsePage(PurchaseRequestPage.class,p);
		}
		else if(filter.equals("Belum Approved"))
		{
			idxFilter=7;
			PageParameters p=new PageParameters();
			p.add("filter", idxFilter);
			fielddesc.setEnabled(false);
			setResponsePage(PurchaseRequestPage.class,p);
		}
		else if(filter.equals("Sudah Approved"))
		{
			idxFilter=8;
			PageParameters p=new PageParameters();
			p.add("filter", idxFilter);
			fielddesc.setEnabled(false);
			setResponsePage(PurchaseRequestPage.class,p);
		}
	}
	
	public void getPRHeaderequest(int idx)
    {
		Object[] params=null;
		params=new Object[]{"","",0};
    	Class[] retTypes =new Class[]{sf.purchasing.PRHeader.class};
    	try {
			Object[] response=_service.callServicePurchasing("getPrequisitionHeader", params,retTypes);
			sf.purchasing.PRHeader item = (sf.purchasing.PRHeader)response[0];
			if(item==null) return;
			if(item.getHeaders()==null) return;
			int count=item.getHeaders().length;
			search=new String[count];
			maps1.clear();
			for(int i=0; i < count;i++)
			{
				sf.purchasing.PRHeader prheader=item.getHeaders()[i];
				if(idx==0)
				{
					if(!maps1.containsKey(prheader.getNobukti()))
					{
						maps1.put(prheader.getNobukti(), prheader);
						search[i]= prheader.getNobukti();	
					}
				}
						
				else if(idx==1)
				{
					if(!maps1.containsKey(prheader.getTglbukti()))
					{
						maps1.put(prheader.getTglbukti(), prheader);
						search[i]= prheader.getTglbukti();	
					}
				}
				else if(idx==2)
				{
					String bulan = prheader.getTglbukti().substring(4, 6);
					if(!maps1.containsKey(bulan))
					{
						maps1.put(bulan, prheader);
						search[i]= bulan;	
					}
				}
				else if(idx==3)
				{
					if(!maps1.containsKey(prheader.getUserinput()))
					{
						maps1.put(prheader.getUserinput(), prheader);
						search[i]= prheader.getUserinput();	
					}
				}
				
			}
		} catch (AxisFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
