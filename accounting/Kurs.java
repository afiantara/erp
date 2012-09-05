package org.apache.wicket.erp.accounting;

import org.apache.wicket.IClusterable;

public class Kurs extends sf.accounting.Kurs implements IClusterable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int no;

	public void setNo(int no) {
		this.no = no;
	}

	public int getNo() {
		return no;
	}
}
