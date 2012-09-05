package org.apache.wicket.erp.purchasing;

public class PODetail extends sf.purchasing.PODetail{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int no;
	private double totalHarga=0;
	public void setNo(int no) {
		this.no = no;
	}

	public int getNo() {
		return no;
	}

	public void setTotalHarga(double totalHarga) {
		this.totalHarga = totalHarga;
	}

	public double getTotalHarga() {
		return totalHarga;
	}
	
	
}
