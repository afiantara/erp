package org.apache.wicket.erp.accounting;

import java.util.Date;

import org.apache.axis2.AxisFault;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.erp.ERPApplication;
import org.apache.wicket.erp.ERPWebPage;
import org.apache.wicket.erp.utils.IActionHandler;
import org.apache.wicket.erp.utils.PanelActionKurs;
import org.apache.wicket.erp.utils.Service;
import org.apache.wicket.erp.utils.UserInfo;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.link.PopupSettings;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import sf.accounting.Valuta;

import java.text.SimpleDateFormat;

public class KursPage extends ERPWebPage implements IActionHandler{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private RequiredTextField<String> kvaluta;
	private TextField<String> ketvaluta;
	boolean isReadOnly=true;
	public Form<Valuta> form;
	final AttributeModifier ro = new AttributeModifier("readonly", isReadOnly);
	private Valuta valuta = new Valuta();
	private static KursPage app;
	private Service _service;
	private ModalWindow modal1;
	private Link listKursPajakButton;
	private Link listKursButton;
	private void init()
	{
		try {
			_service=new Service(Service.ACCOUNTING_SERVICE_URL);
		} catch (AxisFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static KursPage get()
	{
		return app;
	}
	
	public KursPage(PageParameters p)
	{
		this(new Valuta());
		
		kvaluta.remove(ro);
		ketvaluta.remove(ro);
	}
	public KursPage()
	{
		this(new Valuta());
	}
	public KursPage(Valuta valuta)
	{
		super("Accounting | Currency");
		init();
		this.valuta = valuta;
		add(new KursFilter("kursfilter",this));
		form = new Form<Valuta>("form", new CompoundPropertyModel<Valuta>(this.valuta));
		add(form);
		// add a simple text field that uses Input's 'text' property. Nothing
		// can go wrong here
		 kvaluta=new RequiredTextField<String>("kvaluta");
		 ketvaluta=new TextField<String>("ketvaluta");
		this.readOnly();
		form.add(kvaluta);
		form.add(ketvaluta);
		
		PopupSettings popupSettings = new PopupSettings(PopupSettings.LOCATION_BAR | PopupSettings.RESIZABLE | PopupSettings.SCROLLBARS).
		setHeight(300).setWidth(730).setTop(300).setLeft(180);
		
		        
        listKursButton=new Link("kursButton")
		{
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				// TODO Auto-generated method stub
				//setResponsePage(ModalKursHarianPage.class, new PageParameters());
				PageParameters p =new PageParameters();
				p.add("kvaluta", kvaluta.getModelObject());
				setResponsePage(ModalKursHarianPage.class,p);
			}
		};
		listKursButton.setPopupSettings(popupSettings);
		listKursButton.setEnabled((PanelActionKurs.actionType==2||PanelActionKurs.actionType==3)? true:false);
		form.add(listKursButton);
		
		
		listKursPajakButton=new Link("kurspajakButton")
		{
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;
			@Override
			public void onClick() {
				// TODO Auto-generated method stub
				PageParameters p =new PageParameters();
				p.add("kvaluta", kvaluta.getModelObject());
				setResponsePage(ModalKursPajakPage.class,p);
			}
		};
		listKursPajakButton.setPopupSettings(popupSettings);
		listKursPajakButton.setEnabled((PanelActionKurs.actionType==2||PanelActionKurs.actionType==3)? true:false);
		form.add(listKursPajakButton);
        
		final FeedbackPanel feedback = new FeedbackPanel("feedback");
		add(feedback);
		form.add(new PanelActionKurs("actionpanel",this));
		app=this;
		ERPApplication.pageTitle="Accounting | Currency";
	}
	public boolean updateValuta()
	{
		form.process(null);
		Valuta valuta = (Valuta)form.getModelObject();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String dateNow = sdf.format(new Date());
		valuta.setTglupdate(Long.parseLong(dateNow));
		valuta.setUserupdate(UserInfo.USERID);
		
    	Object[] params=new Object[]{valuta};
    	Class[] retTypes =new Class[]{Boolean.class};
    	
    	Object[] response;
		try {
			response = _service.callServiceAccounting("updateValuta", params, retTypes);
			Boolean ret=(Boolean)response[0];
			if(ret)
			{
				setResponsePage(this);
			}
			return ret;
		} catch (AxisFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	public int delete() {
		// TODO Auto-generated method stub
		form.process(null);
		Valuta valuta= (Valuta)form.getModelObject();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String dateNow = sdf.format(new Date());
		valuta.setTglupdate(Long.parseLong(dateNow));
		valuta.setUserupdate(UserInfo.USERID);
		
    	Object[] params=new Object[]{valuta};
    	Class[] retTypes =new Class[]{Boolean.class};
    	
    	Object[] response;
		try {
			response = _service.callServiceAccounting("deleteValuta", params, retTypes);
			Boolean ret=(Boolean)response[0];
			if(ret)
			{
				setResponsePage(KursPage.class);
				return 1;
			}
			return 0;
		} catch (AxisFault e) {
			// TODO Auto-generated catch block
			error("Data tidak berhasil disimpan\r\n" + e.getMessage());
		}
		return 2;
	}

	public int insert() {
		// TODO Auto-generated method stub
		form.process(null);
		Valuta valuta = (Valuta)form.getModelObject();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String dateNow = sdf.format(new Date());
		valuta.setTglupdate(Long.parseLong(dateNow));
		valuta.setUserupdate(UserInfo.USERID);
		
    	Object[] params=new Object[]{valuta};
    	Class[] retTypes =new Class[]{Boolean.class};
    	
    	Object[] response;
    	boolean duplicate=false;
		try {
			response = _service.callServiceAccounting("insertValuta", params, retTypes);
			Boolean ret=(Boolean)response[0];
			if(ret)
			{
				setResponsePage(KursPage.class);
				return 1;
			}
			return 0;
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

	public int update() {
		// TODO Auto-generated method stub
		Boolean bRet=updateValuta();
		if(bRet)
		{
			info("Update Valuta has been successfully.");
			return 1;
		}
		else
			error("Failed to update Valuta.");
		return 0;
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
			kvaluta.remove(ro);
			ketvaluta.remove(ro);
		}
		catch(Exception ex){}
	}

	public void newAction() {
		// TODO Auto-generated method stub
		PageParameters p= new PageParameters();
		p.add("new", 1);
		setResponsePage(this);
		try
		{
			kvaluta.remove(ro);
			ketvaluta.remove(ro);
		}
		catch(Exception ex){}
	}

	public void readOnly() {
		// TODO Auto-generated method stub
		try
		{
			kvaluta.add(ro);
			ketvaluta.add(ro);
		}
		catch(Exception ex){}
	}

	public void cancel() {
		// TODO Auto-generated method stub
		
	}

	public void activate() {
		// TODO Auto-generated method stub
		setResponsePage(KursPage.class);
	}

	public void print() {
		// TODO Auto-generated method stub
		
	}

	public void cancel(boolean flag) {
		// TODO Auto-generated method stub
		if(flag)
			setResponsePage(new KursPage(KursFilter.selectedItem));
		else
			setResponsePage(KursPage.class);
	}

	public void showKurs(AjaxRequestTarget target) {
		// TODO Auto-generated method stub
		modal1.show(target);
	}

	public void showKursPajak(AjaxRequestTarget target) {
		// TODO Auto-generated method stub
		
	}
}
