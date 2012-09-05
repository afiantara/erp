package org.apache.wicket.erp.file;

public class UserAccess extends sf.file.UserAccess{
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
