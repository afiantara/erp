package org.apache.wicket.erp.purchasing;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PRHeader extends sf.purchasing.PRHeader{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int no;
	private String tglbukti2; //dd/MM/yyyy
	private String kbarang;
	private String satuan;
	private double jumlah;
	private String nobukti1;
	private String ket;
	public void setNo(int no) {
		this.no = no;
	}
	public int getNo() {
		return no;
	}

	public void setTglbukti2(String tglbukti2) {
		SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMdd");
		SimpleDateFormat sdf1=new SimpleDateFormat("dd/MM/yyyy");
		Date dt=new Date();
		try {
			dt = sdf.parse(tglbukti2);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.tglbukti2 = sdf1.format(dt);
	}
	public String getTglbukti2() {
		return tglbukti2;
	}
	public void setKbarang(String kbarang) {
		this.kbarang = kbarang;
	}
	public String getKbarang() {
		return kbarang;
	}
	public void setSatuan(String satuan) {
		this.satuan = satuan;
	}
	public String getSatuan() {
		return satuan;
	}
	
	public void setNobukti1(String nobukti1) {
		this.nobukti1 = nobukti1;
	}
	public String getNobukti1() {
		return nobukti1;
	}
	public void setKet(String ket) {
		this.ket = ket;
	}
	public String getKet() {
		return ket;
	}
	public void setJumlah(double jumlah) {
		this.jumlah = jumlah;
	}
	public double getJumlah() {
		return jumlah;
	}
	
}
