package org.apache.wicket.erp.utils;

public interface IActionHandlerIndex extends IActionHandler{
	public int insert(int idx);
	public int update(int idx);
	public int delete(int idx);
	public void newAction(int idx);
	public void editAction(int idx);
	public void deleteAction(int idx);
	public void readOnly(int idx);
	public void cancel(int idx,boolean flag);
	public void activate(int idx);
	public void print(int idx);
}
