package org.apache.wicket.erp.purchasing;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.apache.axis2.AxisFault;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.erp.logistic.Group1;
import org.apache.wicket.erp.logistic.Group2;
import org.apache.wicket.erp.utils.Service;
import org.apache.wicket.erp.utils.UserInfo;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.ListDataProvider;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

public class UnApprovalPRequest extends WebPage{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private TextField<String> nobukti;
	private TextField<String> tglbukti;
	private TextArea<String> keterangan;
	private TextField<String> approvedby;
	private TextField<String> approvedtgl;
	
	private ArrayList<PRDetail> list=new ArrayList<PRDetail>();
	boolean isReadOnly=true;
	private Form<PRHeader> form;
	
	final AttributeModifier ro = new AttributeModifier("readonly", isReadOnly);
	private static PRHeader pheader = new PRHeader();
	
	private static UnApprovalPRequest app;
	private Service _service;
	private void init()
	{
		try {
			_service=new Service(Service.PURCHASING_SERVICE_URL);
		} catch (AxisFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static UnApprovalPRequest get()
	{
		return app;
	}
	
	public UnApprovalPRequest(PageParameters p,PRHeader ph)
	{
		this(ph);
		readOnly(false);
		if(p.getNamedKeys().contains("error"))
			error(p.get("error"));
		if(p.getNamedKeys().contains("info"))
			info(p.get("info"));
		
	}
	public UnApprovalPRequest()
	{
		this(new PRHeader());
		
	}
	
	public UnApprovalPRequest(PRHeader ph)
	{
		
		init();
		this.pheader = ph;
		getPRequestDetail(ph.getNobukti());
		form = new Form<PRHeader>("form", new CompoundPropertyModel<PRHeader>(this.pheader));
		add(form);
		// add a simple text field that uses Input's 'text' property. Nothing
		// can go wrong here
		nobukti = new RequiredTextField<String>("nobukti");
		tglbukti= new RequiredTextField<String>("tglbukti");
		keterangan=new TextArea<String>("keterangan");
		approvedby= new TextField<String>("approvedby");
		approvedtgl= new TextField<String>("approvedtgl");
		this.readOnly();
		form.add(nobukti);
		form.add(tglbukti);
		form.add(keterangan);
		form.add(approvedby);
		form.add(approvedtgl);
		final DataView<Group2> dataView = new DataView<Group2>("pageable", new ListDataProvider(
                list)) {
            /**
					 * 
					 */
			private static final long serialVersionUID = 1L;

			public void populateItem(final Item item) {
                final PRDetail _hj = (PRDetail) item.getModelObject();
				item.add(new Label("no", String.valueOf(_hj.getNo())));
				item.add(new Label("kbarang", _hj.getKbarang()));
				item.add(new Label("nbarang", _hj.getNbarang()));
				item.add(new Label("satuan", _hj.getSatuan()));
				item.add(new Label("jumlah", String.valueOf(_hj.getJumlah())));
				item.add(new Label("nobukti1", _hj.getNobukti1()));
				item.add(new Label("ket", _hj.getKet()));
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
		
		
		final FeedbackPanel feedback = new FeedbackPanel("feedback");
		add(feedback);
		
		add(new AjaxLink<Group1>("unapproved")
		{
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target) {
				
				unApprovePRequest();
			}
		});
		
		add(new AjaxLink<Group1>("cancel")
		{
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target) {
				setResponsePage(PurchaseRequestPage.class);
			}
		});
		
		app=this;
	}
	
	public boolean unApprovePRequest()
	{
		form.process(null);
		PRHeader item = (PRHeader)form.getModelObject();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String dateNow = sdf.format(new Date());
		item.setTglupdate(Long.parseLong(dateNow));
		item.setUserupdate(UserInfo.USERID);
		item.setTglupdate(Long.parseLong(dateNow));
		item.setUserupdate(UserInfo.USERID);
		item.setApprovedby(null);
		item.setApprovedtgl(null);
		
    	Object[] params=new Object[]{item};
    	Class[] retTypes =new Class[]{Boolean.class};
    	
    	Object[] response;
		try {
			response = _service.callServicePurchasing("updatePRHeader", params, retTypes);
			Boolean ret=(Boolean)response[0];
			if(ret)
			{
				setResponsePage(PurchaseRequestPage.class);
			}
			return ret;
		} catch (AxisFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	private void readOnly(boolean flag)
	{
		if(!flag)
		{
		}
		else
		{
			nobukti.add(ro);
			tglbukti.add(ro);
			keterangan.add(ro);
			approvedby.add(ro);
			approvedtgl.add(ro);
		}
	}
	public void readOnly() {
		// TODO Auto-generated method stub
		try
		{
			readOnly(true);
		}
		catch(Exception ex){}
	}
	private void getPRequestDetail(String nobukti)
    {
		sf.purchasing.PRHeader _header=new sf.purchasing.PRHeader();
		_header.setNobukti(nobukti);
    	Object[] params=new Object[]{_header};
    	
    	Class[] retTypes =new Class[]{PRDetail.class};
    	try {
			Object[] response=_service.callServicePurchasing("getPrequisitionDetail", params,retTypes);
			sf.purchasing.PRDetail item = (sf.purchasing.PRDetail)response[0];
			if(item==null) return;
			if(item.getDetails()==null) return;
			int count=item.getDetails().length;
			list = new ArrayList<PRDetail>();
			for(int i=0; i < count;i++)
			{
				sf.purchasing.PRDetail hj=item.getDetails()[i];
				PRDetail _hj = new PRDetail();
				_hj.setKbarang(hj.getKbarang());
				_hj.setNbarang(hj.getNbarang());
				_hj.setNo(i+1);
				_hj.setSatuan(hj.getSatuan());
				_hj.setJumlah(hj.getJumlah());
				_hj.setNobukti1(hj.getNobukti1());
				_hj.setKet(hj.getKet());
				if(null!=_hj) 
				{
					list.add(_hj);
				}
			}
		} catch (AxisFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
