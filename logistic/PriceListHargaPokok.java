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



public class PriceListHargaPokok extends BasePage implements IActionHandler{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Service _service;
	private ArrayList<HargaPokok> list=new ArrayList<HargaPokok>();
	private Form<HargaPokok> form;
	public static HargaPokok hargajual;
	private static String kbarang;
	public PriceListHargaPokok(PageParameters p)
	{
		super("Price List - Harga Pokok");
		if(hargajual==null)
		{
			hargajual=new HargaPokok();
		}
		kbarang=p.get("kbarang").toString();
		init();
		getHargaPokok(kbarang);
		buildPage();
	}
	public PriceListHargaPokok()
	{
		super("Price List - Harga Pokok");
		init();
		getHargaPokok(hargajual.getKbarang());
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
                final HargaPokok _hj = (HargaPokok) item.getModelObject();
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
		
		
		form = new Form<HargaPokok>("form", new CompoundPropertyModel<HargaPokok>(this.hargajual));
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
	
	private void getHargaPokok(String kbarang)
    {
    	Object[] params=new Object[]{kbarang};
    	
    	Class[] retTypes =new Class[]{sf.inventory.HargaPokok.class};
    	try {
			Object[] response=_service.callServiceInventory("getHargaPokok", params,retTypes);
			sf.inventory.HargaPokok item = (sf.inventory.HargaPokok )response[0];
			if(item==null) return;
			if(item.getHargaPokoks()==null) return;
			int count=item.getHargaPokoks().length;
			list = new ArrayList<HargaPokok>();
			for(int i=0; i < count;i++)
			{
				sf.inventory.HargaPokok hj=item.getHargaPokoks()[i];
				HargaPokok _hj = new HargaPokok();
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
