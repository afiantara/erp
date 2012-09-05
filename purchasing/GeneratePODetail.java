package org.apache.wicket.erp.purchasing;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.apache.axis2.AxisFault;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.erp.utils.IActionHandler;
import org.apache.wicket.erp.utils.PanelAction;
import org.apache.wicket.erp.utils.Service;
import org.apache.wicket.erp.utils.UserInfo;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RadioChoice;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.ListDataProvider;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import sf.accounting.Valuta;
import sf.inventory.Produk;
import sf.purchasing.Supplier;

public class GeneratePODetail extends WebPage implements IActionHandler {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static final List<String> STATUS = Arrays
	.asList(new String[] { "Y", "T"});
	
	private static List<String> VENDORS = Arrays
	.asList(new String[] { ""});
	private static List<String> VALUTAS = Arrays
	.asList(new String[] { ""});
	
	private RequiredTextField<String> nobukti;
	private RequiredTextField<String> tglBukti;
	private TextArea<String> keterangan;
	private TextField<String> prno;
	private TextField<String> tglpr;
	private TextField<String> jtempo;
	private TextField<String> blain_ket;
	private TextField<String> blain_nilai;
	private TextField<String> nilaippn;
	private TextField<String> total_nilai_po;
	private DropDownChoice<String> kvendor;
	private DropDownChoice<String> kvaluta;
	private DropDownChoice<String> kbarang;
	private RadioChoice<String> adappn;
	private TextField<String> qtypr;
	private TextField<String> jumlah1;
	private TextField<String> pdisc;
	private RequiredTextField<String> jumlah2;
	private DropDownChoice<String> satuan;
	private TextField<String> hargasatuan;
	private TextField<String> totalharga;
	private TextField<String> nobukti1;
	private TextArea<String> ket;
	private String oriNoPO;
	private AjaxLink<Void> save;
	private AjaxLink<Void> cancel;
	private AjaxLink<Void> prosespo;
	private ArrayList<PRDetail> list=new ArrayList<PRDetail>();
	private HashMap<String, Produk> produks=new HashMap<String, Produk>();
	
	private final Map<String, List<String>> modelsMap = new HashMap<String, List<String>>();
	
	boolean isReadOnly=true;
	private Form<POHeader> form;
	
	final AttributeModifier ro = new AttributeModifier("readonly", isReadOnly);
	public static POHeader pheader = new POHeader();
	
	private static GeneratePODetail app;
	private Service _service;
	private Service _service1;
	private Service _service2;
	public String selectedKBarang;
	public String selectedSatuan;
	private static PRDetail selectedPRDetail;
	private boolean isDisable=false;
	private static TempPRDetail oriPRDetail;
	private void init()
	{
		try {
			_service=new Service(Service.PURCHASING_SERVICE_URL);
			_service1=new Service(Service.INVENTORY_SERVICE_URL);
			_service2=new Service(Service.ACCOUNTING_SERVICE_URL);
		} catch (AxisFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static GeneratePODetail get()
	{
		return app;
	}
	
	public GeneratePODetail(PageParameters p,PRHeader ph)
	{
		this(ph);
		//readOnly(false);
		if(p.getNamedKeys().contains("error"))
			error(p.get("error"));
		if(p.getNamedKeys().contains("info"))
			info(p.get("info"));
		if(p.getNamedKeys().contains("select"))
		{
			removeReadOnlyDetail();
			save.setEnabled(true);
			cancel.setEnabled(false);
		}
		if(p.getNamedKeys().contains("save"))
		{
			save.setEnabled(false);
			cancel.setEnabled(true);
		}
		if(p.getNamedKeys().contains("new"))
		{
			save.setEnabled(false);
			cancel.setEnabled(false);
			prosespo.setEnabled(false);
			selectedPRDetail=null;
		}
		
	}
	public GeneratePODetail()
	{
		this(new PRHeader());
		selectedPRDetail=null;
	}
	

	public GeneratePODetail(PRHeader ph)
	{
		init();
		getProduk();
		getKvaluta();
		getKvendor();
		GeneratePODetail.pheader.setPrHeader(ph);
		if(pheader.getNobukti()==null || pheader.getNobukti().equals(""))
		{
			GeneratePODetail.pheader.setNobukti(getNoPO());
			selectedKBarang=null;
			selectedSatuan=null;
		}
		Produk p = produks.get(ph.getKbarang());
		if(p!=null)
		{
			selectedKBarang = p.getKbarang() + " " + p.getNbarang();
		}
		selectedSatuan = pheader.getPrHeader().getSatuan();
		getPRequestDetail(ph.getNobukti());
		form = new Form<POHeader>("form", new CompoundPropertyModel<POHeader>(pheader));
		add(form);
		// add a simple text field that uses Input's 'text' property. Nothing
		// can go wrong here
		nobukti = new RequiredTextField<String>("nobukti");
		nobukti.setOutputMarkupId(true);
		tglBukti= new RequiredTextField<String>("tglBukti");
		tglBukti.setOutputMarkupId(true);
		tglBukti.add(new AjaxFormComponentUpdatingBehavior("onchange"){

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				// TODO Auto-generated method stub
				if(tglBukti.getValue().compareTo(tglpr.getValue())<0)
				{
					target.appendJavaScript("alert('Tanggal PO harus lebih besar atau sama dengan tanggal PR');");
					tglBukti.setDefaultModelObject(null);
					target.add(tglBukti);
				}
			}
			
		});
		
		keterangan=new TextArea<String>("keterangan");
		prno= new TextField<String>("prno");
		tglpr= new TextField<String>("tglpr");
		jtempo= new TextField<String>("jtempo");
		jtempo.setOutputMarkupId(true);
		jtempo.add(new AjaxFormComponentUpdatingBehavior("onchange"){

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				// TODO Auto-generated method stub
				target.add(jtempo);
			}
		});
		blain_ket= new TextField<String>("blain_ket");
		blain_ket.add(new AjaxFormComponentUpdatingBehavior("onchange")
		{
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			protected void onUpdate(AjaxRequestTarget target)
			{
				if(pheader.getBlain_ket()!=null && !pheader.getBlain_ket().equals(""))
				{
					blain_nilai.setEnabled(true);
					blain_nilai.remove(ro);
				}
				else
				{
					pheader.setBlain_nilai(0);
					blain_nilai.setEnabled(false);
					blain_nilai.add(ro);
				}
				target.add(blain_nilai);
			}
		});
		
		blain_nilai= new TextField<String>("blain_nilai");
		blain_nilai.add(new AjaxFormComponentUpdatingBehavior("onchange")
		{
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			protected void onUpdate(AjaxRequestTarget target)
			{
				generateTotalNilaiPO();
				target.add(total_nilai_po);
			}
		});
		
		blain_nilai.setOutputMarkupId(true);
		
		adappn=new RadioChoice<String>("adappn",STATUS);
		adappn.setSuffix(" ");
		adappn.setOutputMarkupId(true);
		adappn.add(new AjaxFormComponentUpdatingBehavior("onclick")
		{
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			protected void onUpdate(AjaxRequestTarget target)
			{
				if(pheader.getNilaippn()==0)
					pheader.setAdappn("Y");
				else
					pheader.setAdappn("T");
				generateTotalNilaiPO();
				target.add(nilaippn);
				target.add(total_nilai_po);
			}
		});

		nilaippn= new TextField<String>("nilaippn");
		nilaippn.setOutputMarkupId(true);
		nilaippn.add(new AjaxFormComponentUpdatingBehavior("onchange")
		{
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			protected void onUpdate(AjaxRequestTarget target)
			{
				generateTotalNilaiPO();
				target.add(total_nilai_po);
			}
		});

		total_nilai_po= new TextField<String>("total_nilai_po");
		total_nilai_po.setOutputMarkupId(true);
		
		kvendor=new DropDownChoice<String>("kvendor", VENDORS);
		
		kvendor.add(new AjaxFormComponentUpdatingBehavior("onchange")
		{
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			protected void onUpdate(AjaxRequestTarget target)
			{
				setNOPO(pheader.getNobukti(),pheader.getKvendor());
				target.add(nobukti);
			}
		});
		kvaluta=new DropDownChoice<String>("kvaluta", VALUTAS);
		kvaluta.setOutputMarkupId(true);
		IModel<List<? extends String>> modelChoices = new AbstractReadOnlyModel<List<? extends String>>()
		{
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public List<String> getObject()
			{
				return new ArrayList<String>(modelsMap.keySet());
			}

		};
		
		IModel<List<? extends String>> satuanChoices = new AbstractReadOnlyModel<List<? extends String>>()
		{
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public List<String> getObject()
			{
				List<String> models = modelsMap.get(selectedKBarang);
				if (models == null)
				{
					models = Collections.emptyList();
				}
				return models;
			}

		};
		kbarang=new DropDownChoice<String>("kbarang",
				 new PropertyModel<String>(this, "selectedKBarang"), modelChoices);
		kbarang.setRequired(true);
		
		kbarang.add(new AjaxFormComponentUpdatingBehavior("onchange")
			{
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;
	
				@Override
				protected void onUpdate(AjaxRequestTarget target)
				{
					target.add(satuan);
				}
			});
		 
		satuan=new DropDownChoice<String>("satuan",
				 new PropertyModel<String>(this, "selectedSatuan"), satuanChoices);
		
		satuan.setOutputMarkupId(true);
				
		qtypr=new TextField<String>("qtypr");
		jumlah1=new TextField<String>("jumlah1");
		pdisc=new TextField<String>("pdisc");
		pdisc.add(new AjaxFormComponentUpdatingBehavior("onchange")
		{
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			protected void onUpdate(AjaxRequestTarget target)
			{
				generateTotalHarga();
				target.add(totalharga);
			}
		});

		
		jumlah2 = new RequiredTextField<String>("jumlah2");
		jumlah2.setOutputMarkupId(true);
		jumlah2.add(new AjaxFormComponentUpdatingBehavior("onchange")
		{
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			protected void onUpdate(AjaxRequestTarget target)
			{
				double jml2 = Double.parseDouble(jumlah2.getValue());
				double jml = Double.parseDouble(qtypr.getValue());
				double jml1 = Double.parseDouble(jumlah1.getValue());
				
				if(jml2>jml-jml1)
				{
					target.appendJavaScript("alert('jumlah2 harus lebih kecil atau sama dengan jumlah-jumlah1');");
					pheader.setJumlah2(0);
					target.add(jumlah2);
				}
				else
				{
					generateTotalHarga();
					target.add(totalharga);
				}
			}
		});
		
		
		nobukti1= new TextField<String>("nobukti1");
		ket= new TextArea<String>("ket");
		hargasatuan= new TextField<String>("hargasatuan");
		totalharga= new TextField<String>("totalharga");
		totalharga.setOutputMarkupId(true);
		totalharga.add(new AjaxFormComponentUpdatingBehavior("onchange")
		{
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			protected void onUpdate(AjaxRequestTarget target)
			{
				pheader.setTotalharga(Double.parseDouble(totalharga.getValue()));
				selectedPRDetail.setTotal(pheader.getTotalharga());
				generateTotalNilaiPO();
				target.add(total_nilai_po);
				target.add(totalharga);
			}
		});
		this.readOnly();
		
		form.add(nobukti);
		form.add(tglBukti);
		form.add(keterangan);
		form.add(prno);
		form.add(tglpr);
		form.add(jtempo);
		form.add(blain_ket);
		form.add(blain_nilai);
		form.add(nilaippn);
		form.add(kvendor);
		form.add(kvaluta);
		form.add(total_nilai_po);
		form.add(kbarang);
		form.add(satuan);
		form.add(jumlah2);
		form.add(nobukti1);
		form.add(ket);
		form.add(adappn);
		form.add(qtypr);
		form.add(jumlah1);
		form.add(pdisc);
		form.add(hargasatuan);
		form.add(totalharga);
		save=new AjaxLink<Void>("save"){

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target) {
				// TODO Auto-generated method stub
				//save into pr detail.
				selectedPRDetail.setKvaluta(pheader.getKvaluta());
				selectedPRDetail.setKet(pheader.getPrHeader().getKet());
				selectedPRDetail.setJumlah(pheader.getPrHeader().getJumlah());
				selectedPRDetail.setJumlah1(pheader.getJumlah1());
				selectedPRDetail.setJumlah2(pheader.getJumlah2());
				selectedPRDetail.setNvaluta(pheader.getHargasatuan());
				selectedPRDetail.setNobukti(pheader.getPrno());
				selectedPRDetail.setPdisc(pheader.getPdisc());
				selectedPRDetail.setNopo(pheader.getNobukti());
				selectedPRDetail.setTglbukti(pheader.getPrHeader().getTglbukti());
				selectedPRDetail.setTotal(pheader.getTotalharga()); 
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
				String dateNow = sdf.format(new Date());
				selectedPRDetail.setTglupdate(Long.parseLong(dateNow));
				selectedPRDetail.setUserupdate(UserInfo.USERID);
				sf.purchasing.PRDetail prDetail = (sf.purchasing.PRDetail) selectedPRDetail;
				sf.purchasing.PRHeader prHeader= (sf.purchasing.PRHeader) pheader.getPrHeader();
				Object[] params=new Object[]{prHeader ,prDetail};
		    	Class[] retTypes =new Class[]{Boolean.class};
		    	
		    	Object[] response;
				try {
					response = _service.callServicePurchasing("updateSinglePR", params, retTypes);
					Boolean ret=(Boolean)response[0];
					if(ret)
					{
						PageParameters p = new PageParameters();
						p.set("save", "save");
						setResponsePage(new GeneratePODetail(p,pheader.getPrHeader()));
						prosespo.setEnabled(true);
						target.add(prosespo);
					}
				} catch (AxisFault e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};

		
		cancel=new AjaxLink<Void>("cancel"){

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target) {
				// TODO Auto-generated method stub
				selectedPRDetail.setJumlah2(oriPRDetail.getPoQty());
				selectedPRDetail.setPdisc(oriPRDetail.getpDisc());
				selectedPRDetail.setKet(oriPRDetail.getKet());
				selectedPRDetail.setNobukti1(oriPRDetail.getNoBukti1());
				pheader.setJumlah2(oriPRDetail.getPoQty());
				pheader.setPdisc(oriPRDetail.getpDisc());
				pheader.setKet(oriPRDetail.getKet());
				pheader.setNobukti1(oriPRDetail.getNoBukti1());
				
				PageParameters p = new PageParameters();
				sf.purchasing.PRDetail prDetail = (sf.purchasing.PRDetail) selectedPRDetail;
				sf.purchasing.PRHeader prHeader= (sf.purchasing.PRHeader) pheader.getPrHeader();
				Object[] params=new Object[]{prHeader ,prDetail};
		    	Class[] retTypes =new Class[]{Boolean.class};
		    	
		    	Object[] response;
				try {
					response = _service.callServicePurchasing("updateSinglePR", params, retTypes);
					Boolean ret=(Boolean)response[0];
					if(ret)
						setResponsePage(new GeneratePODetail(pheader.getPrHeader()));
				} catch (AxisFault e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		save.setEnabled(false);
		cancel.setEnabled(false);
		
		form.add(save);
		form.add(cancel);
		
		final DataView<PRDetail> dataView = new DataView<PRDetail>("pageable", new ListDataProvider(
                list)) {
            /**
					 * 
					 */
			private static final long serialVersionUID = 1L;

			public void populateItem(final Item<PRDetail> item) {
                final PRDetail _hj = (PRDetail) item.getModelObject();
				item.add(new Label("no", String.valueOf(_hj.getNo())));
				item.add(new Label("kbarang", _hj.getKbarang()));
				item.add(new Label("nbarang", _hj.getNbarang()));
				item.add(new Label("satuan", _hj.getSatuan()));
				item.add(new Label("qtypr", String.valueOf(_hj.getJumlah())));
				item.add(new Label("ttlpo", String.valueOf(_hj.getJumlah1())));
				item.add(new Label("qtypo", String.valueOf(_hj.getJumlah2())));
				item.add(new Label("hargasatuan", String.valueOf(_hj.getNvaluta())));
				item.add(new Label("pdisc", String.valueOf(_hj.getPdisc())));
				item.add(new Label("total", String.valueOf(_hj.getTotal())));
				item.add(new Label("nobukti1", _hj.getNobukti1()));
				item.add(new Label("ket", _hj.getKet()));
				item.add(new AjaxLink<PRDetail>("select")
						{
							/**
							 * 
							 */
							private static final long serialVersionUID = 1L;

							@Override
							public void onClick(AjaxRequestTarget target) {
								// TODO Auto-generated method stub
								PRDetail detail=(PRDetail)getParent().getDefaultModelObject();
								selectedPRDetail=detail;
								
								oriPRDetail=new TempPRDetail();
								oriPRDetail.setPoQty(detail.getJumlah2());
								oriPRDetail.setNoBukti1(detail.getNobukti1());
								oriPRDetail.setpDisc(detail.getPdisc());
								oriPRDetail.setKet(detail.getKet());
								
								selectedKBarang = detail.getKbarang() + " " + detail.getNbarang();
								System.out.println("selectedKBarang: " + selectedKBarang);
								selectedSatuan=detail.getSatuan();
								System.out.println("selectedSatuan: " + selectedSatuan);
								pheader.getPrHeader().setKbarang(selectedKBarang);
								pheader.getPrHeader().setSatuan(selectedSatuan);
								pheader.getPrHeader().setNobukti1(detail.getNobukti1());
								pheader.setKvaluta(detail.getKvaluta());
								pheader.setKet(detail.getKet());
								pheader.setPdisc(detail.getPdisc());
								pheader.setJumlah1(detail.getJumlah1());
								pheader.setJumlah2(detail.getJumlah2());
								pheader.getPrHeader().setKet(detail.getKet());
								pheader.getPrHeader().setJumlah(detail.getJumlah());
								pheader.setHargasatuan(detail.getNvaluta());
								pheader.setTotalharga(detail.getTotal());
								PageParameters p = new PageParameters();
								p.set("select", "select");
								setResponsePage(new GeneratePODetail(p,pheader.getPrHeader()));
							}
						}).setEnabled(!isDisable);
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
		add(new BookmarkablePageLink<Void>("homeLink", PurchaseRequestPage.class));
		
		prosespo=new AjaxLink<Void>("prosespo"){

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target) {
				// TODO Auto-generated method stub
				//insert into po header and detail.
				insert();
			}
		};
		if(selectedPRDetail==null)
			prosespo.setEnabled(false);
		prosespo.setOutputMarkupId(true);
		add(prosespo);
		app=this;
	}
	
	
	private void generateTotalHarga()
	{
		double jml2 = pheader.getJumlah2();
		double hargaSatuan =pheader.getHargasatuan();
		double disc = pheader.getPdisc();
		double dbltotalharga = jml2* hargaSatuan ;
		dbltotalharga -=dbltotalharga *disc/100;
		pheader.setTotalharga(dbltotalharga);
	}
	
	private void generateTotalNilaiPO()
	{
		double totalHarga=0;
		double totalNilai=0;
		for(PRDetail item :list)
		{
			totalHarga = item.getTotal();
			totalNilai+=totalHarga;
		}
		
		totalNilai+=pheader.getBlain_nilai();
		
		if(pheader.getAdappn().equals("Y"))
		{
			pheader.setNilaippn(0.1*totalNilai);
		}
		else
		{
			pheader.setNilaippn(0);
		}
		
		totalNilai +=pheader.getNilaippn();
		pheader.setTotal_nilai_po(totalNilai);
	}
	public boolean updatePRequest()
	{
		form.process(null);
		POHeader item = (POHeader)form.getModelObject();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String dateNow = sdf.format(new Date());
		item.setTglupdate(Long.parseLong(dateNow));
		item.setUserupdate(UserInfo.USERID);
		item.setTglupdate(Long.parseLong(dateNow));
		item.setUserupdate(UserInfo.USERID);
		
		sf.purchasing.PODetail item1 = new sf.purchasing.PODetail();
		Produk p = produks.get(selectedKBarang);
		item1.setNobukti(item.getNobukti());
		item1.setTglbukti(item.getTglBukti());
		item1.setKbarang(p.getKbarang());
		item1.setSatuan(selectedSatuan);
		item1.setNobukti1(item.getPrHeader().getNobukti1());
		item1.setJumlah(item.getPrHeader().getJumlah());
		item1.setKet(item.getPrHeader().getKet());
		item1.setKcompany(item.getKcompany());
		Produk produk = produks.get(selectedKBarang);
		item1.setKgroup(produk.getKgroup());
		item1.setTglupdate(item.getTglupdate());
		item1.setUserupdate(item.getUserupdate());
    	Object[] params=new Object[]{item,item1};
    	Class[] retTypes =new Class[]{Boolean.class};
    	
    	Object[] response;
		try {
			response = _service.callServicePurchasing("updateSinglePR", params, retTypes);
			Boolean ret=(Boolean)response[0];
			if(ret)
			{
				PanelAction.actionType=2;//set panel action with 2//edit and delete button is enabled.
				setResponsePage(new GeneratePODetail(pheader.getPrHeader()));
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
		POHeader hp = (POHeader)form.getModelObject();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String dateNow = sdf.format(new Date());
		hp.setTglupdate(Long.parseLong(dateNow));
		hp.setUserupdate(UserInfo.USERID);
		
		sf.purchasing.PRDetail detail = new sf.purchasing.PRDetail();
		detail.setTglupdate(Long.parseLong(dateNow));
		detail.setUserupdate(UserInfo.USERID);
		detail.setNobukti(hp.getNobukti());
		Produk prod = produks.get(selectedKBarang);
		if(prod!=null)
		{
			detail.setKbarang(prod.getKbarang());
		}
		Object[] params=new Object[]{detail};
		Class[] retTypes =new Class[]{Boolean.class};
	
    	Object[] response;
		try {
			response = _service.callServicePurchasing("deletePrequisitionDetail", params, retTypes);
			Boolean ret=(Boolean)response[0];
			if(ret)
			{
				PageParameters pg=new PageParameters();
				pg.add("info", "Delete Purchase Request has been successfully.");
				pg.remove("error");
				PanelAction.actionType=0;
				
				selectedKBarang=null;
				selectedSatuan=null;
				pheader.getPrHeader().setKbarang(null);
				pheader.getPrHeader().setSatuan(null);
				pheader.getPrHeader().setKet("");
				pheader.getPrHeader().setJumlah(0);
				pheader.getPrHeader().setNobukti1("");
				
				setResponsePage(new GeneratePODetail(pg, pheader.getPrHeader()));
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
		boolean duplicate=false;
		POHeader item = (POHeader)form.getModelObject();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String dateNow = sdf.format(new Date());
		item.setTglupdate(Long.parseLong(dateNow));
		item.setUserupdate(UserInfo.USERID);
		item.setTglinput(item.getTglupdate());
		item.setUserinput(item.getUserupdate());
		item.setKcompany(UserInfo.COMPANY);
		
		PODetail[] item1=new PODetail[list.size()];
		for(int i=0; i < item1.length;i++)
		{
			item1[i]=new PODetail();
			PRDetail detail = list.get(i);
			item1[i].setKbarang(detail.getKbarang());
			item1[i].setNobukti(pheader.getNobukti());
			item1[i].setNobukti1(pheader.getNobukti1());
			item1[i].setJumlah(detail.getJumlah2());
			item1[i].setJumlah1(0);
			item1[i].setJumlah2(detail.getJumlah2());
			item1[i].setKcompany(detail.getKcompany());
			item1[i].setKet(detail.getKet());
			item1[i].setKgroup(detail.getKgroup());
			item1[i].setKvaluta(detail.getKvaluta());
			item1[i].setNbarang(detail.getNbarang());
			item1[i].setNopr(pheader.getPrno());
			item1[i].setNvaluta(detail.getNvaluta());
			item1[i].setPdisc(detail.getPdisc());
			item1[i].setSatuan(detail.getSatuan());
			item1[i].setTglbukti(pheader.getTglBukti());
			item1[i].setTotalHarga(pheader.getTotal_nilai_po());
			item1[i].setTglupdate(Long.parseLong(dateNow));
			item1[i].setUserupdate(UserInfo.USERID);
			item1[i].setTglinput(item.getTglupdate());
			item1[i].setUserinput(item.getUserupdate());
		}
		
    	Object[] params=new Object[]{item,item1};
    	Class[] retTypes =new Class[]{Boolean.class};
    	
    	Object[] response;
    	
		try {
			response = _service.callServicePurchasing("insertPO", params, retTypes);
			Boolean ret=(Boolean)response[0];
			if(ret)
			{
				PageParameters pg=new PageParameters();
				pg.add("info", "Insert Purchase Order has been successfully.");
				pg.remove("error");
				setResponsePage(new GeneratePOPage(pg));
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
		return duplicate ? 2: 0;
	}

	public int update() {
		// TODO Auto-generated method stub
		Boolean bRet=updatePRequest();
		if(bRet)
		{
			info("Update Purchasing Request has been successfully.");
			return 1;
		}
		else
			error("Failed to update Purchasing Request.");
		return 0;
	}

	public void deleteAction() {
		// TODO Auto-generated method stub
		setResponsePage(this);
	}

	public void editAction() {
		// TODO Auto-generated method stub
		readOnly(false);
	}

	public void newAction() {
		// TODO Auto-generated method stub
		//PR-yyyy-999999-xxx
		try
		{
			selectedKBarang=null;
			selectedSatuan=null;
			pheader.getPrHeader().setKbarang(null);
			pheader.getPrHeader().setSatuan(null);
			pheader.getPrHeader().setKet("");
			pheader.getPrHeader().setJumlah(0);
			pheader.getPrHeader().setNobukti1("");
			setResponsePage(new GeneratePODetail(new PageParameters(),pheader.getPrHeader()));
		}
		catch(Exception ex){}
	}

	private void readOnly(boolean flag)
	{
		if(!flag)
		{
			 nobukti.remove(ro);
			 tglBukti.remove(ro);
			 keterangan.remove(ro);
			 prno.remove(ro);
			 tglpr.remove(ro);
			 jtempo.remove(ro);
			 blain_ket.remove(ro);
			 blain_nilai.remove(ro);
			 nilaippn.remove(ro);
			 total_nilai_po.remove(ro);
			 kvendor.remove(ro);
			 kvaluta.remove(ro);
			 kbarang.remove(ro);
			 adappn.remove(ro);
			 qtypr.remove(ro);
			 jumlah1.remove(ro);
			 pdisc.remove(ro);
			 jumlah2.remove(ro);
			 satuan.remove(ro);
			 hargasatuan.remove(ro);
			 totalharga.remove(ro);
			 nobukti1.remove(ro);
			 ket.remove(ro);
		}
		else
		{
			 nobukti.add(ro);
			 //tglBukti.add(ro);
			 keterangan.add(ro);
			 prno.add(ro);
			 tglpr.add(ro);
			 //jtempo.add(ro);
			 //blain_ket.add(ro);
			 blain_nilai.add(ro);
			 nilaippn.add(ro);
			 total_nilai_po.add(ro);
			 //kvendor.add(ro);
			 //kvaluta.add(ro);
			 kbarang.add(ro);
			 adappn.add(ro);
			 qtypr.add(ro);
			 jumlah1.add(ro);
			 pdisc.add(ro);
			 jumlah2.add(ro);
			 satuan.add(ro);
			 hargasatuan.add(ro);
			 totalharga.add(ro);
			 nobukti1.add(ro);
			 ket.add(ro);
		}
	}
	
	public void removeReadOnlyDetail()
	{
		adappn.remove(ro);
		pdisc.remove(ro);
		jumlah2.remove(ro);
		totalharga.remove(ro);
		satuan.remove(ro);
		nobukti1.remove(ro);
		ket.remove(ro);
	}
	
	public void addReadOnlyDetail()
	{
		adappn.add(ro);
		pdisc.add(ro);
		jumlah2.add(ro);
		satuan.add(ro);
		nobukti1.add(ro);
		ket.add(ro);
	}
	
	public void readOnly() {
		// TODO Auto-generated method stub
		try
		{
			readOnly(true);
		}
		catch(Exception ex){}
	}

	public void cancel(boolean flag) {
		// TODO Auto-generated method stub
		
		selectedKBarang=null;
		selectedSatuan=null;
		pheader.getPrHeader().setKbarang(null);
		pheader.getPrHeader().setSatuan(null);
		pheader.getPrHeader().setKet("");
		pheader.getPrHeader().setJumlah(0);
		pheader.getPrHeader().setNobukti1("");
		
		setResponsePage(this);
		/*
		if(flag)
			setResponsePage(new PurchaseRequestDetail(PriceListFilter.selectedHargaJual,PriceListFilter.selectedHargaPokok));
		else
			setResponsePage(PurchaseRequestDetail.class);
		*/
		this.readOnly();
	}

	public void activate() {
		// TODO Auto-generated method stub
		form.clearInput();
	}

	public void print() {
		// TODO Auto-generated method stub
		
	}
	
	
	public void activate(int idx) {
		// TODO Auto-generated method stub
		
	}

	public void cancel(int idx, boolean flag) {
		// TODO Auto-generated method stub
		
	}

	public int delete(int idx) {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
		
		return 2;
	}

	public void deleteAction(int idx) {
		
	}

	public void editAction(int idx) {
		// TODO Auto-generated method stub
		
	}

	public int insert(int idx) {
		// TODO Auto-generated method stub
		return 0;
	}

	public void newAction(int idx) {
		// TODO Auto-generated method stub
		if(idx==0)
		{
			
		}
		else
		{
			
			
			
		}
		this.readOnly(false);
	}

	public void print(int idx) {
		// TODO Auto-generated method stub
		
	}

	public void readOnly(int idx) {
		// TODO Auto-generated method stub
		
	}

	public int update(int idx) {
		// TODO Auto-generated method stub
		if(idx==0)
		{
			return 0;
		}
		else
		{
			return 0 ;
		}
	}
	
	private String[] kodes;
	public void getProduk()
    {
    	Object[] params=new Object[]{UserInfo.COMPANY};
    	
    	Class[] retTypes =new Class[]{Produk.class};
    	try {
			Object[] response=_service1.callServiceInventory("getProduk", params,retTypes);
			Produk item = (Produk)response[0];
			if(item==null) return;
			if(item.getProduks()==null) return;
			int count=item.getProduks().length;
			kodes=new String[count];
			String[] satuan = new String[]{""};
			Vector<String> vSatuan = new Vector<String>();
			if(produks!=null)
			{
				produks=null;
			}
			produks=new HashMap<String, Produk>();
			if(modelsMap!=null)
				modelsMap.clear();
			for(int i=0; i < count;i++)
			{
				Produk acc=item.getProduks()[i];
				if(null!=acc) 
				{
					kodes[i]=acc.getKbarang()+ " " + acc.getNbarang();
					produks.put(kodes[i], acc);
					vSatuan.add(acc.getSatuan());
					if(acc.getSatuan1()!=null)
					{
						vSatuan.add(acc.getSatuan1());
					}
					if(acc.getSatuan2()!=null)
					{
						vSatuan.add(acc.getSatuan2());
					}
					satuan=new String[vSatuan.size()];
					for(int idx=0;idx < vSatuan.size();idx++)
					{
						satuan[idx]=(String)vSatuan.get(idx);
					}
					modelsMap.put(kodes[i],  Arrays.asList(satuan));
					vSatuan.clear();
				}
			}
		} catch (AxisFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
	
	public void getKvaluta()
    {
    	Object[] params=new Object[]{null};
    	
    	Class[] retTypes =new Class[]{Valuta.class};
    	try {
			Object[] response=_service2.callServiceAccounting("getValutas", params,retTypes);
			Valuta item = (Valuta)response[0];
			if(item==null) return;
			if(item.getValutas()==null) return;
			int count=item.getValutas().length;
			kodes=new String[count];
			String[] satuan = new String[]{""};
			Vector<String> vSatuan = new Vector<String>();
			for(int i=0; i < count;i++)
			{
				Valuta acc=item.getValutas()[i];
				if(null!=acc) 
				{
					kodes[i]=acc.getKvaluta();
				}
			}
			VALUTAS=Arrays
			.asList(kodes);
			
		} catch (AxisFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
	
	public void getKvendor()
    {
    	Object[] params=new Object[]{null};
    	
    	Class[] retTypes =new Class[]{Supplier.class};
    	try {
			Object[] response=_service.callServicePurchasing("getSupplier", params,retTypes);
			Supplier item = (Supplier)response[0];
			if(item==null) return;
			if(item.getSuppliers()==null) return;
			int count=item.getSuppliers().length;
			kodes=new String[count];
			String[] satuan = new String[]{""};
			Vector<String> vSatuan = new Vector<String>();
			for(int i=0; i < count;i++)
			{
				Supplier acc=item.getSuppliers()[i];
				if(null!=acc) 
				{
					kodes[i]=acc.getKvendor();
				}
			}
			VENDORS=Arrays
			.asList(kodes);
			
		} catch (AxisFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
	
	public void setNOPO(String defaultNoPO,String kvendor)
	{
		if(oriNoPO==null)
			oriNoPO= pheader.getNobukti();
		if(kvendor!=null)
			GeneratePODetail.pheader.setNobukti(oriNoPO.replace("xxxxxx", kvendor));
		else
			GeneratePODetail.pheader.setNobukti(oriNoPO);
	}
	public String getNoPO()
    {
    	Object[] params=new Object[]{null};
    	
    	Class[] retTypes =new Class[]{Integer.class};
    	try {
			Object[] response=_service.callServicePurchasing("getNOPO", params,retTypes);
			Integer item = (Integer)response[0];
			//PR-yyyy-999999-xxx
			SimpleDateFormat sdf =new SimpleDateFormat("yyyy");
			DecimalFormat df=new DecimalFormat("000000");
			oriNoPO="PO-" +sdf.format(new Date()) + "-" +df.format(item+=1) + "-xxxxxx";
			return oriNoPO;
		} catch (AxisFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
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
				_hj.setKgroup(hj.getKgroup());
				_hj.setNobukti(hj.getNobukti());
				_hj.setTglinput(hj.getTglinput());
				_hj.setUserinput(hj.getUserinput());
				_hj.setKcompany(hj.getKcompany());
				_hj.setKbarang(hj.getKbarang());
				_hj.setNbarang(hj.getNbarang());
				_hj.setNo(i+1);
				_hj.setKvaluta(hj.getKvaluta());
				_hj.setSatuan(hj.getSatuan());
				_hj.setJumlah(hj.getJumlah());
				_hj.setJumlah1(hj.getJumlah1());
				_hj.setJumlah2(hj.getJumlah2());
				_hj.setNvaluta(hj.getNvaluta());
				_hj.setPdisc(hj.getPdisc());
				double total = _hj.getJumlah2() * _hj.getNvaluta() ;// –_hj.getPdisc()/100
				double totaldisc = total * _hj.getPdisc()/100;
				total =total-totaldisc;
				
				if(selectedPRDetail!=null)
				{
					if(selectedPRDetail.getKbarang().equals(hj.getKbarang()))
						total = selectedPRDetail.getTotal();
				}
				_hj.setTotal(total);
				_hj.setNobukti1(hj.getNobukti1());
				_hj.setKet(hj.getKet());
				if(null!=_hj && _hj.getJumlah2()>0) 
				{
					list.add(_hj);
				}
			}
			generateTotalNilaiPO();
		} catch (AxisFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
