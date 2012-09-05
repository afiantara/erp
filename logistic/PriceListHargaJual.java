package org.apache.wicket.erp.logistic;

import java.util.ArrayList;

import org.apache.axis2.AxisFault;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.erp.accounting.BasePage;
import org.apache.wicket.erp.utils.IActionHandler;
import org.apache.wicket.erp.utils.Service;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.ListDataProvider;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

public class PriceListHargaJual extends BasePage implements IActionHandler{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Service _service;
	private ArrayList<HargaJual> list=new ArrayList<HargaJual>();
	private Form<HargaJual> form;
	public static HargaJual hargajual;
	private static String kbarang;
	public PriceListHargaJual(PageParameters p)
	{
		super("Price List - Harga Jual");
		if(hargajual==null)
		{
			hargajual=new HargaJual();
		}
		kbarang=p.get("kbarang").toString();
		init();
		getHargaJual(kbarang);
		buildPage();
	}
	public PriceListHargaJual()
	{
		super("Price List - Harga Jual");
		init();
		getHargaJual(hargajual.getKbarang());
		buildPage();
	}

	@SuppressWarnings("unchecked")
	private void buildPage()
	{
		final DataView<Group2> dataView = new DataView<Group2>("pageable", new ListDataProvider(
                list)) {
            /**
					 * 
					 */
			private static final long serialVersionUID = 1L;

			public void populateItem(final Item item) {
                final HargaJual _hj = (HargaJual) item.getModelObject();
				item.add(new Label("no", String.valueOf(_hj.getNo())));
				item.add(new Label("ktanggal", _hj.getKtanggal()));
				item.add(new Label("kvaluta", _hj.getKvaluta()));
				item.add(new Label("nvaluta", String.valueOf(_hj.getNvaluta())));
				item.add(new Label("potongan", String.valueOf(_hj.getPotongan())));
				

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
		
		
		form = new Form<HargaJual>("form", new CompoundPropertyModel<HargaJual>(this.hargajual));
		add(form);
		// add a simple text field that uses Input's 'text' property. Nothing
		// can go wrong here
		 readOnly();
	}
	
	private void init()
	{
		try {
			_service=new Service(Service.INVENTORY_SERVICE_URL);
		} catch (AxisFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void getHargaJual(String kbarang)
    {
    	Object[] params=new Object[]{kbarang};
    	
    	Class[] retTypes =new Class[]{sf.inventory.HargaJual.class};
    	try {
			Object[] response=_service.callServiceInventory("getHargaJual", params,retTypes);
			sf.inventory.HargaJual item = (sf.inventory.HargaJual)response[0];
			if(item==null) return;
			if(item.getHargaJuals()==null) return;
			int count=item.getHargaJuals().length;
			list = new ArrayList<HargaJual>();
			for(int i=0; i < count;i++)
			{
				sf.inventory.HargaJual hj=item.getHargaJuals()[i];
				HargaJual _hj = new HargaJual();
				_hj.setKbarang(hj.getKbarang());
				_hj.setKtanggal(hj.getKtanggal());
				_hj.setNo(i+1);
				_hj.setKvaluta(hj.getKvaluta());
				_hj.setNvaluta(hj.getNvaluta());
				_hj.setPotongan(hj.getPotongan());
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

	public void readOnly() {
		// TODO Auto-generated method stub
		
	}
	public void activate() {
		// TODO Auto-generated method stub
	}

	public void cancel(boolean flag) {
		// TODO Auto-generated method stub
		
	}

	public int delete() {
		// TODO Auto-generated method stub
		return 0;
	}

	public void deleteAction() {
		// TODO Auto-generated method stub
		
	}

	public void editAction() {
		// TODO Auto-generated method stub
		
	}

	public int insert() {
		// TODO Auto-generated method stub
		return 0;
	}

	public void newAction() {
		// TODO Auto-generated method stub
		
	}

	public void print() {
		// TODO Auto-generated method stub
		
	}

	

	public int update() {
		// TODO Auto-generated method stub
		return 0;
	}
}
