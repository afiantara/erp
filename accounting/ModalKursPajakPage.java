package org.apache.wicket.erp.accounting;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.apache.axis2.AxisFault;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.erp.utils.IActionHandler;
import org.apache.wicket.erp.utils.PanelAction;
import org.apache.wicket.erp.utils.Service;
import org.apache.wicket.erp.utils.UserInfo;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.ListDataProvider;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import java.text.SimpleDateFormat;
public class ModalKursPajakPage extends BasePage implements IActionHandler{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Service _service;
	HashMap<String, KursPajak> maps;
	private ArrayList<KursPajak> list=new ArrayList<KursPajak>();
	private Form<sf.accounting.KursPajak> form;
	public static sf.accounting.KursPajak kurs;
	
	private RequiredTextField<String> kvaluta;
	private RequiredTextField<String> tvaluta1;
	private RequiredTextField<String> tvaluta2;
	private TextField<String> nvaluta;
	boolean isReadOnly=true;
	private static String kvalutaValue;
	final AttributeModifier ro = new AttributeModifier("readonly", isReadOnly);
	
	public ModalKursPajakPage(PageParameters p)
	{
		super("Kurs Pajak");
		kvalutaValue=p.get("kvaluta").toString();
		if(kurs==null)
		{
			kurs=new sf.accounting.KursPajak();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			kurs.setTvaluta1(Integer.parseInt(sdf.format(new Date())));
			kurs.setTvaluta2(Integer.parseInt(sdf.format(new Date())));
		}
		kurs.setKvaluta(kvalutaValue);
		init();
		getKurs(kvalutaValue);
		buildPage();
		try
		{
			if(p.get("new")!=null)
			{
				if(p.get("new").toString().equals("1"))
				{
					tvaluta1.remove(ro);
					tvaluta2.remove(ro);
					nvaluta.remove(ro);
				}
		
			}
		}
		catch(Exception ex){}
		
	}
	
	public ModalKursPajakPage()
	{
		super("Kurs Pajak");
		init();
		getKurs(kurs.getKvaluta());
		buildPage();
	}

	@SuppressWarnings("unchecked")
	private void buildPage()
	{
		final DataView<KursPajak> dataView = new DataView<KursPajak>("pageable", new ListDataProvider(
                list)) {
            /**
					 * 
					 */
			private static final long serialVersionUID = 1L;

			public void populateItem(final Item item) {
                final KursPajak _kurs = (KursPajak) item.getModelObject();
				item.add(new Label("no", String.valueOf(_kurs.getNo())));
				item.add(new Label("valuta", _kurs.getKvaluta()));
				item.add(new Label("tanggal1", String.valueOf(_kurs.getTvaluta1())));
				item.add(new Label("tanggal2", String.valueOf(_kurs.getTvaluta2())));
				item.add(new Label("kurs", String.valueOf(_kurs.getNvaluta())));
				item.add(new AjaxLink<KursPajak>("select")
				{
					/**
					 * 
					 */
					private static final long serialVersionUID = 1L;

					@Override
					public void onClick(AjaxRequestTarget target) {
						// TODO Auto-generated method stub
						KursPajak selected = (KursPajak)getParent().getDefaultModelObject();
						kurs=selected;
						PanelAction.actionType=2;
						setResponsePage(ModalKursPajakPage.class);
					}
				});

				item.add(AttributeModifier.replace("class", new AbstractReadOnlyModel<String>()
				{
					private static final long serialVersionUID = 1L;

					@Override
					public String getObject()
					{
						return (item.getIndex() % 2 == 1) ? "even" : "odd";
					}
				}));
            }
        };
        
		dataView.setItemsPerPage(8);
		add(dataView);

		add(new PagingNavigator("navigator", dataView));
		
		
		form = new Form<sf.accounting.KursPajak>("form", new CompoundPropertyModel<sf.accounting.KursPajak>(this.kurs));
		add(form);
		// add a simple text field that uses Input's 'text' property. Nothing
		// can go wrong here
		 kvaluta=new RequiredTextField<String>("kvaluta");
		 tvaluta1=new RequiredTextField<String>("tvaluta1");
		 tvaluta2=new RequiredTextField<String>("tvaluta2");
		 nvaluta=new TextField<String>("nvaluta");
		 readOnly();
		 form.add(kvaluta);
		 form.add(tvaluta1);
		 form.add(tvaluta2);
		 form.add(nvaluta);
		
		 form.add(new PanelAction("actionpanel",this));
	}
	
	private void init()
	{
		maps=new HashMap<String, KursPajak>();
		try {
			_service=new Service(Service.ACCOUNTING_SERVICE_URL);
		} catch (AxisFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void getKurs(String kvaluta)
    {
    	Object[] params=new Object[]{kvaluta};
    	
    	Class[] retTypes =new Class[]{sf.accounting.KursPajak.class};
    	try {
			Object[] response=_service.callServiceAccounting("getKursPajakByCode", params,retTypes);
			sf.accounting.KursPajak item = (sf.accounting.KursPajak)response[0];
			if(item==null) return;
			if(item.getKursPajaks()==null) return;
			int count=item.getKursPajaks().length;
			list = new ArrayList<KursPajak>();
			for(int i=0; i < count;i++)
			{
				sf.accounting.KursPajak kurs=item.getKursPajaks()[i];
				KursPajak _kurs = new KursPajak();
				_kurs.setKvaluta(kurs.getKvaluta());
				_kurs.setNo(i+1);
				_kurs.setNvaluta(kurs.getNvaluta());
				_kurs.setTvaluta1(kurs.getTvaluta1());
				_kurs.setTvaluta2(kurs.getTvaluta2());
				if(null!=_kurs) 
				{
					list.add(_kurs);
				}
			}
		} catch (AxisFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

	public void readOnly() {
		// TODO Auto-generated method stub
		try
		{
			kvaluta.add(ro);
			tvaluta1.add(ro);
			tvaluta2.add(ro);
			nvaluta.add(ro);
		}
		catch(Exception ex){}
	}
	public void activate() {
		// TODO Auto-generated method stub
		setResponsePage(ModalKursPajakPage.class);
	}

	public void cancel(boolean flag) {
		// TODO Auto-generated method stub
		PageParameters p = new PageParameters();
		p.set("kvaluta", kurs.getKvaluta());
		setResponsePage(ModalKursPajakPage.class,p);
	}

	public int delete() {
		// TODO Auto-generated method stub
		form.process(null);
		sf.accounting.KursPajak valuta= (sf.accounting.KursPajak)form.getModelObject();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String dateNow = sdf.format(new Date());
		valuta.setTglupdate(Long.parseLong(dateNow));
		valuta.setUserupdate(UserInfo.USERID);
		
    	Object[] params=new Object[]{valuta};
    	Class[] retTypes =new Class[]{Boolean.class};
    	
    	Object[] response;
		try {
			response = _service.callServiceAccounting("deleteKursPajak", params, retTypes);
			Boolean ret=(Boolean)response[0];
			if(ret)
			{
				PanelAction.actionType=2;
				PageParameters p = new PageParameters();
				p.set("kvaluta", kurs.getKvaluta());
				kurs = null;
				setResponsePage(ModalKursPajakPage.class,p);
				return 1;
			}
			return 0;
		} catch (AxisFault e) {
			// TODO Auto-generated catch block
			error("Data tidak berhasil disimpan\r\n" + e.getMessage());
		}
		return 2;
	}

	public void deleteAction() {
		// TODO Auto-generated method stub
		setResponsePage(this);
	}

	public void editAction() {
		// TODO Auto-generated method stub
		try
		{
			setResponsePage(this);
			try
			{
				kvaluta.add(ro);
				tvaluta1.add(ro);
				tvaluta2.add(ro);
				nvaluta.add(ro);
			}
			catch(Exception ex){}
		}
		catch(Exception ex){}
	}

	public int insert() {
		// TODO Auto-generated method stub
		form.process(null);
		sf.accounting.KursPajak valuta = (sf.accounting.KursPajak)form.getModelObject();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String dateNow = sdf.format(new Date());
		valuta.setTglupdate(Long.parseLong(dateNow));
		valuta.setUserupdate(UserInfo.USERID);
		
    	Object[] params=new Object[]{valuta};
    	Class[] retTypes =new Class[]{Boolean.class};
    	
    	Object[] response;
    	boolean duplicate=false;
		try {
			response = _service.callServiceAccounting("insertKursPajak", params, retTypes);
			Boolean ret=(Boolean)response[0];
			if(ret)
			{
				PageParameters p = new PageParameters();
				p.set("kvaluta", kurs.getKvaluta());
				setResponsePage(ModalKursPajakPage.class,p);
				PanelAction.actionType=0;
			}
			return ret?1:0;
		} catch (AxisFault e) {
			// TODO Auto-generated catch block
			if(e.getMessage().indexOf("Duplicate")>=0)
			{
				error(e.getMessage() + ",Click activate button if you would like to activate.");
				duplicate=true;
			}
			else
				error(e.getMessage());
		}
		return duplicate? 2: 0;
	}

	public void newAction() {
		// TODO Auto-generated method stub
		PageParameters p= new PageParameters();
		p.add("new", 1);
		if(kurs!=null)
			p.set("kvaluta", kurs.getKvaluta());
		setResponsePage(ModalKursPajakPage.class,p);
		try
		{
			kvaluta.add(ro);
			tvaluta1.add(ro);
			tvaluta2.add(ro);
			nvaluta.add(ro);
		}
		catch(Exception ex){}
	}

	public void print() {
		// TODO Auto-generated method stub
		
	}

	

	public int update() {
		// TODO Auto-generated method stub
		form.process(null);
		sf.accounting.KursPajak kurs = (sf.accounting.KursPajak)form.getModelObject();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String dateNow = sdf.format(new Date());
		kurs.setTglupdate(Long.parseLong(dateNow));
		kurs.setUserupdate(UserInfo.USERID);
		
    	Object[] params=new Object[]{kurs};
    	Class[] retTypes =new Class[]{Boolean.class};
    	
    	Object[] response;
		try {
			response = _service.callServiceAccounting("updateKursPajak", params, retTypes);
			Boolean ret=(Boolean)response[0];
			if(ret)
			{
				PanelAction.actionType=2;
				setResponsePage(ModalKursPajakPage.class);
			}
			return ret?1:0;
		} catch (AxisFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}
}
