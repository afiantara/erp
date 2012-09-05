/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.wicket.erp;

import java.util.HashMap;

import org.apache.axis2.AxisFault;
import org.apache.wicket.Page;
import org.apache.wicket.erp.accounting.BranchPage;
import org.apache.wicket.erp.accounting.COAPage;
import org.apache.wicket.erp.accounting.ExpedisiPage;
import org.apache.wicket.erp.accounting.InvoicePage;
import org.apache.wicket.erp.accounting.KursPage;
import org.apache.wicket.erp.accounting.UserPage;
import org.apache.wicket.erp.crm.CustomerPage;
import org.apache.wicket.erp.engineeringprod.ItemCompositePage;
import org.apache.wicket.erp.file.AlertPage;
import org.apache.wicket.erp.file.CheckTransactionPage;
import org.apache.wicket.erp.file.EndYearPage;
import org.apache.wicket.erp.file.MenusPage;
import org.apache.wicket.erp.file.ParameterPage;
import org.apache.wicket.erp.file.PostingTrxPage;
import org.apache.wicket.erp.file.UserAccessPage;
import org.apache.wicket.erp.logistic.GroupProdukPage;
import org.apache.wicket.erp.logistic.GudangPage;
import org.apache.wicket.erp.logistic.PriceListPage;
import org.apache.wicket.erp.logistic.ProdukPage;
import org.apache.wicket.erp.purchasing.GeneratePOPage;
import org.apache.wicket.erp.purchasing.PurchaseOrderPage;
import org.apache.wicket.erp.purchasing.PurchaseRequestPage;
import org.apache.wicket.erp.purchasing.SupplierPage;
import org.apache.wicket.erp.utils.Service;
import org.apache.wicket.erp.utils.UserInfo;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.link.Link;

import sf.file.UserAccess;


/**
 * The left frame. This page is not bookmarkable, but it's instance is created by
 * {@link org.apache.wicket.examples.frames.BodyFrame} and hold in the same page map as index. It
 * uses the frameTarget object as a shared model; this page updates that model, and as the Index
 * uses that to set the frame tag, any changes to it should be reflected with the next render.
 * 
 * @author Eelco Hillenius
 */
public class LeftFrame extends WebPage
{
	/**
	 * Link that, when clicked, changes the frame target's frame class (and as that is a shared
	 * model which is also being used by the 'master page' {@link BodyFrame}, changes are
	 * immediately reflected) and set the response page to the top level page {@link BodyFrame}.
	 * Tags that use this link should have a <code>target="_parent"</code> attribute, so that the
	 * top frame will be refreshed.
	 */
	private static final class ChangeFramePageLink extends Link<Object>
	{
		private static final long serialVersionUID = 1L;

		/** parent frame class. */
		private final BodyFrame bodyFrame;

		/** this link's target. */
		private final Class<? extends Page> pageClass;

		/**
		 * Construct.
		 * 
		 * @param <C>
		 * 
		 * @param id
		 * @param bodyFrame
		 * @param pageClass
		 */
		public <C extends Page> ChangeFramePageLink(String id, BodyFrame bodyFrame,
			Class<C> pageClass)
		{
			super(id);
			this.bodyFrame = bodyFrame;
			this.pageClass = pageClass;
		}

		/**
		 * @see org.apache.wicket.markup.html.link.Link#onClick()
		 */
		@Override
		public void onClick()
		{
			// change frame class
			bodyFrame.getFrameTarget().setFrameClass(pageClass);

			// trigger re-rendering of the page
			setResponsePage(bodyFrame);
		}
	}

	private static final long serialVersionUID = 1L;

	/**
	 * Constructor
	 * 
	 * @param index
	 *            parent frame class
	 */
	public LeftFrame(BodyFrame index)
	{
		init();
		getUserAccess();
		//add(new ChangeFramePageLink("page1Link", index,Page1.class));
		//add(new ChangeFramePageLink("page2Link", index,Page2.class));
		//add(new ChangeFramePageLink("page3Link", index,Page2.class));
		//add(new ChangeFramePageLink("page4Link", index,Page2.class));
		//add(new ChangeFramePageLink("page5Link", index,Page2.class));
		//add(new ChangeFramePageLink("page6Link", index,Page2.class));
		//add(new ChangeFramePageLink("page7Link", index,Page2.class));
		//add(new ChangeFramePageLink("page8Link", index,Page2.class));
		//add(new ChangeFramePageLink("page9Link", index,Page2.class));
		add(new ChangeFramePageLink("pageParameterLink", index,ParameterPage.class).setVisible(isVisible("Parameter")));
		add(new ChangeFramePageLink("pageMenuAlertLink", index,AlertPage.class).setVisible(isVisible("Menu Alert")));
		add(new ChangeFramePageLink("pageCheckTransactionLink", index,CheckTransactionPage.class).setVisible(isVisible("Check Transaction")));
		add(new ChangeFramePageLink("pagePostingTrxLink", index,PostingTrxPage.class).setVisible(isVisible("Posting Transaction")));
		add(new ChangeFramePageLink("pageEndYearLink", index,EndYearPage.class).setVisible(isVisible("Year End Process")));
		add(new ChangeFramePageLink("pageGudangLink", index,GudangPage.class).setVisible(isVisible("Gudang")));
		add(new ChangeFramePageLink("pageBranchLink", index,BranchPage.class).setVisible(isVisible("Branch")));
		add(new ChangeFramePageLink("pageUserLink", index,UserPage.class).setVisible(isVisible("User ID")));
		add(new ChangeFramePageLink("pageCurrencyLink", index,KursPage.class).setVisible(isVisible("Currency")));
		add(new ChangeFramePageLink("pageInvoiceLink", index,InvoicePage.class).setVisible(isVisible("Invoice")));
		add(new ChangeFramePageLink("pageExpedisiLink", index,ExpedisiPage.class).setVisible(isVisible("Expedisi")));
		add(new ChangeFramePageLink("pageAccountLink", index,COAPage.class).setVisible(isVisible("Account No")));
		add(new ChangeFramePageLink("pageSupplierLink", index,SupplierPage.class).setVisible(isVisible("Master Supplier")));
		add(new ChangeFramePageLink("pageGroupProductLink", index,GroupProdukPage.class).setVisible(isVisible("Group Produk")));
		add(new ChangeFramePageLink("pageItemCompositeLink", index,ItemCompositePage.class).setVisible(isVisible("Item Composite")));
		add(new ChangeFramePageLink("pageCustomerLink", index,CustomerPage.class).setVisible(isVisible("Customer Master")));
		add(new ChangeFramePageLink("pageProductLink", index,ProdukPage.class).setVisible(isVisible("Product Master")));
		add(new ChangeFramePageLink("pagePriceListLink", index,PriceListPage.class).setVisible(UserInfo.ACCESS_HPP && isVisible("Price List")));
		add(new ChangeFramePageLink("pagePRequestLink", index,PurchaseRequestPage.class).setVisible(isVisible("Purchase Request")));
		add(new ChangeFramePageLink("pagePOLink", index,PurchaseOrderPage.class).setVisible(isVisible("Purchase Order")));
		add(new ChangeFramePageLink("pageMenuLink", index,MenusPage.class).setVisible(isVisible("Menu Registration")));
		add(new ChangeFramePageLink("pageUserAccessLink", index,UserAccessPage.class).setVisible(isVisible("User Access")));
		add(new ChangeFramePageLink("pageGeneratePOLink", index,GeneratePOPage.class).setVisible(isVisible("Generate Purchase Order")));
		
	}

	private boolean isVisible(String menuname)
	{
		boolean isAvail=false;
		if(maps.containsKey(menuname))
		{
			UserAccess ua = maps.get(menuname);
			isAvail = ua.getRecstatus().equals("Y")?true:false;
		}
		return (UserInfo.POSISI.equals("AD") || isAvail);
		
	}
	/**
	 * No need for versioning this frame.
	 * 
	 * @see org.apache.wicket.Component#isVersioned()
	 */
	@Override
	public boolean isVersioned()
	{
		return false;
	}
	
	private Service _service;
	private void init()
	{
		try {
			_service=new Service(Service.ACCOUNTING_SERVICE_URL);
		} catch (AxisFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private HashMap<String, UserAccess> maps;
	private void getUserAccess()
	{
		Object[] params=new Object[]{UserInfo.USERID};
		maps=new HashMap<String, UserAccess>();
    	Class[] retTypes =new Class[]{UserAccess.class};
    	try 
    	{
			Object[] response=_service.callServiceAccounting("getUserAccess", params,retTypes);
			UserAccess item = (UserAccess)response[0];
			if(item==null) return;
			if(item.getUsers()==null) return;
			
			int count=item.getUsers().length;
			for(int i=0; i < count;i++)
			{
				UserAccess title=item.getUsers()[i];
				maps.put(title.getMenuname(), title);
			}
		} catch (AxisFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}