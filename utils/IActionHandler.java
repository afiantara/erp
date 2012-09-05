package org.apache.wicket.erp.utils;

public interface IActionHandler {
	public int insert();
	public int update();
	public int delete();
	public void newAction();
	public void editAction();
	public void deleteAction();
	public void readOnly();
	public void cancel(boolean flag);
	public void activate();
	public void print();
}
