package org.apache.wicket.erp.purchasing;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.axis2.AxisFault;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.erp.ERPWebPage;
import org.apache.wicket.erp.logistic.Group1;
import org.apache.wicket.erp.utils.PanelAction;
import org.apache.wicket.erp.utils.Service;
import org.apache.wicket.erp.utils.UserInfo;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.ListDataProvider;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

public class PurchaseRequestPage extends ERPWebPage{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static PurchaseRequestPage app;
	private Service _service;
	private Link newpr;
	private HashMap<String, sf.purchasing.PRHeader> headers=new HashMap<String, sf.purchasing.PRHeader>();
	private ArrayList<PRHeader> list=new ArrayList<PRHeader>();
	private void init()
	{
		try {
			_service=new Service(Service.PURCHASING_SERVICE_URL);
		} catch (AxisFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static PurchaseRequestPage get()
	{
		return app;
	}
	
	public PurchaseRequestPage(PageParameters p)
	{
		super("Purchasing | Supplier");
		init();
		int idx =0;
		if(!p.get("filter").isNull())
		{
			idx =Integer.parseInt(p.get("filter").toString());
			getPRHeaderequest(idx);
		}
		else
		{
			getPRHeaderequest(idx);
		}	
		
		add(new PurchaseRequestFilter("prequestfilter"));
		
		final DataView<PRHeader> dataView = new DataView<PRHeader>("pageable", new ListDataProvider(
                list)) {
            /**
					 * 
					 */
			private static final long serialVersionUID = 1L;

			public void populateItem(final Item item) {
                final PRHeader _hj = (PRHeader) item.getModelObject();
				item.add(new Label("no", String.valueOf(_hj.getNo())));
				item.add(new Label("nobukti", _hj.getNobukti()));
				item.add(new Label("tglbukti", _hj.getTglbukti()));
				item.add(new Label("userinput", _hj.getUserinput()));
				item.add(new Label("approvedby", _hj.getApprovedby()));
				item.add(new Label("po", _hj.getPo()));
				item.add(new AjaxLink<Group1>("select")
				{
					/**
					 * 
					 */
					private static final long serialVersionUID = 1L;

					@Override
					public void onClick(AjaxRequestTarget target) {
						// TODO Auto-generated method stub
						PRHeader header=(PRHeader)getParent().getDefaultModelObject();
						PanelAction.actionType=2;
						sf.purchasing.PRHeader _head=headers.get(header.getNobukti());
						header.setKeterangan(_head.getKeterangan());
						header.setKcompany(_head.getKcompany());
						header.setApprovedby(_head.getApprovedby());
						header.setApprovedtgl(_head.getApprovedtgl());
						setResponsePage(new PurchaseRequestDetail(header));
					}
				});
				item.add(new AjaxLink<Group1>("approval")
				{
					/**
					 * 
					 */
					private static final long serialVersionUID = 1L;

					@Override
					public void onClick(AjaxRequestTarget target) {
						
						PRHeader header=(PRHeader)getParent().getDefaultModelObject();
						sf.purchasing.PRHeader _head=headers.get(header.getNobukti());
						if(_head.getApprovedby()==null || _head.getApprovedby().equals(""))
						{
							header.setKeterangan(_head.getKeterangan());
							header.setKcompany(_head.getKcompany());
							header.setApprovedby(_head.getApprovedby());
							header.setApprovedtgl(_head.getApprovedtgl());
							setResponsePage(new ApprovalPRequest(header));
						}
						else
						{
							target.appendJavaScript("alert('You can\\'t approval this selected purchase request.');");
						}
					}
				}).setEnabled(UserInfo.APPROVE_QUOTATION);
				item.add(new AjaxLink<Group1>("un approval")
				{
					/**
					 * 
					 */
					private static final long serialVersionUID = 1L;

					@Override
					public void onClick(AjaxRequestTarget target) {
						// TODO Auto-generated method stub
						PRHeader header=(PRHeader)getParent().getDefaultModelObject();
						sf.purchasing.PRHeader _head=headers.get(header.getNobukti());
						if(_head.getApprovedby()!=null && !_head.getApprovedby().equals(""))
						{
							header.setKeterangan(_head.getKeterangan());
							header.setKcompany(_head.getKcompany());
							header.setApprovedby(_head.getApprovedby());
							header.setApprovedtgl(_head.getApprovedtgl());
							setResponsePage(new UnApprovalPRequest(header));
						}
						else
						{
							target.appendJavaScript("alert('You can\\'t un approval this selected purchase request.');");
						}
					}
				}).setEnabled(UserInfo.APPROVE_QUOTATION);
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
		
		newpr=new Link("newpr")
		{
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				// TODO Auto-generated method stub
				//setResponsePage(ModalKursHarianPage.class, new PageParameters());
				setResponsePage(PurchaseRequestDetail.class);
			}
		};
		
		add(newpr);
		app=this;
	}
	public PurchaseRequestPage()
	{
		this(new PageParameters());
	}
		
	public void getPRHeaderequest(int idx)
    {
		Object[] params=null;
		if(idx==0)
			params=new Object[]{"","",0};
		else
			params=new Object[]{"","",0};
    	
    	Class[] retTypes =new Class[]{sf.purchasing.PRHeader.class};
    	try {
    		String method="getPrequisitionHeader";
    		if(idx==1)// by no bukti
    		{
    			params=new Object[]{PurchaseRequestFilter.selectedItem.getNobukti()};
    			method="getPRequestHeaderByNoBukti";
    		}
    		else if(idx==2)//by tanggal bukti
    		{
    			params=new Object[]{PurchaseRequestFilter.selectedItem.getTglbukti()};
    			method="getPRequestHeaderByTglBukti";
    		}
    		else if(idx==3)//by bulan
    		{
    			String bulan = PurchaseRequestFilter.selectedItem.getTglbukti().substring(4,6);
    			params=new Object[]{bulan};
    			method="getPRequestHeaderByBulan";
    		}
    		else if(idx==4)//by requester
    		{
    			params=new Object[]{PurchaseRequestFilter.selectedItem.getUserinput()};
    			method="getPRequestHeaderByRequester";
    		}
    		else if(idx==5)
    		{
    			method="getPrequisitionHeaderBelumPO";
    		}
    		else if(idx==6)
    		{
    			method="getPrequisitionHeaderSudahPO";
    		}
    		else if(idx==7)
    			method="getPrequisitionHeaderBelumApproved";
    		else if(idx==8)
    			method="getPrequisitionHeaderSudahApproved";
    		
			Object[] response=_service.callServicePurchasing(method, params,retTypes);
			sf.purchasing.PRHeader item = (sf.purchasing.PRHeader)response[0];
			if(item==null) return;
			if(item.getHeaders()==null) return;
			int count=item.getHeaders().length;
			list = new ArrayList<PRHeader>();
			for(int i=0; i < count;i++)
			{
				sf.purchasing.PRHeader prheader=item.getHeaders()[i];
				if(prheader==null)
					continue;
				headers.put(prheader.getNobukti(), prheader);
				PRHeader _prheader = new PRHeader();
				_prheader.setNo(i+1);
				_prheader.setNobukti(prheader.getNobukti());
				_prheader.setTglbukti(prheader.getTglbukti());
				_prheader.setTglbukti2(prheader.getTglbukti());
				_prheader.setUserinput(prheader.getUserinput());
				_prheader.setApprovedby(prheader.getApprovedby());
				_prheader.setPo(prheader.getPo());

				if(null!=_prheader) 
				{
					list.add(_prheader);
				}
			}
		} catch (AxisFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
