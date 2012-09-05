package org.apache.wicket.erp.purchasing;

public class PRDetail extends sf.purchasing.PRDetail{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int no;
	private double total=0;
	public void setNo(int no) {
		this.no = no;
	}

	public int getNo() {
		return no;
	}

	public void setTotal(double totalHarga) {
		this.total = totalHarga;
	}

	public double getTotal() {
		return total;
	}
}
