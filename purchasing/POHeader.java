package org.apache.wicket.erp.purchasing;


public class POHeader extends sf.purchasing.POHeader{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int no;
	private PRHeader prHeader;
	private double nilaippn;
	private double total_nilai_po;
	private double pdisc;
	private double hargasatuan;
	private double totalharga;
	private double jumlah1;
	private double jumlah2;
	private String prno;
	private String tglpr;
	private double qtypr;
	private double qtypo;
	private String nobukti1;
	private String keterangan;
	private String ket;
	
	
	public POHeader()
	{
		setAdappn("T");
	}
	
	public void setNo(int no) {
		this.no = no;
	}

	public int getNo() {
		return no;
	}

	public void setPrHeader(PRHeader prHeader) {
		this.prHeader = prHeader;
		
		this.prno = prHeader.getNobukti();
		this.tglpr =prHeader.getTglbukti();
		this.keterangan = prHeader.getKeterangan();
		this.qtypr=prHeader.getJumlah();
		this.nobukti1=prHeader.getNobukti1();
		
	}

	public PRHeader getPrHeader() {
		return prHeader;
	}

	public void setNilaippn(double nilaippn) {
		this.nilaippn = nilaippn;
	}

	public double getNilaippn() {
		return nilaippn;
	}

	public void setTotal_nilai_po(double total_nilai_po) {
		this.total_nilai_po = total_nilai_po;
	}

	public double getTotal_nilai_po() {
		return total_nilai_po;
	}

	public void setPdisc(double pdisc) {
		this.pdisc = pdisc;
	}

	public double getPdisc() {
		return pdisc;
	}

	public void setHargasatuan(double hargasatuan) {
		this.hargasatuan = hargasatuan;
	}

	public double getHargasatuan() {
		return hargasatuan;
	}

	public void setTotalharga(double totalharga) {
		this.totalharga = totalharga;
	}

	public double getTotalharga() {
		return totalharga;
	}



	public void setJumlah1(double jumlah1) {
		this.jumlah1 = jumlah1;
	}

	public double getJumlah1() {
		return jumlah1;
	}

	public void setJumlah2(double jumlah2) {
		this.jumlah2 = jumlah2;
	}

	public double getJumlah2() {
		return jumlah2;
	}

	public void setPrno(String prno) {
		this.prno = prno;
	}

	public String getPrno() {
		return prno;
	}

	public void setTglpr(String tglpr) {
		this.tglpr = tglpr;
	}

	public String getTglpr() {
		return tglpr;
	}

	public void setQtypr(double qtypr) {
		this.qtypr = qtypr;
	}

	public double getQtypr() {
		return qtypr;
	}

	public void setQtypo(double qtypo) {
		this.qtypo = qtypo;
	}

	public double getQtypo() {
		return qtypo;
	}

	public void setNobukti1(String nobukti1) {
		this.nobukti1 = nobukti1;
	}

	public String getNobukti1() {
		return nobukti1;
	}

	public void setKeterangan(String keterangan) {
		this.keterangan = keterangan;
	}

	public String getKeterangan() {
		return keterangan;
	}

	public void setKet(String ket) {
		this.ket = ket;
	}

	public String getKet() {
		return ket;
	}
}
