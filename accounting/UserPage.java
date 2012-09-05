package org.apache.wicket.erp.accounting;

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

import sf.accounting.Branch;
import sf.accounting.User;

import java.text.SimpleDateFormat;

public class UserPage extends ERPWebPage implements IActionHandler{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static final List<String> KCOMPANY = Arrays
	.asList(new String[] { "EPP", "WKP"});
	
	private static final List<String> ACCESS = Arrays
	.asList(new String[] { "Harga Pokok", "Preview","Approval"});
	
	private static final List<String> POSISI = Arrays.
	asList(new String[] {"DR –Direksi","MR –Manager","SP –Supervisor","SL –Sales","ST –Staff","TH –Tehnisi","LL –Lain-lain","AD –Administrator" });
	
	private static List<String> CABANG=null;
	
	private RequiredTextField<String> kstaff;
	private RequiredTextField<String> nstaff;
	private RequiredTextField<String> kalias;
	private RequiredTextField<String> address1;
	private RequiredTextField<String> tgllahir;
	private RequiredTextField<String> tglmasuk;
	private RequiredTextField<String> city;
	private DropDownChoice<String> kjabatan;
	private DropDownChoice<String> kcabang;
	private RadioChoice<String> kcompany ;
	private CheckBox khpp;
	private CheckBox kprint;
	private CheckBox kApprove;
	private TextField<String> keterangan;
	private TextField<String> djabatan;
	private TextField<String> tglkeluar;
	private TextField<String> kotalahir;
	private TextField<String> address2;
	private TextField<String> address3;
	private TextField<String> postcode;
	private TextField<String> phone;
	private TextField<String> fax;
	
	boolean isReadOnly=true;
	private Form<User> form;
	final AttributeModifier ro = new AttributeModifier("readonly", isReadOnly);
	private User user = new User();
	private static UserPage app;
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
	
	public static UserPage get()
	{
		return app;
	}
	
	public UserPage(PageParameters p)
	{
		this(new User());
		
		kstaff.remove(ro);
		nstaff.remove(ro);
		kalias.remove(ro);
		address1.remove(ro);
		tgllahir.remove(ro);
		tglmasuk.remove(ro);
		city.remove(ro);
		kjabatan.remove(ro);
		kcabang.remove(ro);
		kcompany.remove(ro);
		khpp.remove(ro);
		kprint.remove(ro);
		kApprove.remove(ro);
		keterangan.remove(ro);
		djabatan.remove(ro);
		tglkeluar.remove(ro);
		kotalahir.remove(ro);
		address2.remove(ro);
		address3.remove(ro);
		postcode.remove(ro);
		phone.remove(ro);
		fax.remove(ro);
	}
	public UserPage()
	{
		this(new User());
	}
	public UserPage(User user)
	{
		super("Accounting | User");
		init();
		getCabang();
		this.user = user;
		add(new UserFilter("userfilter",this));
		form = new Form<User>("form", new CompoundPropertyModel<User>(this.user));
		add(form);
		
		// add a simple text field that uses Input's 'text' property. Nothing
		// can go wrong here
		 kstaff=new RequiredTextField<String>("kstaff");
		 nstaff=new RequiredTextField<String>("nstaff");
		 kalias=new RequiredTextField<String>("kAlias");
		 address1=new RequiredTextField<String>("alamat1");
		 tgllahir=new RequiredTextField<String>("tgllahir");
		 tglmasuk=new RequiredTextField<String>("tglmasuk");
		 city=new RequiredTextField<String>("kota");
		 kjabatan=new DropDownChoice<String>("kjabatan",POSISI);
		 kjabatan.setRequired(true);
		 kcabang=new DropDownChoice<String>("kcabang",CABANG);
		 kcompany=new RadioChoice<String>("kcompany",KCOMPANY);
		 kcompany.setSuffix(" ");
		 khpp=new CheckBox("khpp");
		 kprint=new CheckBox("kprint");
		 kApprove=new CheckBox("kApprove");
		 keterangan=new TextField<String>("keterangan");
		 djabatan=new TextField<String>("djabatan");
		 tglkeluar=new TextField<String>("tglkeluar");
		 kotalahir=new TextField<String>("kotalahir");
		 address2=new TextField<String>("alamat2");
		 address3=new TextField<String>("alamat3");
		 postcode=new TextField<String>("kodepos");
		 phone=new TextField<String>("notelp");
		 fax=new TextField<String>("nofax");
		this.readOnly();
		form.add(kstaff);
		form.add(nstaff);
		form.add(kalias);
		form.add(address1);
		form.add(address2);
		form.add(address3);
		form.add(city);
		form.add(postcode);
		form.add(phone);
		form.add(fax);
		form.add(tgllahir);
		form.add(tglmasuk);
		form.add(tglkeluar);
		form.add(kjabatan);
		form.add(djabatan);
		form.add(kcabang);
		form.add(kcompany);
		form.add(khpp);
		form.add(kprint);
		form.add(kApprove);
		form.add(keterangan);
		
		final FeedbackPanel feedback = new FeedbackPanel("feedback");
		add(feedback);
		form.add(new PanelAction("actionpanel",this));
		app=this;
		ERPApplication.pageTitle="Accounting | User";
	}
	public boolean updateUser()
	{
		form.process(null);
		User user = (User)form.getModelObject();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String dateNow = sdf.format(new Date());
		user.setTglupdate(Long.parseLong(dateNow));
		user.setUserupdate(UserInfo.USERID);
		
    	Object[] params=new Object[]{user};
    	Class[] retTypes =new Class[]{Boolean.class};
    	
    	Object[] response;
		try {
			response = _service.callServiceAccounting("updateUser", params, retTypes);
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
		User user= (User)form.getModelObject();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String dateNow = sdf.format(new Date());
		user.setTglupdate(Long.parseLong(dateNow));
		user.setUserupdate(UserInfo.USERID);
		
    	Object[] params=new Object[]{user};
    	Class[] retTypes =new Class[]{Boolean.class};
    	
    	Object[] response;
		try {
			response = _service.callServiceAccounting("deleteUser", params, retTypes);
			Boolean ret=(Boolean)response[0];
			if(ret)
			{
				setResponsePage(UserPage.class);
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
		User user = (User)form.getModelObject();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String dateNow = sdf.format(new Date());
		user.setTglupdate(Long.parseLong(dateNow));
		user.setUserupdate(UserInfo.USERID);
		
    	Object[] params=new Object[]{user};
    	Class[] retTypes =new Class[]{Boolean.class};
    	
    	Object[] response;
    	boolean duplicate=false;
		try {
			response = _service.callServiceAccounting("insertUser", params, retTypes);
			Boolean ret=(Boolean)response[0];
			if(ret)
			{
				setResponsePage(BranchPage.class);
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
		Boolean bRet=updateUser();
		if(bRet)
		{
			info("Update User has been successfully.");
			return 1;
		}
		else
			error("Failed to update User.");
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
			kstaff.remove(ro);
			nstaff.remove(ro);
			kalias.remove(ro);
			address1.remove(ro);
			tgllahir.remove(ro);
			tglmasuk.remove(ro);
			city.remove(ro);
			kjabatan.remove(ro);
			kcabang.remove(ro);
			kcompany.remove(ro);
			khpp.remove(ro);
			kprint.remove(ro);
			kApprove.remove(ro);
			keterangan.remove(ro);
			djabatan.remove(ro);
			tglkeluar.remove(ro);
			kotalahir.remove(ro);
			address2.remove(ro);
			address3.remove(ro);
			postcode.remove(ro);
			phone.remove(ro);
			fax.remove(ro);
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
			kstaff.remove(ro);
			nstaff.remove(ro);
			kalias.remove(ro);
			address1.remove(ro);
			tgllahir.remove(ro);
			tglmasuk.remove(ro);
			city.remove(ro);
			kjabatan.remove(ro);
			kcabang.remove(ro);
			kcompany.remove(ro);
			khpp.remove(ro);
			kprint.remove(ro);
			kApprove.remove(ro);
			keterangan.remove(ro);
			djabatan.remove(ro);
			tglkeluar.remove(ro);
			kotalahir.remove(ro);
			address2.remove(ro);
			address3.remove(ro);
			postcode.remove(ro);
			phone.remove(ro);
			fax.remove(ro);
		}
		catch(Exception ex){}
	}

	public void readOnly() {
		// TODO Auto-generated method stub
		try
		{
			kstaff.add(ro);
			nstaff.add(ro);
			kalias.add(ro);
			address1.add(ro);
			tgllahir.add(ro);
			tglmasuk.add(ro);
			city.add(ro);
			kjabatan.add(ro);
			kcabang.add(ro);
			kcompany.add(ro);
			khpp.remove(ro);
			kprint.remove(ro);
			kApprove.remove(ro);
			keterangan.add(ro);
			djabatan.add(ro);
			tglkeluar.add(ro);
			kotalahir.add(ro);
			address2.add(ro);
			address3.add(ro);
			postcode.add(ro);
			phone.add(ro);
			fax.add(ro);
		}
		catch(Exception ex){}
	}

	public void cancel() {
		// TODO Auto-generated method stub
		
	}

	public void activate() {
		// TODO Auto-generated method stub
		setResponsePage(UserPage.class);
	}

	public void print() {
		// TODO Auto-generated method stub
		
	}

	public void cancel(boolean flag) {
		// TODO Auto-generated method stub
		if(flag)
			setResponsePage(new UserPage(UserFilter.selectedItem));
		else
			setResponsePage(UserPage.class);
	}
	
	private String[] kodes=null ;
	private void getCabang()
    {
    	Object[] params=new Object[]{null};
    	
    	Class[] retTypes =new Class[]{Branch.class};
    	try {
    		
    		Object[] response=null;
    		if("00".equals(UserInfo.CODE_BRANCH))
    			response=_service.callServiceAccounting("getBranch", params,retTypes);
    		else
    		{
    			params=new Object[]{UserInfo.CODE_BRANCH};
    			response=_service.callServiceAccounting("getBranchByCode", params,retTypes);
    		}
			Branch item = (Branch)response[0];
			if(item==null) return;
			int count=item.getBranches().length;
			kodes=new String[count];
			for(int i=0; i < count;i++)
			{
				Branch branch=item.getBranches()[i];
				if(null!=branch) 
				{
					kodes[i]=branch.getKcabang()+ "-" + branch.getKota();
				}
			}
			CABANG=Arrays.
			asList(kodes);
		} catch (AxisFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
