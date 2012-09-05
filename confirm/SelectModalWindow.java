package org.apache.wicket.erp.confirm;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;

public abstract class SelectModalWindow extends ModalWindow {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SelectModalWindow(String id) {
        super(id);

        // Set sizes of this ModalWindow. You can also do this from the HomePage
        // but its not a bad idea to set some good default values.
        setInitialWidth(450);
        setInitialHeight(300);

        setTitle("Select something");

        // Set the content panel, implementing the abstract methods
        setContent(new SelectContentPanel(this.getContentId()){
        	/**
			 * 
			 */
			private static final long serialVersionUID = 1L;
			@Override
            public void onCancel(AjaxRequestTarget target) {
                SelectModalWindow.this.onCancel(target);
            }
        	@Override
        	public void onSelect(AjaxRequestTarget target, String selection) {
                SelectModalWindow.this.onSelect(target, selection);
            }
        });
    }

    public abstract void onCancel(AjaxRequestTarget target);

    public abstract void onSelect(AjaxRequestTarget target, String selection);

}
