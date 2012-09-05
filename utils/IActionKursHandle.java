package org.apache.wicket.erp.utils;

import org.apache.wicket.ajax.AjaxRequestTarget;


public interface IActionKursHandle extends IActionHandler{
	public void showKurs(AjaxRequestTarget target);
	public void showKursPajak(AjaxRequestTarget target);

}
