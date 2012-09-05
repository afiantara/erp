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

public class PurchaseOrderPage extends ERPWebPage{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static PurchaseOrderPage app;
	private Service _service;
	private Link genereatepo;
	private HashMap<String, sf.purchasing.POHeader> headers=new HashMap<String, sf.purchasing.POHeader>();
	private ArrayList<POHeader> list=new ArrayList<POHeader>();
	
	public String getTitle()
	{
		return "Purchase Order";
	}
	
	private void init()
	{
		try {
			_service=new Service(Service.PURCHASING_SERVICE_URL);
		} catch (AxisFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static PurchaseOrderPage get()
	{
		return app;
	}
	
	public PurchaseOrderPage(PageParameters p)
	{
		super("Purchasing | Supplier");
		init();
		int idx =0;
		if(!p.get("filter").isNull())
		{
			idx =Integer.parseInt(p.get("filter").toString());
			getPOHeade(idx);
		}
		else
		{
			getPOHeade(idx);
		}	
		
		add(new PurchaseOrderFilter("pofilter"));
		
		final DataView<POHeader> dataView = new DataView<POHeader>("pageable", new ListDataProvider(
                list)) {
            /**
					 * 
					 */
			private static final long serialVersionUID = 1L;

			public void populateItem(final Item item) {
                final POHeader _hj = (POHeader) item.getModelObject();
				item.add(new Label("no", String.valueOf(_hj.getNo())));
				item.add(new Label("nobukti", _hj.getNobukti()));
				item.add(new Label("tglbukti", _hj.getTglBukti()));
				item.add(new Label("kvendor", _hj.getUserinput()));
				item.add(new Label("approvedby", _hj.getApprovedby()));
				item.add(new Label("delivery", _hj.getDelivery()));
				item.add(new AjaxLink<Group1>("select")
				{
					/**
					 * 
					 */
					private static final long serialVersionUID = 1L;

					@Override
					public void onClick(AjaxRequestTarget target) {
						// TODO Auto-generated method stub
						POHeader header=(POHeader)getParent().getDefaultModelObject();
						PanelAction.actionType=2;
						sf.purchasing.POHeader _head=headers.get(header.getNobukti());
						header.setKeterangan(_head.getKeterangan());
						header.setKcompany(_head.getKcompany());
						header.setApprovedby(_head.getApprovedby());
						header.setApprovedtgl(_head.getApprovedtgl());
						//setResponsePage(new PurchaseRequestDetail(header));
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
						
						POHeader header=(POHeader)getParent().getDefaultModelObject();
						sf.purchasing.POHeader _head=headers.get(header.getNobukti());
						if(_head.getApprovedby()==null || _head.getApprovedby().equals(""))
						{
							header.setKeterangan(_head.getKeterangan());
							header.setKcompany(_head.getKcompany());
							header.setApprovedby(_head.getApprovedby());
							header.setApprovedtgl(_head.getApprovedtgl());
							//setResponsePage(new ApprovalPRequest(header));
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
						POHeader header=(POHeader)getParent().getDefaultModelObject();
						sf.purchasing.POHeader _head=headers.get(header.getNobukti());
						if(_head.getApprovedby()!=null && !_head.getApprovedby().equals(""))
						{
							header.setKeterangan(_head.getKeterangan());
							header.setKcompany(_head.getKcompany());
							header.setApprovedby(_head.getApprovedby());
							header.setApprovedtgl(_head.getApprovedtgl());
							//setResponsePage(new UnApprovalPRequest(header));
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
		
		genereatepo=new Link("generatePO")
		{
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				// TODO Auto-generated method stub
				//setResponsePage(PurchaseRequestDetail.class);
			}
		};
		
		add(genereatepo);
		app=this;
	}
	public PurchaseOrderPage()
	{
		this(new PageParameters());
	}
		
	public void getPOHeade(int idx)
    {
		Object[] params=null;
		if(idx==0)
			params=new Object[]{"","","",0};
		else
			params=new Object[]{"","","",0};
    	
    	Class[] retTypes =new Class[]{sf.purchasing.POHeader.class};
    	try {
    		String method="getPOHeader";
    		if(idx==1)// by no bukti
    		{
    			params=new Object[]{PurchaseRequestFilter.selectedItem.getNobukti()};
    			method="getPOHeaderByNoBukti";
    		}
    		else if(idx==2)//by tanggal bukti
    		{
    			params=new Object[]{PurchaseRequestFilter.selectedItem.getTglbukti()};
    			method="getPOHeaderByTglBukti";
    		}
    		else if(idx==3)//by bulan
    		{
    			String bulan = PurchaseRequestFilter.selectedItem.getTglbukti().substring(4,6);
    			params=new Object[]{bulan};
    			method="getPOHeaderByBulan";
    		}
    		else if(idx==4)//by requester
    		{
    			params=new Object[]{PurchaseRequestFilter.selectedItem.getUserinput()};
    			method="getPOHeaderBelumDelivery";
    		}
    		else if(idx==5)
    		{
    			method="getPOHeaderSudahDelivery";
    		}
    		else if(idx==6)
    		{
    			method="getPOHeaderBelumApproved";
    		}
    		else if(idx==7)
    			method="getPOHeaderSudajApproved";
    		
			Object[] response=_service.callServicePurchasing(method, params,retTypes);
			sf.purchasing.POHeader item = (sf.purchasing.POHeader)response[0];
			if(item==null) return;
			if(item.getHeaders()==null) return;
			int count=item.getHeaders().length;
			list = new ArrayList<POHeader>();
			for(int i=0; i < count;i++)
			{
				sf.purchasing.POHeader prheader=item.getHeaders()[i];
				if(prheader==null)
					continue;
				headers.put(prheader.getNobukti(), prheader);
				POHeader _prheader = new POHeader();
				_prheader.setNo(i+1);
				_prheader.setNobukti(prheader.getNobukti());
				_prheader.setTglBukti(prheader.getTglBukti());
				_prheader.setUserinput(prheader.getUserinput());
				_prheader.setApprovedby(prheader.getApprovedby());
				_prheader.setDelivery(prheader.getDelivery());

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
