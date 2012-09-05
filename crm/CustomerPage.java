package org.apache.wicket.erp.crm;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.axis2.AxisFault;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.erp.ERPApplication;
import org.apache.wicket.erp.ERPWebPage;
import org.apache.wicket.erp.utils.IActionHandler;
import org.apache.wicket.erp.utils.PanelAction;
import org.apache.wicket.erp.utils.Service;
import org.apache.wicket.erp.utils.UserInfo;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RadioChoice;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import sf.accounting.Pajak;
import sf.accounting.User;
import sf.crm.Customer;
import sf.crm.Industry;
import sf.purchasing.Title;

import java.text.SimpleDateFormat;
public class CustomerPage extends ERPWebPage implements IActionHandler {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static List<String> TITLES = Arrays
			.asList(new String[] { ""});
	private static List<String> SALES = Arrays
			.asList(new String[] { ""});
	private static List<String> INDUSTRIES= Arrays
	.asList(new String[] { ""});
	private static List<String> STATUS= Arrays
	.asList(new String[] { "Customer","Cabang","Tender/Supplier","Lain-lain"});
	private static List<String> CLASS= Arrays
	.asList(new String[] { "A","B","C"});
	
	private static List<String> PAJAKS= Arrays
	.asList(new String[] { ""});
	
	private static List<String> CLIENT1= Arrays
	.asList(new String[] { ""});
	
	private RequiredTextField<String> kclient;
	private TextField<String> kcabang;
	private DropDownChoice<String> ktitel;
	private RequiredTextField<String> nclient;
	private DropDownChoice<String> kindustridesc;
	private RadioChoice<String> kclass;
	private RadioChoice<String> kstatusdesc;
	private DropDownChoice<String> ksales;
	private RequiredTextField<String> kcompany;
	private DropDownChoice<String> kclient1;
	private TextField<String> npwp;
	private DropDownChoice<String> kdtrsdesc;
	private TextField<String> plafond;
	private TextField<String> jtempo;
	private CheckBox tfaktur1;
	private CheckBox tfaktur2;
	private CheckBox tfaktur3;
	private CheckBox tfaktur4;
	private CheckBox tfaktur5;
	private CheckBox tfaktur6;
	private TextField<String> kpdir1;
	private TextField<String> emdir1;
	private TextField<String> hpdir1;
	private TextField<String> kpdir2;
	private TextField<String> emdir2;
	private TextField<String> hpdir2;
	private TextField<String> kpgm1;
	private TextField<String> emgm1;
	private TextField<String> hpgm1;
	private TextField<String> kpgm2;
	private TextField<String> emgm2;
	private TextField<String> hpgm2;
	private TextField<String> kpbeli1;
	private TextField<String> embeli1;
	private TextField<String> hpbeli1;
	private TextField<String> kpbeli2;
	private TextField<String> embeli2;
	private TextField<String> hpbeli2;
	private TextField<String> kpuser1;
	private TextField<String> emuser1;
	private TextField<String> hpuser1;
	private TextField<String> kpuser2;
	private TextField<String> emuser2;
	private TextField<String> hpuser2;
	private RequiredTextField<String> ialamat1;
	private TextField<String> ialamat2;
	private TextField<String> ialamat3;
	private RequiredTextField<String> ikota;
	private TextField<String> ikodepos;
	private TextField<String> inotelp;
	private TextField<String> inofax;
	private TextField<String> iemail;
	private TextField<String> ikontak1;
	private TextField<String> ikontak2;
	private TextField<String> inohp1;
	private TextField<String> inohp2;
	private TextField<String> iemail1;
	private TextField<String> iemail2;
	private TextField<String> talamat1;
	private TextField<String> talamat2;
	private TextField<String> talamat3;
	private TextField<String> tkota;
	private TextField<String> tkodepos;
	private TextField<String> tnotelp;
	private TextField<String> tnofax;
	private TextField<String> temail;
	private TextField<String> tkontak1;
	private TextField<String> tkontak2;
	private TextField<String> tnohp1;
	private TextField<String> tnohp2;
	private TextField<String> temail1;
	private TextField<String> temail2;
	private TextField<String> kalamat1;
	private TextField<String> kalamat2;
	private TextField<String> kalamat3;
	private TextField<String> kkota;
	private TextField<String> kkodepos;
	private TextField<String> knotelp;
	private TextField<String> knofax;
	private TextField<String> kemail;
	private TextField<String> kkontak1;
	private TextField<String> kkontak2;
	private TextField<String> knohp1;
	private TextField<String> knohp2;
	private TextField<String> kemail1;
	private TextField<String> kemail2;
	private TextField<String> palamat1;
	private TextField<String> palamat2;
	private TextField<String> palamat3;
	private TextField<String> pkota;
	private TextField<String> pkodepos;
	private TextField<String> pnotelp;
	private TextField<String> pnofax;
	private TextField<String> pemail;
	private TextField<String> pkontak1;
	private TextField<String> pkontak2;
	private TextField<String> pnohp1;
	private TextField<String> pnohp2;
	private TextField<String> pemail1;
	private TextField<String> pemail2;
	boolean isReadOnly=true;
	private Form<Customer> form;
	final AttributeModifier ro = new AttributeModifier("readonly", isReadOnly);
	private Customer customer = new Customer();
	private static CustomerPage app;
	private Service _service;
	private Service _serviceAcc;
	private Service _servicePurchase;
	private void init()
	{
		try {
			_service=new Service(Service.CRM_SERVICE_URL);
			_serviceAcc=new Service(Service.ACCOUNTING_SERVICE_URL);
			_servicePurchase=new Service(Service.PURCHASING_SERVICE_URL);
		} catch (AxisFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static CustomerPage get()
	{
		return app;
	}
	
	public CustomerPage(PageParameters p)
	{
		this(new Customer());
		readOnly(false);
		if(p.getNamedKeys().contains("error"))
			error(p.get("error"));
		if(p.getNamedKeys().contains("info"))
			info(p.get("info"));
		
	}
	public CustomerPage()
	{
		this(new Customer());
	}
	public CustomerPage(Customer customer)
	{
		super("CRM | Master Customer");
		
		if(customer.getKcompany()==null || customer.getKcompany().equals(""))
		{
			customer.setKcompany(UserInfo.COMPANY);
			customer.setKcabang(UserInfo.CODE_BRANCH);
		}
		if(customer.getKclient1()!=null && customer.getKclient1().equals(""))
			customer.setKclient1(null);
		
		init();
		getSales();
		getTitel();
		getIndustri();
		getPajak();
		getGroupCustomer();
		this.customer = customer;
		add(new CustomerFilter("customerfilter",this));
		form = new Form<Customer>("form", new CompoundPropertyModel<Customer>(this.customer));
		add(form);
		
		// add a simple text field that uses Input's 'text' property. Nothing
		// can go wrong here
		kclient = new RequiredTextField<String>("kclient");
		kcabang = new TextField<String>("kcabang");
		ktitel = new DropDownChoice<String>("ktitel", TITLES);
		ktitel.setRequired(true);
		nclient = new RequiredTextField<String>("nclient");
		kindustridesc = new DropDownChoice<String>("kindustridesc",INDUSTRIES);
		kindustridesc.setRequired(true);
		kclass = new RadioChoice<String>("kclass",CLASS);
		kclass.setSuffix(" ");
		kclass.setRequired(true);
		
		kstatusdesc = new RadioChoice<String>("kstatusdesc",STATUS);
		kstatusdesc.setSuffix(" ");
		kstatusdesc.setRequired(true);
		
		ksales = new DropDownChoice<String>("ksales", SALES);
		ksales.setRequired(true);
		kcompany = new RequiredTextField<String>("kcompany");
		kclient1 = new DropDownChoice<String>("kclient1",CLIENT1);
		npwp = new TextField<String>("npwp");
		kdtrsdesc = new DropDownChoice<String>("kdtrsdesc",PAJAKS);
		kdtrsdesc.setRequired(true);
		plafond = new TextField<String>("plafond");
		jtempo = new TextField<String>("jtempo");
		tfaktur1 = new CheckBox("btfaktur1");
		tfaktur2 = new CheckBox("btfaktur2");
		tfaktur3 = new CheckBox("btfaktur3");
		tfaktur4 = new CheckBox("btfaktur4");
		tfaktur5 = new CheckBox("btfaktur5");
		tfaktur6 = new CheckBox("btfaktur6");
		kpdir1 = new TextField<String>("kpdir1");
		emdir1 = new TextField<String>("emdir1");
		hpdir1 = new TextField<String>("hpdir1");
		kpdir2 = new TextField<String>("kpdir2");
		emdir2 = new TextField<String>("emdir2");
		hpdir2 = new TextField<String>("hpdir2");
		kpgm1 = new TextField<String>("kpgm1");
		emgm1 = new TextField<String>("emgm1");
		hpgm1 = new TextField<String>("hpgm1");
		kpgm2 = new TextField<String>("kpgm2");
		emgm2 = new TextField<String>("emgm2");
		hpgm2 = new TextField<String>("hpgm2");
		kpbeli1 = new TextField<String>("kpbeli1");
		embeli1 = new TextField<String>("embeli1");
		hpbeli1 = new TextField<String>("hpbeli1");
		kpbeli2 = new TextField<String>("kpbeli2");
		embeli2 = new TextField<String>("embeli2");
		hpbeli2 = new TextField<String>("hpbeli2");
		kpuser1 = new TextField<String>("kpuser1");
		emuser1 = new TextField<String>("emuser1");
		hpuser1 = new TextField<String>("hpuser1");
		kpuser2 = new TextField<String>("kpuser2");
		emuser2 = new TextField<String>("emuser2");
		hpuser2 = new TextField<String>("hpuser2");
		ialamat1 = new RequiredTextField<String>("ialamat1");
		ialamat2 = new TextField<String>("ialamat2");
		ialamat3 = new TextField<String>("ialamat3");
		ikota = new RequiredTextField<String>("ikota");
		ikodepos = new TextField<String>("ikodepos");
		inotelp = new TextField<String>("inotelp");
		inofax = new TextField<String>("inofax");
		iemail = new TextField<String>("iemail");
		ikontak1 = new TextField<String>("ikontak1");
		ikontak2 = new TextField<String>("ikontak2");
		inohp1 = new TextField<String>("inohp1");
		inohp2 = new TextField<String>("inohp2");
		iemail1 = new TextField<String>("iemail1");
		iemail2 = new TextField<String>("iemail2");
		talamat1 = new TextField<String>("talamat1");
		talamat2 = new TextField<String>("talamat2");
		talamat3 = new TextField<String>("talamat3");
		tkota = new TextField<String>("tkota");
		tkodepos = new TextField<String>("tkodepos");
		tnotelp = new TextField<String>("tnotelp");
		tnofax = new TextField<String>("tnofax");
		temail = new TextField<String>("temail");
		tkontak1 = new TextField<String>("tkontak1");
		tkontak2 = new TextField<String>("tkontak2");
		tnohp1 = new TextField<String>("tnohp1");
		tnohp2 = new TextField<String>("tnohp2");
		temail1 = new TextField<String>("temail1");
		temail2 = new TextField<String>("temail2");
		kalamat1 = new TextField<String>("kalamat1");
		kalamat2 = new TextField<String>("kalamat2");
		kalamat3 = new TextField<String>("kalamat3");
		kkota = new TextField<String>("kkota");
		kkodepos = new TextField<String>("kkodepos");
		knotelp = new TextField<String>("knotelp");
		knofax = new TextField<String>("knofax");
		kemail = new TextField<String>("kemail");
		kkontak1 = new TextField<String>("kkontak1");
		kkontak2 = new TextField<String>("kkontak2");
		knohp1 = new TextField<String>("knohp1");
		knohp2 = new TextField<String>("knohp2");
		kemail1 = new TextField<String>("kemail1");
		kemail2 = new TextField<String>("kemail2");
		palamat1 = new TextField<String>("palamat1");
		palamat2 = new TextField<String>("palamat2");
		palamat3 = new TextField<String>("palamat3");
		pkota = new TextField<String>("pkota");
		pkodepos = new TextField<String>("pkodepos");
		pnotelp = new TextField<String>("pnotelp");
		pnofax = new TextField<String>("pnofax");
		pemail = new TextField<String>("pemail");
		pkontak1 = new TextField<String>("pkontak1");
		pkontak2 = new TextField<String>("pkontak2");
		pnohp1 = new TextField<String>("pnohp1");
		pnohp2 = new TextField<String>("pnohp2");
		pemail1 = new TextField<String>("pemail1");
		pemail2 = new TextField<String>("pemail2");
		
		this.readOnly();
		
		form.add(kclient);
		form.add(kcabang);
		form.add(ktitel);
		form.add(nclient);
		form.add(kindustridesc);
		form.add(kclass);
		form.add(kstatusdesc);
		form.add(ksales);
		form.add(kcompany);
		form.add(kclient1);
		form.add(npwp);
		form.add(kdtrsdesc);
		form.add(plafond);
		form.add(jtempo);
		form.add(tfaktur1);
		form.add(tfaktur2);
		form.add(tfaktur3);
		form.add(tfaktur4);
		form.add(tfaktur5);
		form.add(tfaktur6);
		form.add(kpdir1);
		form.add(emdir1);
		form.add(hpdir1);
		form.add(kpdir2);
		form.add(emdir2);
		form.add(hpdir2);
		form.add(kpgm1);
		form.add(emgm1);
		form.add(hpgm1);
		form.add(kpgm2);
		form.add(emgm2);
		form.add(hpgm2);
		form.add(kpbeli1);
		form.add(embeli1);
		form.add(hpbeli1);
		form.add(kpbeli2);
		form.add(embeli2);
		form.add(hpbeli2);
		form.add(kpuser1);
		form.add(emuser1);
		form.add(hpuser1);
		form.add(kpuser2);
		form.add(emuser2);
		form.add(hpuser2);
		form.add(ialamat1);
		form.add(ialamat2);
		form.add(ialamat3);
		form.add(ikota);
		form.add(ikodepos);
		form.add(inotelp);
		form.add(inofax);
		form.add(iemail);
		form.add(ikontak1);
		form.add(ikontak2);
		form.add(inohp1);
		form.add(inohp2);
		form.add(iemail1);
		form.add(iemail2);
		form.add(talamat1);
		form.add(talamat2);
		form.add(talamat3);
		form.add(tkota);
		form.add(tkodepos);
		form.add(tnotelp);
		form.add(tnofax);
		form.add(temail);
		form.add(tkontak1);
		form.add(tkontak2);
		form.add(tnohp1);
		form.add(tnohp2);
		form.add(temail1);
		form.add(temail2);
		form.add(kalamat1);
		form.add(kalamat2);
		form.add(kalamat3);
		form.add(kkota);
		form.add(kkodepos);
		form.add(knotelp);
		form.add(knofax);
		form.add(kemail);
		form.add(kkontak1);
		form.add(kkontak2);
		form.add(knohp1);
		form.add(knohp2);
		form.add(kemail1);
		form.add(kemail2);
		form.add(palamat1);
		form.add(palamat2);
		form.add(palamat3);
		form.add(pkota);
		form.add(pkodepos);
		form.add(pnotelp);
		form.add(pnofax);
		form.add(pemail);
		form.add(pkontak1);
		form.add(pkontak2);
		form.add(pnohp1);
		form.add(pnohp2);
		form.add(pemail1);
		form.add(pemail2);
		/*
		kclient.add(StringValidator.maximumLength(6));
		ktitel.add(StringValidator.maximumLength(10));
		nclient.add(StringValidator.maximumLength(100));
		kindustri.add(StringValidator.maximumLength(2));
		kclass.add(StringValidator.maximumLength(1));
		kstatus.add(StringValidator.maximumLength(1));
		ksales.add(StringValidator.maximumLength(4));
		kcompany.add(StringValidator.maximumLength(2));
		kclient1.add(StringValidator.maximumLength(6));
		npwp.add(StringValidator.maximumLength(25));
		kdtrs.add(StringValidator.maximumLength(2));
		
		kpdir1.add(StringValidator.maximumLength(50));
		emdir1.add(StringValidator.maximumLength(50));
		hpdir1.add(StringValidator.maximumLength(50));
		kpdir2.add(StringValidator.maximumLength(50));
		emdir2.add(StringValidator.maximumLength(50));
		hpdir2.add(StringValidator.maximumLength(50));
		kpgm1.add(StringValidator.maximumLength(50));
		emgm1.add(StringValidator.maximumLength(50));
		hpgm1.add(StringValidator.maximumLength(30));
		kpgm2.add(StringValidator.maximumLength(50));
		emgm2.add(StringValidator.maximumLength(50));
		hpgm2.add(StringValidator.maximumLength(30));
		kpbeli1.add(StringValidator.maximumLength(50));
		embeli1.add(StringValidator.maximumLength(50));
		hpbeli1.add(StringValidator.maximumLength(30));
		kpbeli2.add(StringValidator.maximumLength(50));
		embeli2.add(StringValidator.maximumLength(50));
		hpbeli2.add(StringValidator.maximumLength(30));
		kpuser1.add(StringValidator.maximumLength(50));
		emuser1.add(StringValidator.maximumLength(50));
		hpuser1.add(StringValidator.maximumLength(30));
		kpuser2.add(StringValidator.maximumLength(50));
		emuser2.add(StringValidator.maximumLength(50));
		hpuser2.add(StringValidator.maximumLength(30));
		ialamat1.add(StringValidator.maximumLength(100));
		ialamat2.add(StringValidator.maximumLength(100));
		ialamat3.add(StringValidator.maximumLength(100));
		ikota.add(StringValidator.maximumLength(50));
		ikodepos.add(StringValidator.maximumLength(5));
		inotelp.add(StringValidator.maximumLength(30));
		inofax.add(StringValidator.maximumLength(30));
		iemail.add(StringValidator.maximumLength(50));
		ikontak1.add(StringValidator.maximumLength(50));
		ikontak2.add(StringValidator.maximumLength(50));
		inohp1.add(StringValidator.maximumLength(30));
		inohp2.add(StringValidator.maximumLength(30));
		iemail1.add(StringValidator.maximumLength(50));
		iemail2.add(StringValidator.maximumLength(50));
		talamat1.add(StringValidator.maximumLength(100));
		talamat2.add(StringValidator.maximumLength(100));
		talamat3.add(StringValidator.maximumLength(100));
		tkota.add(StringValidator.maximumLength(50));
		tkodepos.add(StringValidator.maximumLength(5));
		tnotelp.add(StringValidator.maximumLength(30));
		tnofax.add(StringValidator.maximumLength(30));
		temail.add(StringValidator.maximumLength(50));
		tkontak1.add(StringValidator.maximumLength(50));
		tkontak2.add(StringValidator.maximumLength(50));
		tnohp1.add(StringValidator.maximumLength(30));
		tnohp2.add(StringValidator.maximumLength(30));
		temail1.add(StringValidator.maximumLength(50));
		temail2.add(StringValidator.maximumLength(50));
		kalamat1.add(StringValidator.maximumLength(100));
		kalamat2.add(StringValidator.maximumLength(100));
		kalamat3.add(StringValidator.maximumLength(100));
		kkota.add(StringValidator.maximumLength(50));
		kkodepos.add(StringValidator.maximumLength(5));
		knotelp.add(StringValidator.maximumLength(30));
		knofax.add(StringValidator.maximumLength(30));
		kemail.add(StringValidator.maximumLength(50));
		kkontak1.add(StringValidator.maximumLength(50));
		kkontak2.add(StringValidator.maximumLength(50));
		knohp1.add(StringValidator.maximumLength(30));
		knohp2.add(StringValidator.maximumLength(30));
		kemail1.add(StringValidator.maximumLength(50));
		kemail2.add(StringValidator.maximumLength(50));
		palamat1.add(StringValidator.maximumLength(100));
		palamat2.add(StringValidator.maximumLength(100));
		palamat3.add(StringValidator.maximumLength(100));
		pkota.add(StringValidator.maximumLength(50));
		pkodepos.add(StringValidator.maximumLength(5));
		pnotelp.add(StringValidator.maximumLength(30));
		pnofax.add(StringValidator.maximumLength(30));
		pemail.add(StringValidator.maximumLength(50));
		pkontak1.add(StringValidator.maximumLength(50));
		pkontak2.add(StringValidator.maximumLength(50));
		pnohp1.add(StringValidator.maximumLength(30));
		pnohp2.add(StringValidator.maximumLength(30));
		pemail1.add(StringValidator.maximumLength(50));
		pemail2.add(StringValidator.maximumLength(50));
		recstatus.add(StringValidator.maximumLength(1));
		*/
		final FeedbackPanel feedback = new FeedbackPanel("feedback");
		add(feedback);
		form.add(new PanelAction("actionpanel",this));
		app=this;
		ERPApplication.pageTitle="CRM | Master Customer";
	}
	
	public boolean updateCustomer()
	{
		form.process(null);
		Customer item = (Customer)form.getModelObject();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String dateNow = sdf.format(new Date());
		item.setTglupdate(Long.parseLong(dateNow));
		item.setUserupdate(UserInfo.USERID);
		if(item.getKdtrsdesc()!=null)
			item.setKdtrs(item.getKdtrsdesc().split("\\-")[0]);
		if(item.getKindustri()!=null)
			item.setKindustri(item.getKindustridesc().split("\\-")[0]);
		String [] cs= item.getKsales().split("\\-");
		item.setKsales(cs[0]);
		
		if(item.getKstatusdesc().equals("Customer"))
			item.setKstatus("C");
		else if(item.getKstatusdesc().equals("Cabang"))
			item.setKstatus("B");
		else if(item.getKstatusdesc().equals("Tender/Supplier"))
			item.setKstatus("T");
		else
			item.setKstatus("L");
		
		if(item.isBtfaktur1())
			item.setTfaktur1("Y");
		else
			item.setTfaktur1("N");
		
		if(item.isBtfaktur2())
			item.setTfaktur2("Y");
		else
			item.setTfaktur2("N");
		
		if(item.isBtfaktur3())
			item.setTfaktur3("Y");
		else
			item.setTfaktur3("N");
		
		if(item.isBtfaktur4())
			item.setTfaktur4("Y");
		else
			item.setTfaktur4("N");
		
		if(item.isBtfaktur5())
			item.setTfaktur5("Y");
		else
			item.setTfaktur5("N");
		
		if(item.isBtfaktur6())
			item.setTfaktur6("Y");
		else
			item.setTfaktur6("N");
		
    	Object[] params=new Object[]{item,item.getKcompany()};
    	Class[] retTypes =new Class[]{Boolean.class};
    	
    	Object[] response;
		try {
			response = _service.callServiceCRM("updateCustomer", params, retTypes);
			Boolean ret=(Boolean)response[0];
			if(ret)
			{
				PanelAction.actionType=2;//set panel action with 2//edit and delete button is enabled.
				setResponsePage(new CustomerPage(CustomerFilter.selectedCustomer));
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
		Customer customer = (Customer)form.getModelObject();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String dateNow = sdf.format(new Date());
		customer.setTglupdate(Long.parseLong(dateNow));
		customer.setUserupdate(UserInfo.USERID);
		
    	Object[] params=new Object[]{customer};
    	Class[] retTypes =new Class[]{Boolean.class};
    	
    	Object[] response;
		try {
			response = _service.callServiceCRM("deleteCustomer", params, retTypes);
			Boolean ret=(Boolean)response[0];
			if(ret)
			{
				PageParameters pg=new PageParameters();
				pg.add("info", "Delete Customer has been successfully.");
				pg.remove("error");
				PanelAction.actionType=0;
				setResponsePage(CustomerPage.class,pg);
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
		Customer item = (Customer)form.getModelObject();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String dateNow = sdf.format(new Date());
		item.setTglupdate(Long.parseLong(dateNow));
		item.setUserupdate(UserInfo.USERID);
		if(item.getKdtrsdesc()!=null)
			item.setKdtrs(item.getKdtrsdesc().split("\\-")[0]);
		
		if(item.getKindustridesc()!=null)
			item.setKindustri(item.getKindustridesc().split("\\-")[0]);
		
		if(item.getKstatusdesc().equals("Customer"))
			item.setKstatus("C");
		else if(item.getKstatusdesc().equals("Cabang"))
			item.setKstatus("B");
		else if(item.getKstatusdesc().equals("Tender/Supplier"))
			item.setKstatus("T");
		else
			item.setKstatus("L");
		
		if(item.isBtfaktur1())
			item.setTfaktur1("Y");
		else
			item.setTfaktur1("N");
		
		if(item.isBtfaktur2())
			item.setTfaktur2("Y");
		else
			item.setTfaktur2("N");
		
		if(item.isBtfaktur3())
			item.setTfaktur3("Y");
		else
			item.setTfaktur3("N");
		
		if(item.isBtfaktur4())
			item.setTfaktur4("Y");
		else
			item.setTfaktur4("N");
		
		if(item.isBtfaktur5())
			item.setTfaktur5("Y");
		else
			item.setTfaktur5("N");
		
		if(item.isBtfaktur6())
			item.setTfaktur6("Y");
		else
			item.setTfaktur6("N");
		
    	Object[] params=new Object[]{item,item.getKcompany()};
    	Class[] retTypes =new Class[]{Boolean.class};
    	
    	Object[] response;
    	boolean duplicate=false;
		try {
			response = _service.callServiceCRM("insertCustomer", params, retTypes);
			Boolean ret=(Boolean)response[0];
			if(ret)
			{
				PageParameters pg=new PageParameters();
				pg.add("info", "Insert Customer has been successfully.");
				pg.remove("error");
				setResponsePage(CustomerPage.class,pg);
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
		Boolean bRet=updateCustomer();
		if(bRet)
		{
			info("Update Customer has been successfully.");
			return 1;
		}
		else
			error("Failed to update Customer.");
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
			ktitel.remove(ro);
			nclient.remove(ro);
			kindustridesc.remove(ro);
			kclass.remove(ro);
			kstatusdesc.remove(ro);
			ksales.remove(ro);
			kcompany.remove(ro);
			kclient1.remove(ro);
			npwp.remove(ro);
			kdtrsdesc.remove(ro);
			plafond.remove(ro);
			jtempo.remove(ro);
			tfaktur1.remove(ro);
			tfaktur2.remove(ro);
			tfaktur3.remove(ro);
			tfaktur4.remove(ro);
			tfaktur5.remove(ro);
			tfaktur6.remove(ro);
			kpdir1.remove(ro);
			emdir1.remove(ro);
			hpdir1.remove(ro);
			kpdir2.remove(ro);
			emdir2.remove(ro);
			hpdir2.remove(ro);
			kpgm1.remove(ro);
			emgm1.remove(ro);
			hpgm1.remove(ro);
			kpgm2.remove(ro);
			emgm2.remove(ro);
			hpgm2.remove(ro);
			kpbeli1.remove(ro);
			embeli1.remove(ro);
			hpbeli1.remove(ro);
			kpbeli2.remove(ro);
			embeli2.remove(ro);
			hpbeli2.remove(ro);
			kpuser1.remove(ro);
			emuser1.remove(ro);
			hpuser1.remove(ro);
			kpuser2.remove(ro);
			emuser2.remove(ro);
			hpuser2.remove(ro);
			ialamat1.remove(ro);
			ialamat2.remove(ro);
			ialamat3.remove(ro);
			ikota.remove(ro);
			ikodepos.remove(ro);
			inotelp.remove(ro);
			inofax.remove(ro);
			iemail.remove(ro);
			ikontak1.remove(ro);
			ikontak2.remove(ro);
			inohp1.remove(ro);
			inohp2.remove(ro);
			iemail1.remove(ro);
			iemail2.remove(ro);
			talamat1.remove(ro);
			talamat2.remove(ro);
			talamat3.remove(ro);
			tkota.remove(ro);
			tkodepos.remove(ro);
			tnotelp.remove(ro);
			tnofax.remove(ro);
			temail.remove(ro);
			tkontak1.remove(ro);
			tkontak2.remove(ro);
			tnohp1.remove(ro);
			tnohp2.remove(ro);
			temail1.remove(ro);
			temail2.remove(ro);
			kalamat1.remove(ro);
			kalamat2.remove(ro);
			kalamat3.remove(ro);
			kkota.remove(ro);
			kkodepos.remove(ro);
			knotelp.remove(ro);
			knofax.remove(ro);
			kemail.remove(ro);
			kkontak1.remove(ro);
			kkontak2.remove(ro);
			knohp1.remove(ro);
			knohp2.remove(ro);
			kemail1.remove(ro);
			kemail2.remove(ro);
			palamat1.remove(ro);
			palamat2.remove(ro);
			palamat3.remove(ro);
			pkota.remove(ro);
			pkodepos.remove(ro);
			pnotelp.remove(ro);
			pnofax.remove(ro);
			pemail.remove(ro);
			pkontak1.remove(ro);
			pkontak2.remove(ro);
			pnohp1.remove(ro);
			pnohp2.remove(ro);
			pemail1.remove(ro);
			pemail2.remove(ro);
		}
		catch(Exception ex){}
	}

	public void newAction() {
		// TODO Auto-generated method stub
		customer=new Customer();
		customer.setKcompany(UserInfo.COMPANY);
		customer.setKcabang(UserInfo.CODE_BRANCH);
		form.setModelObject(customer);
		this.readOnly(false);
	}

	private void readOnly(boolean flag)
	{
		if(!flag)
		{
			kclient.remove(ro);
			ktitel.remove(ro);
			nclient.remove(ro);
			kindustridesc.remove(ro);
			kclass.remove(ro);
			kstatusdesc.remove(ro);
			ksales.remove(ro);
			kclient1.remove(ro);
			npwp.remove(ro);
			kdtrsdesc.remove(ro);
			plafond.remove(ro);
			jtempo.remove(ro);
			tfaktur1.remove(ro);
			tfaktur2.remove(ro);
			tfaktur3.remove(ro);
			tfaktur4.remove(ro);
			tfaktur5.remove(ro);
			tfaktur6.remove(ro);
			kpdir1.remove(ro);
			emdir1.remove(ro);
			hpdir1.remove(ro);
			kpdir2.remove(ro);
			emdir2.remove(ro);
			hpdir2.remove(ro);
			kpgm1.remove(ro);
			emgm1.remove(ro);
			hpgm1.remove(ro);
			kpgm2.remove(ro);
			emgm2.remove(ro);
			hpgm2.remove(ro);
			kpbeli1.remove(ro);
			embeli1.remove(ro);
			hpbeli1.remove(ro);
			kpbeli2.remove(ro);
			embeli2.remove(ro);
			hpbeli2.remove(ro);
			kpuser1.remove(ro);
			emuser1.remove(ro);
			hpuser1.remove(ro);
			kpuser2.remove(ro);
			emuser2.remove(ro);
			hpuser2.remove(ro);
			ialamat1.remove(ro);
			ialamat2.remove(ro);
			ialamat3.remove(ro);
			ikota.remove(ro);
			ikodepos.remove(ro);
			inotelp.remove(ro);
			inofax.remove(ro);
			iemail.remove(ro);
			ikontak1.remove(ro);
			ikontak2.remove(ro);
			inohp1.remove(ro);
			inohp2.remove(ro);
			iemail1.remove(ro);
			iemail2.remove(ro);
			talamat1.remove(ro);
			talamat2.remove(ro);
			talamat3.remove(ro);
			tkota.remove(ro);
			tkodepos.remove(ro);
			tnotelp.remove(ro);
			tnofax.remove(ro);
			temail.remove(ro);
			tkontak1.remove(ro);
			tkontak2.remove(ro);
			tnohp1.remove(ro);
			tnohp2.remove(ro);
			temail1.remove(ro);
			temail2.remove(ro);
			kalamat1.remove(ro);
			kalamat2.remove(ro);
			kalamat3.remove(ro);
			kkota.remove(ro);
			kkodepos.remove(ro);
			knotelp.remove(ro);
			knofax.remove(ro);
			kemail.remove(ro);
			kkontak1.remove(ro);
			kkontak2.remove(ro);
			knohp1.remove(ro);
			knohp2.remove(ro);
			kemail1.remove(ro);
			kemail2.remove(ro);
			palamat1.remove(ro);
			palamat2.remove(ro);
			palamat3.remove(ro);
			pkota.remove(ro);
			pkodepos.remove(ro);
			pnotelp.remove(ro);
			pnofax.remove(ro);
			pemail.remove(ro);
			pkontak1.remove(ro);
			pkontak2.remove(ro);
			pnohp1.remove(ro);
			pnohp2.remove(ro);
			pemail1.remove(ro);
			pemail2.remove(ro);
		}
		else
		{
			kclient.add(ro);
			kcabang.add(ro);
			ktitel.add(ro);
			nclient.add(ro);
			kindustridesc.add(ro);
			kclass.add(ro);
			kstatusdesc.add(ro);
			ksales.add(ro);
			kcompany.add(ro);
			kclient1.add(ro);
			npwp.add(ro);
			kdtrsdesc.add(ro);
			plafond.add(ro);
			jtempo.add(ro);
			tfaktur1.add(ro);
			tfaktur2.add(ro);
			tfaktur3.add(ro);
			tfaktur4.add(ro);
			tfaktur5.add(ro);
			tfaktur6.add(ro);
			kpdir1.add(ro);
			emdir1.add(ro);
			hpdir1.add(ro);
			kpdir2.add(ro);
			emdir2.add(ro);
			hpdir2.add(ro);
			kpgm1.add(ro);
			emgm1.add(ro);
			hpgm1.add(ro);
			kpgm2.add(ro);
			emgm2.add(ro);
			hpgm2.add(ro);
			kpbeli1.add(ro);
			embeli1.add(ro);
			hpbeli1.add(ro);
			kpbeli2.add(ro);
			embeli2.add(ro);
			hpbeli2.add(ro);
			kpuser1.add(ro);
			emuser1.add(ro);
			hpuser1.add(ro);
			kpuser2.add(ro);
			emuser2.add(ro);
			hpuser2.add(ro);
			ialamat1.add(ro);
			ialamat2.add(ro);
			ialamat3.add(ro);
			ikota.add(ro);
			ikodepos.add(ro);
			inotelp.add(ro);
			inofax.add(ro);
			iemail.add(ro);
			ikontak1.add(ro);
			ikontak2.add(ro);
			inohp1.add(ro);
			inohp2.add(ro);
			iemail1.add(ro);
			iemail2.add(ro);
			talamat1.add(ro);
			talamat2.add(ro);
			talamat3.add(ro);
			tkota.add(ro);
			tkodepos.add(ro);
			tnotelp.add(ro);
			tnofax.add(ro);
			temail.add(ro);
			tkontak1.add(ro);
			tkontak2.add(ro);
			tnohp1.add(ro);
			tnohp2.add(ro);
			temail1.add(ro);
			temail2.add(ro);
			kalamat1.add(ro);
			kalamat2.add(ro);
			kalamat3.add(ro);
			kkota.add(ro);
			kkodepos.add(ro);
			knotelp.add(ro);
			knofax.add(ro);
			kemail.add(ro);
			kkontak1.add(ro);
			kkontak2.add(ro);
			knohp1.add(ro);
			knohp2.add(ro);
			kemail1.add(ro);
			kemail2.add(ro);
			palamat1.add(ro);
			palamat2.add(ro);
			palamat3.add(ro);
			pkota.add(ro);
			pkodepos.add(ro);
			pnotelp.add(ro);
			pnofax.add(ro);
			pemail.add(ro);
			pkontak1.add(ro);
			pkontak2.add(ro);
			pnohp1.add(ro);
			pnohp2.add(ro);
			pemail1.add(ro);
			pemail2.add(ro);
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

	public void cancel(boolean flag) {
		// TODO Auto-generated method stub
		if(flag)
			setResponsePage(new CustomerPage(CustomerFilter.selectedCustomer));
		else
			setResponsePage(CustomerPage.class);
		
		this.readOnly();
	}

	public void activate() {
		// TODO Auto-generated method stub
		setResponsePage(CustomerPage.class);
	}

	public void print() {
		// TODO Auto-generated method stub
		
	}
	
	private String[] kodesTitle=null ;
	private String[] kodesSales=null ;
	private String[] kodesIndustry=null ;
	private String[] kodesPajak=null ;
	private String[] kodesClient1=null ;
	
	private void getTitel()
	{
		Object[] params=new Object[]{null};
    	
    	Class[] retTypes =new Class[]{Title.class};
    	try {
			Object[] response=_servicePurchase.callServicePurchasing("getTitle", params,retTypes);
			Title item = (Title)response[0];
			if(item==null) return;
			if(item.getTitles()==null) return;
			
			int count=item.getTitles().length;
			kodesTitle=new String[count];
			for(int i=0; i < count;i++)
			{
				Title title=item.getTitles()[i];
				if(null!=title) 
				{
					kodesTitle[i]=title.getKtitel();
				}
			}
			TITLES=Arrays.
			asList(kodesTitle);
		} catch (AxisFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void getSales()
	{
		Object[] params=new Object[]{UserInfo.COMPANY};
    	
    	Class[] retTypes =new Class[]{User.class};
    	try {
			Object[] response=_serviceAcc.callServiceAccounting("getSales", params,retTypes);
			User item = (User)response[0];
			if(item==null) return;
			if(item.getUsers()==null) return;
			
			int count=item.getUsers().length;
			kodesSales=new String[count];
			for(int i=0; i < count;i++)
			{
				User user=item.getUsers()[i];
				if(null!=user) 
				{
					kodesSales[i]=user.getKstaff();
				}
			}
			SALES=Arrays.
			asList(kodesSales);
		} catch (AxisFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void getIndustri()
	{
		Object[] params=new Object[]{null};
    	
    	Class[] retTypes =new Class[]{Industry.class};
    	try {
			Object[] response=_service.callServiceCRM("getIndustries", params,retTypes);
			Industry item = (Industry)response[0];
			if(item==null) return;
			if(item.getIndustries()==null) return;
			
			int count=item.getIndustries().length;
			kodesIndustry=new String[count];
			for(int i=0; i < count;i++)
			{
				Industry ind=item.getIndustries()[i];
				if(null!=ind) 
				{
					kodesIndustry[i]=ind.getKindustri() + "-" + ind.getNindustri();
				}
			}
			INDUSTRIES=Arrays.
			asList(kodesIndustry);
		} catch (AxisFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void getPajak()
	{
		Object[] params=new Object[]{null};
    	
    	Class[] retTypes =new Class[]{Pajak.class};
    	try {
			Object[] response=_serviceAcc.callServiceAccounting("getPajak", params,retTypes);
			Pajak item = (Pajak)response[0];
			if(item==null) return;
			if(item.getPajaks()==null) return;
			
			int count=item.getPajaks().length;
			kodesPajak=new String[count];
			for(int i=0; i < count;i++)
			{
				Pajak pajak=item.getPajaks()[i];
				if(null!=pajak) 
				{
					kodesPajak[i]=pajak.getKode() + "-" + pajak.getKeterangan();
				}
			}
			PAJAKS=Arrays.
			asList(kodesPajak);
		} catch (AxisFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void getGroupCustomer()
	{
		Object[] params=new Object[]{"EPP"};
    	
    	Class[] retTypes =new Class[]{Customer.class};
    	try {
			Object[] response=_service.callServiceCRM("getCustomers", params,retTypes);
			Customer item = (Customer)response[0];
			if(item==null) return;
			if(item.getCustomers()==null) return;
			
			int count=item.getCustomers().length;
			kodesClient1=new String[count];
			for(int i=0; i < count;i++)
			{
				Customer cust=item.getCustomers()[i];
				if(null!=cust) 
				{
					kodesClient1[i]=cust.getKclient();
				}
			}
			CLIENT1=Arrays.
			asList(kodesClient1);
		} catch (AxisFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
